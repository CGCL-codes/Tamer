package flexjson;

import flexjson.model.*;
import flexjson.transformer.FlatDateTransformer;
import flexjson.transformer.StateTransformer;
import flexjson.transformer.StringArrayTransformer;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.*;

public class SimpleSerializeTest extends TestCase {

    final Logger logger = LoggerFactory.getLogger(SimpleSerializeTest.class);

    public void testDeepSerializePerson() {
        Person person = buildPerson1();
        JSONSerializer serializer = new JSONSerializer();
        serializer.exclude("loopClassOnes.loopClassTwo.loopClassOne");
        String string = serializer.deepSerialize(person);
        logger.info(string);
        assertTrue(string.startsWith("{"));
        assertTrue(string.endsWith("}"));
    }

    public void testSerializePerson() {
        Person person = buildPerson1();
        JSONSerializer serializer = new JSONSerializer();
        serializer.exclude("loopClassOnes.loopClassTwo.loopClassOne");
        String string = serializer.serialize(person);
        logger.info(string);
        assertTrue(string.startsWith("{\""));
        assertTrue(string.endsWith("}"));
    }

    public void testDeepSerializePersonWithRootName() {
        Person person = buildPerson1();
        JSONSerializer serializer = new JSONSerializer().transform(new StateTransformer(), State.class).transform(new StringArrayTransformer(), String[].class).exclude("loopClassOnes.loopClassTwo.loopClassOne").rootName("myRootName");
        String string = serializer.deepSerialize(person);
        logger.info(string);
        assertTrue(string.contains("{\"myRootName\":"));
        assertTrue(string.contains("\"state\":\"Nevada\","));
    }

    public void testDeepSerializePersonWithWriter() {
        Person person = buildPerson1();
        StringWriter writer = new StringWriter();
        JSONSerializer serializer = new JSONSerializer().transform(new StateTransformer(), State.class).transform(new StringArrayTransformer(), String[].class).exclude("loopClassOnes.loopClassTwo.loopClassOne").rootName("myRootName");
        serializer.deepSerialize(person, writer);
        String string = writer.toString();
        logger.info(string);
        assertTrue(string.contains("{\"myRootName\":"));
        assertTrue(string.contains("\"state\":\"Nevada\","));
    }

    public void testDeepSerializePersonWithWriterPretty() {
        Person person = buildPerson1();
        StringWriter writer = new StringWriter();
        JSONSerializer serializer = new JSONSerializer().transform(new StateTransformer(), State.class).transform(new StringArrayTransformer(), String[].class).exclude("loopClassOnes.loopClassTwo.loopClassOne").rootName("myRootName").prettyPrint(true);
        serializer.deepSerialize(person, writer);
        String string = writer.toString();
        logger.info(string);
    }

    public void testSerializeAddressList() {
        Person person = buildPerson1();
        JSONSerializer serializer = new JSONSerializer().prettyPrint(true);
        String string = serializer.serialize(person.getAddresses());
        logger.info(string);
    }

    public void testSerializeAccountsMap() {
        Person person = buildPerson1();
        JSONSerializer serializer = new JSONSerializer().prettyPrint(true);
        String string = serializer.serialize(person.getAccounts());
        logger.info(string);
    }

    public void testInlineOnDate() {
        Date birthDate = buildPerson1().getBirthDate();
        JSONSerializer serializer = new JSONSerializer();
        serializer.transform(new FlatDateTransformer(""), Date.class);
        String json = serializer.serialize(birthDate);
        logger.info(json);
        assertEquals(json, "{\"month\":11,\"day\":13,\"year\":2007}");
    }

    public void testInlineOnPersonWithFlatDate() {
        Person person = buildPerson1();
        JSONSerializer serializer = new JSONSerializer();
        serializer.transform(new FlatDateTransformer("birthDate"), Date.class).exclude("loopClassOnes.loopClassTwo.loopClassOne");
        String json = serializer.serialize(person);
        logger.info(json);
        assertTrue(json.contains("\"birthDateMonth\":11,\"birthDateDay\":13,\"birthDateYear\":2007"));
    }

    public void testInlineOnCandidate() {
        Candidate candidate = buildCandidate1();
        JSONSerializer serializer = new JSONSerializer();
        serializer.transform(new FlatDateTransformer("dateOfBirth"), Date.class);
        String json = serializer.serialize(candidate);
        logger.info(json);
        assertTrue(json.contains("\"dateOfBirthMonth\":11,\"dateOfBirthDay\":13,\"dateOfBirthYear\":2007,"));
        assertFalse(json.contains(",,"));
    }

    public void testInlineOnCandidateWithoutPrefix() {
        Candidate candidate = buildCandidate1();
        JSONSerializer serializer = new JSONSerializer();
        serializer.transform(new FlatDateTransformer(""), Date.class);
        String json = serializer.serialize(candidate);
        logger.info(json);
        assertTrue(json.contains("\"dateOfBirthMonth\":11,\"dateOfBirthDay\":13,\"dateOfBirthYear\":2007,"));
        assertFalse(json.contains(",,"));
    }

    public void testDeferOnExperience() {
        List<Experience> experienceList = new ArrayList<Experience>();
        experienceList.add(builbExperience1());
        experienceList.add(buildExperience2());
        experienceList.add(buildExperience3());
        experienceList.add(buildExperience4());
        JSONSerializer serializer = new JSONSerializer().transform(new FlatDateTransformer(""), Date.class);
        String json = serializer.serialize(experienceList);
        logger.info(json);
    }

    public void testIgnoreOfListChildInMap() {
        Map someObject = new HashMap();
        List someList = new ArrayList();
        someList.add(buildPerson1());
        someList.add(buildPerson1());
        someObject.put("aList", someList);
        JSONSerializer serializer = new JSONSerializer().exclude("aList.loopClassOnes.loopClassTwo").prettyPrint(true);
        String json = serializer.deepSerialize(someObject);
        logger.info(json);
    }

    public Experience builbExperience1() {
        Experience experience = new Experience();
        experience.setId(123);
        experience.setCandidateId(121);
        experience.setOrganization("test");
        experience.setTitle("test");
        experience.setStateAbbr("AK");
        experience.setCity("test");
        Calendar beginCal = Calendar.getInstance();
        beginCal.set(2006, 0, 1);
        experience.setBeginDate(beginCal.getTime());
        experience.setEndDate(null);
        experience.setCurrent(true);
        experience.setJobDescription("� Item 1\n" + "� Item 2\n" + "� Item 3.\n" + "� Item 4.\n" + "� Item 5\n");
        return experience;
    }

    public Experience buildExperience2() {
        Experience experience = new Experience();
        experience.setId(124);
        experience.setCandidateId(121);
        experience.setOrganization("test");
        experience.setTitle("test");
        experience.setStateAbbr("AK");
        experience.setCity("test");
        Calendar beginCal = Calendar.getInstance();
        beginCal.set(2006, 0, 1);
        experience.setBeginDate(beginCal.getTime());
        experience.setEndDate(null);
        experience.setCurrent(true);
        experience.setJobDescription("test");
        return experience;
    }

    public Experience buildExperience3() {
        Experience experience = new Experience();
        experience.setId(125);
        experience.setCandidateId(121);
        experience.setOrganization("test");
        experience.setTitle("test");
        experience.setStateAbbr("AK");
        experience.setCity("test");
        Calendar beginCal = Calendar.getInstance();
        beginCal.set(2007, 0, 1);
        experience.setBeginDate(beginCal.getTime());
        experience.setEndDate(null);
        experience.setCurrent(true);
        experience.setJobDescription("test");
        return experience;
    }

    public Experience buildExperience4() {
        Experience experience = new Experience();
        experience.setId(126);
        experience.setCandidateId(121);
        experience.setOrganization("test");
        experience.setTitle("test");
        experience.setStateAbbr("AK");
        experience.setCity("test");
        Calendar beginCal = Calendar.getInstance();
        beginCal.set(2007, 0, 1);
        experience.setBeginDate(beginCal.getTime());
        Calendar endCal = Calendar.getInstance();
        endCal.set(2007, 0, 1);
        experience.setEndDate(null);
        experience.setCurrent(true);
        experience.setJobDescription("test");
        return experience;
    }

    public Candidate buildCandidate1() {
        Candidate candidate = new Candidate();
        Calendar c1 = Calendar.getInstance();
        c1.set(2007, 11, 13);
        candidate.setDateOfBirth(c1.getTime());
        return candidate;
    }

    public Person buildPerson1() {
        Calendar c = Calendar.getInstance();
        c.set(2007, 11, 13);
        Person person = new Person();
        person.setId(1);
        person.setFirstName("Joe");
        person.setLastName("Blow");
        person.setBirthDate(c.getTime());
        person.setFavoriteFoods(new String[] { "Ice Cream", "Burritos" });
        person.setLuckyNumbers(new Integer[] { 13, 23, 73 });
        person.setPastLottoPicks(new Integer[][] { { 12, 13, 14, 15, 16, 17 }, { 18, 19, 20, 21, 22, 23 }, { 24, 25, 26, 27, 28, 29, 30 } });
        State mt = new State();
        mt.setId(1);
        mt.setAbbrev("MT");
        mt.setName("Montana");
        State nv = new State();
        mt.setId(2);
        mt.setAbbrev("NV");
        mt.setName("Nevada");
        Address address1 = new Address();
        address1.setId(1);
        address1.setStreet1("123 Joke Way");
        address1.setStreet2("Apt 25");
        address1.setCity("Laughville");
        address1.setState(mt);
        address1.setPostal("01234");
        Address address2 = new Address();
        address2.setId(2);
        address2.setStreet1("456 Serious Way");
        address2.setStreet2("Apt 50");
        address2.setCity("Deadpanville");
        address2.setState(nv);
        address2.setPostal("56789");
        List<Address> addresses = new ArrayList<Address>();
        addresses.add(address1);
        addresses.add(address2);
        person.setAddresses(addresses);
        Map<String, Account> accounts = new HashMap<String, Account>();
        Account account1 = new Account();
        account1.setId(1);
        account1.setName("Joe Checking");
        account1.setAccountType(AccountType.Checking);
        account1.setAccountNumber("00001234567");
        account1.setBalance(new BigDecimal("123.40"));
        accounts.put(account1.getAccountNumber(), account1);
        Account account2 = new Account();
        account2.setId(2);
        account2.setName("Joe \"Savings\"");
        account2.setAccountType(AccountType.Savings);
        account2.setAccountNumber("00007654321");
        account2.setBalance(new BigDecimal("800.20"));
        accounts.put(account2.getAccountNumber(), account2);
        person.setAccounts(accounts);
        List<LoopClassOne> loopClassOnes = new ArrayList<LoopClassOne>();
        loopClassOnes.add(new LoopClassOne());
        loopClassOnes.add(new LoopClassOne());
        loopClassOnes.add(new LoopClassOne());
        loopClassOnes.add(new LoopClassOne());
        person.setLoopClassOnes(loopClassOnes);
        return person;
    }

    /**
     * http://sourceforge.net/tracker/?func=detail&atid=947842&aid=3088061&group_id=194042
     */
    public void testSerializeWithWriter() {
        JSONSerializer serializer = new JSONSerializer();
        Writer w = new StringWriter();
        Person person = buildPerson1();
        person.setFirstName("x=\"0123456789\" x=\"0123456789\" x=\"0123456789\" x=\"0123456789\" x=\"0123456789\" x=\"0123456789\" x=\"0123456789\" x=\"0123456789\" x=\"0123456789\" x=\"0123456789\"x=\"0123456789\" x=\"0123456789\" x=\"0123456789\" x=\"0123456789\" x=\"0123456789\"x=\"0123456789\" x=\"0123456789\" x=\"0123456789\" x=\"0123456789\" x=\"0123456789\"x=\"0123456789\" x=\"0123456789\" x=\"0123456789\" x=\"0123456789\" x=\"0123456789\"");
        serializer.deepSerialize(person, w);
    }
}
