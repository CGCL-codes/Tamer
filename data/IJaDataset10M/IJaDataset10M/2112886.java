package bg.unisofia.fmi.kanban.util.model.converter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.PropertyUtils;
import bg.unisofia.fmi.kanban.client.model.IModelElement;
import bg.unisofia.fmi.kanban.client.model.impl.PhaseClientWrapper;
import bg.unisofia.fmi.kanban.client.model.impl.ProjectClientWrapper;
import bg.unisofia.fmi.kanban.client.model.impl.TaskClientWrapper;
import bg.unisofia.fmi.kanban.client.model.impl.TaskHistoryRecordClientWrapper;
import bg.unisofia.fmi.kanban.client.model.impl.UserClientWrapper;
import bg.unisofia.fmi.kanban.entity.Phase;
import bg.unisofia.fmi.kanban.entity.Project;
import bg.unisofia.fmi.kanban.entity.Task;
import bg.unisofia.fmi.kanban.entity.TaskHistoryRecord;
import bg.unisofia.fmi.kanban.entity.User;
import java.lang.reflect.Method;

/**
 * A utility class converting between the hierarchies of JPA entities and the classes
 * used in the GWT client.
 *
 * @author nikolay.grozev
 */
public class ClientServerObjectConverter {

    private static final Map<Class, Class> TRANSLATION_TO_CLIENT_MAPPING;

    private static final Map<Class, Class> TRANSLATION_TO_SERVER_MAPPING;

    static {
        TRANSLATION_TO_CLIENT_MAPPING = new HashMap<Class, Class>();
        TRANSLATION_TO_SERVER_MAPPING = new HashMap<Class, Class>();
        TRANSLATION_TO_CLIENT_MAPPING.put(Project.class, ProjectClientWrapper.class);
        TRANSLATION_TO_SERVER_MAPPING.put(ProjectClientWrapper.class, Project.class);
        TRANSLATION_TO_CLIENT_MAPPING.put(Phase.class, PhaseClientWrapper.class);
        TRANSLATION_TO_SERVER_MAPPING.put(PhaseClientWrapper.class, Phase.class);
        TRANSLATION_TO_CLIENT_MAPPING.put(Task.class, TaskClientWrapper.class);
        TRANSLATION_TO_SERVER_MAPPING.put(TaskClientWrapper.class, Task.class);
        TRANSLATION_TO_CLIENT_MAPPING.put(TaskHistoryRecord.class, TaskHistoryRecordClientWrapper.class);
        TRANSLATION_TO_SERVER_MAPPING.put(TaskHistoryRecordClientWrapper.class, TaskHistoryRecord.class);
        TRANSLATION_TO_CLIENT_MAPPING.put(User.class, UserClientWrapper.class);
        TRANSLATION_TO_SERVER_MAPPING.put(UserClientWrapper.class, User.class);
    }

    /**
     * Converts a JPA project to a GWT compliant project.
     * @param aProject - the project to convert. Must not be null.
     * @return - the convered cleint project.
     */
    public static ProjectClientWrapper convertProject(Project aProject) {
        return (ProjectClientWrapper) convert(aProject, true);
    }

    /**
     * Converts a GWT compliant project.
     * @param aProject - hte GWT projecct to convert. Must not be null.
     * @return - the converted GWT project.
     */
    public static Project convertProject(ProjectClientWrapper aProject) {
        return (Project) convert(aProject, false);
    }

    /**
     *  Converts a JPA user to GWT compliant user.
     * @param user
     * @return
     */
    public static UserClientWrapper convertUser(User user) {
        return (UserClientWrapper) convert(user, true);
    }

    /**
     *  Converts a GWT user to JPA user.
     * @param user
     * @return
     */
    public static User convertUser(UserClientWrapper user) {
        return (User) convert(user, false);
    }

    /**
     *  Converts a JPA task to GWT task. 
     * @param task
     * @return
     */
    public static TaskClientWrapper convertTask(Task task) {
        return (TaskClientWrapper) convert(task, true);
    }

    /** 
     *  Converts a GWT task to JPA task.
     * @param task
     * @return
     */
    public static Task convertTask(TaskClientWrapper task) {
        return (Task) convert(task, false);
    }

    public static PhaseClientWrapper convertPhase(Phase phase) {
        return (PhaseClientWrapper) convert(phase, true);
    }

    public static Phase convertPhase(PhaseClientWrapper phase) {
        return (Phase) convert(phase, false);
    }

    private static Object convert(Object aProject, boolean aToClientFlag) {
        try {
            return convert(aProject, aToClientFlag, new HashMap<Object, Object>());
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ClientServerObjectConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ClientServerObjectConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ClientServerObjectConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ClientServerObjectConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static Object convert(Object anObjToConvert, boolean aToClientFlag, Map<Object, Object> anAlreadyConvertedMap) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (anAlreadyConvertedMap.containsKey(anObjToConvert)) {
            return anAlreadyConvertedMap.get(anObjToConvert);
        }
        if (anObjToConvert instanceof Collection) {
            Collection resultCollection = createCollection(anObjToConvert);
            for (Object o : (Collection) anObjToConvert) {
                resultCollection.add(convert(o, aToClientFlag, anAlreadyConvertedMap));
            }
            return resultCollection;
        }
        if (!(anObjToConvert instanceof IModelElement)) {
            return anObjToConvert;
        }
        Map<Class, Class> translationMapping = aToClientFlag ? TRANSLATION_TO_CLIENT_MAPPING : TRANSLATION_TO_SERVER_MAPPING;
        Class resultClass = translationMapping.get(anObjToConvert.getClass());
        Object result = resultClass.newInstance();
        anAlreadyConvertedMap.put(anObjToConvert, result);
        Map propertiesToConvert = PropertyUtils.describe(anObjToConvert);
        Map propertiesToSet = PropertyUtils.describe(result);
        for (Object entryObj : propertiesToConvert.entrySet()) {
            Map.Entry entry = (Map.Entry) entryObj;
            if (propertiesToSet.containsKey(entry.getKey())) {
                try {
                    Object toSet = convert(entry.getValue(), aToClientFlag, anAlreadyConvertedMap);
                    set(result, entry.getKey().toString(), toSet);
                } catch (Throwable ex) {
                    Logger.getLogger(ClientServerObjectConverter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    private static void set(Object result, String name, Object toSet) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        try {
            PropertyUtils.setProperty(result, name, toSet);
        } catch (NoSuchMethodException ex) {
            for (Method m : result.getClass().getDeclaredMethods()) {
                result.getClass().getClassLoader().setDefaultAssertionStatus(false);
                if (m.getName().equalsIgnoreCase("set" + name) && m.getName().length() > 4 && Character.isUpperCase(m.getName().charAt(3))) {
                    m.invoke(result, toSet);
                    break;
                }
            }
        }
    }

    private static Collection createCollection(Object anObjToConvert) {
        if (Arrays.asList(anObjToConvert.getClass().getInterfaces()).contains(Set.class)) {
            return new HashSet();
        }
        return new ArrayList();
    }
}
