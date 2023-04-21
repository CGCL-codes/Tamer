package com.bones.query.sysexpert_new.dao.jdbc;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import com.bones.core.dao.BaseDaoJdbc;
import com.bones.core.utils.DaoUtils;
import com.bones.core.utils.JdbcDaoUtils;
import com.bones.core.utils.exception.DaoAccessException;
import com.bones.core.web.tags.DefaultPagination;
import com.bones.core.web.tags.IPagination;
import com.bones.query.sysexpert_new.dao.ITmpResAttrHisDao;
import com.bones.query.sysexpert_new.dao.entity.TmpResAttrHis;

/** DAO层Jdbc实现 */
public class TmpResAttrHisDaoJdbcImpl extends BaseDaoJdbc implements ITmpResAttrHisDao {

    /** 分页查询 TMP_RES_ATTR_HIS列表 */
    public IPagination queryTmpResAttrHisList(TmpResAttrHis tmpResAttrHis, int first, int max) throws DaoAccessException {
        StringBuffer querySql = new StringBuffer("");
        if (tmpResAttrHis.getId() != null) {
            querySql.append(" and t.id = '").append(tmpResAttrHis.getId()).append("' ");
        }
        if (tmpResAttrHis.getResId() != null) {
            querySql.append(" and t.res_id = '").append(tmpResAttrHis.getResId()).append("' ");
        }
        if (tmpResAttrHis.getResPath() != null && tmpResAttrHis.getResPath().length() > 0) {
            querySql.append(" and lower(trim(t.res_path)) like '%").append(tmpResAttrHis.getResPath().toLowerCase()).append("%' ");
        }
        if (tmpResAttrHis.getResModelId() != null) {
            querySql.append(" and t.res_model_id = '").append(tmpResAttrHis.getResModelId()).append("' ");
        }
        if (tmpResAttrHis.getAttrGroup() != null) {
            querySql.append(" and t.attr_group = '").append(tmpResAttrHis.getAttrGroup()).append("' ");
        }
        if (tmpResAttrHis.getAttrCode() != null && tmpResAttrHis.getAttrCode().length() > 0) {
            querySql.append(" and lower(trim(t.attr_code)) like '%").append(tmpResAttrHis.getAttrCode().toLowerCase()).append("%' ");
        }
        if (tmpResAttrHis.getCollectTimeStr() != null && tmpResAttrHis.getCollectTimeStr().length() > 0) {
            querySql.append(" and t.collect_time >= to_date('").append(tmpResAttrHis.getCollectTime()).append("', 'yyyy-MM-dd hh24:mi:ss') ");
        }
        if (tmpResAttrHis.getN1() != null) {
            querySql.append(" and t.n1 = '").append(tmpResAttrHis.getN1()).append("' ");
        }
        if (tmpResAttrHis.getN2() != null) {
            querySql.append(" and t.n2 = '").append(tmpResAttrHis.getN2()).append("' ");
        }
        if (tmpResAttrHis.getN3() != null) {
            querySql.append(" and t.n3 = '").append(tmpResAttrHis.getN3()).append("' ");
        }
        if (tmpResAttrHis.getN4() != null) {
            querySql.append(" and t.n4 = '").append(tmpResAttrHis.getN4()).append("' ");
        }
        if (tmpResAttrHis.getN5() != null) {
            querySql.append(" and t.n5 = '").append(tmpResAttrHis.getN5()).append("' ");
        }
        if (tmpResAttrHis.getN6() != null) {
            querySql.append(" and t.n6 = '").append(tmpResAttrHis.getN6()).append("' ");
        }
        if (tmpResAttrHis.getN7() != null) {
            querySql.append(" and t.n7 = '").append(tmpResAttrHis.getN7()).append("' ");
        }
        if (tmpResAttrHis.getN8() != null) {
            querySql.append(" and t.n8 = '").append(tmpResAttrHis.getN8()).append("' ");
        }
        if (tmpResAttrHis.getN9() != null) {
            querySql.append(" and t.n9 = '").append(tmpResAttrHis.getN9()).append("' ");
        }
        if (tmpResAttrHis.getN10() != null) {
            querySql.append(" and t.n10 = '").append(tmpResAttrHis.getN10()).append("' ");
        }
        String infoSql = " select * from tmp_res_attr_his t" + " where 1=1" + querySql.toString() + " order by id";
        StringBuffer countSql = new StringBuffer();
        countSql.append(" select count(*) from (");
        countSql.append(infoSql);
        countSql.append(" ) tcount ");
        Long count = JdbcDaoUtils.getCount(countSql.toString(), true);
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from (").append(infoSql).append(") tpagination limit ?, ?");
        int start = first == 0 ? 0 : first + max;
        List resultList = this.getJdbcTemplate().queryForList(sb.toString(), new Object[] { start, 12 });
        IPagination pagination = new DefaultPagination(first, max);
        pagination.setAllCount(count.intValue());
        if (resultList != null && resultList.size() > 0) {
            List<TmpResAttrHis> retList = new ArrayList<TmpResAttrHis>();
            Iterator it = resultList.iterator();
            while (it.hasNext()) {
                Map objMap = (Map) it.next();
                TmpResAttrHis vo = new TmpResAttrHis();
                if (objMap.get("id") != null) {
                    vo.setId(Integer.valueOf(String.valueOf(objMap.get("id"))));
                }
                if (objMap.get("res_id") != null) {
                    vo.setResId(Integer.valueOf(String.valueOf(objMap.get("res_id"))));
                }
                vo.setResPath(String.valueOf(objMap.get("res_path")));
                if (objMap.get("res_model_id") != null) {
                    vo.setResModelId(Integer.valueOf(String.valueOf(objMap.get("res_model_id"))));
                }
                if (objMap.get("attr_group") != null) {
                    vo.setAttrGroup(Integer.valueOf(String.valueOf(objMap.get("attr_group"))));
                }
                vo.setAttrCode(String.valueOf(objMap.get("attr_code")));
                if (objMap.get("collect_time") != null) {
                    vo.setCollectTime(Timestamp.valueOf(String.valueOf(objMap.get("collect_time"))));
                    String str = String.valueOf(objMap.get("collect_time"));
                    if (str.indexOf(" 00:00:00.0") != -1) {
                        str = str.substring(0, str.indexOf(" 00:00:00.0"));
                    }
                    if (str.substring(str.length() - 2, str.length()).equals(".0")) {
                        str = str.substring(0, str.length() - 2);
                    }
                    vo.setCollectTimeStr(str);
                }
                if (objMap.get("n1") != null) {
                    vo.setN1(Double.valueOf(String.valueOf(objMap.get("n1"))));
                }
                if (objMap.get("n2") != null) {
                    vo.setN2(Double.valueOf(String.valueOf(objMap.get("n2"))));
                }
                if (objMap.get("n3") != null) {
                    vo.setN3(Double.valueOf(String.valueOf(objMap.get("n3"))));
                }
                if (objMap.get("n4") != null) {
                    vo.setN4(Double.valueOf(String.valueOf(objMap.get("n4"))));
                }
                if (objMap.get("n5") != null) {
                    vo.setN5(Double.valueOf(String.valueOf(objMap.get("n5"))));
                }
                if (objMap.get("n6") != null) {
                    vo.setN6(Double.valueOf(String.valueOf(objMap.get("n6"))));
                }
                if (objMap.get("n7") != null) {
                    vo.setN7(Double.valueOf(String.valueOf(objMap.get("n7"))));
                }
                if (objMap.get("n8") != null) {
                    vo.setN8(Double.valueOf(String.valueOf(objMap.get("n8"))));
                }
                if (objMap.get("n9") != null) {
                    vo.setN9(Double.valueOf(String.valueOf(objMap.get("n9"))));
                }
                if (objMap.get("n10") != null) {
                    vo.setN10(Double.valueOf(String.valueOf(objMap.get("n10"))));
                }
                vo = (TmpResAttrHis) DaoUtils.cleanObject(vo);
                retList.add(vo);
            }
            pagination.setResults(retList);
        }
        return pagination;
    }

    /** 查询单条—— TMP_RES_ATTR_HIS详情 */
    public TmpResAttrHis viewTmpResAttrHis(TmpResAttrHis tmpResAttrHis) throws DaoAccessException {
        StringBuffer querySql = new StringBuffer();
        if (tmpResAttrHis.getId() != null) {
            querySql.append(" and t.id = '").append(tmpResAttrHis.getId()).append("' ");
        }
        if (tmpResAttrHis.getResId() != null) {
            querySql.append(" and t.res_id = '").append(tmpResAttrHis.getResId()).append("' ");
        }
        if (tmpResAttrHis.getResPath() != null) {
            querySql.append(" and trim(t.res_path) = '").append(tmpResAttrHis.getResPath()).append("' ");
        }
        if (tmpResAttrHis.getResModelId() != null) {
            querySql.append(" and t.res_model_id = '").append(tmpResAttrHis.getResModelId()).append("' ");
        }
        if (tmpResAttrHis.getAttrGroup() != null) {
            querySql.append(" and t.attr_group = '").append(tmpResAttrHis.getAttrGroup()).append("' ");
        }
        if (tmpResAttrHis.getAttrCode() != null) {
            querySql.append(" and trim(t.attr_code) = '").append(tmpResAttrHis.getAttrCode()).append("' ");
        }
        String sql = " select " + "		t.id," + "		t.res_id," + "		t.res_path," + "		t.res_model_id," + "		t.attr_group," + "		t.attr_code," + "		t.collect_time," + "		t.n1," + "		t.n2," + "		t.n3," + "		t.n4," + "		t.n5," + "		t.n6," + "		t.n7," + "		t.n8," + "		t.n9," + "		t.n10" + " from tmp_res_attr_his t\n" + " where 1=1\n" + querySql.toString() + " ";
        List list = getJdbcTemplate().query(sql, new RowMapper() {

            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                TmpResAttrHis vo = new TmpResAttrHis();
                if (rs.getObject("id") != null) {
                    vo.setId(Integer.valueOf(String.valueOf(rs.getObject("id"))));
                }
                if (rs.getObject("res_id") != null) {
                    vo.setResId(Integer.valueOf(String.valueOf(rs.getObject("res_id"))));
                }
                vo.setResPath(String.valueOf(rs.getObject("res_path")));
                if (rs.getObject("res_model_id") != null) {
                    vo.setResModelId(Integer.valueOf(String.valueOf(rs.getObject("res_model_id"))));
                }
                if (rs.getObject("attr_group") != null) {
                    vo.setAttrGroup(Integer.valueOf(String.valueOf(rs.getObject("attr_group"))));
                }
                vo.setAttrCode(String.valueOf(rs.getObject("attr_code")));
                if (rs.getObject("collect_time") != null) {
                    vo.setCollectTime(rs.getDate("collect_time"));
                    String str = String.valueOf(rs.getDate("collect_time"));
                    if (str.indexOf(" 00:00:00.0") != -1) {
                        str = str.substring(0, str.indexOf(" 00:00:00.0"));
                    }
                    if (str.substring(str.length() - 2, str.length()).equals(".0")) {
                        str = str.substring(0, str.length() - 2);
                    }
                    vo.setCollectTimeStr(str);
                }
                if (rs.getObject("n1") != null) {
                    vo.setN1(Double.valueOf(String.valueOf(rs.getObject("n1"))));
                }
                if (rs.getObject("n2") != null) {
                    vo.setN2(Double.valueOf(String.valueOf(rs.getObject("n2"))));
                }
                if (rs.getObject("n3") != null) {
                    vo.setN3(Double.valueOf(String.valueOf(rs.getObject("n3"))));
                }
                if (rs.getObject("n4") != null) {
                    vo.setN4(Double.valueOf(String.valueOf(rs.getObject("n4"))));
                }
                if (rs.getObject("n5") != null) {
                    vo.setN5(Double.valueOf(String.valueOf(rs.getObject("n5"))));
                }
                if (rs.getObject("n6") != null) {
                    vo.setN6(Double.valueOf(String.valueOf(rs.getObject("n6"))));
                }
                if (rs.getObject("n7") != null) {
                    vo.setN7(Double.valueOf(String.valueOf(rs.getObject("n7"))));
                }
                if (rs.getObject("n8") != null) {
                    vo.setN8(Double.valueOf(String.valueOf(rs.getObject("n8"))));
                }
                if (rs.getObject("n9") != null) {
                    vo.setN9(Double.valueOf(String.valueOf(rs.getObject("n9"))));
                }
                if (rs.getObject("n10") != null) {
                    vo.setN10(Double.valueOf(String.valueOf(rs.getObject("n10"))));
                }
                return DaoUtils.cleanObject(vo);
            }
        });
        if (null != list && list.size() > 0) {
            return (TmpResAttrHis) list.get(0);
        }
        return null;
    }

    /** 删除单条—— TMP_RES_ATTR_HIS记录 */
    public void delTmpResAttrHis(TmpResAttrHis tmpResAttrHis) throws DaoAccessException {
        StringBuffer querySql = new StringBuffer();
        if (tmpResAttrHis.getId() != null) {
            querySql.append(" and id = '").append(tmpResAttrHis.getId()).append("' ");
        }
        if (tmpResAttrHis.getResId() != null) {
            querySql.append(" and res_id = '").append(tmpResAttrHis.getResId()).append("' ");
        }
        if (tmpResAttrHis.getResPath() != null && tmpResAttrHis.getResPath().length() > 0) {
            querySql.append(" and trim(res_path) = '").append(tmpResAttrHis.getResPath()).append("' ");
        }
        if (tmpResAttrHis.getResModelId() != null) {
            querySql.append(" and res_model_id = '").append(tmpResAttrHis.getResModelId()).append("' ");
        }
        if (tmpResAttrHis.getAttrGroup() != null) {
            querySql.append(" and attr_group = '").append(tmpResAttrHis.getAttrGroup()).append("' ");
        }
        if (tmpResAttrHis.getAttrCode() != null && tmpResAttrHis.getAttrCode().length() > 0) {
            querySql.append(" and trim(attr_code) = '").append(tmpResAttrHis.getAttrCode()).append("' ");
        }
        String sql = " delete " + " from tmp_res_attr_his \n" + " where 1=1 \n" + querySql.toString() + " ";
        getJdbcTemplate().execute(sql);
    }
}
