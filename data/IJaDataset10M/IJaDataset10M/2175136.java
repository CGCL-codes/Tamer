package org.monet.kernel.model.definition;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "report")
public class ReportDefinition extends AnalyticalDefinition {

    public static class Use {

        @Attribute(name = "node")
        protected String node;

        public String getNode() {
            return node;
        }
    }

    public static class Render {

        public enum EngineEnumeration {

            MONET, PENTAHO
        }

        @Attribute(name = "engine")
        protected EngineEnumeration engine;

        @Attribute(name = "template")
        protected String template;

        public EngineEnumeration getEngine() {
            return engine;
        }

        public String getTemplate() {
            return template;
        }
    }

    @Element(name = "use", required = false)
    protected Use use;

    @Element(name = "render", required = false)
    protected Render render;

    public Use getUse() {
        return use;
    }

    public Render getRender() {
        return render;
    }
}
