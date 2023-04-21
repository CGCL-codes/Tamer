package bufferings.ktr.wjr.shared.model.meta;

/**
 * A meta of WjrStoreMeta.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrStoreMeta {

    private static final WjrStoreMeta meta = new WjrStoreMeta();

    public static WjrStoreMeta meta() {
        return meta;
    }

    public final String classItems = "classItems";

    public final String methodItems = "methodItems";

    private WjrStoreMeta() {
    }
}
