package ee.webAppToolkit.example.projectTimeTracking.administration;

import javax.inject.Inject;
import ee.webAppToolkit.core.Result;
import ee.webAppToolkit.core.annotations.Flash;
import ee.webAppToolkit.core.annotations.Optional;
import ee.webAppToolkit.example.projectTimeTracking.domain.Role;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.navigation.annotations.HideFromNavigation;
import ee.webAppToolkit.navigation.annotations.NavigationDisplayName;
import ee.webAppToolkit.parameters.ValidationResults;
import ee.webAppToolkit.parameters.annotations.Parameter;
import ee.webAppToolkit.rendering.RenderingController;
import ee.webAppToolkit.storage.Store;

@NavigationDisplayName(@LocalizedString("navigation.roles"))
public class RoleController extends RenderingController {

    @Inject
    @LocalizedString("role.saved")
    private String _savedMessage;

    @Inject
    @LocalizedString("role.removed")
    private String _removedMessage;

    private Store _store;

    @Inject
    public RoleController(Store store) {
        _store = store;
    }

    public Result index(@Optional @Parameter("role") Role role, @Optional @Parameter("id") Long id, ValidationResults validationResults, @Flash String message) {
        if (validationResults.getValidated("role")) {
            if (role == null) {
                if (id != null) {
                    role = _store.load(Role.class, id);
                }
            } else {
                _store.save(role);
                flash.put(_savedMessage);
                redirect();
            }
        }
        Model model = new Model(_store.list(Role.class), role, message);
        return render(model);
    }

    @HideFromNavigation
    public Result remove(@Parameter("id") Long id) {
        Role role = _store.load(Role.class, id);
        return render(new Model(role));
    }

    @HideFromNavigation
    public void removeConfirm(@Parameter("id") Long id) {
        _store.removeByKey(Role.class, id);
        flash.put(_removedMessage);
        redirect();
    }

    public class Model {

        public Iterable<Role> roles;

        public Role role;

        public String message;

        public Model(Role role) {
            this(null, role, null);
        }

        public Model(Iterable<Role> roles, Role role, String message) {
            this.roles = roles;
            this.role = role;
            this.message = message;
        }
    }
}
