package uk.ac.lkl.migen.system.expresser.ui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModelImpl;
import uk.ac.lkl.migen.system.expresser.model.event.AttributeChangeEvent;
import uk.ac.lkl.migen.system.expresser.model.event.AttributeChangeListener;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;

public class MasterSlaveUniverseMicroworldPanel extends ExpresserModelPanel {

    private BlockShapeCanvasPanel masterPanel;

    public MasterSlaveUniverseMicroworldPanel() {
        this(new ExpresserModelImpl());
    }

    public MasterSlaveUniverseMicroworldPanel(final ExpresserModel masterModel) {
        masterModel.setMasterModel(true);
        masterPanel = new BlockShapeCanvasPanel(masterModel);
        setLayout(new BorderLayout());
        add(masterPanel, BorderLayout.CENTER);
        GlobalColorAllocationPanel globalAllocationPanel = masterPanel.getGlobalAllocationPanel(0);
        globalAllocationPanel.colorChosen(null);
        AttributeChangeListener<BlockShape> attributeChangeListener = new AttributeChangeListener<BlockShape>() {

            @Override
            public void attributesChanged(AttributeChangeEvent<BlockShape> e) {
                masterModel.setDirtyModel(true);
            }
        };
        masterModel.addAttributeChangeListener(attributeChangeListener);
    }

    public ExpresserModel getModel() {
        return masterPanel.getModel();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setContentPane(new MasterSlaveUniverseMicroworldPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public BlockShapeCanvasPanel getMasterPanel() {
        return masterPanel;
    }

    @Override
    public ObjectSetCanvas getCanvas() {
        if (masterPanel != null) {
            return masterPanel.getCanvas();
        } else {
            return null;
        }
    }
}
