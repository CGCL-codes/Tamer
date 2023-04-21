package de.schwarzrot.ui.validation.constraints;

import java.io.File;
import com.jgoodies.binding.PresentationModel;
import de.schwarzrot.data.Entity;

/**
 * checks the value of a beans property against given filename. With the mode
 * parameter it is possible to activate additional checks related to the name
 * and/or file type.
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * 
 * @param <E>
 *            the type of the bean, managed by a presentation model
 */
public class VCMatchingFile<E extends Entity> extends AbstractValidationConstraint<E> {

    /**
     * the name of the file must match exactly the filename parameter from
     * constructor
     */
    public static final int EXACT_NAME = 0x001;

    /**
     * the name of the file must contain the string from filename parameter of
     * constructor
     */
    public static final int CONTAINS_NAME = 0x002;

    /**
     * the name of the file must start with the string from filename parameter
     * of constructor
     */
    public static final int STARTS_WITH = 0x004;

    /**
     * the name of the file must end with the string from filename parameter of
     * constructor
     */
    public static final int ENDS_WITH = 0x008;

    /**
     * file must exist and be readable
     */
    public static final int READABLE = 0x010;

    /**
     * file must exist and be writable
     */
    public static final int WRITABLE = 0x020;

    /**
     * file must exist and be executable
     */
    public static final int EXECUTABLE = 0x040;

    /**
     * file must not exist
     */
    public static final int NOT_EXISTS = 0x080;

    /**
     * file must be a directory
     */
    public static final int DIRECTORY = 0x100;

    /**
     * create a valiation constraint, that validates the content of a bean
     * property against given filename and validation modes.
     * <p>
     * {@code mode} may be any combination of {@code EXACT_NAME},
     * {@code CONTAINS_NAME}, {@code STARTS_WITH}, {@code ENDS_WITH},
     * {@code READABLE}, {@code WRITABLE} and {@code EXECUTABLE}.
     * 
     * @param model
     *            - the presentation model, that manages the bean
     * @param propertyName
     *            - the name of the property to check
     * @param mode
     *            - validation modes of the file
     * @param filename
     *            - the filename to check property content with. May be null
     */
    public VCMatchingFile(PresentationModel<E> model, String propertyName, int mode, String filename) {
        super(model, propertyName);
        name2Check = filename;
        validationMode = mode;
    }

    @Override
    public boolean matches() {
        Object value = getValue();
        boolean rv = true;
        if (value == null) return false;
        try {
            File tmp = (File) value;
            if ((validationMode & READABLE) != 0) {
                if (!(tmp.exists() && tmp.canRead())) return false;
            }
            if ((validationMode & WRITABLE) != 0) {
                if (!(tmp.exists() && tmp.canWrite())) return false;
            }
            if ((validationMode & EXECUTABLE) != 0) {
                if (!(tmp.exists() && tmp.canExecute())) return false;
            }
            if ((validationMode & NOT_EXISTS) != 0) {
                if (tmp.exists()) return false;
            }
            if ((validationMode & DIRECTORY) != 0) {
                if (!(tmp.exists() && tmp.isDirectory())) return false;
            }
            if ((validationMode & EXACT_NAME) != 0 && name2Check != null) {
                if (tmp.getName().compareTo(name2Check) != 0) return false;
            }
            if ((validationMode & CONTAINS_NAME) != 0 && name2Check != null) {
                if (!tmp.getName().contains(name2Check)) return false;
            }
            if ((validationMode & STARTS_WITH) != 0 && name2Check != null) {
                if (!tmp.getName().startsWith(name2Check)) return false;
            }
            if ((validationMode & ENDS_WITH) != 0 && name2Check != null) {
                if (!tmp.getName().endsWith(name2Check)) return false;
            }
        } catch (Exception e) {
            rv = false;
            e.printStackTrace();
        }
        return rv;
    }

    private String name2Check;

    private int validationMode;
}
