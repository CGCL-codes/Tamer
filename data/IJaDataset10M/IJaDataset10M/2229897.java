package de.chdev.artools.loga.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.configuration.Configuration;
import de.chdev.artools.loga.lang.KeywordLoader;
import de.chdev.artools.loga.model.ApiLogElement;
import de.chdev.artools.loga.model.LogElement;
import de.chdev.artools.loga.model.LogElementType;
import de.chdev.artools.loga.model.SqlLogElement;

public class SqlController implements ILogController {

    private boolean prefixAvailable;

    public static final String PREFIX = "<SQL >";

    private MainController mainController;

    private Configuration keywords;

    private Pattern patternSqlStart;

    private Pattern patternSqlStop;

    private Pattern patternSqlCommit;

    public SqlController(Configuration keywords, MainController mainController) {
        this.keywords = keywords;
        this.mainController = mainController;
        String regexKey;
        regexKey = keywords.getString("sql.start");
        patternSqlStart = Pattern.compile(regexKey);
        regexKey = keywords.getString("sql.stop");
        patternSqlStop = Pattern.compile(regexKey);
        regexKey = keywords.getString("sql.commit");
        patternSqlCommit = Pattern.compile(regexKey);
    }

    public int setLogLine(String text, int lineNumber) {
        String textWoPrefix = removePrefix(text);
        boolean result = false;
        if (!result) result = checkSqlStart(textWoPrefix, lineNumber);
        if (!result) result = checkSqlStop(textWoPrefix, lineNumber);
        if (!result) result = checkSqlCommit(textWoPrefix, lineNumber);
        return 0;
    }

    private String removePrefix(String text) {
        if (text.startsWith(PREFIX)) {
            prefixAvailable = true;
            return text.substring(6);
        } else {
            prefixAvailable = false;
            return text;
        }
    }

    private boolean checkSqlStart(String textWoPrefix, int lineNumber) {
        Matcher m = patternSqlStart.matcher(textWoPrefix.trim());
        boolean valid = m.matches();
        if (valid) {
            LogElement parent = mainController.getLastOpenLogElement();
            String temp = textWoPrefix;
            String threadId = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(threadId.length());
            String rpcId = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(rpcId.length());
            String queue = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(queue.length());
            String clientRpc = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(clientRpc.length());
            String user = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(user.length());
            String timestampString = temp.substring(0, temp.indexOf("*/") + 2);
            temp = temp.substring(timestampString.length());
            String sqlText = temp;
            Long timestamp = mainController.convertToTimestamp(timestampString.substring(3, timestampString.length() - 3).trim(), PREFIX);
            SqlLogElement logElement = new SqlLogElement();
            logElement.setElementAlias("SQL");
            logElement.setElementType(LogElementType.SERVERACTION);
            logElement.setName(sqlText);
            logElement.setParentLogElement(parent);
            logElement.setQueue(queue);
            logElement.setRpcId(rpcId);
            logElement.setStartLineNumber(lineNumber);
            logElement.setStartTimestamp(timestamp);
            logElement.setText(textWoPrefix);
            logElement.setThreadId(threadId);
            logElement.setUser(user);
            logElement.setValid(true);
            mainController.openNewLogElement(logElement);
            return true;
        } else {
            return false;
        }
    }

    private boolean checkSqlStop(String textWoPrefix, int lineNumber) {
        Matcher m = patternSqlStop.matcher(textWoPrefix.trim());
        boolean valid = m.matches();
        if (valid) {
            LogElement logElement = mainController.getLastOpenLogElement();
            String temp = textWoPrefix;
            String threadId = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(threadId.length());
            String rpcId = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(rpcId.length());
            String queue = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(queue.length());
            String clientRpc = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(clientRpc.length());
            String user = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(user.length());
            String timestampString = temp.substring(0, temp.indexOf("*/") + 2);
            temp = temp.substring(timestampString.length());
            String sqlText = temp;
            String result = temp.substring(sqlText.length());
            Long timestamp = mainController.convertToTimestamp(timestampString.substring(3, timestampString.length() - 3).trim(), PREFIX);
            if (logElement instanceof SqlLogElement) {
                logElement.setEndLineNumber(lineNumber);
                ((SqlLogElement) logElement).setEndTimestamp(timestamp);
                mainController.closeLastLogElement();
            } else {
                System.out.println("Error during sql close in line " + lineNumber);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean checkSqlCommit(String textWoPrefix, int lineNumber) {
        Configuration keywords = KeywordLoader.getConfiguration(PREFIX);
        String regexKey = keywords.getString("sql.commit");
        Pattern p = Pattern.compile(regexKey);
        Matcher m = p.matcher(textWoPrefix.trim());
        boolean valid = m.matches();
        if (valid) {
            LogElement parent = mainController.getLastOpenLogElement();
            String temp = textWoPrefix;
            String threadId = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(threadId.length());
            String rpcId = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(rpcId.length());
            String queue = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(queue.length());
            String clientRpc = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(clientRpc.length());
            String user = temp.substring(0, temp.indexOf(">") + 1);
            temp = temp.substring(user.length());
            String timestampString = temp.substring(0, temp.indexOf("*/") + 2);
            temp = temp.substring(timestampString.length());
            String sqlText = temp;
            String result = temp.substring(sqlText.length());
            Long timestamp = mainController.convertToTimestamp(timestampString.substring(3, timestampString.length() - 3).trim(), PREFIX);
            SqlLogElement logElement = new SqlLogElement();
            logElement.setElementAlias("SQL");
            logElement.setElementType(LogElementType.SERVERACTION);
            logElement.setName(sqlText);
            logElement.setParentLogElement(parent);
            logElement.setQueue(queue);
            logElement.setRpcId(rpcId);
            logElement.setStartLineNumber(lineNumber);
            logElement.setStartTimestamp(timestamp);
            logElement.setText(textWoPrefix);
            logElement.setThreadId(threadId);
            logElement.setUser(user);
            logElement.setValid(true);
            mainController.openNewLogElement(logElement);
            logElement.setEndLineNumber(lineNumber - 1);
            logElement.setEndTimestamp(timestamp);
            mainController.closeLastLogElement();
            return true;
        } else {
            return false;
        }
    }
}
