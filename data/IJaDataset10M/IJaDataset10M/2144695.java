package org.apache.uima.tutorial;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;

/** 
 * Updated by JCasGen Mon Jun 08 23:57:15 CEST 2009
 * @generated */
public class TimeAnnot_Type extends DateTimeAnnot_Type {

    /** @generated */
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /** @generated */
    private final FSGenerator fsGenerator = new FSGenerator() {

        public FeatureStructure createFS(int addr, CASImpl cas) {
            if (TimeAnnot_Type.this.useExistingInstance) {
                FeatureStructure fs = TimeAnnot_Type.this.jcas.getJfsFromCaddr(addr);
                if (null == fs) {
                    fs = new TimeAnnot(addr, TimeAnnot_Type.this);
                    TimeAnnot_Type.this.jcas.putJfsFromCaddr(addr, fs);
                    return fs;
                }
                return fs;
            } else return new TimeAnnot(addr, TimeAnnot_Type.this);
        }
    };

    /** @generated */
    public static final int typeIndexID = TimeAnnot.typeIndexID;

    /** @generated 
     @modifiable */
    public static final boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.tutorial.TimeAnnot");

    /** initialize variables to correspond with Cas Type and Features
	* @generated */
    public TimeAnnot_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());
    }
}
