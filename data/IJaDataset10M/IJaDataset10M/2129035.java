package fr.smile.liferay.portlet.news.model;

/**
 * <p>
 * This class is a wrapper for {@link NewsEntry}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       NewsEntry
 * @generated
 */
public class NewsEntryWrapper implements NewsEntry {

    public NewsEntryWrapper(NewsEntry newsEntry) {
        _newsEntry = newsEntry;
    }

    public long getPrimaryKey() {
        return _newsEntry.getPrimaryKey();
    }

    public void setPrimaryKey(long pk) {
        _newsEntry.setPrimaryKey(pk);
    }

    public java.lang.String getUuid() {
        return _newsEntry.getUuid();
    }

    public void setUuid(java.lang.String uuid) {
        _newsEntry.setUuid(uuid);
    }

    public long getEntryId() {
        return _newsEntry.getEntryId();
    }

    public void setEntryId(long entryId) {
        _newsEntry.setEntryId(entryId);
    }

    public long getCompanyId() {
        return _newsEntry.getCompanyId();
    }

    public void setCompanyId(long companyId) {
        _newsEntry.setCompanyId(companyId);
    }

    public long getUserId() {
        return _newsEntry.getUserId();
    }

    public void setUserId(long userId) {
        _newsEntry.setUserId(userId);
    }

    public java.lang.String getUserUuid() throws com.liferay.portal.kernel.exception.SystemException {
        return _newsEntry.getUserUuid();
    }

    public void setUserUuid(java.lang.String userUuid) {
        _newsEntry.setUserUuid(userUuid);
    }

    public java.lang.String getUserName() {
        return _newsEntry.getUserName();
    }

    public void setUserName(java.lang.String userName) {
        _newsEntry.setUserName(userName);
    }

    public java.util.Date getCreateDate() {
        return _newsEntry.getCreateDate();
    }

    public void setCreateDate(java.util.Date createDate) {
        _newsEntry.setCreateDate(createDate);
    }

    public java.util.Date getModifiedDate() {
        return _newsEntry.getModifiedDate();
    }

    public void setModifiedDate(java.util.Date modifiedDate) {
        _newsEntry.setModifiedDate(modifiedDate);
    }

    public java.lang.String getClassName() {
        return _newsEntry.getClassName();
    }

    public long getClassNameId() {
        return _newsEntry.getClassNameId();
    }

    public void setClassNameId(long classNameId) {
        _newsEntry.setClassNameId(classNameId);
    }

    public long getClassPK() {
        return _newsEntry.getClassPK();
    }

    public void setClassPK(long classPK) {
        _newsEntry.setClassPK(classPK);
    }

    public java.lang.String getTitle() {
        return _newsEntry.getTitle();
    }

    public void setTitle(java.lang.String title) {
        _newsEntry.setTitle(title);
    }

    public java.lang.String getDescription() {
        return _newsEntry.getDescription();
    }

    public void setDescription(java.lang.String description) {
        _newsEntry.setDescription(description);
    }

    public java.lang.String getContent() {
        return _newsEntry.getContent();
    }

    public void setContent(java.lang.String content) {
        _newsEntry.setContent(content);
    }

    public java.util.Date getDisplayDate() {
        return _newsEntry.getDisplayDate();
    }

    public void setDisplayDate(java.util.Date displayDate) {
        _newsEntry.setDisplayDate(displayDate);
    }

    public java.util.Date getExpirationDate() {
        return _newsEntry.getExpirationDate();
    }

    public void setExpirationDate(java.util.Date expirationDate) {
        _newsEntry.setExpirationDate(expirationDate);
    }

    public NewsEntry toEscapedModel() {
        return _newsEntry.toEscapedModel();
    }

    public boolean isNew() {
        return _newsEntry.isNew();
    }

    public void setNew(boolean n) {
        _newsEntry.setNew(n);
    }

    public boolean isCachedModel() {
        return _newsEntry.isCachedModel();
    }

    public void setCachedModel(boolean cachedModel) {
        _newsEntry.setCachedModel(cachedModel);
    }

    public boolean isEscapedModel() {
        return _newsEntry.isEscapedModel();
    }

    public void setEscapedModel(boolean escapedModel) {
        _newsEntry.setEscapedModel(escapedModel);
    }

    public java.io.Serializable getPrimaryKeyObj() {
        return _newsEntry.getPrimaryKeyObj();
    }

    public com.liferay.portlet.expando.model.ExpandoBridge getExpandoBridge() {
        return _newsEntry.getExpandoBridge();
    }

    public void setExpandoBridgeAttributes(com.liferay.portal.service.ServiceContext serviceContext) {
        _newsEntry.setExpandoBridgeAttributes(serviceContext);
    }

    public java.lang.Object clone() {
        return _newsEntry.clone();
    }

    public int compareTo(NewsEntry newsEntry) {
        return _newsEntry.compareTo(newsEntry);
    }

    public int hashCode() {
        return _newsEntry.hashCode();
    }

    public java.lang.String toString() {
        return _newsEntry.toString();
    }

    public java.lang.String toXmlString() {
        return _newsEntry.toXmlString();
    }

    public java.lang.String getDefaultLocale() {
        return _newsEntry.getDefaultLocale();
    }

    public java.lang.String[] getAvailableLocales() {
        return _newsEntry.getAvailableLocales();
    }

    public java.lang.String getContentByLocale(java.lang.String languageId) {
        return _newsEntry.getContentByLocale(languageId);
    }

    public java.lang.String getTitleByLocale(java.lang.String languageId) {
        return _newsEntry.getTitleByLocale(languageId);
    }

    public java.lang.String getDescriptionByLocale(java.lang.String languageId) {
        return _newsEntry.getDescriptionByLocale(languageId);
    }

    public NewsEntry getWrappedNewsEntry() {
        return _newsEntry;
    }

    private NewsEntry _newsEntry;
}
