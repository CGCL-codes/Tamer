package org.eralyautumn.message.format;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import com.yxg.common.util.Constants;

/**
 * 标准通讯消息模型
 * 
 * @author <a href="mailto:fmlou@163.com">HongzeZhang</a>
 * 
 * @version 1.0
 * 
 *          2010-1-12
 */
@SuppressWarnings("unchecked")
public class GameMessage {

    /**
	 * 接收者与消息体间隔标识
	 */
    public static final String BETWEEN_TYPE_AND_MESSAGE_KEY = "@";

    /**
	 * 接受者类与方法间隔标识
	 */
    public static final String BETWEEN_CLASS_AND_METHOD_KEY = "|";

    /**
	 * 中间服务转发消息标识
	 */
    public static final String MIDDLE_SERVER_MESSAGE_KEY = "#";

    /**
	 * 信息接受者标识
	 */
    private String receiverKey;

    /**
	 * 通信数据
	 */
    public MessageData data = null;

    /**
	 * 用户登录唯一标识key
	 */
    private String sessionKey;

    /**
	 * 是否为中转服务
	 */
    private boolean middleServer = false;

    private static Map<Long, GameMessage> gameMessageMap = new ConcurrentHashMap<Long, GameMessage>();

    private GameMessage() {
    }

    private GameMessage initData() {
        data = new MessageData();
        return this;
    }

    /**
	 * 解析消息格式,构造消息对象
	 * 
	 * @param message
	 *            标准通信消息格式的字符串
	 */
    private void initGameMessage(String message) {
        if (message == null || message.equals("")) throw new NullPointerException("Error, message is null.");
        if (!message.contains(BETWEEN_TYPE_AND_MESSAGE_KEY)) throw new NullPointerException("Error, message '" + message + "' don't contains '@'.");
        if (message.startsWith(MIDDLE_SERVER_MESSAGE_KEY)) {
            this.middleServer = true;
            message = message.substring(1);
        }
        int pos1 = message.indexOf(BETWEEN_TYPE_AND_MESSAGE_KEY);
        this.receiverKey = message.substring(0, pos1);
        if (pos1 == message.length() - 1) {
            data = new MessageData();
            return;
        }
        String messageData = message.substring(pos1 + 1);
        try {
            data = new MessageData("[" + messageData + "]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
	 * 根据接受者标识构造对象
	 * 
	 * @param recieverKey
	 * @return
	 */
    public static GameMessage getInstance(String receiverKey) {
        return new GameMessage().initData().setReceiverKey(receiverKey);
    }

    /**
	 * 根据会话id和消息内容构造对象
	 * 
	 * @param sessionlId
	 * @param message
	 * @return
	 */
    public static GameMessage getInstance(long sessionlId, String message) {
        GameMessage gm = gameMessageMap.get(sessionlId);
        if (gm == null) {
            gm = new GameMessage();
            gameMessageMap.put(sessionlId, gm);
        }
        gm.initGameMessage(message);
        return gm;
    }

    /**
	 * 获取控制器类标识
	 * 
	 * @return
	 */
    public String getControllerKey() {
        if (receiverKey == null) throw new NullPointerException("Error, key is null.");
        return receiverKey.substring(0, receiverKey.indexOf(BETWEEN_CLASS_AND_METHOD_KEY));
    }

    /**
	 * 设置接收者标识
	 * 
	 * @param receiverKey
	 * @return
	 */
    public GameMessage setReceiverKey(String receiverKey) {
        if (receiverKey == null) throw new NullPointerException("key is null.");
        this.receiverKey = receiverKey;
        return this;
    }

    /**
	 * 获取消息类型
	 * 
	 * @return
	 */
    public String getReceiverKey() {
        return this.receiverKey;
    }

    /**
	 * 获取某个位置数据对象
	 * 
	 * @param index
	 *            位置
	 * @return
	 */
    public Object getObject(int index) {
        try {
            return data.get(index);
        } catch (JSONException e) {
            System.err.println("Error message=" + this.toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 获取某个位置int数组
	 * 
	 * @param index
	 *            位置
	 * @return
	 */
    public int[] getIntArray(int index) {
        try {
            org.json.JSONArray ja = (org.json.JSONArray) data.get(index);
            int[] array = new int[ja.length()];
            for (int i = 0; i < array.length; i++) {
                array[i] = ja.getInt(i);
            }
            return array;
        } catch (JSONException e) {
            System.err.println("Error message=" + this.toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 获取某个位置字符串
	 * 
	 * @param index
	 *            位置
	 * @return
	 */
    public String getString(int index) {
        try {
            return data.getString(index);
        } catch (JSONException e) {
            System.err.println("Error message=" + this.toString());
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 获取int型数值
	 * 
	 * @param index
	 *            位置
	 * @return
	 */
    public int getInt(int index) {
        try {
            return data.getInt(index);
        } catch (JSONException e) {
            System.err.println("Error message=" + this.toString());
            e.printStackTrace();
        }
        return 0;
    }

    /**
	 * 获取long型数值
	 * 
	 * @param index
	 *            位置
	 * @return
	 */
    public long getLong(int index) {
        try {
            return data.getLong(index);
        } catch (JSONException e) {
            System.err.println("Error message=" + this.toString());
            e.printStackTrace();
        }
        return 0;
    }

    /**
	 * 获取double型值
	 * 
	 * @param index
	 * @return
	 */
    public double getDouble(int index) {
        try {
            return (float) data.getDouble(index);
        } catch (JSONException e) {
            System.err.println("Error message=" + this.toString());
            e.printStackTrace();
        }
        return 0;
    }

    /**
	 * 加入字符串型数据
	 * 
	 * @param str
	 * @return
	 */
    public GameMessage append(String str) {
        data.put(str);
        return this;
    }

    /**
	 * 加入int型数据
	 * 
	 * @param str
	 * @return
	 */
    public GameMessage append(int number) {
        data.put(number);
        return this;
    }

    /**
	 * 加入long型数据
	 * 
	 * @param str
	 * @return
	 */
    public GameMessage append(long number) {
        data.put(number);
        return this;
    }

    public GameMessage appendObject(Object o) {
        data.put(o);
        return this;
    }

    /**
	 * 加入数组
	 * @param o
	 * @return
	 */
    public GameMessage append(Object... o) {
        for (Object object : o) {
            data.put(object);
        }
        return this;
    }

    public GameMessage append(Collection c) {
        if (c != null && c.size() > 0) {
            for (Object object : c) {
                data.put(object);
            }
        }
        return this;
    }

    /**
	 * 添加 toStringFun
	 * 
	 * @param c
	 * @param toStringFun
	 * @return
	 */
    public GameMessage append(Collection c, String toStringFun) {
        for (Object object : c) {
            try {
                object.getClass().getMethod("setToStringFun", String.class).invoke(object, toStringFun);
            } catch (Exception e) {
                e.printStackTrace();
            }
            data.put(object);
        }
        return this;
    }

    public GameMessage appendCollection(Collection c) {
        data.put(c);
        return this;
    }

    /**
	 * 添加 toStringFun
	 * 
	 * @param c
	 * @param toStringFun
	 * @return
	 */
    public GameMessage appendCollection(Collection c, String toStringFun) {
        for (Object object : c) {
            try {
                object.getClass().getMethod("setToStringFun", String.class).invoke(object, toStringFun);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        data.put(c);
        return this;
    }

    /**
	 * 加入double型数据
	 * 
	 * @param str
	 * @return
	 */
    public GameMessage append(double number) {
        try {
            data.put(number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
	 * 加入double型数据
	 * 
	 * @param str
	 * @return
	 */
    public GameMessage append(float number) {
        try {
            data.put(number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
	 * 加入字符串型数据
	 * 
	 * @param str
	 * @return
	 */
    public GameMessage append(int index, String str) {
        data.insert(index, str);
        return this;
    }

    /**
	 * 加入int型数据
	 * 
	 * @param str
	 * @return
	 */
    public GameMessage append(int index, int number) {
        data.insert(index, number);
        return this;
    }

    /**
	 * 加入集合数据
	 * 
	 * @param index
	 * @param c
	 * @return
	 */
    public GameMessage append(int index, Collection c) {
        data.insert(index, c);
        return this;
    }

    public GameMessage append(int index, Collection list, String field) {
        for (Object object : list) {
            try {
                Field f = object.getClass().getField(field);
                data.insert(index, f.get(object));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    /**
	 * 加入double型数据
	 * 
	 * @param str
	 * @return
	 */
    public GameMessage append(int index, double number) {
        data.insert(index, number);
        return this;
    }

    /**
	 * 加入double型数据
	 * 
	 * @param str
	 * @return
	 */
    public GameMessage append(int index, float number) {
        data.insert(index, number);
        return this;
    }

    /**
	 * 获取消息数组长度
	 * 
	 * @return
	 */
    public int length() {
        return data.length();
    }

    /**
	 * 删除元素
	 * 
	 * @param index
	 *            位置
	 */
    public void remove(int index) {
        MessageData newJa = new MessageData();
        for (int j = 0; j < data.length(); j++) {
            if (j != index) try {
                newJa.put(data.get(j));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.data = newJa;
    }

    /**
	 * 获取唯一标识key
	 * 
	 * @return
	 */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
	 * 设置唯一标识key
	 * 
	 * @param sessionKey
	 */
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    /**
	 * 设置为中转消息
	 * 
	 * @return
	 */
    public GameMessage setMiddleServer(boolean b) {
        this.middleServer = b;
        return this;
    }

    /**
	 * 获取是否是中转消息
	 * 
	 * @return
	 */
    public boolean getMiddleServer() {
        return this.middleServer;
    }

    /**
	 * 将消息转为字符串
	 */
    @Override
    public String toString() {
        StringBuilder msgStr = new StringBuilder(middleServer ? MIDDLE_SERVER_MESSAGE_KEY : "");
        msgStr.append(this.getReceiverKey()).append(BETWEEN_TYPE_AND_MESSAGE_KEY);
        String dataStr = data.toString();
        msgStr.append(dataStr.substring(1, dataStr.length() - 1)).append(Constants.MESSAGE_END_FLAG);
        return msgStr.toString().replaceAll("\"null\"|\"NULL\"|null|NULL", "\"\"");
    }

    /**
	 * 删除消息对象
	 * 
	 * @param l
	 */
    public static void removeMessage(long l) {
        gameMessageMap.remove(l);
    }

    /**
	 * 返回参数数量
	 * 
	 * @return
	 */
    public int size() {
        return this.data.length();
    }

    /**
	 * 判断是否为空
	 * 
	 * @return
	 */
    public boolean isEmpty() {
        return this.data.length() > 0 ? false : true;
    }

    public static void main(String[] args) throws JSONException {
        GameMessage gm = new GameMessage();
        gm.initGameMessage("0|1@[12,1],[22,33],4,233,'NULL'");
        System.out.println(gm.toString().replaceAll("\"null\"|\"NULL\"|null|NULL", "\"\""));
    }
}
