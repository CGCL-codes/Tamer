package free.david.wc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import free.david.weather.Weather;

public class MRUMenu extends JMenu implements ActionListener {

    private WeatherClock weatherClock;

    private SortedMap mruList = new TreeMap();

    private Properties props;

    private String lastSelectedCountry = null;

    private String lastSelectedState = null;

    private String lastSelectedStation = null;

    private String lastSelectedFace = null;

    public static final String PROP_STEM = "mruList";

    private class mruEntry {

        String country;

        String state;

        String city;

        String face;

        long creationTime;

        public mruEntry() {
            super();
        }

        public mruEntry(String country, String state, String city, String face) {
            this.country = country;
            this.state = state;
            this.city = city;
            this.face = face;
            this.creationTime = Calendar.getInstance().getTimeInMillis();
        }
    }

    /**
     * This method initializes
     *
     */
    public MRUMenu() {
        super();
        initialize();
    }

    /**
     * This method initializes
     *
     */
    public MRUMenu(Properties props, WeatherClock weatherClock) {
        super();
        this.weatherClock = weatherClock;
        setProps(props);
        initialize();
        addCurrent();
    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        this.setText("Recent Stations");
        populateFromProps();
    }

    private void populateFromProps() {
        for (Iterator allMrus = getProps().keySet().iterator(); allMrus.hasNext(); ) {
            String candidate = (String) allMrus.next();
            if (candidate.startsWith(PROP_STEM)) {
                mruEntry mru = new mruEntry();
                String entry = getProps().getProperty(candidate);
                StringTokenizer tok = new StringTokenizer(entry, "|");
                if (tok.hasMoreTokens()) mru.country = tok.nextToken();
                if (tok.hasMoreTokens()) mru.state = tok.nextToken();
                if (tok.hasMoreTokens()) mru.city = tok.nextToken();
                if (tok.hasMoreTokens()) mru.face = tok.nextToken();
                if (tok.hasMoreTokens()) mru.creationTime = Long.parseLong(tok.nextToken());
                addSet(mru);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (e.getActionCommand().equalsIgnoreCase("MRU")) {
            mruEntry mru = (mruEntry) mruList.get(((JMenuItem) src).getText());
            setLastSelectedFace(mru.face);
            setLastSelectedCountry(mru.country);
            setLastSelectedState(mru.state);
            setLastSelectedStation(mru.city);
            mru.creationTime = Calendar.getInstance().getTimeInMillis();
            updateProps();
            e.setSource(this);
            ActionListener[] als = (ActionListener[]) getListeners(ActionListener.class);
            for (int i = 0; i < als.length; i++) als[i].actionPerformed(e);
        } else addCurrent();
    }

    private void addCurrent() {
        Weather weather = weatherClock.getWeather();
        mruEntry mru = new mruEntry(weather.getCountry(), weather.getStateName(), weather.getCity(), weatherClock.isFaceImage() ? weatherClock.getImageURL() : "The Moon");
        addSet(mru);
        updateProps();
    }

    private void addSet(mruEntry mru) {
        if (mruList.put(makeText(mru), mru) == null) {
            JMenuItem item = new JMenuItem();
            item.setActionCommand("MRU");
            item.setText(makeText(mru));
            item.addActionListener(this);
            add(item);
            if (mruList.size() > 10) removeOldest();
        }
    }

    private void updateProps() {
        for (Iterator allMrus = ((Properties) getProps().clone()).keySet().iterator(); allMrus.hasNext(); ) {
            String candidate = (String) allMrus.next();
            if (candidate.startsWith(PROP_STEM)) getProps().remove(candidate);
        }
        int num = 1;
        for (Iterator newMrus = mruList.keySet().iterator(); newMrus.hasNext(); num++) {
            String mru = (String) newMrus.next();
            mruEntry entry = (mruEntry) mruList.get(mru);
            getProps().put(PROP_STEM + num, entry.country + "|" + entry.state + "|" + entry.city + "|" + entry.face + "|" + entry.creationTime);
        }
    }

    private String makeText(mruEntry mru) {
        return mru.city + ", " + (mru.country.equals("US") ? mru.state : weatherClock.translateCountry(mru.country));
    }

    private void removeOldest() {
        mruEntry mru = null;
        for (Iterator i = mruList.keySet().iterator(); i.hasNext(); ) {
            String station = (String) i.next();
            mruEntry m = (mruEntry) mruList.get(station);
            if (mru == null || mru.creationTime > m.creationTime) mru = m;
        }
        mruList.remove(makeText(mru));
        updateProps();
        mruList.clear();
        removeAll();
        populateFromProps();
    }

    public void setWeatherClock(WeatherClock weatherClock) {
        this.weatherClock = weatherClock;
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    public String getLastSelectedCountry() {
        return lastSelectedCountry;
    }

    public void setLastSelectedCountry(String lastSelectedCountry) {
        this.lastSelectedCountry = lastSelectedCountry;
    }

    public String getLastSelectedFace() {
        return lastSelectedFace;
    }

    public void setLastSelectedFace(String lastSelectedFace) {
        this.lastSelectedFace = lastSelectedFace;
    }

    public String getLastSelectedState() {
        return lastSelectedState;
    }

    public void setLastSelectedState(String lastSelectedState) {
        this.lastSelectedState = lastSelectedState;
    }

    public String getLastSelectedStation() {
        return lastSelectedStation;
    }

    public void setLastSelectedStation(String lastSelectedStation) {
        this.lastSelectedStation = lastSelectedStation;
    }
}
