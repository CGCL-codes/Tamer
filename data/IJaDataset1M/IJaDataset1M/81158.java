package net.sf.dub.miniframework.controller;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public abstract class ProcessableController extends AbstractController {

    public ProcessableController(Controller parentController) {
        super(parentController);
    }

    public abstract void onNext() throws ProcessException;

    public abstract void onBack() throws ProcessException;

    public String getProcessStepNext() {
        return null;
    }

    public String getProcessStepBack() {
        return null;
    }
}
