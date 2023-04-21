package org.moviereport.ui.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.moviereport.core.model.MovieCollection;
import org.moviereport.core.model.MovieDescription;
import org.moviereport.ui.Main;
import org.moviereport.ui.bf.MovieReportCoreAccess;
import org.moviereport.ui.search.SearchPanel;
import com.jidesoft.swing.InfiniteProgressPanel;
import com.jidesoft.swing.JideBoxLayout;

public class ImportMoviesDialog extends JDialog {

    private static final String DIALOG_TITLE = "Import Movies";

    private SearchPanel searchPanel;

    private MovieCollection movieCollectionForImport;

    private InfiniteProgressPanel progressPanel;

    public ImportMoviesDialog() {
        super(Main.getInstance().getMainFrame());
        searchPanel = new SearchPanel(this);
        progressPanel = new InfiniteProgressPanel();
        setGlassPane(progressPanel);
        setTitle(DIALOG_TITLE);
        setModal(true);
        Dimension dimension = new Dimension(800, 600);
        setSize(dimension);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                cancelImportMovies();
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new JideBoxLayout(contentPane, JideBoxLayout.Y_AXIS, 5));
        searchPanel.init();
        contentPane.add(searchPanel, JideBoxLayout.FLEXIBLE);
        contentPane.add(getControlButtonArea(), JideBoxLayout.FIX);
    }

    private JPanel getControlButtonArea() {
        JButton importMoviesButton = new JButton(new ImportMoviesAction());
        importMoviesButton.setText("Import Movies");
        importMoviesButton.setFocusable(true);
        importMoviesButton.setFocusCycleRoot(true);
        importMoviesButton.setRequestFocusEnabled(true);
        importMoviesButton.requestFocusInWindow();
        getRootPane().setDefaultButton(importMoviesButton);
        JButton cancelButton = new JButton(new CancelEditAction());
        cancelButton.setText("Cancel");
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(100, 40));
        panel.setMinimumSize(new Dimension(100, 40));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(Box.createHorizontalGlue());
        panel.add(importMoviesButton);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(cancelButton);
        return panel;
    }

    public void showDialog(MovieCollection selection) {
        this.movieCollectionForImport = selection;
        pack();
        setLocationRelativeTo(Main.getInstance().getMainFrame());
        setVisible(true);
        setVisible(false);
    }

    public void importMovies() {
        List<MovieDescription> searchResults = searchPanel.getSearchResults();
        if (movieCollectionForImport != null) {
            movieCollectionForImport.addAllMovieDescriptions(searchResults);
            MovieReportCoreAccess.getInstance().storeDeep(movieCollectionForImport);
        }
        for (MovieDescription movieDescription : searchResults) {
            MovieReportCoreAccess.getInstance().storeMovieDescription(movieDescription, true);
        }
        setVisible(false);
    }

    public void cancelImportMovies() {
        setVisible(false);
    }

    class ImportMoviesAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            importMovies();
        }
    }

    class CancelEditAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            cancelImportMovies();
        }
    }

    public void showLoadingPorgressBar(boolean visible) {
        if (visible) {
            progressPanel.start();
        } else {
            progressPanel.stop();
        }
        progressPanel.setVisible(visible);
        getGlassPane().setVisible(visible);
    }
}
