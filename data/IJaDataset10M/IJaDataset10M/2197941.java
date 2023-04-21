package br.ufsc.inf.guiga.media.codec.video.h264.vcl.mode.decision;

/**
 * Error Metric measures the energy of the residual transform coefficients after
 * quantization.
 * 
 * @author Guilherme Ferreira <guiga@inf.ufsc.br>
 */
public interface DistortionMetric {

    /**
     * Computes the distortion between two 16x16 blocks.
     * 
     * @param orig the original block.
     * @param pred the predicted block.
     * @return an integer number representing the distortion between the two
     *         blocks. The number magnitude tends to be lower as the two blocks
     *         match closer.
     */
    public int getDistortion16x16(int[][] orig, int[][] pred);

    /**
     * Computes the distortion between two 4x4 blocks.
     * 
     * @param orig the original block.
     * @param pred the predicted block.
     * @param pos_y a vertical offset shall be provide if the blocks height is
     *            wider than 4 elements.
     * @param pos_x a horizontal offset shall be provide if the blocks width is
     *            wider than 4 elements.
     * @return an integer number representing the distortion between the two
     *         blocks. The number magnitude tends to be lower as the two blocks
     *         match closer.
     */
    public int getDistortion4x4(int[][] orig, int[][] pred, int pos_y, int pos_x);
}
