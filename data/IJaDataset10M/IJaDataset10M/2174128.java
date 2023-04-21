package org.jostraca.directive;

import org.jostraca.BlockList;
import org.jostraca.Template;
import org.jostraca.TemplateActionHandler;
import org.jostraca.util.PropertySet;
import org.jostraca.unit.BasicUnitList;
import org.jostraca.transform.TextualTransformManagerTable;

/** Directive actions are performed by classes implementing this interface.
 */
public interface Directive {

    /** Perform directive operations on template.
   *  @return new blocks or null if no new blocks
   */
    public String perform(String pDirectiveName, String pArguments, BasicUnitList pUnitList, TemplateActionHandler pTemplateActionHandler, PropertySet pPropertySet, TextualTransformManagerTable pTextualTransformManagerTable, Template pTemplate) throws DirectiveException;

    public String getName();

    /** Should include value of getName() */
    public String[] getAliases();
}
