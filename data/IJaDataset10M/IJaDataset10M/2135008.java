package com.proyecto.tropero.core.service.model.Impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import com.proyecto.tropero.core.bd.DAO.model.Interface.IAnimalDAO;
import com.proyecto.tropero.core.domain.Animal;
import com.proyecto.tropero.core.domain.Dieta;
import com.proyecto.tropero.core.domain.GrupoAnimal;
import com.proyecto.tropero.core.domain.Origen;
import com.proyecto.tropero.core.domain.Ubicacion;
import com.proyecto.tropero.core.domain.arquitectura.IObjetoPersistente;
import com.proyecto.tropero.core.service.GenericServiceImpl;
import com.proyecto.tropero.core.service.model.ServiceLocator;
import com.proyecto.tropero.core.service.model.Interface.IAnimalService;

public class AnimalServiceImpl extends GenericServiceImpl implements IAnimalService {

    public IAnimalDAO getDao() {
        return (IAnimalDAO) super.getDao();
    }

    public List<Animal> getAnimalesConGrupoDiferentesA(GrupoAnimal grupo) {
        return this.getDao().getAnimalesConGrupoDiferentesA(grupo);
    }

    @SuppressWarnings("unchecked")
    public List<Animal> findAnimalPorNombre(String nombre) {
        return this.getDao().findAnimalPorNombre(nombre);
    }

    @SuppressWarnings("unchecked")
    public List<Animal> findAnimalPorIdSenasa(String id) {
        return this.getDao().findAnimalPorIdSenasa(id);
    }

    public List<Animal> findAnimalesConGrupo(GrupoAnimal grupo) {
        return this.getDao().getAnimalesConGrupo(grupo);
    }

    public List<Animal> findOthers(List<String> animalesId) {
        return this.getDao().findOthers(animalesId);
    }

    public List<Animal> findByDieta(Dieta dieta) {
        return this.getDao().findByDieta(dieta);
    }

    @Override
    public List<Animal> findByUbicacion(Ubicacion ubicacion) {
        return this.getDao().findByUbicacion(ubicacion);
    }

    @Override
    public List<Animal> findByOrigen(Origen origen) {
        return this.getDao().findByOrigen(origen);
    }

    public List<Animal> findByPeso(Integer peso) {
        return this.getDao().findByPeso(peso);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void cambiarDieta(List<Animal> listAnimalesAsociar, Dieta dieta) throws SQLException {
        for (Animal animal : listAnimalesAsociar) {
            animal.setDieta(dieta);
        }
        try {
            this.getDao().saveOrUpdateAll((List) listAnimalesAsociar);
            ServiceLocator.getAlertaService().actualizarAlertas();
        } catch (HibernateJdbcException e) {
            throw new SQLException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Animal> findBy(Criterion... criterion) {
        return this.getDao().findBy(criterion);
    }

    @SuppressWarnings("unchecked")
    public Animal findUniqueAnimalPorNombre(String nombre) {
        return this.getDao().findUniqueAnimalPorNombre(nombre);
    }

    public List<Animal> findAllPadres() {
        return this.getDao().findAllPadres();
    }

    public List<Animal> findAllMadres() {
        return this.getDao().findAllMadres();
    }

    @Override
    public IObjetoPersistente findById(Object objectId) {
        String id = (String) objectId;
        return this.getDao().findById(id);
    }

    @Override
    public List<Animal> getAnimalesFromIds(List<String> ids) {
        return findOthers(ids);
    }

    @Override
    public List<String> obtenerListaNoExistentes(List<String> listaTodos, List<Animal> listaAnimalesExistentes) {
        List<String> lista = new ArrayList<String>(listaTodos);
        for (Animal animal : listaAnimalesExistentes) {
            lista.remove(animal.getIdSenasa());
        }
        return lista;
    }

    @Override
    public List<Animal> getAnimalesFromIdSenasa(List<String> ids) {
        return this.getDao().getAnimalesFromIdSenasa(ids);
    }

    @Override
    public List<Animal> findAnimalPorIdSenasaSexo(String id, String sexo) {
        return this.getDao().findAnimalPorIdSenasaSexo(id, sexo);
    }

    @Override
    public List<Animal> findAnimalPorNombreSexo(String nombre, String sexo) {
        return this.getDao().findAnimalPorNombreSexo(nombre, sexo);
    }

    @Override
    public void eliminarTodos(List<Animal> animalesSelected) {
        this.getDao().deleteAll(animalesSelected);
    }

    @Override
    public void updateDieta(Integer id) {
        String[] parametersNames = new String[1];
        Object[] parametersValue = new Object[1];
        parametersNames[0] = "idDieta";
        parametersValue[0] = id;
        this.getDao().executeUpdate("ACTUALIZAR_DIETA_ELIMINADA", parametersNames, parametersValue);
    }
}
