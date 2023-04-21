package org.rollinitiative.d20web.charactersheet.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rollinitiative.d20.entity.CharacterBridge;
import org.rollinitiative.d20.entity.CharacterFactory;
import org.rollinitiative.d20.entity.Player;
import org.rollinitiative.d20.entity.classes.ClassContentBridge;
import org.rollinitiative.d20.entity.races.RaceContentBridge;
import org.rollinitiative.d20.entity.talents.TalentContentBridge;
import org.rollinitiative.d20.items.ItemContentBridge;
import org.rollinitiative.d20web.charactersheet.client.CharacterData;
import org.rollinitiative.d20web.charactersheet.client.CharacterService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * TODO Support Player query for character list based upon playerName and campaign
 * 
 * TODO Support DM query for character list based on campaign
 * 
 * @author bebopjmm
 */
@SuppressWarnings("serial")
public class CharacterServiceImpl extends RemoteServiceServlet implements CharacterService {

    private static final Log LOG = LogFactory.getLog(CharacterService.class);

    XMLConfiguration d20Config;

    String campaignID;

    CharacterBridge characterBridge;

    RaceContentBridge raceBridge;

    ClassContentBridge classBridge;

    TalentContentBridge talentBridge;

    ItemContentBridge itemBridge;

    CharacterDataFactory factory;

    CharacterFactory characterFactory;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.err.println("initializing...");
        try {
            d20Config = new XMLConfiguration("d20.config.xml");
            campaignID = d20Config.getString("defaultCampaign");
        } catch (ConfigurationException configEx) {
            LOG.fatal("FAILED to load configuration file: d20.config.xml");
        }
        raceBridge = new RaceContentBridge(d20Config);
        raceBridge.loadCollection("arena");
        classBridge = new ClassContentBridge(d20Config);
        classBridge.loadCollection("arena");
        characterBridge = new CharacterBridge(d20Config);
        characterBridge.loadCollection("arena", "characters");
        talentBridge = new TalentContentBridge(d20Config);
        talentBridge.loadCollection("arena");
        itemBridge = new ItemContentBridge(d20Config);
        itemBridge.loadCollection("arena");
        characterFactory = new CharacterFactory();
        characterFactory.setClassContentBridge(classBridge);
        characterFactory.setRaceContentBridge(raceBridge);
        characterFactory.setTalentBridge(talentBridge);
        characterFactory.setItemBridge(itemBridge);
        characterBridge.setCharacterFactory(characterFactory);
        factory = new CharacterDataFactory();
    }

    public CharacterData getCharacter(String name) {
        LOG.info("Loading character: " + name);
        return factory.buildCharacterData(characterBridge.getPC(name));
    }

    public String[] getParty(String partyTag) {
        LOG.info("Retrieving characters for party: " + partyTag);
        characterBridge.loadCollection(campaignID, "party");
        Player characters[];
        try {
            characters = characterBridge.getGroupPCs("party");
            LOG.info("total number of characters in party = " + characters.length);
            String characterNames[] = new String[characters.length];
            for (int i = 0; i < characters.length; i++) {
                characterNames[i] = characters[i].getName();
                LOG.info("-- " + characterNames[i]);
            }
            return characterNames;
        } catch (Exception e) {
            LOG.error("Failed to load the party under name: " + partyTag, e);
            return new String[0];
        }
    }
}
