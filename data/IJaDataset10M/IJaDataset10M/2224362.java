package pack;

public class IExhibitSrv_DelDemand_RequestStruct {

    protected int demandID;

    public IExhibitSrv_DelDemand_RequestStruct() {
    }

    public IExhibitSrv_DelDemand_RequestStruct(int demandID) {
        this.demandID = demandID;
    }

    public int getDemandID() {
        return demandID;
    }

    public void setDemandID(int demandID) {
        this.demandID = demandID;
    }
}
