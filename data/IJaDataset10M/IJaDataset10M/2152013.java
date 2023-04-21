package QosContractRepository;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "QoSContractRepository.idl"
 * <li> <b>IDL Name</b>      ::QosContractRepository::Contains
 * <li> <b>Repository Id</b> IDL:QosContractRepository/Contains:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface Contains : Reflective.RefAssociation {
  ...
};
 * </pre>
 */
public final class ContainsHolder implements org.omg.CORBA.portable.Streamable {

    public QosContractRepository.Contains value;

    public ContainsHolder() {
    }

    public ContainsHolder(final QosContractRepository.Contains _vis_value) {
        this.value = _vis_value;
    }

    public void _read(final org.omg.CORBA.portable.InputStream input) {
        value = QosContractRepository.ContainsHelper.read(input);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream output) {
        QosContractRepository.ContainsHelper.write(output, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return QosContractRepository.ContainsHelper.type();
    }
}
