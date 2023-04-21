package openbrouw.beerxml_struct.recipe;

import pck_tap.alg.Util;

public class Equipment {

    private String vResultString = "";

    private String Name;

    private Double Version;

    private Double Boil_Size;

    private Double Batch_Size;

    private Double Tun_Volume;

    private Double Tun_Weight;

    private Double Tun_Specific_Heat;

    private Double Top_Up_Water;

    private Double Trub_Chiller_Loss;

    private Double Evap_Rate;

    private Double Boil_Time;

    private Boolean Calc_Boil_Volume;

    private Double Lauter_Deadspace;

    private Double Top_Up_Kettle;

    private Double Hop_Utilization;

    private String Notes;

    public Equipment() {
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setVersion(Double Version) {
        this.Version = Version;
    }

    public Double getVersion() {
        return Version;
    }

    public void setBoil_Size(Double Boil_Size) {
        this.Boil_Size = Boil_Size;
    }

    public Double getBoil_Size() {
        return Boil_Size;
    }

    public void setBatch_Size(Double Batch_Size) {
        this.Batch_Size = Batch_Size;
    }

    public Double getBatch_Size() {
        return Batch_Size;
    }

    public void setTun_Volume(Double Tun_Volume) {
        this.Tun_Volume = Tun_Volume;
    }

    public Double getTun_Volume() {
        return Tun_Volume;
    }

    public void setTun_Weight(Double Tun_Weight) {
        this.Tun_Weight = Tun_Weight;
    }

    public Double getTun_Weight() {
        return Tun_Weight;
    }

    public void setTun_Specific_Heat(Double Tun_Specific_Heat) {
        this.Tun_Specific_Heat = Tun_Specific_Heat;
    }

    public Double getTun_Specific_Heat() {
        return Tun_Specific_Heat;
    }

    public void setTop_Up_Water(Double Top_Up_Water) {
        this.Top_Up_Water = Top_Up_Water;
    }

    public Double getTop_Up_Water() {
        return Top_Up_Water;
    }

    public void setTrub_Chiller_Loss(Double Trub_Chiller_Loss) {
        this.Trub_Chiller_Loss = Trub_Chiller_Loss;
    }

    public Double getTrub_Chiller_Loss() {
        return Trub_Chiller_Loss;
    }

    public void setEvap_Rate(Double Evap_Rate) {
        this.Evap_Rate = Evap_Rate;
    }

    public Double getEvap_Rate() {
        return Evap_Rate;
    }

    public void setBoil_Time(Double Boil_Time) {
        this.Boil_Time = Boil_Time;
    }

    public Double getBoil_Time() {
        return Boil_Time;
    }

    public void setCalc_Boil_Volume(Boolean Calc_Boil_Volume) {
        this.Calc_Boil_Volume = Calc_Boil_Volume;
    }

    public Boolean getCalc_Boil_Volume() {
        return Calc_Boil_Volume;
    }

    public void setLauter_Deadspace(Double Lauter_Deadspace) {
        this.Lauter_Deadspace = Lauter_Deadspace;
    }

    public Double getLauter_Deadspace() {
        return Lauter_Deadspace;
    }

    public void setTop_Up_Kettle(Double Top_Up_Kettle) {
        this.Top_Up_Kettle = Top_Up_Kettle;
    }

    public Double getTop_Up_Kettle() {
        return Top_Up_Kettle;
    }

    public void setHop_Utilization(Double Hop_Utilization) {
        this.Hop_Utilization = Hop_Utilization;
    }

    public Double getHop_Utilization() {
        return Hop_Utilization;
    }

    public void setNotes(String Notes) {
        this.Notes = Notes;
    }

    public String getNotes() {
        return Notes;
    }

    public String getInfo() {
        vResultString = "";
        pl("");
        pl("Name              :" + Util.nvl(this.Name, ""));
        pl("Version           :" + Util.nvl(this.Version, Util.Double0()));
        pl("Boil_Size         :" + Util.nvl(this.Boil_Size, Util.Double0()));
        pl("Batch_Size        :" + Util.nvl(this.Batch_Size, Util.Double0()));
        pl("Tun_Volume        :" + Util.nvl(this.Tun_Volume, Util.Double0()));
        pl("Tun_Weight        :" + Util.nvl(this.Tun_Weight, Util.Double0()));
        pl("Tun_Specific_Heat :" + Util.nvl(this.Tun_Specific_Heat, Util.Double0()));
        pl("Top_Up_Water      :" + Util.nvl(this.Top_Up_Water, Util.Double0()));
        pl("Trub_Chiller_Loss :" + Util.nvl(this.Trub_Chiller_Loss, Util.Double0()));
        pl("Evap_Rate         :" + Util.nvl(this.Evap_Rate, Util.Double0()));
        pl("Boil_Time         :" + Util.nvl(this.Boil_Time, Util.Double0()));
        pl("Calc_Boil_Volume  :" + Util.nvl(this.Calc_Boil_Volume, false));
        pl("Lauter_Deadspace  :" + Util.nvl(this.Lauter_Deadspace, Util.Double0()));
        pl("Top_Up_Kettle     :" + Util.nvl(this.Top_Up_Kettle, Util.Double0()));
        pl("Hop_Utilization   :" + Util.nvl(this.Hop_Utilization, Util.Double0()));
        pl("Notes             :" + Util.nvl(this.Notes, ""));
        return vResultString;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }

    private void pl(String s) {
        vResultString = vResultString + s + "\n";
    }
}
