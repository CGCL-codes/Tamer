package net.sf.josceleton.connection.impl.service.motion;

import java.util.Collection;
import net.sf.josceleton.connection.api.service.motion.MotionStream;
import net.sf.josceleton.connection.api.service.motion.MotionStreamListener;
import net.sf.josceleton.connection.api.service.user.UsersCollection;
import net.sf.josceleton.core.api.entity.joint.Joint;
import net.sf.josceleton.core.api.entity.joint.Skeleton;
import net.sf.josceleton.core.api.entity.location.Coordinate;
import net.sf.josceleton.core.api.entity.user.User;
import net.sf.josceleton.core.impl.async.DefaultAsync;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * @since 0.5
 */
class ContinuousMotionStreamInternalImpl extends DefaultAsync<MotionStreamListener> implements ContinuousMotionStreamInternal {

    private final MotionStream internalStream;

    private final UsersCollection users;

    private User currentlyListeningTo = null;

    @Inject
    ContinuousMotionStreamInternalImpl(@Assisted final MotionStream internalStream, @Assisted final UsersCollection users) {
        this.internalStream = internalStream;
        this.users = users;
    }

    /** {@inheritDoc} from {@link ContinuousMotionStreamInternal} */
    @Override
    public final void initAttachingToUser() {
        final Collection<User> processingUsers = this.users.getProcessing();
        if (processingUsers.isEmpty() == false) {
            this.startListeningTo(processingUsers.iterator().next());
        }
    }

    /** {@inheritDoc} from {@link UserServiceListener} */
    @Override
    public final void onUserWaiting(final User user) {
    }

    /** {@inheritDoc} from {@link UserServiceListener} */
    @Override
    public final void onUserProcessing(final User user) {
        if (this.currentlyListeningTo == null) {
            this.startListeningTo(user);
        }
    }

    /** {@inheritDoc} from {@link UserServiceListener} */
    @Override
    public final void onUserDead(final User user) {
        if (this.currentlyListeningTo != null && this.currentlyListeningTo.equals(user)) {
            this.stopListeningTo();
        }
    }

    /** {@inheritDoc} from {@link MotionStreamListener} */
    @Override
    public final void onMoved(final Joint movedJoint, final Coordinate updatedCoordinate, final Skeleton skeleton) {
        for (final MotionStreamListener listener : this.getListeners()) {
            listener.onMoved(movedJoint, updatedCoordinate, skeleton);
        }
    }

    private void startListeningTo(final User user) {
        this.currentlyListeningTo = user;
        this.internalStream.addListenerFor(this.currentlyListeningTo, this);
    }

    private void stopListeningTo() {
        this.internalStream.removeListenerFor(this.currentlyListeningTo, this);
        this.currentlyListeningTo = null;
        this.initAttachingToUser();
    }
}
