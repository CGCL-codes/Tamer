package tilemaster.editor.paintingtools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import tilemaster.editor.TilesetEditor;
import java.util.HashMap;

/**
 * Simple Simutrans light pixel remover.
 *
 * @author Hj. Malthaner
 */
public class SimutransNoLights extends PaintingToolBase {

    private TilesetEditor editor;

    private BufferedImage canvas;

    private HashMap<Integer, Integer> map;

    /**
     * @return The redraw mode to use for this tool.
     */
    @Override
    public int getRefreshMode() {
        return REPAINT_NOTHING;
    }

    /**
     * @return The name to display for this tool.
     */
    public String getToolName() {
        return "Simutrans Remove Light Pixels";
    }

    /**
     * Set the editor which is calling this tool.
     * @param editor The tileset editor which is calling this tool.
     */
    @Override
    public void setEditor(TilesetEditor editor) {
        this.editor = editor;
    }

    @Override
    public void setCanvas(BufferedImage canvas) {
        this.canvas = canvas;
        unlight();
    }

    public void unlight() {
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = canvas.getRGB(x, y) & 0xFFFFFF;
                Integer m = map.get(argb);
                if (m != null) {
                    canvas.setRGB(x, y, m.intValue() | 0xFF000000);
                }
            }
        }
        editor.updateTileData();
        editor.repaint(100);
    }

    /**
     * User clicked a new location
     */
    @Override
    public void firstClick(Graphics gr, int x, int y) {
        unlight();
    }

    public SimutransNoLights() {
        map = new HashMap<Integer, Integer>();
        map.put((255 << 16) + (255 << 8) + 83, (254 << 16) + (254 << 8) + 83);
        map.put((255 << 16) + (33 << 8) + 29, (254 << 16) + (33 << 8) + 29);
        map.put((1 << 16) + (221 << 8) + 1, (1 << 16) + (220 << 8) + 1);
        map.put((77 << 16) + (77 << 8) + 77, (76 << 16) + (76 << 8) + 76);
        map.put((87 << 16) + (101 << 8) + 111, (87 << 16) + (101 << 8) + 110);
        map.put((193 << 16) + (177 << 8) + 209, (193 << 16) + (177 << 8) + 208);
        map.put((227 << 16) + (227 << 8) + 255, (227 << 16) + (227 << 8) + 254);
        map.put((255 << 16) + (1 << 8) + 127, (254 << 16) + (1 << 8) + 127);
        map.put((1 << 16) + (1 << 8) + 255, (1 << 16) + (1 << 8) + 254);
        map.put((107 << 16) + (107 << 8) + 107, (106 << 16) + (106 << 8) + 106);
        map.put((155 << 16) + (155 << 8) + 155, (154 << 16) + (154 << 8) + 154);
        map.put((179 << 16) + (179 << 8) + 179, (178 << 16) + (178 << 8) + 178);
        map.put((201 << 16) + (201 << 8) + 201, (200 << 16) + (200 << 8) + 200);
        map.put((223 << 16) + (223 << 8) + 223, (222 << 16) + (222 << 8) + 222);
    }
}
