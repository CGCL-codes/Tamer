package net.community.chest.spring.test.beans;

import java.util.List;
import javax.inject.Inject;
import net.community.chest.spring.test.entities.EmbeddingEntity;
import org.springframework.stereotype.Service;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @since Jan 20, 2011 10:32:35 AM
 */
@Service
public class TestEmbeddingServiceImpl implements TestEmbeddingService {

    private final EmbeddingEntityDao _dao;

    protected final EmbeddingEntityDao getEmbeddingEntityDao() {
        return _dao;
    }

    @Inject
    public TestEmbeddingServiceImpl(EmbeddingEntityDao dao) {
        _dao = dao;
    }

    @Override
    public List<EmbeddingEntity> list() {
        final EmbeddingEntityDao dao = getEmbeddingEntityDao();
        return dao.findAll();
    }

    @Override
    public Long create(EmbeddingEntity entity) {
        final EmbeddingEntityDao dao = getEmbeddingEntityDao();
        dao.create(entity);
        return entity.getId();
    }
}
