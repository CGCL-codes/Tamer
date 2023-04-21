package hu.gbalage.owl.editor.internal;

import hu.gbalage.owl.editor.IOWLEditorItem;
import hu.gbalage.owl.editor.OWLEditorManager;
import hu.gbalage.owl.editor.TabManager;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author balage
 *
 */
public class CompositeEditorItem extends Composite {

    /**
	 * @param parent
	 * @param style
	 */
    public CompositeEditorItem(Composite parent, IConfigurationElement element, OWLEditorManager manager, TabManager tabManager) {
        super(parent, SWT.NONE);
        if (element.getName().equals("item")) {
            this.setLayout(new FillLayout());
            try {
                IOWLEditorItem item = (IOWLEditorItem) element.createExecutableExtension("editorItem");
                new OWLEditorItemContainer(this, SWT.NONE, item, manager, tabManager);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        } else if (element.getName().equals("vertical")) {
            this.setLayout(new FormLayout());
            IConfigurationElement[] childs = element.getChildren();
            int n = Math.max(1, childs.length);
            int k = 100 / n;
            for (int i = 0; i < childs.length; i++) {
                FormData data = new FormData();
                data.top = new FormAttachment(0);
                data.bottom = new FormAttachment(100);
                data.left = new FormAttachment(i * k);
                data.right = new FormAttachment((i + 1) * k);
                CompositeEditorItem c = new CompositeEditorItem(this, childs[i], manager, tabManager);
                c.setLayoutData(data);
            }
        } else if (element.getName().equals("horizontal")) {
            this.setLayout(new FormLayout());
            IConfigurationElement[] childs = element.getChildren();
            int n = Math.max(1, childs.length);
            int k = 100 / n;
            for (int i = 0; i < childs.length; i++) {
                FormData data = new FormData();
                data.left = new FormAttachment(0);
                data.right = new FormAttachment(100);
                data.top = new FormAttachment(i * k);
                data.bottom = new FormAttachment((i + 1) * k);
                CompositeEditorItem c = new CompositeEditorItem(this, childs[i], manager, tabManager);
                c.setLayoutData(data);
            }
        }
    }
}
