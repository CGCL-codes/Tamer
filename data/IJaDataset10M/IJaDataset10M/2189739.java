package us.wthr.jdem846.render;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import us.wthr.jdem846.AppRegistry;
import us.wthr.jdem846.DiscoverableAnnotationIndexer;
import us.wthr.jdem846.annotations.DemEngine;
import us.wthr.jdem846.annotations.Initialize;
import us.wthr.jdem846.annotations.Registry;
import us.wthr.jdem846.exception.AnnotationIndexerException;
import us.wthr.jdem846.exception.RegistryException;
import us.wthr.jdem846.i18n.I18N;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;

@Registry
public class EngineRegistry implements AppRegistry {

    private static Log log = Logging.getLog(EngineRegistry.class);

    private static Map<String, EngineInstance> engineMap = new HashMap<String, EngineInstance>();

    protected static void addEngineInstance(Class<?> clazz) throws ClassNotFoundException {
        DemEngine annotation = (DemEngine) clazz.getAnnotation(DemEngine.class);
        String name = annotation.name();
        name = I18N.get(name, name);
        EngineInstance engineInstance = new EngineInstance(clazz);
        if (annotation.enabled()) {
            EngineRegistry.engineMap.put(annotation.identifier(), engineInstance);
            log.info("Adding render engine instance for " + clazz.getName() + ": " + annotation.name());
        } else {
            log.info("Render engine is disabled: " + clazz.getName());
        }
    }

    protected EngineRegistry() {
    }

    @Initialize
    public static void init() throws RegistryException {
        log.info("Static initialization of EngineRegistry");
        List<Class<?>> clazzList = null;
        try {
            clazzList = DiscoverableAnnotationIndexer.getAnnotatedClasses(DemEngine.class.getName());
        } catch (AnnotationIndexerException ex) {
            throw new RegistryException("Failed to retrieve DemEngine classes: " + ex.getMessage(), ex);
        }
        try {
            if (clazzList != null) {
                for (Class<?> clazz : clazzList) {
                    addEngineInstance(clazz);
                }
            }
        } catch (Exception ex) {
            throw new RegistryException("Error loading engine class: " + ex.getMessage(), ex);
        }
    }

    public static EngineInstance getInstance(String identifier) {
        return engineMap.get(identifier);
    }

    public static List<EngineInstance> getInstances() {
        List<EngineInstance> instanceList = new LinkedList<EngineInstance>();
        for (String identifier : engineMap.keySet()) {
            instanceList.add(engineMap.get(identifier));
        }
        return instanceList;
    }
}
