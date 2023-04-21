package sqba.jamper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import sqba.jamper.map.ILinkable;
import sqba.jamper.map.Map;
import sqba.jamper.map.object.DefaultLinkable;
import sqba.jamper.map.object.FunctionBox;
import sqba.jamper.map.object.IMapObject;
import sqba.jamper.map.object.LinkLine;
import sqba.jamper.map.output.XSLTRenderer;
import sqba.jamper.map.tree.MapperTree;
import sqba.jamper.map.tree.MapperTreeNode;
import sqba.jamper.map.object.DragableMapObject;

/**
 * This class manages the  lines that  connect  mapped
 * nodes between the two trees. Lines inside the  tree
 * component are managed by the tree component itself.
 *
 * @author	Filip Pavlovic
 * @version	1.0
 */
public class ObjectPanel extends JPanel {

    private MapView _mainMapView = null;

    private LinkLine _dragLine = null;

    private DragSource _dragSource = null;

    private DropTarget _dropTarget = null;

    private boolean _bDragLine = false;

    private Point _ptPopup = new Point();

    private boolean _bAutoRender = false;

    private Map _map = null;

    private JMenu _menu = new MyMenu();

    class MyDragSourceListener implements DragSourceListener {

        public void dragDropEnd(DragSourceDropEvent event) {
            if (event.getDropSuccess()) {
                repaint();
            }
        }

        public void dragEnter(DragSourceDragEvent event) {
            DragSourceContext context = event.getDragSourceContext();
            context.setCursor(DragSource.DefaultLinkDrop);
        }

        public void dragExit(DragSourceEvent event) {
        }

        public void dragOver(DragSourceDragEvent event) {
        }

        public void dropActionChanged(DragSourceDragEvent event) {
        }
    }

    class MyDragGestureListener implements DragGestureListener {

        public void dragGestureRecognized(DragGestureEvent event) {
            Point pt = event.getDragOrigin();
            IMapObject obj = ObjectPanel.this.getMap().getChildAt(pt);
            if (obj != null && (obj instanceof ILinkable)) {
                ILinkable src = (ILinkable) obj;
                if (src.willAccept(null)) {
                    _dragSource.startDrag(event, DragSource.DefaultLinkDrop, (Transferable) src, new MyDragSourceListener());
                    setDragLine(src, new DefaultLinkable(pt));
                    repaint();
                    return;
                }
            }
        }
    }

    class MyDropTargetListener implements DropTargetListener {

        public void dragEnter(DropTargetDragEvent event) {
        }

        public void dragExit(DropTargetEvent event) {
            _bDragLine = false;
            repaint();
        }

        public void dragOver(DropTargetDragEvent event) {
            if (!event.isDataFlavorSupported(MapperTreeNode.mapperTreeNodeFlavor) && !event.isDataFlavorSupported(DefaultLinkable.linkableFlavor) && !event.isDataFlavorSupported(DefaultLinkable.localLinkableFlavor)) {
                return;
            }
            Point pt = event.getLocation();
            Transferable t = event.getTransferable();
            ILinkable src = DefaultLinkable.getLinkable(t);
            ILinkable dst = new DefaultLinkable(pt);
            IMapObject obj = ObjectPanel.this.getMap().getChildAt(pt);
            if (obj != null && (obj instanceof ILinkable)) dst = (ILinkable) obj;
            if (dst.willAccept(src)) {
                _dragLine = new LinkLine(src, dst);
                _bDragLine = true;
                ObjectPanel.this.repaint();
            }
        }

        public void drop(DropTargetDropEvent event) {
            Transferable t = event.getTransferable();
            ILinkable src = DefaultLinkable.getLinkable(t);
            Point pt = event.getLocation();
            IMapObject dst = ObjectPanel.this.getMap().getChildAt(pt);
            if (dst != null && (dst instanceof ILinkable)) if (((ILinkable) dst).willAccept(src)) addLink(src, (ILinkable) dst);
            _map.repaint();
        }

        public void dropActionChanged(DropTargetDragEvent event) {
        }
    }

    class MyMouseListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            IMapObject obj = ObjectPanel.this.getMap().getChildAt(e.getPoint());
            if (null != obj) objectSelected(obj); else objectSelected(_map);
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                _ptPopup = e.getPoint();
                IMapObject obj = ObjectPanel.this.getMap().getChildAt(_ptPopup);
                JPopupMenu popup = new MyPopupMenu(obj);
                popup.show(e.getComponent(), e.getX(), e.getY());
                ObjectPanel.this.repaint();
            }
        }
    }

    class MyMenu extends JMenu {

        public MyMenu() {
            super("Map");
            JMenuItem menuItem;
            JMenu newItemMenu = new JMenu("New");
            menuItem = new JMenuItem("New Variable");
            menuItem.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    ObjectPanel.this.getMap().addNewVariable(ObjectPanel.this._ptPopup);
                }
            });
            newItemMenu.add(menuItem);
            menuItem = new JMenuItem("New If statement");
            menuItem.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    ObjectPanel.this.getMap().addNewIfBox(ObjectPanel.this._ptPopup);
                }
            });
            newItemMenu.add(menuItem);
            menuItem = new JMenuItem("New Function");
            menuItem.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    ObjectPanel.this.getMap().addNewFunction(ObjectPanel.this._ptPopup);
                }
            });
            newItemMenu.add(menuItem);
            menuItem = new JMenuItem("New Choose statement");
            menuItem.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    ObjectPanel.this.getMap().addNewChooseBox(ObjectPanel.this._ptPopup);
                }
            });
            newItemMenu.add(menuItem);
            menuItem = new JMenuItem("New When statement");
            menuItem.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    ObjectPanel.this.getMap().addNewWhenBox(ObjectPanel.this._ptPopup);
                }
            });
            newItemMenu.add(menuItem);
            menuItem = new JMenuItem("New complex expression");
            menuItem.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    ObjectPanel.this.getMap().addNewComplexExpression(ObjectPanel.this._ptPopup);
                }
            });
            newItemMenu.add(menuItem);
            this.add(newItemMenu);
            menuItem = new JMenuItem("Clear");
            menuItem.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    ObjectPanel.this.getMap().clear();
                }
            });
            this.add(menuItem);
            menuItem = new JMenuItem("Render");
            menuItem.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    ObjectPanel.this.render();
                }
            });
            this.add(menuItem);
        }
    }

    class MyPopupMenu extends JPopupMenu {

        public MyPopupMenu(IMapObject obj) {
            JMenuItem menuItem = null;
            if (obj != null) {
                if (!obj.isSelected()) objectSelected(obj);
                menuItem = obj.getMenu();
                if (null != menuItem) this.add(menuItem);
            }
            MyMenu thisMenu = (MyMenu) ObjectPanel.this.getMenu();
            this.add(thisMenu);
            if (obj != null) {
                menuItem = new JMenuItem("Remove");
                menuItem.addActionListener(new AbstractAction() {

                    public void actionPerformed(ActionEvent e) {
                        IMapObject obj = _map.getSelectedObject();
                        _map.removeChild(obj);
                    }
                });
                this.add(menuItem);
                menuItem = new JMenuItem("Properties...");
                menuItem.addActionListener(new AbstractAction() {

                    public void actionPerformed(ActionEvent e) {
                        IMapObject obj = ObjectPanel.this.getMap().getChildAt(_ptPopup);
                        showObjectProperties(obj);
                    }
                });
                this.add(menuItem);
                menuItem = new JMenuItem("Copy");
                menuItem.addActionListener(new AbstractAction() {

                    public void actionPerformed(ActionEvent e) {
                        IMapObject obj = ObjectPanel.this.getMap().getChildAt(_ptPopup);
                        IMapObject copy = obj.copy();
                        _map.addDragableObject((DragableMapObject) copy);
                    }
                });
                this.add(menuItem);
            }
        }
    }

    /**
	 * Constructor
	 */
    public ObjectPanel(Map map) {
        _map = map;
        setBackground(Color.white);
        setBorder(null);
        _dropTarget = new DropTarget(this, new MyDropTargetListener());
        _dragSource = new DragSource();
        _dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, new MyDragGestureListener());
        this.addMouseListener(new MyMouseListener());
    }

    /**
	 * Constructor
	 */
    public ObjectPanel(MapView mapview, MapperTree lTree, MapperTree rTree) {
        _mainMapView = mapview;
        setBackground(Color.white);
        setBorder(null);
        _map = new Map(this, lTree, rTree);
        lTree.setMapObjectView(this);
        rTree.setMapObjectView(this);
        _dropTarget = new DropTarget(this, new MyDropTargetListener());
        _dragSource = new DragSource();
        _dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, new MyDragGestureListener());
        this.addMouseListener(new MyMouseListener());
    }

    public void setMenu(JMenu menu) {
        _menu = menu;
    }

    public JMenu getMenu() {
        return _menu;
    }

    public void setAutoRender(boolean bRender) {
        _bAutoRender = bRender;
    }

    public boolean getAutoRender() {
        return _bAutoRender;
    }

    public Map getMap() {
        return _map;
    }

    public void draw(Graphics g, JComponent parent) {
        _map.draw(g, parent);
        if (_bDragLine == true) {
            _dragLine.draw(g, parent);
            _bDragLine = false;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.draw(g, this);
    }

    /**
	 * Creates a new link and adds it to the
	 * _objects collection.
	 *
	 * @param src source Linkable object
	 * @param dst destination Linkable object
	 *
	 * @return Newly created LinkLine object
	 */
    public LinkLine addLink(ILinkable src, ILinkable dst) {
        LinkLine newLink = _map.addLink(src, dst);
        if (null != newLink) {
            if (_bAutoRender) render();
            return newLink;
        }
        return null;
    }

    public void setDragLine(ILinkable src, ILinkable dst) {
        if (src != null && dst != null) {
            _dragLine = new LinkLine(src, dst);
            _bDragLine = true;
        }
    }

    private void showObjectProperties(IMapObject obj) {
        if (obj != null) {
            String prop = "";
            if (obj instanceof LinkLine) prop = obj.toString(); else if (obj instanceof FunctionBox) prop = ((FunctionBox) obj).getName(); else prop = obj.toString();
            JOptionPane.showMessageDialog(ObjectPanel.this, prop, obj.getName() + " properties", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void objectSelected(IMapObject sel) {
        if (null != _mainMapView) _mainMapView.objectSelected(sel);
        _map.objectSelected(sel);
    }

    public void render() {
        if (null != _mainMapView) {
            XSLTRenderer renderer = new XSLTRenderer();
            renderer.render(_map);
            _mainMapView.setXSLT(renderer.getString());
        }
    }
}
