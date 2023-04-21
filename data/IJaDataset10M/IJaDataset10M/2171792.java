package org.sss.eibs.design;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

/**
 * 选取Web发布所需资源文件
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 519 $ $Date: 2009-09-19 01:42:00 -0400 (Sat, 19 Sep 2009) $
 */
public class ShellWebResource extends AbstractShell {

    static final Log log = LogFactory.getLog(ShellWebResource.class);

    private Tree tree;

    private XMLConfiguration config = new XMLConfiguration();

    private List<Data> list = new ArrayList<Data>();

    private File cfgFile;

    public ShellWebResource(Shell parent, File cfgFile) {
        super(parent);
        this.cfgFile = cfgFile;
        readConfiguration();
        createContents();
    }

    protected void createContents() {
        setText("Resource setting");
        setSize(346, 375);
        final Composite composite = new Composite(this, SWT.NONE);
        composite.setBounds(0, 0, 338, 348);
        final TreeViewer treeViewer = new TreeViewer(composite, SWT.BORDER);
        treeViewer.setContentProvider(new ContentProvider(list));
        treeViewer.setLabelProvider(new LabelProvider(list));
        treeViewer.setInput(new ArrayList());
        tree = treeViewer.getTree();
        tree.setBounds(0, 0, 338, 310);
        final Menu menu = new Menu(tree);
        tree.setMenu(menu);
        final MenuItem menuItemDelete = new MenuItem(menu, SWT.NONE);
        menuItemDelete.setText("Delete");
        final MenuItem menuItemTarget = new MenuItem(menu, SWT.NONE);
        menuItemTarget.setText("Target setting");
        final Button buttonSave = new Button(composite, SWT.NONE);
        buttonSave.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                saveConfiguration();
                ShellWebResource.this.close();
            }
        });
        buttonSave.setText("Save");
        buttonSave.setBounds(248, 316, 80, 22);
        final Button buttonAdd = new Button(composite, SWT.NONE);
        buttonAdd.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                File file = new File(directory(""));
                if (file != null) list.add(new Data(file.getName(), file));
                treeViewer.setInput(new ArrayList());
            }
        });
        buttonAdd.setText("Add");
        buttonAdd.setBounds(162, 316, 80, 22);
    }

    private void readConfiguration() {
        try {
            config.clear();
            config.setValidating(false);
            config.load(cfgFile);
            if (!config.isEmpty()) {
            }
        } catch (Exception e) {
            log.info("Read configuration error.", e);
        }
    }

    private void saveConfiguration() {
        try {
            config.save();
        } catch (Exception e) {
            log.info("Save configuration error.", e);
        }
    }

    class ContentProvider implements ITreeContentProvider {

        List<Data> list;

        public ContentProvider(List<Data> list) {
            this.list = list;
        }

        public Object[] getChildren(Object data) {
            if (data instanceof Data && ((Data) data).file.isDirectory()) return ((Data) data).file.listFiles((FileFilter) FileFilterUtils.makeSVNAware(null));
            if (data instanceof File && ((File) data).isDirectory()) return ((File) data).listFiles((FileFilter) FileFilterUtils.makeSVNAware(null));
            return null;
        }

        public Object getParent(Object data) {
            return null;
        }

        public boolean hasChildren(Object data) {
            if (data instanceof Data && ((Data) data).file.isDirectory()) return true;
            if (data instanceof File && ((File) data).isDirectory()) return true;
            return false;
        }

        public Object[] getElements(Object data) {
            return list.toArray();
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object data1, Object data2) {
        }
    }

    class LabelProvider implements ILabelProvider {

        List<Data> list;

        public LabelProvider(List<Data> list) {
            this.list = list;
        }

        public Image getImage(Object data) {
            if (data instanceof Data && ((Data) data).file.isDirectory()) return SWTResourceManager.getImage(LabelProvider.class, "/ruleIcon.gif");
            if (data instanceof File && ((File) data).isDirectory()) return SWTResourceManager.getImage(LabelProvider.class, "/ruleIcon.gif");
            return SWTResourceManager.getImage(LabelProvider.class, "/ruleDataIcon.gif");
        }

        public String getText(Object data) {
            if (data instanceof File) return ((File) data).getName();
            return data.toString();
        }

        public void addListener(ILabelProviderListener arg0) {
        }

        public void dispose() {
        }

        public boolean isLabelProperty(Object arg0, String arg1) {
            return false;
        }

        public void removeListener(ILabelProviderListener arg0) {
        }
    }

    class Data {

        String target;

        File file;

        public Data(String target, File file) {
            this.target = target;
            this.file = file;
        }

        @Override
        public String toString() {
            return new StringBuffer(file.getAbsolutePath()).append(" --> ").append(target).toString();
        }
    }
}
