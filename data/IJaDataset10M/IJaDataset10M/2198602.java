package tei.cr.input;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Util {

    private static final Logger log = Logger.getLogger(Util.class.getName());

    private static final Class<File2XMLFragmentProducer>[] fragmentProducer = new Class[] { PlainText.class, TextLine.class };

    public static final List<Class<File2XMLFragmentProducer>> getFragmentProducer() {
        return Collections.unmodifiableList(Arrays.asList(fragmentProducer));
    }

    public static final File2XMLFragmentProducer getProducer(String filename, String encoding, String format) {
        for (int i = 0; i < fragmentProducer.length; i++) {
            if (format.equals(fragmentProducer[i].getName())) {
                java.lang.reflect.Constructor constr = null;
                try {
                    constr = fragmentProducer[i].getConstructor(new Class[] { Class.forName("java.lang.String"), Class.forName("java.lang.String") });
                } catch (NoSuchMethodException ex) {
                    log.log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    log.log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
                File2XMLFragmentProducer f = null;
                try {
                    f = (File2XMLFragmentProducer) constr.newInstance(new Object[] { filename, encoding });
                } catch (InstantiationException ex) {
                    log.log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    log.log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    log.log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    log.log(Level.SEVERE, null, ex);
                }
                return (f);
            }
        }
        throw new IllegalStateException("Format not known: " + format);
    }
}
