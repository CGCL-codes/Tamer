package com.comarch.depth.importer;

import java.util.List;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import depth.parser.registry.ParserConfig;

public class ParserChooserDialog extends TrayDialog {

    private String filename;

    private List<ParserConfig> parserList;

    private boolean showUseForAll;

    private String parserID;

    private boolean useForAll;

    public ParserChooserDialog(Shell parent, String fileName, List<ParserConfig> list, boolean showUseForAll) {
        super(parent);
        this.parserList = list;
        this.filename = fileName;
        this.showUseForAll = showUseForAll;
        this.setShellStyle(SWT.RESIZE | SWT.APPLICATION_MODAL | SWT.TITLE);
        this.create();
        this.getShell().setSize(400, 300);
    }

    @Override
    protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
        if (id != IDialogConstants.CANCEL_ID) return super.createButton(parent, id, label, defaultButton);
        return null;
    }

    @Override
    protected Control createDialogArea(Composite composite) {
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        composite.setLayoutData(gd);
        GridLayout layout = new GridLayout(1, false);
        composite.setLayout(layout);
        Label text = new Label(composite, SWT.NONE);
        text.setText("Choose parser for file \"" + filename + "\"");
        org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(composite, SWT.SINGLE);
        for (ParserConfig pc : parserList) {
            String item = pc.getName();
            if (item == null) item = pc.getId();
            list.add(item);
        }
        list.select(0);
        parserID = parserList.get(0).getId();
        list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        list.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                org.eclipse.swt.widgets.List l = (org.eclipse.swt.widgets.List) e.widget;
                parserID = parserList.get(l.getSelectionIndex()).getId();
            }
        });
        list.addListener(SWT.MouseDoubleClick, new Listener() {

            public void handleEvent(Event event) {
                org.eclipse.swt.widgets.List l = (org.eclipse.swt.widgets.List) event.widget;
                parserID = parserList.get(l.getSelectionIndex()).getId();
                ParserChooserDialog.this.close();
            }
        });
        if (showUseForAll) {
            Button button = new Button(composite, SWT.CHECK);
            button.setText("Do not ask again.");
            button.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    Button b = (Button) e.widget;
                    useForAll = b.getSelection();
                }
            });
        }
        this.getShell().setText("Choose Parser");
        return composite;
    }

    public boolean useForAll() {
        return useForAll;
    }

    public String getParserID() {
        return parserID;
    }
}
