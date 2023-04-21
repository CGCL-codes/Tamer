package at.urbiflock.ui;

import java.util.Vector;

public interface Flockr {

    public void addBuddy(Profile profile);

    public void removeBuddy(Profile profile);

    public boolean isBuddy(String uid);

    public Profile getBuddy(String uid);

    public Flock[] getFlocks();

    public void removeFlock(Flock f);

    public void registerProfileChangedListener(Object l);

    public void removeProfileChangedListener(Object l);

    public void registerBuddyListListener(Object l);

    public void removeBuddyListListener(Object l);

    public void registerDiscoveryListener(Object l);

    public void removeDiscoveryListener(Object l);

    public void registerFlocksListener(Object l);

    public void removeFlocksListener(Object l);

    public void updateMatchingProfile(Profile p);

    public void updateProfile();

    public void openFlockEditorOnNewFlock();

    public void deleteFlock(Flock f);

    public Profile getProfile();

    public void createFlockFromFieldMatchers(String flockName, Vector fieldMatchers, boolean shouldBeFriend, boolean shouldBeNearby);

    public void setHasOpenFlockEditor(boolean b);

    public boolean hasOpenFlockEditor();
}
