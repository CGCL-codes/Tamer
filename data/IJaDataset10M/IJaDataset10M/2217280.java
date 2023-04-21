package com.patientis.model.order;

import static com.patientis.model.common.ModelReference.*;
import com.patientis.model.common.Converter;

/**
 * OrderInstanceTransition
 * 
 */
public class OrderInstanceTransitionModel extends OrderInstanceTransitionDataModel {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public OrderInstanceTransitionModel() {
    }

    /**
     * Override and handle patient model custom fields
     * 
     * @see com.patientis.model.common.IBaseModel#getValue(com.patientis.model.common.ModelReference)
     */
    @Override
    public Object getValue(int modelRefId) {
        switch(modelRefId) {
            case ORDERINSTANCETRANSITION_ROLES:
                return Converter.convertDisplayStringList(getRoles());
        }
        return super.getValue(modelRefId);
    }
}
