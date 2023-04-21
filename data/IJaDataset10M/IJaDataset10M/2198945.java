package org.jogre.dominoes.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import org.jogre.dominoes.common.DominoesModel;
import org.jogre.client.awt.JogreComponent;
import org.jogre.common.util.GameProperties;
import org.jogre.common.util.JogreUtils;

/**
 * A component for the dominoes game that will display the number
 * of dominoes remaining in a player's hand.
 *
 * @author  Richard Walter
 * @version Beta 0.3
 */
@SuppressWarnings("serial")
public class DominoesHandSizeComponent extends JogreComponent {

    protected DominoesModel model;

    protected DominoesGraphics dg;

    private Color boneyardColor;

    private int boneyardAreaWidth, boneyardAreaHeight;

    private Font gameFont;

    private int textYOffset;

    private Color outlineColor;

    private static final String DEFAULT_HAND_SIZE_FONT = "Dialog";

    private static final int DEFAULT_FONT_SIZE = 32;

    private static final String DEFAULT_BONEYARD_COLOR = "148,80,0";

    private static final String DEFAULT_OUTLINE_COLOR = "255,255,255";

    /**
	 * Constructor for the view that displays the number of dominoes in a
	 * player's hand
	 *
	 * @param model    The model whose info is to be displayed.
	 */
    public DominoesHandSizeComponent(DominoesModel model) {
        super();
        this.model = model;
        this.dg = DominoesGraphics.getInstance();
        boneyardAreaWidth = dg.imageWidths[DominoesGraphics.BONE_PILE_SIZE_AREA];
        boneyardAreaHeight = dg.imageHeights[DominoesGraphics.BONE_PILE_SIZE_AREA];
        int componentHeight = (model.getNumOfPlayers() == 4) ? 3 * boneyardAreaHeight : 2 * boneyardAreaHeight;
        Dimension dim = new Dimension(boneyardAreaWidth, componentHeight);
        setPreferredSize(dim);
        GameProperties props = GameProperties.getInstance();
        int fontSize = props.getInt("handSize.font.size", DEFAULT_FONT_SIZE);
        String font = props.get("handSize.font", DEFAULT_HAND_SIZE_FONT);
        gameFont = new Font(font, Font.BOLD, fontSize);
        boneyardColor = JogreUtils.getColour(props.get("boneyard.color", DEFAULT_BONEYARD_COLOR));
        outlineColor = JogreUtils.getColour(props.get("handSize.outline.color", DEFAULT_OUTLINE_COLOR));
        FontMetrics gameFontMetrics = getFontMetrics(gameFont);
        int textHeight = gameFontMetrics.getMaxAscent() + gameFontMetrics.getMaxDescent();
        textYOffset = ((boneyardAreaHeight - textHeight) / 2) + gameFontMetrics.getMaxAscent();
    }

    /**
	 * Update the screen to reflect the current state of the model.
	 */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(gameFont);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawArea(g, 0, 0, boneyardColor, DominoesGraphics.BONE_PILE_SIZE_AREA, model.getBoneyardSize(), 0);
        int numPlayers = model.getNumOfPlayers();
        if (numPlayers == 3) {
            int imageId = DominoesGraphics.PLAYER_HAND_SIZE_AREA_SMALL;
            drawPlayerArea(g, 0, 0, imageId, 0);
            drawPlayerArea(g, 1, 0, imageId, 1);
            drawPlayerArea(g, 2, 0, imageId, 2);
        } else {
            int imageId = DominoesGraphics.PLAYER_HAND_SIZE_AREA_LARGE;
            drawPlayerArea(g, 0, 0, imageId, 0);
            drawPlayerArea(g, 1, 0, imageId, 1);
            if (numPlayers == 4) {
                drawPlayerArea(g, 0, 1, imageId, 2);
                drawPlayerArea(g, 1, 1, imageId, 3);
            }
        }
    }

    private void drawPlayerArea(Graphics g, int xIndex, int yIndex, int overlayImageId, int seatNum) {
        int x = xIndex * dg.imageWidths[overlayImageId];
        int y = boneyardAreaHeight + yIndex * dg.imageHeights[overlayImageId];
        drawArea(g, x, y, dg.playerColors[seatNum], overlayImageId, model.getPlayerHandSize(seatNum), model.getLastAddSize(seatNum));
    }

    private void drawArea(Graphics g, int x, int y, Color color, int overlayImageId, int number, int numAdded) {
        int w = dg.imageWidths[overlayImageId];
        int h = dg.imageHeights[overlayImageId];
        g.setColor(color);
        g.fillRoundRect(x, y, w, h, 14, 14);
        dg.paintImage(g, x, y, overlayImageId, 0, 0);
        if (numAdded > 0) {
            drawAddedNum(g, x, y, w, h, numAdded);
        }
        g.setColor((outlineColor == null) ? color : outlineColor);
        String handSizeText = Integer.toString(number);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                dg.drawCenterJustifiedText(g, handSizeText, x + w / 2 + i, y + textYOffset + j);
            }
        }
        g.setColor(Color.BLACK);
        dg.drawCenterJustifiedText(g, handSizeText, x + w / 2, y + textYOffset);
    }

    private void drawAddedNum(Graphics g, int x, int y, int w, int h, int numAdded) {
        int rowSpacing = dg.imageHeights[DominoesGraphics.MICRO_DOMINO] + 1;
        int columnSpacing = dg.imageWidths[DominoesGraphics.MICRO_DOMINO] + 2;
        int lastRowStartY = y + h - 2 - rowSpacing;
        int drawY = lastRowStartY;
        x += 3;
        while (numAdded > 0) {
            if (drawY <= y) {
                x += columnSpacing;
                drawY = lastRowStartY;
            }
            dg.drawMicroDomino(g, x, drawY);
            drawY -= rowSpacing;
            numAdded -= 1;
        }
    }
}
