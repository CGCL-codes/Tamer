package org.synerr.apps.templater;

import java.lang.Exception;

/** 
    Is thrown when their is a problem in processing or handling a call in the TemplateReader or TemplateWorker.
    @author Lucas McGregor
 **/
public class TemplateException extends Exception {

    public TemplateException() {
        super();
    }

    public TemplateException(String s) {
        super(s);
    }
}
