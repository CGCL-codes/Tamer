package javamail.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javamail.conn.tcpSockReadWrite;
import javamail.web.util.eContentTypes;

/**
 *
 * @author sriram
 */
public class webServerThread extends Thread {

    tcpSockReadWrite sock = null;

    public static final String webServerDir = "./";

    private boolean debugEnabled = false;

    public webServerThread(tcpSockReadWrite socket) {
        sock = socket;
    }

    @Override
    public void run() {
        doWork();
    }

    public void doWork() {
        String request = null;
        httpParser parse = new httpParser();
        do {
            request = sock.readLine();
            if (request != null) {
                if (debugEnabled) System.out.println(request);
            }
        } while (parse.parseLine(request));
        request = sock.readChars();
        if (request != null) {
            parse.parseLine(request);
            if (debugEnabled) System.out.println(request);
        }
        sendResponse(parse);
        sock.closeAllSockets();
    }

    public void sendResponse(httpParser p) {
        String fileName = p.getFileName();
        fileName = formatFileName(fileName);
        if (isServerSideScript(fileName)) {
            runServerScript(fileName, p.getParms());
        } else {
            sendFileContents(fileName);
        }
    }

    public String formatFileName(String fileName) {
        if (fileName.startsWith("/") == true) fileName = fileName.substring(1);
        if (fileName.equals("")) fileName = "index.html";
        if (!fileName.endsWith(".sss")) fileName = webServerDir + fileName;
        return fileName;
    }

    public void sendFileContents(String fileName) {
        httpGenerator gen = new httpGenerator();
        int count = 0;
        try {
            FileInputStream inFile = new FileInputStream(fileName);
            gen.addServer();
            gen.addContentType(fileName);
            sock.writeLine(gen.getResponseHeader());
            if (debugEnabled) System.out.println(gen.getResponseHeader());
            byte[] buffer = new byte[16384];
            while ((count = inFile.read(buffer)) > 0) sock.write(buffer, 0, count);
            inFile.close();
        } catch (FileNotFoundException e) {
            gen.add404NotFound();
            gen.addContentType(eContentTypes.HTML.toExt());
            sock.writeLine(gen.getResponseHeader());
            if (debugEnabled) System.out.println(gen.getResponseHeader());
            sock.writeLine(httpGenerator.getNotFoundResponse());
        } catch (IOException e) {
            sock.writeLine(e.getMessage());
        }
    }

    public boolean isServerSideScript(String fileName) {
        return fileName.endsWith(".sss");
    }

    public void runServerScript(String fileName, String parms) {
        httpGenerator gen = new httpGenerator();
        gen.addServer();
        gen.addContentType(fileName);
        if (isServerSideScript(fileName)) {
            sssParser s = new sssParser(parms);
            if (parms != null && !parms.equals("")) {
                s.parse();
            }
            if (fileName.toLowerCase().trim().equals("query.sss") || fileName.toLowerCase().trim().equals("action.sss")) {
                sock.writeLine(gen.getResponseHeader());
                if (debugEnabled) System.out.println(gen.getResponseHeader());
                String response = new String(s.processInput(fileName));
                sock.writeLine(response);
            } else {
                gen.add404NotFound();
                gen.addContentType(eContentTypes.HTML.toExt());
                sock.writeLine(gen.getResponseHeader());
                if (debugEnabled) System.out.println(gen.getResponseHeader());
                String response = new String(httpGenerator.inputInvalidResponse("Script file not found!"));
                sock.writeLine(response);
            }
        }
    }
}
