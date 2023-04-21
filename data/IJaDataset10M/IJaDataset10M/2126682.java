package net.sf.timemanager.service.records;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import net.sf.hattori.PopulationManager;
import net.sf.timemanager.data.TimeRecordDao;
import net.sf.timemanager.data.util.Logger;
import net.sf.timemanager.domain.projects.Project;
import net.sf.timemanager.domain.records.TimeRecord;
import net.sf.timemanager.domain.users.User;
import net.sf.timemanager.service.projects.dto.ProjectDTO;
import net.sf.timemanager.service.records.dto.TimeRecordDTO;
import net.sf.timemanager.service.users.dto.UserDTO;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;

public class TimeRecordsService extends TimeRecordDao {

    public TimeRecordDTO create(UserDTO userDTO, ProjectDTO projectDTO) {
        Logger.info("Creando un TimeRecord");
        User user = (User) PopulationManager.getInstance().populateDomainObject(userDTO, this.getEntityLocator());
        Project project = (Project) PopulationManager.getInstance().populateDomainObject(projectDTO, this.getEntityLocator());
        TimeRecord nuevo = new TimeRecord(user, project, new Date(), new BigDecimal(1), null, null, null);
        TimeRecordDTO result = (TimeRecordDTO) PopulationManager.getInstance().populateDTO(nuevo, TimeRecordDTO.class);
        return result;
    }

    public void add(TimeRecordDTO timeRecordDTO) {
        Logger.info("Agregando un TimeRecord");
        TimeRecord timeRecord = (TimeRecord) PopulationManager.getInstance().populateDomainObject(timeRecordDTO, this.getEntityLocator());
        this.add(timeRecord);
    }

    public List<TimeRecordDTO> listByProject(ProjectDTO projectDTO) {
        Logger.info("Listando TimeRecords");
        List<TimeRecord> list = this.list(TimeRecord.class);
        CollectionUtils.filter(list, new BeanPropertyValueEqualsPredicate("project.name", projectDTO.getName()));
        List<TimeRecordDTO> result = (List<TimeRecordDTO>) PopulationManager.getInstance().populateDTOList(list, TimeRecordDTO.class);
        return result;
    }
}
