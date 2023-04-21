package com.alexmcchesney.poster.tags;

import java.io.*;
import java.util.*;
import com.alexmcchesney.poster.*;
import com.alexmcchesney.poster.utils.*;

/**
 * Represents a simple text file that lists all the tags that the system knows about.
 */
public class TagList {

    /** List of tags classes */
    private CaseInsensitiveStringSet m_tags = new CaseInsensitiveStringSet();

    /** Recommended name for the file */
    public static final String FILENAME = "tags.txt";

    /**
	 * The file to save
	 */
    private File m_file = null;

    /**
	 * Constructor.  Takes the path to the file.  If it does not exist, it will
	 * be created.
	 * @param dir
	 */
    public TagList(File file) throws LoadConfigException {
        m_file = file;
        if (!m_file.exists()) {
            try {
                m_file.createNewFile();
                return;
            } catch (IOException e) {
                throw new LoadConfigException(m_file, e);
            }
        }
        FileReader reader = null;
        BufferedReader lineReader = null;
        String sLine;
        try {
            reader = new FileReader(m_file);
            lineReader = new BufferedReader(reader);
            sLine = lineReader.readLine();
            while (sLine != null) {
                if (sLine.length() > 0) {
                    m_tags.add(sLine);
                }
                sLine = lineReader.readLine();
            }
        } catch (IOException e) {
            throw new LoadConfigException(m_file, e);
        } finally {
            try {
                if (lineReader != null) {
                    lineReader.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                throw new LoadConfigException(m_file, e);
            }
        }
    }

    /**
	 * Saves the current tag list
	 * @throws SaveConfigException	Thrown if we cannot save it
	 */
    public synchronized void save() throws SaveConfigException {
        if (!m_file.exists()) {
            try {
                m_file.createNewFile();
            } catch (IOException e) {
                throw new SaveConfigException(m_file, e);
            }
        }
        FileWriter writer = null;
        BufferedWriter bufferedWriter = null;
        try {
            writer = new FileWriter(m_file);
            bufferedWriter = new BufferedWriter(writer);
            Iterator<String> it = m_tags.iterator();
            while (it.hasNext()) {
                bufferedWriter.write(it.next());
                bufferedWriter.newLine();
            }
        } catch (Exception e) {
            throw new SaveConfigException(m_file, e);
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new SaveConfigException(m_file, e);
            }
        }
    }

    /**
	 * Gets all the defined tags
	 * @return	Array of strings 
	 */
    public String[] getTags() {
        return m_tags.toArray();
    }

    /**
	 * Returns a copy of the string set wrapped by this tag list object
	 * @return	Cloned string set
	 */
    public CaseInsensitiveStringSet cloneTagSet() {
        return m_tags.clone();
    }

    /**
	 * Gets the "live" set of tags in the list.
	 * @return
	 */
    public CaseInsensitiveStringSet getTagSet() {
        return m_tags;
    }

    /**
	 * Adds new tags to the tag list
	 * @param sTags	The tags to add
	 */
    public synchronized void addTags(String[] sTags) {
        m_tags.addAll(sTags);
    }

    /**
	 * Adds a new tag to the tag list
	 * @param sTag Tag to add
	 */
    public synchronized void addTag(String sTag) {
        m_tags.add(sTag);
    }

    /**
	 * Removes tags to the tag list
	 * @param sTags	The tags to remove
	 */
    public synchronized void removeTags(String[] sTags) {
        m_tags.removeAll(sTags);
    }

    /**
	 * Find out if this tag is in the tag set
	 * @param sTag	Tag to find
	 * @return True if found, false if not
	 */
    public boolean hasTag(String sTag) {
        return m_tags.contains(sTag);
    }

    /**
	 * Return all tags we know of that start with the given string
	 * @param sStartsWith	String to look for
	 * @return	Array of tags that start with the string provided
	 */
    public String[] getTagsStartingWith(String sStartsWith) {
        return m_tags.getStringsStartingWith(sStartsWith);
    }
}
