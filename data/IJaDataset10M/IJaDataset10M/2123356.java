package pm.eclipse.prompt;

import java.util.List;

public interface IPromptFactory {

    String USER_COMMANDS_PROVIDER_NAME = "User Commands";

    String LAST_COMMANDS_PROVIDER_NAME = "Previous Commands";

    IStringMatcher createMatcher(String pattern);

    IEclipseCommand findCommand(String id);

    IUserCommand createUserCommand();

    List<ICommandProvider> getProviders();

    ICommandProvider createProvider(String name);

    ICommandProvider getUserCommandProvider();

    void setUserCommandProvider(ICommandProvider provider);

    ICommandProvider getLastCommandProvider();

    void setLastCommandProvider(ICommandProvider provider);

    ICommandProvider fromXml(String string, boolean createUserCommands);

    String toXml(ICommandProvider provider);
}
