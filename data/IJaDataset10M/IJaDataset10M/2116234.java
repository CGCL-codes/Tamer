package org.gvsig.gui.beans.incrementabletask;

import org.gvsig.gui.beans.progresspanel.LogControl;

/**
 * Test del IncrementableTask
 *
 * @version 27/05/2007
 * @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
 */
public class TestIncrementableTask {

    class ClassProcess implements Runnable, IIncrementable, IncrementableListener {

        int i = 0;

        long j = 0;

        LogControl log = new LogControl();

        IncrementableTask incrementableTask = null;

        private volatile Thread blinker;

        public ClassProcess() {
        }

        public void start() {
            blinker = new Thread(this);
            blinker.start();
        }

        public synchronized void stop() {
            blinker = null;
            notify();
        }

        public boolean isAlive() {
            return blinker.isAlive();
        }

        public void procesoDuro() throws InterruptedException {
            try {
                for (long k = 0; k <= 8000; k++) for (long n = 0; n <= 5000; n++) {
                    for (long l = 0; l <= 100; l++) ;
                    if (Thread.currentThread().isInterrupted()) throw new InterruptedException();
                }
                System.out.println("b");
            } finally {
                System.out.println("c");
            }
        }

        public synchronized void run() {
            try {
                procesoDuro();
            } catch (InterruptedException e1) {
                System.out.println("Se ha salido");
            }
            incrementableTask.processFinalize();
        }

        public String getLabel() {
            return "Generando estad�sticas, por favor, espere...";
        }

        public String getLog() {
            return log.getText();
        }

        public int getPercent() {
            return (int) ((j * 100) / 65535);
        }

        public String getTitle() {
            return "Barra de progreso";
        }

        public void setIncrementableTask(IncrementableTask value) {
            incrementableTask = value;
        }

        public void actionCanceled(IncrementableEvent e) {
            blinker.interrupt();
        }

        public void actionResumed(IncrementableEvent e) {
        }

        public void actionSuspended(IncrementableEvent e) {
        }

        public void setLabel(String label) {
        }

        public boolean isSuspended() {
            return false;
        }

        public void resume() {
        }

        public void suspend() {
        }

        public boolean isCancelable() {
            return false;
        }

        public void process() throws InterruptedException, Exception {
        }

        public void setCancelable(boolean b) {
        }

        public boolean isPausable() {
            return false;
        }

        public void setPausable(boolean b) {
        }
    }

    ClassProcess classProcess = null;

    public TestIncrementableTask() {
        super();
        initialize();
    }

    private void initialize() {
        classProcess = new ClassProcess();
        IncrementableTask incrementableTask = new IncrementableTask(classProcess);
        classProcess.setIncrementableTask(incrementableTask);
        incrementableTask.showWindow();
        incrementableTask.addIncrementableListener(classProcess);
        incrementableTask.start();
        classProcess.start();
        while (classProcess.isAlive()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        classProcess.stop();
        classProcess = null;
        incrementableTask = null;
    }

    public static void main(String[] args) {
        new TestIncrementableTask();
    }
}
