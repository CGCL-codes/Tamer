package planning.editor.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import planning.file.props.RRTConnectAlgorithmProp;

public class RRTConnectAlgorithmPropPanelHandler {

    private RRTConnectAlgorithmPropPanel panel;

    private RRTConnectAlgorithmProp algorithm;

    public RRTConnectAlgorithmPropPanelHandler() {
        panel = new RRTConnectAlgorithmPropPanel();
        panel.getJButtonOk().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                approved();
            }
        });
        panel.getJButtonCancel().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                reverted();
            }
        });
    }

    public void showObject(RRTConnectAlgorithmProp algorithm) {
        if (algorithm == null) return;
        panel.getJLabelStatus().setText(" ");
        this.algorithm = algorithm;
        panel.getJTextFieldStepSize().setText((float) algorithm.getStepSize() + "");
        panel.getJTextFieldStepCount().setText(algorithm.getStepCount() + "");
        panel.getJTextFieldThreshold().setText(algorithm.getThreshold() + "");
        panel.getJTextFieldEnlargement().setText((float) algorithm.getCollisionEnlargement() + "");
    }

    private void approved() {
        if (algorithm == null) return;
        panel.getJLabelStatus().setText(" ");
        try {
            double stepSize = Double.parseDouble(panel.getJTextFieldStepSize().getText());
            int stepCount = Integer.parseInt(panel.getJTextFieldStepCount().getText());
            double threshold = Double.parseDouble(panel.getJTextFieldThreshold().getText());
            double enlargement = Double.parseDouble(panel.getJTextFieldEnlargement().getText());
            algorithm.setStepSize(stepSize);
            algorithm.setStepCount(stepCount);
            algorithm.setThreshold(threshold);
            algorithm.setCollisionEnlargement(enlargement);
        } catch (Exception e) {
            panel.getJLabelStatus().setText("Check your inputs!");
        }
    }

    private void reverted() {
        showObject(algorithm);
    }

    public RRTConnectAlgorithmPropPanel getRRTConnectAlgorithmPropPanel() {
        return panel;
    }
}
