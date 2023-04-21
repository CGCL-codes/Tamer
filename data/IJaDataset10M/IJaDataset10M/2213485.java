package com.arcucomp.xmlcommand;

import com.arcucomp.util.XCURLLoader;
import com.arcucomp.xmgel.XMgeLEnvironment;
import java.io.*;
import java.util.*;

public class XCRegisterCommand_i extends XCCommand_i {

    public XCRegisterCommand_i(XMgeLEnvironment cm) {
        super(cm);
        loggerPrintln(5, "XCRegisterCommand(XCCustomCommandManager cm)<");
        this.cm = cm;
        loggerPrintln(5, "XCRegisterCommand()>");
    }

    public void finalize() {
        loggerPrintln(5, "XCRegisterCommand::finalize ends ");
    }

    public int processCommand() {
        loggerPrintln(5, "XCRegisterCommand::processCommand>");
        int status = 0;
        try {
            loggerPrintln(5, "XCRegisterCommand::processCommand-SubCommand");
            String subCommand = new String("");
            m_URL = new String("");
            m_URL = (String) m_subCommands.get("URL");
            m_Type = new String("");
            m_Type = (String) m_subCommands.get("Type");
            m_Name = new String("");
            m_Name = (String) m_subCommands.get("Name");
            String m_className = new String("");
            m_className = (String) m_subCommands.get("ClassName");
            if (m_Type != null && (m_Type.equalsIgnoreCase("cl") || m_Type.equalsIgnoreCase("CommandList"))) {
                if (m_URL != null && !m_URL.equalsIgnoreCase("")) {
                    this.registerCommand(m_Name, m_URL);
                }
            } else if (m_Type != null && m_Type.equalsIgnoreCase("class")) {
                if (m_URL != null && !m_URL.equalsIgnoreCase("")) {
                    XCURLLoader loader = new XCURLLoader();
                    Object o = loader.loadclass(m_URL, m_className);
                    this.registerCommand(m_Name, (XCCommand_i) o);
                }
            }
        } catch (Exception xmlEx) {
            loggerPrintln(5, "Not a Command or not fomated properly" + "XML Exception: " + xmlEx.getMessage());
        }
        loggerPrintln(5, "XCRegisterCommand::processCommand>");
        return status;
    }

    public void registerCommand(String commandString, String cl) {
        if (commandString != null && !(commandString.equals(""))) {
            this.cm.registerCommand(commandString.toLowerCase(), cl);
        }
    }

    public void registerCommand(String commandString, XCCommand_i cmd) {
        if (commandString != null && !(commandString.equals(""))) {
            this.cm.registerCommand(commandString.toLowerCase(), cmd);
        }
    }
}
