package org.dicom4jserver.business.entities.support;

import org.dicom4jserver.dao.DaoLayer;

/**
 * base class for all business entities
 *
 * @since 0.2
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public class BaseBusiness {

    /**
	 * the DAO Layer
	 */
    private DaoLayer fDaoLayer;

    public boolean isValidDataSet() {
        return false;
    }

    public DaoLayer getDaoLayer() {
        return fDaoLayer;
    }

    public void setDaoLayer(DaoLayer daoLayer) {
        fDaoLayer = daoLayer;
    }
}
