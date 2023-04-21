package org.verus.ngl.web.administration.technicalprocessing;

import java.util.StringTokenizer;
import org.verus.ngl.sl.utilities.*;
import java.io.*;
import org.verus.ngl.utilities.NGLUtility;
import java.util.*;
import org.verus.ngl.utilities.logging.NGLLogging;

/**
 *
 * @author root
 */
public class MARCDictionaryHandler implements org.verus.ngl.web.master.FileUploadDownLoadHandler {

    private NewGenLibRootImpl nglRoot = null;

    private NGLUtility utility = null;

    private NewGenLibRoot newGenLibRoot = null;

    /** Creates a new instance of MARCDictionaryHandler */
    public MARCDictionaryHandler() {
        try {
            utility = NGLUtility.getInstance();
            newGenLibRoot = (NewGenLibRoot) NGLBeanFactory.getInstance().getBean("newGenLibRoot");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object processRequest(Object xmlStr) {
        java.util.Vector vect = new java.util.Vector(1, 1);
        try {
            System.out.println("##########This is in server####################");
            NGLLogging.getFineLogger().fine("This is in server####################");
            String xmlReq = xmlStr.toString();
            org.jdom.input.SAXBuilder sax = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc = sax.build(new java.io.StringReader(xmlReq));
            org.jdom.Element rootEle = doc.getRootElement();
            String processId = rootEle.getAttributeValue("process");
            if (processId.equals("1")) {
                String filepath = rootEle.getChildText("FileName");
                StringTokenizer st = new StringTokenizer(filepath, "-");
                String locale = "", filename = "";
                while (st.hasMoreTokens()) {
                    filename = st.nextToken();
                    locale = st.nextToken();
                }
                NGLLogging.getFineLogger().fine("The locale in server is--------->" + locale);
                String localeType = rootEle.getChildText("locale");
                String dateVal = rootEle.getChildText("Date");
                String libId = rootEle.getChildText("LibraryId");
                NGLLogging.getFineLogger().fine("The lib Id in server is------------>" + libId);
                String root = newGenLibRoot.getRoot();
                String fileName = root + "/MarcDictionary/" + filename + locale + ".xml";
                String defaultFileName = root + "/MarcDictionary/" + filename + ".xml";
                NGLLogging.getFineLogger().fine("The file one is---------->" + fileName);
                NGLLogging.getFineLogger().fine("The file two is---------->" + defaultFileName);
                File f = new File(fileName);
                File f1 = new File(defaultFileName);
                NGLLogging.getFineLogger().fine("This is in server and checking for file ---------------->");
                if (dateVal.equals("")) {
                    if (f.exists()) {
                        NGLLogging.getFineLogger().fine("The file exists -in server -which is locale--------->" + filename + locale + ".xml");
                        java.util.Date dateServer = new java.util.Date(f.lastModified());
                        vect.add(filename + locale + ".xml");
                        NGLLogging.getFineLogger().fine("The added first value is------in vector------>" + filename + locale + ".xml");
                        try {
                            java.nio.channels.FileChannel fc = (new java.io.FileInputStream(f)).getChannel();
                            int fileLength = (int) fc.size();
                            java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                            byte[] byx = new byte[bb.capacity()];
                            fc.close();
                            bb.get(byx);
                            NGLLogging.getFineLogger().fine("byx.length" + byx.length);
                            String xml = new String(byx);
                            NGLLogging.getFineLogger().fine("The added second value is------in vector------>");
                            vect.add("A");
                            vect.addElement(xml);
                            vect.add(new java.util.Date(f.lastModified()));
                        } catch (Exception e) {
                            vect.add("A");
                        }
                    } else if (f1.exists()) {
                        NGLLogging.getFineLogger().fine("The file exists -in server -which is default--------->" + filename + ".xml");
                        java.util.Date dateServer = new java.util.Date(f1.lastModified());
                        vect.add(filename + ".xml");
                        NGLLogging.getFineLogger().fine("The added first value is------in vector------>" + filename + ".xml");
                        try {
                            java.nio.channels.FileChannel fc = (new java.io.FileInputStream(f1)).getChannel();
                            int fileLength = (int) fc.size();
                            java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                            byte[] byx = new byte[bb.capacity()];
                            fc.close();
                            bb.get(byx);
                            NGLLogging.getFineLogger().fine("byx.length" + byx.length);
                            String xml = new String(byx);
                            vect.add("A");
                            vect.addElement(xml);
                            vect.add(new java.util.Date(f1.lastModified()));
                        } catch (Exception e) {
                            vect.add("A");
                        }
                    } else {
                        NGLLogging.getFineLogger().fine("This is in server else file not found-------->");
                    }
                } else {
                    if (localeType.equalsIgnoreCase("true")) {
                        if (f.exists()) {
                            int res = 0;
                            java.util.Date dateClient = new java.util.Date(new Long(dateVal).longValue());
                            java.util.Date dateServer = new java.util.Date(f.lastModified());
                            res = dateClient.compareTo(dateServer);
                            if (res < 0) {
                                vect.add(filename + locale + ".xml");
                                NGLLogging.getFineLogger().fine("The added first value is------in vector------>" + filename + locale + ".xml");
                                try {
                                    java.nio.channels.FileChannel fc = (new java.io.FileInputStream(f)).getChannel();
                                    int fileLength = (int) fc.size();
                                    java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                                    byte[] byx = new byte[bb.capacity()];
                                    fc.close();
                                    bb.get(byx);
                                    NGLLogging.getFineLogger().fine("byx.length" + byx.length);
                                    String xml = new String(byx);
                                    NGLLogging.getFineLogger().fine("The added second value is------in vector------>");
                                    vect.add("A");
                                    vect.addElement(xml);
                                    vect.add(new java.util.Date(f.lastModified()));
                                } catch (Exception e) {
                                    vect.add("A");
                                }
                            } else {
                                vect.add(filename + locale + ".xml");
                                vect.add("B");
                            }
                        } else {
                            vect.add(filename + locale + ".xml");
                            vect.add("B");
                        }
                    } else {
                        if (f1.exists()) {
                            int res = 0;
                            java.util.Date dateClient = new java.util.Date(new Long(dateVal).longValue());
                            java.util.Date dateServer = new java.util.Date(f1.lastModified());
                            res = dateClient.compareTo(dateServer);
                            if (res < 0) {
                                vect.add(filename + ".xml");
                                NGLLogging.getFineLogger().fine("The added first value is------in vector------>" + filename + ".xml");
                                try {
                                    java.nio.channels.FileChannel fc = (new java.io.FileInputStream(f1)).getChannel();
                                    int fileLength = (int) fc.size();
                                    java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                                    byte[] byx = new byte[bb.capacity()];
                                    fc.close();
                                    bb.get(byx);
                                    NGLLogging.getFineLogger().fine("byx.length" + byx.length);
                                    String xml = new String(byx);
                                    NGLLogging.getFineLogger().fine("The added second value is------in vector------>");
                                    vect.add("A");
                                    vect.addElement(xml);
                                    vect.add(new java.util.Date(f1.lastModified()));
                                } catch (Exception e) {
                                    vect.add("A");
                                }
                            } else {
                                vect.add(filename + ".xml");
                                vect.add("B");
                            }
                        } else {
                            vect.add(filename + ".xml");
                            vect.add("B");
                        }
                    }
                }
            } else if (processId.equals("2")) {
                String locale = rootEle.getChildText("FileName");
                NGLLogging.getFineLogger().fine("The locale in server is--------->" + locale);
                String libId = rootEle.getChildText("LibraryId");
                NGLLogging.getFineLogger().fine("The lib Id in server is------------>" + libId);
                String root = newGenLibRoot.getRoot();
                String fileName = root + "/MarcDictionary/ControlFields" + locale + ".xml";
                String defaultFileName = root + "/MarcDictionary/ControlFields.xml";
                NGLLogging.getFineLogger().fine("The file one is---------->" + fileName);
                NGLLogging.getFineLogger().fine("The file two is---------->" + defaultFileName);
                File f = new File(fileName);
                File f1 = new File(defaultFileName);
                NGLLogging.getFineLogger().fine("This is in server and checking for file ---------------->");
                if (f.exists()) {
                    NGLLogging.getFineLogger().fine("The file exists -in server -which is locale--------->" + "ControlFields" + locale + ".xml");
                    vect.add("ControlFields" + locale + ".xml");
                    NGLLogging.getFineLogger().fine("The added first value is------in vector------>" + "ControlFields" + locale + ".xml");
                    try {
                        java.nio.channels.FileChannel fc = (new java.io.FileInputStream(f)).getChannel();
                        int fileLength = (int) fc.size();
                        java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                        byte[] byx = new byte[bb.capacity()];
                        fc.close();
                        bb.get(byx);
                        NGLLogging.getFineLogger().fine("byx.length" + byx.length);
                        String xml = new String(byx);
                        NGLLogging.getFineLogger().fine("The added second value is------in vector------>");
                        vect.addElement(xml);
                        vect.add(new java.util.Date(f.lastModified()));
                    } catch (Exception e) {
                    }
                } else if (f1.exists()) {
                    NGLLogging.getFineLogger().fine("The file exists -in server -which is default--------->" + "ControlFields.xml");
                    vect.add("ControlFields.xml");
                    NGLLogging.getFineLogger().fine("The added first value is------in vector------>" + "ControlFields.xml");
                    try {
                        java.nio.channels.FileChannel fc = (new java.io.FileInputStream(f1)).getChannel();
                        int fileLength = (int) fc.size();
                        java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                        byte[] byx = new byte[bb.capacity()];
                        fc.close();
                        bb.get(byx);
                        NGLLogging.getFineLogger().fine("byx.length" + byx.length);
                        String xml = new String(byx);
                        vect.addElement(xml);
                        vect.add(new java.util.Date(f1.lastModified()));
                    } catch (Exception e) {
                    }
                } else {
                    NGLLogging.getFineLogger().fine("This is in server else file not found-------->");
                }
            } else if (processId.equals("4")) {
                java.util.Vector fileInfo = new java.util.Vector();
                String root = newGenLibRoot.getRoot();
                String filePath = root + "/Punctuation/PunctuationRules.xml";
                File file1 = new File(filePath);
                if (file1.exists()) {
                    try {
                        java.nio.channels.FileChannel fileChannel = (new java.io.FileInputStream(file1)).getChannel();
                        int fileLength = (int) fileChannel.size();
                        java.nio.MappedByteBuffer mapBytebuff = fileChannel.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                        byte[] byt = new byte[mapBytebuff.capacity()];
                        fileChannel.close();
                        mapBytebuff.get(byt);
                        String xml = new String(byt);
                        fileInfo.addElement(xml);
                        vect = fileInfo;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (processId.equals("3")) {
                String filepath = rootEle.getChildText("FileName");
                StringTokenizer st = new StringTokenizer(filepath, "-");
                String locale = "", filename = "";
                while (st.hasMoreTokens()) {
                    filename = st.nextToken();
                    locale = st.nextToken();
                }
                NGLLogging.getFineLogger().fine("The locale in server is--------->" + locale);
                String libId = rootEle.getChildText("LibraryId");
                NGLLogging.getFineLogger().fine("The lib Id in server is------------>" + libId);
                String root = newGenLibRoot.getRoot();
                String fileName = root + "/MarcDictionary/" + filename + locale + ".xml";
                String defaultFileName = root + "/MarcDictionary/" + filename + ".xml";
                NGLLogging.getFineLogger().fine("The file one is---------->" + fileName);
                NGLLogging.getFineLogger().fine("The file two is---------->" + defaultFileName);
                File f = new File(fileName);
                File f1 = new File(defaultFileName);
                NGLLogging.getFineLogger().fine("This is in server and checking for file ---------------->");
                if (f.exists()) {
                    NGLLogging.getFineLogger().fine("The file exists -in server -which is locale--------->" + filename + locale + ".xml");
                    vect.add(filename + locale + ".xml");
                    NGLLogging.getFineLogger().fine("The added first value is------in vector------>" + filename + locale + ".xml");
                    try {
                        java.nio.channels.FileChannel fc = (new java.io.FileInputStream(f)).getChannel();
                        int fileLength = (int) fc.size();
                        java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                        byte[] byx = new byte[bb.capacity()];
                        fc.close();
                        bb.get(byx);
                        NGLLogging.getFineLogger().fine("byx.length" + byx.length);
                        String xml = new String(byx);
                        NGLLogging.getFineLogger().fine("The added second value is------in vector------>");
                        vect.addElement(xml);
                    } catch (Exception e) {
                    }
                } else if (f1.exists()) {
                    NGLLogging.getFineLogger().fine("The file exists -in server -which is default--------->" + filename + ".xml");
                    vect.add(filename + ".xml");
                    NGLLogging.getFineLogger().fine("The added first value is------in vector------>" + filename + ".xml");
                    try {
                        java.nio.channels.FileChannel fc = (new java.io.FileInputStream(f1)).getChannel();
                        int fileLength = (int) fc.size();
                        java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                        byte[] byx = new byte[bb.capacity()];
                        fc.close();
                        bb.get(byx);
                        NGLLogging.getFineLogger().fine("byx.length" + byx.length);
                        String xml = new String(byx);
                        vect.addElement(xml);
                    } catch (Exception e) {
                    }
                } else {
                    NGLLogging.getFineLogger().fine("This is in server else file not found-------->");
                }
            } else if (processId.equals("5")) {
                String fileName = utility.getTestedString(rootEle.getChildText("FileName"));
                String lastModified = utility.getTestedString(rootEle.getChildText("LastModified"));
                NGLLogging.getFineLogger().fine("LastModified time of the Local file : " + lastModified);
                if (!fileName.equals("")) {
                    String root = newGenLibRoot.getRoot();
                    String serverPath = root + "/DisplayProfiles/" + fileName + ".xml";
                    File serverFile = new File(serverPath);
                    if (serverFile.exists()) {
                        try {
                            long localLM = Long.parseLong(lastModified);
                            NGLLogging.getFineLogger().fine("Last modified date of the server file : " + serverFile.lastModified());
                            if (serverFile.lastModified() > localLM) {
                                java.nio.channels.FileChannel fc = (new java.io.FileInputStream(serverFile)).getChannel();
                                int fileLength = (int) fc.size();
                                java.nio.MappedByteBuffer bb = fc.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0, fileLength);
                                byte[] byx = new byte[bb.capacity()];
                                fc.close();
                                bb.get(byx);
                                NGLLogging.getFineLogger().fine("byx.length" + byx.length);
                                String content = new String(byx);
                                NGLLogging.getFineLogger().fine("The added second value is------in vector------>");
                                vect.addElement(content);
                            }
                        } catch (Exception e) {
                            NGLLogging.getFineLogger().fine("Exception at server side for display profile file");
                            return vect;
                        }
                    }
                    return vect;
                }
            }
            return vect;
        } catch (Exception e) {
            NGLLogging.getFineLogger().fine("This is in server file not found--------->");
            e.printStackTrace();
            return vect;
        }
    }
}
