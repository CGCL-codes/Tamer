package com.simpledata.bc.datamodel;

/**
* WorkPlace is a type of WorkSheet linked to a Tarif
*/
public abstract class WorkPlace extends WorkSheet {

    /**
	* constructor.. should not be called by itself. use WorkSheet#createWorkSheet(Dispatcher d,Class c)
	*/
    public WorkPlace(WorkSheetContainer parent, String title, String id, String key) {
        super(parent, title, id, key);
    }

    /** XML **/
    public WorkPlace() {
    }
}
