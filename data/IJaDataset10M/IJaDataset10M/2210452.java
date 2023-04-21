package nacaLib.classLoad;

import jlib.classLoader.ClassDynLoader;
import jlib.classLoader.ClassDynLoaderFactory;

public class CustomClassDynLoaderFactory extends ClassDynLoaderFactory {

    private CustomClassDynLoaderFactory() {
    }

    public static ClassDynLoaderFactory getInstance() {
        if (ms_instance == null) ms_instance = new CustomClassDynLoaderFactory();
        return ms_instance;
    }

    public ClassDynLoader make() {
        return new CustomClassDynLoader();
    }
}
