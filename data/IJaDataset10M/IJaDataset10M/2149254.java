package org.storrow.jdbc4d.parser;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class Parser {

    public static void main(String args[]) throws ParseException {
        Parser zqlparser = null;
        if (args.length < 1) {
            System.out.println("/* Reading from stdin (exit; to finish) */");
            zqlparser = new Parser(System.in);
        } else {
            try {
                zqlparser = new Parser(new DataInputStream(new FileInputStream(args[0])));
            } catch (FileNotFoundException _ex) {
                System.out.println("/* File " + args[0] + " not found. Reading from stdin */");
                zqlparser = new Parser(System.in);
            }
        }
        if (args.length > 0) System.out.println("/* Reading from " + args[0] + "*/");
        for (ParserStatement zstatement = null; (zstatement = zqlparser.readStatement()) != null; ) System.out.println(zstatement.toString() + ";");
        System.out.println("exit;");
        System.out.println("/* Parse Successful */");
    }

    public Parser(InputStream inputstream) {
        initParser(inputstream);
    }

    public Parser() {
    }

    public void initParser(InputStream inputstream) {
        if (_parser == null) {
            _parser = new SQLParser(inputstream);
            return;
        } else {
            _parser.ReInit(inputstream);
            return;
        }
    }

    public ParserStatement readStatement() throws ParseException {
        if (_parser == null) throw new ParseException("Parser not initialized: use initParser(InputStream);"); else return _parser.SQLStatement();
    }

    public List readStatements() throws ParseException {
        if (_parser == null) throw new ParseException("Parser not initialized: use initParser(InputStream);"); else return _parser.SQLStatements();
    }

    public Exp readExpression() throws ParseException {
        if (_parser == null) throw new ParseException("Parser not initialized: use initParser(InputStream);"); else return _parser.SQLExpression();
    }

    SQLParser _parser;
}
