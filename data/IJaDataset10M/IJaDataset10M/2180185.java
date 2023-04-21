package userInterface.Messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import utils.Constant;
import utils.MessageConstant;
import beans.MessageBean;
import beans.NewsEventsBean;
import beans.UserDetailsBean;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.data.Property.ValueChangeEvent;
import com.itmill.toolkit.service.ApplicationContext;
import com.itmill.toolkit.terminal.gwt.server.WebApplicationContext;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.CustomComponent;
import com.itmill.toolkit.ui.CustomLayout;
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkit.ui.Panel;
import com.itmill.toolkit.ui.RichTextArea;
import com.itmill.toolkit.ui.Select;
import com.itmill.toolkit.ui.TextField;
import com.itmill.toolkit.ui.Window;
import com.itmill.toolkit.ui.AbstractSelect.Filtering;
import com.itmill.toolkit.ui.Button.ClickEvent;
import database.MessageControl;
import database.UserControl;

/**
 * @author Noel-Admin
 *
 */
public class ComposeMessage extends CustomComponent implements Button.ClickListener, MessageConstant, Constant, Property.ValueChangeListener {

    public Window main;

    public CustomLayout messageBoxLayout;

    public CustomLayout masterPageLayout;

    private Panel messageControlPanel = new Panel("My Message Box");

    private Panel functionDisplayPanel = new Panel();

    public ApplicationContext ctx;

    public WebApplicationContext webCtx;

    public HttpSession session;

    public String username;

    private String callerIs;

    private CustomLayout composeMessageLayout;

    private Select selListSendTo = new Select();

    private Select selType = new Select();

    private Select selTargetAudience = new Select();

    private Select selSendTo = new Select();

    private TextField tfSubject = new TextField();

    private RichTextArea tfMessage = new RichTextArea();

    private Label lbSendTo = new Label("Send To: ");

    private Label lbType = new Label("Message Type: ");

    private Label lbSubject = new Label("Subject: ");

    private Label lbBulltinTile = new Label("<b><font size=\"2\" color=\"#009999\">The Bulletin Board will post messages to " + "all of your friends</font></b>", Label.CONTENT_XHTML);

    private Label lbTargetAudience = new Label("Target audience: ");

    private Button btSend = new Button("Send", this);

    private Button btCancel = new Button("Cancel", this);

    private Button btSelectRecipien = new Button("Mailing list", this);

    private MessageBean message = new MessageBean();

    private String recipientType = "Member";

    NewsEventsBean newEventsBean;

    public ComposeMessage(String type, String sendTo, String subject, Window mainWindow, CustomLayout msgboxlayout, String calledFrom, CustomLayout mainLayout) {
        main = mainWindow;
        messageBoxLayout = msgboxlayout;
        masterPageLayout = mainLayout;
        callerIs = calledFrom;
        ctx = main.getApplication().getContext();
        webCtx = (WebApplicationContext) ctx;
        session = webCtx.getHttpSession();
        username = (String) session.getAttribute("UserName");
        newEventsBean = new NewsEventsBean();
        btSelectRecipien.setStyleName(Button.STYLE_LINK);
        selTargetAudience.addItem("Members Only");
        selTargetAudience.addItem("Public");
        composeMessageLayout = new CustomLayout("msgComposeLayout");
        composeMessageLayout.addComponent(lbSendTo, "lbSendTo");
        composeMessageLayout.addComponent(lbType, "lbType");
        composeMessageLayout.addComponent(lbSubject, "lbSubject");
        if (type.equals(MSG_TYPE_BULLETIN)) {
            componentsForBulletin();
        } else if (type.equals(MSG_TYPE_MESSAGE)) {
            componentsForMessage();
        } else if (type.equals(MSG_TYPE_INVITATION)) {
            componentsForInvitation();
        }
        composeMessageLayout.addComponent(selType, "selType");
        composeMessageLayout.addComponent(tfSubject, "tfSubject");
        composeMessageLayout.addComponent(tfMessage, "tfMessage");
        composeMessageLayout.addComponent(btSend, "btSend");
        composeMessageLayout.addComponent(btCancel, "btCancel");
        functionDisplayPanel.addStyleName("msgPanelsStyle");
        Vector<UserDetailsBean> allMembers = UserControl.getAllActiveMembers();
        for (UserDetailsBean uName : allMembers) {
            selSendTo.addItem(uName.getUserName());
        }
        selSendTo.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
        tfSubject.setColumns(30);
        selSendTo.setValue(sendTo);
        selSendTo.setImmediate(true);
        selSendTo.addListener(this);
        selSendTo.setNullSelectionItemId("- Select member");
        if (subject.length() > 0) tfSubject.setValue("Re: " + subject); else tfSubject.setValue(subject);
        selListSendTo.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
        selListSendTo.setImmediate(true);
        selListSendTo.addListener(this);
        selListSendTo.setNullSelectionItemId("- Select mail list");
        selType.addItem("Message");
        selType.addItem("Invitation");
        selType.addItem("Bulletin");
        selType.select(type);
        selType.setImmediate(true);
        selType.addListener(this);
        selType.setNullSelectionItemId("- Message type");
        functionDisplayPanel.setStyleName(Panel.STYLE_LIGHT);
        functionDisplayPanel.setCaption("Compose new message");
        functionDisplayPanel.addComponent(composeMessageLayout);
        setCompositionRoot(functionDisplayPanel);
    }

    public ComposeMessage(Window mainWindow, String calledFrom, CustomLayout mainLayout, NewsEventsBean event) {
        main = mainWindow;
        masterPageLayout = mainLayout;
        callerIs = calledFrom;
        ctx = main.getApplication().getContext();
        webCtx = (WebApplicationContext) ctx;
        session = webCtx.getHttpSession();
        username = (String) session.getAttribute("UserName");
        newEventsBean = event;
        btSelectRecipien.setStyleName(Button.STYLE_LINK);
        selTargetAudience.addItem("Members Only");
        selTargetAudience.addItem("Public");
        composeMessageLayout.addComponent(lbSendTo, "lbSendTo");
        composeMessageLayout.addComponent(lbType, "lbType");
        composeMessageLayout.addComponent(lbSubject, "lbSubject");
        componentsForBulletin();
        composeMessageLayout.addComponent(selType, "selType");
        composeMessageLayout.addComponent(tfSubject, "tfSubject");
        composeMessageLayout.addComponent(tfMessage, "tfMessage");
        composeMessageLayout.addComponent(btSend, "btSend");
        composeMessageLayout.addComponent(btCancel, "btCancel");
        functionDisplayPanel.setStyleName("msgPanelsStyle");
        Vector<UserDetailsBean> allMembers = UserControl.getAllActiveMembers();
        for (UserDetailsBean uName : allMembers) {
            selSendTo.addItem(uName.getUserName());
        }
        selSendTo.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
        tfSubject.setColumns(30);
        selType.setWidth(100);
        selListSendTo.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
        selListSendTo.setImmediate(true);
        selListSendTo.addListener(this);
        selListSendTo.setNullSelectionItemId("- Select mail list");
        selType.addItem("Message");
        selType.addItem("Invitation");
        selType.addItem("Bulletin");
        selType.select(MSG_TYPE_BULLETIN);
        selType.setImmediate(true);
        selType.addListener(this);
        selType.setNullSelectionItemId("- Message type");
        functionDisplayPanel.setCaption("Compose new message");
        functionDisplayPanel.addComponent(composeMessageLayout);
        setCompositionRoot(functionDisplayPanel);
    }

    public void buttonClick(ClickEvent event) {
        if (event.getSource() == btSend) {
            if (selType.getValue() == null || selType.getValue().toString().equals("")) {
                return;
            }
            if (tfSubject.getValue() == null || tfSubject.getValue().toString().equals("")) {
                return;
            }
            if (tfMessage.getValue() == null || tfMessage.getValue().toString().equals("")) {
                return;
            }
            if (selType.getValue().toString().equals(MSG_TYPE_BULLETIN)) {
                if (selTargetAudience.getValue() == null || selTargetAudience.getValue().toString().equals("")) {
                    return;
                } else {
                    if (selTargetAudience.getValue().toString().equals("Public")) {
                    } else {
                    }
                }
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String currentTime = dateFormat.format(date);
                message.setFrom(username);
                message.setType((String) selType.getValue());
                int lengthOfSubject = tfSubject.getValue().toString().trim().length();
                int lengthOfMessage = tfMessage.getValue().toString().length();
                if (lengthOfSubject > 100) {
                    return;
                }
                if (lengthOfMessage > 5001) {
                    return;
                }
                if (lengthOfSubject > 0) {
                    message.setSubject((String) tfSubject.getValue());
                } else {
                    message.setSubject("[No Subject]");
                }
                message.setMessageText((String) tfMessage.getValue());
                message.setDate(currentTime);
                MessageControl.createMessage(message);
                if (callerIs.equalsIgnoreCase("ProfileLayout")) {
                } else {
                    refreshTwoPanels();
                }
            } else if (selType.getValue().toString().equals(MSG_TYPE_MESSAGE)) {
                Vector<String> recipientVector = new Vector<String>();
                if (recipientType.equals("Member")) {
                    if (selSendTo.getValue() == null) {
                        return;
                    }
                    UserDetailsBean user = new UserDetailsBean();
                    user = UserControl.getUser(selSendTo.getValue().toString().trim());
                    if (user.getUserName().equals("")) {
                        return;
                    }
                    if (!user.isEnabled()) {
                        return;
                    }
                }
                if (recipientType.equals("Mail List")) {
                    if (selListSendTo.getValue() == null) {
                        return;
                    }
                }
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String currentTime = dateFormat.format(date);
                message.setFrom(username);
                message.setType((String) selType.getValue());
                int t = tfSubject.getValue().toString().trim().length();
                int lengthOfMessage = tfMessage.getValue().toString().length();
                if (t > 100) {
                    return;
                }
                if (lengthOfMessage > 5001) {
                    return;
                }
                if (t > 0) {
                    message.setSubject((String) tfSubject.getValue());
                } else {
                    message.setSubject("[No Subject]");
                }
                message.setMessageText((String) tfMessage.getValue());
                message.setDate(currentTime);
                if (recipientType.equals("Member")) {
                    if (recipientVector.size() > 0) {
                        recipientVector.removeAllElements();
                    }
                    recipientVector.add((String) selSendTo.getValue());
                }
                for (String sendTo : recipientVector) {
                    message.setTo(sendTo);
                    MessageControl.createMessage(message);
                }
                refreshTwoPanels();
            } else if (selType.getValue().toString().equals(MSG_TYPE_INVITATION)) {
                Vector<String> recipientVector = new Vector<String>();
                if (recipientType.equals("Member")) {
                    if (selSendTo.getValue() == null) {
                        return;
                    }
                }
                if (recipientType.equals("Mail List")) {
                    if (selListSendTo.getValue() == null) {
                        return;
                    }
                }
                if (recipientType.equals("Member")) {
                    String ValueOftfSendTo = selSendTo.getValue().toString().trim();
                    if (ValueOftfSendTo.equalsIgnoreCase(username)) {
                        return;
                    }
                    return;
                }
                return;
            }
            UserDetailsBean user = new UserDetailsBean();
            if (user.getUserName().equals("")) {
                return;
            }
            if (!user.isEnabled()) {
                return;
            }
        } else if (recipientType.equals("Mail List")) {
        }
        composeMessageLayout.removeComponent("errorMessage");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String currentTime = dateFormat.format(date);
        message.setDate(currentTime);
        message.setFrom(username);
        message.setType((String) selType.getValue());
        int t = tfSubject.getValue().toString().trim().length();
        int lengthOfMessage = tfMessage.getValue().toString().length();
        if (t > 100) {
            return;
        }
        if (lengthOfMessage > 5001) {
            return;
        }
        composeMessageLayout.removeComponent("errorMessage");
        if (t > 0) {
            message.setSubject((String) tfSubject.getValue());
        } else {
            message.setSubject(username + " Invites you to be friends");
        }
        message.setMessageText((String) tfMessage.getValue());
        if (recipientType.equals("Member")) {
        }
        refreshTwoPanels();
    }

    public void valueChange(ValueChangeEvent event) {
        if (event.getProperty() == selSendTo) {
            selListSendTo.setValue("");
            composeMessageLayout.removeComponent("errorMessage");
        } else if (event.getProperty() == selType) {
            String type = event.getProperty().toString();
            composeMessageLayout.removeComponent("errorMessage");
            if (type != null) {
                if (type.equals(MSG_TYPE_BULLETIN)) {
                    componentsForBulletin();
                } else if (type.equals(MSG_TYPE_MESSAGE)) {
                    componentsForMessage();
                } else if (type.equals(MSG_TYPE_INVITATION)) {
                    componentsForInvitation();
                }
            }
        }
    }

    /**
 * components for invitation
 * 
 */
    public void componentsForInvitation() {
        composeMessageLayout.removeComponent("errorMessage");
        composeMessageLayout.removeComponent(lbBulltinTile);
        composeMessageLayout.removeComponent(selTargetAudience);
        composeMessageLayout.removeComponent(lbTargetAudience);
        composeMessageLayout.addComponent(selSendTo, "tfSendTo");
        composeMessageLayout.removeComponent(selListSendTo);
        composeMessageLayout.removeComponent(btSelectRecipien);
        composeMessageLayout.addComponent(btSelectRecipien, "lbListSendTo");
    }

    /**
	 * components for message
	 * 
	 */
    public void componentsForMessage() {
        composeMessageLayout.removeComponent("errorMessage");
        composeMessageLayout.removeComponent(lbBulltinTile);
        composeMessageLayout.removeComponent(selTargetAudience);
        composeMessageLayout.removeComponent(lbTargetAudience);
        composeMessageLayout.addComponent(selSendTo, "tfSendTo");
        composeMessageLayout.addComponent(btSelectRecipien, "lbListSendTo");
    }

    /**
		 * components for bulletin
		 * 
		 */
    public void componentsForBulletin() {
        composeMessageLayout.removeComponent("errorMessage");
        composeMessageLayout.removeComponent(selSendTo);
        composeMessageLayout.removeComponent(selListSendTo);
        composeMessageLayout.removeComponent(btSelectRecipien);
        composeMessageLayout.addComponent(lbBulltinTile, "tfSendTo");
        composeMessageLayout.addComponent(selTargetAudience, "selTargetAudience");
        composeMessageLayout.addComponent(lbTargetAudience, "lbTargetAudience");
    }

    /**
	 * this method refresh both Messagecontrol Panel and Founctional Panel
	 */
    public void refreshTwoPanels() {
        composeMessageLayout.removeComponent("errorMessage");
        functionDisplayPanel.removeAllComponents();
        messageBoxLayout.removeComponent(functionDisplayPanel);
        messageControlPanel.removeAllComponents();
        messageBoxLayout.removeComponent(messageControlPanel);
        messageBoxLayout.addComponent(new MessageControlPanel(messageBoxLayout, main), "messageControlPanel");
        messageBoxLayout.addComponent(new Inbox(messageBoxLayout, main, 0), "functionDisplayPanel");
    }
}
