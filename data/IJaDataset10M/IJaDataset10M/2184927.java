package services.core.commands.execute;

import groovy.lang.GroovyClassLoader;
import java.io.File;
import services.core.commands.SimpleCommand;
import services.core.commands.SimpleCommandRequest;
import services.core.commands.SimpleCommandResponse;

public class GroovyClassCommand extends SimpleCommand {

    public static final String GROOVY_SERVICE_HOME = "GROOVY_SERVICE_HOME";

    public SimpleCommandResponse execute(SimpleCommandRequest commandRequest) {
        String[] requiredParameters = new String[] { "target", "value" };
        if (!commandRequest.doesRequiredParametersExist(requiredParameters)) {
            return SimpleCommandResponse.createMissingParameterResponse(requiredParameters);
        }
        String groovyScriptName = commandRequest.getSimpleValue(requiredParameters[0]);
        String groovyScriptArgument = commandRequest.getSimpleValue(requiredParameters[1]);
        try {
            ClassLoader parent = getClass().getClassLoader();
            GroovyClassLoader loader = new GroovyClassLoader(parent);
            File groovyFile = new File(getGroovyServiceHomePath() + "/" + groovyScriptName);
            Class groovyClass = loader.parseClass(groovyFile);
            SimpleGroovyService groovyService = (SimpleGroovyService) groovyClass.newInstance();
            SimpleCommandResponse commandResponse = groovyService.execute(groovyScriptArgument);
            return commandResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return SimpleCommandResponse.createFailureResponse(e.getMessage());
        }
    }

    public String getGroovyServiceHomePath() {
        if (null != System.getProperty(GROOVY_SERVICE_HOME)) {
            return System.getProperty(GROOVY_SERVICE_HOME);
        } else if (null != System.getenv(GROOVY_SERVICE_HOME)) {
            return System.getenv(GROOVY_SERVICE_HOME);
        } else {
            return new File(".").getAbsolutePath();
        }
    }
}
