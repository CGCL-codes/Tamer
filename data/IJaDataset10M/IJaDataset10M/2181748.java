package net.sf.jerkbot.plugins.meteo;

import net.sf.jerkbot.commands.AbstractCommand;
import net.sf.jerkbot.commands.Command;
import net.sf.jerkbot.commands.MessageContext;
import net.sf.jerkbot.exceptions.CommandSyntaxException;
import net.sf.jerkbot.exceptions.JerkBotException;
import net.sf.jerkbot.util.MessageUtil;
import org.apache.axis.utils.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

/**
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 *         Command to display the weather of a given city
 * @version 0.0.1
 */
@Component(immediate = true)
@Service(value = Command.class)
public class WeatherCommandImpl extends AbstractCommand {

    private static final String COMMAND_HELP = "~weather <city>";

    private static final String COMMAND_NAME = "WEATHER";

    private static final String COMMAND_DESCRIPTION = "Tells the weather of a city";

    public WeatherCommandImpl() {
        super(COMMAND_NAME, COMMAND_DESCRIPTION, COMMAND_HELP);
    }

    public void execute(MessageContext messageContext) throws CommandSyntaxException {
        final String message = messageContext.getMessage();
        if (StringUtils.isEmpty(message)) {
            throw new CommandSyntaxException(COMMAND_HELP);
        }
        try {
            WeatherLocator locator = new WeatherLocator();
            WeatherSoap soap = locator.getWeatherSoap12();
            String response = soap.getWeather(message.trim());
            MessageUtil.sayFormatted(messageContext, "Weather for '%s': %s", message, response);
        } catch (Exception e) {
            throw new JerkBotException(e);
        }
    }
}
