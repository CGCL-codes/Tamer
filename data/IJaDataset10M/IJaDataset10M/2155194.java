package org.ist_spice.mdcs.beans.description_processors;

import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.ist_spice.mdcs.decisionMaking.mediaComperators.PosterComperator;
import org.ist_spice.mdcs.decisionMaking.mediaComperators.VideoComperator;
import org.ist_spice.mdcs.entities.Region;
import org.ist_spice.mdcs.entities.mediaElements.MediaContent;
import org.ist_spice.mdcs.entities.mediaElements.PosterContent;
import org.ist_spice.mdcs.entities.mediaElements.VideoContent;
import org.ist_spice.mdcs.interfaces.SMILMediaContentFactory_IF;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

@Stateless
@Remote(SMILMediaContentFactory_IF.class)
public class SMILMediaContentFactory implements SMILMediaContentFactory_IF {

    @PersistenceContext
    private EntityManager manager;

    public int createMediaContent(Node mediaElement) {
        MediaContent mediaContent;
        System.out.println("createMediaConent");
        mediaContent = parseVideoContent(mediaElement);
        if (mediaContent != null) {
            mediaContent.setMediaComp(new VideoComperator((VideoContent) mediaContent));
            mediaContent.setNode(mediaElement);
            Region region = null;
            String regionAttr = mediaContent.getRegionAttr();
            if (regionAttr != null) {
                System.out.println("performing SQL region statement with: " + regionAttr);
                String query = "SELECT * FROM region WHERE regionID = ?";
                List tempList = manager.createNativeQuery(query, Region.class).setParameter(1, regionAttr).getResultList();
                System.out.println("found: " + tempList.size() + " region match for videocontent");
                if (tempList != null && tempList.size() != 0) {
                    region = (Region) tempList.get(0);
                    region.addContent(mediaContent);
                    mediaContent.setRegion(region);
                }
            }
            return mediaContent.getId();
        }
        mediaContent = parsePosterContent(mediaElement);
        if (mediaContent != null) {
            mediaContent.setMediaComp(new PosterComperator((PosterContent) mediaContent));
            mediaContent.setNode(mediaElement);
            Region region = null;
            String regionAttr = mediaContent.getRegionAttr();
            if (regionAttr != null) {
                System.out.println("performing SQL region statement with: " + regionAttr);
                String query = "SELECT * FROM region WHERE regionID = ?";
                List tempList = manager.createNativeQuery(query, Region.class).setParameter(1, regionAttr).getResultList();
                System.out.println("found: " + tempList.size() + " region match for postercontent");
                if (tempList != null && tempList.size() != 0) {
                    region = (Region) tempList.get(0);
                    region.addContent(mediaContent);
                    mediaContent.setRegion(region);
                }
            }
            return mediaContent.getId();
        }
        return -1;
    }

    /**
	 * Tests a given SMIL content node for being a Video Content, while parsing
	 * into an entity.
	 * 
	 * @param mediaNode
	 * @param videoContent
	 * @return true if VideoContent
	 */
    private VideoContent parseVideoContent(Node mediaNode) {
        final String ELEMENT_NAME = "video";
        final String SRC_ATTRIB = "src";
        final String CLIPBEGIN_ATTRIB = "clipBegin";
        final String CLIPEND_ATTRIB = "clipEnd";
        final String REGION_ATTRIB = "region";
        VideoContent videoContent = null;
        boolean isVideo = false;
        String nodeName = mediaNode.getNodeName();
        if (nodeName.equalsIgnoreCase(ELEMENT_NAME)) {
            isVideo = true;
            videoContent = new VideoContent();
            videoContent.setMediaType(ELEMENT_NAME);
            manager.persist(videoContent);
            System.out.println(ELEMENT_NAME);
        }
        if (isVideo) {
            NamedNodeMap attributes = mediaNode.getAttributes();
            Node tempNode = attributes.getNamedItem(SRC_ATTRIB);
            if (tempNode != null) {
                videoContent.setSrc(tempNode.getNodeValue());
            }
            tempNode = attributes.getNamedItem(CLIPBEGIN_ATTRIB);
            if (tempNode != null) {
                videoContent.setClipBegin(tempNode.getNodeValue());
            }
            tempNode = attributes.getNamedItem(CLIPEND_ATTRIB);
            if (tempNode != null) {
                videoContent.setClipEnd(tempNode.getNodeValue());
            }
            tempNode = attributes.getNamedItem(REGION_ATTRIB);
            if (tempNode != null) {
                videoContent.setRegionAttr(tempNode.getNodeValue());
            }
        }
        return videoContent;
    }

    /**
	 * Tests a given SMIL content node for being a Poster Content, while parsing
	 * into an entity.
	 * 
	 * @param mediaNode
	 * @param mediaContent
	 * @return true if PosterContent
	 */
    private PosterContent parsePosterContent(Node mediaNode) {
        final String ELEMENT_NAME = "aa:poster";
        final String SRC_ATTRIB = "src";
        final String TO_ATTRIB = "to";
        final String TITLE_ATTRIB = "title";
        final String SUMMARY_ATTRIB = "summary";
        final String REGION_ATTRIB = "region";
        boolean isPoster = false;
        PosterContent posterContent = null;
        String nodeName = mediaNode.getNodeName();
        if (nodeName.equalsIgnoreCase(ELEMENT_NAME)) {
            isPoster = true;
            posterContent = new PosterContent();
            posterContent.setMediaType(ELEMENT_NAME);
            manager.persist(posterContent);
            System.out.println("Found poster content");
        }
        if (isPoster) {
            NamedNodeMap attributes = mediaNode.getAttributes();
            Node tempNode = attributes.getNamedItem(SRC_ATTRIB);
            if (tempNode != null) {
                posterContent.setSrc(tempNode.getNodeValue());
            }
            tempNode = attributes.getNamedItem(TO_ATTRIB);
            if (tempNode != null) {
                posterContent.setToMuch(tempNode.getNodeValue());
            }
            tempNode = attributes.getNamedItem(SUMMARY_ATTRIB);
            if (tempNode != null) {
                posterContent.setSummary(tempNode.getNodeValue());
            }
            tempNode = attributes.getNamedItem(TITLE_ATTRIB);
            if (tempNode != null) {
                posterContent.setTitle(tempNode.getNodeValue());
            }
            tempNode = attributes.getNamedItem(REGION_ATTRIB);
            if (tempNode != null) {
                posterContent.setRegionAttr(tempNode.getNodeValue());
            }
        }
        return posterContent;
    }
}
