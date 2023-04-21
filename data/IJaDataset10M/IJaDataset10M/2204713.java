package newgen.presentation.cataloguing;

import newgenlib.marccomponent.marcmodel.*;

/**
 *
 * @author  Administrator
 */
public interface CatalogueIntegrationInterface {

    public CatalogMaterialDescription catalogueRecordDescription();

    public void setCatalogueRecord(String catalogueRecordId, String ownerLibraryId);

    public void setVolumeInformation(String volumeId);

    public void refreshDetails();

    public void setAllVolumes(java.util.Hashtable htVolumes);

    public void setClassificationNumbersHT(java.util.Hashtable htClassification);
}
