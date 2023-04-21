package jp.ekasi.pms.model.impl;

import java.util.Calendar;
import java.util.Date;
import jp.ekasi.pms.model.*;
import jp.ekasi.common.util.DateUtil;
import jp.ekasi.common.util.StringUtil;
import jp.ekasi.pms.model.ModelFactory;
import jp.ekasi.pms.model.ModelPackage;
import jp.ekasi.pms.model.Project;
import jp.ekasi.pms.model.Resource;
import jp.ekasi.pms.model.Task;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelFactoryImpl extends EFactoryImpl implements ModelFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static ModelFactory init() {
        try {
            ModelFactory theModelFactory = (ModelFactory) EPackage.Registry.INSTANCE.getEFactory("http://ekasi.jp/pms/model.ecore");
            if (theModelFactory != null) {
                return theModelFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ModelFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModelFactoryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EObject create(EClass eClass) {
        switch(eClass.getClassifierID()) {
            case ModelPackage.PROJECT:
                return createProject();
            case ModelPackage.TASK:
                return createTask();
            case ModelPackage.RESOURCE:
                return createResource();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch(eDataType.getClassifierID()) {
            case ModelPackage.HCALENDAR:
                return createHCalendarFromString(eDataType, initialValue);
            case ModelPackage.HDURATION:
                return createHDurationFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch(eDataType.getClassifierID()) {
            case ModelPackage.HCALENDAR:
                return convertHCalendarToString(eDataType, instanceValue);
            case ModelPackage.HDURATION:
                return convertHDurationToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Project createProject() {
        ProjectImpl project = new ProjectImpl();
        return project;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Task createTask() {
        TaskImpl task = new TaskImpl();
        return task;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Resource createResource() {
        ResourceImpl resource = new ResourceImpl();
        return resource;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public Calendar createHCalendar(String literal) {
        return createHCalendarFromString(ModelPackage.Literals.HCALENDAR, literal);
    }

    /**
	 * <!-- begin-user-doc -->
	 * create Calendar from String
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public Calendar createHCalendarFromString(EDataType eDataType, String initialValue) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.parse(initialValue));
        return calendar;
    }

    /**
	 * <!-- begin-user-doc -->
	 * convert Calendar to String
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public String convertHCalendar(Calendar instanceValue) {
        return convertHCalendarToString(ModelPackage.Literals.HCALENDAR, instanceValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public String convertHCalendarToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) {
            return null;
        }
        return DateUtil.format(((Calendar) instanceValue).getTime());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public long createHDuration(String literal) {
        return ((Long) super.createFromString(ModelPackage.Literals.HDURATION, literal)).longValue();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Long createHDurationFromString(EDataType eDataType, String initialValue) {
        return (Long) super.createFromString(eDataType, initialValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertHDuration(long instanceValue) {
        return super.convertToString(ModelPackage.Literals.HDURATION, new Long(instanceValue));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertHDurationToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModelPackage getModelPackage() {
        return (ModelPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static ModelPackage getPackage() {
        return ModelPackage.eINSTANCE;
    }
}
