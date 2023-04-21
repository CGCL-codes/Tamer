package ho.core.file.hrf;

import ho.core.model.HOModel;
import ho.core.model.Team;
import ho.core.model.XtraData;
import ho.core.model.misc.Basics;
import ho.core.model.misc.Finanzen;
import ho.core.model.misc.Verein;
import ho.core.model.player.Spieler;
import ho.core.model.series.Liga;
import ho.core.util.HOLogger;
import ho.module.lineup.Lineup;
import ho.tool.arenasizer.Stadium;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Vector;

public class HRFStringParser {

    /** TODO Missing Parameter Documentation */
    private static final String ENTITY = "Entity";

    /** TODO Missing Parameter Documentation */
    private static final String BASICS = "[basics]";

    /** TODO Missing Parameter Documentation */
    private static final String LEAGUE = "[league]";

    /** TODO Missing Parameter Documentation */
    private static final String CLUB = "[club]";

    /** TODO Missing Parameter Documentation */
    private static final String TEAM = "[team]";

    /** TODO Missing Parameter Documentation */
    private static final String LINEUP = "[lineup]";

    /** TODO Missing Parameter Documentation */
    private static final String ECONOMY = "[economy]";

    /** TODO Missing Parameter Documentation */
    private static final String ARENA = "[arena]";

    /** TODO Missing Parameter Documentation */
    private static final String PLAYER = "[player]";

    /** TODO Missing Parameter Documentation */
    private static final String XTRA = "[xtra]";

    /** TODO Missing Parameter Documentation */
    private static final String LASTLINEUP = "[lastlineup]";

    public final HOModel parse(String hrf) {
        HOModel modelReturn = null;
        Timestamp hrfdate = null;
        BufferedReader hrfReader = null;
        if (hrf == null || hrf.length() == 0) {
            HOLogger.instance().log(getClass(), "HRF string is empty");
            return null;
        }
        try {
            final Vector<Properties> propertiesVector = new Vector<Properties>();
            Properties properties = null;
            final ByteArrayInputStream bis = new ByteArrayInputStream(hrf.getBytes("UTF-8"));
            final InputStreamReader isr = new InputStreamReader(bis, "UTF-8");
            hrfReader = new BufferedReader(isr);
            String lineString = "";
            Object entity = null;
            String datestring = "";
            int indexEqualsSign = -1;
            while (hrfReader.ready()) {
                lineString = hrfReader.readLine();
                if ((lineString == null) || lineString.trim().equals("")) continue;
                if (lineString.startsWith("[")) {
                    if (properties != null) {
                        entity = properties.get(ENTITY);
                        if (entity != null && entity.toString().equalsIgnoreCase(BASICS)) {
                            datestring = properties.getProperty("date");
                            hrfdate = getDateFromString(datestring);
                        }
                        propertiesVector.add(properties);
                    }
                    properties = new Properties();
                    if (lineString.startsWith("[player")) {
                        properties.setProperty(ENTITY, PLAYER);
                        properties.setProperty("id", lineString.substring(7, lineString.lastIndexOf(']')));
                    } else properties.setProperty(ENTITY, lineString);
                } else {
                    indexEqualsSign = lineString.indexOf('=');
                    if (indexEqualsSign > 0) {
                        if (properties == null) properties = new Properties();
                        properties.setProperty(lineString.substring(0, indexEqualsSign).toLowerCase(java.util.Locale.ENGLISH), lineString.substring(indexEqualsSign + 1));
                    }
                }
            }
            if (properties != null) propertiesVector.add(properties);
            try {
                hrfReader.close();
            } catch (IOException ioe) {
            }
            modelReturn = createHOModel(propertiesVector, hrfdate);
        } catch (Exception e) {
            HOLogger.instance().error(getClass(), "Error while parsing hrf");
            HOLogger.instance().error(getClass(), e);
        }
        return modelReturn;
    }

    private Timestamp getDateFromString(String date) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.GERMANY);
        try {
            return new Timestamp(simpleFormat.parse(date).getTime());
        } catch (Exception e) {
            try {
                simpleFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.GERMANY);
                return new Timestamp(simpleFormat.parse(date).getTime());
            } catch (Exception expc) {
                HOLogger.instance().log(getClass(), e);
                return new Timestamp(System.currentTimeMillis());
            }
        }
    }

    /**
     * Erzeugt aus dem Vector mit Properties ein HOModel
     *
     * @param propertiesVector TODO Missing Constructuor Parameter Documentation
     * @param hrfdate TODO Missing Constructuor Parameter Documentation
     *
     * @return TODO Missing Return Method Documentation
     *
     * @throws Exception TODO Missing Constructuor Exception Documentation
     */
    private HOModel createHOModel(Vector<Properties> propertiesVector, Timestamp hrfdate) throws Exception {
        final HOModel hoModel = new HOModel();
        int trainerID = -1;
        for (int i = 0; i < propertiesVector.size(); i++) {
            final Properties properties = (Properties) propertiesVector.get(i);
            final Object entity = properties.get(ENTITY);
            if (entity != null) {
                if (entity.toString().equalsIgnoreCase(BASICS)) {
                    hoModel.setBasics(new Basics(properties));
                } else if (entity.toString().equalsIgnoreCase(LEAGUE)) {
                    hoModel.setLiga(new Liga(properties));
                } else if (entity.toString().equalsIgnoreCase(CLUB)) {
                    hoModel.setVerein(new Verein(properties));
                } else if (entity.toString().equalsIgnoreCase(TEAM)) {
                    hoModel.setTeam(new Team(properties));
                } else if (entity.toString().equalsIgnoreCase(LINEUP)) {
                    hoModel.setAufstellung(new Lineup(properties));
                } else if (entity.toString().equalsIgnoreCase(ECONOMY)) {
                    hoModel.setFinanzen(new Finanzen(properties));
                } else if (entity.toString().equalsIgnoreCase(ARENA)) {
                    hoModel.setStadium(new Stadium(properties));
                } else if (entity.toString().equalsIgnoreCase(PLAYER)) {
                    hoModel.addSpieler(new Spieler(properties, hrfdate));
                } else if (entity.toString().equalsIgnoreCase(XTRA)) {
                    hoModel.setXtraDaten(new XtraData(properties));
                    trainerID = Integer.parseInt(properties.getProperty("trainerid", "-1").toString());
                } else if (entity.toString().equalsIgnoreCase(LASTLINEUP)) {
                    hoModel.setLastAufstellung(new Lineup(properties));
                } else {
                    HOLogger.instance().log(getClass(), "Unbekannte Entity: " + entity.toString());
                }
            } else {
                HOLogger.instance().log(getClass(), "Fehlerhafte Datei / Keine Entity gefunden");
                return null;
            }
        }
        if (trainerID > -1) {
            for (int i = 0; (hoModel.getAllSpieler() != null) && (i < hoModel.getAllSpieler().size()); i++) {
                if (((Spieler) hoModel.getAllSpieler().elementAt(i)).isTrainer() && ((((Spieler) hoModel.getAllSpieler().elementAt(i))).getSpielerID() != trainerID)) {
                    (((Spieler) hoModel.getAllSpieler().elementAt(i))).setTrainer(-1);
                    (((Spieler) hoModel.getAllSpieler().elementAt(i))).setTrainerTyp(-1);
                }
            }
        }
        return hoModel;
    }
}
