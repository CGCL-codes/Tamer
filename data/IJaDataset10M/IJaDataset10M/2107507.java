package net.sf.mailsomething.help;

/**
 * @author Administrator
 * @created 13-10-2002
 * 
 */
public interface HelpPage extends HelpItem {

    /**
	 * Method getTitle.
	 * @return String The title of the helppage
	 */
    public String getTitle();

    /**
	 * Sets the title of the helppage. Typically (in this implementation)
	 * displayed as a header.
	 * 
	 * @param title
	 */
    public void setTitle(String title);

    /**
	 * Method for getting all the links of the page. 
	 * 
	 * @return HelpItem[]
	 */
    public HelpItem[] getLink();

    public void addLink(HelpItem item);

    /**
	 * The difference between a link and a content item is how
	 * they are handled (displayed) within the helppage. Ie, a content
	 * item is part of the helppage, and it is displayed like that, as
	 * it is part of it, a paragraph or similar.
	 * 
	 * @param item
	 */
    public void addContent(HelpItem item);

    public HelpItem[] getContent();

    public boolean isIndex();

    public void setIndex(boolean index);

    public void removeContent(HelpItem item);

    public void removeLink(HelpItem item);
}
