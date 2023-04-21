package uk.ac.kingston.aqurate.author_UI;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent.ElementChange;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import uk.ac.kingston.aqurate.author_controllers.DefaultController;
import uk.ac.kingston.aqurate.util.ImageFileFilter;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class AttachImagePanel extends JPanel {

    class AttachImageDocumentListener implements DocumentListener {

        public void changedUpdate(DocumentEvent arg0) {
        }

        public void insertUpdate(DocumentEvent arg0) {
        }

        public void removeUpdate(DocumentEvent arg0) {
            Element elem = null;
            ElementChange elemChg = null;
            Element[] removedElements = null;
            ArrayList<String> alsID = new ArrayList<String>();
            String sID = "";
            HTMLDocument htmldocprompt = null;
            HTMLDocument htmldoc = (HTMLDocument) arg0.getDocument();
            Element root = htmldoc.getDefaultRootElement();
            ElementIterator eli = new ElementIterator(root);
            Element thisElement = eli.first();
            while (thisElement != null) {
                elemChg = arg0.getChange(thisElement);
                if (elemChg != null) {
                    removedElements = elemChg.getChildrenRemoved();
                    for (int i = 0; i < removedElements.length; i++) {
                        if (removedElements[i].getName().equals("img")) {
                            sID = (String) removedElements[i].getAttributes().getAttribute(HTML.Attribute.ID);
                            if (sID != null) {
                                alsID.add(sID);
                            }
                        }
                    }
                }
                thisElement = eli.next();
            }
            htmldocprompt = (HTMLDocument) htmlPanelPrompt.getJEditorPane().getDocument();
            java.util.Iterator<String> it = alsID.iterator();
            while (it.hasNext()) {
                String sIDTemp = it.next();
                elem = htmldocprompt.getElement(sIDTemp);
                if (elem != null) {
                    int nStartOffest = elem.getStartOffset();
                    int nEndOffset = elem.getEndOffset();
                    int nLength = nEndOffset - nStartOffest;
                    try {
                        htmldocprompt.replace(nStartOffest, nLength, "", elem.getAttributes());
                    } catch (BadLocationException bade) {
                        bade.printStackTrace();
                    }
                }
            }
        }
    }

    static final int iImageHeight = 240;

    static final int iImageWidth = 320;

    private static final long serialVersionUID = 1L;

    private ArrayList<Dimension> aDim = null;

    private ArrayList<String> alsAlt = null;

    private AttachImageDocumentListener attachImageDocumentListener = null;

    DefaultController controller;

    private HTMLDocument htmlDocument = null;

    private HTMLEditorKit htmlEditorKit = null;

    HTMLPanel htmlPanelPrompt;

    private JButton jButton = null;

    private JEditorPane jEditorPane = null;

    private AqurateFramework owner = null;

    private JScrollPane jScrollPane = null;

    public AttachImagePanel(AqurateFramework owner, DefaultController controller, HTMLPanel htmlPanelPrompt) {
        super();
        this.owner = owner;
        this.controller = controller;
        this.htmlPanelPrompt = htmlPanelPrompt;
        alsAlt = new ArrayList<String>();
        init();
    }

    ArrayList<Dimension> getDimensions() {
        return aDim;
    }

    JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton("Attach Image");
            jButton.setPreferredSize(new Dimension(120, 30));
            jButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    final JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.addChoosableFileFilter(new ImageFileFilter());
                    jFileChooser.setAcceptAllFileFilterUsed(false);
                    int returnVal = jFileChooser.showOpenDialog(owner);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        int iWidth;
                        int iHeight;
                        File file = jFileChooser.getSelectedFile();
                        BufferedImage bufImg = null;
                        Dimension dim = null;
                        try {
                            bufImg = ImageIO.read(file);
                        } catch (IOException ioex) {
                            ioex.printStackTrace();
                        }
                        iWidth = bufImg.getWidth();
                        iHeight = bufImg.getHeight();
                        if (iWidth > iImageWidth) {
                            double dWidth = iImageWidth;
                            double dHeight = iHeight;
                            double dAspectRatio = dWidth / dHeight;
                            dHeight = dHeight * dAspectRatio;
                            iHeight = (int) dHeight;
                            iWidth = (int) dWidth;
                        } else if (iHeight > iImageHeight) {
                            double dWidth = iWidth;
                            double dHeight = iImageHeight;
                            double dAspectRatio = dWidth / dWidth;
                            dWidth = dWidth * dAspectRatio;
                            iWidth = (int) dWidth;
                            iHeight = (int) dHeight;
                        }
                        dim = new Dimension(iWidth, iHeight);
                        update(dim, file);
                    }
                }
            });
        }
        return jButton;
    }

    public JEditorPane getJEditorPane() {
        if (jEditorPane == null) {
            jEditorPane = new JEditorPane();
            htmlEditorKit = new HTMLEditorKit();
            jEditorPane.setEditorKit(htmlEditorKit);
            htmlDocument = (HTMLDocument) htmlEditorKit.createDefaultDocument();
            jEditorPane.setDocument(htmlDocument);
            attachImageDocumentListener = new AttachImageDocumentListener();
            jEditorPane.getDocument().addDocumentListener(attachImageDocumentListener);
        }
        return jEditorPane;
    }

    JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            Dimension d = new Dimension(510, 30);
            jScrollPane.setPreferredSize(d);
            jScrollPane.setViewportView(getJEditorPane());
        }
        return jScrollPane;
    }

    void init() {
        this.add(getJScrollPane());
        this.add(getJButton());
    }

    private void update(Dimension dim, File filePrompt) {
        String sHTMLimgAttach = "";
        String sHTMLimgPrompt = "";
        int iWidthPrompt = (int) dim.getWidth();
        int iHeightPrompt = (int) dim.getHeight();
        String sWidthPrompt = String.valueOf(iWidthPrompt);
        String sHeightPrompt = String.valueOf(iHeightPrompt);
        Image imageAttach = null;
        BufferedImage bufferedImageAttach = null;
        Graphics2D graphics2D = null;
        File fileAttach = null;
        URL imgURLPrompt = null;
        URL imgURLAttach = null;
        BufferedOutputStream imageoutAttach = null;
        JPEGImageEncoder encoder = null;
        JPEGEncodeParam encoderParam = null;
        FileOutputStream fos = null;
        int id = filePrompt.getName().hashCode();
        int w = 150;
        int h = 15;
        int pix[] = new int[w * h];
        int index = 0;
        for (int y = 0; y < h; y++) {
            int red = (y * 255) / (h - 1);
            for (int x = 0; x < w; x++) {
                int blue = (x * 255) / (w - 1);
                pix[index++] = (255 << 24) | (red << 16) | blue;
            }
        }
        alsAlt.add(filePrompt.getName());
        imageAttach = createImage(new MemoryImageSource(w, h, pix, 0, w));
        bufferedImageAttach = new BufferedImage(imageAttach.getWidth(this), imageAttach.getHeight(this), BufferedImage.TYPE_INT_RGB);
        graphics2D = bufferedImageAttach.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(imageAttach, null, null);
        fileAttach = new File("temp" + alsAlt.size() + ".jpg");
        try {
            imgURLPrompt = filePrompt.toURL();
            imgURLAttach = fileAttach.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        graphics2D.drawString(filePrompt.getName(), 5, imageAttach.getHeight(this) - 5);
        try {
            fos = new FileOutputStream(fileAttach);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageoutAttach = new BufferedOutputStream(fos, 4092);
        encoder = JPEGCodec.createJPEGEncoder(imageoutAttach);
        encoderParam = encoder.getDefaultJPEGEncodeParam(bufferedImageAttach);
        encoderParam.setQuality(1, false);
        encoder.setJPEGEncodeParam(encoderParam);
        try {
            encoder.encode(bufferedImageAttach);
            imageoutAttach.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sHTMLimgPrompt = "<br></br><img src=\"" + imgURLPrompt + "\" alt=\"" + filePrompt.getName() + "\" width=\"" + sWidthPrompt + "\" height=\"" + sHeightPrompt + "\" id=\"id" + id + "\" class=\"attach\"></img>";
        sHTMLimgAttach = "<img src=\"" + imgURLAttach + "\" alt=\"" + filePrompt.getName() + "\" id=\"id" + id + "\"></img>";
        try {
            HTMLDocument htmldoc = (HTMLDocument) htmlPanelPrompt.getJEditorPane().getDocument();
            Element elementHTML = htmldoc.getRootElements()[0];
            Element elementBODY = elementHTML.getElement(0);
            int ij = elementBODY.getElementCount();
            Element elementContent = elementBODY.getElement(ij - 1);
            htmldoc.insertBeforeEnd(elementContent, sHTMLimgPrompt);
            HTMLEditorKit prompthtmlek = (HTMLEditorKit) htmlPanelPrompt.getJEditorPane().getEditorKit();
            htmlEditorKit.insertHTML(htmlDocument, jEditorPane.getCaretPosition(), sHTMLimgAttach, 0, 0, HTML.Tag.IMG);
        } catch (BadLocationException be) {
            be.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}
