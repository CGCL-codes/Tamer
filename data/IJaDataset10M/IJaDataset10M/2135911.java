package com.mockturtlesolutions.snifflib.linalg;

import com.mockturtlesolutions.snifflib.datatypes.Subscript;
import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;
import java.util.Vector;

/**
Class for scheduling stages of Givens rotation within Modi and Clarke's
QR decomposition.
*/
public class ModiClarkeScheduler {

    private int m;

    private int n;

    private int s;

    private int[] Z;

    private int current_stage;

    private Vector Queue;

    private int needed_zeros;

    private int provided_zeros;

    private DblMatrix trace;

    public ModiClarkeScheduler(int M, int N) {
        this(M, N, 1);
    }

    /**
	Specifies the number of processors (or threads) that
	will be used.
	*/
    public ModiClarkeScheduler(int M, int N, int S) {
        this.m = M;
        this.n = N;
        this.s = S;
        this.provided_zeros = 0;
        this.needed_zeros = this.m - 1;
        for (int i = 1; i < this.n; i++) {
            this.needed_zeros += this.m - i - 1;
        }
        this.Z = new int[this.n + 1];
        this.Z[0] = this.m;
        for (int i = 1; i < this.Z.length; i++) {
            this.Z[i] = 0;
        }
        this.current_stage = 0;
        this.Queue = new Vector();
        int[] siz = new int[2];
        siz[0] = this.m;
        siz[1] = this.n;
        this.trace = new DblMatrix(siz);
        this.advanceStage();
    }

    /**
	Allocates a new queue corresponding to the next stage of Given's rotations.
	*/
    private void advanceStage() {
        int new_zeros = 0;
        Vector index;
        if (this.Queue.size() != 0) {
            throw new IllegalArgumentException("Trying to get a new queue but old queue is not empty!");
        }
        Subscript[] subs = new Subscript[2];
        subs[0] = new Subscript(1);
        subs[1] = new Subscript(1);
        int toBeZero;
        for (int i = this.Z.length - 1; i >= 1; i--) {
            new_zeros = ((Z[i - 1] - Z[i]) / 2);
            for (int j = 0; j < new_zeros; j++) {
                index = new Vector(3);
                toBeZero = this.m - Z[i] - j - 1;
                index.add(new Integer(toBeZero));
                index.add(new Integer(toBeZero - new_zeros));
                index.add(new Integer(i - 1));
                this.Queue.add(0, index);
                subs[0].Value.setDoubleAt(new Double(toBeZero), 0);
                subs[1].Value.setDoubleAt(new Double(i - 1), 0);
                this.trace.setSubMatrix(this.current_stage, subs);
            }
            Z[i] += new_zeros;
        }
        if (this.Queue.size() == 0) {
            throw new IllegalArgumentException("Queue is zero size");
        }
        this.current_stage++;
    }

    /**
	Get the next set of indices.  If the number in the current stage left to be done is greater than or 
	equal to s then s indices will be returned.  Otherwise only the remaining number of indices will
	be returned.  Returns null if no more zeros are required in the matrix.
	*/
    public Vector getNext() {
        Vector out = null;
        if (this.provided_zeros < this.needed_zeros) {
            if (this.Queue.size() < this.s) {
                out = this.Queue;
                this.Queue = new Vector();
                this.advanceStage();
            } else {
                out = new Vector(this.s);
                for (int i = 0; i < this.s; i++) {
                    out.add(this.Queue.remove(i));
                }
            }
            this.provided_zeros += out.size();
        }
        return (out);
    }
}
