package org.paradise.dms.services.impl;

import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.paradise.dms.dao.impl.DormitoryDAOImpl;
import org.paradise.dms.pojo.Dormitory;
import org.paradise.dms.pojo.DormitoryRate;
import org.paradise.dms.services.DormitoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dheaven.framework.dao.DaoException;

@Service
public class DormitoryServiceImpl implements DormitoryService {

    private static Logger log = Logger.getLogger(DormitoryServiceImpl.class);

    @Autowired
    private DormitoryDAOImpl dormitoryDAOImpl;

    public DormitoryDAOImpl getDormitoryDAOImpl() {
        return dormitoryDAOImpl;
    }

    public void setDormitoryDAOImpl(DormitoryDAOImpl dormitoryDAOImpl) {
        this.dormitoryDAOImpl = dormitoryDAOImpl;
    }

    public String deleteDormitoryByDormitoryID(String dormitoryid) {
        return dormitoryDAOImpl.deleteDormitorybyDormitoryID(dormitoryid);
    }

    public List<Dormitory> findDormitoryByName(String dormitoryname) {
        return null;
    }

    public String insertDormitory() {
        return null;
    }

    /**
	 * 查询所有的dormitory列表
	 */
    public List<Dormitory> listAllDormitoryByApartmentID(String apartmentid) {
        try {
            return dormitoryDAOImpl.listAllDormitoryByApartmentID(apartmentid);
        } catch (DaoException e) {
            log.error("宿舍列表为空啊！");
            return null;
        }
    }

    /**
	 * @throws DaoException
	 * 
	 */
    public int getRows(String apartmentid, String floorno) throws DaoException {
        return dormitoryDAOImpl.getRows(apartmentid, floorno);
    }

    /**
	 * @throws DaoException
	 * 
	 */
    public int getRows(String apartmentid) throws DaoException {
        return dormitoryDAOImpl.getRows(apartmentid);
    }

    /**
	 * 根据公寓id分页列出宿舍
	 */
    public List<Dormitory> listDormitoryByApartmentIDAndFloorNo(String apartmentid, String floorno, int pageSize, int startRow) {
        return dormitoryDAOImpl.listDormitoryByApartmentIDAndFloorNo(apartmentid, floorno, pageSize, startRow);
    }

    @SuppressWarnings("unchecked")
    public List listDormitoryByApartmentID(String sql) {
        return dormitoryDAOImpl.listDormitoryByApartmentID(sql);
    }

    @SuppressWarnings("unchecked")
    public List findDormitoryDisplayPictureSpec(String[] dormitoryid) throws DaoException, SQLException {
        return dormitoryDAOImpl.getDormitoryDisplayPictureSpec(dormitoryid);
    }

    public int getAFloorRows(String apartmentname, String dormitoryfloorno) throws DaoException {
        return dormitoryDAOImpl.getAFloorRows(apartmentname, dormitoryfloorno);
    }

    /**
	 * @throws DaoException
	 * 
	 */
    public List<Dormitory> findDormitoryByApartmentNameAndFloornoOrDormname(String apartmentid, String dormitoryfloorno, String dormitoryname, int pageSize, int startRow) throws DaoException {
        return dormitoryDAOImpl.getDormitoryByApartmentNameAndFloornoOrDormname(apartmentid, dormitoryfloorno, dormitoryname, pageSize, startRow);
    }

    @SuppressWarnings("unchecked")
    public List listAvailDormitoryByApartmentid(String apartmentid, String floorno, int studentdormitorytype, int pageSize, int startRow) {
        return dormitoryDAOImpl.getAvailDormitoryByApartmentid(apartmentid, floorno, studentdormitorytype, pageSize, startRow);
    }

    public int getTotalAvailDormRows(String apartmentid, String floorno, int studentdormitorytype) {
        return dormitoryDAOImpl.getTotalAvailDormRows(apartmentid, floorno, studentdormitorytype);
    }

    @SuppressWarnings("unchecked")
    public String updateDormitory(Dormitory dormitory) {
        try {
            dormitoryDAOImpl.merge(dormitory);
            return "更新宿舍信息成功！";
        } catch (DaoException e) {
            log.error(e);
        }
        return "更新宿舍信息失败！";
    }

    /**
	 * 
	 */
    public List<Dormitory> getDormitoryByDormitoryPreID(String dormitorypreallocationid, int pageSize, int startRow) {
        return dormitoryDAOImpl.getDormitoryPerPageByPreID(dormitorypreallocationid, pageSize, startRow);
    }

    public int getDormitoryByDormitoryPreIDRows(String dormitorypreallocationid) {
        return dormitoryDAOImpl.getDormitoryByDormitoryPreIDRows(dormitorypreallocationid);
    }

    public String updateBatchDormitoryPreassignInfo(String dormidList, String dormitoryallocationid) {
        return dormitoryDAOImpl.updateBatchDormitoryPreassignInfo(dormidList, dormitoryallocationid);
    }

    @SuppressWarnings("unchecked")
    public List getAvailBedNoByDormitoryID(String dormitoryid) {
        return dormitoryDAOImpl.getAvailBedByDormId(dormitoryid);
    }

    /**
	 * return -1 - 查询宿舍信息失败 0 - 系统用户未改变宿舍房型 10 - 宿舍已住人数大于新房型 11 - 删除床位成功 20 -
	 * 创建床位失败 21 - 创建床位成功
	 */
    public int updateDormitoryBed(String dormitoryid, int newroomtype) {
        Dormitory dormitory = null;
        try {
            dormitory = dormitoryDAOImpl.getDormByDormId(Integer.parseInt(dormitoryid)).get(0);
        } catch (NumberFormatException e) {
            return -1;
        } catch (DaoException e) {
            log.error("DMS_error:更改宿舍信息 - 查询宿舍信息失败");
            return -1;
        }
        int oldroomtype = Integer.parseInt(dormitory.getDormitorylodgetype());
        if (oldroomtype > newroomtype) {
            return dormitoryDAOImpl.deleteDormitoryBed(dormitoryid, oldroomtype - newroomtype);
        } else if (oldroomtype < newroomtype) {
            return dormitoryDAOImpl.insertDormitoryBed(dormitoryid, newroomtype - oldroomtype);
        }
        return 0;
    }

    public int getTotalAvailPreIdDormRows(String dormpreid) {
        return dormitoryDAOImpl.getTotalAvailPreIdDormRows(dormpreid);
    }

    @SuppressWarnings("unchecked")
    public List listAvailDromPerPageByPreID(String dormpreid, int pageSize, int startRow) {
        return dormitoryDAOImpl.listAvailDromPerPageByPreID(dormpreid, pageSize, startRow);
    }

    public List<Dormitory> listDormitoryByApartID(String apartmentid, int pageSize, int startRow) {
        return dormitoryDAOImpl.listDormitoryByApartID(apartmentid, pageSize, startRow);
    }

    public String updateAApartmentDormPreID(String apartmentid, String dormallocationid) {
        return dormitoryDAOImpl.updateAApartmentDormPreID(apartmentid, dormallocationid);
    }

    @SuppressWarnings("unchecked")
    public List getDormListForRatingExcelByApartId(String apartmentid) {
        return dormitoryDAOImpl.getDormListByApartId(apartmentid);
    }

    @SuppressWarnings("unchecked")
    public List getDormitoryRateByStudentno(String stuno, int studentdormitorytype) {
        return dormitoryDAOImpl.getDormitoryRateByStudentno(stuno, studentdormitorytype);
    }

    public void insertDormitoryRateFromEXCEL(DormitoryRate dr) {
        dormitoryDAOImpl.insertDormitoryRateFromEXCEL(dr);
    }

    public String[][] getDormitoryIDListByDormitoryPreID(String dormitorypreid) {
        return dormitoryDAOImpl.getDormitoryIDListByDormitoryPreID(dormitorypreid);
    }

    /**
	 * 
	 */
    public int getAvailDormInAmountByDormitoryPreID(String dormitorypreid) {
        return dormitoryDAOImpl.getAvailDormInAmountByDormitoryPreID(dormitorypreid);
    }

    public Dormitory getDormByDormId(int dormitoryid) {
        try {
            return dormitoryDAOImpl.getDormByDormId(dormitoryid).get(0);
        } catch (DaoException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[][] advancedDormSearch(String sql) {
        List list = dormitoryDAOImpl.advancedDormSearch(sql);
        if (list != null && list.size() > 0) {
            String[][] str = new String[list.size()][7];
            for (int i = 0; i < list.size(); i++) {
                Object[] o = (Object[]) list.get(i);
                str[i][0] = o[0] + "";
                str[i][1] = o[1] + "";
                str[i][2] = o[2] + "";
                str[i][3] = o[3] + "";
                str[i][4] = o[4] + "";
                str[i][5] = o[5] + "";
                str[i][6] = o[6] + "";
            }
            return str;
        } else {
            return null;
        }
    }

    public int getAdvancedDormSearchTotalNumber(String sql) {
        return dormitoryDAOImpl.getAdvancedDormSearchTotalNumber(sql);
    }

    public String updateDormInfo(String key, String value, String id) {
        String field = "dormitory" + key.substring(6);
        return dormitoryDAOImpl.updateDormInfo(field, value, id);
    }

    public String[][] getAllDormitoryConcreteFunction() {
        return dormitoryDAOImpl.getAllDormitoryConcreteFunction();
    }
}
