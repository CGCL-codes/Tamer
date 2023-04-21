package muvis.view.table;

import muvis.view.table.actions.FindSimilarElementsTableAction;
import muvis.view.table.actions.FindNonSimilarElementsTableAction;
import muvis.view.table.actions.PreviewElementTableAction;
import muvis.view.table.actions.AddToPlaylistTableAction;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import muvis.Elements;
import muvis.Environment;
import muvis.view.MainViewsMouseAdapter;
import muvis.view.MusicControllerView;
import muvis.view.ViewManager;
import muvis.view.controllers.ListViewTableViewController;
import muvis.view.controllers.MusicPlayerIndividualTrackController;

/**
 * Mouse Adapter for the tables in the MuVis Application
 * @author Ricardo
 */
public class JTableMouseAdapter extends MainViewsMouseAdapter {

    protected ListViewTableViewController controller;

    protected JTable tracksTable;

    protected ActionListener previewAction, addToPlaylistAction, findSimilarAction, findNonSimilarAction;

    public JTableMouseAdapter(JTable tracksTable, ListViewTableViewController controller) {
        this.controller = controller;
        this.tracksTable = tracksTable;
        previewAction = new PreviewElementTableAction(tracksTable);
        addToPlaylistAction = new AddToPlaylistTableAction(tracksTable, controller);
        findSimilarAction = new FindSimilarElementsTableAction(tracksTable);
        findNonSimilarAction = new FindNonSimilarElementsTableAction(tracksTable);
    }

    @Override
    protected void assignActionListeners() {
        previewElementMenu.addActionListener(previewAction);
        addElementToPlaylistMenu.addActionListener(addToPlaylistAction);
        findSimilarElementMenu.addActionListener(findSimilarAction);
        findNonSimilarElementMenu.addActionListener(findNonSimilarAction);
    }

    @Override
    protected void mouseHandler(MouseEvent e) {
        if (e.isPopupTrigger() && tracksTable.isEnabled()) {
            Point p = new Point(e.getX(), e.getY());
            int col = tracksTable.columnAtPoint(p);
            int row = tracksTable.rowAtPoint(p);
            int mcol = tracksTable.getColumn(tracksTable.getColumnName(col)).getModelIndex();
            String colName = tracksTable.getModel().getColumnName(mcol);
            if (row >= 0 && row < tracksTable.getRowCount()) {
                contextMenu = createContextMenu(colName, ((tracksTable.getSelectedRowCount() <= 1) ? MainViewsMouseAdapter.ElementType.SIMPLE : MainViewsMouseAdapter.ElementType.MULTIPLE));
                if (tracksTable.getSelectedRowCount() <= 1) {
                    ListSelectionModel model = tracksTable.getSelectionModel();
                    model.setSelectionInterval(row, row);
                }
                if (contextMenu != null && contextMenu.getComponentCount() > 0) {
                    contextMenu.show(tracksTable, p.x, p.y);
                }
            }
        } else if (e.getClickCount() == 2) {
            int row = tracksTable.getSelectedRow();
            int trackId = (Integer) tracksTable.getValueAt(row, 0);
            ViewManager vm = Environment.getEnvironmentInstance().getViewManager();
            MusicControllerView mpController = (MusicControllerView) vm.getView(Elements.MUSIC_PLAYER_VIEW);
            ((MusicPlayerIndividualTrackController) mpController.getMusicPlayerIndividualController()).setTrackId(trackId);
            mpController.setPlayingType(MusicControllerView.PlayingType.INDIVIDUAL_TRACK);
            mpController.playTrack();
        }
    }
}
