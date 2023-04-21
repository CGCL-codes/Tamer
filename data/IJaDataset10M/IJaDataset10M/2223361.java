package com.movilnet.clom.framework.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import com.movilnet.clom.framework.entity.Equipo;
import com.movilnet.clom.framework.repository.IGenericRepositoryRemote;

/**
 * @author jpacheco
 *
 */
public class TestDevice {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testNamedQuery() throws Exception {
        InitialContext context = new InitialContext();
        IGenericRepositoryRemote repository = (IGenericRepositoryRemote) context.lookup("ejb/genericRepository");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("serialid", "232213");
        List<Equipo> equipos = (List<Equipo>) repository.findByQueryNamedAndNamedParams(Equipo.class, "equipo.getDeviceMainBySerial", params);
        for (Equipo equipo : equipos) {
            System.out.println("Equipo {id: " + equipo.getId() + ", numero: " + equipo.getNumeroasignado() + "}");
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            TestDevice test = new TestDevice();
            test.testNamedQuery();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
