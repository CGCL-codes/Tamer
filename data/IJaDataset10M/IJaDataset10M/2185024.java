package vobs.plugins.WikiParser.handlers;

import vobs.plugins.WikiParser.*;
import java.util.ArrayList;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DirHandler extends Handler {

    private boolean startPosition = false;

    public DirHandler() {
        startPosition = true;
    }

    /**
   * _notify
   *
   * @param ch char
   * @return boolean
   * @todo Implement this wikiParser.Handler method
   */
    public boolean _notify(char ch) {
        if (startPosition) {
            if (ch == ':') {
                startPosition = false;
                return true;
            } else if (ch == '\n') {
                return false;
            } else {
                startPosition = false;
                return false;
            }
        } else {
            if (ch == '\n') {
                startPosition = true;
                return false;
            } else return false;
        }
    }

    /**
   * isAware
   *
   * @param currentString String
   * @return boolean
   * @todo Implement this wikiParser.Handler method
   */
    public boolean isAware(String currentString) {
        if (currentString.startsWith(":")) return true;
        return false;
    }

    /**
   * parse
   *
   * @param parseString String
   * @param currentString StringBuffer
   * @return String
   * @todo Implement this wikiParser.Handler method
   */
    public String parse(String parseString, StringBuffer currentString) {
        String firstString = null;
        String remaindString = "";
        int index = parseString.indexOf("\n");
        if (index == -1) {
            firstString = parseString;
            remaindString = "";
        } else {
            firstString = parseString.substring(0, index);
            remaindString = parseString.substring(index + 1);
        }
        int level = findLevel(firstString);
        String clearString = clearString(firstString, level);
        put(currentString, level);
        WikiParser wp = new WikiParser();
        ArrayList handlers = new ArrayList();
        handlers.add(new ExternalLinkHandler());
        handlers.add(new ExternalLinkAdvHandler());
        handlers.add(new InternalLinkHandler());
        handlers.add(new VoLinkHandler());
        handlers.add(new VoFileHandler());
        handlers.add(new VoImageHandler());
        wp.initHandlers(handlers);
        String rez = null;
        try {
            rez = wp.parse(clearString);
        } catch (ParseException ex) {
            new Error(ex);
        }
        currentString.append(rez);
        get(currentString, level);
        return remaindString;
    }

    /**
   * get
   *
   * @param currentString StringBuffer
   * @param level int
   */
    private void get(StringBuffer currentString, int level) {
        for (int i = 0; i < level; i++) {
            currentString.append("</dir>");
        }
    }

    /**
   * put
   *
   * @param currentString StringBuffer
   * @param level int
   */
    private void put(StringBuffer currentString, int level) {
        for (int i = 0; i < level; i++) {
            currentString.append("<dir>");
        }
    }

    /**
   * clearString
   *
   * @param firstString String
   * @param level int
   * @return String
   */
    private String clearString(String firstString, int level) {
        return firstString.substring(level);
    }

    /**
   * findLevel
   *
   * @param firstString String
   * @return int
   */
    private int findLevel(String firstString) {
        if (firstString.startsWith(":")) {
            return findLevel(firstString.substring(1)) + 1;
        }
        return 0;
    }

    public void init() {
        startPosition = true;
    }
}
