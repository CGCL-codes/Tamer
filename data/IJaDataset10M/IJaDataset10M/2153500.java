package br.ita.autowidget.defaultgeneration.templates.html;

import java.util.List;
import br.ita.autowidget.metadata.ComponentMetadata;
import br.ita.autowidget.widgetbuilder.HtmlWidgetBuilder;

public class HtmlObjectTemplate implements IHtmlComponentTemplate {

    private final HtmlWidgetBuilder htmlWidgetBuilder;

    public HtmlObjectTemplate(HtmlWidgetBuilder widgetBuilder) {
        this.htmlWidgetBuilder = widgetBuilder;
    }

    @Override
    public String display(ComponentMetadata beanMetadata) {
        final StringBuilder generatedHtml = new StringBuilder();
        final String legend = beanMetadata.getDisplayName();
        if (beanMetadata.getProperties() != null) {
            for (ComponentMetadata property : (List<ComponentMetadata>) beanMetadata.getProperties()) {
                generatedHtml.append(TagHelper.buildDivTag("display-label", property.getDisplayName())).append(TagHelper.buildDivTag("display-value", htmlWidgetBuilder.buildDisplayWidget(property)));
            }
        }
        return TagHelper.buildFieldsetTag(legend, generatedHtml.toString());
    }

    @Override
    public String edit(ComponentMetadata beanMetadata) {
        final StringBuilder generatedHtml = new StringBuilder();
        final String legend = beanMetadata.getDisplayName();
        if (beanMetadata.getProperties() != null) {
            for (ComponentMetadata property : (List<ComponentMetadata>) beanMetadata.getProperties()) {
                generatedHtml.append(TagHelper.buildDivTag("edit-label", property.getDisplayName())).append(TagHelper.buildDivTag("edit-value", htmlWidgetBuilder.buildEditorWidget(property)));
            }
        }
        return TagHelper.buildFieldsetTag(legend, generatedHtml.toString());
    }
}
