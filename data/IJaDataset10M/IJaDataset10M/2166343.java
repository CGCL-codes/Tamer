package emulator.shell.GUI.debugger;

import java.util.Observable;
import java.util.Observer;
import emulator.assembler.CodeAnalyzer;

public class AnalyzerThread extends Thread {

    private CodeAnalyzer analyzer;

    private AnalyzerObservable observable = new AnalyzerObservable();

    @Override
    public void run() {
        observable.setAnalyzerRunning(true);
        try {
            analyzer.runAnalysis();
        } finally {
            observable.setAnalyzerRunning(false);
        }
    }

    class AnalyzerObservable extends Observable {

        private boolean analyzer_running = false;

        /**
		 * @return the analyzer_running
		 */
        public boolean getAnalyzerRunning() {
            return analyzer_running;
        }

        /**
		 * @param analyzer_running the thread_running to set
		 */
        public void setAnalyzerRunning(boolean analyzer_running) {
            this.analyzer_running = analyzer_running;
            setChanged();
            notifyObservers(analyzer_running);
        }
    }

    /**
	 * @return the analyzer
	 */
    public CodeAnalyzer getAnalyzer() {
        return analyzer;
    }

    /**
	 * @param analyzer the analyzer to set
	 */
    public void setAnalyzer(CodeAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void addObserver(Observer codeAnalyzerObserver) {
        observable.addObserver(codeAnalyzerObserver);
    }
}
