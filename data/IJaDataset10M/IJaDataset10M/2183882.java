package ch.oblivion.comixviewer.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link IProfileManager} implementation which stores all profiles in
 * memory. No data are saved persistent.
 * <p>
 * This class is not thread safe.
 * 
 * @author Stefan Gmeiner
 */
public class MemoryProfileManager implements IProfileManager {

    private static final AtomicInteger profileId = new AtomicInteger();

    private static final AtomicInteger pageId = new AtomicInteger();

    private final Map<Integer, MemoryProfile> profiles = new HashMap<Integer, MemoryProfile>();

    public MemoryProfileManager() {
    }

    @Override
    public Profile create() {
        return new MemoryProfile(nextProfileId(), this);
    }

    private int nextProfileId() {
        return profileId.incrementAndGet();
    }

    protected int nextPageId() {
        return pageId.incrementAndGet();
    }

    @Override
    public void deleteProfile(Profile profile, boolean deleteImages) {
        profiles.remove(profile);
        if (deleteImages) {
        }
    }

    @Override
    public Profile[] getProfiles() {
        return profiles.values().toArray(new Profile[0]);
    }

    @Override
    public void saveProfile(Profile profile) {
        if (profile.getName() == null) {
            throw new NullPointerException("Profile name is null");
        }
        if (!(profile instanceof MemoryProfile)) {
            throw new IllegalArgumentException("Profile is not appropiate for this profile manager.");
        }
        MemoryProfile memoryProfile = (MemoryProfile) profile;
        profiles.put(memoryProfile.getProfileId(), memoryProfile);
    }
}
