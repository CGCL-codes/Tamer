package edu.opexcavator.nlp.preprocessing;

/**
 * @author Jesica N. Fera
 *
 */
public class AbstractFreelingModule {

    static {
        System.load(DocumentPreprocessorImpl.class.getClassLoader().getResource("configFiles/libmorfo_java.so").getPath());
    }

    static final String FREELINGDIR = "/usr/local";

    static final String DATA = FREELINGDIR + "/share/FreeLing/";

    static final String LANG = "es";
}
