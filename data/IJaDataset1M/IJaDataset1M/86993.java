package com.dcivision.framework;

/**
 * <p>Class Name:       AutoLabelGenerator.java    </p>
 * <p>Description:      The class mainly handle the auto label generation logic.</p>
 * @author              Zoe Shum
 * @company             DCIVision Limited
 * @creation date       15/10/2003
 * @version             $Revision: 1.2 $
 */
public interface AutoLabelGenerator {

    public abstract String process(String template, Integer curRecordNo) throws ApplicationException;
}
