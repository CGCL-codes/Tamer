package importer;

import importer.models.common.Track;
import importer.services.itunes.Itunes;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

public class ItunesView extends ViewPart {

    public static final String ID = "itunes_tagger.view";

    private CheckboxTableViewer viewer;

    Itunes finder = Itunes.getInstance();

    class TrackInfoContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            return finder.nextTracksWithoutLyrics(5).toArray();
        }
    }

    class TrackInfoLabelProvider extends LabelProvider implements ITableLabelProvider {

        public String getColumnText(Object obj, int index) {
            Track track = (Track) obj;
            switch(index) {
                case 0:
                    return track.getTrackName();
                case 1:
                    return track.getArtist();
            }
            return getText(obj);
        }

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }
    }

    /**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
    public void createPartControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(1, false));
        Button load = new Button(composite, SWT.PUSH);
        load.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        load.setText("Search");
        addLoadListner(load);
        viewer = new CheckboxTableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.getTable().setHeaderVisible(true);
        String[] columns = new String[] { "Artist Name" };
        int[] columnAlignments = new int[] { 250 };
        for (int i = 0; i < columnAlignments.length; i++) {
            TableColumn tc = new TableColumn(viewer.getTable(), columnAlignments[i]);
            tc.setText(columns[i]);
            tc.setWidth(columnAlignments[i]);
        }
        viewer.setContentProvider(new TrackInfoContentProvider());
        viewer.setLabelProvider(new TrackInfoLabelProvider());
        viewer.setInput(getViewSite());
        viewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        Button deleteFromLibrary = new Button(composite, SWT.PUSH);
        deleteFromLibrary.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        deleteFromLibrary.setText("Apply lyrics if exact match");
        addDeleteListener(deleteFromLibrary);
        addContextMenu();
    }

    private void addLoadListner(final Button load) {
        load.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                viewer.refresh();
            }
        });
    }

    private void addContextMenu() {
        MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager mgr) {
                fillContextMenu(mgr);
            }
        });
        Control summaryTable = viewer.getTable();
        Menu menu = menuMgr.createContextMenu(summaryTable);
        summaryTable.setMenu(menu);
    }

    protected void fillContextMenu(IMenuManager mgr) {
        mgr.add(new Action() {

            @Override
            public String getText() {
                return "View Matches";
            }

            @Override
            public void run() {
                try {
                    EditInfoDialog editInfoDialog = new EditInfoDialog(new Shell(Display.getDefault().getActiveShell(), SWT.MIN | SWT.MAX | SWT.RESIZE + SWT.CLOSE), getViewSite());
                    IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
                    if (selection != null) {
                        editInfoDialog.setSelection(selection.toArray());
                    }
                    editInfoDialog.open();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addDeleteListener(Button deleteFromLibrary) {
        deleteFromLibrary.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                for (int i = 212; i < 344; i++) {
                }
                for (Object checked : viewer.getCheckedElements()) {
                }
                viewer.refresh();
            }
        });
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}
