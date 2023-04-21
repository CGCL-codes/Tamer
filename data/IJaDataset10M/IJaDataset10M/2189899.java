package hoplugins.teamplanner.ui;

import hoplugins.Commons;
import hoplugins.TeamPlanner;
import hoplugins.commons.utils.SeriesUtil;
import hoplugins.teamplanner.ui.tabs.WeekHeader;
import hoplugins.teamplanner.vo.YearSetting;
import plugins.IFutureTrainingManager;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:kenmooda@users.sourceforge.net">Tommi Rautava</a>
 */
public class FutureSettingsPane extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7287375759347138254L;

    private List<YearSettingPane> futureYears = new ArrayList<YearSettingPane>();

    /**
     * Creates a new FutureSettingsPane object.
     */
    public FutureSettingsPane() {
        super();
        jbInit();
    }

    /**
     * Missing Method Documentation
     *
     * @param season Missing Method Parameter Documentation
     *
     * @return Missing Return Method Documentation
     */
    public final YearSetting getYearSetting(int season) {
        int index = season - TeamPlanner.ACTUALWEEK.getSeason();
        return getYearPane(index).getSetting();
    }

    /**
     * Missing Method Documentation
     */
    public void refreshTable() {
        String liga = Commons.getModel().getLiga().getLiga();
        int serie = SeriesUtil.getSeriesLevel(liga);
        getYearPane(0).setSerie(serie);
        for (int i = 1; i < futureYears.size(); i++) {
            if (getYearPane(i - 1).getSetting().getSeasonEvent() == 1) {
                serie--;
            }
            if (getYearPane(i - 1).getSetting().getSeasonEvent() == -1) {
                serie++;
            }
            getYearPane(i).setSerie(serie);
        }
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param index TODO Missing Method Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    private YearSettingPane getYearPane(int index) {
        return futureYears.get(index);
    }

    /**
     * Missing Method Documentation
     */
    private void jbInit() {
        JPanel p = new JPanel();
        int count = WeekHeader.instance().getColumnWeek(IFutureTrainingManager.FUTUREWEEKS - 1).getSeason() - TeamPlanner.ACTUALWEEK.getSeason() + 1;
        p.setLayout(new GridLayout(count, 1));
        for (int i = 0; i < count; i++) {
            futureYears.add(new YearSettingPane(TeamPlanner.ACTUALWEEK.getSeason() + i));
            p.add(getYearPane(i));
        }
        refreshTable();
        setLayout(new BorderLayout());
        add(p, BorderLayout.CENTER);
    }
}
