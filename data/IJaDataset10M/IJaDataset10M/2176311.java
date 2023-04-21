package widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Date;
import com.umc.collector.Publisher;
import com.umc.dao.DataAccessFactory;
import com.umc.dao.UMCDataAccessInterface;
import com.umc.gui.widgets.AbstractWidget;

/**
 * This widget shows the latest scanned movies as a list with icons.
 * 
 * @author DonGyros
 *
 */
public class Statistic extends AbstractWidget {

    private static final long serialVersionUID = 1L;

    private int moviesCount = 0;

    private int musicCount = 0;

    private int photoCount = 0;

    private int moviegroupsCount = 0;

    private int seriesCount = 0;

    private int personsCount = 0;

    private int genresCount = 0;

    private int countriesCount = 0;

    private Date lastScanDate = null;

    public Statistic() {
        setMinimumSize(new Dimension(350, 110));
        setPreferredSize(new Dimension(350, 110));
        setRefreshEnabled(true);
        setRefreshTime(300);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (showInfo) {
            Dimension d = getSize();
            Color c = getBackground();
            g2.setColor(c);
            g2.fillRect(0, 0, d.width, d.height);
            paintFrame(g2);
            g2.setColor(colorInfo);
            g2.drawString("Name: " + getName() + " | Version: " + getVersion(), 20, 30);
            g2.drawString("Description: " + getDescription(), 20, 50);
            g2.drawString("Author: " + getAuthor(), 20, 70);
            g2.drawString("Next refresh in: " + (remainingSleeptime / 1000) + " seconds", 20, 90);
        } else {
            paintFrame(g2);
            if (isOpen) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.setFont(textBold10);
                g2.drawString("Movies: " + moviesCount, 20, 30);
                g2.drawString("Groups: " + moviegroupsCount, 20, 50);
                g2.drawString("Series: " + seriesCount, 20, 70);
                g2.drawString("Music: " + musicCount, 110, 30);
                g2.drawString("Persons: " + personsCount, 110, 50);
                g2.drawString("Music: " + musicCount, 110, 70);
                g2.drawString("Photos: " + photoCount, 200, 30);
                g2.drawString("Genres: " + genresCount, 200, 50);
                g2.drawString("Countries: " + countriesCount, 200, 70);
                g2.drawLine(20, 85, 330, 85);
                g2.drawString("Letzter Scan: " + (lastScanDate != null ? lastScanDate : ""), 20, 100);
            }
        }
    }

    public void execute() {
        UMCDataAccessInterface dao = DataAccessFactory.getUMCDataSourceAccessor(DataAccessFactory.DB_TYPE_SQLITE, Publisher.getInstance().getParamDBDriverconnect() + Publisher.getInstance().getParamDBName(), Publisher.getInstance().getParamDBDriver(), Publisher.getInstance().getParamDBUser(), Publisher.getInstance().getParamDBPwd());
        moviesCount = dao.getMovieCount();
        musicCount = dao.getMusicCount();
        photoCount = dao.getPhotoCount();
        moviegroupsCount = dao.getMovieGroupCount();
        seriesCount = dao.getSeriesCount();
        personsCount = dao.getActorCount();
        genresCount = dao.getGenreCount();
        countriesCount = dao.getCountryCount();
        lastScanDate = dao.getLastScanDate();
    }

    public String getName() {
        return "Statistic";
    }

    public String getDescription() {
        return "This widget will show a statistic";
    }

    public String getAuthor() {
        return "DonGyros";
    }

    public double getVersion() {
        return 0.;
    }
}
