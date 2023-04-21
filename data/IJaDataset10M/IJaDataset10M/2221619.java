package net.sf.webdeco.decoration;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Any class that implements this interface is able to do the actual decoration
 * work. They are probably called by {@link net.sf.webdeco.decoration.Decoration}.
 * 
 * @author ninan
 */
public interface Decorator {

    /**
     * This method has to render the decorated content. It is provided with the
     * already extracted parts of the decorated use case content.
     * 
     * @param request The original (incoming) http servlet request
     * @param response The original (incoming and unwrapped) http servlet response
     * @param pageParts Map of Strings that are extracted parts of the use case content.
     * @param model Map of Objects that are extracted from the use case model.
     * @throws java.lang.Exception Any exception thrown during output generation
     */
    void render(HttpServletRequest request, HttpServletResponse response, Map<String, String> pageParts, Map<String, ? extends Object> model) throws Exception;
}
