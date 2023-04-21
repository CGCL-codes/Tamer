package net.sourceforge.eclipsex.ui.widgets;

import net.sourceforge.eclipsex.projectproperties.EXResourceContentProvider;
import net.sourceforge.eclipsex.projectproperties.EXResourceSelectionDialog;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class FileResourceWidget extends Composite {

    private final Text _text;

    public FileResourceWidget(final Composite parent, final String labelText, final IContainer root, final EXResourceContentProvider provider, final boolean write, final int labelWidth, final int textWidth) {
        super(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        setLayout(layout);
        Label label = new Label(this, 0);
        label.setText(labelText);
        GridData data = new GridData(GridData.FILL);
        data.minimumWidth = SWT.DEFAULT;
        data.widthHint = labelWidth;
        label.setLayoutData(data);
        _text = new Text(this, 0);
        data = new GridData(SWT.FILL, SWT.TOP, true, false);
        data.minimumWidth = SWT.DEFAULT;
        data.widthHint = textWidth;
        _text.setLayoutData(data);
        _text.setEnabled(write);
        Button button = new Button(this, 0);
        button.setText("...");
        button.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(final SelectionEvent e) {
            }

            public void widgetSelected(final SelectionEvent e) {
                EXResourceSelectionDialog d = new EXResourceSelectionDialog(getShell(), labelText, root, provider, write);
                IResource res = d.open();
                if (res != null) {
                    _text.setText(res.getFullPath().removeFirstSegments(1).makeRelative().toString());
                }
            }
        });
    }

    public String getText() {
        return _text.getText();
    }

    public void setText(final String text) {
        _text.setText(text);
    }
}
