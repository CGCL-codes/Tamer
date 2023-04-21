package genericAlgorithm.tsp.bean.impl;

import genericAlgorithm.framework.appcontroller.AlgorithmContext;
import genericAlgorithm.framework.bean.AlgorithmDefinitionHandler;
import genericAlgorithm.framework.handlers.ResponseContext;
import genericAlgorithm.tsp.entity.City;
import genericAlgorithm.tsp.*;
import genericAlgorithm.framework.bean.ScenarioDefinition;
import genericAlgorithm.framework.dataTypes.GenericAlgorithmAVo;
import genericAlgorithm.framework.errorHandling.ErrorCodeConstants;
import genericAlgorithm.framework.errorHandling.exception.GenericAlgorithmException;
import genericAlgorithm.framework.handlers.AlgorithmHandler;
import genericAlgorithm.framework.handlers.BuildPopulationHandler;
import genericAlgorithm.framework.utility.AlgorithmConstants;
import genericAlgorithm.tsp.utility.TSPAlgorithmConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Oz
 */
public class BuildTriangleCityRoutesHandler implements BuildPopulationHandler {

    private int DEFAULT_INTIAL_POPULATION_SIZE = 10;

    private int MAX_ROUTE_LENTH = 10;

    private int m_fixedCityLenth = 1;

    private boolean m_isFixedLenth = true;

    private AlgorithmDefinitionHandler definitionHandler;

    private int m_citySize;

    private Map<Integer, City> m_cityMap;

    private Map<String, ScenarioDefinition> scenarioMap;

    @Override
    public ResponseContext execute(AlgorithmContext aContext) {
        scenarioMap = definitionHandler.getScenarioMap();
        GenericAlgorithmAVo ga = (GenericAlgorithmAVo) aContext.getValue(AlgorithmConstants.Context.GENERIC_ALGORITHM_KEY_NAME);
        initCitySize(ga);
        createRowCityLst();
        createRowRoutes();
        createLastCity();
        ((TSPAlgorithmDefinitionHandler) definitionHandler).setCityMap(m_cityMap);
        aContext.setValue(TSPAlgorithmConstants.Context.CITY_MAP_KEY_NAME, m_cityMap);
        return null;
    }

    private void initCitySize(GenericAlgorithmAVo algorithmAVo) {
        ScenarioDefinition defn = scenarioMap.get(algorithmAVo.getScenarioKey());
        if (defn == null) {
            throw new GenericAlgorithmException(ErrorCodeConstants.Technical.GENERIC_ALGORITHM_SCENARIO_DEFENITIONIS_NULL);
        }
        m_citySize = defn.getDefaultEntitySize();
    }

    private void createRowCityLst() {
        int rowCities = m_citySize - 1;
        m_cityMap = new HashMap<Integer, City>(rowCities);
        for (int i = 0; i < rowCities; i++) {
            int cityId = i;
            m_cityMap.put(cityId, new City(cityId));
        }
    }

    private void createRowRoutes() {
        City source = null;
        int rowRoutes = m_citySize - 1;
        for (int i = 0; i < rowRoutes; i++) {
            source = m_cityMap.get(i);
            source.setRouteMap(generateRowCityRouteLst(source));
        }
    }

    private Map<Integer, Double> generateRowCityRouteLst(City source) {
        Map<Integer, Double> routeMap = new HashMap<Integer, Double>();
        City destination;
        for (int i = 0; i < m_cityMap.size(); i++) {
            destination = m_cityMap.get(i);
            if (destination.getId() == source.getId()) continue; else {
                routeMap.put(destination.getId(), calculateRoute(source, destination, false));
            }
        }
        return routeMap;
    }

    private double calculateRoute(City source, City dest, boolean isLstCity) {
        double lenth = 0;
        if (isLstCity) {
            lenth = Math.sqrt(Math.pow(dest.getId(), 2) + Math.pow(Math.PI, 2));
        } else if (m_isFixedLenth) {
            lenth = Math.abs(source.getId() - dest.getId());
        } else {
            lenth = new Random().nextInt(MAX_ROUTE_LENTH);
        }
        return lenth;
    }

    private void createLastCity() {
        int lastCityId = m_citySize - 1;
        City lastCity = new City(lastCityId);
        lastCity.setRouteMap(new HashMap<Integer, Double>());
        City destination = null;
        for (int i = 0; i < m_cityMap.size(); i++) {
            destination = m_cityMap.get(i);
            lastCity.getRouteMap().put(destination.getId(), calculateRoute(lastCity, destination, true));
            destination.getRouteMap().put(lastCity.getId(), calculateRoute(lastCity, destination, true));
        }
        m_cityMap.put(lastCityId, lastCity);
    }

    @Override
    public AlgorithmDefinitionHandler getDefinitionHandler() {
        return definitionHandler;
    }

    @Override
    public void setDefinitionHandler(AlgorithmDefinitionHandler definitionHandler) {
        this.definitionHandler = definitionHandler;
    }
}
