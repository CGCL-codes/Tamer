package fr.free.davidsoft.calibre.datamodel;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import fr.free.davidsoft.calibre.configuration.Configuration;
import fr.free.davidsoft.tools.Helper;

public class Book {

    private static final DateFormat TIMESTAMP_INTITLE_FORMAT = new SimpleDateFormat("dd/MM");

    private File bookFolder;

    private String id;

    private String title;

    private String path;

    private String comment;

    private Integer serieIndex;

    private Date timestamp;

    private String isbn;

    private Author author;

    private Serie serie;

    private List<Tag> tags;

    private List<EBookFile> files;

    public Book(String id, String title, String path, Integer serieIndex, Date timestamp, String isbn) {
        super();
        this.id = id;
        this.title = title;
        this.path = path;
        this.serieIndex = serieIndex;
        this.timestamp = timestamp;
        this.tags = new Vector<Tag>();
        this.files = new Vector<EBookFile>();
        this.isbn = isbn;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle(Option... options) {
        if (Option.contains(options, Option.INCLUDE_SERIE_NUMBER)) return getSerieIndex() + " - " + title; else if (Option.contains(options, Option.INCLUDE_TIMESTAMP)) {
            if (getTimestamp() != null) return TIMESTAMP_INTITLE_FORMAT.format(getTimestamp()) + " - " + title; else return title;
        } else return title;
    }

    public String getPath() {
        return path;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String value) {
        this.comment = value;
    }

    public Integer getSerieIndex() {
        return serieIndex;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Author getAuthor() {
        return author;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie value) {
        this.serie = value;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public List<EBookFile> getFiles() {
        return files;
    }

    public void setFiles(List<EBookFile> files) {
        this.files = files;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String toString() {
        return getId() + " - " + getTitle();
    }

    public String toDetailedString() {
        return getId() + " - " + getAuthor().getName() + " - " + getTitle() + " - " + Helper.concatenateList(getTags()) + " - " + getPath();
    }

    public File getBookFolder() {
        if (bookFolder == null) {
            File calibreLibraryFolder = Configuration.instance().getDatabaseFolder();
            bookFolder = new File(calibreLibraryFolder, getPath());
        }
        return bookFolder;
    }

    public String getEpubFilename() {
        EBookFile file = getEpubFile();
        if (file == null) return null;
        return file.getName() + file.getExtension();
    }

    public EBookFile getEpubFile() {
        for (EBookFile file : getFiles()) {
            if (file.getFormat() == EBookFormat.EPUB) return file;
        }
        return null;
    }

    public boolean doesEpubFileExist() {
        EBookFile file = getEpubFile();
        if (file == null) return false;
        File f = file.getFile();
        return (f != null && f.exists());
    }

    public long getLatestFileModifiedDate() {
        long result = 0;
        for (EBookFile file : getFiles()) {
            File f = file.getFile();
            if (f.exists()) {
                long m = f.lastModified();
                if (m > result) result = m;
            }
        }
        return result;
    }
}
