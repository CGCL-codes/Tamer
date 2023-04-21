package org.interlogy.component;

import org.ajax4jsf.component.AjaxComponent;
import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.event.AjaxEvent;
import org.ajax4jsf.application.ComponentsLoader;
import org.interlogy.raiting.Raiting;
import org.interlogy.raiting.Raitingable;
import org.interlogy.raiting.RaitingSystem;
import org.jboss.seam.Component;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.component.EditableValueHolder;
import javax.faces.event.FacesEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;

/**
 * JSF component class
 *
 */
public abstract class UIVote extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.interlogy.Vote";

    public static final String COMPONENT_FAMILY = "org.interlogy.Vote";

    public abstract void setValue(Object value);

    public abstract Object getValue();

    public void vote(int value) {
        RaitingSystem raitingSystem = (RaitingSystem) Component.getInstance("raitingSystem");
        raitingSystem.rateObject(getRaiting().getObject(), convert(value));
    }

    private int convert(double value) {
        return (int) Math.round(((11.0 * value) / 100.0) - 5.5);
    }

    public void broadcast(FacesEvent facesEvent) throws AbortProcessingException {
        if (facesEvent instanceof AjaxEvent) {
            FacesContext context = getFacesContext();
            AjaxContext.getCurrentInstance(context).addComponentToAjaxRender(this);
            AjaxContext.getCurrentInstance(context).addRegionsFromComponent(this);
        } else {
            super.broadcast(facesEvent);
        }
    }

    public Raiting getRaiting() {
        Object obj = getValue();
        if (obj instanceof Raiting) {
            return (Raiting) obj;
        } else {
            if (obj instanceof Raitingable) {
                Raiting ret = ((Raitingable) obj).getRaiting();
                if (ret == null) {
                    RaitingSystem raitingSystem = (RaitingSystem) Component.getInstance("raitingSystem");
                    ret = raitingSystem.fixObject((Raitingable) obj);
                }
                return ret;
            } else {
                throw new FacesException("Object '" + obj + "' is not raiting or raitingable");
            }
        }
    }
}
