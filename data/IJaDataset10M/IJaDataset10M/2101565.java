package assays.com;

import java.util.*;
import samples.hibernate.MetaData;

public final class AliquotMetadataManager {

    private static HashMap aliquotData = null;

    private static HashMap sampleData = null;

    private static HashMap personData = null;

    private static final String PERSON = "person";

    private static final String SAMPLE = "sample";

    private static final String ALIQUOT = "aliquot";

    public static void buildAliquotData(List aliquotMD) {
        aliquotData = new HashMap(aliquotMD.size());
        Iterator iter = aliquotMD.iterator();
        while (iter.hasNext()) {
            MetaData metadata = (MetaData) iter.next();
            String key = metadata.getVisibleName();
            aliquotData.put(key, new String(metadata.getShortName()));
        }
    }

    public static void buildSampleData(List sampleMD) {
        sampleData = new HashMap(sampleMD.size());
        Iterator iter = sampleMD.iterator();
        while (iter.hasNext()) {
            MetaData metadata = (MetaData) iter.next();
            String key = metadata.getVisibleName();
            sampleData.put(key, new String(metadata.getShortName()));
        }
    }

    public static void buildPersonData(List personMD) {
        personData = new HashMap(personMD.size());
        Iterator iter = personMD.iterator();
        while (iter.hasNext()) {
            MetaData metadata = (MetaData) iter.next();
            String key = metadata.getVisibleName();
            personData.put(key, new String(metadata.getShortName()));
        }
    }

    public static String getAliquotShortName(String key) {
        String shortName = (String) aliquotData.get(key);
        return (shortName != null) ? shortName : key;
    }

    public static String getSampleShortName(String key) {
        String shortName = (String) sampleData.get(key);
        return (shortName != null) ? shortName : key;
    }

    public static String getPersonShortName(String key) {
        String shortName = (String) personData.get(key);
        return (shortName != null) ? shortName : key;
    }
}
