package com.stuffthathappens.concurrency;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * Graphical CyclicBarrier demonstration, shows five progress bars racing
 * to the finish. They use CyclicBarrier to wait for all of the racers to
 * begin at the same time, then the buttons are re-enabled all at once
 * when all racers finish.
 *
 * @author Eric M. Burke
 */
public class CyclicBarrierDemo extends JFrame {

    private static final Random rand = new Random();

    private int racerFinishPosition;

    private static final int NUM_RACERS = 5;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                new CyclicBarrierDemo().setVisible(true);
            }
        });
    }

    public CyclicBarrierDemo() {
        super("CyclicBarrier Demonstration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container cp = getContentPane();
        cp.setLayout(new GridLayout(0, 1));
        CyclicBarrier startBarrier = new CyclicBarrier(NUM_RACERS);
        CyclicBarrier finishBarrier = new CyclicBarrier(NUM_RACERS);
        for (int i = 0; i < NUM_RACERS; i++) {
            cp.add(new RacerPanel(startBarrier, finishBarrier));
        }
        pack();
        setLocationRelativeTo(null);
    }

    public void finished(RacerPanel panel) {
        panel.setFinishPosition(1 + (racerFinishPosition++ % NUM_RACERS));
    }

    private class RacerPanel extends JPanel {

        private final CyclicBarrier startBarrier;

        private final CyclicBarrier finishBarrier;

        private final JProgressBar progressBar = new JProgressBar(0, 100);

        private final JLabel finishPositionLabel = new JLabel("?");

        private Action startAction = new AbstractAction("Start") {

            public void actionPerformed(ActionEvent e) {
                startClicked();
            }
        };

        public RacerPanel(CyclicBarrier startBarrier, CyclicBarrier finishBarrier) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            this.startBarrier = startBarrier;
            this.finishBarrier = finishBarrier;
            add(Box.createHorizontalStrut(5));
            add(new JButton(startAction));
            add(Box.createHorizontalStrut(5));
            add(progressBar);
            add(Box.createHorizontalStrut(5));
            add(finishPositionLabel);
            add(Box.createHorizontalStrut(5));
        }

        public void setFinishPosition(int pos) {
            finishPositionLabel.setText(Integer.toString(pos));
        }

        private void startClicked() {
            startAction.setEnabled(false);
            progressBar.setValue(0);
            finishPositionLabel.setText("?");
            progressBar.setIndeterminate(true);
            SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {

                protected Object doInBackground() throws Exception {
                    final int maxDelayMs = rand.nextInt(100);
                    startBarrier.await();
                    for (int i = 0; i < 100; i++) {
                        setProgress(i);
                        TimeUnit.MILLISECONDS.sleep(rand.nextInt(maxDelayMs));
                    }
                    setProgress(100);
                    return null;
                }

                protected void done() {
                    finished(RacerPanel.this);
                    new SwingWorker<Object, Object>() {

                        protected Object doInBackground() throws Exception {
                            finishBarrier.await();
                            return null;
                        }

                        protected void done() {
                            startAction.setEnabled(true);
                        }
                    }.execute();
                }
            };
            worker.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if ("progress".equals(evt.getPropertyName())) {
                        progressBar.setIndeterminate(false);
                        progressBar.setValue((Integer) evt.getNewValue());
                    }
                }
            });
            worker.execute();
        }
    }
}
