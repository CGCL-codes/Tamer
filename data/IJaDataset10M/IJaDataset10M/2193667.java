package fr.insee.rome.io.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TxtDomainesReader implements DomainesReader {

    private static DomainesReader instance = null;

    private List<String> domaines = null;

    private TxtDomainesReader() {
    }

    public static DomainesReader getInstance() {
        if (instance == null) {
            instance = new TxtDomainesReader();
        }
        return instance;
    }

    public void close() {
        domaines.clear();
    }

    public void open(String path) {
        this.domaines = new ArrayList<String>(70);
        try {
            InputStream stream = new FileInputStream(path);
            Reader reader = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(reader);
            String line = null;
            while ((line = buffer.readLine()) != null) {
                domaines.add(line);
            }
            buffer.close();
            reader.close();
            stream.close();
            buffer = null;
            reader = null;
            stream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasNext() {
        return this.iterator().hasNext();
    }

    public String next() {
        return this.iterator().next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public Iterator<String> iterator() {
        return domaines.iterator();
    }
}
