package mupi;

import java.io.Serializable;
import java.util.Vector;

public class Data implements Serializable {

    private String address;

    private boolean pingSuccessful;

    private Vector<Vector<String>> resultVector;

    private Vector<Vector<String>> paintVector;

    private boolean pingActive;

    private boolean runnable;

    private int maxResult;

    public Data() {
        address = "";
        pingSuccessful = false;
        resultVector = new Vector<Vector<String>>();
        paintVector = new Vector<Vector<String>>();
        pingActive = false;
        runnable = true;
        maxResult = 5;
    }

    public boolean isRunnable() {
        return runnable;
    }

    public void setRunnable(boolean runnable) {
        this.runnable = runnable;
    }

    public boolean isPingActive() {
        return pingActive;
    }

    public void setPingActive(boolean pingActive) {
        this.pingActive = pingActive;
    }

    public boolean isPingSuccessful() {
        return pingSuccessful;
    }

    public void setPingSuccessful(boolean pingSuccessful) {
        this.pingSuccessful = pingSuccessful;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Vector<String> getResultAt(int i) {
        return resultVector.elementAt(i);
    }

    public int getResultSize() {
        return resultVector.size();
    }

    public void removeResultAt(int i) {
        resultVector.removeElementAt(i);
    }

    public void addResult(Vector<String> result) {
        resultVector.addElement(result);
        rebuildPaintVector();
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    private void rebuildPaintVector() {
        Vector<Vector<String>> newOutput = new Vector();
        int maximum = 0;
        int value = 0;
        String svalue = "";
        if (resultVector.size() > 90) {
            for (int i = 0; i < 90; i++) {
                newOutput.add(resultVector.elementAt(resultVector.size() - 90 + i));
                value = Integer.parseInt(newOutput.elementAt(i).elementAt(1));
                if (value > maximum) maximum = value;
            }
        } else {
            for (int i = 0; i < resultVector.size(); i++) {
                newOutput.add(resultVector.elementAt(i));
                value = Integer.parseInt(newOutput.elementAt(i).elementAt(1));
                if (value > maximum) maximum = value;
            }
        }
        maxResult = maximum;
        paintVector = (Vector) newOutput.clone();
    }

    public Vector<Vector<String>> getPaintVector() {
        return paintVector;
    }

    public void setPaintVector(Vector<Vector<String>> paintVector) {
        this.paintVector = paintVector;
    }

    public int getPaintSize() {
        return paintVector.size();
    }

    public Vector<String> getPaintResultAt(int i) {
        return paintVector.elementAt(i);
    }
}
