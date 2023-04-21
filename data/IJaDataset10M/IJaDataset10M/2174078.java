package com.beanview;

import com.beanview.model.ConvertingSelectionModel;

/**
 * This class is used to mark a visual component that supports multiple
 * selections (specifically, a one-to-many relationship). This component type is
 * used to model collections (including both array and java.util.Collection).
 * 
 * @author $Author: wiverson $
 * @version $Revision: 1.1.1.1 $, $Date: 2006/09/19 04:21:37 $
 * 
 */
public interface MultipleSelectComponent extends PropertyComponent {

    void setMultipleSelectOptions(ConvertingSelectionModel manyToManyValues);

    ConvertingSelectionModel getMultipleSelectOptions();
}
