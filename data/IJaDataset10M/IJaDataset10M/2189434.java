package cn.edu.pku.dr.requirement.elicitation.system;

import java.util.ArrayList;
import cn.edu.pku.dr.requirement.elicitation.data.Project;
import cn.edu.pku.dr.requirement.elicitation.data.Scenario;
import cn.edu.pku.dr.requirement.elicitation.data.UserProjectRelation;
import cn.edu.pku.dr.requirement.elicitation.data.UserScenarioRelation;
import easyJ.business.proxy.SingleDataProxy;
import easyJ.common.EasyJException;

/**
 * 这个类中返回1代表要出现，返回0代表要出现，但是灰色，返回1代表不出现。
 * 
 * @author liuf
 */
public class FunctionCondition {

    private static SingleDataProxy sdp = SingleDataProxy.getInstance();

    private static Context contex;

    public static Integer confirmProjectApply(UserProjectRelation relation, Long userId) {
        if (relation.getUserProjectStateRelatedValue() == DictionaryConstant.APPLYING) return 1;
        return -1;
    }

    public static Integer cancelProjectApply(UserProjectRelation relation, Long userId) {
        if (relation.getUserProjectStateRelatedValue() == DictionaryConstant.ACCEPTED) return 1;
        return -1;
    }

    public static Integer rejectProjectApply(UserProjectRelation relation, Long userId) {
        if (relation.getUserProjectStateRelatedValue() == DictionaryConstant.APPLYING) return 1;
        return -1;
    }

    public static Integer applyProject(Project project, Long userId) throws EasyJException {
        UserProjectRelation relation = new UserProjectRelation();
        relation.setUserId(userId);
        relation.setProjectId(project.getProjectId());
        ArrayList list = sdp.query(relation);
        if (list.size() == 0) return 1;
        relation = (UserProjectRelation) list.get(0);
        if (relation.getUserProjectStateRelatedValue() == DictionaryConstant.CANCELED || relation.getUserProjectStateRelatedValue() == DictionaryConstant.REJECTED) return 1;
        return 0;
    }

    public static Integer applyScenario(Scenario scenario, Long userId) throws EasyJException {
        UserScenarioRelation relation = new UserScenarioRelation();
        relation.setUserId(userId);
        relation.setScenarioId(scenario.getScenarioId());
        ArrayList list = sdp.query(relation);
        if (list.size() == 0) return 1;
        relation = (UserScenarioRelation) list.get(0);
        if (relation.getApplyStateRelatedValue() == DictionaryConstant.CANCELED || relation.getApplyStateRelatedValue() == DictionaryConstant.REJECTED) return 1;
        return 0;
    }

    public static Integer confirmScenarioApply(UserScenarioRelation relation, Long userId) {
        if (relation.getApplyStateRelatedValue() == DictionaryConstant.APPLYING) return 1;
        return -1;
    }

    public static Integer cancelScenarioApply(UserScenarioRelation relation, Long userId) {
        if (relation.getApplyStateRelatedValue() == DictionaryConstant.ACCEPTED) return 1;
        return -1;
    }

    public static Integer rejectScenarioApply(UserScenarioRelation relation, Long userId) {
        if (relation.getApplyStateRelatedValue() == DictionaryConstant.APPLYING) return 1;
        return -1;
    }
}
