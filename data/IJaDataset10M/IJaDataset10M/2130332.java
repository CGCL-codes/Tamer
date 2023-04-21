package cn.vlabs.clb.ui.frameservice.folder;

import cn.vlabs.clb.ui.flex.PathDecoder;
import cn.vlabs.clb.ui.flex.PathEncoder;

public class FolderUtil {

    public static String decode(String path) {
        return PathDecoder.decode(path);
    }

    public static String encode(String path) {
        return PathEncoder.encode(path);
    }
}
