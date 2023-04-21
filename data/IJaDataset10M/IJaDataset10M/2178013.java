package tesla.app.command.provider;

import java.util.Map;

public interface IConfigProvider {

    public String getCommand(String key);

    public Map<String, String> getSettings(String key);

    public String getLaunchAppCommand();

    public String getIsRunningCommand();

    public String getTerminateAppCommand();
}
