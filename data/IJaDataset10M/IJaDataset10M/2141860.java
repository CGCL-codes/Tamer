package org.snipsnap.graph.servlet;

import org.radeox.util.Service;
import org.snipsnap.graph.builder.StringTreeBuilder;
import org.snipsnap.graph.builder.TreeBuilder;
import org.snipsnap.graph.context.UrlContext;
import org.snipsnap.graph.renderer.ContentRenderer;
import org.snipsnap.graph.renderer.HtmlMapRenderer;
import org.snipsnap.graph.renderer.Renderer;
import snipsnap.api.app.Application;
import snipsnap.api.plugin.ServletPlugin;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Render special content added to a temporary store. This is used for the
 * graph macro. The main use is to add content to the page that is retrieved
 * by img tags or similar.
 *
 * @author Matthias L. Jugel
 * @version $Id: RenderServlet.java 1843 2006-02-01 19:37:10Z leo $
 */
public class RenderServlet implements ServletPlugin {

    private static final String RENDER_ID = "__render_id";

    private static Map contentMap = Collections.synchronizedMap(new HashMap());

    private static Map handlers = new HashMap();

    private static final Map CTOR = new HashMap();

    private static final String PLUGIN_PATH = "render";

    /**
   * Initialize the render servlet by loading the content handlers.
   */
    static {
        Iterator contentRenderer = Service.providers(ContentRenderer.class);
        while (contentRenderer.hasNext()) {
            ContentRenderer renderer = (ContentRenderer) contentRenderer.next();
            handlers.put(renderer.getName(), renderer);
        }
    }

    /**
   * Add content to the temporary store and return an id that can be used to select
   * the content later. The graph macro uses this to store the graph description
   * here which is then handed over to the rendering handler to translate to an image.
   * The id will persist until content for the same name is added.
   * <p/>
   * Example:
   * &lt;img src="/plugin/org.snipsnap.graph.servlet.Renderservlet?id=XXXX&handler=YYYY"/&gt;
   *
   * @param content the textual content to be rendered
   * @return an it to add to the url for retrieving the rendered content
   */
    public static String addContent(String name, String content) {
        Application app = Application.get();
        String baseId = RENDER_ID + name;
        String renderId = null;
        synchronized (contentMap) {
            String key = null;
            int add = 0;
            do {
                key = String.valueOf(baseId + add++);
            } while (app.getObject(key) != null);
            app.storeObject(key, "");
            renderId = Integer.toHexString(key.hashCode());
            contentMap.put(renderId, content);
        }
        return renderId;
    }

    public static String getImageMap(String renderId, String handler) {
        HtmlMapRenderer mapRenderer = new HtmlMapRenderer();
        TreeBuilder builder = new StringTreeBuilder((String) contentMap.get(renderId));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Renderer renderer = ((ContentRenderer) handlers.get(handler)).getRenderer();
        if (null != renderer) {
            UrlContext context = new UrlContext(renderId, renderer);
            mapRenderer.render(builder.build(), out, context);
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return out.toString(Application.get().getConfiguration().getEncoding());
            } catch (UnsupportedEncodingException e) {
                return out.toString();
            }
        } else {
            return "<!-- image map not possible, missing renderer for handler: " + handler + " -->";
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String handler = request.getParameter("handler");
        String id = request.getParameter("id");
        String content = (String) contentMap.get(id);
        ContentRenderer renderer = (ContentRenderer) handlers.get(handler);
        if (null == renderer) {
            response.sendError(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        renderer.render(request, response, content);
    }

    public String getPath() {
        return PLUGIN_PATH;
    }
}
