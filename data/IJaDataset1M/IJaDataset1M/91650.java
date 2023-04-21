package org.adapit.wctoolkit.transformers.patterns;

import org.adapit.wctoolkit.uml.classes.kernel.Association;
import org.adapit.wctoolkit.uml.classes.kernel.Class;
import org.adapit.wctoolkit.uml.classes.kernel.Operation;
import org.adapit.wctoolkit.uml.classes.kernel.Parameter;
import org.adapit.wctoolkit.uml.classes.kernel.ParameterDirectionKind;
import org.adapit.wctoolkit.uml.classes.kernel.PrimitiveType;
import org.adapit.wctoolkit.uml.classes.kernel.VisibilityKind;
import org.adapit.wctoolkit.uml.ext.core.AssociationEnd;
import org.adapit.wctoolkit.uml.ext.core.Stereotype;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.annotations.TransformationLanguage;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.AbstractTransformer;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationDescriptor;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationParameter;

@SuppressWarnings({ "serial", "unchecked" })
public class ObserverTransformer extends AbstractTransformer {

    protected TransformationParameter transformationParameter1 = new TransformationParameter();

    protected TransformationParameter transformationParameter2 = new TransformationParameter();

    protected void initialize() {
        try {
            transformationParameter1.setName("observer");
            transformationParameter1.setType(org.adapit.wctoolkit.uml.classes.kernel.Class.class.getName());
            super.getParameters().put(transformationParameter1.getName(), transformationParameter1);
            transformationParameter2.setName("subject");
            transformationParameter2.setType(org.adapit.wctoolkit.uml.classes.kernel.Class.class.getName());
            super.getParameters().put(transformationParameter1.getName(), transformationParameter1);
            this.getTransformationDescriptor().setDescription("Such transformer provides an observer pattern to apply betwenn observer and subject classes");
            this.getTransformationDescriptor().setName("Observer Pattern");
            this.getTransformationDescriptor().setReturnType("void");
            this.getTransformationDescriptor().setWriter("F�bio Paulo Basso");
            this.getTransformationDescriptor().setSrc("");
            this.getTransformationDescriptor().setVersion("1.0");
            this.getTransformationDescriptor().setLanguageVersion("1.0");
            this.getTransformationDescriptor().setClassName(this.getClass().getName());
            this.getTransformationDescriptor().setJarUrl("");
            this.getTransformationDescriptor().setLanguage(TransformationLanguage.JAVA);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setTransformationDecriptor(TransformationDescriptor observer) {
        super.setTransformationDecriptor(observer);
        initialize();
    }

    /**
	 * @param observer
	 */
    public ObserverTransformer(TransformationDescriptor observer) {
        super(observer);
        initialize();
    }

    /**
	 * 
	 */
    public ObserverTransformer() {
        super();
    }

    public Object doTransformation() throws Exception {
        Object obj = super.getParameter("observer");
        org.adapit.wctoolkit.uml.classes.kernel.Class observer = (Class) obj;
        org.adapit.wctoolkit.uml.classes.kernel.Class subject = (org.adapit.wctoolkit.uml.classes.kernel.Class) super.getParameter("subject");
        Operation con = new Operation(subject);
        Stereotype st = new Stereotype(subject.getRootParent());
        st.setName("create");
        con.assignStereotype(st);
        con.setName(subject.getName());
        con.setAbstract(false);
        con.setScope("instance");
        con.setVisibility(VisibilityKind.PUBLIC);
        Parameter p = new Parameter(con);
        p.setName("observer");
        p.setType(observer);
        p.setDirection(ParameterDirectionKind.IN);
        con.addElement(p);
        subject.addElement(con);
        Association a = new Association(observer.getRootParent());
        observer.getRootParent().addElement(a);
        AssociationEnd ae = new AssociationEnd(a);
        ae.setVisibility(VisibilityKind.PROTECTED);
        ae.setUpper("1");
        ae.setLower("1");
        ae.setChangeability("changeable");
        ae.setName("observer");
        ae.setType(observer);
        subject.addElement(ae);
        Operation report = new Operation(observer);
        PrimitiveType voidDT = new PrimitiveType(observer.getRootParent());
        voidDT = (PrimitiveType) observer.getRootParent().addElement(voidDT);
        if (voidDT == null) throw new Exception("To make this transformation is needed void DataType");
        report.setReturnType(voidDT);
        report.setName("report");
        report.setVisibility(VisibilityKind.PUBLIC);
        report.setScope("instance");
        org.adapit.wctoolkit.uml.classes.kernel.Parameter p1 = new org.adapit.wctoolkit.uml.classes.kernel.Parameter(report);
        p1.setName("reporter");
        p1.setType(subject);
        p1.setDirection(ParameterDirectionKind.IN);
        report.addElement(p1);
        observer.addElement(report);
        return null;
    }
}
