package lektor.gui.administration;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import lektor.gui.administration.book.BookGui;
import lektor.gui.administration.customer.CustomerGui;
import lektor.gui.administration.genre.GenreGui;

/**
 * @author <a href="mailto:michael.paetzold@caseconsult.com"
 *         style="text-decoration:none;">Michael Pätzold</a>
 *
 */
public class AdministrationGui extends JTabbedPane {

    private int selectedIndex = -1;

    private String buecher = "Bücher";

    private String kunden = "Kunden";

    private String genre = "Genre";

    private Icon iconBuecher = new ImageIcon(getToolkit().getImage("./resources/pics/gui/admin/book_16x16.gif"));

    private Icon iconKunden = new ImageIcon(getToolkit().getImage("./resources/pics/gui/admin/customer_16x16.gif"));

    private Icon iconGenre = new ImageIcon(getToolkit().getImage("./resources/pics/gui/admin/genre_16x16.gif"));

    private ChangeListener cl = null;

    public AdministrationGui(BookGui bookgui, CustomerGui customergui, GenreGui genregui, ChangeListener cl) {
        init(bookgui, customergui, genregui, cl);
        validate();
    }

    public void init(BookGui bookgui, CustomerGui customergui, GenreGui genregui, ChangeListener cl) {
        this.cl = cl;
        this.addTab(buecher, iconBuecher, bookgui);
        this.addTab(kunden, iconKunden, customergui);
        this.addTab(genre, iconGenre, genregui);
        this.addChangeListener(this.cl);
    }

    public boolean indexHasChanded() {
        boolean indexHasChanged = false;
        indexHasChanged = (this.selectedIndex != getSelectedIndex());
        this.selectedIndex = getSelectedIndex();
        return indexHasChanged;
    }
}
