package jacky.lanlan.song.pattern;

/**
 * 复合接口，表达“复合模式”语意。
 * @param <T> 运用“复合模式”的类型
 * 
 * @author Jacky.Song
 */
public interface Composite<T> {

    /**
	 * 把很多部分整合在一起，当作一个整体。
	 * @param parts 组成整体的“部分”，和整体是相同类型
	 * @return 整合了parts的一个“整体”
	 */
    T combine(T... parts);
}
