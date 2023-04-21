package schedsim.ui;

import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.util.Vector;
import schedsim.schedbase.*;
import schedsim.helper.*;

/**
 * <br>
 * Beschreibung:<br>
 * <br>
 * 
 * <br>
 * <pre>
 * Datum:           07.01.2004
 * Class:			ProcessInfoOrigin.java
 * 
 * ****************************************************************************
 * History
 * ****************************************************************************
 * Date			Author			Description
 * 09.01.2004 semamiga inital processes state added
 * 12.01.2004 semamiga added blocking visualization
 * ****************************************************************************
 * </pre>
 * 
 * @author         Martin Migasiewicz
 * @version		1.0
 */
public class ProcessInfoOrigin extends JPanel {

    public BufferedImage image = null;

    private SchedSimInternalMainFrame schedmain = null;

    private Vector procVec = null;

    private JScrollPane scrollPane = null;

    private int width = 20;

    private int height = 23;

    private int border = 30;

    private int y_diff = height + 10;

    /**
	 * 
	 */
    public ProcessInfoOrigin(SchedSimInternalMainFrame schedmain) {
        super();
        this.schedmain = schedmain;
        this.setBackground(new Color(255, 255, 255));
        this.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        (this.image.getGraphics()).setColor(this.getBackground());
        (this.image.getGraphics()).fillRect(0, 0, image.getWidth(), image.getHeight());
        this.procVec = this.schedmain.runner.getSchedulerInterface().GetProcessList().GetAllProcesses();
    }

    public void drawOriginProcesses(Vector processes) {
        int size = processes.size();
        int maxDeadline = 0;
        int maxBlockLength = 0;
        GeneralProcess currProc = null;
        for (int i = 0; i < size; ++i) {
            if (((GeneralProcess) processes.get(i)).GetDeadlineTime() > maxDeadline) maxDeadline = ((GeneralProcess) processes.get(i)).GetDeadlineTime();
            if (((GeneralProcess) processes.get(i)).GetBlockingLength() > maxBlockLength) maxBlockLength = ((GeneralProcess) processes.get(i)).GetBlockingLength();
        }
        if (maxDeadline == 0) {
            for (int i = 0; i < size; ++i) {
                if ((((GeneralProcess) processes.get(i)).GetReadyTime() + ((GeneralProcess) processes.get(i)).GetExecutionTime()) > maxDeadline) {
                    maxDeadline = (((GeneralProcess) processes.get(i)).GetReadyTime() + ((GeneralProcess) processes.get(i)).GetExecutionTime());
                }
            }
        }
        int y = size * y_diff + y_diff + border;
        int x = this.width * maxDeadline + this.width + border;
        if (processes.isEmpty()) {
            x = 1;
            y = 1;
        }
        this.image = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
        Graphics g = this.image.getGraphics();
        g.setColor(this.getBackground());
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        for (int i = 1; i <= size; ++i) {
            currProc = ((GeneralProcess) processes.get(i - 1));
            g.setColor(Helper.generateColor(currProc.GetProcessID()));
            g.setFont(new Font("sans serif", Font.BOLD, 10));
            g.drawString("P" + currProc.GetProcessID(), 1, (height / 2) + i * y_diff);
            int start = currProc.GetReadyTime();
            for (int j = start; j <= maxDeadline + maxBlockLength; ++j) {
                if (j == start + currProc.GetBlockingTime()) {
                    g.setColor(new Color(10, 10, 10));
                    for (int k = j; k < start + currProc.GetBlockingTime() + currProc.GetBlockingLength(); ++k, ++j) g.fillRect((this.width * k) + width, i * y_diff, this.width, this.height);
                }
                if (j < start + currProc.GetExecutionTime() + currProc.GetBlockingLength()) {
                    g.setColor(Helper.generateColor(currProc.GetProcessID()));
                    g.fillRect((this.width * j) + width, i * y_diff, this.width, this.height);
                }
                if (j == currProc.GetDeadlineTime() + currProc.GetBlockingLength()) {
                    g.setColor(Helper.generateColor(currProc.GetProcessID()));
                    g.drawRect(this.width * (currProc.GetExecutionTime() + start + currProc.GetBlockingLength()) + this.width, i * y_diff, this.width * (currProc.GetDeadlineTime() - (currProc.GetExecutionTime() + start + currProc.GetBlockingLength())), this.height - 1);
                    g.drawLine(this.width * (currProc.GetExecutionTime() + start + currProc.GetBlockingLength()) + this.width + this.width * (currProc.GetDeadlineTime() - (currProc.GetExecutionTime() + start + currProc.GetBlockingLength())) - 1, i * y_diff, this.width * (currProc.GetExecutionTime() + start + currProc.GetBlockingLength()) + this.width + this.width * (currProc.GetDeadlineTime() - (currProc.GetExecutionTime() + start + currProc.GetBlockingLength())) - 1, (i * y_diff) + height - 1);
                }
            }
        }
        g.setColor(new Color(0, 0, 0));
        this.drawGrid(g, x, y);
        this.schedmain.procInfoPanel.scrollPane.setViewportView(this);
    }

    private void drawGrid(Graphics g, int width, int height) {
        g.setFont(new Font("sans serif", Font.BOLD, 10));
        g.drawString("Time", 5, height - border - 1);
        g.drawLine(5, height - border, width, height - border);
        for (int j = 0; j < width; ++j) {
            if (j % 5 == 0) g.setColor(new Color(0, 0, 0)); else g.setColor(new Color(100, 100, 100));
            g.drawLine((this.width * j) + this.width, y_diff - 5, (this.width * j) + this.width, y_diff + height - (border + 20));
            g.drawString(String.valueOf(j), (this.width * j) + this.width - 5, height - border + 10);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) return;
        g.drawImage(image, 0, 0, this);
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    public Dimension getMinimumSize() {
        return new Dimension(this.image.getWidth(), this.image.getHeight());
    }

    public Rectangle getBounds() {
        return new Rectangle(this.image.getWidth(), this.image.getHeight());
    }
}
