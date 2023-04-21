package org.plazmaforge.bsolution.payroll.client.swing;

import org.plazmaforge.bsolution.payroll.client.swing.resources.GUIPayrollResources;

/**
 * @author Oleh Hapon
 * Date: 27.10.2004
 * Time: 8:27:15
 * $Id: GUIPayrollEnvironment.java,v 1.2 2010/04/28 06:28:19 ohapon Exp $
 */
public class GUIPayrollEnvironment {

    private static GUIPayrollResources resources;

    static {
        resources = new GUIPayrollResources();
    }

    public static GUIPayrollResources getResources() {
        return resources;
    }
}
