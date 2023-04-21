package org.hfbk.ui;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleButton extends Button {

    public SimpleButton(String text) {
        super(text);
        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                action();
            }
        });
    }

    protected void action() {
    }

    ;
}
