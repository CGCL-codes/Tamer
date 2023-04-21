package com.google.i18n.pseudolocalization.methods;

import com.google.i18n.pseudolocalization.PseudolocalizationMethod;
import com.google.i18n.pseudolocalization.PseudolocalizationPipeline;
import com.google.i18n.pseudolocalization.message.DefaultVisitor;
import com.google.i18n.pseudolocalization.message.Message;
import com.google.i18n.pseudolocalization.message.MessageFragmentVisitor;
import com.google.i18n.pseudolocalization.message.NonlocalizableTextFragment;
import com.google.i18n.pseudolocalization.message.VisitorContext;

/**
 * A {@link PseudolocalizationMethod} that adds brackets around the entire
 * message, to help identify where the application is concatenating separate
 * messages (which is bad because some locales might need to change the two
 * messages when concatenated, such as rearranging the order).  Generally, this
 * should be the last method applied.
 */
public class BracketAdder extends DefaultVisitor implements PseudolocalizationMethod {

    public static void register() {
        PseudolocalizationPipeline.registerMethodClass("brackets", BracketAdder.class);
    }

    @Override
    public void endMessage(VisitorContext ctx, Message message) {
        NonlocalizableTextFragment suffix = ctx.createNonlocalizableTextFragment("]");
        ctx.insertAfter(null, suffix);
    }

    @Override
    public MessageFragmentVisitor visitMessage(VisitorContext ctx, Message message) {
        NonlocalizableTextFragment prefix = ctx.createNonlocalizableTextFragment("[");
        ctx.insertBefore(null, prefix);
        return null;
    }
}
