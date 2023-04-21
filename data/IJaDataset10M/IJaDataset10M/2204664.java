package frost;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import frost.crypt.*;

/**
 * Uploads a message to a certain message board
 */
public class MessageUploadThread extends Thread {

    private static boolean debug = true;

    static java.util.ResourceBundle LangRes = java.util.ResourceBundle.getBundle("res.LangRes");

    static final boolean DEBUG = false;

    private Frame frameToLock;

    private String board;

    private String from;

    private String subject;

    private String text;

    private String messageUploadHtl;

    private String messageDownloadHtl;

    private String date;

    private String time;

    private String keypool;

    private String destination;

    private String privateKey;

    private String publicKey;

    private boolean secure;

    private boolean silent;

    private boolean signed;

    private boolean encryptSign;

    private Identity recipient;

    /**
     * Extracts all attachments from a message and returns
     * them in a Vector
     * @return Vector with paths of the attachments
     */
    private Vector getAttachments() {
        int start = text.indexOf("<attach>");
        int end = text.indexOf("</attach>", start);
        Vector attachments = new Vector();
        while (start != -1 && end != -1) {
            attachments.add(text.substring(start + 8, end));
            start = text.indexOf("<attach>", end);
            end = text.indexOf("</attach>", start);
        }
        return attachments;
    }

    /**
     *sign
     */
    private void sign() {
        if (from.compareTo(frame1.getMyId().getName()) == 0) {
            if (debug) System.out.println("signing message");
            text = new String(text + "<key>" + (frame1.getMyId()).getKeyAddress() + "</key>");
            if (encryptSign && recipient != null) {
                System.out.println("encrypting message");
                text = frame1.getCrypto().encryptSign(text, frame1.getMyId().getPrivKey(), recipient.getKey());
                subject = new String("ENCRYPTED MSG FOR : " + recipient.getStrippedName());
            } else text = frame1.getCrypto().sign(text, (frame1.getMyId()).getPrivKey());
            from = new String(from + "@" + frame1.getCrypto().digest(frame1.getMyId().getKey()));
            signed = true;
        }
    }

    /**
     * Uploads attachments
     */
    private void uploadAttachments() {
        Vector attachments = getAttachments();
        for (int i = 0; i < attachments.size(); i++) {
            String attachment = (String) attachments.elementAt(i);
            String[] result = { "", "" };
            System.out.println("Uploading attachment " + attachment + " with HTL " + frame1.frostSettings.getValue("htlUpload") + ".");
            while (!result[0].equals("KeyCollision") && !result[0].equals("Success")) result = FcpInsert.putFile("CHK@", attachment, frame1.frostSettings.getValue("htlUpload"), true, true);
            String chk = result[1];
            int position = text.indexOf("<attach>" + attachment + "</attach>");
            int length = attachment.length() + 17;
            File attachedFile = new File(attachment);
            String newText = text.substring(0, position) + "<attached>" + attachedFile.getName();
            newText += " * " + chk + "</attached>";
            newText += text.substring(position + length, text.length());
            text = newText;
        }
    }

    public void run() {
        boolean retry = true;
        frame1.tofUploadThreads++;
        String state = SettingsFun.getValue(frame1.keypool + board + ".key", "state");
        if (state.equals("writeAccess")) {
            privateKey = SettingsFun.getValue(frame1.keypool + board + ".key", "privateKey");
            publicKey = SettingsFun.getValue(frame1.keypool + board + ".key", "publicKey");
            secure = true;
        } else {
            secure = false;
        }
        if (DEBUG) System.out.println("tofUpload: " + board + " secure: " + privateKey);
        System.out.println("Uploading message to '" + board + "' board with HTL " + messageUploadHtl + ".");
        uploadAttachments();
        sign();
        String fileSeparator = System.getProperty("file.separator");
        destination = keypool + board + fileSeparator + DateFun.getDate() + fileSeparator;
        File checkDestination = new File(destination);
        if (!checkDestination.isDirectory()) checkDestination.mkdirs();
        if (date.length() == 0 && time.length() == 0) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            date = cal.get(Calendar.YEAR) + ".";
            int month = cal.get(Calendar.MONTH) + 1;
            date += month + ".";
            int day = cal.get(Calendar.DATE);
            date += day;
            time = "";
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if (hour < 10) {
                time += "0" + hour + ":";
            } else {
                time += hour + ":";
            }
            int minute = cal.get(Calendar.MINUTE);
            if (minute < 10) {
                time += "0" + minute + ":";
            } else {
                time += minute + ":";
            }
            int second = cal.get(Calendar.SECOND);
            if (second < 10) {
                time += "0" + second + "GMT";
            } else {
                time += second + "GMT";
            }
        }
        String uploadMe = "unsent" + String.valueOf(System.currentTimeMillis()) + ".txt";
        String content = new String();
        content += "board=" + board + "\r\n";
        content += "from=" + from + "\r\n";
        content += "subject=" + subject + "\r\n";
        content += "date=" + date + "\r\n";
        content += "time=" + time + "\r\n";
        content += "--- message ---\r\n";
        content += text;
        File messageFile = new File(destination + uploadMe);
        FileAccess.writeFile(content, messageFile);
        while (retry) {
            boolean success = false;
            int index = 0;
            String output = new String();
            int tries = 0;
            boolean error = false;
            while (!success) {
                File testMe = new File(destination + date + "-" + board + "-" + index + ".txt");
                if (testMe.length() > 0) {
                    String contentOne = (FileAccess.readFile(messageFile)).trim();
                    String contentTwo = (FileAccess.readFile(testMe)).trim();
                    if (DEBUG) System.out.println(contentOne);
                    if (DEBUG) System.out.println(contentTwo);
                    if (contentOne.equals(contentTwo)) {
                        if (DEBUG) System.out.println("Message has already been uploaded.");
                        success = true;
                    } else {
                        index++;
                        if (DEBUG) System.out.println("File exists, increasing index to " + index);
                    }
                } else {
                    String[] result = new String[2];
                    if (secure) {
                        String upKey = privateKey + "/" + board + "/" + date + "-" + index + ".txt";
                        if (DEBUG) System.out.println(upKey);
                        result = FcpInsert.putFile(upKey, destination + uploadMe, messageUploadHtl, false, true);
                    } else {
                        String upKey = "KSK@sftmeage/" + frame1.frostSettings.getValue("messageBase") + "/" + date + "-" + board + "-" + index + ".txt";
                        if (DEBUG) System.out.println(upKey);
                        result = FcpInsert.putFile(upKey, destination + uploadMe, messageUploadHtl, false, true);
                        if (result[0].equals("Success")) {
                            upKey = "KSK@frost/message/" + frame1.frostSettings.getValue("messageBase") + "/" + date + "-" + board + "-" + index + ".txt";
                            if (DEBUG) System.out.println(upKey);
                            FcpInsert.putFile(upKey, destination + uploadMe, messageUploadHtl, false, true);
                        }
                    }
                    if (result[0] == null || result[1] == null) {
                        result[0] = "Error";
                        result[1] = "Error";
                    }
                    if (result[0].equals("Success")) {
                        success = true;
                    } else {
                        if (result[0].equals("KeyCollision")) {
                            String compareMe = String.valueOf(System.currentTimeMillis()) + ".txt";
                            String requestMe = "KSK@sftmeage/" + frame1.frostSettings.getValue("messageBase") + "/" + date + "-" + board + "-" + index + ".txt";
                            if (secure && publicKey.startsWith("SSK@")) {
                                requestMe = publicKey + "/" + board + "/" + date + "-" + index + ".txt";
                            }
                            if (FcpRequest.getFile(requestMe, "Unknown", keypool + compareMe, messageDownloadHtl, false)) {
                                File numberOne = new File(keypool + compareMe);
                                File numberTwo = new File(destination + uploadMe);
                                String contentOne = (FileAccess.readFile(numberOne)).trim();
                                String contentTwo = (FileAccess.readFile(numberTwo)).trim();
                                if (DEBUG) System.out.println(contentOne);
                                if (DEBUG) System.out.println(contentTwo);
                                if (contentOne.equals(contentTwo)) {
                                    success = true;
                                } else {
                                    index++;
                                    System.out.println("TOF Upload collided, increasing index to " + index);
                                }
                            } else {
                                index++;
                                System.out.println("TOF Upload collided, increasing index to " + index);
                            }
                        } else {
                            System.out.println("TOF upload failed (" + tries + "), retrying index " + index);
                            if (tries > 5) {
                                success = true;
                                error = true;
                            }
                            tries++;
                        }
                    }
                }
            }
            if (!error) {
                File killMe = new File(destination + uploadMe);
                File newMessage = new File(destination + date + "-" + board + "-" + index + ".txt");
                if (signed) FileAccess.writeFile("GOOD", newMessage.getPath() + ".sig");
                killMe.renameTo(newMessage);
                frame1.updateTof = true;
                System.out.println("*********************************************************************");
                System.out.println("Message successfuly uploaded to the '" + board + "' board.");
                System.out.println("*********************************************************************");
                retry = false;
            } else {
                System.out.println("Error while uploading message.");
                if (!silent) {
                    MessageUploadFailedDialog faildialog = new MessageUploadFailedDialog(frameToLock, 10, LangRes.getString("Upload of message failed"), LangRes.getString("I was not able to upload your message."), LangRes.getString("Retry"), LangRes.getString("Cancel"));
                    faildialog.show();
                    retry = faildialog.getAnswer();
                    System.out.println("Will try to upload again: " + retry);
                    faildialog.dispose();
                }
                if (!retry) messageFile.delete();
            }
            System.out.println("TOF Upload Thread finished");
        }
        frame1.tofUploadThreads--;
    }

    /**Constructor*/
    public MessageUploadThread(String[] args, Frame frameToLock) {
        this.board = args[0];
        this.from = args[1];
        this.subject = args[2];
        this.text = args[3];
        this.messageUploadHtl = args[4];
        this.keypool = args[5];
        this.messageDownloadHtl = args[6];
        if (args.length > 7) {
            this.date = args[7];
            this.time = args[8];
        } else {
            this.date = "";
            this.time = "";
        }
        if (args.length > 9 && args[9] != null) {
            encryptSign = true;
            recipient = frame1.getFriends().Get(args[9]);
        }
        this.silent = false;
        this.signed = false;
        this.frameToLock = frameToLock;
    }
}
