package pl.xperios.loggerwithguice;

import com.google.inject.MembersInjector;
import java.lang.reflect.Field;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

/**
 *
 * @author Praca
 */
class Log4JMembersInjector<T> implements MembersInjector<T> {

    private final Field field;

    private final XLog logger;

    Log4JMembersInjector(Field field) {
        this.field = field;
        this.logger = new XLogImpl(field.getDeclaringClass().getName());
        System.out.println("Injecting");
        field.setAccessible(true);
    }

    public void injectMembers(T t) {
        try {
            field.set(t, logger);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
