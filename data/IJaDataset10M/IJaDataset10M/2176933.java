package cn.webwheel.whtml;

import cn.webwheel.template.ComponentRenderer;
import cn.webwheel.template.ComponentRendererBuilder;
import cn.webwheel.template.plain.PlainTemplateParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;

class WHtmlHandler implements HttpHandler, ComponentRenderer<Object> {

    File dir;

    Charset fc;

    private Map<String, String> mime = new HashMap<String, String>();

    public WHtmlHandler(File dir, Charset fc) throws IOException {
        this.dir = dir;
        this.fc = fc;
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("res/mime.txt"), "utf-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            mime.put(line, reader.readLine());
        }
        reader.close();
    }

    private void setMime(String path, HttpExchange httpExchange) {
        int idx = path.lastIndexOf('.');
        if (idx == -1) return;
        String s = path.substring(idx + 1);
        s = mime.get(s);
        if (s == null) return;
        if (s.startsWith("text/")) {
            httpExchange.getResponseHeaders().add("Content-Type", s + "; charset=" + fc.name());
        } else {
            httpExchange.getResponseHeaders().add("Content-Type", s);
        }
    }

    private void send404(HttpExchange httpExchange) throws IOException {
        byte[] msg = "Page NotFound".getBytes("utf-8");
        httpExchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        httpExchange.sendResponseHeaders(404, msg.length);
        httpExchange.getResponseBody().write(msg);
        httpExchange.close();
    }

    private void def(String path, HttpExchange httpExchange) throws IOException {
        File file = new File(dir, path);
        if (!file.exists()) {
            send404(httpExchange);
        } else {
            byte[] data = new byte[(int) file.length()];
            try {
                DataInputStream dis = new DataInputStream(new FileInputStream(file));
                try {
                    dis.readFully(data);
                } finally {
                    dis.close();
                }
            } catch (IOException e) {
                except(httpExchange, e);
                return;
            }
            setMime(path, httpExchange);
            httpExchange.sendResponseHeaders(200, data.length);
            httpExchange.getResponseBody().write(data);
            httpExchange.close();
        }
    }

    private void except(HttpExchange httpExchange, Throwable e) throws IOException {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        except(httpExchange, sw.toString());
    }

    private void except(HttpExchange httpExchange, String e) throws IOException {
        httpExchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        byte[] data = e.getBytes("utf-8");
        httpExchange.sendResponseHeaders(500, data.length);
        httpExchange.getResponseBody().write(data);
        httpExchange.close();
    }

    private void renderResource(HttpExchange httpExchange, String resource, Object root) throws IOException {
        byte[] data;
        if (root != null) {
            InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("res/" + resource), "utf-8");
            StringBuilder sb = new StringBuilder();
            int rd;
            char[] buf = new char[4096];
            while ((rd = reader.read(buf)) != -1) {
                sb.append(buf, 0, rd);
            }
            reader.close();
            ComponentRenderer<Object> renderer;
            try {
                renderer = new ComponentRendererBuilder<Object>(sb.toString()).build();
            } catch (PlainTemplateParser.TemplateParserException e) {
                except(httpExchange, e);
                return;
            }
            StringWriter sw = new StringWriter();
            renderer.render(sw, root, null);
            data = sw.toString().getBytes(fc);
        } else {
            InputStream is = getClass().getResourceAsStream("res/" + resource);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int rd;
            while ((rd = is.read(buf)) != -1) {
                baos.write(buf, 0, rd);
            }
            data = baos.toByteArray();
        }
        setMime(resource, httpExchange);
        httpExchange.sendResponseHeaders(200, data.length);
        httpExchange.getResponseBody().write(data);
        httpExchange.close();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();
        if (path.equals("/whtml.help")) {
            Map<String, Object> ctx = new HashMap<String, Object>();
            ctx.put("charset", fc.name());
            renderResource(httpExchange, "help.html", ctx);
            return;
        }
        if (path.startsWith("/whtmlres/")) {
            renderResource(httpExchange, path.substring("/whtmlres/".length()), null);
            return;
        }
        File file = new File(dir, path);
        if (path.endsWith("/") && file.exists() && file.isDirectory()) {
            if (new File(file, "index.html.wp").exists() || new File(file, "index.html").exists()) {
                if (!path.endsWith("/")) {
                    path += "/";
                }
                path += "index.html";
            } else if (new File(file, "index.htm.wp").exists() || new File(file, "index.htm").exists()) {
                if (!path.endsWith("/")) {
                    path += "/";
                }
                path += "index.htm";
            } else {
                try {
                    listDir(file, httpExchange);
                } catch (Exception e) {
                    except(httpExchange, e);
                }
                return;
            }
        }
        handle(httpExchange, path, query, null);
    }

    public void handle(HttpExchange httpExchange, String path, String query, Map<String, Object> map) throws IOException {
        File wp = new File(dir, path + ".wp");
        if (wp.exists()) {
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[4096];
            int rd;
            InputStreamReader reader = null;
            try {
                reader = new InputStreamReader(new FileInputStream(wp), fc);
                while ((rd = reader.read(buf)) != -1) {
                    sb.append(buf, 0, rd);
                }
            } catch (IOException e) {
                except(httpExchange, e);
                return;
            } finally {
                if (reader != null) reader.close();
            }
            JSONObject json;
            try {
                json = new JSONObject(sb.toString());
            } catch (JSONException e) {
                try {
                    JSONArray array = new JSONArray(sb.toString());
                    json = selectResponse(array);
                    if (json == null) {
                        send404(httpExchange);
                        return;
                    }
                } catch (JSONException e1) {
                    except(httpExchange, e);
                    return;
                }
            }
            if (json.optString("wtemplate", null) == null) {
                try {
                    json.put("wtemplate", path);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            StringWriter sw = new StringWriter();
            try {
                render(sw, new JsonWrapper(json), this);
            } catch (Throwable e) {
                if (e.getCause() instanceof PlainTemplateParser.TemplateParserException) {
                    e = e.getCause();
                }
                except(httpExchange, e);
                return;
            }
            setMime(path, httpExchange);
            byte[] data = sw.toString().getBytes(fc);
            httpExchange.sendResponseHeaders(200, data.length);
            httpExchange.getResponseBody().write(data);
            httpExchange.close();
            return;
        }
        File wa = new File(dir, path + ".wa");
        if (wa.exists()) {
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[4096];
            int rd;
            InputStreamReader reader = null;
            try {
                reader = new InputStreamReader(new FileInputStream(wa), fc);
                while ((rd = reader.read(buf)) != -1) {
                    sb.append(buf, 0, rd);
                }
            } catch (IOException e) {
                except(httpExchange, e);
                return;
            } finally {
                if (reader != null) reader.close();
            }
            JSONObject json;
            try {
                json = new JSONObject(sb.toString());
            } catch (JSONException e) {
                try {
                    JSONArray array = new JSONArray(sb.toString());
                    json = selectResponse(array);
                    if (json == null) {
                        send404(httpExchange);
                        return;
                    }
                } catch (JSONException e1) {
                    except(httpExchange, e);
                    return;
                }
            }
            {
                Iterator it = json.keys();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    if (!(json.opt(key) instanceof String)) {
                        except(httpExchange, "{msg:\"wa field '" + key + "' must be string\"}");
                        return;
                    }
                }
            }
            if (map == null) {
                try {
                    map = parse(httpExchange);
                } catch (Exception e) {
                    except(httpExchange, e);
                    return;
                }
            }
            parse(map, query);
            Object waction = json.remove("waction");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object val = entry.getValue();
                String pat = (String) json.remove(entry.getKey());
                if (pat.startsWith("?")) {
                    pat = pat.substring(1);
                }
                if (pat.startsWith("[") && pat.endsWith("]")) {
                    pat = pat.substring(1, pat.length() - 1);
                } else {
                    if (Array.getLength(val) > 1) {
                        except(httpExchange, "{msg:\"parameter '" + entry.getKey() + "' should not be array\"}");
                        return;
                    }
                }
                if (!pat.equals("*")) {
                    if (!(val instanceof String[])) {
                        except(httpExchange, "{msg:\"parameter '" + entry.getKey() + "' should be string\"}");
                        return;
                    }
                    String[] ss = (String[]) val;
                    for (String s : ss) {
                        try {
                            if (s.matches("^" + pat + "$")) {
                                continue;
                            }
                        } catch (Exception e) {
                            except(httpExchange, e);
                        }
                        except(httpExchange, "{msg:\"parameter '" + entry.getKey() + "' not match '" + pat + "'\"}");
                        return;
                    }
                }
            }
            Iterator it = json.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                String pat = (String) json.opt(key);
                if (!pat.startsWith("?")) {
                    except(httpExchange, "{msg:\"parameter '" + key + "' not exist\"}");
                    return;
                }
            }
            if (waction == null) {
                waction = "{ok:1}";
            }
            if (waction instanceof String) {
                String s = (String) waction;
                if (s.startsWith("forward:")) {
                    s = s.substring("forward:".length()).trim();
                    String p = s, q = null;
                    int idx = s.indexOf('?');
                    if (idx != -1) {
                        p = s.substring(0, idx);
                        q = s.substring(idx + 1);
                    }
                    handle(httpExchange, p, q, map);
                    return;
                } else if (s.startsWith("redirect:")) {
                    httpExchange.getResponseHeaders().put("Location", Arrays.asList(s.substring("redirect:".length())));
                    httpExchange.sendResponseHeaders(302, 0);
                    httpExchange.close();
                    return;
                }
            }
            setMime(path, httpExchange);
            byte[] data = waction.toString().getBytes(fc);
            httpExchange.sendResponseHeaders(200, data.length);
            httpExchange.getResponseBody().write(data);
            httpExchange.close();
            return;
        }
        def(path, httpExchange);
    }

    private void listDir(File dir, HttpExchange httpExchange) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isRoot", dir.equals(new File(this.dir, "/")));
        map.put("dir", dir);
        map.put("root", this.dir);
        List<File> files = new ArrayList<File>();
        File[] list = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        if (list != null) files.addAll(Arrays.asList(list));
        list = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });
        if (list != null) files.addAll(Arrays.asList(list));
        map.put("files", files);
        renderResource(httpExchange, "dir.html", map);
    }

    void readLine(InputStream is, ByteArrayOutputStream baos) throws IOException {
        baos.reset();
        boolean preisr = false;
        int d;
        while ((d = is.read()) != -1) {
            if (d == '\n' && preisr) {
                return;
            }
            if (preisr) {
                baos.write('\r');
            }
            if (!(preisr = d == '\r')) {
                baos.write(d);
            }
        }
        if (preisr) {
            baos.write('\r');
        }
    }

    int boundaryEqual(String boundary, ByteArrayOutputStream baos) throws IOException {
        if (boundary.length() + 2 == baos.size()) {
            if (("--" + boundary).equals(new String(baos.toByteArray(), "iso8859-1"))) {
                return 1;
            }
        } else if (boundary.length() + 4 == baos.size()) {
            if (("--" + boundary + "--").equals(new String(baos.toByteArray(), "iso8859-1"))) {
                return 2;
            }
        }
        return 0;
    }

    private Map<String, Object> parse(HttpExchange httpExchange) throws IOException {
        String contentType = httpExchange.getRequestHeaders().getFirst("Content-Type");
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (httpExchange.getRequestMethod().equalsIgnoreCase("post") && contentType != null) {
            if (contentType.equals("application/x-www-form-urlencoded")) {
                int len = Integer.parseInt(httpExchange.getRequestHeaders().getFirst("Content-Length"));
                byte[] buf = new byte[len];
                DataInputStream dis = new DataInputStream(httpExchange.getRequestBody());
                dis.readFully(buf);
                parse(map, URLDecoder.decode(new String(buf), fc.name()));
            } else if (contentType.startsWith("multipart/form-data; boundary=")) {
                String boundary = contentType.substring("multipart/form-data; boundary=".length());
                BufferedInputStream is = new BufferedInputStream(httpExchange.getRequestBody());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                readLine(is, baos);
                int r = boundaryEqual(boundary, baos);
                if (r != 1) return map;
                loop: while (true) {
                    String name = null;
                    String filename = null;
                    while (true) {
                        readLine(is, baos);
                        if (baos.size() == 0) break;
                        String s = new String(baos.toByteArray(), "iso8859-1");
                        if (s.startsWith("Content-Disposition:")) {
                            for (String ss : s.split(";")) {
                                ss = ss.trim();
                                if (ss.startsWith("name=")) {
                                    name = ss.substring("name=".length() + 1, ss.length() - 1);
                                } else if (ss.startsWith("filename=")) {
                                    filename = ss.substring("filename=".length() + 1, ss.length() - 1);
                                }
                            }
                        }
                    }
                    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                    while (true) {
                        readLine(is, baos);
                        r = boundaryEqual(boundary, baos);
                        if (r == 0) {
                            baos2.write(baos.toByteArray());
                            continue;
                        }
                        if (name != null) {
                            if (filename != null) {
                                Object files = map.get(name);
                                if (files == null) {
                                    map.put(name, files = new File[0]);
                                }
                                if (files instanceof File[]) {
                                    File[] fs = (File[]) files;
                                    File[] nfs = new File[fs.length + 1];
                                    System.arraycopy(fs, 0, nfs, 0, fs.length);
                                    nfs[fs.length] = new File(filename);
                                    map.put(name, nfs);
                                }
                            } else {
                                Object vals = map.get(name);
                                if (vals == null) {
                                    map.put(name, vals = new String[0]);
                                }
                                if (vals instanceof String[]) {
                                    String[] vs = (String[]) vals;
                                    String[] nvs = new String[vs.length + 1];
                                    System.arraycopy(vs, 0, nvs, 0, vs.length);
                                    nvs[vs.length] = new String(baos2.toByteArray(), fc);
                                    map.put(name, nvs);
                                }
                            }
                        }
                        if (r == 1) {
                            continue loop;
                        } else {
                            break loop;
                        }
                    }
                }
            }
        }
        return map;
    }

    private void parse(Map<String, Object> map, String query) {
        if (query == null) return;
        query = query.trim();
        String[] ss = query.split("&");
        for (String s : ss) {
            String[] sss = s.split("=");
            if (sss.length == 2) {
                Object o = map.get(sss[0]);
                if (o == null) {
                    map.put(sss[0], new String[] { sss[1] });
                } else if (o instanceof String[]) {
                    String[] vs = (String[]) o;
                    String[] nvs = new String[vs.length + 1];
                    System.arraycopy(vs, 0, nvs, 0, vs.length);
                    nvs[vs.length] = sss[1];
                    map.put(sss[0], nvs);
                }
            }
        }
    }

    private JSONObject selectResponse(final JSONArray array) throws JSONException {
        if (array.length() == 0) return null;
        if (array.length() == 1) return array.getJSONObject(0);
        final Object[] ops = new Object[array.length() + 1];
        for (int i = 0; i < array.length(); i++) {
            ops[i] = i + 1;
        }
        ops[array.length()] = "random";
        final int[] rs = new int[1];
        if (Main.alwaysRandom) {
            rs[0] = array.length();
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        setTooltip(array, null);
                        Main.setAlwaysOnTop();
                        rs[0] = JOptionPane.showOptionDialog(null, "Please choose response:", "select response", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, ops, ops[array.length()]);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (rs[0] == -1) return null;
        if (rs[0] == array.length()) {
            return array.getJSONObject((int) (Math.random() * array.length()));
        }
        return array.getJSONObject(rs[0]);
    }

    private void setTooltip(final JSONArray array, Container container) {
        if (container != null) {
            for (int i = 0; i < container.getComponentCount(); i++) {
                Component comp = container.getComponent(i);
                if (comp instanceof JButton) {
                    JButton bt = (JButton) comp;
                    if ("random".equals(bt.getText())) {
                        bt.setForeground(Color.green.darker());
                    } else {
                        try {
                            int j = Integer.parseInt(bt.getText()) - 1;
                            StringBuilder sb = new StringBuilder();
                            format(array.getJSONObject(j), sb, 0);
                            bt.setToolTipText("<html><pre>" + sb.toString());
                        } catch (Exception e) {
                        }
                    }
                } else if (comp instanceof Container) {
                    setTooltip(array, (Container) comp);
                }
            }
            return;
        }
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setReshowDelay(0);
        ToolTipManager.sharedInstance().setDismissDelay(1000000);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                for (Window win : JDialog.getWindows()) {
                    if (win instanceof JDialog) {
                        setTooltip(array, ((JDialog) win).getContentPane());
                    }
                }
            }
        });
    }

    private void pad(StringBuilder sb, int pad) {
        for (int i = 0; i < pad; i++) {
            sb.append(' ');
        }
    }

    private void format(Object value, StringBuilder sb, int pad) throws JSONException {
        if (value instanceof Map) {
            value = new JSONObject((Map) value);
        } else if (value instanceof Collection) {
            value = new JSONArray((Collection) value);
        } else if (value.getClass().isArray()) {
            value = new JSONArray(value);
        }
        if (value instanceof JSONObject) {
            format((JSONObject) value, sb, pad + 4);
        } else if (value instanceof JSONArray) {
            JSONArray arr = (JSONArray) value;
            sb.append('[');
            for (int j = 0; j < arr.length(); j++) {
                if (j > 0) sb.append(',');
                sb.append('\n');
                pad(sb, pad + 8);
                format(arr.get(j), sb, pad + 8);
            }
            sb.append('\n');
            pad(sb, pad + 4);
            sb.append(']');
        } else {
            sb.append(JSONObject.valueToString(value));
        }
    }

    private void format(JSONObject json, StringBuilder sb, int pad) throws JSONException {
        sb.append("{");
        int i = 0;
        Iterator it = json.keys();
        while (it.hasNext()) {
            if (i++ > 0) {
                sb.append(',');
            }
            String k = (String) it.next();
            sb.append('\n');
            pad(sb, pad + 4);
            sb.append(k).append(": ");
            format(json.opt(k), sb, pad + 4);
        }
        sb.append('\n');
        pad(sb, pad);
        sb.append('}');
    }

    @Override
    public void render(Writer writer, Object component, ComponentRenderer delegate) throws IOException {
        if (!(component instanceof JsonWrapper)) {
            return;
        }
        JsonWrapper json = (JsonWrapper) component;
        String temp = json.remove("wtemplate").toString();
        File tempFile = new File(dir, temp);
        StringBuilder sb = new StringBuilder();
        int rd;
        char[] buf = new char[4096];
        InputStreamReader reader = new InputStreamReader(new FileInputStream(tempFile), fc);
        try {
            while ((rd = reader.read(buf)) != -1) {
                sb.append(buf, 0, rd);
            }
        } finally {
            reader.close();
        }
        ComponentRenderer<Object> renderer;
        try {
            renderer = new ComponentRendererBuilder<Object>(sb.toString()).build();
        } catch (PlainTemplateParser.TemplateParserException e) {
            throw new IOException(e);
        }
        renderer.render(writer, json, this);
    }
}
