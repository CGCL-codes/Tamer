package net.sf.adatagenerator.febrl.generators;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.adatagenerator.api.CreationException;
import net.sf.adatagenerator.api.FieldDependency;
import net.sf.adatagenerator.api.FieldDependencyManager;
import net.sf.adatagenerator.api.FieldGenerator;
import net.sf.adatagenerator.api.GenerationException;
import net.sf.adatagenerator.core.DefaultFrequencyBasedGenerator;
import net.sf.adatagenerator.util.ResourceUtil;
import net.sf.adatagenerator.util.StringFrequencyFile;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Generates a first name based culture, gender and family role. Based on Febrl python code.
 * 
 * @author chirag
 * 
 * @param <B>
 *            the bean type for which fields will be generated
 * @param <C>
 *            an extension of the bean type. The generated fields may depend on
 *            fields defined only by the extension, even though extension-only
 *            fields themselves will not be generated.
 */
public class FebrlFirstNameGenerator<B> extends DefaultFrequencyBasedGenerator<B, String> {

    public static final String FIELD_NAME = "FirstName";

    public static final String RESOURCE_GIVENNAME_FREQ = "givenname-freq.csv";

    public static final String RESOURCE_GIVENNAME_SEX_CULTURE = "givenname-lookup.tbl";

    protected static <B> FieldDependency<B> buildFebrlDependency(FieldDependencyManager<B> existingDependencies) {
        FieldDependency<B> retVal = existingDependencies.getFieldDependency(FIELD_NAME);
        assert retVal != null;
        retVal.addDependency(FebrlGenderGenerator.FIELD_NAME);
        retVal.addDependency(FebrlGeneratorUtilities.FIELD_NAME_CULTURE);
        retVal.addDependency(FebrlRoleGenerator.FIELD_NAME);
        return retVal;
    }

    private final String fullyQualifiedLookupName;

    private final Map<String, List<String>> firstNameCultureSexLookupDictionary = new HashMap<String, List<String>>();

    private FieldGenerator<B, String> cultureGenerator;

    private FieldGenerator<B, String> genderGenerator;

    /**
	 * Constructs a first name generator with a default field
	 * dependency appropriate to the Febrl generator model.
	 * @throws IOException
	 */
    public FebrlFirstNameGenerator(FieldDependencyManager<B> existingDependencies) {
        this(buildFebrlDependency(existingDependencies));
    }

    /**
	 * Constructs a first name generator with a specified field dependency
	 * that is constructed from a default first-name frequency map.
	 * @param fd must specify dependencies on fields describing culture
	 * and gender
	 * @throws IOException
	 */
    public FebrlFirstNameGenerator(FieldDependency<B> fd) {
        this(FebrlGeneratorUtilities.getFullyQualifiedDataName(RESOURCE_GIVENNAME_FREQ), FebrlGeneratorUtilities.getFullyQualifiedDataName(RESOURCE_GIVENNAME_SEX_CULTURE), FebrlFirstNameGenerator.class.getClassLoader(), fd);
    }

    /**
	 * Constructs a first name generator with a specified field dependency
	 * that is constructed from a specified first-name frequency map.
	 * @param fqfm a fully qualified resource name for a first-name frequency map.
	 * @param fqln a fully qualified resource name for a first-name lookup table
	 * by gender and culture.
	 * @param a non-null ClassLoader that will be used to create an input stream
	 * from the specified resources
	 * @param fd a non-null field dependency that specifies dependencies on fields
	 * describing culture and gender
	 * @throws IOException
	 */
    public FebrlFirstNameGenerator(String fqfm, String fqln, ClassLoader cl, FieldDependency<B> fd) {
        super(fd, new StringFrequencyFile(cl, fqfm));
        if (fqln == null || fqln.isEmpty()) {
            throw new IllegalArgumentException("null or empty lookup table name");
        }
        this.fullyQualifiedLookupName = fqln;
        this.setCultureGenerator(FebrlGeneratorUtilities.createFebrlCultureGenerator(fd.getFieldDependencyManager()));
        this.setGenderGenerator(new FebrlGenderGenerator<B>(fd.getFieldDependencyManager()));
    }

    protected String getFullyQualifiedLookupName() {
        return fullyQualifiedLookupName;
    }

    protected void clearFirstNameCultureSexLookupDictionary() {
        this.firstNameCultureSexLookupDictionary.clear();
    }

    protected void setFirstNameCultureSexLookupDict(Map<String, List<String>> firstNameCultureSexLookupDict) {
        if (firstNameCultureSexLookupDict == null || firstNameCultureSexLookupDict.isEmpty()) {
            throw new IllegalArgumentException("null or empty map");
        }
        clearFirstNameCultureSexLookupDictionary();
        this.firstNameCultureSexLookupDictionary.putAll(firstNameCultureSexLookupDict);
    }

    protected Map<String, List<String>> getFirstNameCultureSexLookupDict() {
        return this.firstNameCultureSexLookupDictionary;
    }

    protected void setCultureGenerator(FieldGenerator<B, String> cultureGenerator) {
        this.cultureGenerator = cultureGenerator;
    }

    public FieldGenerator<B, String> getCultureGenerator() {
        return cultureGenerator;
    }

    protected void setGenderGenerator(FebrlGenderGenerator<B> genderGenerator) {
        this.genderGenerator = genderGenerator;
    }

    public FieldGenerator<B, String> getGenderGenerator() {
        return genderGenerator;
    }

    protected void loadFirstNameCultureSexLookupDict() throws CreationException {
        clearFirstNameCultureSexLookupDictionary();
        ClassLoader cl = this.getClass().getClassLoader();
        String fqrn = this.getFullyQualifiedLookupName();
        Map<String, List<String>> map;
        try {
            map = ResourceUtil.loadMappedLists(cl, fqrn);
        } catch (IOException e) {
            String msg = "Error reading line from " + this.getFullyQualifiedLookupName();
            throw new CreationException(msg, e);
        }
        this.setFirstNameCultureSexLookupDict(map);
    }

    /**
	 * This method determines culture by first checking for an explicit
	 * culture setting in the <code>dependencyValues</code> map. If none
	 * is found, it uses the map to generate a culture. In this case, the
	 * culture generator may implicitly use any settings in the
	 * map to determine culture.<br/>
	 * 
	 * Subclasses may override this method to determine culture by some
	 * other scheme.
	 * @throws GenerationException 
	 */
    public String determineCulture(Map<String, Object> dependencyValues) throws GenerationException {
        String culture = null;
        if (dependencyValues != null) {
            culture = (String) dependencyValues.get(FebrlGeneratorUtilities.FIELD_NAME_CULTURE);
        }
        if (culture == null) {
            culture = getCultureGenerator().generateValue(dependencyValues);
        }
        return culture;
    }

    /**
	 * This method determines gender by first checking for an explicit
	 * gender setting in the <code>dependencyValues</code> map. If none
	 * is found, it uses the map to generate a gender. In this case, the
	 * gender generator may implicitly use any settings in the
	 * map to determine gender.<br/>
	 * 
	 * Subclasses may override this method to determine gender by some
	 * other scheme.
	 * @throws GenerationException 
	 */
    public String determineGender(Map<String, Object> dependencyValues) throws GenerationException {
        String gender = null;
        if (dependencyValues != null) {
            gender = (String) dependencyValues.get(FebrlGenderGenerator.FIELD_NAME);
        }
        if (gender == null) {
            gender = getGenderGenerator().generateValue(dependencyValues);
        }
        return gender;
    }

    /**
	 * @return may be null if no value can be generated for the specified
	 * values of culture and gender.
	 */
    public String generateCultureGenderBasedValue(String culture, String gender) {
        String retVal = null;
        if (culture != null && gender != null) {
            String cultureGenderKey = culture.toUpperCase() + "-" + gender;
            List<String> values = getFirstNameCultureSexLookupDict().get(cultureGenderKey);
            if (values != null && !values.isEmpty()) {
                int randomIndex = getFlatDistribution().nextInt(values.size());
                retVal = values.get(randomIndex).toLowerCase();
            }
        }
        return retVal;
    }

    @Override
    protected void loadData() throws CreationException {
        super.loadData();
        loadFirstNameCultureSexLookupDict();
    }

    /**
	 * If possible, names will be generated {@link #generateCultureGenderBasedValue(String, String)
	 * generated} from the {@link #determineCulture(Map) culture},
	 * and {@link #determineGender(Map) gender} values specified by the
	 * <code>dependencyValues</code> map. However, if this is not possible, names
	 * will be {@link #generateRandom() generated} from a
	 * {@link #getFrequencyBasedList() frequency-based list}
	 * of names.
	 * @throws GenerationException 
	 */
    @Override
    protected String generateValueImpl(Map<String, Object> dependencyValues) throws GenerationException {
        String culture = determineCulture(dependencyValues);
        String gender = determineGender(dependencyValues);
        String retVal = this.generateCultureGenderBasedValue(culture, gender);
        if (retVal == null || retVal.isEmpty()) {
            retVal = this.generateRandom();
            if (retVal == null) {
                retVal = "";
            } else {
                retVal = retVal.toLowerCase();
            }
        }
        assert retVal != null;
        retVal = WordUtils.capitalize(retVal.toLowerCase());
        return retVal;
    }
}
