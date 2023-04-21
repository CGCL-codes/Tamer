package org.apache.axis2.wsdl.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * This class provides support for processing a WSDL4J defintion which includes imports.
 * It allows the imports to be processed into a single WSDL4J Definition object
 */
public class WSDL4JImportedWSDLHelper {

    protected static final Log log = LogFactory.getLog(WSDL4JImportedWSDLHelper.class);

    private static final boolean isTraceEnabled = log.isTraceEnabled();

    /**
     * The intention of this procedure is to process the imports. When
     * processing the imports the imported documents will be populating the
     * items in the main document recursivley
     *
     * @param wsdl4JDefinition
     */
    public static void processImports(Definition wsdl4JDefinition, List processedDocuments) {
        Map wsdlImports = wsdl4JDefinition.getImports();
        if (null != wsdlImports && !wsdlImports.isEmpty()) {
            Collection importsCollection = wsdlImports.values();
            for (Iterator iterator = importsCollection.iterator(); iterator.hasNext(); ) {
                Vector values = (Vector) iterator.next();
                for (int i = 0; i < values.size(); i++) {
                    Import wsdlImport = (Import) values.elementAt(i);
                    if (wsdlImport.getDefinition() != null) {
                        Definition importedDef = wsdlImport.getDefinition();
                        if (importedDef != null) {
                            String key = importedDef.getDocumentBaseURI();
                            if (key == null) {
                                key = importedDef.getTargetNamespace();
                            }
                            if (processedDocuments.contains(key)) {
                                return;
                            }
                            processedDocuments.add(key);
                            processImports(importedDef, processedDocuments);
                            Map namespaces = importedDef.getNamespaces();
                            Iterator keys = namespaces.keySet().iterator();
                            while (keys.hasNext()) {
                                Object key2 = keys.next();
                                if (!wsdl4JDefinition.getNamespaces().containsValue(namespaces.get(key2))) {
                                    wsdl4JDefinition.getNamespaces().put(key2, namespaces.get(key2));
                                }
                            }
                            wsdl4JDefinition.getNamespaces().putAll(namespaces);
                            Types t = importedDef.getTypes();
                            if (t != null) {
                                List typesList = t.getExtensibilityElements();
                                for (int j = 0; j < typesList.size(); j++) {
                                    Types types = wsdl4JDefinition.getTypes();
                                    if (types == null) {
                                        types = wsdl4JDefinition.createTypes();
                                        wsdl4JDefinition.setTypes(types);
                                    }
                                    types.addExtensibilityElement((ExtensibilityElement) typesList.get(j));
                                }
                            }
                            Map messagesMap = importedDef.getMessages();
                            wsdl4JDefinition.getMessages().putAll(messagesMap);
                            Map porttypeMap = importedDef.getPortTypes();
                            wsdl4JDefinition.getPortTypes().putAll(porttypeMap);
                            Map bindingMap = importedDef.getBindings();
                            wsdl4JDefinition.getBindings().putAll(bindingMap);
                            Map serviceMap = importedDef.getServices();
                            wsdl4JDefinition.getServices().putAll(serviceMap);
                            List extElementList = importedDef.getExtensibilityElements();
                            wsdl4JDefinition.getExtensibilityElements().addAll(extElementList);
                        }
                    }
                }
            }
        }
    }
}
