package com.assistsoft.swift.common;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.xml.sax.SAXException;
import com.assistsoft.swift.annotations.QualifierAfterTag;
import com.assistsoft.swift.annotations.ValidQualifiers;

public class SWIFTMessageTreeBuilder {

    private Map<Integer, SWIFTMessageStructure> messageTypeToStructureMap;

    private SWIFTNode lastFieldNode;

    public SWIFTMessageTreeBuilder() throws ClassNotFoundException, IOException, SAXException, IntrospectionException, SecurityException, NoSuchMethodException {
        messageTypeToStructureMap = new HashMap<Integer, SWIFTMessageStructure>();
        File messageDir = new File("bin/com/assistsoft/swift/message");
        String[] messages = messageDir.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("SWIFTMessage_");
            }
        });
        for (String message : messages) {
            String className = message.substring(0, message.length() - ".class".length());
            int pos = className.lastIndexOf('_');
            int messageType = Integer.parseInt(className.substring(pos + 1));
            SWIFTMessageStructure structure = new SWIFTMessageStructure(messageType);
            messageTypeToStructureMap.put(messageType, structure);
            int serialNum = 1;
            className = "com.assistsoft.swift.message." + className;
            Class clazz = Class.forName(className);
            SWIFTNode root = processClass(clazz, structure, serialNum);
            structure.setRoot(root);
        }
    }

    public SWIFTMessageStructure getMessageStructure(int messageType) {
        SWIFTMessageStructure structure = messageTypeToStructureMap.get(messageType);
        return structure;
    }

    private Method getSetMethodFromField(Field field, Class clazz) throws SecurityException, NoSuchMethodException {
        String methodName = field.getName();
        methodName = "set" + Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
        Method method = clazz.getMethod(methodName, field.getType());
        return method;
    }

    private Method getAddToMethodFromField(Field field, Class clazz, Class itemType) throws SecurityException, NoSuchMethodException {
        String methodName = field.getName();
        methodName = "addTo" + Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
        Method method = clazz.getMethod(methodName, itemType);
        return method;
    }

    private SWIFTNode processClass(Class clazz, SWIFTMessageStructure structure, int serialNum) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        SWIFTNode root = null;
        SWIFTNode last = null;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Class typeClass = field.getType();
            String typeName = field.getType().getName();
            SWIFTNode node = null;
            if (typeName.startsWith("com.assistsoft.swift.sequence.SWIFTSequence_")) {
                Method method = getSetMethodFromField(field, clazz);
                node = new SWIFTNode(field, method, SWIFTNodeTypeEnum.SEQUENCE, null, serialNum);
                serialNum++;
                SWIFTNode childRoot = processClass(typeClass, structure, serialNum);
                node.setChild(childRoot);
                for (SWIFTNode tNode = childRoot; tNode != null; tNode = tNode.getRightSibling()) {
                    tNode.setParent(node);
                }
                if (childRoot.getTags() != null && childRoot.getTags().contains("16R")) {
                    structure.addQualifierToSequence(childRoot.getQualifier(), node);
                }
            } else if (typeName.startsWith("com.assistsoft.swift.field.SWIFTField_")) {
                String vQualifiersStr = null;
                ValidQualifiers vQualifiers = field.getAnnotation(ValidQualifiers.class);
                if (vQualifiers != null) {
                    vQualifiersStr = vQualifiers.value();
                }
                int pos = typeName.lastIndexOf('_');
                String tag = typeName.substring(pos + 1);
                Set<String> tags = processTags(tag);
                Class fieldClass = Class.forName(typeName);
                Annotation qualifierAfterTag = fieldClass.getAnnotation(QualifierAfterTag.class);
                if (qualifierAfterTag != null) {
                    Iterator<String> iterator = tags.iterator();
                    while (iterator.hasNext()) {
                        SWIFTNode.addTagWithQualifierAfterTag(iterator.next());
                    }
                }
                Method method = getSetMethodFromField(field, clazz);
                node = new SWIFTNode(field, method, SWIFTNodeTypeEnum.FIELD, vQualifiersStr, serialNum, tags);
                serialNum++;
                lastFieldNode = node;
            } else if (typeName.equals("java.util.List") && (field.getGenericType() instanceof ParameterizedType)) {
                ParameterizedType type = (ParameterizedType) field.getGenericType();
                Type[] actuals = type.getActualTypeArguments();
                if (actuals.length == 1) {
                    if (actuals[0] instanceof Class) {
                        Class actualClass = (Class) actuals[0];
                        if (actualClass.getName().startsWith("com.assistsoft.swift.field.SWIFTField_")) {
                            String vQualifiersStr = null;
                            ValidQualifiers vQualifiers = field.getAnnotation(ValidQualifiers.class);
                            if (vQualifiers != null) {
                                vQualifiersStr = vQualifiers.value();
                            }
                            Method method = getAddToMethodFromField(field, clazz, actualClass);
                            int pos = actualClass.getName().lastIndexOf('_');
                            String tag = actualClass.getName().substring(pos + 1);
                            Set<String> tags = processTags(tag);
                            node = new SWIFTNode(field, method, SWIFTNodeTypeEnum.LIST, vQualifiersStr, serialNum, tags);
                            serialNum++;
                        } else if (actualClass.getName().startsWith("com.assistsoft.swift.sequence.SWIFTSequence_")) {
                            Method method = getAddToMethodFromField(field, clazz, actualClass);
                            node = new SWIFTNode(field, method, SWIFTNodeTypeEnum.LIST, null, serialNum);
                            serialNum++;
                            SWIFTNode childRoot = processClass((Class) actuals[0], structure, serialNum);
                            node.setChild(childRoot);
                            for (SWIFTNode tNode = childRoot; tNode != null; tNode = tNode.getRightSibling()) {
                                tNode.setParent(node);
                            }
                            if (childRoot.getTags() != null && childRoot.getTags().contains("16R")) {
                                structure.addQualifierToSequence(childRoot.getQualifier(), node);
                            }
                            if (lastFieldNode != null) {
                                lastFieldNode.setLoopback(node);
                                lastFieldNode = null;
                            }
                        }
                    }
                }
            }
            if (root == null) {
                root = node;
            } else {
                last.setRightSibling(node);
                node.setLeftSibling(last);
            }
            last = node;
        }
        return root;
    }

    private Set<String> processTags(String tag) {
        Set<String> tags = new HashSet<String>();
        if (tag.length() == 2) {
            tags.add(tag);
        } else {
            String tagStem = tag.substring(0, 2);
            for (int i = 2; i < tag.length(); i++) {
                String newTag = tagStem + tag.charAt(i);
                tags.add(newTag);
            }
        }
        return tags;
    }

    public static void main(String[] args) {
        try {
            SWIFTMessageTreeBuilder mn = new SWIFTMessageTreeBuilder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
