package lslplus.sim;

import com.thoughtworks.xstream.XStream;

public class SimParamDefinition {

    public abstract static class SimParamType {

        public abstract String getControlID();
    }

    public static class SimParamPrim extends SimParamType {

        public String getControlID() {
            return "prim";
        }
    }

    public static class SimParamAvatar extends SimParamType {

        public String getControlID() {
            return "avatar";
        }
    }

    public static class SimParamObject extends SimParamType {

        public String getControlID() {
            return "object";
        }
    }

    public static class SimParamKey extends SimParamType {

        public String getControlID() {
            return "any-key";
        }
    }

    public static class SimParamScript extends SimParamType {

        public String getControlID() {
            return "script";
        }
    }

    public static class SimParamValue extends SimParamType {

        private String valueType;

        public String getValueType() {
            return valueType;
        }

        public String getControlID() {
            return "expression-" + valueType;
        }
    }

    private String name;

    private String description;

    private SimParamType type;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public SimParamType getType() {
        return type;
    }

    public String getControlID() {
        return getType().getControlID();
    }

    public static void configureXStream(XStream xstream) {
        xstream.alias("param", SimParamDefinition.class);
        xstream.aliasType("prim", SimParamPrim.class);
        xstream.aliasType("avatar", SimParamAvatar.class);
        xstream.aliasType("object", SimParamObject.class);
        xstream.aliasType("script", SimParamScript.class);
        xstream.aliasType("any-key", SimParamKey.class);
        xstream.aliasType("value", SimParamValue.class);
    }
}
