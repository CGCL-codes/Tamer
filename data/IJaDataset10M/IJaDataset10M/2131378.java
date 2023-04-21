package geodress.model;

import geodress.exceptions.FileTypeNotSupportedException;
import geodress.exceptions.MetaDataErrorException;
import geodress.exceptions.NoMetaDataException;
import geodress.exceptions.OperationNotSupportedException;
import geodress.exceptions.ParameterOutOfRangeException;
import geodress.main.InfoConstants;
import geodress.main.Logging;
import geodress.model.reader.SanselanReader;
import geodress.model.reader.LocationReader;
import geodress.model.writer.MetaDataWriter;
import geodress.ui.ProgressInfo;
import geodress.ui.graphical.PictureTable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileFilter;

/**
 * This class is the list of pictures that are currently edited. Here are
 * methods to search the addresses of all pictures.
 * 
 * @author Stefan T.
 */
public class PictureBox {

    /** Logger object */
    private Logger logger = null;

    /** code for TXT format */
    public static final int FORMAT_TXT = 0;

    /** code for CSV format */
    public static final int FORMAT_CSV = 1;

    /** code for TXT format with OS-dependent line breaks */
    public static final int FORMAT_TXT_OS = 2;

    /** time in milliseconds to wait between 2 Google Maps requests */
    private long requestInterval = 200;

    /**
	 * This is an array with the column titles of the table that contains the
	 * pictures. It refers to the output of
	 * {@link PictureBox#getPicturesAsArray()} . But just the columns that are
	 * described in this array will be shown via {@link PictureTable}, other
	 * columns contain just additional information.
	 */
    public final String[] columnNames = { "Name", "Date/Time", "Latitude", "Longitude", "Address" };

    /** list with all pictures */
    private List<Picture> pictureList;

    /**
	 * Loads the files.
	 * 
	 * @param directory
	 *            the directory to load the files from
	 * @throws IOException
	 *             thrown if an error occurs while reading a file
	 */
    public PictureBox(File directory) throws IOException {
        logger = Logging.getLogger(this.getClass().getName());
        pictureList = new ArrayList<Picture>();
        Picture pic = null;
        if (directory.exists()) {
            File[] dirEntries = directory.listFiles();
            for (File entry : dirEntries) {
                logger.log(Level.FINEST, "add a picture " + entry.getName() + " to picture box");
                FileFilter filter = new PictureFilter();
                if (entry.isFile() && filter.accept(entry)) {
                    try {
                        pic = new Picture(entry);
                        pic.registerMetaDataReader(new SanselanReader());
                        pictureList.add(pic);
                    } catch (FileTypeNotSupportedException ftnse) {
                        logger.log(Level.FINER, "file type of " + entry.getName() + " is not supported", ftnse);
                    }
                }
            }
            Collections.sort(pictureList);
        } else {
            throw new FileNotFoundException("directory " + directory.getAbsolutePath() + " does not exist");
        }
    }

    /**
	 * Returns all pictures.
	 * 
	 * @param index
	 *            the number of the picture
	 * @return the picture number <i>index</i>
	 */
    public Picture getPictures(int index) {
        return pictureList.get(index);
    }

    /**
	 * Returns a single picture.
	 * 
	 * @return all pictures as an array
	 */
    public Picture[] getPictures() {
        return pictureList.toArray(new Picture[0]);
    }

    /**
	 * Returns the number of pictures.
	 * 
	 * @return the number of pictures entries
	 */
    public int size() {
        return pictureList.size();
    }

    /**
	 * Returns all pictures as an two-dimensional array.
	 * 
	 * @return all pictures as an array in the form String[picture
	 *         number][field] where the fields are:<br>
	 *         0 = name of picture<br>
	 *         1 = date/time<br>
	 *         2 = latitude<br>
	 *         3 = longitude<br>
	 *         4 = address
	 * @see PictureBox#columnNames
	 * @deprecated This method is not efficient.
	 */
    @Deprecated
    public String[][] getPicturesAsArray() {
        Picture[] pictures = getPictures();
        String[][] pictureArray = new String[pictures.length][9];
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        for (int i = 0; i < pictures.length; i++) {
            pictureArray[i][0] = pictures[i].getName();
            try {
                pictureArray[i][1] = dateFormatter.format(pictures[i].getDate().getTime());
            } catch (MetaDataErrorException mdee) {
                pictureArray[i][1] = Picture.NO_DATA;
            }
            try {
                pictureArray[i][2] = String.valueOf(pictures[i].getCoordinates().getLatitudeAsString());
            } catch (MetaDataErrorException mdee) {
                pictureArray[i][2] = Picture.NO_DATA;
            } catch (ParameterOutOfRangeException poore) {
                pictureArray[i][2] = "not valid";
            }
            try {
                pictureArray[i][3] = String.valueOf(pictures[i].getCoordinates().getLongitudeAsString());
            } catch (MetaDataErrorException mdee) {
                pictureArray[i][3] = Picture.NO_DATA;
            } catch (ParameterOutOfRangeException poore) {
                pictureArray[i][3] = "not valid";
            }
            pictureArray[i][4] = pictures[i].getAddress().toString();
        }
        return pictureArray;
    }

    /**
	 * Gets the picture table in a special format.
	 * <ul>
	 * <li>for {@link PictureBox#FORMAT_TXT}: uses two spaces as field separator
	 * and <i>\n</i> as line separator</li>
	 * <li>for {@link PictureBox#FORMAT_CSV}: uses a comma as field separator
	 * and <i>\n</i> as line separator (according to RFC 4180)</li>
	 * <li>for {@link PictureBox#FORMAT_TXT_OS}: uses two spaces as field
	 * separator and the system default line separator</li>
	 * </ul>
	 * 
	 * @param format
	 *            the format, use the constant field values of this class
	 * 
	 * @return all pictures in the according format or an empty String if the
	 *         format doesn't exist
	 * @see PictureBox#getPicturesAsString(String, String, boolean)
	 */
    public String getPicturesAsString(int format) {
        switch(format) {
            case FORMAT_TXT:
                return getPicturesAsString("  ", "\n", false);
            case FORMAT_CSV:
                return getPicturesAsString(",", "\n", true);
            case FORMAT_TXT_OS:
                return getPicturesAsString("  ", System.getProperty("line.separator"), false);
        }
        return "";
    }

    /**
	 * Gets the picture table in text format with special field- and line
	 * separators.
	 * 
	 * @param fieldSeparator
	 *            this String separates the single field values (for example a
	 *            comma for a CSV file according to RFC 4180)
	 * @param lineSeparator
	 *            the line separator (for example <i>\n</i> for UNIX text files)
	 * @param quoted
	 *            if set to <code>true</code>, all field values be will be
	 *            surrounded by &quot;
	 * @return all pictures in this box as text
	 */
    private String getPicturesAsString(String fieldSeparator, String lineSeparator, boolean quoted) {
        String[][] table = getPicturesAsArray();
        StringBuilder output = new StringBuilder();
        if (table.length > 0) {
            int longestValue[] = new int[columnNames.length];
            for (int column = 0; column < columnNames.length; column++) {
                for (int row = 0; row < table.length; row++) {
                    if (table[row][column].length() > longestValue[column]) {
                        longestValue[column] = table[row][column].length();
                    }
                }
            }
            for (int column = 0; column < columnNames.length; column++) {
                if (columnNames[column].length() > longestValue[column]) {
                    longestValue[column] = columnNames[column].length();
                }
            }
            for (int column = 0; column < columnNames.length; column++) {
                if (quoted) output.append("\"");
                output.append(columnNames[column] + filledString(longestValue[column] - columnNames[column].length(), ' '));
                if (quoted) output.append("\"");
                output.append(fieldSeparator);
            }
            output.append(lineSeparator);
            for (int column = 0; column < columnNames.length; column++) {
                output.append(filledString(longestValue[column], '-'));
                output.append(fieldSeparator);
            }
            output.append(lineSeparator);
            for (int row = 0; row < table.length; row++) {
                for (int column = 0; column < columnNames.length; column++) {
                    if (quoted) output.append("\"");
                    output.append(table[row][column] + filledString(longestValue[column] - table[row][column].length(), ' '));
                    if (quoted) output.append("\"");
                    output.append(fieldSeparator);
                }
                output.append(lineSeparator);
            }
        }
        return output.toString();
    }

    /**
	 * Gets the addresses for all pictures in this box.
	 * 
	 * @param reader
	 *            the reader that is used to get the addresses
	 * @param progress
	 *            the progress monitor that is used
	 * @throws UnknownHostException
	 *             {@link LocationReader#getAddress(Coordinate)}
	 */
    public void catchAddresses(LocationReader reader, ProgressInfo progress) throws UnknownHostException {
        progress.setMaxValue(pictureList.size());
        Iterator<Picture> iterator = pictureList.iterator();
        while (iterator.hasNext()) {
            Picture picture = iterator.next();
            progress.increaseValue();
            try {
                picture.setAddress(reader.getAddress(picture.getCoordinates()));
                try {
                    Thread.sleep(requestInterval);
                } catch (InterruptedException ie) {
                    logger.log(Level.FINE, "error while pause between requests", ie);
                }
            } catch (NoMetaDataException nmde) {
                logger.log(Level.FINE, "while catching addresses: no meta data in " + picture.getName());
            } catch (MetaDataErrorException mdee) {
                logger.log(Level.WARNING, "error while reading coordinates from " + picture.getName(), mdee);
            } catch (ParameterOutOfRangeException poore) {
                logger.log(Level.WARNING, "error while reading coordinates from " + picture.getName(), poore);
            }
        }
        progress.end();
    }

    /**
	 * Writes all caught addresses to the files.
	 * 
	 * @param writer
	 *            the writer that is used to write the files
	 * @param field
	 *            the field (see {@link InfoConstants}) that should be written
	 *            to - there will be no writing process if the field is not
	 *            supported
	 * @param progress
	 *            the progress monitor that is used
	 */
    public void writeAddresses(MetaDataWriter writer, int field, ProgressInfo progress) {
        progress.setMaxValue(pictureList.size());
        Iterator<Picture> iterator = pictureList.iterator();
        while (iterator.hasNext()) {
            Picture picture = iterator.next();
            progress.increaseValue();
            if (field == InfoConstants.IMAGE_DESCRIPTION || field == InfoConstants.USER_COMMENT) {
                try {
                    writer.setData(field, picture.getAddress().toString());
                    writer.write(picture);
                    picture.getAddress().setStatus(Address.STATUS_SAVED);
                } catch (OperationNotSupportedException onse) {
                    logger.log(Level.WARNING, "exception while writing addresses to field " + field, onse);
                } catch (MetaDataErrorException mdee) {
                    logger.log(Level.WARNING, "meta data error while writing addresses to field " + field, mdee);
                } catch (IOException ioe) {
                    logger.log(Level.WARNING, "I/O exception while writing addresses to field " + field, ioe);
                } catch (FileTypeNotSupportedException ftnse) {
                    logger.log(Level.WARNING, "error while writing addresses: file type was not supported", ftnse);
                }
            } else {
                logger.log(Level.WARNING, "Picture data could not be written to field " + field + ". Nothing will be done.");
            }
        }
        progress.end();
    }

    /**
	 * Saves all pictures data to a file.
	 * 
	 * @param file
	 *            the file where to be saved, it will be overwritten if it
	 *            already exists!
	 * @param format
	 *            the format of the output file, for example
	 *            {@link PictureBox#FORMAT_TXT} for a text file
	 * @throws IOException
	 *             if an error occurs while writing the file
	 * @see PictureBox#getPicturesAsString(int)
	 */
    public void saveToFile(File file, int format) throws IOException {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file), 512);
            bw.write(getPicturesAsString(format));
        } finally {
            bw.close();
        }
    }

    /**
	 * Returns a string filled with one character.
	 * 
	 * @param length
	 *            the number of characters
	 * @param symbol
	 *            the character that should be filled with
	 * @return a string
	 */
    private static String filledString(int length, char symbol) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(symbol);
        }
        return sb.toString();
    }
}
