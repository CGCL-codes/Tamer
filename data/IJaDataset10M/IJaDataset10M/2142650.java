package ytex.uima.types;

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

/** 
 * Updated by JCasGen Thu Apr 19 20:50:38 EDT 2012
 * @generated */
public class KeyValuePair_Type extends TOP_Type {

    /** @generated */
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /** @generated */
    private final FSGenerator fsGenerator = new FSGenerator() {

        public FeatureStructure createFS(int addr, CASImpl cas) {
            if (KeyValuePair_Type.this.useExistingInstance) {
                FeatureStructure fs = KeyValuePair_Type.this.jcas.getJfsFromCaddr(addr);
                if (null == fs) {
                    fs = new KeyValuePair(addr, KeyValuePair_Type.this);
                    KeyValuePair_Type.this.jcas.putJfsFromCaddr(addr, fs);
                    return fs;
                }
                return fs;
            } else return new KeyValuePair(addr, KeyValuePair_Type.this);
        }
    };

    /** @generated */
    public static final int typeIndexID = KeyValuePair.typeIndexID;

    /** @generated 
     @modifiable */
    public static final boolean featOkTst = JCasRegistry.getFeatOkTst("ytex.uima.types.KeyValuePair");

    /** @generated */
    final Feature casFeat_key;

    /** @generated */
    final int casFeatCode_key;

    /** @generated */
    public String getKey(int addr) {
        if (featOkTst && casFeat_key == null) jcas.throwFeatMissing("key", "ytex.uima.types.KeyValuePair");
        return ll_cas.ll_getStringValue(addr, casFeatCode_key);
    }

    /** @generated */
    public void setKey(int addr, String v) {
        if (featOkTst && casFeat_key == null) jcas.throwFeatMissing("key", "ytex.uima.types.KeyValuePair");
        ll_cas.ll_setStringValue(addr, casFeatCode_key, v);
    }

    /** @generated */
    final Feature casFeat_valueLong;

    /** @generated */
    final int casFeatCode_valueLong;

    /** @generated */
    public long getValueLong(int addr) {
        if (featOkTst && casFeat_valueLong == null) jcas.throwFeatMissing("valueLong", "ytex.uima.types.KeyValuePair");
        return ll_cas.ll_getLongValue(addr, casFeatCode_valueLong);
    }

    /** @generated */
    public void setValueLong(int addr, long v) {
        if (featOkTst && casFeat_valueLong == null) jcas.throwFeatMissing("valueLong", "ytex.uima.types.KeyValuePair");
        ll_cas.ll_setLongValue(addr, casFeatCode_valueLong, v);
    }

    /** @generated */
    final Feature casFeat_valueString;

    /** @generated */
    final int casFeatCode_valueString;

    /** @generated */
    public String getValueString(int addr) {
        if (featOkTst && casFeat_valueString == null) jcas.throwFeatMissing("valueString", "ytex.uima.types.KeyValuePair");
        return ll_cas.ll_getStringValue(addr, casFeatCode_valueString);
    }

    /** @generated */
    public void setValueString(int addr, String v) {
        if (featOkTst && casFeat_valueString == null) jcas.throwFeatMissing("valueString", "ytex.uima.types.KeyValuePair");
        ll_cas.ll_setStringValue(addr, casFeatCode_valueString, v);
    }

    /** initialize variables to correspond with Cas Type and Features
	* @generated */
    public KeyValuePair_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());
        casFeat_key = jcas.getRequiredFeatureDE(casType, "key", "uima.cas.String", featOkTst);
        casFeatCode_key = (null == casFeat_key) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_key).getCode();
        casFeat_valueLong = jcas.getRequiredFeatureDE(casType, "valueLong", "uima.cas.Long", featOkTst);
        casFeatCode_valueLong = (null == casFeat_valueLong) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_valueLong).getCode();
        casFeat_valueString = jcas.getRequiredFeatureDE(casType, "valueString", "uima.cas.String", featOkTst);
        casFeatCode_valueString = (null == casFeat_valueString) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_valueString).getCode();
    }
}
