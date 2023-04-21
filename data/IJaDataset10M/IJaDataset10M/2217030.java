package edu.psu.its.lionshare.metadata;

import edu.psu.its.lionshare.share.ShareManager;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;

public interface MetadataManager {

    public void initialize(ShareManager manager);

    public Object saveMetadata(MetadataType metadata);

    public MetadataType getMetadata(Object id) throws Exception;

    public MetadataType createMetadataType() throws Exception;

    public String getMetadataSchemaURI(Object metadata_id);

    public void deleteMetadata(Object id) throws Exception;

    public void updateMetadata(MetadataType metadata, Object id) throws Exception;

    public boolean isMetadataShared(Object id);

    public Map<String, OntologyType> getAvailableOntologies();

    public OntologyType getOntologyType(String uri);

    public void addOntology(String uri, String ontology, boolean overwrite) throws IOException;

    public String removeOntology(String uri);

    public MetadataExtractor getMetadataExtractorForFile(File file);

    public String getDefaultDisplay();

    public List<Object> query(Object query, Object type);
}
