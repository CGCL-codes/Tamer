package net.jwpa.cache;

import java.util.logging.Logger;
import net.jwpa.config.LogUtil;
import com.db4o.activation.ActivationPurpose;
import com.db4o.activation.Activator;
import com.db4o.ta.Activatable;

abstract class StorableObject implements Activatable {

    private transient Activator _activator;

    private static final Logger logger = LogUtil.getLogger();

    public void bind(Activator activator) {
        if (_activator == activator) {
            return;
        }
        if (activator != null && _activator != null) {
            throw new IllegalStateException();
        }
        _activator = activator;
    }

    public void activate(ActivationPurpose purpose) {
        if (_activator != null) {
            _activator.activate(purpose);
        }
        if (purpose == ActivationPurpose.WRITE) {
            LogUtil.logDebug(logger, "Activating " + this);
        }
    }
}
