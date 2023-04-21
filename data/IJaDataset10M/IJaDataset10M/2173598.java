package sonia.ui;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;

public class ListPicker extends Object {

    private Dialog dialog;

    private Button OK;

    private List displayList;

    private Object pickedObject = null;

    private ArrayList pickList;

    public ListPicker(Frame owner, ArrayList list, String promptString) {
        int numItems = list.size();
        dialog = new Dialog(owner, promptString, true);
        OK = new Button("OK");
        pickList = list;
        displayList = new List(numItems);
        for (int i = 0; i < numItems; i++) {
            displayList.add(((Object) pickList.get(i)).toString());
        }
        displayList.select(0);
        displayList.setBackground(Color.white);
        OK.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                int index = displayList.getSelectedIndex();
                pickedObject = (Object) pickList.get(index);
                dialog.hide();
            }
        });
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 2, 0, 2);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        dialog.add(displayList, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1;
        c.weighty = 1;
        dialog.add(OK, c);
        dialog.setBackground(Color.lightGray);
        dialog.setSize(200, 300);
        dialog.setLocation(100, 100);
        dialog.show();
    }

    public Object getPickedObject() {
        return pickedObject;
    }
}
