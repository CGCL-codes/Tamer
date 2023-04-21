package com.submersion.jspshop.rae;

import javax.rmi.*;
import java.rmi.RemoteException;
import javax.ejb.*;
import javax.naming.*;
import java.util.*;
import com.submersion.jspshop.ejb.*;

/** Collection (Hashmap) of Property types.
 * 
 * @author Jeff Davey (jeffdavey@submersion.com)
 * @see com.submersion.jspshop.rae.Property
 * @see com.submersion.jspshop.rae.Object
 * @version $Revision: 1.2 $
 * @created: August 23, 2001  
 * @changed: $Date: 2001/10/13 19:39:14 $
 * @changedBy: $Author: jeffdavey $
*/
public class Properties extends RAECollection {

    private Long objectID;

    private PropertyHome propertyHome = null;

    private ValueHome valueHome = null;

    private TypeValueHome typeValueHome;

    private int right;

    public Properties(Long objectID, Long userID) {
        rObject object = new rObject(objectID, userID);
        this.right = object.getRight();
        this.objectID = objectID;
        initialise(objectID);
    }

    public Properties(Long objectID, Long userID, Long passedID, int queryType) {
        rObject object = new rObject(objectID, userID);
        this.right = object.getRight();
        this.objectID = objectID;
        switch(queryType) {
            case TYPE:
                initialiseType(objectID, passedID);
                break;
            default:
                initialiseProperty(objectID, passedID);
        }
    }

    public Properties(Long objectID, int right) {
        this.right = right;
        this.objectID = objectID;
        initialise(objectID);
    }

    public Properties(Long objectID, Long passedID, int queryType, int right) {
        this.objectID = objectID;
        this.right = right;
        switch(queryType) {
            case TYPE:
                initialiseType(objectID, passedID);
                break;
            default:
                initialiseProperty(objectID, passedID);
        }
    }

    public Properties(Long objectID, String passedString, int queryType, int right) {
        this.objectID = objectID;
        this.right = right;
        switch(queryType) {
            case PROPERTY:
                initialiseProperty(objectID, passedString);
                break;
            case TYPE:
                initialiseType(objectID, passedString);
                break;
            default:
                initialiseValue(objectID, passedString);
        }
    }

    public Property getItem(Long valueID) {
        return (Property) items.get(valueID);
    }

    public Property create(Long propertyID, String value) throws CreateException {
        if ((right & CREATE) != CREATE) {
            throw new SecurityException("You do not have proper authority for creating.");
        }
        return createProperty(propertyID, value);
    }

    public void delete(Long valueID) throws RemoveException {
        if ((right & DELETE) != DELETE) {
            throw new SecurityException("You do not have proper authority for deleting.");
        }
        deleteProperty(valueID);
    }

    private void deleteProperty(Long valueID) throws RemoveException {
        try {
            deleteTypes(valueID);
            valueHome.remove(valueID);
        } catch (RemoteException e) {
            System.err.println("jspShop: Error connecting to container in deleteProperty method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteTypes(Long valueID) throws RemoveException {
        try {
            Iterator iterator = typeValueHome.findByValueID(valueID).iterator();
            while (iterator.hasNext()) {
                typeValueHome.remove(((TypeValue) iterator.next()).getTypeValueID());
            }
        } catch (RemoteException e) {
            System.err.println("jspShop: Error connecting to container in deleteTypes method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        } catch (FinderException e) {
            System.err.println("jspShop: Error using finder query in deleteTypes method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Property createProperty(Long propertyID, String value) throws CreateException {
        try {
            Value newValue = valueHome.create();
            newValue.setPropertyID(propertyID);
            newValue.setObjectID(objectID);
            newValue.setValue(value);
            return new Property(newValue.getValueID(), this.right);
        } catch (RemoteException e) {
            System.err.println("jspShop: Error connecting to container in createProperty method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        } catch (FinderException e) {
            System.err.println("jspShop: Error using finder query in createProperty method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        } catch (NamingException e) {
            System.err.println("jspShop: Error looking up ValueHome in createProperty method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void preInitialise() {
        try {
            InitialContext context = new InitialContext();
            java.lang.Object homeRef = context.lookup("java:comp/env/ejb/Value");
            valueHome = (ValueHome) PortableRemoteObject.narrow(homeRef, ValueHome.class);
            homeRef = context.lookup("java:comp/env/ejb/TypeValue");
            typeValueHome = (TypeValueHome) PortableRemoteObject.narrow(homeRef, TypeValueHome.class);
        } catch (NamingException e) {
            System.err.println("jspShop: Error looking up ValueHome in preInitialise method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initialiseValue(Long objectID, String value) {
        try {
            preInitialise();
            Iterator iterator = valueHome.findByObjectIDValue(objectID, value).iterator();
            makeHash(iterator);
        } catch (FinderException e) {
            System.err.println("jspShop: Error using finder query in initialiseValue method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        } catch (RemoteException e) {
            System.err.println("jspShop: Error connecting to container in initialiseValue method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initialiseProperty(Long objectID, String property) {
        try {
            preInitialise();
            Iterator iterator = valueHome.findByObjectIDProperty(objectID, property).iterator();
            makeHash(iterator);
        } catch (FinderException e) {
            System.err.println("jspShop: Error using finder query in initialiseProperty method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        } catch (RemoteException e) {
            System.err.println("jspShop: Error connecting to container in initialiseProperty method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initialiseType(Long objectID, String type) {
        try {
            preInitialise();
            Iterator iterator = valueHome.findByObjectIDType(objectID, type).iterator();
            makeHash(iterator);
        } catch (FinderException e) {
            System.err.println("jspShop: Error using finder query in initialiseType method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        } catch (RemoteException e) {
            System.err.println("jspShop: Error connecting to container in initialiseType method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initialise(Long objectID) {
        try {
            preInitialise();
            Iterator iterator = valueHome.findByObjectID(objectID).iterator();
            makeHash(iterator);
        } catch (FinderException e) {
            System.err.println("jspShop: Error using finder query in initialise method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        } catch (RemoteException e) {
            System.err.println("jspShop: Error connecting to container in initialise method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initialiseProperty(Long objectID, Long propertyID) {
        try {
            preInitialise();
            Iterator iterator = valueHome.findByObjectIDProperty(objectID, propertyID).iterator();
            makeHash(iterator);
        } catch (FinderException e) {
            System.err.println("jspShop: Error using finder query in initialiseProperty method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        } catch (RemoteException e) {
            System.err.println("jspShop: Error connecting to container in initialise method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initialiseType(Long objectID, Long typeID) {
        try {
            preInitialise();
            Iterator iterator = valueHome.findByObjectIDType(objectID, typeID).iterator();
            makeHash(iterator);
        } catch (FinderException e) {
            System.err.println("jspShop: Error using finder query in initialiseType method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        } catch (RemoteException e) {
            System.err.println("jspShop: Error connecting to container in initialise method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void makeHash(Iterator iterator) {
        try {
            while (iterator.hasNext()) {
                Long valueID = ((Value) iterator.next()).getValueID();
                Property property = new Property(valueID, right);
                items.put(valueID, property);
            }
        } catch (RemoteException e) {
            System.err.println("jspShop: Error connecting to container in makeHash method in com.submersion.jspshop.rae.Properties. " + e.getMessage());
            e.printStackTrace();
        }
    }
}
