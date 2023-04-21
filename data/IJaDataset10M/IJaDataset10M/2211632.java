package com.multimedia.service.link;

import com.multimedia.model.beans.CommonItem;
import com.multimedia.model.beans.Link;
import common.cms.services2.AGenericCmsService;
import common.services.generic.IGenericService;
import java.util.Collection;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value = "cmsLinkService")
public class CmsLinkServiceImpl extends AGenericCmsService<Link, Long> {

    protected IGenericService<CommonItem, Long> commonItemService;

    @Override
    public void init() {
        super.init();
        StringBuilder sb = new StringBuilder();
        common.utils.MiscUtils.checkNotNull(commonItemService, "commonItemService", sb);
        if (sb.length() > 0) {
            throw new NullPointerException(sb.toString());
        }
    }

    @Override
    public Link getInsertBean(Link obj) {
        if (obj == null) return new Link();
        obj.setSort((Long) service.getSinglePropertyU("max(sort)+1", "id_item", obj.getId_item()));
        obj.setCommonItem(commonItemService.getById(obj.getId_item()));
        return obj;
    }

    @Override
    public int saveOrUpdateCollection(Collection<Link> c) {
        return service.updateCollection(c, "sort");
    }

    @Resource(name = "commonItemService")
    public void setCommonItemService(IGenericService<CommonItem, Long> service) {
        this.commonItemService = service;
    }

    @Resource(name = "linkService")
    public void setService(IGenericService<Link, Long> service) {
        this.service = service;
    }
}
