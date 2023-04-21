package org.qsari.effectopedia.defaults;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

public class DefaultGOSettings {

    private static final DefaultGOSettings INSTANCE = new DefaultGOSettings();

    private DefaultGOSettings() {
    }

    public DefaultGOSettings getGOOptions() {
        return INSTANCE;
    }

    public static Color effectColor = new java.awt.Color(103, 147, 218);

    public static Color linkColor = new java.awt.Color(251, 176, 59);

    public static Color testColor = new java.awt.Color(126, 119, 216);

    public static Color chemicalColor = new java.awt.Color(103, 218, 147);

    public static Color arcColor = new java.awt.Color(200, 200, 200);

    public static Color pathwayColor = new java.awt.Color(127, 127, 127);

    public static Color segmentBoundsColor = new java.awt.Color(235, 235, 235);

    public static Color headerColor = new java.awt.Color(245, 245, 245);

    public static Color headerIconColor = new java.awt.Color(99, 130, 191);

    public static Color activeColor = Color.RED;

    public static Color selectionFrameColor = new java.awt.Color(127, 127, 127);

    public static Color uiSelectedColor = new java.awt.Color(200, 221, 242);

    public static Color selectionFillColor = new java.awt.Color(120, 120, 120);

    public static Color newArcColor = new java.awt.Color(0, 127, 127);

    public static Color footerCaptionColor = new java.awt.Color(99, 99, 99);

    public static Color activeRegionColor = uiSelectedColor;

    public static Font captionFont = new Font("Serif", Font.PLAIN, 12);

    public static Font subCaptionFont = new Font("Serif", Font.ITALIC, 11);

    public static Font lineFont = new Font("Serif", Font.PLAIN, 11);

    public static Font textFont = new Font("Serif", Font.PLAIN, 11);

    public static Font footerFont = new Font("Serif", Font.BOLD + Font.ITALIC, 12);

    public static Color captionColor = new java.awt.Color(103, 147, 218);

    public static int rowHeight = 16;

    public static int columnHeaderHeight = 14;

    public static int rowHeaderWidth = 14;

    public static int footerHeight = 14;

    public static int textRowHeight = 10;

    public static int linkIconRadius = 22;

    public static int headerBarWidth = 4;

    public static int headerBarHeight = 8;

    public static int headerButtonWidth = 8;

    public static int headerButtonHeight = 8;

    public static int headerBarGap = 1;

    public static int defaultNInset = 8;

    public static int defaultEInset = 8;

    public static int defaultSInset = 8;

    public static int defaultWInset = 8;

    public static int defaultHeight = 54;

    public static int defaultWidth = 108;

    public static int defaultFixedGap = 4;

    public static float hVisWeigthChemical = 1.4F;

    public static float vVisWeigthChemical = 1.3F;

    public static float hVisWeigthLink = 1.0F;

    public static float vVisWeigthLink = 1.0F;

    public static float hVisWeigthEffect = 1.2F;

    public static float vVisWeigthEffect = 1.2F;

    public static float hVisWeigthTest = 1.0F;

    public static float vVisWeigthTest = 1.0F;

    public static float hVisWeigthSubstanceContainer = 1.5F;

    public static float vVisWeigthSubstanceContainer = 1.8F;

    public static float hVisWeigthEffectContainer = 1.3F;

    public static float vVisWeigthEffectContainer = 1.3F;

    public static float hVisWeigthLinkContainer = 1.1F;

    public static float vVisWeigthLinkContainer = 1.1F;

    public static int linkIconOffset = 1;

    public static BasicStroke contourPen = new BasicStroke(2.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public static BasicStroke insidePen = new BasicStroke(1.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public static BasicStroke selectionPen = new BasicStroke(1.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public static BasicStroke newArcPen = new BasicStroke(1.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public static BasicStroke arcPen = new BasicStroke(1.0F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2.0F, new float[] { 1.0F, 1.0F }, 1.0F);

    public static boolean hideActionText = true;
}
