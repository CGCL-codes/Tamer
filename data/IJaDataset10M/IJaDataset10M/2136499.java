package org.apache.ws.jaxme.generator.sg.impl.ccsg;

import java.util.ArrayList;
import java.util.List;
import org.apache.ws.jaxme.generator.sg.AttributeSG;
import org.apache.ws.jaxme.generator.sg.ComplexTypeSG;
import org.apache.ws.jaxme.generator.sg.GroupSG;
import org.apache.ws.jaxme.generator.sg.ObjectSG;
import org.apache.ws.jaxme.generator.sg.ParticleSG;
import org.apache.ws.jaxme.generator.sg.PropertySG;
import org.apache.ws.jaxme.generator.sg.PropertySGChain;
import org.apache.ws.jaxme.generator.sg.TypeSG;
import org.apache.ws.jaxme.generator.sg.impl.PropertySGChainImpl;
import org.apache.ws.jaxme.generator.sg.impl.PropertySGImpl;
import org.apache.ws.jaxme.js.JavaField;
import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaQName;
import org.apache.ws.jaxme.js.JavaSource;
import org.xml.sax.SAXException;

/** Implementation of a
 * {@link org.apache.ws.jaxme.generator.sg.impl.ccsg.ParticleVisitor},
 * which generates a bean class.
 */
public class BeanGeneratingVisitor extends ParticleVisitorImpl {

    private final JavaSource js;

    private JavaMethod mixedContentMethod;

    private boolean isMixed;

    private ComplexTypeSG ct;

    /** Creates a new instance generating methods into the
	 * given class.
	 * @param pJs Bean class being generated.
	 */
    public BeanGeneratingVisitor(JavaSource pJs) {
        js = pJs;
    }

    private JavaSource getJavaSource() {
        return js;
    }

    private boolean isInheritedAttribute(AttributeSG pAttr, AttributeSG[] pInheritedAttributes) throws SAXException {
        if (pInheritedAttributes != null) {
            String p = pAttr.getPropertySG().getPropertyName();
            for (int i = 0; i < pInheritedAttributes.length; i++) {
                if (pInheritedAttributes[i].getPropertySG().getPropertyName().equals(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void generateAttributes(ComplexTypeSG pType) throws SAXException {
        JavaSource js = getJavaSource();
        AttributeSG[] myAttributes = pType.getAttributes();
        AttributeSG[] inheritedAttributes = null;
        if (pType.getTypeSG().isExtension()) {
            TypeSG tSG = pType.getTypeSG().getExtendedType();
            if (tSG.isComplex()) {
                inheritedAttributes = tSG.getComplexTypeSG().getAttributes();
            }
        }
        for (int i = 0; i < myAttributes.length; i++) {
            AttributeSG attr = myAttributes[i];
            if (!isInheritedAttribute(attr, inheritedAttributes)) {
                attr.getPropertySG().generate(js);
            }
        }
    }

    public void emptyType(ComplexTypeSG pType) throws SAXException {
        generateAttributes(pType);
    }

    public void simpleContent(ComplexTypeSG pType) throws SAXException {
        generateAttributes(pType);
        pType.getSimpleContentSG().getPropertySG().generate(getJavaSource());
    }

    protected JavaMethod getGetMixedContentMethod() {
        if (mixedContentMethod == null) {
            JavaSource js = getJavaSource();
            JavaMethod jm = js.newJavaMethod("getContent", List.class, JavaSource.PUBLIC);
            if (!js.isInterface()) {
                JavaField mixedContentList = getJavaSource().newJavaField("content", List.class, JavaSource.PRIVATE);
                mixedContentList.setFinal(true);
                mixedContentList.addLine("new ", ArrayList.class, "()");
                jm.addLine("return ", mixedContentList, ";");
            }
        }
        return mixedContentMethod;
    }

    public void startComplexContent(ComplexTypeSG pType) throws SAXException {
        ct = pType;
        generateAttributes(pType);
        if (pType.getComplexContentSG().isMixed()) {
            isMixed = true;
            getGetMixedContentMethod();
        }
    }

    private boolean isInheritedParticle(ParticleSG pParticle) throws SAXException {
        String propertyName = pParticle.getPropertySG().getPropertyName();
        if (ct.getTypeSG().isExtension()) {
            TypeSG extType = ct.getTypeSG().getExtendedType();
            if (extType.isComplex() && !extType.getComplexTypeSG().hasSimpleContent()) {
                ParticleSG[] inheritedParticles = extType.getComplexTypeSG().getComplexContentSG().getElementParticles();
                for (int i = 0; i < inheritedParticles.length; i++) {
                    if (inheritedParticles[i].getPropertySG().getPropertyName().equals(propertyName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void elementParticle(GroupSG pGroupSG, ParticleSG pParticle) throws SAXException {
        if (isInheritedParticle(pParticle)) {
            return;
        }
        final JavaSource pJs = this.js;
        final PropertySG elementSG = pParticle.getPropertySG();
        if (isMixed) {
            JavaQName qName = GroupUtil.getContentClass(pGroupSG, pParticle, pJs.getQName());
            JavaSource js;
            if (qName.isInnerClass()) {
                js = pJs.newJavaInnerClass(qName.getInnerClassName(), JavaSource.PUBLIC);
                if (!pJs.isInterface()) {
                    js.setStatic(true);
                }
            } else {
                js = pJs.getFactory().newJavaSource(qName, JavaSource.PUBLIC);
            }
            if (pJs.isInterface()) {
                js.setType(JavaSource.INTERFACE);
            } else {
                js.addImplements(GroupUtil.getContentClass(pGroupSG, pParticle, ct.getClassContext().getXMLInterfaceName()));
            }
            PropertySGChain chain = ((PropertySGImpl) elementSG).getHeadOfChain();
            PropertySGChain head = new PropertySGChainImpl(chain) {

                public String getXMLFieldName(PropertySG pController) throws SAXException {
                    return "_value";
                }

                public String getPropertyName(PropertySG pController) throws SAXException {
                    return "value";
                }
            };
            PropertySGImpl pSG = new PropertySGImpl(head);
            pSG.generate(js);
        } else {
            elementSG.generate(pJs);
        }
        ObjectSG oSG = pParticle.getObjectSG();
        TypeSG typeSG = oSG.getTypeSG();
        if (!typeSG.isGlobalType() && !typeSG.isGlobalClass() && typeSG.isComplex()) {
            ComplexTypeSG complexTypeSG = typeSG.getComplexTypeSG();
            if (pJs.isInterface()) {
                complexTypeSG.getXMLInterface(pJs);
            } else {
                complexTypeSG.getXMLImplementation(pJs);
            }
        }
    }

    public void simpleElementParticle(GroupSG pGroup, ParticleSG pParticle) throws SAXException {
        elementParticle(pGroup, pParticle);
    }

    public void complexElementParticle(GroupSG pGroup, ParticleSG pParticle) throws SAXException {
        elementParticle(pGroup, pParticle);
    }

    public void wildcardParticle(ParticleSG particle) {
        throw new IllegalStateException("TODO: Add support for wildcards");
    }
}
