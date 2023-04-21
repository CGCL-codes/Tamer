package gumbo.model.state.impl;

import gumbo.core.life.impl.DisposableImpl;
import gumbo.core.struct.StructUtils;
import gumbo.core.util.AssertUtils;
import gumbo.core.util.CoreUtils;
import gumbo.model.state.Animatable;
import gumbo.model.state.AnimateSensor;
import gumbo.model.state.FrameGuard;
import gumbo.model.state.FrameTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Default implementation of AnimateSource.
 */
public class AnimateSensorImpl {

    private AnimateSensorImpl() {
    }

    public static class OfOne<T extends Animatable> extends DisposableImpl.IdentityEquality implements AnimateSensor.OfOne<T> {

        /**
		 * Gets the target monitored by this sensor.
		 * @return Shared exposed target. None if null.
		 */
        protected T getTarget() {
            return _target;
        }

        @Override
        public void setTarget(T target) {
            _target = target;
        }

        @Override
        public void animateState(FrameTime frameTime) {
            _target.animateState(frameTime);
        }

        protected void implDispose() {
            super.implDispose();
            _target = null;
        }

        private T _target;
    }

    public static class OfMany<T extends Animatable> extends DisposableImpl.IdentityEquality implements AnimateSensor.OfMany<T> {

        /**
		 * Gets the targets monitored by this sensor.
		 * @return Shared output group of shared exposed targets. Never null.
		 */
        protected Set<T> getTargets() {
            if (_targetsUnmod == null) {
                _targetsUnmod = StructUtils.unmodSet(_targets);
            }
            return _targetsUnmod;
        }

        @Override
        public void addTargets(Collection<? extends T> targets) {
            AssertUtils.assertNonNullArgAll(targets);
            _targets.addAll(targets);
        }

        @Override
        public void clearTargets() {
            _targets.clear();
        }

        @Override
        public void removeTargets(Collection<?> targets) {
            AssertUtils.assertNonNullArgAll(targets);
            _targets.removeAll(targets);
        }

        @Override
        public void animateState(FrameTime frameTime) {
            if (_frameGuard.isRepeatFrame(frameTime)) return;
            for (T target : _targets) {
                target.animateState(frameTime);
            }
        }

        protected void implDispose() {
            super.implDispose();
            _frameGuard = CoreUtils.dispose(_frameGuard);
            _targets = CoreUtils.dispose(_targets);
            _targetsUnmod = CoreUtils.dispose(_targetsUnmod);
        }

        private FrameGuard _frameGuard = new FrameGuard();

        private Set<T> _targets = new HashSet<T>();

        private Set<T> _targetsUnmod = null;
    }
}
