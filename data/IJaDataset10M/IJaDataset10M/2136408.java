package de.fraunhofer.isst.axbench.operations.writer.autosar;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.axlang.utilities.Role;

/**
 * @brief Soll die Änderung eines aXLang-Modells in das generierte AUTOSAR-XML-Dokument übertragen.
 * Ist aber wohl zu kompliziert.
 * 
 * @author mgrosse
 *
 */
public class AUTOSARSynchronizer {

    private Document autosarDocument;

    private static final Namespace ANS = Namespace.getNamespace("http://autosar.org/3.1.1");

    private Map<IAXLangElement, Element> component2componentTypeMap;

    private Map<IAXLangElement, Element> component2internalBehaviorMap;

    private Map<IAXLangElement, Element> subcomponent2prototypeMap;

    private AUTOSARWriter autosarWriter;

    /**
	 * @brief constructor
	 * 
	 * @param oldModel the previous state of the aXLang model
	 * @param aDocument the previous state of the AUTOSAR XML document, must be consistent with the aXLang model
	 */
    public AUTOSARSynchronizer(IAXLangElement oldModel, Document aDocument) {
        autosarDocument = aDocument;
        AUTOSARRelationBuilder autosarRelationBuilder = new AUTOSARRelationBuilder(oldModel, autosarDocument.getRootElement());
        component2componentTypeMap = autosarRelationBuilder.component2componentTypeMap;
        component2internalBehaviorMap = autosarRelationBuilder.component2internalBehaviorMap;
        subcomponent2prototypeMap = autosarRelationBuilder.subcomponent2prototypeMap;
        autosarWriter = new AUTOSARWriter();
    }

    /**
	 * @brief updates the autosarDocument part w.r.t. the changes in the aXLang element 'Model'
	 * @param oldModel previous state of the Model element
	 * @param newModel new state of the Model element
	 * @return the updated autosarDocument
	 */
    public Document synchronize(IAXLangElement oldModel, IAXLangElement newModel) {
        Element ar_package = autosarDocument.getRootElement().getChild("TOP-LEVEL-PACKAGES", ANS).getChild("AR-PACKAGE", ANS);
        if (!newModel.getIdentifier().equals(oldModel.getIdentifier())) {
            ar_package.getChild("SHORT-NAME", ANS).setText("ARPackage_" + newModel.getIdentifier());
        }
        autosarWriter.setARPackageName(newModel.getIdentifier());
        Element elements = ar_package.getChild("ELEMENTS", ANS);
        Collection<String> oldComponentIdSet = new HashSet<String>();
        for (IAXLangElement oldComponent : oldModel.getChild(Role.APPLICATIONMODEL).getChildren(Role.COMPONENT)) {
            oldComponentIdSet.add(oldComponent.getIdentifier());
        }
        Collection<String> newComponentIdSet = new HashSet<String>();
        for (IAXLangElement newComponent : newModel.getChild(Role.APPLICATIONMODEL).getChildren(Role.COMPONENT)) {
            newComponentIdSet.add(newComponent.getIdentifier());
        }
        for (IAXLangElement newComponent : newModel.getChild(Role.APPLICATIONMODEL).getChildren(Role.COMPONENT)) {
            if (!oldComponentIdSet.contains(newComponent.getIdentifier())) {
                elements.addContent(autosarWriter.toAUTOSAR(newComponent));
            } else {
                IAXLangElement oldComponent = oldModel.getChild(Role.APPLICATIONMODEL).getChild(newComponent.getIdentifier());
                synchronizeComponent(oldComponent, newComponent, elements);
            }
        }
        for (String oldComponentId : oldComponentIdSet) {
            if (!newComponentIdSet.contains(oldComponentId)) {
                elements.removeContent(component2componentTypeMap.get(oldModel.getChild(Role.APPLICATIONMODEL).getChild(oldComponentId)));
                elements.removeContent(component2internalBehaviorMap.get(oldModel.getChild(Role.APPLICATIONMODEL).getChild(oldComponentId)));
            }
        }
        Iterator<Content> docIterator = autosarDocument.getDescendants();
        while (docIterator.hasNext()) {
            Content nextObject = docIterator.next();
            if (nextObject instanceof Element) {
                Element nextElement = (Element) nextObject;
                nextElement.setNamespace(ANS);
            }
        }
        return autosarDocument;
    }

    /**
	 * @brief updates the autosarDocument part w.r.t. the changes in the aXLang element 'Component'
	 * @param oldComponent previous state of the Component element
	 * @param newComponent new state of the Component element
	 * @param parentElement JDOM element ("ELEMENTS") that is updated
	 */
    private void synchronizeComponent(IAXLangElement oldComponent, IAXLangElement newComponent, Element parentElement) {
        if ((oldComponent.getChildren(Role.SUBCOMPONENT).size() == 0) && (newComponent.getChildren(Role.SUBCOMPONENT).size() != 0)) {
            Element oldElement = component2componentTypeMap.get(oldComponent);
            oldElement.setName("COMPOSITION-TYPE");
            Element components = new Element("COMPONENTS");
            oldElement.addContent(components);
            for (IAXLangElement newSubComponent : newComponent.getChildren(Role.SUBCOMPONENT)) {
                components.addContent(autosarWriter.toAUTOSAR(newSubComponent));
            }
        }
        if ((oldComponent.getChildren(Role.SUBCOMPONENT).size() != 0) && (newComponent.getChildren(Role.SUBCOMPONENT).size() == 0)) {
            Element oldElement = component2componentTypeMap.get(oldComponent);
            oldElement.setName("APPLICATION-SOFTWARE-COMPONENT-TYPE");
            oldElement.removeChild("COMPONENTS", ANS);
            oldElement.removeChild("CONNECTORS", ANS);
        }
        Collection<String> oldSubComponentIdSet = new HashSet<String>();
        for (IAXLangElement oldSubComponent : oldComponent.getChildren(Role.SUBCOMPONENT)) {
            oldSubComponentIdSet.add(oldSubComponent.getIdentifier());
        }
        Collection<String> newSubComponentIdSet = new HashSet<String>();
        for (IAXLangElement newSubComponent : newComponent.getChildren(Role.SUBCOMPONENT)) {
            newSubComponentIdSet.add(newSubComponent.getIdentifier());
        }
        for (IAXLangElement newSubComponent : newComponent.getChildren(Role.SUBCOMPONENT)) {
            if (!oldSubComponentIdSet.contains(newSubComponent.getIdentifier())) {
                parentElement.addContent(autosarWriter.toAUTOSAR(newSubComponent));
            } else {
                IAXLangElement oldSubComponent = oldComponent.getChild(newSubComponent.getIdentifier());
                synchronizeSubComponent(oldSubComponent, newSubComponent);
            }
        }
        for (String oldSubComponentId : oldSubComponentIdSet) {
            if (!newSubComponentIdSet.contains(oldSubComponentId)) {
                parentElement.removeContent(subcomponent2prototypeMap.get(oldComponent.getChild(oldSubComponentId)));
            }
        }
    }

    /**
	 * @brief updates the autosarDocument part w.r.t. the changes in the aXLang element 'SubComponent'
	 * @param oldSubComponent previous state of the SubComponent
	 * @param newSubComponent new state of the SubComponent
	 */
    private void synchronizeSubComponent(IAXLangElement oldSubComponent, IAXLangElement newSubComponent) {
        Element oldPrototype = subcomponent2prototypeMap.get(oldSubComponent);
        Element tmpComponentPrototype = autosarWriter.toAUTOSAR(newSubComponent);
        oldPrototype.removeChild("TYPE-TREF");
        oldPrototype.addContent(tmpComponentPrototype.getChild("TYPE-TREF").detach());
    }
}
