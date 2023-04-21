package jp.ac.jaist.ceqea.F_run;

import jp.ac.jaist.ceqea.A_MIG.*;

public class CEq_zgrvObj extends CEq_object {

    public CEq_zgrvObj(String nm) {
        super(nm);
    }

    @Override
    public int compareTo(CEq_object otherObject) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.name(), otherObject.name());
    }
}
