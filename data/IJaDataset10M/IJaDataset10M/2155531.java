package net.sf.daro.swing;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

/**
 * A progress monitor for {@link SwingWorker}.
 * 
 * @author rohe
 */
public class SwingWorkerProgressMonitor {

    /**
	 * worker
	 */
    private SwingWorker<?, ?> worker;

    /**
	 * parent component
	 */
    private Component parentComponent;

    /**
	 * dialog
	 */
    private JDialog dialog;

    /**
	 * panel
	 */
    private JOptionPane panel;

    private Object message;

    /**
	 * progress bar
	 */
    private JProgressBar progressBar;

    private String note;

    private Object[] cancelOption;

    /**
	 * Creates a new SwingWorkerProgressMonitor with the given parent component
	 * for the dialog while the given worker is running.
	 * 
	 * @param parentComponent
	 *            the parent component
	 * @param worker
	 *            the worker
	 */
    public SwingWorkerProgressMonitor(Component parentComponent, SwingWorker<?, ?> worker) {
        if (worker == null) {
            throw new IllegalArgumentException("worker must not be null");
        }
        this.parentComponent = parentComponent;
        this.worker = worker;
        this.worker.addPropertyChangeListener(new SwingWorkerProgressHandler());
        cancelOption = new Object[1];
        cancelOption[0] = UIManager.getString("OptionPane.cancelButtonText");
        progressBar = new JProgressBar(0, 100);
    }

    protected JOptionPane createProgressPane() {
        JOptionPane pane = new JOptionPane(new Object[] { message, progressBar, note }, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, cancelOption, null) {

            private static final long serialVersionUID = 9197706111121400918L;

            @Override
            public int getMaxCharactersPerLineCount() {
                return 60;
            }
        };
        pane.addPropertyChangeListener(new PropertyChangeListener() {

            /**
			 * {@inheritDoc}
			 * 
			 * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
			 */
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if (evt.getSource() == panel && (JOptionPane.VALUE_PROPERTY.equals(propertyName) || JOptionPane.INPUT_VALUE_PROPERTY.equals(propertyName))) {
                    worker.cancel(true);
                }
            }
        });
        return pane;
    }

    protected void setProgress(int value) {
        if (value >= 100) {
            close();
        } else {
            if (panel != null) {
                progressBar.setValue(value);
            } else {
                panel = createProgressPane();
                dialog = panel.createDialog(parentComponent, UIManager.getString("ProgressMonitor.progressText"));
                dialog.setVisible(true);
            }
        }
    }

    protected void setMessage(String value) {
        this.message = value;
        if (panel != null) {
            panel.setMessage(message);
        }
    }

    public void close() {
        if (dialog != null) {
            dialog.setVisible(false);
            dialog.dispose();
            dialog = null;
            panel = null;
            progressBar = null;
        }
    }

    private class SwingWorkerProgressHandler implements PropertyChangeListener {

        /**
		 * {@inheritDoc}
		 * 
		 * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
		 */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            if ("progress".equals(propertyName)) {
                Integer progress = (Integer) evt.getNewValue();
                setProgress(progress);
            } else if ("state".equals(propertyName)) {
                SwingWorker.StateValue newState = (SwingWorker.StateValue) evt.getNewValue();
                if (SwingWorker.StateValue.STARTED == newState) {
                    setProgress(0);
                } else if (SwingWorker.StateValue.DONE == newState) {
                    setProgress(100);
                }
            } else if ("message".equals(propertyName)) {
                setMessage((String) evt.getNewValue());
            }
        }
    }
}
