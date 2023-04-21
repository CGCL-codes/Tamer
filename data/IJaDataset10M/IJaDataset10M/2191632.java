package com.gargoylesoftware.htmlunit.javascript;

import java.util.List;
import org.mozilla.javascript.Scriptable;
import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * An array returned by frames property of Window
 *
 * @author <a href="mailto:chen_jun@users.sourceforge.net>Chen Jun</a>
 * @author Marc Guillemot
 * @version $Revision: 582 $
 */
public class WindowFramesArray extends SimpleScriptable {

    private static final long serialVersionUID = 3108681888070448618L;

    private HtmlPage htmlPage_;

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public WindowFramesArray() {
    }

    /**
     * <p>Return the object at the specified index.</p>
     *
     * <p>TODO: This implementation is particularly inefficient but without a way
     * to detect if an element has been inserted or removed, it isn't safe to
     * cache the array/<p>
     *
     * @param index The index
     * @param start The object that get is being called on.
     * @return The object or NOT_FOUND
     */
    public Object get(final int index, final Scriptable start) {
        final HtmlPage htmlPage = ((WindowFramesArray) start).htmlPage_;
        if (htmlPage == null) {
            return super.get(index, start);
        }
        final List frames = htmlPage.getFrames();
        if (frames == null) {
            return NOT_FOUND;
        }
        final int frameCount = frames.size();
        if (index < 0 || index >= frameCount) {
            return NOT_FOUND;
        }
        return ((WebWindow) frames.get(index)).getScriptObject();
    }

    /**
     * Return the frame at the specified name or NOT_FOUND.
     *
     * @param name The name.
     * @param start The object that get is being called on.
     * @return The object or NOT_FOUND
     */
    public Object get(final String name, final Scriptable start) {
        final HtmlPage htmlPage = ((WindowFramesArray) start).htmlPage_;
        if (htmlPage == null) {
            return super.get(name, start);
        }
        final List frames = htmlPage.getFrames();
        if (frames == null) {
            return NOT_FOUND;
        }
        final int frameCount = frames.size();
        for (int i = 0; i < frameCount; i++) {
            if (((WebWindow) frames.get(i)).getName().equals(name)) {
                return ((WebWindow) frames.get(i)).getScriptObject();
            }
        }
        return super.get(name, start);
    }

    /**
     * Initialize this object
     * @param page The HtmlPage that this object will retrive elements from.
     */
    public void initialize(final HtmlPage page) {
        Assert.notNull("page", page);
        htmlPage_ = page;
    }

    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public final void jsConstructor() {
    }

    /**
     * <p>Return the number of elements in this array</p>
     *
     * Warning: if there is a frame named "length" (what for an idea!), 
     * a js expression like "frames.length" will currently always return the frame 
     * with the given name. That is not what all browsers do (for instance Mozilla 1.5 
     * works this way but not IE6)
     * 
     * @return The number of elements in the array
     */
    public int jsGet_length() {
        Assert.notNull("htmlpage", htmlPage_);
        return htmlPage_.getFrames().size();
    }
}
