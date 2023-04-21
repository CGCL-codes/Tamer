package com.global360.sketchpadbpmn.documents.xpdl.elements;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface DataFieldHolder {

    public DataFieldElement findDataField(String dataFieldId);

    public void addDataField(DataFieldElement dataFieldElement);

    public void removeDataField(String dataFieldId);

    public void removeDataField(DataFieldElement dataFieldElement);
}
