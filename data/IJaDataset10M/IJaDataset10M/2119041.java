package org.mbari.vars.knowledgebase2.ui;

import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.mbari.swing.JFancyButton;
import org.mbari.vars.knowledgebase.model.Concept;
import org.mbari.vars.knowledgebase.model.ConceptName;

/**
 * <p><!-- Class description --></p>
 *
 * @version    $Id: EditorPanel.java 94 2005-12-19 17:43:29Z hohonuuli $
 * @author     <a href="http://www.mbari.org">Monterey Bay Aquarium Research Institute</a>
 */
public abstract class EditorPanel extends JPanel implements ILockableEditor {

    protected static final Concept NULL_CONCEPT = new Concept(new ConceptName("", ConceptName.NAMETYPE_PRIMARY, ConceptName.AUTHOR_UNKNOWN), null);

    private Concept concept;

    private final Icon lockedIcon;

    private JPanel primaryNamePanel = null;

    private JLabel primaryNameLabel = null;

    private boolean locked = true;

    private JButton lockButton = null;

    private final Icon unlockedIcon;

    /**
     * This is the default constructor
     */
    public EditorPanel() {
        super();
        lockedIcon = new ImageIcon(getClass().getResource("/images/vars/knowledgebase/lock.png"));
        unlockedIcon = new ImageIcon(getClass().getResource("/images/vars/knowledgebase/lock_open.png"));
        initialize();
    }

    /**
     * @return Returns the concept.
     */
    public Concept getConcept() {
        return concept;
    }

    /**
     * This method initializes lockButton
     *
     * @return javax.swing.JButton
     */
    public JButton getLockButton() {
        if (lockButton == null) {
            lockButton = new JFancyButton();
            lockButton.setIcon(lockedIcon);
        }
        return lockButton;
    }

    /**
     * This method initializes primaryNamePanel
     *
     * @return javax.swing.JPanel
     */
    protected JPanel getPrimaryNamePanel() {
        if (primaryNamePanel == null) {
            primaryNameLabel = new JLabel();
            primaryNameLabel.setText(" ");
            primaryNameLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 14));
            primaryNamePanel = new JPanel();
            primaryNamePanel.setLayout(new BoxLayout(getPrimaryNamePanel(), BoxLayout.X_AXIS));
            primaryNamePanel.add(Box.createHorizontalStrut(10));
            primaryNamePanel.add(primaryNameLabel, null);
            primaryNamePanel.add(Box.createHorizontalGlue());
            primaryNamePanel.add(getLockButton(), null);
        }
        return primaryNamePanel;
    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        this.setLayout(new BorderLayout());
        this.setSize(300, 200);
        this.add(getPrimaryNamePanel(), java.awt.BorderLayout.NORTH);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * @param concept The concept to set.
     */
    public void setConcept(Concept concept) {
        if (concept == null) {
            concept = NULL_CONCEPT;
        }
        this.concept = concept;
        String primaryName = concept.getPrimaryConceptNameAsString();
        primaryNameLabel.setText(primaryName);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param locked
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
        Icon lockIcon = lockedIcon;
        if (!locked) {
            lockIcon = unlockedIcon;
        }
        getLockButton().setIcon(lockIcon);
    }
}
