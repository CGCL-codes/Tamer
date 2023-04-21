package uima.types;

import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

/** 
 * Updated by JCasGen Sat Apr 26 11:18:03 PDT 2008
 * XML source: C:/Documents and Settings/pon3/My Documents/workspace/uima_pipeline/desc/tae/interestingness/classifiers/TopicControlledNaiveBayesAgg6.xml
 * @generated */
public class Word extends Annotation {

    /** @generated
   * @ordered 
   */
    public static final int typeIndexID = org.apache.uima.jcas.JCasRegistry.register(Word.class);

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
    protected Word() {
    }

    /** Internal - constructor used by generator 
   * @generated */
    public Word(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public Word(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public Word(JCas jcas, int begin, int end) {
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

    /** getter for word - gets 
   * @generated */
    public String getWord() {
        if (Word_Type.featOkTst && ((Word_Type) jcasType).casFeat_word == null) this.jcasType.jcas.throwFeatMissing("word", "uima.types.Word");
        return jcasType.ll_cas.ll_getStringValue(addr, ((Word_Type) jcasType).casFeatCode_word);
    }

    /** setter for word - sets  
   * @generated */
    public void setWord(String v) {
        if (Word_Type.featOkTst && ((Word_Type) jcasType).casFeat_word == null) this.jcasType.jcas.throwFeatMissing("word", "uima.types.Word");
        jcasType.ll_cas.ll_setStringValue(addr, ((Word_Type) jcasType).casFeatCode_word, v);
    }

    /** getter for location - gets 
   * @generated */
    public String getLocation() {
        if (Word_Type.featOkTst && ((Word_Type) jcasType).casFeat_location == null) this.jcasType.jcas.throwFeatMissing("location", "uima.types.Word");
        return jcasType.ll_cas.ll_getStringValue(addr, ((Word_Type) jcasType).casFeatCode_location);
    }

    /** setter for location - sets  
   * @generated */
    public void setLocation(String v) {
        if (Word_Type.featOkTst && ((Word_Type) jcasType).casFeat_location == null) this.jcasType.jcas.throwFeatMissing("location", "uima.types.Word");
        jcasType.ll_cas.ll_setStringValue(addr, ((Word_Type) jcasType).casFeatCode_location, v);
    }
}
