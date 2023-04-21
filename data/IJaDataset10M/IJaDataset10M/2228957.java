package jezuch.utils.starmapper3.model.common;

import java.io.File;
import jezuch.utils.parameters.ValidationException;
import jezuch.utils.parameters.Validator;

/**
 * @author ksobolewski
 */
public class FileValidator implements Validator<File> {

    private final boolean isDir;

    private final boolean nullable;

    public FileValidator(boolean isDir) {
        this(isDir, false);
    }

    public FileValidator(boolean isDir, boolean nullable) {
        this.isDir = isDir;
        this.nullable = nullable;
    }

    public void validate(File value) throws ValidationException {
        if (value == null) {
            if (!nullable) throw new ValidationException("value == null");
        } else {
            if (isDir) {
                if (!value.isDirectory()) throw new ValidationException("!value.isDirectory()");
            } else {
                if (!value.isFile()) throw new ValidationException("!value.isFile()");
            }
        }
    }
}
