package net.etherstorm.jopenrpg.swing.map;

import java.awt.*;
import java.beans.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Title: 		 jOpenRPG
 * Description:  jOpenRPG is an OpenRPG compatible online gaming client.
 * Copyright:  Copyright (c) 2001
 * Company: 		 Etherstorm Software
 * @author $Author: tedberg $
 * @version $Revision: 1.7 $
 */
public class JColorEditor extends JPanel {

    private PropertyEditor editor;

    public JColorEditor(PropertyEditor editor) {
        super(new BorderLayout());
        this.editor = editor;
        add(new CommandButton(editor));
    }
}

class CommandButton extends JButton implements ActionListener {

    public CommandButton(PropertyEditor editor) {
        setEditor(editor);
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        editColor();
    }

    protected PropertyEditor _editor;

    public void setEditor(PropertyEditor val) {
        _editor = val;
    }

    public PropertyEditor getEditor() {
        return _editor;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Rectangle bounds = getBounds();
        int fudge = 4;
        int x1 = fudge;
        int y1 = fudge;
        int x2 = (int) bounds.getWidth() - fudge * 2;
        int y2 = (int) bounds.getHeight() - fudge * 2;
        Color c = (Color) getEditor().getValue();
        g.setColor((c == null ? Color.black : c));
        g.fillRect(x1, y1, x2, y2);
        g.setColor(Color.black);
        g.drawRect(x1, y1, x2, y2);
    }

    protected boolean inUse = false;

    protected synchronized void editColor() {
        if (inUse) return;
        inUse = true;
        Color c = (Color) getEditor().getValue();
        c = (c == null ? Color.black : c);
        JColorChooser cc = new JColorChooser(c);
        int result = JOptionPane.showInternalConfirmDialog(net.etherstorm.jopenrpg.ReferenceManager.getInstance().getMainFrame().getDesktop(), cc, "Edit Color", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) ;
        {
            getEditor().setValue(cc.getColor());
        }
        inUse = false;
    }
}
