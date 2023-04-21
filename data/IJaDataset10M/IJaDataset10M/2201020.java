package com.ilog.translator.java2cs.configuration.info;

import java.util.HashMap;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.ilog.translator.java2cs.configuration.options.DotNetFramework;
import com.ilog.translator.java2cs.configuration.options.MethodMappingPolicy;
import com.ilog.translator.java2cs.configuration.target.TargetIndexer;
import com.ilog.translator.java2cs.configuration.target.TargetMethod;
import com.ilog.translator.java2cs.configuration.target.TargetProperty;
import com.ilog.translator.java2cs.translation.noderewriter.PropertyRewriter;
import com.ilog.translator.java2cs.translation.noderewriter.PropertyRewriter.ProperyKind;
import com.ilog.translator.java2cs.util.Utils;

/**
 * 
 * Encapsulate a java method and its translation (target method) - target -
 * constraints (from parser) - modifiers (from parser) - codeReplacement (from
 * parser) - name (from parser) - property_get (from parser) - property_set
 * (from parser) - indexer_get (from parser) - indexer_set (from parser) -
 * pattern (from parser) - genericsif (from parser) - generation (from parser) -
 * covariant (from parser)
 */
public class MethodInfo extends MemberInfo {

    private HashMap<String, TargetMethod> targetMethods = new HashMap<String, TargetMethod>();

    private final IMethod method;

    private boolean isConstructor;

    private boolean disableAutoproperty = false;

    /**
	 * Create a new MethodInfo that encapsulate the given Method
	 * 
	 * @param meth
	 *            the method of this MethodInfo.
	 */
    protected MethodInfo(MappingsInfo mappingInfo, IMethod meth) {
        super(mappingInfo, meth.getElementName());
        method = meth;
    }

    /**
	 * @return Returns the parameter types.
	 */
    public String[] getParameterTypes() {
        return method.getParameterTypes();
    }

    public String resolveType(String typeName) {
        try {
            final String[][] resolved = method.getDeclaringType().resolveType(typeName);
            if (resolved == null || resolved.length == 0) {
                return typeName;
            }
            String tmpRes = Signature.toQualifiedName(resolved[0]);
            int index = -1;
            if ((index = typeName.indexOf("<")) > 0) {
                final String rest = typeName.substring(index + 1);
                final int endIndex = rest.lastIndexOf(">");
                final String genericArgs = rest.substring(0, endIndex);
                final String[] args = genericArgs.split(",");
                tmpRes += "<";
                for (int j = 0; j < args.length; j++) {
                    final String arg = args[j];
                    tmpRes += resolveType(arg);
                    if (j < args.length - 1) tmpRes += ",";
                }
                tmpRes += ">";
            }
            return tmpRes;
        } catch (final JavaModelException e) {
            return typeName;
        }
    }

    /**
	 * @return Returns the declaring class
	 */
    public IType getDeclaringClass() {
        return method.getDeclaringType();
    }

    /**
	 * 
	 * @return true if that MethodInfo has a target method
	 */
    public boolean hasTargetMethod() {
        return targetMethods.size() > 0;
    }

    public TargetMethod getTarget(String targetFramework) {
        if (targetMethods == null || targetMethods.size() == 0) {
            String mName = name.getValue();
            if (mName != null && needCapitalization(classInfo)) {
                mName = Utils.capitalize(name.getValue());
                mName = Utils.replaceForbiddenChar(mName);
                return new TargetMethod(mName, (int[]) null);
            }
            return null;
        }
        return targetMethods.get(targetFramework);
    }

    public HashMap<String, TargetMethod> getTargetMethods() {
        return targetMethods;
    }

    public void addTargetMethod(String targetFramework, TargetMethod field) {
        targetMethods.put(targetFramework, field);
    }

    public boolean isDisableAutoproperty() {
        return disableAutoproperty;
    }

    public void setDisableAutoproperty(boolean disableAutoproperty) {
        this.disableAutoproperty = disableAutoproperty;
    }

    @Override
    public String toString() {
        return method.toString();
    }

    public MethodInfo copyInto(ClassInfo cinfo) {
        final MethodInfo newMethodInfo = new MethodInfo(mappingInfo, method);
        newMethodInfo.setClassInfo(cinfo);
        for (final String targetFramework : targetMethods.keySet()) {
            TargetMethod m = targetMethods.get(targetFramework);
            newMethodInfo.addTargetMethod(targetFramework, m.cloneForChild());
        }
        return newMethodInfo;
    }

    public MethodInfo cloneForChild() {
        final MethodInfo newMethodInfo = new MethodInfo(mappingInfo, method);
        newMethodInfo.setClassInfo(getClassInfo());
        for (final String targetFramework : targetMethods.keySet()) {
            TargetMethod m = targetMethods.get(targetFramework);
            newMethodInfo.addTargetMethod(targetFramework, m.cloneForChild());
        }
        return newMethodInfo;
    }

    public String toFile() {
        String res = "";
        if (targetMethods != null && targetMethods.size() > 0) {
            res += getSignature() + " { ";
            for (final TargetMethod targetMethod : targetMethods.values()) {
                if (targetMethod.getName() != null) {
                    res += " name = " + targetMethod.getName() + ";";
                }
                if (targetMethod.getChangeModifierDescriptor() != null) {
                    res += " modifiers = " + targetMethod.getChangeModifierDescriptor() + ";";
                }
                if (targetMethod.isTranslated()) res += " remove = true;";
                if (targetMethod.isRenamed()) res += " renamed = true;";
                if (targetMethod.getGenericsTest() != null) {
                    res += "genericsif = " + targetMethod.getGenericsTest() + ";";
                }
            }
            res += "}";
        }
        return null;
    }

    public String getSignature() {
        return "(" + printParametersTypes() + ")";
    }

    private String printParametersTypes() {
        String res = "";
        for (int i = 0; i < method.getParameterTypes().length; i++) {
            String typename = method.getParameterTypes()[i];
            try {
                typename = Signature.toString(typename);
                if (!typename.contains(".")) {
                    String[][] resTN = method.getDeclaringType().resolveType(typename);
                    if (resTN.length == 1) typename = Signature.toQualifiedName(resTN[0]);
                }
            } catch (Exception e) {
            }
            res += typename;
            if (i < method.getParameterTypes().length - 1) res += ",";
        }
        return res;
    }

    public void toXML(StringBuilder res, String tabValue) {
        res.append(Constants.THREETAB + "<!--                               -->\n");
        res.append(Constants.THREETAB + "<!-- method " + name.getValue() + Utils.xmlify(getSignature()) + " -->\n");
        res.append(Constants.THREETAB + "<!--                               -->\n");
        if (isConstructor()) {
            res.append(Constants.THREETAB + "<constructor signature=\"" + Utils.xmlify(getSignature()) + "\"");
        } else {
            res.append(Constants.THREETAB + "<method signature=\"" + Utils.xmlify(getSignature()) + "\" name=\"" + name.getValue() + "\"");
        }
        toXML(res, sinceJDK, "\n" + Constants.FOURTAB + "       ", "", "");
        res.append(">\n");
        if (targetMethods != null && targetMethods.size() > 0) {
            for (TargetMethod m : targetMethods.values()) m.toXML(res, tabValue);
        }
        if (isConstructor()) res.append(Constants.THREETAB + "</constructor>"); else res.append(Constants.THREETAB + "</method>");
    }

    private static boolean needCapitalization(ClassInfo cinfo) {
        boolean neededByClassInfo = true;
        String targetFramework = cinfo.getMappingInfo().getTranslateInfo().getConfiguration().getOptions().getTargetDotNetFramework().name();
        if (cinfo.getTarget(targetFramework) != null) {
            if (cinfo.getTarget(targetFramework).getMemberMappingBehavior() != null) {
                neededByClassInfo = cinfo.getTarget(targetFramework).getMemberMappingBehavior().equals(MethodMappingPolicy.CAPITALIZED);
            }
        }
        if (neededByClassInfo) return neededByClassInfo; else {
            final PackageInfo pInfo = cinfo.getPackageInfo();
            boolean neededByPackageInfo = true;
            if (pInfo.getTarget(targetFramework) != null) {
                if (pInfo.getTarget(targetFramework).getMemberMappingBehavior() != null) {
                    neededByPackageInfo = pInfo.getTarget(targetFramework).getMemberMappingBehavior().equals(MethodMappingPolicy.CAPITALIZED);
                }
            }
            if (neededByPackageInfo) return neededByPackageInfo;
        }
        return false;
    }

    public void fromXML(Element pack) {
        NodeList child = pack.getChildNodes();
        sinceJDK.fromXML(pack);
        for (int i = 0; i < child.getLength(); i++) {
            Node pckNode = child.item(i);
            if (pckNode.getNodeType() == Node.ELEMENT_NODE) {
                Element pckElement = (Element) pckNode;
                String elemName = pckElement.getNodeName();
                if (elemName.equals("target")) {
                    String targetFramework = pckElement.getAttribute("dotnetFramework");
                    if (targetFramework == null || targetFramework.isEmpty()) targetFramework = DotNetFramework.NET3_5.name();
                    if (!pckElement.getAttribute("autoProperty").isEmpty()) {
                        TargetProperty tProperty = null;
                        targetMethods.put(targetFramework, tProperty = new TargetProperty());
                        String mName = name.getValue();
                        String pName = null;
                        if (mName.startsWith("get")) {
                            pName = mName.substring(3);
                            tProperty.getPropertyGetOption().setValue(pName);
                            tProperty.setKind(ProperyKind.READ);
                            tProperty.setRewriter(new PropertyRewriter(pName, ProperyKind.READ));
                        } else if (mName.startsWith("set")) {
                            pName = mName.substring(3);
                            tProperty.getPropertySetOption().setValue(pName);
                            tProperty.setKind(ProperyKind.WRITE);
                            tProperty.setRewriter(new PropertyRewriter(pName, ProperyKind.WRITE));
                        } else if (mName.startsWith("is")) {
                            pName = Utils.capitalize(mName);
                            tProperty.getPropertyGetOption().setValue(pName);
                            tProperty.setKind(ProperyKind.READ);
                            tProperty.setRewriter(new PropertyRewriter(pName, ProperyKind.READ));
                        } else {
                            continue;
                        }
                        tProperty.fromXML(pckElement);
                    } else {
                        TargetMethod tMethod = null;
                        if (isProperty(pckElement)) {
                            tMethod = new TargetProperty();
                        } else if (isIndexer(pckElement)) {
                            tMethod = new TargetIndexer();
                        } else tMethod = new TargetMethod();
                        targetMethods.put(targetFramework, tMethod);
                        tMethod.fromXML(pckElement);
                    }
                }
            }
        }
    }

    private boolean isProperty(Element node) {
        return !node.getAttribute("propertyGet").isEmpty() || !node.getAttribute("propertySet").isEmpty();
    }

    private boolean isIndexer(Element node) {
        return !node.getAttribute("indexerGet").isEmpty() || !node.getAttribute("indexerSet").isEmpty();
    }

    public void setConstructro(boolean b) {
        this.isConstructor = b;
    }

    public boolean isConstructor() {
        return isConstructor;
    }
}
