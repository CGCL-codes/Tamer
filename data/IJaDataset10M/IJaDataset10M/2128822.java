package cn.vlabs.dlog.custom.loader;

import java.io.InputStream;

/**
 * @author thinkbase.net
 * @version $Revision: 1.2 $
 */
public class ClassFileVO {

    private String sourceFile;

    private InputStream classInputStream;

    public ClassFileVO(String sourceFile, InputStream classIs) {
        this.sourceFile = sourceFile;
        this.classInputStream = classIs;
    }

    public InputStream getClassInputStream() {
        return classInputStream;
    }

    public String getSourceFile() {
        return sourceFile;
    }
}
