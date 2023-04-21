package net.sf.eclipse.portlet.core.internal.model.common.dom;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.sf.eclipse.portlet.core.exceptions.ModelCreationException;
import net.sf.eclipse.portlet.core.internal.model.common.ObjectFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * ObjectFactory to initialize Element based upon a {@link Element}
 * 
 * @author fwjwiegerinck
 * @since 0.2
 */
public class DomModelElementFactory<T extends AbstractDomModelElement> implements ObjectFactory<Element, T> {

    /**
	 * Store baseClass
	 */
    private Class<? extends T> baseClass;

    /**
	 * Store document
	 */
    private Document document;

    /**
	 * Initialize factory
	 * @param document Document which will contain the element
	 * @param baseClass BaseClass which will be constructed
	 */
    public DomModelElementFactory(Document document, Class<? extends T> baseClass) {
        super();
        if (document == null) throw new IllegalArgumentException("Parameter \"document\" cannot be NULL");
        if (baseClass == null) throw new IllegalArgumentException("Parameter \"baseClass\" cannot be NULL");
        this.document = document;
        this.baseClass = baseClass;
    }

    /**
	 * Create a new DOM Model Element based upon the supplied element
	 * @param element Create DOM element
	 * @return Created DOM Model element
	 */
    public T create(Element element) {
        if (element == null) throw new IllegalArgumentException("Parameter \"element\" cannot be NULL");
        try {
            Constructor<? extends T> constructor = this.baseClass.getConstructor(Document.class, Element.class);
            return constructor.newInstance(this.document, element);
        } catch (SecurityException e) {
            throw new ModelCreationException(DomModelElementList.class, "Unable to create model element based upon DOM Node due to security exception", e);
        } catch (NoSuchMethodException e) {
            throw new ModelCreationException(DomModelElementList.class, "Unable to create model element based upon DOM Node due missing constructor. Expect constructor(Document, Element)", e);
        } catch (IllegalArgumentException e) {
            throw new ModelCreationException(DomModelElementList.class, "Unable to create model element based upon DOM Node due to illegal argument exception", e);
        } catch (InstantiationException e) {
            throw new ModelCreationException(DomModelElementList.class, "Unable to create model element based upon DOM Node due to instantion exception", e);
        } catch (IllegalAccessException e) {
            throw new ModelCreationException(DomModelElementList.class, "Unable to create model element based upon DOM Node due to illegal access exception", e);
        } catch (InvocationTargetException e) {
            throw new ModelCreationException(DomModelElementList.class, "Unable to create model element based upon DOM Node due to invocation target exception", e);
        }
    }

    /**
	 * @see net.sf.eclipse.portlet.core.internal.model.common.ObjectFactory#getSource(java.lang.Object)
	 */
    public Element getSource(T createdObject) {
        return createdObject.getDOMElement();
    }
}
