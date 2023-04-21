package it.bancaditalia.mathengine.matlab;

import it.bancaditalia.bitsj.Tsmat;
import it.bancaditalia.mathengine.MathEngine;
import it.bancaditalia.mathengine.MathException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class MatlabMathEngineJNA implements MathEngine {

    static Logger log;

    private static final String JNA_LIBRARY_PATH = "jna.library.path";

    private static final String MATHENGINE_PROP_FILENAME = "mathengine.properties";

    private Properties prop;

    MatlabNative matlabNative;

    MxArrayNative mxArrayNative;

    Pointer matlabPointer;

    private boolean active;

    private static int counter = 0;

    private String name;

    static {
        log = Logger.getLogger(MatlabMathEngineJNA.class.getName());
    }

    public MatlabMathEngineJNA() throws MathException {
        name = "MatlabMathEngineJNA-" + (++counter);
        InputStream is = null;
        try {
            is = getClass().getResourceAsStream(MATHENGINE_PROP_FILENAME);
            prop = new Properties();
            if (null == is) {
                log.warn("I can't find a mathengine.properties file!");
            } else {
                prop.load(is);
            }
            Properties sysProps = System.getProperties();
            String jnaLibraryPath = sysProps.getProperty(JNA_LIBRARY_PATH);
            if (jnaLibraryPath == null) {
                jnaLibraryPath = prop.getProperty(JNA_LIBRARY_PATH);
                if (jnaLibraryPath == null) {
                    log.warn("It is likely that everything won't work = unable to find jna.library.path system variable somewhere...");
                    log.warn("set the variable jna.library.path!!");
                    jnaLibraryPath = "/Applications/MATLAB_R2008bSV.app/bin/maci";
                }
            }
            sysProps.setProperty(JNA_LIBRARY_PATH, jnaLibraryPath);
            matlabNative = (MatlabNative) Native.loadLibrary("eng", MatlabNative.class);
            mxArrayNative = (MxArrayNative) Native.loadLibrary("mx", MxArrayNative.class);
            active = false;
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new MathException(e.toString());
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            }
        }
    }

    public boolean supportsMetaData() {
        return true;
    }

    public void evaluate(String command) throws MathException, IOException {
        matlabNative.engEvalString(matlabPointer, command);
    }

    public void putVariable(String varname, double dval) {
        Pointer value = mxArrayNative.mxCreateDoubleScalar(dval);
        matlabNative.engPutVariable(matlabPointer, varname, value);
    }

    public void start() throws IOException {
        log.trace("starting...");
        if (!active) {
            matlabPointer = matlabNative.engOpen("");
            active = true;
        }
        log.trace("started.");
    }

    public void stop() throws IOException {
        log.trace("stopping...");
        if (active) {
            matlabNative.engClose(matlabPointer);
        }
        active = false;
        log.trace("stopped.");
    }

    public boolean isActive() {
        return active;
    }

    public void putVariable(Tsmat mat, String name) {
        double[][] data = mat.getData().getArray();
        Pointer p = mxArrayNative.mxCreateDoubleMatrix(data.length, data[0].length, MxArrayNative.mxComplexity.mxREAL);
        matlabNative.engPutVariable(matlabPointer, name, p);
    }

    public Tsmat getVariable(String name) {
        return null;
    }

    public void removeVariable(String name) {
        Pointer p = matlabNative.engGetVariable(matlabPointer, name);
        mxArrayNative.mxDestroyArray(p);
    }

    public void clearSession() {
        matlabNative.engEvalString(matlabPointer, "clear all");
    }

    public boolean isStarted() {
        return active;
    }

    public String getName() {
        return name;
    }
}
