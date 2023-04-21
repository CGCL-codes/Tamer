package org.dykman.dexter.base;

public class TextFunction implements PathFunction {

    public String apply(String path, String arg) {
        StringBuilder sb = new StringBuilder(path);
        if (path.length() > 0 && path.charAt(path.length() - 1) != '/') {
            sb.append("/");
        }
        sb.append("text()");
        return sb.toString();
    }
}
