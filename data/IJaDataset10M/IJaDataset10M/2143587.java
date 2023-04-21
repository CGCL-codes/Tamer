package quamj.qps.contracttypemgmt.QosContractRepositoryImpl;

import quamj.qps.contracttypemgmt.QosContractRepositoryImpl.*;
import com.dstc.mof.moflet.ObjectPool;
import com.dstc.mof.moflet.Persistence;

public class DimensionStatisticalConstraintImpl extends QosContractRepository.DimensionStatisticalConstraintPOA implements com.dstc.mof.persistence.Persistifiable {

    private com.dstc.mof.persistence.Id _id;

    private Reflective._RefPackage __immediatePackageRef;

    private Reflective._RefPackage __outermostPackageRef;

    private QosContractRepository._QosContractRepositoryPackage __rootPackageRef;

    private Reflective.RefObject __meta_objectRef;

    private java.lang.String __name;

    private org.omg.CORBA.Any __parameter;

    private QosContractRepository.constraining_operator_kind __operator;

    private QosContractRepository.statistical_operator_kind __statistical_operator;

    private org.omg.CORBA.Any __statistical_parameter;

    private QosContractRepository.DimensionStatisticalConstraintClass _proxyClass;

    public DimensionStatisticalConstraintImpl(com.dstc.mof.persistence.Id _id, Reflective._RefPackage __immediatePackageRef, Reflective._RefPackage __outermostPackageRef, QosContractRepository._QosContractRepositoryPackage rootPackageRef, java.lang.String name, org.omg.CORBA.Any parameter, QosContractRepository.constraining_operator_kind operator, QosContractRepository.statistical_operator_kind statistical_operator, org.omg.CORBA.Any statistical_parameter, QosContractRepository.DimensionStatisticalConstraintClass proxyClass) throws Reflective.MofError {
        this._id = _id;
        this.__immediatePackageRef = __immediatePackageRef;
        this.__outermostPackageRef = __outermostPackageRef;
        this.__rootPackageRef = rootPackageRef;
        this.__name = name;
        this.__parameter = parameter;
        this.__operator = operator;
        this.__statistical_operator = statistical_operator;
        this.__statistical_parameter = statistical_parameter;
        this._proxyClass = proxyClass;
    }

    public synchronized QosContractRepository.DimensionStatisticalConstraint[] all_of_type_dimension_statistical_constraint() {
        return _proxyClass.all_of_type_dimension_statistical_constraint();
    }

    public synchronized QosContractRepository.Contained[] all_of_type_contained() {
        return __rootPackageRef.contained_ref().all_of_type_contained();
    }

    public synchronized QosContractRepository.DimensionSingleConstraint[] all_of_type_dimension_single_constraint() {
        return __rootPackageRef.dimension_single_constraint_ref().all_of_type_dimension_single_constraint();
    }

    public synchronized QosContractRepository.DimensionStatisticalConstraint[] all_of_class_dimension_statistical_constraint() {
        return _proxyClass.all_of_class_dimension_statistical_constraint();
    }

    public synchronized QosContractRepository.DimensionSingleConstraint[] all_of_class_dimension_single_constraint() {
        return __rootPackageRef.dimension_single_constraint_ref().all_of_class_dimension_single_constraint();
    }

    public synchronized QosContractRepository.DimensionStatisticalConstraint create_dimension_statistical_constraint(java.lang.String name, org.omg.CORBA.Any parameter, QosContractRepository.constraining_operator_kind operator, QosContractRepository.statistical_operator_kind statistical_operator, org.omg.CORBA.Any statistical_parameter) throws Reflective.MofError {
        return _proxyClass.create_dimension_statistical_constraint(name, parameter, operator, statistical_operator, statistical_parameter);
    }

    public synchronized QosContractRepository.DimensionSingleConstraint create_dimension_single_constraint(java.lang.String name, org.omg.CORBA.Any parameter, QosContractRepository.constraining_operator_kind operator) throws Reflective.MofError {
        return __rootPackageRef.dimension_single_constraint_ref().create_dimension_single_constraint(name, parameter, operator);
    }

    public synchronized java.lang.String name() throws Reflective.MofError {
        return this.__name;
    }

    public synchronized void set_name(java.lang.String newValue) throws Reflective.MofError {
        this.__name = newValue;
        try {
            Persistence.getObjectStore().update(this);
        } catch (com.dstc.mof.persistence.StorageError ex) {
            throw com.dstc.mof.moflet.Checks.storage(ex);
        }
    }

    public synchronized org.omg.CORBA.Any parameter() throws Reflective.MofError {
        return this.__parameter;
    }

    public synchronized void set_parameter(org.omg.CORBA.Any newValue) throws Reflective.MofError {
        this.__parameter = newValue;
        try {
            Persistence.getObjectStore().update(this);
        } catch (com.dstc.mof.persistence.StorageError ex) {
            throw com.dstc.mof.moflet.Checks.storage(ex);
        }
    }

    public synchronized QosContractRepository.constraining_operator_kind operator() throws Reflective.MofError {
        return this.__operator;
    }

    public synchronized void set_operator(QosContractRepository.constraining_operator_kind newValue) throws Reflective.MofError {
        this.__operator = newValue;
        try {
            Persistence.getObjectStore().update(this);
        } catch (com.dstc.mof.persistence.StorageError ex) {
            throw com.dstc.mof.moflet.Checks.storage(ex);
        }
    }

    public synchronized QosContractRepository.statistical_operator_kind statistical_operator() throws Reflective.MofError {
        return this.__statistical_operator;
    }

    public synchronized void set_statistical_operator(QosContractRepository.statistical_operator_kind newValue) throws Reflective.MofError {
        this.__statistical_operator = newValue;
        try {
            Persistence.getObjectStore().update(this);
        } catch (com.dstc.mof.persistence.StorageError ex) {
            throw com.dstc.mof.moflet.Checks.storage(ex);
        }
    }

    public synchronized org.omg.CORBA.Any statistical_parameter() throws Reflective.MofError {
        return this.__statistical_parameter;
    }

    public synchronized void set_statistical_parameter(org.omg.CORBA.Any newValue) throws Reflective.MofError {
        this.__statistical_parameter = newValue;
        try {
            Persistence.getObjectStore().update(this);
        } catch (com.dstc.mof.persistence.StorageError ex) {
            throw com.dstc.mof.moflet.Checks.storage(ex);
        }
    }

    public QosContractRepository.Container defined_in() throws Reflective.NotSet, Reflective.MofError {
        QosContractRepository.Container _res = __rootPackageRef.contains_ref().the_container(_this());
        if (_res == null) {
            throw new Reflective.NotSet();
        }
        return _res;
    }

    public void set_defined_in(QosContractRepository.Container new_value) throws Reflective.MofError {
        quamj.qps.contracttypemgmt.QosContractRepositoryImpl.ContainsImpl assocImpl = (quamj.qps.contracttypemgmt.QosContractRepositoryImpl.ContainsImpl) ObjectPool.acquireServant(__rootPackageRef.contains_ref());
        try {
            assocImpl._set_the_container(_this(), new_value);
        } finally {
            ObjectPool.releaseServant(assocImpl);
        }
    }

    public void unset_defined_in() throws Reflective.MofError {
        quamj.qps.contracttypemgmt.QosContractRepositoryImpl.ContainsImpl assocImpl = (quamj.qps.contracttypemgmt.QosContractRepositoryImpl.ContainsImpl) (ObjectPool.acquireServant(__rootPackageRef.contains_ref()));
        try {
            assocImpl._unset_the_container(_this());
        } finally {
            ObjectPool.releaseServant(assocImpl);
        }
    }

    public synchronized com.dstc.mof.persistence.Id getId() {
        return this._id;
    }

    public java.lang.String ref_mof_id() {
        return _id.toMofId();
    }

    public Reflective.RefObject ref_meta_object() {
        return quamj.qps.contracttypemgmt.QosContractRepositoryImpl.MetaObjectIORs.get_qos_contract_repository__dimension_statistical_constraint_MO();
    }

    public boolean ref_itself(Reflective.RefBaseObject other) {
        return _id.sameAs(other);
    }

    public Reflective._RefPackage ref_immediate_package() {
        return __immediatePackageRef;
    }

    public Reflective._RefPackage ref_outermost_package() {
        return __outermostPackageRef;
    }

    public synchronized void ref_delete() throws Reflective.MofError {
        try {
            com.dstc.mof.moflet.POAs.getActivationLocator().scheduleForDeletion(this.getId());
            com.dstc.mof.moflet.Persistence.getAssociationFactory().expungeObjects(new com.dstc.mof.persistence.Id[] { this.getId() });
        } catch (com.dstc.mof.persistence.StorageError ex) {
            com.dstc.mof.logging.Logging.bug("Exception in ref_delete: " + ex);
        }
    }

    public boolean ref_is_instance_of(Reflective.RefObject _class, boolean consider_subtypes) throws Reflective.MofError {
        try {
            Reflective.RefBaseObject[] all = _allClassMOs();
            if (consider_subtypes) {
                if (com.dstc.mof.persistence.Id.matchObject(_class, all) >= 0) {
                    return true;
                }
            } else {
                if (com.dstc.mof.persistence.Id.sameObject(_class, all[0])) {
                    return true;
                }
            }
        } catch (org.omg.CORBA.SystemException ex) {
            throw com.dstc.mof.moflet.RefChecks.inaccessibleDesignator();
        }
        com.dstc.mof.moflet.RefChecks.checkClassDesignator(_class);
        return false;
    }

    public Reflective.RefObject ref_create_instance(org.omg.CORBA.Any[] args) throws Reflective.MofError {
        com.dstc.mof.moflet.RefChecks.checkNosArgs(args, 5, ref_meta_object());
        java.lang.String __name;
        org.omg.CORBA.Any __parameter;
        QosContractRepository.constraining_operator_kind __operator;
        QosContractRepository.statistical_operator_kind __statistical_operator;
        org.omg.CORBA.Any __statistical_parameter;
        try {
            __name = args[0].extract_string();
        } catch (org.omg.CORBA.BAD_PARAM ex) {
            throw com.dstc.mof.moflet.RefChecks.wrongType(ref_meta_object(), args[0]);
        }
        try {
            __parameter = args[1].extract_any();
        } catch (org.omg.CORBA.BAD_PARAM ex) {
            throw com.dstc.mof.moflet.RefChecks.wrongType(ref_meta_object(), args[1]);
        }
        try {
            __operator = QosContractRepository.constraining_operator_kindHelper.extract(args[2]);
        } catch (org.omg.CORBA.BAD_PARAM ex) {
            throw com.dstc.mof.moflet.RefChecks.wrongType(ref_meta_object(), args[2]);
        }
        try {
            __statistical_operator = QosContractRepository.statistical_operator_kindHelper.extract(args[3]);
        } catch (org.omg.CORBA.BAD_PARAM ex) {
            throw com.dstc.mof.moflet.RefChecks.wrongType(ref_meta_object(), args[3]);
        }
        try {
            __statistical_parameter = args[4].extract_any();
        } catch (org.omg.CORBA.BAD_PARAM ex) {
            throw com.dstc.mof.moflet.RefChecks.wrongType(ref_meta_object(), args[4]);
        }
        return create_dimension_statistical_constraint(__name, __parameter, __operator, __statistical_operator, __statistical_parameter);
    }

    public Reflective.RefObject[] ref_all_objects(boolean include_subtypes) {
        if (include_subtypes) {
            return all_of_type_dimension_statistical_constraint();
        } else {
            return all_of_class_dimension_statistical_constraint();
        }
    }

    public org.omg.CORBA.Any ref_value(Reflective.RefObject feature) throws Reflective.NotSet, Reflective.MofError {
        try {
            org.omg.CORBA.Any returnValue = com.dstc.mof.corba.Orb.createAny();
            switch(com.dstc.mof.persistence.Id.matchObject(feature, _allAttrAndRefMOs())) {
                case 0:
                    returnValue.insert_string(name());
                    return returnValue;
                case 1:
                    QosContractRepository.ContainerHelper.insert(returnValue, defined_in());
                    return returnValue;
                case 2:
                    returnValue.insert_any(parameter());
                    return returnValue;
                case 3:
                    QosContractRepository.constraining_operator_kindHelper.insert(returnValue, operator());
                    return returnValue;
                case 4:
                    QosContractRepository.statistical_operator_kindHelper.insert(returnValue, statistical_operator());
                    return returnValue;
                case 5:
                    returnValue.insert_any(statistical_parameter());
                    return returnValue;
                default:
                    break;
            }
        } catch (org.omg.CORBA.SystemException ex) {
            throw com.dstc.mof.moflet.RefChecks.inaccessibleDesignator();
        }
        com.dstc.mof.moflet.RefChecks.checkAttrOrRefDesignator(feature);
        throw com.dstc.mof.moflet.RefChecks.unknownDesignator(feature, "Class or Reference");
    }

    public void ref_set_value(Reflective.RefObject feature, org.omg.CORBA.Any new_value) throws Reflective.MofError {
        try {
            switch(com.dstc.mof.persistence.Id.matchObject(feature, _allAttrAndRefMOs())) {
                case 0:
                    set_name(new_value.extract_string());
                    return;
                case 1:
                    set_defined_in(QosContractRepository.ContainerHelper.extract(new_value));
                    return;
                case 2:
                    set_parameter(new_value.extract_any());
                    return;
                case 3:
                    set_operator(QosContractRepository.constraining_operator_kindHelper.extract(new_value));
                    return;
                case 4:
                    set_statistical_operator(QosContractRepository.statistical_operator_kindHelper.extract(new_value));
                    return;
                case 5:
                    set_statistical_parameter(new_value.extract_any());
                    return;
                default:
                    break;
            }
        } catch (org.omg.CORBA.BAD_PARAM ex) {
            throw com.dstc.mof.moflet.RefChecks.wrongType(feature, new_value);
        } catch (org.omg.CORBA.SystemException ex) {
            throw com.dstc.mof.moflet.RefChecks.inaccessibleDesignator();
        }
        com.dstc.mof.moflet.RefChecks.checkAttrOrRefDesignator(feature);
        throw com.dstc.mof.moflet.RefChecks.unknownDesignator(feature, "Class or Reference");
    }

    public void ref_unset_value(Reflective.RefObject feature) throws Reflective.MofError {
        try {
            switch(com.dstc.mof.persistence.Id.matchObject(feature, _allAttrAndRefMOs())) {
                case 0:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 1:
                    unset_defined_in();
                    return;
                case 2:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 3:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 4:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 5:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                default:
                    break;
            }
        } catch (org.omg.CORBA.SystemException ex) {
            throw com.dstc.mof.moflet.RefChecks.inaccessibleDesignator();
        }
        com.dstc.mof.moflet.RefChecks.checkAttrOrRefDesignator(feature);
        throw com.dstc.mof.moflet.RefChecks.unknownDesignator(feature, "Class or Reference");
    }

    public void ref_add_value(Reflective.RefObject feature, org.omg.CORBA.Any new_element) throws Reflective.MofError {
        try {
            switch(com.dstc.mof.persistence.Id.matchObject(feature, _allAttrAndRefMOs())) {
                case 0:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 1:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 2:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 3:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 4:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 5:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                default:
                    break;
            }
        } catch (org.omg.CORBA.BAD_PARAM ex) {
            throw com.dstc.mof.moflet.RefChecks.wrongType(feature, new_element);
        } catch (org.omg.CORBA.SystemException ex) {
            throw com.dstc.mof.moflet.RefChecks.inaccessibleDesignator();
        }
        com.dstc.mof.moflet.RefChecks.checkAttrOrRefDesignator(feature);
        throw com.dstc.mof.moflet.RefChecks.unknownDesignator(feature, "Class or Reference");
    }

    public void ref_add_value_before(Reflective.RefObject feature, org.omg.CORBA.Any new_element, org.omg.CORBA.Any before_element) throws Reflective.NotFound, Reflective.MofError {
        try {
            switch(com.dstc.mof.persistence.Id.matchObject(feature, _allAttrAndRefMOs())) {
                case 0:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 1:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 2:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 3:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 4:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 5:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                default:
                    break;
            }
        } catch (org.omg.CORBA.BAD_PARAM ex) {
            throw com.dstc.mof.moflet.RefChecks.wrongType(feature, new_element);
        } catch (org.omg.CORBA.SystemException ex) {
            throw com.dstc.mof.moflet.RefChecks.inaccessibleDesignator();
        }
        com.dstc.mof.moflet.RefChecks.checkAttrOrRefDesignator(feature);
        throw com.dstc.mof.moflet.RefChecks.unknownDesignator(feature, "Class or Reference");
    }

    public void ref_add_value_at(Reflective.RefObject feature, org.omg.CORBA.Any new_element, int position) throws Reflective.BadPosition, Reflective.MofError {
        try {
            switch(com.dstc.mof.persistence.Id.matchObject(feature, _allAttrAndRefMOs())) {
                case 0:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 1:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 2:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 3:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 4:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 5:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                default:
                    break;
            }
        } catch (org.omg.CORBA.BAD_PARAM ex) {
            throw com.dstc.mof.moflet.RefChecks.wrongType(feature, new_element);
        } catch (org.omg.CORBA.SystemException ex) {
            throw com.dstc.mof.moflet.RefChecks.inaccessibleDesignator();
        }
        com.dstc.mof.moflet.RefChecks.checkAttrOrRefDesignator(feature);
        throw com.dstc.mof.moflet.RefChecks.unknownDesignator(feature, "Class or Reference");
    }

    public void ref_modify_value(Reflective.RefObject feature, org.omg.CORBA.Any old_element, org.omg.CORBA.Any new_element) throws Reflective.NotFound, Reflective.MofError {
        try {
            switch(com.dstc.mof.persistence.Id.matchObject(feature, _allAttrAndRefMOs())) {
                case 0:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 1:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 2:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 3:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 4:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 5:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                default:
                    break;
            }
        } catch (org.omg.CORBA.BAD_PARAM ex) {
            throw com.dstc.mof.moflet.RefChecks.wrongType(feature, new_element);
        } catch (org.omg.CORBA.SystemException ex) {
            throw com.dstc.mof.moflet.RefChecks.inaccessibleDesignator();
        }
        com.dstc.mof.moflet.RefChecks.checkAttrOrRefDesignator(feature);
        throw com.dstc.mof.moflet.RefChecks.unknownDesignator(feature, "Class or Reference");
    }

    public void ref_modify_value_at(Reflective.RefObject feature, org.omg.CORBA.Any new_element, int position) throws Reflective.BadPosition, Reflective.MofError {
        try {
            switch(com.dstc.mof.persistence.Id.matchObject(feature, _allAttrAndRefMOs())) {
                case 0:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 1:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 2:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 3:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 4:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 5:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                default:
                    break;
            }
        } catch (org.omg.CORBA.BAD_PARAM ex) {
            throw com.dstc.mof.moflet.RefChecks.wrongType(feature, new_element);
        } catch (org.omg.CORBA.SystemException ex) {
            throw com.dstc.mof.moflet.RefChecks.inaccessibleDesignator();
        }
        com.dstc.mof.moflet.RefChecks.checkAttrOrRefDesignator(feature);
        throw com.dstc.mof.moflet.RefChecks.unknownDesignator(feature, "Class or Reference");
    }

    public void ref_remove_value(Reflective.RefObject feature, org.omg.CORBA.Any old_element) throws Reflective.NotFound, Reflective.MofError {
        try {
            switch(com.dstc.mof.persistence.Id.matchObject(feature, _allAttrAndRefMOs())) {
                case 0:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 1:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 2:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 3:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 4:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 5:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                default:
                    break;
            }
        } catch (org.omg.CORBA.BAD_PARAM ex) {
            throw com.dstc.mof.moflet.RefChecks.wrongType(feature, old_element);
        } catch (org.omg.CORBA.SystemException ex) {
            throw com.dstc.mof.moflet.RefChecks.inaccessibleDesignator();
        }
        com.dstc.mof.moflet.RefChecks.checkAttrOrRefDesignator(feature);
        throw com.dstc.mof.moflet.RefChecks.unknownDesignator(feature, "Class or Reference");
    }

    public void ref_remove_value_at(Reflective.RefObject feature, int position) throws Reflective.BadPosition, Reflective.MofError {
        try {
            switch(com.dstc.mof.persistence.Id.matchObject(feature, _allAttrAndRefMOs())) {
                case 0:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 1:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 2:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 3:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 4:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                case 5:
                    throw com.dstc.mof.moflet.RefChecks.featureWrongMultiplicity(feature);
                default:
                    break;
            }
        } catch (org.omg.CORBA.SystemException ex) {
            throw com.dstc.mof.moflet.RefChecks.inaccessibleDesignator();
        }
        com.dstc.mof.moflet.RefChecks.checkAttrOrRefDesignator(feature);
        throw com.dstc.mof.moflet.RefChecks.unknownDesignator(feature, "Class or Reference");
    }

    public synchronized Reflective.RefObject ref_immediate_composite() {
        return null;
    }

    public synchronized Reflective.RefObject ref_outermost_composite() {
        return null;
    }

    public org.omg.CORBA.Any ref_invoke_operation(Reflective.RefObject operation, Reflective.ValueTypeListHolder args) throws Reflective.OtherException, Reflective.MofError {
        try {
            switch(com.dstc.mof.persistence.Id.matchObject(operation, _allOperationMOs())) {
                default:
                    break;
            }
        } catch (org.omg.CORBA.SystemException ex) {
            throw com.dstc.mof.moflet.RefChecks.inaccessibleDesignator();
        }
        com.dstc.mof.moflet.RefChecks.checkOperationDesignator(operation);
        throw com.dstc.mof.moflet.RefChecks.unknownDesignator(operation, "Operation");
    }

    private static Reflective.RefObject[] _allClassMOs;

    private static Reflective.RefObject[] _allClassMOs() {
        if (_allClassMOs == null) {
            _allClassMOs = new Reflective.RefObject[] { quamj.qps.contracttypemgmt.QosContractRepositoryImpl.MetaObjectIORs.get_qos_contract_repository__dimension_statistical_constraint_MO(), quamj.qps.contracttypemgmt.QosContractRepositoryImpl.MetaObjectIORs.get_qos_contract_repository__dimension_single_constraint_MO() };
        }
        return _allClassMOs;
    }

    private static Reflective.RefObject[] _allAttrAndRefMOs;

    private static Reflective.RefObject[] _allAttrAndRefMOs() {
        if (_allAttrAndRefMOs == null) {
            _allAttrAndRefMOs = new Reflective.RefObject[] { quamj.qps.contracttypemgmt.QosContractRepositoryImpl.MetaObjectIORs.get_qos_contract_repository__contained__name_MO(), quamj.qps.contracttypemgmt.QosContractRepositoryImpl.MetaObjectIORs.get_qos_contract_repository__contained__defined_in_MO(), quamj.qps.contracttypemgmt.QosContractRepositoryImpl.MetaObjectIORs.get_qos_contract_repository__dimension_single_constraint__parameter_MO(), quamj.qps.contracttypemgmt.QosContractRepositoryImpl.MetaObjectIORs.get_qos_contract_repository__dimension_single_constraint__operator_MO(), quamj.qps.contracttypemgmt.QosContractRepositoryImpl.MetaObjectIORs.get_qos_contract_repository__dimension_statistical_constraint__statistical_operator_MO(), quamj.qps.contracttypemgmt.QosContractRepositoryImpl.MetaObjectIORs.get_qos_contract_repository__dimension_statistical_constraint__statistical_parameter_MO() };
        }
        return _allAttrAndRefMOs;
    }

    private static Reflective.RefObject[] _allOperationMOs;

    private static Reflective.RefObject[] _allOperationMOs() {
        if (_allOperationMOs == null) {
            _allOperationMOs = new Reflective.RefObject[] {};
        }
        return _allOperationMOs;
    }
}
