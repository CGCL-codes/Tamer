package org.chemicalcovers.model;

/**
 * CoverList interface made so CoverListModel only expose its add(Cover) method to search engines
 * @author P. Dal Farra
 *
 */
public interface ICoverList {

    public void add(ICoverInfo coverInfo);
}
