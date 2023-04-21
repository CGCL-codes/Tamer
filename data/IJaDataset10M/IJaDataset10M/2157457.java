package cn.jsprun.dao;

import java.util.List;
import cn.jsprun.domain.Magicmarket;

public interface MagicMarketDao {

    public List<Magicmarket> getAllMagicFromMarket(int firstNmu, int maxNum);

    public List<Magicmarket> getMagicFromMarketByUid(Integer uid, int firstNmu, int maxNum);

    public List<Magicmarket> getMagicFormMarketByMagicId(Short magicId, String orderby, String ascDesc, int firstNmu, int maxNum);

    public Magicmarket getMagicmarketById(Short magicmarketId);
}
