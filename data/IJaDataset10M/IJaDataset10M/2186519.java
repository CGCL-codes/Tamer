package net.sf.maple.webapp.std;

import java.lang.reflect.Field;
import java.util.Map;
import net.sf.maple.misc.Maps;
import net.sf.maple.misc.Pair;
import net.sf.maple.webapp.Context;
import net.sf.maple.webapp.Controller;
import net.sf.maple.webapp.Dispatcher;
import net.sf.maple.webapp.View;

public abstract class StdController<I, O> implements Controller {

    public View run(Dispatcher d, Context ctx) {
        try {
            Pair<View, O> pair = runImpl(d, ctx);
            Map<String, Object> m = Maps.fromObject(pair.second);
            m.put("success", "true");
            ctx.assignResult(m);
            return pair.first;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract Pair<View, O> execute(Dispatcher d, Context ctx, I in) throws Exception;

    protected abstract I create() throws Exception;

    private Pair<View, O> runImpl(Dispatcher d, Context ctx) throws Exception {
        I inf = create();
        for (Field f : inf.getClass().getFields()) {
            Class<?> ft = f.getType();
            Object fv = f.get(inf);
            if (String.class.equals(ft)) fv = Maps.get(ctx.params, f.getName(), (String) fv); else if (Boolean.class.equals(ft) || boolean.class.equals(ft)) fv = Maps.get(ctx.params, f.getName(), (Boolean) fv); else if (Integer.class.equals(ft) || int.class.equals(ft)) fv = Maps.get(ctx.params, f.getName(), (Integer) fv); else throw new RuntimeException("field " + f + " not supported");
            f.set(inf, fv);
        }
        Pair<View, O> out = execute(d, ctx, inf);
        return out;
    }
}
