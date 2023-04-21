package com.medcentrex.entity;

import com.medcentrex.interfaces.InvalidValueException;
import com.medcentrex.interfaces.Patient_HistoryEntity;
import com.medcentrex.interfaces.Patient_HistoryEntityData;
import com.medcentrex.interfaces.Patient_HistoryEntityHome;
import com.medcentrex.interfaces.Patient_HistoryEntityPK;
import com.medcentrex.interfaces.ServiceUnavailableException;
import com.medcentrex.interfaces.SequenceGenerator;
import com.medcentrex.interfaces.SequenceGeneratorHome;
import java.sql.Date;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import com.medcentrex.interfaces.PeopleEntity;

/**
 * The Entity bean represents a Patient_HistoryEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="Patient_HistoryEntity"
 *           display-name="Patient_HistoryEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/Patient_HistoryEntity"
 *           local-jndi-name="ejb/com/medcentrex/Patient_HistoryEntityLocal"
 * 			schema="patient_history"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="Patient_History"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 *				ref-name="com/medcentrex/SequenceGenerator"
 *
 * @ejb:ejb-ref ejb-name="PeopleEntity"
 *				ref-name="com/medcentrex/PeopleEntity"
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:data-object extends="com.medcentrex.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="java.util.Collection findAll()"
 *
 *
 * @ejb:finder signature="com.medcentrex.interfaces.Patient_HistoryEntity findByPerson_ID(java.lang.Integer pPerson_ID )"
 * 	query="select object(ob) from patient_history ob where ob.person_ID=?1"
 *
 * @jboss:table-name table-name="patient_history"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class Patient_HistoryEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pPatient_HistoryEntity The Value Object containing the Patient_HistoryEntity values
   *
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(Patient_HistoryEntityData pPatient_HistoryEntity) throws InvalidValueException {
        if (pPatient_HistoryEntity == null) {
            throw new InvalidValueException("object.undefined", "Patient_HistoryEntity");
        }
        if (pPatient_HistoryEntity.getPerson_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Patient_HistoryEntity", "Id" });
        }
        setPatient_History_ID(pPatient_HistoryEntity.getPatient_History_ID());
        setPerson_ID(pPatient_HistoryEntity.getPerson_ID());
        setAlcohol_Drug(pPatient_HistoryEntity.getAlcohol_Drug());
        setAnemia(pPatient_HistoryEntity.getAnemia());
        setArthritis(pPatient_HistoryEntity.getArthritis());
        setAsthma(pPatient_HistoryEntity.getAsthma());
        setBleeding(pPatient_HistoryEntity.getBleeding());
        setBowel(pPatient_HistoryEntity.getBowel());
        setCancer(pPatient_HistoryEntity.getCancer());
        setDiabetes(pPatient_HistoryEntity.getDiabetes());
        setEmphysema(pPatient_HistoryEntity.getEmphysema());
        setEpilepsy(pPatient_HistoryEntity.getEpilepsy());
        setGallstones(pPatient_HistoryEntity.getGallstones());
        setPatient_History_Date(pPatient_HistoryEntity.getPatient_History_Date());
        setGlaucoma(pPatient_HistoryEntity.getGlaucoma());
        setHeart(pPatient_HistoryEntity.getHeart());
        setHypertension(pPatient_HistoryEntity.getHypertension());
        setJaundice(pPatient_HistoryEntity.getJaundice());
        setKidney(pPatient_HistoryEntity.getKidney());
        setNervous(pPatient_HistoryEntity.getNervous());
        setParalysis(pPatient_HistoryEntity.getParalysis());
        setPhlebitis(pPatient_HistoryEntity.getPhlebitis());
        setPneumonia(pPatient_HistoryEntity.getPneumonia());
        setRheumatic_Fever(pPatient_HistoryEntity.getRheumatic_Fever());
        setSerious_Illness(pPatient_HistoryEntity.getSerious_Illness());
        setSTD(pPatient_HistoryEntity.getSTD());
        setThyroid(pPatient_HistoryEntity.getThyroid());
        setTuberculosis(pPatient_HistoryEntity.getTuberculosis());
        setUlcer(pPatient_HistoryEntity.getUlcer());
    }

    /**
   * Create and return a Patient_HistoryEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a Patient_HistoryEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public Patient_HistoryEntityData getValueObject() {
        Patient_HistoryEntityData lData = new Patient_HistoryEntityData();
        lData.setPatient_History_ID(getPatient_History_ID());
        lData.setPerson_ID(getPerson_ID());
        lData.setAlcohol_Drug(getAlcohol_Drug());
        lData.setAnemia(getAnemia());
        lData.setArthritis(getArthritis());
        lData.setAsthma(getAsthma());
        lData.setBleeding(getBleeding());
        lData.setBowel(getBowel());
        lData.setCancer(getCancer());
        lData.setDiabetes(getDiabetes());
        lData.setEmphysema(getEmphysema());
        lData.setEpilepsy(getEpilepsy());
        lData.setGallstones(getGallstones());
        lData.setGlaucoma(getGlaucoma());
        lData.setHeart(getHeart());
        lData.setHypertension(getHypertension());
        lData.setJaundice(getJaundice());
        lData.setPatient_History_Date(getPatient_History_Date());
        lData.setKidney(getKidney());
        lData.setNervous(getNervous());
        lData.setParalysis(getParalysis());
        lData.setPhlebitis(getPhlebitis());
        lData.setPneumonia(getPneumonia());
        lData.setRheumatic_Fever(getRheumatic_Fever());
        lData.setSerious_Illness(getSerious_Illness());
        lData.setSTD(getSTD());
        lData.setThyroid(getThyroid());
        lData.setTuberculosis(getTuberculosis());
        lData.setUlcer(getUlcer());
        return lData;
    }

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pPatient_HistoryEntity The Value Object containing the PeopleEntity values
   *
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(Patient_HistoryEntityData pPatient_HistoryEntity) throws InvalidValueException {
        if (pPatient_HistoryEntity == null) {
            throw new InvalidValueException("object.undefined", "Patient_HistoryEntity");
        }
        if (pPatient_HistoryEntity.getPerson_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Patient_HistoryEntity", "Id" });
        }
        setPerson_ID(pPatient_HistoryEntity.getPerson_ID());
        setAlcohol_Drug(pPatient_HistoryEntity.getAlcohol_Drug());
        setAnemia(pPatient_HistoryEntity.getAnemia());
        setArthritis(pPatient_HistoryEntity.getArthritis());
        setAsthma(pPatient_HistoryEntity.getAsthma());
        setBleeding(pPatient_HistoryEntity.getBleeding());
        setBowel(pPatient_HistoryEntity.getBowel());
        setCancer(pPatient_HistoryEntity.getCancer());
        setDiabetes(pPatient_HistoryEntity.getDiabetes());
        setEmphysema(pPatient_HistoryEntity.getEmphysema());
        setEpilepsy(pPatient_HistoryEntity.getEpilepsy());
        setGallstones(pPatient_HistoryEntity.getGallstones());
        setPatient_History_Date(pPatient_HistoryEntity.getPatient_History_Date());
        setGlaucoma(pPatient_HistoryEntity.getGlaucoma());
        setHeart(pPatient_HistoryEntity.getHeart());
        setHypertension(pPatient_HistoryEntity.getHypertension());
        setJaundice(pPatient_HistoryEntity.getJaundice());
        setKidney(pPatient_HistoryEntity.getKidney());
        setNervous(pPatient_HistoryEntity.getNervous());
        setParalysis(pPatient_HistoryEntity.getParalysis());
        setPhlebitis(pPatient_HistoryEntity.getPhlebitis());
        setPneumonia(pPatient_HistoryEntity.getPneumonia());
        setRheumatic_Fever(pPatient_HistoryEntity.getRheumatic_Fever());
        setSerious_Illness(pPatient_HistoryEntity.getSerious_Illness());
        setSTD(pPatient_HistoryEntity.getSTD());
        setThyroid(pPatient_HistoryEntity.getThyroid());
        setTuberculosis(pPatient_HistoryEntity.getTuberculosis());
        setUlcer(pPatient_HistoryEntity.getUlcer());
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "Patient_HistoryEntityBean [ " + getValueObject() + " ]";
    }

    /**
   * Retrive a unique creation id to use for this bean.  This will end up
   * demarcating this bean from others when it is stored as a record
   * in the database.
   *
   * @return Returns an Integereger that can be used as a unique creation id.
   *
   * @throws ServiceUnavailableException Indicating that it was not possible
   *                                     to retrieve a new unqiue ID because
   *                                     the service is not available
   **/
    private Integer generateUniqueId() throws ServiceUnavailableException {
        Integer lUniqueId = new Integer(-1);
        String lSequenceField = "Patient_History_ID";
        try {
            Context lContext = new InitialContext();
            String lSequenceName = (java.lang.String) lContext.lookup("java:comp/env/SequenceName");
            SequenceGeneratorHome lHome = (SequenceGeneratorHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/com/medcentrex/SequenceGenerator"), SequenceGeneratorHome.class);
            SequenceGenerator lBean = (SequenceGenerator) lHome.create();
            lUniqueId = lBean.getNextNumber(lSequenceName, lSequenceField);
            lBean.remove();
        } catch (NamingException ne) {
            throw new ServiceUnavailableException("Naming lookup failure: " + ne.getMessage());
        } catch (CreateException ce) {
            throw new ServiceUnavailableException("Failure while creating a generator session bean: " + ce.getMessage());
        } catch (RemoveException re) {
        } catch (RemoteException rte) {
            throw new ServiceUnavailableException("Remote exception occured while accessing generator session bean: " + rte.getMessage());
        }
        return lUniqueId;
    }

    /**
   * Retrieve the Patient_HistoryEntity's Patient_History_ID.
   *
   * @return Returns an Integer representing the Patient_History_ID of this Patient_HistoryEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Patient_History_ID"
   **/
    public abstract Integer getPatient_History_ID();

    /**
   * Set the Patient_HistoryEntity's Patient_History_ID.
   *
   * @param pPatient_History_ID The Patient_History_ID of this Patient_HistoryEntity.
   **/
    public abstract void setPatient_History_ID(java.lang.Integer pPatient_History_ID);

    /**
   * Retrieve the Patient_HistoryEntity's Person_ID.
   *
   * @return Returns an Integer representing the Person_ID of this Patient_HistoryEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Person_ID"
   **/
    public abstract Integer getPerson_ID();

    /**
   * Set the Patient_HistoryEntity's Person_ID.
   *
   * @param pPerson_ID The Person_ID of this Patient_HistoryEntity. Is set at creation time.
   **/
    public abstract void setPerson_ID(java.lang.Integer pPerson_ID);

    /**
   * @return Returns the Patient_History_Date of this Patient_HistoryEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Patient_History_Date"
   **/
    public abstract Date getPatient_History_Date();

    /**
   * Specify the Patient_History_Date of this Patient_HistoryEntity
   *
   * @param pPatient_History_Date Patient_History_Date of this Patient_HistoryEntity
   **/
    public abstract void setPatient_History_Date(Date pPatient_History_Date);

    /**
    * @return Returns the Alcohol_Drug of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Alcohol_Drug"
    **/
    public abstract Boolean getAlcohol_Drug();

    /**
    * Specify the Alcohol_Drug of this Patient_HistoryEntity
    *
    * @param pAlcohol_Drug Alcohol_Drug of this Patient_HistoryEntity
    **/
    public abstract void setAlcohol_Drug(Boolean pAlcohol_Drug);

    /**
    * @return Returns the Anemia of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Anemia"
    **/
    public abstract Boolean getAnemia();

    /**
    * Specify the Anemia of this Patient_HistoryEntity
    *
    * @param pAnemia Anemia of this Patient_HistoryEntity
    **/
    public abstract void setAnemia(Boolean pAnemia);

    /**
    * @return Returns the Arthritis of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Arthritis"
    **/
    public abstract Boolean getArthritis();

    /**
    * Specify the Arthritis of this Patient_HistoryEntity
    *
    * @param pArthritis Arthritis of this Patient_HistoryEntity
    **/
    public abstract void setArthritis(Boolean pArthritis);

    /**
    * @return Returns the Asthma of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Asthma"
    **/
    public abstract Boolean getAsthma();

    /**
    * Specify the Asthma of this Patient_HistoryEntity
    *
    * @param pAsthma Asthma of this Patient_HistoryEntity
    **/
    public abstract void setAsthma(Boolean pAsthma);

    /**
    * @return Returns the Bleeding of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Bleeding"
    **/
    public abstract Boolean getBleeding();

    /**
    * Specify the Bleeding of this Patient_HistoryEntity
    *
    * @param pBleeding Bleeding of this Patient_HistoryEntity
    **/
    public abstract void setBleeding(Boolean pBleeding);

    /**
    * @return Returns the Bowel of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Bowel"
    **/
    public abstract Boolean getBowel();

    /**
    * Specify the Bowel of this Patient_HistoryEntity
    *
    * @param pBowel Bowel of this Patient_HistoryEntity
    **/
    public abstract void setBowel(Boolean pBowel);

    /**
    * @return Returns the Cancer of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Cancer"
    **/
    public abstract Boolean getCancer();

    /**
    * Specify the Cancer of this Patient_HistoryEntity
    *
    * @param pCancer Cancer of this Patient_HistoryEntity
    **/
    public abstract void setCancer(Boolean pCancer);

    /**
    * @return Returns the Diabetes of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Diabetes"
    **/
    public abstract Boolean getDiabetes();

    /**
    * Specify the Diabetes of this Patient_HistoryEntity
    *
    * @param pDiabetes Diabetes of this Patient_HistoryEntity
    **/
    public abstract void setDiabetes(Boolean pDiabetes);

    /**
    * @return Returns the Emphysema of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Emphysema"
    **/
    public abstract Boolean getEmphysema();

    /**
    * Specify the Emphysema of this Patient_HistoryEntity
    *
    * @param pEmphysema Emphysema of this Patient_HistoryEntity
    **/
    public abstract void setEmphysema(Boolean pEmphysema);

    /**
    * @return Returns the Epilepsy of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Epilepsy"
    **/
    public abstract Boolean getEpilepsy();

    /**
    * Specify the Epilepsy of this Patient_HistoryEntity
    *
    * @param pEpilepsy Epilepsy of this Patient_HistoryEntity
    **/
    public abstract void setEpilepsy(Boolean pEpilepsy);

    /**
    * @return Returns the Gallstones of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Gallstones"
    **/
    public abstract Boolean getGallstones();

    /**
    * Specify the Gallstones of this Patient_HistoryEntity
    *
    * @param pGallstones Gallstones of this Patient_HistoryEntity
    **/
    public abstract void setGallstones(Boolean pGallstones);

    /**
    * @return Returns the Glaucoma of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Glaucoma"
    **/
    public abstract Boolean getGlaucoma();

    /**
    * Specify the Glaucoma of this Patient_HistoryEntity
    *
    * @param pGlaucoma Glaucoma of this Patient_HistoryEntity
    **/
    public abstract void setGlaucoma(Boolean pGlaucoma);

    /**
    * @return Returns the STD of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="STD"
    **/
    public abstract Boolean getSTD();

    /**
    * Specify the STD of this Patient_HistoryEntity
    *
    * @param pSTD STD of this Patient_HistoryEntity
    **/
    public abstract void setSTD(Boolean pSTD);

    /**
    * @return Returns the Heart of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Heart"
    **/
    public abstract Boolean getHeart();

    /**
    * Specify the Heart of this Patient_HistoryEntity
    *
    * @param pHeart Heart of this Patient_HistoryEntity
    **/
    public abstract void setHeart(Boolean pHeart);

    /**
    * @return Returns the Jaundice of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Jaundice"
    **/
    public abstract Boolean getJaundice();

    /**
    * Specify the Jaundice of this Patient_HistoryEntity
    *
    * @param pJaundice Jaundice of this Patient_HistoryEntity
    **/
    public abstract void setJaundice(Boolean pJaundice);

    /**
    * @return Returns the Hypertension of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Hypertension"
    **/
    public abstract Boolean getHypertension();

    /**
    * Specify the Hypertension of this Patient_HistoryEntity
    *
    * @param pHypertension Hypertension of this Patient_HistoryEntity
    **/
    public abstract void setHypertension(Boolean pHypertension);

    /**
    * @return Returns the Kidney of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Kidney"
    **/
    public abstract Boolean getKidney();

    /**
    * Specify the Kidney of this Patient_HistoryEntity
    *
    * @param pKidney Kidney of this Patient_HistoryEntity
    **/
    public abstract void setKidney(Boolean pKidney);

    /**
    * @return Returns the Phlebitis of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Phlebitis"
    **/
    public abstract Boolean getPhlebitis();

    /**
    * Specify the Phlebitis of this Patient_HistoryEntity
    *
    * @param pPhlebitis Phlebitis of this Patient_HistoryEntity
    **/
    public abstract void setPhlebitis(Boolean pPhlebitis);

    /**
    * @return Returns the Pneumonia of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Pneumonia"
    **/
    public abstract Boolean getPneumonia();

    /**
    * Specify the Pneumonia of this Patient_HistoryEntity
    *
    * @param pPneumonia Pneumonia of this Patient_HistoryEntity
    **/
    public abstract void setPneumonia(Boolean pPneumonia);

    /**
    * @return Returns the Rheumatic_Fever of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Rheumatic_Fever"
    **/
    public abstract Boolean getRheumatic_Fever();

    /**
    * Specify the Rheumatic_Fever of this Patient_HistoryEntity
    *
    * @param pRheumatic_Fever Rheumatic_Fever of this Patient_HistoryEntity
    **/
    public abstract void setRheumatic_Fever(Boolean pRheumatic_Fever);

    /**
    * @return Returns the Ulcer of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Ulcer"
    **/
    public abstract Boolean getUlcer();

    /**
    * Specify the Ulcer of this Patient_HistoryEntity
    *
    * @param pUlcer Ulcer of this Patient_HistoryEntity
    **/
    public abstract void setUlcer(Boolean pUlcer);

    /**
    * @return Returns the Nervous of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Nervous"
    **/
    public abstract Boolean getNervous();

    /**
    * Specify the Nervous of this Patient_HistoryEntity
    *
    * @param pNervous Nervous of this Patient_HistoryEntity
    **/
    public abstract void setNervous(Boolean pNervous);

    /**
    * @return Returns the Paralysis of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Paralysis"
    **/
    public abstract Boolean getParalysis();

    /**
    * Specify the Paralysis of this Patient_HistoryEntity
    *
    * @param pParalysis Paralysis of this Patient_HistoryEntity
    **/
    public abstract void setParalysis(Boolean pParalysis);

    /**
    * @return Returns the Tuberculosis of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Tuberculosis"
    **/
    public abstract Boolean getTuberculosis();

    /**
    * Specify the Tuberculosis of this Patient_HistoryEntity
    *
    * @param pTuberculosis Tuberculosis of this Patient_HistoryEntity
    **/
    public abstract void setTuberculosis(Boolean pTuberculosis);

    /**
    * @return Returns the Thyroid of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Thyroid"
    **/
    public abstract Boolean getThyroid();

    /**
    * Specify the Thyroid of this Patient_HistoryEntity
    *
    * @param pThyroid Thyroid of this Patient_HistoryEntity
    **/
    public abstract void setThyroid(Boolean pThyroid);

    /**
    * @return Returns the Serious_Illness of this Patient_HistoryEntity
    *
    * @ejb:persistent-field
    *
    * @jboss:column-name name="Serious_Illness"
    **/
    public abstract String getSerious_Illness();

    /**
    * Specify the Serious_Illness of this Patient_HistoryEntity
    *
    * @param pSerious_Illness Serious_Illness of this Patient_HistoryEntity
    **/
    public abstract void setSerious_Illness(java.lang.String pSerious_Illness);

    /**
   * Create a Patient_HistoryEntity based on the supplied Patient_HistoryEntity Value Object.
   *
   * @param pPatient_HistoryEntity The data used to create the Patient_HistoryEntity.
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @throws EJBException If no new unique ID could be retrieved this will
   *                      rollback the transaction because there is no
   *                      hope to try again
   * @throws CreateException Because we have to do so (EJB spec.)
   *
   * @ejb:create-method view-type="remote"
   **/
    public Patient_HistoryEntityPK ejbCreate(Patient_HistoryEntityData pPatient_HistoryEntity) throws InvalidValueException, EJBException, CreateException {
        Patient_HistoryEntityData lData = (Patient_HistoryEntityData) pPatient_HistoryEntity.clone();
        try {
            lData.setPatient_History_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(Patient_HistoryEntityData pPatient_HistoryEntity) {
    }

    public void setEntityContext(EntityContext lContext) {
        mContext = lContext;
    }

    public void unsetEntityContext() {
        mContext = null;
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbLoad() {
    }

    public void ejbStore() {
    }

    public void ejbRemove() throws RemoveException {
    }
}
