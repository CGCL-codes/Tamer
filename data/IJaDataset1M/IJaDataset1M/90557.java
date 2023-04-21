package org.jiopi.ibean.kernel;

import org.jiopi.ibean.share.ShareUtil.ResourceUtil;

/**
 * 
 * version 应当为 num.num.num.num 的形式
 * @since 2010.4.19
 *
 */
public class Version implements Comparable<Version> {

    public final String version;

    public Version(String version) {
        if (version == null || "".equals(version)) version = "-1"; else if (!"-1".equals(version) && !ResourceUtil.isCorrectVersion(version)) throw new IllegalArgumentException("version format is incorrect : " + version);
        this.version = version;
    }

    /**
	 * 
	 * 判定当前version是否兼容给定版本
	 * 
	 * 如 当前版本 1.2 兼容 给定版本 1.2.1
	 * 
	 * @param version
	 * @return
	 */
    public boolean isCompatible(Version version) {
        if ("-1".equals(this.version)) return true;
        if ("-1".equals(version.version)) return false;
        return version.version.startsWith(this.version);
    }

    /**
	 * 比较版本大小
	 * 从第一位开始依次比较,相同位数字大的版本大
	 * 版本位数长的版本大
	 * 即 1.2.3>1.2.2 1.2.3>1.2
	 * 
	 * 当前版本大,返回 1
	 * 当前版本小,返回-1
	 * 相同,返回 0
	 * 
	 * 自动兼容版本最大
	 * 
	 * @return
	 */
    public int compareTo(Version version) {
        if (version.equals(version.version)) return 0;
        if ("-1".equals(version)) return 1;
        if ("-1".equals(version.version)) return -1;
        String[] myVersion = this.version.split("\\.");
        String[] otVersion = version.version.split("\\.");
        int maxLength = Math.max(myVersion.length, otVersion.length);
        for (int i = 0; i < maxLength; i++) {
            if (myVersion.length == i || otVersion.length == i) {
                if (myVersion.length == otVersion.length) return 0; else if (myVersion.length == i) return -1; else return 1;
            }
            int my = Integer.parseInt(myVersion[i]);
            int ot = Integer.parseInt(otVersion[i]);
            if (my > ot) return 1; else if (my < ot) return -1;
        }
        return 0;
    }

    public String toString() {
        return "[Version : " + version + "]";
    }
}
