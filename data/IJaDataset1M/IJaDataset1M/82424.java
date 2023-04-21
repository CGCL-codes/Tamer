package csstoswing_engine;

import css_parser.PropertyCSS;

public class CSSDecoratorFactory {

    private static CSSDecoratorFactory cSSDecoratorFactory;

    private CSSDecoratorFactory() {
    }

    public static CSSDecoratorFactory getInstance() {
        if (CSSDecoratorFactory.cSSDecoratorFactory == null) {
            CSSDecoratorFactory.cSSDecoratorFactory = new CSSDecoratorFactory();
        }
        return CSSDecoratorFactory.cSSDecoratorFactory;
    }

    public CSSDecorator getCSSDecoratorForPropery(PropertyCSS propertyCSS) {
        String name = propertyCSS.getClass().getSimpleName();
        Class theClass = null;
        Object object = null;
        try {
            theClass = Class.forName("css_decorators" + "." + name + "Decorator");
            object = theClass.newInstance();
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return (CSSDecorator) object;
    }
}
