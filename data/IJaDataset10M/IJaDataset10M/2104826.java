package frost.storage.database;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import org.joda.time.*;
import frost.*;
import frost.boards.*;
import frost.gui.*;
import frost.messages.*;
import frost.storage.database.applayer.*;
import frost.util.*;

public class ImportXmlMessages {

    private static Logger logger = Logger.getLogger(ImportXmlMessages.class.getName());

    private Hashtable<String, Board> boardDirNames;

    private long uncommittedBytes = 0;

    private Splashscreen splashScreen;

    private String origSplashText;

    private int archiveMode;

    private int messagesMaxDaysOldDefault;

    SettingsClass oldSettings;

    public static final int DELETE_MESSAGES = 1;

    public static final int ARCHIVE_MESSAGES = 2;

    public static final int KEEP_MESSAGES = 3;

    private static final int MINIMUM_DAYS_OLD = 28;

    public void importXmlMessages(SettingsClass settings, List boards, Splashscreen splash, String origTxt, File importBaseDir, SettingsClass set) {
        splashScreen = splash;
        origSplashText = origTxt;
        oldSettings = set;
        String importKeypoolDir = oldSettings.getValue("keypool.dir");
        String importArchiveDir = oldSettings.getValue("archive.dir");
        String importSentDir = oldSettings.getValue("sent.dir");
        String strMode = oldSettings.getValue(SettingsClass.MESSAGE_EXPIRATION_MODE);
        if (strMode.toUpperCase().equals("KEEP")) {
            archiveMode = KEEP_MESSAGES;
        } else if (strMode.toUpperCase().equals("ARCHIVE")) {
            archiveMode = ARCHIVE_MESSAGES;
        } else if (strMode.toUpperCase().equals("DELETE")) {
            archiveMode = DELETE_MESSAGES;
        } else {
            archiveMode = KEEP_MESSAGES;
        }
        messagesMaxDaysOldDefault = Core.frostSettings.getIntValue(SettingsClass.MESSAGE_EXPIRE_DAYS) + 1;
        if (messagesMaxDaysOldDefault < Core.frostSettings.getIntValue(SettingsClass.MAX_MESSAGE_DISPLAY)) {
            messagesMaxDaysOldDefault = Core.frostSettings.getIntValue(SettingsClass.MAX_MESSAGE_DISPLAY) + 1;
        }
        if (messagesMaxDaysOldDefault < Core.frostSettings.getIntValue(SettingsClass.MAX_MESSAGE_DOWNLOAD)) {
            messagesMaxDaysOldDefault = Core.frostSettings.getIntValue(SettingsClass.MAX_MESSAGE_DOWNLOAD) + 1;
        }
        boardDirNames = new Hashtable<String, Board>();
        for (Iterator iter = boards.iterator(); iter.hasNext(); ) {
            Board board = (Board) iter.next();
            boardDirNames.put(board.getBoardFilename(), board);
        }
        try {
            AppLayerDatabase.getInstance().setAutoCommitOff();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "error set autocommit off", e);
        }
        File keypoolDir = new File(importKeypoolDir);
        if (!keypoolDir.isAbsolute()) {
            keypoolDir = new File(importBaseDir.getPath() + File.separatorChar + importKeypoolDir);
        }
        importDir(keypoolDir);
        File archiveDir = null;
        String archiveBaseDir = null;
        if (importArchiveDir == null || importArchiveDir.length() == 0) {
            logger.severe("no ARCHIVE DIR specified!");
        } else {
            File tmpFile = new File(importArchiveDir);
            if (!tmpFile.isAbsolute()) {
                importArchiveDir = importBaseDir.getPath() + File.separatorChar + importArchiveDir;
            }
            if (!importArchiveDir.endsWith(File.separator)) {
                importArchiveDir += File.separatorChar;
            }
            archiveBaseDir = importArchiveDir;
            importArchiveDir += "messages";
            archiveDir = new File(importArchiveDir);
            if (archiveDir.isDirectory() == false) {
                logger.severe("no archive dir found");
                archiveDir = null;
            }
        }
        if (archiveDir != null) {
            importDir(archiveDir);
        }
        File sentDir = new File(importSentDir);
        if (!sentDir.isAbsolute()) {
            sentDir = new File(importBaseDir.getPath() + File.separatorChar + importSentDir);
        }
        importSentDir(sentDir);
        if (archiveBaseDir != null) {
            archiveBaseDir += "sent";
            archiveDir = new File(archiveBaseDir);
            if (archiveDir.isDirectory() == false) {
                logger.severe("no archive sent dir found");
                archiveDir = null;
            } else {
                File[] dateDirs = archiveDir.listFiles();
                if (dateDirs == null || dateDirs.length == 0) {
                    logger.warning("no files in archive sent dir");
                } else {
                    for (int i = 0; i < dateDirs.length; i++) {
                        File dateDir = dateDirs[i];
                        if (!dateDir.isDirectory()) {
                            continue;
                        }
                        importSentDir(dateDir);
                    }
                }
            }
        }
        try {
            AppLayerDatabase.getInstance().setAutoCommitOn();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "error set autocommit on", e);
        }
    }

    private void importSentDir(File sentDir) {
        File[] sentFiles = sentDir.listFiles();
        if (sentFiles == null || sentFiles.length == 0) {
            logger.warning("no files in sent dir");
            return;
        }
        splashScreen.setText(origSplashText + " (sent messages)");
        for (int i = 0; i < sentFiles.length; i++) {
            File sentFile = sentFiles[i];
            String fname = sentFile.getName();
            int index = -1;
            try {
                int p1 = fname.lastIndexOf("-");
                int p2 = fname.lastIndexOf(".xml");
                if (p1 > 0 && p2 > 0) {
                    String ixStr = fname.substring(p1 + 1, p2);
                    index = Integer.parseInt(ixStr);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            if (index < 0) {
                logger.severe("Error getting index from filename: " + fname);
                continue;
            }
            MessageXmlFile mof = null;
            try {
                mof = new MessageXmlFile(sentFile);
            } catch (MessageCreationException e) {
                logger.severe("Error reading sent xml file: " + sentFile.getPath());
                continue;
            }
            String boardName = mof.getBoardName();
            Board board = (Board) boardDirNames.get(boardName);
            if (board == null) {
                logger.warning("board is not in boardlist, skipping import: " + boardName);
                continue;
            }
            FrostMessageObject mo = new FrostMessageObject(mof, board, index);
            try {
                AppLayerDatabase.getSentMessageTable().insertMessage(mo);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error inserting sent message into database", e);
            }
        }
    }

    private void importDir(File impDir) {
        File[] boardDirs = impDir.listFiles();
        if (boardDirs == null || boardDirs.length == 0) {
            logger.severe("no board dirs found: " + impDir.getPath());
            return;
        }
        for (int i = 0; i < boardDirs.length; i++) {
            File boardDir = boardDirs[i];
            if (boardDir.isDirectory() == false) {
                continue;
            }
            String boardName = boardDir.getName();
            Board board = (Board) boardDirNames.get(boardName);
            if (board == null) {
                logger.warning("board is not in boardlist, skipping import: " + boardName);
                continue;
            }
            int messagesMaxDaysOld = messagesMaxDaysOldDefault;
            if (board.isConfigured()) {
                messagesMaxDaysOld = Math.max(board.getMaxMessageDisplay(), messagesMaxDaysOld);
                messagesMaxDaysOld = Math.max(board.getMaxMessageDownload(), messagesMaxDaysOld);
            }
            DateMidnight maxMessageDateBack = new DateMidnight(DateTimeZone.UTC).minusDays(messagesMaxDaysOld);
            int indexMaxDaysOld = Core.frostSettings.getIntValue(SettingsClass.MAX_MESSAGE_DOWNLOAD) * 2;
            if (indexMaxDaysOld < MINIMUM_DAYS_OLD) {
                indexMaxDaysOld = MINIMUM_DAYS_OLD;
            }
            DateMidnight maxIndexDateBack = new DateMidnight(DateTimeZone.UTC).minusDays(indexMaxDaysOld);
            File[] dateDirs = boardDir.listFiles();
            if (dateDirs == null || dateDirs.length == 0) {
                logger.severe("no date dirs in keypool for " + boardDir.getName());
                continue;
            }
            IndexSlotsDatabaseTable indexSlots = new IndexSlotsDatabaseTable(IndexSlotsDatabaseTable.MESSAGES, board);
            for (int j = 0; j < dateDirs.length; j++) {
                File dateDir = dateDirs[j];
                if (dateDir.isDirectory() == false) {
                    continue;
                }
                DateMidnight dateDirCal = null;
                try {
                    dateDirCal = DateFun.FORMAT_DATE.parseDateTime(dateDir.getName()).toDateMidnight();
                } catch (NumberFormatException ex) {
                    logger.warning("Incorrect board date folder name, must be a date: " + dateDir);
                    continue;
                }
                File[] msgFiles = dateDir.listFiles();
                if (msgFiles == null || msgFiles.length == 0) {
                    continue;
                }
                splashScreen.setText(origSplashText + " (" + board.getName() + ", " + dateDir.getName() + ")");
                for (int k = 0; k < msgFiles.length; k++) {
                    File msgFile = msgFiles[k];
                    if (msgFile.isFile() == false) {
                        continue;
                    }
                    importMessageFile(msgFile, board, dateDirCal, indexSlots, maxIndexDateBack, maxMessageDateBack);
                }
            }
            indexSlots.close();
        }
    }

    private void importMessageFile(File msgFile, Board board, DateMidnight calDL, IndexSlotsDatabaseTable indexSlots, DateMidnight maxIndexDateBack, DateMidnight maxMessageDateBack) {
        String fname = msgFile.getName();
        int index = -1;
        try {
            int p1 = fname.lastIndexOf("-");
            int p2 = fname.lastIndexOf(".xml");
            if (p1 > 0 && p2 > 0) {
                String ixStr = fname.substring(p1 + 1, p2);
                index = Integer.parseInt(ixStr);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (index < 0) {
            logger.severe("Error getting index from filename: " + fname);
            return;
        }
        MessageXmlFile mof = null;
        String invalidReason = null;
        try {
            mof = new MessageXmlFile(msgFile);
        } catch (MessageCreationException e) {
            if (e.isEmpty()) {
                invalidReason = FileAccess.readFile(msgFile).trim();
            }
        }
        if (mof != null) {
            FrostMessageObject mo = new FrostMessageObject(mof, board, index);
            mo.setNew(mof.isMessageNew());
            if (mo.getDateAndTime().isBefore(maxMessageDateBack)) {
                if (archiveMode == ARCHIVE_MESSAGES) {
                    mo.setNew(false);
                    insertArchiveMessage(mo);
                } else if (archiveMode == KEEP_MESSAGES) {
                    insertValidMessage(mo);
                } else {
                }
            } else {
                insertValidMessage(mo);
            }
        } else if (invalidReason != null) {
            FrostMessageObject invalidMsg = new FrostMessageObject(board, calDL.toDateTime(), index, invalidReason);
            insertInvalidMessage(invalidMsg);
        }
        uncommittedBytes += msgFile.length();
        if (uncommittedBytes > 5 * 1024 * 1024) {
            try {
                AppLayerDatabase.getInstance().commit();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "error on commit", e);
            }
            uncommittedBytes = 0;
        }
        if (calDL.isAfter(maxIndexDateBack)) {
            try {
                indexSlots.setDownloadSlotUsed(index, calDL.toDateTime().getMillis());
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error inserting message index into database", e);
            }
        }
    }

    private void insertArchiveMessage(FrostMessageObject mo) {
        try {
            AppLayerDatabase.getMessageArchiveTable().insertMessage(mo);
        } catch (SQLException e) {
            if (e.getMessage().indexOf("msgarc_unique") > 0) {
                logger.log(Level.WARNING, "Archive of duplicate message skipped");
            } else {
                logger.log(Level.SEVERE, "Error inserting message into archive database", e);
            }
        }
    }

    private void insertValidMessage(FrostMessageObject mo) {
        try {
            AppLayerDatabase.getMessageTable().insertMessage(mo);
        } catch (SQLException e) {
            if (e.getMessage().indexOf("MSG_ID_UNIQUE_ONLY") > 0) {
                logger.log(Level.WARNING, "Import of duplicate message skipped");
            } else {
                logger.log(Level.SEVERE, "Error inserting message into database", e);
            }
        }
    }

    private void insertInvalidMessage(FrostMessageObject mo) {
        try {
            AppLayerDatabase.getMessageTable().insertMessage(mo);
        } catch (SQLException e) {
            if (e.getMessage().indexOf("MSG_ID_UNIQUE_ONLY") > 0) {
                logger.log(Level.WARNING, "Import of duplicate message skipped");
            } else {
                logger.log(Level.SEVERE, "Error inserting invalid message into database", e);
            }
        }
    }
}
