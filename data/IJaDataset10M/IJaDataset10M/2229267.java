package org.intellij.trinkets.hyperLink;

import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.vcs.IssueNavigationLink;
import org.intellij.trinkets.hyperLink.actions.HyperLinkAction;

/**
 * VCS issue navigation reference.
 *
 * @author Alexey Efimov
 */
public final class IssueHyperLinkReference extends DefaultHyperLinkReference {

    public IssueHyperLinkReference(IssueNavigationLink link, TextAttributes attributes, HyperLinkAction action) {
        super("vcs.issue.navigation.link", link.getIssueRegexp(), link.getLinkRegexp(), attributes, action);
    }
}
