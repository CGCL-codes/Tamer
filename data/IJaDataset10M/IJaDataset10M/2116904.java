package net.kano.joustsim.oscar;

import net.kano.joustsim.Screenname;
import net.kano.joscar.ByteBlock;
import org.jetbrains.annotations.Nullable;

public interface BuddyIconChangeListener {

    void buddyIconChanged(BuddyIconTracker tracker, Screenname screenname, @Nullable ByteBlock iconData);

    void buddyIconPending(BuddyIconTracker tracker, Screenname screenname);
}
