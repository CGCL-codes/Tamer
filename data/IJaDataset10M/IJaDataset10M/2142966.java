package com.umc.plugins;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.umc.plugins.moviedb.IPluginMovieDB;
import com.umc.plugins.fanart.IPluginFanart;
import com.umc.plugins.series.IPluginSeries;
import plugins.series.*;
import com.umc.beans.media.Episode;
import com.umc.beans.media.Languages;
import com.umc.beans.media.Movie;
import com.umc.beans.media.Plugins;
import com.umc.beans.media.Season;
import com.umc.beans.media.SeriesGroup;
import com.umc.beans.persons.Actor;
import com.umc.beans.persons.IPerson;
import com.umc.beans.persons.Person;
import com.umc.helper.UMCConstants;

/**
 *
 * This class collects online-informations of a movie file by using defines plugins.
 *
 * @author Fl4sh
 * @version 0.2
 *
 * History:
 * - 11.09.2009		Fl4sh		ready for 1.9b
 * - 31.03.2009		Fl4sh		ready for 2.0
 *
 */
public class PluginController {

    public static Collection<IPerson> getPersonInfosOnline(Collection<IPerson> collect, Logger log) {
        plugins.persons.IMDB imdbPlugin = new plugins.persons.IMDB();
        for (IPerson person : collect) {
            if (person.getIdIMDB() != null) {
                imdbPlugin.setIMDBID(person.getIdIMDB());
                String tempResult = imdbPlugin.getName();
                if (tempResult != null) person.setName(new String(tempResult));
            } else if (person.getName() != null) {
                imdbPlugin.setIDByName(person.getName());
                String tempResult = imdbPlugin.getID();
                if (tempResult != null) person.setIdIMDB(new String(tempResult));
            }
            if (imdbPlugin.getID() != null) {
                String tempValue = imdbPlugin.getBirthday();
                if (tempValue != null) person.setBirthday(new String(tempValue));
                tempValue = imdbPlugin.getPlaceOfBirth();
                if (tempValue != null) person.setPlaceOfBirth(new String(tempValue));
                tempValue = imdbPlugin.getDeathday();
                if (tempValue != null) person.setDayOfDeath(new String(tempValue));
                tempValue = imdbPlugin.getPlaceOfDeath();
                if (tempValue != null) person.setPlaceOfDeath(new String(tempValue));
                tempValue = imdbPlugin.getPicture();
                if (tempValue != null) person.setPicture(new String(tempValue));
                tempValue = imdbPlugin.getBiography();
                if (tempValue != null) person.setBiography(new String(tempValue));
                person.setFilmography(imdbPlugin.getFilmography());
            }
        }
        return collect;
    }

    public static SeriesGroup getSeriesInfosOnline(SeriesGroup series, Logger log) {
        IPluginSeries plugin = new TheTVDB();
        Boolean initialized = false;
        if (series.getIdTHETVDB() != null) {
            plugin.setID(series.getIdTHETVDB(), series.getUuid());
            initialized = true;
        } else {
            LinkedList<String> languages = new LinkedList<String>();
            languages.addAll(series.getLanguages());
            LinkedList<String> supportedLanguages = plugin.getLanguages();
            plugin.setLanguage("en");
            languages.remove("en");
            if (plugin.setIDByTitle(series.getComputedTitel(), series.getUuid()) != null) {
                initialized = true;
            }
            for (Iterator<String> i_languages = languages.iterator(); i_languages.hasNext() && !initialized; ) {
                String language = i_languages.next();
                if (supportedLanguages.contains(language)) {
                    plugin.setLanguage(language);
                }
                if (plugin.setIDByTitle(series.getComputedTitel(), series.getUuid()) != null) {
                    initialized = true;
                }
            }
        }
        LinkedList<String> languages = new LinkedList<String>();
        languages.addAll(series.getLanguages());
        LinkedList<String> supportedLanguages = plugin.getLanguages();
        if (!initialized) {
            log.error("Serie \"" + series.getComputedTitel() + "\" konnte nicht gefunden werden");
            String language;
            Season season;
            Episode episode;
            for (Iterator<String> i_languages = languages.iterator(); i_languages.hasNext(); ) {
                language = i_languages.next();
                for (Enumeration<Season> e_seasons = series.getSeasons().elements(); e_seasons.hasMoreElements(); ) {
                    season = e_seasons.nextElement();
                    for (Enumeration<Episode> episodes = season.getEpisodes().elements(); episodes.hasMoreElements(); ) {
                        episode = episodes.nextElement();
                        episode.setTitle(language, episode.getComputedTitel());
                    }
                }
            }
        } else {
            boolean fanartSet = false;
            boolean languageIndependentsSet = false;
            for (Iterator<String> i_languages = languages.iterator(); i_languages.hasNext(); ) {
                String language = i_languages.next();
                if (supportedLanguages.contains(language)) {
                    plugin.setLanguage(language);
                    if (!languageIndependentsSet) {
                        series.setPersons(plugin.getPersons());
                        series.setIdTHETVDB(plugin.getID());
                        series.setIdIMDB(plugin.getIdIMDB());
                        series.setRating(plugin.getRating());
                        series.setGenres(plugin.getGenres());
                        series.setFirstAiredDate(plugin.getFirstAiredDate());
                    }
                    String seriesTitle = plugin.getSeriesTitle();
                    if (StringUtils.isNotEmpty(seriesTitle) && StringUtils.isEmpty(series.getTitle(language))) series.setTitle(language, new String(plugin.getSeriesTitle()));
                    if (!fanartSet) {
                        series.setBackdropURLs(plugin.getBackdrops());
                        series.setBannerURLs(plugin.getBanners());
                        series.setPosterURLs(plugin.getPosters());
                        fanartSet = true;
                    }
                    String seriesPlot = plugin.getSeriesPlot();
                    if (StringUtils.isNotEmpty(seriesPlot) && StringUtils.isEmpty(series.getPlot(language))) series.setPlot(language, new String(seriesPlot));
                    Hashtable<Integer, Season> seasons = series.getSeasons();
                    for (Enumeration<Season> e_seasons = seasons.elements(); e_seasons.hasMoreElements(); ) {
                        Season season = e_seasons.nextElement();
                        Hashtable<Integer, Episode> episodes = season.getEpisodes();
                        for (Enumeration<Episode> e_episodes = episodes.elements(); e_episodes.hasMoreElements(); ) {
                            Episode episode = e_episodes.nextElement();
                            Integer season_no = season.getSeasonNumber();
                            Integer episode_no = episode.getEpisodeNumber();
                            String title = plugin.getEpisodeTitle(season_no.toString(), episode_no.toString());
                            if (title != null) episode.setTitle(language, new String(title));
                            String plot = plugin.getEpisodePlot(season_no.toString(), episode_no.toString());
                            if (plot != null) episode.setPlot(language, new String(plot));
                            if (!languageIndependentsSet) {
                                String episodeScreen = plugin.getEpisodeScreen(season_no.toString(), episode_no.toString());
                                if (episodeScreen != null) {
                                    episode.setScreenshotURL(new String(episodeScreen));
                                }
                            }
                        }
                    }
                }
                languageIndependentsSet = true;
            }
        }
        return series;
    }

    public static Movie getMovieFanartOnline(Movie mf, Logger log) {
        IPluginFanart plugin = (IPluginFanart) PluginFactory.getPlugin(PluginFactory.TYPE_FANART, "TheMovieDB");
        String imdb_id = mf.getLanguageData().getID("imdb");
        if (imdb_id != null) plugin.setIMDBID(imdb_id); else {
            String id = mf.getLanguageData().getID("TheMovieDB");
            if (id != null) plugin.setID(id); else plugin.setIDByTitle(mf.getComputedTitel(), mf.getYear() + "");
        }
        LinkedList<String> covers = plugin.getCovers();
        Iterator<String> i = covers.iterator();
        while (i.hasNext()) mf.addCoverURLs(new String(i.next()));
        LinkedList<String> backdrops = plugin.getBackdrops();
        i = backdrops.iterator();
        while (i.hasNext()) mf.addBackdropURL(new String(i.next()));
        return mf;
    }

    /**
	 *
	 * Collects online-informations of a movie.
	 *
	 * @param mf The MovieFile
	 * @param log An instance of a Logger
	 * @return The manipulated MovieFile
	 */
    public static Movie getMovieInfosOnline(Movie mf, Logger log) {
        Languages languageData = mf.getLanguageData();
        String language;
        Plugins plugins;
        String plugin_name;
        if (UMCConstants.debug) log.debug("loading Default IMDB Plugin");
        IPluginMovieDB plugin;
        plugins.moviedb.IMDB imdbPlugin = (plugins.moviedb.IMDB) PluginFactory.getPlugin(PluginFactory.TYPE_MOVIEDB, "IMDB");
        if (UMCConstants.debug) log.debug("getting language data container");
        LinkedList<String> languages = new LinkedList<String>();
        languages.addAll(languageData.getLanguages());
        while (languages.size() > 0) {
            language = languages.removeFirst();
            try {
                Boolean prio_complete = false;
                Boolean standard_complete = false;
                if (UMCConstants.debug) log.debug("getting plugin configuration for language " + language);
                plugins = languageData.getPlugins(language);
                Plugins usedPlugins = new Plugins();
                while (plugins != null && (plugin_name = plugins.getNextPluginName()) != null && !prio_complete) {
                    usedPlugins.addPlugin(plugin_name);
                    plugin = (IPluginMovieDB) PluginFactory.getPlugin(1, plugin_name);
                    Object[] initData = initMoviePlugins(imdbPlugin, languageData.getID("IMDB"), plugin, languageData.getID(plugin_name), languageData.getTitle(language), mf.getComputedTitel(), String.valueOf(mf.getYear()), log);
                    imdbPlugin = (plugins.moviedb.IMDB) initData[0];
                    plugin = (IPluginMovieDB) initData[1];
                    mf = setGeneralMovieData(mf, imdbPlugin, plugin);
                    prio_complete = true;
                    standard_complete = true;
                    String tempValue;
                    if (!languageData.isSet("title", language)) {
                        if (StringUtils.isNotBlank(tempValue = plugin.getTitle())) if (plugins.getStandard("title") != null) {
                            if (plugins.getStandard("title") == plugin_name && !languageData.addTitle(new String(tempValue), language)) standard_complete = false;
                        } else if (!languageData.addTitle(new String(tempValue), language)) prio_complete = false;
                    }
                    if (!languageData.isSet("genres", language) && plugin.getGenres() != null) if (plugins.getStandard("genres") != null) {
                        if (plugins.getStandard("genres") == plugin_name) {
                            for (Iterator<String> i = plugin.getGenres().iterator(); i.hasNext(); ) languageData.addGenre(new String(i.next()), language);
                            if (!languageData.isSet("genres", language)) standard_complete = false;
                        }
                    } else {
                        for (Iterator<String> i = plugin.getGenres().iterator(); i.hasNext(); ) languageData.addGenre(new String(i.next()), language);
                        if (!languageData.isSet("genres", language)) prio_complete = false;
                    }
                    if (!languageData.isSet("plot", language)) if (StringUtils.isNotBlank(tempValue = plugin.getStory())) if (plugins.getStandard("plot") != null) {
                        if (plugins.getStandard("plot") == plugin_name && !languageData.addPlot(tempValue, language)) standard_complete = false;
                    } else if (!languageData.addPlot(tempValue, language)) prio_complete = false;
                    if (!languageData.isSet("countries", language)) {
                        LinkedList<String> tempList = new LinkedList<String>();
                        for (Iterator<String> i = plugin.getCountries().iterator(); i.hasNext(); ) tempList.add(new String(i.next()));
                        if (plugins.getStandard("countries") != null) {
                            if (plugins.getStandard("countries") == plugin_name && !languageData.addCountries(tempList, language)) standard_complete = false;
                        } else if (!languageData.addCountries(tempList, language)) prio_complete = false;
                    }
                    if (!languageData.isSet("ratings", language)) if (StringUtils.isNotBlank(tempValue = plugin.getStory())) if (plugins.getStandard("ratings") != null) {
                        if (plugins.getStandard("ratings") == plugin_name && !languageData.addRating(plugin.getRating(), language)) standard_complete = false;
                    } else if (!languageData.addRating(plugin.getRating(), language)) prio_complete = false;
                    if (plugin.getID() != null) languageData.addID(plugin_name, new String(plugin.getID()));
                    if (!standard_complete && plugins.getPriorityList().size() == 0) plugins = usedPlugins;
                }
            } catch (Exception e) {
                log.error("Allgemeiner Fehler!", e);
            }
            mf.setLanguageData(languageData);
        }
        return mf;
    }

    /**
	 * Tries to initialize/synchronize a plugin with the IMDB-Plugin.
	 *
	 * @param imdbPlugin
	 * @param imdbID
	 * @param plugin
	 * @param ID
	 * @param title
	 * @param computedTitle
	 * @param year
	 * @param log
	 * @return
	 */
    private static Object[] initMoviePlugins(IPluginMovieDB imdbPlugin, String imdbID, IPluginMovieDB plugin, String ID, String title, String computedTitle, String year, Logger log) {
        boolean IMDBset = false;
        if (imdbPlugin.getID() != null && !imdbPlugin.getID().equals("")) IMDBset = true; else if (imdbID != null && !imdbID.equals("")) {
            log.info("IMDB-ID for \"" + computedTitle + "\" is given: " + imdbID);
            imdbPlugin.setIMDBID(imdbID);
            IMDBset = true;
        }
        if (ID != null && !ID.equals("")) {
            log.info("Plugin-ID for \"" + computedTitle + "\" is given: " + ID);
            plugin.setID(ID);
            if (!IMDBset) {
                log.info("NO IMDB-ID found! Try to determine IMDB-ID for \"" + computedTitle + "\" by Plugin-ID.");
                imdbID = plugin.getIMDBID();
                if (imdbID != null && !imdbID.equals("")) {
                    imdbPlugin.setIMDBID(imdbID);
                    IMDBset = true;
                } else {
                    log.info("Plugin could not determine IMDB-ID for \"" + computedTitle + "\". Try to find IMDB-ID by searching for movie-title in IMDB.");
                    if (year == null || year.equals("") || year.equals("-1")) year = plugin.getYear();
                    imdbID = imdbPlugin.setIDByTitle(plugin.getTitle(), year);
                    if (imdbID != null && !imdbID.equals("")) {
                        log.info("Could determine IMDB-ID for \"" + computedTitle + "\" - Title: \"" + plugin.getTitle() + "\", IMDB-ID: \"" + imdbID + "\"");
                        IMDBset = true;
                    } else {
                        log.info("Could not determine IMDB-ID for  \"" + computedTitle + "\" (Searching for: \"" + plugin.getTitle() + "\" in IMDB). Try to search by alternative-title, if possible.");
                        String alternativeTitle = plugin.getAlternative();
                        if (alternativeTitle != null && !alternativeTitle.equals("")) {
                            imdbID = imdbPlugin.setIDByTitle(alternativeTitle, year);
                            if (imdbID != null && !imdbID.equals("")) {
                                log.info("IMDB-ID found for \"" + computedTitle + "\": " + imdbID + " - Alternative-Title: \"" + alternativeTitle + "\"");
                                IMDBset = true;
                            } else log.warn("NO IMDB-ID found for \"" + computedTitle + "\"! - Alternative-Title: \"" + alternativeTitle + "\"");
                        }
                    }
                }
            }
        } else {
            if (!IMDBset) {
                imdbID = imdbPlugin.setIDByTitle(computedTitle, year);
                if (imdbID != null && !imdbID.equals("")) IMDBset = true; else {
                    imdbID = imdbPlugin.setIDByTitle(computedTitle, year);
                    if (imdbID != null && !imdbID.equals("")) IMDBset = true;
                }
            }
            if (IMDBset) {
                boolean IDset = plugin.setIMDBID(imdbPlugin.getIMDBID());
                if (IDset) {
                    log.info("Plugin-ID for \"" + computedTitle + "\" found: " + plugin.getID() + ", Title: \"" + plugin.getTitle() + "\"");
                } else {
                    log.info("Could not determine Plugin-ID for \"" + computedTitle + "\" by using IMDB-ID \"" + imdbPlugin.getIMDBID() + "\"! Try to search by title.");
                    if (year == null || year.equals("") || year.equals("-1")) {
                        year = imdbPlugin.getYear();
                    }
                    ID = plugin.setIDByTitle(imdbPlugin.getTitle(), year);
                    if (StringUtils.isEmpty(ID)) {
                        log.info("Plugin could find ID for \"" + computedTitle + "\" by looking for \"" + imdbPlugin.getTitle() + "\": \"" + ID + "\" - Searching by computed title.");
                        ID = plugin.setIDByTitle(computedTitle, year);
                    }
                    if (ID != null && !ID.equals("")) {
                        log.info("Plugin could find ID for \"" + computedTitle + "\" by looking for \"" + imdbPlugin.getTitle() + "\": \"" + ID + "\"");
                    } else {
                        log.info("Plugin could not find ID for \"" + computedTitle + "\" by looking for \"" + imdbPlugin.getTitle() + "\". Try to search by alternative-title, if possible.");
                        if (imdbPlugin.getAlternative() == null) {
                            log.info("NO alternative-title found for \"" + computedTitle + "\".");
                        } else {
                            ID = plugin.setIDByTitle(imdbPlugin.getAlternative(), year);
                            if (ID != null && !ID.equals("")) {
                                log.info("Plugin-ID found for title \"" + computedTitle + "\": " + ID);
                            } else log.warn("Plugin-ID NOT found for title \"" + computedTitle + "\"!");
                        }
                    }
                }
            } else {
                ID = plugin.setIDByTitle(title, year);
                if (ID == null || ID.equals("")) ID = plugin.setIDByTitle(computedTitle, year);
                if (ID != null && !ID.equals("")) {
                    log.info("Plugin-ID for \"" + computedTitle + "\" found: " + ID + ", Title: \"" + plugin.getTitle() + "\"");
                    log.trace("Search for IMDB-ID by using title: \"" + computedTitle + "\"");
                    imdbID = plugin.getIMDBID();
                    if (imdbID != null && !imdbID.equals("")) {
                        imdbPlugin.setIMDBID(imdbID);
                        IMDBset = true;
                    } else {
                        if (year == null || year.equals("") || year.equals("-1")) year = plugin.getYear();
                        imdbID = imdbPlugin.setIDByTitle(plugin.getTitle(), year);
                        if ((imdbID == null || imdbID.equals("")) && plugin.getAlternative() != null) {
                            String alternativeTitle = plugin.getAlternative();
                            log.trace("Search ID for \"" + computedTitle + "\" by using alternative-title: \"" + alternativeTitle + "\"");
                            imdbID = plugin.setIDByTitle(alternativeTitle, year);
                            if (imdbID != null && !imdbID.equals("")) {
                                log.info("IMDB-ID found for titel \"" + computedTitle + "\": " + imdbID);
                                IMDBset = true;
                            } else log.warn("IMDB-ID NOT found for title \"" + computedTitle + "\"!");
                        }
                    }
                } else {
                    log.error("Neither IMDB-Plugin nor selected Plugin \"" + plugin.getClass() + "\" could find title \"" + computedTitle + "\" finden!");
                }
            }
        }
        Object[] result = new Object[2];
        result[0] = imdbPlugin;
        result[1] = plugin;
        return result;
    }

    /**
	 * Writes language-independant data to the Movie-File
	 *
	 * @param mf MovieFile
	 * @param imdb IMDB-Plugin
	 * @param plugin specific Plugin
	 * @return
	 */
    private static Movie setGeneralMovieData(Movie mf, plugins.moviedb.IMDB imdbPlugin, IPluginMovieDB plugin) {
        if (mf.getYear() == 0 || mf.getYear() == -1) {
            if (imdbPlugin.getYear() != null) mf.setYear((Integer) Integer.valueOf(imdbPlugin.getYear())); else if (plugin.getYear() != null) mf.setYear((Integer) Integer.valueOf(plugin.getYear()));
        }
        if (mf.getPersons().size() == 0 && imdbPlugin.getPersons().size() != 0) {
            LinkedList<IPerson> persons = imdbPlugin.getPersons();
            for (Iterator<IPerson> person_iterator = persons.iterator(); person_iterator.hasNext(); ) {
                IPerson person = person_iterator.next();
                if (StringUtils.isNotBlank(person.getIdIMDB())) {
                    person.setIdIMDB(new String(person.getIdIMDB()));
                    person.setName(new String(person.getName()));
                    person.setRole(new String(person.getRole()));
                    mf.addPerson(person);
                }
            }
        }
        if (StringUtils.isEmpty(mf.getFilmStudio())) mf.setFilmStudio(new String(imdbPlugin.getFilmCompany()));
        if (StringUtils.isEmpty(mf.getOriginalTitle())) mf.setOriginalTitle(new String(imdbPlugin.getTitle()));
        mf.getLanguageData().addID("IMDB", new String(imdbPlugin.getID()));
        return mf;
    }
}
