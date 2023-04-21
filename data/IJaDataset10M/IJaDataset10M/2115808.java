package org.mcisb.ontology.miriam;

import java.util.*;
import uk.ac.ebi.miriam.lib.*;
import org.mcisb.ontology.*;
import org.mcisb.util.*;

/**
 *
 * @author Neil Swainston
 */
class WebServiceMiriamUtils extends MiriamUtils {

    @Override
    protected Map<String, Ontology> getOntologiesMap() throws Exception {
        final Map<String, Ontology> ontologies = new HashMap<String, Ontology>();
        final MiriamLink link = new MiriamLink();
        final String[] dataTypesName = link.getDataTypesName();
        for (int i = 0; i < dataTypesName.length; i++) {
            final String dataTypeName = dataTypesName[i];
            final String dataTypeURI = link.getDataTypeURI(dataTypeName);
            final String[] dataTypeURIs = link.getDataTypeURIs(dataTypeName);
            final String regularExpression = link.getDataTypePattern(dataTypeName);
            final String linkTemplate = CollectionUtils.getFirst(link.getLocations(dataTypeName, Ontology.WILDCARD));
            final Ontology ontology = new Ontology(dataTypeName, dataTypeURI, Arrays.asList(dataTypeURIs), linkTemplate, regularExpression);
            ontologies.put(dataTypeName, ontology);
        }
        return ontologies;
    }
}
