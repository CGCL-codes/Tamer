package astcentric.editor.eclipse.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import astcentric.editor.common.dialog.TextField;

/**
 * Factory of a widget based on a {@link TextField}.
 */
class TextFieldComponentFactory implements FieldComponentFactory<TextField> {

    public Control create(Composite composite, final TextField field) {
        final Text text = new Text(composite, SWT.SINGLE);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        Object value = field.getValue();
        if (value != null) {
            text.setText(value.toString());
        }
        text.addKeyListener(new KeyListener() {

            public void keyReleased(KeyEvent e) {
                field.setValue(text.getText());
            }

            public void keyPressed(KeyEvent e) {
            }
        });
        return text;
    }
}
