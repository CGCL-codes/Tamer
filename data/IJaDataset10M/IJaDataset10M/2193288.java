package org.kablink.teaming.gwt.client.widgets;

import java.util.ArrayList;
import java.util.List;
import org.kablink.teaming.gwt.client.EditSuccessfulHandler;
import org.kablink.teaming.gwt.client.GwtDynamicGroupMembershipCriteria;
import org.kablink.teaming.gwt.client.GwtTeaming;
import org.kablink.teaming.gwt.client.GwtTeamingItem;
import org.kablink.teaming.gwt.client.GwtTeamingMessages;
import org.kablink.teaming.gwt.client.event.GroupCreatedEvent;
import org.kablink.teaming.gwt.client.event.GroupModifiedEvent;
import org.kablink.teaming.gwt.client.mainmenu.GroupInfo;
import org.kablink.teaming.gwt.client.rpc.shared.BooleanRpcResponseData;
import org.kablink.teaming.gwt.client.rpc.shared.CreateGroupCmd;
import org.kablink.teaming.gwt.client.rpc.shared.GetDynamicMembershipCriteriaCmd;
import org.kablink.teaming.gwt.client.rpc.shared.GetGroupMembershipCmd;
import org.kablink.teaming.gwt.client.rpc.shared.GetGroupMembershipRpcResponseData;
import org.kablink.teaming.gwt.client.rpc.shared.GetGroupMembershipTypeCmd;
import org.kablink.teaming.gwt.client.rpc.shared.GetIsDynamicGroupMembershipAllowedCmd;
import org.kablink.teaming.gwt.client.rpc.shared.GetNumberOfMembersCmd;
import org.kablink.teaming.gwt.client.rpc.shared.IntegerRpcResponseData;
import org.kablink.teaming.gwt.client.rpc.shared.ModifyGroupCmd;
import org.kablink.teaming.gwt.client.rpc.shared.VibeRpcResponse;
import org.kablink.teaming.gwt.client.util.GwtClientHelper;
import org.kablink.teaming.gwt.client.widgets.DlgBox;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * This dialog can be used to add a group or modify a group.
 * @author jwootton
 *
 */
public class ModifyGroupDlg extends DlgBox implements EditSuccessfulHandler {

    private GroupInfo m_groupInfo;

    private List<GwtTeamingItem> m_groupMembership;

    private InlineLabel m_nameLabel;

    private TextBox m_nameTxtBox;

    private TextBox m_titleTxtBox;

    private TextArea m_descTextArea;

    private RadioButton m_staticRb;

    private RadioButton m_dynamicRb;

    private ModifyStaticMembershipDlg m_staticMembershipDlg;

    private ModifyDynamicMembershipDlg m_dynamicMembershipDlg;

    private GwtDynamicGroupMembershipCriteria m_dynamicMembershipCriteria;

    private int m_numDynamicMembers;

    private boolean m_dynamicMembershipAllowed;

    /**
	 * 
	 */
    public ModifyGroupDlg(boolean autoHide, boolean modal, int xPos, int yPos) {
        super(autoHide, modal, xPos, yPos);
        createAllDlgContent("", this, null, null);
    }

    /**
	 * Create all the controls that make up the dialog box.
	 */
    public Panel createContent(Object props) {
        GwtTeamingMessages messages;
        FlowPanel mainPanel;
        FlexTable table;
        int nextRow;
        FlexCellFormatter cellFormatter;
        messages = GwtTeaming.getMessages();
        mainPanel = new FlowPanel();
        mainPanel.setStyleName("teamingDlgBoxContent");
        table = new FlexTable();
        table.setCellSpacing(4);
        table.addStyleName("dlgContent");
        cellFormatter = table.getFlexCellFormatter();
        nextRow = 0;
        {
            m_nameLabel = new InlineLabel(messages.modifyGroupDlgNameLabel());
            table.setWidget(nextRow, 0, m_nameLabel);
            m_nameTxtBox = new TextBox();
            m_nameTxtBox.setVisibleLength(30);
            table.setWidget(nextRow, 1, m_nameTxtBox);
            ++nextRow;
        }
        {
            table.setText(nextRow, 0, messages.modifyGroupDlgTitleLabel());
            m_titleTxtBox = new TextBox();
            m_titleTxtBox.setVisibleLength(30);
            table.setWidget(nextRow, 1, m_titleTxtBox);
            ++nextRow;
        }
        {
            table.setText(nextRow, 0, messages.modifyGroupDlgDescriptionLabel());
            ++nextRow;
            m_descTextArea = new TextArea();
            m_descTextArea.setCharacterWidth(50);
            m_descTextArea.setVisibleLines(5);
            table.setWidget(nextRow, 0, m_descTextArea);
            cellFormatter.setColSpan(nextRow, 0, 2);
            ++nextRow;
        }
        {
            FlexTable table2;
            Button btn;
            FlexCellFormatter cellFormatter2;
            ClickHandler clickHandler;
            table2 = new FlexTable();
            cellFormatter2 = table2.getFlexCellFormatter();
            m_staticRb = new RadioButton("membershipType", messages.modifyGroupDlgStaticLabel());
            table2.setWidget(0, 0, m_staticRb);
            m_dynamicRb = new RadioButton("membershipType", messages.modifyGroupDlgDynamicLabel());
            table2.setWidget(1, 0, m_dynamicRb);
            btn = new Button(messages.modifyGroupDlgEditGroupMembershipLabel());
            btn.addStyleName("teamingButton");
            btn.getElement().getStyle().setMarginLeft(10, Unit.PX);
            table2.setWidget(0, 1, btn);
            cellFormatter2.setRowSpan(0, 1, 2);
            clickHandler = new ClickHandler() {

                /**
				 * 
				 */
                public void onClick(ClickEvent event) {
                    Scheduler.ScheduledCommand cmd;
                    cmd = new Scheduler.ScheduledCommand() {

                        /**
						 * 
						 */
                        public void execute() {
                            invokeEditGroupMembershipDlg();
                        }
                    };
                    Scheduler.get().scheduleDeferred(cmd);
                }
            };
            btn.addClickHandler(clickHandler);
            table.setWidget(nextRow, 0, table2);
            cellFormatter.setColSpan(nextRow, 0, 2);
            ++nextRow;
        }
        mainPanel.add(table);
        return mainPanel;
    }

    /**
	 * Issue an rpc request to create a group.  If the rpc request is successful
	 * close this dialog.
	 */
    private void createGroupAndClose() {
        CreateGroupCmd cmd;
        AsyncCallback<VibeRpcResponse> rpcCallback;
        rpcCallback = new AsyncCallback<VibeRpcResponse>() {

            @Override
            public void onFailure(Throwable caught) {
                show();
                GwtClientHelper.handleGwtRPCFailure(caught, GwtTeaming.getMessages().rpcFailure_CreateGroup(), getGroupName());
                m_nameTxtBox.setFocus(true);
            }

            @Override
            public void onSuccess(VibeRpcResponse result) {
                GroupCreatedEvent event;
                GroupInfo groupInfo;
                groupInfo = (GroupInfo) result.getResponseData();
                event = new GroupCreatedEvent(groupInfo);
                GwtTeaming.fireEvent(event);
            }
        };
        cmd = new CreateGroupCmd(getGroupName(), getGroupTitle(), getGroupDesc(), getIsMembershipDynamic(), m_groupMembership, m_dynamicMembershipCriteria);
        GwtClientHelper.executeCommand(cmd, rpcCallback);
        hide();
    }

    /**
	 * This gets called when the user presses ok.  If we are editing an existing group
	 * we will issue an rpc request to save the group and then call m_editSuccessfulHandler.
	 * If we are creating a new group we will issue an rpc request to create the new group
	 * and then call m_editSuccessfulHandler.
	 */
    @Override
    public boolean editSuccessful(Object obj) {
        if (m_groupInfo != null) {
            modifyGroupAndClose();
        } else {
            if (isNameValid() == false) {
                m_nameTxtBox.setFocus(true);
                return false;
            }
            createGroupAndClose();
        }
        return false;
    }

    /**
	 * Get the data from the controls in the dialog box.
	 */
    public Object getDataFromDlg() {
        return Boolean.TRUE;
    }

    /**
	 * Issue an ajax request to get the ldap query of the group we are working with.
	 */
    private void getDynamicMembershipCriteria() {
        if (m_groupInfo != null) {
            GetDynamicMembershipCriteriaCmd cmd;
            AsyncCallback<VibeRpcResponse> rpcCallback;
            rpcCallback = new AsyncCallback<VibeRpcResponse>() {

                @Override
                public void onFailure(Throwable caught) {
                    GwtClientHelper.handleGwtRPCFailure(caught, GwtTeaming.getMessages().rpcFailure_GetGroupLdapQuery());
                }

                @Override
                public void onSuccess(VibeRpcResponse result) {
                    m_dynamicMembershipCriteria = ((GwtDynamicGroupMembershipCriteria) result.getResponseData());
                }
            };
            cmd = new GetDynamicMembershipCriteriaCmd(m_groupInfo.getId());
            GwtClientHelper.executeCommand(cmd, rpcCallback);
        }
    }

    /**
	 * Return the widget that should get the focus when the dialog is shown. 
	 */
    public FocusWidget getFocusWidget() {
        if (m_groupInfo == null) return m_nameTxtBox;
        return m_titleTxtBox;
    }

    /**
	 * Return the description entered by the user.
	 */
    private String getGroupDesc() {
        return m_descTextArea.getText();
    }

    /**
	 * Issue an ajax request to get the membership of the group we are working with.
	 */
    private void getGroupMembership() {
        if (m_groupInfo != null) {
            GetGroupMembershipCmd cmd;
            AsyncCallback<VibeRpcResponse> rpcCallback;
            rpcCallback = new AsyncCallback<VibeRpcResponse>() {

                @Override
                public void onFailure(Throwable caught) {
                    GwtClientHelper.handleGwtRPCFailure(caught, GwtTeaming.getMessages().rpcFailure_GetGroupMembership());
                }

                @Override
                public void onSuccess(VibeRpcResponse result) {
                    GetGroupMembershipRpcResponseData responseData;
                    responseData = ((GetGroupMembershipRpcResponseData) result.getResponseData());
                    m_groupMembership = responseData.getMembers();
                }
            };
            cmd = new GetGroupMembershipCmd(String.valueOf(m_groupInfo.getId()));
            GwtClientHelper.executeCommand(cmd, rpcCallback);
        }
    }

    /**
	 * Return the name entered by the user.
	 */
    private String getGroupName() {
        return m_nameTxtBox.getText();
    }

    /**
	 * Return the title entered by the user.
	 */
    private String getGroupTitle() {
        return m_titleTxtBox.getText();
    }

    /**
	 * Return whether membership is dynamic.
	 */
    private boolean getIsMembershipDynamic() {
        return m_dynamicRb.getValue();
    }

    /**
	 * Issue an ajax request to get the membership type (static or dynamic) for the group
	 * we are working with.
	 */
    private void getMembershipType() {
        if (m_groupInfo != null) {
            GetGroupMembershipTypeCmd cmd;
            AsyncCallback<VibeRpcResponse> rpcCallback;
            rpcCallback = new AsyncCallback<VibeRpcResponse>() {

                @Override
                public void onFailure(Throwable caught) {
                    GwtClientHelper.handleGwtRPCFailure(caught, GwtTeaming.getMessages().rpcFailure_GetGroupMembershipType());
                }

                @Override
                public void onSuccess(VibeRpcResponse result) {
                    BooleanRpcResponseData responseData;
                    responseData = ((BooleanRpcResponseData) result.getResponseData());
                    m_groupInfo.setIsMembershipDynamic(responseData.getBooleanValue());
                    if (m_groupInfo.getIsMembershipDynamic()) {
                        m_staticRb.setValue(false);
                        m_dynamicRb.setValue(true);
                        {
                            Scheduler.ScheduledCommand cmd;
                            cmd = new Scheduler.ScheduledCommand() {

                                public void execute() {
                                    getDynamicMembershipCriteria();
                                    getNumberOfMembers();
                                }
                            };
                            Scheduler.get().scheduleDeferred(cmd);
                        }
                    } else {
                        m_staticRb.setValue(true);
                        m_dynamicRb.setValue(false);
                        {
                            Scheduler.ScheduledCommand cmd;
                            cmd = new Scheduler.ScheduledCommand() {

                                public void execute() {
                                    getGroupMembership();
                                }
                            };
                            Scheduler.get().scheduleDeferred(cmd);
                        }
                    }
                }
            };
            cmd = new GetGroupMembershipTypeCmd(m_groupInfo.getId());
            GwtClientHelper.executeCommand(cmd, rpcCallback);
        }
    }

    /**
	 * Issue an ajax request to get the number of members in this group.
	 */
    private void getNumberOfMembers() {
        if (m_groupInfo != null) {
            GetNumberOfMembersCmd cmd;
            AsyncCallback<VibeRpcResponse> rpcCallback;
            rpcCallback = new AsyncCallback<VibeRpcResponse>() {

                @Override
                public void onFailure(Throwable caught) {
                    GwtClientHelper.handleGwtRPCFailure(caught, GwtTeaming.getMessages().rpcFailure_NumberOfMembers());
                }

                @Override
                public void onSuccess(VibeRpcResponse result) {
                    IntegerRpcResponseData responseData;
                    responseData = (IntegerRpcResponseData) result.getResponseData();
                    m_numDynamicMembers = responseData.getIntegerValue();
                }
            };
            cmd = new GetNumberOfMembersCmd(m_groupInfo.getId());
            GwtClientHelper.executeCommand(cmd, rpcCallback);
        }
    }

    /**
	 * 
	 */
    public void init(GroupInfo groupInfo) {
        m_groupInfo = groupInfo;
        m_groupMembership = new ArrayList<GwtTeamingItem>();
        m_dynamicMembershipCriteria = new GwtDynamicGroupMembershipCriteria();
        m_numDynamicMembers = 0;
        m_dynamicMembershipAllowed = true;
        isDynamicGroupMembershipAllowed();
        m_nameTxtBox.setText("");
        m_titleTxtBox.setText("");
        m_descTextArea.setText("");
        if (m_groupInfo != null) {
            {
                Scheduler.ScheduledCommand cmd;
                cmd = new Scheduler.ScheduledCommand() {

                    public void execute() {
                        getMembershipType();
                    }
                };
                Scheduler.get().scheduleDeferred(cmd);
            }
            setHeaderText(GwtTeaming.getMessages().modifyGroupDlgHeader(m_groupInfo.getTitle()));
            m_nameLabel.setVisible(false);
            m_nameTxtBox.setVisible(false);
            m_titleTxtBox.setText(groupInfo.getTitle());
            m_descTextArea.setText(groupInfo.getDesc());
        } else {
            setHeaderText(GwtTeaming.getMessages().addGroupDlgHeader());
            m_nameLabel.setVisible(true);
            m_nameTxtBox.setVisible(true);
            m_staticRb.setValue(true);
            m_dynamicRb.setValue(false);
        }
    }

    /**
	 * 
	 */
    public void invokeEditGroupMembershipDlg() {
        if (getIsMembershipDynamic() == false) {
            int x;
            int y;
            x = getAbsoluteLeft() + 50;
            y = getAbsoluteTop() + 50;
            if (m_staticMembershipDlg == null) {
                EditSuccessfulHandler handler;
                handler = new EditSuccessfulHandler() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean editSuccessful(Object obj) {
                        m_groupMembership = (List<GwtTeamingItem>) obj;
                        return true;
                    }
                };
                m_staticMembershipDlg = new ModifyStaticMembershipDlg(false, true, handler, null, x, y);
            }
            m_staticMembershipDlg.init(getGroupName(), m_groupMembership);
            m_staticMembershipDlg.setPopupPosition(x, y);
            m_staticMembershipDlg.show();
        } else {
            int x;
            int y;
            if (m_dynamicMembershipAllowed == false) {
                Window.alert(GwtTeaming.getMessages().modifyGroupDlgDynamicGroupMembershipNotAllowed());
            }
            x = getAbsoluteLeft() + 50;
            y = getAbsoluteTop() + 50;
            if (m_dynamicMembershipDlg == null) {
                EditSuccessfulHandler handler;
                handler = new EditSuccessfulHandler() {

                    @Override
                    public boolean editSuccessful(Object obj) {
                        if (obj instanceof GwtDynamicGroupMembershipCriteria) m_dynamicMembershipCriteria = (GwtDynamicGroupMembershipCriteria) obj;
                        return true;
                    }
                };
                m_dynamicMembershipDlg = new ModifyDynamicMembershipDlg(false, true, handler, null, x, y);
            }
            m_dynamicMembershipDlg.init(m_dynamicMembershipCriteria, m_numDynamicMembers);
            m_dynamicMembershipDlg.setPopupPosition(x, y);
            m_dynamicMembershipDlg.show();
        }
    }

    /**
	 * Issue an rpc request to see if dynamic group membership is allowed.
	 */
    private void isDynamicGroupMembershipAllowed() {
        GetIsDynamicGroupMembershipAllowedCmd cmd;
        AsyncCallback<VibeRpcResponse> rpcCallback;
        rpcCallback = new AsyncCallback<VibeRpcResponse>() {

            @Override
            public void onFailure(Throwable caught) {
                GwtClientHelper.handleGwtRPCFailure(caught, GwtTeaming.getMessages().rpcFailure_GetIsDynamicGroupMembershipAllowed());
            }

            @Override
            public void onSuccess(VibeRpcResponse result) {
                BooleanRpcResponseData responseData;
                responseData = (BooleanRpcResponseData) result.getResponseData();
                m_dynamicMembershipAllowed = responseData.getBooleanValue();
            }
        };
        cmd = new GetIsDynamicGroupMembershipAllowedCmd();
        GwtClientHelper.executeCommand(cmd, rpcCallback);
    }

    /**
	 * Is the name entered by the user valid?
	 */
    private boolean isNameValid() {
        String value;
        value = m_nameTxtBox.getValue();
        if (value == null || value.length() == 0) {
            Window.alert(GwtTeaming.getMessages().modifyGroupDlgNameRequired());
            return false;
        }
        if (value.length() > 128) {
            Window.alert(GwtTeaming.getMessages().modifyGroupDlgNameTooLong());
            return false;
        }
        return true;
    }

    /**
	 * Issue an rpc request to modify the group.  If the rpc request was successful
	 * close this dialog.
	 */
    private void modifyGroupAndClose() {
        ModifyGroupCmd cmd;
        AsyncCallback<VibeRpcResponse> rpcCallback;
        rpcCallback = new AsyncCallback<VibeRpcResponse>() {

            @Override
            public void onFailure(Throwable caught) {
                show();
                GwtClientHelper.handleGwtRPCFailure(caught, GwtTeaming.getMessages().rpcFailure_ModifyGroup());
            }

            @Override
            public void onSuccess(VibeRpcResponse result) {
                GroupModifiedEvent event;
                m_groupInfo.setTitle(getGroupTitle());
                m_groupInfo.setDesc(getGroupDesc());
                event = new GroupModifiedEvent(m_groupInfo);
                GwtTeaming.fireEvent(event);
            }
        };
        cmd = new ModifyGroupCmd(m_groupInfo.getId(), getGroupTitle(), getGroupDesc(), getIsMembershipDynamic(), m_groupMembership, m_dynamicMembershipCriteria);
        GwtClientHelper.executeCommand(cmd, rpcCallback);
        hide();
    }
}
