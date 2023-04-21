package ar.com.AmberSoft.iEvenTask.services;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import ar.com.AmberSoft.iEvenTask.backend.entities.Entity;
import ar.com.AmberSoft.iEvenTask.backend.entities.Permission;
import ar.com.AmberSoft.iEvenTask.backend.entities.Profile;
import ar.com.AmberSoft.util.ParamsConst;
import ar.com.AmberSoft.util.ProfileManager;

@SuppressWarnings("rawtypes")
public class CreateProfileService extends CreateService {

    @Override
    public Map execute(Map params) throws Exception {
        Map result = super.execute(params);
        ProfileManager.getInstance().load();
        return result;
    }

    @Override
    public Map onEmulate(Map params) {
        return null;
    }

    @Override
    public Entity getEntity(Map params) {
        Profile profile = new Profile();
        profile.setGroupLDAP((String) params.get(ParamsConst.GROUP));
        Collection permisos = (Collection) params.get(ParamsConst.PERMISSIONS);
        Iterator<Integer> it = permisos.iterator();
        while (it.hasNext()) {
            Integer actual = (Integer) it.next();
            setPermiso(profile, actual.toString());
        }
        return profile;
    }

    public void setPermiso(Profile perfil, String idPermiso) {
        Permission permiso = new Permission();
        permiso.setId(idPermiso);
        perfil.addPermiso(permiso);
    }
}
