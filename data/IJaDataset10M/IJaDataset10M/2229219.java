package uk.ac.cam.caret.lom.impl;

import uk.ac.cam.caret.lom.api.*;
import uk.ac.cam.caret.minibix.general.svo.api.SvoPreferences;
import uk.ac.cam.caret.minibix.general.svo.api.SvoReference;
import uk.ac.cam.caret.minibix.metadata.api.BadFormatException;
import uk.ac.cam.caret.minibix.metadata.api.IncompatibleException;
import uk.ac.cam.caret.minibix.metadata.api.MetadataBuilder;
import uk.ac.cam.caret.minibix.metadata.api.MetadataStore;
import uk.ac.cam.caret.minibix.metadata.api.MetadataUpdater;
import uk.ac.cam.caret.minibix.metadata.api.TypeRegistrar;
import uk.ac.cam.caret.minibix.metadata.api.TypeRegistry;
import uk.ac.cam.caret.minibix.metadata.impl.TypeRegistryImpl;
import java.util.*;

public class LomLifecycleImpl implements LomLifecycle, TypeRegistrar, MetadataBuilder, MetadataUpdater {

    private LomMultilingualStringImpl version;

    private SourceValueImpl status;

    private List<ContributionImpl> contributions = new ArrayList<ContributionImpl>();

    public Contribution[] getContributions() {
        return contributions.toArray(new Contribution[0]);
    }

    public SourceValue getStatus() {
        return status;
    }

    public LomMultilingualString getVersion() {
        return version;
    }

    public void setVersion(LomMultilingualString in) {
        version = (LomMultilingualStringImpl) in;
    }

    public void setStatus(SourceValue in) {
        status = (SourceValueImpl) in;
    }

    public void setStatus(LomMultilingualString source, LomMultilingualString value) {
        status = new SourceValueImpl(source, value);
    }

    public void addContribution(Contribution in) {
        contributions.add((ContributionImpl) in);
    }

    public void buildSvo(SvoReference in, String prefix, SvoPreferences prefs) {
        if (!"".equals(prefix)) prefix += ".";
        if (version != null) in.addRelationS(prefix + "lom.lifecycle.version", version.getValue(prefs));
        if (status != null) status.buildSvo(in, prefix + "lom.lifecycle.status", prefs);
        for (Contribution c : contributions) c.buildSvo(in, prefix + "lom.lifecycle.contribution", prefs);
    }

    public TypeRegistry getTypeRegister(String prefix) {
        return md_register(prefix);
    }

    public static TypeRegistry md_register(String prefix) {
        TypeRegistry reg = new TypeRegistryImpl();
        reg.register(prefix + "/version", "siU{0:language}");
        reg.register(prefix + "/status", "ssUsiUP{0:language,1:language}");
        reg.register(prefix + "/contributions", "ssUssUPssiULUU{0:language,1:language,3:language}");
        reg.register(prefix + "/derived/author", "iL");
        return reg;
    }

    public void buildMetadata(String prefix, MetadataStore store) throws BadFormatException, IncompatibleException {
        if (version != null) Utils.getMetadata(store, prefix + "/version", version.getMap());
        if (status != null) Utils.getMetadata(store, prefix + "/status", status.getWrangledData());
        List<String> authors = new ArrayList<String>();
        Map<Map<Map<String, String>, Map<String, String>>, Map<String, List<Map<String, String>>>> ct = new HashMap<Map<Map<String, String>, Map<String, String>>, Map<String, List<Map<String, String>>>>();
        for (ContributionImpl c : contributions) {
            Map<Map<String, String>, Map<String, String>> sv = c.getRole().getWrangledData();
            Map<String, List<Map<String, String>>> wv = c.getWrangledValue();
            ct.put(sv, wv);
            for (VCard vc : c.getContributors()) {
                authors.add(vc.getValue());
            }
        }
        Utils.getMetadata(store, prefix + "/contributions", ct);
        Utils.getMetadata(store, prefix + "/derived/author", authors);
    }

    public void setMetadata(MetadataStore store, String key, String prefix) throws BadFormatException, IncompatibleException {
        if (!key.startsWith(prefix) || store.getRegistry().getType(key) == null) return;
        Object value = store.getKey(key).getRawValue();
        if (key.equals(prefix + "/version")) version = Utils.makeString((Map<String, String>) value);
        if (key.equals(prefix + "/status")) status = Utils.makeSourceValue((Map<Map<String, String>, Map<String, String>>) value);
        if (key.equals(prefix + "/contributions")) {
            if (value == null) {
                contributions = new ArrayList<ContributionImpl>();
            } else {
                Map<Map<Map<String, String>, Map<String, String>>, Map<String, List<Map<String, String>>>> ct = (Map<Map<Map<String, String>, Map<String, String>>, Map<String, List<Map<String, String>>>>) value;
                contributions = new ArrayList<ContributionImpl>();
                for (Map.Entry<Map<Map<String, String>, Map<String, String>>, Map<String, List<Map<String, String>>>> e : ct.entrySet()) {
                    Map<Map<String, String>, Map<String, String>> sv = e.getKey();
                    Map<String, List<Map<String, String>>> wv = e.getValue();
                    ContributionImpl c = new ContributionImpl();
                    c.setRole(new SourceValueImpl(sv));
                    c.setWrangledValue(wv);
                    contributions.add(c);
                }
            }
        }
    }

    public LomLifecycleImpl reproduce() {
        LomLifecycleImpl out = new LomLifecycleImpl();
        if (version != null) out.version = version.reproduce();
        if (status != null) out.status = status.reproduce();
        for (ContributionImpl in : contributions) out.addContribution(in.reproduce());
        return out;
    }
}
