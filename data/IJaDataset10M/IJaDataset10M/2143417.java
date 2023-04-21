package com.prolix.editor.interaction.conditions;

import com.prolix.editor.resourcemanager.zip.LearningDesignDataModel;
import uk.ac.reload.straker.datamodel.learningdesign.expressions.IfThenElseType;

/**
 * <<class description>>
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public interface ConditionIf {

    public IfThenElseType build();

    public void setLddm(LearningDesignDataModel lddm);
}
