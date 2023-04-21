package org.metatemplate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class BaseReceptacle {

    public void publish(Content content) {
        if (receptacleLog.isDebugEnabled()) {
            receptacleLog.debug("Publish:" + (content != null ? (content.getClass() + ":" + content) : "NULL"));
        }
        if (content == null) {
            return;
        }
        if (content instanceof UnmarkedContent) {
            content = transform((UnmarkedContent) content);
        }
        if (content instanceof TextContent) {
            if (content instanceof RuntimeContent) {
                if (((RuntimeContent) content).content != null) {
                    for (Content c : ((RuntimeContent) content).content) {
                        publish(c);
                    }
                    ((TextContent) content).text = "";
                } else {
                    ((TextContent) content).text = escape(((TextContent) content).text);
                }
            }
            publishText(content.type, ((TextContent) content).text);
        } else if (!(content instanceof NoContent)) {
            System.err.println("Dont know how to publish " + content + ".TYPE:" + content.getClass());
        }
    }

    protected abstract void publish(String text);

    protected Content transform(UnmarkedContent content) {
        return new RuntimeLiteralContent(content.text);
    }

    protected String escape(String text) {
        return text;
    }

    protected void publishText(Content.Type type, String text) {
        String prefix = getPrefix(type);
        String suffix = getSuffix(type);
        publish((prefix != null ? prefix : "") + text + (suffix != null ? suffix : ""));
    }

    protected String getPrefix(Content.Type type) {
        return null;
    }

    protected String getSuffix(Content.Type type) {
        return null;
    }

    private static final Log receptacleLog = LogFactory.getLog("metatemplate.receptacle");
}
