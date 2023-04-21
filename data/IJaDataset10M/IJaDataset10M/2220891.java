package dnl.infra.cli;

/**
 * 
 * @author Daniel Orr
 * 
 */
public class OptionFactory {

    public static CliSetting createSetting(String name) {
        return new CliSetting(name);
    }

    public static CliSetting createMandatorySetting(String name) {
        CliSetting setting = new CliSetting(name);
        setting.setMandatory(true);
        return setting;
    }

    public static CliSetting createMandatorySetting(String name, String description) {
        CliSetting setting = new CliSetting(name);
        setting.setDescription(description);
        setting.setMandatory(true);
        return setting;
    }

    public static CliSetting createSetting(String name, String description) {
        CliSetting setting = new CliSetting(name, description);
        return setting;
    }

    public static CliFlag createFlag(String name, String description) {
        CliFlag flag = new CliFlag(name, description);
        return flag;
    }
}
