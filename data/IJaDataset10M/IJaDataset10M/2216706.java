package org.pointrel.pointrel20090201.test;

import java.util.Iterator;
import org.pointrel.pointrel20090201.core.SharedArchiveCoordinator;
import org.pointrel.pointrel20090201.core.Query;
import org.pointrel.pointrel20090201.core.ResultSet;
import org.pointrel.pointrel20090201.core.Session;
import org.pointrel.pointrel20090201.core.Triple;

public class TestSharedArchives {

    public static void main(String[] args) {
        System.out.println("SharedArchiveCoordinator test");
        SharedArchiveCoordinator sharedArchiveCoordinator = SharedArchiveCoordinator.getInstance();
        String archiveReference = sharedArchiveCoordinator.loadArchiveFromPath("TestArchives/TestArchiveSimple/");
        Session session = new Session(null, null);
        session.archiveSearchPath.add(archiveReference);
        Query query = session.newQuery();
        query.filter.valueData = "A green spiral notebook";
        System.out.println("Starting search");
        ResultSet results = query.getMatchingTriples();
        System.out.println("Done searching");
        System.out.println(results);
        Iterator<Triple> iterator = results.iterator();
        while (iterator.hasNext()) {
            Triple triple = iterator.next();
            triple.print();
        }
    }
}
