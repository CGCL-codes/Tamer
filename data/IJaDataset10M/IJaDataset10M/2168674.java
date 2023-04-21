package edu.thu.keg.iw.app.description.ui.editor.otherinfo.actions;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.forms.AbstractFormPart;
import edu.thu.keg.iw.app.description.model.AppModel;
import edu.thu.keg.iw.app.description.model.IHasChildAppModel;
import edu.thu.keg.iw.app.description.util.ImageCache;
import edu.thu.keg.iw.app.description.util.ImageKey;

public class OtherInfoTreeAddAction extends OtherInfoTreeAbstractAction {

    public OtherInfoTreeAddAction(TreeViewer treeViewer, AbstractFormPart formPart) {
        super(treeViewer, formPart);
        setText("����");
        setImageDescriptor(ImageCache.getImageDescriptor(ImageKey.IMG_TREE_ADD));
    }

    public void run() {
        IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        IHasChildAppModel parent = (IHasChildAppModel) selection.getFirstElement();
        AppModel child = parent.createChild();
        parent.addChild(child);
        treeViewer.add(parent, child);
        treeViewer.setSelection(new TreeSelection(new TreePath(new Object[] { parent, child })));
        formPart.markDirty();
    }

    @Override
    protected boolean calculateEnabled() {
        IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        AppModel entry = (AppModel) selection.getFirstElement();
        return entry instanceof IHasChildAppModel;
    }
}
