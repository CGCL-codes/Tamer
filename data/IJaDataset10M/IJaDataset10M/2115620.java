package abstrasy.externals;

import abstrasy.Bivalence;
import abstrasy.Buffer;
import abstrasy.Interpreter;
import abstrasy.Node;
import abstrasy.Tools;
import abstrasy.interpreter.ExternalTK;
import abstrasy.interpreter.InterpreterException;
import abstrasy.interpreter.StdErrors;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class External_File extends External implements ExternalObject {

    boolean inUse = false;

    String charset = System.getProperty("file.encoding");

    int bytePos = 0;

    int connectTimeOut = 0;

    int readTimeOut = 0;

    File file = null;

    FileInputStream fileInputStream = null;

    FileOutputStream fileOutputStream = null;

    URLConnection urlConnection = null;

    String tcpHost = null;

    int tcpPort = 0;

    Socket socket = null;

    InputStream inputStream = null;

    BufferedInputStream inputBuffer = null;

    DataInputStream input = null;

    OutputStream outputStream = null;

    BufferedOutputStream outputBuffer = null;

    DataOutputStream output = null;

    /**
     * Permet de fermer tous les flux et fichiers ouverts.
     *
     * Cette méthode a une vocation interne et peut être liée à un hook.
     *
     * Si les fichiers sont déjà fermés, il ne se passe rien.
     *
     * @throws Exception
     */
    public void internal_close() throws Exception {
        if (output != null) {
            output.close();
            output = null;
        }
        if (input != null) {
            input.close();
            input = null;
        }
        if (outputStream != null) {
            outputStream.close();
            outputStream = null;
        }
        if (inputStream != null) {
            inputStream.close();
            inputStream = null;
        }
        if (fileOutputStream != null) {
            fileOutputStream.close();
            fileOutputStream = null;
        }
        if (fileInputStream != null) {
            fileInputStream.close();
            fileInputStream = null;
        }
        if (file != null) {
            file = null;
        }
        if (urlConnection != null) {
            urlConnection = null;
        }
        if (socket != null) {
            socket.close();
            socket = null;
        }
        if (tcpHost != null) {
            tcpHost = null;
        }
        inUse = false;
        bytePos = 0;
    }

    public void setBufOut(OutputStream out) {
        outputBuffer = new BufferedOutputStream(out);
        output = new DataOutputStream(outputBuffer);
    }

    public void setBufIn(InputStream in) {
        inputBuffer = new BufferedInputStream(in);
        input = new DataInputStream(inputBuffer);
    }

    public void setClosableOut(OutputStream out) {
        outputStream = out;
        setBufOut(out);
    }

    public void setClosableIn(InputStream in) {
        inputStream = in;
        setBufIn(in);
    }

    /**
     *  Clé du hook...
     */
    private Object myHookKey;

    /**
     * Ajoute un hook en cas où on oublierait de fermer les fichiers et les flux...
     * 
     * Ce hook sera automatiquement exécuté lors de l'arrêt de l'interpréteur, à moins
     * que le fichier soit refermé manuellement. De cette manière, les fichiers devraient
     * conserver un état stable.
     * 
     */
    private void putHook() {
        final WeakReference myself = new WeakReference(this);
        Runnable hook = new Runnable() {

            public void run() {
                External_File me = (External_File) myself.get();
                if (me != null) {
                    if (Interpreter.mySelf().isDebugMode()) {
                        Interpreter.Log("File Hook : close " + me + "...");
                    }
                    try {
                        me.internal_close();
                    } catch (Exception e) {
                        Interpreter.Log(" -> Exception: " + e.toString());
                    }
                }
            }
        };
        myHookKey = Interpreter.supervisor_PUTHOOK(hook);
    }

    /**
     * On retire le hook...
     */
    private void removeHook() {
        Interpreter.supervisor_REMOVEHOOK(myHookKey);
        myHookKey = null;
    }

    public External_File() {
    }

    public Node external_open_file(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(3);
        if (inUse) {
            throw new InterpreterException(StdErrors.extend(StdErrors.Already_used, "File already open"));
        }
        inUse = true;
        file = new File(startAt.getSubNode(1, Node.TYPE_STRING).getString());
        String mode = startAt.getSubNode(2, Node.TYPE_STRING).getString().toLowerCase().trim();
        if (mode.equals("r")) {
            fileInputStream = new FileInputStream(file);
            inputStream = fileInputStream;
            inputBuffer = new BufferedInputStream(inputStream);
            input = new DataInputStream(inputBuffer);
            bytePos = 0;
        } else if (mode.equals("w")) {
            fileOutputStream = new FileOutputStream(file);
            outputStream = fileOutputStream;
            outputBuffer = new BufferedOutputStream(outputStream);
            output = new DataOutputStream(outputBuffer);
            bytePos = 0;
        } else if (mode.equals("a")) {
            bytePos = (int) file.length();
            fileOutputStream = new FileOutputStream(file, true);
            outputStream = fileOutputStream;
            outputBuffer = new BufferedOutputStream(outputStream);
            output = new DataOutputStream(outputBuffer);
        } else {
            throw new InterpreterException(StdErrors.extend(StdErrors.Invalid_parameter, "Mode not supported"));
        }
        putHook();
        return null;
    }

    /**
     * Classe ExtURL privée. Cette classe offre une abstraction supplémentaire au niveau de la gestion des ressources
     * décrites sous la forme d'urls.
     * 
     * Les protocoles émulés sont les suivants:
     * 
     *    stdout://           : Flux de sortie standard.
     *    stdin://            : Flux d'entrée standard.
     *    stderr://           : Flux de sortie standard pour les messages d'erreur.
     *    
     *    tcp://host:port     : Ouverture du port sur le host indiqué à l'aide d'un socket TCP.
     *    ssl+tcp://host:port : Idem, mais au travers d'une couche sécurisée SSL.
     *    
     *    file://path         : Accès à un fichier en écriture à l'aide du protocol file.
     *    
     * Les protocoles gérés par Java sont les suivants:
     * 
     *    file://path         : Accès à un fichier en lecture seule.
     *    
     *    jar://path          : Accès à une ressource d'une archive jar en lecture seule.
     *    
     * Les protocoles assistés (c-à-d gérés par Java, mais nécessitant une abstraction supplémentaire):
     * 
     *    http://...          : Support de GET, PUT et POST, ainsi que les headers HTTP.
     *    https://...         : Idem.
     *    
     *    ftp://...           : Sélection du mode d'accès écriture/lecture.
     *    
     */
    private class ExtURL {

        String urlStr = null;

        String protocol = null;

        String host = null;

        int port = 0;

        URL realUrl = null;

        public ExtURL(String url) throws MalformedURLException {
            this.urlStr = url;
            String u = url;
            if (u.equalsIgnoreCase("stdout://") || u.equalsIgnoreCase("stdin://") || u.equalsIgnoreCase("stderr://")) {
                protocol = u.substring(0, u.indexOf(':')).toLowerCase();
            } else if (u.toLowerCase().startsWith("tcp://") || u.toLowerCase().startsWith("ssl+tcp://")) {
                protocol = urlStr.substring(0, urlStr.indexOf(':')).toLowerCase();
                u = u.substring(protocol.length() + 3, u.length());
                int lpp = u.lastIndexOf(':');
                if (lpp < 0 || lpp >= u.length()) {
                    throw new MalformedURLException(url);
                }
                try {
                    port = Integer.parseInt(u.substring(lpp + 1, u.length()));
                } catch (Exception e) {
                    throw new MalformedURLException(url);
                }
                host = u.substring(0, lpp);
            } else {
                realUrl = new URL(urlStr);
            }
        }

        String getProtocol() {
            if (realUrl != null) {
                return realUrl.getProtocol();
            } else {
                return protocol;
            }
        }

        URI toURI() throws URISyntaxException {
            if (realUrl != null) {
                return realUrl.toURI();
            } else {
                throw new URISyntaxException(urlStr, "Cannot be converted to URI");
            }
        }

        URLConnection openConnection() throws IOException {
            return realUrl.openConnection();
        }

        String getHost() {
            if (realUrl != null) {
                return realUrl.getHost();
            } else {
                return host;
            }
        }

        int getPort() {
            if (realUrl != null) {
                return realUrl.getPort();
            } else {
                return port;
            }
        }
    }

    public Node external_open_url(Node startAt) throws Exception {
        if (inUse) {
            throw new InterpreterException(StdErrors.extend(StdErrors.Already_used, "File already open"));
        }
        inUse = true;
        startAt.isGoodArgsLength(false, 2);
        ExtURL url = new ExtURL(startAt.getSubNode(1, Node.TYPE_STRING).getString());
        String protocol = url.getProtocol();
        String mode = null;
        Node props = null;
        Node datas = null;
        byte[] buffer = null;
        String old_c = null;
        String old_r = null;
        int max_i = startAt.size() - 1;
        if (startAt.elementAt(max_i).getSymbolicValue_undestructive().isVList()) {
            props = startAt.getSubNode(max_i--, Node.TYPE_LIST);
        }
        int i_ = 2;
        if (i_ <= max_i) {
            mode = startAt.getSubNode(i_++, Node.TYPE_STRING).getString().toUpperCase().trim();
            if (protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https")) {
                if (!(mode.equals("GET") || mode.equals("POST") || mode.equals("PUT"))) {
                    throw new InterpreterException(128010, "Unsupported request methode");
                }
            } else if (protocol.equalsIgnoreCase("ftp") || protocol.equalsIgnoreCase("file")) {
                if (!(mode.equalsIgnoreCase("r") || mode.equalsIgnoreCase("w"))) {
                    throw new InterpreterException(128015, "Unsupported access methode");
                }
            } else if (protocol.equalsIgnoreCase("jar") || protocol.equalsIgnoreCase("stdin")) {
                if (!(mode.equalsIgnoreCase("r"))) {
                    throw new InterpreterException(128015, "Unsupported access methode");
                }
            } else if (protocol.equalsIgnoreCase("tcp") || protocol.equalsIgnoreCase("ssl+tcp")) {
                if (!(mode.equalsIgnoreCase("rw"))) {
                    throw new InterpreterException(128015, "Unsupported access methode");
                }
            } else if (protocol.equalsIgnoreCase("stdout") || protocol.equalsIgnoreCase("stderr")) {
                if (!(mode.equalsIgnoreCase("w"))) {
                    throw new InterpreterException(128015, "Unsupported access methode");
                }
            } else {
                throw new InterpreterException(128011, "Unsupported protocol");
            }
        }
        if (i_ <= max_i) {
            if (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https")) {
                throw new InterpreterException(128016, "Unsupported request datas");
            }
            datas = startAt.getSubNode(i_++, Node.TYPE_STRING | Node.TYPE_OBJECT);
            if (datas.isVObject()) {
                Object obj = datas.getVObjectExternalInstance();
                if (External_Buffer.class.isInstance(obj)) {
                    Buffer bbuffer = ((External_Buffer) obj).getBuffer();
                    buffer = bbuffer.read_bytes();
                } else {
                    throw new InterpreterException(StdErrors.extend(StdErrors.Invalid_parameter, "Object (" + obj.getClass().getName() + ") required " + External_Buffer.class.getName()));
                }
            } else {
                buffer = datas.getString().getBytes();
            }
        }
        if (datas != null && mode != null && mode.equals("GET")) {
            throw new InterpreterException(128012, "GET request with data body");
        }
        if (props != null && (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https"))) {
            throw new InterpreterException(128013, "Cannot handle header properties in request");
        }
        try {
            if (protocol.equalsIgnoreCase("file") && mode != null && mode.equalsIgnoreCase("w")) {
                File f = new File(url.toURI());
                outputStream = new FileOutputStream(f);
                outputBuffer = new BufferedOutputStream(outputStream);
                output = new DataOutputStream(outputBuffer);
            } else if (protocol.equalsIgnoreCase("tcp")) {
                tcpHost = url.getHost();
                tcpPort = url.getPort();
                if (tcpPort < 0 || tcpPort > 65535) {
                    throw new InterpreterException(StdErrors.extend(StdErrors.Out_of_range, "" + tcpPort));
                }
                socket = new Socket(tcpHost, tcpPort);
                if (readTimeOut > 0) {
                    socket.setSoTimeout(readTimeOut);
                }
                inputStream = socket.getInputStream();
                inputBuffer = new BufferedInputStream(inputStream);
                input = new DataInputStream(inputBuffer);
                outputStream = socket.getOutputStream();
                outputBuffer = new BufferedOutputStream(outputStream);
                output = new DataOutputStream(outputBuffer);
            } else if (protocol.equalsIgnoreCase("ssl+tcp")) {
                tcpHost = url.getHost();
                tcpPort = url.getPort();
                if (tcpPort < 0 || tcpPort > 65535) {
                    throw new InterpreterException(StdErrors.extend(StdErrors.Out_of_range, "" + tcpPort));
                }
                SocketFactory socketFactory = SSLSocketFactory.getDefault();
                socket = socketFactory.createSocket(tcpHost, tcpPort);
                if (readTimeOut > 0) {
                    socket.setSoTimeout(readTimeOut);
                }
                inputStream = socket.getInputStream();
                inputBuffer = new BufferedInputStream(inputStream);
                input = new DataInputStream(inputBuffer);
                outputStream = socket.getOutputStream();
                outputBuffer = new BufferedOutputStream(outputStream);
                output = new DataOutputStream(outputBuffer);
            } else if (protocol.equalsIgnoreCase("stdout")) {
                setBufOut(System.out);
            } else if (protocol.equalsIgnoreCase("stderr")) {
                setBufOut(System.err);
            } else if (protocol.equalsIgnoreCase("stdin")) {
                setBufIn(System.in);
            } else {
                urlConnection = url.openConnection();
                if (connectTimeOut > 0) {
                    urlConnection.setConnectTimeout(connectTimeOut);
                }
                if (readTimeOut > 0) {
                    urlConnection.setReadTimeout(readTimeOut);
                }
                urlConnection.setUseCaches(false);
                urlConnection.setDoInput(true);
                if (urlConnection instanceof HttpURLConnection) {
                    HttpURLConnection httpCon = (HttpURLConnection) urlConnection;
                    if (props != null) {
                        for (int i = 0; i < props.size(); i++) {
                            Node pnode = props.getSubNode(i, Node.TYPE_DICO);
                            String header_s = Node.getPairKey(pnode);
                            String value_s = Node.node2VString(Node.getPairValue(pnode)).getString();
                            Interpreter.Log("   HTTP-Header: " + header_s + " : " + value_s);
                            httpCon.setRequestProperty(header_s, value_s);
                        }
                    }
                    if (mode != null && (mode.equals("POST") || mode.equals("PUT"))) {
                        if (mode.equals("PUT")) {
                            Interpreter.Log("   HTTP PUT: " + url.toString());
                        } else {
                            Interpreter.Log("   HTTP POST: " + url.toString());
                        }
                        urlConnection.setDoOutput(true);
                        httpCon.setRequestMethod(mode);
                        outputStream = urlConnection.getOutputStream();
                        outputBuffer = new BufferedOutputStream(outputStream);
                        output = new DataOutputStream(outputBuffer);
                        output.write(buffer);
                        output.flush();
                    }
                    inputStream = urlConnection.getInputStream();
                    inputBuffer = new BufferedInputStream(inputStream);
                    input = new DataInputStream(inputBuffer);
                } else {
                    if (mode == null || (mode != null && mode.equalsIgnoreCase("r"))) {
                        Interpreter.Log("   " + protocol + " read : " + url.toString());
                        inputStream = urlConnection.getInputStream();
                        inputBuffer = new BufferedInputStream(inputStream);
                        input = new DataInputStream(inputBuffer);
                    } else {
                        Interpreter.Log("   " + protocol + " write : " + url.toString());
                        outputStream = urlConnection.getOutputStream();
                        outputBuffer = new BufferedOutputStream(outputStream);
                        output = new DataOutputStream(outputBuffer);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
        bytePos = 0;
        putHook();
        return null;
    }

    public Node external_set_connect_timeout(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(2);
        connectTimeOut = Math.min(0, (int) startAt.getSubNode(1, Node.TYPE_NUMBER).getNumber());
        return null;
    }

    public Node external_get_connect_timeout(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        return new Node(connectTimeOut);
    }

    public Node external_set_read_timeout(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(2);
        readTimeOut = Math.min(0, (int) startAt.getSubNode(1, Node.TYPE_NUMBER).getNumber());
        return null;
    }

    public Node external_get_read_timeout(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        return new Node(readTimeOut);
    }

    public Node external_set_charset(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(2);
        charset = startAt.getSubNode(1, Node.TYPE_STRING).getString();
        return null;
    }

    public Node external_static_clear_charset(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        charset = System.getProperty("file.encoding");
        return null;
    }

    public Node external_get_charset(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        String res = charset;
        if (charset == null) {
            res = System.getProperty("file.encoding");
        }
        return new Node(res);
    }

    public Node external_close(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        internal_close();
        removeHook();
        return null;
    }

    public Node external_available(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        return new Node(input.available());
    }

    public Node external_is_opened(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        return new Node(inUse ? Node.TRUE : Node.FALSE);
    }

    public Node external_read_buffer(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1, 2);
        Buffer buffer = new Buffer();
        buffer.setCharset(charset);
        if (startAt.size() == 2) {
            int sz = (int) startAt.getSubNode(1, Node.TYPE_NUMBER).getNumber();
            byte[] buf = new byte[sz];
            int cread = input.read(buf);
            bytePos += cread;
            if (cread > 0) {
                buffer.write_bytes(buf, cread);
            } else {
                return Node.createNothing();
            }
        } else {
            byte[] buf = new byte[4096];
            int cread;
            while ((cread = input.read(buf)) != -1) {
                bytePos += cread;
                if (cread > 0) {
                    buffer.write_bytes(buf, cread);
                }
            }
            if (buffer.size() == 0 && cread == -1) {
                return Node.createNothing();
            }
        }
        External_Buffer res = new External_Buffer();
        res.setBuffer(buffer);
        return ExternalTK.createVObject_External(null, External_Buffer.class.getName(), res);
    }

    public Node external_read_chunked_buffer(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        Buffer buffer = new Buffer();
        buffer.setCharset(charset);
        int sz = 0;
        try {
            sz = input.readInt();
        } catch (EOFException eofe) {
            return Node.createNothing();
        }
        if (sz > 0) {
            byte[] buf = new byte[sz];
            int cread = input.read(buf);
            bytePos += cread;
            if (cread > 0) {
                buffer.write_bytes(buf, cread);
            }
            if (buffer.size() == 0 && cread == -1) {
                return Node.createNothing();
            }
        }
        External_Buffer res = new External_Buffer();
        res.setBuffer(buffer);
        return ExternalTK.createVObject_External(null, External_Buffer.class.getName(), res);
    }

    public Node external_write_buffer(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(2);
        Buffer buffer = ((External_Buffer) External.getArgExternalInstance(startAt, 1, External_Buffer.class)).getBuffer();
        if (buffer.length() > 0) {
            byte[] buf = buffer.read_bytes();
            output.write(buf);
            output.flush();
            bytePos += buf.length;
        }
        return null;
    }

    public Node external_write_chunked_buffer(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(2);
        Buffer buffer = ((External_Buffer) External.getArgExternalInstance(startAt, 1, External_Buffer.class)).getBuffer();
        int len = buffer.length();
        output.writeInt(len);
        if (buffer.length() > 0) {
            byte[] buf = buffer.read_bytes();
            output.write(buf);
        }
        output.flush();
        bytePos += len;
        return null;
    }

    public Node external_read(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        String res = "";
        byte[] buffer = new byte[4096];
        int cread;
        while ((cread = input.read(buffer)) != -1) {
            bytePos += cread;
            if (charset != null) {
                res = res + new String(buffer, 0, cread, charset);
            } else {
                res = res + new String(buffer, 0, cread);
            }
        }
        if (res.length() == 0 && cread == -1) {
            return Node.createNothing();
        }
        return new Node(res);
    }

    public Node external_readln(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        String res = "";
        ArrayList bytes = new ArrayList();
        int cread = -1;
        boolean cont = true;
        while (cont) {
            cread = input.read();
            if (cread == -1) {
                cont = false;
            } else {
                bytePos++;
                bytes.add(new Integer(cread));
                if (cread == 10) {
                    cont = false;
                }
            }
        }
        if (bytes.size() == 0 && cread == -1) {
            return Node.createNothing();
        }
        byte[] buffer = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            buffer[i] = ((Integer) bytes.get(i)).byteValue();
        }
        String res1;
        if (charset != null) {
            res1 = new String(buffer, 0, buffer.length, charset);
        } else {
            res1 = new String(buffer, 0, buffer.length);
        }
        for (int i = 0; i < res1.length(); i++) {
            char c = res1.charAt(i);
            if ((c != 10) && (c != 13)) {
                res = res + c;
            }
        }
        return new Node(res);
    }

    public Node external_write(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(2);
        String res = "";
        byte[] buffer;
        if (charset != null) {
            buffer = startAt.getSubNode(1, Node.TYPE_STRING).getString().getBytes(charset);
        } else {
            buffer = startAt.getSubNode(1, Node.TYPE_STRING).getString().getBytes();
        }
        output.write(buffer);
        output.flush();
        bytePos += buffer.length;
        return null;
    }

    public Node external_writeln(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(2);
        String res = "";
        byte[] buffer;
        if (charset != null) {
            buffer = (startAt.getSubNode(1, Node.TYPE_STRING).getString() + "\n").getBytes(charset);
        } else {
            buffer = (startAt.getSubNode(1, Node.TYPE_STRING).getString() + "\n").getBytes();
        }
        output.write(buffer);
        output.flush();
        bytePos += buffer.length;
        return null;
    }

    public Node external_save(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(2);
        String serial2str = startAt.getSubNode(1, Node.VTYPE_VALUABLE).toString();
        byte[] sbuff = serial2str.getBytes("UTF-8");
        output.writeInt(sbuff.length);
        bytePos += 2;
        output.write(sbuff);
        bytePos += sbuff.length;
        output.flush();
        return null;
    }

    public Node external_save_gz(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(2);
        String serial2str = startAt.getSubNode(1, Node.VTYPE_VALUABLE).toString();
        byte[] sbuff = serial2str.getBytes("UTF-8");
        output.writeInt(sbuff.length);
        bytePos += 2;
        output.write(Tools.compress(sbuff));
        bytePos += sbuff.length;
        output.flush();
        return null;
    }

    public Node external_load(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        int bsize;
        try {
            bsize = input.readInt();
        } catch (EOFException eofe) {
            return Node.createNothing();
        }
        bytePos += 2;
        byte[] sbuff = new byte[bsize];
        int verif = input.read(sbuff);
        if (verif != bsize) {
            return Node.createNothing();
        }
        bytePos += sbuff.length;
        String serial2str = new String(sbuff, "UTF-8");
        Interpreter interpreter = Interpreter.interpr_getNewChildInterpreter();
        interpreter.setSource(serial2str);
        interpreter.compile();
        return interpreter.execute();
    }

    public Node external_load_gz(Node startAt) throws Exception {
        startAt.isGoodArgsCnt(1);
        int bsize;
        try {
            bsize = input.readInt();
        } catch (EOFException eofe) {
            return Node.createNothing();
        }
        bytePos += 2;
        byte[] sbuff = new byte[bsize];
        int verif = input.read(sbuff);
        if (verif != bsize) {
            return Node.createNothing();
        }
        bytePos += sbuff.length;
        String serial2str = new String(Tools.decompress(sbuff), "UTF-8");
        Interpreter interpreter = Interpreter.interpr_getNewChildInterpreter();
        interpreter.setSource(serial2str);
        interpreter.compile();
        return interpreter.execute();
    }

    public Object clone_my_self(Bivalence bival) {
        return null;
    }
}
