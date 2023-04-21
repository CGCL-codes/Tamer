package com.qcs.eduquill.web;

import com.qcs.eduquill.sl.bprocess.Organization;
import com.qcs.eduquill.sl.bprocess.OrganizationMaster;
import com.qcs.eduquill.utilities.EQUserSession;
import com.qcs.eduquill.utilities.EduQuillBeanFactory;
import com.qcs.eduquill.utilities.InformationMessages;
import com.qcs.eduquill.utilities.XMLUtility;
import com.qcs.eduquill.utilities.ZKUtility;
import java.sql.Timestamp;
import java.util.Hashtable;
import org.jdom.Element;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

/**
 *
 * @version      1.0 19 Jun 2009
 * @author       Sreekanthreddy Y
 */
public class EduQuillLoginComposer extends Window {

    public EduQuillLoginComposer() {
        ZKUtility.getInstance().getHTTPRequest().getSession().setAttribute("parentWindow", this);
        if (!validateUser()) {
            initComponents();
            ZKUtility.getInstance().setBrowserTitle(Labels.getLabel("label.EduquillLogin"));
        } else {
            EduQuillHomeComposer composer = new EduQuillHomeComposer();
            composer.setParent(this);
        }
    }

    private boolean validateUser() {
        EQUserSession userSession = new EQUserSession();
        String userId = userSession.getUserId();
        Long organizationId = userSession.getorganizationId();
        if (userId.length() > 0 && organizationId != null) {
            return true;
        }
        return false;
    }

    private void initComponents() {
        setWidth("100%");
        Vbox vbox = new Vbox();
        vbox.setParent(this);
        vbox.setWidth("100%");
        Groupbox groupHeader = new Groupbox();
        groupHeader.setParent(vbox);
        Image logo = new Image("/images/header_banner_2 copy3.png");
        logo.setWidth("100%");
        logo.setHeight("90px");
        Label lblHeader = new Label("Header");
        logo.setParent(groupHeader);
        lblHeader.setStyle("font-size:32px");
        Hbox hbox = new Hbox();
        hbox.setParent(vbox);
        hbox.setWidth("100%");
        hbox.setWidths("75%,25%");
        Groupbox groupboxDesc = new Groupbox();
        groupboxDesc.setParent(hbox);
        Label lbl = new Label("Pending...");
        lbl.setParent(groupboxDesc);
        lbl.setStyle("font-size:32px");
        ZKUtility.getInstance().align(buildUserBox(), "right").setParent(hbox);
        Image Qlogo = new Image("/images/header_banner_2 copy3_1.jpg");
        Qlogo.setWidth("20%");
        Qlogo.setHeight("400px");
        Qlogo.setParent(vbox);
    }

    private Component buildUserBox() {
        Groupbox groupbox = new Groupbox();
        Vbox vbox = new Vbox();
        vbox.setParent(groupbox);
        Hbox hboxUser = new Hbox();
        hboxUser.setParent(vbox);
        ZKUtility.getInstance().align(hboxUser, "right");
        Label lblUser = new Label(Labels.getLabel("label.UserId"));
        lblUser.setParent(hboxUser);
        ZKUtility.getInstance().applyMandatory(lblUser);
        final Textbox textboxUser = new Textbox();
        textboxUser.setParent(hboxUser);
        Hbox hboxPW = new Hbox();
        hboxPW.setParent(vbox);
        ZKUtility.getInstance().align(hboxPW, "right");
        Label lblPW = new Label(Labels.getLabel("label.Password"));
        lblPW.setParent(hboxPW);
        ZKUtility.getInstance().applyMandatory(lblPW);
        final InformationMessages messageBox = new InformationMessages(vbox);
        messageBox.setId("messageBox");
        final Textbox textboxPW = new Textbox();
        textboxPW.setParent(hboxPW);
        textboxPW.setType("password");
        textboxPW.addEventListener("onOK", new EventListener() {

            @Override
            public void onEvent(Event arg0) throws Exception {
                if (authenticate(1, textboxUser.getValue(), textboxPW.getValue())) {
                    messageBox.success("Authentication success");
                }
            }
        });
        Hbox hboxBns = new Hbox();
        ZKUtility.getInstance().align(hboxBns, "right").setParent(vbox);
        Button bnLogin = new Button(Labels.getLabel("label.Login"));
        bnLogin.setParent(hboxBns);
        bnLogin.addEventListener("onClick", new EventListener() {

            @Override
            public void onEvent(Event arg0) throws Exception {
                if (authenticate(1, textboxUser.getValue(), textboxPW.getValue())) {
                    messageBox.success("Authentication success");
                }
            }
        });
        return groupbox;
    }

    private InformationMessages getMessageBox() {
        return (InformationMessages) getFellow("messageBox");
    }

    private boolean authenticate(long organizationId, String userId, String password) {
        if (userId.trim().length() > 0) {
            OrganizationMaster organizationMaster = (OrganizationMaster) com.qcs.eduquill.utilities.EduQuillBeanFactory.getInstance().getBean("organizationMaster");
            int resp = organizationMaster.bm_authenticateOrganization("Test", organizationId, userId, password);
            if (resp == organizationMaster.STATUS_SUCCESS) {
                ZKUtility.getInstance().getHTTPRequest().getSession().setAttribute("EQUserSession", generateUserSessionXML("Test", organizationId, userId));
                getChildren().clear();
                EduQuillHomeComposer composer = new EduQuillHomeComposer();
                composer.setParent(this);
                return true;
            } else {
                getMessageBox().failed("Authentication failed");
            }
        } else {
            getMessageBox().warning("UserId can not be empty");
        }
        return false;
    }

    private String generateUserSessionXML(String databaseId, long organizationId, String userId) {
        Element root = new Element("EQUserSession");
        Element eleUserId = new Element("UserId");
        root.addContent(eleUserId);
        eleUserId.setText(userId);
        Element eleLibraryId = new Element("OrganizationId");
        root.addContent(eleLibraryId);
        eleLibraryId.setText(String.valueOf(organizationId));
        Element eleDatabaseId = new Element("DatabaseId");
        root.addContent(eleDatabaseId);
        eleDatabaseId.setText(databaseId);
        Element eleLoggedInAt = new Element("LoggedInAt");
        root.addContent(eleLoggedInAt);
        eleLoggedInAt.setText(String.valueOf(System.currentTimeMillis()));
        Organization organization = (Organization) EduQuillBeanFactory.getInstance().getBean("organization");
        Hashtable ht = organization.bm_getOrganization(databaseId, organizationId);
        Timestamp timezone = (Timestamp) ht.get("Timezone");
        long time = 0;
        if (timezone != null) {
            time = timezone.getTime();
        }
        Element eleTimezone = new Element("Timezone");
        root.addContent(eleTimezone);
        eleTimezone.setText(String.valueOf(time));
        String resp = XMLUtility.getInstance().generateXML(root);
        return resp;
    }
}
