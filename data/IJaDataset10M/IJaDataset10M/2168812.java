package com.qbrowser.persist;

import javax.jms.Destination;
import com.qbrowser.QBrowserV2;
import com.qbrowser.localstore.LocalMessageContainer;
import com.qbrowser.property.Property;
import com.qbrowser.util.QBrowserUtil;
import java.awt.TextArea;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.swing.JDialog;

/**
 *
 * @author takemura
 */
public class MessagePersister {

    Message message;

    LocalMessageContainer lmc;

    ArrayList headers = new ArrayList();

    ArrayList properties = new ArrayList();

    JDialog msgDialog;

    StringBuilder sbuf;

    public static final String STRING_START = "<@begin-@string@>";

    public static final String STRING_END = "<@end-@string@>";

    public static final String BYTES_START = "<@begin-@bytes@>";

    public static final String BYTES_END = "<@end-@bytes@>";

    static ResourceBundle resources = QBrowserV2.resources;

    public MessagePersister() {
    }

    public MessagePersister(Message value) {
        message = value;
    }

    public MessagePersister(LocalMessageContainer vlmc) {
        lmc = vlmc;
        message = vlmc.getMessage();
    }

    public void setTextBuffer(StringBuilder sbuffer) {
        sbuf = sbuffer;
    }

    public void setMsgDialog(JDialog value) {
        msgDialog = value;
    }

    public void zipUp(File workdirFile, File targetFile) {
        Zipper mm = new Zipper();
        try {
            File tempzip = new File(QBrowserUtil.getQBrowserTempFileDir() + System.currentTimeMillis());
            sbuf.append(resources.getString("qkey.msg.msg257") + " " + targetFile.getAbsolutePath() + "\n");
            mm.zipForDir(workdirFile, tempzip, false, ".zip");
            sbuf.append(resources.getString("qkey.msg.msg258") + " " + targetFile.getName() + "\n");
            if (targetFile.exists()) {
                targetFile.delete();
            }
            if (!tempzip.renameTo(targetFile)) {
                sbuf.append(resources.getString("qkey.msg.msg254"));
                tempzip.delete();
            }
            cleanupWorkDir(workdirFile);
            workdirFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File persistToFile() throws Exception {
        if (lmc != null) {
            collectJMSHeadersFromLMC();
        } else {
            collectJMSHeaders();
        }
        String workdir = QBrowserUtil.getQBrowserTempFileDir() + "JMS" + System.nanoTime() + File.separator;
        File workdirFile = new File(workdir);
        workdirFile.mkdirs();
        File target1 = new File(workdir + "JMSHeaders.txt");
        PrintWriter pwr = null;
        try {
            pwr = new PrintWriter(new FileWriter(target1));
            pwr.println(propertiesToString(headers));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (pwr != null) {
                pwr.close();
                pwr = null;
            }
        }
        collectProperties();
        File target2 = new File(workdir + "UserProperties.txt");
        PrintWriter pwr2 = null;
        try {
            pwr2 = new PrintWriter(new FileWriter(target2));
            pwr2.println(propertiesToString(properties));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (pwr2 != null) {
                pwr2.close();
                pwr2 = null;
            }
        }
        return workdirFile;
    }

    public void cleanupWorkDir(File workdir) {
        if (workdir == null) {
            return;
        }
        File[] files = workdir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                cleanupWorkDir(files[i]);
                if (files[i].listFiles().length == 0) {
                    files[i].delete();
                }
            } else if (files[i].isFile()) {
                try {
                    files[i].delete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    String propertiesToString(ArrayList props) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (int i = 0; i < props.size(); i++) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("\n");
            }
            Property prop = (Property) props.get(i);
            sb.append("\"").append(prop.getKey()).append("\"");
            sb.append(",").append(prop.getProperty_type());
            sb.append("=");
            if (prop.getProperty_type().equals(Property.STRING_TYPE)) {
                sb.append(STRING_START).append(prop.getProperty_value()).append(STRING_END);
            } else {
                sb.append("\"").append(prop.getProperty_value()).append("\"");
            }
        }
        return sb.toString();
    }

    void collectJMSHeaders() {
        try {
            String msgid = message.getJMSMessageID();
            Property msgidp = new Property();
            msgidp.setKey("JMSMessageID");
            msgidp.setProperty_type(Property.STRING_TYPE);
            msgidp.setProperty_value(msgid);
            headers.add(msgidp);
            Destination d1 = message.getJMSDestination();
            String s1 = null;
            if (d1 != null) {
                if (d1 instanceof Queue) {
                    s1 = ((Queue) d1).getQueueName() + QBrowserV2.QUEUE_SUFFIX;
                } else {
                    s1 = ((Topic) d1).getTopicName() + QBrowserV2.TOPIC_SUFFIX;
                }
            } else {
                s1 = "";
            }
            Property jdestp = new Property();
            jdestp.setKey("JMSDestination");
            jdestp.setProperty_type(Property.STRING_TYPE);
            jdestp.setProperty_value(s1);
            headers.add(jdestp);
            Destination d = message.getJMSReplyTo();
            String s = null;
            if (d != null) {
                if (d instanceof Queue) {
                    s = ((Queue) d).getQueueName() + QBrowserV2.QUEUE_SUFFIX;
                } else {
                    s = ((Topic) d).getTopicName() + QBrowserV2.TOPIC_SUFFIX;
                }
            } else {
                s = "";
            }
            Property jrepto = new Property();
            jrepto.setKey("JMSReplyTo");
            jrepto.setProperty_type(Property.STRING_TYPE);
            jrepto.setProperty_value(s);
            headers.add(jrepto);
            String jcorid = message.getJMSCorrelationID();
            Property jcoridp = new Property();
            jcoridp.setKey("JMSCorrelationID");
            jcoridp.setProperty_type(Property.STRING_TYPE);
            jcoridp.setProperty_value(jcorid);
            headers.add(jcoridp);
            int delivermode = message.getJMSDeliveryMode();
            Property delivermodep = new Property();
            delivermodep.setKey("JMSDeliverMode");
            delivermodep.setProperty_type(Property.INT_TYPE);
            delivermodep.setProperty_value(delivermode);
            headers.add(delivermodep);
            int jpri = message.getJMSPriority();
            Property jprip = new Property();
            jprip.setKey("JMSPriority");
            jprip.setProperty_type(Property.INT_TYPE);
            jprip.setProperty_value(jpri);
            headers.add(jprip);
            long jexp = message.getJMSExpiration();
            Property jexp_prop = new Property();
            jexp_prop.setKey("JMSExpiration");
            jexp_prop.setProperty_type(Property.LONG_TYPE);
            jexp_prop.setProperty_value(jexp);
            headers.add(jexp_prop);
            String jtype = message.getJMSType();
            Property jtypep = new Property();
            jtypep.setKey("JMSType");
            jtypep.setProperty_type(Property.STRING_TYPE);
            jtypep.setProperty_value(jtype);
            headers.add(jtypep);
            boolean redelivered = message.getJMSRedelivered();
            Property redvd = new Property();
            redvd.setKey("JMSRedelivered");
            redvd.setProperty_type(Property.BOOLEAN_TYPE);
            redvd.setProperty_value(redelivered);
            headers.add(redvd);
            long jtimestamp = message.getJMSTimestamp();
            Property jtimestampp = new Property();
            jtimestampp.setKey("JMSTimestamp");
            jtimestampp.setProperty_type(Property.LONG_TYPE);
            jtimestampp.setProperty_value(jtimestamp);
            headers.add(jtimestampp);
        } catch (JMSException jmse) {
            jmse.printStackTrace();
        }
    }

    void collectJMSHeadersFromLMC() {
        try {
            String msgid = lmc.getVmsgid();
            Property msgidp = new Property();
            msgidp.setKey("JMSMessageID");
            msgidp.setProperty_type(Property.STRING_TYPE);
            msgidp.setProperty_value(msgid);
            headers.add(msgidp);
            Destination d1 = lmc.getVdest();
            String s1 = null;
            if (d1 != null) {
                if (d1 instanceof Queue) {
                    s1 = ((Queue) d1).getQueueName() + QBrowserV2.QUEUE_SUFFIX;
                } else {
                    s1 = ((Topic) d1).getTopicName() + QBrowserV2.TOPIC_SUFFIX;
                }
            } else {
                s1 = "";
            }
            Property jdestp = new Property();
            jdestp.setKey("JMSDestination");
            jdestp.setProperty_type(Property.STRING_TYPE);
            jdestp.setProperty_value(s1);
            headers.add(jdestp);
            Destination d = lmc.getVreplyto();
            String s = null;
            if (d != null) {
                if (d instanceof Queue) {
                    s = ((Queue) d).getQueueName() + QBrowserV2.QUEUE_SUFFIX;
                } else {
                    s = ((Topic) d).getTopicName() + QBrowserV2.TOPIC_SUFFIX;
                }
            } else {
                s = "";
            }
            Property jrepto = new Property();
            jrepto.setKey("JMSReplyTo");
            jrepto.setProperty_type(Property.STRING_TYPE);
            jrepto.setProperty_value(s);
            headers.add(jrepto);
            String jcorid = lmc.getVcorrelationid();
            Property jcoridp = new Property();
            jcoridp.setKey("JMSCorrelationID");
            jcoridp.setProperty_type(Property.STRING_TYPE);
            jcoridp.setProperty_value(jcorid);
            headers.add(jcoridp);
            int delivermode = lmc.getVdeliverymode();
            Property delivermodep = new Property();
            delivermodep.setKey("JMSDeliverMode");
            delivermodep.setProperty_type(Property.INT_TYPE);
            delivermodep.setProperty_value(delivermode);
            headers.add(delivermodep);
            int jpri = lmc.getVpriority();
            Property jprip = new Property();
            jprip.setKey("JMSPriority");
            jprip.setProperty_type(Property.INT_TYPE);
            jprip.setProperty_value(jpri);
            headers.add(jprip);
            long jexp = lmc.getVexpiration();
            Property jexp_prop = new Property();
            jexp_prop.setKey("JMSExpiration");
            jexp_prop.setProperty_type(Property.LONG_TYPE);
            jexp_prop.setProperty_value(jexp);
            headers.add(jexp_prop);
            String jtype = lmc.getVjms_type();
            Property jtypep = new Property();
            jtypep.setKey("JMSType");
            jtypep.setProperty_type(Property.STRING_TYPE);
            jtypep.setProperty_value(jtype);
            headers.add(jtypep);
            boolean redelivered = lmc.isVredelivered();
            Property redvd = new Property();
            redvd.setKey("JMSRedelivered");
            redvd.setProperty_type(Property.BOOLEAN_TYPE);
            redvd.setProperty_value(redelivered);
            headers.add(redvd);
            long jtimestamp = lmc.getVtimestamp();
            Property jtimestampp = new Property();
            jtimestampp.setKey("JMSTimestamp");
            jtimestampp.setProperty_type(Property.LONG_TYPE);
            jtimestampp.setProperty_value(jtimestamp);
            headers.add(jtimestampp);
            ArrayList aps = lmc.getAdditionalHeaders();
            for (int i = 0; i < aps.size(); i++) {
                headers.add((Property) aps.get(i));
            }
        } catch (JMSException jmse) {
            jmse.printStackTrace();
        }
    }

    void collectProperties() {
        try {
            for (Enumeration enu = message.getPropertyNames(); enu.hasMoreElements(); ) {
                String name = (enu.nextElement()).toString();
                Object propvalueobj = message.getObjectProperty(name);
                Property prop = new Property();
                prop.setKey(name);
                prop.setProperty_value(propvalueobj);
                prop.autoComplementTypeNme();
                properties.add(prop);
            }
        } catch (JMSException jmse) {
            jmse.printStackTrace();
        }
    }

    void stringPropertyToFile(File efile, String string_property) {
        ByteArrayInputStream bis = null;
        java.io.FileOutputStream fo = null;
        try {
            byte[] bibi = new byte[1024];
            if (!efile.getParentFile().exists()) {
                efile.getParentFile().mkdirs();
            }
            bis = new ByteArrayInputStream(string_property.getBytes());
            fo = new FileOutputStream(efile);
            int len = 0;
            long readfilesize = 0;
            int count = 0;
            while ((len = bis.read(bibi, 0, bibi.length)) != -1) {
                fo.write(bibi, 0, len);
                readfilesize += len;
                if (++count > 100) {
                    sbuf.append(readfilesize + " " + resources.getString("qkey.msg.msg098") + string_property.length() + " " + resources.getString("qkey.msg.msg099") + "\n");
                    count = 0;
                }
            }
            if (count != 0) {
                sbuf.append(readfilesize + " " + resources.getString("qkey.msg.msg098") + string_property.length() + " " + resources.getString("qkey.msg.msg099") + "\n");
            }
        } catch (Throwable thex) {
            thex.printStackTrace();
        } finally {
            if (fo != null) {
                try {
                    fo.close();
                } catch (IOException ioe) {
                }
                fo = null;
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioe) {
                }
                bis = null;
            }
        }
    }

    void intPropertyToFile(File efile, Integer int_property) {
        if (!efile.getParentFile().exists()) {
            efile.getParentFile().mkdirs();
        }
        PrintWriter pwr = null;
        try {
            pwr = new PrintWriter(new FileWriter(efile));
            pwr.print(int_property);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pwr != null) {
                pwr.close();
                pwr = null;
            }
        }
    }

    void bytePropertyToFile(File efile, Byte byte_property) {
        if (!efile.getParentFile().exists()) {
            efile.getParentFile().mkdirs();
        }
        PrintWriter pwr = null;
        try {
            pwr = new PrintWriter(new FileWriter(efile));
            pwr.print(byte_property);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pwr != null) {
                pwr.close();
                pwr = null;
            }
        }
    }

    void shortPropertyToFile(File efile, Short short_property) {
        if (!efile.getParentFile().exists()) {
            efile.getParentFile().mkdirs();
        }
        PrintWriter pwr = null;
        try {
            pwr = new PrintWriter(new FileWriter(efile));
            pwr.print(short_property);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pwr != null) {
                pwr.close();
                pwr = null;
            }
        }
    }

    void longPropertyToFile(File efile, Long long_property) {
        if (!efile.getParentFile().exists()) {
            efile.getParentFile().mkdirs();
        }
        PrintWriter pwr = null;
        try {
            pwr = new PrintWriter(new FileWriter(efile));
            pwr.print(long_property);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pwr != null) {
                pwr.close();
                pwr = null;
            }
        }
    }

    void booleanPropertyToFile(File efile, Boolean boolean_property) {
        if (!efile.getParentFile().exists()) {
            efile.getParentFile().mkdirs();
        }
        PrintWriter pwr = null;
        try {
            pwr = new PrintWriter(new FileWriter(efile));
            pwr.print(boolean_property);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pwr != null) {
                pwr.close();
                pwr = null;
            }
        }
    }

    void floatPropertyToFile(File efile, Float float_property) {
        if (!efile.getParentFile().exists()) {
            efile.getParentFile().mkdirs();
        }
        PrintWriter pwr = null;
        try {
            pwr = new PrintWriter(new FileWriter(efile));
            pwr.print(float_property);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pwr != null) {
                pwr.close();
                pwr = null;
            }
        }
    }

    void charPropertyToFile(File efile, Character char_property) {
        if (!efile.getParentFile().exists()) {
            efile.getParentFile().mkdirs();
        }
        PrintWriter pwr = null;
        try {
            pwr = new PrintWriter(new FileWriter(efile));
            pwr.print(char_property);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pwr != null) {
                pwr.close();
                pwr = null;
            }
        }
    }

    void doublePropertyToFile(File efile, double double_property) {
        if (!efile.getParentFile().exists()) {
            efile.getParentFile().mkdirs();
        }
        PrintWriter pwr = null;
        try {
            pwr = new PrintWriter(new FileWriter(efile));
            pwr.print(double_property);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pwr != null) {
                pwr.close();
                pwr = null;
            }
        }
    }

    void bytesPropertyToFile(File efile, byte[] bytes_property) {
        if (!efile.getParentFile().exists()) {
            efile.getParentFile().mkdirs();
        }
        ByteArrayInputStream bis = null;
        java.io.FileOutputStream fo = null;
        try {
            byte[] bibi = new byte[1024];
            bis = new ByteArrayInputStream(bytes_property);
            fo = new FileOutputStream(efile);
            int len = 0;
            long readfilesize = 0;
            int count = 0;
            while ((len = bis.read(bibi, 0, bibi.length)) != -1) {
                fo.write(bibi, 0, len);
                readfilesize += len;
                if (++count > 100) {
                    sbuf.append(readfilesize + " " + resources.getString("qkey.msg.msg098") + bytes_property.length + " " + resources.getString("qkey.msg.msg099") + "\n");
                    count = 0;
                }
            }
            if (count != 0) {
                sbuf.append(readfilesize + " " + resources.getString("qkey.msg.msg098") + bytes_property.length + " " + resources.getString("qkey.msg.msg099") + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fo != null) {
                try {
                    fo.close();
                } catch (IOException ioe) {
                }
                fo = null;
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioe) {
                }
                bis = null;
            }
        }
    }
}
