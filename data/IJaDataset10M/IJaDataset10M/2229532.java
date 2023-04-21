package net.sf.irunninglog.businessobject;

import net.sf.irunninglog.canonical.HasRunnerId;
import net.sf.irunninglog.util.ConstantValues;

public abstract class HasRunnerIdBOTestCase extends BaseBOTestCase {

    protected static final String V_RUNNER_ID_1 = "abcde";

    protected static final String V_RUNNER_ID_2 = "abcdefghijklmnopqrst";

    protected static final String I_RUNNER_ID_1 = "abcdefghijklmnopqrstu";

    public HasRunnerIdBOTestCase(String name) {
        super(name);
    }

    public void testDefaultValues() {
        super.testDefaultValues();
        executeDefaultValuesTests(HasRunnerId.FIELD_RUNNER_ID, null);
    }

    public void testFields() {
        super.testFields();
        executeStringFieldTests(HasRunnerId.FIELD_RUNNER_ID);
    }

    public void testValidateValues() {
        super.testValidateValues();
        executeValidateValuesTests(HasRunnerId.FIELD_RUNNER_ID, new String[] { V_RUNNER_ID_1, V_RUNNER_ID_2 }, new String[] { null, ConstantValues.STRING_BLANK, I_RUNNER_ID_1 });
    }

    public void testGetValues() {
        super.testGetValues();
    }

    public void testGetValuesInternal() {
        super.testGetValuesInternal();
    }

    public void testSetValues() {
        super.testSetValues();
        executeSetValuesTests(HasRunnerId.FIELD_RUNNER_ID, new String[] { V_RUNNER_ID_1, V_RUNNER_ID_2 }, new String[] { null, ConstantValues.STRING_BLANK, I_RUNNER_ID_1 });
    }

    public void testSetValuesInternal() {
        super.testSetValuesInternal();
        executeSetValuesInternalTests(HasRunnerId.FIELD_RUNNER_ID, String.class, new String[] { null, ConstantValues.STRING_BLANK, V_RUNNER_ID_1, V_RUNNER_ID_2, I_RUNNER_ID_1 }, new String[] {});
    }

    public void testPrimaryKey() {
        super.testPrimaryKey();
    }
}
