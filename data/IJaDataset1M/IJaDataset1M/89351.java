package org.hip.vif.member.views;

import java.util.Collection;
import org.hip.kernel.code.AbstractCode;
import org.hip.kernel.code.CodeList;
import org.hip.kernel.code.CodeListHome;
import org.hip.kernel.code.CodeListNotFoundException;
import org.hip.kernel.exc.VException;
import org.hip.kernel.servlet.Context;
import org.hip.kernel.util.XMLRepresentation;
import org.hip.vif.bom.Member;
import org.hip.vif.code.Role;
import org.hip.vif.member.Activator;
import org.hip.vif.member.IAuthenticator;
import org.hip.vif.registry.PlatformHelper;
import org.hip.vif.servlets.AbstractVIFView;
import org.hip.vif.servlets.VIFContext;
import org.osgi.framework.Bundle;

/**
 * View to enter the data for a new member
 * 
 * @author: Benno Luthiger
 */
public class MemberView extends AbstractVIFView {

    private static final String XSL_FILE_NEW = "bodyNew.xsl";

    private static final String XSL_FILE_EDIT = "bodyEdit.xsl";

    private static final String TAG_ROLES_LIST_BEGIN = "<RolesList>";

    private static final String TAG_ROLES_LIST_END = "</RolesList>";

    private boolean newMember;

    /**
	 * MemberView constructor with given Context
	 *
	 * @param inContext org.hip.kernel.servlet.Context
	 */
    public MemberView(Context inContext) {
        super(inContext);
        String lXML = VIFContext.HEADER + VIFContext.ROOT_BEGIN + VIFContext.ROOT_END;
        prepareTransformation(new XMLRepresentation(lXML));
    }

    /**
	 * MemberView constructor with form content to display
	 *
	 * @param inContext org.hip.kernel.servlet.Context
	 * @param inUserID java.lang.String
	 * @param inName java.lang.String
	 * @param inFirstName java.lang.String
	 * @param inStreet java.lang.String
	 * @param inZIP java.lang.String
	 * @param inCity java.lang.String
	 * @param inTel java.lang.String
	 * @param inFax java.lang.String
	 * @param inMail java.lang.String
	 * @param inSex java.lang.String
	 * @param inFromPage java.lang.String The page/context the task is called from (init or memberList)
	 * @param inNew boolean True, if form to display is for a new member
	 */
    public MemberView(Context inContext, String inUserID, String inName, String inFirstName, String inStreet, String inZIP, String inCity, String inTel, String inFax, String inMail, String inSex, String inFromPage, boolean inNew) throws VException {
        super(inContext);
        newMember = inNew;
        setRequestTypeToStylesheet();
        setDisabledParameter(newMember ? 2 : 1);
        prepareTransformation(new XMLRepresentation(createXML(inUserID, inName, inFirstName, inStreet, inZIP, inCity, inTel, inFax, inMail, inSex, inFromPage, inContext, inNew)));
    }

    /**
	 * MemberView constructor with member object to display
	 * 
	 * @param inContext org.hip.kernel.servlet.Context
	 * @param inMember org.hip.vif.bom.Member
	 * @param inRoles List<Role> containing the roles associated with the specified member
	 * @param inNew boolean True, if form to display is for a new member
	 * @throws org.hip.kernel.code.CodeListNotFoundException
	 */
    public MemberView(Context inContext, Member inMember, Collection<? extends AbstractCode> inRoles, boolean inNew) throws VException {
        super(inContext);
        newMember = inNew;
        setRequestTypeToStylesheet();
        setDisabledParameter(1);
        prepareTransformation(new XMLRepresentation(createXML(inMember, inContext, inRoles, inNew)));
    }

    private void setRequestTypeToStylesheet() {
        String lPrefix = Activator.getBundleName();
        setStylesheetParameter("requestType", lPrefix + (newMember ? ".saveMemberNew" : ".saveMember"));
    }

    private void setDisabledParameter(int inDisableMode) throws VException {
        IAuthenticator lAuthenticator = PlatformHelper.getInstance().getAuthenticatorHelper().getActiveAuthenticator().getAuthenticator();
        setStylesheetParameter("disable", lAuthenticator.isExternal() ? inDisableMode : 0);
    }

    private String createXML(String inUserID, String inName, String inFirstName, String inStreet, String inZIP, String inCity, String inTel, String inFax, String inMail, String inSex, String inFromPage, Context inContext, boolean inNew) throws CodeListNotFoundException {
        StringBuffer outXML = new StringBuffer(VIFContext.HEADER);
        outXML.append(VIFContext.ROOT_BEGIN);
        outXML.append("<Member><propertySet>");
        outXML.append("<Name>" + inName + "</Name>");
        outXML.append("<Firstname>" + inFirstName + "</Firstname>");
        outXML.append("<Street>" + inStreet + "</Street>");
        outXML.append("<ZIP>" + inZIP + "</ZIP>");
        outXML.append("<City>" + inCity + "</City>");
        outXML.append("<Tel>" + inTel + "</Tel>");
        outXML.append("<Fax>" + inFax + "</Fax>");
        outXML.append("<Mail>" + inMail + "</Mail>");
        outXML.append("<Sex>" + inSex + "</Sex>");
        outXML.append("</propertySet></Member>");
        outXML.append("<FromPage>" + inFromPage + "</FromPage>");
        CodeList lList = CodeListHome.instance().getCodeList(Role.class, inContext.getLanguage());
        outXML.append(TAG_ROLES_LIST_BEGIN);
        AbstractCode[] lEmpty = {};
        outXML.append(lList.toSelectionString(lEmpty));
        outXML.append(TAG_ROLES_LIST_END);
        outXML.append(VIFContext.ROOT_END);
        return new String(outXML);
    }

    private String createXML(Member inMember, Context inContext, Collection<? extends AbstractCode> inRoles, boolean inNew) throws CodeListNotFoundException {
        StringBuffer outXML = new StringBuffer(VIFContext.HEADER);
        outXML.append(VIFContext.ROOT_BEGIN);
        outXML.append(getSerialized(inMember));
        outXML.append("<FromPage/>");
        CodeList lList = CodeListHome.instance().getCodeList(Role.class, inContext.getLanguage());
        outXML.append(TAG_ROLES_LIST_BEGIN);
        if (inNew) {
            AbstractCode[] lEmpty = {};
            outXML.append(lList.toSelectionString(lEmpty));
        } else {
            outXML.append(lList.toSelectionString(inRoles));
        }
        outXML.append(TAG_ROLES_LIST_END);
        outXML.append(VIFContext.ROOT_END);
        return new String(outXML);
    }

    protected String getXMLName() {
        return newMember ? XSL_FILE_NEW : XSL_FILE_EDIT;
    }

    @Override
    protected Bundle getBundle() {
        return Activator.getContext().getBundle();
    }
}
