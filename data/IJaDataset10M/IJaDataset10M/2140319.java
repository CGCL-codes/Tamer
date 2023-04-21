package QosContractRepository;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "QoSContractRepository.idl"
 * <li> <b>IDL Name</b>      ::QosContractRepository::ContainedClass
 * <li> <b>Repository Id</b> IDL:QosContractRepository/ContainedClass:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface ContainedClass : Reflective.RefObject {
  ...
};
 * </pre>
 */
public final class ContainedClassHolder implements org.omg.CORBA.portable.Streamable {

    public QosContractRepository.ContainedClass value;

    public ContainedClassHolder() {
    }

    public ContainedClassHolder(final QosContractRepository.ContainedClass _vis_value) {
        this.value = _vis_value;
    }

    public void _read(final org.omg.CORBA.portable.InputStream input) {
        value = QosContractRepository.ContainedClassHelper.read(input);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream output) {
        QosContractRepository.ContainedClassHelper.write(output, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return QosContractRepository.ContainedClassHelper.type();
    }
}
