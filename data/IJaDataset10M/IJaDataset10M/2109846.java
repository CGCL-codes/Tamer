package org.nutz.mvc.view;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.nutz.el.El;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.segment.CharSegment;
import org.nutz.lang.segment.Segment;
import org.nutz.lang.util.Context;
import org.nutz.mvc.Loading;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.config.AtMap;
import org.nutz.mvc.impl.processor.ViewProcessor;

/**
 * @author mawm(ming300@gmail.com)
 * @author wendal(wendal1985@gmail.com)
 */
public abstract class AbstractPathView implements View {

    private Segment dest;

    private Map<String, El> exps;

    public AbstractPathView(String dest) {
        if (null != dest) {
            this.dest = new CharSegment(Strings.trim(dest));
            this.exps = new HashMap<String, El>();
            for (String key : this.dest.keys()) {
                this.exps.put(key, new El(key));
            }
        }
    }

    protected String evalPath(HttpServletRequest req, Object obj) {
        if (null == dest) return null;
        Context context = Lang.context();
        Context expContext = createContext(req, obj);
        for (Entry<String, El> en : exps.entrySet()) context.set(en.getKey(), en.getValue().eval(expContext));
        return Strings.trim(this.dest.render(context).toString());
    }

    /**
	 * 为一次 HTTP 请求，创建一个可以被表达式引擎接受的上下文对象
	 * 
	 * @param req
	 *            HTTP 请求对象
	 * @param obj
	 *            入口函数的返回值
	 * @return 上下文对象
	 */
    @SuppressWarnings("unchecked")
    public static Context createContext(HttpServletRequest req, Object obj) {
        Context context = Lang.context();
        Object globalContext = req.getSession().getServletContext().getAttribute(Loading.CONTEXT_NAME);
        if (globalContext != null) {
            context.putAll((Context) globalContext);
        }
        Map<String, Object> a = new HashMap<String, Object>();
        for (Enumeration<String> en = req.getAttributeNames(); en.hasMoreElements(); ) {
            String tem = en.nextElement();
            a.put(tem, req.getAttribute(tem));
        }
        context.set("a", a);
        Map<String, String> p = new HashMap<String, String>();
        for (Object o : req.getParameterMap().keySet()) {
            String key = (String) o;
            String value = req.getParameter(key);
            p.put(key, value);
            context.set(key, value);
        }
        context.set("p", p);
        Map<String, String> u = new HashMap<String, String>();
        AtMap at = Mvcs.getAtMap(req.getSession().getServletContext());
        if (at != null) {
            for (Object o : at.keys()) {
                String key = (String) o;
                u.put(key, at.get(key));
            }
            context.set("u", u);
        }
        if (null != obj) context.set(ViewProcessor.DEFAULT_ATTRIBUTE, obj);
        return context;
    }
}
