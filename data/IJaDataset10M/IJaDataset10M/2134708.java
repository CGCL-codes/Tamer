package net.java.slee.resource.diameter.sh.client;

import net.java.slee.resource.diameter.sh.MessageFactory;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.events.PushNotificationAnswer;
import net.java.slee.resource.diameter.sh.events.PushNotificationRequest;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.events.UserDataRequest;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.SubsReqType;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;

/**
 * The Sh client interface to the Diameter MessageFactory used by applications to create Diameter Sh messages.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ShClientMessageFactory extends MessageFactory {

    /**
   * Create a UserDataRequest using the given parameters to populate the User-Identity and Data-Reference AVPs.
   * @return a UserDataRequest object that can be sent using {@link ShClientActivity#sendUserDataRequest(net.java.slee.resource.diameter.sh.events.types.UserDataRequest)} 
   */
    UserDataRequest createUserDataRequest(UserIdentityAvp userIdentity, DataReferenceType reference);

    /**
   * Create an empty UserDataRequest that will need to have AVPs set on it before being sent.
   * @return a UserDataRequest object that can be sent using {@link ShClientActivity#sendUserDataRequest(net.java.slee.resource.diameter.sh.events.types.UserDataRequest)} 
   */
    UserDataRequest createUserDataRequest();

    /**
   * Create a ProfileUpdateRequest using the given parameters to populate the User-Identity, Data-Reference and User-Data AVPs.
   * @return a ProfileUpdateRequest object that can be sent using {@link ShClientActivity#sendProfileUpdateRequest(net.java.slee.resource.diameter.sh.events.types.ProfileUpdateRequest)} 
   */
    ProfileUpdateRequest createProfileUpdateRequest(UserIdentityAvp userIdentity, DataReferenceType reference, byte[] userData);

    /**
   * Create an empty ProfileUpdateRequest that will need to have AVPs set on it before being sent.
   * 
   * @return a ProfileUpdateRequest object that can be sent using {@link ShClientActivity#sendProfileUpdateRequest(net.java.slee.resource.diameter.sh.events.types.ProfileUpdateRequest)} 
   */
    ProfileUpdateRequest createProfileUpdateRequest();

    /**
   * Create a SubscribeNotificationsRequest using the given parameters to populate the User-Identity, Data-Reference and Subs-Req-Type AVPs.
   * 
   * @return a SubscribeNotificationsRequest object that can be sent using {@link ShClientActivity#sendSubscribeNotificationsRequest(net.java.slee.resource.diameter.sh.events.types.SubscribeNotificationsRequest)} 
   * or {@link ShClientNotificationActivity#sendSubscribeNotificationsRequest(net.java.slee.resource.diameter.sh.events.types.SubscribeNotificationsRequest)}
   */
    SubscribeNotificationsRequest createSubscribeNotificationsRequest(UserIdentityAvp userIdentity, DataReferenceType reference, SubsReqType subscriptionType);

    /**
   * Create an empty SubscribeNotificationsRequest that will need to have AVPs set on it before being sent.
   * 
   * @return a SubscribeNotificationsRequest object that can be sent using {@link ShClientActivity#sendSubscribeNotificationsRequest(net.java.slee.resource.diameter.sh.events.types.SubscribeNotificationsRequest)} 
   * or {@link ShClientNotificationActivity#sendSubscribeNotificationsRequest(net.java.slee.resource.diameter.sh.events.types.SubscribeNotificationsRequest)}
   */
    SubscribeNotificationsRequest createSubscribeNotificationsRequest();

    /**
   * Create a PushNotificationAnswer containing a Result-Code or Experimental-Result AVP populated with the given value.
   * If <code>isExperimentalResultCode</code> is <code>true</code>, the <code>resultCode</code> parameter will be set
   * in a {@link org.mobicents.slee.resource.diameter.base.types.ExperimentalResultAvp} AVP, if it is <code>false</code> it 
   * will be sent as a standard Result-Code AVP.
   * 
   * @return a PushNotificationAnswer object that can be sent using {@link ShClientNotificationActivity#sendPushNotificationAnswer(net.java.slee.resource.diameter.sh.events.types.PushNotificationAnswer)} 
   */
    PushNotificationAnswer createPushNotificationAnswer(PushNotificationRequest request, long resultCode, boolean isExperimentalResultCode);

    /**
   * Create an empty PushNotificationAnswer that will need to have AVPs set on it before being sent.
   * @return a PushNotificationAnswer object that can be sent using {@link ShClientNotificationActivity#sendPushNotificationAnswer(net.java.slee.resource.diameter.sh.events.types.PushNotificationAnswer)}
   */
    PushNotificationAnswer createPushNotificationAnswer(PushNotificationRequest request);
}
