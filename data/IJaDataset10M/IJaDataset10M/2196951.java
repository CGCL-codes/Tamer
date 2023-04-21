package no.ugland.utransprod.gui.model;

import java.beans.PropertyChangeListener;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import no.ugland.utransprod.model.Colli;
import no.ugland.utransprod.model.OrderLine;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.ArrayListModel;

/**
 * GUI-modell for kolli
 * 
 * @author atle.brekka
 * 
 */
public class ColliModel extends AbstractModel<Colli, ColliModel> {

    private static final long serialVersionUID = 1L;

    public static final String PROPERTY_COLLI_NAME = "colliName";

    public static final String PROPERTY_ORDER_LINES = "orderLines";

    public static final String PROPERTY_NUMBER_OF_COLLIES = "numberOfCollies";

    public static final String PROPERTY_COLLI_NAME_AND_NUMBER = "colliNameAndNumber";

    private final ArrayListModel orderLineList;

    /**
	 * @param colli
	 */
    public ColliModel(Colli colli) {
        super(colli);
        orderLineList = new ArrayListModel();
        if (object.getOrderLines() != null) {
            orderLineList.addAll(object.getOrderLines());
        }
    }

    public String getColliName() {
        return object.getColliName();
    }

    public void setColliName(String colliName) {
        String oldName = getColliName();
        object.setColliName(colliName);
        firePropertyChange(PROPERTY_COLLI_NAME, oldName, colliName);
    }

    public String getColliNameAndNumber() {
        return object.getColliName() + (object.getNumberOfCollies() != null ? " - " + object.getNumberOfCollies() : "");
    }

    public String getNumberOfCollies() {
        return object.getNumberOfCollies() != null ? String.valueOf(object.getNumberOfCollies()) : null;
    }

    /**
	 * @param colliName
	 */
    public void setNumberOfCollies(String numberOfCollies) {
        String oldNumber = getNumberOfCollies();
        object.setNumberOfCollies(!StringUtils.isEmpty(numberOfCollies) ? Integer.valueOf(numberOfCollies) : null);
        firePropertyChange(PROPERTY_NUMBER_OF_COLLIES, oldNumber, numberOfCollies);
    }

    /**
	 * @return ordrelinjer
	 */
    @SuppressWarnings("unchecked")
    public List<OrderLine> getOrderLines() {
        return orderLineList;
    }

    /**
	 * @see no.ugland.utransprod.gui.model.AbstractModel#addBufferChangeListener(java.beans.PropertyChangeListener,
	 *      com.jgoodies.binding.PresentationModel)
	 */
    @Override
    public void addBufferChangeListener(PropertyChangeListener listener, PresentationModel presentationModel) {
        presentationModel.getBufferedModel(PROPERTY_COLLI_NAME).addValueChangeListener(listener);
        presentationModel.getBufferedModel(PROPERTY_ORDER_LINES).addValueChangeListener(listener);
    }

    /**
	 * @see no.ugland.utransprod.gui.model.AbstractModel#getBufferedObjectModel(com.jgoodies.binding.PresentationModel)
	 */
    @Override
    public ColliModel getBufferedObjectModel(PresentationModel presentationModel) {
        ColliModel colliModel = new ColliModel(new Colli());
        colliModel.setColliName((String) presentationModel.getBufferedValue(PROPERTY_COLLI_NAME));
        return colliModel;
    }

    public void firePropertyChanged(String propertyName) {
        firePropertyChange(propertyName, null, getColliNameAndNumber());
    }
}
