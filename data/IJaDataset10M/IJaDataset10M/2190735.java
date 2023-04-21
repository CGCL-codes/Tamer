package com.onyourmind.awt.dialog;

import java.util.ArrayList;
import java.util.ResourceBundle;
import com.onyourmind.awt.OymFrame;
import com.onyourmind.tools.Crypt;
import com.onyourmind.tools.UserRecord;

public class UsersBox extends ItemListDialog {

    private static final long serialVersionUID = -770746168842019777L;

    public String username = null;

    public java.util.List<UserRecord> userRecords = new ArrayList<UserRecord>();

    public UsersBox(OymFrame parent, String strTitle, String strUsername) {
        super(parent, strTitle);
        username = strUsername;
        ResourceBundle rb = ResourceBundle.getBundle("i18n.LabelBundle", getFrame().getLocale());
        setListLabel(rb.getString("Existing_People_[Last_Name,_First_Name,_(Username),_Type,_Creation_Date,_Created_By,_Affiliate,_Last_Login,_Status]") + ":");
        setNumberLabel(rb.getString("Number_of_People") + ":");
        createControls();
    }

    public void onListSelect() {
        enableButtons(bEnableAction());
    }

    public boolean bEnableAction() {
        try {
            String strSelectedItem = getList().getSelectedItem();
            return (strSelectedItem.indexOf("(" + username + ")") == -1 && strSelectedItem.indexOf(username) != -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUniqueIDFromSelection() {
        String strSelectedItem = getList().getSelectedItem();
        if (strSelectedItem == null) return null;
        int nStart = strSelectedItem.indexOf("  (") + 3;
        int nEnd = strSelectedItem.indexOf("),  ");
        return strSelectedItem.substring(nStart, nEnd);
    }

    public java.util.List<String> getStringList() {
        userRecords = getFrame().getUserList();
        return getFrame().getUserListString(userRecords);
    }

    public void deleteItem(String strUniqueID) {
        try {
            getFrame().getApp().makeNewSocket();
            getFrame().getApp().dos.writeInt(15);
            getFrame().getApp().dos.writeUTF(strUniqueID);
            getFrame().getApp().dos.writeUTF(getFrame().getApp().username);
            getFrame().getApp().dos.flush();
            getFrame().getApp().getInputStream();
            refreshList();
            getFrame().getApp().dis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onAdd() {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("i18n.LabelBundle", getFrame().getLocale());
            AddUserBox dlg = new AddUserBox(getFrame(), this, rb.getString("Add_User"));
            dlg.setVisible(true);
            getFrame().getApp().makeNewSocket();
            getFrame().getApp().dos.writeInt(14);
            getFrame().sendObject(new UserRecord(dlg.getLastName(), dlg.getFirstName(), dlg.getUsername(), Crypt.crypt(dlg.getPassword()), username));
            getFrame().getApp().dos.flush();
            getFrame().getApp().getInputStream();
            if (!getFrame().getApp().dis.readBoolean()) new InfoBox(getFrame(), ResourceBundle.getBundle("i18n.MessageBundle", getFrame().getLocale()).getString("The_user_could_not_be_added_because_of_a_problem_with_the_database.")); else refreshList();
            getFrame().getApp().dis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
