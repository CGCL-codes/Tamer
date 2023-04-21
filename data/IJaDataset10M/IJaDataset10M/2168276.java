package org.mitre.scap.oval.ovaldi.check;

import org.mitre.scap.xccdf.properties.BuildProperties;
import org.mitre.scap.xccdf.check.CheckEvaluator;
import org.mitre.scap.xccdf.check.CheckEvaluatorHelper;
import org.mitre.scap.xccdf.check.CheckSystem;
import java.math.BigDecimal;
import java.util.Calendar;
import org.mitre.oval.xmlSchema.ovalCommon5.GeneratorType;

public class OVALCheckSystem extends CheckSystem {

    public static final BigDecimal SCHEMA_VERSION = new BigDecimal("5.3");

    public static final String OVAL_SYSTEM_NAME = "http://oval.mitre.org/XMLSchema/oval-definitions-5";

    public OVALCheckSystem() {
        super(OVAL_SYSTEM_NAME);
    }

    @Override
    public CheckEvaluator newEvaluatorInstance(final CheckEvaluatorHelper helper) {
        return new OVALCheckEvaluator(this, helper);
    }

    public GeneratorType getGenerator() {
        GeneratorType retval = GeneratorType.Factory.newInstance();
        retval.setProductName(BuildProperties.getInstance().getApplicationName());
        retval.setProductVersion(BuildProperties.getInstance().getApplicationVersion());
        retval.setSchemaVersion(SCHEMA_VERSION);
        retval.setTimestamp(Calendar.getInstance());
        return retval;
    }
}
