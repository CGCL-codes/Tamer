package org.jostraca.directive;

import org.jostraca.Template;
import org.jostraca.TemplateActionHandler;
import org.jostraca.unit.BasicUnitList;
import org.jostraca.transform.TextualTransformManagerTable;
import org.jostraca.util.PropertySet;

/** Support class for {@link Directive} implementations.
 */
public abstract class DirectiveSupport implements Directive {

    protected DirectiveParser iDirectiveParser = new DirectiveParser();

    public abstract String perform(String pDirectiveSupportName, String pArguments, BasicUnitList pUnitList, TemplateActionHandler pTemplateActionHandler, PropertySet pPropertySet, TextualTransformManagerTable pTextualTransformManagerTable, Template pTemplate) throws DirectiveException;

    public String[] getAliases() {
        return new String[] { getName() };
    }
}
