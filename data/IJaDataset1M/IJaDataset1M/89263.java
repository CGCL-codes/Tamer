package com.jeecms.cms.action;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import com.jeecms.common.hibernate3.OrderBy;
import com.jeecms.core.entity.User;

@SuppressWarnings("serial")
@Scope("prototype")
@Controller("core.userAct")
public class UserAct extends com.jeecms.core.JeeCoreAction {

    private static final Logger log = LoggerFactory.getLogger(UserAct.class);

    public String list() {
        this.pagination = userMng.findAll(pageNo, getCookieCount(), new OrderBy[] { OrderBy.desc("id") });
        return LIST;
    }

    public String add() {
        return ADD;
    }

    public String save() {
        userMng.save(bean);
        log.info("��� �û� �ɹ���{}" + bean.getLoginName());
        return list();
    }

    public String edit() {
        this.bean = userMng.findById(id);
        return EDIT;
    }

    public String update() {
        userMng.updateDefault(bean);
        log.info("�޸� �û� �ɹ���{}" + bean.getLoginName());
        return list();
    }

    public String delete() {
        try {
            for (User o : userMng.deleteById(ids)) {
                log.info("ɾ��  �û� �ɹ�:{}", o.getLoginName());
            }
        } catch (DataIntegrityViolationException e) {
            addActionError("��¼�ѱ����ã�����ɾ��!");
            return SHOW_ERROR;
        }
        return list();
    }

    public String editPassword() {
        return "editPassword";
    }

    /**
	 * ��̨�����û���֤
	 * 
	 * @return
	 */
    public String checkUsername() {
        if (StringUtils.isBlank(username)) {
            return renderText("false");
        }
        return renderText(userMng.checkLoginName(username) ? "true" : "false");
    }

    /**
	 * ��̨�����ʼ���֤
	 * 
	 * @return
	 */
    public String checkEmail() {
        if (StringUtils.isBlank(email)) {
            return renderText("false");
        }
        return renderText(userMng.checkEmail(email) ? "true" : "false");
    }

    public boolean validateSave() {
        if (hasErrors()) {
            return true;
        }
        return false;
    }

    public boolean validateEdit() {
        if (hasErrors()) {
            return true;
        }
        if (vldExist(id)) {
            return true;
        }
        return false;
    }

    public boolean validateUpdate() {
        if (hasErrors()) {
            return true;
        }
        if (vldExist(bean.getId())) {
            return true;
        }
        return false;
    }

    public boolean validateDelete() {
        if (hasErrors()) {
            return true;
        }
        if (vldBatch()) {
            return true;
        }
        for (Long id : ids) {
            if (id.equals(1)) {
                addActionError("��������Ա����ɾ��");
                return true;
            }
            if (vldExist(id)) {
                return true;
            }
        }
        return false;
    }

    private boolean vldExist(Long id) {
        User entity = userMng.findById(id);
        if (entity == null) {
            addActionError("��¼�����ڣ�" + id);
            return true;
        }
        return false;
    }

    private User bean;

    private String username;

    private String email;

    public User getBean() {
        return bean;
    }

    public void setBean(User bean) {
        this.bean = bean;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
