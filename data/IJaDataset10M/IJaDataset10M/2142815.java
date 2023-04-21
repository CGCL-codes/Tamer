package net.shopxx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 实体类 - 会员注册协议
 * ============================================================================
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得SHOP++商业授权之前，您不能将本软件应用于商业用途，否则SHOP++将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.shopxx.net
 * ----------------------------------------------------------------------------
 * KEY: SHOPXXB434FB5ED72E29A02354F64D0573E959
 * ============================================================================
 */
@Entity
public class Agreement extends BaseEntity {

    private static final long serialVersionUID = 7226979256801891226L;

    public static final String AGREEMENT_ID = "1";

    private String content;

    @Column(length = 10000)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
