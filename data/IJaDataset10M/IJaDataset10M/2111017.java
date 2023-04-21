package uk.ac.cam.caret.qticp.impl;

import java.util.*;
import uk.ac.cam.caret.minibix.general.svo.api.SvoPreferences;
import uk.ac.cam.caret.minibix.general.svo.api.SvoReference;
import uk.ac.cam.caret.minibix.metadata.api.BadFormatException;
import uk.ac.cam.caret.minibix.metadata.api.IncompatibleException;
import uk.ac.cam.caret.minibix.metadata.api.MetadataKey;
import uk.ac.cam.caret.minibix.metadata.api.MetadataStore;
import uk.ac.cam.caret.minibix.metadata.api.TypeRegistry;
import uk.ac.cam.caret.minibix.metadata.impl.TypeRegistryImpl;
import uk.ac.cam.caret.qticp.api.*;

public class QTIMetadataImpl implements QTIMetadata {

    private QTIParserFactoryFactoryImpl factory;

    private static final String QTICP_URI_BASE = "http://www.caret.cam.ac.uk/minibix/cp-qti";

    private String composite, feedback_type, item_template, solution_available, time_dependent, tool_name, tool_vendor, tool_version;

    private List<String> interaction_type = new ArrayList<String>();

    private QTICPCreator creator;

    QTIMetadataImpl(QTIParserFactoryFactoryImpl in) {
        factory = in;
    }

    QTIMetadataImpl(QTICPCreatorImpl creator) {
        this.creator = creator;
        factory = creator.getFactory();
    }

    QTIParserFactoryFactoryImpl getFactory() {
        return factory;
    }

    public static TypeRegistry md_register(String prefix) {
        prefix = QTICP_URI_BASE;
        TypeRegistry reg = new TypeRegistryImpl();
        reg.register(prefix + "/item-template", "i");
        reg.register(prefix + "/time-dependent", "i");
        reg.register(prefix + "/composite", "i");
        reg.register(prefix + "/interaction-type", "iL");
        reg.register(prefix + "/feedback-type", "i");
        reg.register(prefix + "/solution-available", "i");
        reg.register(prefix + "/tool-name", "i");
        reg.register(prefix + "/tool-version", "i");
        reg.register(prefix + "/tool-vendor", "i");
        return reg;
    }

    public String getComposite() {
        return composite;
    }

    public String getFeedbackType() {
        return feedback_type;
    }

    public String[] getInteractionType() {
        return interaction_type.toArray(new String[0]);
    }

    public String getItemTemplate() {
        return item_template;
    }

    public String getSolutionAvailable() {
        return solution_available;
    }

    public String getTimeDependent() {
        return time_dependent;
    }

    public String getToolName() {
        return tool_name;
    }

    public String getToolVendor() {
        return tool_vendor;
    }

    public String getToolVersion() {
        return tool_version;
    }

    public QTIMetadataImpl reproduce() {
        QTIMetadataImpl out = new QTIMetadataImpl(factory);
        out.setComposite(getComposite());
        out.setFeedbackType(getFeedbackType());
        out.setItemTemplate(getItemTemplate());
        out.setSolutionAvailable(getSolutionAvailable());
        out.setTimeDependent(getTimeDependent());
        out.setToolName(getToolName());
        out.setToolVendor(getToolVendor());
        out.setToolVersion(getToolVersion());
        out.setInteractionType(getInteractionType());
        return out;
    }

    private void addMetadata(MetadataStore store, String key, Object value) throws BadFormatException, IncompatibleException {
        MetadataKey mk = store.getOrCreateKey(key);
        if (value != null) mk.setRawValue(value);
    }

    public void buildMetadata(String prefix, MetadataStore store) throws BadFormatException, IncompatibleException {
        prefix = QTICP_URI_BASE;
        store.addRegistry(md_register(prefix));
        addMetadata(store, prefix + "/item-template", item_template);
        addMetadata(store, prefix + "/time-dependent", time_dependent);
        addMetadata(store, prefix + "/composite", composite);
        addMetadata(store, prefix + "/interaction-type", interaction_type);
        addMetadata(store, prefix + "/feedback-type", feedback_type);
        addMetadata(store, prefix + "/solution-available", solution_available);
        addMetadata(store, prefix + "/tool-name", tool_name);
        addMetadata(store, prefix + "/tool-version", tool_version);
        addMetadata(store, prefix + "/tool-vendor", tool_vendor);
    }

    public void setMetadata(MetadataStore store, String key, String prefix) throws BadFormatException, IncompatibleException {
        prefix = QTICP_URI_BASE;
        if (!key.startsWith(prefix) || store.getRegistry().getType(key) == null) return;
        Object value = store.getKey(key).getRawValue();
        if (key.equals(prefix + "/item-template")) item_template = (String) value;
        if (key.equals(prefix + "/time-dependent")) time_dependent = (String) value;
        if (key.equals(prefix + "/composite")) composite = (String) value;
        if (key.equals(prefix + "/interaction-type")) interaction_type = (List<String>) value;
        if (key.equals(prefix + "/feedback-type")) feedback_type = (String) value;
        if (key.equals(prefix + "/solution-available")) solution_available = (String) value;
        if (key.equals(prefix + "/tool-name")) feedback_type = (String) value;
        if (key.equals(prefix + "/tool-version")) tool_version = (String) value;
        if (key.equals(prefix + "/tool-vendor")) tool_vendor = (String) value;
    }

    public void setItemTemplate(String in) {
        item_template = in;
    }

    public void setTimeDependent(String in) {
        time_dependent = in;
    }

    public void setComposite(String in) {
        composite = in;
    }

    public void setInteractionType(String[] in) {
        interaction_type = new ArrayList<String>(Arrays.asList(in));
    }

    public void addInteractionType(String in) {
        interaction_type.add(in);
    }

    public void setFeedbackType(String in) {
        feedback_type = in;
    }

    public void setSolutionAvailable(String in) {
        solution_available = in;
    }

    public void setToolName(String in) {
        tool_name = in;
    }

    public void setToolVersion(String in) {
        tool_version = in;
    }

    public void setToolVendor(String in) {
        tool_vendor = in;
    }

    public QTICPCreator getCreator() {
        if (creator == null) creator = new QTICPCreatorImpl(this);
        return creator;
    }

    private void addRelationS(SvoReference in, String key, String value) {
        if (value != null) in.addRelationS(key, value);
    }

    public void buildSvo(SvoReference in, String prefix, SvoPreferences prefs) {
        if (!"".equals(prefix)) prefix += ".";
        addRelationS(in, prefix + "qticp.item-template", item_template);
        addRelationS(in, prefix + "qticp.time-dependent", time_dependent);
        addRelationS(in, prefix + "qticp.composite", composite);
        for (String it : interaction_type) addRelationS(in, prefix + "qticp.interaction-type", it);
        addRelationS(in, prefix + "qticp.feedback-type", feedback_type);
        addRelationS(in, prefix + "qticp.solution-available", solution_available);
        addRelationS(in, prefix + "qticp.tool-name", tool_name);
        addRelationS(in, prefix + "qticp.tool-version", tool_version);
        addRelationS(in, prefix + "qticp.tool-vendor", tool_vendor);
    }
}
