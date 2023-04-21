package net.sf.refactorit.utils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Aqris Software AS</p>
 * @author Tonis Vaga
 * @version 1.0
 */
public final class PathElement {

    public final String dir;

    public final String file;

    /**
   * @param dir
   * @param file
   */
    public PathElement(String dir, String file) {
        this.dir = dir;
        this.file = file;
    }
}
