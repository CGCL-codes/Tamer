package QosContractRepository;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "QoSContractRepository.idl"
 * <li> <b>IDL Name</b>      ::QosContractRepository::statistical_operator_kind
 * <li> <b>Repository Id</b> IDL:QosContractRepository/statistical_operator_kind:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * enum statistical_operator_kind {so_percentile, so_frequency, so_mean,
                                so_variance};
 * </pre>
 */
public final class statistical_operator_kindHelper {

    private static org.omg.CORBA.TypeCode _type;

    private static boolean _initializing;

    private static org.omg.CORBA.ORB _orb() {
        return org.omg.CORBA.ORB.init();
    }

    public static QosContractRepository.statistical_operator_kind read(final org.omg.CORBA.portable.InputStream _input) {
        return QosContractRepository.statistical_operator_kind.from_int(_input.read_long());
    }

    public static void write(final org.omg.CORBA.portable.OutputStream _output, final QosContractRepository.statistical_operator_kind _vis_value) {
        _output.write_long(_vis_value.value());
    }

    public static void insert(final org.omg.CORBA.Any any, final QosContractRepository.statistical_operator_kind _vis_value) {
        if (any instanceof com.inprise.vbroker.CORBA.Any) {
            ((com.inprise.vbroker.CORBA.Any) any).insert_enum(_vis_value.value(), QosContractRepository.statistical_operator_kindHelper.type());
        } else {
            org.omg.CORBA.portable.OutputStream output = any.create_output_stream();
            QosContractRepository.statistical_operator_kindHelper.write(output, _vis_value);
            any.read_value(output.create_input_stream(), QosContractRepository.statistical_operator_kindHelper.type());
        }
    }

    public static QosContractRepository.statistical_operator_kind extract(final org.omg.CORBA.Any any) {
        QosContractRepository.statistical_operator_kind _vis_value;
        if (any instanceof com.inprise.vbroker.CORBA.Any) {
            _vis_value = QosContractRepository.statistical_operator_kind.from_int(((com.inprise.vbroker.CORBA.Any) any).extract_enum(type()));
        } else {
            _vis_value = QosContractRepository.statistical_operator_kindHelper.read(any.create_input_stream());
        }
        return _vis_value;
    }

    public static org.omg.CORBA.TypeCode type() {
        if (_type == null) {
            synchronized (org.omg.CORBA.TypeCode.class) {
                if (_type == null) {
                    final java.lang.String members[] = new java.lang.String[] { "so_percentile", "so_frequency", "so_mean", "so_variance" };
                    _type = _orb().create_enum_tc(id(), "statistical_operator_kind", members);
                }
            }
        }
        return _type;
    }

    public static java.lang.String id() {
        return "IDL:QosContractRepository/statistical_operator_kind:1.0";
    }
}
