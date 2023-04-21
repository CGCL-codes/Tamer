package ch.intertec.storybook.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.miginfocom.swing.MigLayout;
import ch.intertec.storybook.model.Tag;
import ch.intertec.storybook.model.TagLink;
import ch.intertec.storybook.model.TagLinkPeer;
import ch.intertec.storybook.model.Location;
import ch.intertec.storybook.model.SbCharacter;
import ch.intertec.storybook.model.Scene;
import ch.intertec.storybook.model.SceneLinkLocation;
import ch.intertec.storybook.model.SceneLinkLocationPeer;
import ch.intertec.storybook.model.SceneLinkSbCharacter;
import ch.intertec.storybook.model.SceneLinkSbCharacterPeer;
import ch.intertec.storybook.model.SceneLinkStrand;
import ch.intertec.storybook.model.SceneLinkStrandPeer;
import ch.intertec.storybook.model.Strand;
import ch.intertec.storybook.toolkit.swing.SwingTools;
import ch.intertec.storybook.toolkit.swing.label.CleverLabel;

public class LinkComponentFactory {

    public static final String PROPERTY_LOCATION_LINK_LIST = "locationlinklist";

    public static final String PROPERTY_CHARACTER_LINK_LIST = "characterlinklist";

    public static final String PROPERTY_ITEM_LINK_LIST = "itemlinklist";

    public static JScrollPane createItemLinksScroller(Scene scene, boolean setSize) {
        try {
            JPanel panel = new JPanel(new MigLayout("insets 0,flowy"));
            panel.setName("itemlinksscrollpane");
            panel.setOpaque(true);
            panel.setBackground(Color.white);
            List<TagLink> list = TagLinkPeer.doSelectBySceneId(scene.getId());
            if (list.isEmpty()) {
                return null;
            }
            for (TagLink link : list) {
                Tag tag = link.getTag();
                if (tag == null) {
                    continue;
                }
                JLabel label = new JLabel(tag.getLabelText());
                label.setToolTipText(tag.getInfo());
                panel.add(label);
            }
            JScrollPane scroller = new JScrollPane();
            scroller.putClientProperty(PROPERTY_ITEM_LINK_LIST, list);
            scroller.setViewportView(panel);
            scroller.setBorder(null);
            if (setSize) {
                if (list.size() == 1) {
                    scroller.setMinimumSize(new Dimension(100, 30));
                    scroller.setPreferredSize(new Dimension(170, 30));
                } else {
                    scroller.setMinimumSize(new Dimension(100, 40));
                    scroller.setPreferredSize(new Dimension(170, 40));
                }
            }
            scroller.getVerticalScrollBar().setUnitIncrement(10);
            scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            return scroller;
        } catch (Exception e) {
            SwingTools.showException(e);
            e.printStackTrace();
        }
        return new JScrollPane();
    }

    public static JScrollPane createLocationLinksScroller(Scene scene) {
        return createLocationLinksScroller(scene, true);
    }

    public static JScrollPane createLocationLinksScroller(Scene scene, boolean setSize) {
        try {
            JPanel panel = new JPanel(new MigLayout("insets 0,flowy"));
            panel.setName("locationlinksscrollpane");
            panel.setOpaque(true);
            panel.setBackground(Color.white);
            List<SceneLinkLocation> list = SceneLinkLocationPeer.doSelectBySceneId(scene.getId());
            if (list.isEmpty()) {
                return null;
            }
            for (SceneLinkLocation link : list) {
                Location location = link.getLocation();
                JLabel label = new JLabel(location.getLabelText());
                label.setToolTipText(location.getInfo());
                panel.add(label);
            }
            JScrollPane scroller = new JScrollPane();
            scroller.putClientProperty(PROPERTY_LOCATION_LINK_LIST, list);
            scroller.setViewportView(panel);
            scroller.setBorder(null);
            if (setSize) {
                if (list.size() == 1) {
                    scroller.setMinimumSize(new Dimension(100, 30));
                    scroller.setPreferredSize(new Dimension(170, 30));
                } else {
                    scroller.setMinimumSize(new Dimension(100, 40));
                    scroller.setPreferredSize(new Dimension(170, 40));
                }
            }
            scroller.getVerticalScrollBar().setUnitIncrement(10);
            scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            return scroller;
        } catch (Exception e) {
            SwingTools.showException(e);
            e.printStackTrace();
        }
        return new JScrollPane();
    }

    public static JPanel createCharacterLinksPanel(Scene scene) {
        try {
            JPanel panel = new JPanel(new MigLayout("insets 0"));
            panel.setName("characterlinkspanel");
            panel.setMaximumSize(new Dimension(170, 50));
            panel.setOpaque(false);
            List<SceneLinkSbCharacter> list = SceneLinkSbCharacterPeer.doSelectBySceneId(scene.getId());
            for (SceneLinkSbCharacter link : list) {
                SbCharacter character = link.getCharacter();
                Color color = character.getColor();
                CleverLabel label = new CleverLabel(character.getAbbreviation());
                label.setToolTipText(character.getInfo(scene));
                if (color != null) {
                    label.setBackground(color);
                } else {
                    label.setOpaque(false);
                }
                panel.add(label, "gap 0");
            }
            panel.putClientProperty(PROPERTY_CHARACTER_LINK_LIST, list);
            return panel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JPanel();
    }

    public static JPanel createStrandLinksPanel(Scene scene, boolean opaque) {
        try {
            JPanel panel = new JPanel(new MigLayout("insets 2"));
            panel.setName("strandlinkspanel");
            if (opaque) {
                panel.setOpaque(true);
                panel.setBackground(scene.getStrand().getColor());
            }
            List<SceneLinkStrand> list = SceneLinkStrandPeer.doSelectBySceneId(scene.getId());
            for (SceneLinkStrand link : list) {
                Strand strand = link.getStrand();
                CleverLabel label = new CleverLabel(link.getStrand().getAbbreviation(), JLabel.CENTER);
                label.setToolTipText(strand.getInfo());
                label.setBackground(strand.getColor());
                panel.add(label, "w 30");
            }
            return panel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JPanel();
    }
}
