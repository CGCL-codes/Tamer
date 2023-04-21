package utils.binders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import play.data.binding.Global;
import play.data.binding.TypeBinder;

@Global
public class JsonObjectBinder implements TypeBinder<JsonObject> {

    public Object bind(String name, Annotation[] annotations, String value, Class actualClass, Type genericType) throws Exception {
        return new JsonParser().parse(value);
    }
}
