package net.randomice.gengmf.impl;

import java.util.Collection;
import net.randomice.gengmf.GengmfPackage;
import net.randomice.gengmf.Model;
import net.randomice.gengmf.desc.AbstractEdgeDesc;
import net.randomice.gengmf.desc.CompartmentDesc;
import net.randomice.gengmf.desc.DescPackage;
import net.randomice.gengmf.desc.NodeDesc;
import net.randomice.gengmf.template.FigureTemplate;
import net.randomice.gengmf.template.TemplatePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.gmf.tooldef.ToolRegistry;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Model</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link net.randomice.gengmf.impl.ModelImpl#getRootElement <em>Root
 * Element</em>}</li>
 * <li>{@link net.randomice.gengmf.impl.ModelImpl#getNodes <em>Nodes</em>}</li>
 * <li>{@link net.randomice.gengmf.impl.ModelImpl#getEdges <em>Edges</em>}</li>
 * <li>{@link net.randomice.gengmf.impl.ModelImpl#getCompartments <em>
 * Compartments</em>}</li>
 * <li>{@link net.randomice.gengmf.impl.ModelImpl#getFigureTemplates <em>Figure
 * Templates</em>}</li>
 * <li>{@link net.randomice.gengmf.impl.ModelImpl#getTools <em>Tools</em>}</li>
 * <li>{@link net.randomice.gengmf.impl.ModelImpl#getCreationTransformatorExt
 * <em>Creation Transformator Ext</em>}</li>
 * <li>{@link net.randomice.gengmf.impl.ModelImpl#getPostProcTransformatorExt
 * <em>Post Proc Transformator Ext</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ModelImpl extends NamedImpl implements Model {

    /**
	 * The cached value of the '{@link #getRootElement() <em>Root Element</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRootElement()
	 * @generated
	 * @ordered
	 */
    protected EClass rootElement;

    /**
	 * The cached value of the '{@link #getNodes() <em>Nodes</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getNodes()
	 * @generated
	 * @ordered
	 */
    protected EList<NodeDesc> nodes;

    /**
	 * The cached value of the '{@link #getEdges() <em>Edges</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getEdges()
	 * @generated
	 * @ordered
	 */
    protected EList<AbstractEdgeDesc> edges;

    /**
	 * The cached value of the '{@link #getCompartments() <em>Compartments</em>}
	 * ' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getCompartments()
	 * @generated
	 * @ordered
	 */
    protected EList<CompartmentDesc> compartments;

    /**
	 * The cached value of the '{@link #getFigureTemplates()
	 * <em>Figure Templates</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFigureTemplates()
	 * @generated
	 * @ordered
	 */
    protected EList<FigureTemplate> figureTemplates;

    /**
	 * The cached value of the '{@link #getTools() <em>Tools</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTools()
	 * @generated
	 * @ordered
	 */
    protected ToolRegistry tools;

    /**
	 * The default value of the '{@link #getCreationTransformatorExt()
	 * <em>Creation Transformator Ext</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getCreationTransformatorExt()
	 * @generated
	 * @ordered
	 */
    protected static final String CREATION_TRANSFORMATOR_EXT_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getCreationTransformatorExt()
	 * <em>Creation Transformator Ext</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getCreationTransformatorExt()
	 * @generated
	 * @ordered
	 */
    protected String creationTransformatorExt = CREATION_TRANSFORMATOR_EXT_EDEFAULT;

    /**
	 * The default value of the '{@link #getPostProcTransformatorExt()
	 * <em>Post Proc Transformator Ext</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getPostProcTransformatorExt()
	 * @generated
	 * @ordered
	 */
    protected static final String POST_PROC_TRANSFORMATOR_EXT_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getPostProcTransformatorExt()
	 * <em>Post Proc Transformator Ext</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getPostProcTransformatorExt()
	 * @generated
	 * @ordered
	 */
    protected String postProcTransformatorExt = POST_PROC_TRANSFORMATOR_EXT_EDEFAULT;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected ModelImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return GengmfPackage.Literals.MODEL;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EClass getRootElement() {
        if (rootElement != null && rootElement.eIsProxy()) {
            InternalEObject oldRootElement = (InternalEObject) rootElement;
            rootElement = (EClass) eResolveProxy(oldRootElement);
            if (rootElement != oldRootElement) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, GengmfPackage.MODEL__ROOT_ELEMENT, oldRootElement, rootElement));
            }
        }
        return rootElement;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EClass basicGetRootElement() {
        return rootElement;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setRootElement(EClass newRootElement) {
        EClass oldRootElement = rootElement;
        rootElement = newRootElement;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, GengmfPackage.MODEL__ROOT_ELEMENT, oldRootElement, rootElement));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EList<NodeDesc> getNodes() {
        if (nodes == null) {
            nodes = new EObjectContainmentWithInverseEList<NodeDesc>(NodeDesc.class, this, GengmfPackage.MODEL__NODES, DescPackage.NODE_DESC__MODEL);
        }
        return nodes;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EList<AbstractEdgeDesc> getEdges() {
        if (edges == null) {
            edges = new EObjectContainmentWithInverseEList<AbstractEdgeDesc>(AbstractEdgeDesc.class, this, GengmfPackage.MODEL__EDGES, DescPackage.ABSTRACT_EDGE_DESC__MODEL);
        }
        return edges;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EList<CompartmentDesc> getCompartments() {
        if (compartments == null) {
            compartments = new EObjectContainmentWithInverseEList<CompartmentDesc>(CompartmentDesc.class, this, GengmfPackage.MODEL__COMPARTMENTS, DescPackage.COMPARTMENT_DESC__MODEL);
        }
        return compartments;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public EList<FigureTemplate> getFigureTemplates() {
        if (figureTemplates == null) {
            figureTemplates = new EObjectContainmentWithInverseEList<FigureTemplate>(FigureTemplate.class, this, GengmfPackage.MODEL__FIGURE_TEMPLATES, TemplatePackage.FIGURE_TEMPLATE__MODEL);
        }
        return figureTemplates;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public ToolRegistry getTools() {
        if (tools != null && tools.eIsProxy()) {
            InternalEObject oldTools = (InternalEObject) tools;
            tools = (ToolRegistry) eResolveProxy(oldTools);
            if (tools != oldTools) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, GengmfPackage.MODEL__TOOLS, oldTools, tools));
            }
        }
        return tools;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public ToolRegistry basicGetTools() {
        return tools;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setTools(ToolRegistry newTools) {
        ToolRegistry oldTools = tools;
        tools = newTools;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, GengmfPackage.MODEL__TOOLS, oldTools, tools));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public String getCreationTransformatorExt() {
        return creationTransformatorExt;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setCreationTransformatorExt(String newCreationTransformatorExt) {
        String oldCreationTransformatorExt = creationTransformatorExt;
        creationTransformatorExt = newCreationTransformatorExt;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, GengmfPackage.MODEL__CREATION_TRANSFORMATOR_EXT, oldCreationTransformatorExt, creationTransformatorExt));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public String getPostProcTransformatorExt() {
        return postProcTransformatorExt;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void setPostProcTransformatorExt(String newPostProcTransformatorExt) {
        String oldPostProcTransformatorExt = postProcTransformatorExt;
        postProcTransformatorExt = newPostProcTransformatorExt;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, GengmfPackage.MODEL__POST_PROC_TRANSFORMATOR_EXT, oldPostProcTransformatorExt, postProcTransformatorExt));
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case GengmfPackage.MODEL__NODES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getNodes()).basicAdd(otherEnd, msgs);
            case GengmfPackage.MODEL__EDGES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getEdges()).basicAdd(otherEnd, msgs);
            case GengmfPackage.MODEL__COMPARTMENTS:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getCompartments()).basicAdd(otherEnd, msgs);
            case GengmfPackage.MODEL__FIGURE_TEMPLATES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getFigureTemplates()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case GengmfPackage.MODEL__NODES:
                return ((InternalEList<?>) getNodes()).basicRemove(otherEnd, msgs);
            case GengmfPackage.MODEL__EDGES:
                return ((InternalEList<?>) getEdges()).basicRemove(otherEnd, msgs);
            case GengmfPackage.MODEL__COMPARTMENTS:
                return ((InternalEList<?>) getCompartments()).basicRemove(otherEnd, msgs);
            case GengmfPackage.MODEL__FIGURE_TEMPLATES:
                return ((InternalEList<?>) getFigureTemplates()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case GengmfPackage.MODEL__ROOT_ELEMENT:
                if (resolve) return getRootElement();
                return basicGetRootElement();
            case GengmfPackage.MODEL__NODES:
                return getNodes();
            case GengmfPackage.MODEL__EDGES:
                return getEdges();
            case GengmfPackage.MODEL__COMPARTMENTS:
                return getCompartments();
            case GengmfPackage.MODEL__FIGURE_TEMPLATES:
                return getFigureTemplates();
            case GengmfPackage.MODEL__TOOLS:
                if (resolve) return getTools();
                return basicGetTools();
            case GengmfPackage.MODEL__CREATION_TRANSFORMATOR_EXT:
                return getCreationTransformatorExt();
            case GengmfPackage.MODEL__POST_PROC_TRANSFORMATOR_EXT:
                return getPostProcTransformatorExt();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case GengmfPackage.MODEL__ROOT_ELEMENT:
                setRootElement((EClass) newValue);
                return;
            case GengmfPackage.MODEL__NODES:
                getNodes().clear();
                getNodes().addAll((Collection<? extends NodeDesc>) newValue);
                return;
            case GengmfPackage.MODEL__EDGES:
                getEdges().clear();
                getEdges().addAll((Collection<? extends AbstractEdgeDesc>) newValue);
                return;
            case GengmfPackage.MODEL__COMPARTMENTS:
                getCompartments().clear();
                getCompartments().addAll((Collection<? extends CompartmentDesc>) newValue);
                return;
            case GengmfPackage.MODEL__FIGURE_TEMPLATES:
                getFigureTemplates().clear();
                getFigureTemplates().addAll((Collection<? extends FigureTemplate>) newValue);
                return;
            case GengmfPackage.MODEL__TOOLS:
                setTools((ToolRegistry) newValue);
                return;
            case GengmfPackage.MODEL__CREATION_TRANSFORMATOR_EXT:
                setCreationTransformatorExt((String) newValue);
                return;
            case GengmfPackage.MODEL__POST_PROC_TRANSFORMATOR_EXT:
                setPostProcTransformatorExt((String) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case GengmfPackage.MODEL__ROOT_ELEMENT:
                setRootElement((EClass) null);
                return;
            case GengmfPackage.MODEL__NODES:
                getNodes().clear();
                return;
            case GengmfPackage.MODEL__EDGES:
                getEdges().clear();
                return;
            case GengmfPackage.MODEL__COMPARTMENTS:
                getCompartments().clear();
                return;
            case GengmfPackage.MODEL__FIGURE_TEMPLATES:
                getFigureTemplates().clear();
                return;
            case GengmfPackage.MODEL__TOOLS:
                setTools((ToolRegistry) null);
                return;
            case GengmfPackage.MODEL__CREATION_TRANSFORMATOR_EXT:
                setCreationTransformatorExt(CREATION_TRANSFORMATOR_EXT_EDEFAULT);
                return;
            case GengmfPackage.MODEL__POST_PROC_TRANSFORMATOR_EXT:
                setPostProcTransformatorExt(POST_PROC_TRANSFORMATOR_EXT_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case GengmfPackage.MODEL__ROOT_ELEMENT:
                return rootElement != null;
            case GengmfPackage.MODEL__NODES:
                return nodes != null && !nodes.isEmpty();
            case GengmfPackage.MODEL__EDGES:
                return edges != null && !edges.isEmpty();
            case GengmfPackage.MODEL__COMPARTMENTS:
                return compartments != null && !compartments.isEmpty();
            case GengmfPackage.MODEL__FIGURE_TEMPLATES:
                return figureTemplates != null && !figureTemplates.isEmpty();
            case GengmfPackage.MODEL__TOOLS:
                return tools != null;
            case GengmfPackage.MODEL__CREATION_TRANSFORMATOR_EXT:
                return CREATION_TRANSFORMATOR_EXT_EDEFAULT == null ? creationTransformatorExt != null : !CREATION_TRANSFORMATOR_EXT_EDEFAULT.equals(creationTransformatorExt);
            case GengmfPackage.MODEL__POST_PROC_TRANSFORMATOR_EXT:
                return POST_PROC_TRANSFORMATOR_EXT_EDEFAULT == null ? postProcTransformatorExt != null : !POST_PROC_TRANSFORMATOR_EXT_EDEFAULT.equals(postProcTransformatorExt);
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (creationTransformatorExt: ");
        result.append(creationTransformatorExt);
        result.append(", postProcTransformatorExt: ");
        result.append(postProcTransformatorExt);
        result.append(')');
        return result.toString();
    }
}
