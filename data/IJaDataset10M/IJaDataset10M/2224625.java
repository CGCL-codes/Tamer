package gov.sns.apps.diagnostics.corede.corede;

public class AbstractDataInput implements DataManipulator {

    public void cleanup() {
    }

    private boolean locked = false;

    private final Object lock = new Object();

    public boolean isLocked() {
        synchronized (lock) {
            return locked;
        }
    }

    /**
	 * Method setLocked
	 *
	 */
    public void setLocked(boolean l) {
        synchronized (lock) {
            locked = l;
            if (locked) propagateValues();
        }
    }

    protected void propagateValues() {
    }

    public boolean prepareToRun() throws CoredeException {
        return true;
    }

    public boolean setParameter(String pname, String value) throws CoredeException {
        return false;
    }

    public boolean setOutputDataValue(DataValue dv, int index) throws CoredeException {
        try {
            outputs[index] = dv;
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            return false;
        }
        return true;
    }

    public boolean setInputDataValue(DataValue dv, int index) throws CoredeException {
        return false;
    }

    public int getNumberOfInputs() {
        return 0;
    }

    public int getNumberOfOutputs() {
        return numOfOutputs;
    }

    public DataValue getInputDataValue(int index) {
        return null;
    }

    public DataValue getOutputDataValue(int index) {
        return outputs[index];
    }

    public void processData() throws CoredeException {
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int r) {
        if (r == 0) rank = 0; else rank = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
    }

    public int calculateRank() {
        if (rank == 0) return 0;
        for (int i = 0; i < numOfOutputs; i++) {
            DataValue dv = outputs[i];
            if (dv == null) return NULLASOUTPUT;
            int currank = dv.getRank();
            if (currank != -1) return FAILEDTORANK;
            outputs[i].setRank(0);
            rank = 0;
        }
        return rank;
    }

    public boolean isFilled() throws CoredeException {
        for (int i = 0; i < numOfOutputs; i++) {
            if (outputs[i] == null) throw new CoredeException("AbstractDataIput " + name + " has null output field " + i);
        }
        return true;
    }

    public AbstractDataInput(String s) {
        this(s, 1);
    }

    public AbstractDataInput() {
        this(1);
    }

    public AbstractDataInput(int outp) {
        this("DatInp" + globalCounter, outp);
        globalCounter++;
    }

    public AbstractDataInput(String n, int outp) {
        numOfOutputs = outp;
        name = n;
        outputs = new DataValue[outp];
    }

    protected String name = "";

    protected int numOfOutputs = 0;

    protected DataValue[] outputs = null;

    private static int globalCounter = 0;

    private int rank = -1;
}
