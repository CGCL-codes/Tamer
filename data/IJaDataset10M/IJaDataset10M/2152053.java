package cn.edu.nju.software.grapheditor.cmd;

import java.awt.Color;
import java.awt.Point;
import cn.edu.nju.software.grapheditor.Drawing;

public class ColorCmd extends Command {

    public ColorCmd(Color color) {
        Drawing.setCurrentColor(color);
    }

    @Override
    public void executeClick(Point p, Drawing dwg) {
        if (dwg.getFrontmostContainer(p) != null) {
            dwg.getFrontmostContainer(p).setColor(Drawing.getCurrentColor());
        }
        super.executeClick(p, dwg);
    }
}
