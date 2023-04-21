package org.shef.clef;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Document;
import java.io.*;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.shef.clef.scorer.Config;
import org.shef.clef.scorer.Annotation;

public class AnnotationParser {

    private static HashMap keyAnnotIds = new HashMap();

    private static HashMap responseAnnotIds = new HashMap();

    private static boolean entitiesExist = false;

    private static boolean relationsExist = false;

    public static LinkedList annotationList(String filename, String keysORresponses) {
        LinkedList list = new LinkedList();
        Annotation annotation = null;
        HashMap<String, String> tokenIdHash = new HashMap<String, String>();
        HashMap<String, Annotation> dummyAnnotsMap = new HashMap<String, Annotation>();
        File file = new File(filename);
        try {
            FileInputStream fstream = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "Latin1"));
            String strLine;
            boolean textWithNodesFlag = false;
            Pattern node = Pattern.compile("<Node id=\"([0-9]+)\" \\/>([^<]+)");
            while ((strLine = br.readLine()) != null) {
                if (strLine.indexOf("<TextWithNodes>") >= 0) {
                    textWithNodesFlag = true;
                }
                if (textWithNodesFlag) {
                    Matcher matchNode = node.matcher(strLine);
                    while (matchNode.find()) {
                        String id = matchNode.group(1);
                        String token = matchNode.group(2);
                        tokenIdHash.put(id, token);
                    }
                }
                if (strLine.indexOf("</TextWithNodes>") >= 0) {
                    textWithNodesFlag = false;
                    break;
                }
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(file);
            Element root = document.getRootElement();
            Element annotationSet = root.getChild("AnnotationSet");
            List annotations = annotationSet.getChildren();
            Iterator it = annotations.iterator();
            boolean clefTermFlag = false;
            while (it.hasNext()) {
                Element ann = (Element) it.next();
                String annType = ann.getAttributeValue("Type");
                if (!(Config.ENTITY_IGNORED_HASH.containsKey(annType)) && !(Config.RELATION_IGNORED_HASH.containsKey(annType))) {
                    String id = null;
                    String hyp = null;
                    if (annType.equals("clefTerm")) {
                        id = ann.getAttributeValue("Id");
                    } else {
                        List features = ann.getChildren();
                        Iterator fit = features.iterator();
                        while (fit.hasNext()) {
                            Element f = (Element) fit.next();
                            Element n = f.getChild("Name");
                            if (annType.equals("TLC")) {
                                if (n.getValue().equals("hypothetical")) {
                                    Element v = f.getChild("Value");
                                    hyp = v.getValue();
                                }
                                if (n.getValue().equals("tlcid") || n.getValue().equals("tlcId")) {
                                    Element v = f.getChild("Value");
                                    id = v.getValue();
                                }
                            } else if (annType.equals("TIMEX3")) {
                                if (n.getValue().equals("tid")) {
                                    Element v = f.getChild("Value");
                                    id = v.getValue();
                                }
                            } else {
                                if (n.getValue().equals("mention_id")) {
                                    Element v = f.getChild("Value");
                                    id = v.getValue();
                                }
                            }
                        }
                    }
                    if (annType.indexOf("clefRelation") == -1 && annType.indexOf("CTLINK") == -1) {
                        String startNode = null;
                        String endNode = null;
                        String annString = null;
                        startNode = ann.getAttributeValue("StartNode");
                        endNode = ann.getAttributeValue("EndNode");
                        int s = Integer.parseInt(startNode.trim());
                        int e = Integer.parseInt(endNode.trim());
                        StringBuffer buffer = new StringBuffer("");
                        for (int i = s; i < e; i++) {
                            String offset = String.valueOf(i);
                            if (tokenIdHash.containsKey(offset)) {
                                String tmp = (String) tokenIdHash.get(offset);
                                buffer.append(tmp);
                            }
                        }
                        annString = buffer.toString();
                        annotation = new Annotation();
                        annotation.setAnnotType("entity");
                        annotation.setEntryLevelID(startNode + "-" + endNode);
                        annotation.setDataID(id);
                        annotation.setAnnotName(annType);
                        annotation.setAnnotValue4Name(annString);
                        if (hyp != null) {
                            annotation.setAttr("hypothetical", hyp);
                        }
                        list.add(annotation);
                        if (keysORresponses.equals("keys")) {
                            keyAnnotIds.put(id, annotation);
                        } else if (keysORresponses.equals("responses")) {
                            responseAnnotIds.put(id, annotation);
                        }
                        entitiesExist = true;
                    } else if (annType.indexOf("clefRelation") >= 0) {
                        String arg1Id = null;
                        String arg2Id = null;
                        String relType = null;
                        List features = ann.getChildren();
                        Iterator fit = features.iterator();
                        while (fit.hasNext()) {
                            Element f = (Element) fit.next();
                            Element n = f.getChild("Name");
                            if (n.getValue().equals("arg1_id")) {
                                Element v = f.getChild("Value");
                                arg1Id = v.getValue();
                            } else if (n.getValue().equals("arg2_id")) {
                                Element v = f.getChild("Value");
                                arg2Id = v.getValue();
                            } else if (n.getValue().equals("relation_id")) {
                                Element v = f.getChild("Value");
                                id = v.getValue();
                            } else if (n.getValue().equals("relation_type")) {
                                Element v = f.getChild("Value");
                                relType = v.getValue();
                            }
                        }
                        annotation = new Annotation();
                        annotation.setAnnotType("relation");
                        annotation.setDataID(id);
                        annotation.setAnnotName(relType);
                        annotation.setAnnotValue4Name("");
                        annotation.setAttr("_arg1_", arg1Id);
                        annotation.setAttrType("_arg1_", "pointer");
                        annotation.setAttr("_arg2_", arg2Id);
                        annotation.setAttrType("_arg2_", "pointer");
                        list.add(annotation);
                        if (keysORresponses.equals("keys")) {
                            keyAnnotIds.put(id, annotation);
                        } else if (keysORresponses.equals("responses")) {
                            responseAnnotIds.put(id, annotation);
                        }
                        relationsExist = true;
                    } else if (annType.indexOf("CTLINK") >= 0) {
                        String arg1Id = null;
                        String arg2Id = null;
                        String ctlinkRelType = null;
                        String taskValue = null;
                        List features = ann.getChildren();
                        Iterator fit = features.iterator();
                        id = ann.getAttributeValue("Id");
                        while (fit.hasNext()) {
                            Element f = (Element) fit.next();
                            Element n = f.getChild("Name");
                            if (n.getValue().equals("relType")) {
                                Element v = f.getChild("Value");
                                ctlinkRelType = v.getValue();
                            } else if (n.getValue().equals("relatedToTime")) {
                                Element v = f.getChild("Value");
                                arg2Id = v.getValue();
                                if (arg2Id.equals("t0")) {
                                    Annotation dummy_annotation = new Annotation();
                                    dummy_annotation.setAnnotType("entity");
                                    dummy_annotation.setAnnotValue4Name(arg2Id);
                                    dummy_annotation.setEntryLevelID("0-0");
                                    dummy_annotation.setDataID(arg2Id);
                                    dummy_annotation.setAnnotName("HYP_TIMEX");
                                    if (keysORresponses.equals("keys")) {
                                        if (!(keyAnnotIds.containsKey(arg2Id))) {
                                            keyAnnotIds.put(arg2Id, dummy_annotation);
                                        }
                                    } else if (keysORresponses.equals("responses")) {
                                        if (!(responseAnnotIds.containsKey(arg2Id))) {
                                            responseAnnotIds.put(arg2Id, dummy_annotation);
                                        }
                                    }
                                    if (!(dummyAnnotsMap.containsKey(arg2Id))) {
                                        list.add(dummy_annotation);
                                        dummyAnnotsMap.put(arg2Id, dummy_annotation);
                                    }
                                }
                            } else if (n.getValue().equals("tlcid") || n.getValue().equals("tlcId")) {
                                Element v = f.getChild("Value");
                                arg1Id = v.getValue();
                            } else if (n.getValue().equals("task")) {
                                Element v = f.getChild("Value");
                                taskValue = v.getValue();
                            }
                        }
                        if (taskValue != null && !(taskValue.equals("C"))) {
                            annotation = new Annotation();
                            annotation.setAnnotType("relation");
                            annotation.setDataID(id);
                            annotation.setAnnotName("CTLINK");
                            annotation.setAnnotValue4Name("");
                            annotation.setAttr("__tlc__", arg1Id);
                            annotation.setAttrType("__tlc__", "pointer");
                            annotation.setAttr("_time_", arg2Id);
                            annotation.setAttrType("_time_", "pointer");
                            annotation.setAttr("reltype", ctlinkRelType);
                            annotation.setAttrType("reltype", "string");
                            list.add(annotation);
                            if (keysORresponses.equals("keys")) {
                                keyAnnotIds.put(id, annotation);
                            } else if (keysORresponses.equals("responses")) {
                                responseAnnotIds.put(id, annotation);
                            }
                            relationsExist = true;
                        }
                    }
                }
            }
        } catch (JDOMException err) {
            System.out.println("Parsing error" + ", line" + err.getLocalizedMessage());
            System.out.println(" " + err.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return list;
    }

    public static HashMap getKeyAnnotIds() {
        return keyAnnotIds;
    }

    public static HashMap getResponseAnnotIds() {
        return responseAnnotIds;
    }

    public static boolean getEntitiesExist() {
        return entitiesExist;
    }

    public static boolean getRelationsExist() {
        return relationsExist;
    }
}
