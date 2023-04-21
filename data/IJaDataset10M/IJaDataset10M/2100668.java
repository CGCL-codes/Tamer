package apollo.gui.synteny;

import java.lang.reflect.*;
import java.util.*;
import apollo.config.Config;
import apollo.datamodel.*;
import apollo.dataadapter.ApolloDataAdapterI;
import apollo.gui.*;
import apollo.dataadapter.DataLoadEvent;
import apollo.gui.evidencepanel.EvidencePanel;
import apollo.gui.evidencepanel.EvidencePanelOrientationManager;
import apollo.gui.genomemap.*;
import apollo.gui.menus.*;
import apollo.main.LoadUtil;
import apollo.main.DataLoader;
import javax.swing.*;
import java.awt.*;
import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;
import org.simplericity.macify.eawt.DefaultApplication;
import misc.JIniFile;

/**
 * <p>The synteny panels need to display many stranded-zoomable-apollo-panels at once,
 * (unlike the standard apollo frame). However apollo's menus are very aware of
 * apollo frames...this subclass is an attempt to circumvent these issues without
 * having to rewrite lots of the apollo menu-handling code to support synteny
 * viewing.</p>
 *
 * <p>The idea is: to hold many copies of the instance variables that an Apollo frame
 * usually holds (one for each browsed species). 
 * When the user their switches between different species, each copy of 
 * these instance variables is, in turn, switched over to the set specific to that species.
 * Menu items (and anything else which explicitly needs to see the frame) should be 
 * oblivious to the change.</p>
 *
 * <p> Of course, we override the drawing/initialisation code to set up multiple 
 * stranded-zoomable-apollo-panels, controllers etc. </p>
 * <P> Warning this code is scary
 * <p> 1. It is obscure </p>
 * <p> 2. It relies upon the hash keys to be exact string matches to the 
 * names of variables in ApolloFrame makes this extremely vulnerable
 * to bugs cropping up because of name changes to the variables or
 * other changes in initialization. It completely depends upon the
 * exposure of the object internals.
 * It strikes me that this is really not an object that extends
 * the basic ApolloFrame, but really one that manages ApolloFrames.
 * <p> NEEDS TO BE REFACTORED! </p>
 * 
 * Should CompositeApolloFrame be merged with ApolloFrame now that CAF is used
 for single and multi species?
 
**/
public class CompositeApolloFrame extends ApolloFrame {

    private ApolloDataAdapterI compositeAdapter;

    private HashMap linkPanels = new HashMap();

    private Color normalLabelColor = null;

    private Color selectedLabelColor = new Color(0, 153, 0);

    private static CompositeApolloFrame compositeApolloFrameSingleton;

    public static CompositeApolloFrame getApolloFrame() {
        if (compositeApolloFrameSingleton == null) {
            Application app = new DefaultApplication();
            compositeApolloFrameSingleton = new CompositeApolloFrame();
            app.addApplicationListener(new MacApplicationSupport());
        }
        return compositeApolloFrameSingleton;
    }

    /** should singleton method be in CAF? */
    private CompositeApolloFrame() {
    }

    /** loadData gets called with every data load(see LoadUtil). This is where the
      data gets loaded (previously called init()) */
    public void loadData(ApolloDataAdapterI dataAdapter, CompositeDataHolder cdh) {
        getCurationManager().setDataAdapter(dataAdapter);
        getCurationManager().setCompositeDataHolder(cdh);
        initialiseApolloPanels(cdh);
        setTitle();
        getCurationManager().selectInputFeatures();
        validate();
    }

    /** get title for frame -> CurationManager? */
    private void setTitle() {
        String title = "";
        if (getCurationManager().isMultiCuration()) {
            title = "Synteny: ";
        } else {
            title = getCompositeDataHolder().getCurationSet(0).getName();
        }
        for (int i = 0; i < getCompositeDataHolder().numberOfSpecies(); i++) {
            CurationSet currentSet = getCompositeDataHolder().getCurationSet(i);
            if (currentSet.getOrganism() != null && !(currentSet.getOrganism().equals(""))) {
                if (i > 0) title += " /";
                title += " " + currentSet.getOrganism();
            }
        }
        setTitle(title);
    }

    /** called with every load - puts all into existing syntenyPanel. shoould only do 
      this if # of curSets change. creates new syntenyLinkPanels (should be cached in 
      CurManager) and sets its link data. setting of link data should be done in
      a different method so only gui stuff here for curSet # change */
    private void initialiseApolloPanels(CompositeDataHolder compositeDataHolder) {
        StrandedZoomableApolloPanel firstPanel;
        StrandedZoomableApolloPanel secondPanel;
        SyntenyLinkPanel currentLinkPanel;
        Vector orderedSingleSpeciesPanels = new Vector();
        Vector orderedLinkPanels = new Vector();
        for (int i = 0; i < compositeDataHolder.speciesCompSize(); i++) {
            SpeciesComparison specComp = compositeDataHolder.getSpeciesComparison(i);
            firstPanel = getCurationState(i).getSZAP();
            if (orderedSingleSpeciesPanels.isEmpty()) orderedSingleSpeciesPanels.addElement(firstPanel);
            if (specComp.hasSecondSpecies()) {
                secondPanel = getCurationState(i + 1).getSZAP();
                orderedSingleSpeciesPanels.addElement(secondPanel);
                if (specComp.hasLinkSet()) {
                    LinkSet currentLinkSet = compositeDataHolder.getLinkSet(i);
                    if (getLinkPanels().containsKey(currentLinkSet.getName())) {
                        currentLinkPanel = (SyntenyLinkPanel) getLinkPanels().get(currentLinkSet.getName());
                    } else {
                        currentLinkPanel = new SyntenyLinkPanel(firstPanel, secondPanel);
                        getLinkPanels().put(currentLinkSet.getName(), currentLinkPanel);
                        currentLinkPanel.setMinimumSize(new Dimension(0, 0));
                    }
                    currentLinkPanel.setLinks(currentLinkSet);
                    firstPanel.getController().addListener(currentLinkPanel);
                    secondPanel.getController().addListener(currentLinkPanel);
                    currentLinkPanel.setStatusBar(getCurationManager().getStatusBar());
                    orderedLinkPanels.addElement(currentLinkPanel);
                    Config.getStyle().getPropertyScheme().addPropSchemeChangeListener(firstPanel.getController());
                    Config.getStyle().getPropertyScheme().addPropSchemeChangeListener(secondPanel.getController());
                }
            }
        }
        if (getSyntenyPanel() == null) {
            setSyntenyPanel(new SyntenyPanel(orderedSingleSpeciesPanels, orderedLinkPanels, this));
        } else {
            getSyntenyPanel().setPanels(orderedSingleSpeciesPanels, orderedLinkPanels);
        }
        getCurationManager().addListenerToAllCurations(getSyntenyPanel());
    }

    private HashMap getLinkPanels() {
        return linkPanels;
    }

    private Color getNormalLabelColor() {
        return normalLabelColor;
    }

    private void setNormalLabelColor(Color color) {
        normalLabelColor = color;
    }

    private Color getSelectedLabelColor() {
        return selectedLabelColor;
    }

    private void setSelectedLabelColor(Color color) {
        selectedLabelColor = color;
    }

    /**
   * Propagate the setVisible call to all stranded-zoomable panels: their
   * setVisible call is quite involved...
  **/
    public void setVisible(boolean state) {
        super.setVisible(state);
    }

    public void setUseOpaqueLinks(boolean value) {
        getSyntenyPanel().setUseOpaqueLinks(value);
    }

    public void setShadeByPercId(boolean value) {
        getSyntenyPanel().setShadeByPercId(value);
    }

    /** Load new species and links for the link passed in 
       This needs to be moved to LoadUtil/DataLoader */
    public void loadSyntenyLink(SeqFeatureI link) {
        compositeAdapter = CurationManager.getCurationManager().getDataAdapter();
        try {
            compositeAdapter.loadNewSpeciesFromLink(link, getCompositeDataHolder());
        } catch (org.bdgp.io.DataAdapterException e) {
            System.out.println("data adapter exception - do something...");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        loadData(compositeAdapter, getCompositeDataHolder());
    }

    public void setLockScrolling(boolean value) {
        getSyntenyPanel().setLockScrolling(value);
    }

    public void setShiftZoomLock(boolean state) {
        getSyntenyPanel().setShiftLockZoom(state);
    }

    public static class MacApplicationSupport implements ApplicationListener {

        public void handleAbout(ApplicationEvent e) {
            new HelpMenu(null).showAboutBox();
            e.setHandled(true);
        }

        public void handleQuit(ApplicationEvent e) {
            if (LoadUtil.confirmSaved(FileMenu.quit_options, new DataLoader())) {
                System.exit(0);
            }
        }

        public void handleOpenApplication(ApplicationEvent e) {
        }

        public void handleOpenFile(ApplicationEvent e) {
        }

        public void handlePreferences(ApplicationEvent e) {
        }

        public void handlePrintFile(ApplicationEvent e) {
        }

        public void handleReopenApplication(ApplicationEvent e) {
        }
    }
}
