package org.wings.plaf.xhtml;

import java.awt.Color;
import java.io.IOException;
import org.wings.*;
import org.wings.io.*;
import org.wings.plaf.*;

public class DesktopPaneCG implements org.wings.plaf.DesktopPaneCG {

    private static final String propertyPrefix = "DesktopPane";

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    public void installCG(SComponent component) {
        component.setStyle(component.getSession().getCGManager().getStyle(getPropertyPrefix() + ".style"));
    }

    public void uninstallCG(SComponent c) {
    }

    public void write(Device d, SComponent c) throws IOException {
        SBorder border = c.getBorder();
        SDesktopPane desktopPane = (SDesktopPane) c;
        Utils.writeBorderPrefix(d, border);
        writePrefix(d, desktopPane);
        Utils.writeContainerContents(d, desktopPane);
        writePostfix(d, desktopPane);
        Utils.writeBorderPostfix(d, border);
    }

    protected void writePrefix(Device d, SDesktopPane desktopPane) throws IOException {
    }

    protected void writePostfix(Device d, SDesktopPane desktopPane) throws IOException {
    }
}
