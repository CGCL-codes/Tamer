package importer;

import java.util.ArrayList;
import java.util.List;
import iTunes.def.IITTrack;
import importer.ItunesView.TrackInfoContentProvider;
import importer.ItunesView.TrackInfoLabelProvider;
import importer.models.common.ISearchResult;
import importer.models.common.Track;
import importer.models.leoslyrics.Result;
import importer.services.itunes.Itunes;
import importer.services.lyrics.LyricProviders;
import importer.services.lyrics.LyricsProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewSite;

public class EditInfoDialog extends Dialog {

    private Track trackSelection;

    private TableViewer viewer;

    private final IViewSite viewSite;

    protected EditInfoDialog(Shell parentShell, IViewSite viewSite) {
        super(parentShell);
        setShellStyle(SWT.MIN | SWT.MAX | SWT.RESIZE + SWT.CLOSE);
        this.viewSite = viewSite;
    }

    @Override
    protected void configureShell(Shell newShell) {
        newShell.setSize(1000, 600);
        super.configureShell(newShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(1, false));
        viewer = new TableViewer(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.getTable().setHeaderVisible(true);
        String[] columns = new String[] { "Track Name", "Artist Name", "Exact Match?" };
        int[] columnAlignments = new int[] { 200, 200, 200 };
        for (int i = 0; i < columnAlignments.length; i++) {
            TableColumn tc = new TableColumn(viewer.getTable(), columnAlignments[i]);
            tc.setText(columns[i]);
            tc.setWidth(columnAlignments[i]);
        }
        viewer.setContentProvider(new ResultsInfoContentProvider());
        viewer.setLabelProvider(new ResultsInfoLabelProvider());
        viewer.setInput(viewSite);
        viewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        return composite;
    }

    public void setSelection(Object[] selTracks) {
        this.trackSelection = (Track) selTracks[0];
    }

    @Override
    protected void okPressed() {
        ISearchResult result = (ISearchResult) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
        Itunes.getInstance().addLyrics(trackSelection, result.getLyrics());
        super.okPressed();
    }

    class ResultsInfoContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            List<ISearchResult> result = new ArrayList<ISearchResult>();
            for (LyricProviders provider : LyricProviders.values()) {
                List<? extends ISearchResult> results = provider.getProvider().getLyricsFor(trackSelection);
                if (results != null) {
                    result.addAll(results);
                }
            }
            return result.toArray();
        }
    }

    class ResultsInfoLabelProvider extends LabelProvider implements ITableLabelProvider {

        public String getColumnText(Object obj, int index) {
            Result result = (Result) obj;
            switch(index) {
                case 0:
                    return result.title;
                case 1:
                    return result.artist.toString();
                case 2:
                    return result.isExactMatch() ? "YES" : "NO";
            }
            return getText(obj);
        }

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }
    }
}
