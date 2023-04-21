package lv.odylab.evemanage.service.eve;

import com.googlecode.objectify.Key;
import lv.odylab.evemanage.domain.eve.Character;
import lv.odylab.evemanage.domain.user.User;
import java.util.List;

public interface CharacterSynchronizationService {

    void synchronizeCreateCharacter(Character character, Key<User> userKey);

    void synchronizeDeleteCharacter(Long characterID, Key<User> userKey);

    void synchronizeDetachCharacters(List<Character> detachedCharacters, Key<User> userKey);

    void synchronizeUpdateCharacters(List<Character> updatedCharacters, Key<User> userKey);
}
