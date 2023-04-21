package net.confex.groovy.model;

import net.confex.directedit.NodePropertyDialog;
import net.confex.translations.Translator;
import net.confex.tree.ITreeNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class GroovyCompositePropertyDialog extends NodePropertyDialog {

    protected Text header = null;

    protected Text content = null;

    protected Text footer = null;

    protected Text fulltext = null;

    GroovyCompositeNode element;

    public GroovyCompositePropertyDialog(Shell shell) {
        super(shell, null);
    }

    public void setTreeNode(ITreeNode element) {
        super.setTreeNode(element);
        if (!(element instanceof GroovyCompositeNode)) {
            System.err.println("!!! Parametr element must be instanceof GroovyCompositeNode]");
            return;
        }
        this.element = (GroovyCompositeNode) element;
        this.header.setText(this.element.getHeader());
        this.content.setText(this.element.getFullContentText());
        this.footer.setText(this.element.getFooter());
        this.fulltext.setText(this.element.getFullText());
    }

    protected void initLabel() {
        super.initLabel();
        label_src_text.setText(Translator.getString("LABEL_GROOVY"));
        sShell.setSize(new Point(PROPERTY_DIALOG_WIDTH, PROPERTY_DIALOG_HEIGHT + 400));
    }

    protected void initActions() {
        super.initActions();
        initActionsSrcFileButtons();
    }

    protected void createPart1SShell() {
        {
            label_tooltip = new Label(sShell, SWT.NONE);
            label_tooltip.setText("Tooltip:");
            tooltip = new Text(sShell, SWT.BORDER);
            GridData gridData = new GridData();
            gridData.horizontalAlignment = GridData.FILL;
            gridData.grabExcessHorizontalSpace = true;
            gridData.verticalAlignment = GridData.CENTER;
            tooltip.setLayoutData(gridData);
        }
        createTestFilePartSShell();
    }

    /**
	 * Get default directory for src_file_name
	 * @see net.confex.directedit.NodePropertyDialog#getDefaultSrcDir()
	 */
    public String getDefaultSrcDir() {
        return "/groovy/";
    }

    protected void prepareRetOk(ITreeNode element) {
        super.prepareRetOk(element);
        GroovyCompositeNode tree_node = (GroovyCompositeNode) element;
        tree_node.setHeader(header.getText());
        tree_node.setFooter(footer.getText());
    }

    protected void createPart2SShell() {
        label_src_text = new Label(sShell, SWT.NONE);
        GridData gridData = new GridData(GridData.FILL_VERTICAL);
        label_src_text.setLayoutData(gridData);
        TabFolder tf = new TabFolder(sShell, SWT.BORDER);
        GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData2.verticalSpan = 4;
        tf.setLayoutData(gridData2);
        TabItem ti1 = new TabItem(tf, SWT.BORDER);
        ti1.setText("header");
        Composite c1 = new Composite(tf, SWT.BORDER);
        c1.setLayout(new FillLayout());
        header = new Text(c1, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        ti1.setControl(c1);
        TabItem ti2 = new TabItem(tf, SWT.BORDER);
        ti2.setText("content");
        Composite c2 = new Composite(tf, SWT.BORDER);
        c2.setLayout(new FillLayout());
        content = new Text(c2, SWT.READ_ONLY | SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        ti2.setControl(c2);
        TabItem ti3 = new TabItem(tf, SWT.BORDER);
        ti3.setText("footer");
        Composite c3 = new Composite(tf, SWT.BORDER);
        c3.setLayout(new FillLayout());
        footer = new Text(c3, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        ti3.setControl(c3);
        TabItem ti4 = new TabItem(tf, SWT.BORDER);
        ti4.setText("full text");
        Composite c4 = new Composite(tf, SWT.BORDER);
        c4.setLayout(new FillLayout());
        fulltext = new Text(c4, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        ti4.setControl(c4);
        createSrcButtonPartSShell();
    }
}
