package de.larsrohleder.mp3sync;

import java.io.*;
import java.util.*;
import helliker.id3.*;
import org.apache.log4j.*;

/**
 * Collects data from ID3 tags and puts it into a HashSet
 * Double entries will not be added to the database/HashSet
 *
 * @author Lars Rohleder
 * @version 1.0
 */
public class DirectoryCompareVisitor implements DirectoryVisitor {

    private HashSet database;

    private Properties props;

    private int addedFiles = 0;

    private int notaddedFiles = 0;

    private int deletedFiles = 0;

    private boolean delete = false;

    private boolean copy = false;

    private boolean move = false;

    private File copyto = null;

    private int ldistance = 0;

    private Logger log;

    public DirectoryCompareVisitor(HashSet database, Properties props) {
        this.database = database;
        this.delete = Boolean.valueOf(props.getProperty("delete", "false")).booleanValue();
        this.copy = Boolean.valueOf(props.getProperty("copy", "false")).booleanValue();
        this.move = Boolean.valueOf(props.getProperty("move", "false")).booleanValue();
        this.copyto = new File(props.getProperty("copyto", "."));
        this.ldistance = Integer.parseInt(props.getProperty("ldistance", "0"));
        log = Logger.getLogger("DirectoryCompareVisitor");
        PropertyConfigurator.configure("log4j.properties");
    }

    public void enterDirectory(File dir) {
    }

    public void leaveDirectory(File dir) {
    }

    public void visitFile(File file) {
        String filename = file.getName();
        try {
            if (filename.substring(filename.lastIndexOf('.'), filename.length()).equals(".mp3")) {
                MP3File mp3file = new MP3File(file);
                String artist = mp3file.getArtist();
                String title = mp3file.getTitle();
                String both = artist + " - " + title;
                addEntry(both, file);
            }
        } catch (FileNotFoundException ex) {
            log.warn("Couldn't open MP3 file \"" + filename + "\"");
            addEntry(filename.substring(0, filename.length() - 4), file);
        } catch (NoMPEGFramesException ex) {
            log.info("No MPEG Frames in \"" + filename + "\"");
            addEntry(filename.substring(0, filename.length() - 4), file);
        } catch (ID3v2FormatException ex) {
            log.info("ID3v2 format is corrupt in \"" + filename + "\"");
            addEntry(filename.substring(0, filename.length() - 4), file);
        } catch (CorruptHeaderException ex) {
            log.info("ID3 header is corrupt in \"" + filename + "\"");
            addEntry(filename.substring(0, filename.length() - 4), file);
        } catch (Exception ex) {
            log.warn("An unknown error occured in \"" + filename + "\"");
            addEntry(filename.substring(0, filename.length() - 4), file);
        }
    }

    private void add(String entry) {
        ++addedFiles;
        if (log.isDebugEnabled()) {
            log.debug("Added: " + entry);
        }
    }

    private void dontadd(String entry) {
        ++notaddedFiles;
        if (log.isDebugEnabled()) {
            log.debug("Double entry: " + entry);
        }
    }

    private void copy(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            File new_file = new File(copyto.getAbsolutePath() + "/" + file.getName());
            FileOutputStream out = new FileOutputStream(new_file);
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (IOException ex) {
            log.error("IOException: " + ex.getMessage());
        }
    }

    private void move(File file) {
        copy(file);
        file.delete();
    }

    private void delete(File file) {
        if (delete) {
            ++deletedFiles;
            file.delete();
            if (log.isDebugEnabled()) {
                log.debug("Deleted: " + file.getName());
            }
        }
    }

    private void addEntry(String entry, File file) {
        if (ldistance > 0) {
            int dist;
            String temp;
            String entrylow = entry.toLowerCase();
            Iterator it = database.iterator();
            while (it.hasNext()) {
                temp = (String) it.next();
                dist = Distance.getLD(temp, entrylow);
                if (dist <= ldistance) {
                    dontadd(entry);
                    if (delete) {
                        delete(file);
                    }
                    return;
                }
            }
            database.add(entry.toLowerCase());
            if (copy) {
                copy(file);
            } else if (move) {
                move(file);
            }
            add(entry);
        } else {
            boolean insert = database.add(entry.toLowerCase());
            if (insert) {
                if (copy) {
                    copy(file);
                } else if (move) {
                    move(file);
                }
                add(entry);
            }
            if (!insert) {
                dontadd(entry);
                if (delete) {
                    delete(file);
                }
            }
        }
    }

    /**
   *
   * @return the number of files added to the database
   */
    public int getAddedFiles() {
        return addedFiles;
    }

    /**
   *
   * @return the number of files NOT added to the database
   */
    public int getNotAddedFiles() {
        return notaddedFiles;
    }

    /**
   *
   * @return the number of deleted files
   */
    public int getDeletedFiles() {
        return deletedFiles;
    }
}
