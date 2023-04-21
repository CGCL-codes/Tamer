package org.pointrel.pointrel20100813.configuration;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import org.pointrel.pointrel20100813.core.ArchiveAccessSpecification;
import org.pointrel.pointrel20100813.core.Session;
import org.pointrel.pointrel20100813.core.Standards;
import org.pointrel.pointrel20100813.core.Triple;
import org.pointrel.pointrel20100813.core.TypeAndDataPair;

public class AuthorUtilities {

    public static List<TypeAndDataPair> newAuthorList(String authorReferenceType, String authorReference) {
        List<TypeAndDataPair> authorList = new ArrayList<TypeAndDataPair>();
        if (authorReference != null) authorList.add(new TypeAndDataPair(authorReferenceType, authorReference));
        return authorList;
    }

    public static ArrayList<TypeAndDataPair> newDefaultAuthorList() {
        System.out.println("Getting author list");
        Session session = new Session(null, null);
        ArchiveAccessSpecification archiveAccessSpecification = new ArchiveAccessSpecification("default preferences", "prefs:///org/pointrel/pointrel20100813/defaults");
        session.loadArchive(archiveAccessSpecification, true);
        Triple authorTriple = session.mostRecentMatchingTriple("org.pointrel.pointrel20100813.configuration.AuthorUtilities", "default", "PointrelMetadata", "authors", "PointrelMetadata", "defaultAuthor", null, null);
        TypeAndDataPair authorPair;
        if (authorTriple == null) {
            String authorReferenceType = "PointrelAuthorUUID";
            String authorReference = Standards.newUUID();
            authorPair = new TypeAndDataPair(authorReferenceType, authorReference);
            System.out.println("Made unique author reference: " + authorPair);
            session.addTripleForFields("org.pointrel.pointrel20100813.configuration.AuthorUtilities", "default", "PointrelMetadata", "authors", "PointrelMetadata", "defaultAuthor", authorReferenceType, authorReference);
        } else {
            authorPair = authorTriple.getValuePair();
        }
        ArrayList<TypeAndDataPair> authorList = new ArrayList<TypeAndDataPair>();
        authorList.add(authorPair);
        return authorList;
    }

    public static boolean editAuthorsList(Component parent, ArrayList<TypeAndDataPair> authors) {
        TypeAndDataListEditor listEditor = TypeAndDataListEditor.newDialog(parent, "Authors", authors);
        listEditor.setVisible(true);
        if (listEditor.okResult) {
            authors.clear();
            authors.addAll(listEditor.getList());
            return true;
        }
        return false;
    }

    public static boolean editAuthorsList(Component parent, Session session) {
        TypeAndDataListEditor listEditor = TypeAndDataListEditor.newDialog(parent, "Authors", session.currentAuthors);
        listEditor.setVisible(true);
        if (listEditor.okResult) {
            session.currentAuthors = listEditor.getList();
            return true;
        }
        return false;
    }
}
