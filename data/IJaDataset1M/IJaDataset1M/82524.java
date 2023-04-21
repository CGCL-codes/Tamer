package org.jcvi.fastX.fastq;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code IlluminaUtil} is a utility class for working with Illumina data.
 * @author dkatzel
 *
 *
 */
public class IlluminaUtil {

    private static final Pattern NAME_PATTERN = Pattern.compile("^(SOLEXA\\d+).*:(\\d+):(\\d+):(\\d+):(\\d+)#(\\D+)?(\\d+)?\\/(\\d+)$");

    public static boolean isIlluminaRead(String readId) {
        if (readId == null) {
            throw new NullPointerException();
        }
        Matcher matcher = NAME_PATTERN.matcher(readId);
        return matcher.matches();
    }

    /**
     * Gets the unique instrument name from the given read id.
     * @param illuminaReadId the illumina read id to parse.
     * @return the instrument name as a String.
     * @throws IllegalArgumentException if the given read id is not a valid
     * Illumina Read ID.
     * @throws NullPointerException if the given id is null.
     */
    public static final String getInstrumentName(String illuminaReadId) {
        if (illuminaReadId == null) {
            throw new NullPointerException();
        }
        Matcher matcher = NAME_PATTERN.matcher(illuminaReadId);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("is not an illumina read id " + illuminaReadId);
        }
        return matcher.group(1);
    }

    /**
     * Gets the flowcell lane from the given read id.
     * @param illuminaReadId the illumina read id to parse.
     * @return the flowcell lane as an int.
     * @throws IllegalArgumentException if the given read id is not a valid
     * Illumina Read ID.
     * @throws NullPointerException if the given id is null.
     */
    public static final int getFlowcellLane(String illuminaReadId) {
        if (illuminaReadId == null) {
            throw new NullPointerException();
        }
        Matcher matcher = NAME_PATTERN.matcher(illuminaReadId);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("is not an illumina read id " + illuminaReadId);
        }
        return Integer.parseInt(matcher.group(2));
    }

    /**
     * Gets the tile number within the flowcell lane from the given read id.
     * @param illuminaReadId the illumina read id to parse.
     * @return the tile number within the flowcell lane as an int.
     * @throws IllegalArgumentException if the given read id is not a valid
     * Illumina Read ID.
     * @throws NullPointerException if the given id is null.
     */
    public static final int getTileNumber(String illuminaReadId) {
        if (illuminaReadId == null) {
            throw new NullPointerException();
        }
        Matcher matcher = NAME_PATTERN.matcher(illuminaReadId);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("is not an illumina read id " + illuminaReadId);
        }
        return Integer.parseInt(matcher.group(3));
    }

    /**
     * Gets the x-coordinate of the cluster within the tile from the given read id.
     * @param illuminaReadId the illumina read id to parse.
     * @return the x-coordinate of the cluster within the tile as an int.
     * @throws IllegalArgumentException if the given read id is not a valid
     * Illumina Read ID.
     * @throws NullPointerException if the given id is null.
     */
    public static final int getXClusterCoordinate(String illuminaReadId) {
        if (illuminaReadId == null) {
            throw new NullPointerException();
        }
        Matcher matcher = NAME_PATTERN.matcher(illuminaReadId);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("is not an illumina read id " + illuminaReadId);
        }
        return Integer.parseInt(matcher.group(4));
    }

    /**
     * Gets the y-coordinate of the cluster within the tile from the given read id.
     * @param illuminaReadId the illumina read id to parse.
     * @return the y-coordinate of the cluster within the tile as an int.
     * @throws IllegalArgumentException if the given read id is not a valid
     * Illumina Read ID.
     * @throws NullPointerException if the given id is null.
     */
    public static final int getYClusterCoordinate(String illuminaReadId) {
        if (illuminaReadId == null) {
            throw new NullPointerException();
        }
        Matcher matcher = NAME_PATTERN.matcher(illuminaReadId);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("is not an illumina read id " + illuminaReadId);
        }
        return Integer.parseInt(matcher.group(5));
    }

    /**
     * Gets the multiplex index number for the sample.  An index of 
     * {@code 0} means not indexed.
     * @param illuminaReadId the illumina read id to parse.
     * @return multiplex index number as an int.
     * @throws IllegalArgumentException if the given read id is not a valid
     * Illumina Read ID.
     * @throws NullPointerException if the given id is null.
     */
    public static final int getMultiplexIndex(String illuminaReadId) {
        if (illuminaReadId == null) {
            throw new NullPointerException();
        }
        Matcher matcher = NAME_PATTERN.matcher(illuminaReadId);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("is not an illumina read id " + illuminaReadId);
        }
        return Integer.parseInt(matcher.group(7));
    }

    /**
     * Gets the pair number if this read is paired-end or mate-paired.
     * @param illuminaReadId the illumina read id to parse.
     * @return multiplex index number as an int.
     * @throws IllegalArgumentException if the given read id is not a valid
     * Illumina Read ID or is not a member of a pair.
     * @throws NullPointerException if the given id is null.
     */
    public static final int getPairNumber(String illuminaReadId) {
        if (illuminaReadId == null) {
            throw new NullPointerException();
        }
        Matcher matcher = NAME_PATTERN.matcher(illuminaReadId);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("is not an illumina read id or member of a pair" + illuminaReadId);
        }
        return Integer.parseInt(matcher.group(8));
    }
}
