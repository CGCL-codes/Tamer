package hu.csq.dyneta.gui;

import java.util.ArrayList;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 *
 * @author Tamás Cséri
 */
public class LongCalculationExecuter<T> {

    /**
     *
     * @param calc
     * @param modal false is not supported yet.
     * @param GUI if set to false, the calculation will execute normally.
     * @param parent
     * @return
     */
    public T execute(final LongCalculation<T> calc, boolean modal, boolean GUI, java.awt.Frame parent) {
        if (!modal) {
            throw new UnsupportedOperationException("Modeless long calculations are not yet supported.");
        }
        if (GUI) {
            final ArrayList<T> alRes = new ArrayList<T>(1);
            final ArrayList<Exception> alEx = new ArrayList<Exception>(1);
            String calculationDescription = calc.calcString();
            final LongCalculationWindow lcw = new LongCalculationWindow(parent, modal, calculationDescription, new LongCalculationInterrupter(calc));
            Thread bgThread = new Thread(new Runnable() {

                public void run() {
                    try {
                        T res = calc.calc(new LongCalculationCallback() {

                            int currentTick = 0;

                            public void setString(String s) {
                                lcw.setExtendedText(s);
                            }

                            public void tick() {
                                tick(currentTick + 1);
                            }

                            public void tick(int current) {
                                currentTick = current;
                                lcw.setPBarCurr(current);
                            }

                            public void setMaxTick(int max) {
                                lcw.setPBarMax(max);
                            }
                        });
                        alRes.add(res);
                    } catch (Exception ex) {
                        Logger.getLogger(LongCalculationExecuter.class).log(Level.ERROR, "Error while executing the long calcultaion.", ex);
                        alEx.add(ex);
                    } finally {
                        java.awt.EventQueue.invokeLater(new Runnable() {

                            public void run() {
                                lcw.dispose();
                            }
                        });
                    }
                }
            }, "LongCalc");
            Logger.getLogger(LongCalculationExecuter.class).log(Level.DEBUG, "Starting a long calcultaion: " + calculationDescription);
            bgThread.start();
            lcw.setVisible(true);
            if (!alRes.isEmpty()) {
                return alRes.get(0);
            } else {
                throw new RuntimeException(alEx.get(0));
            }
        } else {
            return calc.calc(new LongCalculationCallbackConsole());
        }
    }
}

class LongCalculationCallbackConsole implements LongCalculationCallback {

    public void setString(String s) {
        Logger.getLogger(LongCalculationExecuter.class).log(Level.INFO, s);
    }

    public void tick() {
    }

    public void tick(int current) {
    }

    public void setMaxTick(int max) {
    }
}
