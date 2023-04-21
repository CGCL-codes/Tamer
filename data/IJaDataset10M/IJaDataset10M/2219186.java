package com.roy.java;

public class NutritionFacts {

    private Builder bldr;

    public static class Builder {

        private String name;

        private int servingSize;

        private int servingsPerContainer;

        private int totalfat;

        private int saturatedfat;

        private int transfat;

        private int cholesterol;

        private int sodium;

        public Builder(String name, int servingSize, int servingsPerContainer) {
            this.name = name;
            this.servingSize = servingSize;
            this.servingsPerContainer = servingsPerContainer;
        }

        public Builder totalFat(int val) {
            totalfat = val;
            return this;
        }

        public Builder saturatedFat(int val) {
            saturatedfat = val;
            return this;
        }

        public Builder transFat(int val) {
            transfat = val;
            return this;
        }

        public Builder cholesterol(int val) {
            cholesterol = val;
            return this;
        }

        public Builder sodium(int val) {
            sodium = val;
            return this;
        }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder bldr) {
        this.bldr = bldr;
    }

    public int getCholesterol() {
        return bldr.cholesterol;
    }

    public String getName() {
        return bldr.name;
    }

    public int getSaturatedfat() {
        return bldr.saturatedfat;
    }

    public int getServingSize() {
        return bldr.servingSize;
    }

    public int getServingsPerContainer() {
        return bldr.servingsPerContainer;
    }

    public int getSodium() {
        return bldr.sodium;
    }

    public int getTotalfat() {
        return bldr.totalfat;
    }

    public int getTransfat() {
        return bldr.transfat;
    }

    @Override
    public String toString() {
        return bldr.name + ":" + bldr.servingSize + ":" + bldr.servingsPerContainer + ":" + bldr.totalfat + ":" + bldr.saturatedfat + ":" + bldr.transfat + ":" + bldr.cholesterol + ":" + bldr.sodium;
    }
}
