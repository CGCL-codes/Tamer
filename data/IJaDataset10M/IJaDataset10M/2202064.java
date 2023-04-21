package com.ibm.uima.examples.opennlp;

import org.apache.uima.jcas.JCas;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;

/** Particle. Category for words that should be tagged RP.
 * Updated by JCasGen Thu Mar 20 17:04:32 PDT 2008
 * @generated */
public class PRT_Type extends Phrase_Type {

    /** @generated */
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /** @generated */
    private final FSGenerator fsGenerator = new FSGenerator() {

        public FeatureStructure createFS(int addr, CASImpl cas) {
            if (PRT_Type.this.useExistingInstance) {
                FeatureStructure fs = PRT_Type.this.jcas.getJfsFromCaddr(addr);
                if (null == fs) {
                    fs = new PRT(addr, PRT_Type.this);
                    PRT_Type.this.jcas.putJfsFromCaddr(addr, fs);
                    return fs;
                }
                return fs;
            } else return new PRT(addr, PRT_Type.this);
        }
    };

    /** @generated */
    public static final int typeIndexID = PRT.typeIndexID;

    /** @generated 
     @modifiable */
    public static final boolean featOkTst = org.apache.uima.jcas.JCasRegistry.getFeatOkTst("com.ibm.uima.examples.opennlp.PRT");

    /** initialize variables to correspond with Cas Type and Features
	* @generated */
    public PRT_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());
    }
}
