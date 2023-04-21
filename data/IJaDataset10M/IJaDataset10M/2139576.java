package com.jxva.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author  The Jxva Framework
 * @since   1.0
 * @version 2008-11-27 09:27:01 by Jxva
 */
public abstract class TplUtil {

    /**
	 * 替换模板文件参数值
	 * @param str
	 * @param map
	 * @return
	 */
    public static String replaceParams(String str, Map<String, String> map) {
        for (String key : map.keySet()) {
            str = str.replaceAll("\\$\\{" + key + "\\}", map.get(key) == null ? "" : map.get(key));
        }
        return str;
    }

    /**
	 * 生成新的文件
	 * @param tpl
	 * @param newfile
	 * @param map
	 * @return
	 */
    public static boolean createFile(String tpl, String newFileName, Map<String, String> map) {
        return NIOUtil.write(new File(newFileName), replaceParams(FileUtil.read(new File(tpl)), map));
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("title", "测试一下");
        map.put("content", "晕死吧");
        createFile("tpl.htm", "new.htm", map);
    }
}
