package com.silvasoftinc.s3;

import java.text.SimpleDateFormat;
import java.util.Date;

public class S3AtomHelper {

    private StringBuilder atomDoc;

    public S3AtomHelper() {
        atomDoc = new StringBuilder();
    }

    public void createAtomDocument() {
        atomDoc.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        atomDoc.append("\n");
    }

    public void openAtomDocument() {
        atomDoc.append("<feed xmlns=\"http://www.w3.org/2005/Atom\">");
        atomDoc.append("\n");
    }

    public void closeAtomDocument() {
        atomDoc.append("</feed>");
    }

    public void addAtomFeedInfo(String title, String subtitle, String link, Date updated, String author, String email, String id) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        atomDoc.append("<title>" + title + "</title>");
        atomDoc.append("\n");
        atomDoc.append("<subtitle>" + subtitle + "</subtitle>");
        atomDoc.append("\n");
        atomDoc.append("<updated>" + dateFormat.format(updated) + "T" + timeFormat.format(updated) + "Z" + "</updated>");
        atomDoc.append("\n");
        atomDoc.append("<author>");
        atomDoc.append("\n");
        atomDoc.append("<name>" + author + "</name>");
        atomDoc.append("\n");
        atomDoc.append("<email>" + email + "</email>");
        atomDoc.append("\n");
        atomDoc.append("</author>");
        atomDoc.append("\n");
        atomDoc.append("<id>" + id + "</id>");
        atomDoc.append("\n");
    }

    public void openAtomEntry() {
        atomDoc.append("<entry>");
        atomDoc.append("\n");
    }

    public void closeAtomEntry() {
        atomDoc.append("</entry>");
        atomDoc.append("\n");
    }

    public void addAtomEntryInfo(String title, String link, String id, Date updated, String summary) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        atomDoc.append("<title>" + title + "</title>");
        atomDoc.append("\n");
        atomDoc.append("<link href=\"" + link + "\" />");
        atomDoc.append("\n");
        atomDoc.append("<id>" + id + "</id>");
        atomDoc.append("\n");
        atomDoc.append("<updated>" + dateFormat.format(updated) + "T" + timeFormat.format(updated) + "Z" + "</updated>");
        atomDoc.append("\n");
        atomDoc.append("<summary>" + summary + "</summary>");
        atomDoc.append("\n");
    }

    public String getAtomDocument() {
        return this.atomDoc.toString();
    }
}
