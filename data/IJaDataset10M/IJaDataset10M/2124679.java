package kr.godsoft.egovframe.generator.tables.java;

import java.util.List;
import kr.godsoft.egovframe.generator.tables.service.TablesDefaultVO;
import kr.godsoft.egovframe.generator.tables.service.TablesService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public class TablesClient {

    private static Log log = LogFactory.getLog(TablesClient.class);

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        if (log.isInfoEnabled()) {
            log.info("시작");
        }
        String[] configLocations = { "egovframework/spring/context-aspect.xml", "egovframework/spring/context-common.xml", "egovframework/spring/context-datasource.xml", "egovframework/spring/context-idgen.xml", "egovframework/spring/context-properties.xml", "egovframework/spring/context-sqlMap.xml", "egovframework/spring/context-transaction.xml", "egovframework/spring/tables-beans.xml" };
        ApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
        TablesService tablesService = (TablesService) context.getBean("tablesService");
        try {
            TablesDefaultVO searchVO = new TablesDefaultVO();
            searchVO.setSearchCondition("1");
            searchVO.setSearchKeyword("egovfrm");
            searchVO.setFirstIndex(0);
            searchVO.setPageSize(1000);
            @SuppressWarnings("rawtypes") List tables = tablesService.selectTablesList(searchVO);
            for (int index = 0, size = tables.size(); index < size; index++) {
                EgovMap egovMap = (EgovMap) tables.get(index);
                if (log.isDebugEnabled()) {
                    log.debug("tableCatalog=" + egovMap.get("tableCatalog"));
                    log.debug("tableSchema=" + egovMap.get("tableSchema"));
                    log.debug("tableName=" + egovMap.get("tableName"));
                    log.debug("tableType=" + egovMap.get("tableType"));
                    log.debug("tableComment=" + egovMap.get("tableComment"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (log.isInfoEnabled()) {
            log.info("끝");
        }
    }
}
