package com.microfly.compiler;

import com.microfly.core.*;
import com.microfly.util.*;
import java.io.*;
import java.util.*;

public interface ArticleTokenTypes {

    int EOF = 1;

    int NULL_TREE_LOOKAHEAD = 3;

    int HTML = 4;

    int JAVA = 5;

    int ORS = 6;

    int RS_ENDTAG = 7;

    int ATTACH_DECLARE = 8;

    int ATTACH_ENDTAG = 9;

    int MAP_DECLARE = 10;

    int MAP_ENDTAG = 11;

    int FIELD = 12;

    int RSFIELD = 13;

    int RFIELD = 14;

    int DEFAULTFIELD = 15;

    int VARS = 16;

    int VAR = 17;

    int RSNAME = 18;

    int FIELDNAME = 19;

    int RS_DECLARE = 20;

    int ID_DECLARE = 21;

    int SQL_DECLARE = 22;

    int JAVA_EXPRESSION = 23;

    int JAVAVAR = 24;

    int ROWS_DECLARE = 25;

    int FROM_DECLARE = 26;

    int PAGE_DECLARE = 27;

    int SITE_DECLARE = 28;

    int TOPIC_DECLARE = 29;

    int WHERE_DECLARE = 30;

    int ORDERBY_DECLARE = 31;

    int ATTACH_FILETYPE_DECLARE = 32;

    int JAVA_NEWLINE = 33;

    int JAVA_CODE = 34;

    int HTML_NEWLINE = 35;

    int HTML_PCDATA = 36;

    int WS = 37;

    int ID = 38;

    int STRING = 39;

    int ESCAPESEQUENCE = 40;

    int OCTALESCAPE = 41;

    int UNICODEESCAPE = 42;

    int LCLETTER = 43;

    int HEXDIGIT = 44;

    int INT = 45;

    int DIGIT = 46;

    int UNDEFINED_TOKEN = 47;
}
