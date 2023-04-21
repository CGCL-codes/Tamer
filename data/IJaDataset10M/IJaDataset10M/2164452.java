package org.fao.fenix.domain.info.pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.fao.fenix.domain.Resource;
import org.fao.fenix.domain.exception.FenixException;
import org.fao.fenix.domain.info.dataset.Dataset;
import org.fao.fenix.domain.info.dataset.Flex1Dataset;
import org.fao.fenix.domain.info.pattern.agriculturalinput.AgriculturalInputPattern;
import org.fao.fenix.domain.info.pattern.cropproduction.CropAreaDataset;
import org.fao.fenix.domain.info.pattern.cropproduction.CropProductionDataset;
import org.fao.fenix.domain.info.pattern.cropproduction.CropProductionPattern;
import org.fao.fenix.domain.info.pattern.demography.DemographyPattern;
import org.fao.fenix.domain.info.pattern.demography.PopulationDataset;
import org.fao.fenix.domain.info.pattern.meteorology.MeteorologyPattern;
import org.junit.Test;
import org.springframework.util.StopWatch;

/**
 * @author Erik van Ingen
 * 
 */
public class PatternUtilTest {

    @Test
    public void testpatternClassList() {
        assertEquals(14, PatternUtil.patternClassList.size());
    }

    @Test
    public void testGetDatasets1() {
        assertEquals(4, PatternUtil.getDatasets(AgriculturalInputPattern.class).size());
        List<Class<? extends Dataset>> list = PatternUtil.getDatasets(CropProductionPattern.class);
        Class<? extends Dataset> datasetClass = list.get(0);
        assertEquals(CropAreaDataset.class, datasetClass);
        datasetClass = list.get(1);
        assertEquals(CropProductionDataset.class, datasetClass);
    }

    /**
	 * test the cache
	 * 
	 */
    @Test
    public void testGetDatasets2() {
        StopWatch stopWatch1 = new StopWatch();
        stopWatch1.start();
        PatternUtil.getDatasets(MeteorologyPattern.class);
        stopWatch1.stop();
        StopWatch stopWatch2 = new StopWatch();
        stopWatch2.start();
        PatternUtil.getDatasets(MeteorologyPattern.class);
        stopWatch2.stop();
        assertTrue(stopWatch1.getTotalTimeMillis() >= stopWatch2.getTotalTimeMillis());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetPattern() {
        assertEquals(DemographyPattern.class, PatternUtil.getPattern(PopulationDataset.class));
        try {
            PatternUtil.getPattern(Flex1Dataset.class);
        } catch (FenixException e) {
        }
    }

    /**
	 * convenience method to retrieve the list of patterns, with its associated
	 * data sets with its associated meta data and dimensions.
	 * 
	 */
    @Test
    public void getListOfDatasets() throws InstantiationException, IllegalAccessException {
    }
}
