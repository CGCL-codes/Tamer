package gui.model;

/**
 * HoverItem is used to abstract from all objects that can have additional
 * information to be displayed by the application. Therefore, it is necessary
 * to tell a feature whether it is chosen or not, so that it is able to
 * draw itself accordingly.
 * 
 * @author Christian Holz
 */
public interface HoverItem {

    /**
	 * The method tells the HoverItem whether it has been chosen.
	 * 
	 * @param chosen if it is chosen
	 */
    public void setChosen(boolean chosen);

    /**
	 * @return chosen state of HoverItem
	 */
    public boolean isChosen();
}
