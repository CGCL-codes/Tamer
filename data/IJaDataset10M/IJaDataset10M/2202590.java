package jp.ne.nifty.iga.midori.shell.eng;

import jp.ne.nifty.iga.midori.shell.MdShellDef;
import jp.ne.nifty.iga.midori.shell.cmd.*;
import jp.ne.nifty.iga.midori.shell.util.MdShellUtilString;
import java.io.*;
import java.text.Collator;
import java.util.*;

public class MdShellCmdList {

    public static final String[][] straryCommandList = { { "exec", "invoke module" }, { "exit", "exit                   , quit is alias" }, { "history", "show command history   , ! is alias" }, { "menu", "show menu" }, { "quit", "exit" } };

    /**
	 * list of implemented command object.
	 */
    private Vector vecImplementedCommandElementList = new Vector(128);

    private Vector vecAddCommandFromFile = new Vector();

    public MdShellCmdList() {
        init();
    }

    private void init() {
        for (int index = 0; index < straryCommandList.length; index++) {
            String[] straryLook = straryCommandList[index];
            addImplementedCommand(new MdShellCmdAlias(straryLook[0], straryLook[1]));
        }
        addImplementedCommand(new MdShellCommandBase64());
        addImplementedCommand(new MdShellCommandCat());
        addImplementedCommand(new MdShellCommandCodeConv());
        addImplementedCommand(new MdShellCommandEdit());
        addImplementedCommand(new MdShellCommandGrep());
        addImplementedCommand(new MdShellCommandHelp());
        addImplementedCommand(new MdShellCommandMcd());
        addImplementedCommand(new MdShellCommandMemo());
        addImplementedCommand(new MdShellCommandMmake());
        addImplementedCommand(new MdShellCommandPwd());
        addImplementedCommand(new MdShellCommandSelect());
        addImplementedCommand(new MdShellCommandSplit());
        initFromFile();
        sortImplementedCommand();
    }

    private void initFromFile() {
        try {
            File fileLoad = new File(MdShellDef.FILE_DEF_COMMAND);
            if (fileLoad.exists() == false) {
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(fileLoad));
            for (; ; ) {
                String strRead = reader.readLine();
                if (strRead == null) {
                    break;
                }
                StringTokenizer toknizerTab = new StringTokenizer(strRead, "\t\n", false);
                String strCommand = null;
                String strDisplayString = null;
                String strPathName = null;
                try {
                    strCommand = toknizerTab.nextToken();
                    strDisplayString = toknizerTab.nextToken();
                    strPathName = toknizerTab.nextToken();
                } catch (NoSuchElementException ex) {
                }
                if (strCommand != null) {
                    if (strCommand.equals("edit")) {
                        strCommand = "$" + strCommand;
                    }
                    addImplementedCommand(new MdShellCmdAlias(strCommand, strDisplayString, strPathName));
                    vecAddCommandFromFile.add(new MdShellCmdAlias(strCommand, strDisplayString, strPathName));
                }
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
    }

    private void sortImplementedCommand() {
        Comparator cmparetorMdShellCommandInterface = new Comparator() {

            public int compare(Object o1, Object o2) {
                MdShellCommandInterface f1 = (MdShellCommandInterface) o1;
                MdShellCommandInterface f2 = (MdShellCommandInterface) o2;
                return f1.getCommand().compareTo(f2.getCommand());
            }
        };
        Collections.sort(vecImplementedCommandElementList, cmparetorMdShellCommandInterface);
    }

    /**
	 * 
	 */
    private void addImplementedCommand(MdShellCommandInterface cmdelement) {
        vecImplementedCommandElementList.addElement(cmdelement);
    }

    public Vector getAddedCommandFromFile() {
        return vecAddCommandFromFile;
    }

    public Vector getImplementedCommandList() {
        Vector vecRet = new Vector(128);
        for (int index = 0; index < vecImplementedCommandElementList.size(); index++) {
            MdShellCommandInterface cmdelement = (MdShellCommandInterface) vecImplementedCommandElementList.elementAt(index);
            cmdelement.registerImplementedCommand(vecRet);
        }
        return vecRet;
    }

    public String getImplementedCommandListForDisplayString() {
        StringBuffer strbuf = new StringBuffer();
        for (int index = 0; index < vecImplementedCommandElementList.size(); index++) {
            MdShellCommandInterface cmdelementLook = (MdShellCommandInterface) vecImplementedCommandElementList.elementAt(index);
            if (cmdelementLook.getCommand().charAt(0) == '$') {
                continue;
            }
            strbuf.append(" ");
            strbuf.append(cmdelementLook.getHelpString());
            strbuf.append("\n");
        }
        return strbuf.toString();
    }

    private Vector getMenuFilterList() {
        Vector vecList = new Vector(128);
        try {
            File fileLoad = new File(MdShellDef.FILE_DEF_MENU);
            if (fileLoad.exists() == false) {
                return vecList;
            }
            BufferedReader reader = new BufferedReader(new FileReader(fileLoad));
            for (; ; ) {
                String strRead = reader.readLine();
                if (strRead == null) {
                    break;
                }
                vecList.addElement(strRead);
            }
            reader.close();
        } catch (IOException ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
        return vecList;
    }

    public Vector getFilteredCommandList() {
        Vector vecMenuCommand = new Vector(128);
        for (int index = 0; index < vecImplementedCommandElementList.size(); index++) {
            MdShellCommandInterface cmdelementLook = (MdShellCommandInterface) vecImplementedCommandElementList.elementAt(index);
            String strCommand = cmdelementLook.getCommand();
            for (int indexInner = 0; indexInner < getMenuFilterList().size(); indexInner++) {
                String strCompare = (String) getMenuFilterList().elementAt(indexInner);
                if (strCommand.equals(strCompare)) {
                    vecMenuCommand.addElement(strCommand);
                    break;
                }
            }
        }
        return vecMenuCommand;
    }

    public Vector getFilteredCommandListForDisplay() {
        Vector vecMenuCommand = new Vector(128);
        for (int index = 0; index < vecImplementedCommandElementList.size(); index++) {
            MdShellCommandInterface cmdelementLook = (MdShellCommandInterface) vecImplementedCommandElementList.elementAt(index);
            String strCommand = cmdelementLook.getCommand();
            for (int indexInner = 0; indexInner < getMenuFilterList().size(); indexInner++) {
                String strCompare = (String) getMenuFilterList().elementAt(indexInner);
                if (strCommand.equals(strCompare)) {
                    vecMenuCommand.addElement(cmdelementLook.getHelpString());
                    break;
                }
            }
        }
        return vecMenuCommand;
    }

    public boolean processCommand(MdShellEnv shellenv, String strCommand) throws MdShellException {
        for (int index = 0; index < vecImplementedCommandElementList.size(); index++) {
            MdShellCommandInterface cmdelementLook = (MdShellCommandInterface) vecImplementedCommandElementList.elementAt(index);
            if (cmdelementLook.isThisCommand(shellenv, strCommand)) {
                cmdelementLook.processCommand(shellenv);
                return true;
            }
        }
        return false;
    }

    public String getFullPathNameByCommand(String strCommand) {
        for (int index = 0; index < vecImplementedCommandElementList.size(); index++) {
            MdShellCommandInterface cmdelementLook = (MdShellCommandInterface) vecImplementedCommandElementList.elementAt(index);
            if (strCommand.equals(cmdelementLook.getCommand())) {
                if (cmdelementLook instanceof MdShellCmdAlias) {
                    MdShellCmdAlias cmdDef = (MdShellCmdAlias) cmdelementLook;
                    return cmdDef.getFullPathName();
                } else {
                    return null;
                }
            }
        }
        return null;
    }
}
