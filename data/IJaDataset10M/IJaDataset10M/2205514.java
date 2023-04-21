package gui.generator;

import gui.SimpleEditor;
import gui.value.ValueListChangedListener;
import gui.value.ValueListGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.softmed.jops.ParticleBehaviour;
import org.softmed.jops.fileloading.converters.AngleRadiansConverter;
import org.softmed.jops.fileloading.converters.ColorConverter;

public class ParticleBehaviourEditor extends SimpleEditor implements ActionListener, ValueListChangedListener {

    private static final long serialVersionUID = -6080810278224363390L;

    ParticleBehaviourChooser chooser = new ParticleBehaviourChooser();

    ParticleBehaviour particleBehaviour;

    private ValueListGUI vgui;

    public ParticleBehaviourEditor() {
        setDimension(250, 600);
        JPanel middle = new JPanel();
        middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));
        middle.add(Box.createVerticalGlue());
        middle.add(chooser);
        add(middle);
        chooser.setActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == chooser.getSizeButton()) {
            destination.removeAll();
            vgui = new ValueListGUI("Size");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(null, particleBehaviour.getSize(), 100f, 0f, 20f, 0.01f, 0.01f, 0.1f, 5f, 10f);
            destination.add(vgui);
        } else if (source == chooser.getSpeed()) {
            destination.removeAll();
            vgui = new ValueListGUI("Speed");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(null, particleBehaviour.getSpeed(), 100f, -20f, 20f, 0.01f, 0.01f, 0f, 2.5f, 5f);
            destination.add(vgui);
        } else if (source == chooser.getAlfa()) {
            destination.removeAll();
            vgui = new ValueListGUI("Alpha");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(new ColorConverter(), particleBehaviour.getAlpha(), 1f, 0f, 255f, 1f, 1f, 255f, 100f, 255f);
            destination.add(vgui);
        } else if (source == chooser.getColor()) {
            destination.removeAll();
            vgui = new ValueListGUI("Color");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(new ColorConverter(), particleBehaviour.getColor());
            destination.add(vgui);
        } else if (source == chooser.getSpin()) {
            destination.removeAll();
            vgui = new ValueListGUI("Spin");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(new AngleRadiansConverter(), particleBehaviour.getSpin(), 1f, -3600f, 3600f, 1f, 1f, 0f, 1800f, 3600f);
            destination.add(vgui);
        } else if (source == chooser.getAngle()) {
            destination.removeAll();
            vgui = new ValueListGUI("Angle");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(new AngleRadiansConverter(), particleBehaviour.getAngle(), 1f, -360f, 360f, 1f, 1f, 0f, 90f, 180f);
            destination.add(vgui);
        } else if (source == chooser.getPangleh()) {
            destination.removeAll();
            vgui = new ValueListGUI("P. Angle Horz");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(new AngleRadiansConverter(), particleBehaviour.getParticleAngleH(), 1f, -360f, 360f, 1f, 1f, 0f, 90f, 180f);
            destination.add(vgui);
        } else if (source == chooser.getPanglev()) {
            destination.removeAll();
            vgui = new ValueListGUI("P. Angle Vert");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(new AngleRadiansConverter(), particleBehaviour.getParticleAngleV(), 1f, -360f, 360f, 1f, 1f, 0f, 90f, 180f);
            destination.add(vgui);
        } else if (source == chooser.getPspinh()) {
            destination.removeAll();
            vgui = new ValueListGUI("P. Spin Horz");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(new AngleRadiansConverter(), particleBehaviour.getParticleSpinH(), 1f, -3600f, 3600f, 1f, 1f, 0f, 1800f, 3600f);
            destination.add(vgui);
        } else if (source == chooser.getPspinv()) {
            destination.removeAll();
            vgui = new ValueListGUI("P. Spin Vert");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(new AngleRadiansConverter(), particleBehaviour.getParticleSpinV(), 1f, -3600f, 3600f, 1f, 1f, 0f, 1800f, 3600f);
            destination.add(vgui);
        } else if (source == chooser.getWidthButton()) {
            destination.removeAll();
            vgui = new ValueListGUI("Width");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(null, particleBehaviour.getWidth(), 100f, 0f, 20f, 0.01f, 0.01f, 10f, 2.5f, 5f);
            destination.add(vgui);
        } else if (source == chooser.getHeightButton()) {
            destination.removeAll();
            vgui = new ValueListGUI("Height");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(null, particleBehaviour.getHeight(), 100f, 0f, 20f, 0.01f, 0.01f, 10f, 2.5f, 5f);
            destination.add(vgui);
        }
        if (source == chooser.getTexwidth()) {
            destination.removeAll();
            vgui = new ValueListGUI("Texture Width");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(null, particleBehaviour.getTexWidth(), 100f, 0f, 5f, 0.01f, 0.01f, 10f, 0.5f, 1f);
            destination.add(vgui);
        } else if (source == chooser.getTexheight()) {
            destination.removeAll();
            vgui = new ValueListGUI("Texture Height");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(null, particleBehaviour.getTexHeight(), 100f, 0f, 5f, 0.01f, 0.01f, 10f, 0.5f, 1f);
            destination.add(vgui);
        } else if (source == chooser.getMass()) {
            destination.removeAll();
            vgui = new ValueListGUI("Mass");
            vgui.setListener(this);
            vgui.setActive(true);
            vgui.setValueList(null, particleBehaviour.getMass(), 100f, 0f, 20f, 0.01f, 0.01f, 0.1f, 5f, 10f);
            destination.add(vgui);
        }
        refreshDestination();
    }

    @Override
    public void setObject(Object obj) {
        particleBehaviour = (ParticleBehaviour) obj;
        chooser.setObject(obj);
    }

    public void changed() {
        particleBehaviour.recompile();
    }
}
