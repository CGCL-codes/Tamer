package ar.com.omnipresence.game.server;

import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Stateless;
import ar.com.omnipresence.common.server.SessionBean;
import ar.com.omnipresence.security.server.User;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.persistence.Query;

@Stateless(name = "PrivateRepublicServicesBean")
@Local(PrivateRepublicServices.class)
public class PrivateRepublicServicesBean extends SessionBean implements PrivateRepublicServices {

    private static final Logger logger = Logger.getLogger(PrivateRepublicServicesBean.class.getName());

    @Override
    public Republic findRepublicById(Long republicId) {
        return em.find(Republic.class, republicId);
    }

    public Set<Republic> findAllRepublicsForUserName(String userName) {
        User user = em.find(User.class, userName);
        return new HashSet<Republic>(em.createNamedQuery(Republic.FIND_ALL_FOR_USER).setParameter("user", user).getResultList());
    }

    public Set<Universe> findAllAvaiableUniversesForUser(User user) {
        Query q = em.createNamedQuery(Universe.UNIVERSE_FIND_AVAILABLE_FOR_USER);
        q.setParameter("user", user);
        List<Universe> result = q.getResultList();
        return new HashSet<Universe>(result);
    }

    public Republic createRepublic(String name, User owner, Universe universe) {
        logger.fine("Creating republic " + name + " for user " + owner.getUserName() + " in universe " + universe.getName());
        Republic republic = new Republic(name, owner);
        universe.initializeRepublic(republic);
        em.persist(republic);
        return republic;
    }

    public Set<ItemBlueprint> getAllItemsForRepublic(Republic republic) {
        Set<ItemBlueprint> blueprints = new HashSet<ItemBlueprint>();
        for (Planet planet : republic.getPlanets()) {
            for (Facility facility : planet.getFacilities()) {
                if (facility instanceof ItemFactory) {
                    blueprints.addAll(((ItemFactory) facility).getBlueprints());
                }
            }
        }
        for (Technology technology : republic.getAvailableTechnologies()) {
            blueprints.addAll(technology.getEnabledItems());
        }
        return blueprints;
    }

    public void produceItems(Republic republic, Planet planet, ProductionOrder order) throws ProductionException {
        if (republic.getPlanets().contains(planet)) {
            ProductionOrder planetOrder = null;
            Map<ItemFactory, ProductionOrder> factoryOrders = new HashMap<ItemFactory, ProductionOrder>();
            SimplePrice totalPrice = new SimplePrice();
            for (ProductionOrderItem item : order.getItems()) {
                SimplePrice p = item.getItem().getPrice().convertToSimplePrice();
                SimplePrice required = p.multiply(item.getQuantity());
                totalPrice = totalPrice.add(required);
            }
            republic.consumeResources(totalPrice);
            for (ProductionOrderItem item : order.getItems()) {
                if (item.getItem().getRequiresFactory()) {
                    ItemFactory factory = planet.getFactoryForItem(item.getItem());
                    if (factory == null) {
                        throw new ProductionException("There's no factory in this planet for the item " + item.getItem().getName());
                    }
                    ProductionOrder factoryOrder = factoryOrders.get(factory);
                    if (factoryOrder == null) {
                        factoryOrder = new ProductionOrder();
                    }
                    factoryOrder.addItem(new ProductionOrderItem(item.getItem(), item.getQuantity()));
                } else {
                    if (planetOrder == null) {
                        planetOrder = new ProductionOrder();
                        planetOrder.getLapse().setStartTick(republic.getUniverse().getTicker().getCurrentTick());
                    }
                    planetOrder.addItem(new ProductionOrderItem(item.getItem(), item.getQuantity()));
                }
            }
            for (ItemFactory factory : factoryOrders.keySet()) {
                factory.startProduction(factoryOrders.get(factory));
            }
            planet.produce(planetOrder);
        }
    }

    public Set<ProductionOrder> findAllProductionOrders(Republic republic) {
        Query q = em.createQuery("select p.productionQueue.orders from Planet p where p.owner = :republic");
        q.setParameter("republic", republic);
        return new HashSet<ProductionOrder>(q.getResultList());
    }

    /**
     * Reduces the resources of a republic.
     * @param republic The republic.
     * @param price The price with the resources to reduce.
     * @param quantity The quantity of items to consider.
     * @throws InsufficientResourcesException If the republic doesn't have enough resources.
     */
    private void consumeResources(Republic republic, ItemPrice price, int quantity) throws InsufficientResourcesException {
        Map<Resource, ResourceStock> stocks = republic.getResourceStocks();
        Map<Resource, Integer> substractions = new HashMap<Resource, Integer>();
        for (Resource r : price.getItems().keySet()) {
            int requiredAmmount = price.getItems().get(r).getQuantity() * quantity;
            if (republic.getStock(r).getQuantity() >= requiredAmmount) {
                substractions.put(r, requiredAmmount);
            } else {
                throw new InsufficientResourcesException("The republic does not have " + requiredAmmount + " of " + r.getName());
            }
        }
        for (Resource r : substractions.keySet()) {
            republic.getStock(r).reduce(substractions.get(r));
        }
    }
}
