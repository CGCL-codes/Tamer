package net.java.slee.resource.diameter.sh.client;

import java.io.IOException;
import net.java.slee.resource.diameter.base.DiameterActivity;
import net.java.slee.resource.diameter.sh.DiameterShAvpFactory;
import net.java.slee.resource.diameter.sh.events.PushNotificationAnswer;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;

/**
 * Activity used by a Diameter Sh client to represent a subscription to changes in user data in an HSS.
 * Push-Notification-Request messages are fired on this activity as events of type org.jainslee.resources.diameter.sh.SubscribedPushNotificationRequest and Subscribe-Notifications-Answer messages are fired as events of type org.jainslee.resources.diameter.sh.SubscribeNotificationsAnswer.
 * 
 * This activity is created by a call to ShClientProvider.createShClientSubscriptionActivity().
 *  
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ShClientSubscriptionActivity extends DiameterActivity {

    /**
   * Get a message factory to manually create Sh Client Messages.
   * @return
   */
    ShClientMessageFactory getClientMessageFactory();

    /**
   * Get avp factory.
   * @return
   */
    DiameterShAvpFactory getClientAvpFactory();

    /**
   * Send a Subscribe-Notifications-Request message. 
   * 
   * @param request request message to send
   * @throws IOException if the message could not be sent
   */
    void sendSubscribeNotificationsRequest(SubscribeNotificationsRequest request) throws IOException;

    /**
   * Send a Subscribe-Notifications-Request message containing the AVPs required to UNSUBSCRIBE from the user that this activity represents a subscription to. 
   * 
   * @throws IOException if the request message could not be sent
   */
    void sendUnsubscribeRequest() throws IOException;

    /**
   * Creates PNA for receive PNR. It returns null if there is not PNR received.
   * @return
   */
    PushNotificationAnswer createPushNotificationAnswer();

    /**
   * Send a manually-constructed PushNotificationAnswer to the peer that sent the PushNotificationRequest. 
   * 
   * @param answer the message to be sent
   * @throws IOException if the message could not be sent
   */
    void sendPushNotificationAnswer(PushNotificationAnswer answer) throws IOException;

    /**
   * Convenience method to create and send a PushNotificationAnswer containing a Result-Code or Experimental-Result AVP populated with the given value.
   * 
   * @param resultCode
   * @param isExperimentalResultCode
   * @throws IOException if the message could not be sent
   */
    void sendPushNotificationAnswer(long resultCode, boolean isExperimentalResultCode) throws IOException;

    /**
   * Creates PNA for receive PNR. It returns null if there is not PNR received.
   * @param resultCode - result code to be added
   * @param isExperimaental - true if result code is experimental result code 
   * @return
   */
    PushNotificationAnswer createPushNotificationAnswer(long resultCode, boolean isExperimaental);

    /**
   * Return the User-Identity for the subscription in the HSS represented by this activity.
   * 
   * @return the User-Identity AVP sent in the initial Subscription-Notifications-Request passed to sendSubscribeNotificationsRequest(net.java.slee.resource.diameter.sh.server.events.SubscribeNotificationsRequest).
   */
    UserIdentityAvp getSubscribedUserIdentity();
}
