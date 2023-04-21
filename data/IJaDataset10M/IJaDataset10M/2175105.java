package org.jwaim.modules.pre;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jwaim.core.ModuleCommunicationLine;
import org.jwaim.core.conf.ActionTable;
import org.jwaim.core.conf.CombinationElement;
import org.jwaim.core.conf.SelectBox;
import org.jwaim.core.conf.TextBox;
import org.jwaim.core.interfaces.ConfigurationElement;
import org.jwaim.core.interfaces.ModuleReturnValue;
import org.jwaim.core.interfaces.PreModule;
import org.jwaim.core.logger.JWAIMLogger;
import org.jwaim.core.util.Variables;

/**
 * Keep this ruleset small in order to keep response times fast
 */
public final class DomainPathRule extends PreModule {

    private static final class RuleSet {

        private final String domain;

        private final Pattern domainPattern;

        private final String path;

        private final Pattern pathPattern;

        private final Long ipFrom;

        private final Long ipTo;

        private final ModuleReturnValue returnValue;

        private RuleSet(String domain, String path, Long ipFrom, Long ipTo, ModuleReturnValue returnValue) {
            this.domain = domain;
            this.domainPattern = domain == null ? null : Pattern.compile(domain, Pattern.CASE_INSENSITIVE);
            this.path = path;
            this.pathPattern = path == null ? null : Pattern.compile(path, Pattern.CASE_INSENSITIVE);
            this.ipFrom = ipFrom;
            this.ipTo = ipTo;
            this.returnValue = returnValue;
        }

        private final ModuleReturnValue decide(String domain, String path, Long ip) {
            if (this.domain != null && (!domainPattern.matcher(domain).find())) return null;
            if (this.path != null && (!pathPattern.matcher(path).find())) return null;
            if (this.ipFrom != null && ip < this.ipFrom) return null;
            if (this.ipTo != null && ip > this.ipFrom) return null;
            return this.returnValue;
        }

        private final void getState(String key, Properties properties) {
            if (domain != null) properties.put(key + "domain", domain);
            if (path != null) properties.put(key + "path", path);
            if (ipFrom != null) properties.put(key + "from", ipFrom.toString());
            if (ipTo != null) properties.put(key + "to", ipTo.toString());
            properties.put(key + "ret", returnValue.getId());
        }

        private final String[] getValues() {
            String[] ret = new String[4];
            ret[0] = domain == null ? "" : domain;
            ret[1] = path == null ? "" : path;
            String ipString = "";
            if (ipFrom != null) ipString = Variables.longToIp(ipFrom);
            if (!Variables.equals(ipFrom, ipTo)) ipString += "-";
            if (ipTo != null && !Variables.equals(ipFrom, ipTo)) ipString += Variables.longToIp(ipTo);
            ret[2] = ipString;
            ret[3] = this.returnValue.getTextKey();
            return ret;
        }
    }

    private List<RuleSet> rules;

    private final Object ruleLock = new Object();

    /**
	 * Creates new elements with given communication line and logger
	 * @param communicationLine communication line to use when communicating with other modules
	 * @param logger logger to use when logging events
	 */
    public DomainPathRule(ModuleCommunicationLine communicationLine, JWAIMLogger logger) {
        super(communicationLine, logger);
        this.rules = new LinkedList<RuleSet>();
    }

    @Override
    public final ModuleReturnValue filter(HttpServletRequest request, HttpServletResponse response, Map<String, Object> attributes) {
        String domain = request.getServerName();
        String path = request.getServletPath();
        Long ip = Variables.ipToLong(request.getRemoteAddr(), 0);
        synchronized (ruleLock) {
            for (RuleSet rs : this.rules) {
                ModuleReturnValue ret = rs.decide(domain, path, ip);
                if (ret != null) {
                    return ret;
                }
            }
        }
        return ModuleReturnValue.CONTINUE;
    }

    @Override
    public final void init(Properties propertiesConfiguration) throws IOException {
        List<RuleSet> rules = new LinkedList<RuleSet>();
        super.init(propertiesConfiguration);
        String instances = propertiesConfiguration.getProperty("activelist");
        if (instances != null) {
            String[] ids = instances.split(";");
            for (String s : ids) {
                int id = Variables.toInt(s, -1);
                if (id >= 0) {
                    String key = "rule_" + id + ".";
                    String domain = propertiesConfiguration.getProperty(key + "domain");
                    String path = propertiesConfiguration.getProperty(key + "path");
                    Long from = Variables.toLong(propertiesConfiguration.getProperty(key + "from"), -1);
                    Long to = Variables.toLong(propertiesConfiguration.getProperty(key + "to"), -1);
                    String ret = propertiesConfiguration.getProperty(key + "ret");
                    if (to == -1l) to = null;
                    if (from == -1l) from = null;
                    ModuleReturnValue returnV = ModuleReturnValue.getById(ret);
                    if (returnV == null) continue;
                    rules.add(new RuleSet(domain, path, from, to, returnV));
                }
            }
        }
        synchronized (ruleLock) {
            this.rules = rules;
        }
    }

    @Override
    public final Properties getState() {
        Properties ret = super.getState();
        StringBuffer active = new StringBuffer();
        int counter = 0;
        synchronized (ruleLock) {
            for (RuleSet rs : this.rules) {
                String key = "rule_" + counter + ".";
                rs.getState(key, ret);
                if (active.length() > 0) active.append(';');
                active.append(counter);
                counter++;
            }
        }
        ret.put("activelist", active.toString());
        return ret;
    }

    @Override
    public final Collection<ConfigurationElement> getConfigurationElements() {
        Collection<ConfigurationElement> configurationElements = super.getConfigurationElements();
        Map<String, String> errors = Collections.emptyMap();
        setConfigurationElements(configurationElements, errors, errors);
        return configurationElements;
    }

    private final void setConfigurationElements(Collection<ConfigurationElement> configurationElements, Map<String, String> errors, Map<String, String> values) {
        ActionTable at = new ActionTable("dpr.rules", Arrays.asList(new String[] { "dpr.domain", "dpr.path", "dpr.range", "dpr.action", "dpr.remove" }), this.getId());
        at.addAction("dpr.remove", "&remove=true&id=[ROWID]");
        synchronized (ruleLock) {
            int id = 0;
            for (RuleSet rs : rules) {
                at.addRow("" + id, Arrays.asList(rs.getValues()));
                id++;
            }
        }
        at.attachTextResource(3);
        configurationElements.add(at);
        CombinationElement ce = new CombinationElement("dpr.rule.new");
        ce.setInfoText("dpr.rule.new.info");
        TextBox domain = new TextBox("domain");
        domain.setLabel("dpr.domain");
        if (errors.get("domain") != null) domain.setError(errors.get("domain"));
        if (values.get("domain") != null) domain.setValue(values.get("domain"));
        ce.addElement(domain);
        TextBox path = new TextBox("path");
        path.setLabel("dpr.path");
        if (errors.get("path") != null) path.setError(errors.get("path"));
        if (values.get("path") != null) path.setValue(values.get("path"));
        ce.addElement(path);
        TextBox from = new TextBox("from");
        from.setLabel("dpr.from");
        if (errors.get("from") != null) from.setError(errors.get("from"));
        if (values.get("from") != null) from.setValue(values.get("from"));
        ce.addElement(from);
        TextBox to = new TextBox("to");
        to.setLabel("dpr.to");
        if (errors.get("to") != null) to.setError(errors.get("to"));
        if (values.get("to") != null) to.setValue(values.get("to"));
        ce.addElement(to);
        SelectBox action = new SelectBox("returnv");
        action.setLabel("dpr.action");
        for (ModuleReturnValue mrv : ModuleReturnValue.values()) action.addSelection(mrv.getId(), mrv.getTextKey());
        if (values.get("returnv") != null) action.setValue(values.get("returnv"));
        ce.addElement(action);
        configurationElements.add(ce);
    }

    private final boolean tryPattern(String pattern) {
        if (pattern == null) return true;
        try {
            Pattern.compile(pattern);
        } catch (PatternSyntaxException pse) {
            return false;
        }
        return true;
    }

    @Override
    public final Collection<ConfigurationElement> reconfigure(HttpServletRequest request) {
        String remove = request.getParameter("remove");
        if ("true".equals(remove)) {
            return remove(request);
        }
        Collection<ConfigurationElement> configurationElements = super.reconfigure(request);
        String domain = Variables.trim(request.getParameter("domain"), null);
        String path = Variables.trim(request.getParameter("path"), null);
        String fromS = Variables.trim(request.getParameter("from"), null);
        String toS = Variables.trim(request.getParameter("to"), null);
        String returnv = request.getParameter("returnv");
        Long from = null;
        Long to = null;
        Map<String, String> errorMap = new HashMap<String, String>();
        Map<String, String> valueMap = new HashMap<String, String>();
        valueMap.put("domain", domain);
        valueMap.put("path", path);
        valueMap.put("from", fromS);
        valueMap.put("to", toS);
        valueMap.put("returnv", returnv);
        if (configurationElements != null && !configurationElements.isEmpty()) {
            setConfigurationElements(configurationElements, errorMap, valueMap);
            return configurationElements;
        }
        if (!tryPattern(domain)) {
            errorMap.put("domain", "conf.field.invalid.pattern");
        }
        if (!tryPattern(path)) {
            errorMap.put("path", "conf.field.invalid.pattern");
        }
        if (fromS != null) {
            long l = Variables.ipToLong(fromS, -1);
            if (l == -1l) errorMap.put("from", "conf.field.invalid"); else from = l;
        }
        if (toS != null) {
            long l = Variables.ipToLong(toS, -1);
            if (l == -1l) errorMap.put("to", "conf.field.invalid"); else to = l;
        }
        if (from != null && to != null && to < from) {
            Long tmp = from;
            from = to;
            to = tmp;
        }
        ModuleReturnValue mrv = ModuleReturnValue.getById(returnv);
        if (!errorMap.isEmpty()) {
            configurationElements = super.getConfigurationElements();
            setConfigurationElements(configurationElements, errorMap, valueMap);
        } else if (domain != null || path != null || from != null || to != null) {
            RuleSet newRule = new RuleSet(domain, path, from, to, mrv);
            synchronized (ruleLock) {
                rules.add(newRule);
            }
        }
        return configurationElements;
    }

    private final Collection<ConfigurationElement> remove(HttpServletRequest request) {
        int id = Variables.toInt(request.getParameter("id"), -1);
        if (id >= 0) {
            synchronized (ruleLock) {
                if (id < rules.size()) rules.remove(id);
            }
        }
        return getConfigurationElements();
    }
}
