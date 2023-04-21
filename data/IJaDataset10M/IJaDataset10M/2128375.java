package br.inf.ufrgs.renderxml4desktop.rendering.components;

import br.inf.ufrgs.renderxml4desktop.rendering.RendererFactory;
import java.awt.Dimension;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import org.jdom.Element;
import br.inf.ufrgs.renderxml4desktop.exceptions.ParsingErrorException;
import br.inf.ufrgs.renderxml4desktop.exceptions.RenderingErrorException;
import br.inf.ufrgs.renderxml4desktop.rendering.UsiXMLElementRenderer;
import br.inf.ufrgs.renderxml4desktop.rendering.UsiXMLInterfaceRenderer;

public class UsiXMLJavaDesktopInterfaceRenderer extends UsiXMLInterfaceRenderer {

    private static UsiXMLJavaDesktopInterfaceRenderer _instance = null;

    private UsiXMLElementRenderer mainWindowRenderer = null;

    private JFrame mainWindow = null;

    private UsiXMLJavaDesktopInterfaceRenderer() {
        this.mainWindowRenderer = new UsiXMLJavaDesktopWindowRenderer(this);
    }

    public static UsiXMLJavaDesktopInterfaceRenderer getInstance() {
        if (_instance == null) {
            _instance = new UsiXMLJavaDesktopInterfaceRenderer();
        }
        return _instance;
    }

    public JFrame getMainWindow() {
        return this.mainWindow;
    }

    public UsiXMLElementRenderer getElementRenderer(Class renderedClass) {
        UsiXMLElementRenderer selectedRenderer = null;
        UsiXMLElementRenderer renderer = RendererFactory.getInstance().getFirstElementRenderer(this);
        while (true) {
            if (renderer.getElementClass().equals(renderedClass)) {
                selectedRenderer = renderer;
                break;
            } else {
                renderer = renderer.getNextRenderer();
                if (renderer == null) {
                    break;
                }
            }
        }
        return selectedRenderer;
    }

    public void renderInterface(Element cuiModel) throws RenderingErrorException, ParsingErrorException {
        this.mainWindow = new JFrame();
        JDesktopPane desktopPane = new JDesktopPane();
        String id = cuiModel.getAttributeValue("id");
        String attributeName = cuiModel.getAttributeValue("name");
        List<JInternalFrame> existingFrames = new ArrayList<JInternalFrame>();
        for (Object child : cuiModel.getChildren()) {
            Element childElement = (Element) child;
            JInternalFrame window = (JInternalFrame) RendererFactory.getInstance().getFirstElementRenderer((UsiXMLJavaDesktopInterfaceRenderer) this).renderElement(childElement);
            desktopPane.add(window);
            existingFrames.add(window);
            window.setVisible(false);
            try {
                window.setClosed(true);
            } catch (PropertyVetoException ex) {
                throw new RenderingErrorException();
            }
        }
        JInternalFrame visibleFrame = existingFrames.get(0);
        int width = visibleFrame.getWidth();
        int height = visibleFrame.getHeight();
        this.mainWindow.setPreferredSize(new Dimension(width + 10, height + 10));
        desktopPane.add(visibleFrame);
        visibleFrame.setVisible(true);
        try {
            visibleFrame.setMaximum(true);
        } catch (PropertyVetoException ex) {
            throw new RenderingErrorException();
        }
        this.mainWindow.setContentPane(desktopPane);
        this.mainWindow.setName(attributeName);
        this.mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainWindow.pack();
        this.mainWindow.setVisible(true);
        this.mainWindow.setState(JFrame.MAXIMIZED_BOTH);
        this.mainWindow.toFront();
    }
}
