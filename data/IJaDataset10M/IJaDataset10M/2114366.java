package com.nokia.ats4.appmodel.main.controller;

import com.nokia.ats4.appmodel.model.KendoProject;

/**
 * AutoSaver
 * 
 * Implementors of this interface Auto-save the give project.
 * 
 * @author Esa-Matti Miettinen
 * @version $Revision: 2 $
 */
public interface AutoSaver {

    void autoSaveProject(KendoProject activeProject);
}
