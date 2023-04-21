package ar.com.kalessin.sizoop.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.nodes.P3DRect;

public class SzGraphicConcept {

    float ancho, baseWidth, anchocuerpo, alto;

    PNode node;

    SzConcept concept;

    static double minConceptWidth;

    static double maxConceptWidth;

    static double maxHeigth;

    static double minHeight;

    static double screenRate;

    public SzGraphicConcept() {
        ancho = 0;
        baseWidth = 0;
        anchocuerpo = 0;
        alto = 0;
        screenRate = 0;
    }

    public SzGraphicConcept(PNode n) {
        node = n;
    }

    public static void setMinWidth(double mw) {
        minConceptWidth = mw;
    }

    public static void setMaxWidth(double Mw) {
        maxConceptWidth = Mw;
    }

    public static void setMaxHeight(double mh) {
        System.out.println("Ancho Máximo: " + mh);
        maxHeigth = mh;
    }

    public static void setMinHeigth(double mh) {
        minHeight = mh;
    }

    public static void setScreenRate(double sr) {
        screenRate = sr;
    }

    public void setNode(PNode n) {
        node = n;
    }

    public PText getTitle() {
        PNode borde = getBorder();
        PText tit = (PText) borde.getChild(1);
        return tit;
    }

    public PNode getBorder() {
        PNode bord = node.getChild(0);
        if (bord.getAttribute("role").equals("Concept Border")) {
            return bord;
        } else {
            return null;
        }
    }

    public PImage getImage(PNode nodo) {
        PNode bord = getBorder();
        PImage pimg;
        if (bord.getChildrenCount() > 3) {
            pimg = (PImage) bord.getChild(3);
        } else {
            pimg = null;
        }
        return pimg;
    }

    public PText getBody() {
        PNode bord = getBorder();
        return (PText) bord.getChild(2);
    }

    public void createConcept(SzConcept conc, PNode nodo) {
        PText tit = new PText(conc.getConceptName());
        Font ftit = tit.getFont();
        tit.setFont(ftit.deriveFont(14.0f));
        tit.setName("titulo:" + tit.getText());
        tit.addAttribute("role", "Concept Title");
        if (tit.getWidth() > NodeCreator.maxConceptWidth) {
            tit.setConstrainWidthToTextWidth(false);
            tit.setBounds(tit.getX(), tit.getY(), SzGraphicConcept.maxConceptWidth, NodeCreator.maxHeigth * 0.3);
        }
        String cuerpo = conc.getConceptContent();
        PText body = new PText(cuerpo);
        if (conc.getBodyFont() != null) {
            body.setFont(conc.getBodyFont());
        } else {
            body.setFont(body.getFont().deriveFont(6));
        }
        body.setPickable(false);
        body.addAttribute("role", "Concept Concerns");
        body.setConstrainWidthToTextWidth(false);
        body.setBounds(tit.getX(), tit.getY() + tit.getHeight(), tit.getWidth(), NodeCreator.maxHeigth * 0.7);
        baseWidth = (float) tit.getWidth();
        ancho = (float) ((baseWidth > SzGraphicConcept.minConceptWidth) ? (float) (1.2 * baseWidth) : SzGraphicConcept.minConceptWidth);
        anchocuerpo = (float) ((body.getText().length() == 0) ? 0 : (ancho * 0.65));
        alto = (float) ((body.getHeight() + tit.getHeight()) * 1.25);
        PNode rect = createBorder(nodo, tit, body, conc);
        nodo.addChild(rect);
        nodo.setName(tit.getText());
        nodo.addAttribute("role", "concept");
        String imagePath = conc.getPathToImage();
        double anchoImg = 0;
        double anchotex = ancho * 0.9;
        if (imagePath != null) {
            BufferedImage img = null;
            try {
                String appPath = System.getProperty("user.dir");
                img = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                System.out.println("no pude leer archivo" + imagePath);
            }
            anchoImg = (cuerpo.length() == 0) ? ancho : ancho * 0.4;
            double relWidth = anchoImg / img.getWidth();
            double altoImg = relWidth * img.getHeight();
            if (altoImg > SzGraphicConcept.maxHeigth) {
                double reduceFactor = SzGraphicConcept.maxHeigth / altoImg;
                System.out.println("reduciendo tamaño de imagen");
                altoImg = altoImg * reduceFactor;
                anchoImg = anchoImg * reduceFactor;
            }
            PImage pimg = new PImage(img);
            pimg.addAttribute("role", "Image");
            pimg.addAttribute("path", imagePath);
            anchotex = (anchoImg == 0) ? ancho : ancho * 0.6;
            System.out.println("Ancho de imagen: " + anchoImg + ", ancho texto:" + anchotex);
            pimg.setBounds(tit.getX() + anchocuerpo, tit.getY() + 20, tit.getX() + anchoImg, body.getY() + altoImg);
            pimg.setPickable(false);
            pimg.addAttribute("role", "Image");
            pimg.addAttribute("path", imagePath);
            rect.addChild(pimg);
        }
        body.setBounds(tit.getX(), tit.getY() + tit.getHeight() + 5, anchotex, body.getY());
        if (conc.hasLinkedResource) {
            float[] xp = { 0, 5, 5, 10, 5, 5, 0, 0 };
            float[] yp = { 8, 8, 0, 10, 15, 10, 10, 8 };
            PNode resourceIcon = PPath.createPolyline(xp, yp);
            resourceIcon.setOffset(rect.getX() + rect.getWidth() - 12, rect.getY() + rect.getHeight() - 17);
            rect.addChild(resourceIcon);
        }
    }

    /**
	 * method to draw the concept background
	 * pathFond point to background image
	 * @author seretur
	 * 
	 * @param nodo node which background will be created to
	 * @param tit node with concept name
	 * @param body Piccolo2d's text node with concept description
	 * @param conc internal representation of a concept
	 */
    public PNode createBorder(PNode nodo, PText tit, PText body, SzConcept conc) {
        PImage fond;
        String sep = System.getProperty("file.separator");
        String homeDir = System.getProperty("user.home");
        String pathFond = sep + "szimages" + sep + "fondoJorge.png";
        BufferedImage imgFond = null;
        String appPath = homeDir + pathFond;
        try {
            imgFond = ImageIO.read(new File(appPath));
        } catch (IOException e) {
            System.out.println("no pude leer archivo " + appPath);
        }
        fond = new PImage(imgFond);
        fond.addAttribute("role", "background");
        fond.setName("background");
        PNode rect = new P3DRect((float) nodo.getX() - 5, (float) nodo.getY() - 5, ancho, alto);
        rect.setName("Contorno " + conc.getConceptName());
        rect.addAttribute("role", "Concept Border");
        rect.setWidth(ancho);
        rect.addChild(fond);
        rect.addChild(tit);
        rect.addChild(body);
        Color bgColor = conc.getColor();
        if (bgColor != null) {
            String col = String.valueOf(bgColor.getRed()) + "," + String.valueOf(bgColor.getGreen()) + "," + String.valueOf(bgColor.getBlue());
            rect.setPaint(bgColor);
            rect.addAttribute("color", col);
        } else {
            rect.setPaint(Color.orange);
            rect.addAttribute("color", "255,200,0");
        }
        if (fond != null) {
            fond.setHeight(rect.getHeight());
            fond.setWidth(rect.getWidth());
            fond.setX(nodo.getX() - 3);
            fond.setY(nodo.getY() - 3);
        }
        return rect;
    }
}
