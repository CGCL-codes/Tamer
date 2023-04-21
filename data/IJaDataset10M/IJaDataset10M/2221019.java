package br.ita.autowidget.defaultgeneration.templates.html;

import br.ita.autowidget.html.util.HtmlCorrector;
import br.ita.autowidget.metadata.ComponentMetadata;

public class HtmlIntegerTemplate implements IHtmlComponentTemplate {

    @Override
    public String display(ComponentMetadata beanMetadata) {
        if (beanMetadata.getBean() != null) {
            return HtmlCorrector.encode(String.format("%d", beanMetadata.getBean()));
        }
        return "&nbsp";
    }

    @Override
    public String edit(ComponentMetadata beanMetadata) {
        String encodedValue = null;
        if (beanMetadata.getBean() != null) {
            encodedValue = HtmlCorrector.encode(beanMetadata.getBean().toString());
        }
        return TagHelper.buildInputTag("text-box number", "text", beanMetadata.getNameAttribute(), null, encodedValue);
    }
}
