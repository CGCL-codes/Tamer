package jp.seraphr.file;

import java.io.File;

/**
 * Fileをうけとるインターフェース
 * 所謂VisitorパターンのAccepterではないので注意。
 */
public interface FileAccepter {

    /**
     *
     * @param aFile
     * @param aRelativePath 起点からの相対パス
     */
    public void accept(File aFile, String aRelativePath);
}
