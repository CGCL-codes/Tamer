package mdarte.view.teamwork;

import java.util.ArrayList;
import java.util.Collection;
import mdarte.model.Module;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ChooseModelView extends Dialog {

    protected Object result;

    protected Shell shell;

    private Button btnOk;

    private Button btnCancel;

    private ScrolledComposite scrolledComposite_generic;

    private Composite composite_modules;

    private final String WINDOW_NAME = "Escolha os Módulos";

    /**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
    public ChooseModelView(Shell parent, int style) {
        super(parent, style);
        setText(WINDOW_NAME);
    }

    /**
	 * Create the dialog.
	 * @param parent
	 */
    public ChooseModelView(Shell parent) {
        super(parent);
        setText(WINDOW_NAME);
    }

    /**
	 * Open the dialog.
	 * @return the result
	 */
    public Object open() {
        this.createContents();
        return this.view();
    }

    public Object view() {
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    public void close() {
        shell.close();
    }

    /**
	 * Create contents of the dialog.
	 */
    public void createContents() {
        shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX);
        shell.setSize(243, 323);
        shell.setText(getText());
        Composite composite_generic = new Composite(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        Composite composite_buttons = new Composite(shell, SWT.NONE);
        GroupLayout gl_shell = new GroupLayout(shell);
        gl_shell.setHorizontalGroup(gl_shell.createParallelGroup(GroupLayout.TRAILING).add(gl_shell.createSequentialGroup().addContainerGap().add(gl_shell.createParallelGroup(GroupLayout.TRAILING).add(GroupLayout.LEADING, composite_generic, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE).add(GroupLayout.LEADING, composite_buttons, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)).addContainerGap()));
        gl_shell.setVerticalGroup(gl_shell.createParallelGroup(GroupLayout.LEADING).add(gl_shell.createSequentialGroup().addContainerGap().add(composite_generic, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE).add(18).add(composite_buttons, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        scrolledComposite_generic = new ScrolledComposite(composite_generic, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite_generic.setBounds(0, 0, 217, 209);
        scrolledComposite_generic.setLayout(new RowLayout(SWT.VERTICAL));
        scrolledComposite_generic.setExpandHorizontal(true);
        scrolledComposite_generic.setExpandVertical(true);
        composite_modules = new Composite(scrolledComposite_generic, SWT.NONE);
        composite_modules.setLayout(new RowLayout(SWT.VERTICAL));
        scrolledComposite_generic.setContent(composite_modules);
        scrolledComposite_generic.setMinSize(composite_modules.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        btnCancel = new Button(composite_buttons, SWT.NONE);
        btnCancel.setBounds(110, 10, 92, 29);
        btnCancel.setText("Cancel");
        btnOk = new Button(composite_buttons, SWT.NONE);
        btnOk.setBounds(10, 10, 92, 29);
        btnOk.setText("Ok");
        composite_buttons.setTabList(new Control[] { btnOk, btnCancel });
        shell.setLayout(gl_shell);
        shell.setTabList(new Control[] { composite_buttons, composite_generic });
    }

    public void createContents(String[] modules) {
        this.createContents();
        for (String moduleName : modules) {
            Button button = new Button(composite_modules, SWT.CHECK);
            button.setText(moduleName);
        }
        scrolledComposite_generic.setContent(composite_modules);
        scrolledComposite_generic.setMinSize(composite_modules.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    public void setButtonOkListener(Listener listener) {
        btnOk.addListener(SWT.Selection, listener);
    }

    public void setButtonCancelListener(Listener listener) {
        btnCancel.addListener(SWT.Selection, listener);
    }

    public Collection<Module> getSelections() {
        Collection<Module> models = new ArrayList<Module>();
        for (Control button : (Control[]) composite_modules.getChildren()) {
            Module model = new Module();
            model.setName(((Button) button).getText());
            model.setSelected(((Button) button).getSelection());
            models.add(model);
        }
        return models;
    }
}
