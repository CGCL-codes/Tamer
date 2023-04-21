package org.fao.fenix.web.modules.fpi.server;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.fao.fenix.domain.commodity.HS;
import org.fao.fenix.domain.gaul.FenixGaul0;
import org.fao.fenix.domain.gaul.FenixGaul1;
import org.fao.fenix.domain.gaul.FenixGaul2;
import org.fao.fenix.domain.info.content.NumericCommodity;
import org.fao.fenix.persistence.commodity.HSDao;
import org.fao.fenix.persistence.gaul.FenixGaulDao;
import org.fao.fenix.persistence.info.dataset.DatasetDao;
import org.fao.fenix.web.modules.core.client.view.vo.FenixGaulVo;
import org.fao.fenix.web.modules.core.client.view.vo.FpiGaulVo;
import org.fao.fenix.web.modules.core.client.view.vo.HSVo;
import org.fao.fenix.web.modules.fpi.client.control.GAULCodeSelectorConstants;
import org.fao.fenix.web.modules.fpi.client.control.services.FpiService;
import org.fao.fenix.web.modules.fpi.client.view.vo.EconomicsIndexVo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FpiServiceImpl extends RemoteServiceServlet implements FpiService {

    FenixGaulDao fenixGaulDao;

    HSDao hsDao;

    DatasetDao datasetDao;

    public List<EconomicsIndexVo> calculateLaspeyresIndex(String priceType, String commodityCode, String featureCode, double baseQuantity, int startYear, int endYear) {
        double basePrice = datasetDao.calculateCommodityBasePrice(priceType, commodityCode, featureCode, startYear, endYear);
        List<NumericCommodity> commodities = datasetDao.findAllNumericCommodityForIndex(priceType, commodityCode, featureCode);
        Logger.getRootLogger().warn("RETRIEVED " + commodities.size() + " COMMODITIES");
        List<EconomicsIndexVo> indexes = new ArrayList<EconomicsIndexVo>();
        for (int i = 0; i < commodities.size(); i++) {
            EconomicsIndexVo tmp = new EconomicsIndexVo();
            tmp.setBasePrice(basePrice);
            tmp.setBaseQuantity(baseQuantity);
            tmp.setDate(commodities.get(i).getDate());
            tmp.setCommodityCode(commodities.get(i).getCommodityCode());
            tmp.setGaulCode(commodities.get(i).getFeatureCode());
            tmp.setIndexType("Laspeyres");
            tmp.setPrice(commodities.get(i).getValue());
            tmp.setPriceType(priceType);
            tmp.setIndex(datasetDao.calculateLaspeyresIndex(baseQuantity, basePrice, commodities.get(i).getValue()));
            indexes.add(tmp);
        }
        return indexes;
    }

    public List<EconomicsIndexVo> calculateLaspeyresIndex(String priceType, String commodityCode, String featureCode, double baseQuantity, int startYear, int endYear, int subsetStart, int subsetEnd) {
        double basePrice = datasetDao.calculateCommodityBasePrice(priceType, commodityCode, featureCode, startYear, endYear);
        List<NumericCommodity> commodities = datasetDao.findAllNumericCommodityForIndex(priceType, commodityCode, featureCode, subsetStart, subsetEnd);
        Logger.getRootLogger().warn("RETRIEVED " + commodities.size() + " COMMODITIES");
        List<EconomicsIndexVo> indexes = new ArrayList<EconomicsIndexVo>();
        for (int i = 0; i < commodities.size(); i++) {
            EconomicsIndexVo tmp = new EconomicsIndexVo();
            tmp.setBasePrice(basePrice);
            tmp.setBaseQuantity(baseQuantity);
            tmp.setDate(commodities.get(i).getDate());
            tmp.setCommodityCode(commodities.get(i).getCommodityCode());
            tmp.setGaulCode(commodities.get(i).getFeatureCode());
            tmp.setIndexType("Laspeyres");
            tmp.setPrice(commodities.get(i).getValue());
            tmp.setPriceType(priceType);
            tmp.setIndex(datasetDao.calculateLaspeyresIndex(baseQuantity, basePrice, commodities.get(i).getValue()));
            indexes.add(tmp);
        }
        return indexes;
    }

    public List<FenixGaulVo> getContinents() {
        List<FenixGaul0> list = fenixGaulDao.findAllGaul0();
        List<String> continents = new ArrayList<String>();
        List<FenixGaulVo> result = new ArrayList<FenixGaulVo>();
        for (int i = 0; i < list.size(); i++) if (!continents.contains(list.get(i).getContinent())) {
            continents.add(list.get(i).getContinent());
            result.add(mapFanixGaul0(list.get(i), GAULCodeSelectorConstants.CONTINENT));
        }
        return result;
    }

    public List<FenixGaulVo> getRegionsByContinentName(String continent) {
        List<FenixGaul0> list = fenixGaulDao.findRegionByContinentName(continent);
        List<FenixGaulVo> result = new ArrayList();
        for (int i = 0; i < list.size(); i++) result.add(mapFanixGaul0(list.get(i), GAULCodeSelectorConstants.REGION));
        return result;
    }

    public List<FenixGaulVo> getNationsByRegionName(String region) {
        List<FenixGaul0> list = fenixGaulDao.findNationByRegionName(region);
        List<FenixGaulVo> result = new ArrayList();
        for (int i = 0; i < list.size(); i++) result.add(mapFanixGaul0(list.get(i), GAULCodeSelectorConstants.NATION));
        return result;
    }

    public List<FenixGaulVo> getProvencesByNationCode(long gaul0Code) {
        List<FenixGaul1> list = fenixGaulDao.findProvenceByNationCode(gaul0Code);
        List<FenixGaulVo> result = new ArrayList();
        for (int i = 0; i < list.size(); i++) result.add(mapFanixGaul1(list.get(i), GAULCodeSelectorConstants.PROVENCE));
        return result;
    }

    public List<FenixGaulVo> getCitiesByProvenceCode(long gaul1Code) {
        List<FenixGaul2> list = fenixGaulDao.findCityByProvenceCode(gaul1Code);
        List<FenixGaulVo> result = new ArrayList();
        for (int i = 0; i < list.size(); i++) result.add(mapFanixGaul2(list.get(i), GAULCodeSelectorConstants.CITY));
        return result;
    }

    public FenixGaulVo mapFanixGaul0(FenixGaul0 gaul, String level) {
        FenixGaulVo vo = new FenixGaulVo();
        vo.setCode(gaul.getCode());
        vo.setContinent(gaul.getContinent());
        vo.setName(gaul.getName());
        vo.setRegion(gaul.getRegion());
        vo.setLevel(level);
        return vo;
    }

    public FenixGaulVo mapFanixGaul1(FenixGaul1 gaul, String level) {
        FenixGaulVo vo = new FenixGaulVo();
        vo.setCode(gaul.getCode());
        vo.setContinent(gaul.getContinent());
        vo.setName(gaul.getName());
        vo.setRegion(gaul.getRegion());
        vo.setLevel(level);
        return vo;
    }

    public FenixGaulVo mapFanixGaul2(FenixGaul2 gaul, String level) {
        FenixGaulVo vo = new FenixGaulVo();
        vo.setCode(gaul.getCode());
        vo.setContinent(gaul.getContinent());
        vo.setName(gaul.getName());
        vo.setRegion(gaul.getRegion());
        vo.setLevel(level);
        return vo;
    }

    public List<HSVo> getLevel0HSCode() {
        List<HS> list = hsDao.findAllLevel0();
        List<HSVo> result = new ArrayList<HSVo>();
        for (int i = 0; i < list.size(); i++) {
            HS hs = list.get(i);
            result.add(mapHS((HS) list.get(i)));
        }
        return result;
    }

    public List<HSVo> getLevel1HSCode(String codeLevel0) {
        List<HS> list = hsDao.findAllLevel1(codeLevel0);
        List<HSVo> result = new ArrayList<HSVo>();
        for (int i = 0; i < list.size(); i++) {
            HS hs = list.get(i);
            result.add(mapHS((HS) list.get(i)));
        }
        return result;
    }

    public List<HSVo> getLevel2HSCode(String codeLevel1) {
        List<HS> list = hsDao.findAllLevel2(codeLevel1);
        List<HSVo> result = new ArrayList<HSVo>();
        for (int i = 0; i < list.size(); i++) {
            HS hs = list.get(i);
            result.add(mapHS((HS) list.get(i)));
        }
        return result;
    }

    public HSVo mapHS(HS hs) {
        HSVo vo = new HSVo();
        vo.setCode(hs.getCode());
        vo.setDescription(hs.getDescription());
        vo.setId(hs.getId());
        return vo;
    }

    public List getAdm0ByRegionName(String name) {
        return new ArrayList();
    }

    public List getRegionByContinentName(String continent) {
        return new ArrayList();
    }

    public FpiGaulVo domainToVo(FenixGaulDao domain) {
        return new FpiGaulVo();
    }

    public void setFenixGaulDao(FenixGaulDao fenixGaulDao) {
        this.fenixGaulDao = fenixGaulDao;
    }

    public void setHsDao(HSDao hsDao) {
        this.hsDao = hsDao;
    }

    public void setDatasetDao(DatasetDao datasetDao) {
        this.datasetDao = datasetDao;
    }
}
