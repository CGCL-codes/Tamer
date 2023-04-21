package org.openaion.gameserver.utils.i18n;

import java.lang.reflect.Modifier;
import org.apache.log4j.Logger;
import org.openaion.commons.scripting.classlistener.ClassListener;
import org.openaion.commons.scripting.classlistener.DefaultClassListener;
import org.openaion.commons.utils.ClassUtils;

/**
 * @author blakawk
 *
 */
public class LanguagesLoader extends DefaultClassListener implements ClassListener {

    private static final Logger log = Logger.getLogger(Language.class);

    private final LanguageHandler handler;

    public LanguagesLoader(LanguageHandler handler) {
        this.handler = handler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void postLoad(Class<?>[] classes) {
        for (Class<?> clazz : classes) {
            if (log.isDebugEnabled()) {
                log.debug("Loading class " + clazz.getName());
            }
            if (!isValidClass(clazz)) {
                continue;
            }
            if (ClassUtils.isSubclass(clazz, Language.class)) {
                Class<? extends Language> language = (Class<? extends Language>) clazz;
                if (language != null) {
                    try {
                        handler.registerLanguage(language.newInstance());
                    } catch (Exception e) {
                        log.error("Registering " + language.getName(), e);
                    }
                }
            }
        }
        super.postLoad(classes);
        log.info("Loaded " + handler.size() + " custom message handlers.");
    }

    @Override
    public void preUnload(Class<?>[] classes) {
        if (log.isDebugEnabled()) {
            for (Class<?> clazz : classes) {
                log.debug("Unload language " + clazz.getName());
            }
        }
        super.preUnload(classes);
        handler.clear();
    }

    public boolean isValidClass(Class<?> clazz) {
        final int modifiers = clazz.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
            return false;
        }
        if (!Modifier.isPublic(modifiers)) {
            return false;
        }
        return true;
    }
}
