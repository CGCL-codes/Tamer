package org.mitre.midiki.impl.mitre.galaxy;

import org.mitre.midiki.state.*;
import org.mitre.midiki.impl.mitre.*;
import java.util.*;

/**
 * Provides a simple source of contract information for Galaxy
 * Communicator-based cells.
 *
 * @author <a href="mailto:cburke@mitre.org">Carl Burke</a>
 * @version 1.0
 * @since 1.0
 */
public class GalaxyContractDatabase {

    protected static HashMap db = null;

    protected static void initializeContractDatabase() {
        db = new HashMap();
        db.put("io_podium_input", OSTK_ToIOPodium());
        db.put("io_podium_output", OSTK_FromIOPodium());
        db.put("audio_echo", OSTK_AudioEcho());
    }

    static {
        if (db == null) {
            initializeContractDatabase();
        }
    }

    public static Contract find(String name) {
        if (db == null) {
            initializeContractDatabase();
        }
        return (Contract) db.get(name);
    }

    protected static Contract OSTK_ToIOPodium() {
        ContractImpl io = new ContractImpl("io_podium_input");
        try {
            io.addMethod(new MethodImpl("reinitialize", new ParameterImpl[] {}));
            io.addMethod(new MethodImpl("show_input_string", new ParameterImpl[] { new ParameterImpl("input_string", Class.forName("java.lang.String"), null), new ParameterImpl("input_id", Class.forName("java.lang.Integer"), null), new ParameterImpl("clear_output", Class.forName("java.lang.Integer"), null) }));
            io.addMethod(new MethodImpl("show_output_string", new ParameterImpl[] { new ParameterImpl("output_string", Class.forName("java.lang.String"), null), new ParameterImpl("output_id", Class.forName("java.lang.Integer"), null), new ParameterImpl("clear_input", Class.forName("java.lang.Integer"), null) }));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return io;
    }

    protected static Contract OSTK_FromIOPodium() {
        ContractImpl io = new ContractImpl("io_podium_output");
        try {
            io.addMethod(new MethodImpl("FromIOPodium", new ParameterImpl[] { new ParameterImpl("input_string", Class.forName("java.lang.String"), null), new ParameterImpl("input_id", Class.forName("java.lang.Integer"), null), new ParameterImpl("output_string", Class.forName("java.lang.String"), null), new ParameterImpl("output_id", Class.forName("java.lang.Integer"), null) }));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return io;
    }

    protected static Contract OSTK_AudioEcho() {
        ContractImpl audioEcho = new ContractImpl("audio_echo");
        try {
            audioEcho.addMethod(new MethodImpl("reinitialize", new ParameterImpl[] {}));
            audioEcho.addMethod(new MethodImpl("handle_audio_data", new ParameterImpl[] { new ParameterImpl("audio_host", Class.forName("java.lang.String"), null), new ParameterImpl("audio_port", Class.forName("java.lang.Integer"), null), new ParameterImpl("call_id", Class.forName("java.lang.String"), null), new ParameterImpl("audio_proxy", null, null), new ParameterImpl("batch", Class.forName("java.lang.Integer"), null), new ParameterImpl("mit_playbox_protocol", Class.forName("java.lang.Integer"), null), new ParameterImpl("sample_rate", Class.forName("java.lang.Integer"), null), new ParameterImpl("encoding_format", Class.forName("java.lang.String"), null), new ParameterImpl("logfile", Class.forName("java.lang.String"), null) }));
            audioEcho.addMethod(new MethodImpl("FromEcho", new ParameterImpl[] { new ParameterImpl("synth_host", Class.forName("java.lang.String"), null), new ParameterImpl("synth_port", Class.forName("java.lang.Integer"), null), new ParameterImpl("call_id", Class.forName("java.lang.String"), null), new ParameterImpl("synth_proxy", null, null), new ParameterImpl("sample_rate", Class.forName("java.lang.Integer"), null), new ParameterImpl("encoding_format", Class.forName("java.lang.String"), null) }));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return audioEcho;
    }
}
