package gov.cca.eg.component;

/** A CCA component that implements and provides  StringConsumerPort, and uses a
    StringConsumerPort. */
public class TimeStamper implements gov.cca.Component, gov.cca.eg.port.StringConsumerPort {

    gov.cca.Services svc;

    gov.cca.eg.port.StringConsumerPort out;

    gov.cca.PortInfo outInfo;

    gov.cca.eg.port.TimeProducerPort time;

    gov.cca.PortInfo timeInfo;

    /** CCA Component must have a null constructor. */
    public TimeStamper() {
    }

    /** Services implementation: this is the one and only method that
      makes this component a CCAComponent. */
    public void setServices(gov.cca.Services svc) {
        try {
            gov.cca.eg.port.StringConsumerPort stringer = (gov.cca.eg.port.StringConsumerPort) this;
            svc.addProvidesPort(stringer, svc.createPortInfo("in0", "StringConsumerPort", null));
            svc.registerUsesPort(outInfo = svc.createPortInfo("out0", "StringConsumerPort", null));
            svc.registerUsesPort(timeInfo = svc.createPortInfo("time0", "TimeProducerPort", null));
        } catch (Exception e) {
            System.out.println("Printer set failure:" + e);
        }
    }

    /** Implements the StringConsumerPort: takes a given string Stamps
      it with the time and shoves it out the UsesPort "out0". */
    public void setString(String s) {
        try {
            out = (gov.cca.eg.port.StringConsumerPort) svc.getPort("out0");
            time = (gov.cca.eg.port.TimeProducerPort) svc.getPort("time0");
            out.setString(s + "@" + time.getTime());
        } catch (Exception e) {
            System.out.println("stamper bummer:" + e);
        }
        svc.releasePort("out0");
        svc.releasePort("time0");
    }
}
