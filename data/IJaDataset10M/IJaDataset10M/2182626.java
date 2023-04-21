package com.vmix.simplemq.daemon.handlers;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.simpleframework.xml.*;
import org.simpleframework.xml.load.*;
import com.vmix.simplemq.daemon.ConfigurationManager;
import com.vmix.simplemq.daemon.Message;
import com.vmix.simplemq.daemon.config.QueueConfig;

public class XmlMessagePersister implements MessagePersister {

    private static Logger logger;

    private static final String PATTERN = "msg_(\\d+)\\.xml";

    private static final DecimalFormat ZEROPAD = new DecimalFormat("000000000");

    private long currentId = 0;

    private QueueConfig config;

    private String baseDir;

    private Serializer serializer = new Persister();

    public XmlMessagePersister() {
        if (logger == null) {
            logger = Logger.getLogger(getClass());
        }
    }

    public void initialize(QueueConfig config, Queue<Long> queue) throws MessagePersistenceException {
        this.config = config;
        File destPath = new File(ConfigurationManager.getDataPath("queues/" + ConfigurationManager.getDaemonGuid() + "/" + this.config.name));
        destPath.mkdirs();
        this.baseDir = destPath.getAbsolutePath();
        logger.debug("initializing repository: " + this.baseDir);
        currentId = 0;
        Pattern p = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE);
        ArrayList<Long> idList = new ArrayList<Long>();
        for (File millions : destPath.listFiles()) {
            if (millions.isDirectory()) {
                int directoriesWithFiles = 0;
                for (File thousands : millions.listFiles()) {
                    if (thousands.isDirectory()) {
                        boolean foundFiles = false;
                        for (File file : thousands.listFiles()) {
                            foundFiles = true;
                            Matcher m = p.matcher(file.getName());
                            if (m.find()) {
                                long id = Long.parseLong(m.group(1));
                                if (id > currentId) {
                                    currentId = id;
                                }
                                idList.add(id);
                            }
                        }
                        if (foundFiles) {
                            directoriesWithFiles++;
                        } else {
                            logger.debug(thousands.toString() + " is empty, trying to delete");
                            if (!thousands.delete()) {
                                logger.debug("unable to delete");
                                directoriesWithFiles++;
                            }
                        }
                    }
                }
                if (directoriesWithFiles == 0) {
                    logger.debug(millions.toString() + " is empty, trying to delete");
                    if (!millions.delete()) {
                        logger.debug("unable to delete");
                    }
                }
            }
        }
        logger.debug("sequence start: " + currentId);
        logger.debug("Queue size: " + idList.size());
        Collections.sort(idList);
        for (long id : idList) {
            queue.add(id);
        }
        logger.debug("built queue");
    }

    public void dispose() {
    }

    public void delete(long messageId) throws MessagePersistenceException {
        File messageFile = getFileForId(messageId, false);
        if (!messageFile.exists()) {
            throw new MessagePersistenceException("No file for messageId " + messageId);
        }
        messageFile.delete();
        reapEmptyDir(messageId);
    }

    public Message get(long messageId) throws MessagePersistenceException {
        File messageFile = getFileForId(messageId, false);
        if (!messageFile.exists()) {
            throw new MessagePersistenceException("No file for messageId " + messageId);
        }
        try {
            return serializer.read(Message.class, messageFile);
        } catch (Exception e) {
            logger.error("couldn't deserialize message", e);
            if (!messageFile.renameTo(getFileForId(messageId, true))) {
                logger.error("unable to create backup of bad file, deleting instead)");
                messageFile.delete();
            }
            throw new MessagePersistenceException("Unable to deserialize message");
        }
    }

    public long store(Message message) throws MessagePersistenceException {
        long id = 0;
        synchronized (this) {
            currentId++;
            id = currentId;
        }
        File f = null;
        try {
            f = getFileForId(id, false);
            serializer.write(message, f);
            Message check = get(id);
            if (!check.guid().equals(message.guid())) {
                logger.warn("the persisted message did not match the original");
                throw new MessagePersistenceException("unable to serialize message");
            }
            return id;
        } catch (Exception e) {
            if (f != null) {
                logger.error("couldn't serialize message to " + f.getAbsolutePath(), e);
            } else {
                logger.error("couldn't serialize message and didn't get a file for id " + id, e);
            }
            throw new MessagePersistenceException("unable to serialize message");
        }
    }

    private File getFileForId(long id, boolean bad) {
        String idString = ZEROPAD.format(id);
        String directory = baseDir + "/" + idString.substring(0, 3) + "/" + idString.substring(3, 6) + "/";
        new File(directory).mkdirs();
        String filename = directory + "msg_" + idString + ((bad) ? ".bad" : ".xml");
        return new File(filename);
    }

    private void reapEmptyDir(long id) {
        long last3digits = id - (id / 1000) * 1000;
        if (last3digits != 999) return;
        logger.debug("dealing with last message for directory, let's see about wiping the dir");
        String idString = ZEROPAD.format(id);
        String directory = baseDir + "/" + idString.substring(0, 3) + "/" + idString.substring(3, 6) + "/";
        File dir = new File(directory);
        if (dir.exists() && dir.list().length == 0) {
            logger.debug(dir.toString() + " is empty, trying delete");
            if (!dir.delete()) {
                logger.debug("unable to delete");
            }
            File parent = new File(baseDir + "/" + idString.substring(0, 3));
            if (parent.exists() && parent.list().length == 0) {
                logger.debug(parent.toString() + " is empty, trying delete");
                if (!parent.delete()) {
                    logger.debug("unable to delete");
                }
            }
        }
    }
}
