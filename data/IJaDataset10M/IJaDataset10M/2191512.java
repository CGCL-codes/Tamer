package jacky.lanlan.song.extension.vraptor2;

/**
 * Json 编码器，把用 {@code Json} 标注的 Logic Method 的返回值编码为Json字符串。
 * <p>
 * 注意，Encoder 是个 Singleton，必须线程安全。
 * 
 * @author Jacky.Song
 */
public interface JsonEncoder {

    /**
	 * 编码给定值为Json格式字符串。
	 * 
	 * @param value
	 *          要编码的值
	 * @return Json格式字符串
	 */
    String encode(Object value);
}
