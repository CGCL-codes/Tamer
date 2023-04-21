package org.xsocket.connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.xsocket.QAUtil;

/**
*
* @author grro@xsocket.org
*/
final class JavaProcess {

    private Process p = null;

    private File jarFile = null;

    public InputStream start(String classname, String jarFileURI, String... params) throws IOException {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                terminate();
            }
        });
        System.out.println("loading jar file " + jarFileURI + " ...");
        URL url = new URL(jarFileURI);
        InputStream is = url.openStream();
        jarFile = QAUtil.createTempfile();
        FileOutputStream fos = new FileOutputStream(jarFile);
        byte[] buffer = new byte[4096];
        int bytes_read;
        while ((bytes_read = is.read(buffer)) != -1) {
            fos.write(buffer, 0, bytes_read);
        }
        fos.close();
        is.close();
        String[] args = new String[params.length + 4];
        args[0] = "java";
        args[1] = "-cp";
        args[2] = jarFile.getAbsolutePath();
        args[3] = classname;
        System.arraycopy(params, 0, args, 4, params.length);
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg + " ");
        }
        System.out.println("execute " + sb);
        ProcessBuilder pb = new ProcessBuilder(args);
        p = pb.start();
        return p.getInputStream();
    }

    public void terminate() {
        try {
            p.destroy();
        } catch (Exception ignore) {
        }
        new Thread() {

            public void run() {
                QAUtil.sleep(5000);
                jarFile.delete();
            }
        }.start();
    }
}
