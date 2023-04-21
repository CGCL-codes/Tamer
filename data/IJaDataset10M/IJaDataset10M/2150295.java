package com.g2inc.scap.library.domain.ocil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.g2inc.scap.model.ocil.Reference;
import com.g2inc.scap.model.ocil.Step;
import com.g2inc.scap.model.ocil.TextType;

public class StepImpl extends ModelBaseImpl implements Step {

    public static final HashMap<String, Integer> STEP_ORDER = new HashMap<String, Integer>();

    static {
        STEP_ORDER.put("description", 0);
        STEP_ORDER.put("reference", 1);
        STEP_ORDER.put("step", 2);
    }

    @Override
    public Boolean isDone() {
        return getBoolean("is_done");
    }

    @Override
    public void setDone(Boolean value) {
        setBoolean("is_done", value);
    }

    @Override
    public Boolean isRequired() {
        return getBoolean("is_required");
    }

    @Override
    public void setRequired(Boolean value) {
        setBoolean("is_required", value);
    }

    @Override
    public TextType getDescription() {
        return getApiElement("description", TextTypeImpl.class);
    }

    @Override
    public void setDescription(TextType description) {
        setApiElement(description, getOrderMap(), "description");
    }

    @Override
    public List<Reference> getReferenceList() {
        return getApiElementList(new ArrayList<Reference>(), "reference", ReferenceImpl.class);
    }

    @Override
    public void setReferenceList(List<Reference> list) {
        replaceApiList(list, getOrderMap(), "reference");
    }

    @Override
    public List<Step> getStepList() {
        return getApiElementList(new ArrayList<Step>(), "step", StepImpl.class);
    }

    @Override
    public void setStepList(List<Step> list) {
        replaceApiList(list, getOrderMap(), "step");
    }

    @Override
    public void addStep(Step step) {
        addApiElement(step, getOrderMap());
    }

    @Override
    public HashMap<String, Integer> getOrderMap() {
        return STEP_ORDER;
    }

    @Override
    public void addReference(Reference ref) {
        addApiElement(ref, getOrderMap());
    }
}
