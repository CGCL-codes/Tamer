package com.simplenix.nicasio.admin.eb;

import com.simplenix.nicasio.def.Feature;
import com.simplenix.nicasio.def.Module;
import com.simplenix.nicasio.hmaint.HMaintHide;
import com.simplenix.nicasio.hmaint.annotations.HMaint;
import com.simplenix.nicasio.hmaint.annotations.Hide;
import com.simplenix.nicasio.hmaint.annotations.Searchable;
import com.simplenix.nicasio.misc.StrUtil;
import com.simplenix.nicasio.persistence.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author fronald
 */
@Entity
@Table(name = "NIC_GROUPS")
@HMaint(order = "name", customUpdate = "com.simplenix.nicasio.admin.misc.UpdateGroup")
public class Group implements Serializable {

    private long groupId;

    private String name;

    private List<FeatureSecurity> featureSecurityList;

    private List<UsuGroup> usuGroups;

    /**
	 * @return the groupId
	 */
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "GRP_ID")
    @Hide(where = HMaintHide.HIDE_ON_LIST_AND_ADD_AND_UPDATE)
    public long getGroupId() {
        return groupId;
    }

    /**
	 * @param groupId the groupId to set
	 */
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    /**
	 * @return the name
	 */
    @Column(name = "GRP_NAME")
    @Searchable
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the featureSecurityList
	 */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    public List<FeatureSecurity> getFeatureSecurityList() {
        return featureSecurityList;
    }

    /**
	 * @param featureSecurityList the featureSecurityList to set
	 */
    public void setFeatureSecurityList(List<FeatureSecurity> featureSecurityList) {
        this.featureSecurityList = featureSecurityList;
    }

    public static Boolean haveAccess(Group group, Module module, Feature feature) {
        Boolean rtn = false;
        for (com.simplenix.nicasio.admin.eb.FeatureSecurity feat : group.getFeatureSecurityList()) {
            String featKey = module.getName() + "." + feature.getName();
            if (StrUtil.nvl(feat.getFeatureName()).equals(featKey) && feat.getHavePermission()) {
                rtn = true;
                break;
            }
        }
        return rtn;
    }

    public static void setAccess(Group group, Module module, Feature feature, boolean havePermission) {
        String key = module.getName() + "." + feature.getName();
        boolean create = true;
        if (group.getFeatureSecurityList() != null && group.getFeatureSecurityList().size() > 0) {
            for (com.simplenix.nicasio.admin.eb.FeatureSecurity feat : group.getFeatureSecurityList()) {
                if (StrUtil.nvl(feat.getFeatureName()).equals(key)) {
                    feat.setHavePermission(havePermission);
                    HibernateUtil.getCurrentSession().update(feat);
                    create = false;
                    break;
                }
            }
        }
        if (create) {
            com.simplenix.nicasio.admin.eb.FeatureSecurity feat = new com.simplenix.nicasio.admin.eb.FeatureSecurity();
            feat.setGroup(group);
            feat.setFeatureName(key);
            feat.setHavePermission(havePermission);
            HibernateUtil.getCurrentSession().save(feat);
        }
    }

    /**
	 * @return the usuGroups
	 */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    public List<UsuGroup> getUsuGroups() {
        return usuGroups;
    }

    /**
	 * @param usuGroups the usuGroups to set
	 */
    public void setUsuGroups(List<UsuGroup> usuGroups) {
        this.usuGroups = usuGroups;
    }
}
