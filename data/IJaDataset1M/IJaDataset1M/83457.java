package net.taylor.bpm.testing;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

/**
 * @author jgilbert01
 */
@Name("net.taylor.bpm.timerOverrides")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@BypassInterceptors
public class TimerOverrides {

    private Map<String, Date> timerOverride = new HashMap<String, Date>();

    public Boolean hasTimerOverride(Class process, String key) {
        return timerOverride.containsKey(getKey(process, key));
    }

    public Boolean hasTimerOverride(String key) {
        return timerOverride.containsKey(key);
    }

    public Date getTimerOverride(Class process, String key) {
        return timerOverride.get(getKey(process, key));
    }

    public Date getTimerOverride(String key) {
        return timerOverride.get(key);
    }

    public void addTimerOverrides(Class process, String key, Date value) {
        addTimerOverrides(getKey(process, key), value);
    }

    public void addTimerOverrides(String key, Date value) {
        timerOverride.put(key, value);
    }

    public void clearTimerOverrides() {
        timerOverride.clear();
    }

    protected String getKey(Class process, String key) {
        return process.getName() + "." + key;
    }

    public Map<String, Date> getTimerOverrides() {
        return timerOverride;
    }

    public void disable(String key) {
        timerOverride.put(key, null);
    }

    public void immediately(String key) {
        timerOverride.put(key, new Date());
    }

    public void clear(String key) {
        timerOverride.remove(key);
    }

    public static TimerOverrides getInstance() {
        return (TimerOverrides) Component.getInstance(TimerOverrides.class);
    }
}
