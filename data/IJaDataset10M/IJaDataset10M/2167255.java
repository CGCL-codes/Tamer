package unit.rcp.editor;

import uk.co.q3c.deplan.util.useraccess.UserAccessController.AccessLevel;
import uk.co.q3c.deplan.util.useraccess.rules.UserAccessPropertyRule;

public class TestAccessRule implements UserAccessPropertyRule {

    @Override
    public AccessLevel accessLevel() {
        return AutoEditor_UT.accessLevel;
    }
}
