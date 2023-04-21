package dopisolver;

import java.util.*;

/**
 * Generates input sequences.
 * It supports a given number of consumers (aka the task that run in parallel).
 * After 
 */
class SolverInputGenerator implements Runnable {

    private GeneratorInput in;

    private int numInstances;

    private Collection<String> consumerNames;

    private Map<SolverInputInstance, ConsumedSolverInput> solverMap;

    private boolean finished;

    SolverInputGenerator(GeneratorInput in, int numInstances) {
        this.in = in;
        this.numInstances = numInstances;
        this.consumerNames = new HashSet<String>();
        this.finished = false;
        this.solverMap = new HashMap<SolverInputInstance, ConsumedSolverInput>();
    }

    /** 
     * Add a consumer with a given name.
     * Do it BEFORE {@link #run()} !!!
     * @param consumerName the name of the consumer
     */
    void addConsumer(String consumerName) {
        this.consumerNames.add(consumerName);
    }

    /** Remove the given consumer */
    synchronized void removeConsumer(String consumerName) {
        if (this.consumerNames.remove(consumerName)) {
            consolidateSolverMap();
        }
    }

    private void consolidateSolverMap() {
        Iterator<Map.Entry<SolverInputInstance, ConsumedSolverInput>> it = solverMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<SolverInputInstance, ConsumedSolverInput> entry = it.next();
            if (entry.getValue().consumed(this.consumerNames)) {
                it.remove();
            }
        }
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        for (int n = in.nMin(); n <= in.nMax(); n += in.nStep()) {
            for (int k = in.kMin(); k <= in.kMax(); k += in.kStep()) {
                SolverInput inputs[] = new SolverInput[this.numInstances];
                for (int i = 0; i < this.numInstances; i++) {
                    inputs[i] = RandomInputWork.newGeneratedInput(n, k, i, this.in.getGeneratedInputParentDir());
                }
                synchronized (this) {
                    this.solverMap.put(new SolverInputInstance(n, k), new ConsumedSolverInput(inputs));
                    while (true) {
                        if (this.finished || this.consumerNames.isEmpty()) {
                            return;
                        } else if (this.solverMap.size() > 5) {
                            this.notifyAll();
                            try {
                                this.wait(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }
                    }
                    this.notifyAll();
                }
            }
        }
        synchronized (this) {
            this.finished = true;
            this.notifyAll();
        }
    }

    /**
     * Consumes an input
     * @param consumerName
     * @return the solver input
     * @throws SolverException
     */
    synchronized SolverInput[] consumeInput(String consumerName, int n, int k) throws SolverException {
        ConsumedSolverInput consumedInput;
        SolverInputInstance key = new SolverInputInstance(n, k);
        while (true) {
            consumedInput = this.solverMap.get(key);
            if (consumedInput != null) {
                break;
            } else if (this.finished) {
                throw new SolverException("The input generator is already finished !!!");
            } else {
                try {
                    this.wait(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        consumedInput.registerConsumer(consumerName);
        if (consumedInput.consumed(this.consumerNames)) {
            this.solverMap.remove(key);
            this.notifyAll();
        }
        return consumedInput.getValues();
    }

    /** Cancels the sequence generator */
    public synchronized void cancel() {
        this.finished = true;
        this.notifyAll();
    }

    private static final class SolverInputInstance {

        private int n;

        private int k;

        SolverInputInstance(int n, int k) {
            this.n = n;
            this.k = k;
        }

        /**@see java.lang.Object#hashCode()*/
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + k;
            result = prime * result + n;
            return result;
        }

        /**@see java.lang.Object#equals(java.lang.Object)*/
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            final SolverInputInstance other = (SolverInputInstance) obj;
            if (k != other.k) return false;
            if (n != other.n) return false;
            return true;
        }
    }

    /** Groups the solver input instances and the consumers that already used them */
    private static final class ConsumedSolverInput {

        private SolverInput[] values;

        private HashSet<String> consumers;

        ConsumedSolverInput(SolverInput values[]) {
            this.values = values;
            this.consumers = new HashSet<String>();
        }

        void registerConsumer(String consumerName) {
            this.consumers.add(consumerName);
        }

        boolean consumed(Collection<String> knownConsumers) {
            return this.consumers.containsAll(knownConsumers);
        }

        SolverInput[] getValues() {
            return this.values;
        }
    }
}
