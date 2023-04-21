package net.taylor.mail;

import java.io.IOException;
import java.net.URL;
import net.taylor.jsf.ClasspathResourceResolver;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.ResourceLoader;
import org.jboss.seam.ui.facelet.FaceletCompiler;
import org.jboss.seam.ui.facelet.RendererRequest;
import com.sun.facelets.Facelet;
import com.sun.facelets.impl.DefaultFaceletFactory;

@Scope(ScopeType.STATELESS)
@BypassInterceptors
@Name("org.jboss.seam.faces.renderer")
@AutoCreate
@Install(value = true, precedence = Install.APPLICATION, classDependencies = "com.sun.facelets.Facelet")
public class FaceletsRenderer extends org.jboss.seam.ui.facelet.FaceletsRenderer {

    /**
	 * Render the viewId, anything written to the JSF ResponseWriter is returned
	 */
    @Override
    public String render(final String viewId) {
        RendererRequest rendererRequest = new RendererRequest(viewId) {

            protected Facelet faceletForViewId(String viewId) throws IOException {
                URL url = ResourceLoader.instance().getResource(viewId);
                if (url == null) {
                    throw new IllegalArgumentException("resource doesn't exist: " + viewId);
                }
                return new DefaultFaceletFactory(FaceletCompiler.instance(), new ClasspathResourceResolver()).getFacelet(url);
            }
        };
        try {
            rendererRequest.run();
        } catch (IOException e) {
            throw new RuntimeException("error rendering " + viewId, e);
        }
        return rendererRequest.getOutput();
    }
}
