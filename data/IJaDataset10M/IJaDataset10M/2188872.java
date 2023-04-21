package kohary.datamodel.html.element;

import kohary.datamodel.CssDocument;
import kohary.datamodel.Tag;
import kohary.datamodel.dapi.Attribute;
import kohary.datamodel.dapi.Input;

/**
 *
 * @author Godric
 */
public class TextAreaHtmlElement extends HtmlElement {

    public TextAreaHtmlElement(Attribute attribute, CssDocument cssDocument, Tag div) {
        super(attribute, cssDocument, div);
        setUpLabel();
        Input input = attribute.getInput();
        String disabled = (input.isDisabled()) ? "disabled=disabled" : "";
        Tag textarea = new Tag("textarea", "name=${" + attribute.getInput().getVariable().getLabel() + "} " + disabled);
        textarea.add("${" + attribute.getInput().getVariable().getLabel() + "}");
        div.add(textarea);
    }
}
