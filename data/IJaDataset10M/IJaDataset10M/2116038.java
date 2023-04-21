package macaw.persistenceLayer.demo;

import java.util.ArrayList;
import java.util.HashMap;
import macaw.MacawMessages;
import macaw.businessLayer.Basket;
import macaw.businessLayer.User;
import macaw.io.MacawImportExportUtility;
import macaw.io.VariableFileFormat;
import macaw.persistenceLayer.ChangeEventGenerator;
import macaw.system.ChangeEventType;
import macaw.system.MacawChangeEvent;
import macaw.system.MacawErrorType;
import macaw.system.MacawException;

public class InMemoryBasketManager extends InMemoryCurationConceptManager {

    private HashMap<Integer, Basket> basketFromIdentifier;

    private int basketKey;

    private ArrayList<Basket> baskets;

    public InMemoryBasketManager(InMemoryChangeEventManager changeEventManager) {
        super(changeEventManager);
        basketFromIdentifier = new HashMap<Integer, Basket>();
        baskets = new ArrayList<Basket>();
    }

    public void updateBasketOwner(String originalOwnerID, String revisedOwnerID) throws MacawException {
        ArrayList<MacawChangeEvent> changeEvents = new ArrayList<MacawChangeEvent>();
        for (Basket basket : baskets) {
            String currentOwner = basket.getName();
            if (currentOwner.equalsIgnoreCase(originalOwnerID) == true) {
                MacawChangeEvent changeEvent = new MacawChangeEvent();
                changeEvent.setChangedObjectIdentifier(basket.getIdentifier());
                changeEvent.setChangeType(ChangeEventType.BASKET);
                String changeMessage = MacawMessages.getMessage("basket.saveChanges.ownerChanged", basket.getName(), originalOwnerID, revisedOwnerID);
                changeEvent.setChangeMessage(changeMessage);
                changeEvents.add(changeEvent);
                basket.setName(revisedOwnerID);
            }
        }
        registerChangeEvents(changeEvents);
    }

    public void addBasket(User user, Basket basket) throws MacawException {
        Basket.validateFields(basket);
        checkBasketDuplicates(basket);
        basketKey++;
        basket.setIdentifier(basketKey);
        basket.setNewRecord(false);
        baskets.add(basket);
        basketFromIdentifier.put(basket.getIdentifier(), basket);
        ArrayList<MacawChangeEvent> changeEvents = ChangeEventGenerator.addBasketChange(user, basket);
        registerChangeEvents(changeEvents);
    }

    public void deleteBasket(User user, Basket basket) throws MacawException {
        deleteBasket(user, basket, true);
    }

    public void deleteBasket(User user, Basket basket, boolean checkForMyVariableBasket) throws MacawException {
        checkBasketExists(basket);
        Basket originalBasket = getOriginalBasket(basket);
        if ((checkForMyVariableBasket == true) && (isMyVariablesBasket(user, basket) == true)) {
            String errorMessage = MacawMessages.getMessage("basket.error.cannotDeleteMyVariables", user.getUserID());
            MacawException exception = new MacawException(MacawErrorType.ILLEGAL_DELETION_MY_VARIABLES_BASKET, errorMessage);
            throw exception;
        }
        basketFromIdentifier.remove(basket.getIdentifier());
        baskets.remove(originalBasket);
        ArrayList<MacawChangeEvent> changeEvents = ChangeEventGenerator.deleteBasketChange(user, basket);
        registerChangeEvents(changeEvents);
    }

    public void updateBasket(User user, Basket revisedBasket) throws MacawException {
        Basket.validateFields(revisedBasket);
        checkBasketExists(revisedBasket);
        Basket originalBasket = getOriginalBasket(revisedBasket);
        if (isMyVariablesBasket(user, revisedBasket) == true) {
            if (Basket.nameOrLabelModified(originalBasket, revisedBasket) == true) {
                String errorMessage = MacawMessages.getMessage("basket.error.cannotUpdateMyVariablesMetaData", user.getUserID());
                MacawException exception = new MacawException(MacawErrorType.ILLEGAL_UPDATE_MY_VARIABLES_BASKET, errorMessage);
                throw exception;
            }
        }
        ArrayList<MacawChangeEvent> changeEvents = Basket.detectFieldChanges(user, originalBasket, revisedBasket);
        if (changeEvents.size() == 0) {
            return;
        }
        int identifier = revisedBasket.getIdentifier();
        int foundIndex = baskets.indexOf(originalBasket);
        int numberOfBaskets = baskets.size();
        baskets.remove(originalBasket);
        if (foundIndex == numberOfBaskets - 1) {
            baskets.add(revisedBasket);
        } else {
            baskets.add(foundIndex, revisedBasket);
        }
        basketFromIdentifier.remove(identifier);
        basketFromIdentifier.put(identifier, revisedBasket);
        if (isMyVariablesBasket(user, revisedBasket) == false) {
            registerChangeEvents(changeEvents);
        }
    }

    public Basket getBasket(User user, String basketName) throws MacawException {
        String owner = user.getUserID();
        for (Basket currentBasket : baskets) {
            String currentBasketName = currentBasket.getName();
            if ((currentBasketName.equalsIgnoreCase(basketName) == true) && (owner.equalsIgnoreCase(currentBasket.getOwner()) == true)) {
                return (Basket) currentBasket.clone();
            }
        }
        return null;
    }

    public ArrayList<Basket> getBaskets(User user) {
        String userID = user.getUserID();
        String myVariablesBasketName = MacawMessages.getMessage("basket.myVariables.title");
        ArrayList<Basket> cloneBaskets = new ArrayList<Basket>();
        for (Basket basket : baskets) {
            if (basket.getOwner().equalsIgnoreCase(userID) == true) {
                if (basket.getName().equalsIgnoreCase(myVariablesBasketName) == false) {
                    cloneBaskets.add((Basket) basket.clone());
                }
            }
        }
        return cloneBaskets;
    }

    public Basket getOriginalBasket(Basket basket) {
        int identifier = basket.getIdentifier();
        Basket originalBasket = basketFromIdentifier.get(identifier);
        return originalBasket;
    }

    public String getBasketXML(User user, Basket basket, VariableFileFormat format) throws MacawException {
        MacawImportExportUtility exportUtility = new MacawImportExportUtility();
        return exportUtility.getBasketXML(basket, format);
    }

    public void createMyVariablesBasket(User user) throws MacawException {
        Basket myVariablesBasket = Basket.createMyVariablesBasket(user);
        addBasket(user, myVariablesBasket);
    }

    public Basket getMyVariablesBasket(User user) throws MacawException {
        String myVariablesBasketName = MacawMessages.getMessage("basket.myVariables.title");
        Basket myVariablesBasket = getBasket(user, myVariablesBasketName);
        assert (myVariablesBasket != null);
        return myVariablesBasket;
    }

    public void deleteMyVariablesBasket(User user) throws MacawException {
        Basket myVariablesBasket = getMyVariablesBasket(user);
        deleteBasket(user, myVariablesBasket, false);
    }

    public void clear() {
        basketKey = 0;
        baskets.clear();
        basketFromIdentifier.clear();
    }

    private boolean isMyVariablesBasket(User user, Basket basket) throws MacawException {
        Basket myVariablesBasket = getMyVariablesBasket(user);
        if (myVariablesBasket.getIdentifier() == basket.getIdentifier()) {
            return true;
        }
        return false;
    }

    public void checkBasketExists(Basket basket) throws MacawException {
        Basket originalBasket = getOriginalBasket(basket);
        if (originalBasket == null) {
            String errorMessage = MacawMessages.getMessage("general.error.nonExistentItem", basket.getDisplayName());
            MacawException exception = new MacawException(MacawErrorType.NON_EXISTENT_VARIABLE, errorMessage);
            throw exception;
        }
    }

    private void checkBasketDuplicates(Basket targetBasket) throws MacawException {
        String targetBasketName = targetBasket.getName();
        String targetOwner = targetBasket.getOwner();
        for (Basket currentBasket : baskets) {
            String currentBasketName = currentBasket.getName();
            String currentOwner = currentBasket.getOwner();
            if ((currentBasketName.equalsIgnoreCase(targetBasketName) == true) && (currentOwner.equalsIgnoreCase(targetOwner) == true)) {
                String errorMessage = MacawMessages.getMessage("basket.error.duplicateExists", targetBasketName);
                MacawException exception = new MacawException(MacawErrorType.DUPLICATE_BASKET, errorMessage);
                throw exception;
            }
        }
    }
}
