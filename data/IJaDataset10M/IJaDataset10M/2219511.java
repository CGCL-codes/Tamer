package com.ibm.uima.examples.opennlp;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP_Type;

/** Direct question introduced by a wh-word or a wh-phrase. Indirect questions and relative clauses should be bracketed as SBAR, not SBARQ.
 * Updated by JCasGen Thu Mar 20 17:04:31 PDT 2008
 * XML source: C:/Documents and Settings/pon3/My Documents/workspace/uima_pipeline/desc/types/OpenNLPExampleTypes.xml
 * @generated */
public class SBARQ extends Clause {

    /** @generated
   * @ordered 
   */
    public static final int typeIndexID = org.apache.uima.jcas.JCasRegistry.register(SBARQ.class);

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
    protected SBARQ() {
    }

    /** Internal - constructor used by generator 
   * @generated */
    public SBARQ(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public SBARQ(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public SBARQ(JCas jcas, int begin, int end) {
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
