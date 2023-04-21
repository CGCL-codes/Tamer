package org.kalypso.nofdpidss.ui.view.evaluation.navigation.ranking;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.kalypso.nofdpidss.ui.NofdpUiPlugin;
import org.kalypso.nofdpidss.ui.application.AbstractMenuPart;
import org.kalypso.nofdpidss.ui.i18n.Messages;
import org.kalypso.nofdpidss.ui.view.common.navigation.AbstractNavMenu;

/**
 * @author Dirk Kuch
 */
public class NavMenuBuilderRanking extends AbstractNavMenu {

    public NavMenuBuilderRanking(final Composite parent) throws CoreException {
        super(parent);
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.navmenu.AbstractNavMenu#draw(org.eclipse.swt.widgets.Composite)
   */
    @Override
    protected boolean draw(final Composite parent) throws CoreException {
        if (parent.isDisposed()) return false;
        final FormToolkit toolkit = NofdpUiPlugin.getToolkit();
        final Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
        section.setText(Messages.NavMenuBuilderRanking_0);
        final StringBuffer buf = new StringBuffer();
        buf.append("<form>");
        buf.append("<p>");
        buf.append(Messages.NavMenuBuilderRanking_3);
        buf.append("</p>");
        buf.append("</form>");
        final FormText helpText = toolkit.createFormText(section, true);
        helpText.setWhitespaceNormalized(true);
        helpText.setText(buf.toString(), true, false);
        section.setDescriptionControl(helpText);
        final Composite clientArea = getScrolledClientArea(toolkit, section);
        AbstractMenuPart.call(RanMenuPart.class, toolkit, clientArea);
        return true;
    }
}
