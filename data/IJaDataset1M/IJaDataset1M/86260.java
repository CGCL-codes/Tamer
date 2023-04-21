package org.apache.uima.examples.opennlp;

import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;

/**
 * Wh-adjective Phrase. Adjectival phrase containing a wh-adverb, as in how hot. Updated by JCasGen
 * Fri Dec 02 14:22:24 EST 2005
 * 
 * @generated
 */
public class WHADJP_Type extends Phrase_Type {

    /** @generated */
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /** @generated */
    private final FSGenerator fsGenerator = new FSGenerator() {

        public FeatureStructure createFS(int addr, CASImpl cas) {
            if (WHADJP_Type.this.useExistingInstance) {
                FeatureStructure fs = WHADJP_Type.this.jcas.getJfsFromCaddr(addr);
                if (null == fs) {
                    fs = new WHADJP(addr, WHADJP_Type.this);
                    WHADJP_Type.this.jcas.putJfsFromCaddr(addr, fs);
                    return fs;
                }
                return fs;
            } else return new WHADJP(addr, WHADJP_Type.this);
        }
    };

    /** @generated */
    public static final int typeIndexID = WHADJP.typeIndexID;

    /**
   * @generated
   * @modifiable
   */
    public static final boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.examples.opennlp.WHADJP");

    /**
   * initialize variables to correspond with Cas Type and Features
   * 
   * @generated
   */
    public WHADJP_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());
    }
}
