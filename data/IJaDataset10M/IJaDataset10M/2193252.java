package org.az.gsm.client.managed.ui;

import com.google.gwt.text.shared.AbstractRenderer;
import org.az.gsm.client.managed.request.ApplicationEntityTypesProcessor;
import org.az.gsm.client.managed.request.PersonProxy;
import org.az.gsm.client.managed.request.SkillProxy;
import org.az.gsm.client.managed.request.SkillRefProxy;
import org.az.gsm.client.scaffold.place.ProxyListPlace;

public abstract class ApplicationListPlaceRenderer_Roo_Gwt extends AbstractRenderer<ProxyListPlace> {

    public String render(ProxyListPlace object) {
        return new ApplicationEntityTypesProcessor<String>() {

            @Override
            public void handleSkillRef(SkillRefProxy isNull) {
                setResult("SkillRefs");
            }

            @Override
            public void handleSkill(SkillProxy isNull) {
                setResult("Skills");
            }

            @Override
            public void handlePerson(PersonProxy isNull) {
                setResult("Persons");
            }
        }.process(object.getProxyClass());
    }
}
