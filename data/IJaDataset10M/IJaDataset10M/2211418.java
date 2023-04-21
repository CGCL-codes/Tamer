package edu.diseno.jaspict3d.reader.action;

import edu.diseno.jaspict3d.control.SceneManager;
import edu.diseno.jaspict3d.model.ModelComponent;

/**
 * Creates an aspect.
 * @author maximo
 * @email vezzmax+disenio@gmail.com Dise�o de Sistemas UNC
 */
public class CreateAspectAction extends CreateSceneComponentAction {

    /**
   * Contructor.
   * @param sceneManager The SceneManager.
   * @param aspectName The Aspect Name
   */
    public CreateAspectAction(final SceneManager sceneManager, final String aspectName) {
        super(sceneManager, aspectName);
    }

    /**
   * Creates an aspect and ads it to the model scene.
   */
    public ModelComponent execute() {
        ModelComponent component = getSceneManager().getComponentFactory().createAspect(getName());
        getSceneManager().getModelScene().addModelComponent(component);
        return component;
    }
}
