package uk.ac.cam.caret.lom.impl;

import uk.ac.cam.caret.lom.api.*;
import uk.ac.cam.caret.minibix.general.svo.api.SvoPreferences;
import uk.ac.cam.caret.minibix.general.svo.api.SvoReference;

public class LomAnnotationImpl implements LomAnnotation {

    private LomDateImpl date;

    private VCardImpl person;

    private LomMultilingualStringImpl description;

    public LomDate getDate() {
        return date;
    }

    public LomMultilingualString getDescription() {
        return description;
    }

    public VCardImpl getPerson() {
        return person;
    }

    public void setDate(LomDate in) {
        date = (LomDateImpl) in;
    }

    public void setDate(String time, LomMultilingualString description) {
        date = new LomDateImpl(time, description);
    }

    public void setDescription(LomMultilingualString in) {
        description = (LomMultilingualStringImpl) in;
    }

    public void setPerson(VCard in) {
        person = (VCardImpl) in;
    }

    public void setPerson(String in) {
        person = new VCardImpl(in);
    }

    public void buildSvo(SvoReference in, String prefix, SvoPreferences prefs) {
        if (!"".equals(prefix)) prefix += ".";
        SvoReference ref = in.getUniverse().createAnonymous("lom.annotation");
        in.addRelationR(prefix + "lom.annotation", ref);
        if (date != null) date.buildSvo(ref, "lom.date", prefs);
        if (description != null) ref.addRelationS("lom.description", description.getValue(prefs));
        if (person != null) person.buildSvo(in, prefix + "lom.person", prefs);
        if (description != null) {
            String value = "";
            if (date != null) value += date.keyedValue(prefs);
            value += ":";
            if (person != null) value += person.keyedValue();
            value += ":";
            value += description.getValue(prefs);
            ref.addRelationS("lom.keyed-value", value);
        }
    }

    public LomAnnotationImpl reproduce() {
        LomAnnotationImpl out = new LomAnnotationImpl();
        if (person != null) out.person = person.reproduce();
        if (date != null) out.date = date.reproduce();
        if (description != null) out.description = description.reproduce();
        return out;
    }
}
