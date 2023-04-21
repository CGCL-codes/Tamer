package utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XBMCFile implements Constants {

    public static void main(String[] xxx) {
        System.out.println(new XBMCFile("Netflix-zXz-Instant Queue-zXz-Alphabetical-zXz-W-zXz-Wallace & Gromit in Three Amazing...-zXz-01: A Close Shave").stripExtras(" Gettysburg (HD)"));
    }

    String fanart, file, fileLabel, thumbnail, parentPath, type, title, series, artist;

    int episodeNumber = -1, seasonNumber = -1, year = -1;

    String tvdbId;

    String originalAirDate;

    Subfolder subfolder;

    String finalLocation = null;

    boolean hasBeenLookedUpOnTVDB = false;

    String fileOrDir;

    private boolean skippedBecauseAlreadyArchived = false;

    private boolean isDuplicate = false;

    private boolean isMultiFile = false;

    public List<XBMCFile> duplicateVideos = null;

    private XBMCFile originalVideo = null;

    public static void copyVideoMetaData(XBMCFile source, XBMCFile dest) {
        dest.setTitle(source.getTitle());
        dest.setType(source.getType());
        dest.setArtist(source.getArtist());
        dest.setEpisodeNumber(source.getEpisodeNumber());
        dest.setFileLabel(source.getFileLabel());
        dest.setFinalLocation(source.getFinalLocation());
        dest.setHasBeenLookedUpOnTVDB(source.hasBeenLookedUpOnTVDB());
        dest.setMultiFileVideo(source.isMultiFileVideo());
        dest.setOriginalAirDate(source.getOriginalAirDate());
        if (source.isDuplicate()) dest.setAsDuplicateTo(source.getOriginalVideo()); else dest.setOriginalVideo(source.getOriginalVideo());
        dest.setSeasonNumber(source.getSeasonNumber());
        dest.setSeries(source.getSeries());
        dest.setSkippedBecauseAlreadyArchived(source.skippedBecauseAlreadyArchived);
        dest.setSubfolder(source.getSubfolder());
        dest.setTVDBId(source.getTVDBId());
        dest.setYear(source.getYear());
    }

    public XBMCFile(String fileOrDir, String fanart, String file, String fileLabel, String thumbnail, String parentPath, Subfolder matchingSubfolder) {
        this.fileOrDir = fileOrDir;
        this.fanart = fanart;
        this.file = file;
        this.fileLabel = fileLabel;
        this.thumbnail = thumbnail;
        this.parentPath = parentPath;
        this.subfolder = matchingSubfolder;
    }

    public XBMCFile(String fileOrDir, String fanart, String file, String fileLabel, String thumbnail) {
        this.fileOrDir = fileOrDir;
        this.fanart = fanart;
        this.file = file;
        this.fileLabel = fileLabel;
        this.thumbnail = thumbnail;
    }

    public XBMCFile(String fullPlayonPath) {
        String[] parts = fullPlayonPath.split(DELIM);
        this.fileLabel = parts[parts.length - 1];
        this.parentPath = fullPlayonPath.substring(0, fullPlayonPath.lastIndexOf(DELIM));
    }

    public void setOriginalVideo(XBMCFile video) {
        this.originalVideo = video;
    }

    public XBMCFile getOriginalVideo() {
        return originalVideo;
    }

    public void setSkippedBecauseAlreadyArchived(boolean b) {
        this.skippedBecauseAlreadyArchived = b;
    }

    public boolean skippedBecauseAlreadyArchived() {
        return skippedBecauseAlreadyArchived;
    }

    public boolean isMultiFileVideo() {
        return isMultiFile;
    }

    public boolean isDuplicate() {
        return isDuplicate;
    }

    public void setMultiFileVideo(boolean b) {
        this.isMultiFile = b;
        if (duplicateVideos == null) duplicateVideos = new ArrayList<XBMCFile>();
    }

    public void setAsDuplicateTo(XBMCFile originalVideo) {
        this.isDuplicate = true;
        this.originalVideo = originalVideo;
    }

    public void addDuplicateVideo(XBMCFile video) {
        duplicateVideos.add(video);
    }

    public boolean isFile() {
        return fileOrDir.equals(FILE);
    }

    public boolean isDirectory() {
        return fileOrDir.equals(DIRECTORY);
    }

    public void setHasBeenLookedUpOnTVDB(boolean hasBeenLookedUpOnTVDB) {
        this.hasBeenLookedUpOnTVDB = hasBeenLookedUpOnTVDB;
    }

    public boolean hasBeenLookedUpOnTVDB() {
        return hasBeenLookedUpOnTVDB;
    }

    public void setFinalLocation(String finalLocation) {
        this.finalLocation = finalLocation;
    }

    public String getFinalLocation() {
        return finalLocation;
    }

    public void setSubfolder(Subfolder subf) {
        this.subfolder = subf;
    }

    public Subfolder getSubfolder() {
        return subfolder;
    }

    public boolean addTVDBSeriesEpisodeNumbers(String seasonNumber, String episodeNumber) {
        try {
            int s = Integer.parseInt(seasonNumber);
            int e = Integer.parseInt(episodeNumber);
            setSeasonNumber(s);
            setEpisodeNumber(e);
            return true;
        } catch (Exception x) {
            return false;
        }
    }

    public String getOriginalAirDate() {
        return originalAirDate;
    }

    public void setOriginalAirDate(String originalAirDate) {
        this.originalAirDate = originalAirDate;
    }

    public boolean isTVDBIdOverridden() {
        return tools.valid(getTVDBId());
    }

    public String getTVDBId() {
        return tvdbId;
    }

    public void setTVDBId(String id) {
        this.tvdbId = id;
    }

    public boolean hasValidMetaData() {
        if (isMovie() && tools.valid(getTitle())) return true;
        if (isTvShow() && tools.valid(getSeries()) && getSeasonNumber() > -1 && getEpisodeNumber() > -1) return true;
        if (isMusicVideo() && tools.valid(getTitle()) && tools.valid(getArtist())) return true;
        return false;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public String getSeasonEpisodeNaming() {
        return "S" + getPaddedSeasonNumber() + "E" + getPaddedEpisodeNumber();
    }

    public String getPaddedEpisodeNumber() {
        return padNumber(getEpisodeNumber());
    }

    public String getPaddedSeasonNumber() {
        return padNumber(getSeasonNumber());
    }

    private String padNumber(int num) {
        if (num < 10) return "0" + num; else return "" + num;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean hasYear() {
        return year != -1;
    }

    public void setSeries(String series) {
        this.series = stripExtras(series);
    }

    public String getSeries() {
        return series;
    }

    public void setTitle(String title) {
        this.title = stripExtras(title);
    }

    public String getTitle() {
        return title;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean knownType() {
        return isMovie() || isTvShow() || isMusicVideo();
    }

    public boolean isTvShow() {
        return TV_SHOW.equals(type);
    }

    public boolean isMovie() {
        return MOVIE.equals(type);
    }

    public boolean isMusicVideo() {
        return MUSIC_VIDEO.equals(type);
    }

    public String getFanart() {
        return fanart;
    }

    public String getFile() {
        return file;
    }

    public String getFileList() {
        if (!isMultiFileVideo()) return file; else {
            String files = "";
            Map<String, String> fileMap = new TreeMap<String, String>();
            for (Iterator<XBMCFile> it = duplicateVideos.iterator(); it.hasNext(); ) {
                XBMCFile video = it.next();
                if (video.getFileLabel().toLowerCase().contains("full")) {
                    return video.getFile();
                }
                fileMap.put(video.getFileLabel(), video.getFile());
            }
            String sourceId = null;
            boolean useAllFiles = false;
            for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                String label = entry.getKey();
                String file = entry.getValue();
                if (sourceId == null && !useAllFiles) {
                    if (label.toLowerCase().contains("part")) sourceId = label.substring(0, label.toLowerCase().indexOf("part")); else useAllFiles = true;
                }
                if (useAllFiles || label.startsWith(sourceId)) {
                    files += file + LINE_BRK;
                }
            }
            return files.replaceAll("\\r\\n" + "$", "");
        }
    }

    public String getFileLabel() {
        return fileLabel;
    }

    public void setFileLabel(String fileLabel) {
        this.fileLabel = fileLabel;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getFullPathEscaped() {
        return Config.escapePath(getParentPath() + "/" + getFileLabel());
    }

    public String getParentPathEscaped() {
        return Config.escapePath(getParentPath());
    }

    public String getFullPath() {
        return getParentPath() + DELIM + getFileLabel();
    }

    public String getParentPath() {
        return parentPath;
    }

    public String stripExtras(String source) {
        if (!tools.valid(source)) return source;
        source = source.trim();
        String yearDigits = "[1,2][0-9]{3}";
        String optionalYear = " (\\(" + yearDigits + "\\)|\\[" + yearDigits + "\\])";
        Pattern yearPattern = Pattern.compile(optionalYear, Pattern.CASE_INSENSITIVE);
        Matcher m = yearPattern.matcher(source);
        while (m.find()) {
            String match = m.group().trim();
            if (match.equals(source.substring(source.length() - match.length(), source.length()))) {
                source = source.substring(0, source.length() - match.length()).trim();
                setYear(Integer.parseInt(match.substring(1, match.length() - 1)));
                break;
            }
        }
        source = tools.stripExtraLabels(source);
        return source;
    }

    public boolean isWithinLimits(Subfolder subf) {
        boolean withinLimits = true;
        if (isTvShow()) {
            withinLimits = subf.canAddAnotherSeries(getSeries());
        }
        return withinLimits;
    }
}
