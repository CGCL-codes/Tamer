package net.teqlo.script;

import net.teqlo.util.Loggers;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

public class TeqloContextFactory extends ContextFactory {

    public boolean hasFeature(Context cx, int featureIndex) {
        switch(featureIndex) {
            case Context.FEATURE_STRICT_MODE:
                return Loggers.SCRIPTS.isInfoEnabled();
        }
        return super.hasFeature(cx, featureIndex);
    }
}
