package com.g2d.editor.property;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.g2d.awt.util.AbstractDialog;
import com.g2d.awt.util.AbstractFrame;

public class ObjectPropertyDialog extends AbstractDialog {

    private static final long serialVersionUID = 1L;

    JButton ok = new JButton("OK");

    public ObjectPropertyDialog(Component owner, BaseObjectPropertyPanel panel) {
        super(owner);
        super.setLayout(new BorderLayout());
        super.add(panel, BorderLayout.CENTER);
        JPanel south = new JPanel(new FlowLayout());
        south.add(ok);
        super.add(south, BorderLayout.SOUTH);
        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectPropertyDialog.this.dispose();
            }
        });
    }
}
