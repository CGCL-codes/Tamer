package br.com.sysmap.crux.widgets.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Thiago da Rosa de Bustamante -
 *
 */
public class FinishEvent extends GwtEvent<FinishHandler> {

    private static Type<FinishHandler> TYPE = new Type<FinishHandler>();

    private boolean canceled;

    /**
	 * 
	 */
    protected FinishEvent() {
        super();
    }

    /**
	 * @return
	 */
    public static Type<FinishHandler> getType() {
        return TYPE;
    }

    /**
	 * @param <I>
	 * @param source
	 * @return
	 */
    public static FinishEvent fire(HasFinishHandlers source) {
        FinishEvent event = new FinishEvent();
        source.fireEvent(event);
        return event;
    }

    @Override
    protected void dispatch(FinishHandler handler) {
        handler.onFinish(this);
    }

    @Override
    public Type<FinishHandler> getAssociatedType() {
        return TYPE;
    }

    /**
	 * @return the canceled
	 */
    public boolean isCanceled() {
        return canceled;
    }

    /**
	 * 
	 */
    public void cancel() {
        canceled = true;
    }
}
