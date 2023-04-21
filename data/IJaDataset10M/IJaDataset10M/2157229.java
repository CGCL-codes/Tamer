package test;

import java.io.StringWriter;
import com.serotonin.json.JsonContext;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonWriter;
import com.serotonin.json.convert.ThrowableSerializingConverter;

public class ExceptionTest {

    public static void main(String[] args) throws Exception {
        JsonContext context = new JsonContext();
        context.addConverter(new ThrowableSerializingConverter(), Throwable.class);
        StringWriter out = new StringWriter();
        JsonWriter writer = new JsonWriter(context, out);
        writer.setPrettyOutput(true);
        writer.writeObject(createException1());
        System.out.println(out);
        JsonReader reader = new JsonReader(context, out.toString());
        Throwable t = reader.read(Throwable.class);
        System.out.println("Great success!");
        System.out.println(t.getMessage());
        t.printStackTrace();
    }

    static Exception createException1() {
        return createException2();
    }

    static Exception createException2() {
        return createException3();
    }

    static Exception createException3() {
        return new RuntimeException("The outer exception", createException4());
    }

    static Exception createException4() {
        return new RuntimeException("The inner exception");
    }
}
