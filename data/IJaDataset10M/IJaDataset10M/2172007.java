package it.webscience.uima.annotations.action;

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
 * Updated by JCasGen Mon Feb 21 11:38:31 CET 2011
 * @generated */
public class SystemAnnotation_Type extends Annotation_Type {

    /** @generated */
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /** @generated */
    private final FSGenerator fsGenerator = new FSGenerator() {

        public FeatureStructure createFS(int addr, CASImpl cas) {
            if (SystemAnnotation_Type.this.useExistingInstance) {
                FeatureStructure fs = SystemAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
                if (null == fs) {
                    fs = new SystemAnnotation(addr, SystemAnnotation_Type.this);
                    SystemAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
                    return fs;
                }
                return fs;
            } else return new SystemAnnotation(addr, SystemAnnotation_Type.this);
        }
    };

    /** @generated */
    public static final int typeIndexID = SystemAnnotation.typeIndexID;

    /** @generated 
     @modifiable */
    public static final boolean featOkTst = JCasRegistry.getFeatOkTst("it.webscience.uima.annotations.action.SystemAnnotation");

    /** @generated */
    final Feature casFeat_systemId;

    /** @generated */
    final int casFeatCode_systemId;

    /** @generated */
    public String getSystemId(int addr) {
        if (featOkTst && casFeat_systemId == null) jcas.throwFeatMissing("systemId", "it.webscience.uima.annotations.action.SystemAnnotation");
        return ll_cas.ll_getStringValue(addr, casFeatCode_systemId);
    }

    /** @generated */
    public void setSystemId(int addr, String v) {
        if (featOkTst && casFeat_systemId == null) jcas.throwFeatMissing("systemId", "it.webscience.uima.annotations.action.SystemAnnotation");
        ll_cas.ll_setStringValue(addr, casFeatCode_systemId, v);
    }

    /** @generated */
    final Feature casFeat_systemType;

    /** @generated */
    final int casFeatCode_systemType;

    /** @generated */
    public String getSystemType(int addr) {
        if (featOkTst && casFeat_systemType == null) jcas.throwFeatMissing("systemType", "it.webscience.uima.annotations.action.SystemAnnotation");
        return ll_cas.ll_getStringValue(addr, casFeatCode_systemType);
    }

    /** @generated */
    public void setSystemType(int addr, String v) {
        if (featOkTst && casFeat_systemType == null) jcas.throwFeatMissing("systemType", "it.webscience.uima.annotations.action.SystemAnnotation");
        ll_cas.ll_setStringValue(addr, casFeatCode_systemType, v);
    }

    /** initialize variables to correspond with Cas Type and Features
	* @generated */
    public SystemAnnotation_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());
        casFeat_systemId = jcas.getRequiredFeatureDE(casType, "systemId", "uima.cas.String", featOkTst);
        casFeatCode_systemId = (null == casFeat_systemId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_systemId).getCode();
        casFeat_systemType = jcas.getRequiredFeatureDE(casType, "systemType", "uima.cas.String", featOkTst);
        casFeatCode_systemType = (null == casFeat_systemType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_systemType).getCode();
    }
}
