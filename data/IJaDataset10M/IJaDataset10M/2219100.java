package net.sf.adatagenerator.febrl.generators;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import net.sf.adatagenerator.api.CreationException;
import net.sf.adatagenerator.api.FieldDependency;
import net.sf.adatagenerator.api.FieldDependencyManager;
import net.sf.adatagenerator.api.FieldGenerator;
import net.sf.adatagenerator.api.GenerationException;
import net.sf.adatagenerator.api.GeneratorMap;
import net.sf.adatagenerator.api.GroupCreator;
import net.sf.adatagenerator.api.ProcessingException;
import net.sf.adatagenerator.core.AbstractGeneratorMap;
import net.sf.adatagenerator.core.DefaultFieldDependencyManager;
import net.sf.adatagenerator.core.FieldGeneratorChain;
import net.sf.adatagenerator.febrl.testutils.FebrlTestRecord;
import net.sf.adatagenerator.febrl.testutils.MockGroupCreator;
import net.sf.adatagenerator.febrl.testutils.TestRoles;
import org.joda.time.DateMidnight;
import org.joda.time.Years;

/**
 * The point of these tests is to check:<ul>
 * <li/> Does the FebrlBirthDateGeneratorTest find its resource files?
 * If it doesn't, then it will blow up during construction. Hence the
 * first test is to simply construct an age generator.<br/>
 * 
 * <li/> The next check is to see if it generates values even when no
 * dependency information is passed in. This breaks up into two parts.
 * First, an birth date generator is created using a constructor (see
 * {@link #testGeneratedValueHashMapOfStringObject()}. Next, a birth date
 * generator is constructed in the context of a group using a
 * {@link net.sf.adatagenerator.febrl.testutils.MockGroupCreator
 * MockGroupCreator} that builds empty dependency relationships.<br/>
 * 
 * <li/> The third check is to see if a birth date generator produces
 * values that are properly dependent on age.</ul>
 * 
 * <li/> Finally, the last checks are to see a birth date generator
 * works properly in the context of a group. See
 * {@link FebrlAgeGeneratorTest} for more details.
 * </ul>
 * 
 * @author rphall
 */
public class FebrlBirthDateGeneratorTest extends TestCase {

    /** @see #getRoleAges() */
    @SuppressWarnings("serial")
    private final Map<TestRoles, Object> roleAges = new HashMap<TestRoles, Object>() {

        {
        }
    };

    /**
	 * @return A map of roles to group creators
	 * used by {@link #getFieldValues()}
	 */
    public Map<TestRoles, Object> getRoleAges() {
        return roleAges;
    }

    /** @see #getFieldValues() */
    @SuppressWarnings("serial")
    private final Map<String, Map<TestRoles, Object>> fieldValues = new HashMap<String, Map<TestRoles, Object>>() {

        {
            put(FebrlTestRecord.Fields.Age.name(), getRoleAges());
        }
    };

    /**
	 * @return A map of fields to role-specific group creators
	 * used by {@link net.sf.adatagenerator.febrl.testutils.MockGroupCreator MockGroupCreators}
	 */
    public Map<String, Map<TestRoles, Object>> getFieldValues() {
        return fieldValues;
    }

    /**
	 * Clears the {@link #getRoleAges() role-group map} and the
	 * {@link #getFieldValues() field values map} and then adds back
	 * a mapping of "Age" to the (now empty) role-group map. The
	 * result is that if new mappings for role to group creator are added to the
	 * role-group map, they will be used by any MockGroupCreator that uses
	 * the field values map.
	 */
    public void setUp() {
        this.getRoleAges().clear();
        this.getFieldValues().clear();
        getFieldValues().put(FebrlTestRecord.Fields.Age.name(), getRoleAges());
    }

    /** Tests construction */
    public void testBirthDateGenerator() throws Exception {
        FieldDependencyManager<Object> fdm = new DefaultFieldDependencyManager<Object>();
        new FebrlBirthDateGenerator<Object>(fdm);
    }

    /** Tests null and empty dependency values */
    public void testGeneratedValueHashMapOfStringObject() throws Exception {
        FieldDependencyManager<Object> fdm = new DefaultFieldDependencyManager<Object>();
        FieldGenerator<Object, Date> fg = new FebrlBirthDateGenerator<Object>(fdm);
        Date i = fg.generateValue(null);
        assertTrue(i != null);
        i = fg.generateValue(new HashMap<String, Object>());
        assertTrue(i != null);
    }

    /** Tests dependency */
    public void testGetDependancy() throws Exception {
        FieldDependencyManager<Object> fdm = new DefaultFieldDependencyManager<Object>();
        FieldGenerator<Object, Date> fg = new FebrlBirthDateGenerator<Object>(fdm);
        FieldDependency<Object> fd = fg.getDependency();
        String fieldName = fd.getFieldName();
        assertTrue(FebrlBirthDateGenerator.FIELD_NAME.equals(fieldName));
    }

    /** Tests dependency of generated values */
    public void testDependencyOfGeneratedValue() throws Exception {
        final DateMidnight now = new DateMidnight();
        for (int age = 0; age < 100; age++) {
            FieldDependencyManager<Object> fdm = new DefaultFieldDependencyManager<Object>();
            FieldDependency<Object> ageDependency = fdm.getFieldDependency(FebrlAgeGenerator.FIELD_NAME);
            FebrlBirthDateGenerator<Object> febrlBDG = new FebrlBirthDateGenerator<Object>(ageDependency);
            final Integer Age = new Integer(age);
            Map<String, Object> dependencies = new HashMap<String, Object>();
            dependencies.put(FebrlAgeGenerator.FIELD_NAME, Age);
            Date birthDate = febrlBDG.generateValue(dependencies);
            assertTrue(birthDate != null);
            DateMidnight birthDateMidnight = new DateMidnight(birthDate);
            int birthDateAge = Years.yearsBetween(birthDateMidnight, now).getYears();
            assertTrue(birthDateAge == Age.intValue());
        }
    }

    /** Tests generation within a group with no roles */
    public void testGroupGenerationWithoutRoles() throws IOException, ProcessingException {
        final FieldDependencyManager<FebrlTestRecord> fdm = new DefaultFieldDependencyManager<FebrlTestRecord>();
        final FebrlAgeGenerator<FebrlTestRecord> febrlAG = new FebrlAgeGenerator<FebrlTestRecord>(fdm);
        final FebrlBirthDateGenerator<FebrlTestRecord> febrlBDG = new FebrlBirthDateGenerator<FebrlTestRecord>(fdm);
        this.getFieldValues().put(FebrlTestRecord.Fields.Age.name(), getRoleAges());
        MockGroupCreator gc = new MockGroupCreator(getFieldValues());
        GeneratorMap<FebrlTestRecord> registry = new AbstractGeneratorMap<FebrlTestRecord>() {

            @Override
            public void buildGeneratorMapInternal(FieldDependencyManager<FebrlTestRecord> fdm) throws CreationException {
                map(febrlAG);
                map(febrlBDG);
            }
        };
        registry.buildGeneratorMap(fdm);
        FebrlTestRecord template = new FebrlTestRecord();
        List<FebrlTestRecord> records = gc.newGroup(template, registry, null, null);
        FebrlTestRecord generated = records.get(0);
        String role = generated.getRole();
        assertTrue(role == null);
        Integer age = generated.getAge();
        assertTrue(age != null);
        Date birthDate = generated.getBirthDate();
        assertTrue(birthDate != null);
        DateMidnight now = new DateMidnight();
        DateMidnight birthDateMidnight = new DateMidnight(birthDate);
        int birthDateAge = Years.yearsBetween(birthDateMidnight, now).getYears();
        assertTrue(birthDateAge == age.intValue());
    }

    public static final int FATHER_AGE = 101;

    public static final int MOTHER_AGE = 100;

    /** Tests generation within a group with roles */
    public void testGroupGenerationWithRoles() throws IOException, ProcessingException {
        final FieldDependencyManager<FebrlTestRecord> fdm = new DefaultFieldDependencyManager<FebrlTestRecord>();
        final FebrlAgeGenerator<FebrlTestRecord> febrlAG = new FebrlAgeGenerator<FebrlTestRecord>(fdm);
        final FebrlBirthDateGenerator<FebrlTestRecord> febrlBDG = new FebrlBirthDateGenerator<FebrlTestRecord>(fdm);
        this.getFieldValues().put(FebrlTestRecord.Fields.Age.name(), getRoleAges());
        final MockGroupCreator gc = new MockGroupCreator(getFieldValues());
        GeneratorMap<FebrlTestRecord> registry = new AbstractGeneratorMap<FebrlTestRecord>() {

            @Override
            public void buildGeneratorMapInternal(FieldDependencyManager<FebrlTestRecord> fdm) throws CreationException {
                map(febrlAG);
                map(febrlBDG);
            }
        };
        registry.buildGeneratorMap(fdm);
        final Integer MAX_AGE = febrlAG.getMaximumValue();
        assertTrue(MAX_AGE == null || MAX_AGE < FATHER_AGE);
        assertTrue(MAX_AGE == null || MAX_AGE < MOTHER_AGE);
        getRoleAges().put(TestRoles.Father, FATHER_AGE);
        getRoleAges().put(TestRoles.Mother, MOTHER_AGE);
        FebrlTestRecord template = new FebrlTestRecord();
        template.setRole(TestRoles.Son.name());
        List<FebrlTestRecord> records = gc.newGroup(template, registry, null, null);
        FebrlTestRecord generated = records.get(0);
        Integer Age = generated.getAge();
        assertTrue(Age == null || Age.intValue() < FATHER_AGE);
        assertTrue(Age == null || Age.intValue() < MOTHER_AGE);
        template = new FebrlTestRecord();
        template.setRole(TestRoles.Father.name());
        template.setAge(FATHER_AGE);
        records = gc.newGroup(template, registry, null, null);
        generated = records.get(0);
        Integer genFatherAge = generated.getAge();
        assertTrue(genFatherAge != null);
        Date birthDateFather = generated.getBirthDate();
        assertTrue(birthDateFather != null);
        DateMidnight now = new DateMidnight();
        DateMidnight birthDateMidnight = new DateMidnight(birthDateFather);
        final int birthDateAgeFather = Years.yearsBetween(birthDateMidnight, now).getYears();
        assertTrue(birthDateAgeFather == genFatherAge.intValue());
        template = new FebrlTestRecord();
        template.setRole(TestRoles.Mother.name());
        template.setAge(MOTHER_AGE);
        records = gc.newGroup(template, registry, null, null);
        generated = records.get(0);
        Integer genMotherAge = generated.getAge();
        assertTrue(genMotherAge != null);
        Date birthDateMother = generated.getBirthDate();
        assertTrue(birthDateMother != null);
        now = new DateMidnight();
        birthDateMidnight = new DateMidnight(birthDateMother);
        final int birthDateAgeMother = Years.yearsBetween(birthDateMidnight, now).getYears();
        assertTrue(birthDateAgeMother == genMotherAge.intValue());
        assertTrue(genFatherAge.intValue() < FATHER_AGE);
        assertTrue(genMotherAge.intValue() < MOTHER_AGE);
        assertFalse(birthDateAgeFather == FATHER_AGE || birthDateAgeMother == MOTHER_AGE);
    }

    /** Tests chained generation within a group with roles */
    public void testChainedGroupGenerationWithRoles() throws IOException, ProcessingException {
        final FieldDependencyManager<FebrlTestRecord> fdm = new DefaultFieldDependencyManager<FebrlTestRecord>();
        final FebrlAgeGenerator<FebrlTestRecord> febrlAG = new FebrlAgeGenerator<FebrlTestRecord>(fdm);
        final FebrlBirthDateGenerator<FebrlTestRecord> febrlBDG = new FebrlBirthDateGenerator<FebrlTestRecord>(fdm);
        this.getFieldValues().put(FebrlTestRecord.Fields.Age.name(), getRoleAges());
        final MockGroupCreator gc = new MockGroupCreator(getFieldValues());
        final FieldGeneratorChain<FebrlTestRecord, Integer> familyAgeGenerator = new FieldGeneratorChain<FebrlTestRecord, Integer>(febrlAG) {

            @Override
            public Integer generateValue(Map<String, Object> dependencyValues) throws GenerationException {
                dependencyValues.put(GroupCreator.DEPENDENCYKEY_GROUPCREATOR, gc);
                return this.getDelegate().generateValue(dependencyValues);
            }

            @Override
            public FieldDependency<FebrlTestRecord> getDependency() {
                return this.getDelegate().getDependency();
            }
        };
        GeneratorMap<FebrlTestRecord> registry = new AbstractGeneratorMap<FebrlTestRecord>() {

            @Override
            public void buildGeneratorMapInternal(FieldDependencyManager<FebrlTestRecord> fdm) throws CreationException {
                map(familyAgeGenerator);
                map(febrlBDG);
            }
        };
        registry.buildGeneratorMap(fdm);
        getRoleAges().put(TestRoles.Father, FATHER_AGE);
        getRoleAges().put(TestRoles.Mother, MOTHER_AGE);
        FebrlTestRecord template = new FebrlTestRecord();
        template.setRole(TestRoles.Son.name());
        List<FebrlTestRecord> records = gc.newGroup(template, registry, null, null);
        FebrlTestRecord generated = records.get(0);
        Integer Age = generated.getAge();
        assertTrue(Age.intValue() < FATHER_AGE);
        assertTrue(Age.intValue() < MOTHER_AGE);
        template = new FebrlTestRecord();
        template.setRole(TestRoles.Father.name());
        template.setAge(FATHER_AGE);
        records = gc.newGroup(template, registry, null, null);
        generated = records.get(0);
        Integer genFatherAge = generated.getAge();
        assertTrue(genFatherAge != null);
        Date birthDateFather = generated.getBirthDate();
        assertTrue(birthDateFather != null);
        DateMidnight now = new DateMidnight();
        DateMidnight birthDateMidnight = new DateMidnight(birthDateFather);
        final int birthDateAgeFather = Years.yearsBetween(birthDateMidnight, now).getYears();
        assertTrue(birthDateAgeFather == genFatherAge.intValue());
        template = new FebrlTestRecord();
        template.setRole(TestRoles.Mother.name());
        template.setAge(MOTHER_AGE);
        records = gc.newGroup(template, registry, null, null);
        generated = records.get(0);
        Integer genMotherAge = generated.getAge();
        assertTrue(genMotherAge != null);
        Date birthDateMother = generated.getBirthDate();
        assertTrue(birthDateMother != null);
        now = new DateMidnight();
        birthDateMidnight = new DateMidnight(birthDateMother);
        final int birthDateAgeMother = Years.yearsBetween(birthDateMidnight, now).getYears();
        assertTrue(birthDateAgeMother == genMotherAge.intValue());
        assertTrue(genFatherAge.intValue() == FATHER_AGE);
        assertTrue(genMotherAge.intValue() == MOTHER_AGE);
        assertTrue(birthDateAgeFather == FATHER_AGE && birthDateAgeMother == MOTHER_AGE);
    }
}
