package br.com.caelum.recorder.modelo;

import java.io.Serializable;
import java.util.List;

/**
 * @author leonardobessa
 * 
 */
public interface RestaurantRepository {

    public List<Restaurant> findAll();

    public List<Restaurant> findAllByName(String name);

    public List<Restaurant> findAllByNameAndSpeciality(String name, String speciality);

    public Restaurant find(Serializable id);

    public Restaurant findByName(String name);

    public List<Restaurant> findAll(RestaurantCondition conditions);
}
