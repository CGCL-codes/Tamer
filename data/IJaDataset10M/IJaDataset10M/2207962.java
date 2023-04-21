package net.sourceforge.crhtetris.frames;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import net.miginfocom.swing.MigLayout;
import net.sourceforge.crhtetris.i18n.Text;
import net.sourceforge.crhtetris.misc.TetrisProperty;
import net.sourceforge.crhtetris.panels.SettingsPanel;
import net.sourceforge.crhtetris.utils.CRHFrame;
import net.sourceforge.crhtetris.utils.Writer;
import com.github.croesch.components.CButton;

/**
 * The frame that is responsible to set the settings of the game.
 * 
 * @author croesch
 * @since Date: Aug 27, 2011
 */
public class SettingsFrame extends CRHFrame {

    /** generated serial version uid */
    private static final long serialVersionUID = 3832540049985335711L;

    /** the width of the frame */
    private static final int WIDTH = 550;

    /** the list of all {@link SettingsPanel}s visible in this frame */
    private final List<SettingsPanel> panelList = new ArrayList<SettingsPanel>();

    /** the different types of actions in this frame */
    private static enum ACTION_TYPE {

        /** the save action - will result in saving the properties in the belonging file */
        SAVE(Text.SETTINGS_SAVE), /** cancel action - will restore state before opening this frame */
        CANCEL(Text.SETTINGS_CANCEL);

        /** the text to display this action (on a button) */
        private final Text text;

        /**
     * Constructs the action type with the given {@link Text}.
     * 
     * @since Date: Aug 30, 2011
     * @param t the {@link Text} that can be written on a button
     */
        private ACTION_TYPE(final Text t) {
            this.text = t;
        }

        /**
     * Returns the text to display for this action (perhaps on a button)
     * 
     * @since Date: Aug 30, 2011
     * @return {@link Text} that contains the {@link String} that you can write on a button.
     */
        private Text text() {
            return this.text;
        }
    }

    ;

    /**
   * Constructs the settings frame.
   * 
   * @since Date: Aug 28, 2011
   */
    public SettingsFrame() {
        super(WindowConstants.DISPOSE_ON_CLOSE, Text.SETTINGS.text(), WIDTH, false, true);
        setLayout(new MigLayout("fill"));
        final JPanel pane = new JPanel(new MigLayout("wrap 2", "[right][]"));
        for (final TetrisProperty p : TetrisProperty.values()) {
            final SettingsPanel panel = new SettingsPanel(p);
            if (panel.isViewable()) {
                this.panelList.add(panel);
                pane.add(new JLabel(p.text()));
                pane.add(panel);
            }
        }
        final JScrollPane sPane = new JScrollPane(pane);
        add(sPane, "grow, wrap");
        final JPanel buttonPanel = new JPanel(new MigLayout());
        final CButton saveButton = new CButton(new SettingsFrameAction(ACTION_TYPE.SAVE));
        final CButton cancelButton = new CButton(new SettingsFrameAction(ACTION_TYPE.CANCEL));
        saveButton.setName("saveButton");
        cancelButton.setName("cancelButton");
        buttonPanel.add(saveButton, "sg but");
        buttonPanel.add(cancelButton, "sg but");
        add(buttonPanel, "shrinky, align right");
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent e) {
                super.windowClosing(e);
                for (final SettingsPanel p : SettingsFrame.this.panelList) {
                    p.reset();
                }
            }
        });
    }

    /**
   * Resets each property to the value it has had when opening this frame and closes this frame.
   * 
   * @since Date: Aug 30, 2011
   */
    private void cancelPropertiesEditing() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
   * Tests if any property has been changed and stores the properties if so.
   * 
   * @since Date: Aug 30, 2011
   */
    private void savePropertiesIfNecessary() {
        int changed = 0;
        for (final SettingsPanel panel : this.panelList) {
            if (panel.hasChanged()) {
                ++changed;
            }
        }
        if (changed > 0) {
            try {
                TetrisProperty.store();
                Writer.write(Text.INFO_SETTINGS_SAVED.text(changed));
            } catch (final IOException e) {
                Writer.error(e);
            }
        } else {
            Writer.write(Text.INFO_SETTINGS_NOT_SAVED.text());
        }
    }

    /**
   * Generic class for all actions in that frame.
   * 
   * @author croesch
   * @since Date: Aug 30, 2011
   */
    private class SettingsFrameAction extends AbstractAction {

        /** generated serial version uid */
        private static final long serialVersionUID = -2682861786124738704L;

        /** the type of that action */
        private final ACTION_TYPE type;

        /**
     * Constructs the action. The text for the action will be fetched from the given type, so this must not be
     * <code>null</code>.
     * 
     * @since Date: Aug 30, 2011
     * @param t the type of the action, used to decide what to do when action is performed. And to fetch the text from.
     */
        public SettingsFrameAction(final ACTION_TYPE t) {
            super(t.text().text());
            this.type = t;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            switch(this.type) {
                case SAVE:
                    SettingsFrame.this.savePropertiesIfNecessary();
                    break;
                default:
                    SettingsFrame.this.cancelPropertiesEditing();
            }
        }
    }
}
