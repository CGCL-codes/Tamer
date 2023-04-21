package es.ulpgc.dis.heuristicide.xml;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import es.ulpgc.dis.heuriskein.model.solver.Heuristic;
import es.ulpgc.dis.heuriskein.model.solver.MetaHeuristic;
import es.ulpgc.dis.heuriskein.model.solver.Operator;
import es.ulpgc.dis.heuriskein.model.solver.Selector;
import es.ulpgc.dis.heuriskein.utils.ReflextionUtils;
import es.ulpgc.dis.heuristicide.project.ConfigurationManager;

public class XMLMetaHeuristicWriter {

    private static SAXBuilder builder;

    private static Document document;

    private static Element root;

    public static ByteArrayOutputStream write(MetaHeuristic metaheuristic) {
        builder = new SAXBuilder();
        try {
            document = new Document();
            root = new Element("heuristic");
            if (metaheuristic.getType() != null) root.setAttribute("type", ConfigurationManager.getRepresentation(metaheuristic.getType()));
            document.setRootElement(root);
            writeHeader(metaheuristic);
            writeOperators(metaheuristic);
            XMLOutputter outp = new XMLOutputter();
            outp.setFormat(Format.getPrettyFormat());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            outp.output(document, stream);
            stream.close();
            return stream;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void writeOperators(MetaHeuristic metaheuristic) {
        Element operators = new Element("heuristics");
        for (Heuristic heuristic : metaheuristic.getHeuristics()) {
            Element operator = new Element("operator");
            operator.setAttribute("class", heuristic.getOperator().getClass().getCanonicalName());
            writeParameters(ReflextionUtils.getProperties(heuristic.getOperator(), Operator.class), heuristic.getOperator(), operator);
            Element selector = new Element("selector");
            selector.setAttribute("class", heuristic.getSelector().getClass().getCanonicalName());
            writeParameters(ReflextionUtils.getProperties(heuristic.getSelector(), Selector.class), heuristic.getSelector(), selector);
            operator.addContent(selector);
            operators.addContent(operator);
        }
        root.addContent(operators);
    }

    private static void writeParameters(PropertyDescriptor[] parameters, Object instance, Element operator) {
        for (PropertyDescriptor property : parameters) {
            Element parameter = new Element("parameter");
            parameter.setAttribute("name", property.getName());
            parameter.setAttribute("value", ReflextionUtils.readValue(property, instance));
            operator.addContent(parameter);
        }
    }

    private static void writeHeader(MetaHeuristic metaheuristic) {
        root.setAttribute("name", metaheuristic.getName());
    }
}
