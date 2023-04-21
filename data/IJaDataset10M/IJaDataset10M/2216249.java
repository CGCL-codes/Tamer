package fr.kirin.logger.filter;

import javax.swing.JComponent;
import fr.kirin.logger.model.LoggerTreeModel;

/**This interface represent a GUI filter.
 * A filter is used to filtering data in the tree model, this interface the GUI part of the filter.
 * a Filter GUI will be fronted on a TreeFilter to add to it a Graphical interface it will
 * be added to the TreeDisplayer
 * 
 * 
 * 
 * 
 * @author kirin
 * @see TreeFilter
 *
 */
public interface FilterGUI {

    /**register the tree model to this view.
     * 
     * @param model the model to use.
     */
    public abstract void registerModel(LoggerTreeModel model);

    /**get the GUI component for this filter.
	 * 
	 * @return the GUI component.
	 */
    public abstract JComponent getGUI();

    /**
	 * 
	 * @return the filter associated with this GUI.
	 */
    public abstract TreeFilter getFilter();
}
