package genj.tree;

import java.awt.*;
import genj.util.*;
import genj.gedcom.*;

/**
 * A proxy knows how to draw a property in a tree's entity : BASE
 */
abstract class Proxy {

    /** the path to the property */
    private TagPath path;

    /** the box that the property fits in */
    private Rectangle box;

    /**
   * Generator
   */
    static Proxy generate(TagPath path, Rectangle box) {
        Proxy proxy;
        try {
            proxy = (Proxy) Class.forName("genj.tree.Proxy" + Property.calcDefaultProxy(path)).newInstance();
        } catch (Exception e) {
            Debug.log(Debug.WARNING, Proxy.class, "Property " + path.getLast() + " returned unknown proxy");
            proxy = new ProxyUnknown();
        }
        proxy.init(path, box);
        return proxy;
    }

    /**
   * Gets the logical box of this proxy
   */
    Rectangle getBox() {
        return new Rectangle(box.x, box.y, box.width, box.height);
    }

    /**
   * Returns the path of this proxy
   */
    TagPath getPath() {
        path.setToFirst();
        return path;
    }

    /**
   * Initializing
   */
    private void init(TagPath path, Rectangle box) {
        this.path = path;
        this.box = box;
    }

    /**
   * Renders a Property in a tree
   */
    final void render(Property p, int dit, int pig, int w, int h, TreeGraphics g) {
        int offset = 0;
        if (g.isPropertyImages) {
            Property top = p;
            while (!((top instanceof Entity) || (top.getParent() instanceof Entity))) {
                top = top.getParent();
            }
            ImgIcon img = top.getImage(false);
            g.drawImage(img, dit, pig, new Point(box.x - w / 2, box.y - h / 2));
            offset = img.getIconWidth() + 2;
        }
        renderSpecific(p, dit, pig, new Rectangle(box.x - w / 2 + offset, box.y - h / 2, box.width - offset, box.height), g);
    }

    /**
   * Render property specific information of property
   */
    abstract void renderSpecific(Property p, int dit, int pig, Rectangle box, TreeGraphics g);
}
