package org.velma.tools;

import java.util.Hashtable;
import java.text.DecimalFormat;
import org.velma.VELMA.ScorePanel;
import org.velma.data.msa.MSA;
import org.velma.data.msa.Tables;

/**
 * used to thread scoring alignments so that progress bar can be printed
 *
 * @author Hyun Kyu Shim
 *
 */
public class ScoreAlign {

    public ScoreAlign(MSA ma, double gapPen, double gapExt, String matrixName, ScorePanel scorePanel) {
        scoreRunner task = new scoreRunner(ma, gapPen, gapExt, matrixName, scorePanel);
        new Thread(task).start();
    }

    public class scoreRunner implements Runnable {

        private MSA ma;

        private double gapPen;

        private double gapExt;

        private String matrixName;

        private ScorePanel scorePanel;

        public scoreRunner(MSA ma, double gapPen, double gapExt, String matrixName, ScorePanel scorePanel) {
            this.ma = ma;
            this.gapPen = gapPen;
            this.gapExt = gapExt;
            this.matrixName = matrixName;
            this.scorePanel = scorePanel;
        }

        public void run() {
            double score = score();
            scorePanel.setScore(score);
            DecimalFormat format = new DecimalFormat("#.###");
            scorePanel.addMessage("\nSCORE: " + format.format(score));
            double totalgap = 0;
            int numSeq = ma.getSeqCount(), maxIndex = ma.getPosCount();
            float[][] counts = ma.getCounts();
            byte gap = Tables.AAencode("gap");
            for (int k = 0; k < maxIndex; k++) totalgap += counts[k][gap];
            scorePanel.addMessage("\nTotal " + Double.valueOf(totalgap).intValue() + " gaps out of " + (ma.getSeqCount() * maxIndex) + " (" + format.format(totalgap / (numSeq * maxIndex) * 100.0) + "%).");
            scorePanel.addMessage("\n" + format.format(totalgap / numSeq) + " gaps per sequence.");
            scorePanel.addMessage("\n" + format.format(maxIndex - (totalgap / numSeq)) + " amino acids per sequence (" + format.format((totalgap / numSeq) / (maxIndex - (totalgap / numSeq)) * 100.0) + "%).\n");
            scorePanel.progress(100);
            scorePanel.done();
        }

        public double score() {
            Hashtable<Character, Integer> letter2index = SubstitutionMatrixFactory.getSymbolMap();
            double score = 0;
            int numSeq = ma.getSeqCount(), maxIndex = ma.getPosCount(), pos1, pos2;
            byte gap = Tables.AAencode("gap");
            byte term = Tables.AAencode("TER");
            SubstitutionMatrixFactory factory = SubstitutionMatrixFactory.getSubstitutionMatrixFactory();
            Short matrix[][] = factory.getMatrix(matrixName);
            boolean gap1 = false, gap2 = false;
            byte curr1, curr2;
            int totalIter = ma.getSeqCount() * ma.getSeqCount() / 2;
            int iter = 0;
            for (int i = 0; i < numSeq - 1; i++) {
                for (int j = i + 1; j < numSeq; j++) {
                    iter++;
                    for (int k = 0; k < maxIndex; k++) {
                        curr1 = ma.get(i, k);
                        curr2 = ma.get(j, k);
                        if (curr1 == gap || curr1 == term) {
                            if (curr2 != gap && curr2 != term) if (gap1) score -= gapExt; else {
                                score -= gapPen;
                                gap1 = true;
                                gap2 = false;
                            }
                        } else if (curr2 == gap || curr2 == term) {
                            if (gap2) score -= gapExt; else {
                                score -= gapPen;
                                gap2 = true;
                                gap1 = false;
                            }
                        } else {
                            pos1 = letter2index.get(Tables.AAdecode(curr1));
                            pos2 = letter2index.get(Tables.AAdecode(curr2));
                            score += matrix[pos1 > pos2 ? pos1 : pos2][pos1 > pos2 ? pos2 : pos1];
                            gap1 = gap2 = false;
                        }
                    }
                }
                scorePanel.progress(iter * 100 / totalIter);
            }
            return score;
        }
    }
}
