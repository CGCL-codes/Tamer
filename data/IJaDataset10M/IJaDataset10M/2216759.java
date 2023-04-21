package fr.cnes.sitools.dictionary;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import fr.cnes.sitools.common.store.SitoolsStoreXML;
import fr.cnes.sitools.dictionary.model.ConceptTemplate;

/**
 * Implementation of ConceptTemplateStore with XStream FilePersistenceStrategy
 * 
 * @author AKKA
 * 
 */
public final class ConceptTemplateStoreXML extends SitoolsStoreXML<ConceptTemplate> {

    /** default location for file persistence */
    private static final String COLLECTION_NAME = "templates";

    /**
   * Constructor with the XML file location
   * 
   * @param location
   *          directory for file persistence
   */
    public ConceptTemplateStoreXML(File location) {
        super(ConceptTemplate.class, location);
    }

    /**
   * Default Constructor
   */
    public ConceptTemplateStoreXML() {
        super(ConceptTemplate.class);
        File defaultLocation = new File(COLLECTION_NAME);
        init(defaultLocation);
    }

    /**
   * XStream FilePersistenceStrategy initialization
   * 
   * @param location
   *          Directory
   */
    public void init(File location) {
        Map<String, Class<?>> aliases = new ConcurrentHashMap<String, Class<?>>();
        aliases.put("ConceptTemplate", ConceptTemplate.class);
        this.init(location, aliases);
    }

    @Override
    public ConceptTemplate update(ConceptTemplate conceptTemplate) {
        ConceptTemplate result = null;
        for (Iterator<ConceptTemplate> it = getRawList().iterator(); it.hasNext(); ) {
            ConceptTemplate current = it.next();
            if (current.getId().equals(conceptTemplate.getId())) {
                getLog().info("Updating ConceptTemplate");
                result = current;
                current.setName(conceptTemplate.getName());
                current.setDescription(conceptTemplate.getDescription());
                current.setProperties(conceptTemplate.getProperties());
                it.remove();
                break;
            }
        }
        if (result != null) {
            getRawList().add(result);
        } else {
            getLog().info("ConceptTemplate not found.");
        }
        return result;
    }

    @Override
    public List<ConceptTemplate> retrieveByParent(String id) {
        return null;
    }

    @Override
    public String getCollectionName() {
        return "ConceptTemplate";
    }
}
