package org.jquantlib.methods.montecarlo;

import java.util.Arrays;
import org.jquantlib.time.TimeGrid;

/**
 * Builds Wiener process paths using Gaussian variates
 * <p>
 * This class generates normalized (i.e., unit-variance) paths as sequences of variations. In order to obtain the actual path of the
 * underlying, the returned variations must be multiplied by the integrated variance (including time) over the corresponding time
 * step.
 *
 * @author Richard Gomes
 */
public class BrownianBridge {

    private final int size_;

    private final double[] t_;

    private final double[] sqrtdt_;

    private final int[] bridgeIndex_, leftIndex_, rightIndex_;

    private final double[] leftWeight_, rightWeight_, stdDev_;

    /**
     * unit-time path
     *
     * @param steps
     */
    public BrownianBridge(final int steps) {
        if (System.getProperty("EXPERIMENTAL") == null) throw new UnsupportedOperationException("Work in progress");
        this.size_ = steps;
        this.t_ = new double[this.size_];
        this.sqrtdt_ = new double[this.size_];
        this.bridgeIndex_ = new int[this.size_];
        this.leftIndex_ = new int[this.size_];
        this.rightIndex_ = new int[this.size_];
        this.leftWeight_ = new double[this.size_];
        this.rightWeight_ = new double[this.size_];
        this.stdDev_ = new double[this.size_];
        for (int i = 0; i < size_; ++i) {
            t_[i] = (i + 1);
        }
        initialize();
    }

    /**
     * generic times
     *
     * @note the starting time of the path is assumed to be 0 and must not be included
     *
     * @param times
     */
    public BrownianBridge(final double[] times) {
        this.size_ = times.length;
        this.t_ = Arrays.copyOfRange(times, 0, this.size_);
        this.sqrtdt_ = new double[this.size_];
        this.bridgeIndex_ = new int[this.size_];
        this.leftIndex_ = new int[this.size_];
        this.rightIndex_ = new int[this.size_];
        this.leftWeight_ = new double[this.size_];
        this.rightWeight_ = new double[this.size_];
        this.stdDev_ = new double[this.size_];
        initialize();
    }

    /**
     * generic times
     *
     * @param timeGrid
     */
    public BrownianBridge(final TimeGrid timeGrid) {
        this.size_ = timeGrid.size() - 1;
        this.t_ = new double[this.size_];
        this.sqrtdt_ = new double[this.size_];
        this.bridgeIndex_ = new int[this.size_];
        this.leftIndex_ = new int[this.size_];
        this.rightIndex_ = new int[this.size_];
        this.leftWeight_ = new double[this.size_];
        this.rightWeight_ = new double[this.size_];
        this.stdDev_ = new double[this.size_];
        for (int i = 0; i < size_; ++i) {
            t_[i] = timeGrid.get(i + 1);
        }
        initialize();
    }

    private void initialize() {
        sqrtdt_[0] = Math.sqrt(t_[0]);
        for (int i = 1; i < size_; ++i) {
            sqrtdt_[i] = Math.sqrt(t_[i] - t_[i - 1]);
        }
        final int[] map = new int[size_];
        Arrays.fill(map, 0);
        map[size_ - 1] = 1;
        bridgeIndex_[0] = size_ - 1;
        stdDev_[0] = Math.sqrt(t_[size_ - 1]);
        leftWeight_[0] = rightWeight_[0] = 0.0;
        for (int j = 0, i = 1; i < size_; ++i) {
            while (map[j] != 0) {
                ++j;
            }
            int k = j;
            while (map[k] == 0) {
                ++k;
            }
            final int l = j + ((k - 1 - j) >> 1);
            map[l] = i;
            bridgeIndex_[i] = l;
            leftIndex_[i] = j;
            rightIndex_[i] = k;
            if (j != 0) {
                leftWeight_[i] = (t_[k] - t_[l]) / (t_[k] - t_[j - 1]);
                rightWeight_[i] = (t_[l] - t_[j - 1]) / (t_[k] - t_[j - 1]);
                stdDev_[i] = Math.sqrt(((t_[l] - t_[j - 1]) * (t_[k] - t_[l])) / (t_[k] - t_[j - 1]));
            } else {
                leftWeight_[i] = (t_[k] - t_[l]) / t_[k];
                rightWeight_[i] = t_[l] / t_[k];
                stdDev_[i] = Math.sqrt(t_[l] * (t_[k] - t_[l]) / t_[k]);
            }
            j = k + 1;
            if (j >= size_) {
                j = 0;
            }
        }
    }

    public int size() {
        return size_;
    }

    public final double[] times() {
        return t_;
    }

    public void transform(final double[] input, final double[] output) {
        if (input == null || input.length == 0) throw new IllegalArgumentException("invalid sequence");
        if (input.length != size_) throw new IllegalArgumentException("incompatible sequence size");
        output[size_ - 1] = stdDev_[0] * input[0];
        for (int i = 1; i < size_; ++i) {
            final int j = leftIndex_[i];
            final int k = rightIndex_[i];
            final int l = bridgeIndex_[i];
            if (j != 0) {
                output[l] = leftWeight_[i] * output[j - 1] + rightWeight_[i] * output[k] + stdDev_[i] * input[i];
            } else {
                output[l] = rightWeight_[i] * output[k] + stdDev_[i] * input[i];
            }
        }
        for (int i = size_ - 1; i >= 1; --i) {
            output[i] -= output[i - 1];
            output[i] /= sqrtdt_[i];
        }
        output[0] /= sqrtdt_[0];
    }
}
