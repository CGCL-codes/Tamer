package de.outofbounds.kinderactive.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author root
 */
public abstract class GamePanel {

    protected CategoryTab source;

    protected JPanel buttonPanel;

    /** Creates a new instance of SensoCategoryTab */
    public GamePanel(String introClip, CategoryTab source) {
    }

    protected abstract JPanel buildActionPanel();

    protected abstract JPanel buildButtonPanel();
}
