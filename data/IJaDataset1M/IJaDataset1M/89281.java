package org.afk.cfg.swing;

import javax.swing.*;
import javax.swing.event.*;
import org.afk.cfg.*;

/**
 * @author axel
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class JCfgRadioButton extends JRadioButton implements ConfigListener, ChangeListener {

    private Def myDef;

    /**
	 * 
	 */
    public JCfgRadioButton(Def definition) {
        init(definition);
    }

    /**
	 * @param definition
	 */
    private void init(Def definition) {
        if (definition == null) throw new IllegalArgumentException("definition was null");
        if (definition.getType() != Type.BOOLEAN) throw new IllegalArgumentException("Definition is not of type Boolean");
        this.myDef = definition;
        setName(definition.getEntry());
        super.setSelected(definition.getBoolean());
        setToolTipText(definition.getDescription());
        definition.addListener(this);
        addChangeListener(this);
    }

    /**
	 * @param text
	 * @param selected
	 */
    public JCfgRadioButton(String text, Def definition) {
        super(text);
        init(definition);
    }

    /**
	 * @param icon
	 * @param selected
	 */
    public JCfgRadioButton(Icon icon, Def definition) {
        super(icon);
        init(definition);
    }

    /**
	 * @param text
	 * @param icon
	 * @param selected
	 */
    public JCfgRadioButton(String text, Icon icon, Def definition) {
        super(text, icon);
        init(definition);
    }

    public void configurationRefreshed(ConfigurationRefreshEvent event) {
    }

    public void configValueChanged(ConfigValueChangedEvent event) {
        setSelected(myDef.getBoolean());
    }

    public void stateChanged(ChangeEvent e) {
        try {
            boolean b = isSelected();
            myDef.setCurrentValue("" + b);
        } catch (ValueException ex) {
            ex.printStackTrace();
        }
    }
}
