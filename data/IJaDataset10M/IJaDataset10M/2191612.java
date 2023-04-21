package br.inf.ufrgs.renderxml4desktop.rendering.components;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.JComponent;
import org.jdom.Element;
import br.inf.ufrgs.renderxml4desktop.exceptions.ParsingErrorException;
import br.inf.ufrgs.renderxml4desktop.exceptions.RenderingErrorException;
import br.inf.ufrgs.renderxml4desktop.rendering.UsiXMLLayoutManagerRenderer;
import br.inf.ufrgs.renderxml4desktop.rendering.RendererFactory;
import br.inf.ufrgs.renderxml4desktop.rendering.components.UsiXMLJavaDesktopInterfaceRenderer;
import br.inf.ufrgs.renderxml4desktop.rendering.utils.InformationParser;

/**
 * Class responsible for rendering a gridbagbox element
 */
public class UsiXMLJavaDesktopGridBagBoxRenderer extends UsiXMLLayoutManagerRenderer {

    /**
     * Class constructor
     * @param usiXMLInterfaceRenderer Main interface renderer. Used to connect all
     * the rendered elements to the same interface
     * renderer
     */
    public UsiXMLJavaDesktopGridBagBoxRenderer(UsiXMLJavaDesktopInterfaceRenderer usiXMLInterfaceRenderer) {
        super(usiXMLInterfaceRenderer);
    }

    /**
     * Returns the next renderer in the chain when this isnt
     * the right one
     * @return Next element renderer
     */
    @Override
    public UsiXMLLayoutManagerRenderer getNextRenderer() {
        return new UsiXMLJavaDesktopFlowBoxRenderer((UsiXMLJavaDesktopInterfaceRenderer) this.usiXMLInterfaceRenderer);
    }

    /**
     * Return the UsiXML name of the element which is rendered by
     * this class
     */
    @Override
    public String getValidElementName() {
        return "gridBagBox";
    }

    /**
     * Renders the layout manager, inserting it into the content pane
     * and adding components to it
     * @param element UsiXML element to be rendered
     */
    @Override
    public Container processRendering(Element element, Container contentPane) throws ParsingErrorException, RenderingErrorException {
        String id = element.getAttributeValue("id");
        GridBagLayout gridbag = new GridBagLayout();
        this.usiXMLInterfaceRenderer.addRenderedComponent(id, gridbag);
        GridBagConstraints constraints = new GridBagConstraints();
        contentPane.setLayout(gridbag);
        int gridx = 0;
        int gridy = 0;
        int gridwidth = 0;
        int gridheight = 0;
        double weightx = 0;
        double weighty = 0;
        Insets insets = null;
        for (Object constraint : element.getChildren()) {
            Element child = (Element) constraint;
            try {
                gridx = NumberFormat.getIntegerInstance().parse(child.getAttributeValue("gridx")).intValue();
                gridy = NumberFormat.getIntegerInstance().parse(child.getAttributeValue("gridy")).intValue();
                gridwidth = NumberFormat.getIntegerInstance().parse(child.getAttributeValue("gridwidth")).intValue();
                gridheight = NumberFormat.getIntegerInstance().parse(child.getAttributeValue("gridheight")).intValue();
                weightx = NumberFormat.getNumberInstance().parse(child.getAttributeValue("weightx")).doubleValue();
                weighty = NumberFormat.getNumberInstance().parse(child.getAttributeValue("weighty")).doubleValue();
                InformationParser parser = new InformationParser();
                insets = parser.parseInsets(child.getAttributeValue("insets"));
                String fillString = child.getAttributeValue("fill");
                if (fillString != null) {
                    if (fillString.equals("both")) {
                        constraints.fill = GridBagConstraints.BOTH;
                    } else if (fillString.equals("horizontal")) {
                        constraints.fill = GridBagConstraints.HORIZONTAL;
                    } else if (fillString.equals("vertical")) {
                        constraints.fill = GridBagConstraints.VERTICAL;
                    } else if (fillString.equals("none")) {
                        constraints.fill = GridBagConstraints.NONE;
                    } else {
                        throw new RenderingErrorException("Unknown fill type");
                    }
                }
                String anchorString = child.getAttributeValue("anchor");
                if (anchorString != null) {
                    if (anchorString.equals("north")) {
                        constraints.anchor = GridBagConstraints.NORTH;
                    } else if (anchorString.equals("south")) {
                        constraints.anchor = GridBagConstraints.SOUTH;
                    } else if (anchorString.equals("west")) {
                        constraints.anchor = GridBagConstraints.WEST;
                    } else if (anchorString.equals("east")) {
                        constraints.anchor = GridBagConstraints.EAST;
                    } else if (anchorString.equals("northwest")) {
                        constraints.anchor = GridBagConstraints.NORTHWEST;
                    } else if (anchorString.equals("northeast")) {
                        constraints.anchor = GridBagConstraints.NORTHEAST;
                    } else if (anchorString.equals("northwest")) {
                        constraints.anchor = GridBagConstraints.NORTHWEST;
                    } else if (anchorString.equals("southwest")) {
                        constraints.anchor = GridBagConstraints.SOUTHWEST;
                    } else if (anchorString.equals("southeast")) {
                        constraints.anchor = GridBagConstraints.SOUTHEAST;
                    } else if (anchorString.equals("center")) {
                        constraints.anchor = GridBagConstraints.CENTER;
                    } else if (anchorString.equals("page_start")) {
                        constraints.anchor = GridBagConstraints.PAGE_START;
                    } else if (anchorString.equals("page_end")) {
                        constraints.anchor = GridBagConstraints.PAGE_END;
                    } else if (anchorString.equals("line_start")) {
                        constraints.anchor = GridBagConstraints.LINE_START;
                    } else if (anchorString.equals("line_end")) {
                        constraints.anchor = GridBagConstraints.LINE_END;
                    } else if (anchorString.equals("first_line_start")) {
                        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
                    } else if (anchorString.equals("first_line_end")) {
                        constraints.anchor = GridBagConstraints.FIRST_LINE_END;
                    } else if (anchorString.equals("last_line_start")) {
                        constraints.anchor = GridBagConstraints.LAST_LINE_START;
                    } else if (anchorString.equals("last_line_end")) {
                        constraints.anchor = GridBagConstraints.LAST_LINE_END;
                    } else {
                        throw new RenderingErrorException("Unknown anchor type");
                    }
                }
                constraints.gridx = gridx;
                constraints.gridy = gridy;
                constraints.weightx = weightx;
                constraints.weighty = weighty;
                constraints.gridwidth = gridwidth;
                constraints.gridheight = gridheight;
                constraints.insets = insets;
                Element constraintChild = (Element) child.getChildren().toArray()[0];
                JComponent component = (JComponent) RendererFactory.getInstance().getFirstElementRenderer((UsiXMLJavaDesktopInterfaceRenderer) this.usiXMLInterfaceRenderer).renderElement(constraintChild);
                contentPane.add(component, constraints);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return contentPane;
    }

    /**
     * Validates the UsiXML element being rendered. 
     * Currently not implemented
     * TODO: implement
     */
    @Override
    public boolean validateElement(Element element) {
        return true;
    }
}
