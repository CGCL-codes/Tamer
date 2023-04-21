package com.sitescape.team.smtp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;

public class SendMail {

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2) {
            System.err.println("Usage: SendMail filename <port>");
            System.err.println("  Reads an RFC 2822 formatted (including headers, but NOT dot-encoded) file from file 'filename'");
            System.err.println("  and sends it to the SMTP server on localhost at port number 'port' (defaults to 2525).");
            System.err.println("  SMTP envelope info (MAIL FROM and RCPT TO) taken from To: and From: headers in file");
            System.exit(0);
        }
        File input = new File(args[0]);
        LineNumberReader reader = null;
        int port = 2525;
        String line = null;
        String sender = null;
        List<String> recipients = new LinkedList<String>();
        try {
            reader = new LineNumberReader(new FileReader(input));
            reader.setLineNumber(1);
            reader.mark(9196);
            boolean inHeader = true;
            while (inHeader && (line = reader.readLine()) != null) {
                if (line.length() == 0) {
                    inHeader = false;
                }
                if (inHeader && line.startsWith("To:")) {
                    recipients.add(line.substring(3).trim());
                } else if (inHeader && line.startsWith("From:")) {
                    sender = line.substring(5).trim();
                }
            }
            if (recipients.size() == 0) {
                System.out.println("No 'To:' header found in file.  Cannot send mail.");
                System.exit(1);
            }
            if (sender == null) {
                System.out.println("No 'From:' header found in file.  Cannot send mail.");
                System.exit(1);
            }
            reader.reset();
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file: " + args[0]);
        } catch (IOException e) {
            System.err.println("Error reading from file " + args[0] + ": " + e.getMessage());
            if (reader != null) {
                System.err.println("  at or after line number " + reader.getLineNumber());
            }
        }
        SMTPClient client = null;
        try {
            client = new SMTPClient();
            client.connect("localhost", port);
            int reply = client.getReplyCode();
            if (!SMTPReply.isPositiveCompletion(reply)) {
                System.err.println("Unable to connect to localhost at port " + port + ": " + client.getReplyString());
                client.disconnect();
                System.exit(1);
            }
            if (!client.login()) {
                System.err.println("Unable to login to localhost at port " + port + ": " + client.getReplyString());
                client.disconnect();
                System.exit(1);
            }
            if (!client.setSender(sender)) {
                System.err.println("Server rejected sender " + sender + ": " + client.getReplyString());
                client.disconnect();
                System.exit(1);
            }
            for (String recipient : recipients) {
                if (!client.addRecipient(recipient)) {
                    System.err.println("Server rejected recipient " + recipient + ": " + client.getReplyString());
                    client.disconnect();
                    System.exit(1);
                }
            }
            Writer writer = client.sendMessageData();
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write('\n');
            }
            writer.close();
            if (!client.completePendingCommand()) {
                System.err.println("Error sending message: " + client.getReplyString());
                client.disconnect();
                System.exit(1);
            }
            client.logout();
        } catch (IOException e) {
            System.err.println("Error communicating with localhost on port " + port + ": " + e.getMessage());
            if (client.isConnected()) {
                try {
                    client.disconnect();
                } catch (IOException f) {
                }
            }
        }
    }
}
