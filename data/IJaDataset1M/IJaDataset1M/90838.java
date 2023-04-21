package wfg;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class SpecialPages {

    private String[] action;

    private BufferedOutputStream clientWrite;

    private PrintStream clientWriteText;

    public SpecialPages(String[] _action, BufferedOutputStream _clientWrite, PrintStream _clientWriteText) {
        action = _action;
        clientWrite = _clientWrite;
        clientWriteText = _clientWriteText;
        if (action[1].compareTo("/") == 0) {
            SendText(GenerateIndex(0));
            return;
        }
        String[] request = action[1].split("/");
        if (request.length > 2) {
            if (request[1].compareTo("index") == 0) {
                SendText(GenerateIndex(Long.valueOf(request[2])));
                return;
            }
            if (request[1].compareTo("getfile") == 0) {
                SendFile(DB.GetFile(request[2]), true);
                return;
            }
        }
        SendNotFound();
    }

    private void SendFile(File _file, boolean _forceDownload) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(_file);
        } catch (FileNotFoundException e) {
            SendNotFound();
            return;
        }
        clientWriteText.println("HTTP/1.1 200 OK");
        clientWriteText.println("Content-Length: " + _file.length());
        clientWriteText.println("Keep-Alive: timeout=5, max=64");
        clientWriteText.println("Connection: Keep-Alive");
        clientWriteText.println("Content-Disposition: inline; filename=\"" + DB.GetURLFileName(_file) + "\"");
        if (_forceDownload) clientWriteText.println("Content-Type: application/octet-stream");
        clientWriteText.println();
        clientWriteText.flush();
        int left = (int) _file.length();
        int len;
        byte[] b = new byte[1000];
        try {
            while (left > 0) {
                if (left < b.length) {
                    len = fis.read(b, 0, left);
                } else {
                    len = fis.read(b);
                }
                if (len > 0) {
                    clientWrite.write(b, 0, len);
                    left -= len;
                }
            }
            clientWrite.flush();
        } catch (IOException e) {
            return;
        }
    }

    private void SendNotFound() {
        String text = "<html><body>Not found</body></html>";
        clientWriteText.println("HTTP/1.1 404 Not Found");
        clientWriteText.println("Content-Length: " + text.length());
        clientWriteText.println("Keep-Alive: timeout=5, max=64");
        clientWriteText.println("Connection: Keep-Alive");
        clientWriteText.println("Content-Type: text/html");
        clientWriteText.println("Location: /");
        clientWriteText.println();
        clientWriteText.println(text);
    }

    private void SendText(String _text) {
        clientWriteText.println("HTTP/1.1 200 OK");
        clientWriteText.println("Content-Length: " + _text.length());
        clientWriteText.println("Keep-Alive: timeout=5, max=64");
        clientWriteText.println("Connection: Keep-Alive");
        clientWriteText.println("Content-Type: text/html");
        clientWriteText.println();
        clientWriteText.println(_text);
    }

    private String GenerateIndex(long _minFileSize) {
        return "<html><body><p><a href=\"/\">All</a> |<a href=\"/index/1000000\">Bigger than 1 Mo</a> | <a href=\"/index/10000000\">Bigger than 10 Mo</a></p><hr><p>" + DB.ListToHTML(_minFileSize) + "</p></body></html>";
    }
}
