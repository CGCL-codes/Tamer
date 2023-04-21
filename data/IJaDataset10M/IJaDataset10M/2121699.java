package datagen.groups;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;
import datagen.core.GeneratorMap;
import datagen.rescue.MutationException;
import javassist.CannotCompileException;
import javassist.NotFoundException;

public interface GroupCreator<T> {

    public static final String ROLE_FIELD_NAME = "Role";

    Vector<T> newGroup(T template, int members, GeneratorMap registry) throws NotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, MutationException, IntrospectionException, CannotCompileException, IOException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException;

    Vector<T> newGroup(T template, GeneratorMap registry) throws NotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, MutationException, IntrospectionException, CannotCompileException, IOException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException;

    public Object lookUp(String role, String attr);
}
