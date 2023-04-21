package com.cvicse.ks.flyweight;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Description:The LineFactory is used to create lines.In this demo,
 * we need draw a large number of lines.However,all the lines just have
 * several kinds of colors,the unique difference among lines is it's 
 * begin and end posion!So,we use flyweight pattern to create only one
 * line object for each color.the line's being and end coordinate is
 * get by outter arguments.By the way,the LineFactory is singleton!
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: CVIC SE
 * </p>
 * 
 * @author geng_lchao
 * @checker geng_lchao
 * @version 1.0
 * @created at 2007-8-5
 * @modified by geng_lchao at at 2007-8-5
 */
public class LineFactory {

    private static LineFactory lineFactory = new LineFactory();

    private Map<Color, LineImpl> lines = new HashMap<Color, LineImpl>();

    /**
	 * The intent of making the LineImpl as the LineFactory's private
	 * inner class is preventing clients initialize the Line directly.
	 */
    private class LineImpl implements ILine {

        private Color color;

        private LineImpl(Color color) {
            super();
            this.color = color;
        }

        /**
		 * Generally speaking,the x1,y1,x2,y2 should have been the
		 * LineImpl's field.However,they are all outter state!
		 * According to flyweight parrtern,the outter state should
		 * be extracted.So,the x1,y1,x2,y2 are asigned by outter 
		 * arguments.On the other hand,the color state is inner state
		 * it doesn't change with the outter circumstance.
		 */
        public void draw(Graphics graphics, int x1, int y1, int x2, int y2) {
            graphics.setColor(color);
            graphics.drawLine(x1, y1, x2, y2);
        }

        public Color getColor() {
            return this.color;
        }
    }

    private LineFactory() {
    }

    public static LineFactory getInstance() {
        return lineFactory;
    }

    public ILine createLine(Color color) {
        if (!lines.containsKey(color)) {
            lines.put(color, new LineImpl(color));
        }
        return (ILine) lines.get(color);
    }
}
