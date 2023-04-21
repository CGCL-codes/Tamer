package net.confex.directedit;

import net.confex.translations.Translator;
import net.confex.tree.ITreeNode;
import net.confex.tree.UrlNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class UrlPropertyDialog extends NodePropertyDialog {

    protected Label label_url = null;

    protected Text url = null;

    UrlNode element;

    public UrlPropertyDialog(Shell shell) {
        super(shell, (ITreeNode) null);
    }

    public void setTreeNode(ITreeNode element) {
        super.setTreeNode(element);
        if (!(element instanceof UrlNode)) {
            System.err.println("!!! Parametr element must be instanceof UrlTreeObjectNode [BrowserUrlBookmarkPropertyDialog.setNodeElement()]");
            return;
        }
        this.element = (UrlNode) element;
        this.url.setText(this.element.getPath());
    }

    protected void prepareRetOk(ITreeNode element) {
        super.prepareRetOk(element);
        UrlNode tree_node = (UrlNode) element;
        tree_node.setUrl(url.getText());
    }

    protected void createPart1SShell() {
        label_url = new Label(sShell, SWT.NONE);
        label_url.setText("Url:");
        url = new Text(sShell, SWT.BORDER);
        GridData url_gridData = new GridData();
        url_gridData.horizontalAlignment = GridData.FILL;
        url_gridData.grabExcessHorizontalSpace = true;
        url_gridData.verticalAlignment = GridData.CENTER;
        url.setLayoutData(url_gridData);
    }

    protected void initLabel() {
        super.initLabel();
        label_url.setText(Translator.getString("LABEL_URL_PATH"));
        sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT + 20));
    }
}
