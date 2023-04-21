package jmri.jmrix.bachrus;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jmri.jmrit.catalog.*;

/**
 * Creates a JPanel containing an Dial type speedo display.
 *
 * <p> Based on analogue clock frame by Dennis Miller
 *
 * @author                     Andrew Crosland Copyright (C) 2010
 * @version                    $Revision: 1.12 $
 */
public class SpeedoDial extends JPanel {

    float speedAngle = 0.0F;

    int speedDigits = 0;

    Image logo;

    Image scaledLogo;

    NamedIcon jmriIcon;

    NamedIcon scaledIcon;

    int minuteX[] = { -12, -11, -24, -11, -11, 0, 11, 11, 24, 11, 12 };

    int minuteY[] = { -31, -261, -266, -314, -381, -391, -381, -314, -266, -261, -31 };

    int scaledMinuteX[] = new int[minuteX.length];

    int scaledMinuteY[] = new int[minuteY.length];

    int rotatedMinuteX[] = new int[minuteX.length];

    int rotatedMinuteY[] = new int[minuteY.length];

    Polygon minuteHand;

    Polygon scaledMinuteHand;

    int minuteHeight;

    float scaleRatio;

    int faceSize;

    int size;

    int logoWidth;

    int logoHeight;

    int centreX;

    int centreY;

    int units = Speed.MPH;

    int mphLimit = 80;

    int mphInc = 40;

    int kphLimit = 140;

    int kphInc = 60;

    float priMajorTick;

    float priMinorTick;

    float secTick;

    String priString = "MPH";

    String secString = "KPH";

    public SpeedoDial() {
        super();
        jmriIcon = new NamedIcon("resources/logo.gif", "resources/logo.gif");
        scaledIcon = new NamedIcon("resources/logo.gif", "resources/logo.gif");
        logo = jmriIcon.getImage();
        minuteHand = new Polygon(minuteX, minuteY, 11);
        minuteHeight = minuteHand.getBounds().getSize().height;
        this.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                scaleFace();
            }
        });
        setPreferredSize(new java.awt.Dimension(300, 300));
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(centreX, centreY);
        g2.setColor(Color.white);
        g2.fillOval(-faceSize / 2, -faceSize / 2, faceSize, faceSize);
        g2.setColor(Color.black);
        g2.drawOval(-faceSize / 2, -faceSize / 2, faceSize, faceSize);
        int dotSize = faceSize / 40;
        g2.fillOval(-dotSize * 2, -dotSize * 2, 4 * dotSize, 4 * dotSize);
        g2.drawImage(scaledLogo, -logoWidth / 2, -faceSize / 4, logoWidth, logoHeight, this);
        int fontSize = faceSize / 10;
        if (fontSize < 1) fontSize = 1;
        Font sizedFont = new Font("Serif", Font.PLAIN, fontSize);
        g2.setFont(sizedFont);
        FontMetrics fontM = g2.getFontMetrics(sizedFont);
        int dashSize = size / 60;
        if (units == Speed.MPH) {
            priMajorTick = 240 / ((float) mphLimit / 10);
            priMinorTick = priMajorTick / 5;
            secTick = 240 / (Speed.mphToKph(mphLimit) / 10);
        } else {
            priMajorTick = 240 / ((float) kphLimit / 10);
            priMinorTick = priMajorTick / 5;
            secTick = 240 / (Speed.kphToMph(kphLimit) / 10);
        }
        for (float i = 150; i < 391; i = i + priMinorTick) {
            g2.drawLine(dotX((float) faceSize / 2, i), dotY((float) faceSize / 2, i), dotX((float) faceSize / 2 - dashSize, i), dotY((float) faceSize / 2 - dashSize, i));
        }
        int j = 0;
        for (float i = 150; i < 391; i = i + priMajorTick) {
            g2.drawLine(dotX((float) faceSize / 2, i), dotY((float) faceSize / 2, i), dotX((float) faceSize / 2 - 3 * dashSize, i), dotY((float) faceSize / 2 - 3 * dashSize, i));
            String speed = Integer.toString(10 * j);
            int xOffset = fontM.stringWidth(speed);
            int yOffset = fontM.getHeight();
            g2.drawString(speed, dotX((float) faceSize / 2 - 6 * dashSize, j * priMajorTick - 210) - xOffset / 2, dotY((float) faceSize / 2 - 6 * dashSize, j * priMajorTick - 210) + yOffset / 4);
            j++;
        }
        fontSize = faceSize / 15;
        if (fontSize < 1) fontSize = 1;
        sizedFont = new Font("Serif", Font.PLAIN, fontSize);
        g2.setFont(sizedFont);
        fontM = g2.getFontMetrics(sizedFont);
        g2.setColor(Color.green);
        j = 0;
        for (float i = 150; i < 391; i = i + secTick) {
            g2.fillOval(dotX((float) faceSize / 2 - 10 * dashSize, i), dotY((float) faceSize / 2 - 10 * dashSize, i), 5, 5);
            if (((j & 1) == 0) || (units == Speed.KPH)) {
                String speed = Integer.toString(10 * j);
                int xOffset = fontM.stringWidth(speed);
                int yOffset = fontM.getHeight();
                g2.drawString(speed, dotX((float) faceSize / 2 - 13 * dashSize, j * secTick - 210) - xOffset / 2, dotY((float) faceSize / 2 - 13 * dashSize, j * secTick - 210) + yOffset / 4);
            }
            j++;
        }
        g2.drawString(secString, dotX((float) faceSize / 2 - 5 * dashSize, 45) - fontM.stringWidth(secString) / 2, dotY((float) faceSize / 2 - 5 * dashSize, 45) + fontM.getHeight() / 4);
        g2.setColor(Color.black);
        for (int i = 0; i < scaledMinuteX.length; i++) {
            rotatedMinuteX[i] = (int) (scaledMinuteX[i] * Math.cos(toRadians(speedAngle)) - scaledMinuteY[i] * Math.sin(toRadians(speedAngle)));
            rotatedMinuteY[i] = (int) (scaledMinuteX[i] * Math.sin(toRadians(speedAngle)) + scaledMinuteY[i] * Math.cos(toRadians(speedAngle)));
        }
        scaledMinuteHand = new Polygon(rotatedMinuteX, rotatedMinuteY, rotatedMinuteX.length);
        g2.fillPolygon(scaledMinuteHand);
        int unitsFontSize = (int) ((float) faceSize / 10 * .75);
        if (unitsFontSize < 1) unitsFontSize = 1;
        Font unitsSizedFont = new Font("Serif", Font.PLAIN, unitsFontSize);
        g2.setFont(unitsSizedFont);
        FontMetrics unitsFontM = g2.getFontMetrics(unitsSizedFont);
        g2.drawString(priString, dotX((float) faceSize / 2 - 5 * dashSize, -225) - unitsFontM.stringWidth(priString) / 2, dotY((float) faceSize / 2 - 5 * dashSize, -225) + unitsFontM.getHeight() / 4);
        String speedString = Integer.toString(speedDigits);
        int digitsFontSize = (int) (fontSize * 1.5);
        Font digitsSizedFont = new Font("Serif", Font.PLAIN, digitsFontSize);
        g2.setFont(digitsSizedFont);
        FontMetrics digitsFontM = g2.getFontMetrics(digitsSizedFont);
        int pad = (int) (digitsFontSize * 0.2);
        int h = (int) (digitsFontM.getAscent() * 0.8);
        int w = digitsFontM.stringWidth("999");
        if (pad < 2) {
            pad = 2;
        }
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(-w / 2 - pad, 2 * faceSize / 5 - h - pad, w + pad * 2, h + pad * 2);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRect(-w / 2 - pad, 2 * faceSize / 5 - h - pad, w + pad * 2, h + pad * 2);
        g2.setColor(Color.BLACK);
        g2.drawString(speedString, -digitsFontM.stringWidth(speedString) / 2, 2 * faceSize / 5);
    }

    float toRadians(float degrees) {
        return degrees / 180.0F * (float) Math.PI;
    }

    int dotX(float radius, float angle) {
        int xDist;
        xDist = (int) Math.round(radius * Math.cos(toRadians(angle)));
        return xDist;
    }

    int dotY(float radius, float angle) {
        int yDist;
        yDist = (int) Math.round(radius * Math.sin(toRadians(angle)));
        return yDist;
    }

    public void scaleFace() {
        int panelHeight = this.getSize().height;
        int panelWidth = this.getSize().width;
        size = Math.min(panelHeight, panelWidth);
        faceSize = (int) (size * .97);
        if (faceSize == 0) {
            faceSize = 1;
        }
        int logoScaleWidth = faceSize / 6;
        int logoScaleHeight = (int) ((float) logoScaleWidth * (float) jmriIcon.getIconHeight() / jmriIcon.getIconWidth());
        scaledLogo = logo.getScaledInstance(logoScaleWidth, logoScaleHeight, Image.SCALE_SMOOTH);
        scaledIcon.setImage(scaledLogo);
        logoWidth = scaledIcon.getIconWidth();
        logoHeight = scaledIcon.getIconHeight();
        scaleRatio = faceSize / 2.7F / minuteHeight;
        for (int i = 0; i < minuteX.length; i++) {
            scaledMinuteX[i] = (int) (minuteX[i] * scaleRatio);
            scaledMinuteY[i] = (int) (minuteY[i] * scaleRatio);
        }
        scaledMinuteHand = new Polygon(scaledMinuteX, scaledMinuteY, scaledMinuteX.length);
        centreX = panelWidth / 2;
        centreY = panelHeight / 2;
        return;
    }

    void update(float speed) {
        if (units == Speed.MPH) {
            speedDigits = Math.round(Speed.kphToMph(speed));
            speedAngle = -120 + Speed.kphToMph(speed * priMajorTick / 10);
        } else {
            speedDigits = Math.round(speed);
            speedAngle = -120 + speed * priMajorTick / 10;
        }
        repaint();
    }

    void setUnitsMph() {
        units = Speed.MPH;
        priString = "MPH";
        secString = "KPH";
    }

    void setUnitsKph() {
        units = Speed.KPH;
        priString = "KPH";
        secString = "MPH";
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SpeedoDial.class.getName());
}
