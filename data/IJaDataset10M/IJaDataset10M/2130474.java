package org.aubg.comparisong.util;

import jaudio.custom.FeatureOutput;
import jaudio.custom.FeatureOutputContainer;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.aubg.comparisong.datamodel.DataManager;

public class Util {

    public static void output(String output) {
        System.out.println(output);
    }

    public static String getDoubleInScientificNotation(double number_to_round, int significant_digits) {
        if (Double.isNaN(number_to_round)) return new String("NaN");
        if (Double.isInfinite(number_to_round)) return new String("Infinity");
        String format_pattern = "0.";
        for (int i = 0; i < significant_digits - 1; i++) format_pattern += "#";
        format_pattern += "E0";
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(java.util.Locale.ENGLISH);
        DecimalFormat decimal_formatter = (DecimalFormat) formatter;
        decimal_formatter.applyPattern(format_pattern);
        return decimal_formatter.format(number_to_round);
    }

    public static void printFeatures() {
        for (FeatureOutput fo : FeatureOutputContainer.features) {
            Util.output("Feature: ");
            Util.output(fo.toString());
            Util.output("\n");
        }
    }

    /***************************************************************************
	 * Transforms the double [][] generic feature values into double [] feature
	 * values. First index is count of window, second is count of dimension. But
	 * fund frequency/RMS has 1 dimension, therefore 2nd index is discarded
	 * 
	 * @param featureTimeSeries
	 * @return
	 */
    public static double[] transformTimeSeries(double[][] featureTimeSeries) {
        double[] tmpTimeSeries = new double[featureTimeSeries.length];
        for (int i = 0; i < featureTimeSeries.length; i++) {
            tmpTimeSeries[i] = featureTimeSeries[i][0];
        }
        return tmpTimeSeries;
    }

    /***************************************************************************
	 * Get minimal value of a time series
	 * 
	 * @param series
	 * @return
	 */
    public static double getMin(double[] series) {
        double min = series[0];
        for (int i = 1; i < series.length; i++) {
            if (series[i] < min) min = series[i];
        }
        return min;
    }

    /***************************************************************************
	 * Used to convert char[] into string
	 * 
	 * @param input
	 * @return String
	 */
    public static String convertCharArrayToString(char[] input) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < input.length; i++) {
            char tmpChar = input[i];
            buff.append(tmpChar);
        }
        return buff.toString();
    }

    /***************************************************************************
	 * Matches the tagged target song with the real counterpart. Uses id3
	 * tagging.
	 * 
	 * @param path
	 *            to tagged target song
	 * @return author and data 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * @SuppressWarnings
	 */
    public static String[] getMetadata(File realSong) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat baseAudioFileFormat = null;
        String author = null;
        String title = null;
        baseAudioFileFormat = AudioSystem.getAudioFileFormat(realSong);
        Map properties = baseAudioFileFormat.properties();
        author = (String) properties.get("author");
        title = (String) properties.get("title");
        return new String[] { author.trim(), title.trim() };
    }

    /***************************************************************************
	 * 1. Gets metadata for given File if it has metadata. else tries to get
	 * data from the title 2. Gets metadata for each Real Song in Db dir and
	 * matches
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * 
	 */
    public static File matchTaggedSong(File taggedFile) throws UnsupportedAudioFileException, IOException {
        String fileName = taggedFile.getName();
        String[] matchMetadata = null;
        int pointIndex = fileName.lastIndexOf(".");
        String songName = fileName.substring(0, pointIndex);
        matchMetadata = songName.split("-");
        for (int i = 0; i < matchMetadata.length; i++) {
            matchMetadata[i] = matchMetadata[i].trim();
        }
        File[] realSongs = DataManager.preprocessor.getRealSongs();
        for (int i = 0; i < realSongs.length; i++) {
            File tmpSong = realSongs[i];
            String[] tmpMetadata = getMetadata(tmpSong);
            if (tmpMetadata[0].equalsIgnoreCase(matchMetadata[0]) && tmpMetadata[1].equalsIgnoreCase(matchMetadata[1])) return tmpSong;
        }
        return null;
    }

    /***************************************************************************
	 * Sieves out audio files
	 * 
	 * @param audioDir
	 * @return array of loaded files
	 * @throws AudioFilesException 
	 */
    public static File[] getAudioFiles(String audioDir) throws AudioFilesException {
        File currDir = new File(audioDir);
        if (!currDir.exists() || !currDir.isDirectory()) {
            throw new AudioFilesException(audioDir + "not a directory or does not exist");
        }
        FileFilter songFilter = new FileFilter() {

            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".wav") || pathname.getName().endsWith(".mp3")) return true;
                return false;
            }
        };
        File[] songFiles = currDir.listFiles(songFilter);
        if (songFiles.length == 0) {
            throw new AudioFilesException("no audio files in directory" + audioDir);
        }
        return songFiles;
    }

    /**
	 * @param dbDir - path to dir with db audio files
	 * @return taggedSongFiles 
	 * @return realSongFiles
	 * @throws AudioFilesException 
	 * */
    public static void loadFilesFromDBDirectory(String dbDir, Holder<File[]> taggedSongFiles, Holder<File[]> realSongFiles) throws AudioFilesException {
        realSongFiles.value = Util.getAudioFiles(dbDir);
        String taggedDir = dbDir + "/tagged";
        taggedSongFiles.value = Util.getAudioFiles(taggedDir);
    }

    /**
	 * Truncates to 2 decimal digits
	 * 
	 * @param x
	 * @return
	 */
    public static double truncate(double x) {
        long y = (long) (x * 100);
        return (double) y / 100;
    }
}
