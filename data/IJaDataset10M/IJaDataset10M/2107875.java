package br.com.simtecnologia.access.controll.handlers;

import java.lang.annotation.Annotation;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ResourceHandler;
import br.com.simtecnologia.access.controll.annotation.ControlledResource;

/**
 * Recupera e define como @{@link Resource} os recursos anotados com @{@link ControlledResource}.
 * que são definidos com controle de acesso.
 * @author Tomaz Lavieri
 */
@Component
@ApplicationScoped
public class ControlledResourceHandler extends ResourceHandler {

    private final AccessRouterResolverHandler handler;

    public ControlledResourceHandler(Router router, AccessRouterResolverHandler handler) {
        super(router);
        this.handler = handler;
    }

    @Override
    public Class<? extends Annotation> stereotype() {
        return ControlledResource.class;
    }

    @Override
    public void handle(Class<?> annotatedType) {
        super.handle(annotatedType);
        handler.handle(annotatedType);
    }
}
