package org.apache.bailey.ddb;

import java.io.IOException;
import org.apache.bailey.Database;
import org.apache.bailey.Document;
import org.apache.bailey.Query;
import org.apache.bailey.Range;
import org.apache.bailey.Results;

public class Client extends Database {

    public boolean addDoc(Document d) throws IOException {
        return false;
    }

    public Document getDoc(String id) throws IOException {
        return null;
    }

    public Document getDoc(String id, int position) throws IOException {
        return null;
    }

    public boolean removeDoc(Document d) throws IOException {
        return false;
    }

    public Results search(Query q, int maxHits) throws IOException {
        return null;
    }

    public Results search(Range range, Query q, int maxHits) throws IOException {
        return null;
    }
}
