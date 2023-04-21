package org.hermeneutix.control.swing;

import static org.hermeneutix.control.MessageKeyConstants.MENUBAR_PREFERENCES;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import org.hermeneutix.control.Messages;
import org.hermeneutix.control.Options;
import org.hermeneutix.view.swing.HmxClient;
import org.hermeneutix.view.swing.OptionAnalysisPanel;
import org.hermeneutix.view.swing.OptionPrintPanel;
import org.hermeneutix.view.swing.OptionViewPanel;
import org.hermeneutix.view.swing.SplitFrame;

/**
 * swing controller representing the <code>Preferences</code> entry in the
 * JMenuBar; opened JFrame offers the opportunity to set default values for a
 * new analysis and customizing the appearance of the {@link HmxClient} and its
 * displayed content.
 * 
 * @author C. Englert
 */
public final class OptionControl {

    /**
	 * {@link SplitFrame} opened and filled with content by the constructor.
	 */
    final SplitFrame frame;

    /**
	 * all settings chosen in this instance, which will be saved when the
	 * {@link SplitFrame} is closed by the <code>OK</code>-button.
	 */
    private final Map<String, String> chosenSettings = new HashMap<String, String>();

    /**
	 * opens a {@link SplitFrame} containing two preference views:
	 * <code>View</code> and <code>Analysis</code>, each one offers the
	 * opportunity to change the application settings, mostly representing
	 * default settings for a new analysis or the general appearance.
	 * 
	 * @param parent
	 *            parent {@link HmxClient} to refresh the chosen LookAndFeel
	 */
    private OptionControl(final HmxClient parent) {
        this.frame = new SplitFrame(Messages.getString(MENUBAR_PREFERENCES), new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                saveOptions();
                OptionControl.this.frame.close();
            }
        });
        initOptionFrame(parent);
        this.frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(final WindowEvent event) {
                String toReset = Options.getInstance().getProperty(Options.LOOK_AND_FEEL);
                if (toReset == null || toReset.isEmpty()) {
                    toReset = UIManager.getSystemLookAndFeelClassName();
                }
                try {
                    UIManager.setLookAndFeel(toReset);
                    SwingUtilities.updateComponentTreeUI(parent);
                    parent.revalidate();
                } catch (ClassNotFoundException e) {
                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                } catch (UnsupportedLookAndFeelException e) {
                }
            }
        });
        this.frame.pack();
    }

    /**
	 * opens a {@link SplitFrame} containing two preference views:
	 * <code>View</code> and <code>Analysis</code>, each one offers the
	 * opportunity to change the application settings, mostly representing
	 * default settings for a new analysis.
	 * 
	 * @param parent
	 *            parent {@link HmxClient} to refresh the chosen LookAndFeel
	 */
    public static void showPreferenceDialog(final HmxClient parent) {
        (new OptionControl(parent)).frame.setVisible(true);
    }

    /**
	 * adds the two preference views to the category tree of the
	 * {@link SplitFrame}, representing the <code>View</code> and
	 * <code>Analysis</code>
	 * 
	 * @param parent
	 *            parent {@link HmxClient} to refresh the chosen LookAndFeel
	 */
    private void initOptionFrame(final HmxClient parent) {
        final DefaultMutableTreeNode viewNode = new DefaultMutableTreeNode();
        viewNode.setUserObject(new OptionViewPanel(this, parent));
        this.frame.insertTreeNode(viewNode);
        final DefaultMutableTreeNode analysisNode = new DefaultMutableTreeNode();
        analysisNode.setUserObject(new OptionAnalysisPanel(this));
        this.frame.insertTreeNode(analysisNode);
        final DefaultMutableTreeNode printingNode = new DefaultMutableTreeNode();
        printingNode.setUserObject(new OptionPrintPanel(this));
        this.frame.insertTreeNode(printingNode);
        this.frame.setSelectedNode(viewNode);
    }

    /**
	 * @return the option selection containing frame
	 */
    public SplitFrame getFrame() {
        return this.frame;
    }

    /**
	 * remember the chosen setting value for the specified key.
	 * 
	 * @param key
	 *            which option has been defined
	 * @param value
	 *            chosen value for this setting
	 */
    public void addChosenSetting(final String key, final String value) {
        this.chosenSettings.put(key, value);
    }

    /**
	 * check if there is already a setting value stored for the specified key.
	 * 
	 * @param key
	 *            option key to check for stored option value
	 * @return if the key is stored
	 */
    public boolean containsChosenSettingKey(final String key) {
        return this.chosenSettings.containsKey(key);
    }

    /**
	 * request the stored option value for the specified key.
	 * 
	 * @param key
	 *            option key to get the stored option value for
	 * @return setting value for the specified key
	 */
    public String getChosenSetting(final String key) {
        return this.chosenSettings.get(key);
    }

    /**
	 * stores the chosen preferences in the options file.
	 */
    void saveOptions() {
        final JPanel active = this.frame.getActiveContent();
        if (active != null) {
            active.getParent().remove(active);
        }
        final Options optionInstance = Options.getInstance();
        for (String singleKey : this.chosenSettings.keySet()) {
            optionInstance.setProperty(singleKey, this.chosenSettings.get(singleKey));
        }
        optionInstance.storeChanges();
    }
}
