package org.hip.vif.admin.groupedit.data;

import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.vif.core.bom.MemberHome;
import org.hip.vif.core.bom.impl.NestedParticipantsOfGroupHome;
import org.hip.vif.core.member.MemberBean;
import org.hip.vif.core.util.BeanWrapperHelper;

/**
 * Adapter class for participant models, i.e. instances of <code>NestedParticipantsOfGroup</code>.
 * 
 * @author Luthiger
 * Created: 18.11.2011
 */
public class ParticipantBean extends MemberBean {

    private String zip;

    private String place;

    private Integer groupAdminID;

    private Integer suspendFrom;

    private Integer suspendTo;

    private ParticipantBean(GeneralDomainObject inDomainObject) {
        super(inDomainObject);
        zip = BeanWrapperHelper.getString(MemberHome.KEY_ZIP, inDomainObject);
        place = BeanWrapperHelper.getString(MemberHome.KEY_CITY, inDomainObject);
        groupAdminID = BeanWrapperHelper.getInteger(NestedParticipantsOfGroupHome.KEY_GROUPADMIN, inDomainObject);
        suspendFrom = BeanWrapperHelper.getInteger(NestedParticipantsOfGroupHome.KEY_SUSPENDED_TEST1, inDomainObject);
        suspendTo = BeanWrapperHelper.getInteger(NestedParticipantsOfGroupHome.KEY_SUSPENDED_TEST2, inDomainObject);
    }

    /**
	 * Factory method.
	 * 
	 * @param inParticipant {@link GeneralDomainObject}
	 * @return {@link ParticipantBean}
	 */
    public static ParticipantBean createItem(GeneralDomainObject inParticipant) {
        return new ParticipantBean(inParticipant);
    }

    public String getPlace() {
        return String.format("%s %s", zip, place);
    }

    public String getIsAdmin() {
        return groupAdminID > 0 ? "+" : "-";
    }

    public boolean isAdmin() {
        return groupAdminID > 0;
    }

    public String getIsActive() {
        return suspendFrom + suspendTo < 2 ? "+" : "-";
    }
}
