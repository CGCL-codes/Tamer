package com.peterhi.player.editor;

import com.peterhi.player.shapes.Shape;
import com.peterhi.player.shapes.Text;
import java.awt.Graphics2D;

/**
 *
 * @author YUN TAO
 */
public class TextEditor extends AbstractBoundsEditor {

    public TextEditor(Editor.Callback callback) {
        super(callback);
    }

    public void paint(Graphics2D g) {
        g.drawRect(Math.min(downx, movex), Math.min(downy, movey), Math.abs(downx - movex), Math.abs(downy - movey));
        if (downx != movex || downy != movey) {
            g.drawString("T", Math.min(downx, movex), Math.min(downy, movey));
        }
    }

    public Shape create() {
        Text text = new Text(true);
        text.x = Math.min(downx, movex);
        text.y = Math.min(downy, movey);
        text.width = Math.abs(downx - movex);
        text.height = Math.abs(downy - movey);
        return text;
    }
}
