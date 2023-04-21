package translator;

import java.sql.*;
import java.io.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class mozDBWriter extends Writer {

    private String destinationURL;

    private String languageCode;

    private Connection dbConn;

    private Statement stmt;

    private static final int DATABASE_MODE_UPDATE = 1;

    private static final int DATABASE_MODE_INSERT = 2;

    private int dbWritingMode;

    private String presentInstall;

    private String presentFileName;

    private String presentFilePath;

    private int presentInstallId;

    private int presentFileId;

    private PreparedStatement prepTextStmt;

    private PreparedStatement prepKeyStmt;

    public mozDBWriter(String install, String destDBURL) {
        super(install, destDBURL);
        String strSQL = "";
        presentInstall = install;
        presentFileName = "";
        presentFilePath = "";
        destinationURL = destDBURL;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            dbConn = DriverManager.getConnection(destinationURL, "admin", "");
            stmt = dbConn.createStatement();
            prepKeyStmt = dbConn.prepareStatement("insert into keys (file_id, key_id, key_name) values (?,?,?)");
            prepTextStmt = dbConn.prepareStatement("insert into UIText (key_id, text_id, lang_id, seq_no, text_string) values  (?, ?, ?, ?, ?)");
            setDbWritingMode();
            commitInstallInfoToDB();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public String getTargetLanguageCode() {
        return languageCode;
    }

    public void setTargetLanguageCode(String targetLanguage) {
        languageCode = targetLanguage;
    }

    private void setDbWritingMode() throws SQLException {
        String strSQL = "";
        ResultSet rs = null;
        strSQL = "select install_id from Installs where install_name='" + presentInstall + "'";
        rs = stmt.executeQuery(strSQL);
        if (rs.next()) {
            dbWritingMode = DATABASE_MODE_UPDATE;
            presentInstallId = rs.getInt("install_id");
        } else {
            dbWritingMode = DATABASE_MODE_INSERT;
        }
    }

    private void commitInstallInfoToDB() throws SQLException {
        String strSQL;
        ResultSet rs;
        int newInstallId;
        if (dbWritingMode == DATABASE_MODE_INSERT) {
            strSQL = "select max(install_id) + 1 as maxInstallId from Installs";
            rs = stmt.executeQuery(strSQL);
            if (rs.next()) {
                newInstallId = rs.getInt("maxInstallId");
                strSQL = "insert into installs (install_id, install_name) values";
                strSQL = strSQL + "(" + newInstallId + ",'" + presentInstall + "')";
                stmt.executeUpdate(strSQL);
                presentInstallId = newInstallId;
            }
        } else if (dbWritingMode == DATABASE_MODE_UPDATE) {
        }
    }

    private void commitFileInfoToDB(TranslationFile currentFile) throws SQLException {
        int newFileId;
        String strSQL;
        ResultSet rs;
        if (dbWritingMode == DATABASE_MODE_INSERT) {
            strSQL = "select max(file_id) + 1 as maxFileId from Files";
            rs = stmt.executeQuery(strSQL);
            if (rs.next()) {
                newFileId = rs.getInt("maxFileId");
                strSQL = "insert into Files (install_id, file_id, name, location) values";
                strSQL = strSQL + "(" + presentInstallId + "," + newFileId;
                strSQL = strSQL + ",'" + currentFile.getFileName() + "','" + currentFile.getFilePath() + "')";
                stmt.executeUpdate(strSQL);
                presentFileId = newFileId;
            }
        } else if (dbWritingMode == DATABASE_MODE_UPDATE) {
            strSQL = "select f.file_id, f.location from Files as f, Installs as i ";
            strSQL = strSQL + " where f.install_id = i.install_id ";
            strSQL = strSQL + " and i.install_id=" + presentInstallId;
            strSQL = strSQL + " and f.name='" + currentFile.getFileName() + "'";
            rs = stmt.executeQuery(strSQL);
            String fileLocation = "";
            boolean continueLoop = true;
            while (rs.next() && continueLoop) {
                fileLocation = rs.getString("location");
                if (currentFile.getFilePath().endsWith(fileLocation.substring(14))) {
                    presentFileId = rs.getInt("file_id");
                    continueLoop = false;
                }
            }
        }
    }

    public void exportFile(TranslationFile currentFile) {
        String strSQL = "";
        int intInstallId = 0;
        try {
            commitFileInfoToDB(currentFile);
            presentFileName = currentFile.getFileName();
            presentFilePath = currentFile.getFilePath();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void exportPhrase(Phrase currentPhrase) {
        String strSQL;
        int intFileId = 0;
        int intKeyId = 0;
        if (currentPhrase.getText().length() > 0) {
            if (dbWritingMode == DATABASE_MODE_INSERT) {
                insertNewPhraseToDB(currentPhrase);
            } else {
                updatePhraseInDB(currentPhrase);
            }
        }
    }

    private void insertNewPhraseToDB(Phrase currentPhrase) {
        ResultSet rs;
        String strSQL = "";
        int newKeyId;
        String textString = replaceQuote(currentPhrase.getText());
        try {
            strSQL = "select max(key_id) + 1 as maxKeyId from Keys";
            rs = stmt.executeQuery(strSQL);
            if (rs.next()) {
                newKeyId = rs.getInt("maxKeyId");
                strSQL = "insert into keys (file_id, key_id, key_name) values";
                strSQL = strSQL + "(" + presentFileId + "," + newKeyId + ",'" + currentPhrase.getKey() + "')";
                prepKeyStmt.setInt(1, presentFileId);
                prepKeyStmt.setInt(2, newKeyId);
                prepKeyStmt.setString(3, currentPhrase.getKey());
                prepKeyStmt.executeUpdate();
                strSQL = "insert into keys (file_id, key_id, key_name) values ";
                strSQL = strSQL + "(" + presentFileId + "," + newKeyId + ",'" + currentPhrase.getKey() + "')";
                languageCode = presentFilePath.substring(8, 10);
                strSQL = "select max(text_id) + 1 as maxTextId from UIText";
                ResultSet rs2 = stmt.executeQuery(strSQL);
                rs2.next();
                int intNewTextId = rs2.getInt("maxTextId");
                int textLength = textString.length();
                int segStart = 0;
                int segEnd = 254;
                int seq_no = 0;
                String textSubString = "";
                if (textString.length() > 254) {
                    while (segEnd < textLength) {
                        textSubString = textString.substring(segStart, segEnd);
                        strSQL = "insert into UIText (key_id, text_id, lang_id, seq_no, text_string) values ";
                        strSQL = strSQL + "(" + newKeyId + "," + intNewTextId + "'" + languageCode + "','" + "(" + textString + ")')";
                        prepTextStmt.setInt(1, newKeyId);
                        prepTextStmt.setInt(2, intNewTextId);
                        prepTextStmt.setString(3, languageCode);
                        prepTextStmt.setInt(4, seq_no);
                        prepTextStmt.setString(5, textSubString);
                        prepTextStmt.executeUpdate();
                        seq_no++;
                        segStart = segStart + 254;
                        segEnd = segEnd + 254;
                    }
                    textSubString = textString.substring(segStart, textLength);
                } else {
                    textSubString = textString.substring(0, textLength);
                }
                prepTextStmt.setInt(1, newKeyId);
                prepTextStmt.setInt(2, intNewTextId);
                prepTextStmt.setString(3, languageCode);
                prepTextStmt.setInt(4, seq_no);
                prepTextStmt.setString(5, textSubString);
                prepTextStmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(strSQL);
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void updatePhraseInDB(Phrase currentPhrase) {
        ResultSet rs;
        String strSQL = "";
        int keyId;
        int textId;
        String textString = replaceQuote(currentPhrase.getText());
        try {
            strSQL = "select ui.text_id, k.key_id from ";
            strSQL = strSQL + " UIText as ui, Keys as k, Files as f, Installs as i";
            strSQL = strSQL + " where i.install_id = f.install_id ";
            strSQL = strSQL + " and f.file_id = k.file_id ";
            strSQL = strSQL + " and k.key_id = ui.key_id ";
            strSQL = strSQL + " and i.install_id = " + presentInstallId;
            strSQL = strSQL + " and f.file_id = " + presentFileId;
            strSQL = strSQL + " and k.key_name = '" + currentPhrase.getKey() + "'";
            rs = stmt.executeQuery(strSQL);
            if (rs.next()) {
                keyId = rs.getInt("Key_Id");
                textId = rs.getInt("text_id");
                String languageCode = presentFilePath.substring(8, 10);
                int textLength = textString.length();
                int segStart = 0;
                int segEnd = 254;
                int seq_no = 0;
                String textSubString = "";
                if (textString.length() > 254) {
                    while (segEnd < textLength) {
                        textSubString = textString.substring(segStart, segEnd);
                        strSQL = "insert into UIText (key_id, text_id, lang_id, seq_no, text_string) values ";
                        strSQL = strSQL + "(" + keyId + "," + textId + "'" + languageCode + "','" + "(" + textString + ")')";
                        prepTextStmt.setInt(1, keyId);
                        prepTextStmt.setInt(2, textId);
                        prepTextStmt.setString(3, languageCode);
                        prepTextStmt.setInt(4, seq_no);
                        prepTextStmt.setString(5, textSubString);
                        prepTextStmt.executeUpdate();
                        seq_no++;
                        segStart = segStart + 254;
                        segEnd = segEnd + 254;
                    }
                    textSubString = textString.substring(segStart, textLength);
                } else {
                    textSubString = textString.substring(0, textLength);
                }
                prepTextStmt.setInt(1, keyId);
                prepTextStmt.setInt(2, textId);
                prepTextStmt.setString(3, languageCode);
                prepTextStmt.setInt(4, seq_no);
                prepTextStmt.setString(5, textSubString);
                prepTextStmt.executeUpdate();
            } else {
                insertNewPhraseToDB(currentPhrase);
            }
        } catch (Exception e) {
            System.out.println(strSQL);
            e.printStackTrace();
            System.exit(1);
        }
    }

    private String replaceQuote(String someText) {
        int i;
        String ch;
        String resultText = "";
        for (i = 0; i < someText.length(); i++) {
            ch = someText.substring(i, i + 1);
            if (ch.equalsIgnoreCase("'")) {
                resultText = resultText + "''";
            } else {
                resultText = resultText + ch;
            }
        }
        return resultText;
    }

    public void concludeExportFile() {
    }

    public void prepareExportFile() {
    }

    public void close() {
    }
}
