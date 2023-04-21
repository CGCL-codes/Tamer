package orbe.gui.map.tool.line;

import java.awt.Cursor;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import net.sf.doolin.gui.core.icon.IconSize;
import net.sf.doolin.gui.core.support.GUIUtils;
import net.sf.doolin.gui.service.GUIStrings;
import orbe.gui.map.renderer.LineRenderer;
import orbe.gui.map.renderer.LineRendererFactory;
import orbe.gui.map.tool.ToolLine;
import orbe.gui.task.OrbeTaskLineDelete;
import orbe.gui.task.OrbeTaskLineGradInverse;
import orbe.gui.task.OrbeTaskLineMoveBack;
import orbe.gui.task.OrbeTaskLineMoveDown;
import orbe.gui.task.OrbeTaskLineMoveFront;
import orbe.gui.task.OrbeTaskLineMoveUp;
import orbe.gui.task.OrbeTaskLineNew;
import orbe.gui.task.OrbeTaskLineStyle;
import orbe.model.element.line.OrbeLine;
import orbe.model.style.LineGradType;
import orbe.model.style.StyleLine;

/**
 * Cr�ation d'une nouvelle ligne.
 * 
 * @author Damien Coraboeuf
 * @version $Id: AbstractToolLineDelegateNew.java,v 1.1 2006/12/04 11:41:06
 *          guinnessman Exp $
 */
public abstract class AbstractToolLineDelegateNew<L extends OrbeLine> extends ToolLineDelegate {

    /**
	 * Ligne en cours d'�dition
	 */
    protected L editedLine;

    /**
	 * Curseur d'�dition.
	 */
    private Cursor cursorCross;

    /**
	 * Constructeur.
	 */
    public AbstractToolLineDelegateNew(ToolLine tool) {
        super(tool);
        cursorCross = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }

    /**
	 * MAJ du curseur
	 * 
	 * @see orbe.gui.map.tool.line.ToolLineDelegate#mouseMoved(java.awt.event.MouseEvent)
	 */
    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        getToolLine().setCursor(cursorCross);
        if (editedLine != null) {
            getToolLine().getControler().setOutlinedShape(null);
            onMouseMovedForEdition(e);
        } else {
            OrbeLine line = getLine(e.getPoint());
            if (line != null) {
                LineRenderer renderer = LineRendererFactory.getInstance().getInstance(line.getForm());
                @SuppressWarnings("unchecked") Shape shape = renderer.getOutline(getMap(), getViewSettings(), line);
                getToolLine().getControler().setOutlinedShape(shape);
            } else {
                getToolLine().getControler().setOutlinedShape(null);
            }
        }
    }

    protected abstract void onMouseMovedForEdition(MouseEvent e);

    /**
	 * Annule toute �dition en cours.
	 * 
	 * @see orbe.gui.map.tool.line.ToolLineDelegate#stop()
	 */
    @Override
    public void stop() {
        if (editedLine != null) {
            if (editedLine.getPoints().size() > 1) {
                OrbeTaskLineNew task = new OrbeTaskLineNew(getToolLine().getControler(), editedLine);
                getToolLine().getControler().getContext().addEdit(task);
            } else {
                getMap().getLineList().remove(editedLine);
            }
            editedLine = null;
        }
        getToolLine().getControler().setOutlinedShape(null);
        getToolLine().getControler().setEditionShape(null);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if (editedLine == null && e.isPopupTrigger()) {
            OrbeLine line = getLine(e.getPoint());
            if (line != null) {
                JPopupMenu menu = createMenu(line);
                menu.show(getToolLine().getControler().getComponent(), e.getX(), e.getY());
                getToolLine().getControler().setOutlinedShape(null);
            }
        }
    }

    /**
	 * Menu pour une ligne
	 */
    protected JPopupMenu createMenu(final OrbeLine line) {
        JPopupMenu menu = new JPopupMenu();
        createMenuDelete(line, menu);
        createMenuGraduations(line, menu);
        createMenuOrder(line, menu);
        createMenuStyles(line, menu);
        return menu;
    }

    protected void createMenuOrder(final OrbeLine line, JPopupMenu menu) {
        menu.addSeparator();
        JMenuItem itemMoveFront = new JMenuItem();
        itemMoveFront.setText(GUIStrings.get("ToolLine.Menu.MoveFront"));
        itemMoveFront.setIcon(GUIUtils.getIcon("IconMoveFront", IconSize.SMALL));
        itemMoveFront.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                OrbeTaskLineMoveFront task = new OrbeTaskLineMoveFront(getToolLine().getControler(), line);
                task.process();
                getToolLine().getControler().getContext().addEdit(task);
            }
        });
        menu.add(itemMoveFront);
        JMenuItem itemMoveUp = new JMenuItem();
        itemMoveUp.setText(GUIStrings.get("ToolLine.Menu.MoveUp"));
        itemMoveUp.setIcon(GUIUtils.getIcon("IconMoveUp", IconSize.SMALL));
        itemMoveUp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                OrbeTaskLineMoveUp task = new OrbeTaskLineMoveUp(getToolLine().getControler(), line);
                task.process();
                getToolLine().getControler().getContext().addEdit(task);
            }
        });
        menu.add(itemMoveUp);
        JMenuItem itemMoveDown = new JMenuItem();
        itemMoveDown.setText(GUIStrings.get("ToolLine.Menu.MoveDown"));
        itemMoveDown.setIcon(GUIUtils.getIcon("IconMoveDown", IconSize.SMALL));
        itemMoveDown.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                OrbeTaskLineMoveDown task = new OrbeTaskLineMoveDown(getToolLine().getControler(), line);
                task.process();
                getToolLine().getControler().getContext().addEdit(task);
            }
        });
        menu.add(itemMoveDown);
        JMenuItem itemMoveBack = new JMenuItem();
        itemMoveBack.setText(GUIStrings.get("ToolLine.Menu.MoveBack"));
        itemMoveBack.setIcon(GUIUtils.getIcon("IconMoveBack", IconSize.SMALL));
        itemMoveBack.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                OrbeTaskLineMoveBack task = new OrbeTaskLineMoveBack(getToolLine().getControler(), line);
                task.process();
                getToolLine().getControler().getContext().addEdit(task);
            }
        });
        menu.add(itemMoveBack);
    }

    protected void createMenuDelete(final OrbeLine line, JPopupMenu menu) {
        JMenuItem itemDelete = new JMenuItem();
        itemDelete.setText(GUIStrings.get("ToolLine.Menu.Delete"));
        itemDelete.setIcon(GUIUtils.getIcon("IconDelete", IconSize.SMALL));
        itemDelete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                delete(line);
            }
        });
        menu.add(itemDelete);
    }

    protected void createMenuGraduations(final OrbeLine line, JPopupMenu menu) {
        if (line.getStyle().getGrad() == LineGradType.ONE) {
            menu.addSeparator();
            JCheckBoxMenuItem item = new JCheckBoxMenuItem();
            item.setText(GUIStrings.get("ToolLine.Menu.GradInverse"));
            item.setSelected(line.isGradInverse());
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    OrbeTaskLineGradInverse task = new OrbeTaskLineGradInverse(getToolLine().getControler(), line);
                    task.process();
                    getToolLine().getControler().getContext().addEdit(task);
                }
            });
            menu.add(item);
        }
    }

    protected void createMenuStyles(final OrbeLine line, JPopupMenu menu) {
        menu.addSeparator();
        List<StyleLine> styles = getMap().getSettings().getStyleLineList().getStyles();
        for (final StyleLine style : styles) {
            JMenuItem item = new JMenuItem();
            item.setText(style.getName());
            item.setForeground(style.getColor());
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    editStyle(line, style);
                }
            });
            menu.add(item);
        }
    }

    /**
	 * Edition du style d'une ligne
	 */
    protected void editStyle(OrbeLine line, StyleLine style) {
        if (style.getId() != line.getStyle().getId()) {
            OrbeTaskLineStyle task = new OrbeTaskLineStyle(getToolLine().getControler(), line, style);
            task.process();
            getToolLine().getControler().getContext().addEdit(task);
        }
    }

    /**
	 * Suppression d'une ligne
	 */
    protected void delete(OrbeLine line) {
        if (GUIUtils.getAlertManager().confirm(getToolLine().getControler().getGUIFView(), "ToolLine.Delete.prompt")) {
            OrbeTaskLineDelete task = new OrbeTaskLineDelete(getToolLine().getControler(), line);
            task.process();
            getToolLine().getControler().getContext().addEdit(task);
        }
    }
}
