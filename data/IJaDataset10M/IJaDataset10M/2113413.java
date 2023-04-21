package neo.app.service;

import java.util.List;
import java.util.Map;
import neo.core.common.PagingList;
import neo.core.util.MapUtil;

public class KnowledgeService extends BaseService {

    private static final String SQL_GET_KNOWLEDGE_CATEGORY = "select kd.*,ifnull(count(k.ID),0) as ARTICLENUM from om_knowledge_division kd left outer join om_knowledge k on kd.ID=k.KNOWLEDGEDIVID group by kd.ID";

    /**
	 * 获取分类列表
	 * 
	 * @return
	 */
    public PagingList getAllCategorys() {
        return getPagingList(SQL_GET_KNOWLEDGE_CATEGORY);
    }

    private static final String SQL_ADD_KNOWLEDGE_CATEGORY = "insert into om_knowledge_division(NAME,GRADE,DESCRIPTION,POSTTIME,UPDATETIME,UPDATEIP) values(?,?,?,now(),now(),?)";

    /**
	 * 添加分类信息
	 * 
	 * @param parameterMap
	 * @param ip
	 */
    public void addCatrgory(Map parameterMap, String ip) {
        Object[] params = MapUtil.getObjectArrayFromMap(parameterMap, "name,grade,description");
        jt.update(SQL_ADD_KNOWLEDGE_CATEGORY, params[0], params[1], params[2], ip);
    }

    private static final String SQL_GET_KNOWLEDGE_CATEGORY_BY_ID = "select * from om_knowledge_division where ID=?";

    /**
	 * 获取分类信息供修改
	 * 
	 * @param id
	 * @return
	 */
    public Map getKnowledgeCategoryById(String id) {
        return jt.queryForMap(SQL_GET_KNOWLEDGE_CATEGORY_BY_ID, id);
    }

    private static final String SQL_UPDATE_KNOWLEDGE_DIVISION_BY_ID = "update om_knowledge_division set NAME=?,GRADE=?,DESCRIPTION=?,UPDATETIME=now(),UPDATEIP=? where ID=?";

    /**
	 * 修改知识分类信息
	 * 
	 * @param parameterMap
	 * @param ip
	 * @param id
	 */
    public void updateKnowledgeCategoryById(Map parameterMap, String ip, String id) {
        Object[] params = MapUtil.getObjectArrayFromMap(parameterMap, "name,grade,description");
        jt.update(SQL_UPDATE_KNOWLEDGE_DIVISION_BY_ID, params[0], params[1], params[2], ip, id);
    }

    private static final String SQL_GET_NUM_BY_CATEGORY_ID = "select count(ID) from om_knowledge where KNOWLEDGEDIVID=?";

    /**
	 * 根据分类ID获取信息条数
	 * 
	 * @param id
	 * @return
	 */
    public int getNumByCategoryId(String categoryId) {
        return jt.queryForInt(SQL_GET_NUM_BY_CATEGORY_ID, categoryId);
    }

    private static final String SQL_DELETE_CATEGORY_BY_ID = "delete from om_knowledge_division where ID=?";

    /**
	 * 根据ID删除分类
	 * 
	 * @param id
	 */
    public void deleteCategoryById(String id) {
        jt.update(SQL_DELETE_CATEGORY_BY_ID, id);
    }

    private static final String SQL_GET_ALL_KNOWLEDGES = "select k.*,kd.NAME as NAME,kd.GRADE as GRADE from om_knowledge k join om_knowledge_division kd on k.KNOWLEDGEDIVID=kd.ID order by k.ID desc";

    /**
	 * 获取信息列表
	 * 
	 * @return
	 */
    public PagingList getAllKnowledges() {
        return getPagingList(SQL_GET_ALL_KNOWLEDGES);
    }

    private static final String SQL_GET_ALL_CATEGORYS_FOR_SELECT = "select ID,NAME,GRADE from om_knowledge_division";

    /**
	 * 获取分类列表，不分页
	 * 
	 * @return
	 */
    public List getAllCategoryForSelect() {
        return jt.queryForList(SQL_GET_ALL_CATEGORYS_FOR_SELECT);
    }

    private static final String SQL_ADD_KNOWLEDGE = "insert into om_knowledge(KNOWLEDGEDIVID,TITLE,CONTENT,URL,POSTTIME,UPDATETIME,UPDATEIP) values(?,?,?,?,now(),now(),?)";

    /**
	 * 添加知识
	 * 
	 * @param parameterMap
	 * @param url
	 * @param ip
	 */
    public void addKnowledge(Map parameterMap, String url, String ip) {
        Object[] params = MapUtil.getObjectArrayFromMap(parameterMap, "categoryId,title,content");
        jt.update(SQL_ADD_KNOWLEDGE, params[0], params[1], params[2], url, ip);
    }

    private static final String SQL_GET_KNOWLEDGE_BY_ID = "select k.*,kd.NAME as NAME,kd.GRADE as GRADE from om_knowledge k join om_knowledge_division kd on k.KNOWLEDGEDIVID=kd.ID where k.ID=?";

    /**
	 * 根据ID获取知识点
	 * 
	 * @param id
	 * @return
	 */
    public Map getKnowledgeById(String id) {
        return jt.queryForMap(SQL_GET_KNOWLEDGE_BY_ID, id);
    }

    private static final String SQL_UPDATE_KNOWLEDGE_BY_ID = "update om_knowledge set KNOWLEDGEDIVID=?,TITLE=?,CONTENT=?,URL=?,UPDATETIME=now(),UPDATEIP=? where ID=?";

    /**
	 * 更新知识点信息
	 * 
	 * @param parameterMap
	 * @param url
	 * @param ip
	 * @param id
	 */
    public void updateKnowledgeById(Map parameterMap, String url, String ip, String id) {
        Object[] params = MapUtil.getObjectArrayFromMap(parameterMap, "categoryId,title,content");
        jt.update(SQL_UPDATE_KNOWLEDGE_BY_ID, params[0], params[1], params[2], url, ip, id);
    }

    private static final String SQL_DELETE_ATTACHMENT_BY_ID = "update om_knowledge set URL='' where ID=?";

    /**
	 * 删除附件
	 * 
	 * @param id
	 */
    public void deleteAttachmentById(String id) {
        jt.update(SQL_DELETE_ATTACHMENT_BY_ID, id);
    }

    private static final String SQL_DELETE_KNOWLEDGE_BY_ID = "delete from om_knowledge where ID=?";

    /**
	 * 删除知识点
	 * 
	 * @param id
	 */
    public void deleteKnowledgeById(String id) {
        jt.update(SQL_DELETE_KNOWLEDGE_BY_ID, id);
    }

    private static final String SQL_SEARCH_KNOWLEDGE_BY_CATEGORY_ID = "select k.*,kd.NAME as NAME,kd.GRADE as GRADE from om_knowledge k join om_knowledge_division kd on k.KNOWLEDGEDIVID=kd.ID where k.KNOWLEDGEDIVID=? order by k.ID desc";

    public PagingList getKnowledgeByCategoryId(String categoryId) {
        return getPagingList(SQL_SEARCH_KNOWLEDGE_BY_CATEGORY_ID, new Object[] { categoryId });
    }
}
