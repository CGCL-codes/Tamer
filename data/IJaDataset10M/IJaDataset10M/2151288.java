package ch.ethz.sg.cuttlefish.gui.widgets;

import javax.swing.JButton;
import ch.ethz.sg.cuttlefish.gui.BrowserWidget;
import ch.ethz.sg.cuttlefish.networks.ISimulation;

public class SimulationPanel extends BrowserWidget {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JButton stopButton = null;

    private JButton runButton = null;

    private JButton jButton2 = null;

    private JButton jButton3 = null;

    private Thread thread = null;

    private long sleepTime = 200;

    private boolean isRunning = true;

    /**
	 * This method initializes 
	 * 
	 */
    public SimulationPanel() {
        super();
        initialize();
        setNetworkClass(ISimulation.class);
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        this.add(getJButton3(), null);
        this.add(getStopButton(), null);
        this.add(getRunButton(), null);
        this.add(getJButton2(), null);
    }

    /**
	 * This method initializes stopButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getStopButton() {
        if (stopButton == null) {
            stopButton = new JButton();
            stopButton.setText("stop");
            stopButton.setEnabled(false);
            stopButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    thread = null;
                    isRunning = false;
                    stopButton.setEnabled(false);
                    runButton.setEnabled(true);
                }
            });
        }
        return stopButton;
    }

    /**
	 * This method initializes runButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getRunButton() {
        if (runButton == null) {
            runButton = new JButton();
            runButton.setText("run");
            runButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (thread == null) {
                        stopButton.setEnabled(true);
                        runButton.setEnabled(false);
                        thread = new Thread() {

                            @Override
                            public void run() {
                                isRunning = true;
                                while (isRunning) {
                                    isRunning = ((ISimulation) getNetwork()).update(sleepTime);
                                    System.out.println("RunChange");
                                    getBrowser().onNetworkChange();
                                    try {
                                        Thread.sleep(sleepTime);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        thread.start();
                    }
                }
            });
        }
        return runButton;
    }

    /**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton2() {
        if (jButton2 == null) {
            jButton2 = new JButton();
            jButton2.setText("step");
            jButton2.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ((ISimulation) getNetwork()).update(sleepTime);
                    System.out.println("StepChange");
                    getBrowser().onNetworkChange();
                }
            });
        }
        return jButton2;
    }

    /**
	 * This method initializes jButton3	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton3() {
        if (jButton3 == null) {
            jButton3 = new JButton();
            jButton3.setText("reset");
            jButton3.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ((ISimulation) getNetwork()).reset();
                    System.out.println("ResetChange");
                    getBrowser().onNetworkChange();
                }
            });
        }
        return jButton3;
    }

    @Override
    public void init() {
    }
}
