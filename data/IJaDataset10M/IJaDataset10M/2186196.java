package org.happy.commons.concurrent.loops.parallelFor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.happy.commons.concurrent.loops.ForTask_1x0;
import org.happy.commons.concurrent.loops.Loop_1x0;
import org.happy.commons.concurrent.loops.Parallel_1x0;

/**
 * this is very simple example demonstrating how to control the parallelFor during its
 * execution
 * 
 * @author Andreas Hollmann, Eugen Lofing, Wjatscheslaw Stoljarski
 * 
 */
public class ParallelFor_UserControl {

    public static void main(String args[]) {
        int numberOfTasks = (int) 1e6;
        JFrame frame = new JFrame("ParallelFor Cancel Sample");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton buttonStart = new JButton("start");
        JButton buttonCancel = new JButton("cancel");
        final JTextArea textArea = new JTextArea("");
        final AtomicInteger numberOfexecutedTasks = new AtomicInteger(0);
        final Loop_1x0 loop = Parallel_1x0.createFor(0, numberOfTasks, new ForTask_1x0() {

            public void iteration(final int i) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        textArea.setText("executedTasks=" + numberOfexecutedTasks.getAndIncrement() + "\n");
                    }
                });
            }
        });
        buttonStart.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                loop.start();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                loop.cancel();
                System.out.println("canceled");
            }
        });
        Container contentPane = frame.getContentPane();
        contentPane.add(textArea, BorderLayout.CENTER);
        contentPane.add(buttonStart, BorderLayout.NORTH);
        contentPane.add(buttonCancel, BorderLayout.SOUTH);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}
