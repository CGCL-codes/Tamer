package br.org.ged.direto.model.repository.hibernate;

import java.util.List;
import org.springframework.stereotype.Repository;
import br.org.ged.direto.model.entity.PstGrad;
import br.org.ged.direto.model.repository.PstGradRepository;

@Repository("pstgradRepository")
public class PstGradRepositoryImpl extends BaseRepositoryImpl implements PstGradRepository {

    @Override
    public PstGrad getPstGradById(Integer id) {
        return hibernateTemplate.get(PstGrad.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PstGrad> getAll() {
        return (List<PstGrad>) hibernateTemplate.find("from " + PstGrad.class.getName());
    }
}
