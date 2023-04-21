package shake.context;

import java.lang.reflect.Field;
import java.util.HashMap;
import shake.annotation.Component;
import shake.annotation.Debug;
import shake.util.Reflections;
import com.google.inject.Singleton;

@Debug
@Singleton
@Component(type = SeriarizableObjectFactory.class)
public class HotDeploySeriarizableObjectFactory extends SeriarizableObjectFactory {

    @Override
    public Object create(SeriarizableObject obj) {
        try {
            Class clazz = Reflections.unwrapClass(obj);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("class.name", clazz.getName());
            System.out.println(clazz);
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                map.put(f.getName(), f.get(obj));
                f.setAccessible(false);
            }
            return new HotDeploySeriarizableObject(map);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
