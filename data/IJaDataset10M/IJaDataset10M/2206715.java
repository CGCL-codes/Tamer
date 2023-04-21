package game.gui.graph3d;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.media.j3d.Transform3D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3d;

@SuppressWarnings("serial")
class OptionsPanel extends JPanel {

    private DataTableGraph3D graph3D = null;

    private JButton btnBgColorChooser = new JButton("bg color");

    private JSlider slRotateX = new JSlider(-12, 12, 0);

    private JSlider slRotateY = new JSlider(-12, 12, 0);

    private JSlider slRotateZ = new JSlider(-12, 12, 0);

    private JButton btnResetAngle = new JButton("reset");

    private int oldRotX = 0;

    private int oldRotY = 0;

    private int oldRotZ = 0;

    public OptionsPanel(DataTableGraph3D graph3D) {
        super(new GridBagLayout());
        this.graph3D = graph3D;
        initGui();
    }

    void initGui() {
        btnBgColorChooser.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                openColorChooser();
            }
        });
        btnResetAngle.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                resetAngle();
            }
        });
        ChangeListener actRotateX = new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                int deltaRotX = slRotateX.getValue() - oldRotX;
                float rx;
                if (deltaRotX > 0) rx = (float) Math.toRadians(15); else rx = (float) Math.toRadians(-15);
                Transform3D t = new Transform3D();
                Transform3D tmp = new Transform3D();
                tmp.rotX(rx);
                graph3D.getPlane().getTransformPlane().getTransform(t);
                t.mul(tmp);
                graph3D.getPlane().getTransformPlane().setTransform(t);
                oldRotX = slRotateX.getValue();
            }
        };
        ChangeListener actRotateY = new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                int deltaRotY = slRotateY.getValue() - oldRotY;
                float ry;
                if (deltaRotY > 0) ry = (float) Math.toRadians(15); else ry = (float) Math.toRadians(-15);
                Transform3D t = new Transform3D();
                Transform3D tmp = new Transform3D();
                tmp.rotY(ry);
                graph3D.getPlane().getTransformPlane().getTransform(t);
                t.mul(tmp);
                graph3D.getPlane().getTransformPlane().setTransform(t);
                oldRotY = slRotateY.getValue();
            }
        };
        ChangeListener actRotateZ = new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                int deltaRotZ = slRotateZ.getValue() - oldRotZ;
                float rz;
                if (deltaRotZ > 0) rz = (float) Math.toRadians(15); else rz = (float) Math.toRadians(-15);
                Transform3D t = new Transform3D();
                Transform3D tmp = new Transform3D();
                tmp.rotZ(rz);
                graph3D.getPlane().getTransformPlane().getTransform(t);
                t.mul(tmp);
                graph3D.getPlane().getTransformPlane().setTransform(t);
                oldRotZ = slRotateZ.getValue();
            }
        };
        slRotateX.addChangeListener(actRotateX);
        slRotateY.addChangeListener(actRotateY);
        slRotateZ.addChangeListener(actRotateZ);
        GridBagConstraints c = new GridBagConstraints();
        int y = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Options"));
        c.gridy = y++;
        add(btnBgColorChooser, c);
        c.gridy = y++;
        add(new JLabel("Rotate axis X"), c);
        c.gridy = y++;
        add(slRotateX, c);
        c.gridy = y++;
        add(new JLabel("Rotate axis Y"), c);
        c.gridy = y++;
        add(slRotateY, c);
        c.gridy = y++;
        add(new JLabel("Rotate axis Z"), c);
        c.gridy = y++;
        add(slRotateZ, c);
        c.gridy = y++;
        add(btnResetAngle, c);
        setVisible(true);
    }

    void resetAngle() {
        slRotateX.setValue(4);
        slRotateY.setValue(0);
        slRotateZ.setValue(0);
        Transform3D t = new Transform3D();
        t.setRotation(new AxisAngle4f());
        t.rotX(Math.toRadians(60));
        t.setTranslation(new Vector3d(0, 0, -1.2));
        graph3D.getPlane().getTransformPlane().setTransform(t);
    }

    private void openColorChooser() {
        final JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        final JColorChooser colorChooser = new JColorChooser();
        JButton btnOk = new JButton("Ok");
        JButton btnCancel = new JButton("Cancel");
        btnOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                btnBgColorChooser.setBackground(colorChooser.getColor());
                graph3D.setBgColor(colorChooser.getColor());
                frame.dispose();
            }
        });
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                frame.dispose();
            }
        });
        panel.add(colorChooser);
        panel.add(btnOk);
        panel.add(btnCancel);
        frame.add(panel);
        frame.setVisible(true);
        frame.pack();
    }
}
