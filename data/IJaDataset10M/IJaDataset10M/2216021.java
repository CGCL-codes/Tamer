package myseriesproject.episodes;

import Exceptions.DatabaseException;
import java.util.logging.Level;
import myseriesproject.series.Series;
import tools.MySeriesLogger;
import database.DBConnection;
import database.EpisodesRecord;
import database.SeriesRecord;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import myComponents.MyUsefulFunctions;
import myComponents.myFileFilters.ZipFilter;
import myseriesproject.MySeries;
import tools.archive.ArchiveFile;
import tools.files.SubtitleMover;
import tools.files.VideoMover;
import tools.languages.LangsList;
import tools.languages.Language;
import tools.options.MySeriesOptions;

/**
 * The episodes table class
 * @author ssoldatos
 */
public class Episodes {

    /** The number of columns   */
    public static final int NUMBER_OF_COLUMS = 7;

    /** The episodes number table column : 0   */
    public static final int EPISODE_NUM_COLUMN = 0;

    /** The episodes title table column : 1   */
    public static final int EPISODERECORD_COLUMN = 1;

    /** The episodes aired table column : 2   */
    public static final int AIRED_COLUMN = 2;

    /** The episodes downloaded table column : 3   */
    public static final int DOWNLOADED_COLUMN = 3;

    /** The episodes sub status table column : 4   */
    public static final int SUBS_COLUMN = 4;

    /** The episodes seen table column : 5   */
    public static final int SEEN_COLUMN = 5;

    /** The episodes rate table column : 6   */
    public static final int RATE_COLUMN = 6;

    /** The Episodes number column title : Episode Number   */
    public static final String EPISODE_NUM_COLUMN_TITLE = "Episode";

    /** The episodes title table column title : Title   */
    public static final String EPISODERECORD_COLUMN_TITLE = "Title";

    /** The episodes aired table column title : Aired   */
    public static final String AIRED_COLUMN_TITLE = "Aired";

    /** The episodes downloaded table column  title: Video File   */
    public static final String DOWNLOADED_COLUMN_TITLE = "Video File";

    /** The episodes sub status table column title : Subtitle   */
    public static final String SUBS_COLUMN_TITLE = "Subtitle";

    /** The episodes seen table column title : Seen   */
    public static final String SEEN_COLUMN_TITLE = "Watched";

    /** The episodes rate table column title : rate   */
    public static final String RATE_COLUMN_TITLE = "Rate";

    /** The current episode   */
    private static EpisodesRecord currentEpisode;

    public static void setTableWidths(JTable table, Integer[] EpisodesTableWidths) {
        TableColumnModel model = table.getColumnModel();
        for (int i = 0; i < EpisodesTableWidths.length; i++) {
            Integer width = EpisodesTableWidths[i];
            model.getColumn(i).setPreferredWidth(width);
        }
    }

    private static void unzipSubtitleFiles(SeriesRecord series) {
        if (!series.isValidLocalDir()) {
            return;
        }
        File dir = new File(series.getLocalDir());
        File[] subs = dir.listFiles(new ZipFilter());
        for (int i = 0; i < subs.length; i++) {
            File file = subs[i];
            if (file.isFile()) {
                ArchiveFile u = new ArchiveFile(file);
                try {
                    if (u.unzip(series.getLocalDir(), true, ArchiveFile.SUBTITLES)) {
                        if (!u.extractedFiles.isEmpty()) {
                            MySeriesLogger.logger.log(Level.INFO, "Unzipped {0}", u.extractedFiles);
                            for (Iterator<String> it = u.extractedFiles.iterator(); it.hasNext(); ) {
                                String filename = it.next();
                                if (MySeries.options.getBooleanOption(MySeriesOptions.AUTO_RENAME_SUBS) && MyUsefulFunctions.renameEpisode(series, filename)) {
                                    MySeriesLogger.logger.log(Level.INFO, "Subtitle renamed");
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    MySeriesLogger.logger.log(Level.SEVERE, "Could not unzip " + file, ex);
                }
            }
        }
    }

    /**
   * Checks if an episode is available for watching
   * @param episode The episode record
   */
    public static boolean isWatchable(EpisodesRecord episode, boolean ignoreOption) {
        SeriesRecord series = Series.getCurrentSerial();
        return (episode != null && series.isValidLocalDir() && Episodes.checkDownloads(series, episode, ignoreOption));
    }

    private Episodes() {
    }

    /**
   * Sets the current episode
   * @param episode The episode id
   * @throws java.sql.SQLException
   */
    public static void setCurrentEpisode(int episode) throws SQLException {
        MySeriesLogger.logger.log(Level.INFO, "Setting the current episode");
        currentEpisode = EpisodesRecord.queryOne(null, EpisodesRecord.C_SERIES_ID + "= ? AND " + EpisodesRecord.C_EPISODE + "=?", new String[] { String.valueOf(Series.getCurrentSerial().getSeries_ID()), String.valueOf(episode) }, null, null, null);
        MySeriesLogger.logger.log(Level.FINE, "Current episode set to {0}", getCurrentEpisode().getTitle());
    }

    /**
   * Gets all the episodes of the current series
   * First empty episodes, create the model and apply it to the episodes table
   * Then prints the series fulltitle in the tabbed panel
   * @return An arraylist of all the episodes records
   * @throws java.sql.SQLException
   */
    public static ArrayList<EpisodesRecord> getCurrentSeriesEpisodes(JTable episodesTable) throws SQLException, DatabaseException {
        ArrayList<EpisodesRecord> eps = new ArrayList<EpisodesRecord>();
        ArrayList<EpisodesRecord> updated = new ArrayList<EpisodesRecord>();
        File[] subtitleFiles = null;
        File[] videoFiles = null;
        int episode;
        Boolean download, seen;
        String title, aired;
        Language subs;
        emptyEpisodes(episodesTable);
        SeriesRecord series = Series.getCurrentSerial();
        DefaultTableModel model = (DefaultTableModel) episodesTable.getModel();
        MySeriesLogger.logger.log(Level.INFO, "Getting episodes of series {0}", series.getFullTitle());
        if (series.isValidLocalDir()) {
            MySeriesLogger.logger.log(Level.INFO, "File auto updating is active");
            ArrayList<SeriesRecord> list = new ArrayList<SeriesRecord>();
            list.add(series);
            if (MySeries.options.getBooleanOption(MySeriesOptions.MOVE_VIDEO_FILES)) {
                VideoMover vm = new VideoMover(list);
                vm.move();
            }
            if (MySeries.options.getBooleanOption(MySeriesOptions.AUTO_EXTRACT_ZIPS)) {
                SubtitleMover sm = new SubtitleMover(list);
                sm.move();
                MySeriesLogger.logger.log(Level.INFO, "Auto extracting subtitles is active");
                unzipSubtitleFiles(series);
            }
            subtitleFiles = Series.getSubtitleFiles(series, false);
            videoFiles = Series.getVideoFiles(series, false);
        }
        if (Series.getCurrentSerial() == null) {
        }
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM episodes WHERE series_ID = " + Series.getCurrentSerial().getSeries_ID() + " ORDER BY CAST(episode AS UNSIGNED) ASC";
            Statement stmt = DBConnection.conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                EpisodesRecord e = new EpisodesRecord();
                e.setSeries_ID(rs.getInt("series_ID"));
                e.setEpisode_ID(rs.getInt("episode_ID"));
                e.setTitle(rs.getString("title"));
                aired = rs.getString("aired");
                e.setAired(rs.getString("aired"));
                episode = rs.getInt("episode");
                e.setEpisode(rs.getInt("episode"));
                download = rs.getBoolean("downloaded");
                e.setDownloaded(rs.getInt("downloaded"));
                e.setSeen(rs.getInt("seen"));
                e.setRate(rs.getDouble("rate"));
                e.setSubs(LangsList.getLanguageById(rs.getInt("subs")));
                boolean newDownloadedStatus = download;
                Language cSubs = e.getSubs();
                if (MyUsefulFunctions.hasBeenAired(e.getAired(), true)) {
                    seen = rs.getBoolean("seen");
                    if (videoFiles != null) {
                        newDownloadedStatus = checkDownloads(Series.getCurrentSerial().getSeason(), e.getEpisode(), videoFiles);
                    }
                    if (subtitleFiles != null) {
                        cSubs = checkSubs(Series.getCurrentSerial().getSeason(), e.getEpisode(), subtitleFiles);
                    }
                } else {
                    newDownloadedStatus = false;
                    cSubs = LangsList.NONE;
                    seen = false;
                }
                if (download != newDownloadedStatus) {
                    e.setDownloaded(newDownloadedStatus ? EpisodesRecord.DOWNLOADED : EpisodesRecord.NOT_DOWNLOADED);
                    updated.add(e);
                    download = newDownloadedStatus;
                }
                if (cSubs != e.getSubs()) {
                    e.setSubs(cSubs);
                    updated.add(e);
                }
                subs = e.getSubs();
                Object[] data = { episode, e, e.getAired(), download, e.getSubs(), seen, e.getRate() };
                model.addRow(data);
                eps.add(e);
            }
            MySeriesLogger.logger.log(Level.FINE, "Found {0} episodes", eps.size());
            if (!updated.isEmpty()) {
                MySeriesLogger.logger.log(Level.INFO, "Updating episodes");
                DBConnection.beginTransaction();
                long in = System.currentTimeMillis();
                for (Iterator<EpisodesRecord> it = updated.iterator(); it.hasNext(); ) {
                    EpisodesRecord episodesRecord = it.next();
                    episodesRecord.save();
                    MySeriesLogger.logger.log(Level.FINE, "Updating {0}", episodesRecord.getTitle());
                }
                long d = (System.currentTimeMillis() - in);
                MySeriesLogger.logger.log(Level.FINE, "Updating finished in {0} msec", d);
            }
            episodesTable.setModel(model);
            return eps;
        } catch (SQLException ex) {
            throw ex;
        } finally {
            rs.close();
            DBConnection.endTransaction();
        }
    }

    public static boolean checkDownloads(SeriesRecord series, EpisodesRecord e, boolean ignoreOption) {
        int season = series.getSeason();
        int episode = e.getEpisode();
        MySeriesLogger.logger.log(Level.INFO, "Checking downloaded of series {0} episode {1}", new String[] { series.getFullTitle(), e.getTitle() });
        File[] videoFiles = Series.getVideoFiles(series, ignoreOption);
        try {
            return checkDownloads(season, episode, videoFiles);
        } catch (SQLException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Sql exception occured", ex);
            return false;
        }
    }

    private static boolean checkDownloads(int season, int episode, File[] videoFiles) throws SQLException {
        if (videoFiles == null) {
            MySeriesLogger.logger.log(Level.INFO, "No video files found");
            return false;
        }
        String regex = MyUsefulFunctions.createRegex(season, episode);
        String regexFake = MyUsefulFunctions.createRegex(season, season * 10 + episode);
        Pattern pattern = Pattern.compile(regex);
        Pattern patternFake = Pattern.compile(regexFake);
        MySeriesLogger.logger.log(Level.INFO, "Getting video files  of season {0} episode {1}", new int[] { season, episode });
        for (int j = 0; j < videoFiles.length; j++) {
            File file = videoFiles[j];
            Matcher matcher = pattern.matcher(file.getName());
            Matcher matcherFake = patternFake.matcher(file.getName());
            if (matcher.find() && !matcherFake.find()) {
                MySeriesLogger.logger.log(Level.FINE, "Video file  found {0}", file.getName());
                return true;
            }
        }
        return false;
    }

    private static Language checkSubs(int season, int episode, File[] subtitleFiles) throws SQLException {
        boolean hasPrimary = false, hasSecondary = false, hasOther = false;
        int subsFound = 0, totalSubs = 0;
        Language other = LangsList.NONE;
        String regex = MyUsefulFunctions.createRegex(season, episode);
        String regexFake = MyUsefulFunctions.createRegex(season, season * 10 + episode);
        Pattern pattern = Pattern.compile(regex);
        Pattern patternFake = Pattern.compile(regexFake);
        MySeriesLogger.logger.log(Level.INFO, "Getting subtitle files  of season {0} episode {1}", new int[] { season, episode });
        for (int j = 0; j < subtitleFiles.length; j++) {
            File file = subtitleFiles[j];
            Matcher matcher = pattern.matcher(file.getName());
            Matcher matcherFake = patternFake.matcher(file.getName());
            if (matcher.find() && file.isFile() && !matcherFake.find()) {
                totalSubs++;
                for (Iterator it = myseriesproject.MySeries.languages.getLangs().iterator(); it.hasNext(); ) {
                    Language lang = (Language) it.next();
                    if (file.getName().indexOf("." + lang.getCode() + ".") > 0) {
                        if (lang.isIsPrimary()) {
                            subsFound++;
                            hasPrimary = true;
                        } else if (lang.isIsSecondary()) {
                            hasSecondary = true;
                            subsFound++;
                        } else {
                            hasOther = true;
                            other = lang;
                            subsFound++;
                        }
                    }
                }
            }
        }
        if (subsFound < totalSubs) {
            hasPrimary = true;
        }
        if (hasPrimary && hasSecondary) {
            MySeriesLogger.logger.log(Level.INFO, "Found multiple subtitles");
            return LangsList.MULTIPLE;
        } else if (hasPrimary) {
            MySeriesLogger.logger.log(Level.INFO, "Found primary subtitle");
            return myseriesproject.MySeries.languages.getPrimary();
        } else if (hasSecondary) {
            MySeriesLogger.logger.log(Level.INFO, "Found secondary subtitle");
            return myseriesproject.MySeries.languages.getSecondary();
        } else if (hasOther) {
            MySeriesLogger.logger.log(Level.INFO, "Found other subtitle");
            return other;
        }
        return LangsList.NONE;
    }

    /**
   * Updates the episodes table with the current series episodes
   * @throws java.sql.SQLException
   */
    public static void updateEpisodesTable(JTable table) throws SQLException, DatabaseException {
        MySeriesLogger.logger.log(Level.INFO, "Updating episodes table");
        Episodes.getCurrentSeriesEpisodes(table);
    }

    /**
   * Empty the episodes table and sets the tabbed pane title to empty string
   */
    public static void emptyEpisodes(JTable episodesTable) {
        MySeriesLogger.logger.log(Level.INFO, "Emptying episodes table");
        ((DefaultTableModel) episodesTable.getModel()).setRowCount(0);
    }

    /**
   * @return the currentEpisode
   */
    public static EpisodesRecord getCurrentEpisode() {
        return currentEpisode;
    }
}
