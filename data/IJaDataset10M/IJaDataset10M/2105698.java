package com.onyourmind.awt.dialog;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ResourceBundle;
import com.onyourmind.awt.OymFrame;
import com.onyourmind.awt.OymPanel;

public class AskDialog extends DialogBox implements Serializable {

    private static final long serialVersionUID = -2457475904253352909L;

    private boolean isOkCancel = false;

    private Label static1;

    public AskDialog(OymFrame parent, String strMessage, String strTitle) {
        this(parent, strMessage, strTitle, true);
    }

    public AskDialog(OymFrame parent, String strMessage, String strTitle, boolean bOKCancel) {
        super(parent, strTitle, true);
        isOkCancel = bOKCancel;
        initialize(strMessage);
    }

    public void initialize(String strMessage) {
        createControls();
        static1.setText(strMessage);
        super.createControls();
    }

    public void setAnswer(int nAnswer) {
        dispose();
        setStatus(nAnswer);
    }

    public void createControls() {
        setLayout(new BorderLayout(10, 10));
        static1 = new Label("");
        add("Center", static1);
        OymPanel panel = new OymPanel();
        add("South", panel);
        panel.setLayout(new GridLayout(1, 0, 5, 0));
        ResourceBundle rb = ResourceBundle.getBundle("i18n.LabelBundle", getFrame().getLocale());
        String strYesButton = rb.getString("Yes");
        if (isOkCancel) strYesButton = rb.getString("OK");
        Button buttonYes = new Button(strYesButton);
        panel.add(buttonYes);
        buttonYes.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setAnswer(ACTION_YES);
            }
        });
        if (!isOkCancel) {
            Button buttonNo = new Button(rb.getString("No"));
            panel.add(buttonNo);
            buttonNo.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setAnswer(ACTION_NO);
                }
            });
        }
        Button buttonCancel = new Button(rb.getString("Cancel"));
        panel.add(buttonCancel);
        buttonCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setAnswer(ACTION_CANCEL);
            }
        });
    }
}
