package org.adapit.wctoolkit.transformers.csharp.m2c;

import java.util.Iterator;
import org.adapit.wctoolkit.uml.classes.kernel.Enumeration;
import org.adapit.wctoolkit.uml.classes.kernel.LiteralSpecification;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.annotations.TransformationLanguage;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.AbstractTransformer;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationDescriptor;

@SuppressWarnings({ "serial", "unchecked" })
public class EnumerationTransformer extends AbstractTransformer {

    public EnumerationTransformer(TransformationDescriptor observer) {
        super(observer);
    }

    public EnumerationTransformer() {
        super();
    }

    protected void initialize() {
        try {
            this.getTransformationDescriptor().setDescription("Such Transformer Generates Enumeration Csharp Code");
            this.getTransformationDescriptor().setName("Generates Enumeration Csharp Code");
            this.getTransformationDescriptor().setReturnType("java.lang.String");
            this.getTransformationDescriptor().setWriter("F�bio Paulo Basso");
            this.getTransformationDescriptor().setVersion("1.0");
            this.getTransformationDescriptor().setLanguageVersion("1.0");
            this.getTransformationDescriptor().setClassName(this.getClass().getName());
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

    public Object doTransformation() throws Exception {
        Enumeration en = (Enumeration) getParameter("default");
        String str = "";
        try {
            str += "using System;" + '\n' + "" + '\n';
            str += "public enum " + en.getName() + "{";
            str += "" + '\n' + "" + '\t' + "";
            if (en.getOwnedLiterals() != null && en.getOwnedLiterals().size() > 0) {
                Iterator it = en.getOwnedLiterals().iterator();
                while (it.hasNext()) {
                    LiteralSpecification l = (LiteralSpecification) it.next();
                    if (it.hasNext()) str += l.getName() + ", "; else str += l.getName();
                }
            }
            str += "" + '\n' + "}";
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return str;
    }
}
