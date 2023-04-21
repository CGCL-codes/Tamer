package de.t5book.mixins;

import java.util.List;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.mixins.Autocomplete;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONLiteral;
import org.apache.tapestry5.json.JSONObject;
import de.t5book.entities.Employee;

public class AutocompleteExt extends Autocomplete {

    @Inject
    private ComponentResources resources;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String updateElementFunction;

    protected void configure(final JSONObject config) {
        super.configure(config);
        if (this.resources.isBound("updateElementFunction")) {
            config.put("updateElement", new JSONLiteral(updateElementFunction));
        }
    }

    protected void generateResponseMarkup(MarkupWriter writer, List matches) {
        writer.element("ul");
        for (Object o : matches) {
            Employee employee = (Employee) o;
            writer.element("li");
            writer.attributes("id", employee.getId());
            writer.write(employee.getFirstName() + " " + employee.getLastName());
            writer.end();
        }
        writer.end();
    }
}
