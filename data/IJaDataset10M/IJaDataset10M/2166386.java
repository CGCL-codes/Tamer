package net.sourceforge.omov.app.gui.movie;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import net.sourceforge.jpotpourri.jpotface.util.PtGuiUtil;
import net.sourceforge.omov.app.gui.IPrevNextMovieProvider;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.Quality;
import net.sourceforge.omov.core.bo.Resolution;
import net.sourceforge.omov.guicore.GuiActionListener;
import net.sourceforge.omov.guicore.OmovGuiUtil;
import net.sourceforge.omov.logic.tools.scan.ScannedMovie;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class AddEditMovieDialog extends AbstractAddEditDialog<Movie> {

    private static final Log LOG = LogFactory.getLog(AddEditMovieDialog.class);

    private static final long serialVersionUID = -499631022640948375L;

    private static final int TABPOS_INFO = 0;

    private static final int TABPOS_DETAIL = 1;

    private static final int TABPOS_NOTES = 2;

    private final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);

    private MovieTabInfo tabInfo;

    private MovieTabDetails tabDetails;

    private MovieTabNotes tabNotes;

    private JButton btnPrev = new JButton("Previous");

    private JButton btnNext = new JButton("Next");

    private int moviePrevNextIndex = -1;

    private int moviePrevNextCount = -1;

    private IPrevNextMovieProvider prevNextProvider = null;

    public static AddEditMovieDialog newAddDialog(JFrame owner) {
        return new AddEditMovieDialog(owner, null, null);
    }

    public static AddEditMovieDialog newEditDialog(JFrame owner, Movie editMovie, IPrevNextMovieProvider prevNextProvider) {
        return new AddEditMovieDialog(owner, editMovie, prevNextProvider);
    }

    public static AddEditMovieDialog newEditScanMovieDialog(JFrame owner, ScannedMovie scannedMovie) {
        return new AddEditMovieDialog(owner, scannedMovie, null);
    }

    private AddEditMovieDialog(JFrame owner, Movie editObject, IPrevNextMovieProvider prevNextProvider) {
        super(owner, editObject);
        this.setModal(true);
        this.tabbedPane.setBackground(Constants.getColorWindowBackground());
        if (prevNextProvider == null) {
            this.btnPrev.setEnabled(false);
            this.btnNext.setEnabled(false);
        } else {
            this.moviePrevNextIndex = prevNextProvider.getInitialIndex();
            this.moviePrevNextCount = prevNextProvider.getCountIndices();
            if (this.moviePrevNextIndex == 0) {
                this.btnPrev.setEnabled(false);
            } else if (this.moviePrevNextIndex == this.moviePrevNextCount - 1) {
                this.btnNext.setEnabled(false);
            }
            if (this.moviePrevNextCount == 1) {
                this.btnPrev.setEnabled(false);
                this.btnNext.setEnabled(false);
            }
            this.prevNextProvider = prevNextProvider;
        }
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                AddEditMovieDialog.this.tabInfo.requestInitialFocus();
            }
        });
        final Movie editItem = (this.isAddMode() ? null : this.getEditItem());
        this.initEditMovie(editItem, 0);
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        PtGuiUtil.setCenterLocation(this);
    }

    private void initEditMovie(final Movie movie, final int preselectedTabIndex) {
        this.setEditItem(movie);
        this.setTitle(isAddMode() ? "Add new Movie" : movie.getTitle());
        this.tabInfo = new MovieTabInfo(this, this.isAddMode(), movie);
        this.tabDetails = new MovieTabDetails(this, this.isAddMode(), movie);
        this.tabNotes = new MovieTabNotes(this, this.isAddMode(), movie);
        this.initTabbedPane(preselectedTabIndex);
    }

    private void initTabbedPane(int preselectedTabIndex) {
        this.tabbedPane.removeAll();
        this.tabbedPane.add(" " + this.tabInfo.getTabTitle() + " ", this.tabInfo);
        this.tabbedPane.setMnemonicAt(TABPOS_INFO, KeyEvent.VK_I);
        this.tabbedPane.add(" " + this.tabDetails.getTabTitle() + " ", this.tabDetails);
        this.tabbedPane.setMnemonicAt(TABPOS_DETAIL, KeyEvent.VK_D);
        this.tabbedPane.add(" " + this.tabNotes.getTabTitle() + " ", this.tabNotes);
        this.tabbedPane.setMnemonicAt(TABPOS_NOTES, KeyEvent.VK_N);
        this.tabbedPane.setSelectedIndex(preselectedTabIndex);
        this.tabbedPane.revalidate();
    }

    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.getColorWindowBackground());
        this.initTabbedPane(TABPOS_INFO);
        panel.add(this.tabbedPane, BorderLayout.CENTER);
        panel.add(this.newSouthPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel newSouthPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(this.newSouthWestPanel(), BorderLayout.WEST);
        panel.add(this.newCommandPanel(), BorderLayout.EAST);
        return panel;
    }

    private JPanel newSouthWestPanel() {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        this.btnPrev.setOpaque(false);
        this.btnNext.setOpaque(false);
        this.btnPrev.addActionListener(new GuiActionListener() {

            @Override
            public void action(ActionEvent e) {
                doShowPrevMovie();
            }
        });
        this.btnNext.addActionListener(new GuiActionListener() {

            @Override
            public void action(ActionEvent e) {
                doShowNextMovie();
            }
        });
        panel.add(this.btnPrev);
        panel.add(this.btnNext);
        return panel;
    }

    private void doShowNextMovie() {
        LOG.debug("doShowNextMovie()");
        this.doConfirmWithoutDispose();
        final Movie oldMovie = this.getConfirmedObject();
        this.moviePrevNextIndex++;
        if (this.moviePrevNextIndex == this.moviePrevNextCount - 1) {
            this.btnNext.setEnabled(false);
        }
        this.setNewPrevNextMovie(oldMovie);
        if (this.btnPrev.isEnabled() == false) this.btnPrev.setEnabled(true);
    }

    private void doShowPrevMovie() {
        LOG.debug("doShowPrevMovie()");
        this.doConfirmWithoutDispose();
        final Movie oldMovie = this.getConfirmedObject();
        this.moviePrevNextIndex--;
        if (this.moviePrevNextIndex == 0) {
            this.btnPrev.setEnabled(false);
        }
        this.setNewPrevNextMovie(oldMovie);
        if (this.btnNext.isEnabled() == false) this.btnNext.setEnabled(true);
    }

    private void setNewPrevNextMovie(Movie oldMovie) {
        if (oldMovie.equals(this.getEditItem()) == true) {
            LOG.debug("Not going to update old movie because nothing has changed.");
        } else {
            try {
                LOG.info("Updating old movie because prev/next button was hit; old movie: " + oldMovie);
                BeanFactory.getInstance().getMovieDao().updateMovie(oldMovie);
            } catch (BusinessException e) {
                OmovGuiUtil.error(this, "Core Source Error", "Could not update recent movie!\nChanges were lost, sorry for that dude...");
            }
        }
        final Movie newMovie = this.prevNextProvider.getMovieAt(this.moviePrevNextIndex);
        LOG.info("displaying new movie: " + newMovie);
        this.initEditMovie(newMovie, this.tabbedPane.getSelectedIndex());
    }

    public boolean isCoverChanged() {
        return this.tabInfo.isCoverChanged();
    }

    /** AbstractAddEditDialog */
    @Override
    protected Movie _getConfirmedObject() {
        final long id = this.isAddMode() ? -1 : this.getEditItem().getId();
        final String title = this.tabInfo.getTitle();
        final boolean seen = this.tabInfo.getSeen();
        final int rating = this.tabInfo.getRating();
        final String coverFile = this.tabInfo.getCoverFile();
        final Set<String> genres = this.tabInfo.getGenres();
        final Set<String> languages = this.tabDetails.getLanguages();
        final String style = this.tabInfo.getStyle();
        final String director = this.tabDetails.getDirector();
        final Set<String> actors = this.tabDetails.getActors();
        final int year = this.tabInfo.getYear();
        final String comment = this.tabNotes.getComment();
        final Quality quality = this.tabInfo.getQuality();
        final Date dateAdded = this.tabNotes.getDateAdded();
        final String folderPath = this.tabDetails.getFolderPath();
        final long fileSizeKb = this.tabDetails.getFileSizeKb();
        final String format = this.tabDetails.getFormat();
        final List<String> files = this.tabDetails.getFiles();
        final int duration = this.tabInfo.getDuration().getTotalInMinutes();
        final Resolution resolution = this.tabInfo.getResolution();
        final Set<String> subtitles = this.tabDetails.getSubtitles();
        return Movie.newMovie(id, title, seen, rating, coverFile, genres, languages, style, director, actors, year, comment, quality, dateAdded, fileSizeKb, folderPath, format, files, duration, resolution, subtitles);
    }

    public static void main(String[] args) throws Exception {
        JFrame.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            LOG.error("Unable to set system look&feel!", ex);
        }
        final Movie movie = BeanFactory.getInstance().getMovieDao().getMovie(10);
        final AddEditMovieDialog editDialog = AddEditMovieDialog.newEditDialog(null, movie, new IPrevNextMovieProvider() {

            public int getCountIndices() {
                return 1;
            }

            public int getInitialIndex() {
                return 0;
            }

            public Movie getMovieAt(int index) {
                return null;
            }
        });
        editDialog.setVisible(true);
        System.exit(0);
    }
}
