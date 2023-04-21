package se.ramfelt.psnfriends.model;

import se.ramfelt.psn.model.Friend;
import se.ramfelt.psn.model.TrophySummary;
import se.ramfelt.psn.model.Friend.Presence;
import android.os.Parcel;
import junit.framework.TestCase;

public class ParcelableFriendTest extends TestCase {

    public void testRoundtripParcable() {
        Friend friend = new Friend("onlineid", Presence.Online, "currentgame");
        ParcelableFriend parcelable = new ParcelableFriend(friend);
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Friend copy = ParcelableFriend.CREATOR.createFromParcel(parcel).getFriend();
        assertNotNull("Marshalled friend copy was null", copy);
        assertEquals(friend.getOnlineId(), copy.getOnlineId());
        assertEquals(friend.getCurrentGame(), copy.getCurrentGame());
        assertEquals(friend.getPresence(), copy.getPresence());
    }

    public void testChildParcable() {
        Friend friend = new Friend("onlineid");
        TrophySummary trophies = new TrophySummary();
        friend.setTrophySummary(trophies);
        trophies.setBronze(12);
        ParcelableFriend parcelable = new ParcelableFriend(friend);
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Friend copy = ParcelableFriend.CREATOR.createFromParcel(parcel).getFriend();
        assertNotNull("Marshalled friend copy was null", copy);
        assertEquals(friend.getTrophySummary().getBronze(), copy.getTrophySummary().getBronze());
    }
}
