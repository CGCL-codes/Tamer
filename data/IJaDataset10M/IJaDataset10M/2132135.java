package hu.cubussapiens.modembed.model.memory;

import hu.cubussapiens.modembed.model.infrastructure.InfrastructurePackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see hu.cubussapiens.modembed.model.memory.MemoryFactory
 * @model kind="package"
 * @generated
 */
public interface MemoryPackage extends EPackage {

    /**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "memory";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http://cubussapiens.hu/modembed/memory";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "mem";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    MemoryPackage eINSTANCE = hu.cubussapiens.modembed.model.memory.impl.MemoryPackageImpl.init();

    /**
	 * The meta object id for the '{@link hu.cubussapiens.modembed.model.memory.impl.MemoryDescriptionImpl <em>Description</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see hu.cubussapiens.modembed.model.memory.impl.MemoryDescriptionImpl
	 * @see hu.cubussapiens.modembed.model.memory.impl.MemoryPackageImpl#getMemoryDescription()
	 * @generated
	 */
    int MEMORY_DESCRIPTION = 0;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MEMORY_DESCRIPTION__NAME = InfrastructurePackage.PACKAGE_ELEMENT__NAME;

    /**
	 * The feature id for the '<em><b>Items</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MEMORY_DESCRIPTION__ITEMS = InfrastructurePackage.PACKAGE_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Description</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MEMORY_DESCRIPTION_FEATURE_COUNT = InfrastructurePackage.PACKAGE_ELEMENT_FEATURE_COUNT + 1;

    /**
	 * The meta object id for the '{@link hu.cubussapiens.modembed.model.memory.impl.MemoryItemImpl <em>Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see hu.cubussapiens.modembed.model.memory.impl.MemoryItemImpl
	 * @see hu.cubussapiens.modembed.model.memory.impl.MemoryPackageImpl#getMemoryItem()
	 * @generated
	 */
    int MEMORY_ITEM = 1;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MEMORY_ITEM__NAME = InfrastructurePackage.NAMED_ELEMENT__NAME;

    /**
	 * The number of structural features of the '<em>Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MEMORY_ITEM_FEATURE_COUNT = InfrastructurePackage.NAMED_ELEMENT_FEATURE_COUNT + 0;

    /**
	 * The meta object id for the '{@link hu.cubussapiens.modembed.model.memory.impl.MemorySegmentImpl <em>Segment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see hu.cubussapiens.modembed.model.memory.impl.MemorySegmentImpl
	 * @see hu.cubussapiens.modembed.model.memory.impl.MemoryPackageImpl#getMemorySegment()
	 * @generated
	 */
    int MEMORY_SEGMENT = 2;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MEMORY_SEGMENT__NAME = MEMORY_ITEM__NAME;

    /**
	 * The feature id for the '<em><b>Start Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MEMORY_SEGMENT__START_ADDRESS = MEMORY_ITEM_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MEMORY_SEGMENT__SIZE = MEMORY_ITEM_FEATURE_COUNT + 1;

    /**
	 * The number of structural features of the '<em>Segment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MEMORY_SEGMENT_FEATURE_COUNT = MEMORY_ITEM_FEATURE_COUNT + 2;

    /**
	 * The meta object id for the '{@link hu.cubussapiens.modembed.model.memory.impl.MemorySymbolImpl <em>Symbol</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see hu.cubussapiens.modembed.model.memory.impl.MemorySymbolImpl
	 * @see hu.cubussapiens.modembed.model.memory.impl.MemoryPackageImpl#getMemorySymbol()
	 * @generated
	 */
    int MEMORY_SYMBOL = 3;

    /**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MEMORY_SYMBOL__NAME = MEMORY_ITEM__NAME;

    /**
	 * The feature id for the '<em><b>Address</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MEMORY_SYMBOL__ADDRESS = MEMORY_ITEM_FEATURE_COUNT + 0;

    /**
	 * The number of structural features of the '<em>Symbol</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int MEMORY_SYMBOL_FEATURE_COUNT = MEMORY_ITEM_FEATURE_COUNT + 1;

    /**
	 * Returns the meta object for class '{@link hu.cubussapiens.modembed.model.memory.MemoryDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Description</em>'.
	 * @see hu.cubussapiens.modembed.model.memory.MemoryDescription
	 * @generated
	 */
    EClass getMemoryDescription();

    /**
	 * Returns the meta object for the containment reference list '{@link hu.cubussapiens.modembed.model.memory.MemoryDescription#getItems <em>Items</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Items</em>'.
	 * @see hu.cubussapiens.modembed.model.memory.MemoryDescription#getItems()
	 * @see #getMemoryDescription()
	 * @generated
	 */
    EReference getMemoryDescription_Items();

    /**
	 * Returns the meta object for class '{@link hu.cubussapiens.modembed.model.memory.MemoryItem <em>Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Item</em>'.
	 * @see hu.cubussapiens.modembed.model.memory.MemoryItem
	 * @generated
	 */
    EClass getMemoryItem();

    /**
	 * Returns the meta object for class '{@link hu.cubussapiens.modembed.model.memory.MemorySegment <em>Segment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Segment</em>'.
	 * @see hu.cubussapiens.modembed.model.memory.MemorySegment
	 * @generated
	 */
    EClass getMemorySegment();

    /**
	 * Returns the meta object for the attribute '{@link hu.cubussapiens.modembed.model.memory.MemorySegment#getStartAddress <em>Start Address</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Address</em>'.
	 * @see hu.cubussapiens.modembed.model.memory.MemorySegment#getStartAddress()
	 * @see #getMemorySegment()
	 * @generated
	 */
    EAttribute getMemorySegment_StartAddress();

    /**
	 * Returns the meta object for the attribute '{@link hu.cubussapiens.modembed.model.memory.MemorySegment#getSize <em>Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Size</em>'.
	 * @see hu.cubussapiens.modembed.model.memory.MemorySegment#getSize()
	 * @see #getMemorySegment()
	 * @generated
	 */
    EAttribute getMemorySegment_Size();

    /**
	 * Returns the meta object for class '{@link hu.cubussapiens.modembed.model.memory.MemorySymbol <em>Symbol</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Symbol</em>'.
	 * @see hu.cubussapiens.modembed.model.memory.MemorySymbol
	 * @generated
	 */
    EClass getMemorySymbol();

    /**
	 * Returns the meta object for the attribute '{@link hu.cubussapiens.modembed.model.memory.MemorySymbol#getAddress <em>Address</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Address</em>'.
	 * @see hu.cubussapiens.modembed.model.memory.MemorySymbol#getAddress()
	 * @see #getMemorySymbol()
	 * @generated
	 */
    EAttribute getMemorySymbol_Address();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    MemoryFactory getMemoryFactory();

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
		 * The meta object literal for the '{@link hu.cubussapiens.modembed.model.memory.impl.MemoryDescriptionImpl <em>Description</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see hu.cubussapiens.modembed.model.memory.impl.MemoryDescriptionImpl
		 * @see hu.cubussapiens.modembed.model.memory.impl.MemoryPackageImpl#getMemoryDescription()
		 * @generated
		 */
        EClass MEMORY_DESCRIPTION = eINSTANCE.getMemoryDescription();

        /**
		 * The meta object literal for the '<em><b>Items</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EReference MEMORY_DESCRIPTION__ITEMS = eINSTANCE.getMemoryDescription_Items();

        /**
		 * The meta object literal for the '{@link hu.cubussapiens.modembed.model.memory.impl.MemoryItemImpl <em>Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see hu.cubussapiens.modembed.model.memory.impl.MemoryItemImpl
		 * @see hu.cubussapiens.modembed.model.memory.impl.MemoryPackageImpl#getMemoryItem()
		 * @generated
		 */
        EClass MEMORY_ITEM = eINSTANCE.getMemoryItem();

        /**
		 * The meta object literal for the '{@link hu.cubussapiens.modembed.model.memory.impl.MemorySegmentImpl <em>Segment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see hu.cubussapiens.modembed.model.memory.impl.MemorySegmentImpl
		 * @see hu.cubussapiens.modembed.model.memory.impl.MemoryPackageImpl#getMemorySegment()
		 * @generated
		 */
        EClass MEMORY_SEGMENT = eINSTANCE.getMemorySegment();

        /**
		 * The meta object literal for the '<em><b>Start Address</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute MEMORY_SEGMENT__START_ADDRESS = eINSTANCE.getMemorySegment_StartAddress();

        /**
		 * The meta object literal for the '<em><b>Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute MEMORY_SEGMENT__SIZE = eINSTANCE.getMemorySegment_Size();

        /**
		 * The meta object literal for the '{@link hu.cubussapiens.modembed.model.memory.impl.MemorySymbolImpl <em>Symbol</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see hu.cubussapiens.modembed.model.memory.impl.MemorySymbolImpl
		 * @see hu.cubussapiens.modembed.model.memory.impl.MemoryPackageImpl#getMemorySymbol()
		 * @generated
		 */
        EClass MEMORY_SYMBOL = eINSTANCE.getMemorySymbol();

        /**
		 * The meta object literal for the '<em><b>Address</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute MEMORY_SYMBOL__ADDRESS = eINSTANCE.getMemorySymbol_Address();
    }
}
