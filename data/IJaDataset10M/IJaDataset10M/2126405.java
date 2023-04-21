package self.micromagic.eterna.model;

import self.micromagic.eterna.share.Generator;
import self.micromagic.eterna.digester.ConfigurationException;

public interface UpdateExecuteGenerator extends Generator {

    void setCache(int cacheIndex) throws ConfigurationException;

    void setDoExecute(boolean execute) throws ConfigurationException;

    void setPushResult(boolean push) throws ConfigurationException;

    void setMultiType(boolean multi) throws ConfigurationException;

    void addParamBind(ParamBind bind) throws ConfigurationException;

    Execute createExecute() throws ConfigurationException;
}
