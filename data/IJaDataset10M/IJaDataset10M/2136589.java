package com.stakface.ocmd.util;

import java.io.*;
import com.stakface.ocmd.*;

public class StreamRedirector extends Thread {

    private OCmd _ocmd;

    private BufferedReader _in;

    private BufferedWriter _out;

    private String _prefix;

    public StreamRedirector(OCmd ocmd, InputStream in, OutputStream out, String prefix) {
        _ocmd = ocmd;
        _in = new BufferedReader(new InputStreamReader(in));
        _out = new BufferedWriter(new OutputStreamWriter(out));
        _prefix = prefix;
    }

    public void run() {
        String s;
        try {
            while ((s = _in.readLine()) != null) {
                if (_prefix != null) {
                    _out.write(_prefix);
                }
                _out.write(s);
                _out.write('\n');
            }
            _out.flush();
            if (_prefix != null) {
                _out.write(_prefix);
            }
            _out.write("--DONE--\n");
            _out.flush();
        } catch (IOException ioe) {
            _ocmd.logMessage(OCmd.LogType.LOG_SYSTEM, "Stream redirection error");
            _ocmd.logThrowable(ioe);
        }
    }
}
