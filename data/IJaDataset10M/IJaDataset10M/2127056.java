package com.memoire.vainstall.builder.util;

import com.memoire.vainstall.VAGlobals;
import com.memoire.vainstall.builder.gui.PreferencesBasePanel;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;

/**
 * This is 
 *
 * @see 
 *
 * @author Henrik Falk
 * @version $Id: PreferencesBaseNode.java,v 1.1 2001/09/28 19:41:42 hfalk Exp $
 */
public class PreferencesBaseNode extends AbstractVAIBuilderNode {

    PreferencesBasePanel panel = null;

    private static final Border loweredBorder = new SoftBevelBorder(BevelBorder.LOWERED);

    public PreferencesBaseNode() {
        super();
    }

    public String getName() {
        return VAGlobals.NAME;
    }

    public JPanel getUI() {
        if (panel == null) {
            panel = new PreferencesBasePanel();
            panel.setBorder(loweredBorder);
        }
        return panel;
    }

    public String getTitle() {
        return VAGlobals.NAME + VAGlobals.getResource("com.memoire.vainstall.builder.Language", "PreferencesBaseNode_Version") + VAGlobals.VERSION;
    }

    public ImageIcon getIcon() {
        return new javax.swing.ImageIcon(getClass().getResource("/com/memoire/vainstall/builder/images/New16.gif"));
    }

    public void start() {
        ((PreferencesBasePanel) getUI()).initialize(getModel());
    }

    public void stop() {
        ((PreferencesBasePanel) getUI()).stop();
    }

    public void save() {
    }
}
