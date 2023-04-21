package org.elf.datalayer.kernel.impl.log;

import java.text.SimpleDateFormat;
import org.elf.datalayer.kernel.services.log.*;
import org.elf.datalayer.*;
import java.io.*;

/**
 * Implementaci�n de un KernelLogger, el cual guarda los mensajes en un archivo.
 * 
 * @author  <a href="mailto:logongas@users.sourceforge.net">Lorenzo Gonz�lez</a>
 */
public class KernelLoggerImplFile implements KernelLogger {

    private String _fileName;

    private LogLevel _levelThreshold;

    private String _prefixFileName;

    private String _extensionFileName;

    public void init(LogLevel levelThreshold) {
        _levelThreshold = levelThreshold;
        if (_fileName.lastIndexOf(".") == 0) {
            _prefixFileName = _fileName;
            _extensionFileName = null;
        } else if (_fileName.lastIndexOf(".") == _fileName.length() - 1) {
            _prefixFileName = _fileName.substring(0, _fileName.length() - 1);
            _extensionFileName = null;
        } else if (_fileName.lastIndexOf(".") < 0) {
            _prefixFileName = _fileName;
            _extensionFileName = null;
        } else {
            _prefixFileName = _fileName.substring(0, _fileName.lastIndexOf("."));
            _extensionFileName = _fileName.substring(_fileName.lastIndexOf(".") + 1);
        }
    }

    /**
	 * Establece el nombre del fichero sobre el que se genera el Log
	 * @param fileName Nombre del fichero sobre el que se genera el Log
	 */
    public void setFileName(String fileName) {
        _fileName = fileName;
    }

    public void log(String fqcn, LogLevel level, Object message, Throwable t) {
        try {
            if (_levelThreshold.ordinal() > level.ordinal()) {
                return;
            }
            DLDateTime dt = new DLDateTime();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String realFileName = _prefixFileName + "." + sdfDate.format(dt);
            if (_extensionFileName != null) {
                realFileName = realFileName + "." + _extensionFileName;
            }
            StringBuffer sb = new StringBuffer();
            sb.append(sdfDateTime.format(dt));
            sb.append("\t");
            sb.append(level.toString());
            sb.append("\t");
            sb.append(message);
            if (t != null) {
                StackTraceElement[] ste = t.getStackTrace();
                boolean print = false;
                for (int i = 0; i < ste.length; i++) {
                    if ((print == false) && (ste[i].getClassName().equals(fqcn))) {
                        print = true;
                    }
                    if (print == true) {
                        sb.append("\t");
                        sb.append(ste[i].getClassName() + "." + ste[i].getMethodName() + " " + ste[i].getLineNumber());
                    }
                }
            }
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(realFileName, true));
                out.write(sb.toString());
                out.newLine();
                out.flush();
                out.close();
            } catch (Exception ex) {
                System.out.println("ERROR Ha fallado la generaci�n del Log:\n");
                System.out.println(sb.toString());
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
