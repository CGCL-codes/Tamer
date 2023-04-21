package org.dicom4jserver.dao.beans.support;

import java.sql.Timestamp;
import org.dicom4j.data.DataSet;

/**
 * Base class of Dicom Bean
 * 
 * @since 0.0.3
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public abstract class DicomEntityBean extends BaseBean {

    private byte[] fAttributes;

    private Timestamp fCreatedDateTime;

    private Timestamp fUpdatedDateTime;

    public DicomEntityBean() {
        super();
        fCreatedDateTime = new Timestamp(System.currentTimeMillis());
        UpdateUpdatedDateTime();
    }

    public byte[] getAttibutes() {
        return fAttributes;
    }

    public void setAttibutes(byte[] aData) {
        fAttributes = aData;
    }

    public Timestamp getCreatedDateTime() {
        return fCreatedDateTime;
    }

    public Timestamp getUpdatedDateTime() {
        return fUpdatedDateTime;
    }

    public void UpdateUpdatedDateTime() {
        fUpdatedDateTime = new Timestamp(System.currentTimeMillis());
    }

    /**
	 * Enhance Bean Data from a DataSet
	 * @param aDataSet the DataSet
	 * @throws Exception
	 */
    public abstract void enhanceFromDataSet(DataSet aDataSet) throws Exception;
}
