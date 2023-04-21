package org.mbari.vars.dao;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.mbari.vars.annotation.model.VideoArchiveSet;
import org.mbari.vars.annotation.model.dao.VideoArchiveSetDAO;
import org.mbari.vars.dao.DAOEventQueue;
import org.mbari.vars.dao.DAOException;

/**
 * @author brian
 * @version $Id: DAOQueueTest.java 315 2006-07-10 02:53:24Z hohonuuli $
 */
public class DAOQueueTest extends TestCase {

    public DAOQueueTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(DAOQueueTest.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public void test1() {
        final String platform = "ATEST";
        final int seqNumber = 9999;
        final VideoArchiveSet vasNew = VideoArchiveSet.makeVideoArchiveSet(platform, seqNumber);
        VideoArchiveSetDAO dao = VideoArchiveSetDAO.getInstance();
        try {
            final VideoArchiveSet vas = dao.findByPlatformAndSequenceNumber(platform, seqNumber);
            if (vas != null) {
                System.out.println("Deleting old reference");
                dao.delete(vas);
            }
            final JFrame f = new JFrame(this.getClass().getName());
            f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            final JLabel lbl = new JLabel("Starting");
            f.getContentPane().add(lbl);
            f.pack();
            f.setVisible(true);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        lbl.setText("INSERT");
                        DAOEventQueue.insert(vasNew);
                        lbl.setText("UPDATE");
                        vasNew.setShipName("CHANGED");
                        DAOEventQueue.update(vasNew);
                        lbl.setText("DELETE");
                        DAOEventQueue.delete(vasNew);
                        lbl.setText("DONE");
                        DAOEventQueue.flush();
                        f.dispose();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (DAOException e) {
            fail("Unable to remove an existing test record from the database");
        }
    }
}
