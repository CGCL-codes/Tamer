package com.movilnet.clom.framework.test;

import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.hibernate.Session;
import com.movilnet.clom.framework.entity.EnvioRecepcion;
import com.movilnet.clom.framework.repository.IGenericRepositoryRemote;

public class TestDomain {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        TestDomain test = new TestDomain();
        try {
            test.testEnvioRecepcion();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings({ "rawtypes", "unused", "unchecked" })
    public void testEnvioRecepcion() throws NamingException {
        InitialContext context = new InitialContext();
        IGenericRepositoryRemote repository = (IGenericRepositoryRemote) context.lookup("ejb/genericRepository");
        List<EnvioRecepcion> envioRecepcion = (List<EnvioRecepcion>) repository.findAll(EnvioRecepcion.class);
    }

    public static void testCaja(Session session) {
    }
}
