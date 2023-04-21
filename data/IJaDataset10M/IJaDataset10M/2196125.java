package com.f2ms.model;

import java.util.Date;
import com.f2ms.enumeration.PickupMode;
import com.f2ms.enumeration.PickupStatus;

public class PickupOrder extends BaseModel {

    private static final long serialVersionUID = 8550318184925990848L;

    private Long id;

    private String docNo;

    private Date pickupDate;

    private String bookingNo;

    private Long truckerId;

    private Integer mode = PickupMode.CGS.getValue();

    private Integer status = PickupStatus.NEW.getValue();

    private String statusReason;

    private Long warehouseId;

    private String remarks;

    private String truckerName;

    private String warehouseName;

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public Long getTruckerId() {
        return truckerId;
    }

    public void setTruckerId(Long truckerId) {
        this.truckerId = truckerId;
    }

    public String getTruckerName() {
        return truckerName;
    }

    public void setTruckerName(String truckerName) {
        this.truckerName = truckerName;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
}
