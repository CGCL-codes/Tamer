package org.tolven.gen.bean;

import java.io.Serializable;
import org.tolven.gen.entity.FamilyUnit;

/**
 * Generate data for a single family.
 * @author John Churin
 *
 */
public class GenControlFamily extends GenControlBase implements Serializable {

    private static final long serialVersionUID = 1L;

    private FamilyUnit familyUnit;

    public FamilyUnit getFamilyUnit() {
        return familyUnit;
    }

    public void setFamilyUnit(FamilyUnit familyUnit) {
        this.familyUnit = familyUnit;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(200);
        sb.append(" Account: ");
        sb.append(getChrAccountId());
        sb.append(" Family: ");
        sb.append(familyUnit.getFamilyName());
        sb.append(" Now: ");
        sb.append(getNow());
        return sb.toString();
    }
}
