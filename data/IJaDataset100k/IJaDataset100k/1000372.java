package org.apache.xmlbeans.samples.xquery;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.samples.xquery.employees.PhoneType;

/**
 * This class demonstrates how to use the selectPath method to execute XPath
 * expressions. Compare the code here with the code in the ExecQuery class. 
 * That class uses the execQuery method to execute XQuery expressions.
 * <p/>
 * You can call the selectPath method from either an XmlObject or XmlCursor 
 * instance. Calling from XmlObject returns an XmlObject array. Calling 
 * from XmlCursor returns void, and you use methods of the cursor to 
 * navigate among returned "selections".
 */
public class SelectPath {

    static final String m_namespaceDeclaration = "declare namespace xq='http://xmlbeans.apache.org/samples/xquery/employees';";

    /**
     * Prints the XML bound to <em>empDoc</em>, uses XPath to 
     * retrieve elements containing work phone numbers, changes the numbers 
     * to another number, then prints the XML again to display the changes.
     * 
     * This method demonstrates the following characteristics of the selectPath method: 
     * 
     * - it supports expressions that include predicates 
     * - the XML it returns is the XML queried against -- not a copy, as with results
     * returned via execQuery methods and XQuery. Changes to this XML update 
     * the XML queried against. 
     * - selectPath called from an XMLBean type (instead of a cursor) returns an 
     * array of results (if any). These results can be cast to a matching type 
     * generated from schema.
     * 
     * @param empDoc The incoming XML.
     * @return <code>true</code> if the XPath expression returned results;
     * otherwise, <code>false</code>.
     */
    public boolean updateWorkPhone(XmlObject empDoc) {
        boolean hasResults = false;
        System.out.println("XML as received by updateWorkPhone method: \n\n" + empDoc.toString());
        String pathExpression = "$this/xq:employees/xq:employee/xq:phone[@location='work']";
        XmlObject[] results = empDoc.selectPath(m_namespaceDeclaration + pathExpression);
        if (results.length > 0) {
            hasResults = true;
            PhoneType[] phones = (PhoneType[]) results;
            for (int i = 0; i < phones.length; i++) {
                phones[i].setStringValue("(206)555-1234");
            }
            System.out.println("\nXML as updated by updateWorkPhone method (each work \n" + "phone number has been changed to the same number): \n\n" + empDoc.toString() + "\n");
        }
        return hasResults;
    }

    /**
     * Uses the XPath text() function to get values from <name>
     * elements in received XML, then collects those values as the value of a
     * <names> element created here.
     * <p/>
     * Demonstrates the following characteristics of the selectPath method: 
     * <p/>
     * - It supports expressions that include XPath function calls.
     * - selectPath called from an XmlCursor instance (instead of an XMLBeans 
     * type) places results (if any) into the cursor's selection set.
     * 
     * @param empDoc The incoming XML.
     * @return <code>true</code> if the XPath expression returned results;
     * otherwise, <code>false</code>.
     */
    public boolean collectNames(XmlObject empDoc) {
        boolean hasResults = false;
        XmlCursor pathCursor = empDoc.newCursor();
        pathCursor.toFirstChild();
        pathCursor.selectPath(m_namespaceDeclaration + "$this//xq:employee/xq:name/text()");
        if (pathCursor.getSelectionCount() > 0) {
            hasResults = true;
            XmlObject namesElement = null;
            try {
                namesElement = XmlObject.Factory.parse("<names/>");
            } catch (XmlException e) {
                e.printStackTrace();
            }
            XmlCursor namesCursor = namesElement.newCursor();
            namesCursor.toFirstContentToken();
            namesCursor.toEndToken();
            while (pathCursor.toNextSelection()) {
                namesCursor.insertChars(pathCursor.getTextValue());
                if (pathCursor.hasNextSelection()) {
                    namesCursor.insertChars(", ");
                }
            }
            pathCursor.dispose();
            namesCursor.dispose();
            System.out.println("\nNames collected by collectNames method: \n\n" + namesElement + "\n");
        }
        return hasResults;
    }
}
