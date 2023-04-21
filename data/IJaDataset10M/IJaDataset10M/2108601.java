package com.ibm.uima.examples.opennlp;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

/** Syntax annotation, typically created by a parser.
 * Updated by JCasGen Thu Mar 20 17:04:31 PDT 2008
 * XML source: C:/Documents and Settings/pon3/My Documents/workspace/uima_pipeline/desc/types/OpenNLPExampleTypes.xml
 * @generated */
public class SyntaxAnnotation extends Annotation {

    /** @generated
   * @ordered 
   */
    public static final int typeIndexID = org.apache.uima.jcas.JCasRegistry.register(SyntaxAnnotation.class);

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
    protected SyntaxAnnotation() {
    }

    /** Internal - constructor used by generator 
   * @generated */
    public SyntaxAnnotation(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public SyntaxAnnotation(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public SyntaxAnnotation(JCas jcas, int begin, int end) {
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

    /** getter for componentId - gets Identifier of the annotator that created this annotation.
   * @generated */
    public String getComponentId() {
        if (SyntaxAnnotation_Type.featOkTst && ((SyntaxAnnotation_Type) jcasType).casFeat_componentId == null) this.jcasType.jcas.throwFeatMissing("componentId", "com.ibm.uima.examples.opennlp.SyntaxAnnotation");
        return jcasType.ll_cas.ll_getStringValue(addr, ((SyntaxAnnotation_Type) jcasType).casFeatCode_componentId);
    }

    /** setter for componentId - sets Identifier of the annotator that created this annotation. 
   * @generated */
    public void setComponentId(String v) {
        if (SyntaxAnnotation_Type.featOkTst && ((SyntaxAnnotation_Type) jcasType).casFeat_componentId == null) this.jcasType.jcas.throwFeatMissing("componentId", "com.ibm.uima.examples.opennlp.SyntaxAnnotation");
        jcasType.ll_cas.ll_setStringValue(addr, ((SyntaxAnnotation_Type) jcasType).casFeatCode_componentId, v);
    }

    /** getter for sentenceID - gets 
   * @generated */
    public int getSentenceID() {
        if (SyntaxAnnotation_Type.featOkTst && ((SyntaxAnnotation_Type) jcasType).casFeat_sentenceID == null) this.jcasType.jcas.throwFeatMissing("sentenceID", "com.ibm.uima.examples.opennlp.SyntaxAnnotation");
        return jcasType.ll_cas.ll_getIntValue(addr, ((SyntaxAnnotation_Type) jcasType).casFeatCode_sentenceID);
    }

    /** setter for sentenceID - sets  
   * @generated */
    public void setSentenceID(int v) {
        if (SyntaxAnnotation_Type.featOkTst && ((SyntaxAnnotation_Type) jcasType).casFeat_sentenceID == null) this.jcasType.jcas.throwFeatMissing("sentenceID", "com.ibm.uima.examples.opennlp.SyntaxAnnotation");
        jcasType.ll_cas.ll_setIntValue(addr, ((SyntaxAnnotation_Type) jcasType).casFeatCode_sentenceID, v);
    }

    /** getter for LogProb - gets 
   * @generated */
    public float getLogProb() {
        if (SyntaxAnnotation_Type.featOkTst && ((SyntaxAnnotation_Type) jcasType).casFeat_logProb == null) this.jcasType.jcas.throwFeatMissing("logProb", "com.ibm.uima.examples.opennlp.SyntaxAnnotation");
        return jcasType.ll_cas.ll_getFloatValue(addr, ((SyntaxAnnotation_Type) jcasType).casFeatCode_logProb);
    }

    /** setter for LogProb - sets  
   * @generated */
    public void setLogProb(float v) {
        if (SyntaxAnnotation_Type.featOkTst && ((SyntaxAnnotation_Type) jcasType).casFeat_logProb == null) this.jcasType.jcas.throwFeatMissing("logProb", "com.ibm.uima.examples.opennlp.SyntaxAnnotation");
        jcasType.ll_cas.ll_setFloatValue(addr, ((SyntaxAnnotation_Type) jcasType).casFeatCode_logProb, v);
    }
}
