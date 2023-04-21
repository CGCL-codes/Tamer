package pl.edu.amu.wmi.kino.visualjavafx.javafxcodegenerators.animation.time;

import pl.edu.amu.wmi.kino.visualjavafx.model.animation.animationTime.HardKeyFrame;

/**
 *
 * @author Admin
 */
public class HardKeyFrameGenerator extends KeyFrameGenerator {

    @Override
    public boolean canGenerate(Object c) {
        if (c instanceof HardKeyFrame) return true; else return false;
    }

    @Override
    public void generateUniqueAttributes(StringBuilder sb, Object o, int recursionLevel) {
        sb.append("//HARD_KEY_FRAME\n");
        super.generateUniqueAttributes(sb, o, recursionLevel);
    }
}
