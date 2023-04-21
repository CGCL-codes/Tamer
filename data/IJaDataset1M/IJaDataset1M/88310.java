package ddss.loader;

import org.ksoap2.serialization.KvmSerializable;
import ddss.data.Settings;

public abstract class PersonalBase implements KvmSerializable {

    public static final String NAMESPACE = Settings.WS_NAMESPACE;

    public PersonalBase() {
        super();
    }
}
