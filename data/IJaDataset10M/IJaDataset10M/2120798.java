package info.walnutstreet.vs.junit.ps03;

import info.walnutstreet.vs.ps03.dbserver.GoodModel;
import info.walnutstreet.vs.ps03.exceptions.InvalidAvaiableNumberException;
import info.walnutstreet.vs.ps03.exceptions.InvalidPriceException;
import info.walnutstreet.vs.ps03.exceptions.InvalidUniqueIdException;
import java.rmi.RemoteException;
import junit.framework.TestCase;

/**
 * @author Christoph Gostner
 * @version 0.1
 *
 */
public class TestGoodModel extends TestCase {

    public void testFirstGood() throws RemoteException {
        try {
            new GoodModel(-1, null);
            assertTrue(false);
        } catch (InvalidUniqueIdException e) {
            assertTrue(true);
        }
        try {
            new GoodModel(0, null);
            assertTrue(false);
        } catch (InvalidUniqueIdException e) {
            assertTrue(true);
        }
        try {
            new GoodModel();
            assertTrue(true);
        } catch (InvalidUniqueIdException e) {
            assertTrue(false);
        }
    }

    public void testSecondGood() throws RemoteException {
        try {
            GoodModel goodModel = new GoodModel();
            goodModel.setId(1);
            goodModel.setName("Article #1");
            goodModel.setAvailable(10);
            goodModel.setDescription("Bla");
            goodModel.setPrice(10.0);
            assertTrue(true);
        } catch (InvalidUniqueIdException e) {
            assertTrue(false);
        } catch (InvalidAvaiableNumberException e) {
            assertTrue(false);
        } catch (InvalidPriceException e) {
            assertTrue(false);
        }
        try {
            GoodModel goodModel = new GoodModel(2, "Article #2");
            goodModel.setAvailable(10);
            goodModel.setDescription("Bla");
            goodModel.setPrice(10.0);
            assertTrue(true);
        } catch (InvalidUniqueIdException e) {
            assertTrue(false);
        } catch (InvalidAvaiableNumberException e) {
            assertTrue(false);
        } catch (InvalidPriceException e) {
            assertTrue(false);
        }
    }

    public void testGoodInvalidId() throws RemoteException {
        try {
            new GoodModel(0, "Invalid good");
            assertTrue(false);
        } catch (InvalidUniqueIdException e) {
            assertTrue(true);
        }
        try {
            GoodModel goodModel = new GoodModel();
            goodModel.setId(-3324);
            assertTrue(false);
        } catch (InvalidUniqueIdException e) {
            assertTrue(true);
        }
    }

    public void testGoodInvalidAvaiableNumber() throws RemoteException {
        try {
            GoodModel goodModel = new GoodModel();
            goodModel.setAvailable(10);
            assertTrue(true);
        } catch (InvalidUniqueIdException e) {
            assertTrue(false);
        } catch (InvalidAvaiableNumberException e) {
            assertTrue(false);
        }
        try {
            GoodModel goodModel = new GoodModel();
            goodModel.setAvailable(0);
            assertTrue(true);
        } catch (InvalidUniqueIdException e) {
            assertTrue(false);
        } catch (InvalidAvaiableNumberException e) {
            assertTrue(false);
        }
        try {
            GoodModel goodModel = new GoodModel();
            goodModel.setAvailable(-10);
            assertTrue(false);
        } catch (InvalidUniqueIdException e) {
            assertTrue(false);
        } catch (InvalidAvaiableNumberException e) {
            assertTrue(true);
        }
    }

    public void testGoodWithInvalidPrice() throws InvalidUniqueIdException, RemoteException {
        try {
            GoodModel goodModel = new GoodModel();
            goodModel.setPrice(10.0);
            assertTrue(true);
        } catch (InvalidPriceException e) {
            assertTrue(false);
        }
        try {
            GoodModel goodModel = new GoodModel();
            goodModel.setPrice(0.0);
            assertTrue(true);
        } catch (InvalidPriceException e) {
            assertTrue(false);
        }
        try {
            GoodModel goodModel = new GoodModel();
            goodModel.setPrice(-10.0);
            assertTrue(false);
        } catch (InvalidPriceException e) {
            assertTrue(true);
        }
    }
}
