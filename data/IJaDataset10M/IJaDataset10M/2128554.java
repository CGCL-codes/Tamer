package de.bugbusters.example;

import de.bugbusters.binpacking.model.Size;

/**
 * @author Sven Kiesewetter
 */
public class VolumeAndWeight implements Size {

    private double volume;

    private double weight;

    public VolumeAndWeight(double volume, double weight) {
        this.volume = volume;
        this.weight = weight;
    }

    public double getVolume() {
        return volume;
    }

    public double getWeight() {
        return weight;
    }

    public Size createZeroInstance() {
        return new VolumeAndWeight(0.0, 0.0);
    }

    public Size add(Size other) {
        VolumeAndWeight otherItemSize = (VolumeAndWeight) other;
        return new VolumeAndWeight(this.getVolume() + otherItemSize.getVolume(), this.getWeight() + otherItemSize.getWeight());
    }

    public Size subtract(Size other) {
        VolumeAndWeight otherItemSize = (VolumeAndWeight) other;
        return new VolumeAndWeight(this.getVolume() - otherItemSize.getVolume(), this.getWeight() - otherItemSize.getWeight());
    }

    public double calculatePercentage(Size other) {
        VolumeAndWeight otherItemSize = (VolumeAndWeight) other;
        double volumePercentage = otherItemSize.getVolume() / this.getVolume();
        double weightPercentage = otherItemSize.getWeight() / this.getWeight();
        return (volumePercentage + weightPercentage) / 2;
    }

    public boolean isSmallerThan(Size other) {
        VolumeAndWeight otherItemSize = (VolumeAndWeight) other;
        return getVolume() < otherItemSize.getVolume() && getWeight() < otherItemSize.getWeight();
    }

    public boolean isLargerThan(Size other) {
        VolumeAndWeight otherItemSize = (VolumeAndWeight) other;
        return getVolume() > otherItemSize.getVolume() && getWeight() > otherItemSize.getWeight();
    }

    public int compareTo(Object other) {
        VolumeAndWeight otherItemSize = (VolumeAndWeight) other;
        return (int) ((getVolume() + getWeight()) - (otherItemSize.getVolume() + otherItemSize.getWeight()));
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VolumeAndWeight that = (VolumeAndWeight) o;
        if (Double.compare(that.volume, volume) != 0) return false;
        return Double.compare(that.weight, weight) == 0;
    }

    public int hashCode() {
        int result;
        long temp;
        temp = volume != +0.0d ? Double.doubleToLongBits(volume) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = weight != +0.0d ? Double.doubleToLongBits(weight) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String toString() {
        return "{" + "volume=" + volume + ", weight=" + weight + '}';
    }
}
