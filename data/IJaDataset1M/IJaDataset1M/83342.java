package edu.diseno.jaspict3d.reader.action;

import edu.diseno.jaspict3d.control.SceneManager;
import edu.diseno.jaspict3d.model.ModelComponent;

/**
 * Action to create an Around Advice component.
 * @author maximo
 * @email vezzmax+disenio@gmail.com Dise�o de Sistemas UNC
 */
public class CreateAroundAdviceAction extends CreateSceneComponentAction {

    /**
   * Constructor.
   * @param theSceneManager .
   * @param adviceName .
   */
    public CreateAroundAdviceAction(final SceneManager theSceneManager, final String adviceName) {
        super(theSceneManager, adviceName);
    }

    /**
   * Creates an Advice Around and adds it to the scene.
   */
    public ModelComponent execute() {
        ModelComponent component = getSceneManager().getComponentFactory().createAdviceAround(getName());
        getSceneManager().getModelScene().addModelComponent(component);
        return component;
    }
}
