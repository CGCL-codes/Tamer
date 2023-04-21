package uk.ac.ebi.intact.webapp.search.advancedSearch.powerSearch.business.graphdraw;

import org.apache.log4j.Logger;
import uk.ac.ebi.intact.business.IntactException;
import uk.ac.ebi.intact.context.IntactContext;
import uk.ac.ebi.intact.model.*;
import uk.ac.ebi.intact.webapp.search.advancedSearch.powerSearch.business.graphdraw.graph.*;
import uk.ac.ebi.intact.webapp.search.advancedSearch.powerSearch.struts.business.CvLists;
import uk.ac.ebi.intact.webapp.search.advancedSearch.powerSearch.struts.view.bean.CvBean;
import uk.ac.ebi.intact.webapp.search.business.Constants;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * This class provides methods to create graph images for the controlled vocabulary.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk), Anja Friedrichsen, Michael Kleen
 * @since 27.04.2005
 * @version $Id: CvGraph.java 6452 2006-10-16 16:09:42Z baranda $
 */
public class CvGraph {

    private static Logger logger = Logger.getLogger(Constants.LOGGER_NAME);

    /**
     * string that holds the imageMap corresponding to the image
     */
    private String imageMap;

    private static final long CACHE_TIMEOUT = 2 * 60 * 1000;

    private long lastImageGeneratedTime = -1;

    private HashMap imageCache = new HashMap();

    private static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * List of all supported CVs.
     */
    private static final Set SUPPORTED_CV = new HashSet();

    static {
        SUPPORTED_CV.add(CvInteraction.class);
        SUPPORTED_CV.add(CvInteractionType.class);
        SUPPORTED_CV.add(CvInteractorType.class);
        SUPPORTED_CV.add(CvIdentification.class);
        SUPPORTED_CV.add(CvFeatureType.class);
        SUPPORTED_CV.add(CvFeatureIdentification.class);
    }

    /**
     * First this method retrieves all CVs, that belong to the CV class specified in the argument, out of the database.
     * Afterwards it creates for each Cv object one node for the graph and finds all parent relations between these
     * nodes.
     *
     * @param cvClass Class of the CV which should be drawn
     *
     * @return image showing the DAG of the CVs
     *
     * @throws IntactException if a data access error occurs.
     */
    public synchronized Image createImage(Class cvClass) throws IntactException {
        if (!SUPPORTED_CV.contains(cvClass)) {
            throw new IntactException("Unsupported Controlled Vocabulary: " + cvClass.getName());
        }
        if (lastImageGeneratedTime == -1) {
            lastImageGeneratedTime = System.currentTimeMillis();
        }
        long timeElapsed = System.currentTimeMillis() - lastImageGeneratedTime;
        boolean timeout = timeElapsed > CACHE_TIMEOUT;
        logger.info("time elapsed: " + timeElapsed);
        logger.info("timeout: " + timeout);
        if (!imageCache.containsKey(cvClass) || timeout) {
            logger.info("Generating image for " + cvClass.getName());
            Stroke thinStroke = new BasicStroke(1);
            Graph g = new Graph();
            BufferedImage image;
            StringBuffer sb = null;
            Collection nodes = new ArrayList(64);
            Map cvobjectToNode = new HashMap(64);
            CvLists cvList = new CvLists();
            cvList.addMenuListItem(cvClass, nodes);
            Collection cvObjects = new ArrayList(nodes.size());
            for (Iterator iterator = nodes.iterator(); iterator.hasNext(); ) {
                CvBean cvBean = (CvBean) iterator.next();
                CvDagObject cdo = IntactContext.getCurrentInstance().getDataContext().getDaoFactory().getCvObjectDao(CvDagObject.class).getByAc(cvBean.getAc());
                cvObjects.add(cdo);
            }
            nodes.clear();
            nodes = null;
            for (Iterator iterator = cvObjects.iterator(); iterator.hasNext(); ) {
                CvDagObject cv = (CvDagObject) iterator.next();
                RectangularNode node = new RectangularNode(40, 40, cv.getFullName(), "javascript:SendInfo('" + cv.getShortLabel() + "')", null, cv.getShortLabel(), Color.white, Color.blue, thinStroke);
                g.nodes.add(node);
                cvobjectToNode.put(cv, node);
            }
            for (Iterator iterator = cvObjects.iterator(); iterator.hasNext(); ) {
                CvDagObject cv = (CvDagObject) iterator.next();
                findParentRelations(cv, cvobjectToNode, g);
            }
            HierarchicalLayout layout = new HierarchicalLayout(g, HierarchicalLayout.PARENT_TOP);
            layout.layout();
            final int width = layout.getWidth();
            final int height = layout.getHeight();
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D g2 = image.createGraphics();
            g2.setColor(Color.white);
            g2.fillRect(0, 0, width, height);
            g2.setColor(Color.black);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (Iterator it = g.edges.iterator(); it.hasNext(); ) {
                StrokeEdge edge = (StrokeEdge) it.next();
                edge.render(g2);
            }
            sb = new StringBuffer(16384);
            for (Iterator it = g.nodes.iterator(); it.hasNext(); ) {
                RectangularNode node = (RectangularNode) it.next();
                node.render(g2);
                sb.append(node.getImageMap()).append(NEW_LINE);
            }
            if (logger.isInfoEnabled()) {
                long heapSize = Runtime.getRuntime().totalMemory();
                long heapMaxSize = Runtime.getRuntime().maxMemory();
                long heapFreeSize = Runtime.getRuntime().freeMemory();
                logger.info("in Producer: current size of heap: " + heapSize);
                logger.info("maximum heap size: " + heapMaxSize);
                logger.info("free heap size: " + heapFreeSize);
            }
            imageCache.put(cvClass, image);
            imageCache.put(cvClass + "Map", sb.toString());
        } else {
            logger.info("Use the cache");
        }
        this.setImageMap((String) imageCache.get(cvClass + "Map"));
        return (Image) imageCache.get(cvClass);
    }

    /**
     * Find the parent relations between the nodes and add edges between terms.
     *
     * @param cv             cv object to find the parents for.
     * @param cvobjectToNode Map with all nodes to get the nodes corresponding to the cv object.
     * @param graph          graph to generate.
     */
    public void findParentRelations(CvDagObject cv, Map cvobjectToNode, Graph graph) {
        LayoutNode child = (LayoutNode) cvobjectToNode.get(cv);
        if (child == null) {
            return;
        }
        Collection parents = cv.getParents();
        for (Iterator iterator = parents.iterator(); iterator.hasNext(); ) {
            CvDagObject parentObject = (CvDagObject) iterator.next();
            LayoutNode parent = (LayoutNode) cvobjectToNode.get(parentObject);
            if (parent == null) {
                return;
            }
            graph.edges.add(new StrokeEdge(parent, child, Color.blue, new BasicStroke(1)));
        }
    }

    /**
     * Get the imageMap value.
     *
     * @return string with the imageMap corresponding to the image.
     *
     * @see #setImageMap
     */
    public String getImageMap() {
        return imageMap;
    }

    /**
     * Defines the imageMap.
     *
     * @param imageMap image map to define.
     *
     * @see #getImageMap
     */
    public void setImageMap(String imageMap) {
        this.imageMap = imageMap;
    }
}
