package de.javatt.data.scenario.swt.verification;

import org.eclipse.swt.widgets.Composite;
import de.javatt.data.scenario.swt.SWTAccessor;
import de.javatt.data.scenario.swt.ValueGetter;

/**
 * WidgetProperty is an ObjectProperty that gives
 * access to swt widget instances.
 * 
 * @author Matthias Kempa
 *
 */
public class CompositeProperty extends ControlProperty {

    private Composite myComposite;

    /**
     * 
     */
    public CompositeProperty() {
        super();
    }

    /**
     * Sets the component.
     */
    public void setObject(Object obj) {
        if (obj instanceof Composite) {
            myComposite = (Composite) obj;
        }
        super.setObject(obj);
    }

    public Integer getBackgroundMode() {
        return (myComposite != null ? (Integer) SWTAccessor.getInstance().syncExec(myComposite, new ValueGetter() {

            public Object getValue() {
                return myComposite.getBackgroundMode();
            }
        }) : 0);
    }

    public Boolean getLayoutDeferred() {
        return (myComposite != null ? (Boolean) SWTAccessor.getInstance().syncExec(myComposite, new ValueGetter() {

            public Object getValue() {
                return myComposite.getLayoutDeferred();
            }
        }) : false);
    }
}
