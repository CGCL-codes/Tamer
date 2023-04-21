package pl.dookola.logic;

import javax.persistence.EntityManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import pl.dookola.model.profile.Profile;

@Name("profileEditAction")
@Scope(ScopeType.SESSION)
public class ProfileEditAction {

    @In(required = true)
    private Profile profile;

    @In
    private EntityManager entityManager;

    public void save() {
        entityManager.merge(profile);
    }
}
