package org.fredy.id3tidy.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.fredy.id3tidy.ID3Tag;
import org.fredy.id3tidy.ID3TidyIOException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

/**
 * A utility class to read/write ID3 tag.
 * @author fredy
 */
public class ID3TagIO {

    private static final Logger logger = Logger.getLogger(ID3TagIO.class.getName());

    private ID3TagIO() {
    }

    /**
     * Reads ID3 tag.
     * @return ID3 tag
     * @exception ID3TidyIOException
     */
    public static ID3Tag read(File file) {
        logger.info("Reading ID3 tag from " + file.getPath());
        ID3Tag tag = new ID3Tag();
        AudioFile f;
        try {
            f = AudioFileIO.read(file);
            Tag t = f.getTagOrCreateAndSetDefault();
            tag.setArtist(t.getFirst(FieldKey.ARTIST));
            tag.setAlbum(t.getFirst(FieldKey.ALBUM));
            tag.setTitle(t.getFirst(FieldKey.TITLE));
            tag.setGenre(t.getFirst(FieldKey.GENRE));
            tag.setTrack(t.getFirst(FieldKey.TRACK));
            tag.setYear(t.getFirst(FieldKey.YEAR));
            tag.setComment(t.getFirst(FieldKey.COMMENT));
            f.commit();
        } catch (CannotReadException e) {
            throw new ID3TidyIOException(e);
        } catch (IOException e) {
            throw new ID3TidyIOException(e);
        } catch (TagException e) {
            throw new ID3TidyIOException(e);
        } catch (ReadOnlyFileException e) {
            throw new ID3TidyIOException(e);
        } catch (InvalidAudioFrameException e) {
            throw new ID3TidyIOException(e);
        } catch (CannotWriteException e) {
            throw new ID3TidyIOException(e);
        }
        logger.info("ID3 tag=" + tag);
        return tag;
    }

    /**
     * Reads ID3 tags from the list of mp3 files.
     * @param files the mp3 files
     * @return list of ID3 tags
     */
    public static List<ID3Tag> read(List<File> files) {
        List<ID3Tag> id3Tags = new ArrayList<ID3Tag>();
        for (File file : files) {
            try {
                id3Tags.add(read(file));
            } catch (ID3TidyIOException e) {
                logger.severe(e.getMessage());
            }
        }
        return id3Tags;
    }

    /**
     * Writes ID3 tag.
     * @param tag
     * @param overwrite
     * @exception ID3TidyIOException
     */
    public static void write(File file, ID3Tag tag, boolean overwrite) {
        logger.info("Writing ID3 tag into " + file.getPath());
        logger.info("ID3 tag=" + tag);
        AudioFile f;
        try {
            f = AudioFileIO.read(file);
            Tag t = f.getTag();
            if (overwrite) {
                writeOverwrite(tag, t);
            } else {
                writeNoOverwrite(tag, t);
            }
            f.commit();
        } catch (CannotReadException e) {
            throw new ID3TidyIOException(e);
        } catch (IOException e) {
            throw new ID3TidyIOException(e);
        } catch (TagException e) {
            throw new ID3TidyIOException(e);
        } catch (ReadOnlyFileException e) {
            throw new ID3TidyIOException(e);
        } catch (InvalidAudioFrameException e) {
            throw new ID3TidyIOException(e);
        } catch (CannotWriteException e) {
            throw new ID3TidyIOException(e);
        }
    }

    /**
     * Writes the list of ID3 tags into the list of mp3 files.
     * @param files the mp3 files
     * @param id3Tags the list of ID3 tags
     * @param overwrite
     */
    public static void write(List<File> files, List<ID3Tag> id3Tags, boolean overwrite) {
        if (files.size() != id3Tags.size()) {
            throw new IllegalArgumentException("Number of files must be the same with the number " + "of ID3 tags");
        }
        for (int i = 0; i < files.size(); i++) {
            try {
                write(files.get(i), id3Tags.get(i), overwrite);
            } catch (ID3TidyIOException e) {
                logger.severe(e.getMessage());
            }
        }
    }

    private static void writeNoOverwrite(ID3Tag tag, Tag t) throws FieldDataInvalidException {
        if (StringUtils.isEmpty(t.getFirst(FieldKey.ARTIST))) {
            if (tag.getArtist() != null) {
                t.setField(FieldKey.ARTIST, tag.getArtist());
            }
        }
        if (StringUtils.isEmpty(t.getFirst(FieldKey.ALBUM))) {
            if (tag.getAlbum() != null) {
                t.setField(FieldKey.ALBUM, tag.getAlbum());
            }
        }
        if (StringUtils.isEmpty(t.getFirst(FieldKey.TITLE))) {
            if (tag.getTitle() != null) {
                t.setField(FieldKey.TITLE, tag.getTitle());
            }
        }
        if (StringUtils.isEmpty(t.getFirst(FieldKey.GENRE))) {
            if (tag.getGenre() != null) {
                t.setField(FieldKey.GENRE, tag.getGenre());
            }
        }
        if (StringUtils.isEmpty(t.getFirst(FieldKey.COMMENT))) {
            if (tag.getComment() != null) {
                t.setField(FieldKey.COMMENT, tag.getComment());
            }
        }
        if (StringUtils.isEmpty(t.getFirst(FieldKey.TRACK))) {
            if (tag.getTrack() != null) {
                if (StringUtils.isNumeric(tag.getTrack())) {
                    t.setField(FieldKey.TRACK, tag.getTrack());
                }
            }
        }
        if (StringUtils.isEmpty(t.getFirst(FieldKey.YEAR))) {
            if (tag.getYear() != null) {
                if (StringUtils.isNumeric(tag.getYear())) {
                    t.setField(FieldKey.YEAR, tag.getYear());
                }
            }
        }
    }

    private static void writeOverwrite(ID3Tag tag, Tag t) throws FieldDataInvalidException {
        if (tag.getArtist() != null) {
            t.setField(FieldKey.ARTIST, tag.getArtist());
        }
        if (tag.getAlbum() != null) {
            t.setField(FieldKey.ALBUM, tag.getAlbum());
        }
        if (tag.getTitle() != null) {
            t.setField(FieldKey.TITLE, tag.getTitle());
        }
        if (tag.getGenre() != null) {
            t.setField(FieldKey.GENRE, tag.getGenre());
        }
        if (tag.getComment() != null) {
            t.setField(FieldKey.COMMENT, tag.getComment());
        }
        if (tag.getTrack() != null) {
            if (!StringUtils.isBlank(tag.getTrack()) && StringUtils.isNumeric(tag.getTrack())) {
                t.setField(FieldKey.TRACK, tag.getTrack());
            }
        }
        if (tag.getYear() != null) {
            if (!StringUtils.isBlank(tag.getYear()) && StringUtils.isNumeric(tag.getYear())) {
                t.setField(FieldKey.YEAR, tag.getYear());
            }
        }
    }
}
