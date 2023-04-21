package org.juddi.datatype.response;

import org.juddi.datatype.BusinessKey;
import org.juddi.datatype.RegistryObject;

/**
 * @author Steve Viens (sviens@users.sourceforge.net)
 * @author Anou Mana (anou_mana@users.sourceforge.net)
 */
public class RelatedBusinessesList implements RegistryObject {

    String generic;

    String operator;

    boolean truncated;

    RelatedBusinessInfos relatedBusinessInfos;

    BusinessKey businessKey;

    /**
   * default constructor
   */
    public RelatedBusinessesList() {
    }

    /**
   *
   * @param genericValue
   */
    public void setGeneric(String genericValue) {
        this.generic = genericValue;
    }

    /**
   *
   * @return String UDDI generic value.
   */
    public String getGeneric() {
        return this.generic;
    }

    /**
   *
   */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
   *
   */
    public String getOperator() {
        return this.operator;
    }

    /**
   *
   */
    public boolean isTruncated() {
        return this.truncated;
    }

    /**
   *
   */
    public void setTruncated(boolean val) {
        this.truncated = val;
    }

    /**
   *
   */
    public void addRelatedBusinessInfo(RelatedBusinessInfo info) {
        if (this.relatedBusinessInfos == null) this.relatedBusinessInfos = new RelatedBusinessInfos();
        this.relatedBusinessInfos.addRelatedBusinessInfo(info);
    }

    /**
   *
   */
    public void setRelatedBusinessInfos(RelatedBusinessInfos infos) {
        this.relatedBusinessInfos = infos;
    }

    /**
   *
   */
    public RelatedBusinessInfos getRelatedBusinessInfos() {
        return this.relatedBusinessInfos;
    }

    /**
   * @return businessKey
   */
    public BusinessKey getBusinessKey() {
        return businessKey;
    }

    /**
   * @param key
   */
    public void setBusinessKey(BusinessKey key) {
        businessKey = key;
    }
}
