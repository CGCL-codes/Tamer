package com.roslan.games.moo3d.ui;

import java.util.logging.Logger;
import org.openmali.FastMath;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
import org.xith3d.scenegraph.Texture2D;
import org.xith3d.ui.hud.base.AbstractButton;
import org.xith3d.ui.hud.listeners.ButtonListener;
import org.xith3d.ui.hud.utils.HUDTextureUtils;
import org.xith3d.ui.hud.utils.TileMode;
import org.xith3d.ui.hud.widgets.Button;
import org.xith3d.ui.hud.widgets.Frame;
import org.xith3d.ui.hud.widgets.Label;
import org.xith3d.ui.text2d.TextAlignment;
import com.roslan.games.moo3d.FleetManager;
import com.roslan.games.moo3d.FleetNode.FleetStatus;
import com.roslan.games.moo3d.GameManager;
import com.roslan.games.moo3d.data.Fleet;
import com.roslan.games.moo3d.data.ShipType;

/**
 * Comments go here.
 *
 * @author Roslan Amir
 * @version 1.0 - Jul 21, 2009
 */
public class FleetInfoPanel implements ButtonListener {

    /**
	 * Log4J object for logging purposes.
	 */
    private static final Logger logger = Logger.getLogger("com.roslan.games");

    /**
	 *
	 */
    private static final String RES_TEXTURE_FLEET_INFO = "fleet_info.png";

    /**
	 *
	 */
    private static final String[][] RES_TEXTURE_ALLOC_BUTTONS = { { "button_minimize.png", "button_minimize_pressed.png" }, { "button_decrement.png", "button_decrement_pressed.png" }, { "button_increment.png", "button_increment_pressed.png" }, { "button_maximize.png", "button_maximize_pressed.png" } };

    /**
	 *
	 */
    private static final String RES_TEXTURE_CLOSE_BUTTON = "button_close.png";

    private static final String RES_TEXTURE_CLOSE_BUTTON_HOVERED = "button_close_hovered.png";

    private static final String RES_TEXTURE_CLOSE_BUTTON_PRESSED = "button_close_pressed.png";

    private static final String RES_TEXTURE_RESET_BUTTON = "button_reset.png";

    private static final String RES_TEXTURE_RESET_BUTTON_HOVERED = "button_reset_hovered.png";

    private static final String RES_TEXTURE_RESET_BUTTON_PRESSED = "button_reset_pressed.png";

    private static final String RES_TEXTURE_SEND_BUTTON = "button_send.png";

    private static final String RES_TEXTURE_SEND_BUTTON_HOVERED = "button_send_hovered.png";

    private static final String RES_TEXTURE_SEND_BUTTON_PRESSED = "button_send_pressed.png";

    /**
	 *
	 */
    private final Frame fleetInfoFrame;

    /**
	 * An array of Labels to represent the ship names for each design type.
	 */
    private final Label[] shipNameLabel = new Label[ShipType.TYPE_COUNT];

    /**
	 * An array of Labels to represent the ship count for each design type.
	 */
    private final Label[] shipCountLabel = new Label[ShipType.TYPE_COUNT];

    /**
	 * A 2-dimensional array representing each of the 4 allocation buttons per design type.
	 */
    private final Button[][] allocButton = new Button[ShipType.TYPE_COUNT][4];

    /**
	 * The starting count for each ship type in the current fleet.
	 */
    private final int[] maxShipCount = new int[ShipType.TYPE_COUNT];

    /**
	 * An array for indexing the actual ship design index at the current row index on the panel.
	 */
    private final int[] shipTypeIndex = new int[ShipType.TYPE_COUNT];

    /**
	 * The text label for displaying the warp speed of the whole fleet.
	 */
    private final Label warpSpeedLabel;

    /**
	 * The text label for displaying the maximum range of the whole fleet.
	 */
    private final Label maxRangeLabel;

    /**
	 * The text label for displaying the current destination.
	 */
    private final Label destinationLabel;

    /**
	 * The text label for displaying the distance to the current destination.
	 */
    private final Label distanceLabel;

    /**
	 * The text label for displaying the number of turns to arrive at the destination.
	 */
    private final Label turnsLabel;

    /**
     *
     */
    private final Button closeButton;

    /**
     *
     */
    private final Button resetButton;

    /**
     *
     */
    private final Button sendButton;

    /**
	 *
	 */
    private final GameManager gameManager;

    /**
	 *
	 */
    private final HUDManager hudManager;

    /**
	 *
	 */
    private final FleetManager fleetManager;

    private Texture2D textureButtonMinimize1;

    private Texture2D textureButtonMinimize2;

    private Texture2D textureButtonDecrement1;

    private Texture2D textureButtonDecrement2;

    private Texture2D textureButtonIncrement1;

    private Texture2D textureButtonIncrement2;

    private Texture2D textureButtonMaximize1;

    private Texture2D textureButtonMaximize2;

    private Texture2D textureButtonClose1;

    private Texture2D textureButtonClose2;

    private Texture2D textureButtonClose3;

    private Texture2D textureButtonReset1;

    private Texture2D textureButtonReset2;

    private Texture2D textureButtonReset3;

    private Texture2D textureButtonSend1;

    private Texture2D textureButtonSend2;

    private Texture2D textureButtonSend3;

    /**
	 * Default constructor.
	 */
    public FleetInfoPanel() {
        gameManager = GameManager.getGameManager();
        hudManager = HUDManager.getHUDManager();
        fleetManager = FleetManager.getFleetManager();
        Texture2D textureFleetInfo = HUDTextureUtils.getTexture(RES_TEXTURE_FLEET_INFO);
        textureButtonMinimize1 = HUDTextureUtils.getTexture(RES_TEXTURE_ALLOC_BUTTONS[0][0]);
        textureButtonMinimize2 = HUDTextureUtils.getTexture(RES_TEXTURE_ALLOC_BUTTONS[0][1]);
        textureButtonDecrement1 = HUDTextureUtils.getTexture(RES_TEXTURE_ALLOC_BUTTONS[1][0]);
        textureButtonDecrement2 = HUDTextureUtils.getTexture(RES_TEXTURE_ALLOC_BUTTONS[1][1]);
        textureButtonIncrement1 = HUDTextureUtils.getTexture(RES_TEXTURE_ALLOC_BUTTONS[2][0]);
        textureButtonIncrement2 = HUDTextureUtils.getTexture(RES_TEXTURE_ALLOC_BUTTONS[2][1]);
        textureButtonMaximize1 = HUDTextureUtils.getTexture(RES_TEXTURE_ALLOC_BUTTONS[3][0]);
        textureButtonMaximize2 = HUDTextureUtils.getTexture(RES_TEXTURE_ALLOC_BUTTONS[3][1]);
        textureButtonClose1 = HUDTextureUtils.getTexture(RES_TEXTURE_CLOSE_BUTTON);
        textureButtonClose2 = HUDTextureUtils.getTexture(RES_TEXTURE_CLOSE_BUTTON_HOVERED);
        textureButtonClose3 = HUDTextureUtils.getTexture(RES_TEXTURE_CLOSE_BUTTON_PRESSED);
        textureButtonReset1 = HUDTextureUtils.getTexture(RES_TEXTURE_RESET_BUTTON);
        textureButtonReset2 = HUDTextureUtils.getTexture(RES_TEXTURE_RESET_BUTTON_HOVERED);
        textureButtonReset3 = HUDTextureUtils.getTexture(RES_TEXTURE_RESET_BUTTON_PRESSED);
        textureButtonSend1 = HUDTextureUtils.getTexture(RES_TEXTURE_SEND_BUTTON);
        textureButtonSend2 = HUDTextureUtils.getTexture(RES_TEXTURE_SEND_BUTTON_HOVERED);
        textureButtonSend3 = HUDTextureUtils.getTexture(RES_TEXTURE_SEND_BUTTON_PRESSED);
        final float FRAME_WIDTH = 256;
        final float FRAME_HEIGHT = 512;
        fleetInfoFrame = new Frame(FRAME_WIDTH, FRAME_HEIGHT);
        fleetInfoFrame.getContentPane().setBackgroundTexture(textureFleetInfo, TileMode.TILE_BOTH);
        gameManager.hud.getContentPane().addWidget(fleetInfoFrame, gameManager.hud.getWidth() - fleetInfoFrame.getWidth(), 28);
        final float SHIP_COUNT_LABEL_WIDTH = 118;
        final float SHIP_COUNT_LABEL_HEIGHT = 48;
        final Colorf SHIP_COUNT_TEXT_COLOR = Colorf.YELLOW;
        final float SHIP_COUNT_LABEL_XPOS = 16;
        final float SHIP_COUNT_LABEL_YPOS = 50;
        final float SHIP_NAME_LABEL_WIDTH = 100;
        final float SHIP_NAME_LABEL_HEIGHT = 26;
        final Colorf SHIP_NAME_TEXT_COLOR = Colorf.WHITE;
        final float SHIP_NAME_LABEL_XPOS = 140;
        final float SHIP_NAME_LABEL_YPOS = 50;
        final float ALLOC_BUTTON_HEIGHT = 20;
        final float MIN_MAX_ALLOC_BUTTON_WIDTH = 28;
        final float DEC_INC_ALLOC_BUTTON_WIDTH = 19;
        final float ALLOC_BUTTON_XPOS = 140;
        final float ALLOC_BUTTON_YPOS = 78;
        float shipCountYPos = SHIP_COUNT_LABEL_YPOS;
        float shipNameYPos = SHIP_NAME_LABEL_YPOS;
        float allocButtonYPos = ALLOC_BUTTON_YPOS;
        for (int row = 0; row < ShipType.TYPE_COUNT; row++) {
            shipCountLabel[row] = new Label(SHIP_COUNT_LABEL_WIDTH, SHIP_COUNT_LABEL_HEIGHT, "", hudManager.fontFleetInfoText, SHIP_COUNT_TEXT_COLOR, TextAlignment.BOTTOM_CENTER);
            fleetInfoFrame.getContentPane().addWidget(shipCountLabel[row], SHIP_COUNT_LABEL_XPOS, shipCountYPos);
            shipCountYPos += SHIP_COUNT_LABEL_HEIGHT + 6;
            shipNameLabel[row] = new Label(SHIP_NAME_LABEL_WIDTH, SHIP_NAME_LABEL_HEIGHT, "", hudManager.fontFleetInfoText, SHIP_NAME_TEXT_COLOR, TextAlignment.CENTER_CENTER);
            fleetInfoFrame.getContentPane().addWidget(shipNameLabel[row], SHIP_NAME_LABEL_XPOS, shipNameYPos);
            shipNameYPos += SHIP_COUNT_LABEL_HEIGHT + 6;
            float allocButtonXPos = ALLOC_BUTTON_XPOS;
            allocButton[row][0] = new Button(MIN_MAX_ALLOC_BUTTON_WIDTH, ALLOC_BUTTON_HEIGHT, textureButtonMinimize1, textureButtonMinimize1, textureButtonMinimize2);
            allocButton[row][0].addButtonListener(this);
            allocButton[row][0].setUserObject(new Integer(row * 10 + 0));
            fleetInfoFrame.getContentPane().addWidget(allocButton[row][0], allocButtonXPos, allocButtonYPos);
            allocButtonXPos += MIN_MAX_ALLOC_BUTTON_WIDTH + 2;
            allocButton[row][1] = new Button(DEC_INC_ALLOC_BUTTON_WIDTH, ALLOC_BUTTON_HEIGHT, textureButtonDecrement1, textureButtonDecrement1, textureButtonDecrement2);
            allocButton[row][1].addButtonListener(this);
            allocButton[row][1].setUserObject(new Integer(row * 10 + 1));
            fleetInfoFrame.getContentPane().addWidget(allocButton[row][1], allocButtonXPos, allocButtonYPos);
            allocButtonXPos += DEC_INC_ALLOC_BUTTON_WIDTH + 2;
            allocButton[row][2] = new Button(DEC_INC_ALLOC_BUTTON_WIDTH, ALLOC_BUTTON_HEIGHT, textureButtonIncrement1, textureButtonIncrement1, textureButtonIncrement2);
            allocButton[row][2].addButtonListener(this);
            allocButton[row][2].setUserObject(new Integer(row * 10 + 2));
            fleetInfoFrame.getContentPane().addWidget(allocButton[row][2], allocButtonXPos, allocButtonYPos);
            allocButtonXPos += DEC_INC_ALLOC_BUTTON_WIDTH + 2;
            allocButton[row][3] = new Button(MIN_MAX_ALLOC_BUTTON_WIDTH, ALLOC_BUTTON_HEIGHT, textureButtonMaximize1, textureButtonMaximize1, textureButtonMaximize2);
            allocButton[row][3].addButtonListener(this);
            allocButton[row][3].setUserObject(new Integer(row * 10 + 3));
            fleetInfoFrame.getContentPane().addWidget(allocButton[row][3], allocButtonXPos, allocButtonYPos);
            allocButtonYPos += SHIP_COUNT_LABEL_HEIGHT + 6;
        }
        final Colorf INFO_TEXT_COLOR = Colorf.WHITE;
        final float DEST_LABEL_WIDTH = 228;
        final float DEST_LABEL_HEIGHT = 20;
        final float DEST_LABEL_XPOS = 14;
        final float DEST_LABEL_YPOS = 386;
        final float DIST_LABEL_WIDTH = 113;
        final float DIST_LABEL_HEIGHT = 20;
        final float DIST_LABEL_XPOS = 14;
        final float DIST_LABEL_YPOS = 408;
        final float TURNS_LABEL_WIDTH = 113;
        final float TURNS_LABEL_HEIGHT = 20;
        final float TURNS_LABEL_XPOS = 129;
        final float TURNS_LABEL_YPOS = 408;
        final float RANGE_LABEL_WIDTH = 113;
        final float RANGE_LABEL_HEIGHT = 20;
        final float RANGE_LABEL_XPOS = 14;
        final float RANGE_LABEL_YPOS = 430;
        final float SPEED_LABEL_WIDTH = 113;
        final float SPEED_LABEL_HEIGHT = 20;
        final float SPEED_LABEL_XPOS = 129;
        final float SPEED_LABEL_YPOS = 430;
        destinationLabel = new Label(DEST_LABEL_WIDTH, DEST_LABEL_HEIGHT, "", hudManager.fontFleetInfoText, INFO_TEXT_COLOR, TextAlignment.CENTER_CENTER);
        fleetInfoFrame.getContentPane().addWidget(destinationLabel, DEST_LABEL_XPOS, DEST_LABEL_YPOS);
        distanceLabel = new Label(DIST_LABEL_WIDTH, DIST_LABEL_HEIGHT, "", hudManager.fontFleetInfoText, INFO_TEXT_COLOR, TextAlignment.CENTER_CENTER);
        fleetInfoFrame.getContentPane().addWidget(distanceLabel, DIST_LABEL_XPOS, DIST_LABEL_YPOS);
        turnsLabel = new Label(TURNS_LABEL_WIDTH, TURNS_LABEL_HEIGHT, "", hudManager.fontFleetInfoText, INFO_TEXT_COLOR, TextAlignment.CENTER_CENTER);
        fleetInfoFrame.getContentPane().addWidget(turnsLabel, TURNS_LABEL_XPOS, TURNS_LABEL_YPOS);
        maxRangeLabel = new Label(RANGE_LABEL_WIDTH, RANGE_LABEL_HEIGHT, "", hudManager.fontFleetInfoText, INFO_TEXT_COLOR, TextAlignment.CENTER_CENTER);
        fleetInfoFrame.getContentPane().addWidget(maxRangeLabel, RANGE_LABEL_XPOS, RANGE_LABEL_YPOS);
        warpSpeedLabel = new Label(SPEED_LABEL_WIDTH, SPEED_LABEL_HEIGHT, "", hudManager.fontFleetInfoText, INFO_TEXT_COLOR, TextAlignment.CENTER_CENTER);
        fleetInfoFrame.getContentPane().addWidget(warpSpeedLabel, SPEED_LABEL_XPOS, SPEED_LABEL_YPOS);
        final float BUTTON_WIDTH = 75;
        final float BUTTON_HEIGHT = 32;
        final float BUTTON_START_XPOS = 14;
        final float BUTTON_START_YPOS = 466;
        float buttonXPos = BUTTON_START_XPOS;
        closeButton = new Button(BUTTON_WIDTH, BUTTON_HEIGHT, textureButtonClose1, textureButtonClose2, textureButtonClose3);
        closeButton.addButtonListener(this);
        closeButton.setUserObject(new Integer(60));
        fleetInfoFrame.getContentPane().addWidget(closeButton, buttonXPos, BUTTON_START_YPOS);
        buttonXPos += BUTTON_WIDTH + 2;
        resetButton = new Button(BUTTON_WIDTH - 1, BUTTON_HEIGHT, textureButtonReset1, textureButtonReset2, textureButtonReset3);
        resetButton.addButtonListener(this);
        resetButton.setUserObject(new Integer(70));
        fleetInfoFrame.getContentPane().addWidget(resetButton, buttonXPos, BUTTON_START_YPOS);
        buttonXPos += BUTTON_WIDTH - 1 + 2;
        sendButton = new Button(BUTTON_WIDTH, BUTTON_HEIGHT, textureButtonSend1, textureButtonSend2, textureButtonSend3);
        sendButton.addButtonListener(this);
        sendButton.setUserObject(new Integer(80));
        fleetInfoFrame.getContentPane().addWidget(sendButton, buttonXPos, BUTTON_START_YPOS);
    }

    /**
	 * Check if this frame is currently visible.
	 *
	 * @return
	 */
    public boolean isVisible() {
        return fleetInfoFrame.isVisible();
    }

    /**
	 * Set this frame's visibility.
	 *
	 * @param visible
	 */
    public void setVisible(boolean visible) {
        fleetInfoFrame.setVisible(visible);
    }

    /**
	 * Updates the widgets based on the selected fleet and destination star. Comes here when the player picks a fleet.
	 */
    public void init() {
        logger.info("Inside FleetInfoPanel.init()");
        Fleet currentFleet = fleetManager.currentFleetNode.getFleet();
        maxRangeLabel.setText("Max Range: " + String.valueOf(currentFleet.getMaxRange()));
        warpSpeedLabel.setText("Max Speed: " + String.valueOf(currentFleet.getWarpSpeed()));
        switch(fleetManager.currentFleetNode.status) {
            case ORBITING:
                int row = 0;
                for (int index = 0; index < ShipType.TYPE_COUNT; index++) {
                    if (currentFleet.getShipCount(index) > 0) {
                        maxShipCount[index] = currentFleet.getShipCount(index);
                        shipTypeIndex[row] = index;
                        shipNameLabel[row].setText(currentFleet.getOwner().getShipType(index).getName());
                        shipCountLabel[row].setText(String.valueOf(currentFleet.getShipCount(index)));
                        setLabelsVisible(row, true);
                        showShipAllocation(row);
                        row++;
                    }
                }
                while (row < ShipType.TYPE_COUNT) {
                    setLabelsVisible(row, false);
                    hideShipAllocation(row);
                    row++;
                }
                fleetManager.destinationStarNode = null;
                destinationLabel.setText("Orbiting: " + fleetManager.currentFleetNode.orbiting.getStar().getStarName());
                distanceLabel.setText("");
                turnsLabel.setText("");
                disableResetButton();
                disableSendButton();
                fleetManager.hideFleetRoute();
                fleetManager.tempFleet.copy(currentFleet);
                logger.info("tempFleet=" + fleetManager.tempFleet);
                break;
            case DEPARTING:
                row = 0;
                for (int index = 0; index < ShipType.TYPE_COUNT; index++) {
                    if (currentFleet.getShipCount(index) > 0) {
                        shipNameLabel[row].setText(currentFleet.getOwner().getShipType(index).getName());
                        shipCountLabel[row].setText(String.valueOf(currentFleet.getShipCount(index)));
                        setLabelsVisible(row, true);
                        hideShipAllocation(row);
                        row++;
                    }
                }
                while (row < ShipType.TYPE_COUNT) {
                    setLabelsVisible(row, false);
                    hideShipAllocation(row);
                    row++;
                }
                fleetManager.destinationStarNode = fleetManager.currentFleetNode.destination;
                destinationLabel.setText("Destination: " + ((fleetManager.destinationStarNode.getStar().isExplored()) ? fleetManager.destinationStarNode.getName() : "Unknown"));
                distanceLabel.setText(String.valueOf(currentFleet.getDistanceToDestination()) + " parsecs");
                turnsLabel.setText("ETA: " + String.valueOf(currentFleet.getTurnsToArrive()) + " turns");
                enableResetButton();
                disableSendButton();
                fleetManager.showFleetRoute(fleetManager.destinationStarNode, Colorf.GREEN);
                break;
            default:
                row = 0;
                for (int index = 0; index < ShipType.TYPE_COUNT; index++) {
                    if (currentFleet.getShipCount(index) > 0) {
                        shipNameLabel[row].setText(currentFleet.getOwner().getShipType(index).getName());
                        shipCountLabel[row].setText(String.valueOf(currentFleet.getShipCount(index)));
                        setLabelsVisible(row, true);
                        hideShipAllocation(row);
                        row++;
                    }
                }
                while (row < ShipType.TYPE_COUNT) {
                    setLabelsVisible(row, false);
                    hideShipAllocation(row);
                    row++;
                }
                fleetManager.destinationStarNode = fleetManager.currentFleetNode.destination;
                destinationLabel.setText("Destination: " + ((fleetManager.destinationStarNode.getStar().isExplored()) ? fleetManager.destinationStarNode.getName() : "Unknown"));
                distanceLabel.setText(String.valueOf(currentFleet.getDistanceToDestination()) + " parsecs");
                turnsLabel.setText("ETA: " + String.valueOf(currentFleet.getTurnsToArrive()) + " turns");
                disableResetButton();
                disableSendButton();
                fleetManager.showFleetRoute(fleetManager.destinationStarNode, Colorf.GREEN);
        }
    }

    /**
	 * The star where this fleet is orbiting was selected. Clear the destination, distance and num turns fields.
	 */
    public void reset() {
        fleetManager.destinationStarNode = null;
        destinationLabel.setText("");
        distanceLabel.setText("");
        turnsLabel.setText("");
        disableResetButton();
        disableSendButton();
        fleetManager.hideFleetRoute();
        gameManager.hud.update();
    }

    /**
	 * Updates the widgets based on the selected destination star. Comes here when the player picks a destination for
	 * the fleet.
	 */
    public void update() {
        logger.info("Inside FleetInfoPanel.update()");
        if (fleetManager.currentFleetNode.status != FleetStatus.ORBITING) return;
        if (fleetManager.destinationStarNode == fleetManager.currentFleetNode.orbiting) {
            reset();
            return;
        }
        destinationLabel.setText("Destination: " + ((fleetManager.destinationStarNode.getStar().isExplored()) ? fleetManager.destinationStarNode.getName() : "Unknown"));
        Point3f sourcePos = fleetManager.currentFleetNode.orbiting.getPosition();
        Point3f targetPos = fleetManager.destinationStarNode.getPosition();
        int distance = GameManager.distance(sourcePos.x(), sourcePos.z(), targetPos.x(), targetPos.z());
        fleetManager.tempFleet.setDistanceToDestination(distance);
        distanceLabel.setText(String.valueOf(distance) + " parsecs");
        updateETA(distance);
        disableResetButton();
    }

    /**
	 * Enables/disables the labels on this row.
	 *
	 * @param row
	 * @param enabled
	 */
    private void setLabelsVisible(int row, boolean enabled) {
        shipNameLabel[row].setVisible(enabled);
        shipCountLabel[row].setVisible(enabled);
    }

    /**
	 * Show the ship design and the allocation buttons for the given ship design index.
	 *
	 * @param index
	 */
    private void showShipAllocation(int index) {
        allocButton[index][0].setVisible(true);
        allocButton[index][1].setVisible(true);
        allocButton[index][2].setVisible(true);
        allocButton[index][3].setVisible(true);
    }

    /**
	 * Hide the ship design and the allocation buttons for the given ship design index.
	 *
	 * @param index
	 */
    private void hideShipAllocation(int index) {
        allocButton[index][0].setVisible(false);
        allocButton[index][1].setVisible(false);
        allocButton[index][2].setVisible(false);
        allocButton[index][3].setVisible(false);
    }

    /**
	 * Comment for method.
	 *
	 */
    private void enableSendButton() {
        sendButton.setEnabled(true);
        sendButton.setTextureNormal(textureButtonSend1);
        sendButton.setTextureHovered(textureButtonSend2);
    }

    /**
	 * Comment for method.
	 *
	 */
    private void disableSendButton() {
        sendButton.setEnabled(false);
        sendButton.setTextureNormal(textureButtonSend3);
        sendButton.setTextureHovered(textureButtonSend3);
    }

    /**
	 * Comment for method.
	 *
	 */
    private void enableResetButton() {
        resetButton.setEnabled(true);
        resetButton.setTextureNormal(textureButtonReset1);
        resetButton.setTextureHovered(textureButtonReset2);
    }

    /**
	 * Comment for method.
	 *
	 */
    private void disableResetButton() {
        resetButton.setEnabled(false);
        resetButton.setTextureNormal(textureButtonReset3);
        resetButton.setTextureHovered(textureButtonReset3);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void onButtonClicked(AbstractButton button, Object userObject) {
        int buttonNo = ((Integer) button.getUserObject()).intValue();
        int index;
        switch(buttonNo) {
            case 00:
                index = shipTypeIndex[0];
                if (fleetManager.tempFleet.getShipCount(index) > 0) {
                    boolean changed = fleetManager.tempFleet.removeShip(index, fleetManager.tempFleet.getShipCount(index));
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[0].setText("0");
                }
                break;
            case 01:
                index = shipTypeIndex[0];
                if (fleetManager.tempFleet.getShipCount(index) > 0) {
                    boolean changed = fleetManager.tempFleet.removeShip(index);
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[0].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 02:
                index = shipTypeIndex[0];
                if (fleetManager.tempFleet.getShipCount(index) < maxShipCount[index]) {
                    boolean changed = fleetManager.tempFleet.addShip(index);
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[0].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 03:
                index = shipTypeIndex[0];
                if (fleetManager.tempFleet.getShipCount(index) < maxShipCount[index]) {
                    boolean changed = fleetManager.tempFleet.addShip(index, maxShipCount[index] - fleetManager.tempFleet.getShipCount(index));
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[0].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 10:
                index = shipTypeIndex[1];
                if (fleetManager.tempFleet.getShipCount(index) > 0) {
                    boolean changed = fleetManager.tempFleet.removeShip(index, fleetManager.tempFleet.getShipCount(index));
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[1].setText("0");
                }
                break;
            case 11:
                index = shipTypeIndex[1];
                if (fleetManager.tempFleet.getShipCount(index) > 0) {
                    boolean changed = fleetManager.tempFleet.removeShip(index);
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[1].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 12:
                index = shipTypeIndex[1];
                if (fleetManager.tempFleet.getShipCount(index) < maxShipCount[index]) {
                    boolean changed = fleetManager.tempFleet.addShip(index);
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[1].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 13:
                index = shipTypeIndex[1];
                if (fleetManager.tempFleet.getShipCount(index) < maxShipCount[index]) {
                    boolean changed = fleetManager.tempFleet.addShip(index, maxShipCount[index] - fleetManager.tempFleet.getShipCount(index));
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[1].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 20:
                index = shipTypeIndex[2];
                if (fleetManager.tempFleet.getShipCount(index) > 0) {
                    boolean changed = fleetManager.tempFleet.removeShip(index, fleetManager.tempFleet.getShipCount(index));
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[2].setText("0");
                }
                break;
            case 21:
                index = shipTypeIndex[2];
                if (fleetManager.tempFleet.getShipCount(index) > 0) {
                    boolean changed = fleetManager.tempFleet.removeShip(index);
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[2].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 22:
                index = shipTypeIndex[2];
                if (fleetManager.tempFleet.getShipCount(index) < maxShipCount[index]) {
                    boolean changed = fleetManager.tempFleet.addShip(index);
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[2].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 23:
                index = shipTypeIndex[2];
                if (fleetManager.tempFleet.getShipCount(index) < maxShipCount[index]) {
                    boolean changed = fleetManager.tempFleet.addShip(index, maxShipCount[index] - fleetManager.tempFleet.getShipCount(index));
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[2].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 30:
                index = shipTypeIndex[3];
                if (fleetManager.tempFleet.getShipCount(index) > 0) {
                    boolean changed = fleetManager.tempFleet.removeShip(index, fleetManager.tempFleet.getShipCount(index));
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[3].setText("0");
                }
                break;
            case 31:
                index = shipTypeIndex[3];
                if (fleetManager.tempFleet.getShipCount(index) > 0) {
                    boolean changed = fleetManager.tempFleet.removeShip(index);
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[3].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 32:
                index = shipTypeIndex[3];
                if (fleetManager.tempFleet.getShipCount(index) < maxShipCount[index]) {
                    boolean changed = fleetManager.tempFleet.addShip(index);
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[3].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 33:
                index = shipTypeIndex[3];
                if (fleetManager.tempFleet.getShipCount(index) < maxShipCount[index]) {
                    boolean changed = fleetManager.tempFleet.addShip(index, maxShipCount[index] - fleetManager.tempFleet.getShipCount(index));
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[3].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 40:
                index = shipTypeIndex[4];
                if (fleetManager.tempFleet.getShipCount(index) > 0) {
                    boolean changed = fleetManager.tempFleet.removeShip(index, fleetManager.tempFleet.getShipCount(index));
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[4].setText("0");
                }
                break;
            case 41:
                index = shipTypeIndex[4];
                if (fleetManager.tempFleet.getShipCount(index) > 0) {
                    boolean changed = fleetManager.tempFleet.removeShip(index);
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[4].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 42:
                index = shipTypeIndex[4];
                if (fleetManager.tempFleet.getShipCount(index) < maxShipCount[index]) {
                    boolean changed = fleetManager.tempFleet.addShip(index);
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[4].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 43:
                index = shipTypeIndex[4];
                if (fleetManager.tempFleet.getShipCount(index) < maxShipCount[index]) {
                    boolean changed = fleetManager.tempFleet.addShip(index, maxShipCount[index] - fleetManager.tempFleet.getShipCount(index));
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[4].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 50:
                index = shipTypeIndex[5];
                if (fleetManager.tempFleet.getShipCount(index) > 0) {
                    boolean changed = fleetManager.tempFleet.removeShip(index, fleetManager.tempFleet.getShipCount(index));
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[5].setText("0");
                }
                break;
            case 51:
                index = shipTypeIndex[5];
                if (fleetManager.tempFleet.getShipCount(index) > 0) {
                    boolean changed = fleetManager.tempFleet.removeShip(index);
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[5].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 52:
                index = shipTypeIndex[5];
                if (fleetManager.tempFleet.getShipCount(index) < maxShipCount[index]) {
                    boolean changed = fleetManager.tempFleet.addShip(index);
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[5].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 53:
                index = shipTypeIndex[5];
                if (fleetManager.tempFleet.getShipCount(index) < maxShipCount[index]) {
                    boolean changed = fleetManager.tempFleet.addShip(index, maxShipCount[index] - fleetManager.tempFleet.getShipCount(index));
                    if (changed) {
                        updateSpeedAndRange();
                    }
                    shipCountLabel[5].setText(String.valueOf(fleetManager.tempFleet.getShipCount(index)));
                }
                break;
            case 60:
                fleetManager.closeFleetInfo();
                break;
            case 70:
                fleetManager.resetFleetRouting();
                break;
            default:
                fleetManager.acceptFleetRouting();
        }
    }

    /**
	 * Update the temporary fleet's max range and speed.
	 */
    private void updateSpeedAndRange() {
        logger.info("tempFleet=" + fleetManager.tempFleet);
        if (fleetManager.tempFleet.getMaxRange() == Integer.MAX_VALUE) {
            maxRangeLabel.setText("");
            warpSpeedLabel.setText("");
            fleetManager.tempFleet.setTurnsToArrive(0);
            turnsLabel.setText("");
            disableSendButton();
        } else {
            maxRangeLabel.setText("Max Range: " + String.valueOf(fleetManager.tempFleet.getMaxRange()));
            warpSpeedLabel.setText("Max Speed: " + String.valueOf(fleetManager.tempFleet.getWarpSpeed()));
            if (fleetManager.destinationStarNode != null) {
                updateETA(fleetManager.tempFleet.getDistanceToDestination());
            }
        }
    }

    /**
	 * Calculate how many turns to arrive at the destination star and update the ETA and route accordingly.
	 *
	 * @param distance the distance (in parsec) to the destination star
	 */
    private void updateETA(int distance) {
        boolean inRange = fleetManager.destinationStarNode.getStar().getRange() <= fleetManager.tempFleet.getMaxRange();
        if (inRange) {
            int turns = (int) FastMath.ceil(((float) distance) / ((float) fleetManager.tempFleet.getWarpSpeed()));
            fleetManager.tempFleet.setTurnsToArrive(turns);
            turnsLabel.setText("ETA: " + String.valueOf(turns) + " turns");
            fleetManager.showFleetRoute(fleetManager.destinationStarNode, Colorf.GREEN);
            enableSendButton();
        } else {
            fleetManager.tempFleet.setTurnsToArrive(0);
            turnsLabel.setText("Out of range");
            fleetManager.showFleetRoute(fleetManager.destinationStarNode, Colorf.RED);
            disableSendButton();
        }
    }
}
