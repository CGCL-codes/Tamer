package iwant.dao;

import halo.dao.query.IDao;
import iwant.bean.Province;
import java.util.List;

public interface ProvinceDao extends IDao<Province> {

    /**
	 * 获得某个国家下的省份数据,按照自定义排序
	 * 
	 * @param countryid
	 * @return
	 */
    List<Province> getListByCountryidOrderByOrder_flg(int countryid);

    boolean isExistByCountryidAndName(int countryid, String name);

    boolean isExistByCountryidAndNameAndNotProvinceid(int countryid, String name, int provinceid);
}
