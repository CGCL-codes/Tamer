package com.qbrowser.persist;

import com.qbrowser.QBrowserV2;
import com.qbrowser.localstore.LocalMessageContainer;
import com.qbrowser.util.QBrowserUtil;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author takemura
 */
public class TextMessageReader extends PersistedMessageReader {

    String retrieved_text_body;

    long body_size = -1;

    @Override
    public File readPersistedMessageWithLazyLoad(File msgArchive) throws Exception {
        File workdirFile = super.readPersistedMessageWithLazyLoad(msgArchive);
        File size_file = new File(workdirFile.getAbsolutePath() + File.separator + "textbodysize");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(size_file));
            String line = br.readLine();
            if (line != null) {
                try {
                    body_size = Long.parseLong(line.trim());
                } catch (NumberFormatException ne) {
                }
            }
        } catch (IOException ie) {
            clearDir(workdirFile);
            body_size = 0;
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return workdirFile;
    }

    @Override
    public File readPersistedMessage(File msgArchive) throws Exception {
        File workdirFile = super.readPersistedMessage(msgArchive);
        File body_file = new File(workdirFile.getAbsolutePath() + File.separator + "TextMessageBody.txt");
        if (body_file == null || !body_file.exists()) {
            clearDir(workdirFile);
            throw new IOException(QBrowserV2.resources.getString("qkey.msg.msg332") + body_file.getName() + QBrowserV2.resources.getString("qkey.msg.msg333"));
        }
        java.io.FileInputStream fi = null;
        ByteArrayOutputStream baos = null;
        try {
            fi = new FileInputStream(workdirFile.getAbsolutePath() + File.separator + "TextMessageBody.txt");
            baos = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int len = 0;
            int filesizecount = 0;
            while ((len = fi.read(buf)) != -1) {
                filesizecount += buf.length;
                baos.write(buf, 0, len);
            }
            retrieved_text_body = baos.toString();
        } catch (Exception ie) {
            clearDir(workdirFile);
            ie.printStackTrace();
            throw ie;
        } finally {
            if (fi != null) {
                try {
                    fi.close();
                } catch (IOException iie) {
                }
                fi = null;
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException iie) {
                }
                baos = null;
            }
        }
        return workdirFile;
    }

    @Override
    public LocalMessageContainer recreateMessagefromReadData(Session session) throws Exception {
        TextMessage msg = session.createTextMessage();
        LocalMessageContainer lmc = new LocalMessageContainer();
        if (properties != null) {
            QBrowserUtil.copyUserProperties(properties, msg);
        }
        lmc.setMessage(msg);
        if (headers != null) {
            QBrowserUtil.copyMessageHeaders(headers, msg);
            QBrowserUtil.populateHeadersOfLocalMessageContainer(headers, lmc);
        }
        msg.setText(retrieved_text_body);
        lmc.setReal_file_path(source_file_path);
        return lmc;
    }

    @Override
    public LocalMessageContainer recreateMessagefromReadDataWithLazyLoad() throws Exception {
        LocalMessageContainer lmc = super.recreateMessagefromReadDataWithLazyLoad();
        lmc.setBody_size(body_size);
        lmc.setMessage_type(QBrowserV2.TEXTMESSAGE);
        return lmc;
    }
}
