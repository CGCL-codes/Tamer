package com.nccsjz.back.section.utils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import utils.ConfigUtils;
import utils.FileUtils;
import com.nccsjz.back.section.dao.SectionDAO;
import com.nccsjz.base.BaseService;
import com.nccsjz.pojo.Section;
import com.nccsjz.utils.CardException;

public class JsFileService extends BaseService {

    /**
	 * initSections方法用于初始化栏目信息，启用应用(CardWebListener)调用
	 * 
	 * @param levelOnePath
	 *            一级栏目上下文路径
	 * @param levelOneRealPath
	 *            一级栏目的物理路径
	 * @param levelOneJsRealPath
	 *            一级栏目JS文件物理路径
	 * @param levelOneJsFileName
	 *            一级栏目JS文件名
	 * @param levelTwoJsFileName
	 *            二级栏目JS文件名
	 * @param templateRealPathFileName
	 *            模板html文件的真实路径
	 * @throws CardException
	 * @throws SQLException
	 * @throws IOException
	 */
    public void initSections(String levelOneRealPath, String levelOneJsRealPath, String levelOneJsFileName, String levelTwoJsFileName, String templateRealPathFileName) throws CardException, SQLException, IOException {
        try {
            ResourceBundle levelOnesConfig = ConfigUtils.getLevelOnesConfig();
            if (null == levelOnesConfig) {
                throw new CardException("levelone.properties文件不存在！");
            }
            Set<String> keySet = levelOnesConfig.keySet();
            String[] arrLevelOneFolders = keySet.toArray(new String[] {});
            String[] arrLevelOneTitles = new String[arrLevelOneFolders.length];
            Section[] sections = new Section[arrLevelOneFolders.length];
            for (int i = 0; i < arrLevelOneFolders.length; i++) {
                arrLevelOneTitles[i] = levelOnesConfig.getString(arrLevelOneFolders[i]);
                sections[i] = new Section();
                sections[i].setTitle(arrLevelOneTitles[i]);
                sections[i].setParentId(0L);
                sections[i].setStatus(1);
                sections[i].setFolderName(arrLevelOneFolders[i]);
            }
            openCon();
            con.setAutoCommit(false);
            SectionDAO dao = new SectionDAO(con);
            List<Section> levelOnesList = dao.queryLevelOne();
            for (Section s : sections) {
                if (levelOnesList.size() > 0) {
                    break;
                }
                if (null == dao.queryLevelOneByTitleOrFolderName(s.getTitle(), s.getFolderName())) {
                    dao.insertLevelOne(s);
                }
            }
            for (Section s : levelOnesList) {
                String levelOneRealDirPath = StringUtils.join(new String[] { levelOneRealPath, File.separator, s.getFolderName() });
                FileUtils.makeDir(levelOneRealDirPath);
            }
            String realJsPathFileName = StringUtils.join(new String[] { levelOneJsRealPath, File.separator, levelOneJsFileName });
            LevelOneUtils.createLevelOneSectionsJSFile(realJsPathFileName, levelOnesList, LevelOneUtils.getStaticLevelOnesInfo());
            for (Section s : levelOnesList) {
                List<Section> levelTwosList = dao.queryLevelTwo(s.getSectionId());
                String levelTwoRealPathJsFileName = StringUtils.join(new String[] { levelOneRealPath, File.separator, s.getFolderName(), File.separator, levelTwoJsFileName });
                LevelTwoUtils.createLevelTwoJsFile(levelTwoRealPathJsFileName, levelTwosList);
                for (Section levelTwo : levelTwosList) {
                    String levelTwoFileName = levelTwo.getFileName();
                    int dotLastIndex = levelTwoFileName.lastIndexOf(".");
                    String extendFileName = levelTwoFileName.substring(dotLastIndex + 1);
                    if (!StringUtils.equals(extendFileName, "html")) {
                        continue;
                    }
                    String staticRealPathFileName = StringUtils.join(new String[] { levelOneRealPath, File.separator, levelTwoFileName.replace('/', File.separatorChar) });
                    LevelTwoUtils.createLevelTwoStaticFile(templateRealPathFileName, staticRealPathFileName, new String[] { ConfigUtils.getAppConfig("titleFlag"), ConfigUtils.getAppConfig("contentFlag") }, new String[] { levelTwo.getTitle(), levelTwo.getFileContent() });
                }
            }
            con.commit();
        } catch (CardException e) {
            con.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            closeCon();
        }
    }
}
