package edu.mit.lcs.haystack.ozone.standard.widgets.parts;

import edu.mit.lcs.haystack.ozone.core.Context;
import edu.mit.lcs.haystack.ozone.core.OzoneConstants;
import edu.mit.lcs.haystack.ozone.core.utils.GenericPart;
import edu.mit.lcs.haystack.ozone.data.AdenineDataConsumer;
import edu.mit.lcs.haystack.ozone.data.DataUtilities;
import edu.mit.lcs.haystack.ozone.data.IDataProvider;
import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.rdf.Resource;
import edu.mit.lcs.haystack.rdf.Utilities;

/**
 * @author Dennis Quan
 */
public class DataConsumerPart extends GenericPart {

    protected AdenineDataConsumer m_consumer;

    protected IDataProvider m_dataProvider;

    protected boolean m_ownsDataProvider = false;

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.IPart#initialize(edu.mit.lcs.haystack.rdf.IRDFContainer, edu.mit.lcs.haystack.ozone.core.Context)
	 */
    public void initialize(IRDFContainer source, Context context) {
        setupSources(source, context);
        m_dataProvider = (IDataProvider) m_context.getLocalProperty(OzoneConstants.s_dataProvider);
        if (m_dataProvider == null) {
            Resource chainedDataSource = (Resource) Utilities.getResourceProperty(m_prescription, OzoneConstants.s_dataSource, m_partDataSource);
            if (chainedDataSource != null) {
                m_dataProvider = DataUtilities.createDataProvider(chainedDataSource, m_context, m_source, m_partDataSource);
                m_ownsDataProvider = m_dataProvider != null;
            }
        }
        if (m_dataProvider != null) {
            m_consumer = new AdenineDataConsumer(source, context, Utilities.getResourceProperty(m_prescription, PartConstants.s_onDataChanged, m_partDataSource), Utilities.getResourceProperty(m_prescription, PartConstants.s_onStatusChanged, m_partDataSource), Utilities.getResourceProperty(m_prescription, PartConstants.s_reset, m_partDataSource));
            m_dataProvider.registerConsumer(m_consumer);
        }
    }

    /**
	 * @see edu.mit.lcs.haystack.ozone.core.IPart#dispose()
	 */
    public void dispose() {
        super.dispose();
        if (m_ownsDataProvider) {
            m_dataProvider.dispose();
        }
        m_dataProvider = null;
    }

    public void initializeFromDeserialization(IRDFContainer source) {
        super.initializeFromDeserialization(source);
        m_consumer.initializeFromDeserialization(source);
        if (m_ownsDataProvider) {
            m_dataProvider.initializeFromDeserialization(source);
        }
    }
}
