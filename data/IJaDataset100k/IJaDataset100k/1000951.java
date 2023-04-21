package org.rascalli.mbe.adaptiveMusicCompanion;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.rascalli.framework.chatbotservice.ChatBot;
import org.rascalli.framework.concurrent.Executor;
import org.rascalli.framework.config.ConfigService;
import org.rascalli.framework.core.Agent;
import org.rascalli.framework.core.AgentConfiguration;
import org.rascalli.framework.core.AgentFactory;
import org.rascalli.framework.core.AgentManager;
import org.rascalli.framework.core.User;
import org.rascalli.framework.rss.RssManager;
import org.rascalli.mbe.Effector;
import org.rascalli.mbe.InputProcessor;
import org.rascalli.mbe.MBEAgentImpl;
import org.rascalli.mbe.adaptivemind.AdaptiveMind;
import org.rascalli.mbe.adc.AffectiveDialogueComponent;
import org.rascalli.mbe.chatbot.ChatBotTool;
import org.rascalli.mbe.mmg.MMGTool;
import org.rascalli.webui.ws.RascalliWSImpl;

/**
 * <p>
 * 
 * </p>
 * 
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * 
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * </p>
 * 
 * <p>
 * <b>last modified:</b><br/> $Author: christian $<br/> $Date: 2008-04-25
 * 13:19:41 +0200 (Fr, 25 Apr 2008) $<br/> $Revision: 2452 $
 * </p>
 * 
 * @author Christian Schollum
 */
@Component(immediate = true, architecture = true, public_factory = false)
@Provides
public class AdaptiveMusicCompanionFactory implements AgentFactory {

    @Requires
    private RascalliWSImpl rascalliWS;

    @Requires
    private Executor executor;

    @Requires
    private ConfigService configService;

    @Requires
    private RssManager rssManager;

    @Requires
    private AgentManager agentManager;

    @Requires
    private ChatBot chatBot;

    @Requires(filter = "(type=T-IPAM)")
    private InputProcessor ipTool;

    @Requires(filter = "(type=T-QA)")
    private Effector qaTool;

    @Requires(filter = "(type=T-NALQI)")
    private Effector nalqiTool;

    @Requires(filter = "(type=T-QuestionAnalysis)")
    private Effector questionAnalysisTool;

    @Requires(filter = "(type=T-TextRelevance)")
    private Effector textRelevanceTool;

    public Agent createAgent(User user, AgentConfiguration spec) {
        MBEAgentImpl agent = new MBEAgentImpl(user, spec, new AdaptiveMind(), ipTool, executor, configService, rascalliWS, rssManager, agentManager, getId());
        agent.enableJabber();
        agent.addComponent(qaTool);
        agent.addComponent(nalqiTool);
        agent.addComponent(questionAnalysisTool);
        agent.addComponent(textRelevanceTool);
        agent.addComponent(new ChatBotTool(chatBot));
        final MMGTool mmgTool = new MMGTool();
        agent.addComponent(mmgTool);
        mmgTool.setTemplateFile("templates/master.vm");
        agent.addComponent(new AffectiveDialogueComponent());
        return agent;
    }

    public String getDisplayName() {
        return "Adaptive Music Companion";
    }

    public String getId() {
        return "org.rascalli.mbe.adaptiveMusicCompanion.AdaptiveMusicCompanion-1.1";
    }
}
