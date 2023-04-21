package com.ibm.uima.examples.opennlp;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP_Type;

/** Simple declarative clause, i.e. one that is not introduced by a (possible empty) 
subordinating conjunction or a wh-word and that does not exhibit subject-verb inversion.
 * Updated by JCasGen Thu Mar 20 17:04:31 PDT 2008
 * XML source: C:/Documents and Settings/pon3/My Documents/workspace/uima_pipeline/desc/types/OpenNLPExampleTypes.xml
 * @generated */
public class S extends Clause {

    /** @generated
   * @ordered 
   */
    public static final int typeIndexID = org.apache.uima.jcas.JCasRegistry.register(S.class);

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
    protected S() {
    }

    /** Internal - constructor used by generator 
   * @generated */
    public S(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public S(JCas jcas) {
        super(jcas);
        readObject();
    }

    /** @generated */
    public S(JCas jcas, int begin, int end) {
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
