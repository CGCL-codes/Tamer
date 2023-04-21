package com.ideo.sweetdevria.taglib;

public abstract class BaseStateTagSupport extends BaseTagSupport {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2831834796028869589L;

    protected boolean stateful;

    public void release() {
        super.release();
        stateful = true;
    }

    public boolean isStateful() {
        return stateful;
    }

    /**
	 * Define the stateful ability of the component
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="boolean"
     * description="Define whether this attribute must keep its state according to the page configuration 
     * (default behavior) or be reevaluated every time the tag is encountered. Setting this attribute to true
     * let the page setting being processed, whereas setting it to false force a stateless behavior."
     */
    public void setStateful(boolean stateful) {
        this.stateful = stateful;
    }

    /**
	 * @see com.ideo.sweetdevria.taglib.AbstractComponentTagSupport#setId(final String id)
	 * @jsp.attribute required="true"
	 * rtexprvalue="true"
	 * type="java.lang.String"
	 * description="Unique Identifier. Accepts EL."
	 */
    public void setId(final String id) {
        super.setId(id);
        refractorContext();
    }
}
