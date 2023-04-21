package nakayo.gameserver.questEngine.handlers;

import com.aionemu.commons.scripting.classlistener.ClassListener;
import com.aionemu.commons.scripting.classlistener.DefaultClassListener;
import com.aionemu.commons.utils.ClassUtils;
import nakayo.gameserver.questEngine.QuestEngine;
import org.apache.log4j.Logger;
import java.lang.reflect.Modifier;

/**
 * @author MrPoke
 */
public class QuestHandlerLoader extends DefaultClassListener implements ClassListener {

    private static final Logger logger = Logger.getLogger(QuestHandlerLoader.class);

    public QuestHandlerLoader() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postLoad(Class<?>[] classes) {
        for (Class<?> c : classes) {
            if (logger.isDebugEnabled()) logger.debug("Load class " + c.getName());
            if (!isValidClass(c)) continue;
            if (ClassUtils.isSubclass(c, QuestHandler.class)) {
                try {
                    Class<? extends QuestHandler> tmp = (Class<? extends QuestHandler>) c;
                    if (tmp != null) QuestEngine.getInstance().addQuestHandler(tmp.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        super.postLoad(classes);
    }

    @Override
    public void preUnload(Class<?>[] classes) {
        if (logger.isDebugEnabled()) for (Class<?> c : classes) logger.debug("Unload class " + c.getName());
        super.preUnload(classes);
        QuestEngine.getInstance().clear();
    }

    public boolean isValidClass(Class<?> clazz) {
        final int modifiers = clazz.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) return false;
        if (!Modifier.isPublic(modifiers)) return false;
        return true;
    }
}
