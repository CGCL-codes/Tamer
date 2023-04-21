package org.hanseltest;

/**
 * Switch statements to be covered by the testcases.
 * @author Niklas Mehner
 */
public class CoverSwitch {

    /** 
     * Simple switch.
     * @param i Switch parameter.
     * @return 1 if i=0, 2 if i=1, 3 if i=5, 4 otherwise.
     */
    public int coverSimpleSwitch(int i) {
        switch(i) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 5:
                return 3;
            default:
                return 4;
        }
    }

    /**
     * Switch without default.
     * @param i Switch parameter.
     * @return 2, or 3 if i=0.
     */
    public int coverNoDefault(int i) {
        int result = 2;
        switch(i) {
            case 0:
                result++;
        }
        return result;
    }

    /**
     * Switch translated to a tableswitch.
     * @param i Switch parameter.
     * @return 1 if i in [1..3], 2 if i in [4..6], 3 else.
     */
    public int coverSimpleSwitch2(int i) {
        int result;
        switch(i) {
            case 1:
            case 2:
            case 3:
                result = 1;
                break;
            case 4:
            case 5:
            case 6:
                result = 2;
                break;
            default:
                result = 3;
        }
        return result;
    }
}
