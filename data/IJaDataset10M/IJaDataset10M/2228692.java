package org.eledge;

import java.io.File;

public class Security {

    /**
     * @deprecated
     */
    @Deprecated
    public static boolean fileAccessIsAllowed(String filename, Student student) {
        String allowedPath = "";
        try {
            if (student.getIsInstructor()) {
                allowedPath = new File(Course.uploadsDirectory).getCanonicalPath();
            } else {
                allowedPath = new File(Course.uploadsDirectory + student.getIDNumber()).getCanonicalPath();
            }
        } catch (Exception e) {
            return false;
        }
        return fileAccessIsAllowed(allowedPath, filename);
    }

    public static boolean fileAccessIsAllowed(String allowedPath, String filename) {
        try {
            File uploadFile = new File(filename).getCanonicalFile();
            File parentDirectory = uploadFile.getParentFile();
            File allowedDirectory;
            allowedDirectory = new File(allowedPath).getCanonicalFile();
            if (parentDirectory.equals(allowedDirectory)) {
                return true;
            } else {
                if (fileAccessIsAllowed(allowedPath, parentDirectory.getCanonicalPath())) {
                    if (!parentDirectory.exists()) {
                        parentDirectory.mkdir();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
}
