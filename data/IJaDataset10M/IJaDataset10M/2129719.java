package edu.co.unal.bioing.jnukak3d.VolumeRendering.ui;

import edu.co.unal.bioing.jnukak3d.VolumeRendering.sliceBasedVolumeRendering.SliceBasedUI;
import edu.co.unal.bioing.jnukak3d.VolumeRendering.transferFunction.TransferFunctionCanvas;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TransfeFunctionPanel extends JPanel {

    private TransferFunctionCanvas tfCanvas = new TransferFunctionCanvas();

    public TransfeFunctionPanel(SliceBasedUI parentListener) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        this.add(tfCanvas, c);
        JButton changeButton = new JButton("Apply");
        changeButton.addActionListener(parentListener);
        changeButton.setActionCommand("NewTransferFunction");
        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 1;
        c.anchor = c.BASELINE_TRAILING;
        c.insets = new Insets(4, 0, 0, 0);
        this.add(changeButton, c);
        JButton addFunctionSegmentButton = new JButton("+");
        addFunctionSegmentButton.addActionListener(parentListener);
        addFunctionSegmentButton.setActionCommand("addFunctionSegment");
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = c.BASELINE_LEADING;
        this.add(addFunctionSegmentButton, c);
        JButton removeFunctionSegmentButton = new JButton("-");
        removeFunctionSegmentButton.addActionListener(parentListener);
        removeFunctionSegmentButton.setActionCommand("removeFunctionSegment");
        c.gridx = 2;
        c.gridy = 1;
        c.anchor = c.BASELINE_LEADING;
        c.insets.left = 4;
        this.add(removeFunctionSegmentButton, c);
    }

    /**
     * @return the myCanvas
     */
    public TransferFunctionCanvas getTranferFunctionCanvas() {
        return tfCanvas;
    }
}
