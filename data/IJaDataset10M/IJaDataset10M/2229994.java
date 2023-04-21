package org.alicebot.server.core.targeting;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import org.alicebot.server.core.Globals;
import org.alicebot.server.core.util.Trace;
import org.alicebot.server.core.util.Toolkit;
import org.alicebot.server.core.util.XMLResourceSpec;
import org.alicebot.server.core.util.XMLWriter;

/**
 *  Handles writing of targets to files.
 *
 *  @author Noel Bush
 */
public class TargetWriter extends Targeting {

    /**
     *  Writes out a set of targets to a given file,
     *  deleting the file first.
     *
     *  @param file the File to which to write
     */
    public static void rewriteTargets(HashMap targets, File file) {
        if (!file.canWrite()) {
            Trace.userinfo("Cannot write targets.");
            return;
        }
        file.delete();
        XMLResourceSpec spec = new XMLResourceSpec();
        spec.description = "Targeting Data";
        spec.path = file.getAbsolutePath();
        spec.root = TARGETS;
        spec.dtd = XMLResourceSpec.HTML_ENTITIES_DTD;
        spec.encoding = Globals.getProperty("programd.targeting.data.encoding", "UTF-8");
        Iterator targetsIterator = targets.values().iterator();
        if (targetsIterator.hasNext()) {
            while (targetsIterator.hasNext()) {
                write((Target) targetsIterator.next(), spec);
            }
        } else {
            write(null, spec);
        }
    }

    /**
     *  Writes a target to a file defined by a given typespec.
     *
     *  @param target   the target to write
     *  @param spec     the XML resource spec for the target file
     */
    public static void write(Target target, XMLResourceSpec spec) {
        if (target != null) {
            XMLWriter.write(INDENT + TARGET_START + LINE_SEPARATOR + INDENT + INDENT + INPUT_START + LINE_SEPARATOR + INDENT + INDENT + INDENT + PATTERN_START + target.getLastInputText() + PATTERN_END + LINE_SEPARATOR + INDENT + INDENT + INDENT + THAT_START + target.getLastInputThat() + THAT_END + LINE_SEPARATOR + INDENT + INDENT + INDENT + TOPIC_START + target.getLastInputTopic() + TOPIC_END + LINE_SEPARATOR + INDENT + INDENT + INPUT_END + LINE_SEPARATOR + INDENT + INDENT + MATCH_START + LINE_SEPARATOR + INDENT + INDENT + INDENT + PATTERN_START + target.getMatchPattern() + PATTERN_END + LINE_SEPARATOR + INDENT + INDENT + INDENT + THAT_START + target.getMatchThat() + THAT_END + LINE_SEPARATOR + INDENT + INDENT + INDENT + TOPIC_START + target.getMatchTopic() + TOPIC_END + LINE_SEPARATOR + INDENT + INDENT + INDENT + TEMPLATE_START + target.getMatchTemplate() + TEMPLATE_END + LINE_SEPARATOR + INDENT + INDENT + MATCH_END + LINE_SEPARATOR + INDENT + INDENT + EXTENSION_START + LINE_SEPARATOR + INDENT + INDENT + INDENT + PATTERN_START + target.getExtensionPattern() + PATTERN_END + LINE_SEPARATOR + INDENT + INDENT + INDENT + THAT_START + target.getExtensionThat() + THAT_END + LINE_SEPARATOR + INDENT + INDENT + INDENT + TOPIC_START + target.getExtensionTopic() + TOPIC_END + LINE_SEPARATOR + INDENT + INDENT + EXTENSION_END + LINE_SEPARATOR + INDENT + TARGET_END + LINE_SEPARATOR, spec);
        } else {
            XMLWriter.write(EMPTY_STRING, spec);
        }
    }
}
