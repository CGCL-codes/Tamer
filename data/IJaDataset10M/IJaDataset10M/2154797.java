package playground.wrashid.PSF.energy.charging.optimizedCharging;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.core.gbl.MatsimRandom;
import playground.wrashid.PSF.ParametersPSF;
import playground.wrashid.PSF.energy.charging.ChargeLog;
import playground.wrashid.PSF.parking.ParkingInfo;

public class FacilityChargingPrice implements Comparable<FacilityChargingPrice> {

    private static final Logger log = Logger.getLogger(FacilityChargingPrice.class);

    private double price;

    private int timeSlotNumber;

    private int energyBalanceParkingIndex;

    private double slotStartTime;

    private Id facilityId;

    private double endParkingTime;

    private double randomNumber;

    public double getRandomNumber() {
        return randomNumber;
    }

    public double getEndParkingTime() {
        return endParkingTime;
    }

    private double startParkingTime;

    private double endTimeOfSlot;

    private Id linkId;

    public int getEnergyBalanceParkingIndex() {
        return energyBalanceParkingIndex;
    }

    public FacilityChargingPrice(double price, int timeSlotNumber, int energyBalanceParkingIndex, double slotStartTime, Id facilityId, double startParkingTime, double endParkingTime, Id linkId) {
        super();
        this.price = price;
        this.timeSlotNumber = timeSlotNumber;
        this.energyBalanceParkingIndex = energyBalanceParkingIndex;
        this.slotStartTime = slotStartTime;
        this.facilityId = facilityId;
        this.endParkingTime = endParkingTime;
        this.startParkingTime = startParkingTime;
        this.linkId = linkId;
        endTimeOfSlot = slotStartTime + 900;
        if (startParkingTime > endParkingTime) {
        }
        if (startParkingTime > 86400) {
            System.out.println();
        }
        this.randomNumber = MatsimRandom.getRandom().nextDouble();
        if (ParametersPSF.getMainChargingPriceBlurFactor() > 0) {
            this.price = this.price * (1 + (MatsimRandom.getRandom().nextDouble() - 1) * ParametersPSF.getMainChargingPriceBlurFactor());
        } else if (ParametersPSF.useLinearProbabilisticScalingOfPrice()) {
            if (true) {
                this.price = this.price * this.randomNumber;
            } else {
                double basePrice = 7.99;
                this.price -= basePrice;
                this.price = this.price * this.randomNumber;
                this.price += basePrice;
            }
        } else if (ParametersPSF.useSquareRootProbabilisticScalingOfPrice()) {
            if (true) {
                this.price = this.price * Math.sqrt(this.randomNumber);
            } else {
                double basePrice = 7.99;
                this.price -= basePrice;
                this.price = this.price * Math.sqrt(this.randomNumber);
                this.price += basePrice;
            }
        } else if (ParametersPSF.useQuadraticProbabilisticScalingOfPrice()) {
            this.price = this.price * (this.randomNumber * this.randomNumber);
        }
    }

    public int compareTo(FacilityChargingPrice otherChargingPrice) {
        if (getInitStartChargingTime() == otherChargingPrice.getInitStartChargingTime()) {
            log.error("two slots with the same starting time => some thing is wrong...");
            System.exit(-1);
        }
        if (price > otherChargingPrice.getPrice()) {
            return 1;
        } else if (price < otherChargingPrice.getPrice()) {
            return -1;
        } else {
            if (ParametersPSF.getChargingMode().equalsIgnoreCase(ParametersPSF.MODERATE_CHARGING)) {
                return (randomNumber - otherChargingPrice.getRandomNumber()) > 0 ? 1 : -1;
            } else {
                return timeSlotNumber - otherChargingPrice.getTimeSlotNumber();
            }
        }
    }

    public double getPrice() {
        return price;
    }

    public int getTimeSlotNumber() {
        return timeSlotNumber;
    }

    public double getInitStartChargingTime() {
        double startChargingTime = slotStartTime < startParkingTime ? startParkingTime : slotStartTime;
        startChargingTime = endTimeOfSlot < startParkingTime ? slotStartTime : startChargingTime;
        return startChargingTime;
    }

    public ChargeLog getChargeLog(double minimumEnergyThatNeedsToBeCharged, double maxChargableEnergy) {
        double startChargingTime = getInitStartChargingTime();
        double endChargingTime = getEndTimeOfCharge(minimumEnergyThatNeedsToBeCharged, maxChargableEnergy);
        if (startChargingTime > endChargingTime) {
        }
        return new ChargeLog(linkId, facilityId, startChargingTime, endChargingTime);
    }

    public double getEnergyCharge(double minimumEnergyThatNeedsToBeCharged, double maxChargableEnergy) {
        double startChargingTime = getInitStartChargingTime();
        double electricityPower = ParkingInfo.getParkingElectricityPower(facilityId);
        double chargingDuration = getEndTimeOfCharge(minimumEnergyThatNeedsToBeCharged, maxChargableEnergy) - startChargingTime;
        double energyCharged = electricityPower * chargingDuration;
        if (energyCharged < 0) {
            log.error("the energy is negative!");
            System.exit(-1);
        }
        return energyCharged;
    }

    public double getEndTimeOfCharge(double minimumEnergyThatNeedsToBeCharged, double maxChargableEnergy) {
        double startChargingTime = getInitStartChargingTime();
        double durationNeededForRequiredCharging = Math.min(minimumEnergyThatNeedsToBeCharged, maxChargableEnergy) / ParkingInfo.getParkingElectricityPower(facilityId);
        double endTimeOfCharge = endTimeOfSlot;
        if (slotStartTime > endParkingTime) {
            if (endTimeOfCharge > startChargingTime + durationNeededForRequiredCharging) {
                endTimeOfCharge = startChargingTime + durationNeededForRequiredCharging;
            }
        } else if (slotStartTime < endParkingTime && endTimeOfSlot > startParkingTime) {
            if (endTimeOfCharge > startChargingTime + durationNeededForRequiredCharging) {
                endTimeOfCharge = startChargingTime + durationNeededForRequiredCharging;
            }
        } else {
            if (endParkingTime < endTimeOfCharge && !(endParkingTime < startParkingTime)) {
                endTimeOfCharge = endParkingTime;
            }
            if (endTimeOfCharge > startChargingTime + durationNeededForRequiredCharging) {
                endTimeOfCharge = startChargingTime + durationNeededForRequiredCharging;
            }
        }
        if (endTimeOfCharge - startChargingTime < 0) {
            log.error("startChargingTime needs to be bigger than end charging time!");
            System.exit(-1);
        }
        return endTimeOfCharge;
    }

    public double getSlotStartTime() {
        return slotStartTime;
    }

    public double getEndTimeOfSlot() {
        return endTimeOfSlot;
    }

    public void setEndTimeOfSlot(double endTimeOfSlot) {
        this.endTimeOfSlot = endTimeOfSlot;
    }

    public void setSlotStartTime(double slotStartTime) {
        this.slotStartTime = slotStartTime;
    }
}
