package org.apache.ws.jaxme.generator.sg.impl.ccsg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.UnmarshallerHandler;
import org.apache.ws.jaxme.JMManager;
import org.apache.ws.jaxme.generator.sg.ComplexContentSG;
import org.apache.ws.jaxme.generator.sg.ComplexTypeSG;
import org.apache.ws.jaxme.generator.sg.GroupSG;
import org.apache.ws.jaxme.generator.sg.ObjectSG;
import org.apache.ws.jaxme.generator.sg.ParticleSG;
import org.apache.ws.jaxme.generator.sg.TypeSG;
import org.apache.ws.jaxme.impl.JMParser;
import org.apache.ws.jaxme.impl.JMSAXGroupParser;
import org.apache.ws.jaxme.js.DirectAccessible;
import org.apache.ws.jaxme.js.JavaField;
import org.apache.ws.jaxme.js.JavaInnerClass;
import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.JavaQName;
import org.apache.ws.jaxme.js.JavaSource;
import org.apache.ws.jaxme.js.LocalJavaField;
import org.apache.ws.jaxme.js.Parameter;
import org.apache.ws.jaxme.js.TypedValue;
import org.apache.ws.jaxme.xs.xml.XsQName;
import org.xml.sax.SAXException;

/** An instance of <code>GroupHandlerSG</code> is
 * responsible for creating an instance of
 * {@link org.apache.ws.jaxme.impl.JMSAXGroupParser},
 * or {@link org.apache.ws.jaxme.impl.JMSAXElementParser}.<br>
 * The {@link org.apache.ws.jaxme.generator.sg.ComplexContentSG}
 * creates a <code>GroupHandlerSG</code> for any group,
 * contained in the element.
 */
public abstract class GroupHandlerSG extends HandlerSGImpl {

    protected final ComplexContentSG ccSG;

    protected final GroupSG group;

    protected final ParticleSG[] particles;

    private final Map groups = new HashMap();

    private JavaField stateField;

    private final boolean isMixed;

    protected final GroupHandlerSG outerHandler;

    /** Creates a new instance, which generates a handler for
	 * the complex type <code>pTypeSG</code> by adding methods
	 * and fields to the Java class <code>pJs</code>.
	 */
    protected GroupHandlerSG(ComplexTypeSG pType, JavaSource pJs) throws SAXException {
        super(pType, pJs);
        outerHandler = this;
        ccSG = pType.getComplexContentSG();
        group = ccSG.getGroupSG();
        particles = group.getParticles();
        isMixed = ccSG.isMixed();
        findGroups(particles);
    }

    /** Creates a new instance, which generates a handler for
	 * the group <code>pGroupSG</code> by adding methods and
	 * fields to the Java class <code>pJs</code>.
	 */
    protected GroupHandlerSG(GroupHandlerSG pOuterHandler, ComplexTypeSG pType, GroupSG pGroup, JavaSource pJs) throws SAXException {
        super(pType, pJs);
        outerHandler = pOuterHandler;
        if (!pJs.isInnerClass()) {
            throw new IllegalStateException("Expected inner class");
        }
        ccSG = null;
        group = pGroup;
        particles = pGroup.getParticles();
        isMixed = pType.getComplexContentSG().isMixed();
        findGroups(particles);
    }

    protected abstract JavaField newStateField() throws SAXException;

    protected JavaField getStateField() throws SAXException {
        if (stateField == null) {
            stateField = newStateField();
        }
        return stateField;
    }

    protected GroupHandlerSG getGroupHandlerSG(GroupSG pGroup) {
        return (GroupHandlerSG) groups.get(pGroup);
    }

    private GroupHandlerSG newGroupHandlerSG(GroupSG pGroup, String pName) throws SAXException {
        JavaSource js = getJavaSource();
        JavaInnerClass jic = js.newJavaInnerClass(pName, JavaSource.PUBLIC);
        jic.addExtends(JMSAXGroupParser.class);
        if (pGroup.isSequence()) {
            return new SequenceHandlerSG(outerHandler, ctSG, pGroup, jic);
        } else if (pGroup.isChoice()) {
            return new ChoiceHandlerSG(outerHandler, ctSG, pGroup, jic);
        } else if (pGroup.isAll()) {
            return new AllHandlerSG(outerHandler, ctSG, pGroup, jic);
        } else {
            throw new IllegalStateException("Invalid group type");
        }
    }

    private GroupHandlerSG newGroupHandlerSG(GroupSG pGroup) throws SAXException {
        JavaSource js = getJavaSource();
        String name = GroupUtil.getGroupName(pGroup);
        for (int i = 0; ; i++) {
            String n = name;
            if (i > 0) {
                name += i;
            }
            n += "Handler";
            if (js.getInnerClass(n) == null) {
                GroupHandlerSG result = newGroupHandlerSG(pGroup, n);
                result.newGetHandlerMethod(js);
                return result;
            }
        }
    }

    public JavaMethod newAddAttributeMethod() throws SAXException {
        if (ccSG == null) {
            return null;
        } else {
            return super.newAddAttributeMethod();
        }
    }

    private void findGroups(ParticleSG[] pParticles) throws SAXException {
        for (int i = 0; i < pParticles.length; i++) {
            ParticleSG particle = pParticles[i];
            if (particle.isGroup()) {
                GroupSG group = particle.getGroupSG();
                if (!groups.containsKey(group)) {
                    GroupHandlerSG handler = newGroupHandlerSG(group);
                    groups.put(group, handler);
                }
            } else if (particle.isElement()) {
                TypeSG tSG = particle.getObjectSG().getTypeSG();
                if (tSG.isComplex() && !tSG.isGlobalClass()) {
                    tSG.getComplexTypeSG().getXMLHandler(outerHandler.getJavaSource());
                }
            }
        }
    }

    protected boolean isRequiredParticle(ParticleSG particleSG) {
        return particleSG.getMinOccurs() > 0;
    }

    protected void handleStartOfChildElement(Object pUnmarshallerHandler, JavaMethod pJm, ParticleSG pParticle) {
        ObjectSG oSG = pParticle.getObjectSG();
        TypeSG tSG = oSG.getTypeSG();
        if (tSG.isComplex()) {
            JavaQName elementInterfaceClass = pParticle.getObjectSG().getClassContext().getXMLInterfaceName();
            Object[] o, h;
            if (oSG.getClassContext().isGlobal()) {
                LocalJavaField manager = pJm.newJavaField(JMManager.class);
                manager.addLine("((org.apache.ws.jaxme.impl.JAXBContextImpl)((org.apache.ws.jaxme.impl.JMControllerImpl)((org.apache.ws.jaxme.impl.JMUnmarshallerHandlerImpl)getHandler()).getJMUnmarshaller()).getJAXBContextImpl()).getManagerS(", elementInterfaceClass, ".class)");
                o = new Object[] { manager, ".getElementS();" };
                h = new Object[] { manager, ".getHandler();" };
            } else {
                LocalJavaField manager = pJm.newJavaField(JMManager.class);
                manager.addLine("((org.apache.ws.jaxme.impl.JAXBContextImpl)((org.apache.ws.jaxme.impl.JMControllerImpl)((org.apache.ws.jaxme.impl.JMUnmarshallerHandlerImpl)getHandler()).getJMUnmarshaller()).getJAXBContextImpl()).getManagerS(", elementInterfaceClass, ".class)");
                o = new Object[] { manager, ".getElementS();" };
                h = new Object[] { manager, ".getHandler();" };
            }
            LocalJavaField oField = pJm.newJavaField(Object.class);
            oField.addLine(o);
            LocalJavaField hField = pJm.newJavaField(JMParser.class);
            hField.addLine(h);
            XsQName name = oSG.getName();
            pJm.addLine(hField, ".init(", pUnmarshallerHandler, ", ", oField, ", ", JavaSource.getQuoted(name.getNamespaceURI()), ", ", JavaSource.getQuoted(name.getLocalName()), ", ", pUnmarshallerHandler, ".getLevel());");
            pJm.addLine(hField, ".setAttributes(", getParamAttrs(), ");");
            pJm.addLine(pUnmarshallerHandler, ".addElementParser(", hField, ");");
        } else {
            pJm.addLine("if(", pUnmarshallerHandler, " instanceof org.apache.ws.jaxme.impl.JMUnmarshallerHandlerImpl) {");
            pJm.addLine("((org.apache.ws.jaxme.impl.JMUnmarshallerHandlerImpl)", pUnmarshallerHandler, ").addSimpleAtomicState();");
            pJm.addLine("}");
        }
    }

    protected abstract void acceptParticle(JavaMethod pJm, int pNum) throws SAXException;

    protected void handleStartElementStates(Object pUnmarshallerHandler, JavaMethod pJm, int pFrom, int pTo) throws SAXException {
        if (pFrom < 0 || pFrom >= particles.length || pTo < 0 || pTo >= particles.length || pTo < pFrom) {
            return;
        }
        for (int i = pFrom; i <= pTo; i++) {
            ParticleSG particle = particles[i];
            if (particle.isElement()) {
                ObjectSG oSG = particle.getObjectSG();
                XsQName name = oSG.getName();
                Object uriCondition;
                if ("".equals(name.getNamespaceURI())) {
                    uriCondition = new Object[] { "(", getParamNamespaceURI(), " == null  ||  ", getParamNamespaceURI(), ".length() == 0)" };
                } else {
                    uriCondition = new Object[] { JavaSource.getQuoted(name.getNamespaceURI()), ".equals(", getParamNamespaceURI(), ")" };
                }
                pJm.addIf(i == pFrom, uriCondition, "  &&  ", JavaSource.getQuoted(name.getLocalName()), ".equals(", getParamLocalName(), ")");
                acceptParticle(pJm, i);
                handleStartOfChildElement(pUnmarshallerHandler, pJm, particle);
                pJm.addLine("return true;");
            } else if (particle.isGroup()) {
                GroupSG gSG = particle.getGroupSG();
                GroupHandlerSG handlerSG = getGroupHandlerSG(gSG);
                pJm.addIf(i == pFrom, pUnmarshallerHandler, ".testGroupParser(new ", handlerSG.getJavaSource().getQName(), "(), ", getParamNamespaceURI(), ", ", getParamLocalName(), ", ", getParamQName(), ", ", getParamAttrs(), ")");
                acceptParticle(pJm, i);
                pJm.addLine("return true;");
            } else if (particle.isWildcard()) {
                throw new IllegalStateException("TODO: Add support for wildcards");
            } else {
                throw new IllegalStateException("Invalid particle type");
            }
        }
        pJm.addEndIf();
    }

    protected abstract int getState(int pParticleNum);

    protected abstract DirectAccessible getEndElementState() throws SAXException;

    public JavaMethod newEndElementMethod() throws SAXException {
        JavaMethod result = super.newEndElementMethod();
        JavaQName elementInterface = ctSG.getClassContext().getXMLInterfaceName();
        LocalJavaField element = result.newJavaField(elementInterface);
        element.addLine("(", elementInterface, ") result");
        result.addSwitch(getEndElementState());
        for (int i = 0; i < particles.length; i++) {
            result.addCase(new Integer(getState(i)));
            ParticleSG particle = particles[i];
            handleEndElementState(result, element, particle);
        }
        result.addDefault();
        result.addThrowNew(IllegalStateException.class, JavaSource.getQuoted("Illegal state: "), " + ", getEndElementState());
        result.addEndSwitch();
        return result;
    }

    private void handleEndElementState(JavaMethod pJm, LocalJavaField pElement, ParticleSG pParticle) throws SAXException {
        if (pParticle.isElement()) {
            ObjectSG oSG = pParticle.getObjectSG();
            TypeSG childType = oSG.getTypeSG();
            XsQName name = oSG.getName();
            Object[] uriCondition;
            if ("".equals(name.getNamespaceURI())) {
                uriCondition = new Object[] { getParamNamespaceURI(), " == null  ||  ", getParamNamespaceURI(), ".length() == 0" };
            } else {
                uriCondition = new Object[] { JavaSource.getQuoted(name.getNamespaceURI()), ".equals(", getParamNamespaceURI(), ")" };
            }
            pJm.addIf(uriCondition, "  &&  ", JavaSource.getQuoted(name.getLocalName()), ".equals(", getParamLocalName(), ")");
            JavaQName type;
            TypedValue v = getParamResult();
            if (childType.isComplex()) {
                type = childType.getComplexTypeSG().getClassContext().getXMLInterfaceName();
            } else {
                v = createSimpleTypeConversion(pJm, childType, v, oSG.getName().toString());
                type = null;
                if (isMixed) {
                    LocalJavaField f = pJm.newJavaField(GroupUtil.getContentClass(group, pParticle, ctSG.getClassContext().getXMLInterfaceName()));
                    f.addLine("new ", GroupUtil.getContentClass(group, pParticle, ctSG.getClassContext().getXMLImplementationName()), "()");
                    pJm.addLine(f, ".setValue(", v, ");");
                    v = f;
                }
            }
            if (isMixed) {
                pJm.addLine(pElement, ".getContent().add(", v, ");");
            } else {
                pParticle.getPropertySG().addValue(pJm, pElement, v, type);
            }
            pJm.addLine("return;");
            pJm.addEndIf();
            pJm.addBreak();
        } else if (pParticle.isGroup()) {
            pJm.addThrowNew(IllegalStateException.class, JavaSource.getQuoted("This case should be handled by a nested group parser."));
        } else if (pParticle.isWildcard()) {
            throw new IllegalStateException("TODO: Add support for wildcards.");
        } else {
            throw new IllegalStateException("Invalid particle type");
        }
    }

    public JavaMethod newIsAtomicMethod() throws SAXException {
        return null;
    }

    public JavaMethod newIsEmptyMethod() throws SAXException {
        return null;
    }

    protected JavaMethod newIsMixedMethod() throws SAXException {
        if (!isMixed) {
            return null;
        }
        JavaMethod jm = getJavaSource().newJavaMethod("isMixed", boolean.class, JavaSource.PUBLIC);
        jm.addLine("return true;");
        return jm;
    }

    protected JavaMethod newAddTextMethod() throws SAXException {
        if (!isMixed) {
            return null;
        }
        JavaMethod jm = getJavaSource().newJavaMethod("addText", void.class, JavaSource.PUBLIC);
        Parameter buffer = jm.addParam(char[].class, "pBuffer");
        Parameter offset = jm.addParam(int.class, "pOffset");
        Parameter length = jm.addParam(int.class, "pLength");
        if (ccSG == null) {
            jm.addLine(outerHandler.getJavaSource().getQName(), ".this.addText(", buffer, ", ", offset, ", ", length, ");");
        } else {
            JavaQName elementInterface = ctSG.getClassContext().getXMLInterfaceName();
            LocalJavaField element = jm.newJavaField(elementInterface);
            element.addLine("(", elementInterface, ") result");
            Object s = new Object[] { "new ", String.class, "(", buffer, ", ", offset, ", ", length, ")" };
            LocalJavaField list = jm.newJavaField(List.class);
            list.addLine(element, ".getContent()");
            jm.addIf(list, ".size() > 0");
            LocalJavaField o = jm.newJavaField(Object.class);
            o.addLine(list, ".get(", list, ".size()-1)");
            jm.addIf(o, " instanceof ", String.class);
            jm.addLine(list, ".set(", list, ".size()-1, ", "((", String.class, ") ", o, ") + ", s, ");");
            jm.addLine("return;");
            jm.addEndIf();
            jm.addEndIf();
            jm.addLine(list, ".add(", s, ");");
        }
        return jm;
    }

    private JavaMethod newGetHandlerMethod(JavaSource pOuter) throws SAXException {
        JavaMethod result = getJavaSource().newJavaMethod("getHandler", UnmarshallerHandler.class, JavaSource.PUBLIC);
        result.addLine("return ", pOuter.getQName(), ".this.getHandler();");
        return result;
    }

    public void generate() throws SAXException {
        super.generate();
        newAddTextMethod();
        newIsMixedMethod();
        for (Iterator iter = groups.values().iterator(); iter.hasNext(); ) {
            HandlerSG handler = (HandlerSG) iter.next();
            handler.generate();
        }
    }
}
