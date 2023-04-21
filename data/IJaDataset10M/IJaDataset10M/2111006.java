package com.google.gxp.compiler.msgextract;

import com.google.common.collect.ImmutableList;
import com.google.gxp.compiler.alerts.AlertSet;
import com.google.gxp.compiler.alerts.SourcePosition;
import com.google.gxp.compiler.base.ExtractedMessage;
import com.google.gxp.compiler.base.Root;
import com.google.gxp.compiler.base.Tree;
import java.util.List;

/**
 * The output of {@link MessageExtractor}.
 */
public class MessageExtractedTree extends Tree<Root> {

    private final ImmutableList<ExtractedMessage> messages;

    MessageExtractedTree(SourcePosition sourcePosition, AlertSet alerts, Root root, List<ExtractedMessage> messages) {
        super(sourcePosition, alerts, root);
        this.messages = ImmutableList.copyOf(messages);
    }

    public List<ExtractedMessage> getMessages() {
        return messages;
    }
}
