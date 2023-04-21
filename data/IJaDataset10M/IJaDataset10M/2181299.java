package org.hip.vif.servlets;

/**
 * This task creates the view to display the help about  
 * structured text formating rules. 
 * 
 * @author: Benno Luthiger
 */
public class ShowFormatHelpTask extends AbstractShowHelpTask {

    public static final String HELP_FILE = "format_help.html";

    protected String getHelpFileName() {
        return HELP_FILE;
    }
}
