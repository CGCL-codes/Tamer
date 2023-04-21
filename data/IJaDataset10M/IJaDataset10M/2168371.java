package org.nutz.mvc.adaptor;

import java.lang.reflect.Type;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
import org.nutz.mvc.adaptor.injector.JsonInjector;
import org.nutz.mvc.annotation.Param;

/**
 * 假设，整个输入输入流，是一个 JSON 字符串
 * 
 * @author zozoh(zozohtnt@gmail.com)
 * @author wendal(wendal1985@gmail.com)
 */
public class JsonAdaptor extends PairAdaptor {

    @Override
    protected ParamInjector evalInjector(Type type, Param param) {
        if (param == null) return new JsonInjector(type, null);
        return super.evalInjector(type, param);
    }

    public Object getReferObject(ServletContext sc, HttpServletRequest req, HttpServletResponse resp, String[] pathArgs) {
        try {
            String str = Streams.readAndClose(Streams.utf8r(req.getInputStream()));
            return Json.fromJson(str);
        } catch (Exception e) {
            throw Lang.wrapThrow(e);
        }
    }
}
