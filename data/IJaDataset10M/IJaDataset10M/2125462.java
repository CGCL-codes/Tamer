package org.plazmaforge.bsolution.carservice.common.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.plazmaforge.bsolution.organization.common.beans.IOrganizationTitle;
import org.plazmaforge.bsolution.partner.common.beans.PartnerHeader;
import org.plazmaforge.framework.core.data.SimpleBean;

/**
 * 
 * @author Oleh Hapon
 * $Id: CarInsurerContractVariant.java,v 1.3 2010/12/05 07:53:48 ohapon Exp $
 */
public class CarInsurerContractVariant extends SimpleBean {

    private IOrganizationTitle organization;

    private PartnerHeader insurer;

    private String contractCode;

    private Date contractDate;

    private String variantCode;

    private Date variantDate;

    private List<CarInsurerServPrice> servPriceList;

    private List<CarInsurerPaintTypePrice> paintTypePriceList;

    private List<CarInsurerPaintTypeVolume> paintTypeVolumeList;

    private List<CarInsurerConsumablesPrice> consumablesPriceList;

    private List<CarInsurerPaintBoxPrice> paintBoxPriceList;

    private List<CarInsurerPartPrice> partPriceList;

    private List<CarInsurerPrepareHour> prepareHourList;

    public void init() {
        getServPriceList().size();
        getPaintTypePriceList().size();
        getPaintTypeVolumeList().size();
        getConsumablesPriceList().size();
        getPaintBoxPriceList().size();
        getPartPriceList().size();
        getPrepareHourList().size();
    }

    public float findDiscountOfPartPrice(int age) {
        if (partPriceList == null || partPriceList.isEmpty()) {
            return 0;
        }
        for (CarInsurerPartPrice item : partPriceList) {
            if (item.isAgeInRange(age)) {
                return item.getDiscountPercent();
            }
        }
        return 0;
    }

    public Double findPriceOfServ(int age, CarServType carServType) {
        if (servPriceList == null || servPriceList.isEmpty()) {
            return null;
        }
        for (CarInsurerServPrice item : servPriceList) {
            if (item.isAgeInRange(age, carServType)) {
                return new Double(item.getPrice());
            }
        }
        return null;
    }

    public Double findPriceOfPaint(CarPaintType carPaintType, CarPaintMark carPaintMark) {
        if (paintTypePriceList == null || paintTypePriceList.isEmpty()) {
            return null;
        }
        if (carPaintType == null || carPaintMark == null) {
            return null;
        }
        for (CarInsurerPaintTypePrice item : paintTypePriceList) {
            if (item.isAccept(carPaintType, carPaintMark)) {
                return new Double(item.getPrice());
            }
        }
        return null;
    }

    public CarInsurerPaintTypeVolume findPaintVolumeVariant(CarClass carClass, CarPaintDetailType carPaintDetailType) {
        if (paintTypeVolumeList == null || paintTypeVolumeList.isEmpty()) {
            return null;
        }
        if (carClass == null || carPaintDetailType == null) {
            return null;
        }
        for (CarInsurerPaintTypeVolume item : paintTypeVolumeList) {
            if (item.isAccept(carClass, carPaintDetailType)) {
                return item;
            }
        }
        return null;
    }

    public CarInsurerConsumablesPrice findConsumablePriceVariant(CarClass carClass, CarPaintDetailType carPaintDetailType, CarDamageLevel carDamageLevel) {
        if (consumablesPriceList == null || consumablesPriceList.isEmpty()) {
            return null;
        }
        if (carClass == null || carPaintDetailType == null || carDamageLevel == null) {
            return null;
        }
        for (CarInsurerConsumablesPrice item : consumablesPriceList) {
            if (item.isAccept(carClass, carPaintDetailType, carDamageLevel)) {
                return item;
            }
        }
        return null;
    }

    public IOrganizationTitle getOrganization() {
        return organization;
    }

    public void setOrganization(IOrganizationTitle organization) {
        this.organization = organization;
    }

    public String getOrganizationName() {
        return organization == null ? null : organization.getName();
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public PartnerHeader getInsurer() {
        return insurer;
    }

    public void setInsurer(PartnerHeader insurer) {
        this.insurer = insurer;
    }

    public String getInsurerName() {
        return insurer == null ? null : insurer.getName();
    }

    public String getVariantCode() {
        return variantCode;
    }

    public void setVariantCode(String variantCode) {
        this.variantCode = variantCode;
    }

    public Date getVariantDate() {
        return variantDate;
    }

    public void setVariantDate(Date variantDate) {
        this.variantDate = variantDate;
    }

    public List<CarInsurerServPrice> getServPriceList() {
        if (servPriceList == null) {
            servPriceList = new ArrayList<CarInsurerServPrice>();
        }
        return servPriceList;
    }

    public void setServPriceList(List<CarInsurerServPrice> servPriceList) {
        this.servPriceList = servPriceList;
    }

    public void addServPrice(CarInsurerServPrice servPrice) {
        servPrice.setContractVariant(this);
        getServPriceList().add(servPrice);
    }

    public List<CarInsurerPaintTypePrice> getPaintTypePriceList() {
        if (paintTypePriceList == null) {
            paintTypePriceList = new ArrayList<CarInsurerPaintTypePrice>();
        }
        return paintTypePriceList;
    }

    public void setPaintTypePriceList(List<CarInsurerPaintTypePrice> paintTypePriceList) {
        this.paintTypePriceList = paintTypePriceList;
    }

    public void addPaintTypePrice(CarInsurerPaintTypePrice paintTypePrice) {
        paintTypePrice.setContractVariant(this);
        getPaintTypePriceList().add(paintTypePrice);
    }

    public List<CarInsurerPaintTypeVolume> getPaintTypeVolumeList() {
        if (paintTypeVolumeList == null) {
            paintTypeVolumeList = new ArrayList<CarInsurerPaintTypeVolume>();
        }
        return paintTypeVolumeList;
    }

    public void setPaintTypeVolumeList(List<CarInsurerPaintTypeVolume> paintTypeVolumeList) {
        this.paintTypeVolumeList = paintTypeVolumeList;
    }

    public void addPaintTypeVolume(CarInsurerPaintTypeVolume paintTypeVolume) {
        paintTypeVolume.setContractVariant(this);
        getPaintTypeVolumeList().add(paintTypeVolume);
    }

    public List<CarInsurerConsumablesPrice> getConsumablesPriceList() {
        if (consumablesPriceList == null) {
            consumablesPriceList = new ArrayList<CarInsurerConsumablesPrice>();
        }
        return consumablesPriceList;
    }

    public void setConsumablesPriceList(List<CarInsurerConsumablesPrice> consumablesPriceList) {
        this.consumablesPriceList = consumablesPriceList;
    }

    public void addConsumablesPrice(CarInsurerConsumablesPrice consumablesPrice) {
        consumablesPrice.setContractVariant(this);
        getConsumablesPriceList().add(consumablesPrice);
    }

    public List<CarInsurerPaintBoxPrice> getPaintBoxPriceList() {
        if (paintBoxPriceList == null) {
            paintBoxPriceList = new ArrayList<CarInsurerPaintBoxPrice>();
        }
        return paintBoxPriceList;
    }

    public void setPaintBoxPriceList(List<CarInsurerPaintBoxPrice> paintBoxPriceList) {
        this.paintBoxPriceList = paintBoxPriceList;
    }

    public void addPaintBoxPrice(CarInsurerPaintBoxPrice paintBoxPrice) {
        paintBoxPrice.setContractVariant(this);
        getPaintBoxPriceList().add(paintBoxPrice);
    }

    public List<CarInsurerPartPrice> getPartPriceList() {
        if (partPriceList == null) {
            partPriceList = new ArrayList<CarInsurerPartPrice>();
        }
        return partPriceList;
    }

    public void setPartPriceList(List<CarInsurerPartPrice> partPriceList) {
        this.partPriceList = partPriceList;
    }

    public void addPartPrice(CarInsurerPartPrice partPrice) {
        partPrice.setContractVariant(this);
        getPartPriceList().add(partPrice);
    }

    public List<CarInsurerPrepareHour> getPrepareHourList() {
        if (prepareHourList == null) {
            prepareHourList = new ArrayList<CarInsurerPrepareHour>();
        }
        return prepareHourList;
    }

    public void setPrepareHourList(List<CarInsurerPrepareHour> prepareHourList) {
        this.prepareHourList = prepareHourList;
    }

    public void addPrepareHour(CarInsurerPrepareHour prepareHour) {
        prepareHour.setContractVariant(this);
        getPrepareHourList().add(prepareHour);
    }
}
