package com.googlecode.httl.test.cases;

import java.io.Writer;
import java.util.Map;
import com.googlecode.httl.Engine;
import com.googlecode.httl.Template;

/**
 * HttlCase
 * 
 * @author Liang Fei (liangfei0201 AT gmail DOT com)
 */
public class HttlCase implements Case {

    public void count(String name, Map<String, Object> context, Writer writer, Writer ignore, int times, Counter counter) throws Exception {
        counter.beginning();
        Engine engine = Engine.getEngine();
        counter.initialized();
        Template template = engine.getTemplate(name + ".httl");
        counter.compiled();
        template.render(context, writer);
        counter.executed();
        for (int i = times; i >= 0; i--) {
            template.render(context, ignore);
        }
        counter.finished();
    }
}
