package shake.context;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import shake.servlet.ShakeServletContext;

public class HotDeploySeriarizableObject implements Serializable {

    Map<String, Object> map;

    /**
	 * HotDeploy�̃N���X���[�_��̃N���X��ContextClassLoader���猩���Ȃ��̂ŁA ClassLoader��ύX���ēǂݍ���
	 * 
	 * @return
	 * @throws ObjectStreamException
	 */
    public Object readResolve() throws ObjectStreamException {
        try {
            String name = (String) map.get("class.name");
            ClassLoader loader = ShakeServletContext.getScanClassLoader();
            Class clazz = Class.forName(name, false, loader);
            Object obj = ShakeServletContext.getInjector().getInstance(clazz);
            for (Field f : clazz.getDeclaredFields()) {
                if (map.containsKey(f.getName())) {
                    f.setAccessible(true);
                    f.set(obj, map.get(f.getName()));
                    f.setAccessible(false);
                }
            }
            return name;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public HotDeploySeriarizableObject(Map<String, Object> map) {
        super();
        this.map = map;
    }
}
