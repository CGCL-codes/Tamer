package com.retech.reader.web.client.mobile.ioc;

import com.goodow.web.logging.shared.rpc.ChannelContextProvider;
import com.goodow.web.view.wave.client.NestedBlipTest;
import com.goodow.web.view.wave.client.WaveTest;
import com.goodow.web.view.wave.client.contact.ContactPanel;
import com.goodow.web.view.wave.client.shell.WaveShell;
import com.goodow.web.view.wave.client.tree.BlipTest;
import com.goodow.web.view.wave.client.tree.TreeTest;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryLogHandler.LoggingRequestProvider;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.RequestTransport;
import com.retech.reader.web.client.home.BookFlip;
import com.retech.reader.web.client.home.BookListPanel;
import com.retech.reader.web.client.home.LibraryView;
import com.retech.reader.web.client.home.SearchPanel;
import com.retech.reader.web.client.mobile.ui.BookListEditor;
import com.retech.reader.web.client.mobile.ui.CategoryListEditor;
import com.retech.reader.web.client.mobile.ui.ContentEditor;
import com.retech.reader.web.client.mobile.ui.HomeView;
import com.retech.reader.web.client.mobile.ui.IssueEditor;
import com.retech.reader.web.client.mobile.ui.IssueListImage;
import com.retech.reader.web.client.mobile.ui.IssueNews;
import com.retech.reader.web.client.mobile.ui.SectionBrowserView;
import com.retech.reader.web.client.mobile.ui.SectionListEditor;
import com.retech.reader.web.client.mobile.ui.TestEditor;
import com.retech.reader.web.client.mobile.ui.bar.SettingsView;
import com.retech.reader.web.client.mobile.ui.talk.TalkView;
import com.retech.reader.web.client.topbar.Labs;
import com.retech.reader.web.client.topbar.TopBar;
import com.retech.reader.web.shared.rpc.FinalRequestFactory;
import com.retech.reader.web.shared.rpc.ReaderFactory;
import org.cloudlet.web.boot.shared.MapBinder;
import java.util.logging.Logger;

public final class ReaderMobileGinModule extends AbstractGinModule {

    @Singleton
    public static class Binder {
    }

    @Singleton
    public static class BinderProvider implements Provider<Binder> {

        private final Logger logger = Logger.getLogger(getClass().getName());

        @Inject
        private MapBinder<String, IsWidget> isWidgetMapBinder;

        @Inject
        private AsyncProvider<BookListEditor> bookListEditor;

        @Inject
        private AsyncProvider<SectionListEditor> sectionListEditor;

        @Inject
        private AsyncProvider<HomeView> homeView;

        @Inject
        private AsyncProvider<ContentEditor> pageEditor;

        @Inject
        private AsyncProvider<CategoryListEditor> catgoryListEditor;

        @Inject
        private AsyncProvider<SectionBrowserView> sectionBrowser;

        @Inject
        private AsyncProvider<BookListPanel> bookListPanel;

        @Inject
        private AsyncProvider<BookFlip> bookFlip;

        @Inject
        private AsyncProvider<SettingsView> settingsView;

        @Inject
        private AsyncProvider<TalkView> talkView;

        @Inject
        private AsyncProvider<IssueEditor> issueEditor;

        @Inject
        private AsyncProvider<LibraryView> libraryView;

        @Inject
        private AsyncProvider<IssueListImage> issueListImage;

        @Inject
        private AsyncProvider<IssueNews> issueNews;

        @Inject
        private AsyncProvider<TestEditor> testEditor;

        @Inject
        private AsyncProvider<WaveTest> waveTest;

        @Inject
        private AsyncProvider<SearchPanel> searchPanel;

        @Inject
        private AsyncProvider<ContactPanel> contactPanel;

        @Inject
        private AsyncProvider<TreeTest> treeTest;

        @Inject
        private AsyncProvider<Labs> laboratoryPanel;

        @Inject
        private AsyncProvider<BlipTest> blipTest;

        @Inject
        private AsyncProvider<NestedBlipTest> nestedBlipTest;

        @Inject
        private WaveShell shell;

        @Inject
        private TopBar topBar;

        @Override
        public Binder get() {
            Window.setTitle("睿泰阅读");
            shell.getTopBar().add(topBar);
            logger.finest("EagerSingleton begin");
            isWidgetMapBinder.addBinding("/").toAsyncProvider(bookListPanel);
            isWidgetMapBinder.addBinding(BookListEditor.class.getName()).toAsyncProvider(bookListEditor);
            isWidgetMapBinder.addBinding(SectionListEditor.class.getName()).toAsyncProvider(sectionListEditor);
            isWidgetMapBinder.addBinding(CategoryListEditor.class.getName()).toAsyncProvider(catgoryListEditor);
            isWidgetMapBinder.addBinding(SectionBrowserView.class.getName()).toAsyncProvider(sectionBrowser);
            isWidgetMapBinder.addBinding(WaveTest.class.getName()).toAsyncProvider(waveTest);
            isWidgetMapBinder.addBinding(BookFlip.class.getName()).toAsyncProvider(bookFlip);
            isWidgetMapBinder.addBinding(ContentEditor.class.getName()).toAsyncProvider(pageEditor);
            isWidgetMapBinder.addBinding(SettingsView.class.getName()).toAsyncProvider(settingsView);
            isWidgetMapBinder.addBinding(TalkView.class.getName()).toAsyncProvider(talkView);
            isWidgetMapBinder.addBinding(NestedBlipTest.class.getName()).toAsyncProvider(nestedBlipTest);
            isWidgetMapBinder.addBinding(IssueEditor.class.getName()).toAsyncProvider(issueEditor);
            isWidgetMapBinder.addBinding(LibraryView.class.getName()).toAsyncProvider(libraryView);
            isWidgetMapBinder.addBinding(IssueListImage.class.getName()).toAsyncProvider(issueListImage);
            isWidgetMapBinder.addBinding(IssueNews.class.getName()).toAsyncProvider(issueNews);
            isWidgetMapBinder.addBinding(TestEditor.class.getName()).toAsyncProvider(testEditor);
            isWidgetMapBinder.addBinding(SearchPanel.class.getName()).toAsyncProvider(searchPanel);
            isWidgetMapBinder.addBinding(BlipTest.class.getName()).toAsyncProvider(blipTest);
            isWidgetMapBinder.addBinding(ContactPanel.class.getName()).toAsyncProvider(contactPanel);
            isWidgetMapBinder.addBinding(TreeTest.class.getName()).toAsyncProvider(treeTest);
            isWidgetMapBinder.addBinding(Labs.class.getName()).toAsyncProvider(laboratoryPanel);
            logger.finest("EagerSingleton end");
            return null;
        }
    }

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    protected void configure() {
        bind(Binder.class).toProvider(BinderProvider.class).asEagerSingleton();
        bind(RequestFactory.class).to(FinalRequestFactory.class).in(Singleton.class);
        bind(ReaderFactory.class).to(FinalRequestFactory.class).in(Singleton.class);
        bind(LoggingRequestProvider.class).to(FinalRequestFactory.class).in(Singleton.class);
        bind(ChannelContextProvider.class).to(FinalRequestFactory.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    FinalRequestFactory finalRequestFactoryProvider(final EventBus eventBus, final RequestTransport requestTransport) {
        FinalRequestFactory f = GWT.create(FinalRequestFactory.class);
        f.initialize(eventBus, requestTransport);
        return f;
    }
}
