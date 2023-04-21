package frost;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.table.*;
import javax.swing.*;

public class requestThread extends Thread {

    static java.util.ResourceBundle LangRes = java.util.ResourceBundle.getBundle("res.LangRes");

    final boolean DEBUG = true;

    private String filename;

    private String size;

    private String key;

    private String htl;

    private JTable downloadTable;

    private JTable uploadTable;

    private String board;

    public void run() {
        synchronized (frame1.threadCountLock) {
            frame1.activeDownloadThreads++;
        }
        DefaultTableModel tableModel = (DefaultTableModel) downloadTable.getModel();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        Date today = new Date();
        String date = formatter.format(today);
        File newFile = new File(frame1.frostSettings.getValue("downloadDirectory") + filename);
        boolean do_request = false;
        System.out.println("Download of " + filename + " with HTL " + htl + " started.");
        boolean success = FcpRequest.getFile(key, size, newFile, htl, true);
        synchronized (downloadTable) {
            try {
                int row = getTableEntry(tableModel);
                if (!success) {
                    System.out.println("Download of " + filename + " failed.");
                    if (row != -1) {
                        frame1.downloadTableModel.setValueAt(LangRes.getString("Failed"), row, 3);
                        int intHtl = 15;
                        try {
                            intHtl = Integer.parseInt(htl);
                        } catch (NumberFormatException e) {
                        }
                        if (intHtl > 10) {
                            if (DEBUG) System.out.println("Download failed, uploading request for " + filename);
                            do_request = true;
                        } else {
                            if (DEBUG) System.out.println("Download failed, but htl is too low to request it.");
                        }
                    }
                } else {
                    KeyClass newKey = new KeyClass(key);
                    newKey.setFilename(filename);
                    newKey.setSize(newFile.length());
                    newKey.setDate(date);
                    newKey.setExchange(false);
                    Index.add(newKey, new File(frame1.keypool + board));
                    File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + newFile.getPath());
                    if (row != -1) {
                        tableModel.setValueAt(LangRes.getString("Done"), row, 3);
                        frame1.updateDownloads = true;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception " + e.toString() + " occured in requestThread.run() for file " + filename);
            }
        }
        if (do_request) request(key.trim(), board);
        synchronized (frame1.threadCountLock) {
            frame1.activeDownloadThreads--;
        }
    }

    public int getTableEntry(DefaultTableModel tableModel) {
        for (int i = 0; i < tableModel.getRowCount(); i++) if (key.equals(tableModel.getValueAt(i, 6))) return i;
        return -1;
    }

    private void request(String key, String board) {
        String messageUploadHtl = frame1.frostSettings.getValue("tofUploadHtl");
        boolean requested = false;
        if (DEBUG) System.out.println("Uploading request of " + key + " from " + board);
        String fileSeparator = System.getProperty("file.separator");
        String destination = frame1.keypool + board + fileSeparator + DateFun.getDate() + fileSeparator;
        File checkDestination = new File(destination);
        if (!checkDestination.isDirectory()) checkDestination.mkdirs();
        File[] files = checkDestination.listFiles();
        for (int i = 0; i < files.length; i++) {
            String content = (FileAccess.readFile(files[i])).trim();
            if (content.equals(key)) {
                requested = true;
                System.out.println("File was already requested");
            }
        }
        if (!requested) {
            String date = DateFun.getDate();
            String time = DateFun.getFullExtendedTime() + "GMT";
            String uploadMe = String.valueOf(System.currentTimeMillis()) + ".txt";
            File messageFile = new File(destination + uploadMe);
            FileAccess.writeFile(key, messageFile);
            boolean success = false;
            int index = 0;
            String output = new String();
            int tries = 0;
            boolean error = false;
            while (!success) {
                File testMe = new File(new StringBuffer().append(destination).append(date).append("-").append(board).append("-").append(index).append(".req").toString());
                if (testMe.length() > 0) {
                    index++;
                    if (DEBUG) System.out.println("File exists, increasing index to " + index);
                } else {
                    String[] result = new String[2];
                    String upKey = new StringBuffer().append("KSK@frost/request/").append(frame1.frostSettings.getValue("messageBase")).append("/").append(date).append("-").append(board).append("-").append(index).append(".req").toString();
                    if (DEBUG) System.out.println(upKey);
                    result = FcpInsert.putFile(upKey, destination + uploadMe, messageUploadHtl, false, true);
                    System.out.println("FcpInsert result[0] = " + result[0] + " result[1] = " + result[1]);
                    if (result[0] == null || result[1] == null) {
                        result[0] = "Error";
                        result[1] = "Error";
                    }
                    if (result[0].equals("Success")) {
                        success = true;
                    } else {
                        if (result[0].equals("KeyCollision")) {
                            String compareMe = String.valueOf(System.currentTimeMillis()) + ".txt";
                            String requestMe = new StringBuffer().append("KSK@frost/request/").append(frame1.frostSettings.getValue("messageBase")).append("/").append(date).append("-").append(board).append("-").append(index).append(".req").toString();
                            if (FcpRequest.getFile(requestMe, "Unknown", frame1.keypool + compareMe, htl, false)) {
                                File numberOne = new File(frame1.keypool + compareMe);
                                File numberTwo = new File(destination + uploadMe);
                                String contentOne = (FileAccess.readFile(numberOne)).trim();
                                String contentTwo = (FileAccess.readFile(numberTwo)).trim();
                                if (DEBUG) System.out.println(contentOne);
                                if (DEBUG) System.out.println(contentTwo);
                                if (contentOne.equals(contentTwo)) {
                                    if (DEBUG) System.out.println("Key Collision and file was already requested");
                                    success = true;
                                } else {
                                    index++;
                                    System.out.println("Request Upload collided, increasing index to " + index);
                                }
                            } else {
                                System.out.println("Request upload failed (" + tries + "), retrying index " + index);
                                if (tries > 5) {
                                    success = true;
                                    error = true;
                                }
                                tries++;
                            }
                        }
                    }
                }
            }
            if (!error) {
                File killMe = new File(destination + uploadMe);
                File newMessage = new File(destination + date + "-" + board + "-" + index + ".req");
                killMe.renameTo(newMessage);
                TOF.addNewMessageToTable(newMessage, board);
                System.out.println("*********************************************************************");
                System.out.println("Request successfuly uploaded to the '" + board + "' board.");
                System.out.println("*********************************************************************");
            } else {
                System.out.println("Error while uploading message.");
                messageFile.delete();
            }
            System.out.println("Request Upload Thread finished");
        }
    }

    /**Constructor*/
    public requestThread(String filename, String size, JTable downloadTable, JTable uploadTable, String htl, String key, String board) {
        this.filename = filename;
        this.size = size;
        this.downloadTable = downloadTable;
        this.uploadTable = uploadTable;
        this.htl = htl;
        this.key = key;
        this.board = board.toLowerCase();
    }
}
