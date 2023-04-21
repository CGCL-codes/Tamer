package net.confex.customxml;

import net.confex.customxml.java3D.CustomXmlJava3D;
import net.confex.directedit.NodePropertyDialog;
import net.confex.tree.ITreeNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CustomXmlNodePropertyDialog extends NodePropertyDialog {

    protected Label label_xustom_xml = null;

    protected Text custom_xml = null;

    CustomXmlNode element;

    public CustomXmlNodePropertyDialog(Shell shell) {
        super(shell, null);
    }

    public void setTreeNode(ITreeNode element) {
        super.setTreeNode(element);
        if (!(element instanceof CustomXmlNode)) {
            System.err.println("!!! Parametr element must be instanceof CustomXmlNode ");
            return;
        }
        this.element = (CustomXmlNode) element;
        String s = this.element.getCustomXml();
        this.custom_xml.setText(s);
    }

    protected void prepareRetOk(ITreeNode element) {
        super.prepareRetOk(element);
        CustomXmlNode tree_node = (CustomXmlNode) element;
        tree_node.setCustomXml(custom_xml.getText());
    }

    protected void initLabel() {
        super.initLabel();
        sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH + 20, PROPERTY_DIALOG_HEIGHT + 80));
    }

    protected void createPart1SShell() {
        label_tooltip = new Label(sShell, SWT.NONE);
        label_tooltip.setText("Tooltip:");
        tooltip = new Text(sShell, SWT.BORDER);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.CENTER;
        tooltip.setLayoutData(gridData);
    }

    protected void createPart2SShell() {
        label_xustom_xml = new Label(sShell, SWT.NONE);
        label_xustom_xml.setText("CustomXml:");
        GridData gridData = new GridData(GridData.FILL_VERTICAL);
        label_xustom_xml.setLayoutData(gridData);
        custom_xml = new Text(sShell, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
        GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
        gridData2.verticalSpan = 4;
        custom_xml.setLayoutData(gridData2);
        gridData2.heightHint = 40;
    }
}
