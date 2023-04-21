package com.oneandone.sushi;

import com.oneandone.sushi.fs.World;
import com.oneandone.sushi.metadata.Instance;
import com.oneandone.sushi.metadata.Type;
import com.oneandone.sushi.metadata.reflect.ReflectSchema;
import java.util.Properties;

public class PropertiesSample {

    /** Serialize object to xml and load the result back into an object */
    public static void main(String[] args) {
        Properties props;
        Instance<Obj> data;
        Obj obj;
        props = new Properties();
        props.setProperty("foo.number", "2");
        props.setProperty("foo.string", "hi");
        data = TYPE.loadProperties(props, "foo");
        obj = data.get();
        System.out.println("object:\n" + obj);
        obj.number = 3;
        System.out.println("properties:\n" + data.toProperties("bar"));
    }

    private static final Type TYPE = new ReflectSchema(new World()).type(Obj.class);

    public static class Obj {

        public int number;

        public String string;

        public Obj() {
            this(0, "");
        }

        public Obj(int number, String string) {
            this.number = number;
            this.string = string;
        }

        @Override
        public String toString() {
            return "number=" + number + ",string=" + string;
        }
    }
}
