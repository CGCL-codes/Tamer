package org.fao.waicent.kids.editor.servlet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fao.waicent.kids.editor.PatternOutlineEditor;
import org.fao.waicent.kids.server.kidsSession;
import org.fao.waicent.servlet.ImageServlet;
import org.fao.waicent.util.XColor;
import org.fao.waicent.util.XPatternOutline;
import org.fao.waicent.xmap2D.FeatureLayer;
import org.fao.waicent.xmap2D.FeatureProperties;

public class ColorPreviewServlet extends ImageServlet implements SingleThreadModel {

    public ColorPreviewServlet() {
    }

    public String getServletInfo() {
        return "Return a color preview as a gif.";
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String which = request.getParameter("which");
        int width = Integer.parseInt(request.getParameter("width"));
        int height = Integer.parseInt(request.getParameter("height"));
        Image image = null;
        Dimension image_size = new Dimension(width, height);
        image = new BufferedImage(image_size.width, image_size.height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setClip(new Rectangle(image_size));
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image_size.width, image_size.height);
        Rectangle bounds = new Rectangle(width, height);
        kidsSession kids = (kidsSession) request.getSession(true).getValue("kids");
        if (which.equals("map")) {
            if (kids != null && kids.getMap() != null) {
                kids.getMap().getPatternOutline().paintPreview((Graphics2D) g, bounds);
            }
        } else if (which.equals("fpo")) {
            if (kids != null) {
                int edit_feat_idx = Integer.parseInt(request.getParameter("i"));
                FeatureLayer f_layer = ((FeatureLayer) kids.getMap().getSelectedLayer());
                if (f_layer.getSymbolMode() == FeatureLayer.OneSymbol) {
                    f_layer.getPatternOutline().paintPreview((Graphics2D) g, bounds);
                } else {
                    FeatureProperties fp = f_layer.getFeatureProperties(edit_feat_idx);
                    if (fp.pattern_outline != null) {
                        fp.pattern_outline.paintPreview((Graphics2D) g, bounds);
                    } else {
                        f_layer.getPatternOutline().paintPreview((Graphics2D) g, bounds);
                    }
                }
            }
        } else if (which.equals("poEditor")) {
            if (kids != null) {
                ((PatternOutlineEditor) kids.getAttribute("PO_EDITOR")).getPatternOutline().paintPreview((Graphics2D) g, bounds);
            }
        } else if (which.equals("color")) {
            int red = Integer.parseInt(request.getParameter("r"));
            int green = Integer.parseInt(request.getParameter("g"));
            int blue = Integer.parseInt(request.getParameter("b"));
            int a = 255;
            if (request.getParameter("a") != null) {
                a = Integer.parseInt(request.getParameter("a"));
            }
            int alpha = a;
            Color color = new Color(red, green, blue, alpha);
            g.setColor(color);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.white);
            g.drawRect(0, 0, width, height);
        } else if (which.equals("icolor")) {
            int clr = Integer.parseInt(request.getParameter("color"));
            Color color = XColor.loadColor(clr);
            g.setColor(color);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.black);
            g.drawRect(0, 0, width, height);
        } else if (which.equals("po")) {
            String poStr = request.getParameter("po");
            XPatternOutline po = XPatternOutline.getPO(poStr);
            if (po != null) po.paintPreview((Graphics2D) g, bounds);
        }
        g.dispose();
        super.service(request, response, image, "gif");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
