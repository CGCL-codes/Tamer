package uk.co.caprica.vlcj.radio.model;

/**
 * Specification for a media station directory entry.
 */
public interface DirectoryEntry extends Comparable<DirectoryEntry> {

    /**
   * Get the directory.
   * 
   * @return directory
   */
    String getDirectory();

    /**
   * Get the name.
   * 
   * @return name
   */
    String getName();

    /**
   * Get the player URL.
   * 
   * @return URL
   */
    String getUrl();

    /**
   * Get the (media) type.
   * 
   * @return type
   */
    String getType();

    /**
   * Get the bit-rate of the stream.
   * 
   * @return bit rate
   */
    String getBitRate();

    /**
   * Get the number of audio channels in the stream.
   * 
   * @return number of channels
   */
    int getChannels();

    /**
   * Get the sample-rate for the stream.
   * 
   * @return sample-rate
   */
    int getSampleRate();

    /**
   * Get the genre.
   * 
   * @return genre
   */
    String getGenre();

    /**
   * Get the name of the currently playing item.
   * 
   * @return item name
   */
    String getNowPlaying();
}
