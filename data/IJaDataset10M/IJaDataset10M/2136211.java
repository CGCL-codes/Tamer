package net.sourceforge.omov.core.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sourceforge.jpotpourri.util.PtCollectionUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MovieCreator {

    private static final Log LOG = LogFactory.getLog(MovieCreator.class);

    private final long id;

    private String title;

    private boolean seen;

    private int rating = -1;

    private String coverFile;

    private Set<String> genres;

    private Set<String> languages;

    private String style;

    private String director;

    private Set<String> actors;

    private int year = -1;

    private String comment;

    private Quality quality;

    private Date dateAdded;

    private long fileSizeKb = -1;

    private String folderPath;

    private String format;

    private List<String> files;

    private int duration = -1;

    private Resolution resolution;

    private Set<String> subtitles;

    MovieCreator(long id) {
        if (id < -1) throw new IllegalArgumentException("id: " + id);
        this.id = id;
    }

    public Movie get() {
        if (this.title == null) {
            LOG.warn("title was null, setting to empty string.");
            this.title = "";
        }
        if (this.coverFile == null) {
            LOG.debug("coverFile was null, setting to empty string.");
            this.coverFile = "";
        }
        if (this.rating == -1) {
            LOG.debug("rating was default value -1, setting to 0.");
            this.rating = 0;
        }
        if (this.year == -1) {
            LOG.debug("year was default value -1, setting to 0.");
            this.year = 0;
        }
        if (this.style == null) {
            LOG.debug("style was null, setting to empty string.");
            this.style = "";
        }
        if (this.genres == null) {
            LOG.debug("genres was null, setting to empty set.");
            this.genres = new HashSet<String>();
        }
        if (this.languages == null) {
            LOG.debug("languages was null, setting to empty set.");
            this.languages = new HashSet<String>();
        }
        if (this.quality == null) {
            LOG.debug("quality was null, setting to unrated.");
            this.quality = Quality.UNRATED;
        }
        if (this.director == null) {
            LOG.debug("director was null, setting to empty string.");
            this.director = "";
        }
        if (this.actors == null) {
            LOG.debug("actors was null, setting to empty set.");
            this.actors = new HashSet<String>();
        }
        if (this.comment == null) {
            LOG.debug("comment was null, setting to empty string.");
            this.comment = "";
        }
        if (this.dateAdded == null) {
            LOG.debug("dateAdded was null, setting to 0000-00-00 00:00:00!");
            this.dateAdded = new Date(0);
        }
        if (this.folderPath == null) {
            LOG.debug("folderPath was null, setting to empty string.");
            this.folderPath = "";
        }
        if (this.fileSizeKb == -1) {
            LOG.debug("fileSizeKb was default value -1, setting to 0.");
            this.fileSizeKb = 0;
        }
        if (this.format == null) {
            LOG.debug("format was null, setting to empty string.");
            this.format = "";
        }
        if (this.files == null) {
            LOG.debug("files were null, setting to empty set.");
            this.files = new ArrayList<String>();
        }
        if (this.duration == -1) {
            LOG.debug("duration was default value -1, setting to 0.");
            this.duration = 0;
        }
        if (this.subtitles == null) {
            LOG.debug("subtitles were null, setting to empty set.");
            this.subtitles = new HashSet<String>();
        }
        if (this.resolution == null) {
            LOG.debug("resolution were null, setting to 0x0 resolution.");
            this.resolution = Resolution.R0x0;
        }
        return new Movie(id, title, seen, rating, coverFile, genres, languages, style, director, actors, year, comment, quality, dateAdded, fileSizeKb, folderPath, format, files, duration, resolution, subtitles);
    }

    public MovieCreator title(String input) {
        if (input == null) throw new NullPointerException("title");
        if (this.title != null) throw new IllegalStateException("title already set to '" + this.title + "'.");
        this.title = input;
        return this;
    }

    public MovieCreator seen(boolean input) {
        this.seen = input;
        return this;
    }

    public MovieCreator rating(int input) {
        if (input < 0 || input > 5) throw new IllegalArgumentException("rating: " + input);
        if (this.rating != -1) throw new IllegalStateException("rating already set to '" + this.rating + "'.");
        this.rating = input;
        return this;
    }

    public MovieCreator coverFile(String input) {
        LOG.debug("setting coverFile to '" + input + "'");
        if (input == null) throw new NullPointerException("coverFile");
        if (this.coverFile != null) throw new IllegalStateException("coverFile already set to '" + this.coverFile + "'.");
        this.coverFile = input;
        return this;
    }

    public MovieCreator genres(String... input) {
        if (input == null) throw new NullPointerException("genres");
        if (this.genres != null) throw new IllegalStateException("genres already set to '" + this.genres + "'.");
        this.genres = PtCollectionUtil.asStringSet(input);
        return this;
    }

    public MovieCreator genres(Set<String> input) {
        if (input == null) throw new NullPointerException("genres");
        if (this.genres != null) throw new IllegalStateException("genres already set to '" + this.genres + "'.");
        this.genres = input;
        return this;
    }

    public MovieCreator languages(String... input) {
        if (input == null) throw new NullPointerException("languages");
        if (this.languages != null) throw new IllegalStateException("languages already set to '" + this.languages + "'.");
        this.languages = PtCollectionUtil.asStringSet(input);
        return this;
    }

    public MovieCreator languages(Set<String> input) {
        if (input == null) throw new NullPointerException("languages");
        if (this.languages != null) throw new IllegalStateException("languages already set to '" + this.languages + "'.");
        this.languages = input;
        return this;
    }

    public MovieCreator style(String input) {
        if (input == null) throw new NullPointerException("style");
        if (this.style != null) throw new IllegalStateException("style already set to '" + this.style + "'.");
        this.style = input;
        return this;
    }

    public MovieCreator director(String input) {
        if (input == null) throw new NullPointerException("director");
        if (this.director != null) throw new IllegalStateException("director already set to '" + this.director + "'.");
        this.director = input;
        return this;
    }

    public MovieCreator actors(Set<String> input) {
        if (input == null) throw new NullPointerException("actors");
        if (this.actors != null) throw new IllegalStateException("actors already set to '" + this.actors + "'.");
        this.actors = input;
        return this;
    }

    public MovieCreator actors(String... input) {
        if (input == null) throw new NullPointerException("actors");
        if (this.actors != null) throw new IllegalStateException("actors already set to '" + this.actors + "'.");
        this.actors = PtCollectionUtil.asStringSet(input);
        return this;
    }

    public MovieCreator year(int input) {
        if (input < 0 || input >= 3000) throw new IllegalArgumentException("year: " + input);
        if (this.year != -1) throw new IllegalStateException("year already set to '" + this.year + "'.");
        this.year = input;
        return this;
    }

    public MovieCreator comment(String input) {
        if (input == null) throw new NullPointerException("comment");
        if (this.comment != null) throw new IllegalStateException("comment already set to '" + this.comment + "'.");
        this.comment = input;
        return this;
    }

    public MovieCreator quality(Quality input) {
        if (input == null) throw new NullPointerException("quality");
        if (this.quality != null) throw new IllegalStateException("quality already set to '" + this.quality + "'.");
        this.quality = input;
        return this;
    }

    public MovieCreator dateAdded(Date input) {
        if (input == null) throw new NullPointerException("dateAdded");
        if (this.dateAdded != null) throw new IllegalStateException("dateAdded already set to '" + this.dateAdded + "'.");
        this.dateAdded = input;
        return this;
    }

    public MovieCreator fileSizeKb(long input) {
        if (input < 0) throw new IllegalArgumentException("fileSizeKb: " + input);
        if (this.fileSizeKb != -1) throw new IllegalStateException("fileSizeKb already set to '" + this.fileSizeKb + "'.");
        this.fileSizeKb = input;
        return this;
    }

    public MovieCreator folderPath(String input) {
        if (input == null) throw new NullPointerException("folderPath");
        if (this.folderPath != null) throw new IllegalStateException("folderPath already set to '" + this.folderPath + "'.");
        this.folderPath = input;
        return this;
    }

    public MovieCreator format(String input) {
        if (input == null) throw new NullPointerException("format");
        if (this.format != null) throw new IllegalStateException("format already set to '" + this.format + "'.");
        this.format = input;
        return this;
    }

    public MovieCreator files(String... input) {
        if (input == null) throw new NullPointerException("files");
        if (this.files != null) throw new IllegalStateException("files already set to '" + this.files + "'.");
        this.files = PtCollectionUtil.immutableList(input);
        return this;
    }

    public MovieCreator files(List<String> input) {
        if (input == null) throw new NullPointerException("files");
        if (this.files != null) throw new IllegalStateException("files already set to '" + this.files + "'.");
        this.files = input;
        return this;
    }

    public MovieCreator duration(int input) {
        if (input < 0) throw new IllegalArgumentException("duration: " + input);
        if (this.duration != -1) throw new IllegalStateException("duration already set to '" + this.duration + "'.");
        this.duration = input;
        return this;
    }

    public MovieCreator resolution(Resolution input) {
        if (input == null) throw new NullPointerException("resolution");
        if (this.resolution != null) throw new IllegalStateException("resolution already set to '" + this.resolution + "'.");
        this.resolution = input;
        return this;
    }

    public MovieCreator subtitles(String... input) {
        if (input == null) throw new NullPointerException("subtitles");
        if (this.subtitles != null) throw new IllegalStateException("subtitles already set to '" + this.subtitles + "'.");
        this.subtitles = PtCollectionUtil.asStringSet(input);
        return this;
    }

    public MovieCreator subtitles(Set<String> input) {
        if (input == null) throw new NullPointerException("subtitles");
        if (this.subtitles != null) throw new IllegalStateException("subtitles already set to '" + this.subtitles + "'.");
        this.subtitles = input;
        return this;
    }
}
