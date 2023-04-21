package Transaction;

import Payroll.Employee;

public class ChangeAddressTransaction extends ChangeEmployeeTransaction {

    private String itsAddress;

    public ChangeAddressTransaction(int empid, String address) {
        super(empid);
        itsAddress = address;
    }

    public void Change(Employee e) {
        e.SetAddress(itsAddress);
    }
}
