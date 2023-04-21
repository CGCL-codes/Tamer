package org.base.apps.util.config;

import org.apache.commons.lang3.StringUtils;
import org.base.apps.util.Named;

/**
 * The root parent for all {@link ResourceManager}s and provides direct access
 * to the &quot;env&quot; configuration bundle.
 * 
 * @author Kevan Simpson
 */
public class EnvConfig extends ResourceManager {

    private static EnvConfig mDefault = new EnvConfig(new Named() {

        @Override
        public String getName() {
            return "env-default";
        }
    });

    /** Package-level constructor, only instantiated by {@link ResourceManager}. */
    EnvConfig() {
        this(new Named() {

            @Override
            public String getName() {
                return "env";
            }
        });
    }

    /** Package-level constructor, only instantiated by {@link ResourceManager}. */
    private EnvConfig(Named rsrc) {
        super(rsrc);
        if (StringUtils.equals("env", rsrc.getName())) {
            System.out.println("Loading default env.properties...");
        }
    }

    @Override
    public String getString(CharSequence property, Object... tkns) {
        return replaceVars(getProps().getProperty(String.valueOf(property), "??" + property + "??"));
    }

    /** @see org.base.apps.util.config.ResourceManager#getRawValue(java.lang.CharSequence) */
    @Override
    protected String getRawValue(CharSequence property) {
        String rawv = superGetRawValue(property);
        return (rawv == null) ? mDefault.superGetRawValue(property) : rawv;
    }

    protected String superGetRawValue(CharSequence property) {
        return super.getRawValue(property);
    }
}
