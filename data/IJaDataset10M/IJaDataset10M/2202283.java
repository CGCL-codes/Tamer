package mypodsync.service.calendar;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JPanel;
import mypodsync.Config;

public class GoogleCalendarConfig implements Config {

    private static final String FOLDER = "folder";

    private static final String URLS = "urls";

    private GoogleCalendarConfigPanel panel;

    private Preferences prefs;

    private Logger log = Logger.getLogger(GoogleCalendarConfig.class.getName());

    public GoogleCalendarConfig() {
        panel = new GoogleCalendarConfigPanel();
        prefs = Preferences.userNodeForPackage(getClass());
    }

    public JPanel getUI() {
        return panel;
    }

    public String getTitle() {
        return "Google Calendar to iCal";
    }

    public void save() {
        prefs.put(FOLDER, panel.getFolder());
        CalendarsTableModel tm = panel.getTableModel();
        StringBuffer conf = new StringBuffer();
        for (int i = 0; i < tm.getRowCount() - 1; i++) {
            conf.append(tm.getValueAt(i, 0) + "->" + tm.getValueAt(i, 1) + "|");
        }
        if (tm.getRowCount() > 0) {
            conf.append(tm.getValueAt(tm.getRowCount() - 1, 0) + "->" + tm.getValueAt(tm.getRowCount() - 1, 1));
        }
        prefs.put(URLS, conf.toString());
        try {
            prefs.flush();
        } catch (BackingStoreException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void load() {
        panel.getTableModel().clear();
        panel.setFolder(getFolder());
        Map<String, String> cals = getCalendars();
        for (Iterator<String> iter = cals.keySet().iterator(); iter.hasNext(); ) {
            String key = iter.next();
            String val = cals.get(key);
            panel.getTableModel().add(key, val);
        }
    }

    public String getFolder() {
        return prefs.get(FOLDER, "");
    }

    public Map<String, String> getCalendars() {
        SortedMap<String, String> cals = new TreeMap<String, String>();
        String conf = prefs.get(URLS, null);
        if (conf != null && conf.length() > 1) {
            String[] rows = conf.split("\\|");
            for (int i = 0; i < rows.length; i++) {
                String r = rows[i];
                String[] col = r.split("\\->");
                cals.put(col[0], col[1]);
            }
        }
        return cals;
    }
}
