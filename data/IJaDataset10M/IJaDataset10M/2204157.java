package com.prolix.editor.dialogs.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import com.prolix.editor.GlobalConstants;
import com.prolix.editor.LDT_Constrains;

public class SelectEnvironmentServiceComposite extends ProlixDialogComposite {

    private static final String header_1 = "Types of elements";

    private static final String message_1 = "There are different types of elements. In order to proceed, \n" + "you must choose the type of element you wish to add to the environment. \n" + "Click the info buttons to find out what functions each of the elements has.";

    private static final String header_2 = "Element: learning object";

    private static final String message_2 = "A learning object is any kind of learning material, which may have different \n" + "formats such as html file, audio file, video file or any other format you find \n" + "suitable. \n" + "It is recommended to choose formats for learning objects that can be \n" + "displayed in a web browser. The learning object may contain any material \n" + "you find suited for the learning task at hand.";

    private static final String header_3 = "Element: send mail service";

    private static final String message_3 = "The send mail service will use an internal mail system of the runtime \n" + "environment so that messages may be sent back and forth between \n" + "persons of the unit of learning, but not persons outside the unit of learning.";

    private static final String header_4 = "Element: announcement service";

    private static final String message_4 = "The announcement service allows the role with writing rights to write messages \n" + "that are displayed to specified readers. These messages are unidirectional, \n" + "i.e. they cannot be responded to.";

    private static final String header_5 = "Element: chat service";

    private static final String message_5 = "The chat service allows instant messaging between persons of roles \n" + "that have the right to participate. \n\n" + "This service operates synchronously.";

    private static final String header_6 = "Element: forum service";

    private static final String message_6 = "The forum service allows the writing and posting of messages to a bulletin board, \n" + "usually allowing multiple threads and responses. \n\n" + "This service operates asynchronously. ";

    private static final String header_7 = "Element: search/browse service";

    private static final String message_7 = "The search/browse service provides the possibility to either search or browse \n" + "components (e.g. activities, environments) within the unit of learning.";

    private static final String header_8 = "Element: link to other environment";

    private static final String message_8 = "Within this environment, you may reference another environment that will be completely \n" + "inserted into the current environment. This is useful when a �package� of elements is \n" + "often reused. \n\nFor instance, if a forum is used at two different places within the unit of \n" + "learning, but at each place there are different learning objects used with the forum. \n" + "In this case, the forum would be in one environment, and this environment is referenced \n" + "(linked to) in two other environments, where each contains different learning objects.";

    private Button btn_environment_ref;

    private Button btn_index_search;

    private Button btn_send_mail;

    private Button btn_learning_object;

    private Button btn_chat;

    private Button btn_forum;

    private Button btn_announcement;

    public void createView(Composite parent) {
        Group composite = new Group(parent, SWT.NONE);
        composite.setText(" Select element type ");
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        Label lbl_header1 = new Label(composite, SWT.NONE);
        lbl_header1.setFont(GlobalConstants.DIALOG_HEADER_FONT);
        lbl_header1.setText("* Specify the type of element you wish to add:");
        lbl_header1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        createNewInfoButton(composite, header_1, message_1, "dialoginfo");
        insertSeparator(composite, 2);
        Composite cmp_services = new Composite(composite, SWT.NONE);
        cmp_services.setLayout(new GridLayout());
        cmp_services.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        ((GridData) cmp_services.getLayoutData()).horizontalSpan = 2;
        listEnvironmentServices(cmp_services);
    }

    /**
	 * List environments
	 */
    private void listEnvironmentServices(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btn_learning_object = new Button(composite, SWT.RADIO);
        btn_learning_object.setText("Learning Object");
        btn_learning_object.setImage(LDT_Constrains.ICON_LEARNING_OBJECT);
        btn_learning_object.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btn_learning_object.setSelection(true);
        createNewInfoButton(composite, header_2, message_2, "dialoginfo");
        insertSeparator(composite, 2);
        btn_send_mail = new Button(composite, SWT.RADIO);
        btn_send_mail.setText("Send Mail Service");
        btn_send_mail.setImage(LDT_Constrains.ICON_SENDMAIL);
        btn_send_mail.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        createNewInfoButton(composite, header_3, message_3, "dialoginfo");
        btn_announcement = new Button(composite, SWT.RADIO);
        btn_announcement.setText("Announcement Service (Conference)");
        btn_announcement.setImage(LDT_Constrains.ICON_ANNOUNCEMENT);
        btn_announcement.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        createNewInfoButton(composite, header_4, message_4, "dialoginfo");
        btn_chat = new Button(composite, SWT.RADIO);
        btn_chat.setText("Chat Service (Conference)");
        btn_chat.setImage(LDT_Constrains.ICON_CHAT);
        btn_chat.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        createNewInfoButton(composite, header_5, message_5, "dialoginfo");
        btn_forum = new Button(composite, SWT.RADIO);
        btn_forum.setText("Forum Service (Conference)");
        btn_forum.setImage(LDT_Constrains.ICON_FORUM);
        btn_forum.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        createNewInfoButton(composite, header_6, message_6, "dialoginfo");
        btn_index_search = new Button(composite, SWT.RADIO);
        btn_index_search.setText("Search/Browse Service");
        btn_index_search.setImage(LDT_Constrains.ICON_INDEXSERACH);
        btn_index_search.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        createNewInfoButton(composite, header_7, message_7, "dialoginfo");
        insertSeparator(composite, 2);
        btn_environment_ref = new Button(composite, SWT.RADIO);
        btn_environment_ref.setText("Link to other Environment");
        btn_environment_ref.setImage(LDT_Constrains.ICON_ENVIRONMENT_REF);
        btn_environment_ref.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        createNewInfoButton(composite, header_8, message_8, "dialoginfo");
    }

    /**
	 * get selected service
	 * if no service was selected, the method returns -1.
	 */
    public int getSelectedService() {
        if (btn_chat.getSelection()) return GlobalConstants.CHAT_SERVICE_TYPE;
        if (btn_forum.getSelection()) return GlobalConstants.FORUM_SERVICE_TYPE;
        if (btn_announcement.getSelection()) return GlobalConstants.ANNOUNCEMENT_SERVICE_TYPE;
        if (btn_environment_ref.getSelection()) return GlobalConstants.ENVIRONMENT_REF_TYPE;
        if (btn_index_search.getSelection()) return GlobalConstants.INDEX_SEARCH_SERVICE_TYPE;
        if (btn_learning_object.getSelection()) return GlobalConstants.LEARNING_OBJECT_TYPE;
        if (btn_send_mail.getSelection()) return GlobalConstants.SEND_MAIL_SERVICE_TYPE;
        return -1;
    }
}
