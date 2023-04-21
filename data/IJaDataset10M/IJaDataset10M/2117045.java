package tripleo.cottontail.viewer;

import java.util.List;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import tripleo.cottontail.objects.V.*;

class VersionViewHelper extends LabelProvider implements IColorProvider, ITreeContentProvider, ISelectionChangedListener, IOpenListener, IDoubleClickListener {

    private final ControlExample mExample;

    private ListViewer vvh;

    public VersionViewHelper(ControlExample aExample) {
        mExample = aExample;
    }

    private ResourceObject find_domain_object(Object element) {
        return ResourceViewHelper.keychain.get(element);
    }

    public Object[] getChildren(Object element) {
        final ResourceObject fcwe = find_domain_object(element);
        if (fcwe != null) return fcwe.getKids(); else return new Object[0];
    }

    public Object[] getElements(Object element) {
        return getChildren(element);
    }

    public boolean hasChildren(Object element) {
        return getChildren(element).length > 0;
    }

    public Object getParent(Object element) {
        final ResourceObject fcwe = find_domain_object(element);
        return fcwe.kid().getParent();
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object old_input, Object new_input) {
        int y = 2;
    }

    public String getText(Object element) {
        final ResourceObject fcwe = find_domain_object(element);
        return fcwe.kid().getName();
    }

    public Image getImage(Object element) {
        final ResourceObject fcwe = find_domain_object(element);
        final String s = fcwe.kid().isDirectory() ? "folder" : "file";
        return Util.getImageRegistry().get(s);
    }

    public Color getForeground(Object element) {
        return null;
    }

    public Color getBackground(Object element) {
        Color C;
        final ResourceObject fcwe = find_domain_object(element);
        if (fcwe.isOdd()) C = new Color(Display.getCurrent(), 0xff, 0, 0); else C = new Color(Display.getCurrent(), 0, 0xff, 0);
        return C;
    }

    Object initial() {
        return mExample.rvh.getSelection();
    }

    ListViewer make_lv(final Composite aParent) {
        return vvh = make_lv(aParent, initial(), mExample);
    }

    private ListViewer make_lv(final Composite aParent, final Object aInitial, final ControlExample aP) {
        ListViewer R = new ListViewer(aParent);
        R.setContentProvider(this);
        R.setLabelProvider(this);
        R.setInput(aInitial);
        R.getControl().setSize(-1, -1);
        R.addSelectionChangedListener(this);
        R.addOpenListener(this);
        return R;
    }

    public void open(OpenEvent event) {
        final ISelection selection = event.getSelection();
        final ResourceObject fcw = ResourceViewHelper.keychain.get(selection);
        System.err.println("200 " + fcw.getContentText());
        mExample.stv.setText(fcw.getContentText());
    }

    public void selectionChanged(SelectionChangedEvent event) {
        final StructuredSelection selection = (StructuredSelection) event.getSelection();
        final Object o = ResourceViewHelper.keychain.get(selection.getFirstElement());
        FileContentWrapper fcw = (FileContentWrapper) o;
        System.out.println("210 " + fcw.getContentText());
        mExample.stv.setText("" + fcw.getContentText());
    }

    public void doubleClick(DoubleClickEvent event) {
        final ISelection selection = event.getSelection();
        final ResourceObject fcw = ResourceViewHelper.keychain.get(selection);
        System.err.println("200 " + fcw.getContentText());
        mExample.stv.setText(fcw.getContentText());
    }

    public void changeTo(Object anObject) {
        ResourceObject res = ResourceViewHelper.keychain.get(anObject);
        List<VersionObject> verslist = res.getVersions();
        vvh.setInput(anObject);
    }
}
