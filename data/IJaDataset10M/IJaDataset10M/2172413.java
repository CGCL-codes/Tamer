package gemini.castor.ui.client.page.content.home.apply;

import com.google.gwt.event.shared.GwtEvent;

public class ApplyEvent extends GwtEvent<ApplyHandler> {

    private static Type<ApplyHandler> TYPE;

    public static Type<ApplyHandler> getType() {
        if (TYPE == null) TYPE = new Type<ApplyHandler>();
        return TYPE;
    }

    public enum ForwardType {

        PERSONAL_SUBMIT, DISTRIBUTOR_SUBMIT, AGREEMENT_SUBMIT, CHECKOUT_SUBMIT, CANCEL, BACK
    }

    public ApplyEvent(ForwardType forwardType) {
        super();
        this.setForwardType(forwardType);
    }

    private ForwardType forwardType;

    @Override
    protected void dispatch(ApplyHandler handler) {
        if (forwardType.equals(ForwardType.DISTRIBUTOR_SUBMIT)) {
            handler.onDistributorSubmit(this);
        } else if (forwardType.equals(ForwardType.PERSONAL_SUBMIT)) {
            handler.onPersonalSubmit(this);
        } else if (forwardType.equals(ForwardType.AGREEMENT_SUBMIT)) {
            handler.onAgreementSubmit(this);
        } else if (forwardType.equals(ForwardType.CHECKOUT_SUBMIT)) {
            handler.onCheckOutSubmit(this);
        } else if (forwardType.equals(ForwardType.CANCEL)) {
            handler.onCancel(this);
        } else if (forwardType.equals(ForwardType.BACK)) {
            handler.onBack(this);
        }
    }

    @Override
    public Type<ApplyHandler> getAssociatedType() {
        return getType();
    }

    public void setForwardType(ForwardType forwardType) {
        this.forwardType = forwardType;
    }

    public ForwardType getForwardType() {
        return forwardType;
    }
}
