package net.rptools.maptool.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import junit.framework.TestCase;
import net.rptools.lib.image.ImageUtil;
import net.rptools.maptool.model.Token;

public class TokenUtilTest extends TestCase {

    public void testGuessTokenType() throws Exception {
        BufferedImage img = new BufferedImage(100, 100, Transparency.BITMASK);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.blue);
        g.fillRect(0, 0, 100, 100);
        g.dispose();
        assertEquals(Token.TokenShape.SQUARE, TokenUtil.guessTokenType(img));
        img = ImageUtil.getCompatibleImage("net/rptools/maptool/client/image/squareToken.gif");
        assertEquals(Token.TokenShape.SQUARE, TokenUtil.guessTokenType(img));
        img = new BufferedImage(100, 100, Transparency.BITMASK);
        g = img.createGraphics();
        g.setColor(Color.red);
        g.fillOval(0, 0, 100, 100);
        g.dispose();
        assertEquals(Token.TokenShape.CIRCLE, TokenUtil.guessTokenType(img));
        img = ImageUtil.getCompatibleImage("net/rptools/maptool/client/image/circleToken.png");
        assertEquals(Token.TokenShape.CIRCLE, TokenUtil.guessTokenType(img));
        img = new BufferedImage(100, 100, Transparency.BITMASK);
        g = img.createGraphics();
        g.setColor(Color.red);
        g.fillOval(0, 0, 10, 10);
        g.fillOval(90, 90, 10, 10);
        g.fillRect(0, 50, 100, 10);
        g.dispose();
        assertEquals(Token.TokenShape.TOP_DOWN, TokenUtil.guessTokenType(img));
    }
}
