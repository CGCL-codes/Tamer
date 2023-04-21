package game.rushhour.carmodel;

import java.awt.Color;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.IDomainModel;

/**
 * CarModel specific entity.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-03-21
 */
public class CarModel extends GenCarModel {

    private static final long serialVersionUID = 1174415685658L;

    private static Log log = LogFactory.getLog(CarModel.class);

    /**
	 * Constructs carModel within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public CarModel(IDomainModel model) {
        super(model);
    }

    public Color getColor() {
        return new Color(getRed(), getGreen(), getBlue());
    }
}
