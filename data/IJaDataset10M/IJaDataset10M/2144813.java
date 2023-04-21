package org.fudaa.fudaa.tr.post.profile;

import java.util.Map;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ebli.courbe.EGGraphe;
import org.fudaa.ebli.courbe.EGGroupPersist;
import org.fudaa.ebli.courbe.EGGroupPersistBuilder;

public class MvProfileCourbeGroupPersistBuilder extends EGGroupPersistBuilder<MvProfileCourbeGroup> {

    private static final String VAR_ID = "varId";

    @Override
    protected void postCreatePersist(EGGroupPersist res, MvProfileCourbeGroup groupe, EGGraphe graphe) {
        super.postCreatePersist(res, groupe, graphe);
        res.saveSpecificData(VAR_ID, groupe.getVar().getID());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected MvProfileCourbeGroup createEGObject(EGGroupPersist target, Map params, CtuluAnalyze log) {
        String varId = target.getSpecificStringValue(VAR_ID);
        MvProfileCourbeGroup mvProfileCourbeGroup = new MvProfileCourbeGroup(null);
        mvProfileCourbeGroup.setVarId(varId);
        return mvProfileCourbeGroup;
    }
}
