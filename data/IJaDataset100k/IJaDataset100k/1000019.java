package de.robowars.ui.resource;

import de.robowars.ui.Direction;
import de.robowars.ui.config.ButtonEnumeration;
import de.robowars.ui.config.ShapeEnumeration;
import de.robowars.ui.config.StaticEnumeration;
import de.robowars.comm.transport.FieldElementAppearances;
import de.robowars.comm.transport.FieldElementTypes;
import de.robowars.comm.transport.RobotEnumeration;

/**
 * @author Apollo
 *
 * <p>A LayerFactory manages the creation of Layer objects, which are shown
 * in the client containing a resource that could be one ore more images,
 * text or an animation.</p>
 */
public abstract class LayerFactory {

    public static final Integer SHAPE_DIAMOND = new Integer(0);

    public static final Integer SHAPE_SQUARE = new Integer(1);

    protected static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(LayerFactory.class);

    private static LayerFactory instance = null;

    /**
	 * <p>Constructs an instance of LayerFactory.</p>
	 */
    protected LayerFactory() {
    }

    /**
	 * <p>Returns an instance of the LayerFactory.</p>
	 * 
	 * @return A LayerFactory being an instance of this class.
	 * @throws ResourceException If the underlying ResourceManager failed to instantiate
	 */
    public static LayerFactory getInstance() throws ResourceException {
        if (log.isDebugEnabled()) {
            log.debug("Retrieving instance");
        }
        if (instance == null) {
            instance = new LayerFactoryImpl();
        }
        return instance;
    }

    /**
	 * <p>Creates a certain Layer (a field element of the board) being specified by
	 * an initial direction that is defining
	 * from which perspective the field element represented by the Layer is
	 * shown.</p>
	 * 
	 * @param t The type of the field element that is represented by the Layer.
	 * @param a The appearance of the field element.
	 * @param d The initial Direction of the Layer.
	 * @return A Layer specified by type and dir.
	 * @throws ResourceException If the underlying Resource of the Layer could not be found
	 */
    public abstract BoardLayer createLayer(FieldElementTypes t, FieldElementAppearances a, Direction dir) throws ResourceException;

    /**
	 * <p>Creates an invisible Layer being specified by the shape of its clickable area.</p>
	 * 
	 * @param cmd A command that identifies the action this Layer performs.
	 * @param e The shape of the clickable area of this Layer.
	 * @return A Layer specified by a shape.
	 * @throws ResourceException If the underlying Resource of the Layer could not be found.
	 */
    public abstract ActionLayer createActionLayer(String cmd, ShapeEnumeration e) throws ResourceException;

    /**
	 * <p>Creates a layer with different states to achieve the functionality of a button.</p>
	 * @param The type of the button that shall be created.
	 * @return The specified ButtonLayer.
	 * @throws ResourceException If the underlying Resource of the button could not be found. 
	 */
    public abstract ButtonLayer createButtonLayer(ButtonEnumeration type) throws ResourceException;

    /**
	 * <p>Creates a ButtonLayer that represents a card in the game.</p>
	 * @param cardId The unique id of the card.
	 * @return A ButtonLayer.
	 * @throws ResourceException If the underlying Resource of the card could not be found. 
	 */
    public abstract ButtonLayer createCardLayer(int cardId) throws ResourceException;

    /**
	 * <p>Creates a static Layer showing one image.</p>
	 * 
	 * @param e The type of the static layer. Applying "null" creates an empty static Layer with no image and no text.
	 * @return A static Layer containing an image.
	 * @throws ResourceException If the underlying Resource of the Layer could not be found
	 */
    public abstract StaticLayer createStaticLayer(StaticEnumeration e) throws ResourceException;

    /**
	 * <p>Creates a static layer showing the specified text.</p>
	 * 
	 * @param text A String representing the label of this layer.. Applying "null" creates an empty static Layer with no image and no text.
	 * @return A static Layer with text
	 * @throws ResourceException If the underlying Resource of the Layer could not be found
	 */
    public abstract StaticLayer createStaticLayer(String text) throws ResourceException;

    /**
	 * <p>Creates a certain Robot (on the board) being specified by
	 * an initial direction that is defining
	 * from which perspective the robot is
	 * shown.</p>
	 * 
	 * @param robot The type of the robot that is represented by the Layer.
	 * @param d The initial Direction of the Robot.
	 * @return A Layer specified by type and dir.
	 * @throws ResourceException If the underlying Resource of the Layer could not be found
	 */
    public abstract Robot createRobot(RobotEnumeration robot, Direction d) throws ResourceException;

    /**
	 * <p>Creates a static layer showing the specified text.</p>
	 * 
	 * 
	 * @param e The type of the static layer. Applying "null" has the same effect as createStaticLayer(String text).
	 * @param text A String representing the label of this layer. Applying "null" has the same effect as createStaticLayer(StaticEnumeration e).
	 * @return A static Layer with text and image.
	 * @throws ResourceException If the underlying Resource of the Layer could not be found
	 */
    public abstract StaticLayer createStaticLayer(StaticEnumeration e, String text) throws ResourceException;
}
