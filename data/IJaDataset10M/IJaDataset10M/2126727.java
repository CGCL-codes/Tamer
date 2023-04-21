package org.gbif.portal.dao.occurrence;

import java.util.List;
import org.gbif.portal.model.occurrence.ImageRecord;
import org.gbif.portal.model.occurrence.ORImage;

/**
 * DAO Implementation for accessing ImageRecord model objects.
 * 
 * @author Donald Hobern
 */
public interface ImageRecordDAO {

    /**
	 * Returns the ImageRecords for an occurrence record.
	 * 
	 * @param occurrenceRecordId identifier for an occurrence record
	 * @return List of ImageRecord objects.
	 */
    public List<ImageRecord> getImageRecordsForOccurrenceRecord(long occurrenceRecordId);

    /**
	 * Returns the ImageRecords for an occurrence record.
	 * 
	 * @param occurrenceRecordId identifier for an occurrence record
	 * @return List of ImageRecord objects.
	 */
    public List<ORImage> getImageRecordsForOccurrenceRecords(List<Long> occurrenceRecordIds);

    /**
	 * Returns the ImageRecords for an taxon concept.
	 * 
	 * TODO - handle nub concepts properly
	 * 
	 * @param taxonConceptId identifier for a taxon concept
	 * @return List of ImageRecord objects.
	 */
    public List<ImageRecord> getImageRecordsForTaxonConcept(long taxonConceptId);

    /**
	 * Returns the ImageRecords for an taxon concept.
	 * 
	 * TODO - handle nub concepts properly
	 * 
	 * @param taxonConceptId identifier for a taxon concept
	 * @return List of ImageRecord objects.
	 */
    public ImageRecord getImageRecordFor(long imageRecordId);
}
