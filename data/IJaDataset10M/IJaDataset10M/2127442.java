package net.ontopia.topicmaps.nav.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * PUBLIC: Provides model, view and skin information for the application
 * 
 * <p>Implementors wanting to provide special models, view or skins
 * can implement their own version which may use the controller.xml
 * configuration file. The new class must implement ControlConfigIF.
 */
public class ControlConfig implements ControlConfigIF {

    static Logger log = LoggerFactory.getLogger(ControlConfig.class.getName());

    String model = "simple";

    String view = "no_frames";

    String skin = "blue";

    String modelPath;

    String viewPath;

    String skinPath;

    String behaviour;

    String contentType;

    String resource;

    /**
   * Constructor which takes a path to the configuration file.
   */
    public ControlConfig(String resource) {
        this.resource = resource;
        makePaths();
    }

    /**
   * Updates the state of the object to include user preferences.
   *
   * The application makes its own default model, view and skin for a particular
   * request. <code>userUpdate</code> allows the user preferences to be 
   * incorporated
   *
   * @param model   a string representing the model choice
   * @param view    a string representing the view choice
   * @param skin    a string representing the skin choice   
   */
    public void update(String model, String view, String skin) {
        if (model != null && !model.equals("")) this.model = model;
        if (view != null && !view.equals("")) this.view = view;
        if (skin != null && !skin.equals("")) this.skin = skin;
        makePaths();
    }

    /**
   * Provides the internal logic for the object by making paths to the correct
   * model, view and skin given the application and user inputs. Called on 
   * construction and through ControlServlet when user updates preferences.
   */
    private void makePaths() {
        if (resource == null || resource.equals("/index.html")) resource = "/index.jsp";
        if (resource.equals("/topic.jsp")) modelPath = "/models/topic_" + model + ".jsp"; else if (resource.equals("/topicmap.jsp")) modelPath = "/models/topicmap_" + model + ".jsp"; else modelPath = "/models" + resource;
        if (resource.equals("/def_topic_occ.jsp") || resource.equals("/def_topicmap_occ.jsp") || resource.equals("/blank.jsp")) viewPath = "/views/template_plain.jsp"; else viewPath = "/views/template_" + view + ".jsp";
        skinPath = "skins/" + skin + ".css";
        behaviour = "no_frames";
        if (view.equals("frames")) behaviour = "frames";
        contentType = "text/html";
        if (view.equals("xml")) contentType = "text/xml";
    }

    public String getModelPath() {
        return modelPath;
    }

    public String getViewPath() {
        return viewPath;
    }

    public String getSkinPath() {
        return skinPath;
    }

    public String getBehaviour() {
        return behaviour;
    }

    public String getContentType() {
        return contentType;
    }

    public String getModel() {
        return model;
    }

    public String getView() {
        return view;
    }

    public String getSkin() {
        return skin;
    }
}
