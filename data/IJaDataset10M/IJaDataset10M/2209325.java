package datagen.groups;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.apache.commons.beanutils.BeanUtils;
import datagen.core.GeneratorMap;
import datagen.generators.FirstNameGenerator;
import datagen.rescue.MutationException;

public class TwinGroupCreator<T> extends DefaultGroupCreator<T> implements GroupCreator<T> {

    public static final String GROUP_NAME = "Twin";

    public static final int TOTAL_MEMBERS = 2;

    private ArrayList<String> changeFields = new ArrayList<String>();

    private static Logger logger = Logger.getLogger(TwinGroupCreator.class.getName());

    public TwinGroupCreator() {
        changeFields.add(FirstNameGenerator.FIELD_NAME);
    }

    public enum Roles {

        Son, Daughter
    }

    ;

    public Vector<T> newGroup(T template, GeneratorMap registry) throws NotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, MutationException, IntrospectionException, CannotCompileException, IOException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        return newGroup(template, TOTAL_MEMBERS, registry);
    }

    @SuppressWarnings("unchecked")
    public Vector<T> newGroup(T template, int members, GeneratorMap registry) throws NotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, MutationException, IntrospectionException, CannotCompileException, IOException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Vector<T> vRecords = new Vector<T>();
        for (int i = 0; i < members; i++) {
            Random randomGenerator = new Random();
            int randomIndex = randomGenerator.nextInt(Roles.values().length);
            setRole(template, Roles.values()[randomIndex].name());
            vRecords.add((T) mutate((T) BeanUtils.cloneBean(template), registry, changeFields, logger));
        }
        return vRecords;
    }

    public Object lookUp(String role, String attr) {
        return null;
    }
}
