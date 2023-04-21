package httpServer;

import httpServer.MyEvent.PtzTyp;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;
import javax.swing.event.EventListenerList;

public class HTTPRequestHandler implements Runnable, MyListener {

    static final String CRLF = "\r\n";

    Socket socket;

    String wwwDir = "www";

    InputStream input;

    OutputStream output;

    BufferedReader br;

    private EventListenerList listeners = new EventListenerList();

    public HTTPRequestHandler(Socket socket) throws Exception {
        this.socket = socket;
        this.input = socket.getInputStream();
        this.output = socket.getOutputStream();
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @SuppressWarnings("boxing")
    private int getInt(String str) {
        int erg = 0;
        int pos = str.indexOf("%20");
        if (pos > 0) {
            if (str.indexOf('+', pos) > 0) pos++;
            try {
                erg = Integer.parseInt(str.substring(pos + 3));
            } catch (NumberFormatException e) {
                erg = 0;
            }
        }
        return erg;
    }

    private void processRequest() throws Exception {
        while (true) {
            String headerLine = br.readLine();
            if (headerLine.equals(CRLF) || headerLine.equals("")) break;
            StringTokenizer s = new StringTokenizer(headerLine);
            String temp = s.nextToken();
            if (temp.equals("GET")) {
                String fileName = s.nextToken();
                int pos = fileName.indexOf('?');
                if (pos > 0) {
                    String command = fileName.substring(pos + 1);
                    if (command.startsWith("zoomrel")) {
                        int x = getInt(command);
                        System.out.print("Zoom relative: ");
                        System.out.println(String.valueOf(x));
                        notifyMy(new MyEvent(this, PtzTyp.ZOOMREL, x));
                        getInt(command);
                    } else if (command.startsWith("zoomabs")) {
                        int x = getInt(command);
                        System.out.print("Zoom absolute: ");
                        System.out.println(String.valueOf(x));
                        notifyMy(new MyEvent(this, PtzTyp.ZOOMABS, x));
                    } else if (command.startsWith("uprel")) {
                        int x = getInt(command);
                        System.out.print("Up relative: ");
                        System.out.println(String.valueOf(x));
                        notifyMy(new MyEvent(this, PtzTyp.TILTREL, x));
                    } else if (command.startsWith("upabs")) {
                        int x = getInt(command);
                        System.out.print("Up absolute: ");
                        System.out.println(String.valueOf(x));
                        notifyMy(new MyEvent(this, PtzTyp.TILTABS, x));
                    } else if (command.startsWith("downrel")) {
                        int x = getInt(command);
                        System.out.print("Down relative: ");
                        System.out.println(String.valueOf(x));
                        notifyMy(new MyEvent(this, PtzTyp.TILTREL, x));
                    } else if (command.startsWith("downabs")) {
                        int x = getInt(command);
                        System.out.print("Down relative: ");
                        System.out.println(String.valueOf(x));
                        notifyMy(new MyEvent(this, PtzTyp.TILTABS, x));
                    } else if (command.startsWith("leftrel")) {
                        int x = getInt(command);
                        System.out.print("Left relative : ");
                        System.out.println(String.valueOf(x));
                        notifyMy(new MyEvent(this, PtzTyp.PANREL, x));
                    } else if (command.startsWith("leftabs")) {
                        int x = getInt(command);
                        System.out.print("Left absolute : ");
                        System.out.println(String.valueOf(x));
                        notifyMy(new MyEvent(this, PtzTyp.PANABS, x));
                    } else if (command.startsWith("rightrel")) {
                        int x = getInt(command);
                        System.out.print("Right relative: ");
                        System.out.println(String.valueOf(x));
                        notifyMy(new MyEvent(this, PtzTyp.PANREL, x));
                    } else if (command.startsWith("rightabs")) {
                        int x = getInt(command);
                        System.out.print("Right absolute: ");
                        System.out.println(String.valueOf(x));
                        notifyMy(new MyEvent(this, PtzTyp.PANABS, x));
                    } else if (command.startsWith("home")) {
                        System.out.print("Home");
                        notifyMy(new MyEvent(this, PtzTyp.HOME, 0));
                    }
                    fileName = fileName.substring(0, pos);
                }
                if (fileName.compareTo("/") == 0) fileName = "/ptz.html";
                fileName = "./" + wwwDir + fileName;
                FileInputStream fis = null;
                boolean fileExists = true;
                try {
                    fis = new FileInputStream(fileName);
                } catch (FileNotFoundException e) {
                    fileExists = false;
                }
                String serverLine = "Server: simple java httpServer";
                String statusLine = null;
                String contentTypeLine = null;
                String entityBody = null;
                String contentLengthLine = "error";
                if (fileExists) {
                    statusLine = "HTTP/1.0 200 OK" + CRLF;
                    contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
                    contentLengthLine = "Content-Length: " + (new Integer(fis.available())).toString() + CRLF;
                } else {
                    contentTypeLine = "text/html";
                    statusLine = "HTTP/1.0 404 Not Found" + CRLF;
                    entityBody = "<HTML>" + "<HEAD><TITLE>404 Not Found</TITLE></HEAD>" + "<BODY>404 Not Found</BODY></HTML>";
                    contentLengthLine = "Content-Length: " + (new Integer(entityBody.length())).toString() + CRLF;
                }
                output.write(statusLine.getBytes());
                output.write(serverLine.getBytes());
                output.write(contentTypeLine.getBytes());
                output.write(contentLengthLine.getBytes());
                output.write(CRLF.getBytes());
                if (fileExists) {
                    sendBytes(fis, output);
                    fis.close();
                } else {
                    output.write(entityBody.getBytes());
                }
            }
        }
        try {
            output.close();
            br.close();
            socket.close();
        } catch (Exception e) {
        }
    }

    private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
    }

    private static String contentType(String fileName) {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        }
        return "";
    }

    public void addMyListener(MyListener listener) {
        listeners.add(MyListener.class, listener);
    }

    public void removeMyListener(MyListener listener) {
        listeners.remove(MyListener.class, listener);
    }

    protected synchronized void notifyMy(MyEvent e) {
        for (MyListener l : listeners.getListeners(MyListener.class)) l.ptzMove(e);
    }

    public void ptzMove(MyEvent e) {
    }
}
