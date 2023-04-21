package net.mikaboshi.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 複数のオブジェクトの個数を記憶するカウンタ。
 * このクラスでは同期が行われるため、マルチスレッドで使用されても状態は保障される。
 * 
 * @author Takuma Umezawa
 *
 */
public class MultiCounter {

    private ConcurrentHashMap<Object, int[]> map = new ConcurrentHashMap<Object, int[]>();

    private final int defaultValue;

    /**
	 * カウンタのデフォルト値を0に指定するコンストラクタ。
	 */
    public MultiCounter() {
        this(0);
    }

    /**
	 * カウンタのデフォルト値を明示的に指定するコンストラクタ。
	 * @param defaultValue
	 */
    public MultiCounter(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
	 * カウンタを1増加する。
	 * @param key
	 */
    public void increment(Object key) {
        increment(key, 1);
    }

    /**
	 * カウンタを指定値分、増加する。
	 * @param key
	 * @param n カウンタの増分（負数ならば減算する）
	 */
    public synchronized void increment(Object key, int n) {
        int[] count = this.map.get(key);
        if (count == null) {
            this.map.put(key, new int[] { this.defaultValue + n });
        } else {
            count[0] += n;
        }
    }

    /**
	 * カウンタを初期化する。
	 * @param key
	 */
    public synchronized void reset(Object key) {
        if (this.map.containsKey(key)) {
            this.map.put(key, new int[] { this.defaultValue });
        }
    }

    /**
	 * 現在のカウンタの値を取得する。<br/>
	 * このメソッドに関しては、同期を行わない。つまり、このメソッドを実行中にカウンタが更新された場合、変更前の値が取得される可能性がある。
	 * 
	 * @param key
	 * @return
	 */
    public int getCount(Object key) {
        int[] count = this.map.get(key);
        if (count == null) {
            return this.defaultValue;
        } else {
            return count[0];
        }
    }
}
