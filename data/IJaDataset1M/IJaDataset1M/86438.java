package org.nexopenframework.ide.eclipse.wsdl;

import org.codehaus.xfire.gen.GenerationContext;
import org.codehaus.xfire.jaxws.gen.InterfaceGenerator;
import org.codehaus.xfire.service.Binding;
import org.codehaus.xfire.service.Service;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.internet.internal.proxy.InternetPlugin;
import org.nexopenframework.ide.eclipse.commons.log.Logger;
import org.nexopenframework.ide.eclipse.commons.util.ThreadContext;
import org.nexopenframework.jaxws.annotations.Credentials;
import org.nexopenframework.jaxws.annotations.ProxySettings;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;

/**
 * <p>NexOpen Framework/p>
 * 
 * <p></p>
 * 
 * @see InterfaceGenerator
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class NexOpenInterfaceGenerator extends InterfaceGenerator {

    /**
	 * <p></p>
	 * 
	 * @see org.codehaus.xfire.gen.jsr181.ServiceInterfaceGenerator#annotate(org.codehaus.xfire.gen.GenerationContext, org.codehaus.xfire.service.Service, com.sun.codemodel.JDefinedClass, org.codehaus.xfire.service.Binding)
	 */
    @Override
    protected void annotate(final GenerationContext context, final Service service, final JDefinedClass jc, final Binding binding) {
        final String username = (String) ThreadContext.Context.getAttribute("credentials.username");
        String password = (String) ThreadContext.Context.getAttribute("credentials.password");
        if (username != null && username.trim().length() > 0) {
            final JAnnotationUse ann = jc.annotate(Credentials.class);
            if (password == null) {
                password = "";
            }
            ann.param("username", username);
            ann.param("password", password);
        }
        IPreferenceStore store = getPreferenceStore();
        boolean checked = store.getBoolean(InternetPlugin.PREFERENCE_PROXYCHECKED);
        if (checked) {
            final JAnnotationUse ann = jc.annotate(ProxySettings.class);
            ann.param("host", store.getString(InternetPlugin.PREFERENCE_HOSTNAME));
            ann.param("port", store.getString(InternetPlugin.PREFERENCE_PORT));
            boolean authentication = store.getBoolean(InternetPlugin.PREFERENCE_NAMECHECKED);
            if (authentication) {
                ann.param("username", store.getString(InternetPlugin.PREFERENCE_USERNAME));
                ann.param("password", store.getString(InternetPlugin.PREFERENCE_PASSWORD));
            }
        }
        try {
            super.annotate(context, service, jc, binding);
        } catch (RuntimeException e) {
            Logger.getLog().error("Unexpected RuntimeException in " + this + " calling parent annotate method", e);
            throw e;
        }
    }

    public IPreferenceStore getPreferenceStore() {
        return InternetPlugin.getInstance().getPreferenceStore();
    }
}
