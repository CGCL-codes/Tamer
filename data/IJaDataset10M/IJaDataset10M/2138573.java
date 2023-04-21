package com.vionto.rnd.linguistic.srl.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

/** Fragment.
 * Updated by JCasGen Mon Jun 08 12:21:52 CEST 2009
 * XML source: /home/hannes2/Studium/Diplomarbeit/Johannes_Neubarth_DA/uimaj-examples/resources/org/apache/uima/examples/srl/OpenNLPExampleTypes.xml
 * @generated */
public class FRAG extends Phrase {

    /** @generated
   * @ordered 
   */
    public static final int typeIndexID = JCasRegistry.register(FRAG.class);

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
    protected FRAG() {
    }

    /** Internal - constructor used by generator 
   * @generated */
    public FRAG(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public FRAG(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public FRAG(JCas jcas, int begin, int end) {
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
}
