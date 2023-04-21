package org.jackysoft.web.ui.list;

import java.util.ArrayList;
import java.util.List;
import org.jackysoft.web.ui.HtmlElement;

public class HtmlOptionGroup extends HtmlElement {

    private int optionIndex;

    private List<HtmlOption> options;

    public HtmlOptionGroup() {
        super("optgroup");
        optionIndex = 0;
        options = new ArrayList<HtmlOption>();
    }

    public void setLabel(String value) {
        this.getEntity().addAttribute("label", value);
    }

    public String getLabel() {
        return this.getEntity().attributeValue("label");
    }

    @Override
    public void appendChild(HtmlElement newChild) {
        if (newChild instanceof HtmlOption) {
            super.appendChild(newChild);
            options.add(optionIndex++, (HtmlOption) newChild);
        }
    }

    public List<HtmlOption> getOptions() {
        return this.options;
    }
}
