package com.googlecode.pinthura.factory;

import com.googlecode.pinthura.data.ShapeFactory;
import com.googlecode.pinthura.data.Square;
import com.googlecode.pinthura.data.UrlBoundary;
import com.googlecode.pinthura.data.UrlBoundaryFactory;
import com.googlecode.pinthura.factory.locator.MethodParamBuilder;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import java.lang.reflect.Method;

public final class AMethodParamUnderTest {

    private static final String FACTORY_METHOD_1 = "createUrlBoundary";

    private static final Class<UrlBoundaryFactory> FACTORY_CLASS_1 = UrlBoundaryFactory.class;

    private static final String FACTORY_METHOD_2 = "createSquare";

    private static final Class<ShapeFactory> FACTORY_CLASS_2 = ShapeFactory.class;

    private static final int LENGTH = 200;

    private static final String URL = "1234";

    @SuppressWarnings({ "unchecked" })
    @Test
    public void shouldReturnTheSuppliedSetOfValues() {
        MethodParam param = createMethodParam(FACTORY_CLASS_1, FACTORY_METHOD_1, String.class, URL);
        assertThat(param.getMethod(), equalTo(getMethod(FACTORY_CLASS_1, FACTORY_METHOD_1, String.class)));
        assertThat((Class<UrlBoundary>) param.getReturnType(), equalTo(UrlBoundary.class));
        Object[] arguments = param.getArguments();
        assertThat(arguments.length, equalTo(1));
        assertThat((String) arguments[0], equalTo(URL));
    }

    @SuppressWarnings({ "unchecked" })
    @Test
    public void shouldReturnAnotherSuppliedSetOfValues() {
        MethodParam param = createMethodParam(FACTORY_CLASS_2, FACTORY_METHOD_2, int.class, LENGTH);
        Method expectedMethod = getMethod(FACTORY_CLASS_2, FACTORY_METHOD_2, int.class);
        assertThat(param.getMethod(), equalTo(expectedMethod));
        assertThat((Class<Square>) param.getReturnType(), equalTo(Square.class));
        Object[] arguments = param.getArguments();
        assertThat(arguments.length, equalTo(1));
        assertThat((Integer) arguments[0], equalTo(LENGTH));
    }

    private Method getMethod(final Class<?> factoryClass, final String factoryMethod, final Class<?> paramType) {
        try {
            return factoryClass.getMethod(factoryMethod, paramType);
        } catch (NoSuchMethodException e) {
            throw new AssertionError();
        }
    }

    @SuppressWarnings({ "unchecked" })
    private MethodParam createMethodParam(final Class<?> factoryClass, final String factoryMethod, final Class<?> paramType, final Object arg) {
        return new MethodParamBuilder().forInterface(factoryClass).havingMethod(factoryMethod, paramType).withArgument(arg).build();
    }
}
