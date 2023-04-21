package org.saosis.core.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import org.saosis.core.models.Entry;
import org.saosis.core.models.FeatureQualifier;

/**
 * An abstract entry reader.
 * 
 * @author Daniel Allen Prust (danprust@yahoo.com)
 * 
 */
public abstract class EntryReader extends FeedbackController {

    protected void postProcess(ArrayList<Entry> entries) {
        for (Entry newEntry : entries) postProcess(newEntry);
    }

    protected void postProcess(Entry entry) {
    }

    /**
	 * Reads a given file into a list of entries.
	 * 
	 * @param file
	 *            A file
	 * @return A list of entries
	 * @throws IOException
	 */
    public ArrayList<Entry> read(File file) throws IOException {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            return read(stream);
        } finally {
            StreamUtility.close(stream);
        }
    }

    /**
	 * Reads an input stream into a list of entries.
	 * 
	 * @param stream
	 *            An input stream
	 * @return A list of entries
	 * @throws IOException
	 */
    public abstract ArrayList<Entry> read(InputStream stream) throws IOException;

    /**
	 * Reads entries from a given URL.
	 * 
	 * @param url
	 *            A URL
	 * @return A list of entries
	 * @throws IOException
	 */
    public ArrayList<Entry> read(URL url) throws IOException {
        InputStream stream = null;
        try {
            stream = url.openStream();
            return read(stream);
        } finally {
            StreamUtility.close(stream);
        }
    }

    protected String repairFeatureName(Entry entry, String name) {
        return name;
    }

    protected void repairFeatureQualifiers(Entry entry, ArrayList<FeatureQualifier> qualifiers) {
    }
}
