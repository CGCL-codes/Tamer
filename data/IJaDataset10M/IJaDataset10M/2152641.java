package org.guiceyfruit.ejb;

import org.guiceyfruit.jsr250.Jsr250Module;
import org.guiceyfruit.ejb.support.EJBMemberProvider;
import javax.ejb.EJB;

/**
 * Allows objects to be injected using the {@link EJB} annotation
 *
 * @version $Revision: 1.1 $
 */
public class EjbModule extends Jsr250Module {

    @Override
    protected void configure() {
        super.configure();
        bindAnnotationInjector(EJB.class, EJBMemberProvider.class);
    }
}
