package net.jomper.shyfish.persistent;

import java.util.List;
import net.jomper.shyfish.exception.ShyfishDataAccessException;
import net.jomper.shyfish.model.Consume;

public interface ConsumeDao extends BaseDao<Consume> {

    public void addConsumeTag(String consumeId, String tagId) throws ShyfishDataAccessException;

    /**
	 * delete Consumes and ConsumeTags by id
	 * @param id
	 * @throws DataAccessException
	 */
    public void deleteConsumeAndConsumeTags(String... id) throws ShyfishDataAccessException;

    public void deleteConsumeTag(String consumeId, String tagId) throws ShyfishDataAccessException;

    public List<Consume> getUserConsumes(String email) throws ShyfishDataAccessException;

    public List<Consume> getUserConsumesByDate(String email, String date) throws ShyfishDataAccessException;

    public List<Consume> getUserConsumesByMonth(String email, String month) throws ShyfishDataAccessException;

    public Consume updateConsume(Consume consume) throws ShyfishDataAccessException;

    public void updateConsumePubliced(String id, boolean publiced) throws ShyfishDataAccessException;
}
