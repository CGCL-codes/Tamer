package org.panopticode.report.treemap;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import edu.umd.cs.treemap.Rect;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.panopticode.PanopticodeOntology;
import org.panopticode.java.JavaOntology;
import org.panopticode.java.rdf.JavaRDFStore;
import org.panopticode.java.supplement.javancss.JavaNCSSOntology;
import org.panopticode.rdf.ResourceNotFoundException;
import org.panopticode.report.Report;
import static org.panopticode.util.SVGHelper.addText;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class BaseTreemap implements Report {

    private static final double DETAIL_WIDTH = 300.0;

    private static final double FOOTER_HEIGHT = 25.0;

    private static final double HEADER_HEIGHT = 25.0;

    private static final double REGION_BORDER = 3.0;

    public void runReport(final JavaRDFStore store, final Map<String, String> arguments) throws Exception {
        String outputFile = arguments.get(Report.OUTPUT_FILE);
        PrintStream output;
        XMLWriter xmlWriter;
        boolean interactive = false;
        try {
            if (outputFile == null) {
                output = System.out;
            } else {
                output = new PrintStream(outputFile);
            }
            xmlWriter = new XMLWriter(output, OutputFormat.createPrettyPrint());
            xmlWriter.write(generateXMLDocument(store, arguments, interactive));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Document generateXMLDocument(JavaRDFStore store, Map<String, String> arguments, boolean interactive) throws IOException {
        Rect bounds = new Rect(0, 0, 1024, 768);
        Document document;
        Element root;
        document = DocumentHelper.createDocument();
        root = document.addElement("svg");
        renderSVG(store, root, arguments, interactive, bounds);
        return document;
    }

    private Rect getBoundsByProjectSize(long ncss, Rect bounds) {
        double area = ncss * 16.0;
        double height = Math.sqrt(area);
        double width = height * (bounds.w / bounds.h);
        return new Rect(0, 0, width, height);
    }

    private long getTotalNCSS(JavaRDFStore store) {
        long totalNCSS;
        String query;
        query = "SELECT ?ncss " + "{ " + "?method  <" + RDF.type + "> <" + JavaOntology.METHOD_TYPE + "> . " + "?method  <" + JavaNCSSOntology.NCSS + ">  ?ncss " + "}";
        ResultSet rs = store.select(query);
        totalNCSS = 0L;
        while (rs.hasNext()) {
            QuerySolution qs = rs.nextSolution();
            Literal nameLiteral = qs.getLiteral("ncss");
            long methodNCSS = nameLiteral.getLong();
            totalNCSS = totalNCSS + methodNCSS;
        }
        return totalNCSS;
    }

    Map<String, Rect> getInteractiveLayout(Rect bounds) {
        Map<String, Rect> layout = new HashMap<String, Rect>();
        layout.put("title", new Rect(bounds.x + REGION_BORDER, bounds.y + REGION_BORDER, bounds.w - 2.0 * REGION_BORDER, HEADER_HEIGHT));
        layout.put("legend", new Rect(bounds.x + REGION_BORDER, bounds.h + bounds.y - FOOTER_HEIGHT - REGION_BORDER, bounds.w - 2.0 * REGION_BORDER, FOOTER_HEIGHT));
        layout.put("contents", new Rect(bounds.x + REGION_BORDER, bounds.y + HEADER_HEIGHT + 2.0 * REGION_BORDER, bounds.w - DETAIL_WIDTH - 3.0 * REGION_BORDER, bounds.h - HEADER_HEIGHT - FOOTER_HEIGHT - 4.0 * REGION_BORDER));
        layout.put("details", new Rect(bounds.x + bounds.w - DETAIL_WIDTH - REGION_BORDER, bounds.y + HEADER_HEIGHT + 2.0 * REGION_BORDER, DETAIL_WIDTH, bounds.h - HEADER_HEIGHT - FOOTER_HEIGHT - 4.0 * REGION_BORDER));
        return layout;
    }

    Map<String, Rect> getStaticLayout(Rect bounds) {
        Map<String, Rect> layout = new HashMap<String, Rect>();
        layout.put("title", new Rect(bounds.x + REGION_BORDER, bounds.y + REGION_BORDER, bounds.w - 2.0 * REGION_BORDER, HEADER_HEIGHT));
        layout.put("legend", new Rect(bounds.x + REGION_BORDER, bounds.h + bounds.y - FOOTER_HEIGHT - REGION_BORDER, bounds.w - 2.0 * REGION_BORDER, FOOTER_HEIGHT));
        layout.put("contents", new Rect(bounds.x + REGION_BORDER, bounds.y + HEADER_HEIGHT + 2.0 * REGION_BORDER, bounds.w - 2.0 * REGION_BORDER, bounds.h - HEADER_HEIGHT - FOOTER_HEIGHT - 4.0 * REGION_BORDER));
        return layout;
    }

    Map<String, Rect> getLayout(Rect bounds, boolean interactive) {
        if (interactive) {
            return getInteractiveLayout(bounds);
        } else {
            return getStaticLayout(bounds);
        }
    }

    void renderSVG(JavaRDFStore store, Element svgElement, Map<String, String> arguments, boolean interactive, Rect bounds) throws IOException {
        long ncss = getTotalNCSS(store);
        Map<String, Rect> layout;
        layout = getLayout(bounds, interactive);
        Rect dataBounds = getBoundsByProjectSize(ncss, layout.get("contents"));
        StringBuffer viewBox = new StringBuffer();
        viewBox.append(bounds.x);
        viewBox.append(" ");
        viewBox.append(bounds.y);
        viewBox.append(" ");
        viewBox.append(bounds.w);
        viewBox.append(" ");
        viewBox.append(bounds.h);
        svgElement.add(new Namespace("", "http://www.w3.org/2000/svg"));
        svgElement.add(new Namespace("xlink", "http://www.w3.org/1999/xlink"));
        svgElement.addAttribute("viewBox", viewBox.toString());
        renderTitle(svgElement, layout.get("title"), store, arguments);
        viewBox = new StringBuffer();
        viewBox.append(dataBounds.x);
        viewBox.append(" ");
        viewBox.append(dataBounds.y);
        viewBox.append(" ");
        viewBox.append(dataBounds.w);
        viewBox.append(" ");
        viewBox.append(dataBounds.h);
        Rect dataLayout = layout.get("contents");
        Element dataSvgElement = svgElement.addElement("svg", "http://www.w3.org/2000/svg");
        dataSvgElement.addAttribute("x", String.valueOf(dataLayout.x));
        dataSvgElement.addAttribute("y", String.valueOf(dataLayout.y));
        dataSvgElement.addAttribute("width", String.valueOf(dataLayout.w));
        dataSvgElement.addAttribute("height", String.valueOf(dataLayout.h));
        dataSvgElement.addAttribute("viewBox", viewBox.toString());
        getData(store).renderSVG(dataBounds, dataSvgElement, store);
        getCategorizer().renderHorizontalLegend(layout.get("legend"), svgElement);
    }

    void renderTitle(Element parent, Rect bounds, JavaRDFStore store, Map<String, String> arguments) {
        Element titleElement;
        String title;
        if (arguments.containsKey("title")) {
            title = arguments.get("title");
        } else {
            title = getDefaultTitle(store);
        }
        titleElement = addText(parent, (bounds.w / 2.0) + bounds.x, 17.0 + bounds.y, title);
        titleElement.addAttribute("style", "text-anchor: middle;");
    }

    ContainerMapItem getData(JavaRDFStore store) {
        ContainerMapItem parent = new ContainerMapItem("Project - " + sampleName(store), 0);
        for (Resource packageResource : store.getPackageStorage().fetchPackages()) {
            ContainerMapItem packageContainer = new ContainerMapItem("Package - " + name(packageResource), 3);
            parent.addChild(packageContainer);
            for (Resource classResource : classesInPackage(packageResource)) {
                ContainerMapItem classContainer = new ContainerMapItem("Class - " + name(classResource), 1);
                packageContainer.addChild(classContainer);
                getMethodData(classResource, classContainer);
            }
        }
        return parent;
    }

    private void getMethodData(Resource classResource, ContainerMapItem classContainer) {
        for (Resource methodResource : methodsInClass(classResource)) {
            try {
                if (methodResource.hasProperty(JavaNCSSOntology.NCSS)) {
                    double size = methodResource.getProperty(JavaNCSSOntology.NCSS).getLong();
                    String name = name(methodResource);
                    Category cat = getCategorizer().getCategory(methodResource);
                    String fill = cat.getFill();
                    String border = cat.getBorder();
                    classContainer.addChild(new LeafMapItem(size, fill, border, name, methodResource));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String sampleName(JavaRDFStore store) {
        try {
            return name(store.getSampleStorage().getSampleResource());
        } catch (ResourceNotFoundException e) {
            return "";
        }
    }

    private List<Resource> methodsInClass(Resource classResource) {
        List<Resource> methods = new LinkedList<Resource>();
        StmtIterator methodIt;
        methodIt = classResource.listProperties(JavaOntology.HAS_METHOD);
        while (methodIt.hasNext()) {
            methods.add(methodIt.nextStatement().getResource());
        }
        methodIt = classResource.listProperties(JavaOntology.HAS_CONSTRUCTOR);
        while (methodIt.hasNext()) {
            methods.add(methodIt.nextStatement().getResource());
        }
        return methods;
    }

    private List<Resource> classesInPackage(Resource packageResource) {
        List<Resource> classes = new LinkedList<Resource>();
        StmtIterator typeIt = packageResource.listProperties(JavaOntology.HAS_TYPE);
        while (typeIt.hasNext()) {
            classes.add(typeIt.nextStatement().getResource());
        }
        return classes;
    }

    private String name(Resource resource) {
        return resource.getProperty(PanopticodeOntology.NAME).getString();
    }

    public abstract Categorizer getCategorizer();

    public abstract String getDefaultTitle(JavaRDFStore store);
}
