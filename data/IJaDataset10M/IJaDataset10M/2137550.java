package pack;

public class IExhibitSrv_DeactivateDemand_RequestStruct {

    protected int demandID;

    public IExhibitSrv_DeactivateDemand_RequestStruct() {
    }

    public IExhibitSrv_DeactivateDemand_RequestStruct(int demandID) {
        this.demandID = demandID;
    }

    public int getDemandID() {
        return demandID;
    }

    public void setDemandID(int demandID) {
        this.demandID = demandID;
    }
}
