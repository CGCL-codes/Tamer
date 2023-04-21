package ua.orion.cpu.core.licensing.services;

import java.util.*;
import org.apache.tapestry5.jpa.annotations.CommitAfter;
import ua.orion.cpu.core.licensing.entities.*;
import ua.orion.cpu.core.orgunits.entities.*;

/**
 *
 * @author sl
 */
public interface LicensingService {

    /**
     * Поиск лицензии с указанными серией, номером и датой получения
     *
     * @param serial
     * @param number
     * @param issue
     * @return экземпляр лицензии
     */
    License findLicense(String serial, String number, Calendar issue);

    LicenseRecord findLicenseRecordByTrainingDirection(String serial, String number, Calendar issue, String educationalQualificationLevel, String trainingDirection, Calendar termination);

    LicenseRecord findLicenseRecordBySpeciality(String serial, String number, Calendar issue, String educationalQualificationLevel, String speciality, Calendar termination);

    List<LicenseRecord> findLicenseRecordsByOrgUnit(OrgUnit orgUnit);

    List<LicenseRecord> findLicenseRecordsByTerminationDate(Date terminationDate);

    List<LicenseRecord> findLicenseRecordsByEducationalQualificationLevel(String codeLevel);

    List<LicenseRecord> findLicenseRecordsByTrainingDirection(String codeDirection);

    boolean existsNewStateLicense();

    /**
     * Переводит лицензию в состоянии NEW в состояние FORCED с соответствующим
     * изменением состояния прежней FORCED лицензии {@link ua.orion.cpu.core.licensing.entities.License.getLicenseState()}
     *
     * @param license
     * @return сохраненная лицензия
     */
    @CommitAfter
    void forceAndMergeLicense(License license);

    /**
     * Создает и сохраняет новую лицензию на основе текущей FORCED лицензии
     *
     * @return сохраненная лицензия
     */
    @CommitAfter
    void cloneLicenseRecordsFromForced(License license);

    /**
     * Поиск последней действующей лицензии
     *
     * @return экземпляр лицензии
     */
    License findForcedLicense();

    List<Speciality> findSpecialityByKnowledgeArea(KnowledgeArea knowledgeArea);

    List<TrainingDirection> findTrainingDirectionByKnowledgeArea(KnowledgeArea knowledgeArea);
}
