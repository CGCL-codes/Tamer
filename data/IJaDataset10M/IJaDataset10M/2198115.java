package models;

import play.*;
import play.db.jpa.*;
import javax.persistence.*;
import java.util.*;
import play.data.validation.*;
import validation.Unique;

/**
 * 用户
 * @author yujie
 */
@Entity
public class YongHu extends Model {

    /**
	 * 注册时间：用于记录用户首次注册的时间
	 */
    public Date zhuCeShiJian;

    /**
	 * 登录时间：用于记录用户上次登录的时间
	 */
    public Date dengLuShiJian;

    /**
	 * 用户名（非空，唯一）
	 */
    @Required
    @MinSize(2)
    @Unique(message = "YongHu.yongHuMing.Unique")
    public String yongHuMing;

    /**
	 * 密码（非空，最小长度为6）
	 */
    @Required
    @MinSize(6)
    @Password
    public String miMa;

    /**
	 * 姓名（非空，最小长度为2）
	 */
    @Required
    @MinSize(2)
    public String xingMing;

    /**
	 * 性别（非空）
	 */
    @Required
    public boolean xingBie;

    /**
	 * 电子邮箱（正确邮件格式，非空，唯一）
	 */
    @Email
    @Required
    @Unique(message = "YongHu.youXiang.Unique")
    public String youXiang;

    /**
	 * 联系电话
	 */
    public String dianHua;

    /**
	 * QQ
	 */
    public String qq;

    /**
	 * MSN
	 */
    public String msn;

    /**
	 * 单向映射关系：多个用户对应一个角色
	 * 说明：目前每一个用户只能分配（或从属）一个角色
	 */
    @ManyToOne
    public JueSe jueSe;

    public YongHu() {
        this.zhuCeShiJian = new Date();
    }

    public YongHu(Date dengLuShiJian, String yongHuMing, String miMa, String xingMing, boolean xingBie, String youXiang, String dianHua, String qq, String msn, JueSe jueSe) {
        this();
        this.dengLuShiJian = dengLuShiJian;
        this.yongHuMing = yongHuMing;
        this.miMa = miMa;
        this.xingMing = xingMing;
        this.xingBie = xingBie;
        this.youXiang = youXiang;
        this.dianHua = dianHua;
        this.qq = qq;
        this.msn = msn;
        this.jueSe = jueSe;
    }

    /**
	 * 根据操作判断用户是否具有该操作权限
	 * @param caoZuo
	 * @return
	 */
    public boolean youQuanXian(String caoZuo) {
        if (shiGuanLiYuan()) return true; else {
            QuanXian quanXian = QuanXian.findByCaoZuo(caoZuo);
            if (!(quanXian == null || jueSe == null || jueSe.quanXians == null)) {
                if (this.jueSe.quanXians.contains(quanXian)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * 根据操作判断用户是否具有该操作权限
	 * @param caoZuo
	 * @return
	 */
    public boolean youQuanXian(String caoZuo, ShiTi shiTi) {
        if (shiGuanLiYuan()) return true; else {
            QuanXian quanXian = QuanXian.findByCaoZuo(caoZuo);
            if (!(quanXian == null || jueSe == null || jueSe.quanXians == null)) {
                if (this.jueSe.quanXians.contains(quanXian)) {
                    if (this.jueSe.jiBie >= shiTi.xinZengYongHu.jueSe.jiBie) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
	 * 依据配置文件设定判断是否管理员，该管理员只有一个且有强制的最高权限
	 * @return
	 */
    public boolean shiGuanLiYuan() {
        if (youXiang != null) return youXiang.equals(Play.configuration.getProperty("YongHu.shiGuanLiYuan")); else return false;
    }

    /**
	 * 连接：用于用户登录，验证用户名和密码，返回用户对象
	 * @param yongHuMing2
	 * @param miMa
	 * @return
	 */
    public static YongHu lianJie(String yongHuMing, String miMa) {
        return find("byYongHuMingAndMiMa", yongHuMing, miMa).first();
    }

    @Override
    public String toString() {
        return xingMing + "[" + yongHuMing + "]";
    }
}
