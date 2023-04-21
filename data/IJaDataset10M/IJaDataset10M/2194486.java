package controller;

import java.util.Collection;
import model.common.IModel;
import org.lwjgl.util.vector.Vector2f;
import controller.common.IController;
import controller.common.IEntityFactory;
import controller.components.KeyboardComponent;
import controller.components.MouseComponent;

public interface IMainController {

    /**
	 * Instantiates a new IModel/IController pair. The only way to create a new
	 * instance of an IModel/IController.
	 * 
	 * @param factory
	 *            the entity factory of the sought after IModel/IController pair
	 * @return the IController instance of the newly instantiated
	 *         IModel/IController pair
	 */
    public <M extends IModel, C extends IController<M>, T extends IEntityFactory<M, C>> C instantiate(IEntityFactory<M, C> factory);

    /**
	 * Instantiates a new IModel/IController pair. The only way to create a new
	 * instance of an IModel/IController.
	 * 
	 * @param factory
	 *            the entity factory of the sought after IModel/IController pair
	 * @param position
	 *            the desired position of the entity in the game world
	 * @return the IController instance of the newly instantiated
	 *         IModel/IController pair
	 */
    public <M extends IModel, C extends IController<M>, T extends IEntityFactory<M, C>> C instantiate(IEntityFactory<M, C> factory, Vector2f position);

    /**
	 * Removes an existing IController instance and corresponding IModel from
	 * the game world
	 * 
	 * @param controller
	 *            the IController instance to be removed
	 */
    public <M extends IModel> void remove(IController<M> controller);

    /**
	 * Removes all IModel and IController instances from the game world
	 */
    public void removeAll();

    /**
	 * Finds all controllers of a specific sub-type to IController
	 * 
	 * @param <T>
	 *            the sought after sub-type to IController
	 * @param controllerType
	 *            the sought after type class
	 * @return a list of controllers of type T
	 */
    public <M extends IModel, C extends IController<M>> Collection<C> find(Class<C> type);

    /**
	 * @return a list of all IController instances
	 */
    public Collection<IController<? extends IModel>> getControllers();

    /**
	 * @return gets the keyboard component
	 */
    public KeyboardComponent getKeyboardComponent();

    /**
	 * @return gets the mouse component
	 */
    public MouseComponent getMouseComponent();

    /**
	 * Initializes the main controller and it's components
	 */
    public void initialize();

    /**
	 * Updates the main controller, it's components and all IController
	 * instances and steps forward in time
	 */
    public void update();
}
