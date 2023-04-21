package com.andrewswan.gamenight.domain.club;

import javax.persistence.Query;
import com.andrewswan.gamenight.domain.UniquenessValidator;

/**
 * JSR-303 style validator that checks a {@link Club} has a unique name.
 * 
 * @author Admin
 */
public class UniqueClubNameValidator extends UniquenessValidator<UniqueClubName, Club> {

    @Override
    protected Query getMatchesQuery(final Club club) {
        return Club.findClubsByNameLike(club.getName());
    }
}
