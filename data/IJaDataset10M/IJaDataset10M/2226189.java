package de.beas.explicanto.client.rcp.pageeditor;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import de.bea.services.vidya.client.datastructures.CPage;
import de.beas.explicanto.client.I18N;

public class TextContribution extends ControlContribution {

    private Text titleText, styleText, layoutText;

    private final CPage page;

    private PageEditor parentEditor;

    protected TextContribution(String id, CPage page, PageEditor editor) {
        super(id);
        this.page = page;
        this.parentEditor = editor;
    }

    protected Control createControl(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setLayout(new RowLayout());
        Label label = new Label(comp, SWT.NONE);
        label.setText("  " + I18N.translate("pageEditor.label.pageTitle") + "  ");
        titleText = new Text(comp, SWT.BORDER);
        titleText.setText(page.getPageTitle());
        RowData data = new RowData();
        data.width = 120;
        titleText.setLayoutData(data);
        titleText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                page.setPageTitle(titleText.getText());
                parentEditor.setModified(true);
            }
        });
        label = new Label(comp, SWT.NONE);
        label.setText("  " + I18N.translate("pageEditor.label.style") + "  ");
        styleText = new Text(comp, SWT.BORDER);
        styleText.setEditable(false);
        styleText.setText("Style                          ");
        label = new Label(comp, SWT.NONE);
        label.setText("  " + I18N.translate("pageEditor.label.layout") + "  ");
        layoutText = new Text(comp, SWT.BORDER);
        layoutText.setEditable(false);
        layoutText.setText(page.getPageLayoutType());
        return comp;
    }

    public void setTitle(String title) {
        titleText.setText(title);
    }

    public String getTitle() {
        return titleText.getText();
    }

    public void setStyle(String style) {
        styleText.setText(style);
    }

    public void setLayout(String layout) {
        layoutText.setText(layout);
    }
}
