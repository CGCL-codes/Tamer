package br.com.arsmachina.tapestrycrud.selectmodel.impl;

import java.util.List;
import java.util.Map;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ioc.util.StrategyRegistry;
import br.com.arsmachina.module.service.ControllerSource;
import br.com.arsmachina.tapestrycrud.selectmodel.SelectModelFactory;
import br.com.arsmachina.tapestrycrud.selectmodel.SingleTypeSelectModelFactory;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;

/**
 * Default {@link SelectModelFactory} implementation. It delegates all its methods for
 * {@link SelectModelFactory} instances.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @see SelectModel
 * @see SingleTypeSelectModelFactory
 * @todo handle enums.
 */
public class SelectModelFactoryImpl implements SelectModelFactory {

    @SuppressWarnings("unchecked")
    private final StrategyRegistry<SingleTypeSelectModelFactory> registry;

    /**
	 * Single constructor.
	 * 
	 * @param registrations a {@link Map}&lt;{@link Class}, {@link Link}{@link SingleTypeSelectModelFactory}&gt;.
	 * It cannot be null.
	 * @param controllerSource a {@link ControllerSource}. It cannot be null.
	 * @param encoderSource an {@link EncoderSource}. It cannot be null.
	 */
    @SuppressWarnings("unchecked")
    public SelectModelFactoryImpl(Map<Class, SingleTypeSelectModelFactory> registrations) {
        registry = StrategyRegistry.newInstance(SingleTypeSelectModelFactory.class, registrations);
    }

    /**
	 * @see br.com.arsmachina.tapestrycrud.selectmodel.SelectModelFactory#create(java.lang.Class)
	 */
    public SelectModel create(Class<?> clasz) {
        return create(clasz, null);
    }

    /**
	 * @see br.com.arsmachina.tapestrycrud.selectmodel.SelectModelFactory#create(java.lang.Class,
	 * java.util.List)
	 */
    public <T> SelectModel create(Class<T> clasz, List<T> objects) {
        return get(clasz).create(objects);
    }

    @SuppressWarnings("unchecked")
    private <T> SingleTypeSelectModelFactory<T> get(Class<T> clasz) {
        return registry.get(clasz);
    }
}
