package net.sf.lunareclipse.internal.ui.text.completion;

import net.sf.lunareclipse.internal.ui.LuaUI;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.dltk.ui.text.completion.ScriptCompletionProposal;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Image;

public class LuaCompletionProposal extends ScriptCompletionProposal {

    public LuaCompletionProposal(String replacementString, int replacementOffset, int replacementLength, Image image, String displayString, int relevance) {
        super(replacementString, replacementOffset, replacementLength, image, displayString, relevance);
    }

    public LuaCompletionProposal(String replacementString, int replacementOffset, int replacementLength, Image image, String displayString, int relevance, boolean isInDoc) {
        super(replacementString, replacementOffset, replacementLength, image, displayString, relevance, isInDoc);
    }

    protected boolean isSmartTrigger(char trigger) {
        return false;
    }

    protected boolean insertCompletion() {
        IPreferenceStore preference = LuaUI.getDefault().getPreferenceStore();
        return preference.getBoolean(PreferenceConstants.CODEASSIST_INSERT_COMPLETION);
    }
}
