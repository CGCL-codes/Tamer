package dviz.tools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import dviz.visualSystem.impl.AnimationObject;
import dviz.visualSystem.impl.CameraControl;

/**
 * @author  zxq071000
 */
public class NodeSelector extends JFrame {

    JList list;

    public Node rootNode;

    public Node selectedNode;

    /**
	 * @uml.property  name="parent"
	 * @uml.associationEnd  
	 */
    public DiscoveryFrame parent;

    public class FreeViewCameraControl implements CameraControl, KeyListener, MouseListener, MouseMotionListener {

        Vector3f X;

        Vector3f Y;

        Vector3f Z;

        Vector3f controlVec = new Vector3f();

        @Override
        public void addListener(Component comp) {
            comp.addMouseMotionListener(this);
            comp.addKeyListener(this);
            comp.addMouseListener(this);
        }

        @Override
        public void removeListener(Component comp) {
            comp.removeMouseMotionListener(this);
            comp.removeKeyListener(this);
            comp.removeMouseListener(this);
        }

        @Override
        public void updateCamera(float t, Camera camera) {
            Vector3f loc = camera.getLocation();
            Z = new Vector3f((float) (Math.sin(beta) * Math.cos(alpha)), (float) (Math.cos(beta)), (float) (Math.sin(beta) * Math.sin(alpha)));
            Y = new Vector3f((float) (Math.cos(beta) * Math.cos(alpha) * -1), (float) (Math.sin(beta)), (float) (-Math.cos(beta) * Math.sin(alpha)));
            X = Y.cross(Z);
            Vector3f movDet = Z.mult(controlVec.z).add(Y.mult(controlVec.y).add(X.mult(controlVec.x)));
            loc.addLocal(movDet.mult(5));
            camera.setLocation(loc);
            camera.setAxes(X, Y, Z);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyChar()) {
                case 'w':
                case 'W':
                    controlVec.setZ(1);
                    break;
                case 'a':
                case 'A':
                    controlVec.setX(1);
                    break;
                case 's':
                case 'S':
                    controlVec.setZ(-1);
                    break;
                case 'd':
                case 'D':
                    controlVec.setX(-1);
                    break;
                case 'Q':
                case 'q':
                    controlVec.setY(1);
                    break;
                case 'e':
                case 'E':
                    controlVec.setY(-1);
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch(e.getKeyChar()) {
                case 'w':
                case 'W':
                    controlVec.setZ(0);
                    break;
                case 'a':
                case 'A':
                    controlVec.setX(0);
                    break;
                case 's':
                case 'S':
                    controlVec.setZ(0);
                    break;
                case 'd':
                case 'D':
                    controlVec.setX(0);
                    break;
                case 'Q':
                case 'q':
                    controlVec.setY(0);
                    break;
                case 'e':
                case 'E':
                    controlVec.setY(0);
                    break;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent arg0) {
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            down = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            down = false;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int dx = e.getX() - ox;
            int dy = e.getY() - oy;
            if (down) {
                alpha += dx * MOUSE_SENSITIVITY;
                beta += dy * MOUSE_SENSITIVITY;
                if (beta > Math.PI) beta = (float) (Math.PI);
                if (beta < 0) beta = (0);
            }
            ox = e.getX();
            oy = e.getY();
        }

        float alpha = 0;

        float beta = (float) (Math.PI / 2);

        float MOUSE_SENSITIVITY = 0.02f;

        int ox;

        int oy;

        boolean down;

        @Override
        public void mouseMoved(MouseEvent e) {
            int dx = e.getX() - ox;
            int dy = e.getY() - oy;
            ox = e.getX();
            oy = e.getY();
        }
    }

    public class ChaseCameraControl implements CameraControl, KeyListener, MouseListener, MouseMotionListener {

        float offset = 50;

        float alpha = 0;

        float beta = (float) (Math.PI / 2);

        int ox;

        int oy;

        boolean down;

        @Override
        public void addListener(Component comp) {
            comp.addMouseMotionListener(this);
            comp.addKeyListener(this);
            comp.addMouseListener(this);
        }

        @Override
        public void removeListener(Component comp) {
            comp.removeMouseMotionListener(this);
            comp.removeKeyListener(this);
            comp.removeMouseListener(this);
        }

        @Override
        public void updateCamera(float t, Camera camera) {
            if (selectedNode == null) return;
            Vector3f v3f = new Vector3f(selectedNode.getWorldTranslation());
            v3f.y += offset;
            camera.setLocation(v3f);
            Vector3f X, Y, Z;
            Z = new Vector3f((float) (Math.sin(beta) * Math.cos(alpha)), (float) (Math.cos(beta)), (float) (Math.sin(beta) * Math.sin(alpha)));
            Y = new Vector3f((float) (Math.cos(beta) * Math.cos(alpha) * -1), (float) (Math.sin(beta)), (float) (-Math.cos(beta) * Math.sin(alpha)));
            X = Y.cross(Z);
            camera.setAxes(X, Y, Z);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'w') offset *= 1.1f; else if (e.getKeyChar() == 's') offset /= 1.1f;
            if (offset < 1) offset = 1;
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mouseMoved(e);
        }

        float MOUSE_SENSITIVITY = 0.02f;

        @Override
        public void mouseMoved(MouseEvent e) {
            int dx = e.getX() - ox;
            int dy = e.getY() - oy;
            if (down) {
                alpha += dx * MOUSE_SENSITIVITY;
                beta += dy * MOUSE_SENSITIVITY;
                if (beta > Math.PI) beta = (float) (Math.PI);
                if (beta < 0) beta = (0);
            }
            ox = e.getX();
            oy = e.getY();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            down = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            down = false;
        }
    }

    /**
	 * @uml.property  name="chaseCameraControl"
	 * @uml.associationEnd  
	 */
    ChaseCameraControl chaseCameraControl = new ChaseCameraControl();

    /**
	 * @uml.property  name="freeViewCameraControl"
	 * @uml.associationEnd  
	 */
    FreeViewCameraControl freeViewCameraControl = new FreeViewCameraControl();

    public CameraControl getCameraControl() {
        return new FreeViewCameraControl();
    }

    boolean nodeViewCamera = true;

    public NodeSelector() {
        this.setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        list = new JList();
        JScrollPane pane = new JScrollPane();
        pane.setViewportView(list);
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                Item item = (Item) list.getSelectedValue();
                if (item == null) return;
                selectedNode = item.node;
            }
        });
        getContentPane().add(pane);
        JButton button = new JButton("Refresh");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                refresh(rootNode);
            }
        });
        getContentPane().add(button, BorderLayout.SOUTH);
        JPanel cameraTypePane = new JPanel();
        getContentPane().add(cameraTypePane, BorderLayout.NORTH);
        JRadioButton nodeView = new JRadioButton();
        JRadioButton freeView = new JRadioButton();
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(nodeView);
        buttonGroup.add(freeView);
        buttonGroup.setSelected(freeView.getModel(), true);
        nodeView.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                parent.setCameraControl(chaseCameraControl);
            }
        });
        freeView.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                parent.setCameraControl(freeViewCameraControl);
            }
        });
        nodeView.setText("Node Camera");
        freeView.setText("Free Camera");
        cameraTypePane.add(nodeView);
        cameraTypePane.add(freeView);
        setLocation(800, 0);
        pack();
    }

    static class Item {

        String name;

        Node node;

        public Item(String name, Node node) {
            this.name = name;
            this.node = node;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public void refresh(Node node) {
        Vector<Item> data = new Vector<Item>();
        addChildren(rootNode, data);
        list.setListData(data);
    }

    public void addChildren(Node node, Vector<Item> data) {
        if (node instanceof AnimationObject.RenderNode) {
            data.add(new Item(node.getName(), node));
        }
        for (Spatial spatial : node.getChildren()) {
            if (spatial == null) continue;
            if (spatial instanceof Node) {
                addChildren((Node) spatial, data);
            }
        }
    }

    public static void main(String s[]) {
        NodeSelector nodeSelector = new NodeSelector();
        nodeSelector.setVisible(true);
    }
}
