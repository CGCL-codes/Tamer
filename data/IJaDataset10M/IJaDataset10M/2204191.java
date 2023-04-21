package cn.webwheel.di;

import java.util.ArrayList;
import java.util.List;

/**
 * 当类型无法创建时，此异常被抛出。通过{@link #getKeys()}方法返回正在被依次创建的类型。
 */
public class DIException extends RuntimeException {

    private static final long serialVersionUID = 1531115134314493834L;

    private List<Key> keys = new ArrayList<Key>();

    public DIException(String message, Key key) {
        super(message);
        keys.add(key);
    }

    public DIException(String message, Key key, Throwable cause) {
        super(message, cause);
        keys.add(key);
    }

    public void add(Key key) {
        keys.add(key);
    }

    /**
     * 返回正在被创建的类型
     * @return 正在被创建的类型
     */
    public List<Key> getKeys() {
        return keys;
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        StringBuilder sb = msg == null ? new StringBuilder() : new StringBuilder(msg).append('\n');
        sb.append("keys:");
        for (Key key : keys) {
            sb.append('\n').append(key);
        }
        return sb.toString();
    }
}
