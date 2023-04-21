package com.silenistudios.silenus.dom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.silenistudios.silenus.ParseException;
import com.silenistudios.silenus.dom.lines.Line;
import com.silenistudios.silenus.dom.lines.QuadraticCurve;
import com.silenistudios.silenus.dom.lines.StraightLine;
import com.silenistudios.silenus.xml.Node;
import com.silenistudios.silenus.xml.XMLUtility;

/**
 * This helper class will parse subsequent <Edge> nodes, and generate the appropriate path
 * objects from the somewhat strange data format that is used in the XML file.
 * @author Karel
 *
 */
public class PathGenerator {

    Vector<Path> fFillPaths = new Vector<Path>();

    Vector<Path> fStrokePaths = new Vector<Path>();

    List<Path> fOpenPaths = new LinkedList<Path>();

    public PathGenerator() {
    }

    public void generate(XMLUtility XMLUtility, Node root) throws ParseException {
        Map<Integer, Map<String, List<Line>>> pathsByColor = new HashMap<Integer, Map<String, List<Line>>>();
        Vector<Node> edges = XMLUtility.findNodes(root, "Edge");
        int[] fillTypes = new int[2];
        for (Node edge : edges) {
            Vector<Line> lines = null;
            try {
                lines = getLines(XMLUtility, edge);
            } catch (ParseException e) {
                continue;
            }
            for (int i = 0; i < lines.size(); ++i) {
                fillTypes[0] = XMLUtility.getIntAttribute(edge, "fillStyle0", 0) - 1;
                fillTypes[1] = XMLUtility.getIntAttribute(edge, "fillStyle1", 0) - 1;
                int strokeType = XMLUtility.getIntAttribute(edge, "strokeStyle", 0) - 1;
                if (strokeType != -1) {
                    Path path = new Path(strokeType);
                    path.add(lines.get(i));
                    fStrokePaths.add(path);
                }
                for (int fillType = 0; fillType < 2; ++fillType) {
                    if (fillTypes[fillType] == -1) continue;
                    Line line = lines.get(i);
                    if (fillType == 1) line = line.invert();
                    String hash = getPointHash(line.getStart());
                    if (!pathsByColor.containsKey(fillTypes[fillType])) pathsByColor.put(fillTypes[fillType], new HashMap<String, List<Line>>());
                    Map<String, List<Line>> paths = pathsByColor.get(fillTypes[fillType]);
                    if (!paths.containsKey(hash)) paths.put(hash, new ArrayList<Line>());
                    paths.get(hash).add(line);
                }
            }
        }
        for (Entry<Integer, Map<String, List<Line>>> entry : pathsByColor.entrySet()) {
            Integer fillType = entry.getKey();
            Map<String, List<Line>> hash = entry.getValue();
            while (hash.size() > 0) {
                List<Line> paths = hash.values().iterator().next();
                Line firstLine = paths.get(0);
                Path path1 = new Path(fillType);
                path1.add(firstLine);
                paths.remove(0);
                if (paths.size() == 0) hash.remove(getPointHash(firstLine.getStart()));
                String endHash = getPointHash(firstLine.getStop());
                List<Line> connections = hash.get(endHash);
                if (connections == null) continue;
                Line connection = firstLine;
                while (connections.size() > 0) {
                    boolean alreadyInverse = false;
                    Line connectionInverted = null;
                    for (Line p : connections) {
                        if (p.getStart().equals(connection.getStop()) && p.getStop().equals(connection.getStart())) {
                            alreadyInverse = true;
                            connectionInverted = p;
                        }
                    }
                    if (!alreadyInverse) {
                        connectionInverted = connection.invert();
                        connections.add(connectionInverted);
                    }
                    Collections.sort(connections);
                    ListIterator<Line> it = connections.listIterator(connections.size());
                    while (it.hasPrevious()) {
                        Line p = it.previous();
                        if (!p.equals(connectionInverted)) {
                        } else {
                            if (!alreadyInverse) it.remove();
                            break;
                        }
                    }
                    if (it.hasPrevious()) {
                        connection = it.previous();
                        it.remove();
                    } else {
                        connection = connections.get(connections.size() - 1);
                        connections.remove(connections.size() - 1);
                    }
                    if (connections.size() == 0) {
                        hash.remove(endHash);
                    }
                    path1.add(connection);
                    if (path1.isClosed()) {
                        fFillPaths.add(path1);
                        break;
                    }
                    endHash = getPointHash(connection.getStop());
                    connections = hash.get(endHash);
                    if (connections == null) break;
                }
            }
        }
    }

    private static String getPointHash(Point p) {
        return p.getTwipX() + "_" + p.getTwipY();
    }

    private static Pattern LinePattern = Pattern.compile("([-]?[0-9]*[\\.]?[0-9]+)\\s+([-]?[0-9]*[\\.]?[0-9]+)[S]?[0-9]*\\s*[\\|\\[]{1}\\s*([-]?[0-9]*[\\.]?[0-9]+)\\s+([-]?[0-9]*[\\.]?[0-9]+).*");

    private static Pattern InstructionPattern = Pattern.compile("[!\\x7C\\[\\]][^!\\x7C\\[\\]]+");

    private Vector<Line> getLines(XMLUtility XMLUtility, Node edge) throws ParseException {
        String edgesString = XMLUtility.getAttribute(edge, "edges");
        Matcher matcher = InstructionPattern.matcher(edgesString);
        Vector<Line> lines = new Vector<Line>();
        Point lastStop = null;
        while (matcher.find()) {
            String s = edgesString.substring(matcher.start(), matcher.end());
            char instruction = s.charAt(0);
            switch(instruction) {
                case '!':
                    lastStop = parseMoveTo(s);
                    break;
                case '|':
                case '/':
                    lines.add(new StraightLine(lastStop, s));
                    break;
                case '[':
                case ']':
                    lines.add(new QuadraticCurve(lastStop, s));
                    break;
            }
        }
        return lines;
    }

    private Point parseMoveTo(String s) throws ParseException {
        Matcher matcher = Point.getRegExpCompiled().matcher(s);
        if (!matcher.find() || matcher.groupCount() != 2) throw new ParseException("Invalid move to instruction found in DOMShape: \"" + s + "\"");
        return new Point(matcher.group(1), matcher.group(2));
    }

    public Vector<Path> getStrokePaths() {
        return fStrokePaths;
    }

    public Vector<Path> getFillPaths() {
        return fFillPaths;
    }
}
