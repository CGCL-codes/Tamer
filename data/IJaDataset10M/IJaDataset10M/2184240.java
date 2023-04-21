package com.dosideas.business.ejb.provincia;

import com.dosideas.business.ProvinciaBo;
import com.dosideas.business.exception.NombreInvalidoException;
import com.dosideas.domain.Provincia;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Service;

/**
 *
 * @author mjcali
 */
@Stateless(mappedName = "ejb/ProvinciaSessionBean")
@Interceptors(SpringBeanAutowiringInterceptor.class)
@Service
public class ProvinciaSessionBean implements ProvinciaSessionBeanRemote {

    @Autowired
    private ProvinciaBo provinciaBo;

    public Provincia buscarProvinciaPorId(Long id) {
        return provinciaBo.buscarProvinciaPorId(id);
    }

    public Collection<Provincia> buscarProvinciaPorNombreExacto(String nombre) throws NombreInvalidoException {
        return provinciaBo.buscarProvinciaPorNombreExacto(nombre);
    }

    public Collection<Provincia> buscarProvinciasPorNombre(String nombre) throws NombreInvalidoException {
        return provinciaBo.buscarProvinciasPorNombre(nombre);
    }

    public void guardarProvincia(Provincia provincia) throws NombreInvalidoException {
        provinciaBo.guardarProvincia(provincia);
    }
}
