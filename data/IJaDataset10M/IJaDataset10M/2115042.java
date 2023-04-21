package cn.mmbook.freemark.generator.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javacommon.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import cn.mmbook.freemark.constant.DaoConstant;
import cn.mmbook.freemark.generator.DaoGenerator;
import cn.mmbook.freemark.model.DaoModel;
import cn.mmbook.platform.facade.TagService;
import cn.mmbook.platform.model.manage.SiteChannels;
import cn.org.rapid_framework.page.Page;

@Component("daoGenerator")
@Transactional
public class DaoGeneratorImpl extends AbstractGenerator implements DaoGenerator {

    private TagService tagService;

    /**增加setXXXX()方法,spring就可以通过autowire自动设置对象属性*/
    public void setTagService(TagService manager) {
        this.tagService = manager;
    }

    public void genDaoInterface(String fileName) {
        DaoModel daoModel = new DaoModel();
        daoModel.setPackageName(DaoConstant.PACKAGE);
        String className = StringUtils.substringBefore(fileName, ".");
        daoModel.setClassName(className);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("model", daoModel);
        String filePath = new String("src/" + package2path(DaoConstant.PACKAGE) + "/" + fileName);
        super.generate(DaoConstant.INTERFACE_TEMPLATE, data, filePath);
    }

    public void genDaoImpl(String fileName) {
        DaoModel daoModel = new DaoModel();
        daoModel.setPackageName(DaoConstant.IMPL_PACKAGE);
        String className = StringUtils.substringBefore(fileName, ".");
        daoModel.setClassName(className);
        String impInterface = StringUtils.substringBefore(fileName, "Impl");
        String changeClass = StringUtils.substringBefore(impInterface, "Dao");
        daoModel.setImplementsName(impInterface);
        daoModel.setChangeClass(changeClass);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("model", daoModel);
        String filePath = new String("src/" + package2path(DaoConstant.IMPL_PACKAGE) + "/" + fileName);
        super.generate(DaoConstant.IMPL_TEMPLATE, data, filePath);
    }

    public void genAll(String objectName) {
        genDaoInterface(objectName + "Dao.java");
        genDaoImpl(objectName + "DaoImpl.java");
    }

    public void genSitePartPage() {
        String fileName = DateUtil.createFileNameNew() + ".jsp";
        fileName = "index.jsp";
        String filePath = new String("web/site/" + package2path(DaoConstant.IMPL_SITEPART) + "/" + fileName);
        Map map_param = new HashMap();
        map_param.put("channelsId", "1");
        map_param.put("count", "0,12");
        map_param.put("page", "2");
        map_param.put("sortColumns", " insert_time_ desc");
        Page page_info = tagService.getSitePartList(map_param);
        Map<String, Object> data = new HashMap<String, Object>();
        List list_info = (List) page_info.getResult();
        System.out.println(list_info.size());
        data.put("listData", list_info);
        SiteChannels newsitem = tagService.getSiteChannelInfo(1);
        data.put("newsitem", newsitem);
        super.generate(DaoConstant.SITEPART_TEMPLATE_INDEX, data, filePath);
    }

    public void genIndex() {
        System.out.println("genIndex()");
        String fileName = "index.jsp";
        String filePath = new String("web/site/" + package2path(DaoConstant.IMPL_SITEPART) + "/" + fileName);
        Map map_param = new HashMap();
        map_param.put("siteId", "1");
        map_param.put("count", "10");
        map_param.put("page", "2");
        map_param.put("sortColumns", " insert_time_ desc");
        Page page_info = tagService.getSiteChannelList(map_param);
        Map<String, Object> data = new HashMap<String, Object>();
        List list_info = page_info.getResult();
        System.out.println(list_info.size());
        data.put("SiteChannelList", list_info);
        map_param = new HashMap();
        map_param.put("siteId", "1");
        List channelAndpart = tagService.getAllChannelPart(map_param);
        data.put("channelAndpart", channelAndpart);
        super.generate(DaoConstant.SITEPART_TEMPLATE_INDEX, data, filePath);
    }

    public void generate() {
    }

    public void init(Object obj) {
    }
}
