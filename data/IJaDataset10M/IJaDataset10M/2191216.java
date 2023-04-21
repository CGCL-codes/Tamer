package moa.classifiers;

import moa.AbstractMOAObject;

/**
 * ADaptive sliding WINdow method. This method is a change detector and estimator.
 * It keeps a variable-length window of recently seen
 * items, with the property that the window has the maximal length statistically
 * consistent with the hypothesis "there has been no change in the average value
 * inside the window".
 *
 *
 * @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
 * @version $Revision: 7 $
 */
public class ADWIN extends AbstractMOAObject {

    private class List {

        protected int count;

        protected ListItem head;

        protected ListItem tail;

        public List() {
            clear();
            addToHead();
        }

        public int size() {
            return this.count;
        }

        public ListItem head() {
            return this.head;
        }

        public ListItem tail() {
            return this.tail;
        }

        public boolean isEmpty() {
            return (this.size() == 0);
        }

        public void clear() {
            this.head = null;
            this.tail = null;
            this.count = 0;
        }

        public void addToHead() {
            this.head = new ListItem(this.head, null);
            if (this.tail == null) {
                this.tail = this.head;
            }
            this.count++;
        }

        public void removeFromHead() {
            this.head = this.head.next();
            if (this.head != null) {
                this.head.setPrevious(null);
            } else {
                this.tail = null;
            }
            this.count--;
            return;
        }

        public void addToTail() {
            this.tail = new ListItem(null, this.tail);
            if (this.head == null) {
                this.head = this.tail;
            }
            this.count++;
        }

        public void removeFromTail() {
            this.tail = this.tail.previous();
            if (this.tail == null) {
                this.head = null;
            } else {
                this.tail.setNext(null);
            }
            this.count--;
            return;
        }
    }

    private class ListItem {

        protected ListItem next;

        protected ListItem previous;

        protected int bucketSizeRow = 0;

        protected int MAXBUCKETS = ADWIN.MAXBUCKETS;

        protected double bucketTotal[] = new double[MAXBUCKETS + 1];

        protected double bucketVariance[] = new double[MAXBUCKETS + 1];

        public ListItem() {
            this(null, null);
        }

        public void clear() {
            bucketSizeRow = 0;
            for (int k = 0; k <= MAXBUCKETS; k++) {
                clearBucket(k);
            }
        }

        private void clearBucket(int k) {
            setTotal(0, k);
            setVariance(0, k);
        }

        public ListItem(ListItem nextNode, ListItem previousNode) {
            this.next = nextNode;
            this.previous = previousNode;
            if (nextNode != null) {
                nextNode.previous = this;
            }
            if (previousNode != null) {
                previousNode.next = this;
            }
            clear();
        }

        public void insertBucket(double Value, double Variance) {
            int k = bucketSizeRow;
            bucketSizeRow++;
            setTotal(Value, k);
            setVariance(Variance, k);
        }

        public void RemoveBucket() {
            compressBucketsRow(1);
        }

        public void compressBucketsRow(int NumberItemsDeleted) {
            for (int k = NumberItemsDeleted; k <= MAXBUCKETS; k++) {
                bucketTotal[k - NumberItemsDeleted] = bucketTotal[k];
                bucketVariance[k - NumberItemsDeleted] = bucketVariance[k];
            }
            for (int k = 1; k <= NumberItemsDeleted; k++) {
                clearBucket(MAXBUCKETS - k + 1);
            }
            bucketSizeRow -= NumberItemsDeleted;
        }

        public ListItem previous() {
            return this.previous;
        }

        public void setPrevious(ListItem previous) {
            this.previous = previous;
        }

        public ListItem next() {
            return this.next;
        }

        public void setNext(ListItem next) {
            this.next = next;
        }

        public double Total(int k) {
            return bucketTotal[k];
        }

        public double Variance(int k) {
            return bucketVariance[k];
        }

        public void setTotal(double value, int k) {
            bucketTotal[k] = value;
        }

        public void setVariance(double value, int k) {
            bucketVariance[k] = value;
        }
    }

    public static final double DELTA = .002;

    private static final int mintMinimLongitudWindow = 10;

    private double mdbldelta = .002;

    private int mintTime = 0;

    private int mintClock = 32;

    private double mdblWidth = 0;

    public static final int MAXBUCKETS = 5;

    private int lastBucketRow = 0;

    private double TOTAL = 0;

    private double VARIANCE = 0;

    private int WIDTH = 0;

    private int BucketNumber = 0;

    private int Detect = 0;

    private int numberDetections = 0;

    private int DetectTwice = 0;

    private boolean blnBucketDeleted = false;

    private int BucketNumberMAX = 0;

    private int mintMinWinLength = 5;

    private List listRowBuckets;

    public boolean getChange() {
        return blnBucketDeleted;
    }

    public void resetChange() {
        blnBucketDeleted = false;
    }

    public int getBucketsUsed() {
        return BucketNumberMAX;
    }

    public int getWidth() {
        return WIDTH;
    }

    public void setClock(int intClock) {
        mintClock = intClock;
    }

    public int getClock() {
        return mintClock;
    }

    public boolean getWarning() {
        return false;
    }

    public boolean getDetect() {
        return (Detect == mintTime);
    }

    public int getNumberDetections() {
        return numberDetections;
    }

    public double getTotal() {
        return TOTAL;
    }

    public double getEstimation() {
        return TOTAL / WIDTH;
    }

    public double getVariance() {
        return VARIANCE / WIDTH;
    }

    public double getWidthT() {
        return mdblWidth;
    }

    private void initBuckets() {
        listRowBuckets = new List();
        lastBucketRow = 0;
        TOTAL = 0;
        VARIANCE = 0;
        WIDTH = 0;
        BucketNumber = 0;
    }

    private void insertElement(double Value) {
        WIDTH++;
        insertElementBucket(0, Value, listRowBuckets.head());
        double incVariance = 0;
        if (WIDTH > 1) {
            incVariance = (WIDTH - 1) * (Value - TOTAL / (WIDTH - 1)) * (Value - TOTAL / (WIDTH - 1)) / WIDTH;
        }
        VARIANCE += incVariance;
        TOTAL += Value;
        compressBuckets();
    }

    private void insertElementBucket(double Variance, double Value, ListItem Node) {
        Node.insertBucket(Value, Variance);
        BucketNumber++;
        if (BucketNumber > BucketNumberMAX) {
            BucketNumberMAX = BucketNumber;
        }
    }

    private int bucketSize(int Row) {
        return (int) Math.pow(2, Row);
    }

    public int deleteElement() {
        ListItem Node;
        Node = listRowBuckets.tail();
        int n1 = bucketSize(lastBucketRow);
        WIDTH -= n1;
        TOTAL -= Node.Total(0);
        double u1 = Node.Total(0) / n1;
        double incVariance = Node.Variance(0) + n1 * WIDTH * (u1 - TOTAL / WIDTH) * (u1 - TOTAL / WIDTH) / (n1 + WIDTH);
        VARIANCE -= incVariance;
        Node.RemoveBucket();
        BucketNumber--;
        if (Node.bucketSizeRow == 0) {
            listRowBuckets.removeFromTail();
            lastBucketRow--;
        }
        return n1;
    }

    public void compressBuckets() {
        int n1, n2;
        double u2, u1, incVariance;
        ListItem cursor;
        ListItem nextNode;
        cursor = listRowBuckets.head();
        int i = 0;
        do {
            int k = cursor.bucketSizeRow;
            if (k == MAXBUCKETS + 1) {
                nextNode = cursor.next();
                if (nextNode == null) {
                    listRowBuckets.addToTail();
                    nextNode = cursor.next();
                    lastBucketRow++;
                }
                n1 = bucketSize(i);
                n2 = bucketSize(i);
                u1 = cursor.Total(0) / n1;
                u2 = cursor.Total(1) / n2;
                incVariance = n1 * n2 * (u1 - u2) * (u1 - u2) / (n1 + n2);
                nextNode.insertBucket(cursor.Total(0) + cursor.Total(1), cursor.Variance(0) + cursor.Variance(1) + incVariance);
                BucketNumber++;
                cursor.compressBucketsRow(2);
                if (nextNode.bucketSizeRow <= MAXBUCKETS) {
                    break;
                }
            } else {
                break;
            }
            cursor = cursor.next();
            i++;
        } while (cursor != null);
    }

    public boolean setInput(double intEntrada) {
        return setInput(intEntrada, mdbldelta);
    }

    public boolean setInput(double intEntrada, double delta) {
        boolean blnChange = false;
        boolean blnExit = false;
        ListItem cursor;
        mintTime++;
        insertElement(intEntrada);
        blnBucketDeleted = false;
        if (mintTime % mintClock == 0 && getWidth() > mintMinimLongitudWindow) {
            boolean blnReduceWidth = true;
            while (blnReduceWidth) {
                blnReduceWidth = false;
                blnExit = false;
                int n0 = 0;
                int n1 = WIDTH;
                double u0 = 0;
                double u1 = getTotal();
                double v0 = 0;
                double v1 = VARIANCE;
                double n2 = 0;
                double u2 = 0;
                cursor = listRowBuckets.tail();
                int i = lastBucketRow;
                do {
                    for (int k = 0; k <= (cursor.bucketSizeRow - 1); k++) {
                        n2 = bucketSize(i);
                        u2 = cursor.Total(k);
                        if (n0 > 0) {
                            v0 += cursor.Variance(k) + (double) n0 * n2 * (u0 / n0 - u2 / n2) * (u0 / n0 - u2 / n2) / (n0 + n2);
                        }
                        if (n1 > 0) {
                            v1 -= cursor.Variance(k) + (double) n1 * n2 * (u1 / n1 - u2 / n2) * (u1 / n1 - u2 / n2) / (n1 + n2);
                        }
                        n0 += bucketSize(i);
                        n1 -= bucketSize(i);
                        u0 += cursor.Total(k);
                        u1 -= cursor.Total(k);
                        if (i == 0 && k == cursor.bucketSizeRow - 1) {
                            blnExit = true;
                            break;
                        }
                        double absvalue = (double) (u0 / n0) - (u1 / n1);
                        if ((n1 > mintMinWinLength + 1 && n0 > mintMinWinLength + 1) && blnCutexpression(n0, n1, u0, u1, v0, v1, absvalue, delta)) {
                            blnBucketDeleted = true;
                            Detect = mintTime;
                            if (Detect == 0) {
                                Detect = mintTime;
                            } else if (DetectTwice == 0) {
                                DetectTwice = mintTime;
                            }
                            blnReduceWidth = true;
                            blnChange = true;
                            if (getWidth() > 0) {
                                n0 -= deleteElement();
                                blnExit = true;
                                break;
                            }
                        }
                    }
                    cursor = cursor.previous();
                    i--;
                } while (((!blnExit && cursor != null)));
            }
        }
        mdblWidth += getWidth();
        if (blnChange) {
            numberDetections++;
        }
        return blnChange;
    }

    private boolean blnCutexpression(int n0, int n1, double u0, double u1, double v0, double v1, double absvalue, double delta) {
        int n = getWidth();
        double dd = Math.log(2 * Math.log(n) / delta);
        double v = getVariance();
        double m = ((double) 1 / ((n0 - mintMinWinLength + 1))) + ((double) 1 / ((n1 - mintMinWinLength + 1)));
        double epsilon = Math.sqrt(2 * m * v * dd) + (double) 2 / 3 * dd * m;
        return (Math.abs(absvalue) > epsilon);
    }

    public ADWIN() {
        mdbldelta = DELTA;
        initBuckets();
        Detect = 0;
        numberDetections = 0;
        DetectTwice = 0;
    }

    public ADWIN(double d) {
        mdbldelta = d;
        initBuckets();
        Detect = 0;
        numberDetections = 0;
        DetectTwice = 0;
    }

    public ADWIN(int cl) {
        mdbldelta = DELTA;
        initBuckets();
        Detect = 0;
        numberDetections = 0;
        DetectTwice = 0;
        mintClock = cl;
    }

    public String getEstimatorInfo() {
        return "ADWIN;;";
    }

    public void setW(int W0) {
    }

    @Override
    public void getDescription(StringBuilder sb, int indent) {
    }
}
