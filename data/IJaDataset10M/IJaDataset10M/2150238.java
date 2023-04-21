package net.naijatek.myalumni.modules.common.persistence.hibernate;

import java.util.Date;
import java.util.List;
import net.naijatek.myalumni.modules.common.domain.ScrollVO;
import net.naijatek.myalumni.modules.common.domain.SystemConfigVO;
import net.naijatek.myalumni.modules.common.domain.TwitterVO;
import net.naijatek.myalumni.modules.common.persistence.BaseHibernateDao;
import net.naijatek.myalumni.modules.common.persistence.iface.SystemConfigDao;
import net.naijatek.myalumni.util.BaseConstants;
import net.naijatek.myalumni.util.SystemConfigConstants;
import net.naijatek.myalumni.util.encryption.base64.Base64Coder;

public class SystemConfigHibernateDao extends BaseHibernateDao implements SystemConfigDao {

    @SuppressWarnings("unchecked")
    public void updateOrgInfo(SystemConfigVO config) {
        SystemConfigVO systemConfig = new SystemConfigVO();
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            _systemConfig.setLastModification(BaseConstants.UPDATED);
            _systemConfig.setLastModifiedBy(config.getLastModifiedBy());
            _systemConfig.setLastModifiedDate(new Date());
            _systemConfig.setOrganizationName(config.getOrganizationName());
            _systemConfig.setOrganizationShortName(config.getOrganizationShortName());
            _systemConfig.setOrgEmail(config.getOrgEmail());
            _systemConfig.setOrgFirstYear(config.getOrgFirstYear());
            update(_systemConfig);
        } else {
            systemConfig.setLastModification(BaseConstants.ADDED);
            systemConfig.setLastModifiedBy(config.getLastModifiedBy());
            systemConfig.setLastModifiedDate(new Date());
            systemConfig.setOrganizationName(config.getOrganizationName());
            systemConfig.setOrganizationShortName(config.getOrganizationShortName());
            systemConfig.setOrgEmail(config.getOrgEmail());
            systemConfig.setOrgFirstYear(config.getOrgFirstYear());
            add(systemConfig);
        }
    }

    @SuppressWarnings("unchecked")
    public SystemConfigVO getOrgInfo() {
        SystemConfigVO config = new SystemConfigVO();
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            config.setOrganizationName(_systemConfig.getOrganizationName());
            config.setOrganizationShortName(_systemConfig.getOrganizationShortName());
            config.setOrgEmail(_systemConfig.getOrgEmail());
            config.setOrgFirstYear(_systemConfig.getOrgFirstYear());
            config.setOrgAboutUs(_systemConfig.getOrgAboutUs());
            config.setOrgIntro(_systemConfig.getOrgIntro());
        }
        return config;
    }

    @SuppressWarnings("unchecked")
    public void updateRssFeedUrl(String rssFeedUrl, String rssFeedHeader, String lastModifiedBy) {
        SystemConfigVO systemConfig = new SystemConfigVO();
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            _systemConfig.setLastModification(BaseConstants.UPDATED);
            _systemConfig.setLastModifiedBy(lastModifiedBy);
            _systemConfig.setLastModifiedDate(new Date());
            _systemConfig.setRssHeader(rssFeedHeader);
            _systemConfig.setRssUrl(rssFeedUrl);
            update(_systemConfig);
        } else {
            systemConfig.setLastModification(BaseConstants.ADDED);
            systemConfig.setLastModifiedBy(lastModifiedBy);
            systemConfig.setLastModifiedDate(new Date());
            systemConfig.setRssHeader(rssFeedHeader);
            systemConfig.setRssUrl(rssFeedUrl);
            add(systemConfig);
        }
    }

    @SuppressWarnings("unchecked")
    public SystemConfigVO getRssFeedSource() {
        SystemConfigVO config = new SystemConfigVO();
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            config.setRssUrl(_systemConfig.getRssUrl());
            config.setRssHeader(_systemConfig.getRssHeader());
        }
        return config;
    }

    @SuppressWarnings("unchecked")
    public int getSessionTimeOut() {
        int timeout = SystemConfigConstants.DEFAULT_SESSION_TIMEOUT;
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            if (_systemConfig.getSessionTimeout() != null) {
                try {
                    timeout = Integer.parseInt(_systemConfig.getSessionTimeout());
                } catch (NumberFormatException nfe) {
                    timeout = SystemConfigConstants.DEFAULT_SESSION_TIMEOUT;
                }
            }
        }
        return timeout;
    }

    @SuppressWarnings("unchecked")
    public void updateSessionTimeOut(String sessionTimeout, String lastModifiedBy) {
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            _systemConfig.setLastModification(BaseConstants.UPDATED);
            _systemConfig.setSessionTimeout(sessionTimeout);
        } else {
            _systemConfig = new SystemConfigVO();
            _systemConfig.setLastModification(BaseConstants.ADDED);
        }
        _systemConfig.setLastModifiedDate(new Date());
        _systemConfig.setLastModifiedBy(lastModifiedBy);
        getHibernateTemplate().saveOrUpdate(_systemConfig);
    }

    /**
	 * Org About Us
	 */
    public String getOrgAboutUs() {
        return getSystemConfig().getOrgAboutUs();
    }

    public void updateOrgAboutUs(String orgAboutUs, String lastModifiedBy) {
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            _systemConfig.setLastModification(BaseConstants.UPDATED);
        } else {
            _systemConfig = new SystemConfigVO();
            _systemConfig.setLastModification(BaseConstants.ADDED);
        }
        _systemConfig.setOrgAboutUs(orgAboutUs);
        _systemConfig.setLastModifiedBy(lastModifiedBy);
        _systemConfig.setLastModifiedDate(new Date());
        getHibernateTemplate().saveOrUpdate(_systemConfig);
    }

    /**
	 * Org Intro
	 */
    public String getOrgIntro() {
        return getSystemConfig().getOrgIntro();
    }

    public void updateOrgIntro(String orgIntro, String lastModifiedBy) {
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            _systemConfig.setLastModification(BaseConstants.UPDATED);
        } else {
            _systemConfig = new SystemConfigVO();
            _systemConfig.setLastModification(BaseConstants.ADDED);
        }
        _systemConfig.setOrgIntro(orgIntro);
        _systemConfig.setLastModifiedBy(lastModifiedBy);
        _systemConfig.setLastModifiedDate(new Date());
        getHibernateTemplate().saveOrUpdate(_systemConfig);
    }

    /**
	 * Server URL
	 */
    public SystemConfigVO getSystemConfig() {
        SystemConfigVO systemSetup = (SystemConfigVO) getSession().getNamedQuery("get.systemConfig").uniqueResult();
        return (systemSetup == null) ? new SystemConfigVO() : systemSetup;
    }

    public void updateServerUrl(String serverUrl, String lastModifiedBy) {
        SystemConfigVO systemSetup = getSystemConfig();
        if (systemSetup.getSystemConfigId() == null) systemSetup = new SystemConfigVO();
        systemSetup.setServerUrl(serverUrl);
        systemSetup.setLastModifiedBy(lastModifiedBy);
        getHibernateTemplate().saveOrUpdate(systemSetup);
    }

    public void updateAlbumUrl(String albumUrl, String lastModifiedBy) {
        SystemConfigVO systemSetup = getSystemConfig();
        if (systemSetup.getSystemConfigId() == null) systemSetup = new SystemConfigVO();
        systemSetup.setAlbumUrl(albumUrl);
        systemSetup.setLastModifiedBy(lastModifiedBy);
        getHibernateTemplate().saveOrUpdate(systemSetup);
    }

    public void updateForumUrl(String forumUrl, String lastModifiedBy) {
        SystemConfigVO systemSetup = getSystemConfig();
        if (systemSetup.getSystemConfigId() == null) systemSetup = new SystemConfigVO();
        systemSetup.setForumUrl(forumUrl);
        systemSetup.setLastModifiedBy(lastModifiedBy);
        getHibernateTemplate().saveOrUpdate(systemSetup);
    }

    /**
	 * First year school started
	 */
    @SuppressWarnings("unchecked")
    public int getFirstYearofSchool() {
        int year = 1900;
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            if (_systemConfig.getOrgFirstYear() != null) {
                try {
                    year = Integer.parseInt(_systemConfig.getOrgFirstYear());
                } catch (NumberFormatException nfe) {
                    year = 1900;
                }
            }
        }
        return year;
    }

    @SuppressWarnings("unchecked")
    public void updateFirstYearofSchool(SystemConfigVO sysConfigVO) {
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            _systemConfig.setLastModification(BaseConstants.UPDATED);
            _systemConfig.setOrgFirstYear(sysConfigVO.getOrgFirstYear());
        } else {
            _systemConfig = new SystemConfigVO();
            _systemConfig.setLastModification(BaseConstants.ADDED);
        }
        _systemConfig.setLastModifiedDate(new Date());
        _systemConfig.setLastModifiedBy(sysConfigVO.getLastModifiedBy());
        getHibernateTemplate().saveOrUpdate(_systemConfig);
    }

    /**
	 * Scroll
	 */
    public void addScroll(ScrollVO scroll) {
        add(scroll);
    }

    @SuppressWarnings("unchecked")
    public List<ScrollVO> getAllScrolls() {
        return getHibernateTemplate().find("from ScrollVO");
    }

    @SuppressWarnings("unchecked")
    public ScrollVO getLatestScroll() {
        ScrollVO scroll = new ScrollVO();
        List<ScrollVO> scrolls = getSession().createQuery("select from ScrollVO s order by s.lastModifiedDate desc").list();
        if (scrolls != null && scrolls.size() > 0) {
            scroll = scrolls.get(0);
        }
        return scroll;
    }

    public void updateScroll(String scrollId, String lastModifiedBy) {
        ScrollVO scrollVO = (ScrollVO) load(ScrollVO.class, scrollId);
        scrollVO.setLastModifiedBy(lastModifiedBy);
        update(scrollVO);
    }

    /**
	 * Dormitory
	 */
    public void updateDormitory(SystemConfigVO systemConfigVO) {
        SystemConfigVO systemSetup = getSystemConfig();
        if (systemSetup.getSystemConfigId() == null) systemSetup = new SystemConfigVO();
        systemSetup.setHasDormitory(systemConfigVO.getHasDormitory());
        getHibernateTemplate().saveOrUpdate(systemSetup);
    }

    public String getDormitory() {
        String hasDorm = BaseConstants.BOOLEAN_NO;
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            if (_systemConfig.getHasDormitory() != null) {
                try {
                    hasDorm = _systemConfig.getHasDormitory();
                } catch (NumberFormatException nfe) {
                    hasDorm = BaseConstants.BOOLEAN_NO;
                }
            }
        }
        return hasDorm;
    }

    /**
     * Upload Logo
     */
    public void uploadLogo(SystemConfigVO systemConfigVO) {
        SystemConfigVO systemSetup = getSystemConfig();
        if (systemSetup.getSystemConfigId() == null) systemSetup = new SystemConfigVO();
        systemSetup.setLogoFileName(systemConfigVO.getLogoFileName());
        getHibernateTemplate().saveOrUpdate(systemSetup);
    }

    /**
	 * Birthday Notification
	 */
    public void updateBirthdayNotification(SystemConfigVO systemConfigVO) {
        SystemConfigVO systemSetup = getSystemConfig();
        if (systemSetup.getSystemConfigId() == null) systemSetup = new SystemConfigVO();
        systemSetup.setBirthdayNotification(systemConfigVO.getBirthdayNotification());
        getHibernateTemplate().saveOrUpdate(systemSetup);
    }

    public String getBirthdayNotification() {
        String notification = BaseConstants.BOOLEAN_NO;
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            if (_systemConfig.getBirthdayNotification() != null) {
                notification = _systemConfig.getBirthdayNotification();
            }
        }
        return notification;
    }

    /**
	 * Setup
	 */
    public void setupIntialization(SystemConfigVO systemConfigVO) {
        SystemConfigVO systemSetup = getSystemConfig();
        if (systemSetup.getSystemConfigId() == null) systemSetup = new SystemConfigVO();
        systemSetup.setOrgFirstYear(systemConfigVO.getOrgFirstYear());
        systemSetup.setOrganizationName(systemConfigVO.getOrganizationName());
        systemSetup.setOrgEmail(systemConfigVO.getOrgEmail());
        systemSetup.setOrganizationShortName(systemConfigVO.getOrganizationShortName());
        systemSetup.setHasDormitory(systemConfigVO.getHasDormitory());
        systemSetup.setAlbumUrl(systemConfigVO.getAlbumUrl());
        systemSetup.setForumUrl(systemConfigVO.getForumUrl());
        systemSetup.setServerUrl(systemConfigVO.getServerUrl());
        systemSetup.setSessionTimeout(systemConfigVO.getSessionTimeout());
        systemSetup.setRssUrl(systemConfigVO.getRssUrl());
        systemSetup.setBirthdayNotification(systemConfigVO.getBirthdayNotification());
        systemSetup.setLastModifiedBy("sysadmin");
        systemSetup.setLastModification(BaseConstants.ADDED);
        systemSetup.setLastModifiedDate(new Date());
        getHibernateTemplate().saveOrUpdate(systemSetup);
    }

    /**
	 * TwitterCredentials
	 */
    public void updateTwitterCredentials(TwitterVO twitterVO) {
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            _systemConfig.setLastModification(BaseConstants.UPDATED);
            _systemConfig.setTwitteruser(twitterVO.getTwitteruser());
            _systemConfig.setTwitterpswd(twitterVO.getTwitterpswd());
        } else {
            _systemConfig = new SystemConfigVO();
            _systemConfig.setTwitteruser(twitterVO.getTwitteruser());
            _systemConfig.setTwitterpswd(twitterVO.getTwitterpswd());
            _systemConfig.setLastModification(BaseConstants.ADDED);
        }
        _systemConfig.setLastModifiedDate(new Date());
        _systemConfig.setLastModifiedBy(twitterVO.getLastModifiedBy());
        getHibernateTemplate().saveOrUpdate(_systemConfig);
    }

    /**
	 * TwitterCredentials
	 * @return
	 */
    public TwitterVO getTwitterCredentials() {
        TwitterVO twitterVO = new TwitterVO();
        SystemConfigVO _systemConfig = getSystemConfig();
        if (_systemConfig.getSystemConfigId() != null) {
            if (_systemConfig.getTwitteruser() != null) {
                twitterVO.setTwitteruser(_systemConfig.getTwitteruser());
                twitterVO.setTwitterpswd(Base64Coder.decodeString(_systemConfig.getTwitterpswd()));
            }
        }
        return twitterVO;
    }
}
