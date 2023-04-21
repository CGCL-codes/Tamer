package edu.xtec.jclic.activities.panels;

import edu.xtec.jclic.*;
import edu.xtec.jclic.automation.ActiveBagContentKit;
import edu.xtec.jclic.boxes.*;
import edu.xtec.jclic.media.EventSounds;
import edu.xtec.util.JDomUtility;
import java.awt.*;
import java.awt.event.MouseEvent;
import edu.xtec.jclic.project.JClicProject;

/**
 *
 * @author Francesc Busquets (fbusquets@xtec.net)
 * @version 1.0
 */
public class Identify extends Activity implements ActiveBagContentKit.Compatible {

    int nonAssignedCells;

    int cellsToMatch;

    /** Creates new Identify */
    public Identify(JClicProject project) {
        super(project);
        abc = new ActiveBagContent[2];
        scramble[0] = true;
        nonAssignedCells = 0;
        cellsToMatch = 1;
    }

    public void initNew() {
        super.initNew();
        abc[0] = ActiveBagContent.initNew(3, 2, 'A');
        abc[0].setAllIdsTo(0);
        abc[0].defaultIdValue = 0;
    }

    public org.jdom.Element getJDomElement() {
        org.jdom.Element ex = null;
        if (abc[0] == null) return null;
        org.jdom.Element e = super.getJDomElement();
        e.addContent(abc[0].getJDomElement().setAttribute(ID, PRIMARY));
        if (abc[1] != null) e.addContent(abc[1].getJDomElement().setAttribute(ID, SOLVED_PRIMARY));
        ex = new org.jdom.Element(SCRAMBLE);
        {
            ex.setAttribute(TIMES, Integer.toString(shuffles));
            ex.setAttribute(PRIMARY, JDomUtility.boolString(scramble[0]));
            e.addContent(ex);
        }
        return e;
    }

    public void setProperties(org.jdom.Element e, Object aux) throws Exception {
        org.jdom.Element child;
        super.setProperties(e, aux);
        ActiveBagContent bag = null;
        abc[1] = null;
        java.util.Iterator itr = e.getChildren(ActiveBagContent.ELEMENT_NAME).iterator();
        while (itr.hasNext()) {
            child = ((org.jdom.Element) itr.next());
            bag = ActiveBagContent.getActiveBagContent(child, project.mediaBag);
            String id = JDomUtility.getStringAttr(child, ID, PRIMARY, false);
            if (PRIMARY.equals(id)) abc[0] = bag; else if (SOLVED_PRIMARY.equals(id)) abc[1] = bag;
        }
        if (abc[0] == null) throw new IllegalArgumentException("Identify activity without contents");
        if ((child = e.getChild(SCRAMBLE)) != null) {
            shuffles = JDomUtility.getIntAttr(child, TIMES, shuffles);
            scramble[0] = JDomUtility.getBoolAttr(child, PRIMARY, scramble[0]);
        }
    }

    public void setProperties(edu.xtec.jclic.clic3.Clic3Activity c3a) throws Exception {
        super.setProperties(c3a);
        abc[0] = c3a.createActiveBagContent(0);
        abc[0].setBoxBase(c3a.getBoxBase(0));
        abc[0].setIds(c3a.ass);
        scramble[0] = c3a.bar[0];
        abc[1] = c3a.sol ? c3a.createActiveBagContent(2) : null;
    }

    public int getMinNumActions() {
        return cellsToMatch;
    }

    public boolean hasRandom() {
        return true;
    }

    public Activity.Panel getActivityPanel(PlayStation ps) {
        return new Panel(ps);
    }

    class Panel extends Activity.Panel {

        ActiveBoxBag bg;

        protected Panel(PlayStation ps) {
            super(ps);
            bg = null;
        }

        public void clear() {
            if (bg != null) {
                bg.end();
                bg = null;
            }
        }

        public void buildVisualComponents() throws Exception {
            if (firstRun) super.buildVisualComponents();
            clear();
            if (abc[0] != null) {
                if (acp != null) acp.generateContent(new ActiveBagContentKit(abc[0].nch, abc[0].ncw, (new ActiveBagContent[] { abc[0], null, abc[1] }), true), ps);
                bg = ActiveBoxGrid.createEmptyGrid(null, this, margin, margin, abc[0]);
                bg.setContent(abc[0], abc[1]);
                bg.setAlternative(false);
                bg.setDefaultIdAss();
                nonAssignedCells = 0;
                cellsToMatch = 0;
                for (int i = 0; i < bg.getNumCells(); i++) {
                    ActiveBox bx = bg.getActiveBox(i);
                    int id = bx.idAss;
                    if (id == 1) cellsToMatch++; else if (id == -1) {
                        nonAssignedCells++;
                        bx.switchToAlt(ps);
                    }
                }
                bg.setVisible(true);
                invalidate();
            }
        }

        public void initActivity() throws Exception {
            super.initActivity();
            if (!firstRun) buildVisualComponents();
            firstRun = false;
            setAndPlayMsg(MAIN, EventSounds.START);
            if (bg != null) {
                if (scramble[0]) shuffle(new ActiveBoxBag[] { bg }, true, true);
                if (useOrder) currentItem = bg.getNextItem(-1, 1);
                playing = true;
            }
        }

        public void render(Graphics2D g2, Rectangle dirtyRegion) {
            if (bg != null) bg.update(g2, dirtyRegion, this);
        }

        public Dimension setDimension(Dimension preferredMaxSize) {
            if (getSize().equals(preferredMaxSize)) return preferredMaxSize;
            return BoxBag.layoutSingle(preferredMaxSize, bg, margin);
        }

        public void processMouse(MouseEvent e) {
            ActiveBox bx;
            boolean m = false;
            if (playing && e.getID() == MouseEvent.MOUSE_PRESSED) {
                ps.stopMedia(1);
                if ((bx = bg.findActiveBox(e.getPoint())) != null) {
                    if (bx.idAss != -1) {
                        boolean ok = false;
                        String src = bx.getDescription();
                        m |= bx.playMedia(ps);
                        if (bx.idAss == 1 && (!useOrder || bx.idOrder == currentItem)) {
                            ok = true;
                            bx.idAss = -1;
                            if (bx.switchToAlt(ps)) m |= bx.playMedia(ps); else bx.clear();
                            if (useOrder) currentItem = bg.getNextItem(currentItem, 1);
                        }
                        int cellsOk = bg.countCellsWithIdAss(-1);
                        ps.reportNewAction(getActivity(), ACTION_SELECT, src, null, ok, cellsOk - nonAssignedCells);
                        if (ok && cellsOk == cellsToMatch + nonAssignedCells) finishActivity(true); else if (!m) playEvent(ok ? EventSounds.ACTION_OK : EventSounds.ACTION_ERROR);
                    }
                }
            }
        }
    }
}
