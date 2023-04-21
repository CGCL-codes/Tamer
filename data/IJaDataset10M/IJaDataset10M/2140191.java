package org.jtools.tmplc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jtools.tmpl.compiler.api.Identifier;

/**
 *
 * @author <a href="mailto:rainer.noack@jtools.org">rainer noack </a>
 */
public class MacroManager {

    private static final Logger LOG = Logger.getLogger(MacroManager.class.getName());

    private final String output;

    private Set<SimpleMacroDescriptor> descriptors = new HashSet<SimpleMacroDescriptor>();

    private Map<String, List<SimpleMacroDescriptor>> macroByIdentifier = new HashMap<String, List<SimpleMacroDescriptor>>();

    private Map<ParsedStatement, SimpleMacroDescriptor> results = new HashMap<ParsedStatement, SimpleMacroDescriptor>();

    public MacroManager(String output) {
        this.output = output;
    }

    private void addMacro(SimpleMacroDescriptor d, Identifier identifier) {
        String id = identifier.toString();
        List<SimpleMacroDescriptor> macros = macroByIdentifier.get(id);
        if (macros == null) macroByIdentifier.put(id, macros = new ArrayList<SimpleMacroDescriptor>(5));
        macros.add(0, d);
    }

    public void addMacro(SimpleMacroDescriptor d, InfoSupport support) {
        if (this.output != null && d.getMacro().getDestLanguages() != null && !d.getMacro().getDestLanguages().contains(output)) {
            LOG.logp(Level.FINE, getClass().getName(), "addMacro", "macro {0} omitted: output {1} not supported", new Object[] { d, output });
            return;
        }
        if (!descriptors.add(d)) return;
        addMacro(d, d.getIdentifier());
        addMacro(d, new Identifier(d.getIdentifier().getNamespace(), d.getIdentifier().getName(), null));
        addMacro(d, new Identifier(null, d.getIdentifier().getName(), null));
        addMacro(d, new Identifier(null, d.getIdentifier().getName(), d.getIdentifier().getClassname()));
        addMacro(d, new Identifier(null, null, d.getIdentifier().getClassname()));
        LOG.logp(Level.FINE, getClass().getName(), "addMacro", "macro {0} added", new Object[] { d });
        results.clear();
    }

    public SimpleMacroDescriptor findMacro(ParsedStatement call) {
        if (call == null) return null;
        SimpleMacroDescriptor result = results.get(call);
        if (result != null) return result;
        if (results.containsKey(result)) {
            LOG.logp(Level.FINE, getClass().getName(), "findMacro", "cached: no matching macro for call '{0}'", new Object[] { result });
            return null;
        }
        List<SimpleMacroDescriptor> descr = macroByIdentifier.get(call.getMacroIdentifier().toString());
        if (descr == null) {
            results.put(call, null);
            LOG.logp(Level.FINE, getClass().getName(), "findMacro", "no list for call '{0}': caching", new Object[] { call });
            return null;
        }
        for (SimpleMacroDescriptor d : descr) if (d.match(call)) {
            results.put(call, d);
            return d;
        }
        LOG.logp(Level.FINE, getClass().getName(), "findMacro", "no result for call '{0}': caching", new Object[] { call });
        results.put(call, null);
        return null;
    }
}
