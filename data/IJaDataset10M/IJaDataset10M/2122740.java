package neon.tools.maps;

import javax.swing.*;
import java.awt.*;
import neon.graphics.*;
import neon.tools.Editor;
import neon.tools.ObjectTransferHandler;
import java.awt.event.*;
import org.jdom2.Element;
import neon.tools.resources.IContainer;
import neon.tools.resources.IDoor;
import neon.tools.resources.IObject;
import neon.tools.resources.IRegion;
import neon.tools.resources.Instance;
import neon.tools.resources.RTerrain;

/**
 * @author	mdriesen
 */
@SuppressWarnings("serial")
public class EditablePane extends JScrollPane implements MouseMotionListener, MouseListener {

    private Scene scene;

    private int layer;

    private ZoneTreeNode node;

    private Instance cut;

    private Instance copy;

    private Point position;

    private JVectorPane pane;

    private float zoom = 12;

    private IRegion newRegion;

    /**
	 * Initializes this <code>EditablePane</code>.
	 */
    public EditablePane(ZoneTreeNode node, float width, float height) {
        super();
        if (node.getZone().getScene() == null) {
            node.getZone().map.load();
        }
        layer = -1;
        this.node = node;
        setBackground(Color.black);
        pane = new JVectorPane();
        setViewportView(pane);
        scene = node.getZone().getScene();
        pane.setScene(scene);
        pane.setEditable(true);
        getViewport().getView().addMouseListener(this);
        getViewport().getView().addMouseMotionListener(this);
        pane.setTransferHandler(new ObjectTransferHandler(node.getZone()));
    }

    public ZoneTreeNode getNode() {
        return node;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void toggleView(boolean view) {
        System.out.println("toggle");
        for (Layer layer : scene.getLayers()) {
            if (layer.getDepth() == this.layer) {
                layer.setVisible(true);
            } else {
                layer.setVisible(view);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        Renderable selected = pane.getSelectedObject();
        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1 && selected != null) {
            if (selected instanceof IDoor) {
                new DoorInstanceEditor((IDoor) selected, Editor.getFrame()).show();
            } else if (selected instanceof IContainer) {
                new ContainerInstanceEditor((IContainer) selected, Editor.getFrame(), node).show();
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            JPopupMenu menu = new JPopupMenu();
            menu.add(new ClickAction("Cut", selected != null));
            menu.add(new ClickAction("Copy", selected != null));
            menu.add(new ClickAction("Paste", this.cut != null || this.copy != null, e.getX(), e.getY()));
            menu.add(new ClickAction("Delete", selected != null));
            menu.addSeparator();
            menu.add(new ClickAction("Properties...", selected instanceof IContainer || selected instanceof IDoor || selected instanceof IRegion));
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void mouseDragged(MouseEvent e) {
        Instance selected = (Instance) pane.getSelectedObject();
        if (MapEditor.drawMode()) {
            newRegion.width = (int) (e.getX() / zoom) - newRegion.getX();
            newRegion.height = (int) (e.getY() / zoom) - newRegion.getY();
            repaint();
        } else if (selected != null) {
            selected.setX((int) (e.getX() / zoom));
            selected.setY((int) (e.getY() / zoom));
            scene.removeElement(selected);
            scene.addElement(selected, selected.getBounds(), selected.z);
            repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
        Editor.getStatusBar().setMessage("x: " + (int) (e.getX() / zoom) + ", y: " + (int) (e.getY() / zoom));
    }

    public void mousePressed(MouseEvent e) {
        if (MapEditor.drawMode()) {
            if (MapEditor.getSelectedTerrain() != null) {
                Element region = new Element("region");
                region.setAttribute("text", MapEditor.getSelectedTerrain());
                region.setAttribute("x", Integer.toString((int) (e.getX() / zoom)));
                region.setAttribute("y", Integer.toString((int) (e.getY() / zoom)));
                region.setAttribute("w", "1");
                region.setAttribute("h", "1");
                region.setAttribute("l", Integer.toString(layer));
                newRegion = new IRegion(region);
                scene.addElement(newRegion, newRegion.getBounds(), newRegion.z);
            }
        } else {
            Renderable selected = pane.getSelectedObject();
            position = new Point(selected.getX(), selected.getY());
        }
    }

    public void mouseReleased(MouseEvent e) {
        Instance selected = (Instance) pane.getSelectedObject();
        if (MapEditor.drawMode()) {
            scene.removeElement(newRegion);
            if (newRegion.width < 0) {
                newRegion.width = -newRegion.width;
                newRegion.setX((int) (e.getX() / zoom));
            }
            if (newRegion.height < 0) {
                newRegion.width = -newRegion.height;
                newRegion.setY((int) (e.getY() / zoom));
            }
            scene.addElement(newRegion, newRegion.getBounds(), newRegion.z);
            repaint();
        } else if (selected != null) {
            MapEditor.setUndoAction(new UndoAction.Move(selected, scene, position.x, position.y));
            repaint();
        }
    }

    private class ClickAction extends AbstractAction {

        private int x, y;

        public ClickAction(String name, boolean enabled) {
            super(name);
            setEnabled(enabled);
        }

        public ClickAction(String name, boolean enabled, int x, int y) {
            super(name);
            setEnabled(enabled);
            this.x = x;
            this.y = y;
        }

        private Instance clone(Instance original) {
            Instance clone = null;
            Element originalElement = copy.toElement();
            Element cloneElement = new Element(originalElement.getName());
            cloneElement.setAttribute("x", Integer.toString((int) (x / zoom)));
            cloneElement.setAttribute("y", Integer.toString((int) (y / zoom)));
            cloneElement.setAttribute("uid", Integer.toString(node.getZone().map.createUID(cloneElement)));
            if (original instanceof IRegion) {
                cloneElement.setAttribute("text", originalElement.getAttributeValue("text"));
                cloneElement.setAttribute("w", originalElement.getAttributeValue("w"));
                cloneElement.setAttribute("h", originalElement.getAttributeValue("h"));
                cloneElement.setAttribute("l", originalElement.getAttributeValue("l"));
                clone = new IRegion((RTerrain) original.resource, (int) (x / zoom), (int) (y / zoom), original.getZ(), original.width, original.height);
            } else if (original instanceof IObject) {
                cloneElement.setAttribute("id", originalElement.getAttributeValue("id"));
                clone = new IObject(original.resource, (int) (x / zoom), (int) (y / zoom), original.getZ(), 0);
            }
            return clone;
        }

        public void actionPerformed(ActionEvent e) {
            Instance selected = (Instance) pane.getSelectedObject();
            if (e.getActionCommand().equals("Cut")) {
                if (selected != null) {
                    copy = null;
                    cut = selected;
                }
            } else if (e.getActionCommand().equals("Copy")) {
                if (selected != null) {
                    cut = null;
                    copy = selected;
                }
            } else if (e.getActionCommand().equals("Paste")) {
                if (copy != null) {
                    Instance clone = clone(copy);
                    scene.addElement(clone, clone.getBounds(), clone.getZ());
                } else if (cut != null) {
                    cut.setX((int) (x / zoom));
                    cut.setY((int) (y / zoom));
                    scene.removeElement(cut);
                    scene.addElement(cut, cut.getBounds(), cut.z);
                    cut = null;
                    repaint();
                }
            } else if (e.getActionCommand().equals("Delete")) {
                if (selected != null) {
                    scene.removeElement(selected);
                    if (selected instanceof IObject) {
                        node.getZone().map.removeObjectUID(((IObject) selected).uid);
                        if (selected instanceof IContainer) {
                            for (IObject io : ((IContainer) selected).contents) {
                                node.getZone().map.removeObjectUID(io.uid);
                            }
                        }
                    }
                }
            } else if (e.getActionCommand().equals("Properties...")) {
                if (selected instanceof IDoor) {
                    new DoorInstanceEditor((IDoor) selected, Editor.getFrame()).show();
                } else if (selected instanceof IContainer) {
                    new ContainerInstanceEditor((IContainer) selected, Editor.getFrame(), node).show();
                } else if (selected instanceof IRegion) {
                    new RegionInstanceEditor((IRegion) selected, Editor.getFrame(), node).show();
                }
            }
        }
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
        pane.setZoom(zoom);
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }
}
