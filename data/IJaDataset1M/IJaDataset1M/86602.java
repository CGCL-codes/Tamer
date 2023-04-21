package payroll;

public class HoldMethod implements PaymentMethod {

    public void Pay(Paycheck pc) {
        pc.SetField("Disposition", "Hold");
    }
}
