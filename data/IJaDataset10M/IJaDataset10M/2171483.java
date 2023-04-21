package models.responses;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * This class represents a collection of all responses that the client is capable of supporting. It
 * provides the functionality to instantiate the appropriate response class given the name of the response.
 * 
 * @author Thomas Pedley
 */
public final class Responses {

    /** 
	 * A collection of the responses known to the client. The "Class" type is a generic type, all
	 * responses extend from "Response", however we cannot use this as the generic type as the Java
	 * compiler throws an error when we try adding subclasses as it claims they are not of type "Response"
	 * so we have to use the "?" type.
	 */
    private static HashMap<String, Class<?>> responses = new HashMap<String, Class<?>>();

    /**
	 * Private constructor to stop instantiation.
	 */
    private Responses() {
    }

    /**
	 * Static constructor.
	 */
    static {
        responses.put("Ping", ResponsePing.class);
        responses.put("Connect", ResponseConnect.class);
        responses.put("Disconnect", ResponseDisconnect.class);
        responses.put("FriendsList", ResponseFriendsList.class);
        responses.put("GroupList", ResponseGroupList.class);
        responses.put("AvatarStatusChange", ResponseAvatarStatusChange.class);
        responses.put("InstantMessage", ResponseInstantMessage.class);
        responses.put("TypingStatusChange", ResponseTypingStatusChange.class);
        responses.put("Error", ResponseError.class);
        responses.put("EncryptionDetails", ResponseEncryptionDetails.class);
        responses.put("NearbyAvatar", ResponseNearbyAvatar.class);
        responses.put("Chat", ResponseChat.class);
        responses.put("AvatarTrackingCanSetState", ResponseAvatarTrackingCanSetState.class);
        responses.put("AvatarTrackingSetState", ResponseAvatarTrackingSetState.class);
        responses.put("AvatarTrackingState", ResponseAvatarTrackingState.class);
        responses.put("NearbyAvatarsClear", ResponseNearbyAvatarsClear.class);
        responses.put("SessionMemberJoin", ResponseSessionMemberJoin.class);
        responses.put("SessionMemberLeave", ResponseSessionMemberLeave.class);
        responses.put("GroupMessage", ResponseGroupMessage.class);
        responses.put("SessionJoin", ResponseSessionJoin.class);
        responses.put("AvatarSearchResult", ResponseAvatarSearchResult.class);
        responses.put("FriendshipOfferResponse", ResponseFriendshipOfferResponse.class);
        responses.put("FriendshipTerminated", ResponseFriendshipTerminated.class);
        responses.put("GroupSearchStart", ResponseGroupSearchStart.class);
        responses.put("GroupSearch", ResponseGroupSearch.class);
        responses.put("SessionMembers", ResponseSessionMembers.class);
        responses.put("FriendshipOffer", ResponseFriendshipOffer.class);
        responses.put("GroupInvitationOffer", ResponseGroupInvitationOffer.class);
        responses.put("TeleportOffer", ResponseTeleportOffer.class);
        responses.put("CurrentLocation", ResponseCurrentLocation.class);
        responses.put("ImageCanSetState", ResponseImageCanSetState.class);
        responses.put("ImageSetState", ResponseImageSetState.class);
        responses.put("ImageState", ResponseImageState.class);
        responses.put("Image", ResponseImage.class);
        responses.put("CurrentParcel", ResponseCurrentParcel.class);
        responses.put("MessageBox", ResponseMessageBox.class);
        responses.put("InventoryOffer", ResponseInventoryOffer.class);
        responses.put("GroupNotice", ResponseGroupNotice.class);
        responses.put("BalanceChange", ResponseBalanceChange.class);
        responses.put("AvatarProfile", ResponseAvatarProfile.class);
        responses.put("TosUpdate", ResponseTosUpdate.class);
    }

    /**
	 * Instantiate the response associated with the given name.
	 * 
	 * @param name The name of the response to instantiate.
	 * @return The response (null if not found).
	 */
    public static Response InstantiateResponse(String name) {
        Class<?> c = responses.get(name);
        if (c != null) {
            Constructor<?> con = c.getConstructors()[0];
            try {
                return (Response) con.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
