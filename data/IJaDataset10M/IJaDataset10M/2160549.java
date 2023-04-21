package ia1011.zanzibar.chaos.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** A Lemma represents an morphosyntactic and semantic interpretation (generally obtained in a dictionary) of a token or a token span.
 * Updated by JCasGen Mon Mar 28 12:52:46 CEST 2011
 * @generated */
public class UimaLemma_Type extends Annotation_Type {

    /** @generated */
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /** @generated */
    private final FSGenerator fsGenerator = new FSGenerator() {

        public FeatureStructure createFS(int addr, CASImpl cas) {
            if (UimaLemma_Type.this.useExistingInstance) {
                FeatureStructure fs = UimaLemma_Type.this.jcas.getJfsFromCaddr(addr);
                if (null == fs) {
                    fs = new UimaLemma(addr, UimaLemma_Type.this);
                    UimaLemma_Type.this.jcas.putJfsFromCaddr(addr, fs);
                    return fs;
                }
                return fs;
            } else return new UimaLemma(addr, UimaLemma_Type.this);
        }
    };

    /** @generated */
    public static final int typeIndexID = UimaLemma.typeIndexID;

    /** @generated 
     @modifiable */
    public static final boolean featOkTst = JCasRegistry.getFeatOkTst("ia1011.zanzibar.chaos.type.UimaLemma");

    /** @generated */
    final Feature casFeat_id;

    /** @generated */
    final int casFeatCode_id;

    /** @generated */
    public int getId(int addr) {
        if (featOkTst && casFeat_id == null) jcas.throwFeatMissing("id", "ia1011.zanzibar.chaos.type.UimaLemma");
        return ll_cas.ll_getIntValue(addr, casFeatCode_id);
    }

    /** @generated */
    public void setId(int addr, int v) {
        if (featOkTst && casFeat_id == null) jcas.throwFeatMissing("id", "ia1011.zanzibar.chaos.type.UimaLemma");
        ll_cas.ll_setIntValue(addr, casFeatCode_id, v);
    }

    /** @generated */
    final Feature casFeat_surface;

    /** @generated */
    final int casFeatCode_surface;

    /** @generated */
    public String getSurface(int addr) {
        if (featOkTst && casFeat_surface == null) jcas.throwFeatMissing("surface", "ia1011.zanzibar.chaos.type.UimaLemma");
        return ll_cas.ll_getStringValue(addr, casFeatCode_surface);
    }

    /** @generated */
    public void setSurface(int addr, String v) {
        if (featOkTst && casFeat_surface == null) jcas.throwFeatMissing("surface", "ia1011.zanzibar.chaos.type.UimaLemma");
        ll_cas.ll_setStringValue(addr, casFeatCode_surface, v);
    }

    /** @generated */
    final Feature casFeat_kind;

    /** @generated */
    final int casFeatCode_kind;

    /** @generated */
    public String getKind(int addr) {
        if (featOkTst && casFeat_kind == null) jcas.throwFeatMissing("kind", "ia1011.zanzibar.chaos.type.UimaLemma");
        return ll_cas.ll_getStringValue(addr, casFeatCode_kind);
    }

    /** @generated */
    public void setKind(int addr, String v) {
        if (featOkTst && casFeat_kind == null) jcas.throwFeatMissing("kind", "ia1011.zanzibar.chaos.type.UimaLemma");
        ll_cas.ll_setStringValue(addr, casFeatCode_kind, v);
    }

    /** @generated */
    final Feature casFeat_morphologicalFeatures;

    /** @generated */
    final int casFeatCode_morphologicalFeatures;

    /** @generated */
    public String getMorphologicalFeatures(int addr) {
        if (featOkTst && casFeat_morphologicalFeatures == null) jcas.throwFeatMissing("morphologicalFeatures", "ia1011.zanzibar.chaos.type.UimaLemma");
        return ll_cas.ll_getStringValue(addr, casFeatCode_morphologicalFeatures);
    }

    /** @generated */
    public void setMorphologicalFeatures(int addr, String v) {
        if (featOkTst && casFeat_morphologicalFeatures == null) jcas.throwFeatMissing("morphologicalFeatures", "ia1011.zanzibar.chaos.type.UimaLemma");
        ll_cas.ll_setStringValue(addr, casFeatCode_morphologicalFeatures, v);
    }

    /** @generated */
    final Feature casFeat_wordsenses;

    /** @generated */
    final int casFeatCode_wordsenses;

    /** @generated */
    public int getWordsenses(int addr) {
        if (featOkTst && casFeat_wordsenses == null) jcas.throwFeatMissing("wordsenses", "ia1011.zanzibar.chaos.type.UimaLemma");
        return ll_cas.ll_getRefValue(addr, casFeatCode_wordsenses);
    }

    /** @generated */
    public void setWordsenses(int addr, int v) {
        if (featOkTst && casFeat_wordsenses == null) jcas.throwFeatMissing("wordsenses", "ia1011.zanzibar.chaos.type.UimaLemma");
        ll_cas.ll_setRefValue(addr, casFeatCode_wordsenses, v);
    }

    /** @generated */
    public String getWordsenses(int addr, int i) {
        if (featOkTst && casFeat_wordsenses == null) jcas.throwFeatMissing("wordsenses", "ia1011.zanzibar.chaos.type.UimaLemma");
        if (lowLevelTypeChecks) return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_wordsenses), i, true);
        jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_wordsenses), i);
        return ll_cas.ll_getStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_wordsenses), i);
    }

    /** @generated */
    public void setWordsenses(int addr, int i, String v) {
        if (featOkTst && casFeat_wordsenses == null) jcas.throwFeatMissing("wordsenses", "ia1011.zanzibar.chaos.type.UimaLemma");
        if (lowLevelTypeChecks) ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_wordsenses), i, v, true);
        jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_wordsenses), i);
        ll_cas.ll_setStringArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_wordsenses), i, v);
    }

    /** initialize variables to correspond with Cas Type and Features
	* @generated */
    public UimaLemma_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());
        casFeat_id = jcas.getRequiredFeatureDE(casType, "id", "uima.cas.Integer", featOkTst);
        casFeatCode_id = (null == casFeat_id) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_id).getCode();
        casFeat_surface = jcas.getRequiredFeatureDE(casType, "surface", "uima.cas.String", featOkTst);
        casFeatCode_surface = (null == casFeat_surface) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_surface).getCode();
        casFeat_kind = jcas.getRequiredFeatureDE(casType, "kind", "uima.cas.String", featOkTst);
        casFeatCode_kind = (null == casFeat_kind) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_kind).getCode();
        casFeat_morphologicalFeatures = jcas.getRequiredFeatureDE(casType, "morphologicalFeatures", "uima.cas.String", featOkTst);
        casFeatCode_morphologicalFeatures = (null == casFeat_morphologicalFeatures) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_morphologicalFeatures).getCode();
        casFeat_wordsenses = jcas.getRequiredFeatureDE(casType, "wordsenses", "uima.cas.StringArray", featOkTst);
        casFeatCode_wordsenses = (null == casFeat_wordsenses) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_wordsenses).getCode();
    }
}
