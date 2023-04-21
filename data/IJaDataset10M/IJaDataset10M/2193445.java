package org.elf.weblayer.kernel.security;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;
import org.elf.common.StringUtil;
import org.elf.datalayer.DLSession;
import org.elf.datalayer.kernel.services.security.*;

/**
 *
 * @author  <a href="mailto:logongas@users.sourceforge.net">Lorenzo Gonz�lez</a>
 */
public class KernelAuthorizationImplHardwired implements KernelAuthorization {

    /**
     * Averigua si el usuario est� autorizado el acceso a cierto recurso protegido mediante cierta operaci�n
     * @param object Objeto protegido del que se quiere saber si se tiene permiso
     * @param method M�todo con la que queremos acceder al objeto
     * @return Retorna si se tiene o no acceso a un recurso o no se sabe
     */
    public AccessType isAccessAuthorized(Object object, Object method) {
        AccessType accessType = null;
        if (object instanceof URI) {
            String url = ((URI) object).toString();
            List<String> urlPrefixGranted = new ArrayList<String>();
            Collections.addAll(urlPrefixGranted, "/common/javascript", "/controls/javascript", "/controls/img", "/controls/css", "/img", "/js", "/css", "/icons", "/controller/DLSession/service/createSession");
            for (int i = 0; i < urlPrefixGranted.size(); i++) {
                if (url.startsWith(urlPrefixGranted.get(i))) {
                    accessType = AccessType.Granted;
                }
            }
            List<String> urlGranted = new ArrayList<String>();
            Collections.addAll(urlGranted, "/common/dictionary/DL_User");
            for (int i = 0; i < urlGranted.size(); i++) {
                if (url.equals(urlGranted.get(i))) {
                    accessType = AccessType.Granted;
                }
            }
            if (accessType == null) {
                if (DLSession.getSecurity().isPublic() == true) {
                    if (StringUtil.stringCount(url, "/") > 1) {
                        accessType = AccessType.DenyIfAllOthersAbstain;
                    } else {
                        accessType = AccessType.GrantedIfAllOthersAbstain;
                    }
                } else {
                    accessType = AccessType.GrantedIfAllOthersAbstain;
                }
            }
        } else {
            if (DLSession.getSecurity().isPublic() == true) {
                accessType = AccessType.DenyIfAllOthersAbstain;
            } else {
                accessType = AccessType.GrantedIfAllOthersAbstain;
            }
        }
        return accessType;
    }
}
