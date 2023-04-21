package org.equanda.tapestry.navigation;

/**
 * Getters/Setters for current tab page from the Tabstastic page stuff
 * Current page needs to be saved, for instance, when you're in some page of a table, select a link
 * and want to go back to the same tab page
 *
 * @author Florin
 */
public interface HasMultiplePagesTableParameter {

    public abstract String getCurrentPage();

    public abstract void setCurrentPage(String currentPage);
}
