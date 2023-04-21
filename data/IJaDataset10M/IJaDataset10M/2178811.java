package uima.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Wed Apr 16 16:19:23 PDT 2008
 * @generated */
public class Cluster_Type extends Annotation_Type {

    /** @generated */
    protected FSGenerator getFSGenerator() {
        return fsGenerator;
    }

    /** @generated */
    private final FSGenerator fsGenerator = new FSGenerator() {

        public FeatureStructure createFS(int addr, CASImpl cas) {
            if (Cluster_Type.this.useExistingInstance) {
                FeatureStructure fs = Cluster_Type.this.jcas.getJfsFromCaddr(addr);
                if (null == fs) {
                    fs = new Cluster(addr, Cluster_Type.this);
                    Cluster_Type.this.jcas.putJfsFromCaddr(addr, fs);
                    return fs;
                }
                return fs;
            } else return new Cluster(addr, Cluster_Type.this);
        }
    };

    /** @generated */
    public static final int typeIndexID = Cluster.typeIndexID;

    /** @generated 
     @modifiable */
    public static final boolean featOkTst = org.apache.uima.jcas.JCasRegistry.getFeatOkTst("uima.types.Cluster");

    /** @generated */
    final Feature casFeat_docID;

    /** @generated */
    final int casFeatCode_docID;

    /** @generated */
    public int getDocID(int addr) {
        if (featOkTst && casFeat_docID == null) this.jcas.throwFeatMissing("docID", "uima.types.Cluster");
        return ll_cas.ll_getIntValue(addr, casFeatCode_docID);
    }

    /** @generated */
    public void setDocID(int addr, int v) {
        if (featOkTst && casFeat_docID == null) this.jcas.throwFeatMissing("docID", "uima.types.Cluster");
        ll_cas.ll_setIntValue(addr, casFeatCode_docID, v);
    }

    /** @generated */
    final Feature casFeat_clusterID;

    /** @generated */
    final int casFeatCode_clusterID;

    /** @generated */
    public int getClusterID(int addr) {
        if (featOkTst && casFeat_clusterID == null) this.jcas.throwFeatMissing("clusterID", "uima.types.Cluster");
        return ll_cas.ll_getIntValue(addr, casFeatCode_clusterID);
    }

    /** @generated */
    public void setClusterID(int addr, int v) {
        if (featOkTst && casFeat_clusterID == null) this.jcas.throwFeatMissing("clusterID", "uima.types.Cluster");
        ll_cas.ll_setIntValue(addr, casFeatCode_clusterID, v);
    }

    /** @generated */
    final Feature casFeat_similarity;

    /** @generated */
    final int casFeatCode_similarity;

    /** @generated */
    public double getSimilarity(int addr) {
        if (featOkTst && casFeat_similarity == null) this.jcas.throwFeatMissing("similarity", "uima.types.Cluster");
        return ll_cas.ll_getDoubleValue(addr, casFeatCode_similarity);
    }

    /** @generated */
    public void setSimilarity(int addr, double v) {
        if (featOkTst && casFeat_similarity == null) this.jcas.throwFeatMissing("similarity", "uima.types.Cluster");
        ll_cas.ll_setDoubleValue(addr, casFeatCode_similarity, v);
    }

    /** @generated */
    final Feature casFeat_categoryID;

    /** @generated */
    final int casFeatCode_categoryID;

    /** @generated */
    public int getCategoryID(int addr) {
        if (featOkTst && casFeat_categoryID == null) this.jcas.throwFeatMissing("categoryID", "uima.types.Cluster");
        return ll_cas.ll_getIntValue(addr, casFeatCode_categoryID);
    }

    /** @generated */
    public void setCategoryID(int addr, int v) {
        if (featOkTst && casFeat_categoryID == null) this.jcas.throwFeatMissing("categoryID", "uima.types.Cluster");
        ll_cas.ll_setIntValue(addr, casFeatCode_categoryID, v);
    }

    /** initialize variables to correspond with Cas Type and Features
	* @generated */
    public Cluster_Type(JCas jcas, Type casType) {
        super(jcas, casType);
        casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl) this.casType, getFSGenerator());
        casFeat_docID = jcas.getRequiredFeatureDE(casType, "docID", "uima.cas.Integer", featOkTst);
        casFeatCode_docID = (null == casFeat_docID) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_docID).getCode();
        casFeat_clusterID = jcas.getRequiredFeatureDE(casType, "clusterID", "uima.cas.Integer", featOkTst);
        casFeatCode_clusterID = (null == casFeat_clusterID) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_clusterID).getCode();
        casFeat_similarity = jcas.getRequiredFeatureDE(casType, "similarity", "uima.cas.Double", featOkTst);
        casFeatCode_similarity = (null == casFeat_similarity) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_similarity).getCode();
        casFeat_categoryID = jcas.getRequiredFeatureDE(casType, "categoryID", "uima.cas.Integer", featOkTst);
        casFeatCode_categoryID = (null == casFeat_categoryID) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl) casFeat_categoryID).getCode();
    }
}
