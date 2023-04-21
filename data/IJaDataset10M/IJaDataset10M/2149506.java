package pl.edu.agh.uddiProxy.dao;

import java.util.List;
import pl.edu.agh.uddiProxy.types.ServiceDynamicParameters;

public interface DynamicParameterDAO {

    void create(ServiceDynamicParameters dynamicParameter);

    void update(ServiceDynamicParameters dynamicParameter);

    void delete(ServiceDynamicParameters dynamicParameter);

    boolean exists(String tModelKey);

    boolean exists(Long id);

    List<ServiceDynamicParameters> getAll();

    List<Long> getAllIds();

    ServiceDynamicParameters getById(Long id);

    ServiceDynamicParameters getByTModel(String tModelKey);

    List<ServiceDynamicParameters> getDynamicParameters(final String query);

    List<ServiceDynamicParameters> getDynamicParameters(final String query, List<String> tModels);

    List<String> getTModels(final String query);

    List<String> getTModels(final String query, List<String> tModels);
}
