package com.prolix.editor.graph.check.lvlb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.prolix.editor.graph.check.lvla.DiagramToReloadAAct;
import com.prolix.editor.graph.check.lvla.elements.DiagramToReloadAElement;
import com.prolix.editor.interaction.conditions.Condition;
import com.prolix.editor.interaction.conditions.ConditionImpl;
import com.prolix.editor.interaction.conditions.steuerung.ConditionSteuerungShowHide;
import com.prolix.editor.resourcemanager.zip.LearningDesignDataModel;

/**
 * <<class description>>
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public class DiagramToReloadBAct {

    private DiagramToReloadAAct act;

    private List linkedActivities;

    private Condition condition;

    public DiagramToReloadBAct(DiagramToReloadAAct act, LearningDesignDataModel lddm) {
        this.act = act;
        condition = new ConditionImpl(lddm);
    }

    public boolean needLvlB() {
        if (!act.getBuild()) return false;
        System.out.println("needlvlb");
        Iterator it = getLinkedActivitiesFromAct().iterator();
        while (it.hasNext()) System.out.println(it.next().toString());
        return getLinkedActivitiesFromAct().size() > 0;
    }

    public List getLinkedActivitiesFromAct() {
        if (linkedActivities != null) return linkedActivities;
        linkedActivities = new ArrayList();
        Iterator it = act.getActivities().iterator();
        while (it.hasNext()) {
            DiagramToReloadAElement element = (DiagramToReloadAElement) it.next();
            if (element.isShow()) {
                Iterator prefs = element.getPref().iterator();
                boolean add = false;
                while (prefs.hasNext() && !add) {
                    if (((DiagramToReloadAElement) prefs.next()).getAct() == act.getPos()) add = true;
                }
                if (add) linkedActivities.add(element);
            }
        }
        return linkedActivities;
    }

    public void buildIt() {
        if (!needLvlB()) return;
        Iterator it = getLinkedActivitiesFromAct().iterator();
        while (it.hasNext()) {
            DiagramToReloadAElement element = (DiagramToReloadAElement) it.next();
            condition.add(new ConditionSteuerungShowHide(element.getPref(), element));
        }
        condition.build();
    }

    public void clean() {
        if (!needLvlB()) return;
        condition.clean();
    }
}
