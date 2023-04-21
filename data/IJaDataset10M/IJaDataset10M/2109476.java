package mcujavasource.transformer;

import java.util.*;
import mcujavasource.transformer.instance.JavaClass;
import mcujavasource.transformer.instance.UserClass;
import mcujavasource.transformer.instance.UserInstance;
import org.w3c.dom.*;

/**
 *
 */
public class SourceToCTransformer extends DOMTransformer {

    private SourceContext context;

    private SourceTranslator sourceTranslator;

    private UserMethodTransformer userMethodTransformer;

    /** Belongs to result */
    private Node cSourceNode;

    private boolean inMainClass = false;

    /**
   * Creates a new instance of SourceToCTransformer
   */
    public SourceToCTransformer() {
    }

    public void transform(List<Node> source, Node result) throws DOMTransformerException {
        context = new SourceContext(settings);
        userMethodTransformer = new UserMethodTransformer(this, context, result.getOwnerDocument());
        sourceTranslator = new SourceTranslator(context, userMethodTransformer);
        for (Node n : source) processClassDeclarations(n, "", null);
        processClasses(result);
        new SourceContextToCTransformer(context, cSourceNode).process();
    }

    public void transform(Node source, Node result) throws DOMTransformerException {
        List<Node> nodeList = new ArrayList<Node>();
        nodeList.add(source);
        transform(nodeList, result);
    }

    private void processClassDeclarations(Node src, String path, String packageName) {
        NodeList srcList = src.getChildNodes();
        for (int i = 0; i < srcList.getLength(); i++) {
            Node n = srcList.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) n;
                String name = n.getNodeName();
                if (name.equals("class")) {
                    processClassDeclaration(el, path, packageName);
                } else if (name.equals("package-decl")) {
                    packageName = el.getAttribute("name");
                }
            }
        }
    }

    private void processClassDeclaration(Element n, String path, String packageName) {
        JavaClass c = new JavaClass();
        c.setName(path + n.getAttribute("name"));
        c.setPackageName(packageName);
        NodeList nList = n.getChildNodes();
        for (int i = 0; i < nList.getLength(); i++) {
            Node n1 = nList.item(i);
            if (n1.getNodeType() == Node.ELEMENT_NODE) {
                Element e1 = (Element) n1;
                String n1Name = n1.getNodeName();
                if (n1Name.equals("superclass")) c.setSuperclass(e1.getAttribute("name")); else if (n1Name.equals("implement")) c.getInterfaces().add(e1.getAttribute("interface"));
            }
        }
        c.setContent(n);
        context.getClasses().add(c);
        processClassDeclarations(n, c.getName() + ".", packageName);
    }

    private void processClasses(Node dest) {
        ArrayList<JavaClass> classes = context.getClasses();
        ClassLoader compiledLoader = (ClassLoader) context.getSettings().get("compiledLoader");
        for (JavaClass c : classes) {
            if (!c.getSuperclass().equals("Microcontroller")) {
                ListenerImplementation li = ListenerImplementation.fromSourceClass(context, c);
                if (li == null) {
                    try {
                        Class<?> compiledClass = Class.forName(c.getName(), true, compiledLoader);
                        UserClass uc = new UserClass();
                        uc.setCompiledClass(compiledClass);
                        context.getUserClasses().add(uc);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            }
        }
        for (JavaClass c : classes) {
            if (c.getSuperclass().equals("Microcontroller")) {
                cSourceNode = dest;
                context.getPath().setClassName(c.getName());
                context.getPath().setMethodName(null);
                inMainClass = true;
                process(c.getContent(), cSourceNode);
                inMainClass = false;
            }
            break;
        }
        if (cSourceNode == null) throw new InvalidInputDataException("No main class found. Main class must extend Microcontroller.", "source");
        for (JavaClass c : classes) {
            if (!c.getSuperclass().equals("Microcontroller")) {
                ListenerImplementation li = null;
                for (ListenerImplementation limp : context.getListenerImplementations()) {
                    if (limp.getName().equals(c.getName())) {
                        li = limp;
                        break;
                    }
                }
                if (li == null) {
                } else {
                    Node result = dest.getOwnerDocument().createElement("result");
                    c.setResult(result);
                    li.setContent(result);
                    context.getPath().setClassName(c.getName());
                    context.getPath().setMethodName(null);
                    process(c.getContent(), result);
                }
            }
        }
    }

    private void process(Node src, Node dest) {
        NodeList srcList = src.getChildNodes();
        for (int i = 0; i < srcList.getLength(); i++) {
            Node n = srcList.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                processDirect(n, dest);
            }
        }
    }

    void processDirect(Node n, Node dest) {
        String name = n.getNodeName();
        if (name.equals("block")) process(n, keep(n, dest)); else if (name.equals("class")) processClass((Element) n, dest); else if (name.equals("constructor")) process(n, keep(n, dest)); else if (name.equals("method")) {
            String methodName = ((Element) n).getAttribute("name");
            context.getPath().setMethodName(methodName);
            Node methodNode = keep(n, dest);
            if (inMainClass && methodName.equals("init")) {
                context.setInit(true);
                process(n, methodNode);
                context.setInit(false);
                Node initBlockNode = getChildNode(methodNode, "block");
                context.getInitContext().appendRegisters(initBlockNode);
            } else process(n, methodNode);
            context.getPath().setMethodName(null);
        } else if (name.equals("formal-arguments")) process(n, keep(n, dest)); else if (name.equals("formal-argument")) process(n, keep(n, dest)); else if (name.equals("field")) processField((Element) n, dest); else if (name.equals("arguments")) process(n, keep(n, dest)); else if (name.equals("send")) processSend((Element) n, dest); else if (name.equals("var-ref")) processVarRef((Element) n, dest); else if (name.equals("field-access")) processFieldAccess((Element) n, dest); else if (name.equals("new-array")) process(n, keep(n, dest)); else if (name.equals("dim-expr")) process(n, keep(n, dest)); else if (name.equals("array-initializer")) process(n, keep(n, dest)); else if (name.equals("array-ref")) processArrayRef((Element) n, dest); else if (name.equals("base")) process(n, keep(n, dest)); else if (name.equals("offset")) process(n, keep(n, dest)); else if (name.equals("local-variable")) processLocalVariable((Element) n, dest); else if (name.equals("type")) process(n, keep(n, dest)); else if (name.equals("if")) process(n, keep(n, dest)); else if (name.equals("test")) process(n, keep(n, dest)); else if (name.equals("true-case")) process(n, keep(n, dest)); else if (name.equals("false-case")) process(n, keep(n, dest)); else if (name.equals("switch")) process(n, keep(n, dest)); else if (name.equals("switch-block")) process(n, keep(n, dest)); else if (name.equals("case")) process(n, keep(n, dest)); else if (name.equals("default-case")) process(n, keep(n, dest)); else if (name.equals("loop")) process(n, keep(n, dest)); else if (name.equals("init")) process(n, keep(n, dest)); else if (name.equals("test")) process(n, keep(n, dest)); else if (name.equals("update")) process(n, keep(n, dest)); else if (name.equals("return")) process(n, keep(n, dest)); else if (name.equals("continue")) processContinue((Element) n, dest); else if (name.equals("break")) processBreak((Element) n, dest); else if (name.equals("empty")) keep(n, dest); else if (name.equals("paren")) process(n, keep(n, dest)); else if (name.equals("expr")) process(n, keep(n, dest)); else if (name.equals("assignment-expr")) processAssignmentExpr((Element) n, dest); else if (name.equals("lvalue")) process(n, keep(n, dest)); else if (name.equals("conditional-expr")) process(n, keep(n, dest)); else if (name.equals("binary-expr")) process(n, keep(n, dest)); else if (name.equals("unary-expr")) process(n, keep(n, dest)); else if (name.equals("cast-expr")) process(n, keep(n, dest)); else if (name.equals("literal-number")) keep(n, dest); else if (name.equals("literal-string")) keep(n, dest); else if (name.equals("literal-char")) keep(n, dest); else if (name.equals("literal-boolean")) keep(n, dest); else if (name.equals("code-fragment")) process(n, keep(n, dest)); else if (name.equals("result")) process(n, keep(n, dest)); else if (name.equals("this-call") && context.getCurrentMethodOwner() != null) {
            sourceTranslator.translateExpression(n, dest);
        } else if (name.equals("label")) process(n, keep(n, dest)); else if (name.equals("superclass") || name.equals("implement") || name.equals("target")) ; else if (name.equals("try") || name.equals("throw") || name.equals("instanceof-test") || name.equals("literal-null") || name.equals("this") || name.equals("super") || name.equals("super-call") || name.equals("this-call")) throw new InvalidInputDataException("Expression is not supported", name); else if (name.equals("synchronized") || name.equals("assert") || name.equals("throws") || name.equals("interface")) ; else System.err.println("Unrecognized element: " + name);
    }

    private void processClass(Element n, Node dest) {
    }

    private void processField(Element n, Node dest) {
        processFieldAndLocalVariable(n, dest, true);
    }

    private void processSend(Element n, Node dest) {
        Node target = null;
        NodeList nList = n.getChildNodes();
        for (int i = 0; i < nList.getLength(); i++) {
            Node n1 = nList.item(i);
            if (n1.getNodeType() == Node.ELEMENT_NODE) {
                String n1Name = n1.getNodeName();
                if (n1Name.equals("target")) {
                    NodeList n1List = n1.getChildNodes();
                    for (int k = 0; k < n1List.getLength(); k++) {
                        Node n2 = n1List.item(k);
                        if (n2.getNodeType() == Node.ELEMENT_NODE) {
                            String n2Name = n2.getNodeName();
                            if (!n2Name.equals("this")) {
                                target = n2;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (target == null && context.getCurrentMethodOwner() == null) process(n, keep(n, dest)); else sourceTranslator.translateExpression(n, dest);
    }

    private void processAssignmentExpr(Element n, Node dest) {
        String varName = null;
        NodeList nList = n.getChildNodes();
        for (int i = 0; i < nList.getLength(); i++) {
            Node n1 = nList.item(i);
            if (n1.getNodeType() == Node.ELEMENT_NODE) {
                Element e1 = (Element) n1;
                String n1Name = n1.getNodeName();
                if (n1Name.equals("lvalue")) {
                    varName = sourceTranslator.getVariableName(n1);
                    if (context.isPrimitive(varName)) {
                        dest = keep(n, dest);
                        process(n1, keep(n1, dest));
                    } else {
                        sourceTranslator.translateExpression(n, dest);
                        break;
                    }
                } else {
                    processDirect(n1, dest);
                }
            }
        }
    }

    private void processVarRef(Element n, Node dest) {
        if (context.getCurrentMethodOwner() != null) {
            UserInstance ui = context.getCurrentMethodOwner();
            n.setAttribute("name", ui.getClassName() + "_" + ui.getName() + "_" + n.getAttribute("name"));
        }
        String name = n.getAttribute("name");
        if (context.isPrimitive(name)) keep(n, dest); else sourceTranslator.translateExpression(n, dest);
    }

    private void processArrayRef(Element n, Node dest) {
        if (context.getCurrentMethodOwner() != null) {
            UserInstance ui = context.getCurrentMethodOwner();
            n.setAttribute("name", ui.getClassName() + "_" + ui.getName() + "_" + n.getAttribute("name"));
        }
        String varName = sourceTranslator.getVariableNameInElement(n);
        if (context.isPrimitive(varName)) process(n, keep(n, dest)); else sourceTranslator.translateExpression(n, dest);
    }

    private void processFieldAccess(Element n, Node dest) {
        String name = n.getAttribute("field");
        NodeList nList = n.getChildNodes();
        for (int i = 0; i < nList.getLength(); i++) {
            Node n1 = nList.item(i);
            if (n1.getNodeType() == Node.ELEMENT_NODE) {
                String n1Name = n1.getNodeName();
                if (!n1Name.equals("this")) throw new InvalidInputDataException("References to fields of non-this class are not supported", name);
            }
        }
        if (context.getCurrentMethodOwner() != null) {
            UserInstance ui = context.getCurrentMethodOwner();
            n.setAttribute("field", ui.getClassName() + "_" + ui.getName() + "_" + n.getAttribute("field"));
            name = n.getAttribute("field");
        }
        if (context.isPrimitive(name)) keep(n, dest); else sourceTranslator.translateExpression(n, dest);
    }

    private void processLocalVariable(Element n, Node dest) {
        processFieldAndLocalVariable(n, dest, false);
    }

    /** Not implemented: final int N = 2+3; (expressions)
   */
    private void processFieldAndLocalVariable(Element n, Node dest, boolean field) {
        if (context.getCurrentMethodOwner() != null) {
            UserInstance ui = context.getCurrentMethodOwner();
            n.setAttribute("name", ui.getClassName() + "_" + ui.getName() + "_" + n.getAttribute("name"));
        }
        boolean primitive = false;
        String typeName = "";
        int dimensions = 0;
        NodeList nList = n.getChildNodes();
        for (int i = 0; i < nList.getLength(); i++) {
            Node n1 = nList.item(i);
            if (n1.getNodeType() == Node.ELEMENT_NODE) {
                Element e1 = (Element) n1;
                String n1Name = n1.getNodeName();
                if (n1Name.equals("type")) {
                    if (e1.hasAttribute("primitive") && e1.getAttribute("primitive").equals("true")) primitive = true;
                    typeName = e1.getAttribute("name");
                    if (e1.hasAttribute("dimensions")) dimensions = Integer.decode(e1.getAttribute("dimensions"));
                    break;
                }
            }
        }
        boolean isFinal = false;
        if (n.hasAttribute("final") && n.getAttribute("final").equals("true")) isFinal = true;
        if (primitive) {
            if (isFinal) {
                if (dimensions == 0) {
                    for (int i = 0; i < nList.getLength(); i++) {
                        Node n1 = nList.item(i);
                        if (n1.getNodeType() == Node.ELEMENT_NODE) {
                            Element e1 = (Element) n1;
                            String n1Name = n1.getNodeName();
                            Object value = null;
                            if (typeName.equals("boolean") && n1Name.equals("literal-boolean")) {
                                value = Boolean.parseBoolean(e1.getAttribute("value"));
                            } else if (typeName.equals("char") && n1Name.equals("literal-char")) {
                                value = e1.getAttribute("value").charAt(0);
                            } else if (n1Name.equals("literal-number")) {
                                if (typeName.equals("int")) {
                                    value = Integer.decode(e1.getAttribute("value"));
                                } else if (typeName.equals("long")) {
                                    value = Long.decode(e1.getAttribute("value"));
                                } else if (typeName.equals("byte")) {
                                    value = Byte.decode(e1.getAttribute("value"));
                                } else if (typeName.equals("short")) {
                                    value = Short.decode(e1.getAttribute("value"));
                                } else if (typeName.equals("float")) {
                                    value = Float.parseFloat(e1.getAttribute("value"));
                                } else if (typeName.equals("double")) {
                                    value = Double.parseDouble(e1.getAttribute("value"));
                                }
                            }
                            if (value != null) context.getPrimitiveConstants().put(n.getAttribute("name"), value);
                        }
                    }
                } else {
                    sourceTranslator.declareConstant(n);
                    Node result = keep(n, dest);
                    process(n, result);
                }
            } else process(n, keep(n, dest));
        } else {
            if (typeName.equals("String") || typeName.equals("java.lang.String")) {
                if (isFinal) {
                    if (dimensions == 0) {
                        sourceTranslator.declareConstant(n);
                        Node result = keep(n, dest);
                        process(n, result);
                    } else throw new InvalidInputDataException("String arrays are not supported", "String[] " + n.getAttribute("name"));
                } else throw new InvalidInputDataException("Non-final Strings are not supported", "String " + n.getAttribute("name"));
            } else sourceTranslator.translateExpression(n, dest);
        }
    }

    private void processContinue(Element n, Node dest) {
        if (n.hasAttribute("targetname")) {
            throw new InvalidInputDataException("Expression is not supported", "continue " + n.getAttribute("targetname"));
        } else keep(n, dest);
    }

    private void processBreak(Element n, Node dest) {
        if (n.hasAttribute("targetname")) {
            Element gotoElement = dest.getOwnerDocument().createElement("goto");
            gotoElement.setAttribute("targetname", n.getAttribute("targetname"));
            dest.appendChild(gotoElement);
        } else keep(n, dest);
    }

    /** Copies {@code src} node and appends it to {@code dest}. Note, that
   * {@code src} and {@code dest} nodes belong to different documents.
   */
    private void copy(Node src, Node dest) {
        dest.appendChild(dest.getOwnerDocument().importNode(src, true));
    }

    /** Imports {@code src} node itself (without children), appends it to
   * {@code dest} and returns it. Note, that {@code src} belongs to one
   * document, {@code dest} and result - to another.
   */
    private Node keep(Node src, Node dest) {
        Node n = dest.getOwnerDocument().importNode(src, false);
        dest.appendChild(n);
        return n;
    }

    private static Node getChildNode(Node parent, String childName) {
        NodeList nList = parent.getChildNodes();
        for (int i = 0; i < nList.getLength(); i++) {
            Node n1 = nList.item(i);
            if (n1.getNodeType() == Node.ELEMENT_NODE) {
                String n1Name = n1.getNodeName();
                if (n1Name.equals(childName)) return n1;
            }
        }
        return null;
    }
}
