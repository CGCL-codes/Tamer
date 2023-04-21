package org.stars.daostars.pipeline;

import org.stars.dao.exception.DaoException;
import org.stars.daostars.DaoBase;
import org.stars.daostars.DaoSession;
import org.stars.daostars.DaoUtility;
import org.stars.daostars.EntityBean;
import org.stars.daostars.pipeline.DaoPipeline.TypeConnection;
import org.stars.daostars.pipeline.DaoPipelineElement.TypeQuery;
import org.stars.daostars.sqlmapper.SqlMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Gestisce l'esecuzione delle pipeline.
 * 
 * @author Francesco Benincasa (908099)
 * @date 21/nov/07, 14:06:57
 * 
 */
public abstract class PipelineRuntime {

    /**
	 * esegue una pipeline.
	 * 
	 * @param dao
	 *            dao da cui si esegue la pipeline
	 * @param <E>
	 *            entity bean gestito dal dao
	 * @param nome
	 *            nome della pipeline
	 * @param input
	 *            oggetto utilizzato come parametri in ingresso
	 * @return lista di entity bean ottenuti dalla pipeline
	 * @throws Exception
	 *             in caso di errore
	 */
    @SuppressWarnings("unchecked")
    public static <E> List run(DaoBase<E> dao, String nome, EntityBean input) throws Exception {
        List ret = new ArrayList();
        SqlMapper definition = dao.getDefinition();
        DaoPipeline pipeline = definition.getPipeline(nome);
        DaoSession session = new DaoSession();
        if (pipeline.getTypeConnection() == TypeConnection.TRANSACTION) {
            session.beginTransation();
        } else {
            session.openConnection();
        }
        session.addDao(dao);
        if (!pipeline.isEnabled()) {
            throw (new DaoException("The pipeline " + pipeline.getName() + " of the dao " + definition.getName() + " is disabled!"));
        }
        Set<String> columnSet = null;
        try {
            for (DaoPipelineElement item : pipeline.getPipeline()) {
                if (item.getType() == TypeQuery.READ) {
                    int row = dao.read(item.getQueryName(), input);
                    if (row > 0) {
                        columnSet = DaoUtility.getFilledPropertiesSet(dao.getDettaglioBean(), dao.getColumns());
                        input = (EntityBean) DaoUtility.copyAttributes(input, dao.getDettaglioBean(), columnSet);
                    }
                } else if (item.getType() == TypeQuery.EXECUTE) {
                    dao.execute(item.getQueryName(), input);
                } else if (item.getType() == TypeQuery.BATCH_EXECUTE) {
                    throw (new DaoException("Actually query batch is not supported from the pipeline " + pipeline.getName() + " of the dao " + definition.getName() + ""));
                }
            }
            if (pipeline.getTypeConnection() == TypeConnection.TRANSACTION) {
                session.commitTransaction();
            } else {
                session.closeConnection();
            }
        } catch (Exception e) {
            if (pipeline.getTypeConnection() == TypeConnection.TRANSACTION) {
                session.rollbackTransaction();
            } else {
                session.closeConnection();
            }
        }
        return ret;
    }
}
