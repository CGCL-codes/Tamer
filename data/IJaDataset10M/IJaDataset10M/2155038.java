package org.gamenet.application.mm8leveleditor.handler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.gamenet.application.mm8leveleditor.lod.LodResource;
import org.gamenet.swing.controls.SwatchPanel;
import org.gamenet.util.ByteConversions;
import org.gamenet.util.TaskObserver;
import org.gamenet.util.UnimplementedMethodException;

public class PalHandler implements LodResourceHandler {

    public Component getComponentFor(LodResource lodResource, TaskObserver taskObserver) {
        Component component = null;
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea description = new JTextArea(lodResource.getTextDescription());
        panel.add(description, BorderLayout.PAGE_START);
        byte byteArray[] = null;
        try {
            byteArray = lodResource.getData();
        } catch (IOException anIOException) {
            Throwable throwable = new Throwable("Unable to extract data.", anIOException);
            throwable.printStackTrace();
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter, true);
            throwable.printStackTrace(printWriter);
            component = new JTextArea(stringWriter.toString());
            panel.add(component, BorderLayout.CENTER);
            return panel;
        }
        Color paletteColorArray[] = new Color[256];
        for (int index = 0; index < 256; ++index) {
            int red = ByteConversions.convertByteToInt(byteArray[(index * 3) + 0]);
            int green = ByteConversions.convertByteToInt(byteArray[(index * 3) + 1]);
            int blue = ByteConversions.convertByteToInt(byteArray[(index * 3) + 2]);
            paletteColorArray[index] = new Color(red, green, blue);
        }
        component = new SwatchPanel(paletteColorArray, 16, 16, 2, 12);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    public LodResource getUpdatedLodResource() {
        throw new UnimplementedMethodException("getUpdatedLodResource() Unimplemented.");
    }
}
