package edu.ucdavis.genomics.metabolomics.xdoclet.task.swt;

import xdoclet.TemplateSubTask;

/**
 * @author wohlgemuth
 * is used for label generation
 * @ant.element   display-name="Label Provider" name="labelprovider" parent="edu.ucdavis.genomics.metabolomics.xdoclet.task.swt.SWTTask"
 */
public class LabelProviderSubTask extends TemplateSubTask {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2L;

    /**
	 * the fild for this task
	 */
    private static String DEFAULT_TEMPLATE_FILE = "resources/swt-labelprovider.xdt";

    public LabelProviderSubTask() {
        setDestinationFile("{0}LabelProvider.java");
        setTemplateURL(getClass().getResource(DEFAULT_TEMPLATE_FILE));
        setHavingClassTag("swt");
        setAcceptAbstractClasses(true);
        setAcceptInterfaces(true);
    }
}
