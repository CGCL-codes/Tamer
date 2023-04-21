package com.googlecode.pondskum.gui.swing.notifyer;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

public final class RemainderPanel extends JPanel {

    private static final long serialVersionUID = -1614904446957062628L;

    private static final int HUNDRED = 100;

    private final DisplayDetailsPack displayDetailsPack;

    public RemainderPanel() {
        displayDetailsPack = new DefaultDisplayDetailsPack();
    }

    public RemainderPanel(final DisplayDetailsPack displayDetailsPack) {
        this.displayDetailsPack = displayDetailsPack;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        double usage = displayDetailsPack.getTotalUsage();
        double usageRatio = usage / displayDetailsPack.getQuotaLimit();
        Dimension panelDimension = getSize();
        g.setColor(displayDetailsPack.getBackgroundColour());
        g.fill3DRect(0, 0, panelDimension.width, panelDimension.height, false);
        g.setColor(displayDetailsPack.getUsageColourChooser().getColor(usageRatio));
        g.fill3DRect(0, 0, (int) (panelDimension.width * usageRatio), panelDimension.height, true);
        g.setColor(displayDetailsPack.getLimitTextColour());
        Font oldFont = getFont();
        g.setFont(displayDetailsPack.getQuotaFont());
        String limitText = displayDetailsPack.getQuotaLimitWithUnits();
        Dimension limitDimension = LocationFinder.findRightCorner(g, limitText, panelDimension);
        g.drawString(limitText, limitDimension.width, limitDimension.height);
        String usageText = getFormattedUsage(usage);
        Dimension usageDimension = LocationFinder.findLeftCorner(g, usageText, panelDimension);
        g.drawString(usageText, usageDimension.width, limitDimension.height);
        g.setFont(oldFont);
        g.setColor(displayDetailsPack.getPercentageUsageTextColour());
        String percentageText = ((int) (usageRatio * HUNDRED)) + "%";
        Dimension centreDimension = LocationFinder.findCentreLocation(g, percentageText, panelDimension);
        g.drawString(percentageText, centreDimension.width, centreDimension.height);
    }

    private String getFormattedUsage(final double usage) {
        return String.format("%1$5.2g %2$s", usage, displayDetailsPack.getQuotaUnits());
    }
}
