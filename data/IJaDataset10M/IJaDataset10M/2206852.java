package mf.demo.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import mf.demo.User;
import mfinder.annotation.Action;
import mf.mfrpc.serialize.DataSource;
import mf.mfrpc.serialize.DataSourceIterator;
import mf.mfrpc.serialize.FileDataSource;
import mfinder.annotation.Namespace;

/**
 * RPCServerAction for testing
 * see MFrpc-demo.xml
 */
@Namespace(name = "/rpc")
public class RPCServerAction {

    @Action(name = "/rpc/testException")
    public String testException() {
        int i = 1 / 0;
        return "OK";
    }

    @Action(name = "testString")
    public String testString(String msg) {
        return "The Server receive : " + msg;
    }

    @Action(name = "testObject")
    public Object testObject() {
        User u = new User("admin", "admin");
        Map map = new HashMap();
        map.put("string", "ok");
        map.put("int", 100);
        map.put("boolaen", true);
        map.put("string[]", new String[] { "the end" });
        map.put("user", u);
        return map;
    }

    @Action(name = "testBean")
    public User testBean(String name, String pwd) {
        User u = new User(name, pwd);
        return u;
    }

    @Action(name = "testInputStream")
    public InputStream testInputStream(String filepath) throws Exception {
        File f = new File(filepath);
        return f.exists() ? new FileInputStream(f) : null;
    }

    @Action(name = "testDataSource")
    public DataSource testDataSource(String filepath) {
        File f = new File(filepath);
        return f.exists() ? new FileDataSource(f) : null;
    }

    @Action(name = "testUploadDataSource")
    public void testUploadDataSource(DataSource data) throws IOException {
        System.out.println(data.getName() + "," + data.getSize() + "," + data.getLastModified());
        System.out.println("==============================");
        System.out.println(getStream(data.getInputStream()));
        System.out.println("==============================");
    }

    @Action(name = "testUploadDataSources")
    public String testUploadDataSources(DataSourceIterator it) throws IOException {
        if (it != null) {
            while (it.hasNext()) {
                DataSource data = it.next();
                InputStream in = data.getInputStream();
                System.out.println("------------------------------");
                System.out.println(data.getName() + "," + data.getSize());
                System.out.println("==============================");
                System.out.println(getStream(in));
                System.out.println("==============================");
            }
        }
        return "OK";
    }

    private static String getStream(InputStream in) throws IOException {
        byte[] response = new byte[in.available()];
        in.read(response);
        in.close();
        return new String(response, "utf-8").trim();
    }
}
