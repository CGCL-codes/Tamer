package com.agilejava.blammo.mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;

/**
 * A JavaDoc parser that will construct a set of abstraction descriptions of the
 * Blammo loggers found in the sources.
 * 
 * @author Wilfred Springer
 */
public class BlammoParser {

    private int messageOffset = 0;

    private String messageIdPrefix;

    private int messageIndex;

    public static final String TAG_LOGGER = "blammo.logger";

    public static final String TAG_MESSAGE = "blammo.message";

    public static final String TAG_LEVEL = "blammo.level";

    public static final String TAG_ID = "blammo.id";

    public BlammoParser() {
        this(null);
    }

    public BlammoParser(String messageIdPrefix) {
        super();
        this.messageIdPrefix = messageIdPrefix;
    }

    public BlammoParser(String messageIdPrefix, int messageOffset) {
        this(messageIdPrefix);
        this.messageOffset = messageOffset;
    }

    public List parse(File javaSourcesDir) throws BlammoParserException {
        messageIndex = messageOffset;
        ArrayList loggers = new ArrayList();
        JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSourceTree(javaSourcesDir);
        JavaClass[] javaClasses = builder.getClasses();
        for (int i = 0; i < javaClasses.length; i++) {
            JavaClass javaClass = javaClasses[i];
            if (javaClass.isInterface()) {
                if (javaClass.getTagByName(TAG_LOGGER) != null) {
                    Logger logger = new Logger();
                    logger.setJavaClass(javaClass);
                    extractEvents(logger, javaClass);
                    loggers.add(logger);
                }
            }
        }
        return loggers;
    }

    private void extractEvents(Logger logger, JavaClass javaClass) throws BlammoParserException {
        JavaMethod[] methods = javaClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            JavaMethod method = methods[i];
            LogEvent event = new LogEvent();
            event.setJavaMethod(method);
            event.setComments(method.getComment());
            DocletTag tag = method.getTagByName(TAG_MESSAGE);
            if (tag == null) {
                throw new BlammoParserException("Method " + method.getName() + " is missing " + TAG_MESSAGE + " annotation.", method.getLineNumber(), method.getSource().getURL());
            } else {
                try {
                    event.parseMessage(tag.getValue());
                } catch (LogMessageFormatException lfe) {
                    throw new BlammoParserException("Wrong message for " + event.getJavaMethod().getName() + ".", tag.getLineNumber(), method.getSource().getURL());
                }
            }
            tag = method.getTagByName(TAG_LEVEL);
            if (tag != null) {
                event.setLevel(tag.getValue().trim());
            } else {
                event.setLevel("error");
            }
            tag = method.getTagByName(TAG_ID);
            if (tag != null) {
                event.setIdentifier(tag.getValue().trim());
            } else {
                event.setIdentifier(generateId());
            }
            logger.addLogEvent(event);
        }
    }

    public String generateId() {
        StringBuffer buffer = new StringBuffer();
        if (messageIdPrefix != null) {
            buffer.append(messageIdPrefix);
        }
        buffer.append(StringUtils.repeat("0", 8 - messageIdPrefix.length() - Integer.toString(messageIndex).length()));
        buffer.append(messageIndex++);
        return buffer.toString();
    }
}
