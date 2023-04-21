package org.xulbooster.eclipse.ui.utils.form.rows;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public abstract class AbstractRow {

    protected String id = "";

    protected GridLayout gl = null;

    protected Control labelControl;

    protected IEditOccuredSignal signal;

    protected String path;

    public String getXmlPath() {
        return path;
    }

    public void setEditOccuredSignal(IEditOccuredSignal aSignal) {
        signal = aSignal;
    }

    public IEditOccuredSignal getEditOccuredSignal() {
        return signal;
    }

    protected void fillIntoGrid(Composite parent, int indent, Text text, Button browse) {
        Layout layout = parent.getLayout();
        if (layout instanceof GridLayout) {
            GridData gd;
            int span = ((GridLayout) layout).numColumns;
            gd = new GridData(GridData.VERTICAL_ALIGN_CENTER);
            gd.horizontalIndent = indent;
            labelControl.setLayoutData(gd);
            int tspan = browse != null ? span - 2 : span - 1;
            gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
            gd.horizontalSpan = tspan;
            gd.grabExcessHorizontalSpace = (tspan == 1);
            gd.widthHint = 10;
            text.setLayoutData(gd);
            if (browse != null) {
                gd = new GridData(GridData.VERTICAL_ALIGN_CENTER);
                browse.setLayoutData(gd);
            }
        } else if (layout instanceof TableWrapLayout) {
            TableWrapData td;
            int span = ((TableWrapLayout) layout).numColumns;
            td = new TableWrapData();
            td.valign = TableWrapData.MIDDLE;
            td.indent = indent;
            labelControl.setLayoutData(td);
            int tspan = browse != null ? span - 2 : span - 1;
            td = new TableWrapData(TableWrapData.FILL);
            td.colspan = tspan;
            td.grabHorizontal = (tspan == 1);
            td.valign = TableWrapData.MIDDLE;
            text.setLayoutData(td);
            if (browse != null) {
                td = new TableWrapData();
                td.valign = TableWrapData.MIDDLE;
                browse.setLayoutData(td);
            }
        }
    }

    protected abstract void addListeners();

    protected void editOccured(ModifyEvent e) {
        if (signal != null) {
            signal.editOccured(this);
        }
    }

    protected void hyperLinkActivated(HyperlinkEvent e) {
        if (signal != null) {
            signal.hyperLinkactivated((HyperLinkRow) this);
        }
    }

    public void setXmlPath(String aPath) {
        this.path = aPath;
    }

    public abstract String getValue();

    public abstract void setValue(String aValue);

    public void setValue(DocXML aDoc) {
        setValue(aDoc.getValue(this.path));
    }

    public DocXML getValue(DocXML aDoc) {
        aDoc.setValue(this.path, getValue());
        return aDoc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
