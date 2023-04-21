package com.googlecode.jrename.gui.action;

import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import com.googlecode.jrename.bean.Option;
import com.googlecode.jrename.gui.Resources;
import com.googlecode.jrename.ui.Command;
import com.googlecode.jrename.ui.RenameMediator;

public class PreviewCheckBox extends JCheckBox implements Command, Option {

    private static final long serialVersionUID = 1L;

    private RenameMediator med;

    public PreviewCheckBox(ActionListener al, RenameMediator m) {
        super(Resources.get("selection.live.preview"));
        setOpaque(false);
        setSelected(true);
        addActionListener(al);
        med = m;
        med.registerOption(this);
        execute();
    }

    public void execute() {
        med.setLivePreview(this.isSelected());
    }

    public void restoreDefault() {
        setSelected(true);
        execute();
    }
}
