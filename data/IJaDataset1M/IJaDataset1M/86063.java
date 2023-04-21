package gui.common.detail;

import gui.ButtonName;
import gui.GuiCommon;
import gui.client.listener.ClientButtonListener;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import data.Base;
import data.Group;

public class GroupDetail extends DetailPage {

    private static final long serialVersionUID = 1L;

    private int width = 400;

    ;

    private int height = 240;

    private Group dataObj = null;

    private boolean isNew = false;

    JTextField nameText = new JTextField();

    JTextField descriptionText = new JTextField();

    JTextField proposerText = new JTextField();

    JTextField dateText = new JTextField();

    public GroupDetail(boolean isNew, Base data, boolean isServer) {
        super(isServer);
        this.isNew = isNew;
        this.dataObj = (Group) data;
        this.setTitle("Request New Group");
        initDialogue();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initDialogue() {
        GuiCommon.setWindowSize(this, width, height);
        JLabel gname = new JLabel("Group Name:");
        JLabel gdescription = new JLabel("Group Description:");
        JLabel gproposer = new JLabel("Proposer:");
        JLabel gdate = new JLabel("Date:");
        JButton creat = new JButton(ButtonName.CREATE_GROUP);
        creat.addActionListener(al);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        GridLayout layout = new GridLayout(5, 2);
        layout.setHgap(10);
        layout.setVgap(10);
        Container contentPane = getContentPane();
        contentPane.setLayout(layout);
        contentPane.add(gname);
        contentPane.add(nameText);
        contentPane.add(gdescription);
        contentPane.add(descriptionText);
        contentPane.add(gproposer);
        Group g = (Group) dataObj;
        proposerText.setText(g.getCreaterName());
        proposerText.setEditable(false);
        contentPane.add(proposerText);
        contentPane.add(gdate);
        if (isNew) {
            g.setCreated_date(new Date(System.currentTimeMillis()));
        }
        dateText.setText(g.getDateString());
        dateText.setEnabled(false);
        contentPane.add(dateText);
        contentPane.add(creat);
        contentPane.add(cancel);
    }

    public String getGroupName() {
        return nameText.getText();
    }

    public String getDescription() {
        return descriptionText.getText();
    }

    @Override
    public Base getOperationResult() {
        if (dataObj != null) {
            Group group = dataObj;
            group.setName(nameText.getText().trim());
            group.setDescription(descriptionText.getText().trim());
        }
        return dataObj;
    }
}
