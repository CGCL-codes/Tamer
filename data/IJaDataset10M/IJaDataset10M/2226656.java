package net.sf.brightside.dentalwizard.tapestry.pages;

import net.sf.brightside.dentalwizard.core.exception.BusinessException;
import net.sf.brightside.dentalwizard.enums.AccountRole;
import net.sf.brightside.dentalwizard.enums.StaffType;
import net.sf.brightside.dentalwizard.metamodel.StaffMember;
import net.sf.brightside.dentalwizard.metamodel.User;
import net.sf.brightside.dentalwizard.service.office.CreateStaffMember;
import net.sf.brightside.dentalwizard.tapestry.pages.office.Login;
import net.sf.brightside.dentalwizard.tapestry.util.DateUtil;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.EnumSelectModel;
import org.springframework.web.context.WebApplicationContext;

public class NewStaffMember {

    private String blank = " ";

    public String getBlank() {
        return blank;
    }

    @ApplicationState(create = false)
    private User authenticatedUser;

    @SuppressWarnings("unused")
    private boolean authenticatedUserExists;

    @InjectPage
    private Info info;

    @Inject
    private WebApplicationContext webAppContext;

    private StaffMember staffMember;

    public StaffMember getStaffMember() {
        return staffMember;
    }

    public void setStaffMember(StaffMember staffMember) {
        this.staffMember = staffMember;
    }

    @Component
    private Form addNewStaffMember;

    @Inject
    private CreateStaffMember createStaffMember;

    @InjectPage
    private StaffMembers staffMembers;

    public Object onActivate() {
        staffMember = (StaffMember) webAppContext.getBean(StaffMember.class.getName());
        if (authenticatedUserExists && !authenticatedUser.getRole().equals(AccountRole.ADMIN)) {
            info.setMessage("You are not authorized for this section.");
            return info;
        }
        if (!authenticatedUserExists) return Login.class;
        return null;
    }

    @Persist
    private boolean ok;

    void onValidateFormFromAddNewStaffMember() {
        ok = true;
        if (staffMember.getGender() == null || staffMember.getGender().equals("")) {
            addNewStaffMember.recordError("Gender must be specified.");
            ok = false;
        }
        if (staffMember.getDateOfBirth() == null) {
            addNewStaffMember.recordError("Wrong date of Birth format");
            ok = false;
        } else {
            if (staffMember.getDateOfBirth().compareTo(DateUtil.getToday()) > 0) {
                addNewStaffMember.recordError("Date of birth can not be in the future.");
                ok = false;
            }
        }
        if (staffMember.getEmploymentDate() == null) {
            addNewStaffMember.recordError("Wrong employment date format");
            ok = false;
        } else {
            if (staffMember.getEmploymentDate().compareTo(DateUtil.getToday()) > 0) {
                addNewStaffMember.recordError("Employment date can not be in the future.");
                ok = false;
            }
        }
    }

    @OnEvent(component = "addNewStaffMember", value = "submit")
    public Object onSubmitFromFormAddNewStaffMember() {
        if (!ok) {
            return this;
        }
        if (staffMember.getAddress().getCountry() == null) {
            staffMember.getAddress().setCountry("-");
        }
        try {
            createStaffMember.createStaffMember(staffMember);
        } catch (BusinessException e) {
            addNewStaffMember.recordError(e.getMessage());
            ok = false;
        }
        if (!ok) return this;
        return staffMembers;
    }

    @Inject
    private Messages messages;

    public SelectModel getTypeModel() {
        return new EnumSelectModel(StaffType.class, messages);
    }
}
