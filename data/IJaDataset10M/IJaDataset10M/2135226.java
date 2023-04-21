package info.metlos.jcdc.widgets;

import java.util.Set;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * This is a simple dialog that ask the user a question and show several buttons
 * as a possible answer to that question. Buttons are entered as a set of
 * {@link DialogResult}s and the return value of the {@link #open()} method
 * corresponds to one of them.
 * 
 * @author metlos
 * 
 * @version $Id: QuestionDialog.java 94 2007-06-25 01:27:47Z metlos $
 */
public class QuestionDialog extends Dialog {

    private Set<DialogResult> buttons;

    private String question;

    public QuestionDialog(Shell parent, int style) {
        super(parent, SWT.DIALOG_TRIM | style);
    }

    public QuestionDialog(Shell parent) {
        super(parent);
    }

    private class DialogContent extends Composite {

        private Label lblQuestion = null;

        public DialogResult result = DialogResult.none;

        public DialogContent(Composite parent, int style) {
            super(parent, style);
            initialize();
        }

        private void initialize() {
            final DialogContent me = this;
            if (buttons != null) {
                GridData gridData = new GridData();
                gridData.grabExcessHorizontalSpace = true;
                gridData.horizontalSpan = buttons.size();
                GridLayout gridLayout = new GridLayout();
                gridLayout.numColumns = buttons.size();
                lblQuestion = new Label(this, SWT.NONE);
                lblQuestion.setText(question != null ? question : "");
                lblQuestion.setLayoutData(gridData);
                boolean first = true;
                for (final DialogResult res : buttons) {
                    GridData gd = new GridData();
                    gd.horizontalAlignment = GridData.END;
                    if (first) {
                        gd.grabExcessHorizontalSpace = true;
                        first = false;
                    }
                    Button btn = new Button(this, SWT.NONE);
                    btn.setText(res.toString());
                    btn.setLayoutData(gd);
                    btn.addSelectionListener(new SelectionListener() {

                        public void widgetDefaultSelected(SelectionEvent arg0) {
                        }

                        public void widgetSelected(SelectionEvent arg0) {
                            result = res;
                            me.getShell().close();
                        }
                    });
                }
                this.setLayout(gridLayout);
                this.setSize(new Point(300, 55));
            }
        }
    }

    /**
	 * @return the question
	 */
    public String getQuestion() {
        return question;
    }

    /**
	 * @param question
	 *            the question to set
	 */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
	 * @return the buttons that will be shown
	 */
    public Set<DialogResult> getButtons() {
        return buttons;
    }

    /**
	 * @param buttons
	 *            the buttons to be shown as possible answers to the question.
	 */
    public void setButtons(Set<DialogResult> buttons) {
        this.buttons = buttons;
    }

    @Override
    public DialogResult open() {
        DialogContent cnt = new DialogContent(getDialogShell(), SWT.NONE);
        getDialogShell().pack();
        run();
        return cnt.result;
    }
}
