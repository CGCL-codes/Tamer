package org.jostraca;

import org.jostraca.unit.UnitList;
import org.jostraca.util.StandardException;

/** Process a template element: text, script, expression, directive, etc.
 *  Places content in appropriate section.
 */
public interface TemplateElementProcessor {

    public static final int ELEMENT_TYPE_none = 0;

    public static final int ELEMENT_TYPE_text = 1000;

    public static final int ELEMENT_TYPE_script = 2000;

    public static final int ELEMENT_TYPE_expression = 3000;

    public static final int ELEMENT_TYPE_directive = 4000;

    /** Process parsed template block (text or script).
   *  @param pBlock Block to process.
   */
    public String process(Block pBlock) throws StandardException;

    public String getContent();

    /** transitional method */
    public UnitList getUnitList();
}
