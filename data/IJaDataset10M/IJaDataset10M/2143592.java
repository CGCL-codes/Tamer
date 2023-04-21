package com.silenistudios.silenus.dom;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import com.silenistudios.silenus.ParseException;
import com.silenistudios.silenus.SceneRenderer;
import com.silenistudios.silenus.ShapeRenderInterface;
import com.silenistudios.silenus.dom.fillstyles.FillStyle;
import com.silenistudios.silenus.xml.Node;
import com.silenistudios.silenus.xml.XMLUtility;

/**
 * A shape represents a vector drawing in flash.
 * @author Karel
 *
 */
public class ShapeInstance extends Instance {

    long fId;

    private static long IdCounter = 0;

    Vector<FillStyle> fFillStyles = new Vector<FillStyle>();

    Vector<StrokeStyle> fStrokeStyles = new Vector<StrokeStyle>();

    Vector<Path> fFillPaths = new Vector<Path>();

    Vector<Path> fStrokePaths = new Vector<Path>();

    public ShapeInstance(XMLUtility XMLUtility, Node root, int frameIndex) throws ParseException {
        super(XMLUtility, root, frameIndex);
        if (XMLUtility.hasNode(root, "fills")) {
            Node node = XMLUtility.findNode(root, "fills");
            Vector<Node> fills = XMLUtility.findNodes(node, "FillStyle");
            for (Node fillNode : fills) {
                FillStyle style = new FillStyle(XMLUtility, fillNode);
                fFillStyles.add(style);
            }
        }
        if (XMLUtility.hasNode(root, "strokes")) {
            Node node = XMLUtility.findNode(root, "strokes");
            Vector<Node> strokes = XMLUtility.findNodes(node, "StrokeStyle");
            for (Node strokeNode : strokes) {
                StrokeStyle style = new StrokeStyle(XMLUtility, strokeNode);
                fStrokeStyles.add(style);
            }
        }
        Node node = XMLUtility.findNode(root, "edges");
        PathGenerator pathGenerator = new PathGenerator();
        pathGenerator.generate(XMLUtility, node);
        fStrokePaths = pathGenerator.getStrokePaths();
        fFillPaths = pathGenerator.getFillPaths();
        for (Path path : fStrokePaths) if (path.getIndex() >= fStrokeStyles.size()) throw new ParseException("Non-existing stroke style refered in path");
        for (Path path : fFillPaths) if (path.getIndex() >= fFillStyles.size()) throw new ParseException("Non-existing stroke style refered in path");
        fId = IdCounter++;
        setLibraryItemName("########SHAPE#########+++" + fId + "+++");
    }

    public long getId() {
        return fId;
    }

    public StrokeStyle getStrokeStyle(int index) {
        return fStrokeStyles.get(index);
    }

    public FillStyle getFillStyle(int index) {
        return fFillStyles.get(index);
    }

    public Vector<Path> getStrokePaths() {
        return fStrokePaths;
    }

    public Vector<Path> getFillPaths() {
        return fFillPaths;
    }

    public void render(ShapeRenderInterface renderer) {
        Vector<Path> fillPaths = getFillPaths();
        for (Path path : fillPaths) {
            path.render(renderer);
            if (!isMask()) getFillStyle(path.getIndex()).getPaint().render(renderer); else renderer.clip();
        }
        Vector<Path> strokePaths = getStrokePaths();
        for (Path path : strokePaths) {
            path.render(renderer);
            renderer.stroke(getStrokeStyle(path.getIndex()));
        }
    }

    @Override
    public String getJSON() {
        StringBuilder ss = new StringBuilder();
        ss.append("{");
        ss.append("\"type\":\"shape\",");
        ss.append("\"strokeStyles\":[");
        for (int i = 0; i < fStrokeStyles.size(); ++i) {
            if (i != 0) ss.append(",");
            ss.append(fStrokeStyles.get(i).getJSON());
        }
        ss.append("],");
        ss.append("\"fillStyles\":[");
        for (int i = 0; i < fFillStyles.size(); ++i) {
            if (i != 0) ss.append(",");
            ss.append(fFillStyles.get(i).getJSON());
        }
        ss.append("],");
        ss.append("\"strokePaths\":[");
        for (int i = 0; i < fStrokePaths.size(); ++i) {
            if (i != 0) ss.append(",");
            ss.append(fStrokePaths.get(i).getJSON());
        }
        ss.append("],");
        ss.append("\"fillPaths\":[");
        for (int i = 0; i < fFillPaths.size(); ++i) {
            if (i != 0) ss.append(",");
            ss.append(fFillPaths.get(i).getJSON());
        }
        ss.append("]");
        ss.append("}");
        return ss.toString();
    }

    @Override
    public void render(SceneRenderer renderer, int frame) {
        renderer.renderShape(this);
    }

    @Override
    public Set<Bitmap> getUsedImages() {
        return new HashSet<Bitmap>();
    }
}
