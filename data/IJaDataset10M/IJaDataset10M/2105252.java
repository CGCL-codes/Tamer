package jacky.lanlan.song.closure;

/**
 * 包装“处理业务逻辑，然后返回一个值”的“闭包”。
 * @author Jacky.Song
 */
public interface ReturnableHandler<I, O> {

    /**
	 * 处理输入的值，然后返回一个结果值。
	 * 
	 * @param in
	 *          输入的值
	 * @return 结果值
	 */
    O doWith(I in);
}
