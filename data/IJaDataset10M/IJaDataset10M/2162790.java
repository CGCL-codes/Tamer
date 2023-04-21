package ch.mastermapframework.basicGUIs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import ch.mastermapframework.basicGUIs.layout.MMLayoutManager;
import ch.mastermapframework.basicGUIs.layoutGUI.GuiComponent;
import ch.mastermapframework.basicGUIs.layoutGUI.MMFormManager;
import ch.mastermapframework.parameterInitialization.BasicParameterInterface;

public class BasicGUIFrame {

    private JFrame myFrame;

    private Dimension frameDimension;

    private Point frameLocation;

    private static int MenuBarHeight = 20;

    private MMFormManager formManager;

    private String frameTitle;

    private Color backgroundColor = MMLayoutManager.basicGUIFrameBackgroundColor;

    private Vector<GuiComponent> guiComponents;

    public BasicGUIFrame() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.myFrame = new JFrame();
    }

    public void showFrame() {
        this.constructFrame();
        if (this.frameDimension != null) {
            this.myFrame.setSize(this.frameDimension);
        }
        if (this.frameLocation != null) {
            this.myFrame.setLocation(this.frameLocation);
        }
        this.myFrame.setVisible(true);
        this.myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void constructFrame() {
        this.layoutFrame();
        this.assembleComponents();
    }

    private void layoutFrame() {
        this.formManager = new MMFormManager(this.guiComponents);
        this.formManager.createFormLayout();
        this.myFrame.setLayout(this.formManager.getFormLayout());
        this.frameDimension = this.formManager.getOptimalGuiSize();
        this.frameDimension.height += MenuBarHeight;
        this.myFrame.setSize(this.frameDimension);
        this.myFrame.setBackground(backgroundColor);
    }

    private void assembleComponents() {
        Iterator I = this.guiComponents.iterator();
        while (I.hasNext()) {
            GuiComponent c = (GuiComponent) I.next();
            this.myFrame.add(c.myComponent, this.formManager.getCellConstraint(c.loc));
        }
    }

    public MMPanel getMainFramePanel() {
        MMPanel myPanel = new MMPanel();
        if (this.frameTitle != null) {
            myPanel.addBorderTitle(this.frameTitle);
        }
        Iterator I = this.guiComponents.iterator();
        while (I.hasNext()) {
            GuiComponent c = (GuiComponent) I.next();
            myPanel.add(c.myComponent, c.loc.x, c.loc.y);
        }
        myPanel.constructPanel();
        return myPanel;
    }

    public void closeMainFrame() {
        BasicParameterInterface.storeAllUserSpecificParametersToXML();
        this.myFrame.dispose();
    }

    public void setLocation(Point D) {
        frameLocation = D;
    }

    public Point getLocation() {
        return frameLocation;
    }

    public void setMainFrameDimension(Dimension d) {
        this.frameDimension = d;
    }

    public void addMenuBar(JMenu in) {
        this.myFrame.getJMenuBar().add(in);
    }

    public Dimension getMainFrameDimension() {
        return this.frameDimension;
    }

    public void setFrameTitle(String IN) {
        this.frameTitle = IN;
        this.myFrame.setTitle(IN);
    }

    public void addWindowListerner2Frame(WindowListener IN) {
        this.myFrame.addWindowListener(IN);
    }

    public void setMainFrameBGColor(Color IN) {
        this.myFrame.setBackground(IN);
    }

    public void validateFrame() {
        this.myFrame.validate();
        this.myFrame.repaint();
    }

    public void add(Component c, int col, int row) {
        if (this.guiComponents == null) {
            this.guiComponents = new Vector<GuiComponent>();
        }
        GuiComponent gC = new GuiComponent(c, new Point(row, col));
        this.guiComponents.add(gC);
    }

    public JFrame getMainFrame() {
        return this.myFrame;
    }

    public void clearComponents() {
        this.guiComponents.clear();
        this.myFrame.removeAll();
    }

    public void setIconImage(String logoPath) {
        ImageIcon im = new ImageIcon(logoPath);
        this.myFrame.setIconImage(im.getImage());
    }
}
