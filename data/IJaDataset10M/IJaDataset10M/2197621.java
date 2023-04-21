package com.windsor.node.service.helper;

public interface ArgValidationHelper {

    /**
     * Validates
     * 
     * @param args
     * @param index
     * @param optional
     * @return
     */
    String validate(String[] args, Integer index, Boolean optional);
}
