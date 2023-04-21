package toolkit.levelEditor.tools;

import game.resourceObjects.LevelResource;
import game.resourceObjects.LevelResource.PolygonResource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import net.phys2d.math.Vector2f;
import com.baseoneonline.java.nanoxml.XMLElement;
import com.baseoneonline.java.nanoxml.XMLParseException;

public class SVGImporter {

    private final Logger log = Logger.getLogger(getClass().getName());

    private static SVGImporter instance;

    private SVGImporter() {
    }

    public LevelResource loadSVG(final File svgFile) {
        log.info("Attempting to import SVG: " + svgFile.getAbsolutePath());
        try {
            final XMLElement xml = new XMLElement();
            xml.parseFromReader(new FileReader(svgFile));
            final LevelResource resource = resourceFromXML(xml);
            log.info("SVG Loaded: " + printInfo(resource));
            return resource;
        } catch (final XMLParseException e) {
            e.printStackTrace();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String printInfo(final LevelResource resource) {
        final StringBuffer buf = new StringBuffer();
        int points = 0;
        for (final PolygonResource p : resource.collision) {
            points += p.points.size();
        }
        buf.append(resource.collision.size() + " polygons, " + points + " points.");
        return buf.toString();
    }

    public LevelResource resourceFromXML(final XMLElement xml) {
        final LevelResource lvl = new LevelResource();
        lvl.title = "Timed " + System.currentTimeMillis();
        boolean absolute = true;
        final List<XMLElement> pathList = xml.getChildren("path", true);
        for (final XMLElement path : pathList) {
            final String pathData = path.getStringAttribute("d");
            if (null != pathData) {
                final String cleanedPathData = pathData.trim().replace("\t", "").replace("\n", "");
                final List<String> tokens = tokenizePath(cleanedPathData);
                final Vector2f p = new Vector2f();
                PolygonResource poly = null;
                final Iterator<String> iter = tokens.iterator();
                while (iter.hasNext()) {
                    final String cmd = iter.next();
                    if (cmd.equals("m")) {
                        absolute = false;
                        if (null != poly) {
                            lvl.collision.add(poly);
                        }
                        poly = new PolygonResource();
                    } else if (cmd.equals("M")) {
                        absolute = true;
                        if (null != poly) {
                            lvl.collision.add(poly);
                        }
                        poly = new PolygonResource();
                    } else if (cmd.equalsIgnoreCase("z")) {
                        poly.closed = true;
                    } else if (cmd.equals("c")) {
                        absolute = false;
                    } else if (cmd.equals("C")) {
                        absolute = true;
                        p.set(0, 0);
                    } else if (cmd.equals("h")) {
                        absolute = false;
                        p.x += Float.parseFloat(iter.next());
                        poly.points.add(copy(p));
                    } else if (cmd.equals("H")) {
                        absolute = true;
                        p.x = Float.parseFloat(iter.next());
                        poly.points.add(copy(p));
                    } else if (cmd.equals("v")) {
                        absolute = false;
                        p.y += Float.parseFloat(iter.next());
                        poly.points.add(p);
                    } else if (cmd.equals("V")) {
                        absolute = true;
                        p.y = Float.parseFloat(iter.next());
                        poly.points.add(copy(p));
                        p.set(0, 0);
                    } else if (cmd.equals("s")) {
                        absolute = false;
                    } else if (cmd.equals("S")) {
                        absolute = true;
                        p.set(0, 0);
                    } else if (cmd.equals("l")) {
                        absolute = false;
                    } else if (cmd.equals("L")) {
                        absolute = true;
                        p.set(0, 0);
                    } else {
                        if (absolute) {
                            p.x = Float.parseFloat(cmd);
                            p.y = Float.parseFloat(iter.next());
                        } else {
                            p.x += Float.parseFloat(cmd);
                            p.y += Float.parseFloat(iter.next());
                        }
                        poly.points.add(copy(p));
                    }
                }
                if (!lvl.collision.contains(poly)) {
                    lvl.collision.add(poly);
                }
            }
        }
        return lvl;
    }

    private Vector2f copy(final Vector2f v) {
        return new Vector2f(v.x, -v.y);
    }

    private List<String> tokenizePath(final String subjectString) {
        final List<String> data = new ArrayList<String>();
        try {
            final Pattern regex = Pattern.compile("([M|C|V|L|Z|S|H])|(-?[|\\d|.]+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            final Matcher regexMatcher = regex.matcher(subjectString);
            while (regexMatcher.find()) {
                data.add(regexMatcher.group());
            }
        } catch (final PatternSyntaxException ex) {
        }
        return data;
    }

    public static LevelResource load(final File svgFile) {
        return getInstance().loadSVG(svgFile);
    }

    public static SVGImporter getInstance() {
        if (null == instance) instance = new SVGImporter();
        return instance;
    }
}
