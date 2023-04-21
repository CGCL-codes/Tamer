package com.vionto.rnd.linguistic.srl.types;

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

/** 
 * Updated by JCasGen Mon Jun 08 12:21:50 CEST 2009
 * @generated */
public class Token_Type extends Annotation_Type {

    /** @generated */
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /** @generated */
    private final FSGenerator fsGenerator = new FSGenerator() {

        public FeatureStructure createFS(int addr, CASImpl cas) {
            if (Token_Type.this.useExistingInstance) {
                FeatureStructure fs = Token_Type.this.jcas.getJfsFromCaddr(addr);
                if (null == fs) {
                    fs = new Token(addr, Token_Type.this);
                    Token_Type.this.jcas.putJfsFromCaddr(addr, fs);
                    return fs;
                }
                return fs;
            } else return new Token(addr, Token_Type.this);
        }
    };

    /** @generated */
    public static final int typeIndexID = Token.typeIndexID;

    /** @generated 
     @modifiable */
    public static final boolean featOkTst = JCasRegistry.getFeatOkTst("com.vionto.rnd.linguistic.srl.types.Token");

    /** @generated */
    final Feature casFeat_componentId;

    /** @generated */
    final int casFeatCode_componentId;

    /** @generated */
    public String getComponentId(int addr) {
        if (featOkTst && casFeat_componentId == null) jcas.throwFeatMissing("componentId", "com.vionto.rnd.linguistic.srl.types.Token");
        return ll_cas.ll_getStringValue(addr, casFeatCode_componentId);
    }

    /** @generated */
    public void setComponentId(int addr, String v) {
        if (featOkTst && casFeat_componentId == null) jcas.throwFeatMissing("componentId", "com.vionto.rnd.linguistic.srl.types.Token");
        ll_cas.ll_setStringValue(addr, casFeatCode_componentId, v);
    }

    /** @generated */
    final Feature casFeat_PosTag;

    /** @generated */
    final int casFeatCode_PosTag;

    /** @generated */
    public String getPosTag(int addr) {
        if (featOkTst && casFeat_PosTag == null) jcas.throwFeatMissing("PosTag", "com.vionto.rnd.linguistic.srl.types.Token");
        return ll_cas.ll_getStringValue(addr, casFeatCode_PosTag);
    }

    /** @generated */
    public void setPosTag(int addr, String v) {
        if (featOkTst && casFeat_PosTag == null) jcas.throwFeatMissing("PosTag", "com.vionto.rnd.linguistic.srl.types.Token");
        ll_cas.ll_setStringValue(addr, casFeatCode_PosTag, v);
    }

    /** initialize variables to correspond with Cas Type and Features
	* @generated */
    public Token_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());
        casFeat_componentId = jcas.getRequiredFeatureDE(casType, "componentId", "uima.cas.String", featOkTst);
        casFeatCode_componentId = (null == casFeat_componentId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_componentId).getCode();
        casFeat_PosTag = jcas.getRequiredFeatureDE(casType, "PosTag", "uima.cas.String", featOkTst);
        casFeatCode_PosTag = (null == casFeat_PosTag) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_PosTag).getCode();
    }
}
