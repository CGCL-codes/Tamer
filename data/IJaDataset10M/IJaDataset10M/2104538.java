package jacky.lanlan.song.pattern.visitor;

/**
 * 代表 Visitor 模式的访问者。
 * @author Jacky.Song
 * @param <T> 需要访问的对象类型
 */
public interface Visitor<T extends Visitable<?>> {

    /**
	 * 访问指定对象。
	 * @param visitable 可访问的对象
	 */
    void visit(T visitable);
}
