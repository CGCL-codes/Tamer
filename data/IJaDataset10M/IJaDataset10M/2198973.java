package dnl.infra.cli.flavors;

import org.apache.commons.lang.StringUtils;
import dnl.infra.cli.CliFlavor;
import dnl.infra.cli.CliOption;
import dnl.infra.cli.Options;

/**
 * Abbreviated names start with single dash and long names with double dash.
 * 
 * @author Daniel Orr
 * 
 */
public class StandardNixFlavor implements CliFlavor {

    @Override
    public boolean isOption(String cliArgument) {
        if (cliArgument.startsWith("--")) return true;
        if (cliArgument.startsWith("-")) return true;
        return false;
    }

    @Override
    public String getOptionName(String cliArgument) {
        if (cliArgument.startsWith("--")) return StringUtils.removeStart(cliArgument, "--");
        if (cliArgument.startsWith("-")) return StringUtils.removeStart(cliArgument, "-");
        throw new IllegalArgumentException("Argument should start with either '-' or '--'");
    }

    @Override
    public CliOption matchOption(String cliArgument, Options options) {
        if (cliArgument.startsWith("--")) {
            String optionName = getOptionName(cliArgument);
            for (CliOption cliOption : options) {
                if (cliOption.getName().equals(optionName)) return cliOption;
            }
        } else if (cliArgument.startsWith("-")) {
            String optionName = getOptionName(cliArgument);
            for (CliOption cliOption : options) {
                if (cliOption.getShortName() != null && cliOption.getShortName().equals(optionName)) return cliOption;
            }
        }
        throw new IllegalArgumentException("Argument should start with either '-' or '--'");
    }
}
