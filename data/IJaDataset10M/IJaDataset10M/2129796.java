package ru.amse.jsynchro.kernel;

import java.io.File;
import java.util.Deque;

/**
 * @author miha
 * class contains difference (or necessary change to remove this differences).
 *  Difference is just a file or folder which is different from
 *  the similar file/folder of the compared folder. 
 *  Difference is described by the path, which is relative to it's root holder,
 *  an operation, which must be performed to remove this difference, 
 *  and a flag about if the difference
 */
public class Difference {

    private Operation myOperation;

    private final String myPath;

    private boolean isDirectory;

    public Difference(Deque<String> relativePath, Operation operation, boolean isDirectory) {
        StringBuilder sb = new StringBuilder();
        for (String s : relativePath) {
            sb.append(s + File.separator);
        }
        sb.deleteCharAt(sb.length() - 1);
        myPath = sb.toString();
        myOperation = operation;
        this.isDirectory = isDirectory;
    }

    public Difference(String relativePath, Operation operation, boolean isDirectory) {
        myOperation = operation;
        myPath = relativePath;
        this.isDirectory = isDirectory;
    }

    public void setOperation(Operation operation) {
        myOperation = operation;
    }

    public Operation getOperation() {
        return myOperation;
    }

    public String toString() {
        return myPath + " " + myOperation;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Difference)) {
            return false;
        }
        Difference d = (Difference) o;
        return (myPath.equals(myPath) && myOperation == d.myOperation);
    }

    /**
     * @return a relative of root folder path
     */
    public String getPath() {
        return myPath;
    }

    public boolean isDirectory() {
        return isDirectory;
    }
}
