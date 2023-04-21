package org.jxpl.primitives.service.jxpl2Interface;

import org.jxpl.*;
import org.jxpl.bindings.*;
import org.jxpl.primitives.service.*;
import org.jxpl.exception.*;
import java.util.*;

/**
*   Generate the wrapper required to produce the needing functionality for the function.
*   This class will package the script AND then use 
*/
public class JxplInterface {

    private String name, qualifiedName;

    private List<JxplMethod> methods = new LinkedList();

    private static JxplTypeTable typeConverter = new JxplTypeTable();

    private JxplElement script;

    public void setQualifiedName(String name) {
        qualifiedName = name;
    }

    private void addMethod(JxplMethod elem) {
        methods.add(elem);
    }

    /**
    *   Write the source code required for wrapper class.
    */
    public String writeSource(JxplElement script) {
        String interfaceString = "package jxplService;\r\n" + "import org.jxpl.primitives.service.jxpl2Interface.*;\r\n" + "import org.jxpl.*;\r\n" + "import org.jxpl.bindings.*;\r\n" + "import org.jxpl.primitives.*;\r\n" + "import org.jxpl.exception.*;\r\n\r\n";
        interfaceString += "public class " + name + "{";
        interfaceString += "\t\tprivate Processor environment=new Processor();\r\n\r\n";
        String xml = script.toString().replace("\r", "").replace("\n", "").replace("\"", "\\\"");
        interfaceString += "\t\tprivate String xml=\"" + xml + "\";\r\n";
        Iterator<JxplMethod> itr = methods.iterator();
        while (itr.hasNext()) {
            interfaceString += "\r\n\t" + fillMethod(script, itr.next());
        }
        interfaceString += "\r\n}";
        return interfaceString;
    }

    /**
    *   Take the method header and construct the functional implementation
    */
    private String fillMethod(JxplElement script, JxplMethod m) {
        String impl = "";
        impl += m.toString() + "{\n\n";
        impl += "\t\tObject[] params={";
        List<JxplParam> params = m.getParams();
        for (int i = 0; i < params.size(); i++) {
            impl += params.get(i).getName();
            if (i < params.size() - 1) impl += ",";
        }
        impl += "};\r\n\n";
        impl += "\t\ttry{\r\n\n";
        impl += "\t\t\treturn (" + m.getReturnType() + ") WrapperUtil.evaluate(environment, xml, \"" + m.getName() + "\", params, \"" + m.getReturnType() + "\");\r\n";
        impl += "\t\t}catch (JxplException e){return null;}\r\n\n";
        impl += "\t}";
        return impl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String generateInterface(Service s) {
        JxplInterface intfce = new JxplInterface();
        intfce.setName(s.getServiceName());
        intfce.setScript(s.getScript());
        List<Operation> l = s.getOperations();
        for (int i = 0; i < l.size(); i++) {
            Operation oper = l.get(i);
            JxplMethod meth = new JxplMethod(oper.getName(), typeConverter.getJxpl2Java(oper.getReturnType()));
            List<Param> prms = oper.getParameters();
            for (int j = 0; j < prms.size(); j++) {
                Param p = prms.get(j);
                JxplParam param = new JxplParam(p.getVariable().getQName().toString(), typeConverter.getJxpl2Java(p.getTypeDesc()));
                meth.addParam(param);
            }
            intfce.addMethod(meth);
        }
        return intfce.writeSource(s.getScript());
    }

    public void setScript(JxplElement script) {
        this.script = script;
    }
}

class JxplMethod {

    private String name;

    private String returnType = "void";

    private List<JxplParam> params = new LinkedList();

    public JxplMethod(String name, String returnType) {
        this.name = name;
        this.returnType = returnType;
    }

    public void addParam(JxplParam in) {
        params.add(in);
    }

    public String toString() {
        String methodString = "public " + returnType + " " + name + "(";
        Iterator itr = params.iterator();
        while (itr.hasNext()) {
            methodString += itr.next().toString();
            if (itr.hasNext()) methodString += ",";
        }
        methodString += ")";
        return methodString;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<JxplParam> getParams() {
        return params;
    }
}

class JxplParam {

    String type;

    String name;

    public JxplParam(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public JxplParam(String name) {
        this(name, "Object");
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return type + " " + name;
    }
}
