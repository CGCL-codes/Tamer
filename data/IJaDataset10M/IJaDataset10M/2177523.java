package org.ctor.dev.llrps2.persistence;

import org.ctor.dev.llrps2.model.Round;

public interface RoundDao extends BaseDao<Round, Long> {

    Round findByName(String name);
}
