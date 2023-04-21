package vzone.structs;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 *公司信息 饭店 大卖场 商家 投资公司 软件公司等都可以用此类描述
 * @author Kingpro
 */
public class Company implements Serializable {

    /**
     * 公司ID
     */
    public UUID IdUuid;

    /**
     * 公司所有者
     */
    public BasePeople Owner;

    /**
     * 公司名称
     */
    public String NameS;

    /**
     * 公司建立时间
     */
    public Date StartD;

    /**
     * 公司描述
     */
    public String DescriptionS;

    public String pwdHash;

    /**
 * 构造函数 应再加入其他重载构造函数
 * @param owner
 */
    public Company(BasePeople owner) {
        Owner = owner;
        IdUuid = UUID.randomUUID();
    }

    public Company() {
    }
}
