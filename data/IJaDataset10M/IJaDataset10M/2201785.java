package org.mobicents.slee.sippresence.server.subscription;

import java.util.HashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ServerTransaction;
import javax.sip.header.HeaderFactory;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.facilities.Tracer;
import net.java.slee.resource.sip.SleeSipProvider;
import org.mobicents.slee.ChildRelationExt;
import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.sipevent.server.publication.PublicationControlSbbLocalObject;
import org.mobicents.slee.sipevent.server.subscription.ImplementedSubscriptionControlParentSbbLocalObject;
import org.mobicents.slee.sipevent.server.subscription.NotifyContent;
import org.mobicents.slee.sipevent.server.subscription.data.Notifier;
import org.mobicents.slee.sipevent.server.subscription.data.Subscription;
import org.mobicents.slee.sipevent.server.subscription.data.SubscriptionKey;
import org.mobicents.slee.sippresence.server.jmx.SipPresenceServerManagement;
import org.mobicents.slee.sippresence.server.presrulescache.PresRulesActivityContextInterfaceFactory;
import org.mobicents.slee.sippresence.server.presrulescache.PresRulesSbbInterface;
import org.mobicents.slee.sippresence.server.presrulescache.RulesetUpdatedEvent;

/**
 * Implemented Subscription control child sbb for a SIP Presence Server.
 * 
 * @author eduardomartins
 * 
 */
public abstract class PresenceSubscriptionControlSbb implements Sbb, PresenceSubscriptionControlSbbInterface {

    private static Tracer tracer;

    private static final SipPresenceServerManagement configuration = SipPresenceServerManagement.getInstance();

    private static final PresenceSubscriptionControl presenceSubscriptionControl = new PresenceSubscriptionControl();

    protected SleeSipProvider sipProvider;

    protected HeaderFactory headerFactory;

    protected PresRulesSbbInterface presRulesSbbInterface;

    protected PresRulesActivityContextInterfaceFactory presRulesACIF;

    /**
	 * SbbObject's sbb context
	 */
    private SbbContextExt sbbContext;

    public void setSbbContext(SbbContext sbbContext) {
        this.sbbContext = (SbbContextExt) sbbContext;
        if (tracer == null) {
            tracer = sbbContext.getTracer(getClass().getSimpleName());
        }
        try {
            Context context = (Context) new InitialContext().lookup("java:comp/env");
            sipProvider = (SleeSipProvider) context.lookup("slee/resources/jainsip/1.2/provider");
            headerFactory = sipProvider.getHeaderFactory();
            presRulesSbbInterface = (PresRulesSbbInterface) context.lookup("slee/resources/presence/presrulescache/1.0/sbbinterface");
            presRulesACIF = (PresRulesActivityContextInterfaceFactory) context.lookup("slee/resources/presence/presrulescache/1.0/acif");
        } catch (NamingException e) {
            tracer.severe("Can't set sbb context.", e);
        }
    }

    public boolean acceptsEventList() {
        return true;
    }

    ;

    @Override
    public ImplementedSubscriptionControlParentSbbLocalObject getParentSbb() {
        return (ImplementedSubscriptionControlParentSbbLocalObject) sbbContext.getSbbLocalObject().getParent();
    }

    public String[] getEventPackages() {
        return PresenceSubscriptionControl.getEventPackages();
    }

    public void isSubscriberAuthorized(String subscriber, String subscriberDisplayName, Notifier notifier, SubscriptionKey key, int expires, String content, String contentType, String contentSubtype, boolean eventList, ServerTransaction serverTransaction) {
        presenceSubscriptionControl.isSubscriberAuthorized(subscriber, subscriberDisplayName, notifier, key, expires, content, contentType, contentSubtype, eventList, configuration.getPresRulesAUID(), configuration.getPresRulesDocumentName(), serverTransaction, this);
    }

    public void removingSubscription(Subscription subscription) {
        presenceSubscriptionControl.removingSubscription(subscription, configuration.getPresRulesAUID(), configuration.getPresRulesDocumentName(), this);
    }

    public NotifyContent getNotifyContent(Subscription subscription) {
        return presenceSubscriptionControl.getNotifyContent(subscription, this);
    }

    @Override
    public Object filterContentPerSubscriber(Subscription subscription, Object unmarshalledContent) {
        return presenceSubscriptionControl.filterContentPerSubscriber(subscription, unmarshalledContent, this);
    }

    public void onRulesetUpdatedEvent(RulesetUpdatedEvent event, ActivityContextInterface aci) {
        presenceSubscriptionControl.rulesetUpdated(event.getDocumentSelector(), event.getRuleset(), this);
    }

    public PresRulesActivityContextInterfaceFactory getPresRulesACIF() {
        return presRulesACIF;
    }

    public PresRulesSbbInterface getPresRulesSbbInterface() {
        return presRulesSbbInterface;
    }

    @Override
    public SbbLocalObject getSbbLocalObject() {
        return sbbContext.getSbbLocalObject();
    }

    public abstract ChildRelationExt getPublicationControlChildRelation();

    public PublicationControlSbbLocalObject getPublicationChildSbb() {
        ChildRelationExt childRelationExt = getPublicationControlChildRelation();
        PublicationControlSbbLocalObject childSbb = (PublicationControlSbbLocalObject) childRelationExt.get(ChildRelationExt.DEFAULT_CHILD_NAME);
        if (childSbb == null) {
            try {
                childSbb = (PublicationControlSbbLocalObject) childRelationExt.create(ChildRelationExt.DEFAULT_CHILD_NAME);
            } catch (Exception e) {
                tracer.severe("Failed to create child sbb", e);
                return null;
            }
        }
        return childSbb;
    }

    @SuppressWarnings("rawtypes")
    public abstract void setCombinedRules(HashMap rules);

    @SuppressWarnings("rawtypes")
    public abstract HashMap getCombinedRules();

    public HeaderFactory getHeaderFactory() {
        return headerFactory;
    }

    /**
	 * interface used by rules processor to get sphere for a notifier
	 */
    public String getSphere(String notifier) {
        return presenceSubscriptionControl.getSphere(notifier, this);
    }

    public void sbbActivate() {
    }

    public void sbbCreate() throws CreateException {
    }

    public void sbbExceptionThrown(Exception arg0, Object arg1, ActivityContextInterface arg2) {
    }

    public void sbbLoad() {
    }

    public void sbbPassivate() {
    }

    public void sbbPostCreate() throws CreateException {
    }

    public void sbbRemove() {
    }

    public void sbbRolledBack(RolledBackContext arg0) {
    }

    public void sbbStore() {
    }

    public void unsetSbbContext() {
        this.sbbContext = null;
    }
}
