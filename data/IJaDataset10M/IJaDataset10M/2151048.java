package org.adapit.wctoolkit.transformers.python.m2c;

import java.util.Iterator;
import org.adapit.wctoolkit.models.util.AllElements;
import org.adapit.wctoolkit.modeltocodeframework.AbstractOperationCodeGenerator;
import org.adapit.wctoolkit.uml.classes.kernel.Class;
import org.adapit.wctoolkit.uml.classes.kernel.Operation;
import org.adapit.wctoolkit.uml.classes.kernel.Parameter;
import org.adapit.wctoolkit.uml.classes.kernel.ParameterDirectionKind;
import org.adapit.wctoolkit.uml.classes.kernel.VisibilityKind;
import org.adapit.wctoolkit.uml.ext.core.IElement;
import org.adapit.wctoolkit.uml.ext.core.Interface;
import org.adapit.wctoolkit.uml.ext.core.NamedElement;
import org.adapit.wctoolkit.uml.ext.core.ScopeKind;
import org.adapit.wctoolkit.uml.ext.core.TaggedValue;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.AbstractTransformer;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationDescriptor;

@SuppressWarnings({ "serial", "unchecked" })
public class GenerateOperationWithCollectionCode extends AbstractOperationCodeGenerator {

    public GenerateOperationWithCollectionCode(TransformationDescriptor observer, Class _class) {
        super(observer, _class);
    }

    public GenerateOperationWithCollectionCode(Class _class) {
        super(_class);
    }

    private org.adapit.wctoolkit.uml.classes.kernel.Class rootClass;

    public Object doTransformation() throws Exception {
        refactor();
        if (_class.getOperations() != null && _class.getOperations().size() > 0) {
            if (_class.getParentElement() instanceof org.adapit.wctoolkit.uml.classes.kernel.Class) {
                rootClass = (Class) _class.getLastAncestor(org.adapit.wctoolkit.uml.classes.kernel.Class.class);
            } else rootClass = _class;
            include = getIncludeTag(rootClass);
            Iterator it = _class.getOperations().iterator();
            while (it.hasNext()) {
                Object ob = it.next();
                if (ob instanceof org.adapit.wctoolkit.uml.classes.kernel.Operation) {
                    try {
                        Operation operation = (Operation) ob;
                        String code = getOperationCode(operation);
                        this.operationsCode.add(code);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return operationsCode;
    }

    public String getOperationCode(Operation operation) throws Exception {
        String str = "";
        if (operation.getReturnType() != null) {
            PythonTransformerUtility.refactorDataTypeNameToJavaType((NamedElement) operation.getReturnType().getType());
        }
        TaggedValue tv = new TaggedValue(operation);
        tv.setName("HeaderCode");
        if (operation.contains(tv)) {
            tv = operation.getAssignedTaggedValue("HeaderCode");
            if (tv.getValue() != null && !tv.getValue().trim().equals("")) str += replaceByIdValues(tv.getValue());
        }
        if (operation.getScope() != null && operation.getScope() == ScopeKind.INSTANCE) {
            str += "" + '\n' + "" + '\t' + "@classmethod";
        } else if (operation.getScope() != null) {
            str += "" + '\n' + "" + '\t' + "@staticmethod";
        }
        tv = new TaggedValue(operation);
        tv.setName("annotations");
        if (operation.contains(tv)) {
            tv = operation.getAssignedTaggedValue("annotations");
            if (tv.getValue() != null && !tv.getValue().trim().equals("")) str += replaceByIdValues(tv.getValue());
        }
        tv = new TaggedValue(operation);
        tv.setName("documentation");
        if (operation.contains(tv)) {
            tv = operation.getAssignedTaggedValue("documentation");
            if (tv.getValue() != null && !tv.getValue().trim().equals("")) str += "# " + '\n' + " " + replaceByIdValues(tv.getValue()) + '\n' + " #";
        }
        tv = new TaggedValue(operation);
        tv.setName("comment");
        if (operation.contains(tv)) {
            tv = operation.getAssignedTaggedValue("comment");
            if (tv.getValue() != null && !tv.getValue().trim().equals("")) str += "# " + '\n' + " " + replaceByIdValues(tv.getValue()) + '\n' + " " + " #";
        }
        str += "" + '\n' + "" + '\t' + "";
        if (operation.getVisibility() != null && operation.getVisibility() != VisibilityKind.DEFAULT) {
        }
        if (operation.isAbstract()) {
        } else if (operation.containsStereotype("frozen")) {
        }
        if (!operation.containsStereotype("create")) {
            if (operation.getReturnType() != null && operation.getReturnType().getType() != null && !operation.getReturnType().isCollection()) {
                if (operation.containsTaggedValue("TemplateClass")) {
                    String elid = operation.getAssignedTaggedValue("TemplateClass").getValue();
                    IElement tempClass = (IElement) AllElements.getInstance().getElements().get(elid);
                    if (include.getValue().indexOf(tempClass.getNamespaceWithoutModel().replaceAll("::", ".") + ";") < 0) {
                        include.setValue(include.getValue() + '\n' + "import " + tempClass.getNamespaceWithoutModel().replaceAll("::", ".") + ";");
                    }
                } else {
                }
                checkImportOnReturnType(operation);
            } else if (operation.getReturnType() != null && operation.getReturnType().getType() != null && operation.getReturnType().isCollection()) {
                if (operation.containsTaggedValue("TemplateClass")) {
                    String elid = operation.getAssignedTaggedValue("TemplateClass").getValue();
                    IElement tempClass = (IElement) AllElements.getInstance().getElements().get(elid);
                    if (include.getValue().indexOf(tempClass.getNamespaceWithoutModel().replaceAll("::", ".")) < 0) {
                        include.setValue(include.getValue() + '\n' + "import " + tempClass.getNamespaceWithoutModel().replaceAll("::", ".") + ";");
                    }
                } else {
                }
                if (!AbstractTransformer.isPrimitiveJavaType(operation.getReturnType())) {
                    if (include.getValue().indexOf(operation.getReturnType().getType().getNamespaceWithoutModel().replaceAll("::", ".") + ";") < 0) include.setValue(include.getValue() + '\n' + "import " + operation.getReturnType().getType().getNamespaceWithoutModel().replaceAll("::", ".") + ";");
                }
            } else {
            }
            if (operation.getParameters() != null && operation.getParameters().size() > 0) {
                Iterator ite = operation.getParameters().iterator();
                while (ite.hasNext()) {
                    Object o = ite.next();
                    if (o instanceof Parameter) {
                        Parameter p = (Parameter) o;
                        PythonTransformerUtility.refactorDataTypeNameToJavaType((NamedElement) p.getType());
                        if (!p.getType().getName().equalsIgnoreCase("int") && !p.getType().getName().equalsIgnoreCase("float") && !p.getType().getName().equalsIgnoreCase("double") && !p.getType().getName().equalsIgnoreCase("boolean") && !p.getType().getName().equalsIgnoreCase("char") && !p.getType().getName().equalsIgnoreCase("long") && !p.getType().getName().equalsIgnoreCase("short") && !p.getType().getName().equalsIgnoreCase("void") && !p.getType().getName().equalsIgnoreCase("string") && !p.getType().getName().equals("java.lang.Integer") && !p.getType().getName().equals("java.lang.String") && !p.getType().getName().equals("java.lang.Character") && !p.getType().getName().equals("java.lang.Float") && !p.getType().getName().equals("java.lang.Math") && !p.getType().getName().equals("java.lang.Double") && !p.getType().getName().equals("byte")) {
                            if (include.getValue().indexOf(p.getType().getNamespaceWithoutModel().replaceAll("::", ".") + ";") < 0) include.setValue(include.getValue() + '\n' + "import " + p.getType().getNamespaceWithoutModel().replaceAll("::", ".") + ";");
                        }
                    }
                }
            }
        }
        if (operation.getName() != null) str += "def " + operation.getName() + "(";
        if (operation.getParameters() != null && operation.getParameters().size() > 0) {
            try {
                Iterator ite = operation.getParameters().iterator();
                while (ite.hasNext()) {
                    java.lang.Object obj = ite.next();
                    if (obj instanceof org.adapit.wctoolkit.uml.classes.kernel.Parameter) {
                        Parameter p = (Parameter) obj;
                        if (p.getDirection() != ParameterDirectionKind.RETURN) {
                            if (p.isArray()) {
                                String name = "";
                                if (p.containsTaggedValue("TemplateClass")) {
                                    String elid = p.getAssignedTaggedValue("TemplateClass").getValue();
                                    IElement tempClass = (IElement) AllElements.getInstance().getElements().get(elid);
                                    if (include.getValue().indexOf(tempClass.getNamespaceWithoutModel().replaceAll("::", ".") + ";") < 0) {
                                        include.setValue(include.getValue() + '\n' + "import " + tempClass.getNamespaceWithoutModel().replaceAll("::", ".") + ";");
                                    }
                                } else {
                                }
                                if (ite.hasNext()) str += name + ","; else str += name + ")";
                            } else {
                                String name = "";
                                if (p.containsTaggedValue("TemplateClass")) {
                                    String elid = p.getAssignedTaggedValue("TemplateClass").getValue();
                                    IElement tempClass = (IElement) AllElements.getInstance().getElements().get(elid);
                                    if (include.getValue().indexOf(tempClass.getNamespaceWithoutModel().replaceAll("::", ".") + ";") < 0) {
                                        include.setValue(include.getValue() + '\n' + "import " + tempClass.getNamespaceWithoutModel().replaceAll("::", ".") + ";");
                                    }
                                } else name = p.getName() + " ";
                                if (ite.hasNext()) str += name + ","; else str += name + ")";
                            }
                            if (!AbstractTransformer.isPrimitiveJavaType(p)) {
                                if (include.getValue().indexOf(p.getType().getNamespaceWithoutModel().replaceAll("::", ".") + ";") < 0) include.setValue(include.getValue() + '\n' + "import " + p.getType().getNamespaceWithoutModel().replaceAll("::", ".") + ";");
                            }
                        } else if (operation.getParameters().size() == 1) str += ")";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else str += ")";
        try {
            if (!operation.isAbstract() && !(operation.getParentElement() instanceof Interface)) {
                str += ":" + '\n';
                if (operation.getBody() != null) {
                    str += '\n';
                    str += "" + '\t' + "" + '\t' + "";
                    str += replaceByIdValues(operation.getBody());
                }
                tv = new TaggedValue(operation);
                tv.setName("BodyCode");
                if (operation.contains(tv)) {
                    tv = operation.getAssignedTaggedValue("BodyCode");
                    str += replaceByIdValues(tv.getValue()).replace(".", "->");
                }
                tv = new TaggedValue(operation);
                tv.setName("BottomCode");
                if (operation.contains(tv)) {
                    tv = operation.getAssignedTaggedValue("BottomCode");
                    str += replaceByIdValues(tv.getValue());
                }
            } else {
                str += ":";
                str += '\n';
                str += "" + '\t' + "" + '\t' + "pass";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str;
    }
}
