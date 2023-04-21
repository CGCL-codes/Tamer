package zcatalog.fs;

import zcatalog.db.ZCatObject;
import zcatalog.xml.jaxb.FileMeta;
import zcatalog.xml.jaxb.FolderMeta;
import zcatalog.xml.jaxb.ImageMeta;
import zcatalog.xml.jaxb.PathMeta;

/**
 *
 * @author Alessandro Zigliani
 */
public class DummyTraverseObserver implements EnumObserver {

    @Override
    public void notifyActionDescr(String msg) {
    }

    @Override
    public void setBounds(int lowerBound, int upperBound) {
    }

    @Override
    public void setProgress(int progress) {
    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Override
    public void incrementProgress(int value) {
    }

    @Override
    public void notifyObjectName(String objName) {
    }
}
