package syntelos.lang.type;

import alto.lang.Component;
import alto.sys.FileManager;
import alto.sys.Reference;

/**
 * The type <code>"application/x-syntelos-config"</code>.
 */
public class Config extends Abstract {

    public Config(Reference reference, String typename, String classname) {
        super(reference, typename, classname);
    }

    public Config(Reference reference) {
        super(reference);
    }

    public Config(alto.sys.PHeaders pheaders) {
        super(pheaders);
    }

    public Config() {
        super(Component.Type.Tools.ReferenceTo(Component.Type.Strings.Config));
        this.setMimeType(Component.Type.Strings.Config);
        this.setFext("config");
        this.setTypeClass(this.getClass());
        this.setHashFunction("xor");
        this.typePostProcess();
        this.setStorageContent();
    }
}
