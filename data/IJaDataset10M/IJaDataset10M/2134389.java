package architecture.common.license;

import java.util.Collection;
import architecture.common.license.validator.Validator;

public interface LicenseProvider {

    public abstract String getName();

    public abstract License.Version getVersion();

    public abstract Collection<Validator> getValidators();

    public abstract Collection<License.Module> getInstalledModules();
}
