package websiteschema.fb.common.merge;

import websiteschema.fb.annotation.*;
import websiteschema.fb.core.FunctionBlock;

/**
 *
 * @author st
 */
@EI(name = { "E1:M", "E2:M", "E3:M", "E4:M" })
@EO(name = { "EO" })
public class Event4Data4Merger extends FunctionBlock {

    @DI(name = "D1")
    public String d1;

    @DI(name = "D2")
    public String d2;

    @DI(name = "D3")
    public String d3;

    @DI(name = "D4")
    public String d4;

    @DO(name = "DO", relativeEvents = { "EO" })
    public String dataout;

    @Algorithm(name = "M")
    public void DataMerger4(@EVT String ei) {
        if (ei.equals("E1")) {
            dataout = d1;
        } else if ((ei.equals("E2"))) {
            dataout = d2;
        } else if (ei.equals("E3")) {
            dataout = d2;
        } else {
            dataout = d4;
        }
        this.triggerEvent("EO");
    }
}
