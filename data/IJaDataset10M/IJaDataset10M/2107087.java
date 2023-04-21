package it.bancaditalia.mathengine.matlab;

import it.bancaditalia.bitsj.Tsmat;
import it.bancaditalia.mathengine.MathEngine;
import it.bancaditalia.mathengine.MathException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jmatlink.JMatLink;
import org.apache.log4j.Logger;

public class MatlabMathEngineJNI implements MathEngine {

    private static final Logger log;

    static {
        log = Logger.getLogger(MatlabMathEngineJNI.class.getName());
    }

    private JMatLink jmatlink;

    private boolean active;

    private long sessionID;

    private static int counter = 0;

    private String name;

    public MatlabMathEngineJNI() {
        name = "MatlabMathEngineJNI-" + (++counter);
        jmatlink = new JMatLink();
        sessionID = 0;
    }

    public void evaluate(String command) throws MathException, IOException {
        jmatlink.engEvalString(sessionID, command);
    }

    public void start() throws IOException {
        sessionID = jmatlink.engOpen();
        active = true;
    }

    public void stop() throws IOException {
        jmatlink.engClose(sessionID);
        active = false;
    }

    public boolean supportsMetaData() {
        return true;
    }

    public Tsmat getVariable(String name) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(name).append('=').append('[').append(name).append(".start(1)").append(' ').append(name).append(".start(2)").append(' ').append(name).append(".freq").append(' ').append(name).append(".matdata").append(' ').append(']');
        if (log.isDebugEnabled()) {
            log.debug(buffer.toString());
        }
        jmatlink.engEvalString(buffer.toString());
        double[][] array0 = jmatlink.engGetArray(sessionID, name);
        double[] array = array0[0];
        log.debug(array[0]);
        log.debug(array[1]);
        log.debug(array[2]);
        List<Double> list = new ArrayList<Double>();
        for (int i = 0; i < array.length - 3; i++) {
            list.add(array[3 + i]);
            if (log.isDebugEnabled()) {
                log.debug("numeri" + array[3 + i]);
            }
        }
        return new Tsmat((int) array[0], (int) array[1], (int) array[2], list);
    }

    public void putVariable(Tsmat mat, String name) {
        double[][] array0 = mat.getData().getArray();
        double[] array = new double[mat.getData().getRowCount()];
        for (int i = 0; i < array.length; i++) {
            array[i] = array0[i][0];
        }
        List<Double> listArray = new ArrayList<Double>();
        listArray.add((double) mat.getStart().getYear());
        listArray.add((double) mat.getStart().getPeriod());
        listArray.add((double) mat.getStart().getIntFreq());
        for (int i = 0; i < array.length; i++) {
            listArray.add(array[i]);
        }
        array = new double[listArray.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = listArray.get(i);
        }
        jmatlink.engPutArray(sessionID, name, array);
        StringBuffer buffer = new StringBuffer();
        buffer.append(name).append("=tsmat(").append(name).append("(1),").append(name).append("(2),").append(name).append("(3),").append(name).append("(4:end))");
        if (log.isDebugEnabled()) {
            log.debug(buffer.toString());
        }
        jmatlink.engEvalString(sessionID, buffer.toString());
    }

    public void clearSession() {
        jmatlink.engEvalString(sessionID, "clear all");
    }

    public boolean isStarted() {
        return active;
    }

    public String getName() {
        return name;
    }
}
