package gnu.classpath.examples.management;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryManagerMXBean;
import java.util.Arrays;
import java.util.Iterator;

public class TestMemoryManager {

    public static void main(String[] args) {
        Iterator beans = ManagementFactory.getMemoryManagerMXBeans().iterator();
        while (beans.hasNext()) {
            MemoryManagerMXBean bean = (MemoryManagerMXBean) beans.next();
            System.out.println("Bean: " + bean);
            System.out.println("Name: " + bean.getName());
            System.out.println("Memory pool names: " + Arrays.toString(bean.getMemoryPoolNames()));
            System.out.println("Is valid: " + (bean.isValid() ? "yes" : "no"));
        }
    }
}
