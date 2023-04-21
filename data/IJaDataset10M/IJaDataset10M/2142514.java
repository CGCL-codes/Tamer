package au.edu.diasb.emmet;

import org.springframework.web.servlet.ModelAndView;

public class EmmetResponse extends ModelAndView {

    /**
     * Construct a response to be rendered using a JSP
     * 
     * @param name the JSP name
     * @param object the object to be rendered.
     * @param key the map key for the object.
     * @param props the Properties that will be made available to the JSP
     */
    public EmmetResponse(String name, Object object, String key) {
        super(name);
        this.addObject(key, object);
    }

    /**
     * Construct a response to be rendered using a JSP
     * 
     * @param name the JSP name
     */
    public EmmetResponse(String name) {
        super(name);
    }
}
