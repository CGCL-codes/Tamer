package de.definitives.creoleconverter.document;

import de.definitives.creoleconverter.renderer.Renderer;

/**
 * A Component represents an element of a document (or the document itself).
 *
 * @author Andre Steinert, Christian Helmbold
 */
public interface Component {

    /**
     * Renders this Component.
     *
     * <p>Implement this method as follows:</p>
     * <pre>
       public void renderWith(Renderer renderer) {
        renderer.render(this);
       }
       </pre>
     * <p>This interface must be implemented in the class that sould be
     * renedered (and no super class thereof), since the renderer distinguisches
     * the Components by their types.
     *
     * <p>@see <a href="http://en.wikipedia.org/wiki/Visitor_pattern"
       >Visitor Pattern</a></p>
     *
     * @param renderer
     */
    abstract void renderWith(Renderer renderer);
}
