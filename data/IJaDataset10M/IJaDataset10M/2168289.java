package com.objectwave.sourceParser;

import com.objectwave.sourceModel.CommentElement;
import java.io.*;

/**
 * Assumes that there is no leading whitespace to the string passed in.
 * 
 * @author Dave Hoag
 * @version 1.3
 * @fixme Not yet a ClassParser object.
 */
public class CommentParser {

    CommentElement theObject;

    CommentParser theRestParser;

    /**
 * The main entry point. 
 * @fixme - DAH - Multiple comments may exist for a single method. What do we do?
 * @author Dave Hoag
 * @param comment java.lang.String There is NO leading whitespace on that provided comment.
 */
    public void finishParsing(String comment) {
        theObject = new CommentElement();
        if (comment.length() == 0) return;
        int bodyIdx = 0;
        if (comment.startsWith("/**")) {
            theObject.setJavaDoc(true);
            bodyIdx = 3;
        } else {
            if (comment.startsWith("/*")) theObject.setCStyle(true); else {
                theObject.setCplusplusStyle(true);
                int idx = comment.indexOf("\n");
                if (idx < 0) idx = comment.length();
                if (idx > 0) theObject.setBody(comment.substring(2, idx));
                String theRest = null;
                if (idx > 0 && (idx + 1) <= comment.length()) {
                    theRest = comment.substring(idx, comment.length());
                    theObject.setTheRest(theRest);
                }
                if (theRest != null) {
                    theRestParser = new CommentParser();
                    theRestParser.finishParsing(theRest.trim());
                    theObject.setNext((CommentElement) theRestParser.getDataObject());
                }
                return;
            }
            bodyIdx = 2;
        }
        if (comment.length() <= bodyIdx + 2) {
            theObject.setBody("");
            return;
        }
        int idx = comment.indexOf("*/");
        String theRest = null;
        if (comment.length() > idx + 2) {
            theRest = comment.substring(idx + 2, comment.length());
            theObject.setTheRest(theRest);
        }
        if (theRest != null) {
            theRestParser = new CommentParser();
            theRestParser.finishParsing(theRest.trim());
            theObject.setNext((CommentElement) theRestParser.getDataObject());
        }
        StringReader rdr = new StringReader(comment.substring(bodyIdx, idx));
        parseBody(rdr);
    }

    /**
 * getDataObject method comment.
 */
    public com.objectwave.sourceModel.ClassElement getDataObject() {
        return theObject;
    }

    /**
 * Starts the application.
 * @param args an array of command-line arguments
 */
    public static void main(java.lang.String[] args) {
        String test2 = "/**\nThis is a test\n * @fixme\n @author Dave Hoag\n * @author Steve */\n//Havoc I say!\n/** More crap\n*/";
        String test = "// break me\n\n/**\n *  Multiple lines\n *  of Text for this comment.\n *  Hope it works!\n *  \n *  Another line\n * @version asdfee\n * @author \n */ // more on same line";
        String test3 = "// first line\n//*  Multiple lines\n";
        String[] tests = new String[3];
        tests[0] = test;
        tests[1] = test2;
        tests[2] = test3;
        int idx = 0;
        try {
            idx = Integer.parseInt(args[0]);
        } catch (Exception e) {
        }
        CommentParser p = new CommentParser();
        p.finishParsing(tests[idx]);
        ((CommentElement) p.getDataObject()).setFullText(tests[idx]);
        p.getDataObject().dumpObject();
        ((CommentElement) p.getDataObject()).addDocParam("see", "me");
        System.out.println("addDocParam ");
        System.out.println(p.getDataObject().getFullText());
        p = new CommentParser();
        p.finishParsing(tests[idx]);
        ((CommentElement) p.getDataObject()).setFullText(tests[idx]);
        java.util.Vector v = new java.util.Vector();
        v.addElement("me");
        v.addElement("you");
        ((CommentElement) p.getDataObject()).setDocParam("see", v);
        System.out.println("setDocParam ");
        System.out.println(p.getDataObject().getFullText());
    }

    /**
 * Determine the contents of the comment.
 * @author Dave Hoag
 */
    void parseBody(StringReader rdr) {
        boolean buildingBody = true, buildingKey = false;
        boolean newLine = false, previousIsStar = false;
        StringBuffer body = new StringBuffer("");
        StringBuffer key = null;
        StringBuffer val = null;
        try {
            for (int i = rdr.read(); i > -1; i = rdr.read()) {
                char c = (char) i;
                if (c == '*') previousIsStar = true; else {
                    if (previousIsStar && c == '/') break;
                    previousIsStar = false;
                }
                if (c == '\n') newLine = true;
                if (!Character.isWhitespace(c) && newLine) {
                    if (c == '*') {
                        continue;
                    } else newLine = false;
                }
                if (c == '@') {
                    if (buildingBody) theObject.setBody(body.toString());
                    buildingBody = false;
                    buildingKey = true;
                    if (key != null) {
                        theObject.addDocParam(key.toString().trim(), val.toString().trim());
                    }
                    key = new StringBuffer("");
                    val = new StringBuffer("");
                } else if (Character.isWhitespace(c) && buildingKey) buildingKey = false; else if (buildingBody) {
                    body.append(c);
                } else if (buildingKey) {
                    key.append(c);
                } else val.append(c);
            }
            if (key != null) {
                theObject.addDocParam(key.toString().trim(), val.toString().trim());
            }
            if (buildingBody) theObject.setBody(body.toString());
        } catch (IOException ex) {
        }
    }
}
