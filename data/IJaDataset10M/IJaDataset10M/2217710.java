package net.sf.brightside.dentalwizard.tapestry.util;

import java.util.Map;
import net.sf.brightside.dentalwizard.metamodel.StaffMember;
import org.apache.tapestry5.OptionModel;

public class StaffMemberOptionModel implements OptionModel {

    private StaffMember staffMember;

    public StaffMemberOptionModel(StaffMember staffMember) {
        this.staffMember = staffMember;
    }

    public String getLabel() {
        return staffMember.getFirstName() + " " + staffMember.getLastName();
    }

    public boolean isDisabled() {
        return false;
    }

    public Map<String, String> getAttributes() {
        return null;
    }

    public Object getValue() {
        return staffMember;
    }
}
