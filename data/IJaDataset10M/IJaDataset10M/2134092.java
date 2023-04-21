package org.fao.fenix.domain.upload.neww;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.fao.fenix.domain.upload.recordparsing.FieldParser;
import org.junit.Test;

public class MetadataLoaderTest {

    FieldParser fieldParser = new FieldParser();

    @Test
    public void testLoadCSVMetaDataFile() throws FileNotFoundException {
        FileInputStream mfile = new FileInputStream(new File("src/main/resources/datasets/Ethiopia Level 2 Population/Ethiopia Level 2 Population_Metadata.csv"));
        MetadataLoader metadataLoader = new MetadataLoader();
        NewDatasetMetaData datasetMetadata = metadataLoader.loadCSVMetaDataFile(mfile);
        assertEquals("PopulationDataset", datasetMetadata.getTitle());
        assertEquals("GAUL 2", datasetMetadata.getFeatureLevel());
        assertEquals("PopulationDataset", datasetMetadata.getTitle());
        assertEquals("(abstract)", datasetMetadata.getAbstractAbstract());
        assertEquals("(keywords)", datasetMetadata.getKeywords());
        assertEquals("(provider)", datasetMetadata.getProvider());
        assertEquals("FAO", datasetMetadata.getSource());
        assertEquals("Info-GIEWS-Workstation@fao.org", datasetMetadata.getContact());
        assertEquals(fieldParser.parseDate("01/01/1998"), datasetMetadata.getStartDate());
        assertEquals(fieldParser.parseDate("01/01/2006"), datasetMetadata.getEndDate());
        assertEquals(fieldParser.parseDate("18/03/2008"), datasetMetadata.getDateLastUpdate());
        assertEquals("1", datasetMetadata.getPeriodTypeCode());
        assertEquals("Public", datasetMetadata.getSharingCode());
        assertEquals("Input data", datasetMetadata.getDatastatus());
        assertEquals("Ethiopia", datasetMetadata.getGeographicName());
        assertEquals("GAUL 2", datasetMetadata.getFeatureLevel());
        assertEquals("NoCategoryDefinedYet, CategoryUnknown", datasetMetadata.getCategories());
        assertEquals("Africa", datasetMetadata.getRegion());
    }
}
