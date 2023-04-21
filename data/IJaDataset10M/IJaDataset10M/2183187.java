package org.neblinux.nebliserver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.neblinux.nebliserver.configuracion.Carpeta;
import org.neblinux.nebliserver.configuracion.Configuracion;
import org.neblinux.nebliserver.scripts.ProcesadorScript;

class HTTPSession implements Runnable {

    private static final Logger log = Logger.getLogger(HTTPSession.class);

    private static final Pattern PATTERN_SPLIT_AMPERSAND = Pattern.compile("\\&");

    private static final Pattern PATTERN_SPLIT_SPACE = Pattern.compile("\\s+");

    private final String codificacion;

    private Configuracion configuracion;

    private Socket mySocket;

    public HTTPSession(Socket s, Configuracion conf) throws HTTPSesionException {
        this.codificacion = conf.getCodificacion();
        this.configuracion = conf;
        this.mySocket = s;
    }

    public Response archivo(File f, Hashtable<String, String> header) {
        try {
            String mime = null;
            int dot = f.getCanonicalPath().lastIndexOf('.');
            if (dot >= 0) {
                mime = configuracion.obtenerMime((f.getCanonicalPath().substring(dot + 1).toLowerCase()));
            }
            long startFrom = 0;
            String range = header.get("Range");
            if (range != null) {
                if (range.startsWith("bytes=")) {
                    range = range.substring("bytes=".length());
                    int minus = range.indexOf('-');
                    if (minus > 0) {
                        range = range.substring(0, minus);
                    }
                    try {
                        startFrom = Long.parseLong(range);
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
            FileInputStream fis = new FileInputStream(f);
            fis.skip(startFrom);
            Response r = new Response(HTTPConstantes.HTTP_OK, mime, fis);
            r.addHeader("Content-length", "" + (f.length() - startFrom));
            r.addHeader("Content-range", "" + startFrom + "-" + (f.length() - 1) + "/" + f.length());
            return r;
        } catch (IOException ioe) {
            return new Response(HTTPConstantes.HTTP_FORBIDDEN, HTTPConstantes.MIME_PLAINTEXT, "Error 404, archivo no encontrado");
        }
    }

    private void decodeParms(String parms, Hashtable<String, String> p) {
        decodeParms(parms, p, codificacion);
    }

    private void decodeParms(String parms, Hashtable<String, String> p, String codificacion) {
        if (parms == null) {
            return;
        }
        String par[] = PATTERN_SPLIT_AMPERSAND.split(parms);
        for (String s : par) {
            int sep = s.indexOf('=');
            if (sep >= 0) {
                String llave = s.substring(0, sep).trim();
                String valor = Utilidades.decodificar(s.substring(sep + 1).trim(), codificacion);
                p.put(llave, valor);
            }
        }
    }

    private Response procesarArchivo(Carpeta carpeta, String uri, Hashtable<String, String> header, Hashtable<String, String> get, Hashtable<String, String> post) throws UnsupportedEncodingException {
        String ext = uri.substring(uri.lastIndexOf(".") + 1);
        ProcesadorScript motor = carpeta.getMotor();
        if (motor.isMiExtension(ext)) {
            return motor.procesarScript(carpeta.getRaiz(), uri, header, get, post);
        }
        return archivo(new File(carpeta.getRaiz(), uri), header);
    }

    public void run() {
        try {
            InputStream is = mySocket.getInputStream();
            if (is == null) {
                mySocket.close();
                return;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String[] request = PATTERN_SPLIT_SPACE.split(in.readLine());
            String method = null;
            String uri = null;
            Hashtable<String, String> parmsGet = new Hashtable<String, String>();
            Hashtable<String, String> parmsPost = new Hashtable<String, String>();
            Hashtable<String, String> parmsHeader = new Hashtable<String, String>();
            if (request.length > 1) {
                method = request[0];
                uri = request[1].trim();
                log.info("uri->" + uri);
                int qmi = uri.indexOf('?');
                if (qmi >= 0) {
                    log.info(">>>>> decodificando parms GET");
                    this.decodeParms(uri.substring(qmi + 1), parmsGet);
                    uri = uri.substring(0, qmi);
                }
                parmsHeader.put("uri", uri);
            } else {
                sendError(HTTPConstantes.HTTP_BADREQUEST, "BAD REQUEST: Syntax error. Usage: GET /example/file.html");
                return;
            }
            String line = in.readLine();
            while (line.trim().length() > 0) {
                int p = line.indexOf(':');
                parmsHeader.put(line.substring(0, p).trim().toLowerCase(), line.substring(p + 1).trim());
                line = in.readLine();
            }
            if (method.equalsIgnoreCase("POST")) {
                long size = 0x7FFFFFFFFFFFFFFFl;
                String contentLength = parmsHeader.get("content-length");
                if (contentLength != null) {
                    try {
                        size = Integer.parseInt(contentLength);
                    } catch (NumberFormatException ex) {
                    }
                }
                String postLine = "";
                char buf[] = new char[2048];
                int read = in.read(buf);
                while (read >= 0 && size > 0 && !postLine.endsWith("\r\n")) {
                    size -= read;
                    postLine += String.valueOf(buf, 0, read);
                    if (size > 0) {
                        read = in.read(buf);
                    }
                }
                postLine = postLine.trim();
                log.info(">>>>> decodificando parms POST");
                this.decodeParms(postLine, parmsPost);
            }
            Response r = serve(uri, method, parmsHeader, parmsGet, parmsPost);
            if (r == null) {
                sendError(HTTPConstantes.HTTP_INTERNALERROR, "ERROR interno del servidor: el metodo 'Serve()' retorna una respuesta null.");
            } else {
                sendResponse(r.getStatus(), r.getMimeType(), r.getHeader(), r.getData());
            }
            in.close();
            is.close();
        } catch (IOException ioe) {
            try {
                sendError(HTTPConstantes.HTTP_INTERNALERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            } catch (Throwable t) {
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
	 * Returns an error message as a HTTP response and throws
	 * InterruptedException to stop furhter request processing.
	 */
    private void sendError(String status, String msg) throws InterruptedException {
        sendResponse(status, HTTPConstantes.MIME_PLAINTEXT, null, new ByteArrayInputStream(msg.getBytes()));
        throw new InterruptedException();
    }

    /**
	 * Sends given response to the socket.
	 */
    private void sendResponse(String status, String mime, Properties header, InputStream data) {
        try {
            if (status == null) {
                throw new Error("sendResponse(): Status can't be null.");
            }
            log.info("responde: " + status);
            OutputStream out = mySocket.getOutputStream();
            OutputStreamWriter pw = new OutputStreamWriter(out, codificacion);
            pw.write("HTTP/1.1 " + status + " \r\n");
            if (mime != null) {
                pw.write("Content-Type: " + mime + "\r\n");
            }
            pw.write("Server: NebliServer 0.2\r\n");
            Date ahora = new Date();
            if (header == null || header.getProperty("Date") == null) {
                pw.write("Date: " + HTTPConstantes.gmtFrmt.format(ahora) + "\r\n");
            }
            if (header != null) {
                Enumeration<Object> e = header.keys();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    String value = header.getProperty(key);
                    pw.write(key + ": " + value + "\r\n");
                }
            }
            pw.write("Connection: close\r\n");
            pw.write("\r\n");
            pw.flush();
            if (data != null) {
                byte[] buff = new byte[2048];
                while (true) {
                    int read = data.read(buff, 0, 2048);
                    if (read <= 0) {
                        break;
                    }
                    out.write(buff, 0, read);
                }
            } else {
                log.error("sin datos!!!");
            }
            pw.flush();
            pw.close();
            out.flush();
            out.close();
            if (data != null) {
                data.close();
            }
        } catch (IOException ioe) {
            log.error(ioe.toString());
            System.out.println(ioe.toString());
        } finally {
            try {
                mySocket.close();
            } catch (IOException e) {
            }
        }
    }

    private Response serve(String uri, String method, Hashtable<String, String> header, Hashtable<String, String> get, Hashtable<String, String> post) throws UnsupportedEncodingException {
        if (uri.startsWith("..") || uri.endsWith("..") || uri.indexOf("../") >= 0) {
            return new Response(HTTPConstantes.HTTP_FORBIDDEN, HTTPConstantes.MIME_PLAINTEXT, "FORBIDDEN: Acceso a recurso no autorizado por seguridad.");
        }
        for (Carpeta c : configuracion.getCarpetas()) {
            if (uri.startsWith(c.getAlias())) {
                uri = uri.substring(c.getAlias().length());
                File ar = new File(c.getRaiz(), uri);
                if (ar.exists()) {
                    if (ar.isDirectory()) {
                        for (String in : c.getIndices()) {
                            File f = new File(ar, in);
                            if (f.exists()) {
                                uri = uri + in;
                                break;
                            }
                        }
                    }
                    return procesarArchivo(c, uri, header, get, post);
                } else {
                    return new Response(HTTPConstantes.HTTP_FORBIDDEN, HTTPConstantes.MIME_PLAINTEXT, "Error 404, archivo no encontrado");
                }
            }
        }
        File webRoot = configuracion.getRaiz().getRaiz();
        File ar = new File(webRoot, uri);
        if (ar.exists()) {
            if (ar.isDirectory()) {
                for (String in : configuracion.getRaiz().getIndices()) {
                    File f = new File(ar, in);
                    if (f.exists()) {
                        uri = uri + in;
                        break;
                    }
                }
            }
            return procesarArchivo(configuracion.getRaiz(), uri, header, get, post);
        } else {
            return new Response(HTTPConstantes.HTTP_FORBIDDEN, HTTPConstantes.MIME_PLAINTEXT, "Error 404, archivo no encontrado");
        }
    }
}
