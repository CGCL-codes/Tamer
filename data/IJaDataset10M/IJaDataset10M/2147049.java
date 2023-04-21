package com.patientis.business.order;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import com.patientis.business.calculator.StandardUnits;
import com.patientis.business.controllers.DefaultCustomController;
import com.patientis.client.service.med.MedService;
import com.patientis.framework.controls.ISControlPanel;
import com.patientis.framework.logging.Log;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.common.ModelReference;
import com.patientis.model.med.AdminRouteModel;
import com.patientis.model.med.MedOrderModel;
import com.patientis.model.order.OrderModel;
import com.patientis.model.reference.ValueUnitReference;
import com.patientis.model.scheduling.FrequencyModel;

/**
 * @author gcaulton2
 *
 */
public class ClientPrescriptionMonitor extends DefaultCustomController {

    /**
	 * 
	 */
    private MedOrderModel previousValue = new MedOrderModel();

    /**
	 * 
	 */
    private ISControlPanel formDetailsControlPanel = null;

    /**
	 * @see com.patientis.business.common.ICustomController#initialize(com.patientis.model.common.IBaseModel)
	 */
    @Override
    public void clientInitializeMonitor(IBaseModel model, ISControlPanel formDetailsControlPanel) throws Exception {
        this.formDetailsControlPanel = formDetailsControlPanel;
        FormModel form = (FormModel) model;
        initialize(form.giveOrder(), form.giveOrder().giveMedOrder());
    }

    /**
	 * 
	 * @param order
	 */
    public void initialize(final OrderModel order, final MedOrderModel medOrder) throws Exception {
        if (order == null) {
            return;
        }
        updateOrder(order, medOrder);
        order.addPropertyChangeListener(String.valueOf(ModelReference.BASE), new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                updateOrder(order, medOrder);
            }
        });
    }

    /**
	 * Use this on all client side forms e.g. patient reg.
	 * add business rule to execute
	 * 
	 * @param order
	 * @param medOrder
	 */
    private void updateOrder(final OrderModel order, final MedOrderModel medOrder) {
        try {
            if (medOrder.getItemVolume() > 0) {
                if (medOrder.getOrderDose() != previousValue.getOrderDose()) {
                    medOrder.setVolumeForDose();
                } else if (medOrder.getInfuseVolume() != previousValue.getInfuseVolume()) {
                    medOrder.setDoseForVolume();
                }
            } else {
                if (medOrder.getOrderDose() != previousValue.getOrderDose()) {
                    medOrder.setDispenseQuantityForDose();
                } else if (medOrder.getDispenseQuantity() != previousValue.getDispenseQuantity()) {
                    medOrder.setDoseForDispenseQuantity();
                }
            }
            formDetailsControlPanel.setComponentVisible("Volume", medOrder.getItemVolume() > 0);
            formDetailsControlPanel.setComponentVisible("Volume Units", medOrder.getItemVolume() > 0);
            if (medOrder.getOrderDose() > 0 && order.getOrderFrequencyRef().isNotNew()) {
                createDirections(order, medOrder);
            }
            if (order.getOrderStartDt().isBefore(DateTimeModel.getNow().getStartOfHour())) {
                order.setOrderStartDt(DateTimeModel.getNow());
            }
            if (order.getOrderStopDt().isNull() || order.getOrderStopDt().isBefore(order.getOrderStartDt())) {
                order.setOrderStopDt(order.getStartDt());
            }
            previousValue.copyAllFrom(medOrder);
        } catch (Exception ex) {
            Log.exception(ex);
        }
    }

    /**
	 * Default directions
	 * 
	 * @param frequencyModel
	 */
    public void createDirections(OrderModel order, MedOrderModel medOrder) throws Exception {
        if (order.getFrequencyRef().isNotNew()) {
            StringBuffer sb = new StringBuffer(128);
            sb.append(Converter.convertDisplayString(medOrder.getDirectionsPrefix()));
            sb.append(" ");
            if (medOrder.hasVolume()) {
                sb.append(" ");
                sb.append(Converter.convertDisplayString(medOrder.getInfuseVolume()));
                sb.append(" ");
                sb.append(medOrder.getInfuseVolumeUnitRef().getDisplay().toLowerCase());
            } else {
                if (medOrder.getDispenseQuantity() > 0) {
                    sb.append(Converter.convertDisplayString(medOrder.getDispenseQuantity()));
                    sb.append(" ");
                    sb.append(medOrder.getOrderDoseFormRef().getDisplay().toLowerCase());
                    sb.append(" ");
                }
            }
            sb.append(" ");
            sb.append(Converter.convertDisplayString(medOrder.getDoseDirectionsSuffix()));
            order.setInstructions(Converter.convertDisplayString(sb.toString()));
        }
    }

    /**
	 * Daily dose
	 * 
	 * @return
	 */
    public double getDailyDoseQuantity(FrequencyModel frequencyRef, OrderModel order, MedOrderModel medOrder) {
        try {
            if (medOrder.hasVolume()) {
                return medOrder.getInfuseVolume() * frequencyRef.getAdminCount();
            } else {
                return medOrder.getOrderDose() / medOrder.getItemStrength() * frequencyRef.getAdminCount();
            }
        } catch (Exception ex) {
            Log.exception(ex);
            return 0;
        }
    }

    /**
	 * 
	 */
    public void calculateDispense(FrequencyModel frequency, OrderModel order, MedOrderModel medOrder) {
        try {
            double days = StandardUnits.convertValue(order.getDuration(), order.getDurationUnitRefId(), ValueUnitReference.DAY.getRefId());
            medOrder.setDispenseQuantity(Converter.convertLong(Math.round(days * getDailyDoseQuantity(frequency, order, medOrder))));
        } catch (Exception ex) {
            Log.exception(ex);
        }
    }

    /**
	 * 
	 * @param propName
	 */
    public void setVolumeForStrength(MedOrderModel medOrder) {
        if (medOrder.hasVolume()) {
            double volume = medOrder.getOrderDose() * medOrder.getItemStrengthVolume() / medOrder.getItemStrength();
            if (medOrder.getInfuseVolume() != volume) {
                medOrder.setInfuseVolume(volume);
                medOrder.setInfuseVolumeUnitRef(medOrder.getItemStrengthVolumeUnitRef());
                medOrder.setOrderDose(medOrder.getItemStrength() * medOrder.getInfuseVolume() / medOrder.getItemStrengthVolume());
            }
        }
    }

    /**
	 * 
	 * @param propName
	 */
    public void setStrengthForVolume(MedOrderModel medOrder) {
        if (medOrder.hasVolume()) {
            medOrder.setOrderDose(medOrder.getItemStrength() * medOrder.getInfuseVolume() / medOrder.getItemStrengthVolume());
        }
    }
}
