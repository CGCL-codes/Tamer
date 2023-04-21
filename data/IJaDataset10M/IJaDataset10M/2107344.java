package ispyb.server.data.ejb;

import ispyb.server.data.interfaces.DataCollectionFullValue;
import ispyb.server.util.ServerLogger;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

/**
 * @author Patrice Brenchereau
 * 
 * PLEASE NOTE: Currently the data can only be accessed in a read-only way.
 * If you need write access you have to change the class to write also to all columns.
 * 
 * @ejb.bean name="DataCollectionFull"
 *      description="Entity for tables DataCollection, Screening and ScreeningOutput"
 *		local-jndi-name="ispyb/DataCollectionFullLocalHome"
 *		view-type="local"
 *		type="BMP"
 *		transaction-type="Container"
 *		primkey-field="dataCollectionId"
 *

 * @ejb.value-object
 *    name="DataCollectionFull"
 *    match="*"
 *
 * @ejb.facade 
 * view-type="local" type="Stateless"
 * 
 * @ejb.resource-ref res-ref-name="jdbc/experimental"
 * 		res-type="javax.sql.DataSource"
 * 		res-auth="Container"
 * 
 * @jboss.resource-ref res-ref-name="jdbc/experimental"
 * 		jndi-name="${datasource.experimental.jndi.name}"
 * 
 * @jboss.container-configuration name="Instance Per Transaction BMP EntityBean"
 * 
 */
public abstract class DataCollectionFullBean implements EntityBean {

    /**
	 * The primary key.
	 */
    private Integer dataCollectionId = null;

    /**
	 * Value object.
	 */
    private DataCollectionFullValue value;

    /**
	 * EntityContext.
	 */
    private EntityContext ctx = null;

    /**
     * Overridden by generated entity bean to optimize JDBC access 
     */
    protected abstract void makeDirty();

    /**
	 * Initializes the application's properties.
	 */
    public void setEntityContext(EntityContext ctx) throws EJBException, RemoteException {
        this.ctx = ctx;
    }

    public void unsetEntityContext() throws EJBException {
        this.ctx = null;
    }

    /** 
	 * Not implemented
	 */
    public void ejbRemove() throws RemoveException, EJBException, RemoteException {
        ServerLogger.getInstance().error("ejbRemove: operation is not implemented");
        throw new RemoveException("Could not delete the data, since this functionality is not implemented.");
    }

    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

    /**
	 * Synchronizes object with database. 
	 */
    public void ejbLoad() throws EJBException {
        try {
            this.value = DataCollectionFullDAO.getInstance().load((Integer) this.ctx.getPrimaryKey());
            if (this.value == null) {
                ServerLogger.getInstance().error("ejbLoad: Could not load dataCollectionFull for dataCollectionId " + this.ctx.getPrimaryKey().toString());
                throw new NoSuchEntityException("Could not load dataCollectionFull for dataCollectionId " + this.ctx.getPrimaryKey().toString());
            }
            this.dataCollectionId = this.value.getDataCollectionId();
            if (ServerLogger.getInstance().isDebugEnabled()) {
                ServerLogger.getInstance().debug("ejbLoad: loaded the data for dataCollectionFull with dataCollectionId " + this.dataCollectionId);
            }
        } catch (SQLException e) {
            ServerLogger.getInstance().error("ejbLoad: could not load dataCollectionFull with dataCollectionId " + this.dataCollectionId, e);
            throw new EJBException("Could not load dataCollectionFull from DB: " + e.getMessage());
        }
    }

    /**
	 * Synchronizes object with database. 
	 */
    public void ejbStore() throws EJBException {
        ServerLogger.getInstance().debug("ejbStore: operation is not implemented");
    }

    public Integer ejbFindByPrimaryKey(Integer dataCollectionId) throws FinderException {
        if (dataCollectionId == null) {
            ServerLogger.getInstance().error("ejbFindByPrimaryKey: dataCollectionId is null");
            throw new ObjectNotFoundException("DataCollectionFull primary key is null");
        }
        try {
            Integer pk = DataCollectionFullDAO.getInstance().findByPrimaryKey(dataCollectionId);
            if (pk == null) {
                ServerLogger.getInstance().error("ejbFindByPrimaryKey: NodataCollectionFull found with dataCollectionId " + dataCollectionId.toString());
                throw new ObjectNotFoundException("No dataCollectionFull found with dataCollectionId " + dataCollectionId.toString());
            }
            return pk;
        } catch (SQLException e) {
            ServerLogger.getInstance().error("ejbFindByPrimaryKey: could not find dataCollectionFull with dataCollectionId=" + dataCollectionId, e);
            throw new FinderException("Could not find dataCollectionFull in DB: " + e.getMessage());
        }
    }

    public Collection<Integer> ejbFindBySessionId(Integer sessionId) throws FinderException {
        try {
            Collection<Integer> result = DataCollectionFullDAO.getInstance().findBySessionId(sessionId);
            return result;
        } catch (SQLException e) {
            ServerLogger.getInstance().error("ejbFindByPrimaryKey: could not find dataCollectionFull with sessionId=" + sessionId, e);
            throw new FinderException("Could not find dataCollectionFull in DB: " + e.getMessage());
        }
    }

    public Collection<Integer> ejbFindBySessionIdAndPrintable(Integer sessionId, java.lang.Byte printableForReport) throws FinderException {
        try {
            Collection<Integer> result = DataCollectionFullDAO.getInstance().findBySessionIdAndPrintable(sessionId, printableForReport);
            return result;
        } catch (SQLException e) {
            ServerLogger.getInstance().error("ejbFindByPrimaryKey: could not find dataCollectionFull with sessionId=" + sessionId, e);
            throw new FinderException("Could not find dataCollectionFull in DB: " + e.getMessage());
        }
    }

    public Collection<Integer> ejbFindByNameAndPrintable(java.lang.String name, java.lang.Byte printableForReport, java.lang.Integer proposalId) throws FinderException {
        try {
            Collection<Integer> result = DataCollectionFullDAO.getInstance().findByNameAndPrintable(name, printableForReport, proposalId);
            return result;
        } catch (SQLException e) {
            ServerLogger.getInstance().error("ejbFindByPrimaryKey: could not find dataCollectionFull with name=" + name, e);
            throw new FinderException("Could not find dataCollectionFull in DB: " + e.getMessage());
        }
    }

    public Collection<Integer> ejbFindBySampleName(java.lang.String sampleName, java.lang.Integer proposalId) throws FinderException {
        try {
            Collection<Integer> result = DataCollectionFullDAO.getInstance().findBySampleName(sampleName, proposalId);
            return result;
        } catch (SQLException e) {
            ServerLogger.getInstance().error("ejbFindByPrimaryKey: could not find dataCollectionFull with sampleName=" + sampleName, e);
            throw new FinderException("Could not find dataCollectionFull in DB: " + e.getMessage());
        }
    }

    public Collection<Integer> ejbFindBySampleId(Integer sampleId, java.lang.Integer proposalId) throws FinderException {
        try {
            Collection<Integer> result = DataCollectionFullDAO.getInstance().findBySampleId(sampleId, proposalId);
            return result;
        } catch (SQLException e) {
            ServerLogger.getInstance().error("ejbFindByPrimaryKey: could not find dataCollectionFull with sampleId=" + sampleId, e);
            throw new FinderException("Could not find dataCollectionFull in DB: " + e.getMessage());
        }
    }

    public Collection<Integer> ejbFindBySampleIdAndPrintable(Integer sampleId, java.lang.Byte printableForReport, java.lang.Integer proposalId) throws FinderException {
        try {
            Collection<Integer> result = DataCollectionFullDAO.getInstance().findBySampleIdAndPrintable(sampleId, printableForReport, proposalId);
            return result;
        } catch (SQLException e) {
            ServerLogger.getInstance().error("ejbFindByPrimaryKey: could not find dataCollectionFull with sampleId=" + sampleId, e);
            throw new FinderException("Could not find dataCollectionFull in DB: " + e.getMessage());
        }
    }

    public Collection<Integer> ejbFindByAcronymAndPrintable(java.lang.String acronym, java.lang.Byte printableForReport, java.lang.Integer proposalId) throws FinderException {
        try {
            Collection<Integer> result = DataCollectionFullDAO.getInstance().findByAcronymAndPrintable(acronym, printableForReport, proposalId);
            return result;
        } catch (SQLException e) {
            ServerLogger.getInstance().error("ejbFindByPrimaryKey: could not find dataCollectionFull with acronym=" + acronym, e);
            throw new FinderException("Could not find dataCollectionFull in DB: " + e.getMessage());
        }
    }

    public Collection<Integer> ejbFindByProteinAcronym(java.lang.String proteinAcronym, java.lang.Integer proposalId) throws FinderException {
        try {
            Collection<Integer> result = DataCollectionFullDAO.getInstance().findByProteinAcronym(proteinAcronym, proposalId);
            return result;
        } catch (SQLException e) {
            ServerLogger.getInstance().error("ejbFindByPrimaryKey: could not find dataCollectionFull with proteinAcronym=" + proteinAcronym, e);
            throw new FinderException("Could not find dataCollectionFull in DB: " + e.getMessage());
        }
    }

    public Collection<Integer> ejbFindByCustomQuery(Integer proposalId, java.lang.String sampleName, java.lang.String proteinAcronym, java.lang.String beamlineName, java.sql.Date experimentDateStart, java.sql.Date experimentDateEnd, Integer minNumberOfImages, Integer maxNumberOfImages, boolean onlyPrintableForReport, Integer maxRecords) throws FinderException {
        try {
            Collection<Integer> result = DataCollectionFullDAO.getInstance().findByCustomQuery(proposalId, sampleName, proteinAcronym, beamlineName, experimentDateStart, experimentDateEnd, minNumberOfImages, maxNumberOfImages, onlyPrintableForReport, maxRecords);
            return result;
        } catch (SQLException e) {
            ServerLogger.getInstance().error("ejbFindByPrimaryKey: could not find dataCollectionFull with the custom query.", e);
            throw new FinderException("Could not find dataCollectionFull in DB: " + e.getMessage());
        }
    }

    /**
	 * @ejb.create-method
	 */
    public Integer ejbCreate() throws CreateException {
        ServerLogger.getInstance().error("ejbCreate: operation is not implemented");
        throw new CreateException("Could not create the run mode, since this functionality is not implemented.");
    }

    /**
	 * Implemented by generated class
	 * @return
	 * 
	 * @ejb.interface-method
	 */
    public ispyb.server.data.interfaces.DataCollectionFullValue getValue() {
        return value;
    }

    /**
	 * The primary key is read-only since it is generated by the DB.
	 * @return Returns the pk (primary key).
     *
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Integer getDataCollectionId() {
        return this.dataCollectionId;
    }

    /**
	 * @ejb.interface-method
	 */
    public void setDataCollectionId(java.lang.Integer dataCollectionId) {
        this.dataCollectionId = dataCollectionId;
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Integer getBlSampleId() {
        return value.getBlSampleId();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setBlSampleId(java.lang.Integer blSampleId) {
        value.setBlSampleId(blSampleId);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Integer getSessionId() {
        return value.getSessionId();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setSessionId(java.lang.Integer sessionId) {
        value.setSessionId(sessionId);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Integer getDataCollectionNumber() {
        return value.getDataCollectionNumber();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setDataCollectionNumber(java.lang.Integer dataCollectionNumber) {
        value.setDataCollectionNumber(dataCollectionNumber);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.util.Date getStartTime() {
        return value.getStartTime();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setStartTime(java.util.Date startTime) {
        value.setStartTime(startTime);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.String getRunStatus() {
        return value.getRunStatus();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setRunStatus(java.lang.String runStatus) {
        value.setRunStatus(runStatus);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Double getAxisStart() {
        return value.getAxisStart();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setAxisStart(java.lang.Double axisStart) {
        value.setAxisStart(axisStart);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Double getAxisRange() {
        return value.getAxisRange();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setAxisRange(java.lang.Double axisRange) {
        value.setAxisRange(axisRange);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Integer getNumberOfImages() {
        return value.getNumberOfImages();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setNumberOfImages(java.lang.Integer numberOfImages) {
        value.setNumberOfImages(numberOfImages);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Double getExposureTime() {
        return value.getExposureTime();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setExposureTime(java.lang.Double exposureTime) {
        value.setExposureTime(exposureTime);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.String getImagePrefix() {
        return value.getImagePrefix();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setImagePrefix(java.lang.String imagePrefix) {
        value.setImagePrefix(imagePrefix);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Double getWavelength() {
        return value.getWavelength();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setWavelength(java.lang.Double wavelength) {
        value.setWavelength(wavelength);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Double getTransmission() {
        return value.getTransmission();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setTransmission(java.lang.Double transmission) {
        value.setTransmission(transmission);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Double getResolution() {
        return value.getResolution();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setResolution(java.lang.Double resolution) {
        value.setResolution(resolution);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Double getDetectorDistance() {
        return value.getDetectorDistance();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setXbeam(java.lang.Double xbeam) {
        value.setXbeam(xbeam);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Double getXbeam() {
        return value.getXbeam();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setYbeam(java.lang.Double ybeam) {
        value.setYbeam(ybeam);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Double getYbeam() {
        return value.getYbeam();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setDetectorDistance(java.lang.Double detectorDistance) {
        value.setDetectorDistance(detectorDistance);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.String getCrystalClass() {
        return value.getCrystalClass();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setCrystalClass(java.lang.String crystalClass) {
        value.setCrystalClass(crystalClass);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.String getComments() {
        return value.getComments();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setComments(java.lang.String comments) {
        value.setComments(comments);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Byte getPrintableForReport() {
        return value.getPrintableForReport();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setPrintableForReport(java.lang.Byte printableForReport) {
        value.setPrintableForReport(printableForReport);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.String getXtalSnapshotFullPath1() {
        return value.getXtalSnapshotFullPath1();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setXtalSnapshotFullPath1(java.lang.String xtalSnapshotFullPath1) {
        value.setXtalSnapshotFullPath1(xtalSnapshotFullPath1);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.String getXtalSnapshotFullPath2() {
        return value.getXtalSnapshotFullPath2();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setXtalSnapshotFullPath2(java.lang.String xtalSnapshotFullPath2) {
        value.setXtalSnapshotFullPath2(xtalSnapshotFullPath2);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.String getXtalSnapshotFullPath3() {
        return value.getXtalSnapshotFullPath3();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setXtalSnapshotFullPath3(java.lang.String xtalSnapshotFullPath3) {
        value.setXtalSnapshotFullPath3(xtalSnapshotFullPath3);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.String getXtalSnapshotFullPath4() {
        return value.getXtalSnapshotFullPath4();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setXtalSnapshotFullPath4(java.lang.String xtalSnapshotFullPath4) {
        value.setXtalSnapshotFullPath4(xtalSnapshotFullPath4);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Double getBeamSizeAtSampleX() {
        return value.getBeamSizeAtSampleX();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setBeamSizeAtSampleX(java.lang.Double beamSizeAtSampleX) {
        value.setBeamSizeAtSampleX(beamSizeAtSampleX);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Double getBeamSizeAtSampleY() {
        return value.getBeamSizeAtSampleY();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setBeamSizeAtSampleY(java.lang.Double beamSizeAtSampleY) {
        value.setBeamSizeAtSampleY(beamSizeAtSampleY);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.String getBeamLineName() {
        return value.getBeamLineName();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setBeamLineName(java.lang.String beamlineName) {
        value.setBeamLineName(beamlineName);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Integer getScreeningId() {
        return value.getScreeningId();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setScreeningId(java.lang.Integer screeningId) {
        value.setScreeningId(screeningId);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Integer getScreeningOutputId() {
        return value.getScreeningOutputId();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setScreeningOutputId(java.lang.Integer screeningOutputId) {
        value.setScreeningOutputId(screeningOutputId);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.String getStatusDescription() {
        return value.getStatusDescription();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setStatusDescription(java.lang.String statusDescription) {
        value.setStatusDescription(statusDescription);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Byte getScreeningSuccess() {
        return value.getScreeningSuccess();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setScreeningSuccess(java.lang.Byte screeningSuccess) {
        value.setScreeningSuccess(screeningSuccess);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.Integer getEnergyScanId() {
        return value.getEnergyScanId();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setEnergyScanId(java.lang.Integer energyScanId) {
        value.setEnergyScanId(energyScanId);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.String getAcronym() {
        return value.getAcronym();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setAcronym(java.lang.String acronym) {
        value.setAcronym(acronym);
    }

    /**
	 * @ejb.interface-method
     * @ejb.value-object
	 */
    public java.lang.String getSampleName() {
        return value.getSampleName();
    }

    /**
	 * @ejb.interface-method
	 */
    public void setSampleName(java.lang.String sampleName) {
        value.setSampleName(sampleName);
    }
}
