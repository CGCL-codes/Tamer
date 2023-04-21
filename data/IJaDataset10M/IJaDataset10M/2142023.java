package com.apachetune.httpserver;

import com.apachetune.httpserver.impl.HttpServerManagerImpl;
import com.apachetune.httpserver.impl.RecentOpenedServersManagerImpl;
import com.apachetune.httpserver.ui.HttpServerWorkItem;
import com.apachetune.httpserver.ui.impl.HttpServerWorkItemImpl;
import com.apachetune.httpserver.ui.messagesystem.MessageManager;
import com.apachetune.httpserver.ui.messagesystem.MessageStatusBarSite;
import com.apachetune.httpserver.ui.messagesystem.MessageStore;
import com.apachetune.httpserver.ui.messagesystem.ScheduleLoadNewsMessagesStrategy;
import com.apachetune.httpserver.ui.messagesystem.impl.LocalMessageStoreImpl;
import com.apachetune.httpserver.ui.messagesystem.impl.MessageManagerImpl;
import com.apachetune.httpserver.ui.messagesystem.impl.MessageStatusBarSiteImpl;
import com.apachetune.httpserver.ui.messagesystem.impl.ScheduleLoadNewsMessagesStrategyImpl;
import com.apachetune.httpserver.ui.updating.UpdateInfoDialog;
import com.apachetune.httpserver.ui.updating.OpenWebPageHelper;
import com.apachetune.httpserver.ui.updating.UpdateConfiguration;
import com.apachetune.httpserver.ui.updating.UpdateManager;
import com.apachetune.httpserver.ui.updating.impl.UpdateInfoDialogImpl;
import com.apachetune.httpserver.ui.updating.impl.OpenWebPageHelperImpl;
import com.apachetune.httpserver.ui.updating.impl.UpdateConfigurationImpl;
import com.apachetune.httpserver.ui.updating.impl.UpdateManagerImpl;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import static com.apachetune.core.Constants.APPLICATION_WEB_PORTAL_HOME;
import static com.apachetune.httpserver.Constants.*;
import static com.google.inject.Scopes.SINGLETON;

/**
 * FIXDOC
 *
 * @author <a href="mailto:progmonster@gmail.com">Aleksey V. Katorgin</a>
 * @version 1.0
 */
public class HttpServerModule extends AbstractModule {

    protected void configure() {
        bind(HttpServerWorkItem.class).to(HttpServerWorkItemImpl.class).in(SINGLETON);
        bind(HttpServerManager.class).to(HttpServerManagerImpl.class).in(SINGLETON);
        bind(RecentOpenedServersManager.class).to(RecentOpenedServersManagerImpl.class).in(SINGLETON);
        bind(MessageManager.class).to(MessageManagerImpl.class).in(SINGLETON);
        bind(ScheduleLoadNewsMessagesStrategy.class).to(ScheduleLoadNewsMessagesStrategyImpl.class);
        bind(com.apachetune.httpserver.ui.messagesystem.RemoteManager.class).to(com.apachetune.httpserver.ui.messagesystem.impl.RemoteManagerImpl.class).in(SINGLETON);
        bind(MessageStore.class).to(LocalMessageStoreImpl.class).in(SINGLETON);
        bind(String.class).annotatedWith(Names.named(MESSAGE_STORE_DB_URL_PROP)).toInstance("jdbc:h2:message_db;FILE_LOCK=SERIALIZED");
        bind(MessageStatusBarSite.class).to(MessageStatusBarSiteImpl.class).in(SINGLETON);
        bind(String.class).annotatedWith(Names.named(REMOTE_MESSAGE_SERVICE_URL_PROP)).toInstance(APPLICATION_WEB_PORTAL_HOME + "services/news");
        bind(Long.class).annotatedWith(Names.named(CHECK_UPDATE_DELAY_IN_MSEC_PROP)).toInstance(120L * 1000);
        bind(String.class).annotatedWith(Names.named(REMOTE_UPDATE_SERVICE_URL_PROP)).toInstance(APPLICATION_WEB_PORTAL_HOME + "services/updates");
        bind(UpdateManager.class).to(UpdateManagerImpl.class).in(SINGLETON);
        bind(UpdateConfiguration.class).to(UpdateConfigurationImpl.class).in(SINGLETON);
        bind(com.apachetune.httpserver.ui.updating.RemoteManager.class).to(com.apachetune.httpserver.ui.updating.impl.RemoteManagerImpl.class).in(SINGLETON);
        bind(OpenWebPageHelper.class).to(OpenWebPageHelperImpl.class);
        bind(UpdateInfoDialog.class).to(UpdateInfoDialogImpl.class);
    }
}
