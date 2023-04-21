package chrriis.udoc.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import chrriis.udoc.model.processor.ClassProcessor;

public class ClassInfoLoader {

    protected static void loadClassInfo(ClassInfo classInfo, ClassProcessor classProcessor) {
        try {
            InputStream in = classProcessor.getClassInfoDataInputStream(classInfo.getClassName());
            if (in == null) {
                classInfo.setModifiers(classInfo.getModifiers() | Modifiers.LOADING_FAILED);
                return;
            }
            loadClassInfo(classInfo, classProcessor, in);
        } catch (Exception e) {
            e.printStackTrace();
            classInfo.setModifiers(classInfo.getModifiers() | Modifiers.LOADING_FAILED);
            return;
        }
        classInfo.setModifiers(classInfo.getModifiers() & ~Modifiers.LOADING_FAILED);
    }

    protected static void loadClassInfo(ClassInfo classInfo, ClassProcessor classProcessor, InputStream in) {
        try {
            if (in == null) {
                return;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(in);
            NodeList nodeList = document.getChildNodes().item(0).getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String name = node.getNodeName();
                if ("prototype".equals(name)) {
                    String prototype = node.getAttributes().getNamedItem("value").getNodeValue();
                    classInfo.setPrototype(prototype);
                    loadPrototypeInfo(classInfo, prototype);
                    break;
                }
            }
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String name = node.getNodeName();
                if (!"prototype".equals(name)) {
                    processClassNode(classInfo, nodeList.item(i), classProcessor);
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void processClassNode(ClassInfo classInfo, Node node, ClassProcessor classProcessor) {
        String name = node.getNodeName();
        if ("superTypes".equals(name)) {
            loadSuperTypes(classInfo, node.getChildNodes(), classProcessor);
        } else if ("subTypes".equals(name)) {
            loadSubTypes(classInfo, node.getChildNodes(), classProcessor);
        } else if ("enums".equals(name)) {
            loadFields(classInfo, node.getChildNodes(), classProcessor, true);
        } else if ("fields".equals(name)) {
            loadFields(classInfo, node.getChildNodes(), classProcessor, false);
        } else if ("methods".equals(name)) {
            loadMethods(classInfo, node.getChildNodes(), classProcessor, METHOD_METHOD_TYPE);
        } else if ("constructors".equals(name)) {
            loadMethods(classInfo, node.getChildNodes(), classProcessor, CONSTRUCTOR_METHOD_TYPE);
        } else if ("annotationMembers".equals(name)) {
            loadMethods(classInfo, node.getChildNodes(), classProcessor, ANNOTATION_METHOD_TYPE);
        }
    }

    protected static void loadPrototypeInfo(ClassInfo classInfo, String prototype) {
        while (prototype.startsWith("@") && !prototype.startsWith("@interface ")) {
            int count = 0;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < prototype.length(); i++) {
                char c = prototype.charAt(i);
                switch(c) {
                    case '(':
                        count++;
                        sb.append(c);
                        break;
                    case ')':
                        count--;
                        sb.append(c);
                        break;
                    case ' ':
                        if (count == 0) {
                            prototype = prototype.substring(i + 1);
                            i = prototype.length();
                        }
                        break;
                    default:
                        sb.append(c);
                        break;
                }
            }
        }
        int modifiers = 0;
        for (int index; (index = prototype.indexOf(' ')) > 0; ) {
            String token = prototype.substring(0, index);
            int modifier = Util.getModifier(token);
            if (modifier < 0) {
                break;
            }
            modifiers |= modifier;
            prototype = prototype.substring(index + 1);
        }
        int count = 0;
        for (int i = 0; i < prototype.length(); i++) {
            char c = prototype.charAt(i);
            switch(c) {
                case '<':
                    count++;
                    break;
                case '>':
                    count--;
                    break;
            }
            String s = prototype.substring(i);
            if (count == 0 && (s.startsWith(" extends ") || s.startsWith(" implements "))) {
                prototype = prototype.substring(0, i).trim();
                break;
            }
        }
        String[] tokens = prototype.split("[<,]");
        Set genericSet = new HashSet();
        for (int i = 1; i < tokens.length; i++) {
            String[] gTokens = tokens[i].split("[ >]");
            for (int j = 0; j < gTokens.length; j++) {
                String s = gTokens[j];
                if (s.length() > 0) {
                    genericSet.add(s);
                    break;
                }
            }
        }
        classInfo.setDeclaration(prototype, (String[]) genericSet.toArray(new String[0]));
        if ((modifiers & (Modifiers.PUBLIC | Modifiers.PROTECTED | Modifiers.PRIVATE)) == 0) {
            modifiers |= Modifiers.DEFAULT;
        }
        classInfo.setModifiers(modifiers);
    }

    protected static void loadSuperTypes(ClassInfo classInfo, NodeList nodeList, ClassProcessor classProcessor) {
        ArrayList list = new ArrayList(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String name = node.getNodeName();
            if ("superType".equals(name)) {
                String superTypePrototype = node.getAttributes().getNamedItem("value").getNodeValue();
                ClassInfo superTypeClassInfo = loadSuperTypeInfo(superTypePrototype, classProcessor);
                if (superTypeClassInfo.getPrototype() == null) {
                    superTypeClassInfo.setPrototype(superTypePrototype);
                }
                list.add(superTypeClassInfo);
            }
        }
        if (!list.isEmpty()) {
            classInfo.setSuperTypes((ClassInfo[]) list.toArray(new ClassInfo[0]));
        }
    }

    protected static ClassInfo loadSuperTypeInfo(String prototype, ClassProcessor classProcessor) {
        int modifiers = 0;
        for (int index; (index = prototype.indexOf(' ')) > 0; ) {
            String token = prototype.substring(0, index);
            int modifier = Util.getModifier(token);
            if (modifier < 0) {
                break;
            }
            modifiers |= modifier;
            prototype = prototype.substring(index + 1);
        }
        String className = prototype;
        int index = className.indexOf('<');
        if (index >= 0) {
            className = className.substring(0, index);
        }
        ClassInfo classInfo = createClassInfo(className, classProcessor);
        if (!classInfo.isLoaded()) {
            String[] tokens = prototype.split("[<,]");
            Set genericSet = new HashSet();
            for (int i = 1; i < tokens.length; i++) {
                String[] gTokens = tokens[i].split("[ >]");
                for (int j = 0; j < gTokens.length; j++) {
                    String s = gTokens[j];
                    if (s.length() > 0) {
                        genericSet.add(s);
                        break;
                    }
                }
            }
            classInfo.setDeclaration(prototype, (String[]) genericSet.toArray(new String[0]));
            if ((modifiers & (Modifiers.PUBLIC | Modifiers.PROTECTED | Modifiers.PRIVATE)) == 0) {
                modifiers |= Modifiers.DEFAULT;
            }
            classInfo.setModifiers(modifiers);
        }
        return classInfo;
    }

    protected static void loadSubTypes(ClassInfo classInfo, NodeList nodeList, ClassProcessor classProcessor) {
        ArrayList list = new ArrayList(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String name = node.getNodeName();
            if ("subType".equals(name)) {
                String subTypePrototype = node.getAttributes().getNamedItem("value").getNodeValue();
                ClassInfo subTypeClassInfo = loadSubTypeInfo(subTypePrototype, classProcessor);
                if (subTypeClassInfo.getPrototype() == null) {
                    subTypeClassInfo.setPrototype(subTypePrototype);
                }
                list.add(subTypeClassInfo);
            }
        }
        if (!list.isEmpty()) {
            classInfo.setSubTypes((ClassInfo[]) list.toArray(new ClassInfo[0]));
        }
    }

    protected static ClassInfo loadSubTypeInfo(String prototype, ClassProcessor classProcessor) {
        int modifiers = 0;
        for (int index; (index = prototype.indexOf(' ')) > 0; ) {
            String token = prototype.substring(0, index);
            int modifier = Util.getModifier(token);
            if (modifier < 0) {
                break;
            }
            modifiers |= modifier;
            prototype = prototype.substring(index + 1);
        }
        String className = prototype;
        int index = className.indexOf('<');
        if (index >= 0) {
            className = className.substring(0, index);
        }
        ClassInfo classInfo = createClassInfo(className, classProcessor);
        if (!classInfo.isLoaded()) {
            String[] tokens = prototype.split("[<,]");
            Set genericSet = new HashSet();
            for (int i = 1; i < tokens.length; i++) {
                String[] gTokens = tokens[i].split("[ >]");
                for (int j = 0; j < gTokens.length; j++) {
                    String s = gTokens[j];
                    if (s.length() > 0) {
                        genericSet.add(s);
                        break;
                    }
                }
            }
            classInfo.setDeclaration(prototype, (String[]) genericSet.toArray(new String[0]));
            if ((modifiers & (Modifiers.PUBLIC | Modifiers.PROTECTED | Modifiers.PRIVATE)) == 0) {
                modifiers |= Modifiers.DEFAULT;
            }
            classInfo.setModifiers(modifiers);
        }
        return classInfo;
    }

    protected static void loadFields(ClassInfo classInfo, NodeList nodeList, ClassProcessor classProcessor, boolean isEnum) {
        ArrayList list = new ArrayList(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String name = node.getNodeName();
            if ("field".equals(name) || "enum".equals(name)) {
                String fieldInfoPrototype = node.getAttributes().getNamedItem("value").getNodeValue();
                FieldInfo fieldInfo = loadFieldInfo(classInfo, fieldInfoPrototype, classProcessor);
                if (fieldInfo.getPrototype() == null) {
                    fieldInfo.setPrototype(fieldInfoPrototype);
                }
                list.add(fieldInfo);
            }
        }
        if (!list.isEmpty()) {
            FieldInfo[] fields = (FieldInfo[]) list.toArray(new FieldInfo[0]);
            Arrays.sort(fields, new Comparator() {

                public int compare(Object o1, Object o2) {
                    return ((FieldInfo) o1).getName().toLowerCase(Locale.ENGLISH).compareTo(((FieldInfo) o2).getName().toLowerCase(Locale.ENGLISH));
                }
            });
            if (isEnum) {
                classInfo.setEnums(fields);
            } else {
                classInfo.setFields(fields);
            }
        }
    }

    protected static FieldInfo loadFieldInfo(ClassInfo classInfo, String prototype, ClassProcessor classProcessor) {
        List annotationList = new ArrayList();
        while (prototype.startsWith("@") && !prototype.startsWith("@interface ")) {
            int count = 0;
            boolean hasParameters = false;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < prototype.length(); i++) {
                char c = prototype.charAt(i);
                switch(c) {
                    case '(':
                        count++;
                        sb.append(c);
                        hasParameters = true;
                        break;
                    case ')':
                        count--;
                        sb.append(c);
                        break;
                    case ' ':
                        if (count == 0) {
                            prototype = prototype.substring(i + 1);
                            i = prototype.length();
                        }
                        break;
                    default:
                        sb.append(c);
                        break;
                }
            }
            String annotationClassName = sb.toString();
            if (hasParameters) {
                annotationClassName = annotationClassName.substring(1, annotationClassName.indexOf('('));
            } else {
                annotationClassName = annotationClassName.substring(1);
            }
            annotationList.add(ClassInfoLoader.createClassInfo(annotationClassName, classProcessor));
        }
        int modifiers = 0;
        for (int index; (index = prototype.indexOf(' ')) > 0; ) {
            String token = prototype.substring(0, index);
            int modifier = Util.getModifier(token);
            if (modifier < 0) {
                break;
            }
            modifiers |= modifier;
            prototype = prototype.substring(index + 1);
        }
        int index = prototype.lastIndexOf(' ');
        String fieldName = prototype.substring(index + 1);
        prototype = prototype.substring(0, index);
        if ((modifiers & (Modifiers.PUBLIC | Modifiers.PROTECTED | Modifiers.PRIVATE)) == 0) {
            modifiers |= Modifiers.DEFAULT;
        }
        FieldInfo fieldInfo = new FieldInfo(classInfo, null, prototype, fieldName, modifiers, classProcessor);
        fieldInfo.setAnnotations((ClassInfo[]) annotationList.toArray(new ClassInfo[0]));
        return fieldInfo;
    }

    protected static final int METHOD_METHOD_TYPE = 1;

    protected static final int CONSTRUCTOR_METHOD_TYPE = 2;

    protected static final int ANNOTATION_METHOD_TYPE = 3;

    protected static void loadMethods(ClassInfo classInfo, NodeList nodeList, ClassProcessor classProcessor, int methodType) {
        ArrayList list = new ArrayList(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String name = node.getNodeName();
            if ("method".equals(name) || "constructor".equals(name) || "annotationMember".equals(name)) {
                String methodInfoPrototype = node.getAttributes().getNamedItem("value").getNodeValue();
                MethodInfo methodInfo = loadMethodInfo(classInfo, methodInfoPrototype, node, classProcessor);
                if (methodInfo.getPrototype() == null) {
                    methodInfo.setPrototype(methodInfoPrototype);
                }
                list.add(methodInfo);
            }
        }
        if (!list.isEmpty()) {
            MethodInfo[] methods = (MethodInfo[]) list.toArray(new MethodInfo[0]);
            Arrays.sort(methods, new Comparator() {

                public int compare(Object o1, Object o2) {
                    return ((MethodInfo) o1).getName().toLowerCase(Locale.ENGLISH).compareTo(((MethodInfo) o2).getName().toLowerCase(Locale.ENGLISH));
                }
            });
            switch(methodType) {
                case METHOD_METHOD_TYPE:
                    classInfo.setMethods(methods);
                    break;
                case CONSTRUCTOR_METHOD_TYPE:
                    classInfo.setConstructors(methods);
                    break;
                case ANNOTATION_METHOD_TYPE:
                    classInfo.setAnnotationMembers(methods);
                    break;
            }
        }
    }

    protected static MethodInfo loadMethodInfo(ClassInfo classInfo, String prototype, Node node, ClassProcessor classProcessor) {
        List annotationList = new ArrayList();
        while (prototype.startsWith("@") && !prototype.startsWith("@interface ")) {
            int count = 0;
            boolean hasParameters = false;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < prototype.length(); i++) {
                char c = prototype.charAt(i);
                switch(c) {
                    case '(':
                        count++;
                        sb.append(c);
                        hasParameters = true;
                        break;
                    case ')':
                        count--;
                        sb.append(c);
                        break;
                    case ' ':
                        if (count == 0) {
                            prototype = prototype.substring(i + 1);
                            i = prototype.length();
                        }
                        break;
                    default:
                        sb.append(c);
                        break;
                }
            }
            String annotationClassName = sb.toString();
            if (hasParameters) {
                annotationClassName = annotationClassName.substring(1, annotationClassName.indexOf('('));
            } else {
                annotationClassName = annotationClassName.substring(1);
            }
            annotationList.add(ClassInfoLoader.createClassInfo(annotationClassName, classProcessor));
        }
        int modifiers = 0;
        for (int index; (index = prototype.indexOf(' ')) > 0; ) {
            String token = prototype.substring(0, index);
            int modifier = Util.getModifier(token);
            if (modifier < 0) {
                break;
            }
            modifiers |= modifier;
            prototype = prototype.substring(index + 1);
        }
        String methodName = prototype;
        int index = prototype.lastIndexOf(' ', prototype.indexOf('('));
        boolean isConstructor = index < 0;
        if (!isConstructor) {
            methodName = methodName.substring(index + 1);
            prototype = prototype.substring(0, index);
        }
        index = methodName.indexOf(" throws ");
        String exceptionsString = null;
        if (index >= 0) {
            exceptionsString = methodName.substring(index + " throws ".length());
            methodName = methodName.substring(0, index);
        }
        index = methodName.indexOf('(');
        String parametersString = methodName.substring(index + 1, methodName.length() - 1);
        methodName = methodName.substring(0, index);
        Set genericSet = new HashSet();
        if (prototype.startsWith("<")) {
            int count = 1;
            int i = 0;
            for (i = 1; count > 0 && i < prototype.length(); i++) {
                switch(prototype.charAt(i)) {
                    case '<':
                        count++;
                        break;
                    case '>':
                        count--;
                        break;
                }
            }
            if (count == 0) {
                String[] tokens = prototype.substring(0, i).split("[<,]");
                for (int k = 1; k < tokens.length; k++) {
                    String[] gTokens = tokens[k].split("[ >]");
                    for (int j = 0; j < gTokens.length; j++) {
                        String s = gTokens[j];
                        if (s.length() > 0) {
                            genericSet.add(s);
                            break;
                        }
                    }
                }
                prototype = prototype.substring(i).trim();
            }
        }
        String[] generics = (String[]) genericSet.toArray(new String[0]);
        if ((modifiers & (Modifiers.PUBLIC | Modifiers.PROTECTED | Modifiers.PRIVATE)) == 0) {
            modifiers |= Modifiers.DEFAULT;
        }
        MethodInfo methodInfo = new MethodInfo(classInfo, methodName, modifiers, generics);
        FieldInfo[] exceptionsParameterInfo = null;
        if (exceptionsString != null) {
            StringTokenizer st = new StringTokenizer(exceptionsString, ",");
            exceptionsParameterInfo = new FieldInfo[st.countTokens()];
            for (int i = 0; st.hasMoreTokens(); i++) {
                exceptionsParameterInfo[i] = new FieldInfo(classInfo, methodInfo, st.nextToken().trim(), classProcessor);
            }
        }
        FieldInfo[] parametersParameterInfo = null;
        if (parametersString.length() > 0) {
            List parametersList = new ArrayList();
            int count = 0;
            for (int i = 0; i < parametersString.length(); i++) {
                char c = parametersString.charAt(i);
                switch(c) {
                    case '<':
                        count++;
                        break;
                    case '>':
                        count--;
                        break;
                    case ',':
                        if (count == 0) {
                            String s = parametersString.substring(0, i).trim();
                            parametersList.add(s);
                            parametersString = parametersString.substring(i + 1).trim();
                            i = 0;
                        }
                        break;
                }
            }
            parametersList.add(parametersString);
            parametersParameterInfo = new FieldInfo[parametersList.size()];
            for (int i = 0; i < parametersParameterInfo.length; i++) {
                String parameter = (String) parametersList.get(i);
                int sIndex = parameter.lastIndexOf(' ');
                parametersParameterInfo[i] = new FieldInfo(classInfo, methodInfo, parameter.substring(0, sIndex), parameter.substring(sIndex + 1), classProcessor);
            }
        }
        FieldInfo returnedType = isConstructor ? null : new FieldInfo(classInfo, methodInfo, prototype, classProcessor);
        methodInfo.setMethodParameters(parametersParameterInfo, returnedType, exceptionsParameterInfo);
        methodInfo.setAnnotations((ClassInfo[]) annotationList.toArray(new ClassInfo[0]));
        return methodInfo;
    }

    protected static HashMap classNameToClassInfoMap = new HashMap();

    public static ClassInfo createClassInfo(String className, ClassProcessor classProcessor) {
        ClassInfo classInfo = (ClassInfo) classNameToClassInfoMap.get(className);
        if (classInfo == null) {
            classInfo = new ClassInfo(className, classProcessor);
            classNameToClassInfoMap.put(className, classInfo);
        }
        return classInfo;
    }

    public static void destroyClassInfo(ClassInfo classInfo) {
        classNameToClassInfoMap.remove(classInfo.getClassName());
        ClassProcessor classProcessor = classInfo.getClassProcessor();
        if (classProcessor != null) {
            classProcessor.destroyClassInfo(classInfo);
        }
    }

    public static ClassInfo[] getClassInfos() {
        return (ClassInfo[]) classNameToClassInfoMap.values().toArray(new ClassInfo[0]);
    }

    public static ClassInfo getClassInfo(String className) {
        return (ClassInfo) classNameToClassInfoMap.get(className);
    }

    protected static void setClassInfos(ClassInfo[] classInfos) {
        classNameToClassInfoMap.clear();
        for (int i = 0; i < classInfos.length; i++) {
            ClassInfo classInfo = classInfos[i];
            classNameToClassInfoMap.put(classInfo.getClassName(), classInfo);
        }
    }
}
