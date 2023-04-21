package org.codemon.analysis;

import java.io.Serializable;
import org.codemon.tool.MetaInfo;

public abstract class Point implements Serializable {

    private MetaInfo metaInfo;

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    public boolean isCoverd() {
        return false;
    }

    public void cover(int message) {
    }

    public void unCover(Point point) {
    }

    public String getStringValue() {
        return null;
    }

    public Point(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }
}
