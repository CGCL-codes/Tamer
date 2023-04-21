package com.loribel.tools.sa.bo.generated;

import java.util.List;
import com.loribel.tools.sa.bo.GB_BOSATestAbstract;
import com.loribel.tools.sa.bo.GB_SARemoveBeforeAfterBO;

public abstract class GB_SARemoveBeforeAfterTestGen extends GB_BOSATestAbstract {

    public GB_SARemoveBeforeAfterTestGen(String a_name) {
        super(a_name);
    }

    public final void setUp() {
        super.setUp();
    }

    public String getBOName() {
        return GB_SARemoveBeforeAfterBO.BO_NAME;
    }

    public List getBOsForTest() {
        List retour = getBOsGeneric();
        retour.add(new GB_SARemoveBeforeAfterBO());
        return retour;
    }
}
