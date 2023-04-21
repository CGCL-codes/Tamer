package struts2gui.model;

/**
 * Class InterceptorsItem.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class InterceptorsItem implements java.io.Serializable {

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * Field _interceptor.
     */
    private struts2gui.model.Interceptor _interceptor;

    /**
     * Field _interceptorStack.
     */
    private struts2gui.model.InterceptorStack _interceptorStack;

    public InterceptorsItem() {
        super();
    }

    /**
     * Returns the value of field 'choiceValue'. The field
     * 'choiceValue' has the following description: Internal choice
     * value storage
     * 
     * @return the value of field 'ChoiceValue'.
     */
    public java.lang.Object getChoiceValue() {
        return this._choiceValue;
    }

    /**
     * Returns the value of field 'interceptor'.
     * 
     * @return the value of field 'Interceptor'.
     */
    public struts2gui.model.Interceptor getInterceptor() {
        return this._interceptor;
    }

    /**
     * Returns the value of field 'interceptorStack'.
     * 
     * @return the value of field 'InterceptorStack'.
     */
    public struts2gui.model.InterceptorStack getInterceptorStack() {
        return this._interceptorStack;
    }

    /**
     * Sets the value of field 'interceptor'.
     * 
     * @param interceptor the value of field 'interceptor'.
     */
    public void setInterceptor(final struts2gui.model.Interceptor interceptor) {
        this._interceptor = interceptor;
        this._choiceValue = interceptor;
    }

    /**
     * Sets the value of field 'interceptorStack'.
     * 
     * @param interceptorStack the value of field 'interceptorStack'
     */
    public void setInterceptorStack(final struts2gui.model.InterceptorStack interceptorStack) {
        this._interceptorStack = interceptorStack;
        this._choiceValue = interceptorStack;
    }

    @Override
    public String toString() {
        if (getChoiceValue() == getInterceptor()) {
            return getInterceptor().getName().toString();
        } else if (getChoiceValue() == getInterceptorStack()) {
            return getInterceptorStack().getName().toString();
        }
        return "Interceptor Item";
    }
}
