package org.impalaframework.module.source;

import java.util.List;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.spi.ModuleElementNames;
import org.impalaframework.module.spi.TypeReader;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class XMLModuleDefinitionSource extends BaseXmlModuleDefinitionSource {

    private TypeReaderRegistry typeReaderRegistry;

    private RootModuleDefinition rootModuleDefinition;

    public XMLModuleDefinitionSource() {
        this(TypeReaderRegistryFactory.getTypeReaderRegistry());
    }

    protected XMLModuleDefinitionSource(TypeReaderRegistry typeReaderRegistry) {
        super();
        this.typeReaderRegistry = typeReaderRegistry;
    }

    public RootModuleDefinition getModuleDefinition() {
        Element root = getRootElement();
        rootModuleDefinition = getRootModuleDefinition(root);
        readChildDefinitions(rootModuleDefinition, root, ModuleElementNames.MODULES_ELEMENT);
        readChildDefinitions(null, root, ModuleElementNames.SIBLINGS_ELEMENT);
        return rootModuleDefinition;
    }

    private void readChildDefinitions(ModuleDefinition definition, Element element, String elementName) {
        Element definitionsElement = DomUtils.getChildElementByTagName(element, elementName);
        if (definitionsElement != null) {
            readDefinitions(definition, definitionsElement);
        }
    }

    @SuppressWarnings("unchecked")
    private void readDefinitions(ModuleDefinition parentDefinition, Element definitionsElement) {
        List<Element> definitionElementList = DomUtils.getChildElementsByTagName(definitionsElement, ModuleElementNames.MODULE_ELEMENT);
        for (Element definitionElement : definitionElementList) {
            String name = getName(definitionElement);
            String type = getType(definitionElement);
            TypeReader typeReader = typeReaderRegistry.getTypeReader(type);
            ModuleDefinition childDefinition = typeReader.readModuleDefinition(parentDefinition, name, definitionElement);
            if (parentDefinition == null) {
                rootModuleDefinition.addSibling(childDefinition);
            }
            readChildDefinitions(childDefinition, definitionElement, ModuleElementNames.MODULES_ELEMENT);
        }
    }

    private RootModuleDefinition getRootModuleDefinition(Element root) {
        TypeReader typeReader = typeReaderRegistry.getTypeReader(ModuleTypes.ROOT);
        String name = getName(root);
        return (RootModuleDefinition) typeReader.readModuleDefinition(null, name, root);
    }
}
