package tests;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import org.junit.Test;

public class MMSTest {

    @Test
    public void day() {
    }

    public static void main(String[] args) {
        try {
            File root = new File("C:/TOOLS/workspace/projects/INKIUM_FOREST/INSTALL/mms/lib");
            File[] files = root.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    if (name.endsWith(".jar")) return true;
                    return false;
                }
            });
            int size = files.length;
            URL[] urls = new URL[size];
            for (int i = 0; i < size; i++) {
                try {
                    urls[i] = files[i].toURL();
                    System.out.println("##" + urls[i]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            ClassLoader classloader = URLClassLoader.newInstance(urls, null);
            Class targetClass = classloader.loadClass("architecture.ext.util.MMSServiceHelper");
            Method method = targetClass.getMethod("getMMSClient", new Class[] {});
            Object client = method.invoke(null, new Object[] {});
            System.out.println(client);
            Method send = client.getClass().getMethod("send", String.class, String.class, String.class, String.class, String.class);
            send.invoke(client, "01090262795", "홍길순", "01090262795", "문자 전송 테스트", "문자전송테스트입니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
