package macaw.test.curation;

import java.util.ArrayList;
import macaw.MacawMessages;
import macaw.businessLayer.User;
import macaw.system.MacawErrorType;
import macaw.system.MacawException;
import macaw.util.DisplayableListSorter;

public class TestCurateUsers extends MacawCurationTestCase {

    private String verified;

    private String unverified;

    private User sallyMcLaren;

    private User brianFarnsworth;

    private User jenineHawthorn;

    public TestCurateUsers() {
        super("Test users");
        unverified = MacawMessages.getMessage("user.status.unverified");
        verified = MacawMessages.getMessage("user.status.verified");
        sallyMcLaren = new User();
        sallyMcLaren.setUserID("smclaren");
        sallyMcLaren.setFirstName("Sally");
        sallyMcLaren.setLastName("McLaren");
        sallyMcLaren.setAffiliation("GSTH");
        sallyMcLaren.setAddressLine1("345 5th Avenue");
        sallyMcLaren.setCity("London");
        sallyMcLaren.setCounty("None");
        sallyMcLaren.setPostCode("NW2 4R6");
        sallyMcLaren.setPhone("444 444 44444");
        sallyMcLaren.setEmail("sm@companyX.com");
        sallyMcLaren.setStatus(verified);
        sallyMcLaren.setPassword("pswd1");
        brianFarnsworth = new User();
        brianFarnsworth.setUserID("bfarnsworth");
        brianFarnsworth.setFirstName("Brian");
        brianFarnsworth.setLastName("Farnsworth");
        brianFarnsworth.setAffiliation("Manchester MRI");
        brianFarnsworth.setAddressLine1("555 Oxford Rd.");
        brianFarnsworth.setCity("Manchester");
        brianFarnsworth.setCounty("Lancashire");
        brianFarnsworth.setPostCode("M15 0R4");
        brianFarnsworth.setPhone("333 333 33333");
        brianFarnsworth.setEmail("brian@mri.org.uk");
        brianFarnsworth.setStatus(unverified);
        brianFarnsworth.setPassword("tig1ger2");
        jenineHawthorn = new User();
        jenineHawthorn.setUserID("jhawthorn");
        jenineHawthorn.setFirstName("Jenine");
        jenineHawthorn.setLastName("Hawthorn");
        jenineHawthorn.setAffiliation("Royal Hospital");
        jenineHawthorn.setAddressLine1("123 Yellow Brick Lane");
        jenineHawthorn.setCity("London");
        jenineHawthorn.setCounty("None");
        jenineHawthorn.setPostCode("SE4 3S8");
        jenineHawthorn.setPhone("222 222 22222");
        jenineHawthorn.setEmail("jhawthorn@royalHospital.org.uk");
        jenineHawthorn.setStatus(verified);
        jenineHawthorn.setPassword("t0x1n");
    }

    /**
	 * ensure userID field is tested
	 */
    public void testFieldValidationN1() {
        try {
            User user = (User) jenineHawthorn.clone();
            user.setUserID("");
            curationService.addUser(admin, user);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_USER);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * ensure that the first name has a value
	 */
    public void testFieldValidationN2() {
        try {
            User user = (User) jenineHawthorn.clone();
            user.setFirstName("");
            curationService.addUser(admin, user);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_USER);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * ensure that the last name has a value
	 */
    public void testFieldValidationN3() {
        try {
            User user = (User) jenineHawthorn.clone();
            user.setLastName("");
            curationService.addUser(admin, user);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_USER);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * ensure that the affliation has a value
	 */
    public void testFieldValidationN4() {
        try {
            User user = (User) jenineHawthorn.clone();
            user.setAffiliation("");
            curationService.addUser(admin, user);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_USER);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * ensure that the first line of the address has a value
	 */
    public void testFieldValidationN5() {
        try {
            User user = (User) jenineHawthorn.clone();
            user.setAddressLine1("");
            curationService.addUser(admin, user);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_USER);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * ensure that the city of the address has a value
	 */
    public void testFieldValidationN6() {
        try {
            User user = (User) jenineHawthorn.clone();
            user.setCity("");
            curationService.addUser(admin, user);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_USER);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * ensure that the city of the address has a value
	 */
    public void testFieldValidationN7() {
        try {
            User user = (User) jenineHawthorn.clone();
            user.setCounty("");
            curationService.addUser(admin, user);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_USER);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * ensure that the post code of the address has a value
	 */
    public void testFieldValidationN8() {
        try {
            User user = (User) jenineHawthorn.clone();
            user.setPostCode("");
            curationService.addUser(admin, user);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_USER);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * ensure that the phone of the address has a value
	 */
    public void testFieldValidationN9() {
        try {
            User user = (User) jenineHawthorn.clone();
            user.setPhone("");
            curationService.addUser(admin, user);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_USER);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * ensure that the email of the address has a value
	 */
    public void testFieldValidationN10() {
        try {
            User user = (User) jenineHawthorn.clone();
            user.setEmail("");
            curationService.addUser(admin, user);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_USER);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * ensure that the email of the address has a value
	 */
    public void testFieldValidationN11() {
        try {
            User user = (User) jenineHawthorn.clone();
            user.setStatus("");
            curationService.addUser(admin, user);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_USER);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * ensure that the password has a value
	 */
    public void testFieldValidationN12() {
        try {
            User user = (User) jenineHawthorn.clone();
            user.setPassword("");
            curationService.addUser(admin, user);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_USER);
            assertEquals(1, numberOfErrors);
        }
    }

    public void testAddUserN1() {
        try {
            curationService.addUser(admin, sallyMcLaren);
            curationService.addUser(admin, brianFarnsworth);
            curationService.addUser(admin, jenineHawthorn);
            ArrayList<User> usersSoFar = curationService.getUsers(admin);
            DisplayableListSorter<User> sorter = new DisplayableListSorter<User>();
            sorter.sort(usersSoFar);
            assertEquals(3, usersSoFar.size());
            assertEquals("bfarnsworth", usersSoFar.get(0).getUserID());
            assertEquals("jhawthorn", usersSoFar.get(1).getUserID());
            assertEquals("smclaren", usersSoFar.get(2).getUserID());
        } catch (MacawException exception) {
            log.logException(exception);
            fail();
        }
    }

    /**
	 * add two users who have the same first and last names but who have different
	 * userIDs
	 */
    public void testAddUserA1() {
        try {
            User user1 = (User) sallyMcLaren.clone();
            user1.setUserID("smclaren");
            curationService.addUser(admin, user1);
            User user2 = (User) sallyMcLaren.clone();
            user2.setUserID("smclaren2");
            user2.setEmail("another_email@abc.com");
            curationService.addUser(admin, user2);
            ArrayList<User> usersSoFar = curationService.getUsers(admin);
            DisplayableListSorter<User> sorter = new DisplayableListSorter<User>();
            sorter.sort(usersSoFar);
            assertEquals(2, usersSoFar.size());
            assertEquals("smclaren", usersSoFar.get(0).getUserID());
            assertEquals("smclaren2", usersSoFar.get(1).getUserID());
        } catch (MacawException exception) {
            log.logException(exception);
            fail();
        }
    }

    /**
	 * ERROR the addition of two users who have the same user ID
	 */
    public void testAddUserE1() {
        try {
            User user1 = (User) sallyMcLaren.clone();
            curationService.addUser(admin, user1);
            User user2 = (User) sallyMcLaren.clone();
            curationService.addUser(admin, user2);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.DUPLICATE_USER);
            assertEquals(1, numberOfErrors);
        }
    }

    public void testUpdateUserN1() {
        try {
            User user1 = (User) sallyMcLaren.clone();
            curationService.addUser(admin, user1);
            int identifier = curationService.getUserIdentifier(admin, user1);
            user1.setIdentifier(identifier);
            user1.setFirstName("Charlotte");
            curationService.updateUser(admin, user1);
            ArrayList<User> usersSoFar = curationService.getUsers(admin);
            User user2 = usersSoFar.get(0);
            assertEquals("Charlotte", user2.getFirstName());
            user2.setLastName("West");
            curationService.updateUser(admin, user2);
            usersSoFar = curationService.getUsers(admin);
            User user3 = usersSoFar.get(0);
            assertEquals("West", user3.getLastName());
            user3.setUserID("cwest1");
            curationService.updateUser(admin, user3);
            usersSoFar = curationService.getUsers(admin);
            User user4 = usersSoFar.get(0);
            assertEquals("cwest1", user4.getUserID());
            user4.setPassword("pazzw0rd");
            curationService.updateUser(admin, user4);
            usersSoFar = curationService.getUsers(admin);
            User user5 = usersSoFar.get(0);
            assertEquals("pazzw0rd", user5.getPassword());
            user5.setAffiliation("JJJ");
            curationService.updateUser(admin, user5);
            usersSoFar = curationService.getUsers(admin);
            User user6 = usersSoFar.get(0);
            assertEquals("JJJ", user6.getAffiliation());
            user6.setAddressLine1("address1");
            curationService.updateUser(admin, user6);
            usersSoFar = curationService.getUsers(admin);
            User user7 = usersSoFar.get(0);
            assertEquals("address1", user7.getAddressLine1());
            user7.setAddressLine2("address2");
            curationService.updateUser(admin, user7);
            usersSoFar = curationService.getUsers(admin);
            User user8 = usersSoFar.get(0);
            assertEquals("address2", user8.getAddressLine2());
            user8.setCity("cccity");
            curationService.updateUser(admin, user8);
            usersSoFar = curationService.getUsers(admin);
            User user9 = usersSoFar.get(0);
            assertEquals("cccity", user9.getCity());
            user9.setCounty("countyABC");
            curationService.updateUser(admin, user9);
            usersSoFar = curationService.getUsers(admin);
            User user10 = usersSoFar.get(0);
            assertEquals("countyABC", user10.getCounty());
            User user11 = user10;
            user11.setPhone("111 111 11111");
            curationService.updateUser(admin, user11);
            usersSoFar = curationService.getUsers(admin);
            User user12 = usersSoFar.get(0);
            assertEquals("111 111 11111", user12.getPhone());
            user12.setEmail("qqq@mycompany.co.uk");
            curationService.updateUser(admin, user12);
            usersSoFar = curationService.getUsers(admin);
            User user13 = usersSoFar.get(0);
            assertEquals("qqq@mycompany.co.uk", user13.getEmail());
            user13.setStatus(unverified);
            curationService.updateUser(admin, user13);
            usersSoFar = curationService.getUsers(admin);
            User user14 = usersSoFar.get(0);
            assertEquals(unverified, user14.getStatus());
            user14.setPostCode("NE1 2Y5");
            curationService.updateUser(admin, user14);
            ArrayList<User> usSoFar = curationService.getUsers(admin);
            User user15 = usSoFar.get(0);
            assertEquals("NE1 2Y5", user15.getPostCode());
        } catch (MacawException exception) {
            exception.printStackTrace(System.out);
            log.logException(exception);
            fail();
        }
    }

    public void testDeleteUserN1() {
        try {
            User user1 = (User) sallyMcLaren.clone();
            curationService.addUser(admin, user1);
            User user2 = (User) brianFarnsworth.clone();
            curationService.addUser(admin, user2);
            int identifier2 = curationService.getUserIdentifier(admin, user2);
            user2.setIdentifier(identifier2);
            User user3 = (User) jenineHawthorn.clone();
            curationService.addUser(admin, user3);
            int identifier3 = curationService.getUserIdentifier(admin, user3);
            user3.setIdentifier(identifier3);
            ArrayList<User> itemsToDelete = new ArrayList<User>();
            itemsToDelete.add(user2);
            itemsToDelete.add(user3);
            curationService.deleteUsers(admin, itemsToDelete);
            ArrayList<User> usersSoFar = curationService.getUsers(admin);
            DisplayableListSorter<User> sorter = new DisplayableListSorter<User>();
            sorter.sort(usersSoFar);
            assertEquals(1, usersSoFar.size());
            assertEquals("smclaren", usersSoFar.get(0).getUserID());
        } catch (MacawException exception) {
            log.logException(exception);
            fail();
        }
    }

    /**
	 * delete record from a one item list
	 */
    public void testDeleteUserA1() {
        try {
            User user1 = (User) brianFarnsworth.clone();
            curationService.addUser(admin, user1);
            int identifier2 = curationService.getUserIdentifier(admin, user1);
            user1.setIdentifier(identifier2);
            ArrayList<User> itemsToDelete = new ArrayList<User>();
            itemsToDelete.add(user1);
            curationService.deleteUsers(admin, itemsToDelete);
            ArrayList<User> usersSoFar = curationService.getUsers(admin);
            assertEquals(0, usersSoFar.size());
        } catch (MacawException exception) {
            log.logException(exception);
            fail();
        }
    }

    /**
	 * delete non-existent user
	 */
    public void testDeleteUserE1() {
        try {
            User user1 = (User) brianFarnsworth.clone();
            curationService.addUser(admin, user1);
            User user2 = (User) brianFarnsworth.clone();
            user2.setIdentifier(34242);
            ArrayList<User> itemsToDelete = new ArrayList<User>();
            itemsToDelete.add(user2);
            curationService.deleteUsers(admin, itemsToDelete);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.NON_EXISTENT_USER);
            assertEquals(1, numberOfErrors);
        }
    }
}
