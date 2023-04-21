package net.sf.dpdesktop.module.message;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import net.sf.dpdesktop.gui.MessageDialog;

/**
 *
 * @author Heiner Reinhardt
 */
public class MessageModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MessageController.class).in(Scopes.SINGLETON);
        bind(MessageDialog.class).in(Scopes.SINGLETON);
        bind(MessageModel.class).in(Scopes.SINGLETON);
    }
}
