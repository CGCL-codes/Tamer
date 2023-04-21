package it.webscience.uima.annotations.eventData;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

/** 
 * Updated by JCasGen Tue Feb 22 11:24:36 CET 2011
 * XML source: C:/WebScience/Progetti/K-People/OntologyController_UIMA/apache-uima/examples/descriptors/webscience/typeSystems/EventDataTypeSystem.xml
 * @generated */
public class AttachmentAnnotation extends Annotation {

    /** @generated
   * @ordered 
   */
    public static final int typeIndexID = JCasRegistry.register(AttachmentAnnotation.class);

    /** @generated
   * @ordered 
   */
    public static final int type = typeIndexID;

    /** @generated  */
    public int getTypeIndexID() {
        return typeIndexID;
    }

    /** Never called.  Disable default constructor
   * @generated */
    protected AttachmentAnnotation() {
    }

    /** Internal - constructor used by generator 
   * @generated */
    public AttachmentAnnotation(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public AttachmentAnnotation(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public AttachmentAnnotation(JCas jcas, int begin, int end) {
        super(jcas);
        setBegin(begin);
        setEnd(end);
        readObject();
    }

    /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
    private void readObject() {
    }

    /** getter for id - gets 
   * @generated */
    public String getId() {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_id == null) jcasType.jcas.throwFeatMissing("id", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        return jcasType.ll_cas.ll_getStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_id);
    }

    /** setter for id - sets  
   * @generated */
    public void setId(String v) {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_id == null) jcasType.jcas.throwFeatMissing("id", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        jcasType.ll_cas.ll_setStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_id, v);
    }

    /** getter for attachmentType - gets 
   * @generated */
    public String getAttachmentType() {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_attachmentType == null) jcasType.jcas.throwFeatMissing("attachmentType", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        return jcasType.ll_cas.ll_getStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_attachmentType);
    }

    /** setter for attachmentType - sets  
   * @generated */
    public void setAttachmentType(String v) {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_attachmentType == null) jcasType.jcas.throwFeatMissing("attachmentType", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        jcasType.ll_cas.ll_setStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_attachmentType, v);
    }

    /** getter for attachmentData - gets 
   * @generated */
    public String getAttachmentData() {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_attachmentData == null) jcasType.jcas.throwFeatMissing("attachmentData", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        return jcasType.ll_cas.ll_getStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_attachmentData);
    }

    /** setter for attachmentData - sets  
   * @generated */
    public void setAttachmentData(String v) {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_attachmentData == null) jcasType.jcas.throwFeatMissing("attachmentData", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        jcasType.ll_cas.ll_setStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_attachmentData, v);
    }

    /** getter for attachmentName - gets 
   * @generated */
    public String getAttachmentName() {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_attachmentName == null) jcasType.jcas.throwFeatMissing("attachmentName", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        return jcasType.ll_cas.ll_getStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_attachmentName);
    }

    /** setter for attachmentName - sets  
   * @generated */
    public void setAttachmentName(String v) {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_attachmentName == null) jcasType.jcas.throwFeatMissing("attachmentName", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        jcasType.ll_cas.ll_setStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_attachmentName, v);
    }

    /** getter for hashcode - gets 
   * @generated */
    public String getHashcode() {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_hashcode == null) jcasType.jcas.throwFeatMissing("hashcode", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        return jcasType.ll_cas.ll_getStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_hashcode);
    }

    /** setter for hashcode - sets  
   * @generated */
    public void setHashcode(String v) {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_hashcode == null) jcasType.jcas.throwFeatMissing("hashcode", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        jcasType.ll_cas.ll_setStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_hashcode, v);
    }

    /** getter for urlAttachment - gets 
   * @generated */
    public String getUrlAttachment() {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_urlAttachment == null) jcasType.jcas.throwFeatMissing("urlAttachment", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        return jcasType.ll_cas.ll_getStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_urlAttachment);
    }

    /** setter for urlAttachment - sets  
   * @generated */
    public void setUrlAttachment(String v) {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_urlAttachment == null) jcasType.jcas.throwFeatMissing("urlAttachment", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        jcasType.ll_cas.ll_setStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_urlAttachment, v);
    }

    /** getter for author - gets 
   * @generated */
    public String getAuthor() {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_author == null) jcasType.jcas.throwFeatMissing("author", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        return jcasType.ll_cas.ll_getStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_author);
    }

    /** setter for author - sets  
   * @generated */
    public void setAuthor(String v) {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_author == null) jcasType.jcas.throwFeatMissing("author", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        jcasType.ll_cas.ll_setStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_author, v);
    }

    /** getter for creationDate - gets 
   * @generated */
    public String getCreationDate() {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_creationDate == null) jcasType.jcas.throwFeatMissing("creationDate", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        return jcasType.ll_cas.ll_getStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_creationDate);
    }

    /** setter for creationDate - sets  
   * @generated */
    public void setCreationDate(String v) {
        if (AttachmentAnnotation_Type.featOkTst && ((AttachmentAnnotation_Type) jcasType).casFeat_creationDate == null) jcasType.jcas.throwFeatMissing("creationDate", "it.webscience.uima.annotations.eventData.AttachmentAnnotation");
        jcasType.ll_cas.ll_setStringValue(addr, ((AttachmentAnnotation_Type) jcasType).casFeatCode_creationDate, v);
    }
}
