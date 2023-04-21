package vademecum.externals.ssh;

import java.io.FileInputStream;
import java.util.Properties;

public class Plugin extends org.java.plugin.Plugin {

    public static Properties props;

    @Override
    protected void doStart() throws Exception {
        props = new Properties();
        props.load(new FileInputStream("config/vademecum.external.ssh.properties"));
    }

    @Override
    protected void doStop() throws Exception {
    }
}
