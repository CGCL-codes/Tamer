package net.taylor.search.service;

import org.jboss.seam.annotations.Name;

/**
 * SearchPage represents an entry in the paging navigation.
 *
 * @author anichols
 */
@Name("SearchPage")
public class SearchPage {

    private String text;

    private String currentPage;

    /**
	 * Constructor for SearchPage when the user has a string representation of the text
	 * they want displayed and a string representation of the page number.
	 * */
    public SearchPage(String text, String currentPage) {
        this.text = text + " ";
        this.currentPage = Integer.toString(Integer.valueOf(currentPage));
    }

    /**
	 * Constructor for SearchPage when the user has a string representation of the text
	 * they want displayed and an integer for the page number.
	 * */
    public SearchPage(String text, int currentPage) {
        this.text = text + " ";
        this.currentPage = Integer.toString(currentPage);
    }

    /**
	 * Constructor for SearchPage when the text and page number are the same.
	 * */
    public SearchPage(int textAndPage) {
        this.text = (Integer.toString(textAndPage) + " ");
        this.currentPage = Integer.toString(textAndPage);
    }

    /**
	 * @return the text to be displayed on the page.
	 * */
    public String getText() {
        return text;
    }

    /**
	 * @param text the desired text to be displayed onto the page. Cannot be null. 
	 * */
    public void setText(String text) {
        this.text = text;
    }

    /**
	 * @return the currentPage parameter to be passed in the URL
	 * */
    public String getCurrentPage() {
        return currentPage;
    }

    /**
	 * @param currentPage the page that the text represents.
	 * */
    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }
}
