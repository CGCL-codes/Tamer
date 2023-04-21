package cx.ath.mancel01.dependencyshot.test.typesafe;

import cx.ath.mancel01.dependencyshot.graph.Binder;
import javax.inject.Provider;

/**
 *
 * @author Mathieu ANCELIN
 */
public class TypesafeModule extends Binder {

    @Override
    public void configureBindings() {
        bind(Service.class).to(ServiceImpl.class);
        bind(Service.class).named("service").providedBy(new Provider<Service>() {

            @Override
            public Service get() {
                return new ServiceImpl();
            }
        });
        bind(Service.class).named("service2").providedBy(new Provider<ServiceImpl>() {

            @Override
            public ServiceImpl get() {
                return new ServiceImpl();
            }
        });
    }
}
