package co.edu.unal.ungrid.image.dicom.database;

import co.edu.unal.ungrid.image.dicom.core.Attribute;
import co.edu.unal.ungrid.image.dicom.core.AttributeList;
import co.edu.unal.ungrid.image.dicom.core.DicomException;
import co.edu.unal.ungrid.image.dicom.core.InformationEntity;
import co.edu.unal.ungrid.image.dicom.core.TagFromName;

/**
 * <p>
 * The
 * {@link co.edu.unal.ungrid.image.dicom.database.StudySeriesInstanceModel StudySeriesInstanceModel}
 * class supports a minimal DICOM Study/Series/Instance model.
 * </p>
 * 
 * <p>
 * Matching of each information entity is performed by using only the instance
 * UIDs, not the list of attributes that are used in
 * {@link co.edu.unal.ungrid.image.dicom.database.StudySeriesInstanceSelectiveMatchModel StudySeriesInstanceSelectiveMatchModel}.
 * </p>
 * 
 * <p>
 * Attributes of other DICOM entities than Study, Series and Instance are
 * included at the appropriate lower level entity.
 * </p>
 * 
 * @see co.edu.unal.ungrid.image.dicom.database.StudySeriesInstanceSelectiveMatchModel
 * @see co.edu.unal.ungrid.image.dicom.database.DicomDictionaryForStudySeriesInstanceModel
 * 
 * 
 */
public class StudySeriesInstanceModel extends DicomDatabaseInformationModel {

    /**
	 * @param databaseName
	 * @exception DicomException
	 */
    public StudySeriesInstanceModel(String databaseName) throws DicomException {
        super(databaseName, InformationEntity.STUDY, new DicomDictionaryForStudySeriesInstanceModel());
    }

    /**
	 * @param ie
	 *            the information entity
	 * @return true if the information entity is in the model
	 */
    protected boolean isInformationEntityInModel(InformationEntity ie) {
        return ie == InformationEntity.STUDY || ie == InformationEntity.SERIES || ie == InformationEntity.INSTANCE;
    }

    /**
	 * @param ie
	 *            the parent information entity
	 * @param concatenation
	 *            true if concatenations are to be considered in the model
	 * @return the child information entity
	 */
    public InformationEntity getChildTypeForParent(InformationEntity ie, boolean concatenation) {
        if (ie == InformationEntity.STUDY) return InformationEntity.SERIES; else if (ie == InformationEntity.SERIES) return InformationEntity.INSTANCE; else return null;
    }

    /**
	 * @param ie
	 *            the parent information entity
	 * @param concatenationUID
	 *            the ConcatenationUID, if present, else null, as a flag to use
	 *            concatenations in the model or not
	 * @return the child information entity
	 */
    private InformationEntity getChildTypeForParent(InformationEntity ie, String concatenationUID) {
        return getChildTypeForParent(ie, concatenationUID != null);
    }

    /**
	 * @param ie
	 *            the parent information entity
	 * @return the child information entity
	 */
    public InformationEntity getChildTypeForParent(InformationEntity ie) {
        return getChildTypeForParent(ie, true);
    }

    /**
	 * @param ie
	 *            the parent information entity
	 * @param list
	 *            an AttributeList, in which ConcatenationUID may or may not be
	 *            present,as a flag to use concatenations in the model or not
	 * @return the child information entity
	 */
    public InformationEntity getChildTypeForParent(InformationEntity ie, AttributeList list) {
        String concatenationUID = Attribute.getSingleStringValueOrNull(list, TagFromName.ConcatenationUid);
        return getChildTypeForParent(ie, concatenationUID);
    }

    /**
	 * @param ie
	 *            the information entity
	 * @return the name of a column in the table that describes the instance of
	 *         the information entity
	 */
    public String getDescriptiveColumnName(InformationEntity ie) {
        if (ie == InformationEntity.STUDY) return "StudyID"; else if (ie == InformationEntity.SERIES) return "SeriesNumber"; else if (ie == InformationEntity.INSTANCE) return "InstanceNumber"; else return null;
    }

    /**
	 * @param ie
	 *            the information entity
	 * @return the name of another column in the table that describes the
	 *         instance of the information entity
	 */
    public String getOtherDescriptiveColumnName(InformationEntity ie) {
        return null;
    }

    /**
	 * @param ie
	 *            the information entity
	 * @return the name of yet another column in the table that describes the
	 *         instance of the information entity
	 */
    public String getOtherOtherDescriptiveColumnName(InformationEntity ie) {
        if (ie == InformationEntity.STUDY) return "StudyDescription"; else if (ie == InformationEntity.SERIES) return "SeriesDescription"; else if (ie == InformationEntity.INSTANCE) return "ImageComments"; else return null;
    }

    /**
	 * @param ie
	 *            the information entity
	 * @return the name of the column in the table that describes the UID of the
	 *         information entity, or null if none
	 */
    public String getUIDColumnNameForInformationEntity(InformationEntity ie) {
        if (ie == InformationEntity.STUDY) return "StudyInstanceUID"; else if (ie == InformationEntity.SERIES) return "SeriesInstanceUID"; else if (ie == InformationEntity.INSTANCE) return "SOPInstanceUID"; else return null;
    }

    /**
	 * @param b
	 * @param list
	 * @param ie
	 * @exception DicomException
	 */
    protected void extendStatementStringWithMatchingAttributesForSelectedInformationEntity(StringBuffer b, AttributeList list, InformationEntity ie) throws DicomException {
        if (ie == InformationEntity.STUDY) {
            b.append("STUDY.StudyInstanceUID");
            appendExactOrIsNullMatch(b, getQuotedEscapedSingleStringValueOrNull(list.get(TagFromName.StudyInstanceUid)));
        } else if (ie == InformationEntity.SERIES) {
            b.append(" AND ");
            b.append("SERIES.SeriesInstanceUID");
            appendExactOrIsNullMatch(b, getQuotedEscapedSingleStringValueOrNull(list.get(TagFromName.SeriesInstanceUid)));
        } else if (ie == InformationEntity.INSTANCE) {
            b.append(" AND ");
            b.append("INSTANCE.SOPInstanceUID");
            appendExactOrIsNullMatch(b, getQuotedEscapedSingleStringValueOrNull(list.get(TagFromName.SopInstanceUid)));
        }
    }
}
