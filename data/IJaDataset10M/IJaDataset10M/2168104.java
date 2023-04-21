package br.unb.unbiquitous.ubiquitos.uos.driver.deviceDriver;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import br.unb.unbiquitous.ubiquitos.authentication.AuthenticationDao;
import br.unb.unbiquitous.ubiquitos.authentication.HashGenerator;
import br.unb.unbiquitous.ubiquitos.authentication.SessionKeyDaoHSQLDB;
import br.unb.unbiquitous.ubiquitos.authentication.exception.IdNotInformedException;
import br.unb.unbiquitous.ubiquitos.authentication.exception.SessionKeyNotInformedException;
import br.unb.unbiquitous.ubiquitos.uos.security.basic.AuthenticationDaoHSQLDB;
import br.unb.unbiquitous.ubiquitos.uos.security.basic.BasicSecurityHandler;

public class TestEncodeDecode extends TestCase {

    protected static long currentTest = 0;

    private static Logger logger = Logger.getLogger(TestEncodeDecode.class);

    public void testEncodeDecodeWithAuthenticatedDevice() throws Exception {
        System.out.println("\n");
        logger.info("============== Teste " + currentTest++ + ": testEncodeDecodeWithAuthenticatedDevice ==========================");
        System.out.println("\n");
        SessionKeyDaoHSQLDB sessionKeyDao = new SessionKeyDaoHSQLDB();
        try {
            sessionKeyDao.insert("newDevice", "1234567890abcdef1234567890abcdef");
            AuthenticationDao authenticationDao = new AuthenticationDaoHSQLDB();
            if (authenticationDao.findByHashId(HashGenerator.generateHash("newDevice")) == null) {
                authenticationDao.insert("newDevice", HashGenerator.generateHash("newDevice"), "1234567890abcdef1234567890abcdef");
            }
            BasicSecurityHandler basicSecurityHandler = new BasicSecurityHandler();
            String message = "new message to be encoded";
            String encodedMessage = basicSecurityHandler.encode(message, "newDevice");
            logger.debug("encoded: " + encodedMessage);
            String decodedMessage = basicSecurityHandler.decode(encodedMessage, "newDevice");
            assertEquals(message, decodedMessage);
        } catch (IdNotInformedException e) {
            logger.fatal(e.toString());
            fail();
        } catch (SessionKeyNotInformedException e) {
            logger.fatal(e.toString());
            fail();
        }
    }

    public void testEncodeWithExpiredSessionKey() throws Exception {
        System.out.println("\n");
        logger.info("============== Teste " + currentTest++ + ": testEncodeDecodeWithExpiredSessionKey ==========================");
        System.out.println("\n");
        SessionKeyDaoHSQLDB sessionKeyDao = new SessionKeyDaoHSQLDB();
        try {
            sessionKeyDao.insert("newDevice", "1234567890abcdef1234567890abcdef");
            int TIME_TO_LIVE = -3;
            java.util.Date today = new java.util.Date();
            java.sql.Date newExpirationDate = new java.sql.Date(today.getTime());
            java.sql.Time newExpirationTime = new java.sql.Time(new java.util.Date().getTime() + ((1000 * 60 * 60) * TIME_TO_LIVE));
            sessionKeyDao.update("newDevice", "1234567890abcdef1234567890abcdef", newExpirationDate, newExpirationTime);
            AuthenticationDao authenticationDao = new AuthenticationDaoHSQLDB();
            if (authenticationDao.findByHashId(HashGenerator.generateHash("newDevice")) == null) {
                authenticationDao.insert("newDevice", HashGenerator.generateHash("newDevice"), "1234567890abcdef1234567890abcdef");
            }
            BasicSecurityHandler basicSecurityHandler = new BasicSecurityHandler();
            String message = "new message to be encoded";
            String encodedMessage = basicSecurityHandler.encode(message, "newDevice");
            assertNull(message, encodedMessage);
        } catch (IdNotInformedException e) {
            logger.fatal(e.toString());
            fail();
        } catch (SessionKeyNotInformedException e) {
            logger.fatal(e.toString());
            fail();
        }
    }

    public void testEncodeWithNonAuthenticatedDevice() throws Exception {
        System.out.println("\n");
        logger.info("============== Teste " + currentTest++ + ": testEncodeWithNonAuthenticatedDevice ==========================");
        System.out.println("\n");
        SessionKeyDaoHSQLDB sessionKeyDao = new SessionKeyDaoHSQLDB();
        try {
            if (sessionKeyDao.findById("newDevice") != null) {
                sessionKeyDao.delete("newDevice");
            }
            AuthenticationDao authenticationDao = new AuthenticationDaoHSQLDB();
            if (authenticationDao.findByHashId(HashGenerator.generateHash("newDevice")) == null) {
                authenticationDao.insert("newDevice", HashGenerator.generateHash("newDevice"), "1234567890abcdef1234567890abcdef");
            }
            sessionKeyDao = new SessionKeyDaoHSQLDB();
            BasicSecurityHandler basicSecurityHandler = new BasicSecurityHandler();
            String message = "new message to be encoded";
            String encodedMessage = basicSecurityHandler.encode(message, "newDevice");
            assertNull(message, encodedMessage);
        } catch (IdNotInformedException e) {
            logger.fatal(e.toString());
            fail();
        }
    }

    public void testDecodeWithExpiredSessionKey() throws Exception {
        System.out.println("\n");
        logger.info("============== Teste " + currentTest++ + ": testDecodeWithExpiredSessionKey ==========================");
        System.out.println("\n");
        SessionKeyDaoHSQLDB sessionKeyDao = new SessionKeyDaoHSQLDB();
        try {
            sessionKeyDao.insert("newDevice", "1234567890abcdef1234567890abcdef");
            AuthenticationDao authenticationDao = new AuthenticationDaoHSQLDB();
            if (authenticationDao.findByHashId(HashGenerator.generateHash("newDevice")) == null) {
                authenticationDao.insert("newDevice", HashGenerator.generateHash("newDevice"), "1234567890abcdef1234567890abcdef");
            }
            sessionKeyDao = new SessionKeyDaoHSQLDB();
            BasicSecurityHandler basicSecurityHandler = new BasicSecurityHandler();
            String message = "new message to be encoded";
            String encodedMessage = basicSecurityHandler.encode(message, "newDevice");
            int TIME_TO_LIVE = -3;
            java.util.Date today = new java.util.Date();
            java.sql.Date newExpirationDate = new java.sql.Date(today.getTime());
            java.sql.Time newExpirationTime = new java.sql.Time(new java.util.Date().getTime() + ((1000 * 60 * 60) * TIME_TO_LIVE));
            sessionKeyDao.update("newDevice", "1234567890abcdef1234567890abcdef", newExpirationDate, newExpirationTime);
            String decodedMessage = basicSecurityHandler.decode(encodedMessage, "newDevice");
            assertNull(decodedMessage);
        } catch (IdNotInformedException e) {
            logger.fatal(e.toString());
            fail();
        } catch (SessionKeyNotInformedException e) {
            logger.fatal(e.toString());
            fail();
        }
    }

    public void testDecodeWithNonAuthenticatedDevice() throws Exception {
        System.out.println("\n");
        logger.info("============== Teste " + currentTest++ + ": testDecodeWithExpiredSessionKey ==========================");
        System.out.println("\n");
        SessionKeyDaoHSQLDB sessionKeyDao = new SessionKeyDaoHSQLDB();
        try {
            sessionKeyDao.insert("newDevice", "1234567890abcdef1234567890abcdef");
            AuthenticationDao authenticationDao = new AuthenticationDaoHSQLDB();
            if (authenticationDao.findByHashId(HashGenerator.generateHash("newDevice")) == null) {
                authenticationDao.insert("newDevice", HashGenerator.generateHash("newDevice"), "1234567890abcdef1234567890abcdef");
            }
            sessionKeyDao = new SessionKeyDaoHSQLDB();
            BasicSecurityHandler basicSecurityHandler = new BasicSecurityHandler();
            String message = "new message to be encoded";
            String encodedMessage = basicSecurityHandler.encode(message, "newDevice");
            sessionKeyDao.delete("newDevice");
            basicSecurityHandler.encode(encodedMessage, "newDevice");
        } catch (IdNotInformedException e) {
            logger.fatal(e.toString());
            fail();
        } catch (SessionKeyNotInformedException e) {
            logger.fatal(e.toString());
            fail();
        }
    }
}
