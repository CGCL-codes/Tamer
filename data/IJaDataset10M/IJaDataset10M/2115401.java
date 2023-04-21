package filter.aspect;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.InvalidXPathException;
import org.dom4j.XPathException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.swt.widgets.Control;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import data.SchemaContainerSingleton;
import filter.ContainerCheckedTreeViewer;
import filter.MetadataTreeLabelProvider;

/**
 * @author Roy
 * @author Roman Rudenko
 */
public class AspectDialog extends Window {

    private Composite self;

    private AspectFilter filter;

    private ListViewer filterViewer;

    private ContainerCheckedTreeViewer treeViewer;

    private Text description;

    private Document descriptions;

    private Button done, cancel;

    public AspectDialog(Shell parent, AspectFilter filter) {
        super(parent);
        setShellStyle(getShellStyle() | SWT.PRIMARY_MODAL);
        this.filter = filter;
        descriptions = SchemaContainerSingleton.getInstance();
    }

    protected Control createContents(Composite parent) {
        parent.setSize(new Point(640, 480));
        self = parent;
        parent.setLayout(new FillLayout());
        SashForm sash = new SashForm(parent, SWT.NONE);
        Composite left = new Composite(sash, SWT.NONE);
        {
            GridLayout gridLayout = new GridLayout();
            gridLayout.marginHeight = 0;
            gridLayout.marginWidth = 0;
            left.setLayout(gridLayout);
        }
        {
            Label label = new Label(left, SWT.LEFT);
            label.setText("Check metadata elements to show:");
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
            label.setLayoutData(gridData);
        }
        {
            treeViewer = new ContainerCheckedTreeViewer(left);
            try {
                treeViewer.setLabelProvider(new MetadataTreeLabelProvider());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            treeViewer.setContentProvider(new AspectTreeContentProvider());
            GridData gridData = new GridData(GridData.FILL_BOTH);
            treeViewer.getTree().setLayoutData(gridData);
            load();
            treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

                public void selectionChanged(SelectionChangedEvent arg0) {
                    update();
                }
            });
        }
        Composite right = new Composite(sash, SWT.NONE);
        {
            GridLayout gridLayout = new GridLayout();
            gridLayout.marginHeight = 0;
            gridLayout.marginWidth = 0;
            gridLayout.numColumns = 2;
            right.setLayout(gridLayout);
        }
        {
            description = new Text(right, SWT.READ_ONLY | SWT.BORDER | SWT.WRAP);
            GridData gridData = new GridData(GridData.FILL_BOTH);
            gridData.horizontalSpan = 2;
            description.setLayoutData(gridData);
        }
        {
            done = new Button(right, SWT.NONE);
            done.setText("OK");
            getShell().setDefaultButton(done);
            done.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    Object[] checkedElements = treeViewer.getCheckedElements();
                    Object[] greyElements = treeViewer.getGrayedElements();
                    ArrayList elements = new ArrayList();
                    elements.addAll(Arrays.asList(checkedElements));
                    elements.removeAll(Arrays.asList(greyElements));
                    filter.setElements(elements);
                    filter.setGreyElements(Arrays.asList(greyElements));
                    close();
                }
            });
        }
        {
            cancel = new Button(right, SWT.NONE);
            cancel.setText("Cancel");
            cancel.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    close();
                }
            });
        }
        getShell().addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.ESC) {
                    cancel.setSelection(true);
                }
            }
        });
        return sash;
    }

    private void update() {
        Object selection = ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
        if (selection != null) {
            description.setText(((Element) selection).valueOf("@desc"));
        }
    }

    private void load() {
        Element root = descriptions.getRootElement();
        if (root != null) {
            try {
                List result = root.selectNodes("//lom/*");
                treeViewer.setInput(result);
                List elements = filter.getElements();
                if (elements != null) treeViewer.setCheckedElements(elements.toArray());
            } catch (InvalidXPathException ex) {
                MessageDialog.openError(self.getShell(), "Invalid XPath expression", ex.getMessage());
            } catch (XPathException ex) {
                MessageDialog.openError(self.getShell(), "Invalid XPath expression", ex.getMessage());
            }
        } else treeViewer.setInput(root);
    }
}
