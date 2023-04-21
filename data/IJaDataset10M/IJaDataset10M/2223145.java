package com.sisioh.erp.core.entity.partner.customer;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.sisioh.erp.core.entity.AbstractEntity;
import com.sisioh.erp.core.entity.employee.Employee;

/**
 * 顧客スケジュール
 * 
 * @author j5ik2o
 */
@Entity
@SuppressWarnings("serial")
public class CustomerSchedule extends AbstractEntity {

    /**
	 * 顧客スケジュールID(PK)
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_SCHEDULE_GEN")
    @SequenceGenerator(name = "CUSTOMER_SCHEDULE_GEN", sequenceName = "CUSTOMER_SCHEDULE_SEQ", allocationSize = 1)
    @Column(precision = 19, nullable = false, unique = true)
    public Long customerScheduleId;

    /**
	 * 顧客スケジュールシステムコード(UK)
	 */
    @Column(length = 20, nullable = false, unique = true)
    public String customerScheduleSystemCode;

    /**
	 * 顧客スケジュールコード(UK)
	 */
    @Column(length = 255, nullable = true, unique = true)
    public String customerScheduleCode;

    /**
	 * 削除区分 (0=非削除,1=削除)
	 */
    @Column(precision = 10, nullable = false, unique = false)
    public boolean deleteType;

    /**
	 * 無効区分 (0=有効,1=無効)
	 */
    @Column(precision = 10, nullable = false, unique = false)
    public boolean disableType;

    /**
	 * 加盟店ID(FK)
	 */
    @Column(precision = 19, nullable = false, unique = false)
    public Long memberId;

    /**
	 * 開始日
	 */
    @Temporal(TemporalType.DATE)
    @Column(nullable = false, unique = false)
    public Date startDate;

    /**
	 * 終了日
	 */
    @Temporal(TemporalType.DATE)
    @Column(nullable = true, unique = false)
    public Date endDate;

    /**
	 * 開始時間
	 */
    @Temporal(TemporalType.TIME)
    @Column(nullable = true, unique = false)
    public Date startTime;

    /**
	 * 終了時間
	 */
    @Temporal(TemporalType.TIME)
    @Column(nullable = true, unique = false)
    public Date endTime;

    /**
	 * 担当者ID(FK)
	 */
    @Column(precision = 19, nullable = true, unique = false)
    public Long inChargeId;

    /**
	 * 公開区分 (0=非公開, 1=公開)
	 */
    @Column(length = 1, nullable = false, unique = false)
    public boolean publicType;

    /**
	 * タイトル
	 */
    @Column(length = 255, nullable = false, unique = false)
    public String title;

    /**
	 * メモ
	 */
    @Lob
    @Column(length = 2147483647, nullable = true, unique = false)
    public String memo;

    /**
	 * 担当者 関連プロパティ
	 */
    @ManyToOne
    @JoinColumn(name = "in_charge_id", referencedColumnName = "employee_id")
    public Employee inCharge;

    /**
	 * 顧客スケジュール購読者リスト 関連プロパティ
	 */
    @OneToMany(mappedBy = "customerSchedule")
    public List<CustomerScheduleSubscriber> customerScheduleSubscriberList;

    @Override
    public String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this);
    }
}
