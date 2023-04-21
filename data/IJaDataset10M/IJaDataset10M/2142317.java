package kr.godsoft.egovframe.egovframegenerator;

import java.io.File;
import java.util.List;
import model.Attribute;
import model.DataModelContext;
import model.Query;
import operation.CrudCodeGen;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CrudCodeGenServiceImpl {

    private static final String ENCODING = "UTF-8";

    private static final Log log = LogFactory.getLog(CrudCodeGenApp.class);

    private static final String TAB = "            ";

    private CrudCodeGen crudCodeGen;

    private CrudCodeGenPath crudCodeGenPath;

    private StringBuilder sqlMap = new StringBuilder();

    public CrudCodeGenServiceImpl() {
        crudCodeGen = new CrudCodeGen();
    }

    public CrudCodeGenPath getCrudCodeGenPath() {
        return crudCodeGenPath;
    }

    public void setCrudCodeGenPath(CrudCodeGenPath crudCodeGenPath) {
        this.crudCodeGenPath = crudCodeGenPath;
    }

    public String getSqlMap() {
        return sqlMap.toString();
    }

    public void setSqlMap(DataModelContext dataModelContext) {
        crudCodeGenPath.setSqlMapResource(dataModelContext);
        this.sqlMap.append("    ");
        this.sqlMap.append("<sqlMap resource=\"");
        this.sqlMap.append(crudCodeGenPath.getSqlMapResource());
        this.sqlMap.append("\"/>");
        this.sqlMap.append("\n");
    }

    public void genDefaultVO(DataModelContext dataModelContext) throws Exception {
        String templateFile = "eGovFrameTemplates/crud/java/pkg/service/Sample2DefaultVO.vm";
        String data = crudCodeGen.generate(dataModelContext, templateFile);
        crudCodeGenPath.setDefaultVOPath(dataModelContext);
        if (log.isDebugEnabled()) {
            log.debug("defaultVOPath=" + crudCodeGenPath.getDefaultVOPath());
        }
        writeStringToFile(crudCodeGenPath.getDefaultVOPath(), data);
    }

    public void genVO(DataModelContext dataModelContext) throws Exception {
        String templateFile = "eGovFrameTemplates/crud/java/pkg/service/Sample2VO.vm";
        String data = crudCodeGen.generate(dataModelContext, templateFile);
        crudCodeGenPath.setVoPath(dataModelContext);
        if (log.isDebugEnabled()) {
            log.debug("voPath=" + crudCodeGenPath.getVoPath());
        }
        writeStringToFile(crudCodeGenPath.getVoPath(), data);
    }

    public void genSQLMap(DataModelContext dataModelContext) throws Exception {
        String insert = insert(dataModelContext);
        String update = update(dataModelContext);
        Query query = new Query();
        query.setInsert(insert);
        query.setUpdate(update);
        dataModelContext.setQuery(query);
        String templateFile = "eGovFrameTemplates/crud/resource/pkg/EgovSample_Sample2_SQL.vm";
        String data = crudCodeGen.generate(dataModelContext, templateFile);
        crudCodeGenPath.setSqlMapPath(dataModelContext);
        if (log.isDebugEnabled()) {
            log.debug("SqlMapPath=" + crudCodeGenPath.getSqlMapPath());
        }
        writeStringToFile(crudCodeGenPath.getSqlMapPath(), data);
    }

    private String insert(DataModelContext dataModelContext) {
        StringBuilder sb = new StringBuilder();
        List<Attribute> attributes = dataModelContext.getAttributes();
        sb.append(TAB);
        sb.append("INSERT INTO ");
        sb.append(dataModelContext.getEntity().getLcName());
        sb.append(" (\n");
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attribute = attributes.get(i);
            if (i == 0) {
                sb.append(TAB);
                sb.append("    ");
                sb.append(attribute.getLcName());
            } else {
                if ("last_updt_pnttm".equals(attribute.getLcName()) || "last_updusr_id".equals(attribute.getLcName())) {
                } else {
                    sb.append("\n");
                    sb.append(TAB);
                    sb.append("    , ");
                    sb.append(attribute.getLcName());
                }
            }
        }
        sb.append("\n");
        sb.append(TAB);
        sb.append(") VALUES (\n");
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attribute = attributes.get(i);
            if (i == 0) {
                sb.append(TAB);
                sb.append("    #");
                sb.append(attribute.getCcName());
                sb.append("#");
            } else {
                if ("last_updt_pnttm".equals(attribute.getLcName()) || "last_updusr_id".equals(attribute.getLcName())) {
                } else {
                    sb.append("\n");
                    sb.append(TAB);
                    sb.append("    , ");
                    if ("frst_regist_pnttm".equals(attribute.getLcName())) {
                        if ("MySql".equals(dataModelContext.getVender())) {
                            sb.append("SYSDATE()");
                        } else if ("Oracle".equals(dataModelContext.getVender())) {
                            sb.append("SYSDATE");
                        } else {
                            sb.append("SYSDATE()");
                        }
                    } else {
                        sb.append("#");
                        sb.append(attribute.getCcName());
                        sb.append("#");
                    }
                }
            }
        }
        sb.append("\n");
        sb.append(TAB);
        sb.append(")");
        return sb.toString();
    }

    private String update(DataModelContext dataModelContext) {
        StringBuilder sb = new StringBuilder();
        List<Attribute> attributes = dataModelContext.getAttributes();
        List<Attribute> primaryKeys = dataModelContext.getPrimaryKeys();
        sb.append(TAB);
        sb.append("UPDATE ");
        sb.append(dataModelContext.getEntity().getLcName());
        sb.append("\n");
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attribute = attributes.get(i);
            if (i == 0) {
                sb.append(TAB);
                sb.append("    SET\n");
                sb.append(TAB);
                sb.append("    ");
                sb.append(attribute.getLcName());
                sb.append(" = #");
                sb.append(attribute.getCcName());
                sb.append("#");
            } else {
                if ("frst_regist_pnttm".equals(attribute.getLcName()) || "frst_register_id".equals(attribute.getLcName())) {
                } else {
                    sb.append("\n");
                    sb.append(TAB);
                    sb.append("    , ");
                    sb.append(attribute.getLcName());
                    sb.append(" = ");
                    if ("last_updt_pnttm".equals(attribute.getLcName())) {
                        if ("MySql".equals(dataModelContext.getVender())) {
                            sb.append("SYSDATE()");
                        } else if ("Oracle".equals(dataModelContext.getVender())) {
                            sb.append("SYSDATE");
                        } else {
                            sb.append("SYSDATE()");
                        }
                    } else {
                        sb.append("#");
                        sb.append(attribute.getCcName());
                        sb.append("#");
                    }
                }
            }
        }
        sb.append("\n");
        sb.append(TAB);
        sb.append(where(dataModelContext));
        return sb.toString();
    }

    private String where(DataModelContext dataModelContext) {
        StringBuilder sb = new StringBuilder();
        List<Attribute> primaryKeys = dataModelContext.getPrimaryKeys();
        sb.append("    WHERE 1 = 1\n");
        for (int i = 0; i < primaryKeys.size(); i++) {
            Attribute attribute = primaryKeys.get(i);
            if (i == 0) {
            } else {
                sb.append("\n");
            }
            sb.append(TAB);
            sb.append("    AND ");
            sb.append(attribute.getLcName());
            sb.append(" = #");
            sb.append(attribute.getCcName());
            sb.append("#");
        }
        return sb.toString();
    }

    public void genDao(DataModelContext dataModelContext) throws Exception {
        String templateFile = "eGovFrameTemplates/crud/java/pkg/service/impl/Sample2DAO.vm";
        String data = crudCodeGen.generate(dataModelContext, templateFile);
        crudCodeGenPath.setDaoPath(dataModelContext);
        if (log.isDebugEnabled()) {
            log.debug("daoPath=" + crudCodeGenPath.getDaoPath());
        }
        writeStringToFile(crudCodeGenPath.getDaoPath(), data);
    }

    public void genService(DataModelContext dataModelContext) throws Exception {
        String templateFile = "eGovFrameTemplates/crud/java/pkg/service/EgovSample2Service.vm";
        String data = crudCodeGen.generate(dataModelContext, templateFile);
        crudCodeGenPath.setGenServicePath(dataModelContext);
        if (log.isDebugEnabled()) {
            log.debug("genServicePath=" + crudCodeGenPath.getGenServicePath());
        }
        writeStringToFile(crudCodeGenPath.getGenServicePath(), data);
    }

    public void genServiceImpl(DataModelContext dataModelContext) throws Exception {
        String templateFile = "eGovFrameTemplates/crud/java/pkg/service/impl/EgovSample2ServiceImpl.vm";
        String data = crudCodeGen.generate(dataModelContext, templateFile);
        crudCodeGenPath.setGenServiceImplPath(dataModelContext);
        if (log.isDebugEnabled()) {
            log.debug("genServiceImplPath=" + crudCodeGenPath.getGenServiceImplPath());
        }
        writeStringToFile(crudCodeGenPath.getGenServiceImplPath(), data);
    }

    public void genController(DataModelContext dataModelContext) throws Exception {
        String templateFile = "eGovFrameTemplates/crud/java/pkg/web/EgovSample2Controller.vm";
        String data = crudCodeGen.generate(dataModelContext, templateFile);
        crudCodeGenPath.setControllerPath(dataModelContext);
        if (log.isDebugEnabled()) {
            log.debug("controllerPath=" + crudCodeGenPath.getControllerPath());
        }
        writeStringToFile(crudCodeGenPath.getControllerPath(), data);
    }

    public void genList(DataModelContext dataModelContext) throws Exception {
        String templateFile = "eGovFrameTemplates/crud/jsp/pkg/egovSample2List.vm";
        String data = crudCodeGen.generate(dataModelContext, templateFile);
        crudCodeGenPath.setListPath(dataModelContext);
        if (log.isDebugEnabled()) {
            log.debug("listPath=" + crudCodeGenPath.getListPath());
        }
        writeStringToFile(crudCodeGenPath.getListPath(), data);
    }

    public void genRegister(DataModelContext dataModelContext) throws Exception {
        String templateFile = "eGovFrameTemplates/crud/jsp/pkg/egovSample2Register.vm";
        String data = crudCodeGen.generate(dataModelContext, templateFile);
        crudCodeGenPath.setRegisterPath(dataModelContext);
        if (log.isDebugEnabled()) {
            log.debug("registerPath=" + crudCodeGenPath.getRegisterPath());
        }
        writeStringToFile(crudCodeGenPath.getRegisterPath(), data);
    }

    public void genSqlMapConfig(String str) throws Exception {
        DataModelContext dataModelContext = new DataModelContext();
        dataModelContext.setStr(str);
        String templateFile = "eGovFrameTemplates/crud/resource/pkg/sqlMapConfig.vm";
        String data = crudCodeGen.generate(dataModelContext, templateFile);
        crudCodeGenPath.setProjectSqlMapConfigPath(crudCodeGenPath.getSqlMapConfigPath());
        if (log.isDebugEnabled()) {
            log.debug("sqlMapConfigPath=" + crudCodeGenPath.getSqlMapConfigPath());
        }
        writeStringToFile(crudCodeGenPath.getSqlMapConfigPath(), data);
    }

    private void writeStringToFile(String pathname, String data) throws Exception {
        File file = new File(pathname);
        FileUtils.writeStringToFile(file, data, ENCODING);
    }
}
