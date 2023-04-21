package cn.vlabs.clb.ui.frameservice;

import cn.vlabs.clb.api.document.VersionInfo;
import cn.vlabs.clb.domain.document.Version;

public class DataTransform {

    public static VersionInfo toVersionInfo(Version v) {
        VersionInfo vinfo = new VersionInfo();
        vinfo.filename = v.getFilename();
        vinfo.filesize = v.getSize();
        vinfo.updateby = v.getUpdateBy();
        vinfo.updateDate = v.getUpdateTime();
        return vinfo;
    }
}
