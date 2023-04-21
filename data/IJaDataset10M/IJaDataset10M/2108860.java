package org.kubiki.ide;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.font.*;
import org.kubiki.xml.*;
import org.kubiki.base.*;
import org.kubiki.gui.*;
import org.kubiki.ide.*;
import org.kubiki.graphic.*;
import java.io.*;
import com.keypoint.PngEncoder;
import java.awt.image.*;

public class BasicFormElement extends BasicObject {

    BasicFormDefinition fd;

    Vector types;

    Shape[] s;

    Font font;

    GradientPaint gp = null;

    public BasicFormElement() {
        super();
        types = new Vector();
        types.add(new ConfigValue("textfield", "textfield", "textfield"));
        types.add(new ConfigValue("selection", "selection", "selection"));
        types.add(new ConfigValue("timestamp"));
        classtype = "org.kubiki.ide.BasicFormElement";
        addProperty("title", "string", "");
        addProperty("dataobject", "string", "");
        addProperty("default", "string", "");
        addProperty("datatype", "string", "String");
        addProperty("type", "string", "textfield");
        addProperty("selectionsource", "string", "");
        addProperty("values", "text", "");
        addProperty("fontname", "string", "Arial");
        addProperty("fontsize", "Integer", "12");
        addProperty("fontstyle", "Integer", "0");
        addProperty("altcolor", "Color", "-1");
        addProperty("descriptor", "text", "");
        x = 200;
        y = 200;
        width = 200;
        height = 25;
        getProperty("type").setSelection(types);
        s = new Shape[0];
        font = new Font("Arial", 0, 12);
        setProperty("width", "200");
        setProperty("height", "25");
    }

    public void setParent(BasicClass bc) {
        this.fd = (BasicFormDefinition) bc;
        this.parent = bc;
    }

    public void drawObject(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        g.setStroke(stroke);
        g.setFont(font);
        g.setColor(Color.decode(getString("fillcolor")));
        g.fillRect(x, y, 100, height);
        g.setColor(Color.decode(getString("drawcolor")));
        g.drawString(getString("title"), x + 2, y + 20);
        g.setColor(Color.black);
        g.drawRect(100 + x, y, width, height);
    }

    public void initObjectLocal() {
        super.initObjectLocal();
    }

    public void mouseReleased(MouseEvent e) {
        setProperty("x", x + "");
        setProperty("y", y + "");
        fd.repaint();
    }

    public void doLeftMouseClicked(MouseEvent e) {
        IDE ide = (IDE) getRoot();
        ide.setPropertyPanel(getPropertyPanel());
    }

    public Shape getShape(String descriptor, float xtranslate, float ytranslate) {
        GeneralPath gp = new GeneralPath();
        String[] parts = descriptor.split(";");
        float x = (float) 0;
        float y = (float) 0;
        for (int i = 0; i < parts.length; i++) {
            String[] args = parts[i].split(":");
            if (args.length > 2 && i == 0) {
                x = (new Float(args[1])).floatValue();
                y = (new Float(args[2])).floatValue();
                gp.moveTo(x, y);
            } else if (args.length > 4 && args[0].equals("q")) {
                float x1 = (new Float(args[1])).floatValue();
                float y1 = (new Float(args[2])).floatValue();
                float x2 = (new Float(args[3])).floatValue();
                float y2 = (new Float(args[4])).floatValue();
                gp.quadTo(x2, y2, x1, y1);
            } else if (args.length > 6 && args[0].equals("c")) {
                float x1 = (new Float(args[1])).floatValue();
                float y1 = (new Float(args[2])).floatValue();
                float x2 = (new Float(args[3])).floatValue();
                float y2 = (new Float(args[4])).floatValue();
                float x3 = (new Float(args[5])).floatValue();
                float y3 = (new Float(args[6])).floatValue();
                gp.curveTo(x2, y2, x3, y3, x1, y1);
            } else if (args.length > 2 && args[0].equals("l")) {
                float x1 = (new Float(args[1])).floatValue();
                float y1 = (new Float(args[2])).floatValue();
                gp.lineTo(x1, y1);
            }
        }
        AffineTransform at = new AffineTransform();
        at.translate(xtranslate, ytranslate);
        gp.transform(at);
        return gp;
    }

    public void actionPerformedLocal(ActionEvent e) {
        if (e.getActionCommand().equals("Export Image")) {
            exportImage();
        }
    }

    public void exportImage() {
        PngEncoder png;
        initObject();
        BufferedImage buf;
        buf = new BufferedImage(getProperty("width").intValue() + 1, getProperty("height").intValue() * 2 + 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buf.createGraphics();
        RenderingHints qualityHints;
        qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(qualityHints);
        g.setColor(Color.decode(getParent().getString("bgcolor")));
        g.fillRect(0, 0, 500, 500);
        g.translate(-x, -y);
        drawObject(g);
        active = 1;
        g.translate(0, getProperty("height").intValue() + 1);
        drawObject(g);
        active = -1;
        IDE ide = (IDE) getRoot();
        Image im = ide.mainFrame.createImage(buf.getSource());
        boolean encodeAlpha = false;
        int filter = 0;
        int compressionLevel = 0;
        byte[] pngbytes;
        png = new PngEncoder(im, (encodeAlpha) ? PngEncoder.ENCODE_ALPHA : PngEncoder.NO_ALPHA, filter, compressionLevel);
        try {
            FileOutputStream outfile = new FileOutputStream(getProject().getString("exportpath") + "/images/" + getName() + ".png");
            pngbytes = png.pngEncode();
            if (pngbytes == null) {
                System.out.println("Null image");
            } else {
                outfile.write(pngbytes);
            }
            outfile.flush();
            outfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
