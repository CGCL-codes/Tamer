package com.finalist.jag.template.parser;

public interface JagParserConstants {

    static int IDENT = 0;

    static int STRING = 1;

    static int INTEGER = 2;

    static int FLOAT = 3;

    static int TEXT = 4;

    static int HEADERDEF_BEGIN = 5;

    static int HEADERDEF_END = 6;

    static int TAGDEF_BEGIN = 7;

    static int TAGDEF_END = 8;

    static int TAGDEF_CLOSE = 9;

    static int ASSIGN = 10;

    static int SEMICOLON = 11;

    static int COLON = 12;

    static int LANGUAGE = 13;

    static int TAGSTART = 14;

    static int TAGEND = 15;

    static int TAGLIB = 16;

    static int URI = 17;

    static int PREFIX = 18;

    static int DEFINE = 19;

    static int PARAMDEF = 20;

    static int TAGNAME = 21;

    static int TAGACTION = 22;

    static int SLIST = 23;

    static int COMMENT_BEGIN = 24;

    static int COMMENT_END = 25;

    static final char EOF_CHAR = (char) -1;

    static String[] tokennames = { "IDENT", "STRING", "INTEGER", "FLOAT", "TEXT", "<#@", "#>", "<", ">", "/", "=", ";", ":", "language", "tagstart", "tagend", "taglib", "uri", "prefix", "define", "PARAMDEF", "TAGNAME", "TAGACTION", "SLIST", "!--", "--" };
}

;
