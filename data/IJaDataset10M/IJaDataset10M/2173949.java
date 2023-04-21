package fr.loria.ecoo.pbcast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Set;

public class Log {

    private Hashtable<Object, Object> log;

    private String logFilePath;

    public Log(String logFilePath) throws Exception {
        this.logFilePath = logFilePath;
        this.loadLog();
    }

    public synchronized void addMessage(Message message) throws Exception {
        this.loadLog();
        this.log.put(message.getId(), message);
        this.storeLog();
    }

    public void clearLog() throws Exception {
        this.log = new Hashtable<Object, Object>();
        this.storeLog();
    }

    public int logSize() throws Exception {
        this.loadLog();
        return this.log.size();
    }

    public Message getMessage(Object messageId) throws Exception {
        this.loadLog();
        return (Message) this.log.get(messageId);
    }

    public Object[] getMessagesId() throws Exception {
        this.loadLog();
        return this.log.keySet().toArray();
    }

    public boolean existInLog(Object messageId) throws Exception {
        this.loadLog();
        return this.log.containsKey(messageId);
    }

    public Object[] getDiff(Object[] messagesId) throws Exception {
        this.loadLog();
        Set diff = this.log.keySet();
        for (int i = 0; i < messagesId.length; i++) {
            Object id = messagesId[i];
            if (this.log.keySet().contains(id)) {
                diff.remove(id);
            }
        }
        return diff.toArray();
    }

    private void loadLog() throws Exception {
        File logFile = new File(logFilePath);
        ObjectInputStream ois = null;
        if (!logFile.exists()) {
            this.log = new Hashtable<Object, Object>();
            this.storeLog();
        } else {
            ois = new ObjectInputStream(new FileInputStream(logFile));
            Hashtable<Object, Object> readObject = (Hashtable<Object, Object>) ois.readObject();
            this.log = readObject;
            ois.close();
        }
    }

    private void storeLog() throws Exception {
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        fout = new FileOutputStream(logFilePath);
        oos = new ObjectOutputStream(fout);
        oos.writeObject(log);
        oos.flush();
        fout.close();
    }
}
