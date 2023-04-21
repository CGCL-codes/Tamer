package org.apache.uima.examples.opennlp;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

/**
 * Clause introduced by a (possibly empty) subordinating conjunction. Updated by JCasGen Fri Dec 02
 * 14:22:24 EST 2005 XML source:
 * c:/workspace/uimaj-examples/opennlp/src/org/apache/uima/examples/opennlp/annotator/OpenNLPExampleTypes.xml
 * 
 * @generated
 */
public class SBAR extends Clause {

    /**
   * @generated
   * @ordered
   */
    public static final int typeIndexID = JCasRegistry.register(SBAR.class);

    /**
   * @generated
   * @ordered
   */
    public static final int type = typeIndexID;

    /** @generated */
    public int getTypeIndexID() {
        return typeIndexID;
    }

    /**
   * Never called. Disable default constructor
   * 
   * @generated
   */
    protected SBAR() {
    }

    /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
    public SBAR(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public SBAR(JCas jcas) {
        super(jcas);
        readObject();
    }

    public SBAR(JCas jcas, int begin, int end) {
        super(jcas);
        setBegin(begin);
        setEnd(end);
        readObject();
    }

    /**
   * <!-- begin-user-doc --> Write your own initialization here <!-- end-user-doc -->
   * 
   * @generated modifiable
   */
    private void readObject() {
    }
}
