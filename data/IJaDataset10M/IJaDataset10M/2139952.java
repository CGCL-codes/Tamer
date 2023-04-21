package self.micromagic.eterna.search;

import self.micromagic.eterna.digester.ConfigurationException;
import self.micromagic.eterna.share.Generator;

public interface ConditionBuilderGenerator extends Generator {

    void setName(String name) throws ConfigurationException;

    String getName() throws ConfigurationException;

    void setCaption(String caption) throws ConfigurationException;

    void setOperator(String operator) throws ConfigurationException;

    ConditionBuilder createConditionBuilder() throws ConfigurationException;
}
