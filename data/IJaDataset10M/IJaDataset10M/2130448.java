package net.sf.adatagenerator.ex.healthreg.bean.groups;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import net.sf.adatagenerator.api.CreationException;
import net.sf.adatagenerator.api.GeneratorMap;
import net.sf.adatagenerator.api.ProcessingException;
import net.sf.adatagenerator.core.AbstractGroupCreator;
import net.sf.adatagenerator.ex.healthreg.api.GeneratedPatientRecord;
import net.sf.adatagenerator.ex.healthreg.api.PatientRecord.PatientField;
import net.sf.adatagenerator.ex.healthreg.api.Record.RecordField;
import org.apache.commons.beanutils.BeanUtils;

public class MarriedName extends AbstractGroupCreator<GeneratedPatientRecord> {

    public static final String GROUP_NAME = "MarriedName";

    public static final String PN_MIN = "min";

    public static final String PN_MAX = "max";

    public static final String PN_TYPE = "type";

    public static final int MINIMUM_MIN = 1;

    public static final int DEFAULT_MIN = 4;

    public static final int DEFAULT_MAX = 4;

    public static final String DEFAULT_TYPE = "random";

    private ArrayList<String> changeFields = new ArrayList<String>();

    private static Logger logger = Logger.getLogger(MarriedName.class.getName());

    public HashMap<String, HashMap<String, String>> fieldRoleValues = new HashMap<String, HashMap<String, String>>();

    public MarriedName() {
        for (RecordField field : RecordField.values()) {
            changeFields.add(field.name());
        }
        for (PatientField field : PatientField.values()) {
            changeFields.add(field.name());
        }
        changeFields.remove(RecordField.ABORIGIN.name());
        changeFields.remove(RecordField.ABTSI.name());
        changeFields.remove(RecordField.DOB.name());
        changeFields.remove(RecordField.AGE.name());
        changeFields.remove(RecordField.GIVNAMES.name());
        changeFields.remove(RecordField.ALGNAME.name());
        changeFields.remove(RecordField.ADDRESS.name());
        changeFields.remove(RecordField.LOCALITY.name());
        changeFields.remove(RecordField.PCODE.name());
    }

    @Override
    public List<GeneratedPatientRecord> newGroup(GeneratedPatientRecord template, GeneratorMap<GeneratedPatientRecord> registry, String superGroupName, Properties params) throws CreationException {
        int members = totalMembers(params);
        List<GeneratedPatientRecord> retVal = newGroup(template, members, registry, superGroupName);
        return retVal;
    }

    protected int getMin(Properties p) throws CreationException {
        assert p != null;
        int retVal = DEFAULT_MIN;
        String sMin = p.getProperty(PN_MIN);
        if (sMin != null) {
            try {
                retVal = Integer.parseInt(sMin);
            } catch (NumberFormatException x) {
                throw new CreationException("invalid value for minimum group size (" + sMin + ")");
            }
        }
        if (retVal < MINIMUM_MIN) {
            throw new CreationException("minimum group size (" + retVal + ") is less than " + MINIMUM_MIN);
        }
        return retVal;
    }

    protected int getMax(Properties p) throws CreationException {
        assert p != null;
        int retVal = DEFAULT_MAX;
        String sMax = p.getProperty(PN_MAX);
        if (sMax != null) {
            try {
                retVal = Integer.parseInt(sMax);
            } catch (NumberFormatException x) {
                throw new CreationException("invalid value for maximum group size (" + sMax + ")");
            }
        }
        if (retVal < MINIMUM_MIN) {
            throw new CreationException("maximum group size (" + retVal + ") is less than " + MINIMUM_MIN);
        }
        return retVal;
    }

    protected int totalMembers(Properties params) throws CreationException {
        assert params != null;
        String distribution = params.getProperty(PN_TYPE);
        if (distribution != null && !distribution.isEmpty() && !distribution.trim().equalsIgnoreCase(DEFAULT_TYPE)) {
            logger.warning("ignoring specified distribution type (" + distribution + ")");
        }
        int members = randomValue(getMin(params), getMax(params));
        return members;
    }

    public List<GeneratedPatientRecord> newGroup(GeneratedPatientRecord template, int members, GeneratorMap<GeneratedPatientRecord> registry, String superGroupId) throws CreationException {
        List<GeneratedPatientRecord> vRecords = new ArrayList<GeneratedPatientRecord>();
        try {
            for (int i = 0; i < members; i++) {
                GeneratedPatientRecord clonedRecord = (GeneratedPatientRecord) BeanUtils.cloneBean(template);
                vRecords.add(mutate(clonedRecord, registry, changeFields, logger));
            }
        } catch (Exception e) {
            throw new CreationException(e.getMessage(), e);
        }
        return vRecords;
    }

    @Override
    public <F> F lookUpFieldValue(Class<F> fieldType, String groupRoleName, String fieldName) {
        return null;
    }

    @Override
    public Map<String, Integer> setMemberDistributionAndOrder(int total) throws ProcessingException {
        return null;
    }
}
