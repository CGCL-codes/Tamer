package com.kenstevens.jei.move;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.kenstevens.jei.main.ApplicationContextProvider;
import com.kenstevens.jei.model.Item;
import com.kenstevens.jei.model.Sector;

@Component
@Scope("prototype")
public class IslandMover extends ApplicationContextProvider {

    Log log = LogFactory.getLog(IslandMover.class);

    @Autowired
    MobilityCalculator mobilityCalculator;

    private final IslandEconomy islandEconomy;

    private final SectorAmountList<Consumer> consumers;

    private final SectorAmountList<Supplier> suppliers;

    private final Item item;

    private FeederCandidateFinder feederCandidateFinder;

    public IslandMover() {
        islandEconomy = null;
        consumers = null;
        suppliers = null;
        item = null;
    }

    public IslandMover(IslandEconomy islandEconomy) {
        this.islandEconomy = islandEconomy;
        consumers = islandEconomy.getConsumers();
        suppliers = islandEconomy.getSuppliers();
        item = islandEconomy.getItem();
    }

    public MoveCommandList moveIt() {
        feederCandidateFinder = getBean(FeederCandidateFinder.class, new Object[] { islandEconomy });
        feederCandidateFinder.setMobilityCalculator(mobilityCalculator);
        List<Consumer> currentConsumers = consumers.getSectorAmounts();
        List<Supplier> currentSuppliers = suppliers.getSectorAmounts();
        MoveCommandList moveCommands = new MoveCommandList();
        feedConsumers(currentConsumers, currentSuppliers, moveCommands);
        return moveCommands;
    }

    private void feedConsumers(List<Consumer> currentConsumers, List<Supplier> currentSuppliers, MoveCommandList moveCommands) {
        log.debug("Current consumers: " + currentConsumers);
        log.debug("Current suppliers: " + currentSuppliers);
        feederCandidateFinder.tryToPushFromSuppliers(currentConsumers, currentSuppliers, moveCommands);
        SortedSet<FeederCandidate> feederCandidates = feederCandidateFinder.findFeederCandidates(currentConsumers);
        List<Consumer> nextConsumers = new ArrayList<Consumer>();
        for (FeederCandidate feederCandidate : feederCandidates) {
            if (getAvailableMobility(feederCandidate) <= 0) {
                continue;
            }
            Delivery delivery = feederCandidate.getDelivery();
            if (delivery.isDone()) {
                continue;
            }
            if (delivery.getConsumer().isDone()) {
                continue;
            }
            if (delivery.getSupplier().isDone()) {
                continue;
            }
            MoveResult moveOneResult = mobilityCalculator.moveItem(feederCandidate.getSector(), delivery.getConsumer().getSector(), item, 1);
            assert moveOneResult.succeeded();
            int amountDesired = delivery.getUncommitted();
            assert amountDesired > 0;
            assert getAvailableMobility(feederCandidate) > 0;
            int amountCanMove = (int) (getAvailableMobility(feederCandidate) / moveOneResult.getMobilityCost());
            if (amountCanMove <= 0) {
                continue;
            }
            int amountToMove = Math.min(amountDesired, amountCanMove);
            assignFeederCandidate(feederCandidate, delivery, amountToMove, nextConsumers);
        }
        SectorAmountList.clean(currentConsumers);
        SectorAmountList.clean(currentSuppliers);
        if (!nextConsumers.isEmpty()) {
            feedConsumers(nextConsumers, currentSuppliers, moveCommands);
        }
    }

    private int getAvailableMobility(FeederCandidate feederCandidate) {
        Sector sector = feederCandidate.getWeightedConsumer().getSectorAmount().getSector();
        return sector.getMobility() - feederCandidateFinder.minimumMobility(sector);
    }

    private void assignFeederCandidate(FeederCandidate feederCandidate, Delivery delivery, int amountToMove, List<Consumer> nextConsumers) {
        assert amountToMove > 0;
        Consumer consumer = delivery.getConsumer();
        MoveResult moveResult = mobilityCalculator.moveItem(feederCandidate.getSector(), consumer.getSector(), item, amountToMove);
        assert moveResult.succeeded();
        Delivery nextConsumerDelivery = pullBackConsumerToFeeder(consumer, feederCandidate, amountToMove);
        Consumer nextConsumer = nextConsumerDelivery.getConsumer();
        SectorAmount sectorAmount = feederCandidate.getWeightedConsumer().getSectorAmount();
        Supplier feeder = new Supplier(sectorAmount);
        feeder.setAmount(amountToMove);
        Delivery tentativeMoveCommand = new TentativeDelivery(feeder, consumer, amountToMove, moveResult.getMobilityCost());
        if (delivery.getDownstream() != null) {
            tentativeMoveCommand.setDownstream(delivery.getDownstream());
        }
        delivery.addCommitted(amountToMove);
        if (delivery.isDone()) {
            delivery.delete();
        }
        nextConsumerDelivery.setDownstream(tentativeMoveCommand);
        nextConsumers.add(nextConsumer);
    }

    private Delivery pullBackConsumerToFeeder(Consumer consumer, FeederCandidate feederCandidate, int amountToMove) {
        Delivery delivery = feederCandidate.getDelivery();
        Consumer nextConsumer = new Consumer(feederCandidate.getSector(), amountToMove);
        Delivery nextConsumerDelivery = new Delivery(delivery.getSupplier(), nextConsumer, amountToMove);
        log.debug("Pulling back " + consumer + " to " + nextConsumer + " for supplier " + delivery.getSupplier());
        return nextConsumerDelivery;
    }

    public void setMobilityCalculator(MobilityCalculator mobilityCalculator) {
        this.mobilityCalculator = mobilityCalculator;
    }
}
