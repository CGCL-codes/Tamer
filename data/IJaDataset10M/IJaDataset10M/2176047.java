package org.tdwg.tapir;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

/**
 * <CODE>CapabilitiesResponse</CODE> represents a TAPIR capabilities
 * response.
 * It is a Java implementation porting TpCapabilitiesResponse by
 * Renato De Giovanni <renato [at] cria . org . br>.
 *
 * @version 	25 May. 2008
 * @author 	Nozomi `James' Ytow
 */
public class CapabilitiesResponse extends TAPIRResponse {

    protected static QName SEARCHABLE_ATTRIBUTE = new QName(TAPIR.URL, "searchable");

    protected static QName REQUIRED_ATTRIBUTE = new QName(TAPIR.URL, "required");

    protected boolean supportsXML;

    protected boolean supportsInventory;

    protected boolean supportsInventoryOnAnyConcepts;

    protected Vector<QueryTemplate> inventoryTemplates;

    protected boolean supportsSearch;

    protected boolean supportsSearchWithAnyOutputModels;

    protected Vector<OutputModel> knownOutputModels;

    protected Vector<QueryTemplate> searchTemplates;

    protected boolean supportsFilter;

    protected Hashtable<String, MappedSchema> mappedSchemata;

    protected Hashtable<String, MappedConcept> mappedConcepts;

    protected Vector<String> supportedVariables;

    protected Hashtable<String, String> settings;

    protected String schemaNamespace;

    protected String setting;

    public CapabilitiesResponse(InputStream xml, String namespace) throws TAPIRException {
        super(xml, namespace);
    }

    protected void preParse() {
        supportsXML = false;
        supportsInventory = false;
        supportsInventoryOnAnyConcepts = false;
        inventoryTemplates = new Vector<QueryTemplate>();
        supportsSearch = false;
        supportsSearchWithAnyOutputModels = false;
        knownOutputModels = new Vector<OutputModel>();
        supportsXML = false;
        searchTemplates = new Vector<QueryTemplate>();
        supportsFilter = false;
        mappedSchemata = new Hashtable<String, MappedSchema>();
        supportedVariables = new Vector<String>();
        settings = new Hashtable<String, String>();
        super.preParse();
    }

    public boolean supportsXML() {
        return supportsXML;
    }

    public boolean supportsInventory() {
        return supportsInventory;
    }

    public boolean supportsInventoryOnAnyConcepts() {
        return supportsInventoryOnAnyConcepts;
    }

    public Iterator<QueryTemplate> getInventoryTemplates() {
        if (inventoryTemplates == null) return null;
        return inventoryTemplates.iterator();
    }

    public boolean supportsSearch() {
        return supportsSearch;
    }

    public boolean supportsSearchWithAnyOutputModels() {
        return supportsSearchWithAnyOutputModels;
    }

    public Iterator<OutputModel> getKnownOutputModels() {
        return (knownOutputModels == null) ? null : knownOutputModels.iterator();
    }

    public Iterator<QueryTemplate> getSearchTemplates() {
        return (searchTemplates == null) ? null : searchTemplates.iterator();
    }

    public boolean supportsFilter() {
        return supportsFilter;
    }

    public Iterator<MappedSchema> getMappedSchemata() {
        return (mappedSchemata == null) ? null : mappedSchemata.values().iterator();
    }

    public Iterator<String> getMappedConcepts() {
        if (mappedConcepts == null) {
            mappedConcepts = new Hashtable<String, MappedConcept>();
            for (Iterator<MappedSchema> i = getMappedSchemata(); i.hasNext(); ) {
                MappedSchema s = i.next();
                for (Iterator<MappedConcept> concepts = s.getMappedConcepts(); concepts.hasNext(); ) {
                    MappedConcept c = concepts.next();
                    mappedConcepts.put(c.getId(), c);
                }
            }
        }
        return mappedConcepts.keySet().iterator();
    }

    public boolean mappedSchema(String namespace) {
        if (namespace == null) return false;
        return (null != mappedSchemata.get(namespace));
    }

    public boolean mappedConcept(String conceptId) {
        if (conceptId == null) return false;
        if (mappedConcepts == null) getMappedConcepts();
        return (null != mappedConcepts.get(conceptId));
    }

    public Iterator<String> getSupportedVariables() {
        return supportedVariables.iterator();
    }

    public String getSetting(String name) {
        return settings.get(name);
    }

    private String[] getLocationAlias(StartElement element) {
        String[] parameters = new String[2];
        Attribute a = element.getAttributeByName(DataModel.LOCATION_ATTRIBUTE);
        if (a != null) parameters[0] = a.getValue();
        a = element.getAttributeByName(DataModel.ALIAS_ATTRIBUTE);
        if (a != null) parameters[1] = a.getValue();
        return parameters;
    }

    private QueryTemplate createQueryTemplate(StartElement element) {
        try {
            String[] args = getLocationAlias(element);
            return new QueryTemplate(args[0], args[1]);
        } catch (TAPIRException e) {
        }
        return null;
    }

    protected void startElement(StartElement element) {
        String path = implode("/", inTags);
        if ("response/capabilities/operations/inventory".equals(path)) {
            supportsInventory = true;
        } else if ("response/capabilities/operations/search".equals(path)) {
            supportsSearch = true;
        } else if ("response/capabilities/operations/inventory/templates/template".equals(path)) {
            inventoryTemplates.add(createQueryTemplate(element));
        } else if ("response/capabilities/operations/search/templates/template".equals(path)) {
            searchTemplates.add(createQueryTemplate(element));
        } else if ("response/capabilities/operations/inventory/anyConcepts".equals(path)) {
            supportsInventoryOnAnyConcepts = true;
        } else if ("response/capabilities/operations/search/outputModels/anyOutputModels".equals(path)) {
            supportsSearchWithAnyOutputModels = true;
        } else if ("response/capabilities/operations/search/outputModels/knownOutputModels/outputModel".equals(path)) {
            String[] args = getLocationAlias(element);
            try {
                knownOutputModels.add(new OutputModel(args[0], args[1]));
            } catch (TAPIRException e) {
            }
        } else if ("response/capabilities/requests/encoding/xml".equals(path)) {
            supportsXML = true;
        } else if ("response/capabilities/requests/filter/encoding".equals(path)) {
            supportsFilter = true;
        } else if ("response/capabilities/variables/environment".equals(path)) {
            supportedVariables.add(element.getName().getLocalPart());
        } else if (inTags.size() == 4 && path.contains("response/capabilities/settings/")) {
            setting = element.getName().getLocalPart();
        } else if ("response/capabilities/concepts/schema".equals(path)) {
            String[] attrs = getLocationAlias(element);
            if (attrs[0] != null) {
                schemaNamespace = element.getAttributeByName(MappedSchema.NAMESPACE_ATTRIBUTE).getValue();
                try {
                    mappedSchemata.put(schemaNamespace, new MappedSchema(schemaNamespace, attrs[0], attrs[1]));
                } catch (TAPIRException e) {
                }
            }
        } else if ("response/capabilities/concepts/schema/mappedConcept".equals(path)) {
            Attribute a = element.getAttributeByName(MappedConcept.ID_ATTRIBUTE);
            if (a != null) {
                String id = a.getValue();
                a = element.getAttributeByName(SEARCHABLE_ATTRIBUTE);
                boolean searchable = (a == null) ? true : (a.getValue().equals("true"));
                a = element.getAttributeByName(REQUIRED_ATTRIBUTE);
                boolean required = (a == null) ? false : (a.getValue().equals("true"));
                a = element.getAttributeByName(MappedConcept.ID_ATTRIBUTE);
                String alias = (a == null) ? null : a.getValue();
                try {
                    mappedSchemata.get(schemaNamespace).addConcept(new MappedConcept(id, searchable, required, alias));
                } catch (TAPIRException e) {
                }
            }
        }
    }

    ;

    protected void characterData(Characters characters) {
        String path = implode("/", inTags);
        if (inTags.size() == 4 && path.contains("response/capabilities/settings/")) {
            settings.put(setting, characters.getData());
        }
    }
}
