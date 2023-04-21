package com.ibm.uima.examples.opennlp;

import org.apache.uima.jcas.JCas;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;

/** Phrase level syntactic annotation.
 * Updated by JCasGen Thu Mar 20 17:04:31 PDT 2008
 * @generated */
public class Phrase_Type extends SyntaxAnnotation_Type {

    /** @generated */
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /** @generated */
    private final FSGenerator fsGenerator = new FSGenerator() {

        public FeatureStructure createFS(int addr, CASImpl cas) {
            if (Phrase_Type.this.useExistingInstance) {
                FeatureStructure fs = Phrase_Type.this.jcas.getJfsFromCaddr(addr);
                if (null == fs) {
                    fs = new Phrase(addr, Phrase_Type.this);
                    Phrase_Type.this.jcas.putJfsFromCaddr(addr, fs);
                    return fs;
                }
                return fs;
            } else return new Phrase(addr, Phrase_Type.this);
        }
    };

    /** @generated */
    public static final int typeIndexID = Phrase.typeIndexID;

    /** @generated 
     @modifiable */
    public static final boolean featOkTst = org.apache.uima.jcas.JCasRegistry.getFeatOkTst("com.ibm.uima.examples.opennlp.Phrase");

    /** initialize variables to correspond with Cas Type and Features
	* @generated */
    public Phrase_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());
    }
}
