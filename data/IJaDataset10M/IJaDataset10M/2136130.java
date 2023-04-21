package org.lindenb.tinytools;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.lindenb.me.Me;
import org.lindenb.treemap.TreeMap;
import org.lindenb.treemap.TreeMapModeler;
import org.lindenb.util.Compilation;
import org.lindenb.xml.XMLUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Paint a treemap of the activity in FriendFeed.
 *
 */
public class FriendFeedMap {

    /**
 * A user in friendFeed
 *
 */
    private class User implements TreeMap {

        String uri;

        String name;

        int post = 0;

        int like = 0;

        int comment = 0;

        Rectangle2D bounds = null;

        @Override
        public Rectangle2D getBounds() {
            return this.bounds;
        }

        @Override
        public void setBounds(Rectangle2D bounds) {
            this.bounds = bounds;
        }

        @Override
        public double getWeight() {
            switch(FriendFeedMap.this.observe) {
                case 0:
                    return post;
                case 1:
                    return like;
                case 2:
                    return comment;
            }
            throw new IllegalStateException();
        }
    }

    static final String DEFAULT_ROOM = "the-life-scientists";

    private String roomName = DEFAULT_ROOM;

    private int observe = 0;

    private DocumentBuilder docBuilder;

    private Map<String, User> uri2user = new HashMap<String, User>();

    private XPathExpression xpathEntries;

    private XPathExpression xpathUser;

    private XPathExpression xpathUserProfileUrl;

    private XPathExpression xpathUserName;

    private XPathExpression xpathLike;

    private XPathExpression xpathComment;

    private Rectangle outputRect = new Rectangle(0, 0, 400, 500);

    private String outputFile = "_treemap";

    private FriendFeedMap() throws ParserConfigurationException, XPathExpressionException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setCoalescing(true);
        f.setNamespaceAware(true);
        f.setValidating(false);
        f.setExpandEntityReferences(true);
        f.setIgnoringComments(false);
        f.setIgnoringElementContentWhitespace(true);
        docBuilder = f.newDocumentBuilder();
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        xpathEntries = xpath.compile("/feed/entry");
        xpathUser = xpath.compile("user");
        xpathUserProfileUrl = xpath.compile("profileUrl/text()");
        xpathUserName = xpath.compile("name/text()");
        xpathLike = xpath.compile("like");
        xpathComment = xpath.compile("comment");
    }

    private User findUser(Node root) throws XPathExpressionException, IOException, SAXException {
        Node node = (Node) xpathUser.evaluate(root, XPathConstants.NODE);
        if (node == null) return null;
        String userUri = (String) xpathUserProfileUrl.evaluate(node, XPathConstants.STRING);
        if (userUri == null || userUri.trim().length() == 0) return null;
        User user = this.uri2user.get(userUri);
        if (user == null) {
            user = new User();
            user.uri = userUri;
            user.name = (String) xpathUserName.evaluate(node, XPathConstants.STRING);
            this.uri2user.put(userUri, user);
            System.err.println("found new " + userUri + " " + this.uri2user.size());
        }
        return user;
    }

    private void parseFeeds() throws IOException, SAXException, XPathExpressionException {
        int start = 0;
        final int num = 100;
        while (true) {
            String uri = "http://friendfeed.com/api/feed/room/" + roomName + "?format=xml&start=" + start + "&num=" + num;
            System.err.println(uri);
            Document dom = this.docBuilder.parse(uri);
            NodeList entries = (NodeList) xpathEntries.evaluate(dom, XPathConstants.NODESET);
            for (int i = 0; i < entries.getLength(); ++i) {
                User user = findUser(entries.item(i));
                if (user != null) user.post++;
                NodeList nodelist = (NodeList) xpathLike.evaluate(entries.item(i), XPathConstants.NODESET);
                for (int j = 0; j < nodelist.getLength(); ++j) {
                    user = findUser(nodelist.item(j));
                    if (user != null) user.like++;
                }
                nodelist = (NodeList) xpathComment.evaluate(entries.item(i), XPathConstants.NODESET);
                for (int j = 0; j < nodelist.getLength(); ++j) {
                    user = findUser(nodelist.item(j));
                    if (user != null) user.comment++;
                }
            }
            if (entries.getLength() == 0) break;
            start += entries.getLength();
        }
        List<User> users = new ArrayList<User>(this.uri2user.size());
        for (String uri : this.uri2user.keySet()) {
            User u = uri2user.get(uri);
            if (u.getWeight() <= 0) continue;
            users.add(u);
        }
        TreeMapModeler modeler = new TreeMapModeler();
        modeler.layout(users, new Rectangle2D.Double(0, 0, this.outputRect.width, this.outputRect.height));
        BufferedImage img = new BufferedImage(this.outputRect.width, this.outputRect.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (User u : users) {
            URL url = new URL(u.uri + "/picture?size=large");
            BufferedImage picture = ImageIO.read(url);
            Rectangle2D rect = u.getBounds();
            g.drawImage(picture, (int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight(), null);
        }
        g.dispose();
        ImageIO.write(img, "png", new File(this.outputFile + ".png"));
        PrintWriter out = new PrintWriter(new File(this.outputFile + ".html"));
        out.println("<html><body><div style='text-align:center;'>");
        out.print("<map name='map1'>");
        for (User u : users) {
            Rectangle2D rect = u.getBounds();
            out.print("<area href='" + u.uri + "' ");
            out.print("alt='" + XMLUtilities.escape(u.name) + "' ");
            out.print("title='" + XMLUtilities.escape(u.name) + "' ");
            out.print("shape='rect' ");
            out.print("coords='" + (int) rect.getX() + "," + (int) rect.getY() + "," + (int) rect.getMaxX() + "," + (int) rect.getMaxY() + "' ");
            out.print("/>");
        }
        out.print("</map>");
        out.println("<img src='jeter.png' width='" + this.outputRect.width + "' height='" + this.outputRect.height + "' usemap='#map1'/>");
        out.println("</div></body></html>");
        out.flush();
        out.close();
    }

    public static void main(String[] args) {
        try {
            FriendFeedMap app = new FriendFeedMap();
            int optind = 0;
            while (optind < args.length) {
                if (args[optind].equals("-h")) {
                    System.err.println("Pierre Lindenbaum PhD 2009. " + Me.WWW + " " + Me.MAIL);
                    System.err.println(Compilation.getLabel());
                    System.err.println("Creates a Treemap for a FriendFeed room.");
                    System.err.println("-room <name> default:" + app.roomName);
                    System.err.println("-t <integer> 0: observe posts. 1 observe likes. 2 observe comments. default:0");
                    System.err.println("-w <integer> width default:" + app.outputRect.width);
                    System.err.println("-h <integer> height default:" + app.outputRect.height);
                    System.err.println("-o <base-file> file default:" + app.outputFile);
                } else if (args[optind].equals("-room")) {
                    app.roomName = args[++optind];
                } else if (args[optind].equals("-t")) {
                    app.observe = Integer.parseInt(args[++optind]);
                    if (app.observe < 0 || app.observe > 2) {
                        System.err.println("Bad value for -t");
                        return;
                    }
                } else if (args[optind].equals("-room")) {
                    app.roomName = args[++optind];
                } else if (args[optind].equals("-o")) {
                    app.outputFile = args[++optind];
                } else if (args[optind].equals("-w")) {
                    app.outputRect.width = Integer.parseInt(args[++optind]);
                } else if (args[optind].equals("-h")) {
                    app.outputRect.height = Integer.parseInt(args[++optind]);
                } else if (args[optind].equals("--")) {
                    optind++;
                    break;
                } else if (args[optind].startsWith("-")) {
                    System.err.println("Unknown option " + args[optind]);
                } else {
                    break;
                }
                ++optind;
            }
            app.parseFeeds();
        } catch (Throwable err) {
            err.printStackTrace();
        }
    }
}
