package net.nothinginteresting.datamappers3.annotations;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.nothinginteresting.datamappers3.DM;
import org.apache.log4j.Logger;
import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

/**
 * @author Dmitriy Gorbenko
 */
public class AnnotationRegistry {

    private static final Logger logger = Logger.getLogger(AnnotationRegistry.class);

    private static List<Class<?>> annotatedClasses;

    public static List<Class<?>> getAnnotatedClasses() {
        return annotatedClasses;
    }

    public static void init() throws IOException, ClassNotFoundException {
        annotatedClasses = Collections.unmodifiableList(getAnnotatedClasses(getAnnotationDB()));
    }

    private static List<Class<?>> getAnnotatedClasses(AnnotationDB db) throws ClassNotFoundException {
        logger.debug("Going to find classes annotated by Datamapper");
        Set<String> set = db.getAnnotationIndex().get(Datamapper.class.getName());
        List<Class<?>> result = new ArrayList<Class<?>>();
        for (String name : set) {
            Class<?> cls = Class.forName(name);
            if (!DM.class.isAssignableFrom(cls)) result.add(cls);
        }
        logger.debug("Found " + result.size() + " classes annotated by Datamapper");
        return result;
    }

    private static AnnotationDB getAnnotationDB() throws IOException {
        URL urls = ClasspathUrlFinder.findResourceBase("", Thread.currentThread().getContextClassLoader());
        logger.debug("Going to scan " + urls);
        AnnotationDB db = new AnnotationDB();
        db.scanArchives(urls);
        logger.debug("Scan finished");
        return db;
    }

    public static List<Class<?>> getAnnotatedSubtypes(Class<?> klass) {
        List<Class<?>> result = new ArrayList<Class<?>>();
        for (Class<?> key : annotatedClasses) {
            if (klass.isAssignableFrom(key)) result.add(key);
        }
        return result;
    }
}
