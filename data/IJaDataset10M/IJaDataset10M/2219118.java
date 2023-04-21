package org.metamap.uima.ts;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.cas.TOP_Type;

/** A meta mapping.
 * Updated by JCasGen Sat Apr 17 20:21:11 CEST 2010
 * @generated */
public class Mapping_Type extends TOP_Type {

    /** @generated */
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /** @generated */
    private final FSGenerator fsGenerator = new FSGenerator() {

        public FeatureStructure createFS(int addr, CASImpl cas) {
            if (Mapping_Type.this.useExistingInstance) {
                FeatureStructure fs = Mapping_Type.this.jcas.getJfsFromCaddr(addr);
                if (null == fs) {
                    fs = new Mapping(addr, Mapping_Type.this);
                    Mapping_Type.this.jcas.putJfsFromCaddr(addr, fs);
                    return fs;
                }
                return fs;
            } else return new Mapping(addr, Mapping_Type.this);
        }
    };

    /** @generated */
    public static final int typeIndexID = Mapping.typeIndexID;

    /** @generated 
     @modifiable */
    public static final boolean featOkTst = JCasRegistry.getFeatOkTst("org.metamap.uima.ts.Mapping");

    /** @generated */
    final Feature casFeat_score;

    /** @generated */
    final int casFeatCode_score;

    /** @generated */
    public int getScore(int addr) {
        if (featOkTst && casFeat_score == null) jcas.throwFeatMissing("score", "org.metamap.uima.ts.Mapping");
        return ll_cas.ll_getIntValue(addr, casFeatCode_score);
    }

    /** @generated */
    public void setScore(int addr, int v) {
        if (featOkTst && casFeat_score == null) jcas.throwFeatMissing("score", "org.metamap.uima.ts.Mapping");
        ll_cas.ll_setIntValue(addr, casFeatCode_score, v);
    }

    /** @generated */
    final Feature casFeat_candidates;

    /** @generated */
    final int casFeatCode_candidates;

    /** @generated */
    public int getCandidates(int addr) {
        if (featOkTst && casFeat_candidates == null) jcas.throwFeatMissing("candidates", "org.metamap.uima.ts.Mapping");
        return ll_cas.ll_getRefValue(addr, casFeatCode_candidates);
    }

    /** @generated */
    public void setCandidates(int addr, int v) {
        if (featOkTst && casFeat_candidates == null) jcas.throwFeatMissing("candidates", "org.metamap.uima.ts.Mapping");
        ll_cas.ll_setRefValue(addr, casFeatCode_candidates, v);
    }

    /** @generated */
    public int getCandidates(int addr, int i) {
        if (featOkTst && casFeat_candidates == null) jcas.throwFeatMissing("candidates", "org.metamap.uima.ts.Mapping");
        if (lowLevelTypeChecks) return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_candidates), i, true);
        jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_candidates), i);
        return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_candidates), i);
    }

    /** @generated */
    public void setCandidates(int addr, int i, int v) {
        if (featOkTst && casFeat_candidates == null) jcas.throwFeatMissing("candidates", "org.metamap.uima.ts.Mapping");
        if (lowLevelTypeChecks) ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_candidates), i, v, true);
        jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_candidates), i);
        ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_candidates), i, v);
    }

    /** initialize variables to correspond with Cas Type and Features
	* @generated */
    public Mapping_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());
        casFeat_score = jcas.getRequiredFeatureDE(casType, "score", "uima.cas.Integer", featOkTst);
        casFeatCode_score = (null == casFeat_score) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_score).getCode();
        casFeat_candidates = jcas.getRequiredFeatureDE(casType, "candidates", "uima.cas.FSArray", featOkTst);
        casFeatCode_candidates = (null == casFeat_candidates) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_candidates).getCode();
    }
}
