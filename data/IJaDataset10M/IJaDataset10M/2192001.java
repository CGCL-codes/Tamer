package idlcompiler.topicconfigcreators;

import idlcompiler.files.Project;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import ops.StaticManager;
import ops.Topic;
import org.xml.sax.SAXException;
import parsing.TopicInfo;
import util.FileHelper;

/**
 *
 * @author angr
 */
public class CppTopicConfigCreator {

    private String outfilePath;

    private static String CLASSNAME_TEXT = "__className";

    private static String PACKAGENAME_TEXT = "__packageName";

    private static String REPLYTOPIC_TEXT = "__replyTopic";

    private static String REQUESTTOPIC_TEXT = "__requestTopic";

    private static String REPLYTYPE_TEXT = "__replyType";

    private static String REQUESTTYPE_TEXT = "__requestType";

    private static String REQUESTTYPE_NOPACKAGE_TEXT = "__requestNoPackage";

    private static String REPLYTYPE_NOPACKAGE_TEXT = "__replyNoPackage";

    private static String TOPICCONFIGCLASS_TEXT = "__topicConfigClass";

    /** Creates a new instance of CppTopicConfigCreator */
    public CppTopicConfigCreator(Project project, String packageString) throws SAXException, FileNotFoundException, IOException {
        String configFileString = "file:///" + project.getRunDirectory().replace("\\", "/") + "/" + project.getTopicConfigFile().getFile().getName();
        XMLConfigLoader configLoader = new XMLConfigLoader(configFileString);
        generateTopicConfigClass(project, packageString, configLoader);
        generateRequestReplyClasses(project, packageString, configLoader);
    }

    private void generateRequestReplyClasses(Project project, String packageString, XMLConfigLoader configLoader) throws FileNotFoundException, IOException {
        for (RequestReplyInfo reqRepInfo : configLoader.getRequestReplyInfos()) {
            String fileText = FileHelper.getTextFileText("cpprequestreplytemplate.tpl");
            fileText = fileText.replace(CLASSNAME_TEXT, reqRepInfo.className);
            fileText = fileText.replace(PACKAGENAME_TEXT, packageString);
            fileText = fileText.replace(REPLYTOPIC_TEXT, reqRepInfo.replyTopic);
            fileText = fileText.replace(REQUESTTOPIC_TEXT, reqRepInfo.requestTopic);
            String replyType = configLoader.getTypeForTopic(reqRepInfo.replyTopic);
            fileText = fileText.replace(REPLYTYPE_TEXT, replyType.replace(".", "::"));
            String requestType = configLoader.getTypeForTopic(reqRepInfo.requestTopic);
            fileText = fileText.replace(REQUESTTYPE_TEXT, requestType.replace(".", "::"));
            String requestTypeNoPackage = FileHelper.getExtension(requestType);
            fileText = fileText.replace(REQUESTTYPE_NOPACKAGE_TEXT, requestTypeNoPackage);
            String replyTypeNoPackage = FileHelper.getExtension(replyType);
            fileText = fileText.replace(REPLYTYPE_NOPACKAGE_TEXT, replyTypeNoPackage);
            String topicConfigClass = project.getName() + "TopicConfig";
            fileText = fileText.replace(TOPICCONFIGCLASS_TEXT, topicConfigClass);
            String outdirPath = FileHelper.unixSlashed(project.getRunDirectory()) + "/" + project.getRelativeOutputPath() + "/" + "C++/" + packageString.replace(".", "/") + "/";
            new File(outdirPath).mkdirs();
            outfilePath = outdirPath + "/" + reqRepInfo.className + ".h";
            FileHelper.createAndWriteFile(outfilePath, fileText);
        }
    }

    private void generateTopicConfigClass(Project project, String packageString, XMLConfigLoader configLoader) {
        String className = project.getName().replace(".prj", "") + "TopicConfig";
        String output = "";
        output += "#ifndef " + className + "H\n";
        output += "#define " + className + "H\n\n";
        output += "#include \"ops.h\"\n";
        for (TopicInfo t : configLoader.getTopics()) {
            String includeString = t.type.replace(".", "/") + ".h";
            output += "#include \"" + includeString + "\"\n";
        }
        output += "namespace " + packageString + " \n{\n";
        output += "class " + className + "\n";
        output += "{\n";
        output += "\tprivate:\n\tstd::string domainAddress;\n";
        output += "\tpublic:\n\t" + className + "(std::string domainAddress)\n";
        output += "\t{\n";
        output += "\t\tthis->domainAddress = domainAddress;\n";
        output += "\t}\n";
        for (TopicInfo t : configLoader.getTopics()) {
            output += "\tops::Topic<" + t.type.replace(".", "::") + "> get" + t.name + "()\n";
            output += "\t{\n";
            output += "\t\treturn ops::Topic<" + t.type.replace(".", "::") + ">(\"" + t.name + "\", " + t.port + ", \"" + t.type + "\", this->domainAddress);\n";
            output += "\t}\n";
        }
        output += "};\n";
        output += "}\n";
        output += "#endif\n";
        try {
            String outdirPath = FileHelper.unixSlashed(project.getRunDirectory()) + "/" + project.getRelativeOutputPath() + "/" + "C++/" + packageString.replace(".", "/") + "/";
            new File(outdirPath).mkdirs();
            outfilePath = outdirPath + "/" + FileHelper.cropExtension(project.getName()) + "TopicConfig.h";
            idlcompiler.compilers.Compiler.createAndWriteFile(getOutfilePath(), output);
            outfilePath = "\"" + outfilePath + "\"";
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String getOutfilePath() {
        return outfilePath;
    }
}
