package org.kwantu.m2.model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kwantu.m2.KwantuFaultException;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.graph.layout.GraphLayoutListener;
import org.netbeans.api.visual.graph.layout.GridGraphLayout;
import org.netbeans.api.visual.graph.layout.UniversalGraph;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene.SceneListener;
import org.netbeans.api.visual.widget.Widget;

/**
 * This class generates and stores a graphical image of the relationships between
 * the KwantuClasses of a KwantuModel.
 * 
 */
public class KwantuImageExporter {

    public static final Log LOG = LogFactory.getLog(KwantuImageExporter.class);

    private static class MyListener implements GraphLayoutListener<String, String> {

        @Override
        public void graphLayoutStarted(UniversalGraph<String, String> graph) {
            System.out.println("Layout started");
        }

        @Override
        public void graphLayoutFinished(UniversalGraph<String, String> graph) {
            System.out.println("Layout finished");
        }

        @Override
        public void nodeLocationChanged(UniversalGraph<String, String> graph, String node, Point previousPreferredLocation, Point newPreferredLocation) {
            System.out.println("Node location changed: " + node + " -> " + newPreferredLocation);
        }
    }

    private static class sceneListener implements SceneListener {

        @Override
        public void sceneRepaint() {
            System.out.println("Scene Repaint");
        }

        @Override
        public void sceneValidating() {
            System.out.println("Scene Validating");
        }

        @Override
        public void sceneValidated() {
            System.out.println("Scene Validated");
        }
    }

    public KwantuModelGraphScene createSceneFromModel(KwantuBusinessObjectModel model) {
        KwantuModelGraphScene scene = new KwantuModelGraphScene();
        scene.addSceneListener(new sceneListener());
        HashMap<KwantuClass, LabelWidget> mapClassesToWidgets = new HashMap<KwantuClass, LabelWidget>();
        for (KwantuClass c : model.getKwantuClasses()) {
            LOG.info("KwantuClass: " + c.getName());
            Widget nodeWidget = scene.addNode(c);
            nodeWidget.getActions().addAction(ActionFactory.createMoveAction());
            mapClassesToWidgets.put(c, scene.getWidgetLabel());
        }
        for (KwantuClass c : model.getKwantuClasses()) {
            for (final KwantuRelationship r : c.getDeclaredKwantuRelationships()) {
                if (r.isPrimaryRelationship()) {
                    KwantuEdge edge = new KwantuEdge() {

                        @Override
                        public String getTooltipText() {
                            return (r.isOwner() ? "<>- " : "--- ") + r.getName() + " " + (r.getCardinality().equals(KwantuRelationship.Cardinality.MANY) ? "(*)" : "(1)") + " -->\\n" + "<-- (1) " + r.getInverseKwantuRelationship().getName();
                        }

                        @Override
                        public boolean isInheritance() {
                            return false;
                        }
                    };
                    ConnectionWidget edgeWidget = (ConnectionWidget) scene.addEdge(edge);
                    KwantuClass fromClass = r.getInverseKwantuRelationship().getRelationshipToKwantuClass();
                    KwantuClass toClass = r.getRelationshipToKwantuClass();
                    scene.setEdgeSource(edge, fromClass);
                    scene.setEdgeTarget(edge, toClass);
                }
            }
        }
        for (final KwantuClass c : model.getKwantuClasses()) {
            final KwantuClass superClass = c.getKwantuSuperClass();
            if (superClass != null) {
                KwantuEdge edge = new KwantuEdge() {

                    @Override
                    public String getTooltipText() {
                        return c.getName() + " extends " + superClass.getName();
                    }

                    @Override
                    public boolean isInheritance() {
                        return true;
                    }
                };
                scene.addEdge(edge);
                scene.setEdgeSource(edge, c);
                scene.setEdgeTarget(edge, superClass);
            }
        }
        return scene;
    }

    public File exportModelTree(KwantuBusinessObjectModel objectModel) {
        KwantuModelGraphScene scene = createSceneFromModel(objectModel);
        GridGraphLayout<KwantuClass, KwantuEdge> graphLayout = new GridGraphLayout<KwantuClass, KwantuEdge>();
        graphLayout.setGaps(50, 50);
        SceneLayout sceneGraphLayout = LayoutFactory.createSceneGraphLayout(scene, graphLayout);
        sceneGraphLayout.invokeLayout();
        BufferedImage emptyImage = new BufferedImage(400, 400, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D emptyGraphics = emptyImage.createGraphics();
        scene.validate(emptyGraphics);
        emptyGraphics.dispose();
        Rectangle viewBounds = scene.convertSceneToView(scene.getBounds());
        BufferedImage bufferedImage = new BufferedImage(viewBounds.width, viewBounds.height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        scene.paint(graphics2D);
        graphics2D.dispose();
        try {
            File file = File.createTempFile("model-tree", ".png");
            if (file != null) {
                modelTreeToPNG(file, bufferedImage);
            }
            return file;
        } catch (IOException ex) {
            throw new KwantuFaultException("unable to create temp file", ex);
        }
    }

    private void modelTreeToPNG(File file, BufferedImage bf) {
        try {
            ImageIO.write(bf, "png", file);
        } catch (IOException e) {
            throw new KwantuFaultException("Cannot write scene file" + e);
        }
    }
}
