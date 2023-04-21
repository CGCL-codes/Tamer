package tr.edu.boun.phys.springmassmodel.UI;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import tr.edu.boun.phys.springmassmodel.Mass;
import tr.edu.boun.phys.springmassmodel.SpringMassModel;
import tr.edu.boun.phys.springmassmodel.UI.PropertiesWindow.ComboConstraints;
import tr.edu.boun.phys.springmassmodel.UI.PropertiesWindow.PropertiesWindow;

public class ResourceManager {

    private SpringMassModel model;

    private MainWindow mainWindow;

    private PropertiesWindow propertiesWindow;

    private Display display;

    private DisplayMouseListener displayMouseListener;

    private ComboConstraints comboConstraints;

    private Mass selectedMass;

    private boolean isPaused;

    public ResourceManager(SpringMassModel model) {
        this.model = model;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        comboConstraints = new ComboConstraints(this);
        display = new Display(this.model, this);
        propertiesWindow = new PropertiesWindow(this);
        displayMouseListener = new DisplayMouseListener(this);
        display.addMouseListener(displayMouseListener);
        display.addMouseMotionListener(displayMouseListener);
        mainWindow = new MainWindow();
        mainWindow.getContentPane().add(display);
        mainWindow.setVisible(true);
        propertiesWindow.setVisible(true);
        propertiesWindow.getContentPane().repaint();
        this.isPaused = false;
    }

    public SpringMassModel getStringModel() {
        return model;
    }

    public Display getDisplay() {
        return display;
    }

    public PropertiesWindow getPropertiesWindow() {
        return propertiesWindow;
    }

    public ComboConstraints getComboConstraints() {
        return comboConstraints;
    }

    public Mass getSelectedMass() {
        return selectedMass;
    }

    public void setSelectedMass(Mass selectedMass, int index) {
        this.selectedMass = selectedMass;
        propertiesWindow.selectMass(model.getMass(index), index);
        if (selectedMass != null) {
            comboConstraints.setSelectedIndex(model.getMass(index).getConstraint());
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean b) {
        this.isPaused = b;
    }
}
