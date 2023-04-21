package org.apache.uima.examples.opennlp;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

/**
 * Parenthetical. Updated by JCasGen Fri Dec 02 14:22:24 EST 2005 XML source:
 * c:/workspace/uimaj-examples/opennlp/src/org/apache/uima/examples/opennlp/annotator/OpenNLPExampleTypes.xml
 * 
 * @generated
 */
public class PRNphrase extends Phrase {

    /**
   * @generated
   * @ordered
   */
    public static final int typeIndexID = JCasRegistry.register(PRNphrase.class);

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
    protected PRNphrase() {
    }

    /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
    public PRNphrase(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public PRNphrase(JCas jcas) {
        super(jcas);
        readObject();
    }

    public PRNphrase(JCas jcas, int begin, int end) {
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
