package org.porphyry.model;

import java.net.*;
import java.util.*;
import org.xml.sax.*;

public class Viewpoints extends HyperTopicResource {

    private final Vector<LabeledURL> viewpoints = new Vector<LabeledURL>();

    private final XMLHandler xmlHandler = new XMLHandler() {

        private String url;

        @Override
        public void startElement(String u, String n, String element, Attributes attr) throws SAXException {
            if (element.equals("viewpoint")) {
                this.url = attr.getValue("href");
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            try {
                if (this.url != null) {
                    Viewpoints.this.add(this.url, new String(ch, start, length));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void endElement(String u, String n, String element) {
            if (element.equals("viewpoint")) {
                this.url = null;
            }
        }
    };

    public Viewpoints(String url) throws MalformedURLException {
        super(url);
    }

    public Viewpoints(URL url) {
        super(url);
    }

    public void add(String viewpointURL, String label) throws MalformedURLException {
        this.viewpoints.add(new LabeledURL(this.getAbsoluteURL(viewpointURL), label));
    }

    @Override
    public void clear() {
        this.viewpoints.clear();
    }

    @Override
    public XMLHandler getXMLHandler() {
        return this.xmlHandler;
    }

    public Vector<LabeledURL> getViewpoints() {
        return new Vector<LabeledURL>(this.viewpoints);
    }

    @Override
    public String toXML() {
        String xml = super.toXML() + "<viewpoints>\n";
        for (int i = 0; i < this.viewpoints.size(); i++) {
            xml += "<viewpoint href=\"" + this.viewpoints.get(i).getURL() + "\">" + this.viewpoints.get(i).getLabel() + "</viewpoint>\n";
        }
        return xml + "</viewpoints>\n";
    }

    @Override
    public void httpPut() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void httpPostCreate() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public void httpPostUpdate() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void httpDelete() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public static void main(String args[]) {
        try {
            Viewpoints a = new Viewpoints("http://localhost/viewpoint/");
            a.httpGet(false);
            System.out.println(a.getViewpoints());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
