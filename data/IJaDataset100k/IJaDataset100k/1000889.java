package it.webscience.uima.annotations.eventData;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

/** 
 * Updated by JCasGen Tue Feb 22 11:24:36 CET 2011
 * XML source: C:/WebScience/Progetti/K-People/OntologyController_UIMA/apache-uima/examples/descriptors/webscience/typeSystems/EventDataTypeSystem.xml
 * @generated */
public class TitleAnnotation extends Annotation {

    /** @generated
   * @ordered 
   */
    public static final int typeIndexID = JCasRegistry.register(TitleAnnotation.class);

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
    protected TitleAnnotation() {
    }

    /** Internal - constructor used by generator 
   * @generated */
    public TitleAnnotation(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public TitleAnnotation(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public TitleAnnotation(JCas jcas, int begin, int end) {
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

    /** getter for value - gets 
   * @generated */
    public String getValue() {
        if (TitleAnnotation_Type.featOkTst && ((TitleAnnotation_Type) jcasType).casFeat_value == null) jcasType.jcas.throwFeatMissing("value", "it.webscience.uima.annotations.eventData.TitleAnnotation");
        return jcasType.ll_cas.ll_getStringValue(addr, ((TitleAnnotation_Type) jcasType).casFeatCode_value);
    }

    /** setter for value - sets  
   * @generated */
    public void setValue(String v) {
        if (TitleAnnotation_Type.featOkTst && ((TitleAnnotation_Type) jcasType).casFeat_value == null) jcasType.jcas.throwFeatMissing("value", "it.webscience.uima.annotations.eventData.TitleAnnotation");
        jcasType.ll_cas.ll_setStringValue(addr, ((TitleAnnotation_Type) jcasType).casFeatCode_value, v);
    }
}
