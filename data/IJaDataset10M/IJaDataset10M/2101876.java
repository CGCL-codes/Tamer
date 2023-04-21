package com.rapidminer.io.process;

import org.w3c.dom.Element;
import com.rapidminer.operator.ExecutionUnit;
import com.rapidminer.operator.Operator;

/** Filter applied to operators and processes during import and export of
 *  process XML files. E.g., the ProcessRenderer uses this functionality
 *  to add GUI information to the process XML.
 * 
 * @author Simon Fischer
 *
 */
public interface ProcessXMLFilter {

    public void operatorExported(Operator operator, Element element);

    public void operatorImported(Operator operator, Element element);

    public void executionUnitExported(ExecutionUnit unit, Element element);

    public void executionUnitImported(ExecutionUnit unit, Element element);
}
