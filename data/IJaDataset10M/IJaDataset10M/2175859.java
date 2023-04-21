package org.extremengine.demo;

import java.awt.Component;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.extremengine.World;

public class SettingPanel extends JPanel {

    private JSlider sldRigidBodyElasticity;

    private JLabel lblWallElasticity;

    private JSlider sldWallFriction;

    private JLabel lblRigidBodyFriction;

    private JLabel lblGroundElasticity;

    private JSlider sldGroundElasticity;

    private JLabel lblWallFriction;

    private JLabel lblGroundFriction;

    private JLabel lblRigidBodyElasticity;

    private JSlider sldWallElasticity;

    private JLabel lblGravityAccel;

    private JSlider sldRigidBodyFriction;

    private JSlider sldGroundFriction;

    private JSlider sldGravityAccel;

    private World world;

    /**
	 * Create the panel.
	 */
    public SettingPanel() {
        lblGravityAccel = new JLabel("重力加速度");
        sldGravityAccel = new JSlider();
        sldGravityAccel.setMaximum(98);
        sldGravityAccel.setToolTipText("");
        sldGravityAccel.addChangeListener(new ChangeListener() {

            public void stateChanged(final ChangeEvent e) {
                handleSldGravityAccelStateChanged(e);
            }
        });
        sldGravityAccel.setValue(98);
        lblGroundFriction = new JLabel("地面摩擦系数");
        lblGroundFriction.setVisible(false);
        sldGroundFriction = new JSlider();
        sldGroundFriction.setVisible(false);
        lblGroundElasticity = new JLabel("地面弹性系数");
        lblGroundElasticity.setVisible(false);
        sldGroundElasticity = new JSlider();
        sldGroundElasticity.setVisible(false);
        lblWallFriction = new JLabel("墙面摩擦系数：");
        lblWallFriction.setVisible(false);
        sldWallFriction = new JSlider();
        sldWallFriction.setVisible(false);
        lblWallElasticity = new JLabel("墙面弹性系数");
        lblWallElasticity.setVisible(false);
        sldWallElasticity = new JSlider();
        sldWallElasticity.setVisible(false);
        lblRigidBodyFriction = new JLabel("刚体表面摩擦系数：");
        lblRigidBodyFriction.setVisible(false);
        sldRigidBodyFriction = new JSlider();
        sldRigidBodyFriction.setVisible(false);
        lblRigidBodyElasticity = new JLabel("刚体表面弹性系数：");
        lblRigidBodyElasticity.setVisible(false);
        sldRigidBodyElasticity = new JSlider();
        sldRigidBodyElasticity.setVisible(false);
        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false).addComponent(sldGravityAccel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lblGravityAccel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false).addComponent(sldGroundFriction, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(sldGroundElasticity, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(sldWallFriction, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(sldWallElasticity, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(sldRigidBodyFriction, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(sldRigidBodyElasticity, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lblGroundFriction, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lblGroundElasticity, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lblWallFriction, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lblWallElasticity, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lblRigidBodyFriction, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lblRigidBodyElasticity, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addContainerGap(27, Short.MAX_VALUE)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addGap(11).addComponent(lblGravityAccel).addPreferredGap(ComponentPlacement.RELATED).addComponent(sldGravityAccel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(71).addComponent(lblGroundFriction).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(sldGroundFriction, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblGroundElasticity).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(sldGroundElasticity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblWallFriction).addGap(8).addComponent(sldWallFriction, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(11).addComponent(lblWallElasticity).addGap(15).addComponent(sldWallElasticity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(7).addComponent(lblRigidBodyFriction).addGap(15).addComponent(sldRigidBodyFriction, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(9).addComponent(lblRigidBodyElasticity).addGap(11).addComponent(sldRigidBodyElasticity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(21)));
        groupLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] { sldGravityAccel, sldGroundFriction, sldGroundElasticity, sldWallFriction, sldWallElasticity, sldRigidBodyFriction, sldRigidBodyElasticity });
        setLayout(groupLayout);
    }

    private boolean inited = false;

    ;

    /**
	 * 用各个Demo中World的默认值初始化SettingPanel
	 * @param world
	 */
    public void init(World world) {
        inited = false;
        this.world = world;
        if (world != null) {
            this.sldGravityAccel.setValue((int) (world.getGravityAccelaration() * 10));
            this.updateLables();
        } else {
            System.err.println("World is not constructed");
        }
        inited = true;
    }

    protected void handleSldGravityAccelStateChanged(final ChangeEvent e) {
        if (inited == true && !sldGravityAccel.getValueIsAdjusting()) {
            System.out.println("Gravity changed to: " + sldGravityAccel.getValue() / 10.0);
            if (world != null) {
                world.setGravityAccelaration(sldGravityAccel.getValue() / 10.0);
                this.updateLables();
            } else {
                System.out.println("World is not set");
            }
        }
    }

    protected void updateLables() {
        lblGravityAccel.setText("重力加速度: " + sldGravityAccel.getValue() / 10.0);
        lblGroundFriction.setText("地面摩擦系数: " + sldGroundFriction.getValue() / 100.0);
        lblGroundElasticity.setText("地面弹性系数: " + sldGroundElasticity.getValue() / 100.0);
        lblWallFriction.setText("墙面摩擦系数：" + sldWallFriction.getValue() / 100.0);
        lblWallElasticity.setText("墙面弹性系数: " + sldWallElasticity.getValue() / 100.0);
        lblRigidBodyFriction.setText("刚体表面摩擦系数：" + sldRigidBodyFriction.getValue() / 100.0);
        lblRigidBodyElasticity.setText("刚体表面弹性系数：" + sldRigidBodyElasticity.getValue() / 100.0);
    }
}
