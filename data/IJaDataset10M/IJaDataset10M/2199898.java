package net.sourceforge.turtleweed.cmd;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.sourceforge.turtleweed.map.ExistsException;
import net.sourceforge.turtleweed.map.IMapManager;

public class AddEnvCommand implements IMapCommand {

    private IMapManager mm;

    private IMapValidator validator = new AddEnvParamsValidator();

    Properties props = null;

    public AddEnvCommand() {
        props = new Properties();
        try {
            props.load(getClass().getResourceAsStream(getClass().getSimpleName() + ".properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IResponse execute(List<String> params) {
        GenericResponse r = null;
        validator.setMapManager(mm);
        IValidationResult result = validator.validatePatams(params);
        if (!result.isValidationsuccess()) {
            r = new GenericResponse(false);
            r.setDisplayString(result.getFormattedErrorMessage());
            return r;
        }
        Map<String, Object> cleansedParams = result.getCleansedParams();
        try {
            boolean defaultEnv = Boolean.parseBoolean(cleansedParams.get("DEFAULT").toString());
            mm.addEnvironment(cleansedParams.get("ENVID").toString(), defaultEnv);
            r = new GenericResponse(true);
        } catch (ExistsException e) {
            e.printStackTrace();
            r = new GenericResponse(false);
            r.setDisplayString(e.getMessage());
        }
        return r;
    }

    @Override
    public Object getDetailedDescription() {
        return props.getProperty("cmd.detaileddescription");
    }

    @Override
    public Object getShortDescription() {
        return props.getProperty("cmd.shortdescription");
    }

    @Override
    public Object getUsageDescription() {
        return props.getProperty("cmd.usagedescription");
    }

    @Override
    public String getUsageName() {
        return props.getProperty("cmd.usagename");
    }

    @Override
    public void setMapManager(IMapManager manager) {
        this.mm = manager;
    }
}
