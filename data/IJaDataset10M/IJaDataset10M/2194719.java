package org.yaoqiang.bpmn.model.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * XMLComplexChoice
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public abstract class XMLComplexChoice extends XMLElement {

    protected List<XMLElement> choices;

    protected XMLElement choosen;

    public XMLComplexChoice(XMLComplexElement parent) {
        super(parent);
        fillChoices();
    }

    public List<XMLElement> getChoices() {
        return choices;
    }

    public XMLElement getChoosen() {
        return choosen;
    }

    public void setChoosen(XMLElement ch) {
        this.choosen = ch;
    }

    public boolean isEmpty() {
        return choosen == null || choosen.isEmpty();
    }

    protected abstract void fillChoices();

    public Object clone() {
        XMLComplexChoice d = (XMLComplexChoice) super.clone();
        d.choices = new ArrayList<XMLElement>();
        d.choosen = null;
        for (XMLElement c : choices) {
            XMLElement cloned = (XMLElement) c.clone();
            d.choices.add(cloned);
            cloned.setParent(d);
            if (d.choosen == null && this.choosen != null && this.choosen.equals(c)) {
                d.choosen = cloned;
            }
        }
        return d;
    }
}
