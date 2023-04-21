package sol.admin.systemmanagement.util;

import sol.admin.systemmanagement.jni.*;

/**
 * 
 * @author Markus Hammori
 */
class PasswordReaderImplementationJNI implements PasswordReaderInterface {

    public PasswordReaderImplementationJNI() {
    }

    public String readPwd(String prompt) {
        System.out.print(prompt);
        sol.admin.systemmanagement.jni.PasswordReader reader = new sol.admin.systemmanagement.jni.PasswordReader();
        return reader.readPwd();
    }
}
