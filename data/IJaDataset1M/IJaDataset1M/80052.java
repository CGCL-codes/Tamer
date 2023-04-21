package org.jrazdacha.dao.hibernate;

import org.hibernate.Query;
import org.jrazdacha.announce.exception.TorrentNotFoundException;
import org.jrazdacha.dao.TorrentDAO;
import org.jrazdacha.model.Torrent;

/**
 * Class representing Data Access Object for Torrents.
 * 
 * @author Alexey Rogatkin
 * @author Vitaliy Ruzhnikov
 * @author Alexey Tulin
 */
public class TorrentHibernateDAO extends AbstractHibernateDAO<Long, Torrent> implements TorrentDAO {

    /**
	 * Retrieves <code>Torrent</code> object with same info_hash
	 * <code>hash</code>.
	 * 
	 * @param infoHash
	 *            info_hash of torrent
	 * @return torrent entity
	 */
    @Override
    public Torrent findTorrentByInfoHash(String infoHash) {
        Torrent result;
        Query query = getSession().getNamedQuery("Torrent.findByInfoHash").setString("infoHash", infoHash);
        result = (Torrent) query.uniqueResult();
        if (result == null) {
            throw new TorrentNotFoundException(infoHash);
        }
        return result;
    }
}
