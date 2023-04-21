package com.nexirius.framework.menu;

import com.nexirius.util.TextToken;
import com.nexirius.util.XFile;
import java.io.PushbackInputStream;

public class MenuParser {

    public static final String TOOLBAR = "TOOLBAR";

    public static final String MENUBAR = "MENUBAR";

    public static final String MENU = "MENU";

    public static final String ITEM = "ITEM";

    public static final String SEPARATOR = "SEPARATOR";

    public static final String END = "END";

    protected String menuFileName;

    public MenuParser(String menuFileName) throws Exception {
        this.menuFileName = menuFileName;
    }

    public void parse(MenuCreator creator) throws Exception {
        parse(createStream(), creator);
    }

    private PushbackInputStream createStream() {
        XFile xf = new XFile(menuFileName);
        try {
            return new PushbackInputStream(xf.getBufferedInputStream());
        } catch (Exception ex) {
            throw new RuntimeException("Can't open menu file '" + menuFileName + "'");
        }
    }

    private void parse(PushbackInputStream in, MenuCreator creator) throws Exception {
        TextToken textToken = nextToken(in);
        while (textToken != null) {
            if (textToken.isIdentifier(MENUBAR)) {
                creator.newMenuBar();
                parseMenuBar(in, creator);
            } else if (textToken.isIdentifier(TOOLBAR)) {
                creator.newToolBar();
                parseToolBar(in, creator);
            } else {
                throw new Exception("Expecting MENUBAR or TOOLBAR but have: " + textToken);
            }
            textToken = nextToken(in);
        }
        in.close();
    }

    private void parseMenuBar(PushbackInputStream in, MenuCreator creator) throws Exception {
        TextToken textToken = nextToken(in);
        while (textToken != null) {
            if (textToken.isIdentifier(MENU)) {
                parseMenu(in, creator);
            } else if (textToken.isIdentifier(END)) {
                creator.end();
                return;
            } else {
                throw new Exception("Expecting MENU or END but have: " + textToken);
            }
            textToken = nextToken(in);
        }
    }

    private void parseToolBar(PushbackInputStream in, MenuCreator creator) throws Exception {
        parseItems(in, creator);
    }

    private void parseMenu(PushbackInputStream in, MenuCreator creator) throws Exception {
        TextToken textToken = nextToken(in);
        if (textToken == null || !textToken.isString()) {
            throw new Exception("Expecting string literal (menu name) but have: " + textToken);
        }
        creator.addMenu(textToken.getString());
        parseItems(in, creator);
    }

    private void parseItems(PushbackInputStream in, MenuCreator creator) throws Exception {
        TextToken textToken = nextToken(in);
        while (textToken != null) {
            if (textToken.isIdentifier(ITEM)) {
                textToken = nextToken(in);
                if (textToken == null || !textToken.isString()) {
                    throw new Exception("Expecting string literal (menu item name) but have: " + textToken);
                }
                creator.addMenuItem(textToken.getString());
            } else if (textToken.isIdentifier(END)) {
                creator.end();
                return;
            } else if (textToken.isIdentifier(SEPARATOR)) {
                creator.addSeparator();
            } else if (textToken.isIdentifier(MENU)) {
                parseMenu(in, creator);
            } else {
                throw new Exception("Expecting ITEM or SEPARATOR or END but have: " + textToken);
            }
            textToken = nextToken(in);
        }
    }

    private TextToken nextToken(PushbackInputStream in) throws Exception {
        TextToken ret = TextToken.nextToken(in);
        while (ret != null && ret.isChar('#')) {
            TextToken.skipLine(in);
            ret = TextToken.nextToken(in);
        }
        return ret;
    }
}
