package org.sss.eibs.design;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.sss.eibs.design.utils.DesignerUtil;

/**
 * 记录父窗口属性的窗口基类
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 533 $ $Date: 2009-09-25 11:22:19 -0400 (Fri, 25 Sep 2009) $
 */
public class AbstractShell extends Shell {

    public static final Widget[] EMPTY_WIDGETS = new Widget[] {};

    protected static final VerifyListener numericVerifier = new Verifier("[0-9]\\d*");

    protected static final VerifyListener nameVerifier = new Verifier("[a-z][a-z0-9]*");

    protected static final VerifyListener tableNameVerifier = new Verifier("[a-z][_\\,a-z0-9]*");

    protected static final VerifyListener columnNameVerifier = new Verifier("[a-z][\\.a-z0-9]*");

    protected static final IInputValidator nameValidator = new Validator("[a-z][a-z0-9]*");

    protected static final IInputValidator indexNameValidator = new Validator("[a-z][_a-z0-9]*");

    protected Shell parent;

    protected boolean modified = false;

    protected boolean changed = false;

    protected Listener updateListener;

    private Widget[] widgets = EMPTY_WIDGETS;

    private ModifyListener modifyListener;

    private SelectionAdapter selectionAdapter;

    public AbstractShell(Shell parent) {
        this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
    }

    public AbstractShell(Shell parent, int type) {
        super(Display.getCurrent(), type);
        if (parent != null) this.parent = parent; else this.parent = Display.getCurrent().getActiveShell();
        updateListener = new Listener() {

            public void handleEvent(Event event) {
                updateChanged();
            }
        };
    }

    @Override
    public void setText(String title) {
        if (title != null) super.setText(title);
    }

    protected void setText(Text text, String value) {
        if (value != null) text.setText(value);
    }

    protected void setText(Label label, String value) {
        if (value != null) label.setText(value);
    }

    protected void setText(Combo combo, String value) {
        if (value != null) combo.setText(value);
    }

    protected void addUpdateListener(Widget[] widgets) {
        addUpdateListener(widgets, this);
    }

    protected void addUpdateListener(Widget[] widgets, Control widget) {
        this.widgets = widgets;
        this.addShellListener(new ShellAdapter() {

            @Override
            public void shellClosed(ShellEvent event) {
                event.doit = !changed || confirm("Data changed! Exit?");
                if (event.doit && modified && parent instanceof AbstractShell) ((AbstractShell) parent).updateChanged();
            }
        });
        modifyListener = new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                updateChanged();
            }
        };
        selectionAdapter = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                updateChanged();
            }
        };
        if (widget != null) addUpdateListener(widget);
    }

    protected void addUpdateListener(Control widget) {
        if (widget instanceof Text) ((Text) widget).addModifyListener(modifyListener); else if (widget instanceof Combo) {
            ((Combo) widget).addSelectionListener(selectionAdapter);
            ((Combo) widget).addModifyListener(modifyListener);
        } else if (widget instanceof Button) {
            int style = widget.getStyle();
            if ((style & SWT.RADIO) != 0 || (style & SWT.CHECK) != 0) ((Button) widget).addSelectionListener(selectionAdapter);
        } else if (widget instanceof Spinner) ((Spinner) widget).addModifyListener(modifyListener); else if (widget instanceof Composite) {
            for (Control child : ((Composite) widget).getChildren()) addUpdateListener(child);
        }
    }

    protected void updateChanged() {
        if (!changed) {
            changed = true;
            modified = true;
            for (Widget widget : widgets) setEnable(widget, true);
        }
    }

    protected void updateParentChanged() {
        if (parent instanceof AbstractShell) ((AbstractShell) parent).updateChanged();
    }

    protected void resetAllChanged() {
        resetChanged(true);
        if (parent instanceof AbstractShell) ((AbstractShell) parent).resetChanged(true);
    }

    protected void resetChanged() {
        resetChanged(false);
    }

    protected void resetChanged(boolean flag) {
        if (changed) {
            changed = false;
            if (flag) modified = false;
            for (Widget widget : widgets) setEnable(widget, false);
        }
    }

    private void setEnable(Widget widget, boolean flag) {
        if (widget instanceof Control) ((Control) widget).setEnabled(flag); else if (widget instanceof MenuItem) ((MenuItem) widget).setEnabled(flag); else if (widget instanceof ToolItem) ((ToolItem) widget).setEnabled(flag);
    }

    protected void show(Shell shell) {
        DesignerUtil.show(shell, false);
    }

    protected void showChanged(AbstractShell shell) {
        DesignerUtil.show(shell, false);
        shell.updateChanged();
    }

    protected void error(String message) {
        MessageDialog.openError(this, "Error", message);
    }

    protected boolean confirm(String message) {
        return MessageDialog.openConfirm(this, "Warning", message);
    }

    protected String directory(String message) {
        return DesignerUtil.directory(this, message, null);
    }

    protected String input(String title, String message, String value, IInputValidator validator) {
        return DesignerUtil.input(this, title, message, value, validator);
    }

    @Override
    protected final void checkSubclass() {
    }

    static class Validator implements IInputValidator {

        Pattern pattern;

        String message;

        public Validator(String regex) {
            this(regex, "Must match " + regex + " !");
        }

        public Validator(String regex, String message) {
            this.pattern = Pattern.compile(regex);
            this.message = message;
        }

        public String isValid(String newText) {
            Matcher matcher = pattern.matcher(newText);
            if (matcher.matches()) return null;
            return message;
        }
    }

    static class Verifier implements VerifyListener {

        Pattern pattern;

        public Verifier(String regex) {
            this.pattern = Pattern.compile(regex);
        }

        public void verifyText(VerifyEvent e) {
            String text = null;
            Object o = e.getSource();
            if (o instanceof Text) text = ((Text) o).getText(); else if (o instanceof Combo) text = ((Combo) o).getText();
            if (text != null) text += e.text; else text = e.text;
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) e.doit = true; else if (e.text.length() > 0) e.doit = false; else e.doit = true;
        }
    }
}
