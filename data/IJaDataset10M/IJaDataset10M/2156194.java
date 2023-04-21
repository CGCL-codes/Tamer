package games.basicgame.properties;

import games.EventHandler;
import games.basicgame.BasicGamePlayer;
import games.script.ObjectScript;
import games.script.ScriptException;

public interface Property extends EventHandler {

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public Object getDefaultValue();

    public void setDefaultValue(Object defaultValue);

    public boolean setValue(BasicGamePlayer source, BasicGamePlayer target, String channel, Object value, Object... param) throws ScriptException, InvalidValueException;

    public void init() throws ScriptException;

    public boolean randomizeValue(BasicGamePlayer source, BasicGamePlayer target, String channel, Object... param) throws ScriptException;

    public Object getValue(String value);

    public String valueOf(Object value);

    public void setScript(ObjectScript script);
}
