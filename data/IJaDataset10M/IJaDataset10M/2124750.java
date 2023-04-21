package org.jostraca.directive;

import org.jostraca.Template;
import org.jostraca.TemplateActionHandler;
import org.jostraca.unit.BasicUnitList;
import org.jostraca.transform.TextualTransformManager;
import org.jostraca.transform.TextualTransformManagerTable;
import org.jostraca.transform.CollapseWhiteSpaceTransform;
import org.jostraca.transform.TextualTransform;
import org.jostraca.util.PropertySet;

/** Remove new lines in text elements.
 *  Usage:<br />
 *  &nbsp;&nbsp;&lt;% @collapseWhiteSpace %&gt; 
 *  &nbsp;&nbsp;&lt;% @| %&gt; 
 */
public class CollapseWhiteSpaceDirective implements Directive {

    private static final String NAME = "collapseWhiteSpace";

    private static final String[] ALIASES = new String[] { NAME, "|" };

    private TextualTransform iCollapseWhiteSpaceTransform = new CollapseWhiteSpaceTransform();

    public CollapseWhiteSpaceDirective() throws DirectiveException {
    }

    public String perform(String pDirectiveName, String pArguments, BasicUnitList pUnitList, TemplateActionHandler pTemplateActionHandler, PropertySet pPropertySet, TextualTransformManagerTable pTextualTransformManagerTable, Template pTemplate) throws DirectiveException {
        try {
            TextualTransformManager textTTM = pTextualTransformManagerTable.getTextTTM();
            if (textTTM.contains(iCollapseWhiteSpaceTransform)) {
                textTTM.remove(iCollapseWhiteSpaceTransform);
            } else {
                textTTM.prepend(iCollapseWhiteSpaceTransform);
            }
            pTemplateActionHandler.append(pArguments);
            return null;
        } catch (Exception e) {
            throw new DirectiveException(e.getMessage());
        }
    }

    public String getName() {
        return NAME;
    }

    /** Should include value of getName() */
    public String[] getAliases() {
        return ALIASES;
    }
}
