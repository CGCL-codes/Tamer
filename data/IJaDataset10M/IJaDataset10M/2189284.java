package com.cell.gfx.game;

import java.io.Serializable;
import com.cell.CMath;
import com.cell.CObject;

/**
 * @author yifeizhang
 * @since 2006-11-30 
 * @version 1.0
 */
public class CGroup extends CObject implements Serializable {

    private static final long serialVersionUID = 1L;

    protected short Frames[][];

    protected int SubIndex;

    protected int SubCount;

    protected short w_left = 0;

    protected short w_top = 0;

    protected short w_bottom = 1;

    protected short w_right = 1;

    protected short w_width = 0;

    protected short w_height = 0;

    /**
	 * @param left
	 * @param top
	 * @param right
	 * @param botton 
	 */
    protected void fixArea(int left, int top, int right, int botton) {
        if (left < w_left) w_left = (short) left;
        if (top < w_top) w_top = (short) top;
        if (right > w_right) w_right = (short) right;
        if (botton > w_bottom) w_bottom = (short) botton;
        w_width = (short) (w_right - w_left);
        w_height = (short) (w_bottom - w_top);
    }

    /**
	 * fast detect 2 collides's area specify frame, 
	 * area is every part within a frame's max scope. 
	 * </br>
	 * @param c1
	 * @param index1
	 * @param x1
	 * @param y1
	 * @param c2
	 * @param index2
	 * @param x2
	 * @param y2
	 * @return 
	 */
    public static boolean touchArea(CGroup c1, int x1, int y1, CGroup c2, int x2, int y2) {
        if (CMath.intersectRect(x1 + c1.w_left, y1 + c1.w_top, x1 + c1.w_right, y1 + c1.w_bottom, x2 + c2.w_left, y2 + c2.w_top, x2 + c2.w_right, y2 + c2.w_bottom)) {
            return true;
        }
        return false;
    }

    /**
	 * set frame sequence, frames[frame id][part id] = groupted object. </br> 
	 * e.g. : animates's image id ; collides's CCD object ;</br>
	 * @param frames frames[frame id][part id]
	 */
    public void setFrames(short[][] frames) {
        Frames = frames;
    }

    public short[][] getFrames() {
        return Frames;
    }

    /**
	 * set part sequence specify frame index</br>
	 * @param frame frames[frame id][part id]
	 * @param index frame id
	 */
    public void setComboFrame(short[] frame, int index) {
        Frames[index] = frame;
    }

    /**
	 * get frames count</br>
	 * @return count
	 */
    public int getCount() {
        return Frames.length;
    }

    /**
	 * get frames count</br>
	 * @return count
	 */
    public int getComboFrameCount(int index) {
        return Frames[index].length;
    }
}
