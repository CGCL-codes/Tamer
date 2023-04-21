package org.elf.weblayer.kernel.security;

import org.elf.datalayer.kernel.KernelSession;
import org.elf.datalayer.kernel.services.security.KernelAuthorization;
import org.elf.datalayer.kernel.services.security.KernelAuthorizationFactory;

/**
 * Seguridad b�sica de la Web. Lo �nico que hace es denegar a la sesi�n publica
 * todo excepto las p�ginas que est� en el raiz de la aplicaci�n
 * @author  <a href="mailto:logongas@users.sourceforge.net">Lorenzo Gonz�lez</a>
 */
public class KernelAuthorizationFactoryImplHardwired implements KernelAuthorizationFactory {

    public void init(KernelSession kernelSession) {
    }

    public KernelAuthorization createKernelAuthorization() {
        return new KernelAuthorizationImplHardwired();
    }
}
