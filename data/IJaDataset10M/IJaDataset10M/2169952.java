package neo.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import neo.core.Constants;

public class CommonUtil {

    /**
	 * 将queryForList结果List转换为Object[]
	 * @param list queryForList的结果
	 * @param key List中Map里转换为String的Key
	 * @return
	 */
    public static Object[] ListToObjectArray(List list, String key) {
        int listSize = list.size();
        if (listSize > 0) {
            Object[] objArray = new Object[listSize];
            for (int i = 0; i < listSize; i++) {
                objArray[i] = ((Map) list.get(i)).get(key);
            }
            return objArray;
        } else {
            return null;
        }
    }

    /**
	 * 加密密码
	 */
    public static String makePassword(String password) {
        StringBuffer key = new StringBuffer(30);
        byte passChar[] = password.getBytes();
        for (int i = 0; i < passChar.length; i++) {
            key.append(String.valueOf(passChar[i] ^ 0xff));
        }
        return key.toString();
    }

    /**
	 * 解密密码
	 */
    public static String getPassword(String key) {
        StringBuffer password = new StringBuffer();
        int sum = key.length() / 3;
        int index = 0;
        for (int i = 0; i < sum; i++) {
            char c = (char) (Integer.parseInt(key.substring(index, index + 3)) ^ 0xff);
            password.append(c);
            index += 3;
        }
        return password.toString();
    }

    /**
	 * 将Web请求Map中的Key转换大小写
	 */
    public static Map mapKeyChangeCase(Map m, String ul) {
        Map newMap = new HashMap(m.size());
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if ("Upper".equals(ul)) {
                newMap.put(key.toUpperCase(), m.get(key));
            } else {
                newMap.put(key.toLowerCase(), m.get(key));
            }
        }
        return newMap;
    }

    /**
	 * 将给定的字符串使用UTF-8进行URL编码
	 */
    public static String toUrl(String src) {
        return toUrl(src, Constants.ENCODING);
    }

    /**
	 * 将给定的字符串进行URL编码
	 */
    public static String toUrl(String src, String enc) {
        String target = null;
        try {
            target = URLEncoder.encode(src, enc);
        } catch (UnsupportedEncodingException e) {
            target = src;
        }
        return target;
    }

    /**
	 * 分类时在类Id前补零
	 */
    public static String fillZero(String nextId, String parentId) {
        int len = parentId.length() + 3;
        if ("0".equals(parentId)) {
            len = 3;
        }
        int srcLen = nextId.length();
        if (srcLen >= len) {
            return nextId;
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < len - srcLen; i++) {
                sb.append("0");
            }
            sb.append(nextId);
            return sb.toString();
        }
    }

    /**
	 * 将数据库中查询出的简单的List转化为Map
	 */
    public static Map simpleListToMap(List list, String key, String value) {
        Map m = new LinkedHashMap();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Map item = (Map) it.next();
            m.put(item.get(key), item.get(value));
        }
        return m;
    }

    /**
	 * 按字节切分字符串
	 */
    public static String substring(String str, int toCount, String suffix) {
        int reInt = 0;
        StringBuffer reStr = new StringBuffer();
        if (str == null) return "";
        char[] tempChar = str.toCharArray();
        for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {
            String s1 = String.valueOf(tempChar[kk]);
            byte[] b = s1.getBytes();
            reInt += b.length;
            if (toCount >= reInt) {
                reStr.append(tempChar[kk]);
            }
        }
        if (suffix != null && !"".equals(suffix)) {
            if (toCount == reInt || (toCount == reInt - 1)) reStr.append(suffix);
        }
        return reStr.toString();
    }

    /**
	 * 将数据库中查询出的简单的List转化为已主键为key的Map
	 */
    public static Map listToMap(List list, String key) {
        Map m = new LinkedHashMap();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Map item = (Map) it.next();
            m.put(item.get(key).toString(), item);
        }
        return m;
    }

    /**
	 * 将数据库查询出的List转换为JSON格式字符串
	 */
    public static String listToJson(List list) {
        JSONArray array = new JSONArray();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Object tmp = list.get(i);
                if (tmp instanceof Map) {
                    Map srcMap = (Map) tmp;
                    JSONObject obj = new JSONObject();
                    Iterator it = srcMap.keySet().iterator();
                    Object key = null;
                    while (it.hasNext()) {
                        key = it.next();
                        Object value = srcMap.get(key);
                        if (value instanceof java.util.Date) {
                            value = value.toString();
                        }
                        obj.put(key.toString(), value);
                    }
                    array.add(obj);
                } else {
                    array.add(tmp);
                }
            }
        }
        return array.toString();
    }

    /**
	 * 获取字符串的MD5加密结果
	 */
    public static String getMD5ofStr(String str) {
        return MD5.getMD5ofStr(str);
    }
}
