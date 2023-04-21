package br.org.ged.direto.model.service.impl;

import java.util.List;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import br.org.ged.direto.model.entity.PstGrad;
import br.org.ged.direto.model.repository.PstGradRepository;
import br.org.ged.direto.model.service.PstGradService;

@Service("pstgradService")
@RemoteProxy(name = "pstgradJS")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class PstGradServiceImpl implements PstGradService {

    private PstGradRepository pstgradRepository;

    @Autowired
    public void setPstgradRepository(PstGradRepository pstgradRepository) {
        this.pstgradRepository = pstgradRepository;
    }

    @Override
    public PstGrad getPstGradById(Integer id) {
        return this.pstgradRepository.getPstGradById(id);
    }

    @Override
    @RemoteMethod
    public List<PstGrad> getAll() {
        return this.pstgradRepository.getAll();
    }
}
