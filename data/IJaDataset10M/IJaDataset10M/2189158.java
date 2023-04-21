package org.larozanam.arq.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;

public class FieldTip {

    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
    private String text;

    @InjectContainer
    private ClientElement element;

    @Inject
    private RenderSupport render;

    @AfterRender
    protected void afterRender(MarkupWriter writer) {
        Element e = writer.getDocument().getElementById(element.getClientId());
        e.attribute("onFocus", String.format("if (this.value == '%s'){ this.value = '';}", text));
        e.attribute("onBlur", String.format("if (this.value == ''){ this.value = '%s';}", text));
        render.addScript(String.format("if (document.getElementById('%s').value == ''){ document.getElementById('%s').value = '%s';}", element.getClientId(), element.getClientId(), text));
    }
}
