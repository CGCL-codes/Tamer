package br.com.cefetrn.apoena.view.decorators;

import javax.servlet.jsp.PageContext;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

public abstract class AbstractColumn implements DisplaytagColumnDecorator {

    public abstract String decorate(Object obj) throws DecoratorException;

    public Object decorate(Object obj, PageContext arg1, MediaTypeEnum arg2) throws DecoratorException {
        return decorate(obj);
    }
}
