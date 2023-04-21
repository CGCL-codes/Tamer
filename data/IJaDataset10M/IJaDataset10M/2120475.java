package au.org.tpac.portal.service;

import static org.easymock.EasyMock.createMock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import au.org.tpac.portal.domain.Category;
import au.org.tpac.portal.domain.Dataset;
import au.org.tpac.portal.manager.CategoryManager;
import au.org.tpac.portal.manager.DatasetManager;
import au.org.tpac.portal.service.DatasetTreeServiceImpl;

public class DatasetTreeServiceImplTests {

    private DatasetTreeServiceImpl datasetTreeServiceImpl;

    private DatasetManager mockDatasetManager;

    private CategoryManager mockCategoryManager;

    private static int DS1_ID = 3;

    private static String DS1_NAME = "Dataset 1";

    private static int DS2_ID = 4;

    private static String DS2_NAME = "Dataset 2";

    private static int CAT1_ID = 3;

    private static String CAT1_NAME = "Category 1";

    private static int CAT2_ID = 4;

    private static String CAT2_NAME = "Category 2";

    private List<Category> categories;

    private List<Dataset> datasets;

    private HashMap<Integer, String> idsAndNames;

    private HashMap<Integer, String> datasetIdsAndNames1;

    private HashMap<Integer, String> datasetIdsAndNames2;

    @Before
    public void setUp() throws Exception {
        mockDatasetManager = createMock(DatasetManager.class);
        mockCategoryManager = createMock(CategoryManager.class);
        datasetTreeServiceImpl = new DatasetTreeServiceImpl();
        datasetTreeServiceImpl.setCategoryManager(mockCategoryManager);
        datasetTreeServiceImpl.setDatasetManager(mockDatasetManager);
        categories = new ArrayList<Category>();
        datasets = new ArrayList<Dataset>();
        Dataset dataset = new Dataset();
        dataset.setId(DS1_ID);
        dataset.setName(DS1_NAME);
        dataset.setCategoryId(CAT1_ID);
        datasets.add(dataset);
        dataset = new Dataset();
        dataset.setId(DS2_ID);
        dataset.setName(DS2_NAME);
        dataset.setCategoryId(CAT2_ID);
        datasets.add(dataset);
        Category category = new Category();
        category.setName(CAT1_NAME);
        category.setId(CAT1_ID);
        categories.add(category);
        category = new Category();
        category.setName(CAT2_NAME);
        category.setId(CAT2_ID);
        categories.add(category);
        idsAndNames = new HashMap<Integer, String>();
        idsAndNames.put(CAT1_ID, CAT1_NAME);
        idsAndNames.put(CAT2_ID, CAT2_NAME);
        datasetIdsAndNames1 = new HashMap<Integer, String>();
        datasetIdsAndNames2 = new HashMap<Integer, String>();
        datasetIdsAndNames1.put(DS1_ID, DS1_NAME);
        datasetIdsAndNames2.put(DS2_ID, DS2_NAME);
    }

    @Test
    public void testGetNames() {
    }
}
