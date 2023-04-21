package QosContractRepository;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "QoSContractRepository.idl"
 * <li> <b>IDL Name</b>      ::QosContractRepository::DimensionMultiConstraintSet
 * <li> <b>Repository Id</b> IDL:QosContractRepository/DimensionMultiConstraintSet:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * typedef sequence&ltQosContractRepository.DimensionMultiConstraint&gt DimensionMultiConstraintSet;
 * </pre>
 */
public final class DimensionMultiConstraintSetHolder implements org.omg.CORBA.portable.Streamable {

    public QosContractRepository.DimensionMultiConstraint[] value;

    public DimensionMultiConstraintSetHolder() {
    }

    public DimensionMultiConstraintSetHolder(final QosContractRepository.DimensionMultiConstraint[] _vis_value) {
        this.value = _vis_value;
    }

    public void _read(final org.omg.CORBA.portable.InputStream input) {
        value = QosContractRepository.DimensionMultiConstraintSetHelper.read(input);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream output) {
        QosContractRepository.DimensionMultiConstraintSetHelper.write(output, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return QosContractRepository.DimensionMultiConstraintSetHelper.type();
    }
}
