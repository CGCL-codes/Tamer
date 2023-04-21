package org.owasp.orizon.tools.osh.parser;

/**
 * Token literal values and constants.
 */
public interface OshConstants {

    int EOF = 0;

    int MIRAGE = 4;

    int DUSK = 5;

    int JERICHO = 6;

    int QUIT = 7;

    int HELP = 8;

    int INFO = 9;

    int NL = 10;

    int FILENAME = 11;

    int COMMAND = 9;

    int MIRAGECOMMAND = 10;

    int JERICHOCOMMAND = 11;

    int DUSKCOMMAND = 12;

    int QUITCOMMAND = 13;

    int HELPCOMMAND = 14;

    int INFOCOMMAND = 15;

    int NOCOMMAND = 16;

    /**
   * Lexical States
   */
    int DEFAULT = 0;

    String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "\"\\r\"", "\"mirage\"", "\"dusk\"", "\"jericho\"", "\"quit\"", "\"help\"", "\"info\"", "\"\\n\"", "<FILENAME>" };

    String[] nodeNames = { "EOF", "MIRAGE", "DUSK", "JERICHO", "QUIT", "HELP", "INFO", "NL", "FILENAME", "Command", "MirageCommand", "JerichoCommand", "DuskCommand", "QuitCommand", "HelpCommand", "InfoCommand", "NoCommand" };
}
