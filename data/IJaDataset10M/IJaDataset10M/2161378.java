package com.newisys.verilog;

/**
 * Interface used to receive callbacks.
 * 
 * @author Trevor Robinson
 */
public interface VerilogCallbackHandler {

    void run(VerilogCallback cb, VerilogCallbackData data);
}
