package com.shenming.sms.dc.sql2java;

/**
 * SmVwVipAccountBean is a mapping of sm_vw_vip_account Table.
*/
public class SmVwVipAccountBean {

    private Long smVipSeq;

    private boolean smVipSeq_is_modified = false;

    private boolean smVipSeq_is_initialized = false;

    private String vipChtName;

    private boolean vipChtName_is_modified = false;

    private boolean vipChtName_is_initialized = false;

    private String vipEngName;

    private boolean vipEngName_is_modified = false;

    private boolean vipEngName_is_initialized = false;

    private java.util.Date birthday;

    private boolean birthday_is_modified = false;

    private boolean birthday_is_initialized = false;

    private String sex;

    private boolean sex_is_modified = false;

    private boolean sex_is_initialized = false;

    private String blood;

    private boolean blood_is_modified = false;

    private boolean blood_is_initialized = false;

    private String marrage;

    private boolean marrage_is_modified = false;

    private boolean marrage_is_initialized = false;

    private String address;

    private boolean address_is_modified = false;

    private boolean address_is_initialized = false;

    private String phone;

    private boolean phone_is_modified = false;

    private boolean phone_is_initialized = false;

    private String mobile;

    private boolean mobile_is_modified = false;

    private boolean mobile_is_initialized = false;

    private String mailAddr;

    private boolean mailAddr_is_modified = false;

    private boolean mailAddr_is_initialized = false;

    private String fovarite;

    private boolean fovarite_is_modified = false;

    private boolean fovarite_is_initialized = false;

    private String job;

    private boolean job_is_modified = false;

    private boolean job_is_initialized = false;

    private java.util.Date crDate;

    private boolean crDate_is_modified = false;

    private boolean crDate_is_initialized = false;

    private Long buySum;

    private boolean buySum_is_modified = false;

    private boolean buySum_is_initialized = false;

    private String isValidVip;

    private boolean isValidVip_is_modified = false;

    private boolean isValidVip_is_initialized = false;

    private String username;

    private boolean username_is_modified = false;

    private boolean username_is_initialized = false;

    private String password;

    private boolean password_is_modified = false;

    private boolean password_is_initialized = false;

    private Long errorCnt;

    private boolean errorCnt_is_modified = false;

    private boolean errorCnt_is_initialized = false;

    private Long owner;

    private boolean owner_is_modified = false;

    private boolean owner_is_initialized = false;

    private String roleType;

    private boolean roleType_is_modified = false;

    private boolean roleType_is_initialized = false;

    private Long userId;

    private boolean userId_is_modified = false;

    private boolean userId_is_initialized = false;

    private String description;

    private boolean description_is_modified = false;

    private boolean description_is_initialized = false;

    private boolean _isNew = true;

    /**
     * Do not use this constructor directly, please use the factory method
     * available in the associated manager.
     */
    SmVwVipAccountBean() {
    }

    /**
     * Getter method for smVipSeq.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.sm_vip_seq
     * <li>column size: 22
     * <li>jdbc type returned by the driver: Types.DECIMAL
     * </ul>
     *
     * @return the value of smVipSeq
     */
    public Long getSmVipSeq() {
        return smVipSeq;
    }

    /**
     * Setter method for smVipSeq.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to smVipSeq
     */
    public void setSmVipSeq(Long newVal) {
        if ((newVal != null && this.smVipSeq != null && (newVal.compareTo(this.smVipSeq) == 0)) || (newVal == null && this.smVipSeq == null && smVipSeq_is_initialized)) {
            return;
        }
        this.smVipSeq = newVal;
        smVipSeq_is_modified = true;
        smVipSeq_is_initialized = true;
    }

    /**
     * Setter method for smVipSeq.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to smVipSeq
     */
    public void setSmVipSeq(long newVal) {
        setSmVipSeq(new Long(newVal));
    }

    /**
     * Determines if the smVipSeq has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isSmVipSeqModified() {
        return smVipSeq_is_modified;
    }

    /**
     * Determines if the smVipSeq has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isSmVipSeqInitialized() {
        return smVipSeq_is_initialized;
    }

    /**
     * Getter method for vipChtName.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.vip_cht_name
     * <li>column size: 50
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of vipChtName
     */
    public String getVipChtName() {
        return vipChtName;
    }

    /**
     * Setter method for vipChtName.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to vipChtName
     */
    public void setVipChtName(String newVal) {
        if ((newVal != null && this.vipChtName != null && (newVal.compareTo(this.vipChtName) == 0)) || (newVal == null && this.vipChtName == null && vipChtName_is_initialized)) {
            return;
        }
        this.vipChtName = newVal;
        vipChtName_is_modified = true;
        vipChtName_is_initialized = true;
    }

    /**
     * Determines if the vipChtName has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isVipChtNameModified() {
        return vipChtName_is_modified;
    }

    /**
     * Determines if the vipChtName has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isVipChtNameInitialized() {
        return vipChtName_is_initialized;
    }

    /**
     * Getter method for vipEngName.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.vip_eng_name
     * <li>column size: 50
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of vipEngName
     */
    public String getVipEngName() {
        return vipEngName;
    }

    /**
     * Setter method for vipEngName.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to vipEngName
     */
    public void setVipEngName(String newVal) {
        if ((newVal != null && this.vipEngName != null && (newVal.compareTo(this.vipEngName) == 0)) || (newVal == null && this.vipEngName == null && vipEngName_is_initialized)) {
            return;
        }
        this.vipEngName = newVal;
        vipEngName_is_modified = true;
        vipEngName_is_initialized = true;
    }

    /**
     * Determines if the vipEngName has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isVipEngNameModified() {
        return vipEngName_is_modified;
    }

    /**
     * Determines if the vipEngName has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isVipEngNameInitialized() {
        return vipEngName_is_initialized;
    }

    /**
     * Getter method for birthday.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.birthday
     * <li>column size: 0
     * <li>jdbc type returned by the driver: Types.TIMESTAMP
     * </ul>
     *
     * @return the value of birthday
     */
    public java.util.Date getBirthday() {
        return birthday;
    }

    /**
     * Setter method for birthday.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to birthday
     */
    public void setBirthday(java.util.Date newVal) {
        if ((newVal != null && this.birthday != null && (newVal.compareTo(this.birthday) == 0)) || (newVal == null && this.birthday == null && birthday_is_initialized)) {
            return;
        }
        this.birthday = newVal;
        birthday_is_modified = true;
        birthday_is_initialized = true;
    }

    /**
     * Setter method for birthday.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to birthday
     */
    public void setBirthday(long newVal) {
        setBirthday(new java.util.Date(newVal));
    }

    /**
     * Determines if the birthday has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isBirthdayModified() {
        return birthday_is_modified;
    }

    /**
     * Determines if the birthday has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isBirthdayInitialized() {
        return birthday_is_initialized;
    }

    /**
     * Getter method for sex.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.sex
     * <li>column size: 10
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * Setter method for sex.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to sex
     */
    public void setSex(String newVal) {
        if ((newVal != null && this.sex != null && (newVal.compareTo(this.sex) == 0)) || (newVal == null && this.sex == null && sex_is_initialized)) {
            return;
        }
        this.sex = newVal;
        sex_is_modified = true;
        sex_is_initialized = true;
    }

    /**
     * Determines if the sex has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isSexModified() {
        return sex_is_modified;
    }

    /**
     * Determines if the sex has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isSexInitialized() {
        return sex_is_initialized;
    }

    /**
     * Getter method for blood.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.blood
     * <li>column size: 10
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of blood
     */
    public String getBlood() {
        return blood;
    }

    /**
     * Setter method for blood.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to blood
     */
    public void setBlood(String newVal) {
        if ((newVal != null && this.blood != null && (newVal.compareTo(this.blood) == 0)) || (newVal == null && this.blood == null && blood_is_initialized)) {
            return;
        }
        this.blood = newVal;
        blood_is_modified = true;
        blood_is_initialized = true;
    }

    /**
     * Determines if the blood has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isBloodModified() {
        return blood_is_modified;
    }

    /**
     * Determines if the blood has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isBloodInitialized() {
        return blood_is_initialized;
    }

    /**
     * Getter method for marrage.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.marrage
     * <li>column size: 10
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of marrage
     */
    public String getMarrage() {
        return marrage;
    }

    /**
     * Setter method for marrage.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to marrage
     */
    public void setMarrage(String newVal) {
        if ((newVal != null && this.marrage != null && (newVal.compareTo(this.marrage) == 0)) || (newVal == null && this.marrage == null && marrage_is_initialized)) {
            return;
        }
        this.marrage = newVal;
        marrage_is_modified = true;
        marrage_is_initialized = true;
    }

    /**
     * Determines if the marrage has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isMarrageModified() {
        return marrage_is_modified;
    }

    /**
     * Determines if the marrage has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isMarrageInitialized() {
        return marrage_is_initialized;
    }

    /**
     * Getter method for address.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.address
     * <li>column size: 100
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter method for address.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to address
     */
    public void setAddress(String newVal) {
        if ((newVal != null && this.address != null && (newVal.compareTo(this.address) == 0)) || (newVal == null && this.address == null && address_is_initialized)) {
            return;
        }
        this.address = newVal;
        address_is_modified = true;
        address_is_initialized = true;
    }

    /**
     * Determines if the address has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isAddressModified() {
        return address_is_modified;
    }

    /**
     * Determines if the address has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isAddressInitialized() {
        return address_is_initialized;
    }

    /**
     * Getter method for phone.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.phone
     * <li>column size: 20
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Setter method for phone.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to phone
     */
    public void setPhone(String newVal) {
        if ((newVal != null && this.phone != null && (newVal.compareTo(this.phone) == 0)) || (newVal == null && this.phone == null && phone_is_initialized)) {
            return;
        }
        this.phone = newVal;
        phone_is_modified = true;
        phone_is_initialized = true;
    }

    /**
     * Determines if the phone has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isPhoneModified() {
        return phone_is_modified;
    }

    /**
     * Determines if the phone has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isPhoneInitialized() {
        return phone_is_initialized;
    }

    /**
     * Getter method for mobile.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.mobile
     * <li>column size: 20
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Setter method for mobile.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to mobile
     */
    public void setMobile(String newVal) {
        if ((newVal != null && this.mobile != null && (newVal.compareTo(this.mobile) == 0)) || (newVal == null && this.mobile == null && mobile_is_initialized)) {
            return;
        }
        this.mobile = newVal;
        mobile_is_modified = true;
        mobile_is_initialized = true;
    }

    /**
     * Determines if the mobile has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isMobileModified() {
        return mobile_is_modified;
    }

    /**
     * Determines if the mobile has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isMobileInitialized() {
        return mobile_is_initialized;
    }

    /**
     * Getter method for mailAddr.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.mail_addr
     * <li>column size: 100
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of mailAddr
     */
    public String getMailAddr() {
        return mailAddr;
    }

    /**
     * Setter method for mailAddr.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to mailAddr
     */
    public void setMailAddr(String newVal) {
        if ((newVal != null && this.mailAddr != null && (newVal.compareTo(this.mailAddr) == 0)) || (newVal == null && this.mailAddr == null && mailAddr_is_initialized)) {
            return;
        }
        this.mailAddr = newVal;
        mailAddr_is_modified = true;
        mailAddr_is_initialized = true;
    }

    /**
     * Determines if the mailAddr has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isMailAddrModified() {
        return mailAddr_is_modified;
    }

    /**
     * Determines if the mailAddr has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isMailAddrInitialized() {
        return mailAddr_is_initialized;
    }

    /**
     * Getter method for fovarite.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.fovarite
     * <li>column size: 200
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of fovarite
     */
    public String getFovarite() {
        return fovarite;
    }

    /**
     * Setter method for fovarite.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to fovarite
     */
    public void setFovarite(String newVal) {
        if ((newVal != null && this.fovarite != null && (newVal.compareTo(this.fovarite) == 0)) || (newVal == null && this.fovarite == null && fovarite_is_initialized)) {
            return;
        }
        this.fovarite = newVal;
        fovarite_is_modified = true;
        fovarite_is_initialized = true;
    }

    /**
     * Determines if the fovarite has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isFovariteModified() {
        return fovarite_is_modified;
    }

    /**
     * Determines if the fovarite has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isFovariteInitialized() {
        return fovarite_is_initialized;
    }

    /**
     * Getter method for job.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.job
     * <li>column size: 100
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of job
     */
    public String getJob() {
        return job;
    }

    /**
     * Setter method for job.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to job
     */
    public void setJob(String newVal) {
        if ((newVal != null && this.job != null && (newVal.compareTo(this.job) == 0)) || (newVal == null && this.job == null && job_is_initialized)) {
            return;
        }
        this.job = newVal;
        job_is_modified = true;
        job_is_initialized = true;
    }

    /**
     * Determines if the job has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isJobModified() {
        return job_is_modified;
    }

    /**
     * Determines if the job has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isJobInitialized() {
        return job_is_initialized;
    }

    /**
     * Getter method for crDate.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.cr_date
     * <li>column size: 0
     * <li>jdbc type returned by the driver: Types.TIMESTAMP
     * </ul>
     *
     * @return the value of crDate
     */
    public java.util.Date getCrDate() {
        return crDate;
    }

    /**
     * Setter method for crDate.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to crDate
     */
    public void setCrDate(java.util.Date newVal) {
        if ((newVal != null && this.crDate != null && (newVal.compareTo(this.crDate) == 0)) || (newVal == null && this.crDate == null && crDate_is_initialized)) {
            return;
        }
        this.crDate = newVal;
        crDate_is_modified = true;
        crDate_is_initialized = true;
    }

    /**
     * Setter method for crDate.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to crDate
     */
    public void setCrDate(long newVal) {
        setCrDate(new java.util.Date(newVal));
    }

    /**
     * Determines if the crDate has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isCrDateModified() {
        return crDate_is_modified;
    }

    /**
     * Determines if the crDate has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isCrDateInitialized() {
        return crDate_is_initialized;
    }

    /**
     * Getter method for buySum.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.buy_sum
     * <li>column size: 22
     * <li>jdbc type returned by the driver: Types.DECIMAL
     * </ul>
     *
     * @return the value of buySum
     */
    public Long getBuySum() {
        return buySum;
    }

    /**
     * Setter method for buySum.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to buySum
     */
    public void setBuySum(Long newVal) {
        if ((newVal != null && this.buySum != null && (newVal.compareTo(this.buySum) == 0)) || (newVal == null && this.buySum == null && buySum_is_initialized)) {
            return;
        }
        this.buySum = newVal;
        buySum_is_modified = true;
        buySum_is_initialized = true;
    }

    /**
     * Setter method for buySum.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to buySum
     */
    public void setBuySum(long newVal) {
        setBuySum(new Long(newVal));
    }

    /**
     * Determines if the buySum has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isBuySumModified() {
        return buySum_is_modified;
    }

    /**
     * Determines if the buySum has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isBuySumInitialized() {
        return buySum_is_initialized;
    }

    /**
     * Getter method for isValidVip.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.is_valid_vip
     * <li>default value: N
     * <li>column size: 1
     * <li>jdbc type returned by the driver: Types.CHAR
     * </ul>
     *
     * @return the value of isValidVip
     */
    public String getIsValidVip() {
        return isValidVip;
    }

    /**
     * Setter method for isValidVip.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to isValidVip
     */
    public void setIsValidVip(String newVal) {
        if ((newVal != null && this.isValidVip != null && (newVal.compareTo(this.isValidVip) == 0)) || (newVal == null && this.isValidVip == null && isValidVip_is_initialized)) {
            return;
        }
        this.isValidVip = newVal;
        isValidVip_is_modified = true;
        isValidVip_is_initialized = true;
    }

    /**
     * Determines if the isValidVip has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isIsValidVipModified() {
        return isValidVip_is_modified;
    }

    /**
     * Determines if the isValidVip has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isIsValidVipInitialized() {
        return isValidVip_is_initialized;
    }

    /**
     * Getter method for username.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.username
     * <li>column size: 50
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter method for username.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to username
     */
    public void setUsername(String newVal) {
        if ((newVal != null && this.username != null && (newVal.compareTo(this.username) == 0)) || (newVal == null && this.username == null && username_is_initialized)) {
            return;
        }
        this.username = newVal;
        username_is_modified = true;
        username_is_initialized = true;
    }

    /**
     * Determines if the username has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isUsernameModified() {
        return username_is_modified;
    }

    /**
     * Determines if the username has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isUsernameInitialized() {
        return username_is_initialized;
    }

    /**
     * Getter method for password.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.password
     * <li>column size: 200
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter method for password.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to password
     */
    public void setPassword(String newVal) {
        if ((newVal != null && this.password != null && (newVal.compareTo(this.password) == 0)) || (newVal == null && this.password == null && password_is_initialized)) {
            return;
        }
        this.password = newVal;
        password_is_modified = true;
        password_is_initialized = true;
    }

    /**
     * Determines if the password has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isPasswordModified() {
        return password_is_modified;
    }

    /**
     * Determines if the password has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isPasswordInitialized() {
        return password_is_initialized;
    }

    /**
     * Getter method for errorCnt.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.error_cnt
     * <li>column size: 22
     * <li>jdbc type returned by the driver: Types.DECIMAL
     * </ul>
     *
     * @return the value of errorCnt
     */
    public Long getErrorCnt() {
        return errorCnt;
    }

    /**
     * Setter method for errorCnt.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to errorCnt
     */
    public void setErrorCnt(Long newVal) {
        if ((newVal != null && this.errorCnt != null && (newVal.compareTo(this.errorCnt) == 0)) || (newVal == null && this.errorCnt == null && errorCnt_is_initialized)) {
            return;
        }
        this.errorCnt = newVal;
        errorCnt_is_modified = true;
        errorCnt_is_initialized = true;
    }

    /**
     * Setter method for errorCnt.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to errorCnt
     */
    public void setErrorCnt(long newVal) {
        setErrorCnt(new Long(newVal));
    }

    /**
     * Determines if the errorCnt has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isErrorCntModified() {
        return errorCnt_is_modified;
    }

    /**
     * Determines if the errorCnt has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isErrorCntInitialized() {
        return errorCnt_is_initialized;
    }

    /**
     * Getter method for owner.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.owner
     * <li>comments: The group of user
     * <li>column size: 22
     * <li>jdbc type returned by the driver: Types.DECIMAL
     * </ul>
     *
     * @return the value of owner
     */
    public Long getOwner() {
        return owner;
    }

    /**
     * Setter method for owner.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to owner
     */
    public void setOwner(Long newVal) {
        if ((newVal != null && this.owner != null && (newVal.compareTo(this.owner) == 0)) || (newVal == null && this.owner == null && owner_is_initialized)) {
            return;
        }
        this.owner = newVal;
        owner_is_modified = true;
        owner_is_initialized = true;
    }

    /**
     * Setter method for owner.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to owner
     */
    public void setOwner(long newVal) {
        setOwner(new Long(newVal));
    }

    /**
     * Determines if the owner has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isOwnerModified() {
        return owner_is_modified;
    }

    /**
     * Determines if the owner has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isOwnerInitialized() {
        return owner_is_initialized;
    }

    /**
     * Getter method for roleType.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.role_type
     * <li>column size: 20
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of roleType
     */
    public String getRoleType() {
        return roleType;
    }

    /**
     * Setter method for roleType.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to roleType
     */
    public void setRoleType(String newVal) {
        if ((newVal != null && this.roleType != null && (newVal.compareTo(this.roleType) == 0)) || (newVal == null && this.roleType == null && roleType_is_initialized)) {
            return;
        }
        this.roleType = newVal;
        roleType_is_modified = true;
        roleType_is_initialized = true;
    }

    /**
     * Determines if the roleType has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isRoleTypeModified() {
        return roleType_is_modified;
    }

    /**
     * Determines if the roleType has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isRoleTypeInitialized() {
        return roleType_is_initialized;
    }

    /**
     * Getter method for userId.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.user_id
     * <li>column size: 22
     * <li>jdbc type returned by the driver: Types.DECIMAL
     * </ul>
     *
     * @return the value of userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Setter method for userId.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to userId
     */
    public void setUserId(Long newVal) {
        if ((newVal != null && this.userId != null && (newVal.compareTo(this.userId) == 0)) || (newVal == null && this.userId == null && userId_is_initialized)) {
            return;
        }
        this.userId = newVal;
        userId_is_modified = true;
        userId_is_initialized = true;
    }

    /**
     * Setter method for userId.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to userId
     */
    public void setUserId(long newVal) {
        setUserId(new Long(newVal));
    }

    /**
     * Determines if the userId has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isUserIdModified() {
        return userId_is_modified;
    }

    /**
     * Determines if the userId has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isUserIdInitialized() {
        return userId_is_initialized;
    }

    /**
     * Getter method for description.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: sm_vw_vip_account.description
     * <li>column size: 200
     * <li>jdbc type returned by the driver: Types.VARCHAR
     * </ul>
     *
     * @return the value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for description.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to description
     */
    public void setDescription(String newVal) {
        if ((newVal != null && this.description != null && (newVal.compareTo(this.description) == 0)) || (newVal == null && this.description == null && description_is_initialized)) {
            return;
        }
        this.description = newVal;
        description_is_modified = true;
        description_is_initialized = true;
    }

    /**
     * Determines if the description has been modified.
     *
     * @return true if the field has been modified, false if the field has not been modified
     */
    public boolean isDescriptionModified() {
        return description_is_modified;
    }

    /**
     * Determines if the description has been initialized.
     * <br>
     * It is useful to determine if a field is null on purpose or just because it has not been initialized.
     *
     * @return true if the field has been initialized, false otherwise
     */
    public boolean isDescriptionInitialized() {
        return description_is_initialized;
    }

    /**
     * Determines if the current object is new.
     *
     * @return true if the current object is new, false if the object is not new
     */
    public boolean isNew() {
        return _isNew;
    }

    /**
     * Specifies to the object if it has been set as new.
     *
     * @param isNew the boolean value to be assigned to the isNew field
     */
    public void isNew(boolean isNew) {
        this._isNew = isNew;
    }

    /**
     * Determines if the object has been modified since the last time this method was called.
     * <br>
     * We can also determine if this object has ever been modified since its creation.
     *
     * @return true if the object has been modified, false if the object has not been modified
     */
    public boolean isModified() {
        return smVipSeq_is_modified || vipChtName_is_modified || vipEngName_is_modified || birthday_is_modified || sex_is_modified || blood_is_modified || marrage_is_modified || address_is_modified || phone_is_modified || mobile_is_modified || mailAddr_is_modified || fovarite_is_modified || job_is_modified || crDate_is_modified || buySum_is_modified || isValidVip_is_modified || username_is_modified || password_is_modified || errorCnt_is_modified || owner_is_modified || roleType_is_modified || userId_is_modified || description_is_modified;
    }

    /**
     * Resets the object modification status to 'not modified'.
     */
    public void resetIsModified() {
        smVipSeq_is_modified = false;
        vipChtName_is_modified = false;
        vipEngName_is_modified = false;
        birthday_is_modified = false;
        sex_is_modified = false;
        blood_is_modified = false;
        marrage_is_modified = false;
        address_is_modified = false;
        phone_is_modified = false;
        mobile_is_modified = false;
        mailAddr_is_modified = false;
        fovarite_is_modified = false;
        job_is_modified = false;
        crDate_is_modified = false;
        buySum_is_modified = false;
        isValidVip_is_modified = false;
        username_is_modified = false;
        password_is_modified = false;
        errorCnt_is_modified = false;
        owner_is_modified = false;
        roleType_is_modified = false;
        userId_is_modified = false;
        description_is_modified = false;
    }

    /**
     * Copies the passed bean into the current bean.
     *
     * @param bean the bean to copy into the current bean
     */
    public void copy(SmVwVipAccountBean bean) {
        setSmVipSeq(bean.getSmVipSeq());
        setVipChtName(bean.getVipChtName());
        setVipEngName(bean.getVipEngName());
        setBirthday(bean.getBirthday());
        setSex(bean.getSex());
        setBlood(bean.getBlood());
        setMarrage(bean.getMarrage());
        setAddress(bean.getAddress());
        setPhone(bean.getPhone());
        setMobile(bean.getMobile());
        setMailAddr(bean.getMailAddr());
        setFovarite(bean.getFovarite());
        setJob(bean.getJob());
        setCrDate(bean.getCrDate());
        setBuySum(bean.getBuySum());
        setIsValidVip(bean.getIsValidVip());
        setUsername(bean.getUsername());
        setPassword(bean.getPassword());
        setErrorCnt(bean.getErrorCnt());
        setOwner(bean.getOwner());
        setRoleType(bean.getRoleType());
        setUserId(bean.getUserId());
        setDescription(bean.getDescription());
    }

    /**
     * Returns the object string representation.
     *
     * @return the object as a string
     */
    public String toString() {
        return "\n[sm_vw_vip_account] " + "\n - sm_vw_vip_account.sm_vip_seq = " + (smVipSeq_is_initialized ? ("[" + (smVipSeq == null ? null : smVipSeq.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.vip_cht_name = " + (vipChtName_is_initialized ? ("[" + (vipChtName == null ? null : vipChtName.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.vip_eng_name = " + (vipEngName_is_initialized ? ("[" + (vipEngName == null ? null : vipEngName.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.birthday = " + (birthday_is_initialized ? ("[" + (birthday == null ? null : birthday.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.sex = " + (sex_is_initialized ? ("[" + (sex == null ? null : sex.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.blood = " + (blood_is_initialized ? ("[" + (blood == null ? null : blood.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.marrage = " + (marrage_is_initialized ? ("[" + (marrage == null ? null : marrage.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.address = " + (address_is_initialized ? ("[" + (address == null ? null : address.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.phone = " + (phone_is_initialized ? ("[" + (phone == null ? null : phone.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.mobile = " + (mobile_is_initialized ? ("[" + (mobile == null ? null : mobile.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.mail_addr = " + (mailAddr_is_initialized ? ("[" + (mailAddr == null ? null : mailAddr.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.fovarite = " + (fovarite_is_initialized ? ("[" + (fovarite == null ? null : fovarite.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.job = " + (job_is_initialized ? ("[" + (job == null ? null : job.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.cr_date = " + (crDate_is_initialized ? ("[" + (crDate == null ? null : crDate.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.buy_sum = " + (buySum_is_initialized ? ("[" + (buySum == null ? null : buySum.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.is_valid_vip = " + (isValidVip_is_initialized ? ("[" + (isValidVip == null ? null : isValidVip.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.username = " + (username_is_initialized ? ("[" + (username == null ? null : username.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.password = " + (password_is_initialized ? ("[" + (password == null ? null : password.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.error_cnt = " + (errorCnt_is_initialized ? ("[" + (errorCnt == null ? null : errorCnt.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.owner = " + (owner_is_initialized ? ("[" + (owner == null ? null : owner.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.role_type = " + (roleType_is_initialized ? ("[" + (roleType == null ? null : roleType.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.user_id = " + (userId_is_initialized ? ("[" + (userId == null ? null : userId.toString()) + "]") : "not initialized") + "" + "\n - sm_vw_vip_account.description = " + (description_is_initialized ? ("[" + (description == null ? null : description.toString()) + "]") : "not initialized") + "";
    }
}
