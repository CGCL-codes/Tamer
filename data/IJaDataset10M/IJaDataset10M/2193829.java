package com.tsac.familytree.gedcom;

import com.tsac.familytree.gui.*;
import com.tsac.familytree.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

public class GedcomLoader implements FamilyTreeLoader {

    public static final int NONE = 0x00;

    public static final int HEAD = 0x01;

    public static final int HEAD_SOUR = 0x11;

    public static final int HEAD_DEST = 0x21;

    public static final int HEAD_DATE = 0x31;

    public static final int HEAD_FILE = 0x41;

    public static final int HEAD_GEDC = 0x51;

    public static final int HEAD_CHAR = 0x61;

    public static final int HEAD_SUBM = 0x71;

    public static final int HEAD_SUBN = 0x81;

    public static final int SUBM = 0x02;

    public static final int SOUR = 0x03;

    public static final int REPO = 0x04;

    public static final int SUBN = 0x05;

    public static final int INDI = 0x06;

    public static final int INDI_NAME = 0x16;

    public static final int INDI_NAME_GIVN = 0x116;

    public static final int INDI_NAME_SURN = 0x216;

    public static final int INDI_AFN = 0x26;

    public static final int INDI_SEX = 0x36;

    public static final int INDI_SOUR = 0x46;

    public static final int INDI_BIRT = 0x56;

    public static final int INDI_BIRT_DATE = 0x156;

    public static final int INDI_BIRT_PLAC = 0x256;

    public static final int INDI_BIRT_SOUR = 0x356;

    public static final int INDI_CHR = 0x66;

    public static final int INDI_CHR_DATE = 0x166;

    public static final int INDI_CHR_PLAC = 0x266;

    public static final int INDI_CHR_SOUR = 0x366;

    public static final int INDI_BURI = 0x76;

    public static final int INDI_BURI_DATE = 0x176;

    public static final int INDI_BURI_PLAC = 0x276;

    public static final int INDI_BURI_SOUR = 0x376;

    public static final int INDI_DEAT = 0x86;

    public static final int INDI_DEAT_DATE = 0x186;

    public static final int INDI_DEAT_PLAC = 0x286;

    public static final int INDI_DEAT_SOUR = 0x386;

    public static final int INDI_DEAT_CAUS = 0x486;

    public static final int INDI_IES_DATE = 0x1f6;

    public static final int INDI_IES_PLAC = 0x2f6;

    public static final int INDI_IES_SOUR = 0x3f6;

    public static final int INDI_SOUR2 = 0x96;

    public static final int INDI_FAMS = 0xa6;

    public static final int INDI_FAMC = 0xb6;

    public static final int INDI_OCCU = 0xc6;

    public static final int INDI_NOTE = 0xd6;

    public static final int FAM = 0x07;

    public static final int FAM_HUSB = 0x17;

    public static final int FAM_WIFE = 0x27;

    public static final int FAM_CHIL = 0x37;

    public static final int FAM_MARR = 0x47;

    public static final int FAM_MARR_DATE = 0x147;

    public static final int FAM_MARR_PLAC = 0x247;

    public static final int FAM_MARR_SOUR = 0x347;

    public static final int FAM_DIV = 0x57;

    public static final int FAM_NOTE = 0x67;

    public static final int TRLR = 0x08;

    private static Hashtable _cmds[][] = new Hashtable[0x9][5];

    private static Hashtable _cmds0 = new Hashtable();

    private Vector _people = new Vector();

    private Vector _marriages = new Vector();

    private int _mode = NONE;

    private String _param;

    private Person _person = null;

    private String _surname;

    private String _forenames;

    private String _key;

    private String _husbkey = null;

    private String _wifekey = null;

    private Vector _children = new Vector();

    private String _marrdate = null;

    private String _marrplace = null;

    private boolean _loadedOk;

    public GedcomLoader() {
        _cmds0.put("HEAD", new Integer(HEAD));
        _cmds0.put("SUBM", new Integer(SUBM));
        _cmds0.put("SOUR", new Integer(SOUR));
        _cmds0.put("REPO", new Integer(REPO));
        _cmds0.put("SUBN", new Integer(SUBN));
        _cmds0.put("INDI", new Integer(INDI));
        _cmds0.put("FAM", new Integer(FAM));
        _cmds0.put("TRLR", new Integer(TRLR));
        for (int n = 0; n < _cmds.length; n++) {
            for (int m = 0; m < _cmds[n].length; m++) _cmds[n][m] = new Hashtable();
        }
        _cmds[HEAD][1].put("SOUR", new Integer(HEAD_SOUR));
        _cmds[HEAD][1].put("DEST", new Integer(HEAD_DEST));
        _cmds[HEAD][1].put("DATE", new Integer(HEAD_DATE));
        _cmds[HEAD][1].put("FILE", new Integer(HEAD_FILE));
        _cmds[HEAD][1].put("GEDC", new Integer(HEAD_GEDC));
        _cmds[HEAD][1].put("CHAR", new Integer(HEAD_CHAR));
        _cmds[HEAD][1].put("SUBM", new Integer(HEAD_SUBM));
        _cmds[HEAD][1].put("SUBN", new Integer(HEAD_SUBN));
        _cmds[SUBM][1].put("NAME", new Integer(0));
        _cmds[SUBM][1].put("ADDR", new Integer(0));
        _cmds[SOUR][1].put("AUTH", new Integer(0));
        _cmds[SOUR][1].put("TITL", new Integer(0));
        _cmds[SOUR][1].put("PUBL", new Integer(0));
        _cmds[SOUR][1].put("REPO", new Integer(0));
        _cmds[REPO][1].put("NAME", new Integer(0));
        _cmds[REPO][1].put("ADDR", new Integer(0));
        _cmds[SUBN][1].put("DESC", new Integer(0));
        _cmds[SUBN][1].put("ORDI", new Integer(0));
        _cmds[INDI][1].put("NAME", new Integer(INDI_NAME));
        _cmds[INDI][1].put("AFN", new Integer(INDI_AFN));
        _cmds[INDI][1].put("SEX", new Integer(INDI_SEX));
        _cmds[INDI][1].put("SOUR", new Integer(INDI_SOUR));
        _cmds[INDI][1].put("BIRT", new Integer(INDI_BIRT));
        _cmds[INDI][1].put("CHR", new Integer(INDI_CHR));
        _cmds[INDI][1].put("BURI", new Integer(INDI_BURI));
        _cmds[INDI][1].put("DEAT", new Integer(INDI_DEAT));
        _cmds[INDI][1].put("FAMS", new Integer(INDI_FAMS));
        _cmds[INDI][1].put("FAMC", new Integer(INDI_FAMC));
        _cmds[INDI][1].put("OCCU", new Integer(INDI_OCCU));
        _cmds[INDI][1].put("NOTE", new Integer(INDI_NOTE));
        _cmds[FAM][1].put("HUSB", new Integer(FAM_HUSB));
        _cmds[FAM][1].put("WIFE", new Integer(FAM_WIFE));
        _cmds[FAM][1].put("CHIL", new Integer(FAM_CHIL));
        _cmds[FAM][1].put("MARR", new Integer(FAM_MARR));
        _cmds[FAM][1].put("DIV", new Integer(FAM_DIV));
        _cmds[FAM][1].put("NOTE", new Integer(FAM_NOTE));
        _cmds[HEAD][2].put("VERS", new Integer(0));
        _cmds[HEAD][2].put("NAME", new Integer(0));
        _cmds[HEAD][2].put("CORP", new Integer(0));
        _cmds[HEAD][2].put("ADDR", new Integer(0));
        _cmds[HEAD][2].put("DATA", new Integer(0));
        _cmds[HEAD][2].put("TIME", new Integer(0));
        _cmds[HEAD][2].put("FORM", new Integer(0));
        _cmds[SUBM][2].put("CONT", new Integer(0));
        _cmds[SOUR][2].put("CALN", new Integer(0));
        _cmds[REPO][2].put("CONT", new Integer(0));
        _cmds[INDI][2].put("GIVN", new Integer(INDI_NAME_GIVN));
        _cmds[INDI][2].put("SURN", new Integer(INDI_NAME_SURN));
        _cmds[INDI][2].put("DATE", new Integer(INDI_IES_DATE));
        _cmds[INDI][2].put("PLAC", new Integer(INDI_IES_PLAC));
        _cmds[INDI][2].put("SOUR", new Integer(INDI_IES_SOUR));
        _cmds[INDI][2].put("CAUS", new Integer(INDI_DEAT_CAUS));
        _cmds[INDI][2].put("PAGE", new Integer(0));
        _cmds[INDI][2].put("CONT", new Integer(0));
        _cmds[INDI][2].put("CONC", new Integer(0));
        _cmds[FAM][2].put("DATE", new Integer(FAM_MARR_DATE));
        _cmds[FAM][2].put("PLAC", new Integer(FAM_MARR_PLAC));
        _cmds[FAM][2].put("SOUR", new Integer(FAM_MARR_SOUR));
        _cmds[FAM][2].put("FOST", new Integer(0));
        _cmds[HEAD][3].put("ADDR", new Integer(0));
        _cmds[HEAD][3].put("DATE", new Integer(0));
        _cmds[HEAD][3].put("COPR", new Integer(0));
        _cmds[SOUR][3].put("MEDI", new Integer(0));
        _cmds[HEAD][4].put("CONT", new Integer(0));
        _cmds[HEAD][4].put("PHON", new Integer(0));
    }

    public void load(String filename) {
        _loadedOk = false;
        _mode = NONE;
        _people.removeAllElements();
        _marriages.removeAllElements();
        _children.removeAllElements();
        _person = null;
        try {
            FileReader frd = new FileReader(filename);
            BufferedReader istrm = new BufferedReader(frd);
            String input, cmdstr;
            Integer cmdint;
            int line = 0;
            int level = -1;
            int index, cmd;
            while ((input = istrm.readLine()) != null) {
                line++;
                if (input.length() == 0) continue;
                try {
                    if ((input.length() < 5) || (input.charAt(1) != ' ')) throw new ParseException("Line too short");
                    try {
                        level = Integer.parseInt(input.substring(0, 1));
                    } catch (NumberFormatException e) {
                        throw new ParseException("Bad level");
                    }
                    if (input.charAt(2) == '@') {
                        index = input.indexOf(" ", 2);
                        if (index == -1) throw new ParseException("Missing @xnn@ command");
                        cmdstr = input.substring(index + 1);
                        _param = input.substring(3, index - 1);
                    } else {
                        index = input.indexOf(" ", 2);
                        if (index == -1) {
                            cmdstr = input.substring(2);
                            _param = "";
                        } else {
                            cmdstr = input.substring(2, index);
                            _param = input.substring(index + 1);
                        }
                    }
                    if (_param.length() == 0) _param = null;
                    if (level == 0) {
                        closeRecord();
                        cmdint = (Integer) _cmds0.get(cmdstr);
                    } else cmdint = (Integer) _cmds[_mode & 0x0f][level].get(cmdstr);
                    if (cmdint == null) {
                        if (cmdstr.charAt(0) != '@') throw new ParseException("Unknown tag '" + cmdstr + "' in mode 0x" + Integer.toHexString(_mode));
                        continue;
                    }
                    cmd = cmdint.intValue();
                    switch(_mode) {
                        case NONE:
                            if (cmd == HEAD) setMode(cmd); else throw new ParseException("Expecting HEAD");
                            break;
                        case HEAD:
                            if (cmd == HEAD_SOUR) setMode(cmd); else throw new ParseException("Expecting HEAD.SOUR");
                            break;
                        case HEAD_SOUR:
                            if ((cmd == HEAD_DEST) || (cmd == HEAD_DATE)) setMode(cmd); else if (level < 2) throw new ParseException("Expecting HEAD.DEST or HEAD.DATE");
                            break;
                        case HEAD_DEST:
                            if (cmd == HEAD_DATE) setMode(cmd); else throw new ParseException("Expecting HEAD.DATE");
                            break;
                        case HEAD_DATE:
                            if ((cmd == HEAD_FILE) || (cmd == HEAD_CHAR)) setMode(cmd); else if (level < 2) throw new ParseException("Expecting HEAD.FILE or HEAD.CHAR");
                            break;
                        case HEAD_FILE:
                            if (cmd == HEAD_GEDC) setMode(cmd); else throw new ParseException("Expecting HEAD.GEDC");
                            break;
                        case HEAD_GEDC:
                            if ((cmd == HEAD_CHAR) || (cmd == SUBM) || (cmd == INDI)) setMode(cmd); else if (level < 2) throw new ParseException("Expecting HEAD.CHAR, SUBM or INDI");
                            break;
                        case HEAD_CHAR:
                            if ((cmd == HEAD_SUBM) || (cmd == HEAD_GEDC)) setMode(cmd); else throw new ParseException("Expecting HEAD.SUBM or HEAD.GEDC");
                            break;
                        case HEAD_SUBM:
                            if ((cmd == HEAD_SUBN) || (cmd == HEAD_FILE)) setMode(cmd); else throw new ParseException("Expecting HEAD.SUBN or HEAD.FILE");
                            break;
                        case HEAD_SUBN:
                            if (cmd == SUBM) setMode(cmd); else throw new ParseException("Expecting 0 @@ SUBM");
                            break;
                        case SUBM:
                            if ((cmd == SOUR) || (cmd == INDI)) setMode(cmd); else if (level < 1) throw new ParseException("Expecting 0 @@ SOUR or INDI");
                            break;
                        case SOUR:
                            if ((cmd == REPO) || (cmd == TRLR)) setMode(cmd); else if (level < 1) throw new ParseException("Expecting 0 @@ REPO or TRLR");
                            break;
                        case REPO:
                            if (cmd == SUBN) setMode(cmd); else if (level < 1) throw new ParseException("Expecting 0 @@ SUBN");
                            break;
                        case SUBN:
                            if (cmd == INDI) setMode(cmd); else if (level < 1) throw new ParseException("Expecting 0 @@ INDI");
                            break;
                        case INDI:
                            if (cmd == INDI_NAME) setMode(cmd); else throw new ParseException("Expecting INDI.NAME");
                            break;
                        case INDI_NAME:
                            if ((cmd == INDI_NAME_GIVN) || (cmd == INDI_SEX)) setMode(cmd); else if (cmd == INDI_BIRT) {
                                setMode(cmd);
                                System.out.println("WARNING: " + line + ": Missing INDI.SEX");
                            } else if (cmd == INDI_FAMC) {
                                setMode(cmd);
                                System.out.println("WARNING: " + line + ": Missing INDI.SEX");
                            } else throw new ParseException("Expecting INDI.NAME.GIVN or INDI.SEX");
                            break;
                        case INDI_NAME_GIVN:
                            if (cmd == INDI_NAME_SURN) setMode(cmd); else throw new ParseException("Expecting INDI.NAME.SURN");
                            break;
                        case INDI_NAME_SURN:
                            if (cmd == INDI_AFN) setMode(cmd); else if (cmd == INDI_SEX) setMode(cmd); else throw new ParseException("Expecting INDI.AFN or INDI.SEX");
                            break;
                        case INDI_AFN:
                            if (cmd == INDI_SEX) setMode(cmd); else throw new ParseException("Expecting INDI.SEX");
                            break;
                        case INDI_SEX:
                            if ((cmd == INDI_SOUR) || (cmd == INDI_FAMS) || (cmd == INDI_FAMC) || (cmd == INDI_BIRT) || (cmd == INDI_DEAT)) setMode(cmd); else throw new ParseException("Expecting INDI.SOUR, INDI.FAMS, INDI.FAMC or INDI.BIRT");
                            break;
                        case INDI_SOUR:
                            if ((cmd == INDI_BIRT) || (cmd == INDI_CHR) || (cmd == INDI_BURI) || (cmd == INDI_FAMS) || (cmd == INDI_FAMC)) setMode(cmd); else if (cmd == INDI_SOUR) setMode(INDI_SOUR2); else throw new ParseException("Expecting INDI.BIRT, .CHR, .SOUR, .FAMS or .FAMC");
                            break;
                        case INDI_BIRT:
                            if (cmd == INDI_IES_DATE) setMode(INDI_BIRT_DATE); else if (cmd == INDI_IES_PLAC) setMode(INDI_BIRT_PLAC); else throw new ParseException("Expecting INDI.BIRT.DATE or INDI.BIRT.PLAC");
                            break;
                        case INDI_BIRT_DATE:
                            if (cmd == INDI_IES_PLAC) setMode(INDI_BIRT_PLAC); else if (cmd == INDI_IES_SOUR) setMode(INDI_BIRT_SOUR); else if ((cmd == INDI_DEAT) || (cmd == INDI_OCCU) || (cmd == INDI_FAMS) || (cmd == INDI_FAMC) || (cmd == INDI) || (cmd == FAM)) setMode(cmd); else throw new ParseException("Expecting INDI.BIRT.PLAC, .BIRT.SOUR, .DEAT, .OCCU, .FAMS, .FAMC or INDI or FAM");
                            break;
                        case INDI_BIRT_PLAC:
                            if (cmd == INDI_IES_SOUR) setMode(INDI_BIRT_SOUR); else if ((cmd == INDI_CHR) || (cmd == INDI_DEAT) || (cmd == INDI_BURI) || (cmd == INDI_FAMS) || (cmd == INDI_FAMC)) setMode(cmd); else if (cmd == INDI_SOUR) setMode(INDI_SOUR2); else throw new ParseException("Expecting INDI.BIRT.SOUR, INDI.CHR, .DEAT, .BURI, .SOUR, .FAMS or .FAMC");
                            break;
                        case INDI_BIRT_SOUR:
                            if ((cmd == INDI_CHR) || (cmd == INDI_DEAT) || (cmd == INDI_BURI) || (cmd == INDI_FAMS) || (cmd == INDI_FAMC)) setMode(cmd); else if (cmd == INDI_SOUR) setMode(INDI_SOUR2); else throw new ParseException("Expecting INDI.CHR, .DEAT, .BURI, .SOUR, .FAMS or .FAMC");
                            break;
                        case INDI_CHR:
                            if (cmd == INDI_IES_DATE) setMode(INDI_CHR_DATE); else throw new ParseException("Expecting INDI.CHR.DATE");
                            break;
                        case INDI_CHR_DATE:
                            if (cmd == INDI_IES_PLAC) setMode(INDI_CHR_PLAC); else if (cmd == INDI_IES_SOUR) setMode(INDI_CHR_SOUR); else throw new ParseException("Expecting INDI.CHR.PLAC or INDI.CHR.SOUR");
                            break;
                        case INDI_CHR_PLAC:
                            if (cmd == INDI_IES_SOUR) setMode(INDI_CHR_SOUR); else if ((cmd == INDI_DEAT) || (cmd == INDI_BURI) || (cmd == INDI_FAMS) || (cmd == INDI_FAMC)) setMode(cmd); else if (cmd == INDI_SOUR) setMode(INDI_SOUR2); else throw new ParseException("Expecting INDI.CHR.SOUR, INDI.SOUR, .DEAT, .BURI, .FAMS or .FAMC");
                            break;
                        case INDI_CHR_SOUR:
                            if ((cmd == INDI_DEAT) || (cmd == INDI_BURI) || (cmd == INDI_FAMS) || (cmd == INDI_FAMC)) setMode(cmd); else if (cmd == INDI_SOUR) setMode(INDI_SOUR2); else throw new ParseException("Expecting INDI.SOUR, .DEAT, .BURI, .FAMS or .FAMC");
                            break;
                        case INDI_DEAT:
                            if (cmd == INDI_IES_DATE) setMode(INDI_DEAT_DATE); else if (cmd == INDI) {
                                setMode(INDI);
                                System.out.println("WARNING: " + line + ": Missing INDI.DEAT.DATE");
                            } else throw new ParseException("Expecting INDI.DEAT.DATE");
                            break;
                        case INDI_DEAT_DATE:
                            if (cmd == INDI_IES_PLAC) setMode(INDI_DEAT_PLAC); else if (cmd == INDI_IES_SOUR) setMode(INDI_DEAT_SOUR); else if ((cmd == INDI_DEAT_CAUS) || (cmd == INDI_FAMS) || (cmd == INDI_FAMC) || (cmd == INDI)) setMode(cmd); else throw new ParseException("Expecting INDI.DEAT.PLAC, INDI.DEAT.SOUR, INDI.DEAT.CAUS, INDI.FAMS, INDI.FAMC or INDI");
                            break;
                        case INDI_DEAT_PLAC:
                            if (cmd == INDI_SOUR) setMode(INDI_SOUR2); else if (cmd == INDI_IES_SOUR) setMode(INDI_DEAT_SOUR); else if ((cmd == INDI_DEAT_CAUS) || (cmd == INDI_FAMS) || (cmd == INDI_FAMC)) setMode(cmd); else throw new ParseException("Expecting INDI.DEAT.SOUR, INDI.SOUR, .DEAT.CAUS, .FAMS or .FAMC");
                            break;
                        case INDI_DEAT_SOUR:
                            if (cmd == INDI_SOUR) setMode(INDI_SOUR2); else if ((cmd == INDI_FAMS) || (cmd == INDI_FAMC)) setMode(cmd); else throw new ParseException("Expecting INDI.SOUR, .FAMS or .FAMC");
                            break;
                        case INDI_DEAT_CAUS:
                            if (cmd == INDI_SOUR) setMode(INDI_SOUR2); else if ((cmd == INDI_FAMS) || (cmd == INDI_FAMC)) setMode(cmd); else throw new ParseException("Expecting INDI.SOUR, .FAMS or .FAMC");
                            break;
                        case INDI_BURI:
                            if (cmd == INDI_IES_DATE) setMode(INDI_BURI_DATE); else throw new ParseException("Expecting INDI.BURI.DATE");
                            break;
                        case INDI_BURI_DATE:
                            if (cmd == INDI_IES_PLAC) setMode(INDI_BURI_PLAC); else if (cmd == INDI_IES_SOUR) setMode(INDI_BURI_SOUR); else throw new ParseException("Expecting INDI.BURI.PLAC or INDI.BURI.SOUR");
                            break;
                        case INDI_BURI_PLAC:
                            if (cmd == INDI_SOUR) setMode(INDI_SOUR2); else if (cmd == INDI_IES_SOUR) setMode(INDI_BURI_SOUR); else if ((cmd == INDI_FAMS) || (cmd == INDI_FAMC)) setMode(cmd); else throw new ParseException("Expecting INDI.BURI.SOUR, INDI.SOUR, .FAMS or .FAMC");
                            break;
                        case INDI_BURI_SOUR:
                            if (cmd == INDI_SOUR) setMode(INDI_SOUR2); else if ((cmd == INDI_FAMS) || (cmd == INDI_FAMC)) setMode(cmd); else throw new ParseException("Expecting INDI.SOUR, .FAMS or .FAMC");
                            break;
                        case INDI_SOUR2:
                            if (cmd == INDI_FAMS) setMode(cmd); else if (level < 2) throw new ParseException("Expecting INDI.FAMS");
                            break;
                        case INDI_FAMS:
                            if ((cmd == INDI_FAMC) || (cmd == INDI_FAMS) || (cmd == INDI_BIRT)) setMode(cmd); else if (cmd == INDI) setMode(cmd); else if (cmd == FAM) setMode(cmd); else throw new ParseException("Expecting INDI.FAMC or 0 @@ INDI or FAM");
                            break;
                        case INDI_FAMC:
                            if ((cmd == INDI) || (cmd == FAM) || (cmd == INDI_BIRT)) setMode(cmd); else throw new ParseException("Expecting 0 @@ INDI or FAM or INDI.BIRT");
                            break;
                        case INDI_OCCU:
                            if ((cmd == INDI_NOTE) || (cmd == INDI)) setMode(cmd); else if (level < 2) throw new ParseException("Expecting INDI.NOTE or 0 @@ INDI");
                            break;
                        case INDI_NOTE:
                            if (cmd == INDI) setMode(cmd); else if (level < 2) throw new ParseException("Expecting 0 @@ INDI");
                            break;
                        case FAM:
                            if ((cmd == FAM_HUSB) || (cmd == FAM_MARR) || (cmd == FAM_DIV)) setMode(cmd); else if (cmd == FAM_WIFE) {
                                setMode(cmd);
                                System.out.println("WARNING: " + line + ": Missing FAM.HUSB");
                            } else throw new ParseException("Expecting FAM.HUSB, FAM.MARR or FAM.DIV");
                            break;
                        case FAM_HUSB:
                            if (cmd == FAM_WIFE) setMode(cmd); else if (cmd == FAM_CHIL) {
                                setMode(cmd);
                                System.out.println("WARNING: " + line + ": Missing FAM.WIFE");
                            } else throw new ParseException("Expecting FAM.WIFE");
                            break;
                        case FAM_WIFE:
                            if ((cmd == FAM_CHIL) || (cmd == FAM_MARR) || (cmd == FAM) || (cmd == TRLR)) setMode(cmd); else throw new ParseException("Expecting FAM.CHIL, FAM.MARR, FAM or TRLR");
                            break;
                        case FAM_CHIL:
                            if ((cmd == FAM_CHIL) || (cmd == FAM_MARR) || (cmd == FAM_DIV) || (cmd == FAM_NOTE) || (cmd == FAM) || (cmd == TRLR)) setMode(cmd); else if (level < 2) throw new ParseException("Expecting FAM.CHIL, .MARR, .DIV, .NOTE, FAM or TRLR");
                            break;
                        case FAM_MARR:
                            if ((cmd == FAM_MARR_DATE) || (cmd == FAM_MARR_PLAC)) setMode(cmd); else throw new ParseException("Expecting FAM.MARR.DATE or FAM.MARR.PLAC");
                            break;
                        case FAM_MARR_DATE:
                            if ((cmd == FAM_MARR_PLAC) || (cmd == FAM_MARR_SOUR) || (cmd == FAM_DIV) || (cmd == FAM_HUSB) || (cmd == FAM)) setMode(cmd); else throw new ParseException("Expecting FAM.MARR.PLAC, FAM.MARR.SOUR, FAM.DIV, FAM.HUSB or 0 @@ FAM");
                            break;
                        case FAM_MARR_PLAC:
                            if ((cmd == FAM_MARR_SOUR) || (cmd == FAM_DIV) || (cmd == FAM) || (cmd == SOUR) || (cmd == TRLR)) setMode(cmd); else throw new ParseException("Expecting FAM.MARR.SOUR, FAM.DIV, 0 @@ FAM, 0 @@ SOUR or TRLR");
                            break;
                        case FAM_MARR_SOUR:
                            if ((cmd == FAM) || (cmd == SOUR) || (cmd == TRLR)) setMode(cmd); else throw new ParseException("Expecting 0 @@ FAM, 0 @@ SOUR or TRLR");
                            break;
                        case FAM_DIV:
                            if ((cmd == FAM_HUSB) || (cmd == FAM)) setMode(cmd); else throw new ParseException("Expecting FAM.HUSB or 0 @@ FAM");
                            break;
                        case FAM_NOTE:
                            if (cmd == FAM) setMode(cmd); else throw new ParseException("Expecting FAM");
                            break;
                        default:
                            throw new ParseException("Unhandled tag '" + cmdstr + "' in mode 0x" + Integer.toHexString(_mode));
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("PARSE ERROR: " + line + ": Not enough commas (" + input + ")");
                } catch (ParseException e) {
                    System.out.println("PARSE ERROR: " + line + ": " + e.getMessage() + " (" + input + ")");
                    break;
                }
            }
            if (_mode == TRLR) {
                System.out.println("Parsed ok.");
                _loadedOk = true;
            } else System.out.println("PARSE ERROR: Malformed input");
            frd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMode(int mode) throws ParseException {
        _mode = mode;
        switch(mode) {
            case INDI:
                _key = _param;
                break;
            case INDI_NAME:
                if (_param.indexOf('/') != -1) {
                    if (_param.indexOf('/') > 0) _forenames = _param.substring(0, _param.indexOf('/') - 1);
                    _surname = _param.substring(_param.indexOf('/') + 1);
                    if (_surname.indexOf('/') != -1) _surname = _surname.substring(0, _surname.indexOf('/'));
                    if (_surname.indexOf('<') != -1) _surname = _surname.substring(0, _surname.indexOf('<') - 1);
                }
                break;
            case INDI_NAME_GIVN:
                _forenames = _param;
                break;
            case INDI_NAME_SURN:
                _surname = _param;
                break;
            case INDI_SEX:
                if (_param.equals("M")) _person = new Male(_key, _surname, _forenames); else if (_param.equals("F")) _person = new Female(_key, _surname, _forenames); else throw new ParseException("Sex must be M or F");
                break;
            case INDI_BIRT_DATE:
                if (_person != null) _person.setBorn(_param);
                break;
            case INDI_CHR_DATE:
                if (_person != null) _person.setChristened(_param);
                break;
            case INDI_DEAT_DATE:
                if (_person != null) _person.setDied(_param);
                break;
            case INDI_BURI_DATE:
                if (_person != null) _person.setDied(_param);
                break;
            case INDI_BIRT_PLAC:
                if (_person != null) _person.setBornPlace(_param);
                break;
            case INDI_CHR_PLAC:
                if (_person != null) _person.setChristenPlace(_param);
                break;
            case INDI_DEAT_PLAC:
                if (_person != null) _person.setDiedPlace(_param);
                break;
            case INDI_BURI_PLAC:
                if (_person != null) _person.setDiedPlace(_param);
                break;
            case FAM_HUSB:
                _husbkey = _param.substring(1, _param.length() - 1);
                break;
            case FAM_WIFE:
                _wifekey = _param.substring(1, _param.length() - 1);
                break;
            case FAM_CHIL:
                _children.addElement(_param.substring(1, _param.length() - 1));
                break;
            case FAM_MARR_DATE:
                _marrdate = _param;
                break;
            case FAM_MARR_PLAC:
                _marrplace = _param;
                break;
        }
    }

    private void closeRecord() throws ParseException {
        switch(_mode & 0x0f) {
            case INDI:
                if (_person != null) {
                    _people.addElement(_person);
                    _person = null;
                }
                _forenames = null;
                _surname = null;
                _key = null;
                break;
            case FAM:
                if ((_husbkey != null) && (_wifekey != null)) {
                    Person husband = Person.getPerson(_husbkey);
                    if (husband == null) throw new ParseException("Invalid husband key '" + _husbkey + "'");
                    Person wife = Person.getPerson(_wifekey);
                    if (wife == null) throw new ParseException("Invalid wife key '" + _wifekey + "'");
                    Marriage marriage = new Marriage(husband, wife, _marrdate);
                    if (_marrplace != null) marriage.setPlace(_marrplace);
                    _marriages.addElement(marriage);
                    if (!_children.isEmpty()) {
                        for (Enumeration e = _children.elements(); e.hasMoreElements(); ) {
                            Person child = Person.getPerson((String) e.nextElement());
                            if (child != null) {
                                child.mother = wife;
                                wife.addChild(child);
                                child.father = husband;
                                husband.addChild(child);
                            }
                        }
                    }
                }
                _husbkey = null;
                _wifekey = null;
                _children.removeAllElements();
                _marrdate = null;
                _marrplace = null;
        }
    }

    public Marriage[] getMarriages() {
        Marriage[] ret = new Marriage[_marriages.size()];
        _marriages.copyInto(ret);
        return ret;
    }

    public Person[] getPeople() {
        Person[] ret = new Person[_people.size()];
        _people.copyInto(ret);
        return ret;
    }

    public Person[] getConventionalRoots() {
        return null;
    }

    public Person[] getInvertedRoots() {
        return null;
    }

    public boolean isLoadedOk() {
        return _loadedOk;
    }

    public static void main(String[] args) {
        try {
            GedcomLoader loader = new GedcomLoader();
            loader.load("THORNBURROW.ged");
            Person[] people = loader.getPeople();
            Marriage[] marriages = loader.getMarriages();
            if (loader.isLoadedOk()) {
                PrintWriter pout = new PrintWriter(new FileWriter("gedcom.xml"));
                pout.println("<?xml version=\"1.0\" ?>");
                pout.println("");
                pout.println("<!DOCTYPE ftml SYSTEM \"ftml.dtd\">");
                pout.println("");
                pout.println("<ftml>");
                pout.println(" <people>");
                for (int n = 0; n < people.length; n++) people[n].write(pout);
                pout.println(" </people>");
                pout.println(" <marriages>");
                for (int n = 0; n < marriages.length; n++) marriages[n].write(pout);
                pout.println(" </marriages>");
                pout.println("</ftml>");
                pout.flush();
                pout.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("Press any key to exit");
        try {
            System.in.read();
        } catch (java.io.IOException e) {
        }
        System.exit(0);
    }
}
