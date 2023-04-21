package ch.bgaechter.wtlw;

import java.util.ArrayList;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class QuizNameDialog extends Dialog {

    private Text text;

    private ArrayList<String> questions = new ArrayList<String>();

    private ArrayList<String> answers = new ArrayList<String>();

    private DataAccess dA;

    /**
	 * Create the dialog.
	 * @param parentShell
	 */
    public QuizNameDialog(Shell parentShell, ArrayList<String> q, ArrayList<String> a) {
        super(parentShell);
        questions = q;
        answers = a;
    }

    /**
	 * Create contents of the dialog.
	 * @param parent
	 */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        Label lblEnterAQuiz = new Label(container, SWT.NONE);
        lblEnterAQuiz.setText("Enter a Quiz name");
        text = new Text(container, SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        return container;
    }

    /**
	 * Create contents of the button bar.
	 * @param parent
	 */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseUp(MouseEvent e) {
                dA = new DataAccess();
                dA.setAnswers(answers);
                dA.setQuestions(questions);
                dA.createNewQuiz(text.getText().toString());
            }
        });
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
	 * Return the initial size of the dialog.
	 */
    @Override
    protected Point getInitialSize() {
        return new Point(450, 150);
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
