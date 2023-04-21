package com.umc.collector;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import com.umc.online.PluginMovieDBInterface;
import com.umc.plugins.IMDB;
import com.umc.beans.MovieFile;
import com.umc.beans.persons.IPerson;

public class MovieInfosOnline {

    /**Language*/
    public static final String KEY_LANGUAGE = "language";

    /**ID*/
    public static final String KEY_ID = "id";

    /**IMDB ID*/
    public static final String KEY_IMDB_ID = "imdb_id";

    /**Title*/
    public static final String KEY_TITLE = "title";

    /**Alternative Title*/
    public static final String KEY_TITLE_ALTERNATIVE = "alternative title";

    /**Year*/
    public static final String KEY_YEAR = "year";

    /**Cover*/
    public static final String KEY_COVER = "cover";

    /**Rating*/
    public static final String KEY_RATING = "rating";

    /**Genre*/
    public static final String KEY_GENRES = "genres";

    /**Short Story*/
    public static final String KEY_SHORT_STORY = "short story";

    /**Story*/
    public static final String KEY_STORY = "story";

    /**Cast*/
    public static final String KEY_PERSONS = "persons";

    /**Produced in*/
    public static final String KEY_PRODUCTION_COUNTRIES = "production land";

    public static MovieFile getMovieInfosOnline(MovieFile mf, Logger log) {
        PluginMovieDBInterface imdbPlugin = new IMDB();
        LinkedList<Hashtable<String, Object>> languages = mf.getLanguages();
        Boolean complete = false;
        for (Iterator<Hashtable<String, Object>> language_iterator = languages.iterator(); language_iterator.hasNext(); ) {
            Hashtable<String, Object> language = language_iterator.next();
            LinkedList<String[]> order = (LinkedList<String[]>) language.get("order");
            Hashtable<String, Object> languageData = new Hashtable<String, Object>();
            languageData.put(KEY_LANGUAGE, language.get("language"));
            try {
                Iterator<String[]> plugin_iterator = order.iterator();
                while (!complete) {
                    PluginMovieDBInterface plugin;
                    Boolean last_iteration = false;
                    if (plugin_iterator.hasNext()) {
                        String[] pluginData = plugin_iterator.next();
                        String pluginName = "com.umc.plugins." + pluginData[0];
                        Class<com.umc.online.PluginMovieDBInterface> Klasse = (Class<PluginMovieDBInterface>) ClassLoader.getSystemClassLoader().loadClass(pluginName);
                        plugin = (PluginMovieDBInterface) Klasse.newInstance();
                        plugin.setID(pluginData[1]);
                    } else {
                        plugin = imdbPlugin;
                        last_iteration = true;
                    }
                    Object[] initData = initPlugins(imdbPlugin, mf.getIdIMDB(), plugin, mf.getID(), mf.getTitle(), String.valueOf(mf.getYear()), log);
                    imdbPlugin = (PluginMovieDBInterface) initData[0];
                    plugin = (PluginMovieDBInterface) initData[1];
                    if (!languageData.containsKey(KEY_TITLE) || languageData.get(KEY_TITLE).equals("")) {
                        languageData.put(KEY_TITLE, plugin.getTitle());
                    }
                    if (!languageData.containsKey(KEY_GENRES) || ((LinkedList<String>) languageData.get(KEY_GENRES)).size() == 0) {
                        languageData.put(KEY_GENRES, plugin.getGenres());
                    }
                    if (!languageData.containsKey(KEY_STORY) || languageData.get(KEY_STORY).equals("")) {
                        languageData.put(KEY_STORY, plugin.getStory());
                    }
                    if (!languageData.containsKey(KEY_PRODUCTION_COUNTRIES) || ((LinkedList<String>) languageData.get("KEY_PRODUCTION_COUNTRIES")).size() == 0) {
                        languageData.put(KEY_PRODUCTION_COUNTRIES, plugin.getGenres());
                    }
                    if (!languageData.containsKey(KEY_PERSONS) || ((LinkedList<String>) languageData.get("KEY_PERSONS")).size() == 0) {
                        if (imdbPlugin.getIMDBID() != null && !imdbPlugin.equals("")) {
                            LinkedList<IPerson> persons = imdbPlugin.getPersons();
                            persons = plugin.getPersonIDs(persons);
                            for (Iterator<IPerson> person_iterator = persons.iterator(); person_iterator.hasNext(); ) {
                                IPerson person = person_iterator.next();
                                if (person.getID() == null || !person.getID().equals("")) {
                                    person_iterator.remove();
                                }
                            }
                            if (persons != null) languageData.put(KEY_PERSONS, persons);
                        }
                    }
                    if (languageData.containsKey(KEY_TITLE) && languageData.containsKey(KEY_GENRES) && languageData.containsKey(KEY_STORY) && languageData.containsKey(KEY_PRODUCTION_COUNTRIES) && languageData.containsKey(KEY_PERSONS)) {
                        complete = true;
                    } else if (last_iteration) {
                        complete = true;
                        log.warn("Es konnten nicht alle Filminformationen gefunden werden: \"" + mf.getTitle() + "\"");
                    }
                }
            } catch (ClassNotFoundException e) {
                log.error("Konnte Plugin nicht finden!", e);
            } catch (InstantiationException e) {
                log.error("Konnte Instanz des Plugins nicht erzeugen!", e);
            } catch (IllegalAccessException e) {
                log.error("Ungültiger Zugriff!", e);
            } catch (Exception e) {
                log.error("Allgemeiner Fehler!", e);
            }
            mf.addData(languageData);
        }
        if (mf.getTitle() == null || mf.getTitle().equals("")) {
            mf.setTitle(imdbPlugin.getTitle());
        }
        if (mf.getYear() == 0 || mf.getYear() == -1) {
            mf.setYear((Integer) Integer.valueOf(imdbPlugin.getYear()));
        }
        if (mf.getIMDBID() == null || mf.getIMDBID().equals("")) {
            mf.setIMDBID(imdbPlugin.getIMDBID());
        }
        return mf;
    }

    private static Object[] initPlugins(PluginMovieDBInterface imdbPlugin, String imdbID, PluginMovieDBInterface plugin, String ID, String title, String year, Logger log) {
        boolean IMDBset = false;
        if (imdbPlugin.getID() != null && !imdbPlugin.getID().equals("")) IMDBset = true; else if (imdbID != null && !imdbID.equals("")) {
            log.info("IMDB-ID für \"" + title + "\" angegeben: " + imdbID);
            imdbPlugin.setIMDBID(imdbID);
            IMDBset = true;
        }
        if (ID != null && !ID.equals("")) {
            log.info("Plugin-ID für \"" + title + "\" angegeben: " + ID);
            plugin.setID(ID);
            if (!IMDBset) {
                log.info("KEINE IMDB-ID angegeben! Versuche IMDB-ID für \"" + title + "\" über Plugin zu ermitteln.");
                imdbID = plugin.getIMDBID();
                if (imdbID != null && !imdbID.equals("")) {
                    imdbPlugin.setIMDBID(imdbID);
                    IMDBset = true;
                } else {
                    log.info("Plugin konnte IMDB-ID für \"" + title + "\" nicht ermitteln. Suche nach Titel über IMDB.");
                    if (year == null || year.equals("") || year.equals("-1")) year = plugin.getYear();
                    imdbID = imdbPlugin.getIDByTitle(plugin.getTitle(), year);
                    if (imdbID != null && !imdbID.equals("")) {
                        log.info("IMDB-Suche konnte IMDB-ID für \"" + title + "\" über Suche \"" + plugin.getTitle() + "\" finden: \"" + imdbID + "\"");
                        IMDBset = true;
                    } else {
                        log.info("IMDB-Suche konnte IMDB-ID für \"" + title + "\" über Suche \"" + plugin.getTitle() + "\" NICHT finden. Beginne suche über Alternativtitel.");
                        imdbID = imdbPlugin.getIDByTitle(plugin.getAlternative(), year);
                        if (imdbID != null && !imdbID.equals("")) {
                            log.info("IMDB-ID für Titel \"" + title + "\" gefunden: " + imdbID);
                            IMDBset = true;
                        } else log.warn("IMDB-ID für Titel \"" + title + "\" NICHT gefunden!");
                    }
                }
            }
        } else {
            if (!IMDBset) {
                imdbID = imdbPlugin.getIDByTitle(title, year);
                if (imdbID != null && !imdbID.equals("")) IMDBset = true;
            }
            if (IMDBset) {
                boolean IDset = plugin.setIMDBID(imdbPlugin.getIMDBID());
                if (IDset) {
                    log.info("Plugin-ID für \"" + title + "\" gefunden: " + plugin.getID() + ", Titel: \"" + plugin.getTitle() + "\"");
                } else {
                    log.info("Es konnte keine Plugin-ID für \"" + title + "\" über IMDB-ID \"" + imdbPlugin.getIMDBID() + "\" ermittelt werden! Beginne Titelsuche.");
                    if (year == null || year.equals("") || year.equals("-1")) year = imdbPlugin.getYear();
                    ID = plugin.getIDByTitle(imdbPlugin.getTitle(), year);
                    if (ID != null && !ID.equals("")) {
                        log.info("Plugin-Suche konnte ID für \"" + title + "\" über Suche \"" + imdbPlugin.getTitle() + "\" finden: \"" + ID + "\"");
                    } else {
                        log.info("Plugin-Suche konnte ID für \"" + title + "\" über Suche \"" + imdbPlugin.getTitle() + "\" nicht finden. Beginne suche über Alternativtitel.");
                        ID = imdbPlugin.getIDByTitle(plugin.getAlternative(), year);
                        if (ID != null && !ID.equals("")) {
                            log.info("Plugin-ID für Titel \"" + title + "\" gefunden: " + ID);
                        } else log.warn("Plugin-ID für Titel \"" + title + "\" NICHT gefunden!");
                    }
                }
            } else {
                ID = plugin.getIDByTitle(title, year);
                if (ID != null && !ID.equals("")) {
                    log.info("Plugin-ID für \"" + title + "\" gefunden: " + ID + ", Titel: \"" + plugin.getTitle() + "\"");
                    log.trace("Suche nach IMDB-ID für Title \"" + title + "\"");
                    imdbID = plugin.getIMDBID();
                    if (imdbID != null && !imdbID.equals("")) {
                        imdbPlugin.setIMDBID(imdbID);
                        IMDBset = true;
                    } else {
                        if (year == null || year.equals("") || year.equals("-1")) year = plugin.getYear();
                        imdbID = imdbPlugin.getIDByTitle(plugin.getTitle(), year);
                        if ((imdbID == null || imdbID.equals("")) && plugin.getAlternative() != null) {
                            String alternativeTitle = plugin.getAlternative();
                            log.trace("Suche nach ID für Title \"" + title + "\" über Alternativtitel: \"" + alternativeTitle + "\"");
                            imdbID = plugin.getIDByTitle(alternativeTitle, year);
                            if (imdbID != null && !imdbID.equals("")) {
                                log.info("IMDB-ID für Titel \"" + title + "\" gefunden: " + imdbID);
                                IMDBset = true;
                            } else log.warn("IMDB-ID für Titel \"" + title + "\" NICHT gefunden!");
                        }
                    }
                } else {
                    log.error("Weder IMDB-Plugin noch Plugin\"" + plugin.getClass() + "\" konnte Titel \"" + title + "\" finden!");
                }
            }
        }
        Object[] result = new Object[2];
        result[0] = imdbPlugin;
        result[1] = plugin;
        return result;
    }
}
