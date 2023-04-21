package org.eclipse.tptp.test.tools.web.criterion.ba.data;

import org.eclipse.tptp.models.web.common.test.data.CriterionType;
import org.eclipse.tptp.models.web.common.test.data.ICriterion;

public interface IBusinessAttributeCriterion extends ICriterion {

    public static final CriterionType CriterionType = new CriterionType("Business Attribute Criterion", "Checks pairs of Business Attribute Names and Values. For example in an HTML table with two columns.", 3);

    public static final String PROPERTY_VALUE_XPATH_NAME = "valueXPath";

    public static final String PROPERTY_NAME_XPATH_NAME = "nameXPath";

    public static final String PROPERTY_ELEMENT_XPATH_NAME = "elementXPath";

    public static final String PROPERTY_TEST_BA_SEQUENCE = "testBASequence";

    String getXPathName();

    String getXPathValue();

    String getXPathElement();

    void setXPathName(String nameXPath);

    void setXPathValue(String valueXPath);

    void setXPathElement(String elementXPath);

    boolean getTestBASequence();

    void setTestBASequence(boolean testBASequence);
}
