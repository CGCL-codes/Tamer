package org.dcm4chee.web.dao.trash;

import java.util.List;
import javax.ejb.Local;
import org.dcm4che2.data.DicomObject;
import org.dcm4chee.archive.entity.BaseEntity;
import org.dcm4chee.archive.entity.PrivateFile;
import org.dcm4chee.archive.entity.PrivateInstance;
import org.dcm4chee.archive.entity.PrivatePatient;
import org.dcm4chee.archive.entity.PrivateSeries;
import org.dcm4chee.archive.entity.PrivateStudy;
import org.dcm4chee.archive.entity.Study;

/**
 * @author Franz Willer <franz.willer@gmail.com>
 * @version $Revision$ $Date$
 * @since May 10, 2010
 */
@Local
public interface TrashListLocal {

    String JNDI_NAME = "dcm4chee-web-ear/TrashListBean/local";

    int count(TrashListFilter filter, List<String> roles);

    List<PrivatePatient> findPatients(TrashListFilter filter, int pagesize, int offset, List<String> roles);

    public int countStudiesOfPatient(long pk, List<String> roles);

    List<PrivateStudy> findStudiesOfPatient(long pk, List<String> roles);

    List<PrivateSeries> findSeriesOfStudy(long pk);

    List<PrivateInstance> findInstancesOfSeries(long pk);

    PrivatePatient getPatient(long pk);

    PrivateStudy getStudy(long pk);

    PrivateSeries getSeries(long pk);

    PrivateInstance getInstance(long pk);

    public void removeTrashEntities(List<Long> pks, Class<? extends BaseEntity> clazz, boolean removeFile);

    public void removeTrashAll();

    public List<PrivateFile> getFilesForEntity(long pk, Class<? extends BaseEntity> clazz);

    public List<Study> getStudiesInFolder(String[] suids);

    public DicomObject getDicomAttributes(long filePk);

    public Long getNumberOfSeriesOfStudy(long studyPk);

    public Long getNumberOfInstancesOfStudy(long studyPk);

    public Long getNumberOfInstancesOfSeries(long seriesPk);
}
