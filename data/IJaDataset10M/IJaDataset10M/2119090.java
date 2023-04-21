package br.org.acessobrasil.processoacessibilidade.dao;

import java.util.List;
import javax.persistence.Query;
import br.org.acessobrasil.processoacessibilidade.vo.JobPro;
import br.org.acessobrasil.processoacessibilidade.vo.PaginaPro;

public class JobDao extends SuperDao<JobPro> {

    @Override
    public JobPro find(long id) {
        return null;
    }

    /**
	 * Carrega a lista e o indice
	 * @param job
	 */
    public void reload(JobPro job) {
        Query avaliadas = getEntityManager().createQuery("Select p from PaginaPro p where p.idSitio=? and p.originalData != null");
        avaliadas.setParameter(1, job.getSitio().getId());
        Query q = getEntityManager().createQuery("Select p from PaginaPro p where p.idSitio=? and p.originalData = null");
        q.setParameter(1, job.getSitio().getId());
        List<PaginaPro> listAvaliadas = avaliadas.getResultList();
        for (PaginaPro p : listAvaliadas) {
            job.getListaLink().add(p.getUrl());
        }
        if (listAvaliadas.size() > 0) {
            job.setIndice(listAvaliadas.size() - 1);
        }
        List<PaginaPro> listNaoAvaliadas = q.getResultList();
        for (PaginaPro p : listNaoAvaliadas) {
            job.getListaLink().add(p.getUrl());
        }
    }
}
