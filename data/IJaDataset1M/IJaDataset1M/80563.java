package jmax.editors.qlist;

import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

class QListFindDialog extends JPanel {

    public QListFindDialog(QList qList, QListPanel qListPanel) {
        super(qList.getEditor().getEnclosingFrame(), "Qlist: Find", false);
        this.qListPanel = qListPanel;
        continueDialog = new QListContinueDialog(qList.getEditor().getEnclosingFrame(), qList, this);
        Panel p1 = new Panel();
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        p1.add(new Label("Find:"));
        textField = new TextField("", 40);
        textField.addActionListener(new MaxAction() {

            public void doAction() {
                find();
            }
        });
        p1.add(textField);
        add("North", p1);
        Panel p2 = new Panel();
        p2.setLayout(new BorderLayout());
        Button findButton = new Button("Find");
        findButton.addActionListener(new MaxAction() {

            public void doAction() {
                find();
            }
        });
        p2.add("East", findButton);
        Button closeButton = new Button("Close");
        closeButton.addActionListener(new MaxAction() {

            public void doAction() {
                close();
            }
        });
        p2.add("West", closeButton);
        add("South", p2);
        pack();
    }

    class QListContinueDialog extends JPanel {

        public QListContinueDialog(JFrame parent, QListFindDialog asker) {
            super(parent, "Qlist: Question", false);
            this._asker = asker;
            Panel p1 = new Panel();
            p1.setLayout(new FlowLayout(FlowLayout.LEFT));
            p1.add(new Label("End reached; continue from beginning?"));
            add("North", p1);
            Panel p2 = new Panel();
            Button okButton = new Button("OK");
            okButton.addActionListener(new MaxAction() {

                public void doAction() {
                    setVisible(false);
                    _asker.restart();
                }
            });
            p2.add(okButton);
            Button cancelButton = new Button("Cancel");
            cancelButton.addActionListener(new MaxAction() {

                public void doAction() {
                    setVisible(false);
                }
            });
            p2.add(cancelButton);
            add("South", p2);
            pack();
        }

        protected QListFindDialog _asker;
    }

    protected void doFind(int fromIndex) {
        int index;
        index = qListPanel.find(textField.getText(), fromIndex);
        if (index < 0) continueDialog.setVisible(true);
    }

    protected void restart() {
        doFind(0);
    }

    public void find() {
        doFind(qListPanel.getCaretPosition());
    }

    public void open() {
        setVisible(true);
    }

    public void close() {
        setVisible(false);
    }

    TextField textField;

    QListContinueDialog continueDialog;

    QListPanel qListPanel;
}
