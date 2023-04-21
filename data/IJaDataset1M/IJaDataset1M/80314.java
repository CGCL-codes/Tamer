package de.mse.mogwai.utils.erdesigner.dialogs;

import javax.swing.JFrame;

/**
 * @author Mirko Sertic
 */
public class SQLErrorDialog extends BaseEditor {

    private SQLErrorDialogView m_view;

    public static final int MODAL_RESULT_CANCELCOMPLETE = 200;

    /**
	 * This method initializes
	 * 
	 */
    public SQLErrorDialog() {
        this(null);
    }

    /**
	 * @param parent
	 */
    public SQLErrorDialog(JFrame parent) {
        super(parent);
        this.initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.m_view = new SQLErrorDialogView() {

            public void handleIgnoreButtonActionPerformed(String actionCommand) {
                setModalResult(MODAL_RESULT_CANCEL);
            }

            public void handleExecuteAgainButtonActionPerformed(String actionCommand) {
                setModalResult(MODAL_RESULT_OK);
            }

            public void handleStopButtonActionPerformed(String actionCommand) {
                setModalResult(MODAL_RESULT_CANCELCOMPLETE);
            }
        };
        this.setContentPane(this.m_view);
        this.setTitle("Error in SQL statement");
        this.pack();
        this.setResizable(false);
    }

    public void setStatement(String statement) {
        this.m_view.getSQLStatement().setText(statement);
    }

    public void setError(Exception exception) {
        this.m_view.getErrorMessage().setText(exception.toString());
    }
}
