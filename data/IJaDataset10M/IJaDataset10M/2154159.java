package br.com.sysmap.crux.gwt.client;

import br.com.sysmap.crux.core.client.declarative.DeclarativeFactory;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Label;

/**
 * Represents a LabelFactory DeclarativeFactory
 * @author Thiago Bustamante
 *
 */
@DeclarativeFactory(id = "label", library = "gwt")
public class LabelFactory extends AbstractLabelFactory<Label> {

    @Override
    public Label instantiateWidget(Element element, String widgetId) {
        return new Label();
    }
}
