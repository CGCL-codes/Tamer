package net.diet_rich.jabak.main.jabak;

import java.awt.Color;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import net.diet_rich.jabak.main.GUICommon;
import net.diet_rich.util.PropertyChangeListener;

/**
 * TODO 1: doc
 * @author Georg Dietrich
 */
public class JabakFrameMethods {

    /** the settings influenced by the GUI */
    private final Settings settings;

    private final ParameterSettings params;

    /**
	 * TODO 1: doc
	 */
    public JabakFrameMethods(Settings settings) {
        this.settings = settings;
        params = settings.p;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final JabakFrameMethods myself = this;
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    JFrame jabakFrame = new JabakFrame(myself);
                    jabakFrame.setLocationRelativeTo(null);
                    jabakFrame.setVisible(true);
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * TODO 1: doc
	 */
    public void sourceFieldInit(final JTextField sourceField) {
        params.source.addChangeListener(new PropertyChangeListener<File>() {

            public void changed(File object) {
                if (!sourceField.getText().equals(params.source.toString())) sourceField.setText(params.source.toString());
            }
        });
        params.source.status.addChangeListener(new GUICommon.ProblemMarker(sourceField));
        settings.enable.addChangeListener(new GUICommon.ComponentDisablerBool(sourceField));
    }

    /**
	 * TODO 1: doc
	 */
    public void targetFieldInit(final JTextField targetField) {
        params.target.addChangeListener(new PropertyChangeListener<File>() {

            public void changed(File object) {
                if (!targetField.getText().equals(params.target.toString())) targetField.setText(params.target.toString());
            }
        });
        params.target.status.addChangeListener(new GUICommon.ProblemMarker(targetField));
        settings.enable.addChangeListener(new GUICommon.ComponentDisablerBool(targetField));
    }

    /**
	 * TODO 1: doc
	 */
    public void sourceButtonInit(JButton sourceButton) {
    }

    /**
	 * TODO 1: doc
	 */
    public void targetButtonInit(JButton targetButton) {
    }

    /**
	 * TODO 1: doc
	 */
    void startButtonInit(final JButton startButton) {
        settings.ok.addChangeListener(new GUICommon.ProblemMarker(startButton));
        settings.ok.addChangeListener(new PropertyChangeListener<String>() {

            public void changed(String string) {
                startButton.setEnabled(string.equals(""));
            }
        });
    }

    /**
	 * TODO 1: doc
	 */
    public void sourceFieldTextUpdate(String text) {
        params.source.stringSet(text);
    }

    /**
	 * TODO 1: doc
	 */
    public void targetFieldTextUpdate(String text) {
        params.target.stringSet(text);
    }

    /**
	 * TODO 1: doc
	 */
    public void sourceButtonPressed(JButton sourceButton) {
    }

    /**
	 * TODO 1: doc
	 */
    public void targetButtonPressed(JButton targetButton) {
    }

    /**
	 * TODO 1: doc
	 */
    void startButtonPressed(final JButton startButton) {
        startButton.setText("running");
        startButton.setBackground(Color.yellow);
        startButton.setEnabled(false);
        settings.enable.set(false);
        SwingWorker<Exception, Void> worker = new SwingWorker<Exception, Void>() {

            @Override
            protected Exception doInBackground() throws Exception {
                try {
                    return new Jabak().call(settings);
                } catch (Exception e) {
                    return e;
                }
            }

            @Override
            protected void done() {
                Exception e = null;
                try {
                    e = get();
                } catch (Exception ex) {
                    e = ex;
                }
                if (e == null) {
                    startButton.setText("finished");
                    startButton.setBackground(Color.green);
                } else {
                    startButton.setText("error - reset");
                    startButton.setBackground(Color.red);
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    e.printStackTrace();
                }
                settings.enable.set(true);
            }
        };
        worker.execute();
    }
}
