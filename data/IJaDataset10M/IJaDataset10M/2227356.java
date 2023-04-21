package edu.mit.lcs.haystack.ozone.standard.widgets.parts;

import edu.mit.lcs.haystack.ozone.core.Context;
import edu.mit.lcs.haystack.ozone.core.IGUIHandler;
import edu.mit.lcs.haystack.ozone.core.IPart;
import edu.mit.lcs.haystack.ozone.core.IVisualPart;
import edu.mit.lcs.haystack.ozone.core.IdleRunnable;
import edu.mit.lcs.haystack.ozone.core.Ozone;
import edu.mit.lcs.haystack.ozone.core.OzoneConstants;
import edu.mit.lcs.haystack.ozone.core.SingleChildContainerPartBase;
import edu.mit.lcs.haystack.ozone.core.utils.ChildPartEvent;
import edu.mit.lcs.haystack.ozone.data.*;
import edu.mit.lcs.haystack.rdf.*;
import java.util.*;

/**
 * @version 	1.0
 * @author		David Huynh
 */
public class MultiplexPartContainerPart extends SingleChildContainerPartBase {

    protected ResourceDataConsumer m_dataConsumer;

    protected ResourceDataProviderWrapper m_dataProviderWrapper;

    protected List m_partDatas = new ArrayList();

    protected HashMap m_visualParts = new HashMap();

    protected boolean m_visible = true;

    public static final Resource s_multiplexPart = new Resource(PartConstants.s_namespace + "multiplexPart");

    static final Resource s_initialPart = new Resource(PartConstants.s_namespace + "initialPart");

    static org.apache.log4j.Logger s_logger = org.apache.log4j.Logger.getLogger(MultiplexPartContainerPart.class);

    public void initializeFromDeserialization(IRDFContainer source) {
        IVisualPart child = m_child;
        m_child = null;
        super.initializeFromDeserialization(source);
        m_child = child;
        if (m_dataProviderWrapper != null) {
            m_dataProviderWrapper.getDataProvider().initializeFromDeserialization(source);
        }
        Iterator i = m_visualParts.values().iterator();
        while (i.hasNext()) {
            IPart part = (IPart) i.next();
            part.initializeFromDeserialization(source);
        }
        if (m_dataProviderWrapper != null) {
            Ozone.idleExec(new IdleRunnable() {

                public void run() {
                    try {
                        if (m_dataProviderWrapper != null) {
                            Resource res = m_dataProviderWrapper.getResource();
                            if (res != null) {
                                internalSetPart(res, false);
                            }
                        }
                    } catch (DataNotAvailableException e) {
                        e.printStackTrace();
                    } catch (DataMismatchException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
	 * @see IPart#dispose()
	 */
    public void dispose() {
        if (m_dataProviderWrapper != null) {
            m_dataProviderWrapper.getDataProvider().unregisterConsumer(m_dataConsumer);
            m_dataProviderWrapper.dispose();
            m_dataProviderWrapper = null;
        }
        m_dataConsumer = null;
        Iterator i = m_visualParts.values().iterator();
        while (i.hasNext()) {
            IVisualPart vp = (IVisualPart) i.next();
            vp.dispose();
        }
        m_visualParts.clear();
        m_partDatas.clear();
        m_visualParts = null;
        m_partDatas = null;
        m_child = null;
        super.dispose();
    }

    public void setPart(Resource resource) {
        if (Ozone.isUIThread()) {
            internalSetPart(resource, true);
        } else {
            Ozone.idleExec(new IdleRunnable(m_context) {

                Resource m_resource;

                public IdleRunnable initialize(Resource resource) {
                    m_resource = resource;
                    return this;
                }

                public void run() {
                    internalSetPart(m_resource, true);
                }
            }.initialize(resource));
        }
    }

    public void setPartByIndex(int index) {
        if (Ozone.isUIThread()) {
            internalSetPartByIndex(index, true);
        } else {
            Ozone.idleExec(new IdleRunnable(m_context) {

                int m_index;

                public IdleRunnable initialize(int index) {
                    m_index = index;
                    return this;
                }

                public void run() {
                    internalSetPartByIndex(m_index, true);
                }
            }.initialize(index));
        }
    }

    protected void internalInitialize() {
        super.internalInitialize();
        m_context.putProperty(s_multiplexPart, this);
        retrieveChildPartData();
        Resource dataSource = Utilities.getResourceProperty(m_prescription, OzoneConstants.s_dataSource, m_partDataSource);
        if (dataSource == null) {
            dataSource = Utilities.getResourceProperty(m_resPart, OzoneConstants.s_dataSource, m_source);
        }
        if (dataSource != null) {
            IDataProvider dataProvider = DataUtilities.createDataProvider(dataSource, m_context, m_source, m_partDataSource);
            if (dataProvider != null) {
                m_dataProviderWrapper = new ResourceDataProviderWrapper(dataProvider);
                m_dataConsumer = new ResourceDataConsumer() {

                    protected void onResourceChanged(Resource newResource) {
                        {
                            Ozone.idleExec(new IdleRunnable(m_context) {

                                Resource m_resource;

                                public IdleRunnable initialize(Resource resource) {
                                    m_resource = resource;
                                    return this;
                                }

                                public void run() {
                                    if (m_visualParts != null) {
                                        internalSetPart(m_resource, false);
                                    }
                                }
                            }.initialize(newResource));
                        }
                    }

                    protected void onResourceDeleted(Resource previousResource) {
                    }
                };
                dataProvider.registerConsumer(m_dataConsumer);
            }
        } else {
            Resource initialPart = Utilities.getResourceProperty(m_prescription, s_initialPart, m_source);
            if (initialPart != null) {
                internalSetPart(initialPart, false);
            } else {
                internalSetPartByIndex(0, false);
            }
        }
    }

    protected void retrieveChildPartData() {
        try {
            Iterator i = ListUtilities.accessDAMLList(Utilities.getResourceProperty(m_prescription, OzoneConstants.s_children, m_source), m_source);
            while (i.hasNext()) {
                Resource partData = (Resource) i.next();
                m_partDatas.add(partData);
            }
        } catch (Exception e) {
        }
    }

    protected IVisualPart createChildPart(Resource partData) {
        try {
            Resource part = Ozone.findPart(partData, m_source, m_partDataSource);
            Class c = Utilities.loadClass(part, m_source);
            IVisualPart visualPart = (IVisualPart) c.newInstance();
            if (visualPart != null) {
                Context childContext = new Context(m_context);
                childContext.putLocalProperty(OzoneConstants.s_parentPart, this);
                childContext.putLocalProperty(OzoneConstants.s_partData, partData);
                childContext.putLocalProperty(OzoneConstants.s_part, part);
                visualPart.initialize(m_source, childContext);
                m_visualParts.put(partData, visualPart);
                return visualPart;
            }
        } catch (Exception e) {
        }
        return null;
    }

    protected void internalSetPart(Resource partData, boolean notify) {
        IVisualPart newVisualPart = (IVisualPart) m_visualParts.get(partData);
        if (newVisualPart == null) {
            newVisualPart = createChildPart(partData);
        }
        if (newVisualPart != m_child) {
            setVisible(m_child, false);
            m_child = newVisualPart;
            setVisible(newVisualPart, true);
            onChildResize(new ChildPartEvent(this));
            if (m_dataProviderWrapper != null) {
                try {
                    m_source.replace(m_prescription, s_initialPart, null, partData);
                } catch (RDFException e) {
                    s_logger.error("Failed to set initialPart for " + m_prescription, e);
                }
            }
        }
        if (notify && m_dataProviderWrapper != null) {
            try {
                m_dataProviderWrapper.requestResourceSet(partData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void internalSetPartByIndex(int i, boolean notify) {
        if (i >= 0 && i < m_partDatas.size()) {
            internalSetPart((Resource) m_partDatas.get(i), notify);
        }
    }

    protected void setVisible(IVisualPart vp, boolean visible) {
        if (vp != null) {
            IGUIHandler gh = vp.getGUIHandler(null);
            if (gh != null) {
                gh.setVisible(visible && m_visible);
            }
        }
    }

    public void setVisible(boolean visible) {
        m_visible = visible;
        super.setVisible(visible);
    }
}
