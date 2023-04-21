package org.hip.vif.forum.search.ui;

import java.net.URL;
import org.hip.vif.core.interfaces.IMessages;
import org.hip.vif.core.search.NoHitsException;
import org.hip.vif.forum.search.Activator;
import org.hip.vif.forum.search.data.ContributionContainer;
import org.hip.vif.forum.search.tasks.SearchContentTask;
import org.hip.vif.web.util.HelpButton;
import org.hip.vif.web.util.VIFViewHelper;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

/**
 * View to display the input field to search for contributions.
 * 
 * @author Luthiger
 * Created: 30.09.2011
 */
@SuppressWarnings("serial")
public class SearchContentView extends CustomComponent {

    private static final int PAGE_LENGTH = 15;

    private static final int MIN_INPUT = 2;

    /**
	 * View constructor.
	 * 
	 * @param inHelpContent URL
	 * @param inTask {@link SearchContentTask}
	 */
    public SearchContentView(URL inHelpContent, final SearchContentTask inTask) {
        VerticalLayout lLayout = new VerticalLayout();
        setCompositionRoot(lLayout);
        lLayout.setSizeFull();
        final IMessages lMessages = Activator.getMessages();
        lLayout.setStyleName("vif-view");
        lLayout.addComponent(new Label(String.format(VIFViewHelper.TMPL_TITLE, "vif-pagetitle", lMessages.getMessage("ui.search.view.title.page")), Label.CONTENT_XHTML));
        HorizontalLayout lInput = new HorizontalLayout();
        lInput.setMargin(true, true, true, false);
        Label lLabel = new Label(String.format("%s:&#160;", lMessages.getMessage("ui.search.view.label.input")), Label.CONTENT_XHTML);
        lInput.addComponent(lLabel);
        lInput.setComponentAlignment(lLabel, Alignment.MIDDLE_LEFT);
        final TextField lSearch = new TextField();
        lSearch.setColumns(50);
        lSearch.focus();
        lInput.addComponent(lSearch);
        lInput.addComponent(new HelpButton(lMessages.getMessage("ui.search.view.button.help"), inHelpContent, 700, 620));
        lLayout.addComponent(lInput);
        final Table lSearchResult = new Table();
        Button lButton = new Button(lMessages.getMessage("ui.search.view.button.search"));
        lButton.setClickShortcut(KeyCode.ENTER);
        lButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent inEvent) {
                String lQuery = lSearch.getValue().toString().trim();
                if (lQuery.length() <= MIN_INPUT) {
                    getWindow().showNotification(lMessages.getMessage("errmsg.search.noInput"), Notification.TYPE_WARNING_MESSAGE);
                } else {
                    try {
                        initTable(inTask.search(lQuery), lSearchResult);
                        lSearchResult.setVisible(true);
                    } catch (NoHitsException exc) {
                        lSearchResult.setVisible(false);
                        getWindow().showNotification(lMessages.getFormattedMessage("errmsg.search.noHits", exc.getQueryString()), Notification.TYPE_WARNING_MESSAGE);
                    } catch (Exception exc) {
                        lSearchResult.setVisible(false);
                        getWindow().showNotification(lMessages.getMessage("errmsg.search.wrongInput"), Notification.TYPE_WARNING_MESSAGE);
                    }
                }
            }
        });
        lLayout.addComponent(lButton);
        lLayout.addComponent(VIFViewHelper.createSpacer());
        lSearchResult.setVisible(false);
        lSearchResult.setSizeFull();
        lSearchResult.setColumnCollapsingAllowed(true);
        lSearchResult.setColumnReorderingAllowed(true);
        lSearchResult.setSelectable(true);
        lSearchResult.setImmediate(true);
        lSearchResult.addListener((Property.ValueChangeListener) inTask);
        lLayout.addComponent(lSearchResult);
    }

    protected void initTable(ContributionContainer inSearchResult, Table inTable) {
        inTable.setContainerDataSource(inSearchResult);
        inTable.setPageLength(inSearchResult.size() > PAGE_LENGTH ? PAGE_LENGTH : 0);
        inTable.setVisibleColumns(ContributionContainer.NATURAL_COL_ORDER);
        inTable.setColumnHeaders(VIFViewHelper.getColumnHeaders(ContributionContainer.COL_HEADERS, Activator.getMessages()));
    }
}
