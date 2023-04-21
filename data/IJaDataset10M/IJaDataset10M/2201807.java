package org.vikamine.gui.databaseIO;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import org.vikamine.kernel.data.creators.DBConnector;

public class PathRequest extends Login {

    /** generated */
    private static final long serialVersionUID = -898592449553091613L;

    private ArrayList<String> list;

    public PathRequest(ArrayList<String> s) {
        super();
        list = s;
    }

    ArrayList<ArrayList<String>> userList;

    public PathRequest(ArrayList<String> attributeList, ArrayList<ArrayList<String>> userAllList) {
        super();
        list = attributeList;
        this.userList = userAllList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton jb = (JButton) e.getSource();
        if (jb.getText().equals("Connect")) {
            String tempString = (String) dataBase.getModel().getSelectedItem();
            str1 = str1 + "/" + tempString;
            DBConnector dbo = new DBConnector(str1, str2, str3);
            if (!dbo.isConnected()) {
                ipAdress.setText("");
                password.setText("");
                userName.setText("");
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                dataBase.setModel(model);
            } else {
                if (userList != null && userList.size() != 0) {
                    ExportPath ep = new ExportPath(dbo, tempString, list, userList);
                    ep.setVisible(true);
                } else {
                    ExportPath ep = new ExportPath(dbo, tempString, list);
                    ep.setVisible(true);
                }
                this.setVisible(false);
            }
        }
        if (jb.getText().equals("Cancel")) {
            this.setVisible(false);
        }
        if (jb.getText().equals("check")) {
            DBConnector dbo = connection();
            if (!dbo.isConnected()) {
                System.out.println(str4);
                dataBase.setEditable(false);
                errorWaring();
            } else {
                dataBase.setEnabled(true);
            }
        }
    }
}
