package geisler.projekt.editor.controller;

import geisler.projekt.editor.view.EditorFrame;
import geisler.projekt.editor.view.TileSetLabel;
import geisler.projekt.game.constants.EnumNpcStatus;
import geisler.projekt.game.constants.EnumObjectType;
import geisler.projekt.game.constants.EnumTileAttribute;
import geisler.projekt.game.interfaces.DrawableObj;
import geisler.projekt.game.model.Enemy;
import geisler.projekt.game.model.Friend;
import geisler.projekt.game.model.NonPlayingCharacter;
import geisler.projekt.game.model.NpcShop;
import geisler.projekt.game.model.Tile2D;
import geisler.projekt.game.model.TileAnimation2D;
import geisler.projekt.game.view.GamePanel;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MouseController implements MouseListener, MouseMotionListener, MouseWheelListener {

    private static Log LOG = LogFactory.getLog(MouseController.class);

    EditorFrame parent;

    private Point startRectTileSet;

    private Point stopRectTileSet;

    private Point startRectMapPanel;

    private Point stopRectMapPanel;

    private Point copyFromP;

    private Point pasteToP;

    private DrawableObj selectedObj;

    private List<DrawableObj> listSelectedObjs;

    public MouseController(EditorFrame parent) {
        this.parent = parent;
        this.startRectTileSet = new Point(0, 0);
        this.stopRectTileSet = new Point(0, 0);
        this.startRectMapPanel = new Point(0, 0);
        this.stopRectMapPanel = new Point(0, 0);
        this.copyFromP = null;
        this.pasteToP = null;
        this.selectedObj = null;
        this.listSelectedObjs = new ArrayList<DrawableObj>();
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
        LOG.debug("selektierter Mauspunkt: " + e.getPoint());
        if (copyFromP == null || copyFromP.equals(new Point())) {
            if (selectedObj != null) {
                selectedObj.setSelected(false);
                selectedObj = null;
            }
            if (!listSelectedObjs.isEmpty()) {
                for (DrawableObj drawObj : listSelectedObjs) {
                    if (drawObj != null) {
                        drawObj.setSelected(false);
                    }
                }
                listSelectedObjs.clear();
            }
        }
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.getSource() instanceof TileSetLabel) {
                resetTileSetRects();
                startRectTileSet = parent.getNewRect(e.getPoint());
                parent.getTileSetPanel().setRectAuswahl(startRectTileSet);
            } else if (e.getSource() instanceof GamePanel) {
                resetMapPanelRects();
                startRectMapPanel = parent.getNewRect(e.getPoint());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point exactPoint = (Point) SerializationUtils.clone(e.getPoint());
        if (parent.isInsertMode()) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (e.getSource() instanceof TileSetLabel) {
                    stopRectTileSet = parent.getNewRect(e.getPoint());
                    if (startRectTileSet.equals(stopRectTileSet)) {
                        storeTileInformationsOnEqual();
                    } else {
                        storeTileInformationsOnNotEqual();
                    }
                    LOG.info("Gr��e der tempor�ren Tile-Liste: " + parent.getTileSetPanel().getAuswahlImgs().size());
                } else if (e.getSource() instanceof GamePanel) {
                    stopRectMapPanel = parent.getNewRect(e.getPoint());
                    parent.getLblInfo().setForeground(Color.BLUE);
                    parent.getLblInfo().setText("");
                    if (parent.getAktiveNPC() != null) {
                        insertNPCToMap();
                    } else {
                        try {
                            if (startRectMapPanel.equals(stopRectMapPanel) || parent.getTileSetPanel().getAuswahlImgs().size() > 1) {
                                insertOnExactMapPosition(exactPoint);
                            } else if (!parent.getTileSetPanel().getAuswahlImgs().isEmpty()) {
                                insertOnMapArea();
                            }
                        } catch (Exception e1) {
                            parent.getLblInfo().setForeground(Color.RED);
                            parent.getLblInfo().setText("Bitte erst Ebene ausw�hlen, zum Anlegen von Tiles.");
                        }
                    }
                }
            } else if (e.getSource() instanceof GamePanel && e.getButton() == MouseEvent.BUTTON3) {
                insertAttributeOnExactMapPosition(exactPoint);
            }
        } else {
            if (e.getSource() instanceof GamePanel) {
                stopRectMapPanel = parent.getNewRect(e.getPoint());
                EnumObjectType tempType = GamePanel.getAktiveEnumObjectType();
                Integer tempEbene = GamePanel.getAktiveEbene();
                if (startRectMapPanel.equals(stopRectMapPanel)) {
                    if (copyFromP != null && !copyFromP.equals(new Point())) {
                        return;
                    }
                    Point objPoint = stopRectMapPanel;
                    try {
                        selectedObj = parent.getRess().getMaps().get(parent.getRess().getActiveMap()).getObjs().get(tempType).get(tempEbene).getDrawObjektByXandY(objPoint.x, objPoint.y);
                    } catch (Exception e1) {
                        parent.getLblInfo().setForeground(Color.RED);
                        parent.getLblInfo().setText("Bitte erst Ebene ausw�hlen.");
                    }
                    if (selectedObj != null) {
                        selectedObj.setSelected(true);
                    }
                } else {
                    if (stopRectMapPanel.y - startRectMapPanel.y < 0) {
                        int temp = stopRectMapPanel.y;
                        stopRectMapPanel.y = startRectMapPanel.y;
                        startRectMapPanel.y = temp;
                    }
                    if (stopRectMapPanel.x - startRectMapPanel.x < 0) {
                        int temp = stopRectMapPanel.x;
                        stopRectMapPanel.x = startRectMapPanel.x;
                        startRectMapPanel.x = temp;
                    }
                    int recHeight = stopRectMapPanel.y - startRectMapPanel.y + 32;
                    int recWidth = stopRectMapPanel.x - startRectMapPanel.x + 32;
                    int anzahlRechts = recWidth / 32;
                    int anzahlUnten = recHeight / 32;
                    erste: for (int i = 0; i < anzahlUnten; i++) {
                        zweite: for (int j = 0; j < anzahlRechts; j++) {
                            int x = 0, y = 0;
                            x = (startRectMapPanel.x) + (j * 32);
                            y = (startRectMapPanel.y) + (i * 32);
                            DrawableObj tempDrawO = null;
                            try {
                                tempDrawO = parent.getRess().getMaps().get(parent.getRess().getActiveMap()).getObjs().get(tempType).get(tempEbene).getDrawObjektByXandY(x, y);
                                listSelectedObjs.add(tempDrawO);
                            } catch (Exception e1) {
                                parent.getLblInfo().setForeground(Color.RED);
                                parent.getLblInfo().setText("Bitte erst Ebene ausw�hlen.");
                                break erste;
                            }
                            if (tempDrawO != null) {
                                tempDrawO.setSelected(true);
                            }
                        }
                    }
                }
                parent.getGamePanel().requestFocusInWindow();
            }
        }
        parent.getTileSetPanel().repaint();
        parent.getGamePanel().repaint();
        parent.repaint();
    }

    private void insertOnMapArea() {
        if (stopRectMapPanel.y - startRectMapPanel.y < 0) {
            int temp = stopRectMapPanel.y;
            stopRectMapPanel.y = startRectMapPanel.y;
            startRectMapPanel.y = temp;
        }
        if (stopRectMapPanel.x - startRectMapPanel.x < 0) {
            int temp = stopRectMapPanel.x;
            stopRectMapPanel.x = startRectMapPanel.x;
            startRectMapPanel.x = temp;
        }
        int recHeight = stopRectMapPanel.y - startRectMapPanel.y + 32;
        int recWidth = stopRectMapPanel.x - startRectMapPanel.x + 32;
        int anzahlRechts = recWidth / 32;
        int anzahlUnten = recHeight / 32;
        Tile2D tempTile = parent.getTileSetPanel().getAuswahlImgs().get(0);
        for (int i = 0; i < anzahlUnten; i++) {
            for (int j = 0; j < anzahlRechts; j++) {
                int x = 0, y = 0;
                x = (startRectMapPanel.x) + (j * 32);
                y = (startRectMapPanel.y) + (i * 32);
                DrawableObj neu = null;
                if (parent.getAktiveAnimation() != null) {
                    TileAnimation2D temp = parent.getAktiveAnimation();
                    long id = temp.getId();
                    String name = temp.getName();
                    long delay = temp.getDelay();
                    TileAnimation2D neuTA = new TileAnimation2D(id, name, 0, 0, delay);
                    neuTA.setPics(new ArrayList<Tile2D>());
                    for (Tile2D tile : temp.getPics()) {
                        Tile2D nTile = new Tile2D(0, 0);
                        nTile.setTileImage(tile.getTileImage());
                        nTile.setTileSet(tile.getTileSet());
                        nTile.setTileSetX(tile.getTileSetX());
                        nTile.setTileSetY(tile.getTileSetY());
                        neuTA.getPics().add(nTile);
                    }
                    neuTA.setMapCoords(x, y);
                    neu = neuTA;
                } else {
                    Tile2D neuT = new Tile2D(x, y);
                    neuT.setAttribute(tempTile.getAttribute());
                    neuT.setTileImage(tempTile.getTileImage());
                    neuT.setTileSet(tempTile.getTileSet());
                    neuT.setTileSetX(tempTile.getTileSetX());
                    neuT.setTileSetY(tempTile.getTileSetY());
                    neu = neuT;
                }
                parent.getRess().getMaps().get(parent.getRess().getActiveMap()).getObjs().get(GamePanel.getAktiveEnumObjectType()).get(GamePanel.getAktiveEbene()).addObjectToList(neu);
                LOG.debug("x = " + x + " y = " + y);
            }
        }
    }

    private void insertOnExactMapPosition(Point exactPoint) {
        Point addP = parent.getNewRect(exactPoint);
        int startx = addP.x;
        int starty = addP.y;
        double tempY = 0;
        boolean first = true;
        int tempX = 0;
        for (Tile2D newTile : parent.getTileSetPanel().getAuswahlImgs()) {
            LOG.debug("Drinn in AuswahlImageListe vom TileSetPanel");
            if (newTile.getTileSetY() == tempY || first) {
                tempY = newTile.getTileSetY();
                newTile.x = startx;
                newTile.y = starty;
                if (first) {
                    first = false;
                }
            } else {
                startx -= tempX;
                starty += 32;
                newTile.x = startx;
                newTile.y = starty;
                tempY = newTile.getTileSetY();
                tempX = 0;
            }
            startx += 32;
            tempX += 32;
            DrawableObj neu = null;
            if (parent.getAktiveAnimation() != null) {
                TileAnimation2D temp = parent.getAktiveAnimation();
                long id = temp.getId();
                String name = temp.getName();
                double x = newTile.x;
                double y = newTile.y;
                long delay = temp.getDelay();
                TileAnimation2D neuTA = new TileAnimation2D(id, name, 0, 0, delay);
                neuTA.setPics(new ArrayList<Tile2D>());
                for (Tile2D tile : temp.getPics()) {
                    Tile2D nTile = new Tile2D(0, 0);
                    nTile.setTileImage(tile.getTileImage());
                    nTile.setTileSet(tile.getTileSet());
                    nTile.setTileSetX(tile.getTileSetX());
                    nTile.setTileSetY(tile.getTileSetY());
                    neuTA.getPics().add(nTile);
                }
                neuTA.setMapCoords(x, y);
                neu = neuTA;
            } else {
                Tile2D neuT = new Tile2D(newTile.x, newTile.y);
                neuT.setAttribute(newTile.getAttribute());
                neuT.setTileImage(newTile.getTileImage());
                neuT.setTileSet(newTile.getTileSet());
                neuT.setTileSetX(newTile.getTileSetX());
                neuT.setTileSetY(newTile.getTileSetY());
                neu = neuT;
            }
            parent.getRess().getMaps().get(parent.getRess().getActiveMap()).getObjs().get(GamePanel.getAktiveEnumObjectType()).get(GamePanel.getAktiveEbene()).addObjectToList(neu);
        }
    }

    private void insertNPCToMap() {
        NonPlayingCharacter temp = parent.getAktiveNPC();
        NonPlayingCharacter neuNPC = null;
        if (temp.getNpcStatus() == EnumNpcStatus.STATUS_FRIEND) {
            neuNPC = new Friend(temp.getId(), temp.getSpriteNr(), parent.getRess().getActiveMap(), temp.getName(), stopRectMapPanel.x, stopRectMapPanel.y);
        } else if (temp.getNpcStatus() == EnumNpcStatus.STATUS_ENEMY) {
            neuNPC = new Enemy(temp.getId(), temp.getSpriteNr(), parent.getRess().getActiveMap(), temp.getName(), stopRectMapPanel.x, stopRectMapPanel.y);
        } else if (temp.getNpcStatus() == EnumNpcStatus.STATUS_QUEST) {
        } else if (temp.getNpcStatus() == EnumNpcStatus.STATUS_SAVE_SPOT) {
        } else if (temp.getNpcStatus() == EnumNpcStatus.STATUS_SHOP) {
            neuNPC = new NpcShop(temp.getId(), temp.getSpriteNr(), parent.getRess().getActiveMap(), temp.getName(), stopRectMapPanel.x, stopRectMapPanel.y);
        }
        neuNPC.setListMessages(temp.getListMessages());
        neuNPC.setPicNumber(temp.getPicNumber());
        neuNPC.setHeads(temp.getHeads());
        neuNPC.setPics(temp.getPics());
        parent.getRess().getMaps().get(parent.getRess().getActiveMap()).getObjs().get(EnumObjectType.TYPE_SPRITE).get(0).addObjectToList(neuNPC);
    }

    private void insertAttributeOnExactMapPosition(Point exactPoint) {
        Point exPoint = (Point) SerializationUtils.clone(exactPoint);
        Point addAttrP = parent.getNewRect(exactPoint);
        JComboBox cbb = parent.getButtonPanel().getCbbEnumAttribute();
        EnumTileAttribute value = (EnumTileAttribute) cbb.getSelectedItem();
        try {
            parent.getLblInfo().setForeground(Color.BLUE);
            parent.getLblInfo().setText("");
            Tile2D tempTile = (Tile2D) parent.getRess().getMaps().get(parent.getRess().getActiveMap()).getObjs().get(EnumObjectType.TYPE_SPRITE).get(0).getDrawObjektByXandY(addAttrP.getX(), addAttrP.getY());
            if (tempTile != null) {
                tempTile.setAttribute(value);
            } else {
                tempTile = new Tile2D(addAttrP.x, addAttrP.y);
                tempTile.setAttribute(value);
            }
            byte ori = getRectangleOrientationByPosition(exPoint);
            LOG.info("Orientation: " + ori);
            tempTile.updateFlagsWithOrientation(ori);
            parent.getRess().getMaps().get(parent.getRess().getActiveMap()).getObjs().get(EnumObjectType.TYPE_SPRITE).get(0).addObjectToList(tempTile);
        } catch (Exception e1) {
            parent.getLblInfo().setForeground(Color.RED);
            parent.getLblInfo().setText("Es muss erst eine Ebene im Sprite-Layer angelegt werden um Attribute zu setzen.");
        }
    }

    private void storeTileInformationsOnEqual() {
        parent.getTileSetPanel().setAuswahlWidth(32);
        parent.getTileSetPanel().setAuswahlHeight(32);
        parent.getTileSetPanel().addImageToTileList(startRectTileSet);
        LOG.info(startRectTileSet);
    }

    private void storeTileInformationsOnNotEqual() {
        if (stopRectTileSet.y - startRectTileSet.y < 0) {
            int temp = stopRectTileSet.y;
            stopRectTileSet.y = startRectTileSet.y;
            startRectTileSet.y = temp;
        }
        if (stopRectTileSet.x - startRectTileSet.x < 0) {
            int temp = stopRectTileSet.x;
            stopRectTileSet.x = startRectTileSet.x;
            startRectTileSet.x = temp;
        }
        LOG.info(startRectTileSet);
        int recHeight = stopRectTileSet.y - startRectTileSet.y + 32;
        int recWidth = stopRectTileSet.x - startRectTileSet.x + 32;
        parent.getTileSetPanel().setAuswahlWidth(recWidth);
        parent.getTileSetPanel().setAuswahlHeight(recHeight);
        int anzahlRechts = recWidth / 32;
        int anzahlUnten = recHeight / 32;
        for (int i = 0; i < anzahlUnten; i++) {
            for (int j = 0; j < anzahlRechts; j++) {
                int x = 0, y = 0;
                x = (startRectTileSet.x) + (j * 32);
                y = (startRectTileSet.y) + (i * 32);
                parent.getTileSetPanel().addImageToTileList(new Point(x, y));
                LOG.debug("x = " + x + " y = " + y);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    private void resetTileSetRects() {
        this.startRectTileSet.x = 0;
        this.startRectTileSet.y = 0;
        this.stopRectTileSet.x = 0;
        this.stopRectTileSet.y = 0;
        parent.getTileSetPanel().setAuswahlWidth(32);
        parent.getTileSetPanel().setAuswahlHeight(32);
        parent.getTileSetPanel().setAuswahlImgs(new ArrayList<Tile2D>());
    }

    private void resetMapPanelRects() {
        this.startRectMapPanel.x = 0;
        this.startRectMapPanel.y = 0;
        this.stopRectMapPanel.x = 0;
        this.stopRectMapPanel.y = 0;
    }

    public void resetCopyAttributes() {
        this.copyFromP = null;
        this.pasteToP = null;
        if (this.selectedObj != null) {
            selectedObj.setSelected(false);
        }
        this.selectedObj = null;
        if (this.listSelectedObjs != null) {
            for (DrawableObj selObj : this.listSelectedObjs) {
                if (selObj != null) {
                    selObj.setSelected(false);
                }
            }
        }
        this.listSelectedObjs = new ArrayList<DrawableObj>();
    }

    /**
	 * Methode die aus der gegebenen Mouse-Position
	 * eine Richtung angibt in dem Sie das Viereck in 
	 * gleiche Dreiecke aufteilt und die Position mit diesen
	 * auswertet 
	 * 
	 * @return Wert zwischen 0 und 3 (0 = Up, 1 = Down, 2 = Left, 3 = Right, 4 = Center)
	 */
    private byte getRectangleOrientationByPosition(Point mouseLoc) {
        LOG.info("MousePos: " + mouseLoc);
        int posInRecX = mouseLoc.x % 32;
        int posInRecY = mouseLoc.y % 32;
        LOG.info("RecX: " + posInRecX);
        LOG.info("RecY: " + posInRecY);
        Rectangle2D.Double rectUp = new Rectangle2D.Double(11, 0, 10, 11);
        Rectangle2D.Double rectDown = new Rectangle2D.Double(11, 20, 10, 11);
        Rectangle2D.Double rectLeft = new Rectangle2D.Double(0, 11, 11, 10);
        Rectangle2D.Double rectRight = new Rectangle2D.Double(20, 11, 11, 10);
        Rectangle2D.Double rectCenter = new Rectangle2D.Double(11, 11, 10, 10);
        if (rectUp.contains(posInRecX, posInRecY)) {
            return 0;
        } else if (rectDown.contains(posInRecX, posInRecY)) {
            return 1;
        } else if (rectLeft.contains(posInRecX, posInRecY)) {
            return 2;
        } else if (rectRight.contains(posInRecX, posInRecY)) {
            return 3;
        } else if (rectCenter.contains(posInRecX, posInRecY)) {
            return 4;
        }
        return 5;
    }

    /**
	 * @return the listSelectedObjs
	 */
    public List<DrawableObj> getListSelectedObjs() {
        return listSelectedObjs;
    }

    /**
	 * @return the selectedObj
	 */
    public DrawableObj getSelectedObj() {
        return selectedObj;
    }

    /**
	 * @param copyFromP the copyFromP to set
	 */
    public void setCopyFromP() {
        this.copyFromP = (Point) SerializationUtils.clone(startRectMapPanel);
    }

    /**
	 * @return the copyFromP
	 */
    public Point getCopyFromP() {
        return copyFromP;
    }

    /**
	 * @param pasteToP the pasteToP to set
	 */
    public void setPasteToP() {
        this.pasteToP = (Point) SerializationUtils.clone(stopRectMapPanel);
    }

    /**
	 * @return the pasteToP
	 */
    public Point getPasteToP() {
        return pasteToP;
    }
}
