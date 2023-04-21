package com.gm.core.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.gm.core.collections.ArrayStack;
import com.gm.core.collections.CharStack;
import com.gm.core.collections.CollectionUtils;
import com.gm.core.lang.BooleanUtils;
import com.gm.core.lang.NumberUtils;
import com.gm.core.lang.StringUtils;
import com.gm.core.lang.builder.ToStringBuilder;

/**
 * <p>
 * A tool class for split JSON string
 * </p>
 * <p>
 * etc. String str =
 * "{\"err_code\":0,\"err_msg\":\"\",\"res\":{\"user_id\":16000,\"icon32\":\"http://210.193.49.105/avatar/unknown_32x32.png\",\"icon48\":\"http://210.193.49.105/avatar/unknown_48x48.png\",\"icon64\":\"http://210.193.49.105/avatar/unknown_64x64.png\",\"icon200\":\"http://210.193.49.105/avatar/unknown_96x96.png\"}}"
 * ; JSON json = JSON.fromString(str);
 * System.out.println(json.getString("err_code"));
 * System.out.println(json.getString("err_msg"));
 * System.out.println(json.getString("res.user_id")); JSON subJson =
 * json.getJSON("res"); System.out.println(subJson.getString("user_id"));
 * System.out.println(json); System.out.println(subJson);
 * 
 * result: 0
 * 
 * 16000 16000
 * {res={user_id:16000,icon32:http://210.193.49.105/avatar/unknown_32x32
 * .png,icon48
 * :http://210.193.49.105/avatar/unknown_48x48.png,icon64:http://210.193
 * .49.105/avatar
 * /unknown_64x64.png,icon200:http://210.193.49.105/avatar/unknown_96x96.png},
 * err_msg=, res.icon48=http://210.193.49.105/avatar/unknown_48x48.png,
 * err_code=0, res.icon200=http://210.193.49.105/avatar/unknown_96x96.png,
 * res.icon64=http://210.193.49.105/avatar/unknown_64x64.png,
 * res.icon32=http://210.193.49.105/avatar/unknown_32x32.png, res.user_id=16000}
 * {icon200=http://210.193.49.105/avatar/unknown_96x96.png,
 * icon64=http://210.193.49.105/avatar/unknown_64x64.png, user_id=16000,
 * icon48=http://210.193.49.105/avatar/unknown_48x48.png,
 * icon32=http://210.193.49.105/avatar/unknown_32x32.png}
 * </p>
 * 
 * 
 */
public class JSON {

    private Map<String, Object> map;

    static class Item {

        private Item() {
        }

        String key;

        String value;

        int type;

        public static final int SIMPLE = 1;

        public static final int ARRAY = 2;

        public static final int OBJECT = 3;

        public static Item fromString(String str) {
            Item item = new Item();
            char[] cs = str.toCharArray();
            char preChar = ' ';
            StringBuilder sbKey = new StringBuilder();
            StringBuilder sbValue = new StringBuilder();
            int bracketsNum = 0;
            boolean forKey = true;
            for (char c : cs) {
                if (c == ':') {
                    if (preChar != '\\') {
                        if (forKey) {
                            forKey = false;
                        } else {
                            sbValue.append(c);
                        }
                    } else {
                        if (forKey) {
                            sbKey.append(c);
                        }
                    }
                } else {
                    if (forKey) {
                        sbKey.append(c);
                    } else {
                        sbValue.append(c);
                        if (c == '[') {
                            bracketsNum++;
                        } else if (c == '{') {
                            item.type = Item.OBJECT;
                        }
                    }
                }
                preChar = c;
            }
            if (item.type == 0) {
                if (bracketsNum == 0) {
                    item.type = Item.SIMPLE;
                } else if (bracketsNum == 1) {
                    item.type = Item.ARRAY;
                } else {
                    item.type = Item.OBJECT;
                }
            }
            item.key = sbKey.toString();
            item.value = sbValue.toString();
            return item;
        }

        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    private JSON() {
        this.map = new HashMap<String, Object>();
    }

    private JSON(Map<String, Object> map) {
        this.map = map;
    }

    /**
	 * <p>
	 * Construct JSON form an input JSON String
	 * </p>
	 * 
	 * @param str
	 * @return
	 */
    public static JSON fromString(String str) {
        JSON json = new JSON();
        json.split("", str);
        return json;
    }

    /**
	 * <p>
	 * Construct JSON form an input JSON string builder
	 * </p>
	 * 
	 * @param sb
	 * @return
	 */
    public static JSON fromStringBuilder(StringBuilder sb) {
        return fromString(sb.toString());
    }

    private void split(String prefix, String str) {
        List<Item> itemList = splitItems(str);
        for (Item item : itemList) {
            if (item.type == Item.SIMPLE) {
                this.map.put((prefix + item.key).trim(), item.value);
            } else if (item.type == Item.ARRAY) {
                this.map.put((prefix + item.key).trim(), splitJSONStringToArray(item.value));
            } else if (item.type == Item.OBJECT) {
                this.map.put((prefix + item.key).trim(), item.value);
                split(prefix + item.key + ".", item.value);
            }
        }
    }

    private static String[] splitJSONStringToArray(String str) {
        str = str.replace("[", "");
        str = str.replace("]", "");
        return StringUtils.split(str, ',', false);
    }

    private static List<Item> splitItems(String str) {
        List<Item> itemList = new ArrayList<Item>();
        str = str.replace("\"", "");
        char[] cs = str.toCharArray();
        CharStack stack = new CharStack();
        ArrayStack<Character> astack = new ArrayStack<Character>();
        for (char c : cs) {
            if (c == '{' || c == '[') {
                astack.push(c);
                stack.push(c);
            } else if (c == '}' || c == ']') {
                astack.pop();
                if (astack.size() == 0) {
                    itemList.add(Item.fromString(stack.popFromLastUntil('{', '[')));
                } else {
                    stack.push(c);
                }
            } else if (c == ',') {
                if (astack.size() == 1) {
                    itemList.add(Item.fromString(stack.popFromLastUntil('{')));
                } else {
                    stack.push(c);
                }
            } else {
                stack.push(c);
            }
        }
        return itemList;
    }

    /**
	 * <p>
	 * Get sub JSON by appointing prefix
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
    public JSON getJSON(String key) {
        return new JSON(CollectionUtils.subMap(this.map, new HashMap<String, Object>(), key + "."));
    }

    /**
	 * <p>
	 * Get Object from JSON
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
    public Object getObject(String key) {
        return this.map.get(key);
    }

    /**
	 * <p>
	 * Get String from JSON
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
    public String getString(String key) {
        Object obj = getObject(key);
        return obj != null ? obj.toString() : null;
    }

    /**
	 * <p>
	 * Get Integer form JSON
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
    public int getInt(String key) {
        return NumberUtils.toIntStrict(getString(key));
    }

    /**
	 * <p>
	 * Get Double form JSON
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
    public double getDouble(String key) {
        return NumberUtils.toDoubleStrict(getString(key));
    }

    /**
	 * <p>
	 * GET float from JSON
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
    public float getFloat(String key) {
        return NumberUtils.toFloatStrict(getString(key));
    }

    /**
	 * <p>
	 * Get boolean from JSON
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
    public boolean getBoolean(String key) {
        return BooleanUtils.toBooleanStrict(getString(key));
    }

    /**
	 * <p>
	 * Get String array from JSON
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
    public String[] getStringArray(String key) {
        String[] strs = (String[]) this.getObject(key);
        return strs;
    }

    /**
	 * <p>
	 * Get integer array from JSON
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
    public int[] getIntArray(String key) {
        String[] strs = getStringArray(key);
        return NumberUtils.toIntArray(strs);
    }

    public String toString() {
        return this.map.toString();
    }
}
