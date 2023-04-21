package ca.gc.drdc_rddc.atlantic.rti1516.cmd;

import hla.rti1516.FederateAmbassador;
import ca.gc.drdc_rddc.atlantic.hla.HLAModel;

public interface QueuedCommand {

    public void doMe(FederateAmbassador fedAmb) throws Exception;

    public void resolveHandles(HLAModel model, HandleNameMapper rti) throws Exception;

    public void resolveNames(HLAModel model, HandleNameMapper rti) throws Exception;

    public void playback(HLAModel model) throws Exception;

    public long getRelativeTime();

    public void setRelativeTime(long rt);
}
