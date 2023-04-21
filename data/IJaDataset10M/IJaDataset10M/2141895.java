package org.herasaf.xacml.core.function.impl.setFunction;

/** <p>The implementation of the urn:oasis:names:tc:xacml:1.0:function:boolean-subset function.</p>
* <p>See: Apendix A.3 of the <a
* href="http://www.oasis-open.org/committees/tc_home.php?wg_abbrev=xacml#XACML20">
* OASIS eXtensible Access Control Markup Langugage (XACML) 2.0, Errata 29 June
* 2006</a> page 105, for further information.</p>
* 
* @author Stefan Oberholzer 
* @version 1.0
*/
public class BooleanSubsetFunction extends AbstractSubsetFunction<Boolean> {

    private static final long serialVersionUID = -367894928512846701L;

    private static final String ID = "urn:oasis:names:tc:xacml:1.0:function:boolean-subset";

    @Override
    public String toString() {
        return ID;
    }
}
