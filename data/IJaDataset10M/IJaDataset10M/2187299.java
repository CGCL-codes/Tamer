package org.hip.vif.admin.member.ui;

import org.hip.vif.admin.member.Activator;
import org.hip.vif.admin.member.data.RoleContainer;
import org.hip.vif.admin.member.data.RoleWrapper;
import org.hip.vif.admin.member.tasks.MemberShowTask;
import org.hip.vif.core.bom.Member;
import org.hip.vif.core.bom.MemberHome;
import org.hip.vif.core.interfaces.IMessages;
import org.hip.vif.core.util.BeanWrapperHelper;
import org.hip.vif.core.util.RatingsHelper;
import org.hip.vif.web.components.LabelValueTable;
import org.hip.vif.web.util.MemberViewHelper;
import org.hip.vif.web.util.RatingsTable;
import org.hip.vif.web.util.VIFViewHelper;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

/**
 * View to display the member's data.
 * 
 * @author Luthiger
 * Created: 26.10.2011
 */
@SuppressWarnings("serial")
public class MemberView extends AbstractMemberView {

    /**
	 * Constructor for the view to display the member's data, e.g. in a lookup window.
	 * 
	 * @param inMember {@link Member}
	 * @param inRoles {@link RoleContainer}
	 * @param inRatings {@link RatingsHelper}
	 */
    public MemberView(final Member inMember, final RoleContainer inRoles, RatingsHelper inRatings) {
        final IMessages lMessages = Activator.getMessages();
        VerticalLayout lLayout = initLayout(lMessages);
        LabelValueTable lTable = displayMember(inMember, lMessages);
        lTable.addRow(lMessages.getMessage("ui.member.editor.label.role"), displayRoles(inRoles));
        lLayout.addComponent(lTable);
        lLayout.addComponent(VIFViewHelper.createSpacer());
        lLayout.addComponent(VIFViewHelper.createSpacer());
        lLayout.addComponent(new RatingsTable(inRatings));
    }

    private Label displayRoles(RoleContainer inRoles) {
        StringBuilder lRoles = new StringBuilder();
        boolean lFirst = true;
        for (RoleWrapper lRole : inRoles.getSelected()) {
            if (!lFirst) {
                lRoles.append("<br />");
            }
            lFirst = false;
            lRoles.append(lRole.getLabel());
        }
        return new Label(String.format(LabelValueTable.STYLE_PLAIN, new String(lRoles)), Label.CONTENT_XHTML);
    }

    /**
	 * Constructor for the view to edit the member's role.
	 * 
	 * @param inMember {@link Member}
	 * @param inRoles {@link RoleContainer}
	 * @param inRatings {@link RatingsHelper}
	 * @param inTask {@link MemberShowTask}
	 */
    public MemberView(final Member inMember, final RoleContainer inRoles, RatingsHelper inRatings, final MemberShowTask inTask) {
        final IMessages lMessages = Activator.getMessages();
        VerticalLayout lLayout = initLayout(lMessages, "ui.member.edit.title.page");
        Button lSave = createSaveButton(lMessages);
        VerticalLayout lMemberLayout = new VerticalLayout();
        lMemberLayout.setStyleName("vif-view-member");
        lLayout.addComponent(lMemberLayout);
        LabelValueTable lTable = displayMember(inMember, lMessages);
        lMemberLayout.addComponent(lTable);
        OptionGroup lRoles = createRolesOptions(inRoles);
        lTable.addRowEmphasized(lMessages.getMessage("ui.member.editor.label.role"), lRoles);
        lLayout.addComponent(VIFViewHelper.createSpacer());
        lLayout.addComponent(lSave);
        lSave.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent inEvent) {
                if (!inRoles.hasChecked()) {
                    getWindow().showNotification(lMessages.getMessage("errmsg.member.role.empty"), Notification.TYPE_ERROR_MESSAGE);
                } else {
                    if (!inTask.saveRoles(inMember, inRoles)) {
                        getWindow().showNotification(lMessages.getMessage("errmsg.save.general"), Notification.TYPE_WARNING_MESSAGE);
                    }
                }
            }
        });
        lLayout.addComponent(VIFViewHelper.createSpacer());
        lLayout.addComponent(VIFViewHelper.createSpacer());
        lLayout.addComponent(new RatingsTable(inRatings));
    }

    /**
	 * @param inMember
	 * @param inMessages
	 * @return
	 */
    private LabelValueTable displayMember(final Member inMember, final IMessages inMessages) {
        LabelValueTable lTable = new LabelValueTable();
        lTable.addRow(inMessages.getMessage("ui.member.editor.label.address"), MemberViewHelper.getMemberAddressLabel(inMember));
        lTable.addRow(inMessages.getMessage("ui.member.editor.label.firstname"), BeanWrapperHelper.getString(MemberHome.KEY_FIRSTNAME, inMember));
        lTable.addRow(inMessages.getMessage("ui.member.editor.label.name"), BeanWrapperHelper.getString(MemberHome.KEY_NAME, inMember));
        lTable.addRow(inMessages.getMessage("ui.member.editor.label.street"), BeanWrapperHelper.getString(MemberHome.KEY_STREET, inMember));
        lTable.addRow(inMessages.getMessage("ui.member.editor.label.city"), createZipCityLabels(inMember));
        lTable.addRow(inMessages.getMessage("ui.member.editor.label.phone"), BeanWrapperHelper.getString(MemberHome.KEY_PHONE, inMember));
        lTable.addRow(inMessages.getMessage("ui.member.editor.label.fax"), BeanWrapperHelper.getString(MemberHome.KEY_FAX, inMember));
        lTable.addRow(inMessages.getMessage("ui.member.editor.label.mail"), BeanWrapperHelper.getString(MemberHome.KEY_MAIL, inMember));
        return lTable;
    }

    private Component createZipCityLabels(Member inMember) {
        HorizontalLayout outLayout = new HorizontalLayout();
        outLayout.setStyleName("vif-value");
        outLayout.addComponent(new Label(BeanWrapperHelper.getString(MemberHome.KEY_ZIP, inMember)));
        outLayout.addComponent(new Label("&#160;", Label.CONTENT_XHTML));
        outLayout.addComponent(new Label(BeanWrapperHelper.getString(MemberHome.KEY_CITY, inMember)));
        return outLayout;
    }
}
