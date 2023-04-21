package viewer.action;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SwingHelper;
import viewer.Searcher;
import viewer.config.ConfigWriter;
import viewer.config.Configuration;
import viewer.config.edit.ConfigurationEditor;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class ConfigurationAction extends SearcherAction {

    private static final long serialVersionUID = -4879240469021570017L;

    static final Logger logger = LoggerFactory.getLogger(ConfigurationAction.class);

    private static final String FILE_IMAGE = "res/images/preferences-other.png";

    private String configFile;

    private JDialog dialog;

    private ConfigurationEditor configurationEditor;

    /**
	 * Create a new action instance.
	 * 
	 * @param searcher
	 *            the searcher this is about.
	 */
    public ConfigurationAction(Searcher searcher) {
        super(searcher, FILE_IMAGE, "settings");
        setDescription("configure the settings for this application");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        logger.debug("configure settings");
        configFile = getSearcher().getConfigFile();
        Configuration config = getSearcher().getConfiguration();
        showDialog(config);
    }

    private void showDialog(Configuration configuration) {
        configurationEditor = new ConfigurationEditor(configuration) {

            private static final long serialVersionUID = 1L;

            @Override
            public void ok() {
                ConfigurationAction.this.ok();
            }

            @Override
            public void cancel() {
                ConfigurationAction.this.cancel();
            }
        };
        JFrame parentFrame = SwingHelper.getContainingFrame(getSearcher());
        dialog = new JDialog(parentFrame, "Settings: " + configFile);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setContentPane(configurationEditor);
        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                closing();
            }
        });
        dialog.setModal(true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    /**
	 * Called when dialog is submitted.
	 */
    protected void ok() {
        logger.debug("OK");
        logger.debug("writing to: " + configFile);
        Configuration configuration = configurationEditor.getConfiguration();
        OutputStream out = null;
        boolean success = false;
        try {
            out = new FileOutputStream(configFile);
            ConfigWriter.write(configuration, out);
            success = true;
        } catch (FileNotFoundException e) {
            showError("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            showError("IOException: " + e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }
            getSearcher().setConfiguration(configuration);
        }
        if (success) {
            dialog.dispose();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(dialog, message, "error", JOptionPane.ERROR_MESSAGE);
    }

    /**
	 * Called when dialog has been canceled.
	 */
    protected void cancel() {
        logger.debug("CANCEL");
        dialog.dispose();
    }

    /**
	 * Called when dialog is closing.
	 */
    protected void closing() {
        logger.debug("CLOSING");
        dialog.dispose();
    }
}
