package au.org.cherel.datagen.bean.resources;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.sf.adatagenerator.api.CreationException;
import net.sf.adatagenerator.util.DefaultNullComparator;
import net.sf.adatagenerator.util.FrequencyBasedList;
import net.sf.adatagenerator.util.FrequencyBasedListFactory;
import net.sf.adatagenerator.util.ResourceUtil;
import au.org.cherel.datagen.api.Record.Gender;

/**
 * Initializes a frequency based list of CHeReL gender values by reading
 * the relative frequency of the values from a csv text file of
 * values and counts; e.g.<pre>
 * 1,15513373
 * 2,16945979
 * 3,611
 * 4,21
 * 9,3206
 * </pre>
 * 
 * @author rphall
 */
public class CherelGenderFrequencyFile implements FrequencyBasedListFactory<Gender> {

    public static final String NULL_VALUE_MARKER = "NULL";

    private static final Comparator<Gender> comparator = new DefaultNullComparator<Gender>();

    private final String fullyQualifiedResourceName;

    private final ClassLoader classLoader;

    /**
	 * @param fqrn a fully qualified resource name for the file from which
	 * frequency-weighted data should be loaded.
	 * @param classLoader a non-null class loader that will create an input stream
	 * from the specified resource file
	 */
    public CherelGenderFrequencyFile(ClassLoader cl, String fqrn) {
        if (fqrn == null || fqrn.isEmpty()) {
            throw new IllegalArgumentException("null or empty fully qualified resource name");
        }
        if (cl == null) {
            throw new IllegalArgumentException("null class loader");
        }
        this.fullyQualifiedResourceName = fqrn;
        this.classLoader = cl;
    }

    public String getFullyQualifiedResourceName() {
        return fullyQualifiedResourceName;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Comparator<Gender> getListComparator() {
        return comparator;
    }

    public FrequencyBasedList<Gender> createFrequencyBasedList() throws CreationException {
        ClassLoader cl = this.getClassLoader();
        String fqrn = this.getFullyQualifiedResourceName();
        Map<Gender, Integer> relativeFrequencies = new HashMap<Gender, Integer>();
        try {
            FrequencyBasedList<String> fbl = ResourceUtil.loadFrequencyBasedList(cl, fqrn, NULL_VALUE_MARKER);
            Map<String, Integer> stringFrequencies = fbl.getRelativeFrequencies();
            Set<String> stringValues = stringFrequencies.keySet();
            for (String stringValue : stringValues) {
                try {
                    Gender gender;
                    if (stringValue == null) {
                        gender = null;
                    } else {
                        Integer value = Integer.valueOf(stringValue);
                        gender = Gender.parseValue(value);
                    }
                    Integer relativeFrequency = stringFrequencies.get(stringValue);
                    relativeFrequencies.put(gender, relativeFrequency);
                } catch (NumberFormatException e) {
                    String msg = "Unable to parse '" + stringValue + "' to an Integer";
                    throw new CreationException(msg, e);
                }
            }
        } catch (IOException e) {
            String msg = "Error reading resource file: " + e.toString();
            throw new CreationException(msg, e);
        }
        FrequencyBasedList<Gender> retVal = new FrequencyBasedList<Gender>(relativeFrequencies, getListComparator());
        return retVal;
    }
}
