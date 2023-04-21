package org.icefaces.application.showcase.view.bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.faces.event.ValueChangeEvent;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
import java.io.Serializable;

/**
 * <p>The BaseBean is a nice little helper class for common functionality
 * accross the component examples.  The BaseBean or the notion of a base
 * bean is handy in most application as it can provice commonality for logging,
 * init and dispose methods as well as references to Service lookup
 * mechanism. </p>
 *
 * <p>The valueChangeEffect is used by most example beans to highlight
 * changes in backing bean values that are reflected on the client side.</p>
 *
 * @since 1.7
 */
public class BaseBean implements Serializable {

    protected final Log logger = LogFactory.getLog(this.getClass());

    protected Effect valueChangeEffect;

    public BaseBean() {
        valueChangeEffect = new Highlight("#fda505");
        valueChangeEffect.setFired(true);
    }

    /**
     * Resets the valueChange effect to fire when the current response
     * is completed.
     *
     * @param event jsf action event
     */
    public void effectChangeListener(ValueChangeEvent event) {
        valueChangeEffect.setFired(false);
    }

    /**
	 * Used to initialize the managed bean.
	 */
    protected void init() {
    }

    public Effect getValueChangeEffect() {
        return valueChangeEffect;
    }

    public void setValueChangeEffect(Effect valueChangeEffect) {
        this.valueChangeEffect = valueChangeEffect;
    }
}
