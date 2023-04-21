package org.apache.uima.tutorial;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

/**
 * Updated by JCasGen Mon Nov 29 15:02:38 EST 2004 XML source: C:/Program
 * Files/apache/uima/examples/descriptors/tutorial/ex6/TutorialTypeSystem.xml
 * 
 * @generated
 */
public class WordAnnot extends Annotation {

    /**
   * @generated
   * @ordered
   */
    public static final int typeIndexID = JCasRegistry.register(WordAnnot.class);

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
    protected WordAnnot() {
    }

    /**
   * Internal - constructor used by generator
   * 
   * @generated
   */
    public WordAnnot(int addr, TOP_Type type) {
        super(addr, type);
        readObject();
    }

    /** @generated */
    public WordAnnot(JCas jcas) {
        super(jcas);
        readObject();
    }

    public WordAnnot(JCas jcas, int begin, int end) {
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
