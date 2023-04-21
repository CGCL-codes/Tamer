package it.dangelo.saj.validation.json_vl.date;

public class GDay implements GType {

    private int day;

    public GDay(int day) {
        super();
        if (day <= 0 || day > 31) throw new IllegalArgumentException("Invalid day " + day);
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GDay)) return false;
        GDay gDay = (GDay) obj;
        return gDay.day == this.day;
    }

    @Override
    public int hashCode() {
        final int iConstant = 37;
        int iTotal = 17;
        iTotal = iTotal * iConstant + this.day;
        return iTotal;
    }

    @Override
    public int compareTo(Object other) {
        if (other instanceof GDay) {
            GDay o = (GDay) other;
            return (this.day == o.day ? 0 : (this.day > o.day ? 1 : -1));
        }
        return -1;
    }

    @Override
    public String toString() {
        return Integer.toString(this.day);
    }
}
