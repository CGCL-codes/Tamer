package simulatorKit.resultsAnalysis;

import java.util.ArrayList;
import java.util.Iterator;

/** @author Igaler 
 * 
 *  This class represent the single node in topology for statistical uses
 */
public class ResultNode {

    /** list of number of parts that was recived per round */
    protected ArrayList<Integer> roundInput;

    /** list of number of parts that was recived per round */
    protected ArrayList<Integer> roundOutput;

    /** defoult c-tor */
    ResultNode() {
        this.roundInput = new ArrayList<Integer>();
        this.roundOutput = new ArrayList<Integer>();
    }

    /**
     * copy c-tor
     * @param other - node to build from
     */
    ResultNode(ResultNode other) {
        this.roundInput = new ArrayList<Integer>();
        this.roundOutput = new ArrayList<Integer>();
    }

    /**
     * this method update the statistics of node according to sended number of parts
     * @param numOfParts number of parts that uploaded by this node
     */
    void sent(int numOfParts) {
        if (this.roundOutput.size() <= ResultAnalyser.ROUND_NUM) {
            while (this.roundOutput.size() < ResultAnalyser.ROUND_NUM) this.roundOutput.add(null);
            this.roundOutput.add(new Integer(numOfParts));
            return;
        }
        Integer currOutput = (Integer) this.roundOutput.get(ResultAnalyser.ROUND_NUM);
        this.roundOutput.set(ResultAnalyser.ROUND_NUM, new Integer(currOutput.intValue() + numOfParts));
    }

    /**
     * this method update the statistics of node according to recived number of parts
     * @param numOfParts number of parts that downloaded by this node
     */
    void recive(int numOfParts) {
        if (this.roundInput.size() <= ResultAnalyser.ROUND_NUM) {
            while (this.roundInput.size() < ResultAnalyser.ROUND_NUM) this.roundInput.add(null);
            this.roundInput.add(new Integer(numOfParts));
            return;
        }
        Integer currInput = (Integer) this.roundInput.get(ResultAnalyser.ROUND_NUM);
        this.roundInput.set(ResultAnalyser.ROUND_NUM, new Integer(currInput.intValue() + numOfParts));
    }

    /**
     * calc and return the stress of node in given round
     * @param index - number of round
     * @return number of parts that uploaded by this node in given round
     */
    public int getRoundStress(int index) {
        Integer currInt = (Integer) this.roundOutput.get(index);
        if (currInt != null) return currInt.intValue();
        return 0;
    }

    /**
     * calc and return the total stress(upload) of node
     * @return stress value of this node
     */
    public long getStress() {
        long answer = 0;
        Iterator itr = this.roundOutput.iterator();
        Integer currInt;
        while (itr.hasNext()) {
            currInt = (Integer) itr.next();
            if (currInt != null) answer += currInt.intValue();
        }
        return answer;
    }

    /**
     * clac and return the per round stress(upload) of given node
     * @return array of per round stress of this node
     */
    public long[] getPerRoundStress() {
        if (this.getStress() == 0) return null;
        long[] answer = new long[this.roundOutput.size()];
        Iterator itr = this.roundOutput.iterator();
        for (int i = 0; itr.hasNext(); i++) {
            Integer curr = (Integer) itr.next();
            if (curr == null) {
                answer[i] = 0;
            } else {
                answer[i] = curr.intValue();
            }
        }
        return answer;
    }
}
