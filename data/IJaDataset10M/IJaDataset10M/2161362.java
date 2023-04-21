package org.extwind.osgi.tapestry.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * @author Donf Yang
 * 
 */
public class ECheckbox {

    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String name;

    /**
	 * The value to be read or updated. If not bound, the Checkbox will attempt
	 * to edit a property of its container whose name matches the component's
	 * id.
	 */
    @Parameter("false")
    private boolean checked;

    @Parameter(value = "prop:componentResources.id", defaultPrefix = BindingConstants.LITERAL)
    private String clientId;

    @Parameter
    private String value;

    @Inject
    private ComponentResources resources;

    @BeginRender
    void begin(MarkupWriter writer) {
        writer.element("input", "type", "checkbox", "name", name, "id", clientId, "value", value, "checked", checked ? "checked" : null);
        resources.renderInformalParameters(writer);
    }

    @AfterRender
    void after(MarkupWriter writer) {
        writer.end();
    }
}
