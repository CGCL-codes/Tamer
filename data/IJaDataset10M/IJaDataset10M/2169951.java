package tefkat.config.TefkatConfig;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see tefkat.config.TefkatConfig.TefkatConfigFactory
 * @model kind="package"
 *        annotation="Copyright Copyright='michael lawley' Licence='Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA'"
 * @generated
 */
public interface TefkatConfigPackage extends EPackage {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String copyright = "Copyright michael lawley Pty Ltd 2004-2005";

    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "TefkatConfig";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.dstc.edu.au:8080/qvt/TefkatConfig.ecore";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "config";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    TefkatConfigPackage eINSTANCE = tefkat.config.TefkatConfig.impl.TefkatConfigPackageImpl.init();

    /**
     * The meta object id for the '{@link tefkat.config.TefkatConfig.impl.ConfigurationImpl <em>Configuration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tefkat.config.TefkatConfig.impl.ConfigurationImpl
     * @see tefkat.config.TefkatConfig.impl.TefkatConfigPackageImpl#getConfiguration()
     * @generated
     */
    int CONFIGURATION = 0;

    /**
     * The feature id for the '<em><b>Transformation Tasks</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONFIGURATION__TRANSFORMATION_TASKS = 0;

    /**
     * The feature id for the '<em><b>Package Classes</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONFIGURATION__PACKAGE_CLASSES = 1;

    /**
     * The number of structural features of the '<em>Configuration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONFIGURATION_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link tefkat.config.TefkatConfig.impl.TransformationTaskImpl <em>Transformation Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tefkat.config.TefkatConfig.impl.TransformationTaskImpl
     * @see tefkat.config.TefkatConfig.impl.TefkatConfigPackageImpl#getTransformationTask()
     * @generated
     */
    int TRANSFORMATION_TASK = 1;

    /**
     * The feature id for the '<em><b>Transformation</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSFORMATION_TASK__TRANSFORMATION = 0;

    /**
     * The feature id for the '<em><b>Source Models</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSFORMATION_TASK__SOURCE_MODELS = 1;

    /**
     * The feature id for the '<em><b>Target Models</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSFORMATION_TASK__TARGET_MODELS = 2;

    /**
     * The feature id for the '<em><b>Trace</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSFORMATION_TASK__TRACE = 3;

    /**
     * The feature id for the '<em><b>Enabled</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSFORMATION_TASK__ENABLED = 4;

    /**
     * The feature id for the '<em><b>Mode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSFORMATION_TASK__MODE = 5;

    /**
     * The feature id for the '<em><b>Properties</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSFORMATION_TASK__PROPERTIES = 6;

    /**
     * The feature id for the '<em><b>Uri Map</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSFORMATION_TASK__URI_MAP = 7;

    /**
     * The number of structural features of the '<em>Transformation Task</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TRANSFORMATION_TASK_FEATURE_COUNT = 8;

    /**
     * The meta object id for the '{@link tefkat.config.TefkatConfig.impl.ModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tefkat.config.TefkatConfig.impl.ModelImpl
     * @see tefkat.config.TefkatConfig.impl.TefkatConfigPackageImpl#getModel()
     * @generated
     */
    int MODEL = 2;

    /**
     * The feature id for the '<em><b>Location Uri</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL__LOCATION_URI = 0;

    /**
     * The feature id for the '<em><b>Var Group</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL__VAR_GROUP = 1;

    /**
     * The number of structural features of the '<em>Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MODEL_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link tefkat.config.TefkatConfig.impl.URIMapEntryImpl <em>URI Map Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tefkat.config.TefkatConfig.impl.URIMapEntryImpl
     * @see tefkat.config.TefkatConfig.impl.TefkatConfigPackageImpl#getURIMapEntry()
     * @generated
     */
    int URI_MAP_ENTRY = 3;

    /**
     * The feature id for the '<em><b>Key</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int URI_MAP_ENTRY__KEY = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int URI_MAP_ENTRY__VALUE = 1;

    /**
     * The number of structural features of the '<em>URI Map Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int URI_MAP_ENTRY_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link tefkat.config.TefkatConfig.impl.PropertyImpl <em>Property</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tefkat.config.TefkatConfig.impl.PropertyImpl
     * @see tefkat.config.TefkatConfig.impl.TefkatConfigPackageImpl#getProperty()
     * @generated
     */
    int PROPERTY = 4;

    /**
     * The feature id for the '<em><b>Key</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__KEY = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY__VALUE = 1;

    /**
     * The number of structural features of the '<em>Property</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROPERTY_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link tefkat.config.TefkatConfig.ExecutionMode <em>Execution Mode</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tefkat.config.TefkatConfig.ExecutionMode
     * @see tefkat.config.TefkatConfig.impl.TefkatConfigPackageImpl#getExecutionMode()
     * @generated
     */
    int EXECUTION_MODE = 5;

    /**
     * Returns the meta object for class '{@link tefkat.config.TefkatConfig.Configuration <em>Configuration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Configuration</em>'.
     * @see tefkat.config.TefkatConfig.Configuration
     * @generated
     */
    EClass getConfiguration();

    /**
     * Returns the meta object for the containment reference list '{@link tefkat.config.TefkatConfig.Configuration#getTransformationTasks <em>Transformation Tasks</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Transformation Tasks</em>'.
     * @see tefkat.config.TefkatConfig.Configuration#getTransformationTasks()
     * @see #getConfiguration()
     * @generated
     */
    EReference getConfiguration_TransformationTasks();

    /**
     * Returns the meta object for the attribute list '{@link tefkat.config.TefkatConfig.Configuration#getPackageClasses <em>Package Classes</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Package Classes</em>'.
     * @see tefkat.config.TefkatConfig.Configuration#getPackageClasses()
     * @see #getConfiguration()
     * @generated
     */
    EAttribute getConfiguration_PackageClasses();

    /**
     * Returns the meta object for class '{@link tefkat.config.TefkatConfig.TransformationTask <em>Transformation Task</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Transformation Task</em>'.
     * @see tefkat.config.TefkatConfig.TransformationTask
     * @generated
     */
    EClass getTransformationTask();

    /**
     * Returns the meta object for the containment reference '{@link tefkat.config.TefkatConfig.TransformationTask#getTransformation <em>Transformation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Transformation</em>'.
     * @see tefkat.config.TefkatConfig.TransformationTask#getTransformation()
     * @see #getTransformationTask()
     * @generated
     */
    EReference getTransformationTask_Transformation();

    /**
     * Returns the meta object for the containment reference list '{@link tefkat.config.TefkatConfig.TransformationTask#getSourceModels <em>Source Models</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Source Models</em>'.
     * @see tefkat.config.TefkatConfig.TransformationTask#getSourceModels()
     * @see #getTransformationTask()
     * @generated
     */
    EReference getTransformationTask_SourceModels();

    /**
     * Returns the meta object for the containment reference list '{@link tefkat.config.TefkatConfig.TransformationTask#getTargetModels <em>Target Models</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Target Models</em>'.
     * @see tefkat.config.TefkatConfig.TransformationTask#getTargetModels()
     * @see #getTransformationTask()
     * @generated
     */
    EReference getTransformationTask_TargetModels();

    /**
     * Returns the meta object for the containment reference '{@link tefkat.config.TefkatConfig.TransformationTask#getTrace <em>Trace</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Trace</em>'.
     * @see tefkat.config.TefkatConfig.TransformationTask#getTrace()
     * @see #getTransformationTask()
     * @generated
     */
    EReference getTransformationTask_Trace();

    /**
     * Returns the meta object for the attribute '{@link tefkat.config.TefkatConfig.TransformationTask#isEnabled <em>Enabled</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Enabled</em>'.
     * @see tefkat.config.TefkatConfig.TransformationTask#isEnabled()
     * @see #getTransformationTask()
     * @generated
     */
    EAttribute getTransformationTask_Enabled();

    /**
     * Returns the meta object for the attribute '{@link tefkat.config.TefkatConfig.TransformationTask#getMode <em>Mode</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Mode</em>'.
     * @see tefkat.config.TefkatConfig.TransformationTask#getMode()
     * @see #getTransformationTask()
     * @generated
     */
    EAttribute getTransformationTask_Mode();

    /**
     * Returns the meta object for the map '{@link tefkat.config.TefkatConfig.TransformationTask#getProperties <em>Properties</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>Properties</em>'.
     * @see tefkat.config.TefkatConfig.TransformationTask#getProperties()
     * @see #getTransformationTask()
     * @generated
     */
    EReference getTransformationTask_Properties();

    /**
     * Returns the meta object for the map '{@link tefkat.config.TefkatConfig.TransformationTask#getUriMap <em>Uri Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>Uri Map</em>'.
     * @see tefkat.config.TefkatConfig.TransformationTask#getUriMap()
     * @see #getTransformationTask()
     * @generated
     */
    EReference getTransformationTask_UriMap();

    /**
     * Returns the meta object for class '{@link tefkat.config.TefkatConfig.Model <em>Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model</em>'.
     * @see tefkat.config.TefkatConfig.Model
     * @generated
     */
    EClass getModel();

    /**
     * Returns the meta object for the attribute '{@link tefkat.config.TefkatConfig.Model#getLocationUri <em>Location Uri</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Location Uri</em>'.
     * @see tefkat.config.TefkatConfig.Model#getLocationUri()
     * @see #getModel()
     * @generated
     */
    EAttribute getModel_LocationUri();

    /**
     * Returns the meta object for the attribute '{@link tefkat.config.TefkatConfig.Model#getVarGroup <em>Var Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Var Group</em>'.
     * @see tefkat.config.TefkatConfig.Model#getVarGroup()
     * @see #getModel()
     * @generated
     */
    EAttribute getModel_VarGroup();

    /**
     * Returns the meta object for class '{@link java.util.Map.Entry <em>URI Map Entry</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>URI Map Entry</em>'.
     * @see java.util.Map.Entry
     * @model keyType="java.lang.String"
     *        valueType="java.lang.String"
     * @generated
     */
    EClass getURIMapEntry();

    /**
     * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Key</em>'.
     * @see java.util.Map.Entry
     * @see #getURIMapEntry()
     * @generated
     */
    EAttribute getURIMapEntry_Key();

    /**
     * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see java.util.Map.Entry
     * @see #getURIMapEntry()
     * @generated
     */
    EAttribute getURIMapEntry_Value();

    /**
     * Returns the meta object for class '{@link java.util.Map.Entry <em>Property</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Property</em>'.
     * @see java.util.Map.Entry
     * @model keyType="java.lang.String"
     *        valueType="java.lang.String"
     * @generated
     */
    EClass getProperty();

    /**
     * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Key</em>'.
     * @see java.util.Map.Entry
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_Key();

    /**
     * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see java.util.Map.Entry
     * @see #getProperty()
     * @generated
     */
    EAttribute getProperty_Value();

    /**
     * Returns the meta object for enum '{@link tefkat.config.TefkatConfig.ExecutionMode <em>Execution Mode</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Execution Mode</em>'.
     * @see tefkat.config.TefkatConfig.ExecutionMode
     * @generated
     */
    EEnum getExecutionMode();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    TefkatConfigFactory getTefkatConfigFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {

        /**
         * The meta object literal for the '{@link tefkat.config.TefkatConfig.impl.ConfigurationImpl <em>Configuration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see tefkat.config.TefkatConfig.impl.ConfigurationImpl
         * @see tefkat.config.TefkatConfig.impl.TefkatConfigPackageImpl#getConfiguration()
         * @generated
         */
        EClass CONFIGURATION = eINSTANCE.getConfiguration();

        /**
         * The meta object literal for the '<em><b>Transformation Tasks</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONFIGURATION__TRANSFORMATION_TASKS = eINSTANCE.getConfiguration_TransformationTasks();

        /**
         * The meta object literal for the '<em><b>Package Classes</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CONFIGURATION__PACKAGE_CLASSES = eINSTANCE.getConfiguration_PackageClasses();

        /**
         * The meta object literal for the '{@link tefkat.config.TefkatConfig.impl.TransformationTaskImpl <em>Transformation Task</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see tefkat.config.TefkatConfig.impl.TransformationTaskImpl
         * @see tefkat.config.TefkatConfig.impl.TefkatConfigPackageImpl#getTransformationTask()
         * @generated
         */
        EClass TRANSFORMATION_TASK = eINSTANCE.getTransformationTask();

        /**
         * The meta object literal for the '<em><b>Transformation</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TRANSFORMATION_TASK__TRANSFORMATION = eINSTANCE.getTransformationTask_Transformation();

        /**
         * The meta object literal for the '<em><b>Source Models</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TRANSFORMATION_TASK__SOURCE_MODELS = eINSTANCE.getTransformationTask_SourceModels();

        /**
         * The meta object literal for the '<em><b>Target Models</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TRANSFORMATION_TASK__TARGET_MODELS = eINSTANCE.getTransformationTask_TargetModels();

        /**
         * The meta object literal for the '<em><b>Trace</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TRANSFORMATION_TASK__TRACE = eINSTANCE.getTransformationTask_Trace();

        /**
         * The meta object literal for the '<em><b>Enabled</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TRANSFORMATION_TASK__ENABLED = eINSTANCE.getTransformationTask_Enabled();

        /**
         * The meta object literal for the '<em><b>Mode</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TRANSFORMATION_TASK__MODE = eINSTANCE.getTransformationTask_Mode();

        /**
         * The meta object literal for the '<em><b>Properties</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TRANSFORMATION_TASK__PROPERTIES = eINSTANCE.getTransformationTask_Properties();

        /**
         * The meta object literal for the '<em><b>Uri Map</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TRANSFORMATION_TASK__URI_MAP = eINSTANCE.getTransformationTask_UriMap();

        /**
         * The meta object literal for the '{@link tefkat.config.TefkatConfig.impl.ModelImpl <em>Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see tefkat.config.TefkatConfig.impl.ModelImpl
         * @see tefkat.config.TefkatConfig.impl.TefkatConfigPackageImpl#getModel()
         * @generated
         */
        EClass MODEL = eINSTANCE.getModel();

        /**
         * The meta object literal for the '<em><b>Location Uri</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MODEL__LOCATION_URI = eINSTANCE.getModel_LocationUri();

        /**
         * The meta object literal for the '<em><b>Var Group</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute MODEL__VAR_GROUP = eINSTANCE.getModel_VarGroup();

        /**
         * The meta object literal for the '{@link tefkat.config.TefkatConfig.impl.URIMapEntryImpl <em>URI Map Entry</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see tefkat.config.TefkatConfig.impl.URIMapEntryImpl
         * @see tefkat.config.TefkatConfig.impl.TefkatConfigPackageImpl#getURIMapEntry()
         * @generated
         */
        EClass URI_MAP_ENTRY = eINSTANCE.getURIMapEntry();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute URI_MAP_ENTRY__KEY = eINSTANCE.getURIMapEntry_Key();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute URI_MAP_ENTRY__VALUE = eINSTANCE.getURIMapEntry_Value();

        /**
         * The meta object literal for the '{@link tefkat.config.TefkatConfig.impl.PropertyImpl <em>Property</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see tefkat.config.TefkatConfig.impl.PropertyImpl
         * @see tefkat.config.TefkatConfig.impl.TefkatConfigPackageImpl#getProperty()
         * @generated
         */
        EClass PROPERTY = eINSTANCE.getProperty();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__KEY = eINSTANCE.getProperty_Key();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PROPERTY__VALUE = eINSTANCE.getProperty_Value();

        /**
         * The meta object literal for the '{@link tefkat.config.TefkatConfig.ExecutionMode <em>Execution Mode</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see tefkat.config.TefkatConfig.ExecutionMode
         * @see tefkat.config.TefkatConfig.impl.TefkatConfigPackageImpl#getExecutionMode()
         * @generated
         */
        EEnum EXECUTION_MODE = eINSTANCE.getExecutionMode();
    }
}
