package net.itsite.zhaopin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.itsite.user.UserSearchUtils;
import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.applets.attention.AttentionBean;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.applets.tag.TagBean;
import net.simpleframework.applets.tag.TagRBean;
import net.simpleframework.applets.tag.TagUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

public class ZhaopinPaper extends AbstractPagerHandle {

    @Override
    public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
        if ("title".equals(beanProperty)) {
            final StringBuffer buf = new StringBuffer(32);
            final int tagId = ConvertUtils.toInt(compParameter.getRequestParameter("tagId"), 0);
            final int t = ConvertUtils.toInt(compParameter.getRequestParameter("t"), 0);
            buf.append("<a href=\"/zhaopin/" + (t) + "-0-0.html\">招聘</a>");
            buf.append(HTMLBuilder.NAV);
            if (tagId != 0) {
                final TagBean tagBean = TagUtils.getTagBeanById(tagId);
                if (tagBean != null) {
                    buf.append("关联标签");
                    buf.append(HTMLBuilder.NAV);
                    buf.append("<span style=\"color:#B13D0A;font-weight: bold;\">").append(tagBean.getTagText()).append("</span>");
                }
            } else {
                buf.append("查询结果");
            }
            return buf.toString();
        }
        return super.getBeanProperty(compParameter, beanProperty);
    }

    @Override
    public IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter) {
        final String c = WebUtils.toLocaleString(compParameter.getRequestParameter("c"));
        if (StringUtils.hasText(c)) {
            UserSearchUtils.createSearch(compParameter, EFunctionModule.zhaopin, c);
            return ZhaopinUtils.applicationModule.createLuceneManager(compParameter).getLuceneQuery(c);
        }
        final IAccount login = AccountSession.getLogin(compParameter.getSession());
        final ITableEntityManager tMgr = ZhaopinUtils.applicationModule.getDataObjectManager();
        final String zhaopin_topic = compParameter.getRequestParameter("_zhaopin_topic");
        final StringBuffer sql = new StringBuffer();
        final StringBuffer where = new StringBuffer();
        final List<Object> lv = new ArrayList<Object>();
        sql.append("select zp.* from ");
        sql.append(ZhaopinApplicationModule.zhaopin.getName()).append(" zp ");
        final int tagId = ConvertUtils.toInt(compParameter.getRequestParameter("tagId"), 0);
        if (tagId != 0) {
            sql.append(" left join ").append(TagUtils.getTableEntityManager(TagRBean.class).getTablename()).append(" b");
            sql.append(" on zp.id = b.rid ");
            where.append(" and  b.tagid=? ");
            lv.add(tagId);
        }
        final int t = ConvertUtils.toInt(compParameter.getRequestParameter("t"), 0);
        final int od = ConvertUtils.toInt(compParameter.getRequestParameter("od"), 0);
        if (t == 2) {
            if (login == null) {
                return null;
            }
            sql.append("left join ").append(AttentionUtils.getTableEntityManager(AttentionBean.class).getTablename()).append(" at ");
            sql.append("on zp.id=at.attentionId ");
            where.append(" and zp.id=at.attentionId");
            where.append(" and at.accountId=?");
            lv.add(login.getId());
        } else if (t == 3) {
            if (login == null) {
                return null;
            }
            where.append(" and zp.userId=").append(login.getId());
        }
        if (StringsUtils.isNotBlank(zhaopin_topic)) {
            where.append(" and zp.title like ?");
            lv.add("%" + zhaopin_topic + "%");
        }
        final String zhaopin = compParameter.getRequestParameter("zhaopin");
        if (StringUtils.hasText(zhaopin)) {
            where.append(" and zp.zhaopin=?");
            lv.add(Boolean.valueOf(zhaopin));
        }
        sql.append("where 1=1 ").append(where);
        sql.append(" order by ttop desc");
        if (1 == od) {
            sql.append(",views desc");
        } else if (0 == od) {
            sql.append(",modifyDate desc");
        } else if (2 == od) {
            sql.append(",votes desc");
        } else if (3 == od) {
            sql.append(",remarkDate desc");
        } else if (4 == od) {
            sql.append(",attentions desc");
        }
        tMgr.reset();
        return tMgr.query(new SQLValue(sql.toString(), lv.toArray()), ZhaopinBean.class);
    }

    @Override
    public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
        final Map<String, Object> parameters = super.getFormParameters(compParameter);
        putParameter(compParameter, parameters, "t");
        putParameter(compParameter, parameters, "c");
        putParameter(compParameter, parameters, "od");
        putParameter(compParameter, parameters, "_zhaopin_topic");
        putParameter(compParameter, parameters, "zhaopin");
        putParameter(compParameter, parameters, "tagId");
        return parameters;
    }
}
