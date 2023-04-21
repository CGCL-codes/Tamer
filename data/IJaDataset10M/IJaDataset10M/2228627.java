package com.mkk.kenji1016.util;

import org.apache.struts2.ServletActionContext;
import org.springframework.util.StringUtils;
import java.util.*;

/**
 * User: mkk
 * Date: 11-7-5
 * Time: 下午9:55
 * <p/>
 * Application tools
 */
public class ApplicationUtil {

    /**
     * Create a new List(ArrayList).
     *
     * @param <T>T
     * @return List
     */
    public static <T> List<T> createList() {
        return new ArrayList<T>();
    }

    /**
     * Create a new List(ArrayList).
     *
     * @param <T>T
     * @param size List size
     * @return List
     */
    public static <T> List<T> createList(int size) {
        return new ArrayList<T>(size);
    }

    /**
     * Create a new Map(HashMap).
     *
     * @param <K> K
     * @param <V> V
     * @return Map
     */
    public static <K, V> Map<K, V> createMap() {
        return new HashMap<K, V>();
    }

    /**
     * Create a new Map(HashMap).
     *
     * @param <K>  K
     * @param <V>  V
     * @param size Map size
     * @return Map
     */
    public static <K, V> Map<K, V> createMap(int size) {
        return new HashMap<K, V>(size);
    }

    /**
     * Get request remote ip address
     *
     * @return Ip address
     */
    public static String remoteIp() {
        return ServletActionContext.getRequest().getRemoteAddr();
    }

    /**
     * Check text whether or not number
     *
     * @param numberAsText NumberAsText
     * @return True is number
     */
    public static boolean isNumber(String numberAsText) {
        return StringUtils.hasText(numberAsText) && numberAsText.matches("^\\d+$");
    }

    /**
     * Get query paging start value.
     *
     * @param page page
     * @param rows rows
     * @return start number
     */
    public static int getPageStart(int page, int rows) {
        return (page - 1) * rows;
    }

    /**
     * Check whether or not TXT file.
     *
     * @param fileName File name
     * @return true is txt file,otherwise false
     */
    public static boolean isTxtFile(String fileName) {
        if (StringUtils.hasText(fileName)) {
            int index = fileName.lastIndexOf(".") + 1;
            String suffix = fileName.substring(index);
            return "txt".equalsIgnoreCase(suffix);
        }
        return false;
    }

    /**
     * Check whether or not Excel file.
     *
     * @param fileName File name
     * @return true is excel file,otherwise false
     */
    public static boolean isExcelFile(String fileName) {
        if (StringUtils.hasText(fileName)) {
            int index = fileName.lastIndexOf(".") + 1;
            String suffix = fileName.substring(index);
            return "xls".equalsIgnoreCase(suffix) || "xlsx".equalsIgnoreCase(suffix);
        }
        return false;
    }

    /**
     * Get one random text.
     * from {@link UUID}
     *
     * @return random text
     */
    public static String random() {
        return UUID.randomUUID().toString();
    }
}
