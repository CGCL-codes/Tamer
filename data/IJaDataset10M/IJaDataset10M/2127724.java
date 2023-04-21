package name.huzhenbo.java.algorithm.calculation;

class SuperSub {

    /**
     * Complexity: O(n2)
     * @param input
     * @return
     */
    public int[] loopGo(int[] input) {
        int max = 0;
        int start = -1, end = -1;
        for (int i = 0; i < input.length; i++) {
            int tempMax = 0;
            for (int j = i; j < input.length; j++) {
                tempMax += input[j];
                if (tempMax > max) {
                    max = tempMax;
                    start = i;
                    end = j;
                }
            }
        }
        return new int[] { start, end };
    }

    /**
     * Complexity: O(nlogn)
     * @param input
     * @return
     */
    public int divideGo(int[] input) {
        return _divideGo(input, 0, input.length - 1);
    }

    private int _divideGo(int[] input, int start, int end) {
        if (start > end) {
            return 0;
        }
        if (start == end) {
            return max(0, input[start]);
        }
        int m = (end + start) / 2;
        return max(toLeftMaxSum(input, start, m) + toRightMaxSum(input, m + 1, end), _divideGo(input, start, m), _divideGo(input, m + 1, end));
    }

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    private int max(int a, int b, int c) {
        return a > b ? (a > c ? a : c) : (b > c ? b : c);
    }

    private int toLeftMaxSum(int[] input, int start, int end) {
        int currentMax = 0;
        int max = 0;
        for (int i = end; i >= start; i--) {
            max += input[i];
            currentMax = max(currentMax, max);
        }
        return currentMax;
    }

    private int toRightMaxSum(int[] input, int start, int end) {
        int currentMax = 0;
        int max = 0;
        for (int i = start; i <= end; i++) {
            max += input[i];
            currentMax = max(currentMax, max);
        }
        return currentMax;
    }

    public int recursiveGo(int[] input) {
        return _recursiveGo(input, input.length - 1);
    }

    /**
     * This method actually is the recursive implementation of loopScan.
     * @param input
     * @param end
     * @return
     */
    private int _recursiveGo(int[] input, int end) {
        if (end < 0) {
            return 0;
        }
        if (end == 0) {
            return max(0, input[end]);
        }
        int toLeftMax = toLeftMaxSum(input, 0, end);
        return max(_recursiveGo(input, end - 1), toLeftMax);
    }

    /**
     * Complexity: O(n)
     * @param input
     * @return
     */
    public int scanGo(int[] input) {
        int maxProceed = 0;
        int maxSofar = 0;
        for (int i : input) {
            maxProceed = max(maxProceed + i, 0);
            maxSofar = max(maxSofar, maxProceed);
        }
        return maxSofar;
    }
}
