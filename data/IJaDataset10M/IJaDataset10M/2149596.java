package ops.model.manager;

import java.io.File;
import ops.model.Class;

public class ClassManager extends EntityManager<Class> {

    private static final File CLASSES_FILE = new File("data/classes.dat");

    public ClassManager() {
        super();
    }

    @Override
    public File getFile() {
        return CLASSES_FILE;
    }

    @Override
    protected Object getKeyForEntity(Class element) {
        return element.getCode();
    }
}
