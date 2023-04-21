package jOptical.ImageProcessor.LocalOperators;

import jOptical.Bitmap.Pixel;
import jOptical.ImageProcessor.ImageChannel;
import jOptical.ImageProcessor.Operation;
import jOptical.ImageProcessor.Window;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * This class is responsible for performing a smoothening operation on an image.
 * It is called from LocalOperation.java. Depending on the number of available
 * CPU's this implementation will only perform smoothening on a small fraction
 * of the image to distribute the load among the available CPU's.
 * 
 * @author dennis
 */
public final class SmootheningOperation implements Operation {

    private Thread t;

    private Window w;

    private Pixel[][] bitmap, bitmap2;

    private long[][] window;

    private long norm;

    private ImageChannel channel;

    private CyclicBarrier barrier;

    /**
	 * Creates a new SmootheningOperation class.
	 * 
	 * @param bitmap
	 *            resulting bitmap.
	 * @param bitmap2
	 *            input bitmap.
	 * @param channel
	 *            red/green/blue/RGB or null for grayscale.
	 * @param operator
	 *            operator window from OperatorWindow()
	 * @param w
	 *            a rectangle describing the boundaries of this operation.
	 * @param barrier
	 *            thread synchronization.
	 */
    private SmootheningOperation(Pixel[][] bitmap, Pixel[][] bitmap2, ImageChannel channel, OperatorWindow operator, Window w, CyclicBarrier barrier) {
        this.w = w;
        this.bitmap = bitmap;
        this.bitmap2 = bitmap2;
        this.barrier = barrier;
        this.channel = channel;
        this.window = operator.window;
        this.norm = operator.norm;
        this.t = new Thread(this);
        this.t.start();
    }

    protected SmootheningOperation() {
    }

    @Override
    public void run() {
        int[] median = null;
        int[] medianR = null, medianG = null, medianB = null;
        int borderWidth = window.length / 2;
        long sum = 0;
        long sumR = 0, sumG = 0, sumB = 0;
        int i2 = 0, j2 = 0;
        int m = 0;
        if (norm == -1) {
            median = new int[window.length * window.length];
            medianR = new int[window.length * window.length];
            medianG = new int[window.length * window.length];
            medianB = new int[window.length * window.length];
        }
        for (int i = w.getX(), n = (w.getX() + w.getXLength()); i < n; i++) {
            for (int j = w.getY(), k = (w.getY() + w.getYLength()); j < k; j++) {
                for (int iw = 0; iw < window.length; iw++) {
                    i2 = i - borderWidth + iw;
                    for (int jw = 0; jw < window.length; jw++) {
                        j2 = j - borderWidth + jw;
                        if (channel == null) {
                            if (norm == -1) {
                                median[m++] = bitmap2[i2][j2].getGrayscaleValue();
                            } else {
                                sum += window[iw][jw] * bitmap2[i2][j2].getGrayscaleValue();
                            }
                        } else {
                            switch(channel) {
                                case Blue:
                                    {
                                        if (norm == -1) {
                                            medianB[m++] = bitmap2[i2][j2].blue;
                                        } else {
                                            sumB += window[iw][jw] * bitmap2[i2][j2].blue;
                                        }
                                        break;
                                    }
                                case Green:
                                    {
                                        if (norm == -1) {
                                            medianG[m++] = bitmap2[i2][j2].green;
                                        } else {
                                            sumG += window[iw][jw] * bitmap2[i2][j2].green;
                                        }
                                        break;
                                    }
                                case Red:
                                    {
                                        if (norm == -1) {
                                            medianR[m++] = bitmap2[i2][j2].red;
                                        } else {
                                            sumR += window[iw][jw] * bitmap2[i2][j2].red;
                                        }
                                        break;
                                    }
                                case RGB:
                                    {
                                        if (norm == -1) {
                                            medianR[m] = bitmap2[i2][j2].red;
                                            medianG[m] = bitmap2[i2][j2].green;
                                            medianB[m] = bitmap2[i2][j2].blue;
                                            m++;
                                        } else {
                                            sumR += window[iw][jw] * bitmap2[i2][j2].red;
                                            sumG += window[iw][jw] * bitmap2[i2][j2].green;
                                            sumB += window[iw][jw] * bitmap2[i2][j2].blue;
                                        }
                                        break;
                                    }
                            }
                        }
                    }
                }
                if (channel == null) {
                    if (norm == -1) {
                        Arrays.sort(median);
                        if (window.length % 2 == 1) {
                            bitmap[i][j].setGrayscaleValue(median[window.length + 1]);
                        } else {
                            sum = median[window.length] + median[window.length + 1];
                            bitmap[i][j].setGrayscaleValue((int) sum / 2);
                        }
                        Arrays.fill(median, 0);
                        m = 0;
                    } else {
                        bitmap[i][j].setGrayscaleValue((int) (sum / norm));
                        sum = 0;
                    }
                } else {
                    switch(channel) {
                        case Blue:
                            {
                                if (norm == -1) {
                                    Arrays.sort(medianB);
                                    if (window.length % 2 == 1) {
                                        bitmap[i][j].blue = medianB[window.length + 1];
                                    } else {
                                        sum = medianB[window.length] + medianB[window.length + 1];
                                        bitmap[i][j].blue = (int) (sum / 2);
                                    }
                                    Arrays.fill(medianB, 0);
                                    m = 0;
                                } else {
                                    bitmap[i][j].blue = (int) (sumB / norm);
                                    sumB = 0;
                                }
                                break;
                            }
                        case Green:
                            {
                                if (norm == -1) {
                                    Arrays.sort(medianG);
                                    if (window.length % 2 == 1) {
                                        bitmap[i][j].green = medianG[window.length + 1];
                                    } else {
                                        sum = medianG[window.length] + medianG[window.length + 1];
                                        bitmap[i][j].green = (int) (sum / 2);
                                    }
                                    Arrays.fill(medianG, 0);
                                    m = 0;
                                } else {
                                    bitmap[i][j].green = (int) (sumG / norm);
                                    sumG = 0;
                                }
                                break;
                            }
                        case Red:
                            {
                                if (norm == -1) {
                                    Arrays.sort(median);
                                    if (window.length % 2 == 1) {
                                        bitmap[i][j].red = medianR[window.length + 1];
                                    } else {
                                        sum = medianR[window.length] + medianR[window.length + 1];
                                        bitmap[i][j].red = (int) (sum / 2);
                                    }
                                    Arrays.fill(medianR, 0);
                                    m = 0;
                                } else {
                                    bitmap[i][j].red = (int) (sumR / norm);
                                    sumR = 0;
                                }
                                break;
                            }
                        case RGB:
                            {
                                if (norm == -1) {
                                    Arrays.sort(medianR);
                                    Arrays.sort(medianG);
                                    Arrays.sort(medianB);
                                    if (window.length % 2 == 1) {
                                        bitmap[i][j].red = medianR[window.length + 1];
                                        bitmap[i][j].green = medianG[window.length + 1];
                                        bitmap[i][j].blue = medianB[window.length + 1];
                                    } else {
                                        sum = medianR[window.length] + medianR[window.length + 1];
                                        bitmap[i][j].red = (int) (sum / 2);
                                        sum = medianG[window.length] + medianG[window.length + 1];
                                        bitmap[i][j].green = (int) (sum / 2);
                                        sum = medianB[window.length] + medianB[window.length + 1];
                                        bitmap[i][j].blue = (int) (sum / 2);
                                    }
                                    Arrays.fill(medianR, 0);
                                    Arrays.fill(medianG, 0);
                                    Arrays.fill(medianB, 0);
                                    m = 0;
                                } else {
                                    bitmap[i][j].red = (int) (sumR / norm);
                                    sumR = 0;
                                    bitmap[i][j].green = (int) (sumG / norm);
                                    sumG = 0;
                                    bitmap[i][j].blue = (int) (sumB / norm);
                                    sumB = 0;
                                }
                                break;
                            }
                    }
                }
            }
        }
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Operation setupOperation(Pixel[][] bitmap1, Pixel[][] bitmap2, Pixel[][] bitmap3, ImageChannel channel, OperatorWindow operator, Object parameter, Window w, CyclicBarrier barrier) {
        if (operator.norm == 0) throw new IllegalArgumentException("Norm must not be null, illegal operator");
        if (!w.isValid(bitmap1) || !w.isValid(bitmap2)) throw new IllegalArgumentException("Window is invalid");
        return (new SmootheningOperation(bitmap1, bitmap2, channel, operator, w, barrier));
    }
}
