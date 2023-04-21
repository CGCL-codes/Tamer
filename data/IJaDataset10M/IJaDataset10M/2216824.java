package org.fao.fenix.persistence.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import org.fao.fenix.domain.info.content.Numeric;
import org.fao.fenix.domain.info.dataset.Dataset;
import org.fao.fenix.domain.info.pattern.meteorology.RainfallDataset;
import org.fao.fenix.domain.upload.FenixLoader;
import org.fao.fenix.domain.upload.types.DatasetGroup;
import org.fao.fenix.persistence.BaseDaoTest;

public class DatasetGroupDaoTest extends BaseDaoTest {

    private DatasetGroupDao datasetGroupDao;

    public void xtestTestFindById() {
        fail("Not yet implemented");
    }

    public void testSaveDatasetGroup() {
        FenixLoader fenixloader = new FenixLoader();
        File zipFile = new File("src/test/resources/testfiles/gws/multiple_append.zip");
        try {
            InputStream inputStream = new FileInputStream(zipFile);
            DatasetGroup group = fenixloader.zipPoor2DatasetGroup(RainfallDataset.class, inputStream);
            assertEquals(2, group.getDatasetList().size());
            RainfallDataset dataset1 = (RainfallDataset) group.getDatasetList().get(0);
            RainfallDataset dataset2 = (RainfallDataset) group.getDatasetList().get(1);
            assertEquals(1324, dataset1.getContentList().size());
            assertEquals(159, dataset2.getContentList().size());
            datasetGroupDao.save(group);
            DatasetGroup datasetGroupFromDatabase = datasetGroupDao.findAll().get(0);
            assertEquals(group.getId(), datasetGroupFromDatabase.getId());
            Iterator<Dataset> iterator = datasetGroupFromDatabase.getDatasetList().iterator();
            RainfallDataset datasetje1 = (RainfallDataset) iterator.next();
            RainfallDataset datasetje2 = (RainfallDataset) iterator.next();
            assertEquals(1324, datasetje1.getContentList().size());
            assertEquals(159, datasetje2.getContentList().size());
            Numeric rp = (Numeric) datasetje1.getContentList().get(101);
            assertEquals(87.0d, rp.getValue());
            assertEquals("37785", rp.getFeatureCode());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    public void xtestTestUpdate() {
        fail("Not yet implemented");
    }

    public void xtestTestDelete() {
        fail("Not yet implemented");
    }

    public void xtestTestFindAll() {
        fail("Not yet implemented");
    }

    public DatasetGroupDao getDatasetGroupDao() {
        return datasetGroupDao;
    }

    public void setDatasetGroupDao(DatasetGroupDao datasetGroupDao) {
        this.datasetGroupDao = datasetGroupDao;
    }
}
