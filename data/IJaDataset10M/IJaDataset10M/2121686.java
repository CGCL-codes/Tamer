package cn.ziroom.webserive;

import java.util.List;
import javax.jws.WebMethod;
import cn.ziroom.mapper.SubwayStation;
import cn.ziroom.webserive.service.SubwayStationService;

/**
 * 地铁站点webservice接口
 * 
 * @author Administrator
 * 
 */
@javax.jws.WebService(serviceName = "SubwayStationWebService")
public class SubwayStationWebService {

    private SubwayStationService subwayStationService;

    /**
	 * 保存数据
	 * 
	 * @param list
	 * @return
	 */
    @WebMethod
    public String insert(List<SubwayStation> list) {
        try {
            subwayStationService.insert(list);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "错误";
        }
    }

    /**
	 * 更新
	 * 
	 * @return
	 * @throws Exception
	 */
    @WebMethod
    public String update(List<SubwayStation> list) throws Exception {
        try {
            subwayStationService.update(list);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "错误";
        }
    }

    /**
	 * 删除
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
    @WebMethod
    public String delete(List<String> list) throws Exception {
        try {
            subwayStationService.delete(list);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "错误";
        }
    }

    public void setSubwayStationService(SubwayStationService subwayStationService) {
        this.subwayStationService = subwayStationService;
    }
}
