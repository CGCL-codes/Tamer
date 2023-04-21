package ru.rsgpu.mining.libmining;

class OrganizationCalculator {

    private static double normCompletingCoefficient;

    private static double drillingTime;

    private static double airTime;

    private static double deliveryTime;

    private static double blastingTime;

    private static double cleaningTime;

    private static double fixatingTime;

    private static double totalTime;

    private static double anchorDrillingTime;

    public static void calculate() {
        double holeLenght = HoleLenghtCalculator.getBoreholeLength();
        int numOfDrills = BlastingCalculator.getNumOfHoles();
        int numOfEmptyHoles = BlastingInputKeeper.getNumOfEmptyHoles();
        double drillingAmount = holeLenght * (numOfDrills + numOfEmptyHoles);
        double deliveryAmount = BlastingCalculator.getExplosiveUsage();
        double blastingAmount = holeLenght * numOfDrills;
        double outerArea;
        if (FixTypeChooser.getExcavationShape() == ExcavationShape.TRAPEZIFORM) {
            TrapeziformShape shape = (TrapeziformShape) ShapeSize.getShape();
            outerArea = shape.getOuterArea();
        } else {
            ArchedRectangleShape shape = (ArchedRectangleShape) ShapeSize.getShape();
            outerArea = shape.getOuterArea();
        }
        double holeUsageCoefficient = BlastingInputKeeper.getHoleUsageCoefficient();
        double cleaningAmount = outerArea * holeLenght * holeUsageCoefficient;
        double fixRate = FixCalculator.getFixRate();
        double fixatingAmount = fixRate * holeLenght * holeUsageCoefficient;
        NormPicker np = new NormPicker(InputKeeper.getDbPath());
        double anchorDrillingAmount;
        double anchorDrillingNorm;
        if (FixTypeChooser.getFix() == FixType.ANCHOR) {
            anchorDrillingAmount = FixCalculator.getFixingLength() * fixRate * holeLenght * holeUsageCoefficient;
            anchorDrillingNorm = np.getDrillingNorm(DrillingMachine.PT_48) / 10.0;
        } else {
            anchorDrillingAmount = 0;
            anchorDrillingNorm = 0;
        }
        double drillingSpending = drillingAmount * np.getDrillingNorm() / 10.0;
        double deliverySpending = deliveryAmount * np.getDeliveryNorm() / 100.0;
        double blastingSpending = blastingAmount * np.getExplosionNorm() / 10.0;
        double cleaningSpending = cleaningAmount * np.getCleaningNorm() * 2.0;
        double fixatingSpending = fixatingAmount * np.getFixatingNorm();
        double anchorDrillingSpending = anchorDrillingAmount * anchorDrillingNorm;
        int numOfCyclesPerChange = HoleLenghtCalculator.getNumOfCyclesPerChange();
        double cycleTime = 6.0 / numOfCyclesPerChange;
        int numOfAirings = HoleLenghtCalculator.getNumOfAirings();
        airTime = 0.5;
        normCompletingCoefficient = (drillingSpending + deliverySpending + blastingSpending + cleaningSpending + fixatingSpending + anchorDrillingSpending) / (2.0 * cycleTime);
        if (numOfAirings > 1) {
            numOfAirings = 1;
        }
        double alpha = (cycleTime - airTime * numOfAirings) / cycleTime;
        double tail = alpha / (2.0 * normCompletingCoefficient);
        drillingTime = drillingSpending * tail;
        deliveryTime = deliverySpending * tail;
        blastingTime = blastingSpending * tail;
        cleaningTime = cleaningSpending * tail;
        anchorDrillingTime = anchorDrillingSpending * tail;
        fixatingTime = fixatingSpending * tail + anchorDrillingTime;
        totalTime = drillingTime + deliveryTime + blastingTime + airTime + cleaningTime + fixatingTime;
    }

    public static double getNormCompletingCoefficient() {
        return normCompletingCoefficient;
    }

    public static double getDrillingTime() {
        return drillingTime;
    }

    public static double getAirTime() {
        return airTime;
    }

    public static double getDeliveryTime() {
        return deliveryTime;
    }

    public static double getBlastingTime() {
        return blastingTime;
    }

    public static double getCleaningTime() {
        return cleaningTime;
    }

    public static double getFixatingTime() {
        return fixatingTime;
    }

    public static double getTotalTime() {
        return totalTime;
    }
}
