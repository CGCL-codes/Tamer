package org.vardb.explorer;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.vardb.converters.parsers.CFastaMultipleAlignmentParser;
import org.vardb.explorer.dao.*;
import org.vardb.resources.*;
import org.vardb.sequences.CSequences;
import org.vardb.sequences.ISequence;
import org.vardb.tags.CTags;

public class CExplorer {

    protected String m_identifier;

    protected String m_title;

    protected Map<String, Dataset> m_datasets = new LinkedHashMap<String, Dataset>();

    protected Map<String, Label> m_labels = new HashMap<String, Label>();

    public String getIdentifier() {
        return m_identifier;
    }

    public void setIdentifier(String identifier) {
        m_identifier = identifier;
    }

    public String getTitle() {
        return m_title;
    }

    public void setTitle(String title) {
        m_title = title;
    }

    public CExplorer(CExplorerData data) {
        m_identifier = data.getId();
        m_title = data.getTitle();
        m_datasets.put(data.getId(), new Dataset(data, false));
        for (CExplorerData subset : data.getSubsets()) {
            m_datasets.put(subset.getId(), new Dataset(subset, true));
        }
        for (CLabel label : data.getLabels()) {
            m_labels.put(label.getIdentifier(), new Label(label));
        }
    }

    public Collection<Dataset> getDatasets() {
        return m_datasets.values();
    }

    public Dataset getDataset(String identifier) {
        return m_datasets.get(identifier);
    }

    public Collection<Label> getLabels() {
        return m_labels.values();
    }

    public Label getLabel(String identifier) {
        return m_labels.get(identifier);
    }

    public static class Dataset {

        protected String m_identifier;

        protected String m_title;

        protected boolean m_subset;

        public String getIdentifier() {
            return m_identifier;
        }

        public void setIdentifier(final String identifier) {
            m_identifier = identifier;
        }

        public String getTitle() {
            return m_title;
        }

        public void setTitle(final String title) {
            m_title = title;
        }

        public boolean getSubset() {
            return m_subset;
        }

        public void setSubset(final boolean subset) {
            m_subset = subset;
        }

        public Dataset(CExplorerData subset, boolean isSubset) {
            m_identifier = subset.getId();
            m_title = subset.getTitle();
            m_subset = isSubset;
        }
    }

    public static class Label {

        protected Integer m_id;

        protected String m_identifier;

        protected String m_name;

        public Integer getId() {
            return m_id;
        }

        public void setId(final Integer id) {
            m_id = id;
        }

        public String getIdentifier() {
            return m_identifier;
        }

        public void setIdentifier(final String identifier) {
            m_identifier = identifier;
        }

        public String getName() {
            return m_name;
        }

        public void setName(final String name) {
            m_name = name;
        }

        public Label(CLabel label) {
            m_id = label.getId();
            m_identifier = label.getIdentifier();
            m_name = label.getName();
        }
    }
}
