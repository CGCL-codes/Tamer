package drcl.inet.mac;

import drcl.comp.Port;
import drcl.comp.Contract;

/**
 * This class implements the free space radio propagation model.
 * @author Ye Ge
 */
public class FreeSpaceModel extends drcl.inet.mac.RadioPropagationModel {

    public FreeSpaceModel() {
        super();
    }

    /**
     * Processes the path loss query message which is sent from the WirelessPhy component.
     * The calculation result is sent back through the queryPort.
     */
    protected synchronized void processPathLossQuery(Object data_) {
        double loss;
        double ht, hr, Lambda, d;
        RadioPropagationQueryContract.Message msg = (RadioPropagationQueryContract.Message) data_;
        Lambda = msg.getLambda();
        if (isCartesian) d = msg.getDxyz(); else d = msg.getDxyz2();
        loss = calculatePathLoss(Lambda, d);
        queryPort.doSending(new RadioPropagationQueryContract.Message(loss));
    }

    /**
     * Calculate the path loss.
     *
     *@param Lamda_ the wavelength. 
     *@param d_     the distance between the two points. 
     */
    protected double calculatePathLoss(double Lambda_, double d_) {
        return FreeSpace(Lambda_, d_);
    }

    /**
     * Calculates the path loss using free space model.
     *
     *@param Lamda_ the wavelength. 
     *@param d_     the distance between the two points. 
     *
     */
    protected double FreeSpace(double Lambda_, double d_) {
        double loss;
        loss = Friis(Lambda_, d_);
        return loss;
    }

    protected double Friis(double Lambda_, double d_) {
        double M = Lambda_ / (4 * Math.PI * d_);
        return M * M;
    }

    public String info() {
        return "FreeSpaceModel" + "\n";
    }
}
