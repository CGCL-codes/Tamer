package im.one;

import java.util.ArrayList;
import java.util.List;
import net.sf.maple.factory.TypedFactory;

public class SimpleFactory extends TypedFactory {

    public static final SimpleFactory INST = new SimpleFactory();

    public final Producer<Integer> limit = singleton(Integer.class, 3);

    @SuppressWarnings("unchecked")
    public final Producer<List> lst = singleton(List.class, ArrayList.class);

    public final Producer<Source> single = singleton(Source.class, DataSourceImpl.class, prop("base", "b"), prop("limit", limit.get()), prop("list", lst.get()), destroyMethod("destroy"));

    public final Producer<Source> prot = prototype(Source.class, DataSourceImpl.class, prop("base", "b"), prop("limit", limit.get()), prop("list", lst.get()));

    {
        DataSourceImpl dsi = new DataSourceImpl();
        dsi.setBase("b");
        dsi.setLimit(limit.get());
        dsi.setList(lst.get());
        explicit = singleton(Source.class, dsi);
    }

    public final Producer<Source> explicit;
}
