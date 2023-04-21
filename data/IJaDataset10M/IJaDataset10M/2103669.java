package org.springframework.richclient.command;

import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.config.CommandFaceDescriptorRegistry;

/**
 * @author Keith Donald
 */
public interface CommandManager extends CommandServices, CommandRegistry, CommandFaceDescriptorRegistry, CommandConfigurer, CommandFactory {

    public void addCommandInterceptor(String commandId, ActionCommandInterceptor interceptor);

    public void removeCommandInterceptor(String commandId, ActionCommandInterceptor interceptor);
}
