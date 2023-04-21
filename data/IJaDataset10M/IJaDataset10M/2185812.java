package de.laures.cewolf;

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;

/**
 * @author  Guido Laures
 */
public class ImageBaseFactory {

    private static final String IMAGE_BASE_KEY = ImageBase.class.getName();

    public static final ImageBase getImageBase(ServletContext ctx) {
        ImageBase res = (ImageBase) ctx.getAttribute(IMAGE_BASE_KEY);
        if (res == null) {
            res = new ImageBaseImpl();
            ctx.setAttribute(IMAGE_BASE_KEY, res);
        }
        return res;
    }

    public static final ImageBase getImageBase(PageContext ctx) {
        return getImageBase(ctx.getServletContext());
    }
}
