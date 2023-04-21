package com.systop.core.webapp.struts2.action;

import org.apache.commons.lang.StringUtils;
import com.systop.core.model.BaseModel;
import com.systop.core.service.BaseGenericsManager;
import com.systop.core.util.RequestUtil;

/**
 * 配合ExtJs（http://extjs.com）的grid的Action基类。提供了取得pageSize和pageNo
 * 的方法。{@link ExtJsCrudAction#index()}方法会根据Request Header的Accept属性
 * 判断是否返回JSon数据。
 * @author Sam Lee
 *
 * @param <T>
 * @param <M>
 */
@SuppressWarnings({ "serial" })
public class ExtJsCrudAction<T extends BaseModel, M extends BaseGenericsManager<T>> extends JsonCrudAction<T, M> {

    /**
   * ExtJs 的分页参数
   */
    public static final String EXT_PARAM_START = "start";

    /**
   * ExtJs 的分页参数
   */
    public static final String EXT_PARAM_LIMIT = "limit";

    /**
   * ExtJs 排序参数
   */
    public static final String EXT_PARAM_SORT_PROPERTY = "sort";

    /**
   * ExtJs排序参数
   */
    public static final String EXT_PARAM_SORT_DIR = "dir";

    /**
   * 如果是json请求，则取得request的PageNo信息，在使用Extjs的情况下
   * pageNo属性，保存在"start"参数中，表示的是起始记录的索引。可以根据
   * pageSize属性计算pageNo.
   * 如果不是json请求，从ecside中取得pageNo.
   * @see DefaultCrudAction#getPageNo()
   */
    @Override
    protected int getPageNo() {
        int pageNo = 0;
        if (RequestUtil.isJsonRequest(getRequest())) {
            String start = getRequest().getParameter(EXT_PARAM_START);
            if (StringUtils.isNumeric(start)) {
                pageNo = (Integer.valueOf(start) / getPageSize()) + 1;
            }
        } else {
            pageNo = super.getPageNo();
        }
        return pageNo;
    }

    /**
   * 如果是json请求，则取得request的pageSize信息，在使用Extjs的情况下
   * pageSize属性，保存在"limit"参数中。
   * 如果不是json请求，从ecside中取得PageSize.
   */
    @Override
    protected int getPageSize() {
        int pageSize = 0;
        if (RequestUtil.isJsonRequest(getRequest())) {
            String limit = getRequest().getParameter(EXT_PARAM_LIMIT);
            if (StringUtils.isNumeric(limit)) {
                pageSize = Integer.valueOf(limit);
            }
        } else {
            pageSize = super.getPageSize();
        }
        return pageSize;
    }

    /**
   * 取得ExtJS的排序属性
   * @see {@link DefaultCrudAction#getSortProperty()}
   */
    @Override
    protected String getSortProperty() {
        if (RequestUtil.isJsonRequest(getRequest())) {
            return getRequest().getParameter(EXT_PARAM_SORT_PROPERTY);
        } else {
            return super.getSortProperty();
        }
    }

    /**
   * 取得ExtJs的排序方向
   */
    @Override
    protected String getSortDir() {
        if (!RequestUtil.isJsonRequest(getRequest())) {
            return super.getSortDir();
        }
        String dir = null;
        if (StringUtils.isNotBlank(getSortProperty())) {
            dir = getRequest().getParameter(EXT_PARAM_SORT_DIR);
            if (StringUtils.isBlank(dir)) {
                dir = "DESC";
            }
        }
        return dir;
    }
}
