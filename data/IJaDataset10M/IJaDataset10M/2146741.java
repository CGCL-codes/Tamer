package pcgen.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import pcgen.core.Campaign;
import pcgen.core.GameMode;
import pcgen.core.Globals;
import pcgen.core.SettingsHandler;
import pcgen.core.SystemCollections;
import pcgen.core.facade.CampaignFacade;
import pcgen.core.facade.util.DefaultListFacade;
import pcgen.core.facade.DefaultReferenceFacade;
import pcgen.core.facade.GameModeFacade;
import pcgen.core.facade.util.ListFacade;
import pcgen.core.facade.util.ListFacades;
import pcgen.core.facade.LoadableFacade.LoadingState;
import pcgen.core.facade.ReferenceFacade;
import pcgen.core.facade.SourceSelectionFacade;
import pcgen.util.Logging;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class FacadeFactory {

    private static PropertyContext sourcesContext = PCGenSettings.getInstance().createChildContext("customSources");

    private static DefaultListFacade<SourceSelectionFacade> quickSources = null;

    private static DefaultListFacade<CampaignFacade> campaigns = null;

    private static DefaultListFacade<GameModeFacade> gamemodes = null;

    private static DefaultListFacade<SourceSelectionFacade> displayedSources = null;

    private static DefaultListFacade<SourceSelectionFacade> customSources;

    private static Map<String, CampaignFacade> campaignMap;

    private static Map<GameModeFacade, DefaultListFacade<CampaignFacade>> campaignListMap = null;

    static void initialize() {
        List<GameMode> modes = SystemCollections.getUnmodifiableGameModeList();
        List<Campaign> camps = Globals.getCampaignList();
        gamemodes = new DefaultListFacade<GameModeFacade>(modes);
        campaigns = new DefaultListFacade<CampaignFacade>(camps);
        quickSources = new DefaultListFacade<SourceSelectionFacade>();
        displayedSources = new DefaultListFacade<SourceSelectionFacade>();
        customSources = new DefaultListFacade<SourceSelectionFacade>();
        campaignMap = new HashMap<String, CampaignFacade>();
        campaignListMap = new HashMap<GameModeFacade, DefaultListFacade<CampaignFacade>>();
        initCampaigns();
        initGameModes(modes);
        initCustomSourceSelections();
        initDisplayedSources();
    }

    private static void initCampaigns() {
        for (final CampaignFacade campaign : campaigns) {
            campaignMap.put(campaign.getName(), campaign);
            ListFacade<GameModeFacade> gameModeList = campaign.getGameModes();
            for (GameModeFacade gameModeFacade : gameModeList) {
                if (!campaignListMap.containsKey(gameModeFacade)) {
                    campaignListMap.put(gameModeFacade, new DefaultListFacade<CampaignFacade>());
                }
                campaignListMap.get(gameModeFacade).addElement(campaign);
            }
            if (campaign.showInMenu()) {
                GameModeFacade game = gameModeList.getElementAt(0);
                ListFacade<CampaignFacade> list = new DefaultListFacade<CampaignFacade>(Collections.singleton(campaign));
                quickSources.addElement(new BasicSourceSelectionFacade(campaign.getName(), list, game));
            }
        }
    }

    private static void initGameModes(List<GameMode> modes) {
        for (GameMode mode : modes) {
            String title = mode.getDefaultSourceTitle();
            if (SettingsHandler.getGame().equals(mode) && title == null && !mode.getDefaultDataSetList().isEmpty()) {
                title = LanguageBundle.getFormattedString("in_qsrc_game_default", mode.getName());
            }
            if (title != null && !"".equals(title)) {
                DefaultListFacade<CampaignFacade> qcamps = new DefaultListFacade<CampaignFacade>();
                List<String> sources = mode.getDefaultDataSetList();
                for (String string : sources) {
                    Campaign camp = Globals.getCampaignKeyed(string);
                    qcamps.addElement(camp);
                }
                quickSources.addElement(new BasicSourceSelectionFacade(mode.getDefaultSourceTitle(), qcamps, mode));
            }
        }
    }

    private static void initDisplayedSources() {
        String[] hiddenElements = PCGenSettings.getInstance().getStringArray("hiddenSources", ArrayUtils.EMPTY_STRING_ARRAY);
        for (int i = 0; i < quickSources.getSize(); i++) {
            SourceSelectionFacade selection = quickSources.getElementAt(i);
            if (!ArrayUtils.contains(hiddenElements, selection.toString())) {
                displayedSources.addElement(selection);
            }
        }
    }

    private static void initCustomSourceSelections() {
        String[] keys = sourcesContext.getStringArray("selectionNames");
        if (keys == null) {
            return;
        }
        for (String name : keys) {
            PropertyContext context = sourcesContext.createChildContext(name);
            String modeName = context.getProperty("gamemode");
            GameMode mode = SystemCollections.getGameModeNamed(modeName);
            if (mode == null) {
                Logging.errorPrint("Unable to load quick source '" + name + "'. Game mode '" + modeName + "' is missing");
                continue;
            }
            String[] selectionArray = context.getStringArray("campaigns");
            List<CampaignFacade> sources = new ArrayList<CampaignFacade>();
            boolean error = false;
            for (String campaign : selectionArray) {
                CampaignFacade c = campaignMap.get(campaign);
                if (c != null) {
                    sources.add(c);
                } else {
                    error = true;
                    Logging.log(Logging.WARNING, "'" + campaign + "'" + " campaign not found, custom quick source '" + name + "' might not work correctly.");
                }
            }
            CustomSourceSelectionFacade selection = new CustomSourceSelectionFacade(name);
            selection.setGameMode(mode);
            selection.setCampaigns(sources);
            if (error) {
                selection.setLoadingState(LoadingState.LOADED_WITH_ERRORS);
                selection.setErrorMessage("Some campaigns are missing");
            }
            customSources.addElement(selection);
            quickSources.addElement(selection);
        }
    }

    public static SourceSelectionFacade createCustomSourceSelection(String name) {
        SourceSelectionFacade selection = new CustomSourceSelectionFacade(name);
        customSources.addElement(selection);
        quickSources.addElement(selection);
        displayedSources.addElement(selection);
        setCustomSourceSelectionArray();
        return selection;
    }

    public static void deleteCustomSourceSelection(SourceSelectionFacade source) {
        if (!(source instanceof CustomSourceSelectionFacade)) {
            throw new IllegalArgumentException();
        }
        customSources.removeElement(source);
        quickSources.removeElement(source);
        displayedSources.removeElement(source);
        PropertyContext context = sourcesContext.createChildContext(source.toString());
        context.removeProperty("gamemode");
        context.removeProperty("campaigns");
        setCustomSourceSelectionArray();
    }

    private static void setCustomSourceSelectionArray() {
        List<String> sources = new ArrayList<String>();
        for (SourceSelectionFacade csel : customSources) {
            sources.add(csel.toString());
        }
        sourcesContext.setStringArray("selectionNames", sources);
    }

    public static void setDisplayedSources(SourceSelectionFacade[] sources) {
        displayedSources.setContents(Arrays.asList(sources));
        ArrayList<String> hiddenElements = new ArrayList<String>();
        for (SourceSelectionFacade selection : quickSources) {
            if (!ArrayUtils.contains(sources, selection)) {
                hiddenElements.add(selection.toString());
            }
        }
        PCGenSettings.getInstance().setStringArray("hiddenSources", hiddenElements);
    }

    public static SourceSelectionFacade createSourceSelection(GameModeFacade gameMode, List<CampaignFacade> campaignList) {
        return new BasicSourceSelectionFacade(null, new DefaultListFacade(campaignList), gameMode);
    }

    public static ListFacade<SourceSelectionFacade> getDisplayedSourceSelections() {
        return displayedSources;
    }

    public static ListFacade<SourceSelectionFacade> getSourceSelections() {
        return quickSources;
    }

    public static ListFacade<SourceSelectionFacade> getCustomSourceSelections() {
        return customSources;
    }

    public static ListFacade<CampaignFacade> getCampaigns() {
        return campaigns;
    }

    public static ListFacade<GameModeFacade> getGameModes() {
        return gamemodes;
    }

    public static ListFacade<CampaignFacade> getSupportedCampaigns(GameModeFacade gameMode) {
        if (!campaignListMap.containsKey(gameMode)) {
            return ListFacades.emptyList();
        }
        return campaignListMap.get(gameMode);
    }

    private static class BasicSourceSelectionFacade implements SourceSelectionFacade {

        private final ListFacade<CampaignFacade> campaignModel;

        private final DefaultReferenceFacade<GameModeFacade> gameModeRef;

        private final String name;

        public BasicSourceSelectionFacade(String name, ListFacade<CampaignFacade> campaignModel, GameModeFacade gameMode) {
            this.name = name;
            this.campaignModel = campaignModel;
            gameModeRef = new DefaultReferenceFacade<GameModeFacade>(gameMode);
        }

        public void setCampaigns(List<CampaignFacade> campaign) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setGameMode(GameModeFacade gameMode) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isModifiable() {
            return false;
        }

        public LoadingState getLoadingState() {
            return LoadingState.LOADED;
        }

        public String getLoadingErrorMessage() {
            return null;
        }

        public String toString() {
            if (name != null) {
                return name;
            }
            if (gameModeRef != null && gameModeRef.getReference() != null) {
                return LanguageBundle.getFormattedString("in_source_gamemode", gameModeRef.getReference().toString());
            }
            return "";
        }

        public ListFacade<CampaignFacade> getCampaigns() {
            return campaignModel;
        }

        public ReferenceFacade<GameModeFacade> getGameMode() {
            return gameModeRef;
        }
    }

    private static class CustomSourceSelectionFacade implements SourceSelectionFacade {

        private PropertyContext context;

        private String name;

        private LoadingState loadingState = LoadingState.LOADED;

        private String errorMessage = null;

        public CustomSourceSelectionFacade(String name) {
            this.name = name;
            this.context = sourcesContext.createChildContext(name);
        }

        private DefaultListFacade<CampaignFacade> campaigns = new DefaultListFacade<CampaignFacade>();

        private DefaultReferenceFacade<GameModeFacade> gameModeRef = new DefaultReferenceFacade<GameModeFacade>();

        public boolean isModifiable() {
            return true;
        }

        public void setCampaigns(List<CampaignFacade> campaign) {
            campaigns.setContents(campaign);
            List<String> camps = new ArrayList<String>();
            for (CampaignFacade camp : campaign) {
                camps.add(camp.getName());
            }
            context.setStringArray("campaigns", camps);
        }

        public void setGameMode(GameModeFacade gameMode) {
            gameModeRef.setReference(gameMode);
            context.setProperty("gamemode", gameMode.getName());
        }

        @Override
        public String toString() {
            return name;
        }

        public LoadingState getLoadingState() {
            return loadingState;
        }

        public String getLoadingErrorMessage() {
            return errorMessage;
        }

        public void setLoadingState(LoadingState loadingState) {
            this.loadingState = loadingState;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public ListFacade<CampaignFacade> getCampaigns() {
            return campaigns;
        }

        public ReferenceFacade<GameModeFacade> getGameMode() {
            return gameModeRef;
        }
    }
}
