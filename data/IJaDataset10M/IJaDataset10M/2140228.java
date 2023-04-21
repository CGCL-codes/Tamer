package com.common.util;

/**
 * 数组列表适用工具类
 * 
 * @author 孙树林
 * 
 */
public class ArraysUtils {

    /**
	 * 数组是否存在，且不为空
	 * 
	 * @param obj
	 * @return
	 */
    public static boolean empty(Object[] obj) {
        if (obj != null && obj.length > 0) {
            return false;
        }
        return true;
    }
}
