package xsd2als.translation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalElement;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelElement;
import xsd2als.als.model.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Aaron Evans
 */
public class ElementTranslator {

    /** Commons logging */
    private static Log log = LogFactory.getFactory().getInstance(ElementTranslator.class);

    private static Map<String, FunctionCall> elementDefinitions = new HashMap<String, FunctionCall>();

    private static List<String> elementNames = new ArrayList<String>();

    public static FunctionCall referenceElement(String elementName) {
        return new FunctionCall(elementDefinitions.get(elementName));
    }

    public static void addTopLevelElement(TopLevelElement element) throws TranslationException {
        log.info("Add top level element " + element);
        if (element.getComplexType() != null) {
            addComplexElement(element);
        } else if (element.getType() != null) {
            addReferenceElement(element);
        } else if (element.getBlock() == null) {
            addEmptyElement(element);
        } else {
            log.warn("Unknown top level element: " + element);
        }
    }

    private static void addEmptyElement(TopLevelElement element) {
        String elementName = element.getName();
        log.info("Add empty element " + elementName);
        SignatureDeclaration elementSignature = new SignatureDeclaration(elementName, AlloyModel.getSimpleElement());
        AlloyModel.addDeclaration(elementSignature);
        FunctionCall referenceCall = new FunctionCall("match");
        referenceCall.addParameter(new RawExpression(elementName));
        elementDefinitions.put(elementName, referenceCall);
    }

    private static void addComplexElement(TopLevelElement element) {
        String elementName = element.getName();
        log.info("Add element " + elementName);
        SignatureDeclaration elementSignature = new SignatureDeclaration(elementName, AlloyModel.getComplexElement());
        AlloyModel.addDeclaration(elementSignature);
        FunctionCall childRef = ComplexTypeTranslator.addComplexType(element.getComplexType());
        FunctionDeclaration elementFunctionDeclaration = new FunctionDeclaration(elementName + "_e");
        elementFunctionDeclaration.addParameter(new VarName("x"), AlloyModel.getRootElement().getTypeReference());
        elementFunctionDeclaration.setReturnType(AlloyModel.getRootElement().getTypeReference());
        childRef.addParameter(new RawExpression("z.(xml/tree.fchild)"));
        StringBuffer function = new StringBuffer();
        function.append("{ z : x | ");
        function.append("let w1 = " + childRef + " | let r1 = right[w1] | ");
        function.append("some w1 && no r1 && z in " + elementSignature.getTypeReference());
        function.append("}");
        elementFunctionDeclaration.setDefinition(new RawExpression(function.toString()));
        AlloyModel.addDeclaration(elementFunctionDeclaration);
        elementDefinitions.put(elementName, elementFunctionDeclaration.getReference());
    }

    private static void addReferenceElement(TopLevelElement element) {
        String elementName = element.getName();
        log.info("Add reference element " + elementName);
        SignatureDeclaration elementSignature = new SignatureDeclaration(elementName, AlloyModel.getComplexElement());
        AlloyModel.addDeclaration(elementSignature);
        FunctionCall childRef = ComplexTypeTranslator.referenceComplexType(element.getType().getLocalPart());
        FunctionDeclaration elementFunctionDeclaration = new FunctionDeclaration(elementName + "_e");
        elementFunctionDeclaration.addParameter(new VarName("x"), AlloyModel.getRootElement().getTypeReference());
        elementFunctionDeclaration.setReturnType(AlloyModel.getRootElement().getTypeReference());
        childRef.addParameter(new RawExpression("z.(xml/tree.fchild)"));
        StringBuffer function = new StringBuffer();
        function.append("{ z : x | ");
        function.append("let w1 = " + childRef + " | let r1 = right[w1] | ");
        function.append("some w1 && no r1 && z in " + elementSignature.getTypeReference());
        function.append("}");
        elementFunctionDeclaration.setDefinition(new RawExpression(function.toString()));
        AlloyModel.addDeclaration(elementFunctionDeclaration);
        elementDefinitions.put(elementName, elementFunctionDeclaration.getReference());
    }

    public static FunctionCall addLocalElement(LocalElement element) {
        log.info("Add element " + element);
        FunctionCall result;
        if (element.getName() != null && element.getComplexType() != null) {
            result = addComplexElement(element);
        } else if (element.getName() != null) {
            log.info("Local simple element");
            if (!elementNames.contains(element.getName())) {
                SignatureDeclaration localElementSig = new SignatureDeclaration(element.getName(), AlloyModel.getSimpleElement());
                AlloyModel.addDeclaration(localElementSig);
                elementNames.add(element.getName());
            }
            result = new FunctionCall("match");
            result.addParameter(new RawExpression(element.getName()));
        } else if (element.getRef() != null) {
            String referenceName = element.getRef().getLocalPart();
            log.info("Local element reference to " + referenceName);
            return elementDefinitions.get(referenceName);
        } else {
            log.warn("Unknown local element " + element);
            result = null;
        }
        return result;
    }

    private static FunctionCall addComplexElement(LocalElement element) {
        String elementName = element.getName();
        log.info("Add element " + elementName);
        SignatureDeclaration elementSignature = new SignatureDeclaration(elementName, AlloyModel.getComplexElement());
        AlloyModel.addDeclaration(elementSignature);
        FunctionCall childRef = ComplexTypeTranslator.addComplexType(element.getComplexType());
        FunctionDeclaration elementFunctionDeclaration = new FunctionDeclaration(elementName + "_e");
        elementFunctionDeclaration.addParameter(new VarName("x"), AlloyModel.getRootElement().getTypeReference());
        elementFunctionDeclaration.setReturnType(AlloyModel.getRootElement().getTypeReference());
        childRef.addParameter(new RawExpression("z.(xml/tree.fchild)"));
        StringBuffer function = new StringBuffer();
        function.append("{ z : x | ");
        function.append("let w1 = " + childRef + " | let r1 = right[w1] | ");
        function.append("some w1 && no r1 && z in " + elementSignature.getTypeReference());
        function.append("}");
        elementFunctionDeclaration.setDefinition(new RawExpression(function.toString()));
        AlloyModel.addDeclaration(elementFunctionDeclaration);
        elementDefinitions.put(elementName, elementFunctionDeclaration.getReference());
        return elementFunctionDeclaration.getReference();
    }
}
