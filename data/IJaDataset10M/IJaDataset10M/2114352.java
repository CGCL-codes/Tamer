package com.poltman.dspace.db.repository.pm;

import org.springframework.data.jpa.repository.JpaRepository;
import com.poltman.dspace.db.entity.pm.MainMenuLabelsEntity;

/**
 * 
 * @author z.ciok@poltman.com
 *
 */
public interface MainMenuLabelsRepository extends JpaRepository<MainMenuLabelsEntity, Integer> {

    public MainMenuLabelsEntity findByKeyAndLanguageCode(String key, String languageCode);
}
