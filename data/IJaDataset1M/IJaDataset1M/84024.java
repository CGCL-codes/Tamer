package org.rascalli.mbe.adc;

import java.io.StringWriter;
import java.net.URL;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.rascalli.framework.config.ConfigService;
import org.rascalli.framework.core.CommunicationChannel;
import org.rascalli.mbe.AbstractTool;
import org.rascalli.mbe.MBEAgent;
import org.rascalli.mbe.RdfData;
import org.rascalli.mbe.ToolID;
import org.rascalli.mbe.ToolOutput;
import org.rascalli.mbe.UtteranceExtractor;

public class AffectiveDialogueComponent extends AbstractTool {

    private final String templateFileEca = "templates/affQuestions.vm";

    private final String templateFileJabber = "templates/affQuestionsJabber.vm";

    private final String maleVoice = "jmk-arctic";

    private final String femaleVoice = "slt-arctic";

    private VelocityContext context = new VelocityContext();

    private MBEAgent agent;

    private CommunicationChannel channel;

    private final Log log = LogFactory.getLog(getClass());

    private int sid;

    public AffectiveDialogueComponent() {
        super("T-AffD-1");
        context = new VelocityContext();
        sid = 1;
    }

    public void setConfigService(ConfigService configService) throws Exception {
        final URL templateDirUrl = configService.getConfigFileUrl("adc");
        Properties p = new Properties();
        p.setProperty("resource.loader", "file");
        p.setProperty("file.resource.loader.path", templateDirUrl.getPath());
        p.setProperty("velocimacro.permissions.allow.inline.to.replace.global", "true");
        p.setProperty("velocimacro.library", "templates/macros.vm");
        Velocity.init(p);
        context.put("map", new DataStore());
        context.put("random", new Random(System.currentTimeMillis()));
        context.put("maleVoice", maleVoice);
        context.put("femaleVoice", femaleVoice);
        context.put("sid", sid);
    }

    public String generateQuestion() throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, Exception {
        StringWriter output = new StringWriter();
        if (agent == null) {
            return null;
        }
        if (agent.userIsOnline(CommunicationChannel.ECA)) {
            channel = CommunicationChannel.ECA;
            Velocity.mergeTemplate(templateFileEca, "iso-8859-1", context, output);
        } else {
            channel = CommunicationChannel.JABBER;
            Velocity.mergeTemplate(templateFileJabber, "iso-8859-1", context, output);
        }
        sid++;
        return output.toString();
    }

    public MBEAgent getAgent() {
        return agent;
    }

    public void setMBEAgent(MBEAgent agent) {
        this.agent = agent;
        context.put("agent", agent);
        context.put("user", agent.getUser());
    }

    public AffectType classifyAnswer(RdfData data) {
        String answer = null;
        if (answer == null) {
            return AffectType.UNKNOWN;
        }
        data.toString();
        Pattern pattern = Pattern.compile(".*(<userUtterance>(.*)<\\/userUtterance>).*", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(data.toString());
        if (matcher.matches()) {
            answer = matcher.group(1);
        }
        return classifyAnswer(answer);
    }

    public AffectType classifyAnswer(String answer) {
        String[] pos = { "good", "ok", "nice", "fine", "alright", "happy", "great" };
        String[] neg = { "bad", "unhappy", "enraged", "mad", "nonsense", "not working", "wrong", "false", "untrue", "at all" };
        String posPatternString = "";
        for (String s : pos) {
            posPatternString += s + "|";
        }
        posPatternString = "(" + posPatternString.substring(0, posPatternString.length() - 1) + ")";
        Pattern posPattern = Pattern.compile(".*" + posPatternString + ".*", Pattern.DOTALL);
        String negPatternString = "";
        for (String s : neg) {
            negPatternString += s + "|";
        }
        negPatternString = "(" + negPatternString.substring(0, negPatternString.length() - 1) + ")";
        Pattern negPattern = Pattern.compile(".*" + negPatternString + ".*", Pattern.DOTALL);
        Pattern notPosPattern = Pattern.compile(".*not (so|that|really|quite)? ?" + posPatternString + ".*");
        Matcher matcher;
        matcher = notPosPattern.matcher(answer);
        if (matcher.matches()) {
            return AffectType.NEGATIVE;
        }
        matcher = posPattern.matcher(answer);
        if (matcher.matches()) {
            return AffectType.POSITIVE;
        }
        matcher = negPattern.matcher(answer);
        if (matcher.matches()) {
            return AffectType.NEGATIVE;
        }
        return AffectType.UNKNOWN;
    }

    public RdfData executeAction(RdfData data) throws Exception {
        String textValue = UtteranceExtractor.extractUtterance(data.getRepository()).getUtterance();
        if (textValue == null || textValue.equals("")) {
            String question = generateQuestion();
            sendQuestion(question);
            ToolOutput output = new ToolOutput(ToolID.ADC);
            return output;
        } else {
            AffectType affect = classifyAnswer(textValue);
            ToolOutput output = new ToolOutput(ToolID.ADC);
            output.toolHas("hasOutput", affect.toString());
            final org.openrdf.model.URI input = output.toolHas("hasInput", "String");
            output.hasTextValue(input, textValue);
            return output;
        }
    }

    private void sendQuestion(String bml) {
        try {
            if (channel.equals(CommunicationChannel.ECA)) {
                agent.sendMultimodalOutput(bml, CommunicationChannel.ECA);
            } else {
                agent.sendMultimodalOutput(bml, CommunicationChannel.JABBER);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e);
            } else {
                System.err.println("Error sending BML to user");
            }
        }
    }
}
