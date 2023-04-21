package com.commonsware.android.EMusicDownloader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandlerTracks extends DefaultHandler {

    private int iCounter = 0;

    private int iTypeFlag = 0;

    public int nItems = 0;

    public int nTotalItems = 0;

    public int statuscode = 200;

    public String[] albums;

    public String[] tracks;

    public String[] artists;

    public String[] urls;

    public String[] images;

    public String[] artistURLs;

    public String[] albumIds;

    String[] artistType = { "artist", "author" };

    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (localName.equals("track")) {
            tracks[iCounter] = atts.getValue("name");
        } else if (localName.equals("album")) {
            albumIds[iCounter] = atts.getValue("id");
            albums[iCounter] = atts.getValue("name");
            urls[iCounter] = atts.getValue("url");
            images[iCounter] = atts.getValue("image");
        } else if (localName.equals("status")) {
            statuscode = Integer.parseInt(atts.getValue("code"));
        } else if (localName.equals("tracks")) {
            String nItemsString = atts.getValue("size");
            nItems = Integer.parseInt(nItemsString);
            albumIds = new String[nItems];
            albums = new String[nItems];
            artists = new String[nItems];
            urls = new String[nItems];
            tracks = new String[nItems];
            images = new String[nItems];
            artistURLs = new String[nItems];
        } else if (localName.equals("view")) {
            nTotalItems = Integer.parseInt(atts.getValue("total"));
        } else if (localName.equals(artistType[iTypeFlag])) {
            artists[iCounter] = atts.getValue("name");
            artistURLs[iCounter] = atts.getValue("url");
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (localName.equals("albums")) {
        } else if (localName.equals("album")) {
        } else if (localName.equals("track")) {
            iCounter++;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {
    }
}
