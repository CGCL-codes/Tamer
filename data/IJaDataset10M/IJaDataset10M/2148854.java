package com.techstar.framework.ui.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import com.techstar.framework.dao.IBaseJdbcDao;
import com.techstar.framework.dao.model.CommonQueryObj;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.framework.ui.dao.ISysUiGridDao;
import com.techstar.framework.ui.dto.SysUiGridDto;
import com.techstar.framework.ui.entity.GridInfoObj;
import com.techstar.framework.ui.entity.SysUiGrid;
import com.techstar.framework.ui.service.ISysUiGridService;
import com.techstar.framework.ui.utils.grid.GridXMLHelper;
import com.techstar.framework.ui.utils.grid.parseIndividuation;
import com.techstar.framework.ui.web.tag.engine.Column;
import com.techstar.framework.ui.web.tag.engine.ObjtoGridXmlHelper;
import com.techstar.framework.ui.web.tag.utils.GridConstant;
import com.techstar.framework.utils.BeanHelper;
import com.techstar.framework.utils.ConfigurationHelper;
import com.techstar.framework.utils.SequenceCreator;

/**
 * 业务对象服务接口实现类
 * 
 * 
 * @author majian
 * @date
 */
public class SysUiGridServiceImpl implements ISysUiGridService {

    private ISysUiGridDao sysUiGridDao;

    private IBaseJdbcDao baseJdbcDao;

    public SysUiGridServiceImpl() {
    }

    public void saveOrUpdateSysUiGrid(SysUiGridDto dto) {
        if (StringUtils.isEmpty(dto.getId())) {
            if (StringUtils.isEmpty(dto.getId())) {
                dto.setId(new SequenceCreator().getUID());
            }
        }
        SysUiGrid sysUiGrid = (SysUiGrid) BeanHelper.buildBean(SysUiGrid.class, dto);
        sysUiGridDao.saveOrUpdate(sysUiGrid);
    }

    public void deleteSysUiGrid(String sysUiGridId) {
        SysUiGrid sysUiGrid = new SysUiGrid();
        sysUiGrid.setId(sysUiGridId);
        sysUiGridDao.delete(sysUiGrid);
    }

    public SysUiGridDto loadSysUiGrid(String id) {
        SysUiGrid sysUiGrid = (SysUiGrid) sysUiGridDao.findByPk(id);
        SysUiGridDto dto = (SysUiGridDto) BeanHelper.buildBean(SysUiGridDto.class, sysUiGrid);
        return dto;
    }

    public QueryListObj listSysUiGrid() {
        QueryListObj obj = sysUiGridDao.getQueryList();
        if (obj.getElemList() != null) {
            List dtos = (List) BeanHelper.buildBeans(SysUiGridDto.class, obj.getElemList());
            obj.setElemList(dtos);
        }
        return obj;
    }

    public QueryListObj getQueryListByHql(String hql) {
        QueryListObj obj = sysUiGridDao.getQueryListByHql(hql);
        if (obj.getElemList() != null) {
            List dtos = (List) BeanHelper.buildBeans(SysUiGridDto.class, obj.getElemList());
            obj.setElemList(dtos);
        }
        return obj;
    }

    public QueryListObj getListByParam(String userId, String busName, String subName) {
        StringBuffer hql = new StringBuffer();
        hql.append(" from SysUiGrid ");
        hql.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(userId)) {
            hql.append(" and user_id ='" + userId + "' ");
        } else {
            hql.append(" and user_id is null ");
        }
        hql.append(" and bus_name='" + busName + "' ");
        hql.append(" and sub_name='" + subName + "'");
        QueryListObj queryObj = sysUiGridDao.getQueryListByHql(hql.toString());
        return queryObj;
    }

    public void updateCustomData(String userId, String busName, String subName, String customXml) {
        QueryListObj queryObj = this.getListByParam(userId, busName, subName);
        List listData = queryObj.getElemList();
        try {
            if (listData.size() > 0) {
                SysUiGrid grid = (SysUiGrid) listData.get(0);
                if (!StringUtils.isEmpty(customXml)) {
                    grid.setCustom_xml(customXml.getBytes("utf-8"));
                    sysUiGridDao.saveOrUpdate(grid);
                }
            } else {
                SysUiGridDto gridDto = new SysUiGridDto();
                gridDto.setUser_id(userId);
                gridDto.setBus_name(busName);
                gridDto.setSub_name(subName);
                if (!StringUtils.isEmpty(customXml)) {
                    gridDto.setCustom_xml(customXml.getBytes("utf-8"));
                    gridDto.setVersion(0);
                    this.saveOrUpdateSysUiGrid(gridDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAdvanceQueryData(String userId, String busName, String subName, String queryName, String type) {
        try {
            if (StringUtils.isNotEmpty(type)) {
                if (type.equalsIgnoreCase("public")) {
                    userId = "".trim();
                }
            }
            QueryListObj queryObj = this.getListByParam(userId, busName, subName);
            List listData = queryObj.getElemList();
            if (listData.size() > 0) {
                SysUiGrid grid = (SysUiGrid) listData.get(0);
                if (!StringUtils.isEmpty(queryName)) {
                    String advanceQuery = new String(grid.getAdvance_query(), "utf-8");
                    parseIndividuation parseIndiv = new parseIndividuation();
                    String newAdvanceQuery = parseIndiv.deleteElementByQuery(advanceQuery, queryName);
                    grid.setAdvance_query(newAdvanceQuery.getBytes("utf-8"));
                    sysUiGridDao.saveOrUpdate(grid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * 更新用户<高级查询>页面中个性化设置参数
	 * 
	 * @param userId
	 *            用户ID
	 * @param busName
	 *            业务名称
	 * @param subName
	 *            子业务名称
	 * 
	 * @param advanceQuery
	 *            高级查询XML数据
	 */
    public void updateAdvanceQueryData(String userId, String busName, String subName, String advanceQuery) {
        QueryListObj queryObj = this.getListByParam(userId, busName, subName);
        List listData = queryObj.getElemList();
        try {
            if (listData.size() > 0) {
                SysUiGrid grid = (SysUiGrid) listData.get(0);
                if (!StringUtils.isEmpty(advanceQuery)) {
                    grid.setAdvance_query(advanceQuery.getBytes("utf-8"));
                    sysUiGridDao.saveOrUpdate(grid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDefaultQuery(String userId, String busName, String subName, String queryName, String type) {
        parseIndividuation parse = new parseIndividuation();
        if (StringUtils.isNotEmpty(type)) {
            if ("public".equalsIgnoreCase(type)) {
                String advanceQuery = this.getAdvanceQueryXml("".trim(), busName, subName, "public");
                advanceQuery = parse.setDefaultQuery(advanceQuery, queryName);
                if (!StringUtils.isEmpty(advanceQuery)) {
                    this.updateAdvanceQueryData("".trim(), busName, subName, advanceQuery);
                }
                String advanceQuery2 = this.getAdvanceQueryXml(userId, busName, subName, "individual");
                advanceQuery2 = parse.setNullDefaultQuery(advanceQuery2);
                if (!StringUtils.isEmpty(advanceQuery2)) {
                    this.updateAdvanceQueryData(userId, busName, subName, advanceQuery2);
                }
            } else {
                String advanceQuery = this.getAdvanceQueryXml(userId, busName, subName, "individual");
                advanceQuery = parse.setDefaultQuery(advanceQuery, queryName);
                if (!StringUtils.isEmpty(advanceQuery)) {
                    this.updateAdvanceQueryData(userId, busName, subName, advanceQuery);
                }
                String advanceQuery2 = this.getAdvanceQueryXml("".trim(), busName, subName, "public");
                advanceQuery2 = parse.setNullDefaultQuery(advanceQuery2);
                if (!StringUtils.isEmpty(advanceQuery2)) {
                    this.updateAdvanceQueryData("".trim(), busName, subName, advanceQuery2);
                }
            }
        }
    }

    public void saveAdvanceQueryData(String userId, String busName, String subName, String advanceQuery, String name, String queryStr, String type, String queryType) {
        QueryListObj queryObj = null;
        QueryListObj publicObj = null;
        SysUiGridDto gridDto = new SysUiGridDto();
        if (StringUtils.isNotEmpty(queryType)) {
            if (queryType.equalsIgnoreCase("public")) {
                gridDto.setUser_id("".trim());
                queryObj = this.getListByParam("".trim(), busName, subName);
                publicObj = this.getListByParam(userId, busName, subName);
            } else {
                gridDto.setUser_id(userId);
                queryObj = this.getListByParam(userId, busName, subName);
                publicObj = this.getListByParam("".trim(), busName, subName);
            }
        }
        parseIndividuation parse = new parseIndividuation();
        try {
            List listData = queryObj.getElemList();
            List publicData = publicObj.getElemList();
            if ((listData.size() <= 0) && (publicData.size() <= 0)) {
                queryStr = parse.setDefalutSign(queryStr);
                System.out.println("##################### parse.setDefalutSign(queryStr) #####################");
            } else {
                String aQuery = "";
                if (listData.size() > 0) {
                    SysUiGrid grid = (SysUiGrid) listData.get(0);
                    aQuery += new String(grid.getAdvance_query(), "utf-8");
                }
                if (publicData.size() > 0) {
                    SysUiGrid grid = (SysUiGrid) publicData.get(0);
                    aQuery += new String(grid.getAdvance_query(), "utf-8");
                }
                if (StringUtils.isEmpty(aQuery.trim())) {
                    queryStr = parse.setDefalutSign(queryStr);
                    System.out.println("#####################TWO parse.setDefalutSign(queryStr) #####################");
                }
            }
            if (listData.size() > 0) {
                SysUiGrid grid = (SysUiGrid) listData.get(0);
                if (!StringUtils.isEmpty(queryStr)) {
                    String returnStr = parse.addOrCoverAdvanceQuery(advanceQuery, name, queryStr, type);
                    grid.setAdvance_query(returnStr.getBytes("utf-8"));
                    sysUiGridDao.saveOrUpdate(grid);
                }
            } else {
                if (!StringUtils.isEmpty(queryStr)) {
                    gridDto.setBus_name(busName);
                    gridDto.setSub_name(subName);
                    String returnStr = parse.addOrCoverAdvanceQuery(advanceQuery, name, queryStr, type);
                    gridDto.setAdvance_query(returnStr.getBytes("utf-8"));
                    this.saveOrUpdateSysUiGrid(gridDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * 得到高级查询Xml字串
	 * 
	 * @param userId
	 * @param busName
	 * @param subName
	 * @return
	 */
    public String getAdvanceQueryOnlyXml(String userId, String busName, String subName) {
        String queryStr = "";
        queryStr = new String(new byte[1]);
        try {
            QueryListObj queryObj = this.getListByParam(userId, busName, subName);
            List list = queryObj.getElemList();
            if (list.size() > 0) {
                byte[] advanceByte = ((SysUiGrid) list.get(0)).getAdvance_query();
                if (advanceByte != null) {
                    if (advanceByte.length > 0) {
                        String advanceQuery = new String(advanceByte);
                        advanceQuery = new String(advanceQuery.getBytes("gbk"), "utf-8");
                        if (!StringUtils.isEmpty(advanceQuery)) {
                            queryStr = advanceQuery;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryStr;
    }

    /**
	 * 得到高级查询Xml字串[可包含公用信息]
	 * 
	 * @param userId
	 * @param busName
	 * @param subName
	 * @return
	 */
    public String getAdvanceQueryXml(String userId, String busName, String subName, String type) {
        String queryStr = "";
        queryStr = new String(new byte[1]);
        String queryStr2 = "";
        queryStr2 = new String(new byte[1]);
        try {
            if (StringUtils.isNotEmpty(type)) {
                if (type.equalsIgnoreCase("individual") || type.equalsIgnoreCase("double") || type.equalsIgnoreCase("both")) {
                    QueryListObj queryObj = this.getListByParam(userId, busName, subName);
                    List list = queryObj.getElemList();
                    if (list.size() > 0) {
                        byte[] advanceByte = ((SysUiGrid) list.get(0)).getAdvance_query();
                        if (advanceByte != null) {
                            if (advanceByte.length > 0) {
                                String advanceQuery = new String(advanceByte, "utf-8");
                                if (!StringUtils.isEmpty(advanceQuery)) {
                                    queryStr = advanceQuery;
                                }
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(type)) {
                if (type.equalsIgnoreCase("public") || type.equalsIgnoreCase("double") || type.equalsIgnoreCase("both")) {
                    QueryListObj queryObj2 = this.getListByParam("".trim(), busName, subName);
                    List list2 = queryObj2.getElemList();
                    if (list2.size() > 0) {
                        byte[] advanceByte = ((SysUiGrid) list2.get(0)).getAdvance_query();
                        if (advanceByte != null) {
                            if (advanceByte.length > 0) {
                                String advanceQuery = new String(advanceByte, "utf-8");
                                if (!StringUtils.isEmpty(advanceQuery)) {
                                    queryStr2 = advanceQuery;
                                }
                            }
                        }
                    }
                }
            }
            parseIndividuation parse = new parseIndividuation();
            queryStr = parse.uniteAdvanceQueryXml(queryStr.trim(), queryStr2.trim(), type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryStr;
    }

    /**
	 * 获取查询参数
	 * 
	 * @param queryList
	 * @param queryStr
	 * @return
	 */
    private String getQueryParams(QueryListObj queryList, QueryListObj publicList, String queryStr) {
        String queryParam = "";
        try {
            List list = queryList.getElemList();
            List pList = publicList.getElemList();
            if (queryStr == null || queryStr.equalsIgnoreCase("")) {
                parseIndividuation parseIndiv = new parseIndividuation();
                String advanceQuery = new String(((SysUiGrid) list.get(0)).getAdvance_query(), "utf-8");
                String pAdvanceQuery = new String(((SysUiGrid) pList.get(0)).getAdvance_query(), "utf-8");
                advanceQuery = parseIndiv.uniteAdvanceQueryXml(advanceQuery, pAdvanceQuery, "double");
                if (!StringUtils.isEmpty(advanceQuery)) {
                    queryParam = parseIndiv.returnDefaultQueryValue(advanceQuery);
                } else {
                    queryParam = queryStr;
                }
            } else {
                queryParam = queryStr;
            }
        } catch (Exception ex) {
        }
        return queryParam;
    }

    /**
	 * 获取page信息
	 */
    private String getPageInfo(QueryListObj queryList, String pageStr) {
        String pageInfo = "";
        try {
            List list = queryList.getElemList();
            if (list.size() > 0) {
                parseIndividuation parseIndiv = new parseIndividuation();
                String customXml = new String(((SysUiGrid) list.get(0)).getCustom_xml(), "utf-8");
                String pageSelect = parseIndiv.returnQueryStr(customXml, "pageSelect");
                if (!StringUtils.isEmpty(pageSelect)) {
                    String[] arra = pageStr.split("\\^");
                    pageInfo = arra[0] + "^" + pageSelect;
                } else {
                    pageInfo = pageStr;
                }
            } else {
                pageInfo = pageStr;
            }
        } catch (Exception ex) {
        }
        return pageInfo;
    }

    /**
	 * 用户GRID的个性化信息
	 * 
	 * @param queryList
	 *            SysUiGrids
	 * @param gridParam
	 * @param busName
	 * @param subName
	 * @return
	 */
    private Map getPersonalInfoOfGrid(QueryListObj queryList, Map gridParam, String busName, String subName) {
        Map params = new LinkedHashMap();
        try {
            List list = queryList.getElemList();
            String xmlHeadFile = getXmlHeadFile(busName);
            ObjtoGridXmlHelper xmlObjHelper = new ObjtoGridXmlHelper(new ArrayList(), 1, 1, 1, 1);
            List columnList = new ArrayList();
            String sequenceParam = "";
            String visableParam = "";
            String widthParam = "";
            String foldMode = "";
            String multipMode = "";
            String changeColor = "";
            String selectMode = "";
            String lockColumn = "";
            String pageSelect = "";
            parseIndividuation parseIndiv = new parseIndividuation();
            if (list.size() > 0) {
                String customXml = new String(((SysUiGrid) list.get(0)).getCustom_xml());
                sequenceParam = parseIndiv.returnQueryStr(customXml, "sequence");
                visableParam = parseIndiv.returnQueryStr(customXml, "visable");
                widthParam = parseIndiv.returnQueryStr(customXml, "width");
                foldMode = parseIndiv.returnQueryStr(customXml, "foldMode");
                multipMode = parseIndiv.returnQueryStr(customXml, "multipMode");
                changeColor = parseIndiv.returnQueryStr(customXml, "changeColor");
                selectMode = parseIndiv.returnQueryStr(customXml, "selectMode");
                lockColumn = parseIndiv.returnQueryStr(customXml, "lockColumn");
                pageSelect = parseIndiv.returnQueryStr(customXml, "pageSelect");
            }
            Object[] keyArra = (Object[]) (gridParam.keySet().toArray());
            Object[] valueArra = (Object[]) (gridParam.values().toArray());
            if (!StringUtils.isEmpty(sequenceParam) && !StringUtils.isEmpty(visableParam) && !StringUtils.isEmpty(widthParam)) {
                String xmlString = parseIndiv.parseIndividuation(xmlHeadFile, sequenceParam, visableParam, widthParam);
                StringWriter outputWriter = new StringWriter();
                outputWriter.write(xmlString);
                InputStream xmlStream = new ByteArrayInputStream(outputWriter.toString().getBytes("utf-8"));
                xmlObjHelper.xmlParser(xmlStream);
                columnList = xmlObjHelper.getColumnList();
                for (int x = 0; x < gridParam.size(); x++) {
                    String key = (String) keyArra[x];
                    String value = (String) valueArra[x];
                    value = parseIndiv.parseParam(xmlHeadFile, sequenceParam, value);
                    params.put(key, value);
                }
            } else {
                for (int x = 0; x < gridParam.size(); x++) {
                    String key = (String) keyArra[x];
                    String value = (String) valueArra[x];
                    params.put(key, value);
                }
                xmlObjHelper.xmlParser(new File(xmlHeadFile));
                columnList = xmlObjHelper.getColumnList();
                System.out.println("columnList2:" + columnList.size());
            }
            String colName = "";
            String colType = "";
            String dataTypeLen = "";
            String isPrimary = "";
            String visible = "";
            String width = "";
            String isEdit = "";
            String textColor = "";
            String className = "";
            String refName = "";
            String displayName = "";
            String refInfo = "";
            for (int x = 0; x < columnList.size(); x++) {
                String coName = ((Column) columnList.get(x)).getColName();
                String cName = ((Column) columnList.get(x)).getClassName();
                String rName = ((Column) columnList.get(x)).getRefName();
                if (x == 0) {
                    colName = ((Column) columnList.get(x)).getColName();
                    colType = ((Column) columnList.get(x)).getColType();
                    dataTypeLen = ((Column) columnList.get(x)).getDataTypeLen();
                    isPrimary = ((Column) columnList.get(x)).getIsPrimary();
                    visible = ((Column) columnList.get(x)).getVisible();
                    width = ((Column) columnList.get(x)).getWidth();
                    isEdit = ((Column) columnList.get(x)).getIsEdit();
                    textColor = ((Column) columnList.get(x)).getTextColor();
                    className = ((Column) columnList.get(x)).getClassName();
                    refName = ((Column) columnList.get(x)).getRefName();
                    if (coName.indexOf("opButton_") == -1) {
                        displayName += ((Column) columnList.get(x)).getViewName();
                    } else {
                        displayName += "" + gridParam.get("titleRownoImg");
                    }
                } else {
                    colName += "," + ((Column) columnList.get(x)).getColName();
                    colType += "," + ((Column) columnList.get(x)).getColType();
                    dataTypeLen += "," + ((Column) columnList.get(x)).getDataTypeLen();
                    isPrimary += "," + ((Column) columnList.get(x)).getIsPrimary();
                    visible += "," + ((Column) columnList.get(x)).getVisible();
                    width += "," + ((Column) columnList.get(x)).getWidth();
                    isEdit += "," + ((Column) columnList.get(x)).getIsEdit();
                    textColor += "," + ((Column) columnList.get(x)).getTextColor();
                    className += "," + ((Column) columnList.get(x)).getClassName();
                    refName += "," + ((Column) columnList.get(x)).getRefName();
                    if (coName.indexOf("opButton_") == -1) {
                        displayName += "," + ((Column) columnList.get(x)).getViewName();
                    } else {
                        displayName += "," + gridParam.get("titleRownoImg");
                    }
                }
                refInfo += coName + "^";
                if (StringUtils.isEmpty(cName)) {
                    refInfo += xmlObjHelper.getObjName() + "^";
                } else {
                    refInfo += cName + "^";
                }
                if (StringUtils.isEmpty(rName)) {
                    refInfo += coName + "";
                } else {
                    refInfo += rName + "";
                }
                if (x != columnList.size() - 1) {
                    refInfo += "@@@";
                }
            }
            params.put("colName", colName);
            params.put("colType", colType);
            params.put("dataTypeLen", dataTypeLen);
            params.put("isPrimary", isPrimary);
            params.put("visible", visible);
            params.put("width", width);
            params.put("isEdit", isEdit);
            params.put("textColor", textColor);
            params.put("displayName", displayName);
            params.put("className", className);
            params.put("refName", refName);
            params.put("refInfo", refInfo);
            params.put("foldMode", foldMode);
            params.put("multipMode", multipMode);
            params.put("changeColor", changeColor);
            params.put("selectMode", selectMode);
            params.put("lockColumn", lockColumn);
            params.put("pageSelect", pageSelect);
            params.put("sequenceParam", sequenceParam);
            params.put("visableParam", visableParam);
            params.put("widthParam", widthParam);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return params;
    }

    /**
	 * 根椐配置文件中colName和colType 得到查询条件中的SelectColNames参数 整理相关的otherparams参数
	 * 
	 * @param paramMap
	 * @param gridParam
	 * @return
	 */
    public Map setSelectColNames(Map paramMap, Map gridParam) {
        StringBuffer sbParam = new StringBuffer();
        StringBuffer isTotal = new StringBuffer();
        String colNames = (String) paramMap.get("KEY_COLNAMES");
        String oparams = (String) paramMap.get("KEY_OPARAMS");
        if (colNames != null && !colNames.equalsIgnoreCase("")) {
            String colName = (String) gridParam.get("colName");
            String colType = (String) gridParam.get("colType");
            if (colName != null && !colName.equalsIgnoreCase("") && colType != null && !colType.equalsIgnoreCase("")) {
                String[] colNameArra = colName.split(",");
                String[] colTypeArra = colType.split(",");
                for (int x = 0; x < colNameArra.length; x++) {
                    if (x == 0) {
                        sbParam.append(colNameArra[x] + "^" + colTypeArra[x]);
                    } else {
                        isTotal.append(",");
                        sbParam.append("," + colNameArra[x] + "^" + colTypeArra[x]);
                    }
                    String isTotalStr = (String) gridParam.get("isTotal");
                    String[] isTotalArra = isTotalStr.split(",");
                    int sign = 0;
                    for (int y = 0; y < isTotalArra.length; y++) {
                        if (colNameArra[x].equalsIgnoreCase(isTotalArra[y])) {
                            sign = 1;
                        }
                    }
                    if (sign == 1) {
                        isTotal.append(colNameArra[x] + "^1");
                    } else {
                        isTotal.append(colNameArra[x] + "^0");
                    }
                }
                paramMap.put("KEY_OPARAMS", oparams + "" + isTotal.toString());
            }
            paramMap.put("KEY_COLNAMES", sbParam.toString());
        }
        return paramMap;
    }

    /**
	 * 解析生成列表所需要的合计参数
	 * 
	 * @param params
	 *            Grid参数Map
	 * @param sumInfo
	 *            合计值List
	 * @return
	 */
    public Map setTotalParam(Map params, List sumInfo) {
        StringBuffer totalValue = new StringBuffer();
        StringBuffer attachFooter = new StringBuffer();
        String isTotal = (String) params.get("isTotal");
        if (!StringUtils.isEmpty(isTotal)) {
            if (isTotal.indexOf(",") != -1) {
                int totalNum = 0;
                String[] isTotalArra = isTotal.split(",");
                for (int x = 0; x < isTotalArra.length; x++) {
                    if (isTotalArra[x].indexOf("^") != -1) {
                        String cName = isTotalArra[x].split("\\^")[0];
                        String cValue = isTotalArra[x].split("\\^")[1];
                        if (cValue.equalsIgnoreCase("1")) {
                            String sumValue = "";
                            if (sumInfo != null && sumInfo.size() > 0) {
                                if (sumInfo.get(0) != null) {
                                    if (sumInfo.get(0) instanceof Object[]) {
                                        Object[] sumArra = (Object[]) sumInfo.get(0);
                                        sumValue = sumArra[totalNum].toString();
                                    } else {
                                        sumValue = sumInfo.get(0).toString();
                                    }
                                }
                            } else {
                                sumValue = "0";
                            }
                            if (x == 0) {
                                totalValue.append("" + sumValue);
                                attachFooter.append("<div id=colname>" + sumValue + "</div>");
                            } else {
                                totalValue.append("," + sumValue);
                                attachFooter.append(",<div id=colname>" + sumValue + "</div>");
                            }
                            totalNum++;
                        } else {
                            if (x == 0) {
                                totalValue.append("");
                                attachFooter.append("");
                            } else {
                                totalValue.append(",");
                                attachFooter.append(",");
                            }
                        }
                    }
                }
            }
        }
        params.put("totalValue", totalValue.toString());
        params.put("attachFooter", attachFooter.toString());
        return params;
    }

    /**
	 * 根椐配置文件中colType参数 得到查询条件中的colAlign,editType,sortType,displayFormat等参数
	 * 
	 * 
	 * @param paramsMap
	 * @return
	 */
    public Map setOtherGridParams(Map paramsMap) {
        StringBuffer colAlign = new StringBuffer();
        StringBuffer editType = new StringBuffer();
        StringBuffer sortType = new StringBuffer();
        StringBuffer displayFormat = new StringBuffer();
        StringBuffer filterRules = new StringBuffer();
        StringBuffer columnMove = new StringBuffer();
        StringBuffer isTotal = new StringBuffer();
        StringBuffer listBox = new StringBuffer();
        String colName = (String) paramsMap.get("colName");
        String colType = (String) paramsMap.get("colType");
        String columnMoveStr = (String) paramsMap.get("columnMove");
        String isTotalStr = (String) paramsMap.get("isTotal");
        String listBoxStr = (String) paramsMap.get("listBox");
        String colAlignStr = (String) paramsMap.get("colAlign");
        String editTypeStr = (String) paramsMap.get("editType");
        String sortTypeStr = (String) paramsMap.get("sortType");
        String displayFormatStr = (String) paramsMap.get("displayFormat");
        String filterRulesStr = (String) paramsMap.get("filterRules");
        String[] columnMoveArra = columnMoveStr.split(",");
        String[] isTotalArra = isTotalStr.split(",");
        String[] listBoxArra = listBoxStr.split(",");
        String[] colAlignArra = colAlignStr.split(",");
        String[] editTypeArra = editTypeStr.split(",");
        String[] sortTypeArra = sortTypeStr.split(",");
        String[] displayFormatArra = displayFormatStr.split(",");
        String[] filterRulesArra = filterRulesStr.split(",");
        if (!StringUtils.isEmpty(colType)) {
            String[] colTypeArra = colType.split(",");
            String[] colNameArra = colName.split(",");
            for (int x = 0; x < colTypeArra.length; x++) {
                if (x != 0) {
                    colAlign.append(",");
                    editType.append(",");
                    sortType.append(",");
                    displayFormat.append(",");
                    filterRules.append(",");
                    columnMove.append(",");
                    isTotal.append(",");
                    listBox.append(",");
                }
                StringBuffer colAlignS = new StringBuffer();
                StringBuffer editTypeS = new StringBuffer();
                StringBuffer sortTypeS = new StringBuffer();
                StringBuffer displayFormatS = new StringBuffer();
                StringBuffer filterRulesS = new StringBuffer();
                if (colNameArra[x].indexOf("opButton_") == -1) {
                    if (colTypeArra[x].equalsIgnoreCase("string")) {
                        colAlignS.append("left");
                        editTypeS.append("ed");
                        sortTypeS.append("str");
                        displayFormatS.append("");
                        filterRulesS.append("9");
                    } else if (colTypeArra[x].equalsIgnoreCase("blob")) {
                        colAlignS.append("left");
                        editTypeS.append("ed");
                        sortTypeS.append("str");
                        displayFormatS.append("");
                        filterRulesS.append("9");
                    } else if (colTypeArra[x].equalsIgnoreCase("float") || colTypeArra[x].equalsIgnoreCase("double")) {
                        colAlignS.append("right");
                        editTypeS.append("price");
                        sortTypeS.append("price");
                        displayFormatS.append("0,000");
                        filterRulesS.append("1");
                    } else if (colTypeArra[x].equalsIgnoreCase("timestamp") || colTypeArra[x].equalsIgnoreCase("date")) {
                        colAlignS.append("left");
                        editTypeS.append("ed");
                        sortTypeS.append("str");
                        if (colTypeArra[x].equalsIgnoreCase("timestamp")) {
                            displayFormatS.append("yyyy-mm-dd hh:ii:ss");
                        } else {
                            displayFormatS.append("yyyy-mm-dd");
                        }
                        filterRulesS.append("2");
                    } else if (colTypeArra[x].equalsIgnoreCase("boolean")) {
                        colAlignS.append("center");
                        editTypeS.append("ra");
                        sortTypeS.append("int");
                        displayFormatS.append("");
                        filterRulesS.append("0");
                    } else if (colTypeArra[x].equalsIgnoreCase("integer")) {
                        colAlignS.append("left");
                        editTypeS.append("ed");
                        sortTypeS.append("int");
                        displayFormatS.append("");
                        filterRulesS.append("1");
                    }
                } else {
                    colAlignS.append("center");
                    editTypeS.append("ro");
                    sortTypeS.append("na");
                    displayFormatS.append("");
                    filterRulesS.append("0");
                }
                int sign = 0;
                String tempColAlign = "";
                for (int y = 0; y < colAlignArra.length; y++) {
                    String[] innerArra = colAlignArra[y].split("#");
                    if (colNameArra[x].equalsIgnoreCase(innerArra[0])) {
                        sign = 1;
                        tempColAlign = innerArra[1];
                    }
                    if (colNameArra[x].indexOf("opButton_") != -1) {
                        sign = 1;
                        tempColAlign = "center";
                    }
                }
                if (sign == 1) {
                    colAlign.append(tempColAlign);
                } else {
                    colAlign.append(colAlignS.toString());
                }
                sign = 0;
                String tempEditType = "";
                for (int y = 0; y < editTypeArra.length; y++) {
                    String[] innerArra = editTypeArra[y].split("#");
                    if (colNameArra[x].equalsIgnoreCase(innerArra[0])) {
                        sign = 1;
                        tempEditType = innerArra[1];
                    }
                    if (colNameArra[x].indexOf("opButton_") != -1) {
                        sign = 1;
                        tempEditType = "ro";
                    }
                }
                if (sign == 1) {
                    editType.append(tempEditType);
                } else {
                    editType.append(editTypeS.toString());
                }
                sign = 0;
                String tempSortType = "";
                for (int y = 0; y < sortTypeArra.length; y++) {
                    String[] innerArra = sortTypeArra[y].split("#");
                    if (colNameArra[x].equalsIgnoreCase(innerArra[0])) {
                        sign = 1;
                        tempSortType = innerArra[1];
                    }
                    if (colNameArra[x].indexOf("opButton_") != -1) {
                        sign = 1;
                        tempSortType = "na";
                    }
                }
                if (sign == 1) {
                    sortType.append(tempSortType);
                } else {
                    sortType.append(sortTypeS.toString());
                }
                sign = 0;
                String tempDisplayFormat = "";
                for (int y = 0; y < displayFormatArra.length; y++) {
                    String[] innerArra = displayFormatArra[y].split("#");
                    if (colNameArra[x].equalsIgnoreCase(innerArra[0])) {
                        sign = 1;
                        tempDisplayFormat = innerArra[1];
                    }
                    if (colNameArra[x].indexOf("opButton_") != -1) {
                        sign = 1;
                        tempDisplayFormat = "";
                    }
                }
                if (sign == 1) {
                    displayFormat.append(tempDisplayFormat);
                } else {
                    displayFormat.append(displayFormatS.toString());
                }
                sign = 0;
                String tempFilterRules = "";
                for (int y = 0; y < filterRulesArra.length; y++) {
                    String[] innerArra = filterRulesArra[y].split("#");
                    if (colNameArra[x].equalsIgnoreCase(innerArra[0])) {
                        sign = 1;
                        tempFilterRules = innerArra[1];
                    }
                    if (colNameArra[x].indexOf("opButton_") != -1) {
                        sign = 1;
                        tempFilterRules = "0";
                    }
                }
                if (sign == 1) {
                    filterRules.append(tempFilterRules);
                } else {
                    filterRules.append(filterRulesS.toString());
                }
                sign = 0;
                for (int y = 0; y < columnMoveArra.length; y++) {
                    if (colNameArra[x].equalsIgnoreCase(columnMoveArra[y])) {
                        sign = 1;
                    }
                    if (colNameArra[x].indexOf("opButton_") != -1) {
                        sign = 1;
                    }
                }
                if (sign == 1) {
                    columnMove.append("false");
                } else {
                    columnMove.append("true");
                }
                sign = 0;
                for (int y = 0; y < isTotalArra.length; y++) {
                    if (colNameArra[x].equalsIgnoreCase(isTotalArra[y])) {
                        sign = 1;
                    }
                    if (colNameArra[x].indexOf("opButton_") != -1) {
                        sign = 0;
                    }
                }
                if (sign == 1) {
                    isTotal.append(colNameArra[x] + "^1");
                } else {
                    isTotal.append(colNameArra[x] + "^0");
                }
                sign = 0;
                String tempListBox = "";
                for (int y = 0; y < listBoxArra.length; y++) {
                    String[] innerArra = listBoxArra[y].split("#");
                    if (colNameArra[x].equalsIgnoreCase(innerArra[0])) {
                        sign = 1;
                        tempListBox = innerArra[1];
                    }
                    if ("opButton".equalsIgnoreCase(innerArra[0])) {
                        if (!StringUtils.isEmpty(innerArra[1])) {
                            String opListBox = innerArra[1].replace('!', '&');
                            paramsMap.put("opListBox", opListBox);
                        } else {
                            paramsMap.put("opListBox", "");
                        }
                    }
                    if (colNameArra[x].indexOf("opButton_") != -1) {
                        if (colNameArra[x].equalsIgnoreCase(innerArra[0])) {
                            sign = 1;
                            tempListBox = innerArra[1].replace('!', '&');
                            break;
                        } else {
                            sign = 1;
                            tempListBox = "";
                        }
                    }
                }
                if (sign == 1) {
                    listBox.append(tempListBox);
                } else {
                    listBox.append("");
                }
            }
            paramsMap.put("colAlign", colAlign.toString());
            paramsMap.put("editType", editType.toString());
            paramsMap.put("sortType", sortType.toString());
            paramsMap.put("displayFormat", displayFormat.toString());
            paramsMap.put("filterRules", filterRules.toString());
            paramsMap.put("columnMove", columnMove.toString());
            paramsMap.put("isTotal", isTotal.toString());
            paramsMap.put("listBox", listBox.toString());
        }
        return paramsMap;
    }

    /**
	 * 获取GRID的信息
	 * 
	 */
    public GridInfoObj getGridInfoObj(Map gridParam, Map paramMap, String hqlStr, String userId) {
        String queryStr = (String) gridParam.get("wheres");
        String pageStr = (String) gridParam.get("pageStr");
        String busName = (String) gridParam.get("busName");
        String subName = (String) gridParam.get("subName");
        GridInfoObj gridInfo = new GridInfoObj();
        try {
            QueryListObj queryList = this.getListByParam(userId, busName, subName);
            QueryListObj publicList = this.getListByParam("".trim(), busName, subName);
            String queryParam = this.getQueryParams(queryList, publicList, queryStr);
            String pageInfo = this.getPageInfo(queryList, pageStr);
            paramMap.put("KEY_WHERE", queryParam);
            paramMap.put("KEY_PAGE", pageInfo);
            Map gridPersonalInfo = this.getPersonalInfoOfGrid(queryList, gridParam, busName, subName);
            gridPersonalInfo.put("userSign", userId);
            System.out.println("refInfo:::::" + gridPersonalInfo.get("refInfo"));
            paramMap.put("KEY_REFINFO", gridPersonalInfo.get("refInfo"));
            paramMap = this.setSelectColNames(paramMap, gridPersonalInfo);
            gridPersonalInfo.put("otherparams", paramMap.get("KEY_OPARAMS"));
            gridPersonalInfo.put("selectColName", paramMap.get("KEY_COLNAMES"));
            String hqlSql = "";
            int beginPage = Integer.parseInt(pageInfo.split("\\^")[0]);
            int pageSize = Integer.parseInt(pageInfo.split("\\^")[1]);
            String sumSql = "";
            if (!StringUtils.isEmpty((String) paramMap.get("KEY_DOMAINNAME"))) {
                GridXMLHelper gridHelper = new GridXMLHelper();
                CommonQueryObj queryObj = gridHelper.parseQueryCondition(paramMap, hqlStr);
                hqlSql = gridHelper.genQueryHql(queryObj);
                beginPage = gridHelper.getBeginPage(queryObj);
                pageSize = gridHelper.getPageSize(queryObj);
                sumSql = gridHelper.genCountSQLByParams(queryObj);
            }
            gridInfo.setHqlSql(hqlSql);
            gridInfo.setBeginPage(beginPage);
            gridInfo.setPageSize(pageSize);
            gridInfo.setSumSql(sumSql);
            gridInfo.setGridPersonalInfo(gridPersonalInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return gridInfo;
    }

    /**
	 * 获取基本GRID查询操作信息的信息
	 * 
	 */
    public GridInfoObj getGenGridInfoObj(Map paramMap, String hqlStr) {
        GridInfoObj gridInfo = new GridInfoObj();
        String hqlSql = "";
        int beginPage = 0;
        int pageSize = 0;
        String sumSql = "";
        if (!StringUtils.isEmpty((String) paramMap.get("KEY_DOMAINNAME"))) {
            GridXMLHelper gridHelper = new GridXMLHelper();
            CommonQueryObj queryObj = gridHelper.parseQueryCondition(paramMap, hqlStr);
            hqlSql = gridHelper.genQueryHql(queryObj);
            beginPage = gridHelper.getBeginPage(queryObj);
            pageSize = gridHelper.getPageSize(queryObj);
            sumSql = gridHelper.genCountSQLByParams(queryObj);
        }
        gridInfo.setHqlSql(hqlSql);
        gridInfo.setBeginPage(beginPage);
        gridInfo.setPageSize(pageSize);
        gridInfo.setSumSql(sumSql);
        gridInfo.setGridPersonalInfo(null);
        return gridInfo;
    }

    /**
	 * 获取GRID的头信息文件路径
	 * 
	 * @param dtoName
	 * @return
	 */
    private String getXmlHeadFile(String dtoName) {
        File[] files = ConfigurationHelper.getGridDisFile();
        String classPath = "";
        if (files.length > 0) {
            String path = files[0].getParent();
            classPath += path + "/" + dtoName + ".xml";
        }
        return classPath;
    }

    /**
	 * 形成XML的数据信息及个性化信息
	 * 
	 * @param dtoName
	 * @param params
	 * @param dtoList
	 * @param beginPage
	 * @param pageSize
	 * @param isFilter
	 * @param isHanldCol
	 * @return
	 */
    public String parseParamToGridStr(Map params, List dtoList, int beginPage, int pageSize, int totalCount, List sumInfo) {
        String dtoName = (String) params.get("domainName");
        String busName = (String) params.get("busName");
        int isFilter = Integer.valueOf((String) params.get("isFilter")).intValue();
        int isOperater = Integer.valueOf((String) params.get("isOperater")).intValue();
        String sequenceParam = (String) params.get("sequenceParam");
        String visableParam = (String) params.get("visableParam");
        String widthParam = (String) params.get("widthParam");
        String xmlHead = this.getXmlHeadFile(busName);
        String xmlStr = "";
        ObjtoGridXmlHelper xmlObjHelper = new ObjtoGridXmlHelper(dtoList, beginPage, pageSize, isFilter, isOperater);
        parseIndividuation parseIndiv = new parseIndividuation();
        try {
            if (!StringUtils.isEmpty(sequenceParam) && !StringUtils.isEmpty(visableParam) && !StringUtils.isEmpty(widthParam)) {
                String xmlString = parseIndiv.parseIndividuation(xmlHead, sequenceParam, visableParam, widthParam);
                StringWriter outputWriter = new StringWriter();
                outputWriter.write(xmlString);
                InputStream xmlStream = new ByteArrayInputStream(outputWriter.toString().getBytes("utf-8"));
                xmlObjHelper.xmlParser(xmlStream);
            } else {
                xmlObjHelper.xmlParser(new File(xmlHead));
            }
            String objName = xmlObjHelper.getObjName();
            String xmlData = xmlObjHelper.getXmlData();
            params = this.setOtherGridParams(params);
            params = this.setTotalParam(params, sumInfo);
            params.put("currPage", beginPage + "");
            params.put("totalCount", totalCount + "");
            xmlStr = this.returnGridXML(objName, xmlData, params);
        } catch (Exception e) {
            System.out.println("<===========praseParamToGridStr===========>");
            e.printStackTrace();
            System.out.println("<===========praseParamToGridStr===========>");
        }
        return xmlStr;
    }

    /**
	 * 
	 * @param objName
	 * @param xmlData
	 * @param params
	 * @return
	 */
    public String returnGridXML(String objName, String xmlData, Map params) {
        Object[] keyArra = (Object[]) (params.keySet().toArray());
        Object[] valueArra = (Object[]) (params.values().toArray());
        StringBuffer gridXml = new StringBuffer();
        gridXml.append(objName);
        gridXml.append("#####");
        for (int i = 0; i < params.size(); i++) {
            if (i == 0) {
                gridXml.append(keyArra[i] + "==" + valueArra[i]);
            } else {
                gridXml.append("@@@@@" + keyArra[i] + "==" + valueArra[i]);
            }
        }
        gridXml.append("#####");
        gridXml.append(xmlData);
        System.out.println(xmlData);
        return gridXml.toString();
    }

    public QueryListObj getQueryListByHql(String hql, int beginPage, int pageSize) {
        return sysUiGridDao.getQueryListByHql(hql, beginPage, pageSize);
    }

    public List getObjPropertySums(String sql) {
        return this.baseJdbcDao.queryForListBySql(sql);
    }

    public void setSysUiGridDao(ISysUiGridDao sysUiGridDao) {
        this.sysUiGridDao = sysUiGridDao;
    }

    public void setBaseJdbcDao(IBaseJdbcDao baseJdbcDao) {
        this.baseJdbcDao = baseJdbcDao;
    }
}
