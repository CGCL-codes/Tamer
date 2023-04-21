package org.xmlsh.commands.experimental;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.xmlsh.core.Options;
import org.xmlsh.core.XCommand;
import org.xmlsh.core.XValue;
import org.xmlsh.util.Base64Coder;
import org.xmlsh.util.Util;

public class http extends XCommand {

    private static Logger mLogger = LogManager.getLogger(http.class);

    @Override
    public int run(List<XValue> args) throws Exception {
        Options opts = new Options("get:,put:,post:,head:,options:,delete:,connectTimeout:,contentType:,readTimeout:,+useCaches,+followRedirects,user:,password:", args);
        opts.parse();
        String method = "GET";
        boolean doInput = true;
        boolean doOutput = false;
        String surl = null;
        if (opts.hasOpt("get")) {
            method = "GET";
            surl = opts.getOptString("get", null);
        } else if (opts.hasOpt("put")) {
            method = "PUT";
            doInput = false;
            doOutput = true;
            surl = opts.getOptString("put", null);
        } else if (opts.hasOpt("post")) {
            method = "POST";
            doOutput = true;
            surl = opts.getOptString("post", null);
        } else if (opts.hasOpt("head")) {
            method = "HEAD";
            surl = opts.getOptString("head", null);
        } else if (opts.hasOpt("options")) {
            surl = opts.getOptString("options", null);
            method = "OPTIONS";
        } else if (opts.hasOpt("delete")) {
            surl = opts.getOptString("delete", null);
            method = "DELETE";
        } else if (opts.hasOpt("trace")) {
            method = "TRACE";
            surl = opts.getOptString("trace", null);
        } else surl = opts.getRemainingArgs().get(0).toString();
        if (surl == null) {
            usage();
            return 1;
        }
        int ret = 0;
        URL url = new URL(surl);
        URLConnection conn = url.openConnection();
        if (conn instanceof HttpURLConnection) {
            HttpURLConnection http = (HttpURLConnection) conn;
            setOptions(http, opts);
            http.setRequestMethod(method);
            http.setDoInput(doInput);
            http.setDoOutput(doOutput);
            if (doOutput) {
                conn.connect();
                OutputStream out = http.getOutputStream();
                Util.copyStream(getStdin().asInputStream(getSerializeOpts()), out);
                out.close();
            }
            ret = http.getResponseCode();
            if (ret == 200) ret = 0;
        }
        if (doInput) {
            InputStream in = conn.getInputStream();
            Util.copyStream(in, getStdout().asOutputStream());
            in.close();
        }
        return ret;
    }

    private void setOptions(HttpURLConnection http, Options opts) throws UnsupportedEncodingException {
        if (opts.hasOpt("connectTimeout")) http.setConnectTimeout((int) (opts.getOptDouble("connectTimeout", 0) * 1000.));
        if (opts.hasOpt("readTimeout")) http.setReadTimeout((int) (opts.getOptDouble("readTimeout", 0) * 1000.));
        if (opts.hasOpt("useCaches")) http.setUseCaches(opts.getOpt("useCaches").getFlag());
        if (opts.hasOpt("followRedirects")) http.setInstanceFollowRedirects(opts.getOpt("followRedirects").getFlag());
        if (opts.hasOpt("contentType")) http.setRequestProperty("Content-Type", opts.getOptString("contentType", "text/xml"));
        String user = opts.getOptString("user", null);
        String pass = opts.getOptString("password", null);
        if (user != null && pass != null) {
            String up = user + ":" + pass;
            String encoding = new String(Base64Coder.encode(up.getBytes(getEnv().getShell().getSerializeOpts().getEncoding())));
            http.setRequestProperty("Authorization", "Basic " + encoding);
        }
    }

    private void usage() {
        printErr("usage: http [-get|-put|-post url");
    }
}
