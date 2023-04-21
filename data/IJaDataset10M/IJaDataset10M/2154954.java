package jacky.lanlan.song.closure;

/**
 * 包装“处理业务逻辑”的“闭包”。
 * @author Jacky.Song
 */
public interface Handler<T> {

    /**
	 * 包装处理业务逻辑的过程。
	 * @param data 处理业务逻辑需要的数据
	 */
    void doWith(T data);
}
