package com.skruk.elvis.admin.gui;

/**
 * @author     skruk
 * @created    7 listopad 2003
 */
public interface WizardFinishListener {

    /**
	 *  Description of the Method
	 *
	 * @param  context   Description of the Parameter
	 * @param  progress  Description of the Parameter
	 * @param  wizard    Description of the Parameter
	 */
    void finishProcess(WizardContext context, Progressable progress, Wizard wizard);
}
