package ops.netbeansmodules.idlsupport.compilers;

import ops.netbeansmodules.idlsupport.godegenerator.OPSConfigCompiler;
import java.io.IOException;
import java.util.ArrayList;
import ops.Domain;
import ops.OPSConfig;
import ops.Topic;
import ops.netbeansmodules.idlsupport.godegenerator.CompilerHelper;
import static ops.netbeansmodules.idlsupport.godegenerator.CompilerHelper.*;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Anton Gravestam
 */
@ServiceProvider(service = OPSConfigCompiler.class)
public class JavaOPSConfigCompiler implements OPSConfigCompiler {

    private String JAVA_DIR = "Java";

    private String GETTERS_REGEX = "__topicGetters";

    private String CREATE_PARTICIPANT_REGEX = "__createParticipant";

    public ArrayList<String> compileConfig(OPSConfig config, String projectDirectory) {
        ArrayList<String> generatedFiles = new ArrayList<String>();
        try {
            for (Domain domain : config.getDomains()) {
                String projectName = extractProjectName(projectDirectory);
                CompilerHelper ch = new CompilerHelper();
                String className = domain.getDomainID() + "TopicConfig";
                ch.setOutputFileName(projectDirectory + JAVA_DIR + "/" + projectName + "/" + className + ".java");
                String resource = "/ops/netbeansmodules/idlsupport/templates/javaconfig.tpl";
                ch.setTemplateTextFromResource(resource);
                String templateText = ch.getTemplateText();
                templateText = templateText.replace(CREATE_PARTICIPANT_REGEX, getCreateParticipant(domain));
                templateText = templateText.replace(CompilerHelper.PACKAGE_NAME_REGEX, projectName);
                templateText = templateText.replace(CompilerHelper.CLASS_NAME_REGEX, className);
                templateText = templateText.replace(GETTERS_REGEX, getTopicGetters(domain));
                ch.saveOutputText(templateText);
                System.out.println(templateText);
                generatedFiles.add(ch.getOutputFileName());
            }
            return generatedFiles;
        } catch (IOException ex) {
            ex.printStackTrace();
            Exceptions.printStackTrace(ex);
            return generatedFiles;
        }
    }

    private String getCreateParticipant(Domain domain) {
        String ret = "";
        ret += tab(2) + "Domain domain = new Domain();" + endl;
        ret += tab(2) + "domain.setDomainID(\"" + domain.getDomainID() + "\");" + endl;
        ret += tab(2) + "domain.setDomainAddress(\"" + domain.getDomainAddress() + "\");" + endl;
        ret += tab(2) + "domain.setLocalInterface(\"" + domain.getLocalInterface() + "\");" + endl;
        ret += tab(2) + "Topic topic = null;" + endl;
        for (Topic topic : domain.getTopics()) {
            ret += tab(2) + "topic = new Topic();" + endl;
            ret += tab(2) + "topic.setTypeID(\"" + topic.getTypeID() + "\");" + endl;
            ret += tab(2) + "topic.setName(\"" + topic.getName() + "\");" + endl;
            ret += tab(2) + "topic.setPort(" + topic.getPort() + ");" + endl;
            ret += tab(2) + "topic.setDomainAddress(domainAddress);" + endl;
            ret += tab(2) + "topic.setDomainID(domain.getDomainID());" + endl;
            ret += tab(2) + "topic.setTransport(\"" + topic.getTransport() + "\");" + endl;
            ret += tab(2) + "topic.setInSocketBufferSize(" + topic.getInSocketBufferSize() + ");" + endl;
            ret += tab(2) + "topic.setOutSocketBufferSize(" + topic.getOutSocketBufferSize() + ");" + endl;
            ret += tab(2) + "topic.setSampleMaxSize(" + topic.getSampleMaxSize() + ");" + endl;
            ret += tab(2) + "domain.getTopics().add(topic);" + endl;
            ret += tab(3) + endl;
        }
        ret += tab(2) + "participant = Participant.getInstance(domain, domain.getDomainID());" + endl;
        return ret;
    }

    private String getTopicGetters(Domain domain) {
        String topicGetters = "";
        for (Topic topic : domain.getTopics()) {
            topicGetters += tab(2) + "public Topic get" + topic.getName() + "()" + endl;
            topicGetters += tab(2) + "{" + endl;
            topicGetters += tab(3) + "return participant.createTopic(\"" + topic.getName() + "\");" + endl;
            topicGetters += tab(2) + "}" + endl;
            topicGetters += endl;
        }
        return topicGetters;
    }
}
