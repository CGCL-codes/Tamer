package com.poltman.dspace.db.repository.pm;

import org.springframework.data.jpa.repository.JpaRepository;
import com.poltman.dspace.db.entity.pm.ConfigurationEntity;

/**
 * 
 * @author z.ciok@poltman.com
 *
 */
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Integer> {

    public ConfigurationEntity findByParam(String param);
}
