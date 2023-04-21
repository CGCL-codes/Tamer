package net.deytan.wofee.gwt.ui.main.page;

import net.deytan.wofee.gwt.bean.UserFeedItemBean;
import net.deytan.wofee.gwt.ui.AbstractWidget;
import net.deytan.wofee.gwt.ui.common.ScrollPanel;
import net.deytan.wofee.gwt.ui.event.feeditem.AbstractFeedItemEvent;
import net.deytan.wofee.gwt.ui.event.feeditem.AddFeedItemEvent;
import net.deytan.wofee.gwt.ui.event.feeditem.FeedItemEventHandler;
import net.deytan.wofee.gwt.ui.event.feeditem.RemoveFeedItemEvent;
import net.deytan.wofee.gwt.ui.event.feeditem.ShowFeedItemEvent;
import net.deytan.wofee.gwt.ui.event.feeditem.UpdateFeedItemEvent;
import net.deytan.wofee.gwt.utils.FormatUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class Page extends AbstractWidget {

    interface Binder extends UiBinder<FlowPanel, Page> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    FlowPanel panel;

    @UiField
    DivElement title;

    @UiField
    SpanElement author;

    @UiField
    SpanElement date;

    @UiField
    SpanElement lastUpdate;

    @UiField
    HTML content;

    public Page(final EventBus eventBus) {
        super(eventBus);
        Page.binder.createAndBindUi(this);
        this.getEventBus().addHandler(AbstractFeedItemEvent.TYPE, new FeedItemEventHandler() {

            @Override
            public void onShowFeedItem(final ShowFeedItemEvent event) {
                Page.this.showFeedItem(event.getItem());
            }

            @Override
            public void onAddFeedItem(final AddFeedItemEvent event) {
            }

            @Override
            public void onRemoveFeedItem(final RemoveFeedItemEvent event) {
            }

            @Override
            public void onUpdateFeedItem(final UpdateFeedItemEvent event) {
            }
        });
    }

    @Override
    public Widget asWidget() {
        return this.panel;
    }

    private void showFeedItem(final UserFeedItemBean item) {
        this.title.setInnerText(item.getTitle());
        this.author.setInnerText(item.getAuthor());
        this.date.setInnerHTML(FormatUtils.formatDate("dd/MM/yyyy HH:mm", item.getDate()));
        String content = null;
        if (item.getResume() != null) {
            content = item.getResume();
        } else {
            content = item.getContent();
        }
        this.content.setHTML(content);
    }

    @UiFactory
    ScrollPanel makeScrollPanel() {
        return new ScrollPanel(this.getEventBus());
    }
}
