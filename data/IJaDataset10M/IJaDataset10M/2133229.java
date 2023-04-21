package lv.odylab.evemanage.service.blueprint;

import com.googlecode.objectify.Key;
import lv.odylab.evemanage.application.exception.EveApiException;
import lv.odylab.evemanage.application.exception.EveDbException;
import lv.odylab.evemanage.application.exception.validation.InvalidNameException;
import lv.odylab.evemanage.domain.blueprint.Blueprint;
import lv.odylab.evemanage.domain.user.User;
import lv.odylab.evemanage.integration.evedb.dto.BlueprintDetailsDto;
import lv.odylab.evemanage.integration.evedb.dto.TypeMaterialDto;
import lv.odylab.evemanage.integration.evedb.dto.TypeRequirementDto;
import java.util.List;

public interface BlueprintManagementService {

    Blueprint createBlueprint(String blueprintTypeName, Integer meLevel, Integer peLevel, Key<User> userKey) throws EveDbException, InvalidNameException;

    Blueprint createBlueprint(Long blueprintTypeID, Long itemID, Integer meLevel, Integer peLevel, Long attachedCharacterID, String sharingLevel, Key<User> userKey) throws EveDbException, InvalidNameException;

    BlueprintDetailsDto getBlueprintDetails(Long blueprintTypeID) throws EveDbException;

    BlueprintDetailsDto getBlueprintDetails(String blueprintTypeName) throws EveDbException, InvalidNameException;

    List<TypeMaterialDto> getTypeMaterials(Long typeID) throws EveDbException;

    List<TypeRequirementDto> getTypeRequirements(Long typeID) throws EveDbException;

    void put(Blueprint blueprint, Key<User> userKey);

    List<Blueprint> getBlueprints(Key<User> userKey);

    List<Blueprint> getCorporationBlueprints(Key<User> userKey);

    List<Blueprint> getAllianceBlueprints(Key<User> userKey);

    Blueprint saveBlueprint(Long blueprintID, Long itemID, Integer meLevel, Integer peLevel, Long attachedCharacterID, String sharingLevel, Key<User> userKey);

    void deleteBlueprint(Long blueprintID, Key<User> userKey);

    void importBlueprintsFromXml(String importXml, Long attachedCharacterID, String sharingLevel, Key<User> userKey) throws EveApiException;

    void importBlueprintsFromCsv(String importCsv, Long attachedCharacterID, String sharingLevel, Key<User> userKey);

    void importBlueprintsUsingOneTimeFullApiKey(String fullApiKey, Long userID, Long characterID, String level, Long attachedCharacterID, String sharingLevel, Key<User> userKey) throws EveApiException;

    void importBlueprintsUsingFullApiKey(Long characterID, String level, Long attachedCharacterID, String sharingLevel, Key<User> userKey) throws EveApiException;
}
