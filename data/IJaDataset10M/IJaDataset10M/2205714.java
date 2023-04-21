package org.wijiscommons.ssaf.consumers.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.wijiscommons.ssaf.consumers.SSAFConsumer;
import org.wijiscommons.ssaf.exception.DropoffMailBoxException;
import org.wijiscommons.ssaf.message.Message;
import org.wijiscommons.ssaf.message.impl.MessageImpl;
import org.wijiscommons.ssaf.process.dropoff.SSAFMessageReceivalProcess;
import org.wijiscommons.ssaf.process.dropoff.impl.MessageReceivalImpl;
import org.wijiscommons.ssaf.utility.IDGeneratorUtility;
import org.wijiscommons.ssaf.utility.XMLToStringConversionUtility;

public class FolderReceiverConsumer implements SSAFConsumer {

    private String folderName;

    public FolderReceiverConsumer(String folderName) {
        this.folderName = folderName;
    }

    public void run() {
        startProcessing();
    }

    public void startProcessing() {
        Map<String, Long> currentFiles = takeSnapshot();
        if (currentFiles != null) {
            Set<String> fileEnum = currentFiles.keySet();
            Iterator<String> iter = fileEnum.iterator();
            while (iter.hasNext()) {
                String fileName = (String) iter.next();
                String messageID = IDGeneratorUtility.getNextMessageID();
                Message message = new MessageImpl();
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(currentFiles.get(fileName));
                message.setFolderArrivalTimestamp(cal);
                message.setFolderSource(folderName);
                message.setIncomingMessageXML(XMLToStringConversionUtility.convertXMLFileToString(fileName));
                message.setMessageID(messageID);
                try {
                    SSAFMessageReceivalProcess mr = new MessageReceivalImpl();
                    boolean isExecuted = mr.receiveMessage(message);
                    if (isExecuted) {
                        System.out.println("Executed incoming message " + messageID + " without any errors");
                        copyFile(fileName, messageID, false);
                    }
                } catch (DropoffMailBoxException mbe) {
                    copyFile(fileName, mbe.getMessageID(), true);
                    System.out.println("Error occured for incoming message " + messageID);
                } finally {
                    System.out.println("Deleting file for incoming message " + messageID);
                    deleteFile(fileName);
                }
            }
        }
    }

    private void copyFile(String fileName, String messageID, boolean isError) {
        try {
            File inputFile = new File(fileName);
            File outputFile = null;
            if (isError) {
                outputFile = new File(provider.getErrorDataLocation(folderName) + messageID + ".xml");
            } else {
                outputFile = new File(provider.getDataProcessedLocation(folderName) + messageID + ".xml");
            }
            FileReader in = new FileReader(inputFile);
            FileWriter out = new FileWriter(outputFile);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            in.close();
            out.close();
        } catch (Exception e) {
        }
    }

    private boolean deleteFile(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) throw new IllegalArgumentException("Delete: no such file or directory: " + fileName);
        if (!f.canWrite()) throw new IllegalArgumentException("Delete: write protected: " + fileName);
        boolean success = false;
        if (f.isFile()) {
            success = f.delete();
        }
        return success;
    }

    private LinkedHashMap<String, Long> takeSnapshot() {
        HashMap<String, Long> currentFiles = new HashMap<String, Long>();
        File theDirectory = new File(provider.getDataLocation(folderName));
        File[] children = theDirectory.listFiles();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                File file = children[i];
                if (file.isFile()) {
                    String path = file.getAbsolutePath();
                    String aPath = path.toUpperCase();
                    if (aPath.contains(".XML")) {
                        currentFiles.put(path, new Long(file.lastModified()));
                    }
                }
            }
            return sortHashMapByValuesDuplicate(currentFiles);
        } else {
            return null;
        }
    }

    public LinkedHashMap<String, Long> sortHashMapByValuesDuplicate(HashMap<String, Long> passedMap) {
        List<String> mapKeys = new ArrayList<String>(passedMap.keySet());
        List<Long> mapValues = new ArrayList<Long>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);
        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<String, Long>();
        Iterator<Long> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();
            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();
                if (comp1.equals(comp2)) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String) key, (Long) val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    /**
	 * @return the folderName
	 */
    public String getFolderName() {
        return folderName;
    }

    /**
	 * @param folderName the folderName to set
	 */
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
