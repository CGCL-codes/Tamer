package com.pinae.nala.xb.builder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.File;
import com.pinae.nala.xb.NoSuchPathException;
import com.pinae.nala.xb.UnmarshalException;
import com.pinae.nala.xb.resource.AttributeConfig;
import com.pinae.nala.xb.resource.NodeConfig;
import com.pinae.nala.xb.unmarshal.XMLParser;
import com.pinae.nala.xb.util.StringUtils;
import com.pinae.nala.xb.xml.XMLReader;

/**
 * ClassBuilderʵ����ͨ�������XML����ɶ�Ӧ��Java��
 * 
 * 
 * @author ��ع��
 *
 */
public class ClassBuilder extends XMLParser {

    private String packagePath;

    private NodeConfig config;

    private String path = "";

    /**
	 * @param packagePath �����İ�����com.pinae.test
	 * @param xml ��Ҫ������XML�ļ����ļ�
	 * @throws NoSuchPathException �����XML�ļ�����������쳣
	 * @throws UnmarshalException �����쳣
	 * @throws IOException IO����ʱ������쳣 
	 */
    public ClassBuilder(String packagePath, String xml) throws NoSuchPathException, UnmarshalException, IOException {
        this.packagePath = packagePath;
        config = XMLParser.parser(XMLReader.getFileStream(xml));
    }

    /**
	 * ��������XML����������ɣ���ɵ�·��Ϊ��ǰִ�е�Ŀ¼��ͬʱ�����ļ���·�����հ��·�����
	 * 
	 * @param basePath Java�ļ��Ĵ��λ��
	 * @throws IOException ���Java�ļ����쳣
	 */
    public void build(String basePath) throws IOException {
        if (basePath != null) {
            File file = new File(basePath);
            file.mkdir();
        }
        if (packagePath != null && packagePath.length() > 0) {
            path = buildPackage(basePath, packagePath);
        }
        buildClass(path, packagePath, config);
    }

    private void saveClassFile(String filename, StringBuffer fileContext) throws IOException {
        FileWriter input;
        input = new FileWriter(path + filename);
        input.write(fileContext.toString(), 0, fileContext.length());
        input.flush();
        input.close();
    }

    private void buildClass(String path, String packagePath, NodeConfig config) throws IOException {
        Map<String, String> method = new HashMap<String, String>();
        boolean importList = false;
        StringBuffer classBuffer = new StringBuffer();
        if (config.getAttribute() != null && config.getAttribute().size() > 0) {
            for (Iterator iterAttribute = config.getAttribute().iterator(); iterAttribute.hasNext(); ) {
                AttributeConfig attribute = (AttributeConfig) iterAttribute.next();
                method.put(attribute.getName(), "String");
            }
        }
        if (config.getChildrenNodes() != null && config.getChildrenNodes().size() > 0) {
            for (Iterator iterNode = config.getChildrenNodes().iterator(); iterNode.hasNext(); ) {
                NodeConfig node = (NodeConfig) iterNode.next();
                buildClass(path, packagePath, node);
                if (method.get(node.getName()) != null) {
                    method.put(node.getName(), node.getName() + "#");
                    importList = true;
                } else {
                    method.put(node.getName(), node.getName());
                }
            }
        } else {
            method.put("value", "String");
        }
        if (packagePath != null) {
            classBuffer.append("package " + packagePath + ";\n");
        }
        if (importList) {
            classBuffer.append(buildImport());
        }
        classBuffer.append("public class " + StringUtils.toUpperCase(config.getName(), 1) + " extends com.pinae.nala.xb.NalaObject {\n");
        classBuffer.append(buildVariable(method));
        classBuffer.append(buildMethod(method));
        classBuffer.append("}");
        try {
            saveClassFile(StringUtils.toUpperCase(config.getName(), 1) + ".java", classBuffer);
        } catch (IOException e) {
            throw new IOException("Couldn't save file:" + StringUtils.toUpperCase(config.getName(), 1) + ".java");
        }
    }

    private StringBuffer buildMethod(Map<String, String> methodMap) {
        StringBuffer methodBuffer = new StringBuffer();
        for (Iterator iterMethod = methodMap.keySet().iterator(); iterMethod.hasNext(); ) {
            String methodName = (String) iterMethod.next();
            String methodType = methodMap.get(methodName);
            if (methodType.indexOf("#") > 0) {
                methodType = StringUtils.subString(methodType, methodType.length() - 1, true);
                methodBuffer.append("\tpublic void set");
                methodBuffer.append(StringUtils.toUpperCase(methodName, 1));
                methodBuffer.append("(" + StringUtils.toUpperCase(methodType, 1) + " " + methodName + "){\n");
                methodBuffer.append("\t\tthis." + methodName + ".add(" + methodName + ");\n\t}\n");
                methodBuffer.append("\tpublic List<" + StringUtils.toUpperCase(methodType, 1));
                methodBuffer.append("> get" + StringUtils.toUpperCase(methodName, 1) + "(){\n");
                methodBuffer.append("\t\treturn " + methodName + ";\n\t}\n");
            } else {
                methodBuffer.append("\tpublic void set");
                methodBuffer.append(StringUtils.toUpperCase(methodName, 1));
                methodBuffer.append("(" + StringUtils.toUpperCase(methodType, 1) + " " + methodName + "){\n");
                methodBuffer.append("\t\tthis." + methodName + " = " + methodName + ";\n\t}\n");
                methodBuffer.append("\tpublic " + StringUtils.toUpperCase(methodType, 1));
                methodBuffer.append(" get" + StringUtils.toUpperCase(methodName, 1) + "(){\n");
                methodBuffer.append("\t\treturn " + methodName + ";\n\t}\n");
            }
        }
        return methodBuffer;
    }

    private StringBuffer buildVariable(Map<String, String> variableMap) {
        StringBuffer variableBuffer = new StringBuffer();
        for (Iterator iterVariable = variableMap.keySet().iterator(); iterVariable.hasNext(); ) {
            String variableName = (String) iterVariable.next();
            String variableType = variableMap.get(variableName);
            if (variableType.indexOf("#") > 0) {
                variableType = StringUtils.subString(variableType, variableType.length() - 1, true);
                variableBuffer.append("\tprivate List<" + StringUtils.toUpperCase(variableType, 1) + "> " + variableName);
                variableBuffer.append(" = new ArrayList<" + StringUtils.toUpperCase(variableType, 1) + ">();\n");
            } else {
                variableBuffer.append("\tprivate " + StringUtils.toUpperCase(variableType, 1) + " " + variableName + ";\n");
            }
        }
        return variableBuffer;
    }

    private StringBuffer buildImport() {
        StringBuffer importBuffer = new StringBuffer();
        importBuffer.append("import java.util.List;\n");
        importBuffer.append("import java.util.ArrayList;\n");
        return importBuffer;
    }

    private String buildPackage(String basePath, String packagePath) {
        List folders = StringUtils.split(packagePath, ".".charAt(0));
        String path = basePath;
        for (Iterator iterFolder = folders.iterator(); iterFolder.hasNext(); ) {
            path += (String) iterFolder.next() + "\\";
            File file = new File(path);
            file.mkdir();
        }
        return path;
    }

    /**
	 * ���к���
	 * 
	 * @param args ���в���
	 * @throws NoSuchPathException û������XML��Դ��·���쳣
	 * @throws IOException ����Java�ļ�ʱ���쳣
	 * @throws UnmarshalException �����쳣
	 */
    public static void main(String[] args) throws NoSuchPathException, IOException, UnmarshalException {
        if (args.length >= 2) {
            ClassBuilder builder = new ClassBuilder(args[0], args[1]);
            if (args.length < 3) {
                builder.build("");
            } else if (args.length >= 3) {
                builder.build(args[2]);
            }
        } else {
            System.out.println("java ClassBuilder[Java������][XML�ļ����][�����ౣ��·��]");
        }
    }
}
